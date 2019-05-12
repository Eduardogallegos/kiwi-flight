package mx.tec.kiwiFlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class StartScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private final kiwiFlight kiwiFlight;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;

    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture playTexture;
    private Texture playPressTexture;
    private Texture settingsTexture;
    private Texture settingsPressTexture;
    private Texture creditsPressTexture;
    private Texture creditsTexture;
    private Texture shopTexture;
    private Texture shopPressTexture;
    private Texture closetPressTexture;
    private Texture closetTexture;

    private Table table;
    private Texture kiwiTexture;
    private Music music;
    private Preferences preferencias;
    private float musicVolume;
    private Texture quitPanelTexture;
    private Texture yesQuitTexture;
    private Texture yesQuitPressTexture;
    private Texture noQuitTexture;
    private Texture noQuitPressTexture;
    private Stage quitStage;

    private enum STATE {
        NORMAL, QUIT
    }

    private STATE state = STATE.NORMAL;

    public StartScreen(kiwiFlight kiwiFlight) {
        this.kiwiFlight = kiwiFlight;
        this.preferencias = kiwiFlight.getPreferences();
    }

    @Override
    public void show() {
        loadPreferences();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        music = Gdx.audio.newMusic(Gdx.files.internal("principal/song.mp3"));
        updateVolume();
        music.setLooping(true);
        music.play();

        backgroundTexture = new Texture(Gdx.files.internal("principal/MenuBackground.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        titleTexture = new Texture(Gdx.files.internal("principal/MenuLogo.png"));
        Image title = new Image(titleTexture);

        kiwiTexture= new Texture(Gdx.files.internal("principal/kiwi.png"));
        Image kiwi = new Image(kiwiTexture);

        playTexture = new Texture(Gdx.files.internal("principal/play.png"));
        playPressTexture = new Texture(Gdx.files.internal("principal/playPress.png"));
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                kiwiFlight.setScreen(new LevelsScreen(kiwiFlight));
                dispose();
            }
        });

        settingsTexture = new Texture(Gdx.files.internal("principal/settings.png"));
        settingsPressTexture = new Texture(Gdx.files.internal("principal/settingsPress.png"));
        ImageButton settings = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsTexture)), new TextureRegionDrawable(new TextureRegion(settingsPressTexture)));
        settings.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                kiwiFlight.setScreen(new SettingsScreen(kiwiFlight));
                dispose();
            }
        });

        creditsTexture = new Texture(Gdx.files.internal("principal/credits.png"));
        creditsPressTexture = new Texture(Gdx.files.internal("principal/creditsPress.png"));
        ImageButton credits = new ImageButton(new TextureRegionDrawable(new TextureRegion(creditsTexture)), new TextureRegionDrawable(new TextureRegion(creditsPressTexture)));
        credits.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                kiwiFlight.setScreen(new CreditsScreen(kiwiFlight));
                dispose();
            }
        });

        closetTexture = new Texture(Gdx.files.internal("principal/closet.png"));
        closetPressTexture = new Texture(Gdx.files.internal("principal/closetPress.png"));
        ImageButton closet = new ImageButton(new TextureRegionDrawable(new TextureRegion(closetTexture)), new TextureRegionDrawable(new TextureRegion(closetPressTexture)));
        closet.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                kiwiFlight.setScreen(new ClosetScreen(kiwiFlight));
                dispose();
            }
        });

        shopTexture = new Texture(Gdx.files.internal("principal/shop.png"));
        shopPressTexture = new Texture(Gdx.files.internal("principal/shopPress.png"));
        ImageButton shop = new ImageButton(new TextureRegionDrawable(new TextureRegion(shopTexture)), new TextureRegionDrawable(new TextureRegion(shopPressTexture)));
        shop.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                kiwiFlight.setScreen(new ShopScreen(kiwiFlight));
                dispose();
            }
        });

        quitStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        quitPanelTexture = new Texture(Gdx.files.internal("back/quitpanel.png"));
        Image quitPanel = new Image(quitPanelTexture);

        yesQuitTexture = new Texture(Gdx.files.internal("back/yes.png"));
        yesQuitPressTexture = new Texture(Gdx.files.internal("back/yesPress.png"));
        ImageButton yesQuit = new ImageButton(new TextureRegionDrawable(new TextureRegion(yesQuitTexture)), new TextureRegionDrawable(new TextureRegion(yesQuitPressTexture)));
        yesQuit.setPosition(WORLD_WIDTH/3+20, WORLD_HEIGHT/3-20);
        yesQuit.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                dispose();
                Gdx.app.exit();
            }
        });

        noQuitTexture = new Texture(Gdx.files.internal("back/no.png"));
        noQuitPressTexture = new Texture(Gdx.files.internal("back/noPress.png"));
        ImageButton noQuit = new ImageButton(new TextureRegionDrawable(new TextureRegion(noQuitTexture)), new TextureRegionDrawable(new TextureRegion(noQuitPressTexture)));
        noQuit.setPosition(2*WORLD_WIDTH/3-170, WORLD_HEIGHT/3-20);
        noQuit.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        quitStage.addActor(quitPanel);
        quitStage.addActor(yesQuit);
        quitStage.addActor(noQuit);

        table = new Table();
        //table.debug(); //Enables debug

        // ROW 1
        table.row();
        table.add(title).colspan(4).padBottom(30F);
        // ROW 2
        table.row();
        table.add(settings);

        table.add(kiwi).colspan(2);

        table.add(shop);

        // ROW 3
        table.row();
        table.add(credits);

        table.add(play).colspan(2);

        table.add(closet);
        table.setFillParent(true);
        table.pack();
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        stage.act(delta);
        stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            state = STATE.QUIT;
        }
        if(state == STATE.QUIT){
            quitStage.draw();
            Gdx.input.setInputProcessor(quitStage);
        }else{
            Gdx.input.setInputProcessor(stage);
        }
        updateVolume();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        playPressTexture.dispose();
        titleTexture.dispose();
        closetTexture.dispose();
        closetPressTexture.dispose();
        creditsTexture.dispose();
        creditsPressTexture.dispose();
        shopTexture.dispose();
        shopPressTexture.dispose();
        settingsTexture.dispose();
        settingsPressTexture.dispose();
        music.dispose();
        yesQuitPressTexture.dispose();
        yesQuitTexture.dispose();
        noQuitPressTexture.dispose();
        noQuitTexture.dispose();
        quitStage.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
