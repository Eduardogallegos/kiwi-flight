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

public class EndingTransitionScreen extends ScreenAdapter {
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH=1280;
    private static final float WORLD_HEIGHT=720;

    private Stage stage;

    private Texture story11Texture;
    private Texture endingTexture;

    private Texture buttonTexture;
    private Texture buttonPressTexture;


    public EndingTransitionScreen(MenuDemo menuDemo){
        this.menuDemo =menuDemo;
    }

    public void show(){
        super.show();
        stage=new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        story11Texture= new Texture(Gdx.files.internal("loading/11.png"));
        endingTexture= new Texture(Gdx.files.internal("loading/ending.png"));

        buttonTexture=new Texture(Gdx.files.internal("loading/skip.png"));
        buttonPressTexture=new Texture(Gdx.files.internal("loading/skipPress.png"));

        ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
        skipButton.setPosition(1020,35);
        skipButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new StartScreen( menuDemo));
                dispose();
            }
        });

        Image story11= new Image(story11Texture);
        Image ending= new Image(endingTexture);


        stage.addActor(story11);
        stage.addActor(ending);

        stage.addActor(skipButton);

        story11.getColor().a=0f;
        ending.getColor().a=0f;



        story11.addAction(sequence(delay(0), fadeIn(2),delay(4), fadeOut(2)));

        ending.addAction(sequence(delay(7), fadeIn(2),delay(2), fadeOut(1), run(new Runnable() {
            @Override
            public void run() {
                menuDemo.setScreen(new StartScreen(menuDemo));
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
        story11Texture.dispose();
        endingTexture.dispose();
        buttonPressTexture.dispose();
        buttonTexture.dispose();

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}