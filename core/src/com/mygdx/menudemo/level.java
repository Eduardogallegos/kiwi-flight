package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private static int LEVEL;

    private float[] PADS = {0,97,194,291,388};
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private Stage stage;
    private Game game;
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

    private OrthographicCamera cameraHUD;
    private FitViewport viewportHUD;
    private Stage stageUI;
    private Texture playButtonTexture;
    private Texture speedBarTexture;
    private Texture lifesBarTexture;
    private Texture coinsIndicatorTexture;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Music music;
    //private final AssetManager assetManager = new AssetManager();

    private enum STATE {
        PLAYING, PAUSED
    }
    private STATE state = STATE.PLAYING;

    public level(Game aGame, int level) {
        game = aGame;
        this.LEVEL = level;

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width,height);
        viewportHUD.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));


        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();

        music = MenuDemo.getAssetManager().get("level"+LEVEL+"/song.mp3");
        music.setLooping(true);
        music.play();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        kiiwTexture = MenuDemo.getAssetManager().get("defaultLevels/RunningKiwi.png");
        kiiw = new Kiiw(kiiwTexture);
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);
        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=5;i++){
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
        playButtonTexture = MenuDemo.getAssetManager().get("defaultLevels/pausa.png");
        ImageButton pause = new ImageButton(new TextureRegionDrawable(new TextureRegion(playButtonTexture)), new TextureRegionDrawable(new TextureRegion(playButtonTexture)));
        pause.setPosition(WORLD_WIDTH - pause.getWidth()*1.2f, WORLD_HEIGHT- pause.getHeight()*1.2f);
        pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(state==STATE.PLAYING){
                    state = STATE.PAUSED;
                }else{
                    state = STATE.PLAYING;
                }
            }
        });

        speedBarTexture = MenuDemo.getAssetManager().get("defaultLevels/Barra.png");
        Image speedBar = new Image(speedBarTexture);
        speedBar.setPosition(speedBar.getWidth()/5, WORLD_HEIGHT - speedBar.getHeight()*1.7f);

        lifesBarTexture = MenuDemo.getAssetManager().get("defaultLevels/lifes2.png");
        Image lifesBar = new Image(lifesBarTexture);
        lifesBar.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT-lifesBar.getHeight()*1.7f);

        coinsIndicatorTexture = MenuDemo.getAssetManager().get("defaultLevels/Coins.png");
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(2*WORLD_WIDTH/3, WORLD_HEIGHT-coins.getHeight()*1.7f);

        stageUI.addActor(pause);
        stageUI.addActor(speedBar);
        stageUI.addActor(lifesBar);
        stageUI.addActor(coins);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stageUI);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new GestureHandler()));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        if(state == STATE.PLAYING){
            update(delta);
            stage.act();
            levelTimer+=delta;
            secondsTimer-=delta;
            chechIfTimeFinish();
        }
        clearScreen();
        draw();
    }

    private void chechIfTimeFinish() {
        if (levelTimer>=120){
            game.setScreen(new LevelsScreen(game));
            dispose();
        }
    }

    private void draw(){
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        kiiw.draw(batch);
        drawObstacle();
        drawMinuteTimer();
        drawSecondtimer();
        batch.end();
        stageUI.draw();
        //drawDebug();
        //Gdx.app.log("Debug", String.valueOf(batch.totalRenderCalls));
    }

    private void drawSecondtimer() {
        String secondsAsString = Integer.toString(seconds);
        glyphLayout.setText(bitmapFont, secondsAsString);
        bitmapFont.draw(batch, secondsAsString,80, 7 * viewport.getWorldHeight() / 8);
    }

    private void drawMinuteTimer() {
        String minutesAsString = Integer.toString(minutes);
        glyphLayout.setText(bitmapFont, minutesAsString);
        bitmapFont.draw(batch, minutesAsString + "  : ",50, 7 * viewport.getWorldHeight() / 8);
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
        updateMinuteTimer();
        updateSecondTimer();
        updateLifesIndicator();
        if (checkForCollision()){
            restLife();
            kiiw.setHit(true);
        }
    }

    private void updateLifesIndicator() {
        if (lifes == 1) lifesBarTexture = MenuDemo.getAssetManager().get("defaultLevels/lifes1.png");
        else lifesBarTexture = MenuDemo.getAssetManager().get("defaultLevels/lifes0.png");
    }

    private void checkIfIsGrass() {
        if(kiiw.isHit()){
            for (Obstacle obstacle: obstacles){
                if(obstacle.grass()){
                    sumLife();
                }else{
                    restLife();
                }
            }
        }

    }

    private void restLife() {
        if (lifes<=0)restart();
        else{
            lifes--;
            Gdx.app.log("LOG", String.valueOf(lifes));
        }
    }

    private void sumLife() {
        lifes++;
        Gdx.app.log("LOG", String.valueOf(lifes));
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
                Gdx.app.log("LOG: ", String.valueOf(velocityY));
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
        padCounter = 2;
        levelTimer = 0;
        secondsTimer = 60;
        minutes=0;
        seconds = 0;
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);
        obstacles.clear();
        lifes = 2;

    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles){
            obstacle.update(delta);
            float speed = obstacle.getSpeedPerSecond()+LEVEL*(levelTimer/2);
            obstacle.setSpeedPerSecond(speed);
            //Gdx.app.log("LOG", String.valueOf(speed));
        }
        checkIfNewObstacleNeeded();
        removeObstaclesIfPassed();
    }

    private void blockKiiwLeavingTheWorld() {
        kiiw.setPosition(kiiw.getX(), MathUtils.clamp(kiiw.getY(),kiiw.getHeigth(),388 + kiiw.RADIUS));
    }

    private void createNewObstacle(){
        Random rnd = new Random();
        int RandomPad = rnd.nextInt(5);
        boolean isGrass = rnd.nextBoolean();
        int obstacleWidth;
        int obstacleHeight;
        if (!isGrass){
            boolean rock = rnd.nextBoolean();
            if(rock){
                obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/roca.png"));
                obstacleWidth = 166;
                obstacleHeight = 105;

            }else {
                obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/arbol.png"));
                if(LEVEL==1) {
                    obstacleWidth = 208;
                    obstacleHeight = 270;
                }else if(LEVEL == 2) {
                    obstacleWidth = 135;
                    obstacleHeight = 256;
                }else{
                    obstacleWidth = 480;
                    obstacleHeight=258;
                }
            }
        }else{
            obstacleTexture = new Texture(Gdx.files.internal("level"+LEVEL+"/pasto.png"));
            obstacleWidth = 115;
            obstacleHeight = 165;
        }
        Obstacle newObstacle = new Obstacle(isGrass, obstacleTexture, obstacleWidth, obstacleHeight);
        float y = PADS[RandomPad];
        newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y + newObstacle.WIDTH/2);
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

    private  boolean checkForCollision(){
        if(!kiiw.isHit()){
            for (Obstacle obstacle : obstacles){
                if (obstacle.isKiiwColliding(kiiw)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
        kiiwTexture.dispose();
        playButtonTexture.dispose();
        speedBarTexture.dispose();
        lifesBarTexture.dispose();
        coinsIndicatorTexture.dispose();
        stageUI.dispose();
    }

}
