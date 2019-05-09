package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
    private Texture partySkinPressedButtonTexture;
    private Texture hatSkinButtonPressedTexture;
    private Texture tieSkinButtonPressedTexture;
    private Texture crownSkinButtonPressedTexture;
    private Texture hulkSkinButtonPressedTexture;
    private Texture ricardoSkinButtonPressedTexture;
    private Stage stageSkinButtons;
    private InputMultiplexer multiplexer = new InputMultiplexer();
    private Stage quitStage;
    private Texture quitPanelTexture;
    private Texture yesQuitTexture;
    private Texture yesQuitPressTexture;
    private Texture noQuitTexture;
    private Texture noQuitPressTexture;

    private enum STATE {
        NORMAL, QUIT
    }

    private STATE state = STATE.NORMAL;

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
                updateSkinButton();
            }
        });
        stage.addActor(defaultSkin);

        stageSkinButtons = new Stage(viewport);

        updateSkinButton();


        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(stageSkinButtons);
        Gdx.input.setInputProcessor(multiplexer);

        createQuitPanel();
    }

    private void updateSkinButton() {
        stageSkinButtons.clear();
        if(partyBought){
            if(actualSkin.compareTo("party") == 0) {
                partySkinButtonTexture = new Texture(Gdx.files.internal("closet/partySelected.png"));
                partySkinPressedButtonTexture = new Texture(Gdx.files.internal("closet/partySelected.png"));
                ImageButton partySkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinPressedButtonTexture)));
                partySkin.setPosition(115, WORLD_HEIGHT / 2 + 25);
                stageSkinButtons.addActor(partySkin);
            }else {
                partySkinButtonTexture = new Texture(Gdx.files.internal("closet/partyButton.png"));
                partySkinPressedButtonTexture = new Texture(Gdx.files.internal("closet/partyButtonPress.png"));
                ImageButton partySkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinPressedButtonTexture)));
                partySkin.setPosition(115, WORLD_HEIGHT / 2 + 25);
                partySkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "party";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(partySkin);
            }
        }

        if(hatBought){
            if(actualSkin.compareTo("hat") == 0){
                hatSkinButtonTexture = new Texture(Gdx.files.internal("closet/hatButtonSelected.png"));
                hatSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/hatButtonSelected.png"));
                ImageButton hatSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonPressedTexture)));
                hatSkin.setPosition(275, WORLD_HEIGHT/2+25);
                stageSkinButtons.addActor(hatSkin);
            }else {
                hatSkinButtonTexture = new Texture(Gdx.files.internal("closet/hatButton.png"));
                hatSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/hatButtonPress.png"));
                ImageButton hatSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonPressedTexture)));
                hatSkin.setPosition(275, WORLD_HEIGHT / 2 + 25);
                hatSkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "hat";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(hatSkin);
            }
        }

        if(tieBought){
            if(actualSkin.compareTo("tie") == 0){
                tieSkinButtonTexture = new Texture(Gdx.files.internal("closet/tieSelected.png"));
                tieSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/tieSelected.png"));
                ImageButton tieSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(tieSkinButtonPressedTexture)));
                tieSkin.setPosition(435, WORLD_HEIGHT / 2 + 25);
                stageSkinButtons.addActor(tieSkin);
            }else {
                tieSkinButtonTexture = new Texture(Gdx.files.internal("closet/tieButton.png"));
                tieSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/tieButtonPress.png"));
                ImageButton tieSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(tieSkinButtonPressedTexture)));
                tieSkin.setPosition(435, WORLD_HEIGHT / 2 + 25);
                tieSkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "tie";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(tieSkin);
            }
        }

        if(crownBought){
            if(actualSkin.compareTo("crown") == 0){
                crownSkinButtonTexture = new Texture(Gdx.files.internal("closet/crownSelected.png"));
                crownSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/crownSelected.png"));
                ImageButton crownSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressedTexture)));
                crownSkin.setPosition(115, WORLD_HEIGHT / 2 - crownSkin.getHeight() + 15);
                stageSkinButtons.addActor(crownSkin);
            }else {
                crownSkinButtonTexture = new Texture(Gdx.files.internal("closet/crownButton.png"));
                crownSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/crownButtonPress.png"));
                ImageButton crownSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressedTexture)));
                crownSkin.setPosition(115, WORLD_HEIGHT / 2 - crownSkin.getHeight() + 15);
                crownSkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "crown";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(crownSkin);
            }
        }

        if(hulkBought){
            if(actualSkin.compareTo("hulk") == 0){
                hulkSkinButtonTexture = new Texture(Gdx.files.internal("closet/hulkSelected.png"));
                hulkSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/hulkSelected.png"));
                ImageButton hulkSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hulkSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hulkSkinButtonPressedTexture)));
                hulkSkin.setPosition(275, WORLD_HEIGHT / 2 - hulkSkin.getWidth() + 15);
                stageSkinButtons.addActor(hulkSkin);
            }else {
                hulkSkinButtonTexture = new Texture(Gdx.files.internal("closet/hulkButton.png"));
                hulkSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/hulkButtonPress.png"));
                ImageButton hulkSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(hulkSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hulkSkinButtonPressedTexture)));
                hulkSkin.setPosition(275, WORLD_HEIGHT / 2 - hulkSkin.getWidth() + 15);
                hulkSkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "hulk";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(hulkSkin);
            }
        }

        if (ricardoBought){
            if(actualSkin.compareTo("ricardo") == 0){
                ricardoSkinButtonTexture = new Texture(Gdx.files.internal("closet/ricardoSelected.png"));
                ricardoSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/ricardoSelected.png"));
                ImageButton ricardoSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonPressedTexture)));
                ricardoSkin.setPosition(435, WORLD_HEIGHT / 2 - ricardoSkin.getWidth() + 15);
                stageSkinButtons.addActor(ricardoSkin);
            }else {
                ricardoSkinButtonTexture = new Texture(Gdx.files.internal("closet/ricardoButton.png"));
                ricardoSkinButtonPressedTexture = new Texture(Gdx.files.internal("closet/ricardoButtonPress.png"));
                ImageButton ricardoSkin = new ImageButton(new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(ricardoSkinButtonPressedTexture)));
                ricardoSkin.setPosition(435, WORLD_HEIGHT / 2 - ricardoSkin.getWidth() + 15);
                ricardoSkin.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        actualSkin = "ricardo";
                        updateKiiwAnimation(actualSkin);
                        savePreferences();
                        updateSkinButton();
                    }
                });
                stageSkinButtons.addActor(ricardoSkin);
            }
        }

    }

    private void createQuitPanel() {
        quitStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        quitPanelTexture = new Texture(Gdx.files.internal("back/quitpanel.png"));
        Image quitPanel = new Image(quitPanelTexture);

        yesQuitTexture = new Texture(Gdx.files.internal("back/yes.png"));
        yesQuitPressTexture = new Texture(Gdx.files.internal("back/yesPress.png"));
        ImageButton yesQuit = new ImageButton(new TextureRegionDrawable(new TextureRegion(yesQuitTexture)), new TextureRegionDrawable(new TextureRegion(yesQuitPressTexture)));
        yesQuit.setPosition(WORLD_WIDTH/3+20, WORLD_HEIGHT/3-20);
        yesQuit.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Gdx.app.exit();
            }
        });

        noQuitTexture = new Texture(Gdx.files.internal("back/no.png"));
        noQuitPressTexture = new Texture(Gdx.files.internal("back/noPress.png"));
        ImageButton noQuit = new ImageButton(new TextureRegionDrawable(new TextureRegion(noQuitTexture)), new TextureRegionDrawable(new TextureRegion(noQuitPressTexture)));
        noQuit.setPosition(2*WORLD_WIDTH/3-170, WORLD_HEIGHT/3-20);
        noQuit.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        quitStage.addActor(quitPanel);
        quitStage.addActor(yesQuit);
        quitStage.addActor(noQuit);
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
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            state = STATE.QUIT;
        }
        if(state == STATE.QUIT){
            quitStage.draw();
            Gdx.input.setInputProcessor(quitStage);
        }else{
            Gdx.input.setInputProcessor(multiplexer);
        }

    }

    private void draw() {
        stage.draw();
        stageSkinButtons.draw();
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
