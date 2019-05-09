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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class level extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float GAP_BETWEEN_OBSTACLES = 80f;
    private static final float GAP_BETWEEN_COINS = 350f;
    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static final float EFFECTS_VOLUME_DEFAULT = 1;
    private static final int COINS_DEFAULT = 0;
    private static final String ACTUAL_SKIN = "default";
    private static final float FRAME_DURATION = 0.4f;
    private static final int WINNING_TILE_HEIGHT = 133;
    private static final int WINNING_TILE_WIDTH = 200;
    private static final int LOOSING_TILE_HEIGHT = 140;
    private static final int LOOSING_TILE_WIDTH = 237;
    private static final float LOOSE_FRAME_DURATION = 0.2F;
    private static int LEVEL;
    private static final float DEFAULT_CHUNCK_SIZE = 11 ;

    private float[] PADS = {0,97,194,291,388};
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Coin> coins = new Array<Coin>();
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private Stage stage;
    private MenuDemo menuDemo;
    private SpriteBatch batch;
    private Kiiw kiiw;
    private Hawk hawk;
    private int padCounter = 2;
    private int lifes = 2;
    private Texture kiiwTexture;
    private Texture obstacleTexture;
    private float levelTimer = 0;
    private float secondsTimer = 60;
    private int minutes;
    private int seconds;
    private float nonCollisionTimer=0;
    private int actualSpeed;

    private OrthographicCamera cameraHUD;
    private FitViewport viewportHUD;
    private Stage stageUI;
    private Texture playButtonTexture;
    private Texture speedBarTexture;
    private Texture coinsIndicatorTexture;
    private Texture pausePanelTexture;
    private Texture hawkTexture = menuDemo.getAssetManager().get("level4/hawk.png");

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Music music;
    private Texture resumeButtonTexture;
    private Texture resumePressedButtonTexture;
    private Stage stagePause;
    private Texture settingsPressedButtonTexture;
    private Texture settingsButtonTexture;
    private Texture restartButtonTexture;
    private Texture restartPressedButtonTexture;
    private Texture exitButtonTexture;
    private Texture exitPressedButtonTexture;
    private int speedNeeded;
    private Texture coinTexture;
    private int lowSpeedLimit = 15;

    private Rectangle speedBarRectangle;
    private float speedBarChunkSize = DEFAULT_CHUNCK_SIZE;
    private Stage stageGameOver;
    private Texture gameOverPanelTexture;
    private Texture yesButtonTexture;
    private Texture yesPressedButtonTexture;
    private Texture noButtonTexture;
    private Texture noPressedButtonTexture;
    private Preferences preferencias;
    private float musicVolume;
    private float effectsVolume;
    private Music hitEffect;
    private Music coinEffect;
    private Music loseEffect;
    private Music loseMusic;
    private Music winEffect;
    private int bgImagesNumber = 5;
    private boolean bossLevel = false;
    private InputMultiplexer multiplexer = new InputMultiplexer();
    private Texture beginPanelTexture;
    private Stage stageBegin;
    private Texture okButtonTexture;
    private Texture okPressedButtonTexture;
    private Stage stageTutorial;
    private Texture obstaclesTutorialTexture;
    private Texture nextButtonTexture;
    private Texture nextPressedButtonTexture;
    private int coinsCollected;
    private Texture playPressedButtonTexture;
    private int coinsAtBegining;
    private Texture lifesBarTexture1;
    private Texture lifesBarTexture2;
    private Texture lifesBarTexture3;
    private Image lifesBar;
    private Stage stageLifes;
    private Texture speedTutorialTexture;
    private Texture timeTutorialTexture;
    private Texture coinsTutorialTexture;
    private Image tutorialPanel;
    private Texture backButtonTexture;
    private Texture backPressedButtonTexture;
    private String skin;
    private Stage stageSettings;
    private Texture settingsPanelTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Slider musicSlider;
    private Slider soundSlider;
    private Texture kiiwWinningTexture;
    private Texture kiiwLoosingTexture;
    private Animation winningAnimation;
    private float animationTimer = 0;
    private TextureRegion winningKiiw;
    private TextureRegion loosingKiiw;
    private float winingKiiwX = WORLD_WIDTH/4;
    private float winingKiiwY;
    private Animation loosingAnimation;
    private float looseKiiwX = 20;
    private boolean finish = false;
    private Texture lifesBarTexture0;
    private Texture skipButtonTexture;
    private Texture buttonPressTexture;
    private Stage quitStage;
    private Texture quitPanelTexture;
    private Texture yesQuitTexture;
    private Texture yesQuitPressTexture;
    private Texture noQuitTexture;
    private Texture noQuitPressTexture;

    private enum STATE {
        PLAYING, PAUSED, GAMEOVER, WIN, PANELS, TUTORIAL, QUIT, SETTINGS
    }
    private STATE state = STATE.PANELS;

    public level(MenuDemo menuDemo, int level) {
        this.menuDemo = menuDemo;
        this.LEVEL = level;
        this.preferencias = menuDemo.getPreferences();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width,height);
        viewportHUD.update(width, height);
    }

    @Override
    public void show() {
        loadPreferences();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        bitmapFont = new BitmapFont(Gdx.files.internal("defaultLevels/numbers.fnt"));
        glyphLayout = new GlyphLayout();

        loadMusic();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        if (LEVEL == 4){
            bgImagesNumber = 3;
            bossLevel = true;
            if(skin.compareTo("default") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingKiwi.png");
            }else if(skin.compareTo("party") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingPartyHatKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningPartyHatKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingPartyHatKiwi.png");
            }else if(skin.compareTo("hat") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingTopHatKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningTopHatKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingTopHatKiwi.png");
            }else if(skin.compareTo("tie") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingTieKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningTieKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingTieKiwi.png");
            }else if(skin.compareTo("crown") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingCrownKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningCrownKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingCrownKiwi.png");
            }else if(skin.compareTo("hulk") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingHulkKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningHulkKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingHulkKiwi.png");
            }else if(skin.compareTo("ricardo") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingDancingKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningDancingKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingDancingKiwi.png");
            }
            hawk = new Hawk(hawkTexture);
            actualSpeed = 15;
            //hawk.setPosition(20, WORLD_HEIGHT/2);
        }else{
            actualSpeed = 0;
            if(skin.compareTo("default") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/RunningKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingKiwi.png");
            }else if(skin.compareTo("party") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/partyKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningPartyHatKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingPartyHatKiwi.png");
            }else if(skin.compareTo("hat") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/HatKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningTopHatKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingTopHatKiwi.png");
            }else if(skin.compareTo("tie") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/TieKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningTieKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingTieKiwi.png");
            }else if(skin.compareTo("crown") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/CrownKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningCrownKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingCrownKiwi.png");
            }else if(skin.compareTo("hulk") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/HulkKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningHulkKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingHulkKiwi.png");
            }else if(skin.compareTo("ricardo") == 0){
                kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/ricardoKiwi.png");
                kiiwWinningTexture = menuDemo.getAssetManager().get("defaultLevels/WinningDancingKiwi.png");
                kiiwLoosingTexture = menuDemo.getAssetManager().get("defaultLevels/LosingDancingKiwi.png");
            }

        }

        kiiw = new Kiiw(kiiwTexture, bossLevel);
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);

        TextureRegion [][] kiwiWinningTextures = new TextureRegion(kiiwWinningTexture).split(WINNING_TILE_WIDTH, WINNING_TILE_HEIGHT);
        winningAnimation = new Animation(FRAME_DURATION, kiwiWinningTextures[0][0], kiwiWinningTextures[0][1],kiwiWinningTextures[0][2],kiwiWinningTextures[0][3],kiwiWinningTextures[0][4],kiwiWinningTextures[0][5],kiwiWinningTextures[0][6],kiwiWinningTextures[0][7],kiwiWinningTextures[0][8],kiwiWinningTextures[0][9],kiwiWinningTextures[0][10],kiwiWinningTextures[0][11],kiwiWinningTextures[0][12],kiwiWinningTextures[0][13],kiwiWinningTextures[0][14],kiwiWinningTextures[0][15]);
        winningAnimation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion [][] kiwiLoosingTextures = new TextureRegion(kiiwLoosingTexture).split(LOOSING_TILE_WIDTH, LOOSING_TILE_HEIGHT);
        loosingAnimation = new Animation(LOOSE_FRAME_DURATION, kiwiLoosingTextures[0][0], kiwiLoosingTextures[0][1],kiwiLoosingTextures[0][2],kiwiLoosingTextures[0][3],kiwiLoosingTextures[0][4],kiwiLoosingTextures[0][5],kiwiLoosingTextures[0][6],kiwiLoosingTextures[0][7],kiwiLoosingTextures[0][8],kiwiLoosingTextures[0][9],kiwiLoosingTextures[0][10],kiwiLoosingTextures[0][11],kiwiLoosingTextures[0][12],kiwiLoosingTextures[0][13],kiwiLoosingTextures[0][14],kiwiLoosingTextures[0][15]);
        loosingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        Array<Texture> textures = new Array<Texture>();

        for(int i = 1; i <=bgImagesNumber;i++){
            textures.add(new Texture(Gdx.files.internal("level"+LEVEL+"/BG"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        ParallaxBackground parallaxBackground = new ParallaxBackground(textures, WORLD_WIDTH, WORLD_HEIGHT);
        parallaxBackground.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        cameraHUD = new OrthographicCamera();
        viewportHUD = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,cameraHUD);
        cameraHUD.update();

        stageUI = new Stage(viewportHUD);
        playButtonTexture = menuDemo.getAssetManager().get("defaultLevels/pausa.png");
        playPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/pausaPress.png");
        ImageButton pause = new ImageButton(new TextureRegionDrawable(new TextureRegion(playButtonTexture)), new TextureRegionDrawable(new TextureRegion(playPressedButtonTexture)));
        pause.setPosition(WORLD_WIDTH - pause.getWidth()*1.1f, WORLD_HEIGHT- pause.getHeight()*1.1f);
        pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(state==STATE.PLAYING){
                    state = STATE.PAUSED;
                }
            }
        });

        if(LEVEL == 1)speedNeeded = 25;
        else if( LEVEL == 2)speedNeeded = 30;
        else if (LEVEL == 3)speedNeeded = 35;
        else speedNeeded = 40;

        speedBarTexture = menuDemo.getAssetManager().get("defaultLevels/Barra.png");
        Image speedBar = new Image(speedBarTexture);
        speedBar.setPosition(30, WORLD_HEIGHT - speedBar.getHeight()*1.5f);
        speedBarChunkSize = 275 / speedNeeded;

        speedBarRectangle = new Rectangle();
        speedBarRectangle.x = 31;
        speedBarRectangle.y = WORLD_HEIGHT - speedBar.getHeight()*1.4f;
        speedBarRectangle.height = 35;
        speedBarRectangle.width = 0;

        stageLifes = new Stage(viewportHUD);
        lifesBarTexture3 = menuDemo.getAssetManager().get("defaultLevels/lifes2.png");
        lifesBarTexture2 = menuDemo.getAssetManager().get("defaultLevels/lifes1.png");
        lifesBarTexture1 = menuDemo.getAssetManager().get("defaultLevels/lifes0.png");
        lifesBarTexture0 = menuDemo.getAssetManager().get("defaultLevels/lifesNull.png");
        lifesBar = new Image(lifesBarTexture3);
        lifesBar.setPosition(1*WORLD_WIDTH/3+80, WORLD_HEIGHT-lifesBar.getHeight()*1.1f);
        stageLifes.addActor(lifesBar);

        coinsIndicatorTexture = menuDemo.getAssetManager().get("defaultLevels/Coins.png");
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(3*WORLD_WIDTH/5+70, WORLD_HEIGHT-coins.getHeight()-10);

        stageUI.addActor(pause);
        stageUI.addActor(speedBar);
        stageUI.addActor(coins);

        stagePause = new Stage(viewportHUD);

        pausePanelTexture = menuDemo.getAssetManager().get("defaultLevels/pausepanel.png");
        Image pausePanel = new Image(pausePanelTexture);

        resumeButtonTexture = menuDemo.getAssetManager().get("defaultLevels/resume.png");
        resumePressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/resumePressed.png");
        ImageButton resume = new ImageButton(new TextureRegionDrawable(new TextureRegion(resumeButtonTexture)), new TextureRegionDrawable(new TextureRegion(resumePressedButtonTexture)));
        resume.setPosition(WORLD_WIDTH/2-resume.getWidth()/2+15, WORLD_HEIGHT/2+resume.getHeight()-20);
        resume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state=STATE.PLAYING;
            }
        });

        settingsButtonTexture = menuDemo.getAssetManager().get("defaultLevels/settings.png");
        settingsPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/settingsPressed.png");
        ImageButton settings = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsButtonTexture)), new TextureRegionDrawable(new TextureRegion(settingsPressedButtonTexture)));
        settings.setPosition(WORLD_WIDTH/2-settings.getWidth()/2+15, WORLD_HEIGHT/2-30);
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state = STATE.SETTINGS;
            }
        });

        restartButtonTexture = menuDemo.getAssetManager().get("defaultLevels/restart.png");
        restartPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/restartPressed.png");
        final ImageButton restart = new ImageButton(new TextureRegionDrawable(new TextureRegion(restartButtonTexture)), new TextureRegionDrawable(new TextureRegion(restartPressedButtonTexture)));
        restart.setPosition(WORLD_WIDTH/2-restart.getWidth()/2+15, WORLD_HEIGHT/2-(2*restart.getHeight())+20);
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                state = STATE.PLAYING;
                restart();
            }
        });

        exitButtonTexture = menuDemo.getAssetManager().get("defaultLevels/exit.png");
        exitPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/exitPressed.png");
        ImageButton exit = new ImageButton(new TextureRegionDrawable(new TextureRegion(exitButtonTexture)), new TextureRegionDrawable(new TextureRegion(exitPressedButtonTexture)));
        exit.setPosition(WORLD_WIDTH/2-exit.getWidth()/2+15, WORLD_HEIGHT/2-(3*exit.getHeight()));
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.stop();
                menuDemo.setScreen(new LevelsScreen(menuDemo));
            }
        });

        stageSettings = new Stage(viewportHUD);

        settingsPanelTexture = menuDemo.getAssetManager().get("defaultLevels/settingsPanel.png");
        Image settingsPanel = new Image(settingsPanelTexture);

        returnTexture = menuDemo.getAssetManager().get("defaultLevels/flecha.png");
        returnPressTexture = menuDemo.getAssetManager().get("defaultLevels/flechaPress.png");
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.setPosition(20, 20);
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                saveVolume();
                state = STATE.PAUSED;
            }
        });

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("defaultLevels/sliderBarra.png"))));
        ss.knob = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("defaultLevels/circle.png"))));

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

        stageSettings.addActor(settingsPanel);
        stageSettings.addActor(soundSlider);
        stageSettings.addActor(musicSlider);
        stageSettings.addActor(retur);

        if(LEVEL==1) {
            stageTutorial = new Stage(viewportHUD);
            obstaclesTutorialTexture = menuDemo.getAssetManager().get("level1/obstacles.png");
            speedTutorialTexture = menuDemo.getAssetManager().get("level1/speed.png");
            timeTutorialTexture = menuDemo.getAssetManager().get("level1/time.png");
            coinsTutorialTexture = menuDemo.getAssetManager().get("level1/coins.png");
            updateTutorial(0);
        }

        stageBegin = new Stage(viewportHUD);
        beginPanelTexture = menuDemo.getAssetManager().get("level"+LEVEL+"/panel.png");
        Image beginPanel = new Image(beginPanelTexture);

        okButtonTexture = menuDemo.getAssetManager().get("defaultLevels/ok.png");
        okPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/okPressed.png");
        ImageButton ok = new ImageButton(new TextureRegionDrawable(new TextureRegion(okButtonTexture)), new TextureRegionDrawable(new TextureRegion(okPressedButtonTexture)));
        ok.setPosition(WORLD_WIDTH-1.2f*ok.getWidth(), ok.getHeight()/2);
        ok.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(LEVEL == 1){
                    state = STATE.TUTORIAL;
                }else{
                    state = STATE.PLAYING;
                }
            }
        });

        stageGameOver = new Stage(viewportHUD);
        gameOverPanelTexture = menuDemo.getAssetManager().get("defaultLevels/gameOverPanel.png");
        Image gameOverPanel = new Image(gameOverPanelTexture);

        yesButtonTexture = menuDemo.getAssetManager().get("defaultLevels/yes.png");
        yesPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/yesPressed.png");
        ImageButton retry = new ImageButton(new TextureRegionDrawable(new TextureRegion(yesButtonTexture)), new TextureRegionDrawable(new TextureRegion(yesPressedButtonTexture)));
        retry.setPosition(WORLD_WIDTH/2-290, WORLD_HEIGHT/2-75);
        retry.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               loseMusic.stop();
               loseEffect.stop();
               state = STATE.PLAYING;
               restart();
           }
        });

        noButtonTexture = menuDemo.getAssetManager().get("defaultLevels/no.png");
        noPressedButtonTexture = menuDemo.getAssetManager().get("defaultLevels/noPressed.png");
        ImageButton noRetry = new ImageButton(new TextureRegionDrawable(new TextureRegion(noButtonTexture)), new TextureRegionDrawable(new TextureRegion(noPressedButtonTexture)));
        noRetry.setPosition(WORLD_WIDTH/2+30, WORLD_HEIGHT/2-75);
        noRetry.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                loseMusic.stop();
                loseEffect.stop();
                menuDemo.setScreen(new LevelsScreen(menuDemo));
            }
        });

        createQuitPanel();

        stageGameOver.addActor(gameOverPanel);
        stageGameOver.addActor(retry);
        stageGameOver.addActor(noRetry);

        stageBegin.addActor(beginPanel);
        stageBegin.addActor(ok);

        stagePause.addActor(pausePanel);
        stagePause.addActor(resume);
        stagePause.addActor(settings);
        stagePause.addActor(restart);
        stagePause.addActor(exit);

        multiplexer.addProcessor(stageUI);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new GestureHandler()));
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
                state = STATE.PAUSED;
            }
        });

        quitStage.addActor(quitPanel);
        quitStage.addActor(yesQuit);
        quitStage.addActor(noQuit);
    }

    private void updateTutorial(int tutorialIndex) {
        stageTutorial.clear();
        if(tutorialIndex == 0){
            tutorialPanel = new Image(obstaclesTutorialTexture);

            nextButtonTexture = menuDemo.getAssetManager().get("level1/next.png");
            nextPressedButtonTexture = menuDemo.getAssetManager().get("level1/nextPressed.png");
            ImageButton nextButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(nextButtonTexture)), new TextureRegionDrawable(new TextureRegion(nextPressedButtonTexture)));
            nextButton.setPosition(WORLD_WIDTH-1.2f*nextButton.getWidth(), nextButton.getHeight()/2);
            nextButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(1);
                }
            });

            backButtonTexture = menuDemo.getAssetManager().get("level1/back.png");
            backPressedButtonTexture = menuDemo.getAssetManager().get("level1/backPressed.png");
            ImageButton backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backButtonTexture)), new TextureRegionDrawable(new TextureRegion(backPressedButtonTexture)));
            backButton.setPosition(backButton.getWidth()*.2f, nextButton.getHeight()/2);
            backButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    state =  STATE.PANELS;
                }
            });

            skipButtonTexture=menuDemo.getAssetManager().get("defaultLevels/panelSkip.png");
            buttonPressTexture=menuDemo.getAssetManager().get("defaultLevels/skipPress.png");

            ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(skipButtonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
            skipButton.setPosition(WORLD_WIDTH-1.1f*skipButton.getWidth(),WORLD_HEIGHT-1.1f*skipButton.getHeight());
            skipButton.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    state = STATE.PLAYING;
                }
            });

            stageTutorial.addActor(tutorialPanel);
            stageTutorial.addActor(nextButton);
            stageTutorial.addActor(backButton);
            stageTutorial.addActor(skipButton);
        }else if(tutorialIndex == 1){
            tutorialPanel = new Image(speedTutorialTexture);
            nextButtonTexture = menuDemo.getAssetManager().get("level1/next.png");
            nextPressedButtonTexture = menuDemo.getAssetManager().get("level1/nextPressed.png");
            ImageButton nextButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(nextButtonTexture)), new TextureRegionDrawable(new TextureRegion(nextPressedButtonTexture)));
            nextButton.setPosition(WORLD_WIDTH-1.2f*nextButton.getWidth(), nextButton.getHeight()/2);
            nextButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(2);
                }
            });
            backButtonTexture = menuDemo.getAssetManager().get("level1/back.png");
            backPressedButtonTexture = menuDemo.getAssetManager().get("level1/backPressed.png");
            ImageButton backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backButtonTexture)), new TextureRegionDrawable(new TextureRegion(backPressedButtonTexture)));
            backButton.setPosition(backButton.getWidth()*.2f, nextButton.getHeight()/2);
            backButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(0);
                }
            });
            skipButtonTexture=menuDemo.getAssetManager().get("defaultLevels/panelSkip.png");
            buttonPressTexture=menuDemo.getAssetManager().get("defaultLevels/skipPress.png");

            ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(skipButtonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
            skipButton.setPosition(WORLD_WIDTH-1.1f*skipButton.getWidth(),WORLD_HEIGHT-1.1f*skipButton.getHeight());
            skipButton.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    state = STATE.PLAYING;
                }
            });
            stageTutorial.addActor(tutorialPanel);
            stageTutorial.addActor(nextButton);
            stageTutorial.addActor(backButton);
            stageTutorial.addActor(skipButton);
        }else if(tutorialIndex == 2){
            tutorialPanel = new Image(timeTutorialTexture);

            nextButtonTexture = menuDemo.getAssetManager().get("level1/next.png");
            nextPressedButtonTexture = menuDemo.getAssetManager().get("level1/nextPressed.png");
            ImageButton nextButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(nextButtonTexture)), new TextureRegionDrawable(new TextureRegion(nextPressedButtonTexture)));
            nextButton.setPosition(WORLD_WIDTH-1.2f*nextButton.getWidth(), nextButton.getHeight()/2);
            nextButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(3);
                }
            });

            backButtonTexture = menuDemo.getAssetManager().get("level1/back.png");
            backPressedButtonTexture = menuDemo.getAssetManager().get("level1/backPressed.png");
            ImageButton backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backButtonTexture)), new TextureRegionDrawable(new TextureRegion(backPressedButtonTexture)));
            backButton.setPosition(backButton.getWidth()*.2f, nextButton.getHeight()/2);
            backButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(1);
                }
            });
            skipButtonTexture=menuDemo.getAssetManager().get("defaultLevels/panelSkip.png");
            buttonPressTexture=menuDemo.getAssetManager().get("defaultLevels/skipPress.png");

            ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(skipButtonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
            skipButton.setPosition(WORLD_WIDTH-1.1f*skipButton.getWidth(),WORLD_HEIGHT-1.1f*skipButton.getHeight());
            skipButton.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    state = STATE.PLAYING;
                }
            });
            stageTutorial.addActor(tutorialPanel);
            stageTutorial.addActor(nextButton);
            stageTutorial.addActor(backButton);
            stageTutorial.addActor(skipButton);
        }else if(tutorialIndex == 3){
            tutorialPanel = new Image(coinsTutorialTexture);

            nextButtonTexture = menuDemo.getAssetManager().get("level1/got.png");
            nextPressedButtonTexture = menuDemo.getAssetManager().get("level1/gotPressed.png");
            ImageButton nextButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(nextButtonTexture)), new TextureRegionDrawable(new TextureRegion(nextPressedButtonTexture)));
            nextButton.setPosition(WORLD_WIDTH-1.2f*nextButton.getWidth(), nextButton.getHeight()/2);
            nextButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    state = STATE.PLAYING;
                }
            });

            backButtonTexture = menuDemo.getAssetManager().get("level1/back.png");
            backPressedButtonTexture = menuDemo.getAssetManager().get("level1/backPressed.png");
            ImageButton backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backButtonTexture)), new TextureRegionDrawable(new TextureRegion(backPressedButtonTexture)));
            backButton.setPosition(backButton.getWidth()*.2f, nextButton.getHeight()/2);
            backButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    updateTutorial(2);
                }
            });

            skipButtonTexture=menuDemo.getAssetManager().get("defaultLevels/panelSkip.png");
            buttonPressTexture=menuDemo.getAssetManager().get("defaultLevels/skipPress.png");
            ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(skipButtonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
            skipButton.setPosition(WORLD_WIDTH-1.1f*skipButton.getWidth(),WORLD_HEIGHT-1.1f*skipButton.getHeight());
            skipButton.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    state = STATE.PLAYING;
                }
            });
            stageTutorial.addActor(tutorialPanel);
            stageTutorial.addActor(nextButton);
            stageTutorial.addActor(backButton);
            stageTutorial.addActor(skipButton);
        }
    }

    private void loadMusic() {
        hitEffect = menuDemo.getAssetManager().get("defaultLevels/hit.mp3");
        coinEffect = menuDemo.getAssetManager().get("defaultLevels/coin.mp3");
        loseEffect = menuDemo.getAssetManager().get("defaultLevels/Kiwhine.mp3");
        loseMusic = menuDemo.getAssetManager().get("defaultLevels/KiiwLose.mp3");
        winEffect = menuDemo.getAssetManager().get("defaultLevels/KiiwWin.mp3");
        music = menuDemo.getAssetManager().get("level"+LEVEL+"/song.mp3");
        updateVolume();
        music.setLooping(true);
        music.play();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        effectsVolume = preferencias.getFloat("effectsVolume", EFFECTS_VOLUME_DEFAULT);
        coinsCollected = preferencias.getInteger("coins", COINS_DEFAULT);
        coinsAtBegining = preferencias.getInteger("coins", COINS_DEFAULT);
        skin = preferencias.getString("actualSkin", ACTUAL_SKIN);
    }

    private void saveVolume(){
        preferencias.putFloat("musicVolume", musicVolume);
        preferencias.putFloat("effectsVolume", effectsVolume);
        preferencias.flush();
    }

    private void savePreferences() {
        preferencias.putInteger("coins", coinsCollected);
        preferencias.putString("actualSkin", skin);
        if(LEVEL == 1){
            preferencias.putBoolean("level2", false);
        }else if(LEVEL == 2){
            preferencias.putBoolean("level3", false);
        }else if (LEVEL == 3){
            preferencias.putBoolean("level4", false);
        }
        preferencias.flush();
    }

    @Override
    public void pause() {
        saveVolume();
    }

    @Override
    public void render(float delta) {
        if(state == STATE.PLAYING){
            Gdx.input.setInputProcessor(multiplexer);
            update(delta);
            stage.act();
            levelTimer+=delta;
            secondsTimer-=delta;
            checkIfTimeFinish();
            checkIfSpeedReached();
            if(kiiw.isHit()){
                nonCollisionTimer = 0;
            }else{
                nonCollisionTimer+=delta;
            }
        }else if(state == STATE.WIN || state == STATE.GAMEOVER) {
            animationTimer += delta;
            stage.act();
            updateKiiwWinAnimation();
        }
        clearScreen();
        updateVolume();
        draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            state = STATE.QUIT;
        }
        if(state == STATE.QUIT){
            quitStage.draw();
            Gdx.input.setInputProcessor(quitStage);
        }
    }

    private void updateKiiwWinAnimation() {
        winingKiiwX += 3;
        winingKiiwY += 0.7;
    }


    private void updateVolume() {
        music.setVolume(musicVolume);
        loseMusic.setVolume(musicVolume);
        winEffect.setVolume(effectsVolume);
        coinEffect.setVolume(effectsVolume);
        hitEffect.setVolume(effectsVolume);
        loseEffect.setVolume(effectsVolume);
    }

    private void checkIfSpeedReached() {
        if(actualSpeed == speedNeeded){
            winEffect.play();
            state = STATE.WIN;
            finish = true;
            winingKiiwY = kiiw.getY();
        }
    }

    private void checkIfTimeFinish() {
        if (levelTimer>=120){
            loseEffect.play();
            loseMusic.play();
            state = STATE.GAMEOVER;
            finish = true;
        }
    }

    private void draw(){
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        if(bossLevel){
            hawk.draw(batch);
        }
        if(!finish) kiiw.draw(batch);
        if(state == STATE.WIN) {
            winningKiiw = (TextureRegion) winningAnimation.getKeyFrame(animationTimer);
            batch.draw(winningKiiw, winingKiiwX, winingKiiwY);
        }else if(state == STATE.GAMEOVER){
            loosingKiiw = (TextureRegion) loosingAnimation.getKeyFrame(animationTimer);
            batch.draw(loosingKiiw, kiiw.getX(), kiiw.getY());
        }
        drawCoin();
        drawObstacle();
        drawTimerString();
        drawMinuteTimer();
        drawSecondtimer();
        drawCoinsCounter();
        drawNeededSpeedIndicator();
        drawActualSpeedIndicator();
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CORAL);
        shapeRenderer.rect(speedBarRectangle.x,speedBarRectangle.y,speedBarRectangle.width,speedBarRectangle.height);
        shapeRenderer.end();

        stageLifes.draw();
        stageUI.draw();

        if(state == STATE.TUTORIAL){
            stageTutorial.draw();
            Gdx.input.setInputProcessor(stageTutorial);
        }else if(state == STATE.PANELS){
            stageBegin.draw();
            Gdx.input.setInputProcessor(stageBegin);
        }else if(state== STATE.PAUSED){
            stagePause.draw();
            Gdx.input.setInputProcessor(stagePause);
        }else if (state == STATE.GAMEOVER){
            music.stop();
            obstacles.clear();
            coins.clear();
            stageLifes.clear();
            lifesBar = new Image(lifesBarTexture0);
            lifesBar.setPosition(1*WORLD_WIDTH/3+80, WORLD_HEIGHT-lifesBar.getHeight()*1.1f);
            stageLifes.addActor(lifesBar);
            if(animationTimer>=2.5){
                stageGameOver.draw();
                Gdx.input.setInputProcessor(stageGameOver);
            }
        }else if(state == STATE.WIN){
            savePreferences();
            obstacles.clear();
            coins.clear();
            if(animationTimer>=4.5){
                if(bossLevel){
                    menuDemo.setScreen(new EndingTransitionScreen(menuDemo));
                    music.stop();
                }else {
                    menuDemo.setScreen(new LevelsScreen(menuDemo));
                    music.stop();
                }
            }
        }else if( state == STATE.SETTINGS){
            stageSettings.draw();
            Gdx.input.setInputProcessor(stageSettings);
        }
    }

    private void drawActualSpeedIndicator() {
        String actualSpeedAsString = Integer.toString(actualSpeed) + " km/h";
        glyphLayout.setText(bitmapFont, actualSpeedAsString);
        bitmapFont.draw(batch, actualSpeedAsString, 25, WORLD_HEIGHT-speedBarTexture.getHeight()-25);
    }

    private void drawCoin() {
        for(Coin coin:coins){
            coin.draw(batch);
        }
    }

    private void drawTimerString() {
        String timerString = "time left";
        glyphLayout.setText(bitmapFont, timerString);
        bitmapFont.draw(batch, timerString, 25, 7*viewport.getWorldHeight()/8-18);
    }

    private void drawNeededSpeedIndicator() {
        String speedNeededAsString = Integer.toString(speedNeeded) + " km/h";
        glyphLayout.setText(bitmapFont, speedNeededAsString);
        bitmapFont.draw(batch, speedNeededAsString, 30+speedBarTexture.getWidth()+20,WORLD_HEIGHT-25 );
    }

    private void drawCoinsCounter() {
        String coinsAsString = Integer.toString(coinsCollected);
        glyphLayout.setText(bitmapFont, coinsAsString);
        bitmapFont.draw(batch, coinsAsString, 3*WORLD_WIDTH/5+170, WORLD_HEIGHT-coinsIndicatorTexture.getHeight()+50);
    }

    //https://learning.oreilly.com/library/view/libgdx-game-development/9781785281440/ch05s03.html

    private void drawSecondtimer() {
        String secondsAsString;
        if(seconds<10){
            secondsAsString = "0" + Integer.toString(seconds);
        }else{
            secondsAsString = Integer.toString(seconds);
        }
        glyphLayout.setText(bitmapFont, secondsAsString);
        bitmapFont.draw(batch, secondsAsString,65, (7 * viewport.getWorldHeight() / 8)-2*bitmapFont.getCapHeight());
    }

    private void drawMinuteTimer() {
        String minutesAsString = Integer.toString(minutes)+ " : ";
        glyphLayout.setText(bitmapFont, minutesAsString);
        bitmapFont.draw(batch, minutesAsString ,30, (7 * viewport.getWorldHeight() / 8)-2*bitmapFont.getCapHeight());
    }

    private void drawObstacle() {
        for(Obstacle obstacle : obstacles){
            obstacle.draw(batch);
        }
    }


    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta){
        updateKiiw(delta);
        if(bossLevel) hawk.update(delta);
        updateObstacles(delta);
        updateCoins(delta);
        updateMinuteTimer();
        updateSecondTimer();
        updateActualSpeed();
        handleCollisions();
        updateSpeedBarRectangle();
    }

    private void handleCollisions() {

        Obstacle culprit = checkForCollision();

        if (culprit != null){
            if(culprit.isGrass()){
                substractSpeed();
            }else{
                substractLife();
                substractSpeed();
            }

            kiiw.setHit(true);
        }
    }

    private void updateSpeedBarRectangle() {
        speedBarRectangle.width = speedBarChunkSize * actualSpeed;
    }

    private void updateActualSpeed() {
        if(nonCollisionTimer >= 2){
            actualSpeed++;
            nonCollisionTimer=0;
        }else if(actualSpeed<0){
            actualSpeed = 0;
        }
    }

    private void updateCoins(float delta) {
        for(Coin coin: coins){
            coin.update(delta);
            if (actualSpeed < 0) {
                coin.setSpeedPerSecond(350F);
            }else{
                float speed = coin.getSpeedPerSecond()+actualSpeed/2;
                coin.setSpeedPerSecond(speed);
            }
        }
        checkIfNewCoinIsNeeded();
        removeCoinIfCollected();
        removeCoinIfPassed();
    }

    private void updateLifesIndicator() {
        stageLifes.clear();
        if(lifes == 2){
            lifesBar = new Image(lifesBarTexture3);
        }
        else if(lifes == 1){
            lifesBar = new Image(lifesBarTexture2);
        }else if (lifes == 0){
            lifesBar = new Image(lifesBarTexture1);
        }
        lifesBar.setPosition(1*WORLD_WIDTH/3+80, WORLD_HEIGHT-lifesBar.getHeight()*1.1f);
        stageLifes.addActor(lifesBar);
    }

    private void substractLife() {
        if(lifes<=0){
            loseEffect.play();
            loseMusic.play();
            state = STATE.GAMEOVER;
            finish = true;
        }
        else{
            lifes--;
            updateLifesIndicator();
            hitEffect.play();
        }
    }


    private void substractSpeed() {
        if(actualSpeed<=lowSpeedLimit && bossLevel){
            music.stop();
            state = STATE.GAMEOVER;
            finish = true;
        }else {
            actualSpeed--;
        }
    }

    private void updateSecondTimer() {
        if (secondsTimer<0){
            secondsTimer=60;
        }
        seconds = (int) secondsTimer;
    }

    private void updateMinuteTimer() {
        if (levelTimer > 60){
            minutes = 0;
        }else if (levelTimer> 1){
            minutes = 1;
        }else{
            minutes = 2;
        }
    }

    private class GestureHandler extends GestureDetector.GestureAdapter {
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
                if (velocityY < 0) {
                    kiiw.setPosition(WORLD_WIDTH / 4, (97 * ++padCounter) + kiiw.RADIUS);
                } else {
                    kiiw.setPosition(WORLD_WIDTH / 4, (97 * --padCounter) + kiiw.RADIUS);
                }
            return false;
        }
    }

    private void updateKiiw(float delta) {
        kiiw.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) kiiw.setPosition(WORLD_WIDTH/4, (97* ++padCounter)+kiiw.RADIUS);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) kiiw.setPosition(WORLD_WIDTH/4, (97* --padCounter)+kiiw.RADIUS);
        blockKiiwLeavingTheWorld();
    }


    private void restart() {
        loseMusic.stop();
        music.play();
        padCounter = 2;
        levelTimer = 0;
        secondsTimer = 60;
        minutes=0;
        seconds = 0;
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);
        obstacles.clear();
        coins.clear();
        lifes = 2;
        kiiw.setHit(false);
        coinsCollected = coinsAtBegining;
        if(bossLevel){
            actualSpeed = 15;
        }else{
            actualSpeed = 0;
        }
        nonCollisionTimer = 0;
        updateLifesIndicator();
        finish = false;
        animationTimer = 0;
        looseKiiwX = 20;
        winingKiiwY = 0;
        winingKiiwX = WORLD_WIDTH/4;
        }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles){
            obstacle.update(delta);
            if (actualSpeed<0){
                obstacle.setSpeedPerSecond(350F);
            }else{
                float speed = obstacle.getSpeedPerSecond()+actualSpeed/2;
                obstacle.setSpeedPerSecond(speed);
            }
        }
        checkIfNewObstacleNeeded();
        removeObstaclesIfPassed();
    }

    private void blockKiiwLeavingTheWorld() {
        kiiw.setPosition(kiiw.getX(), MathUtils.clamp(kiiw.getY(),kiiw.getHeigth(),388 + kiiw.RADIUS));
    }

    private void createNewCoin(){
        coinTexture = MenuDemo.getAssetManager().get("defaultLevels/gamecoin.png");
        Coin newCoin = new Coin(coinTexture);
        Random random = new Random();
        int RandomPad1 = random.nextInt(5);
        float pad = PADS[RandomPad1];
        newCoin.setPosition(WORLD_WIDTH+Coin.WIDTH, pad+newCoin.WIDTH/2);
        coins.add(newCoin);
    }

    private void checkIfNewCoinIsNeeded(){
        if(coins.size == 0){
            createNewCoin();
        } else{
            Coin coin = coins.peek();
            if(coin.getX()<WORLD_WIDTH-GAP_BETWEEN_COINS){
                createNewCoin();
            }
        }
    }

    private void removeCoinIfPassed(){
        if(coins.size>0){
            Coin firstCoin = coins.first();
            if (firstCoin.getX() < -Coin.WIDTH){
                coins.removeValue(firstCoin, true);
            }
        }
    }

    private void removeCoinIfCollected(){
        if (coins.size>0){
            if(!kiiw.isHit()){
                for (Coin coin: coins){
                    if (coin.isKiiwColecting(kiiw)){
                        coinsCollected++;
                        coinEffect.play();
                        coins.removeValue(coin, true);
                    }
                }
            }
        }
    }

    private void createNewObstacle() {
        Random rnd = new Random();
        int RandomPad = rnd.nextInt(5);
        int obstacleWidth = 168;
        int obstacleHeight = 107;
        int obstacleType = rnd.nextInt(3);
        boolean isGrass = false;
        switch (obstacleType) {
            case 0:
                obstacleTexture = new Texture(Gdx.files.internal("level" + LEVEL + "/roca.png"));
                if (bossLevel) {
                    obstacleWidth = 150;
                    obstacleHeight = 150;
                }
                break;
            case 1:
                obstacleTexture = new Texture(Gdx.files.internal("level" + LEVEL + "/arbol.png"));
                if (LEVEL == 1) {
                    obstacleWidth = 212;
                    obstacleHeight = 272;
                } else if (LEVEL == 2) {
                    obstacleWidth = 137;
                    obstacleHeight = 265;
                } else if (LEVEL == 3) {
                    obstacleWidth = 482;
                    obstacleHeight = 258;
                } else {
                    obstacleWidth = 150;
                    obstacleHeight = 150;
                }
                break;
            case 2:
                obstacleTexture = new Texture(Gdx.files.internal("level" + LEVEL + "/pasto.png"));
                if (bossLevel) {
                    obstacleWidth = 149;
                    obstacleHeight = 89;
                } else {
                    obstacleWidth = 118;
                    obstacleHeight = 171;
                }
                isGrass = true;
                break;
        }
        Obstacle newObstacle = new Obstacle(bossLevel, isGrass, obstacleTexture, obstacleWidth, obstacleHeight);
        float y = PADS[RandomPad];
        if(LEVEL == 4){
            if(newObstacle.isGrass()){
                newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y+newObstacle.WIDTH/2);
            }else {
                newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y-newObstacle.WIDTH/2);
            }
        }else {
            newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH, y + newObstacle.WIDTH / 2);
        }
        obstacles.add(newObstacle);
    }

    private void checkIfNewObstacleNeeded() {
        if(obstacles.size==0){
            createNewObstacle();
        }else{
            Obstacle obstacle = obstacles.peek();
            if(obstacle.getX()<WORLD_WIDTH-GAP_BETWEEN_OBSTACLES){
                createNewObstacle();
            }
        }
    }

    private void removeObstaclesIfPassed() {
        if(obstacles.size > 0){
            Obstacle firstObstacle = obstacles.first();
            if(firstObstacle.getX() < -Obstacle.WIDTH){
                obstacles.removeValue(firstObstacle, true);
            }
        }
    }

    // returns the colliding obstacle or null if not colliding
    private  Obstacle checkForCollision(){
        if(!kiiw.isHit()){
            for (Obstacle obstacle : obstacles){
                if (obstacle.isKiiwColliding(kiiw)){
                    return obstacle;
                }
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
        stageUI.dispose();
        stagePause.dispose();
    }

}
