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

class LevelsScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture levelOneTexture;
    private Texture levelOnePressTexture;

    private Texture levelTwoTexture;
    private Texture levelTwoPressTexture;
    private Texture levelThreeTexture;
    private Texture levelThreePressTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Texture bossTexture;
    private Texture bossPressTexture;
    private Texture lockTexture;
    private Table table;
    private Music music;
    private float musicVolume;
    private Preferences preferencias;

    public LevelsScreen(MenuDemo menuDemo) {
        this.menuDemo =menuDemo;
        this.preferencias = menuDemo.getPreferences();
    }

    @Override
    public void show(){
        loadPreferences();
        super.show();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("levels/song.mp3"));
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("levels/fondo.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        //titleTexture = new Texture(Gdx.files.internal("MenuLogo.png"));
        //Image title = new Image(titleTexture);

        returnTexture = new Texture(Gdx.files.internal("levels/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("levels/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new StartScreen(menuDemo));
                dispose();
            }
        });

        levelOneTexture = new Texture(Gdx.files.internal("levels/1.png"));
        levelOnePressTexture = new Texture(Gdx.files.internal("levels/1Press.png"));
        ImageButton levelOne = new ImageButton(new TextureRegionDrawable(new TextureRegion(levelOneTexture)), new TextureRegionDrawable(new TextureRegion(levelOnePressTexture)));
        levelOne.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LoadingScreen((MenuDemo) menuDemo, 1));
                dispose();
            }
        });

        levelTwoTexture = new Texture(Gdx.files.internal("levels/2.png"));
        levelTwoPressTexture = new Texture(Gdx.files.internal("levels/2Press.png"));
        ImageButton level2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(levelTwoTexture)), new TextureRegionDrawable(new TextureRegion(levelTwoPressTexture)));
        level2.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LoadingScreen((MenuDemo) menuDemo, 2));
                dispose();
            }
        });

        levelThreeTexture = new Texture(Gdx.files.internal("levels/3.png"));
        levelThreePressTexture = new Texture(Gdx.files.internal("levels/3Press.png"));
        ImageButton level3 = new ImageButton(new TextureRegionDrawable(new TextureRegion(levelThreeTexture)), new TextureRegionDrawable(new TextureRegion(levelThreePressTexture)));
        level3.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LoadingScreen((MenuDemo) menuDemo, 3));
                dispose();
            }
        });

        bossTexture = new Texture(Gdx.files.internal("levels/boss.png"));
        bossPressTexture = new Texture(Gdx.files.internal("levels/bossPress.png"));
        ImageButton boss = new ImageButton(new TextureRegionDrawable(new TextureRegion(bossTexture)), new TextureRegionDrawable(new TextureRegion(bossPressTexture)));
        boss.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LoadingScreen((MenuDemo) menuDemo,4));
                dispose();
            }
        });

        levelOne.setPosition(100,200);
        level2.setPosition(400,200);
        level3.setPosition(730,200);
        boss.setPosition(1030,375);
        stage.addActor(levelOne);
        stage.addActor(level2);
        stage.addActor(level3);
        stage.addActor(boss);

        table = new Table();
        table.pad(20);
        //table.setDebug(true);

        table.add(retur).expand().top().left();
        //table.add(title).padBottom(100).colspan(3);

        table.setFillParent(true);
        table.pack();
        stage.addActor(table);


    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        Gdx.app.log("LOG:","Music volume: " +  musicVolume + "/100");
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

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose();
        levelOneTexture.dispose();
        levelOnePressTexture.dispose();
        levelTwoTexture.dispose();
        levelTwoPressTexture.dispose();
        levelThreeTexture.dispose();
        levelThreePressTexture.dispose();
        //titleTexture.dispose();
        music.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
