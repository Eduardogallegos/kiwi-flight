package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class ClosetScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static final boolean SKIN_BOUGHT = false;
    private static final String ACTUAL_SKIN = "default";
    private static final int TILE_WIDTH = 473;
    private static final int TILE_HEIGHT = 387;
    private static final float FRAME_DURATION = 0.1f;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture flecha;
    private Texture flechaPress;

    private Music music;
    private Preferences preferencias;
    private float musicVolume;
    private Texture partySkinButtonTexture;
    private boolean partyBought;
    private boolean hatBought;
    private boolean tieBought;
    private boolean crownBought;
    private boolean hulkBought;
    private boolean ricardoBought;
    private Texture hatSkinButtonTexture;
    private Texture tieSkinButtonTexture;
    private Texture crownSkinButtonTexture;
    private Texture hulkSkinButtonTexture;
    private Texture ricardoSkinButtonTexture;
    private String actualSkin;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Camera camera;
    private Texture kiiwTexture;
    private Animation animation;
    private TextureRegion kiiw;
    private float animationTimer = 0;
    private Texture defaultSkinButtonTexture;
    private Texture defaultSkinPressedButtonTexture;

    public ClosetScreen(MenuDemo menuDemo) {
        this.menuDemo = menuDemo;
        this.preferencias = menuDemo.getPreferences();
    }

    public void show() {
        loadPreferences();

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        camera.update();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("closet/song.mp3"));
        updateVolume();
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("closet/fondo closet.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        updateKiiwAnimation(actualSkin);

        flecha = new Texture(Gdx.files.internal("closet/closet flecha.png"));
        flechaPress = new Texture(Gdx.files.internal("closet/closet flecha pressed.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(flecha)), new TextureRegionDrawable(new TextureRegion(flechaPress)));
        retur.setPosition(15,15);
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                savePreferences();
                menuDemo.setScreen(new StartScreen(menuDemo));
                dispose();
            }
        });
        stage.addActor(retur);

        defaultSkinButtonTexture = new Texture(Gdx.files.internal("closet/default.png"));
        defaultSkinPressedButtonTexture = new Texture(Gdx.files.internal("closet/defaultPress.png"));
        ImageButton defaultSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(defaultSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(defaultSkinPressedButtonTexture)));
        defaultSkin.setPosition(WORLD_WIDTH/3, 30);
        defaultSkin.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                actualSkin = "default";
                updateKiiwAnimation(actualSkin);
            }
        });
        stage.addActor(defaultSkin);

        if(partyBought){
            partySkinButtonTexture = new Texture(Gdx.files.internal("closet/partyButton.png"));
            ImageButton partySkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)));
            partySkin.setPosition(115, WORLD_HEIGHT/2+25);
            partySkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "party";
                    updateKiiwAnimation(actualSkin);
                }
            });
            stage.addActor(partySkin);
        }

        if(hatBought){
            hatSkinButtonTexture = new Texture(Gdx.files.internal("closet/hatButton.png"));
            ImageButton hatSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)));
            hatSkin.setPosition(275, WORLD_HEIGHT/2+25);
            hatSkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "hat";
                    updateKiiwAnimation(actualSkin);
                    updateButton(actualSkin);
                }
            });
            stage.addActor(hatSkin);
        }

        if(tieBought){
            tieSkinButtonTexture = new Texture(Gdx.files.internal("closet/tieButton.png"));
            ImageButton tieSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)));
            tieSkin.setPosition(435, WORLD_HEIGHT/2+25);
            tieSkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "tie";
                    updateKiiwAnimation(actualSkin);
                }
            });
            stage.addActor(tieSkin);
        }

        if(crownBought){
            crownSkinButtonTexture = new Texture(Gdx.files.internal("closet/crownButton.png"));
            ImageButton crownSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)));
            crownSkin.setPosition(115, WORLD_HEIGHT/2-crownSkin.getHeight()+15);
            crownSkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "crown";
                    updateKiiwAnimation(actualSkin);
                }
            });
            stage.addActor(crownSkin);
        }

        if(hulkBought){
            hulkSkinButtonTexture = new Texture(Gdx.files.internal("closet/hulkButton.png"));
            ImageButton hulkSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hulkSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hulkSkinButtonTexture)));
            hulkSkin.setPosition(275, WORLD_HEIGHT/2-hulkSkin.getWidth()+15);
            hulkSkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "hulk";
                    updateKiiwAnimation(actualSkin);
                }
            });
            stage.addActor(hulkSkin);
        }

        if (ricardoBought){
            ricardoSkinButtonTexture = new Texture(Gdx.files.internal("closet/ricardoButton.png"));
            ImageButton ricardoSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonTexture)));
            ricardoSkin.setPosition(435, WORLD_HEIGHT/2-ricardoSkin.getWidth()+15);
            ricardoSkin.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    actualSkin = "ricardo";
                    updateKiiwAnimation(actualSkin);
                }
            });
            stage.addActor(ricardoSkin);
        }

    }

    private void updateButton(String actualSkin) {
        if(actualSkin.compareTo("party") == 0){
            partySkinButtonTexture = new Texture(Gdx.files.internal("closet/partyButton.png"));
            ImageButton partySkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)));
            partySkin.setPosition(115, WORLD_HEIGHT/2+25);
        }else if(actualSkin.compareTo("hat") == 0){
            hatSkinButtonTexture = new Texture(Gdx.files.internal("closet/hatButton.png"));
            ImageButton hatSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)));
            hatSkin.setPosition(275, WORLD_HEIGHT/2+25);
        }else if(actualSkin.compareTo("tie") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/TieKiwiStill.png"));
        }else if(actualSkin.compareTo("crown") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/CrownKiwiStill.png"));
        }else if(actualSkin.compareTo("hulk") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/HulkKiwiStill.png"));
        }else if(actualSkin.compareTo("ricardo") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/DancingKiwiStill.png"));
        }
    }

    private void updateKiiwAnimation(String actualSkin) {
        if(actualSkin.compareTo("default") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/DefaultKiwi.png"));
        }else if(actualSkin.compareTo("party") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/PartyHatKiwiStill.png"));
        }else if(actualSkin.compareTo("hat") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/TopHatKiwiStill.png"));
        }else if(actualSkin.compareTo("tie") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/TieKiwiStill.png"));
        }else if(actualSkin.compareTo("crown") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/CrownKiwiStill.png"));
        }else if(actualSkin.compareTo("hulk") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/HulkKiwiStill.png"));
        }else if(actualSkin.compareTo("ricardo") == 0){
            kiiwTexture = new Texture(Gdx.files.internal("closet/DancingKiwiStill.png"));
        }

        TextureRegion [][] kiwiTextures = new TextureRegion(kiiwTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, kiwiTextures[0][0], kiwiTextures[0][1],kiwiTextures[0][2],kiwiTextures[0][3],kiwiTextures[0][4],kiwiTextures[0][5],kiwiTextures[0][6],kiwiTextures[0][7]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void pause() {
        savePreferences();
    }

    private void savePreferences() {
        preferencias.putString("actualSkin", actualSkin);
        preferencias.flush();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        partyBought = preferencias.getBoolean("partyBought", SKIN_BOUGHT);
        hatBought = preferencias.getBoolean("hatBought", SKIN_BOUGHT);
        tieBought = preferencias.getBoolean("tieBought", SKIN_BOUGHT);
        crownBought = preferencias.getBoolean("crownBought", SKIN_BOUGHT);
        hulkBought = preferencias.getBoolean("hulkBought", SKIN_BOUGHT);
        ricardoBought = preferencias.getBoolean("ricardoBought", SKIN_BOUGHT);
        actualSkin = preferencias.getString("actualSkin", ACTUAL_SKIN);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        animationTimer+=delta;
        clearScreen();
        updateVolume();
        stage.act(delta);
        draw();

    }

    private void draw() {
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        kiiw = (TextureRegion) animation.getKeyFrame(animationTimer) ;
        batch.draw(kiiw, 750, 120);
        batch.end();

    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
        backgroundTexture.dispose();
        flecha.dispose();
        flechaPress.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


}
