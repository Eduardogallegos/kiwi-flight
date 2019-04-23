package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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

public class bossLevel extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float GAP_BETWEEN_OBSTACLES = 80f;

    private float[] PADS = {0,97,194,291,388};
    private Array<Nube> nubes = new Array<Nube>();
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
    private Texture nubeTexture;
    private float levelTimer = 0;

    private OrthographicCamera cameraHUD;
    private FitViewport viewportHUD;
    private Stage stageUI;
    private Texture playButtonTexture;
    private Texture speedBarTexture;
    private Texture livesBarTexture;
    private Texture coinsIndicatorTexture;

    private Music music;

    private enum STATE {
        PLAYING, PAUSED
    }
    private STATE state = STATE.PLAYING;

    public bossLevel(Game Game) {
        game = Game;
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

        music = Gdx.audio.newMusic(Gdx.files.internal("levelBoss/song.mp3"));
        music.setLooping(true);
        music.play();

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        kiiwTexture = new Texture(Gdx.files.internal("defaultLevels/RunningKiwi.png"));
        kiiw = new Kiiw(kiiwTexture);
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);
        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=3;i++){
            textures.add(new Texture(Gdx.files.internal("levelBoss/BG"+i+".png")));
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
        playButtonTexture = new Texture(Gdx.files.internal("defaultLevels/pausa.png"));
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
            };
        });

        speedBarTexture = new Texture(Gdx.files.internal("defaultLevels/Barra.png"));
        Image speedBar = new Image(speedBarTexture);
        speedBar.setPosition(speedBar.getWidth()/5, WORLD_HEIGHT - speedBar.getHeight()*1.7f);

        livesBarTexture = new Texture(Gdx.files.internal("defaultLevels/Lives.png"));
        Image livesBar = new Image(livesBarTexture);
        livesBar.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT-livesBar.getHeight()*1.7f);

        coinsIndicatorTexture = new Texture(Gdx.files.internal("defaultLevels/Coins.png"));
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(2*WORLD_WIDTH/3, WORLD_HEIGHT-coins.getHeight()*1.7f);

        stageUI.addActor(pause);
        stageUI.addActor(speedBar);
        stageUI.addActor(livesBar);
        stageUI.addActor(coins);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stageUI);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(new GestureHandler()));
        Gdx.input.setInputProcessor(multiplexer);
        /*Gdx.input.setInputProcessor(stageUI);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(new GestureDetector(new GestureHandler()));
    */
    }

    @Override
    public void render(float delta) {
        if(state == STATE.PLAYING){
            update(delta);
            stage.act();
        }
        levelTimer+=delta;
        clearScreen();
        draw();
        chechIfTimeFinish();
    }

    private void chechIfTimeFinish() {
        if (levelTimer>=120){
            game.setScreen(new LevelsScreen(game));
        }
    }

    private void draw(){
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        kiiw.draw(batch);
        drawObstacle();
        batch.end();

        stageUI.draw();
        //drawDebug();
        //Gdx.app.log("Debug", String.valueOf(batch.totalRenderCalls));
    }

    private void drawObstacle() {
        for(Nube nube : nubes){
            nube.draw(batch);
        }
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        kiiw.drawDebug(shapeRenderer);
        for (Nube nube: nubes){
            nube.drawDebug(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta){

        updateKiiw(delta);
        updateNube(delta);
        if (checkForCollision()){
            restLife();
            kiiw.setHit(true);
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

    private void restLife() {
        if (lifes<=0)restart();
        else{
            lifes--;
            //Gdx.app.log("LOG", String.valueOf(lifes));
        }
    }

    private void restart() {
        padCounter = 2;
        levelTimer = 0;
        kiiw.setPosition(WORLD_WIDTH/4,97*padCounter+kiiw.RADIUS);
        nubes.clear();
        lifes = 3;

    }

    private void updateNube(float delta) {
        for (Nube nube: nubes){
            nube.update(delta);
            float speed = nube.getSpeedPerSecond()+(levelTimer/2);
            nube.setSpeedPerSecond(speed);
            Gdx.app.log("LOG", String.valueOf(speed));
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
        boolean isRayo = rnd.nextBoolean();
        int obstacleWidth;
        int obstacleHeight;
        if (!isRayo){
            nubeTexture = new Texture(Gdx.files.internal("levelBoss/blanca.png"));
            obstacleWidth = 166;
            obstacleHeight = 105;
        }else{
            nubeTexture = new Texture(Gdx.files.internal("levelBoss/rayo.png"));
            obstacleWidth = 115;
            obstacleHeight = 165;
        }
        Nube newNube = new Nube(isRayo, nubeTexture, obstacleWidth, obstacleHeight);
        float y = PADS[RandomPad];
        newNube.setPosition(WORLD_WIDTH + Obstacle.WIDTH,  y + newNube.WIDTH/2);
        nubes.add(newNube);
    }

    private void checkIfNewObstacleNeeded() {
        if(nubes.size==0){
            createNewObstacle();
        }else{
            Nube nube = nubes.peek();
            if(nube.getX()<WORLD_WIDTH-GAP_BETWEEN_OBSTACLES){
                createNewObstacle();
            }
        }
    }

    private void removeObstaclesIfPassed() {
        if(nubes.size > 0){
            Nube firstNube = nubes.first();
            if(firstNube.getX() < -Obstacle.WIDTH){
                nubes.removeValue(firstNube, true);
            }
        }
    }

    private  boolean checkForCollision(){
        if(!kiiw.isHit()){
            for (Nube nube: nubes){
                if (nube.isKiiwColliding(kiiw)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

