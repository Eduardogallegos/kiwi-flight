package com.mygdx.kiwiFlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class SettingsScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 100;
    private static final float EFFECTS_VOLUME_DEFAULT = 100;
    private final kiwiFlight kiwiFlight;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Slider musicSlider;
    private Slider soundSlider;

    private Music music;
    private float musicVolume;
    private float effectsVolume;

    private Preferences preferencias;
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


    public SettingsScreen(kiwiFlight game) {
        this.kiwiFlight =game;
        this.preferencias = kiwiFlight.getPreferences();
    }


    @Override
    public void show() {
        loadPreferences();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("settings/song.mp3"));
        updateVolume();
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("settings/fondoAjustes.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        returnTexture = new Texture(Gdx.files.internal("settings/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("settings/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.setPosition(20, 20);
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                savePreferences();
                kiwiFlight.setScreen(new StartScreen(kiwiFlight));
                dispose();
            }
        });

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("settings/sliderbarraajustes.png"))));
        ss.knob = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("settings/slider_knob.png"))));

        musicSlider = new Slider(0f, 1f, 0.1f, false, ss);
        musicSlider.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/2);
        musicSlider.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                musicVolume = musicSlider.getValue();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        soundSlider = new Slider(0f, 1f, 0.1f, false, ss);
        soundSlider.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/3+15);
        soundSlider.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                effectsVolume = soundSlider.getValue();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(soundSlider);
        stage.addActor(musicSlider);
        stage.addActor(retur);

        createQuitPanel();

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

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);

        effectsVolume = preferencias.getFloat("effectsVolume", EFFECTS_VOLUME_DEFAULT);
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

        preferencias.putFloat("effectsVolume", effectsVolume);

        preferencias.flush();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        updateVolume();
        stage.act(delta);
        stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            state = STATE.QUIT;
        }
        if(state == STATE.QUIT){
            quitStage.draw();
            Gdx.input.setInputProcessor(quitStage);
        }else{
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose();
        music.dispose();
        returnTexture.dispose();
        returnPressTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
