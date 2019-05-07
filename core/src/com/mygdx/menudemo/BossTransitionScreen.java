package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class BossTransitionScreen extends ScreenAdapter {
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH=1280;
    private static final float WORLD_HEIGHT=720;

    private Stage stage;
    private Texture story8Texture;
    private Texture story9Texture;
    private Texture story10Texture;

    private Texture buttonTexture;
    private Texture buttonPressTexture;


    public BossTransitionScreen(MenuDemo menuDemo){
        this.menuDemo =menuDemo;
    }

    public void show(){
        super.show();
        stage=new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        story8Texture= new Texture(Gdx.files.internal("loading/8.png"));
        story9Texture= new Texture(Gdx.files.internal("loading/9.png"));
        story10Texture= new Texture(Gdx.files.internal("loading/10.png"));
        buttonTexture=new Texture(Gdx.files.internal("loading/skip.png"));
        buttonPressTexture=new Texture(Gdx.files.internal("loading/skipPress.png"));

        ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
        skipButton.setPosition(1020,35);
        skipButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new LoadingScreen( menuDemo,4));
                dispose();
            }
        });

        Image story8 = new Image(story8Texture);
        Image story9 = new Image(story9Texture);
        Image story10= new Image(story10Texture);


        stage.addActor(story8);
        stage.addActor(story9);
        stage.addActor(story10);

        stage.addActor(skipButton);
        story8.getColor().a = 0f;
        story9.getColor().a = 0f;
        story10.getColor().a=0f;


        story8.addAction(sequence(delay(0), fadeIn(1),delay(2), fadeOut(0)));

        story9.addAction(sequence(delay(3), fadeIn(0),delay(2), fadeOut(1)));

        story10.addAction(sequence(delay(5), fadeIn(1),delay(4), fadeOut(1), run(new Runnable() {
            @Override
            public void run() {
                menuDemo.setScreen(new LoadingScreen( menuDemo,4));
                dispose();
            }
        })));
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
        story8Texture.dispose();
        story9Texture.dispose();
        story10Texture.dispose();
        buttonPressTexture.dispose();
        buttonTexture.dispose();

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
