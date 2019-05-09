package com.mygdx.kiwiFlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
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

public class TransitionScreen extends ScreenAdapter {
   private final kiwiFlight kiwiFlight;
   private static final float WORLD_WIDTH=1280;
   private static final float WORLD_HEIGHT=720;
   private static final float MUSIC_VOLUME_DEFAULT = 1;

   private Stage stage;
   private Texture backgroundTexture;
   private Texture logoTexture;
    private Texture story1Texture;
    private Texture story2Texture;
    private Texture story3Texture;
    private Texture story4Texture;
    private Texture story5Texture;
    private Texture story6Texture;
   private Texture story7Texture;
   private Texture buttonTexture;
    private Texture buttonPressTexture;

    private Music music;
    private float musicVolume;
    private Preferences preferencias;


   public TransitionScreen(kiwiFlight kiwiFlight){
       this.kiwiFlight = kiwiFlight;
       this.preferencias = kiwiFlight.getPreferences();
   }

   public void show(){
       super.show();
       loadPreferences();
       stage=new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
       Gdx.input.setInputProcessor(stage);

       backgroundTexture= new Texture(Gdx.files.internal("loading/Background1.png"));
       logoTexture= new Texture(Gdx.files.internal("loading/Logo.png"));
       story1Texture= new Texture(Gdx.files.internal("loading/1.png"));
       story2Texture= new Texture(Gdx.files.internal("loading/2.png"));
       story3Texture= new Texture(Gdx.files.internal("loading/3.png"));
       story4Texture= new Texture(Gdx.files.internal("loading/4.png"));
       story5Texture= new Texture(Gdx.files.internal("loading/5.png"));
       story6Texture= new Texture(Gdx.files.internal("loading/6.png"));
       story7Texture= new Texture(Gdx.files.internal("loading/7.png"));
       buttonTexture=new Texture(Gdx.files.internal("loading/skip.png"));
       buttonPressTexture=new Texture(Gdx.files.internal("loading/skipPress.png"));

       music = Gdx.audio.newMusic(Gdx.files.internal("loading/intro.mp3"));
       updateVolume();
       music.play();

       ImageButton skipButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)), new TextureRegionDrawable(new TextureRegion(buttonPressTexture)));
       skipButton.setPosition(1020,35);
       skipButton.addListener(new ActorGestureListener() {
           @Override
           public void tap(InputEvent event, float x, float y, int count, int button) {
               super.tap(event, x, y, count, button);
               music.stop();
               kiwiFlight.setScreen(new StartScreen(kiwiFlight));
               dispose();
           }
       });

       Image background = new Image(backgroundTexture);
       Image logo = new Image(logoTexture);
       Image story1= new Image(story1Texture);
       Image story2= new Image(story2Texture);
       Image story3= new Image(story3Texture);
       Image story4= new Image(story4Texture);
       Image story5= new Image(story5Texture);
       Image story6= new Image(story6Texture);
       Image story7= new Image(story7Texture);

       stage.addActor(background);
       stage.addActor(logo);
       stage.addActor(story1);
       stage.addActor(story2);
       stage.addActor(story3);
       stage.addActor(story4);
       stage.addActor(story5);
       stage.addActor(story6);
       stage.addActor(story7);
       stage.addActor(skipButton);
       logo.getColor().a = 0f;
       story1.getColor().a=0f;
       story2.getColor().a=0f;
       story3.getColor().a=0f;
       story4.getColor().a=0f;
       story5.getColor().a=0f;
       story6.getColor().a=0f;
       story7.getColor().a=0f;
       skipButton.getColor().a=0f;

       background.addAction(sequence(delay(2), fadeOut(2)));

       logo.addAction(sequence(delay(4), fadeIn(1),delay(2), fadeOut(1)));

       story1.addAction(sequence(delay(8), fadeIn(0),delay(3), fadeOut(0)));
       story2.addAction(sequence(delay(11), fadeIn(0),delay(4), fadeOut(0)));
       story3.addAction(sequence(delay(15), fadeIn(0),delay(3), fadeOut(0)));
       story4.addAction(sequence(delay(18), fadeIn(0),delay(2), fadeOut(0)));
       story5.addAction(sequence(delay(20), fadeIn(0),delay(2), fadeOut(0)));
       story6.addAction(sequence(delay(22), fadeIn(1),delay(3), fadeOut(0)));
       skipButton.addAction(sequence(delay(8), fadeIn(0),delay(23), fadeOut(1)));
       story7.addAction(sequence(delay(26), fadeIn(0),delay(5), fadeOut(1), run(new Runnable() {
           @Override
           public void run() {
               music.stop();
               kiwiFlight.setScreen(new StartScreen(kiwiFlight));
               dispose();
           }
       })));
   }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
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
        story1Texture.dispose();
        story2Texture.dispose();
        story3Texture.dispose();
        story4Texture.dispose();
        story5Texture.dispose();
        story6Texture.dispose();
        story7Texture.dispose();
        music.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
