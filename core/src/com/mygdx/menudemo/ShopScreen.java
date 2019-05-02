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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

class ShopScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static  final int COINS_DEFAULT = 0;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;
    private Camera camera;
    private Texture backgroundTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Table table;
    private Texture codeTexture;
    private Texture codePressTexture;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    private SpriteBatch batch;

    private Music music;
    private Preferences preferencias;
    private float musicVolume;
    private Texture coinsIndicatorTexture;
    private int coinsCollected = 0;

    public ShopScreen(MenuDemo menuDemo) {
        this.menuDemo =menuDemo;
        this.preferencias = menuDemo.getPreferences();
    }

    public void show() {
        loadPreferences();
        super.show();
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("shop/song.mp3"));
        music.setLooping(true);
        music.play();

        bitmapFont = new BitmapFont(Gdx.files.internal("defaultLevels/numbers.fnt"));
        glyphLayout = new GlyphLayout();
        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("shop/fondo.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        coinsIndicatorTexture = new Texture(Gdx.files.internal("defaultLevels/Coins.png"));
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(3*WORLD_WIDTH/5+50, WORLD_HEIGHT-coins.getHeight()-10);

        returnTexture = new Texture(Gdx.files.internal("shop/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("shop/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new StartScreen(menuDemo));
                savePreferences();
                dispose();
            }
        });

        codeTexture = new Texture(Gdx.files.internal("shop/code.png"));
        codePressTexture = new Texture(Gdx.files.internal("shop/codePress.png"));
        ImageButton code = new ImageButton(new TextureRegionDrawable(new TextureRegion(codeTexture)), new TextureRegionDrawable(new TextureRegion(codePressTexture)));
        code.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new StartScreen(menuDemo));
                dispose();
            }
        });

        table = new Table();
        table.pad(20);
        //table.setDebug(true);

        //table.add(title).padBottom(100).expand().top().colspan(3);

        table.row();
        table.add(code).bottom().expand();

        table.row();
        table.add(retur).bottom().left();

        table.setFillParent(true);
        table.pack();
        stage.addActor(table);
        stage.addActor(coins);
    }

    private void savePreferences() {
        preferencias.putInteger("coins", coinsCollected);
        preferencias.flush();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        coinsCollected = preferencias.getInteger("coins", COINS_DEFAULT);
        Gdx.app.log("LOG:","Shop Coins: " +  coinsCollected);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        updateVolume();
        stage.act();
        draw();
    }

    private void draw() {
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        drawCoinsCounter();
        batch.end();
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    private void drawCoinsCounter() {
        String coinsAsString = Integer.toString(coinsCollected);
        glyphLayout.setText(bitmapFont, coinsAsString);
        bitmapFont.draw(batch, coinsAsString, 3*WORLD_WIDTH/5+145, WORLD_HEIGHT-coinsIndicatorTexture.getHeight()+50);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose();
        returnTexture.dispose();
        returnPressTexture.dispose();
        codeTexture.dispose();
        codePressTexture.dispose();
        music.dispose();
        //titleTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}