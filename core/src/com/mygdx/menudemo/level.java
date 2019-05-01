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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private static int LEVEL;
    private static final float DEFAULT_CHUNCK_SIZE = 11 ;

    private float[] PADS = {0,97,194,291,388};
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Texture> lifeTextures = new Array<Texture>();
    private Array<Coin> coins = new Array<Coin>();
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private Stage stage;
    private MenuDemo menuDemo;
    private SpriteBatch batch;
    private Kiiw kiiw;
    private int padCounter = 2;
    private int lifes = 2;
    private Texture kiiwTexture;
    private Texture obstacleTexture;
    private float levelTimer = 0;
    private float secondsTimer = 60;
    private int minutes;
    private int seconds;
    private int coinsCounter = 0;
    private float nonCollisionTimer=0;
    private int actualSpeed = 0;

    private OrthographicCamera cameraHUD;
    private FitViewport viewportHUD;
    private Stage stageUI;
    private Texture playButtonTexture;
    private Texture speedBarTexture;
    private Texture lifesBarTexture;
    private Texture coinsIndicatorTexture;
    private Texture pausePanelTexture;

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
    private Stage stageWin;
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

    private enum STATE {
        PLAYING, PAUSED, GAMEOVER, WIN
    }
    private STATE state = STATE.PLAYING;

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

        music = MenuDemo.getAssetManager().get("level"+LEVEL+"/song.mp3");
        music.setLooping(true);
        music.play();

        hitEffect = MenuDemo.getAssetManager().get("defaultLevels/hit.mp3");

        coinEffect = menuDemo.getAssetManager().get("defaultLevels/coin.mp3");

        loseEffect = menuDemo.getAssetManager().get("defaultLevels/Kiwhine.mp3");
        loseMusic = menuDemo.getAssetManager().get("defaultLevels/KiiwLose.mp3");

        winEffect = menuDemo.getAssetManager().get("defaultLevels/KiiwWin.mp3");

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        if (!bossLevel){
            kiiwTexture = menuDemo.getAssetManager().get("defaultLevels/RunningKiwi.png");

        }else{
            kiiwTexture = menuDemo.getAssetManager().get("level4/FlyingKiwi.png");
        }
        kiiw = new Kiiw(kiiwTexture, bossLevel);
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);

        if(LEVEL==4){
            bgImagesNumber = 3;
            bossLevel = true;
        }
        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=bgImagesNumber;i++){
            textures.add(new Texture(Gdx.files.internal("level"+LEVEL+"/BG"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        ParallaxBackground parallaxBackground = new ParallaxBackground(textures, WORLD_WIDTH, WORLD_HEIGHT);
        parallaxBackground.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        //int parallaxSpeed = 1+(int)(levelTimer);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        cameraHUD = new OrthographicCamera();
        viewportHUD = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,cameraHUD);
        cameraHUD.update();

        stageUI = new Stage(viewportHUD);
        playButtonTexture = menuDemo.getAssetManager().get("defaultLevels/pausa.png");
        ImageButton pause = new ImageButton(new TextureRegionDrawable(new TextureRegion(playButtonTexture)), new TextureRegionDrawable(new TextureRegion(playButtonTexture)));
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
        speedBar.setPosition(30, WORLD_HEIGHT - speedBar.getHeight()*1.1f);
        speedBarChunkSize = 275 / speedNeeded;

        speedBarRectangle = new Rectangle();
        speedBarRectangle.x = 31;
        speedBarRectangle.y = WORLD_HEIGHT - speedBar.getHeight()*1f;
        speedBarRectangle.height = 35;
        speedBarRectangle.width = 0;

        for(int i = 0; i <=2;i++){
            lifeTextures.add((Texture) menuDemo.getAssetManager().get("defaultLevels/lifes"+i+".png"));
        }
        lifesBarTexture = (Texture) lifeTextures.get(lifes);
        Image lifesBar = new Image(lifesBarTexture);
        lifesBar.setPosition(1*WORLD_WIDTH/3+50, WORLD_HEIGHT-lifesBar.getHeight()*1.1f);

        coinsIndicatorTexture = menuDemo.getAssetManager().get("defaultLevels/Coins.png");
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(3*WORLD_WIDTH/5+50, WORLD_HEIGHT-coins.getHeight()-10);

        stageUI.addActor(pause);
        stageUI.addActor(speedBar);
        stageUI.addActor(lifesBar);
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
                //music.stop();
                //menuDemo.setScreen(new SettingsScreen(menuDemo));
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
                menuDemo.setScreen(new StartScreen(menuDemo));
            }
        });

        stageWin = new Stage(viewportHUD);

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
                menuDemo.setScreen(new StartScreen(menuDemo));
            }
        });

        stageGameOver.addActor(gameOverPanel);
        stageGameOver.addActor(retry);
        stageGameOver.addActor(noRetry);

        stagePause.addActor(pausePanel);
        stagePause.addActor(resume);
        stagePause.addActor(settings);
        stagePause.addActor(restart);
        stagePause.addActor(exit);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stageUI);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new GestureHandler()));
        Gdx.input.setInputProcessor(multiplexer);

    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        Gdx.app.log("LOG:","Music volume: " +  musicVolume + "/100");
        effectsVolume = preferencias.getFloat("effectsVolume", EFFECTS_VOLUME_DEFAULT);
        Gdx.app.log("LOG:","Effects volume: " + effectsVolume + "/100");
    }

    @Override
    public void render(float delta) {
        if(state == STATE.PLAYING){
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
        }
        clearScreen();
        updateVolume();
        draw();
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
            dispose();
        }
    }

    private void checkIfTimeFinish() {
        if (levelTimer>=120){
            loseEffect.play();
            loseMusic.play();
            state = STATE.GAMEOVER;
            dispose();
        }
    }

    private void draw(){
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        kiiw.draw(batch);
        drawCoin();
        drawObstacle();
        drawTimerString();
        drawMinuteTimer();
        drawSecondtimer();
        drawCoinsCounter();
        drawNeededSpeedIndicator();
        drawActualSpeedIndicator();
        drawLifeIndicator();
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CORAL);
        shapeRenderer.rect(speedBarRectangle.x,speedBarRectangle.y,speedBarRectangle.width,speedBarRectangle.height);
        shapeRenderer.end();


        stageUI.draw();
        if(state== STATE.PAUSED){
            stagePause.draw();
            Gdx.input.setInputProcessor(stagePause);
        }
        if (state == STATE.GAMEOVER){
            music.stop();
            stageGameOver.draw();
            Gdx.input.setInputProcessor(stageGameOver);

        }
    }

    private void drawLifeIndicator() {
        String lifesAsString = Integer.toString(lifes + 1);
        glyphLayout.setText(bitmapFont, lifesAsString);
        bitmapFont.draw(batch, lifesAsString, WORLD_WIDTH/2, WORLD_HEIGHT-5);
    }

    private void drawActualSpeedIndicator() {
        String actualSpeedAsString = Integer.toString(actualSpeed);
        glyphLayout.setText(bitmapFont, actualSpeedAsString);
        bitmapFont.draw(batch, actualSpeedAsString, 25, WORLD_HEIGHT-speedBarTexture.getHeight()-15);
    }

    private void drawCoin() {
        for(Coin coin:coins){
            coin.draw(batch);
        }
    }

    private void drawTimerString() {
        String timerString = "time left";
        glyphLayout.setText(bitmapFont, timerString);
        bitmapFont.draw(batch, timerString, 25, 7*viewport.getWorldHeight()/8);
    }

    private void drawNeededSpeedIndicator() {
        String speedNeededAsString = Integer.toString(speedNeeded) ;
        glyphLayout.setText(bitmapFont, speedNeededAsString);
        bitmapFont.draw(batch, speedNeededAsString, 30+speedBarTexture.getWidth()+20,WORLD_HEIGHT-5 );
    }

    private void drawCoinsCounter() {
        String coinsAsString = Integer.toString(coinsCounter);
        glyphLayout.setText(bitmapFont, coinsAsString);
        bitmapFont.draw(batch, coinsAsString, 3*WORLD_WIDTH/5+145, WORLD_HEIGHT-coinsIndicatorTexture.getHeight()+50);
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
        bitmapFont.draw(batch, secondsAsString,80, (7 * viewport.getWorldHeight() / 8)-bitmapFont.getCapHeight());
    }

    private void drawMinuteTimer() {
        String minutesAsString = Integer.toString(minutes)+ ":";
        glyphLayout.setText(bitmapFont, minutesAsString);
        bitmapFont.draw(batch, minutesAsString ,30, (7 * viewport.getWorldHeight() / 8)-bitmapFont.getCapHeight());
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
        updateObstacles(delta);
        updateCoins(delta);
        updateMinuteTimer();
        updateSecondTimer();
        updateActualSpeed();
        updateLifesIndicator();

        handleCollisions();

        updateSpeedBarRectangle();
    }

    private void handleCollisions() {

        Obstacle culprit = checkForCollision();

        if (culprit != null){
            if(culprit.isGrass()){
                substractSpeed();
                Gdx.app.log("LOG: ", "Collided with grass!!!");
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
            lifesBarTexture = (Texture) lifeTextures.get(lifes);
    }

    private void substractLife() {
        if(lifes<=0){
            loseEffect.play();
            loseMusic.play();
            state = STATE.GAMEOVER;
        }
        else{
            lifes--;
            hitEffect.play();
            Gdx.app.log("LOG", String.valueOf(lifes));
        }
    }


    private void substractSpeed() {
        if(actualSpeed<=lowSpeedLimit && bossLevel){
            music.stop();
            state = STATE.GAMEOVER;
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
        coinsCounter = 0;
        actualSpeed = 0;
        nonCollisionTimer = 0;
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
                        coinsCounter++;
                        coinEffect.play();
                        coins.removeValue(coin, true);
                    }
                }
            }
        }
    }

    private void createNewObstacle(){
        Random rnd = new Random();
        int RandomPad = rnd.nextInt(5);
        int obstacleWidth=160;
        int obstacleHeight = 105;
        int obstacleType = rnd.nextInt(3);
        boolean isGrass = false;
        switch (obstacleType){
            case 0:
                obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/roca.png"));
                if(bossLevel){
                    obstacleWidth = 170;
                    obstacleHeight = 170;
                }
                break;
            case 1:
                obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/arbol.png"));
                if(LEVEL==1) {
                    obstacleWidth = 208;
                    obstacleHeight = 270;
                }else if(LEVEL == 2) {
                    obstacleWidth = 135;
                    obstacleHeight = 256;
                }else if(LEVEL == 3){
                    obstacleWidth = 480;
                    obstacleHeight=258;
                }else{
                    obstacleWidth = 170;
                    obstacleHeight = 170;
                }
                break;
            case 2:
                obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/pasto.png"));
                if(bossLevel){
                    obstacleWidth = 170;
                    obstacleHeight = 102;
                }else{
                    obstacleWidth = 116;
                    obstacleHeight=170;
                }
                isGrass=true;
                break;
        }
        Obstacle newObstacle = new Obstacle(bossLevel, isGrass, obstacleTexture, obstacleWidth, obstacleHeight);
        float y = PADS[RandomPad];
        if(LEVEL == 4){
            newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y-newObstacle.WIDTH);
        }else{
            newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y + newObstacle.WIDTH/2);
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
