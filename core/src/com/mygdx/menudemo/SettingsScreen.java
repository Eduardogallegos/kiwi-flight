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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class SettingsScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static final float EFFECTS_VOLUME_DEFAULT = 1;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Texture musicLabel;
    private Texture soundLabel;
    private Slider musicSlider;
    private Slider soundSlider;
    private Table table;

    private Music music;
    private float musicVolume;
    private float effectsVolume;

    private Preferences preferencias;


    public SettingsScreen(MenuDemo game) {
        this.menuDemo =game;
        this.preferencias = menuDemo.getPreferences();
    }


    @Override
    public void show() {
        loadPreferences();

        super.show();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("settings/song.mp3"));
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("settings/fondoAjustes.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        titleTexture = new Texture(Gdx.files.internal("settings/ajustes.png"));
        Image title = new Image(titleTexture);

        musicLabel = new Texture(Gdx.files.internal("settings/musica.png"));
        Image musicTitle = new Image(musicLabel);

        soundLabel = new Texture(Gdx.files.internal("settings/sonido.png"));
        Image soundTitle = new Image(soundLabel);

        returnTexture = new Texture(Gdx.files.internal("settings/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("settings/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                savePreferences();
                menuDemo.setScreen(new StartScreen(menuDemo));
                dispose();
            }
        });

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("settings/sliderbarraajustes.png"))));
        ss.knob = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("settings/slider_knob.png"))));

        musicSlider = new Slider(0f, 1f, 0.1f, false, ss);
        musicSlider.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                musicVolume = musicSlider.getValue();
                Gdx.app.log("EVENT", "slider changed to " + musicSlider.getValue());
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        soundSlider = new Slider(0f, 1f, 0.1f, false, ss);
        soundSlider.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                effectsVolume = soundSlider.getValue();
                Gdx.app.log("EVENT", "slider changed to " + soundSlider.getValue());
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        table = new Table();
        table.pad(20);
        //table.setDebug(true);

        table.add(title).expand().colspan(3);

        table.row();
        table.add(musicTitle).expand();
        table.add(musicSlider).expand();

        table.row();
        table.add(soundTitle).expand();
        table.add(soundSlider).expand();

        table.row();
        table.add(retur).bottom().left();

        table.setFillParent(true);
        table.pack();
        stage.addActor(table);

    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        Gdx.app.log("LOG:","Music volume: " +  musicVolume + "/100");

        effectsVolume = preferencias.getFloat("effectsVolume", EFFECTS_VOLUME_DEFAULT);
        Gdx.app.log("LOG:","Effects volume: " + effectsVolume + "/100");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause(){
        //se guardan las preferencias antes de salir
        savePreferences();
    }

    private void savePreferences() {
        preferencias.putFloat("musicVolume", musicVolume);
        Gdx.app.log("LOG:","Music volume: " +  musicVolume + "/100");

        preferencias.putFloat("effectsVolume", effectsVolume);
        Gdx.app.log("LOG:","Effects volume: " + effectsVolume + "/100");

        preferencias.flush();
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
        titleTexture.dispose();
        music.dispose();
        returnTexture.dispose();
        returnPressTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
