package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class ShopScreen extends ScreenAdapter {

    private final Game game;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;
    private Texture backgroundTexture;
    //private Texture titleTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Table table;
    private Texture codeTexture;
    private Texture codePressTexture;

    public ShopScreen(Game game) {
        this.game=game;
    }

    public void show() {
        super.show();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("shop/fondo.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);


        returnTexture = new Texture(Gdx.files.internal("shop/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("shop/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new StartScreen(game));
                dispose();
            }
        });

        codeTexture = new Texture(Gdx.files.internal("shop/code.png"));
        codePressTexture = new Texture(Gdx.files.internal("shop/codePress.png"));
        ImageButton code = new ImageButton(new TextureRegionDrawable(new TextureRegion(codeTexture)), new TextureRegionDrawable(new TextureRegion(codePressTexture)));
        code.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new StartScreen(game));
                dispose();
            }
        });

        table = new Table();
        table.pad(20);
        //table.setDebug(true);

        //table.add(title).padBottom(100).expand().top().colspan(3);

        table.row();
        table.add(code).bottom().expand();

        table.row();
        table.add(retur).bottom().left();

        table.setFillParent(true);
        table.pack();
        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        backgroundTexture.dispose();
        returnTexture.dispose();
        returnPressTexture.dispose();
        codeTexture.dispose();
        codePressTexture.dispose();
        //titleTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}