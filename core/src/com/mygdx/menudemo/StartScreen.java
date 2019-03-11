package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class StartScreen extends ScreenAdapter {

    private final Game game;
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


    public StartScreen(Game game) {
        this.game=game;
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("MenuBackground.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        titleTexture = new Texture(Gdx.files.internal("MenuLogo.png"));
        Image title = new Image(titleTexture);

        panelTexture= new Texture(Gdx.files.internal("MenuLogo.png"));
        Image panel = new Image(panelTexture);

        playTexture = new Texture(Gdx.files.internal("play.png"));
        playPressTexture = new Texture(Gdx.files.internal("playPress.png"));
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new LevelsScreen());
                dispose();
            }
        });

        settingsTexture = new Texture(Gdx.files.internal("options.png"));
        settingsPressTexture = new Texture(Gdx.files.internal("optionsPress.png"));
        ImageButton options = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsTexture)), new TextureRegionDrawable(new TextureRegion(settingsPressTexture)));
        options.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });



    }

}
