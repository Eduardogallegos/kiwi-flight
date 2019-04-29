package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class TransitionScreen extends ScreenAdapter {
   private final MenuDemo menuDemo;
   private static final float WORLD_WIDTH=1280;
   private static final float WORLD_HEIGHT=720;

   private Stage stage;
   private Texture backgroundTexture;
   private Texture logoTexture;

   public TransitionScreen(MenuDemo menuDemo){
       this.menuDemo =menuDemo;
   }

   public void show(){
       super.show();
       stage=new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
       Gdx.input.setInputProcessor(stage);

       backgroundTexture= new Texture(Gdx.files.internal("loading/Background1.png"));
       logoTexture= new Texture(Gdx.files.internal("loading/Logo.png"));

       Image background = new Image(backgroundTexture);
       Image logo = new Image(logoTexture);
       stage.addActor(background);
       stage.addActor(logo);
       logo.getColor().a = 0f;

       background.addAction(sequence(delay(2), fadeOut(2)));

       logo.addAction(sequence(delay(4), fadeIn(1),delay(2), fadeOut(1), run(new Runnable() {
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
        backgroundTexture.dispose();
        logoTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
