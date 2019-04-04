package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class level extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float GAP_BETWEEN_OBSTACLES = 40f;
    private float[] PADS = {0,97,194,291,388};

    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private Stage stage;
    private Game game;
    private SpriteBatch batch;
    private Kiiw kiiw;
    private int padCounter = 0;
    private int lifes = 3;

    private Texture background;
    private Texture kiiwTexture;

    public GameScreen(Game aGame) {
        game = aGame;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width,height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("BGselva5.png"));
        kiiwTexture = new Texture(Gdx.files.internal("RunningKiwi.png"));
        kiiw = new Kiiw(kiiwTexture);
        kiiw.setPosition(WORLD_WIDTH/4,padCounter);

        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=5;i++){
            textures.add(new Texture(Gdx.files.internal("BGselva"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        ParallaxBackground parallaxBackground = new ParallaxBackground(textures, WORLD_WIDTH, WORLD_HEIGHT);
        parallaxBackground.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        clearScreen();
        update(delta);
        draw();
    }

    private void draw(){
        stage.act();
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        //batch.draw(background,0,0);
        kiiw.draw(batch);
        batch.end();
        drawDebug();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        kiiw.drawDebug(shapeRenderer);
        for (Obstacle obstacle : obstacles){
            obstacle.drawDebug(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta){
        updateKiiw(delta);
        updateObstacles(delta);
        if (checkForCollision()){
            restLife();
        }
    }

    private void updateKiiw(float delta) {
        kiiw.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) kiiw.setPosition(WORLD_WIDTH/4, 97* ++padCounter);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) kiiw.setPosition(WORLD_WIDTH/4, 97* --padCounter);
        blockKiiwLeavingTheWorld();
    }

    private void restLife() {
        if (lifes<=0)restart();
        else lifes--;
    }

    private void restart() {
        padCounter = 0;
        kiiw.setPosition(WORLD_WIDTH/4,padCounter);
        obstacles.clear();
        lifes = 3;

    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles){
            obstacle.update(delta);
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
        Obstacle newObstacle = new Obstacle();
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
        for (Obstacle obstacle : obstacles){
            if (obstacle.isKiiwColliding(kiiw)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
