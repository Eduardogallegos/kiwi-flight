package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class level extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final int UP = 0;
    private static final int DOWN = 1;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private Kiiw kiiw = new Kiiw();
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private static final float GAP_BETWEEN_OBSTACLES = 200F;
    private int score = 0;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    private final Game game;
    private enum STATE{
        PLAYING, PAUSE
    }
    private STATE state = STATE.PLAYING;

    public level (Game game){
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        kiiw.setPosition(WORLD_WIDTH/4,WORLD_HEIGHT/2);
    }

    @Override
    public void render(float delta) {
        switch (state){
            case PLAYING:{
                queryInput();
                update(delta);

            }
            break;
            case PAUSE:{

            }
            break;
        }
        draw();
        drawDebug();
        clearSreen();
    }


    private void queryInput() {
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if(uPressed) moveKiiw(UP);
        if(dPressed) moveKiiw(DOWN);
    }

    //Me quedé aqui, falta dibujar formas en general
    //Implementar restart, stage para botón pausa

    private void draw() {
        batch.totalRenderCalls = 0;
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        //batch.draw(background,0,0);
        //drawObstacles();
        //kiiw.draw(batch);
        //drawSpeed();
        //drawSpeedBar();

        batch.end();
        drawDebug();
        Gdx.app.log("Debug", String.valueOf(batch.totalRenderCalls));
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        kiiw.drawDebug(shapeRenderer);
        shapeRenderer.end();

    }

    /*private void drawObstacles() {

    }*/

    private void update(float delta) {
        updateKiiw(delta);
        updateObstacles(delta);
        kiiw.update(delta);
        if(checkForCollision()){
            restart();
        }
    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles){
            obstacle.update(delta);
        }
        checkIfNewObstacleNeeded();
        removeObstaclesIfPassed();
    }

    private void removeObstaclesIfPassed() {
        if(obstacles.size > 0){
            Obstacle firstObstacle = obstacles.first();
            if(firstObstacle.getX() < -Obstacle.WIDTH){
                obstacles.removeValue(firstObstacle, true);
            }
        }
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

    private void createNewObstacle() {
        Obstacle newObstacle = new Obstacle();
        newObstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH);
        obstacles.add(newObstacle);
    }

    private void updateKiiw(float delta) {
        kiiw.update(delta);
        queryInput();
    }

    private void moveKiiw(int newKiiwDirection) {
        switch (newKiiwDirection){
            case UP:{
                kiiw.changePadUp();
            }
            break;
            case DOWN:{
                kiiw.changePadDown();
            }
            break;
        }
    }

    private void clearSreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    private void restart() {
        kiiw.setPosition(WORLD_WIDTH/5, .26f);
        obstacles.clear();
    }

    private boolean checkForCollision() {
        for(Obstacle obstacle : obstacles){
            if(obstacle.isKiiwColliding(kiiw)){
                return true;
            }
        }
        return false;
    }



}
