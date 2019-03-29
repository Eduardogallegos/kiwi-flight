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
    private Kiiw kiiw;
    private static final float GAP_BETWEEN_FLOWERS = 200F;
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
        clearSreen();
    }

    private void queryInput() {
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if(uPressed) moveKiiw(UP);
        if(dPressed) moveKiiw(DOWN);
    }

    //Me quedé aqui, falta dibujar formas en general, método para dibujar obstaculos
    //Implementar restart, stage para botón pausa

    private void draw() {
        batch.totalRenderCalls = 0;
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        //batch.draw(background,0,0);
        drawObstacles();
        kiiw.draw(batch);
        //drawSpeed();
        //drawSpeedBar();
        batch.end();
        //drawDebug();
        Gdx.app.log("Debug", String.valueOf(batch.totalRenderCalls));
    }

    private void drawObstacles() {

    }

    private void update(float delta) {
        updateKiiw(delta);
        updateObstacles(delta);
        kiiw.update(delta);
        if(checkForCollision()){
            restart();
        }
    }

    private void updateObstacles(float delta) {

    }

    private void updateKiiw(float delta) {
        kiiw.update(delta);
        if (Gdx.input.isTouched()){

        }
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

    }

    private boolean checkForCollision() {
        return false;
    }



}
