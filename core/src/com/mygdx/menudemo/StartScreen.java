package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class StartScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture playTexture;
    private Texture playPressTexture;
    private Texture settingsTexture;
    private Texture settingsPressTexture;
    private Texture panelTexture;
    private Texture creditsPressTexture;
    private Texture creditsTexture;
    private Texture shopTexture;
    private Texture shopPressTexture;
    private Texture closetPressTexture;
    private Texture closetTexture;

    private Table table;
    private Texture kiwiTexture;
    private Music music;
    private Preferences preferencias;
    private float musicVolume;


    public StartScreen(MenuDemo menuDemo) {
        this.menuDemo =menuDemo;
        this.preferencias = menuDemo.getPreferences();
    }

    @Override
    public void show() {
        loadPreferences();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("principal/song.mp3"));
        updateVolume();
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("principal/MenuBackground.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        titleTexture = new Texture(Gdx.files.internal("principal/MenuLogo.png"));
        Image title = new Image(titleTexture);

        panelTexture= new Texture(Gdx.files.internal("principal/panel.png"));
        Image panel = new Image(panelTexture);

        kiwiTexture= new Texture(Gdx.files.internal("principal/kiwi.png"));
        Image kiwi = new Image(kiwiTexture);

        playTexture = new Texture(Gdx.files.internal("principal/play.png"));
        playPressTexture = new Texture(Gdx.files.internal("principal/playPress.png"));
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LevelsScreen(menuDemo));
                dispose();
            }
        });

        settingsTexture = new Texture(Gdx.files.internal("principal/settings.png"));
        settingsPressTexture = new Texture(Gdx.files.internal("principal/settingsPress.png"));
        ImageButton settings = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsTexture)), new TextureRegionDrawable(new TextureRegion(settingsPressTexture)));
        settings.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new SettingsScreen(menuDemo));
                dispose();
            }
        });

        creditsTexture = new Texture(Gdx.files.internal("principal/credits.png"));
        creditsPressTexture = new Texture(Gdx.files.internal("principal/creditsPress.png"));
        ImageButton credits = new ImageButton(new TextureRegionDrawable(new TextureRegion(creditsTexture)), new TextureRegionDrawable(new TextureRegion(creditsPressTexture)));
        credits.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new CreditsScreen(menuDemo));
                dispose();
            }
        });

        closetTexture = new Texture(Gdx.files.internal("principal/closet.png"));
        closetPressTexture = new Texture(Gdx.files.internal("principal/closetPress.png"));
        ImageButton closet = new ImageButton(new TextureRegionDrawable(new TextureRegion(closetTexture)), new TextureRegionDrawable(new TextureRegion(closetPressTexture)));
        closet.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new ClosetScreen(menuDemo));
                dispose();
            }
        });

        shopTexture = new Texture(Gdx.files.internal("principal/shop.png"));
        shopPressTexture = new Texture(Gdx.files.internal("principal/shopPress.png"));
        ImageButton shop = new ImageButton(new TextureRegionDrawable(new TextureRegion(shopTexture)), new TextureRegionDrawable(new TextureRegion(shopPressTexture)));
        shop.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new ShopScreen(menuDemo));
                dispose();
            }
        });

        table = new Table();
        //table.debug(); //Enables debug

        // ROW 1
        table.row();
        table.add(title).colspan(4).padBottom(30F);
        // ROW 2
        table.row();
        table.add(settings);

        table.add(kiwi).colspan(2);

        table.add(closet);

        // ROW 3
        table.row();
        table.add(credits);

        table.add(play).colspan(2);

        table.add(shop);

        // Pack table
        table.setFillParent(true);
        table.pack();

        // Set titles's alpha to 0
        //title.getColor().a = 0f;
        // Adds created table to stage
        stage.addActor(table);

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
        stage.act(delta);
        stage.draw();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        playPressTexture.dispose();
        titleTexture.dispose();
        closetTexture.dispose();
        closetPressTexture.dispose();
        creditsTexture.dispose();
        creditsPressTexture.dispose();
        shopTexture.dispose();
        shopPressTexture.dispose();
        settingsTexture.dispose();
        settingsPressTexture.dispose();
        music.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
