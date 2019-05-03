package com.mygdx.menudemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class ShopScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static  final int COINS_DEFAULT = 0;
    private static final boolean SKIN_BOUGHT = false;
    private final MenuDemo menuDemo;
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private Stage stage;
    private Camera camera;
    private Texture backgroundTexture;
    private Texture returnTexture;
    private Texture returnPressTexture;
    private Texture codeTexture;
    private Texture codePressTexture;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    private SpriteBatch batch;

    private Music music;
    private Preferences preferencias;
    private float musicVolume;
    private Texture coinsIndicatorTexture;
    private int coinsCollected = 0;
    private Texture crownSkinButtonTexture;
    private Texture crownSkinButtonPressTexture;
    private Texture tieSkinButtonPressTexture;
    private Texture tieSkinButtonTexture;
    //private Texture hulkSkinButtonPressTexture;
    //private Texture hulkSkinButtonTexture;
    private Texture hatSkinButtonPressTexture;
    private Texture hatSkinButtonTexture;
    private Texture partySkinButtonTexture;
    private Texture partySkinButtonPressTexture;
    private FitViewport viewport;
    private Stage stageBuy;
    private Texture buyPanelTexture;
    private Texture yesButtonTexture;
    private Texture yesPressedButtonTexture;
    private Texture noButtonTexture;
    private Texture noPressedButtonTexture;
    private int costo;
    private boolean partyBought;
    private boolean hatBought;
    private boolean tieBought;
    private boolean crownBought;
    private boolean hulkBought;
    private boolean ricardoBought;
    private Stage stageNoCoins;
    private Texture noCoinsPanelTexture;
    private Texture okButtonTexture;
    private Texture okButtonPressedTexture;
    private Stage stageCode;
    private Texture codePanelTexture;
    private Texture crossButtonTexture;
    private Texture crossButtonPressedTexture;
    private Texture submitButtonTexture;
    private Texture submitButtonPressedTexture;
    private Texture whiteSpaceTexture;

    private enum STATE{
        NORMAL, RUSURE, NOCOINS, CODE
    }

    private STATE state = STATE.NORMAL;

    public ShopScreen(MenuDemo menuDemo) {
        this.menuDemo =menuDemo;
        this.preferencias = menuDemo.getPreferences();
    }

    public void show() {
        loadPreferences();
        super.show();


        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);


        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        music = Gdx.audio.newMusic(Gdx.files.internal("shop/song.mp3"));
        music.setLooping(true);
        music.play();

        bitmapFont = new BitmapFont(Gdx.files.internal("defaultLevels/numbers.fnt"));
        glyphLayout = new GlyphLayout();
        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("shop/fondo.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        coinsIndicatorTexture = new Texture(Gdx.files.internal("defaultLevels/Coins.png"));
        Image coins = new Image(coinsIndicatorTexture);
        coins.setPosition(WORLD_WIDTH/6, 30);

        returnTexture = new Texture(Gdx.files.internal("shop/return.png"));
        returnPressTexture = new Texture(Gdx.files.internal("shop/returnPress.png"));
        ImageButton retur = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)), new TextureRegionDrawable(new TextureRegion(returnPressTexture)));
        retur.setPosition(20, 20);
        retur.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                music.stop();
                super.tap(event, x, y, count, button);
                menuDemo.setScreen(new StartScreen(menuDemo));
                dispose();
            }
        });

        codeTexture = new Texture(Gdx.files.internal("shop/code.png"));
        codePressTexture = new Texture(Gdx.files.internal("shop/codePress.png"));
        ImageButton code = new ImageButton(new TextureRegionDrawable(new TextureRegion(codeTexture)), new TextureRegionDrawable(new TextureRegion(codePressTexture)));
        code.setPosition(200, 150);
        code.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.CODE;
            }
        });

        if(partyBought){
            partySkinButtonTexture = new Texture(Gdx.files.internal("shop/partyKiwiSold.png"));
            partySkinButtonPressTexture = new Texture(Gdx.files.internal("shop/partyKiwiSold.png"));
            ImageButton party = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinButtonPressTexture)));
            party.setPosition(WORLD_WIDTH / 2 + 100, WORLD_HEIGHT / 2);
            stage.addActor(party);
        }else {
            partySkinButtonTexture = new Texture(Gdx.files.internal("shop/partyKiwi.png"));
            partySkinButtonPressTexture = new Texture(Gdx.files.internal("shop/partyKiwiPress.png"));
            ImageButton party = new ImageButton(new TextureRegionDrawable(new TextureRegion(partySkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(partySkinButtonPressTexture)));
            party.setPosition(WORLD_WIDTH / 2 + 100, WORLD_HEIGHT / 2 + 10);
            party.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    costo = 30;
                    checkIfCanAfford(costo);
                }
            });
            stage.addActor(party);
        }

        if(hatBought){
            hatSkinButtonTexture = new Texture(Gdx.files.internal("shop/hatKiwiSold.png"));
            hatSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/hatKiwiSold.png"));
            ImageButton hat = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonPressTexture)));
            hat.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 + 10);
            stage.addActor(hat);
        }else {
            hatSkinButtonTexture = new Texture(Gdx.files.internal("shop/hatKiwi.png"));
            hatSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/hatKiwiPress.png"));
            ImageButton hat = new ImageButton(new TextureRegionDrawable(new TextureRegion(hatSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hatSkinButtonPressTexture)));
            hat.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 + 10);
            hat.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    costo = 35;
                    checkIfCanAfford(costo);
                }
            });
            stage.addActor(hat);
        }

        if(tieBought){
            tieSkinButtonTexture = new Texture(Gdx.files.internal("shop/tieKiwiSold.png"));
            tieSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/tieKiwiSold.png"));
            ImageButton tie = new ImageButton(new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(tieSkinButtonPressTexture)));
            tie.setPosition(WORLD_WIDTH / 2 + 100, WORLD_HEIGHT / 2 - 180);
            stage.addActor(tie);
        }else {
            tieSkinButtonTexture = new Texture(Gdx.files.internal("shop/tieKiwi.png"));
            tieSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/tieKiwiPress.png"));
            ImageButton tie = new ImageButton(new TextureRegionDrawable(new TextureRegion(tieSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(tieSkinButtonPressTexture)));
            tie.setPosition(WORLD_WIDTH / 2 + 100, WORLD_HEIGHT / 2 - 180);
            tie.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    costo = 40;
                    checkIfCanAfford(costo);
                }
            });
            stage.addActor(tie);
        }

        if (crownBought){
            crownSkinButtonTexture = new Texture(Gdx.files.internal("shop/crownKiwiSold.png"));
            crownSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/crownKiwiSold.png"));
            ImageButton crown = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressTexture)));
            crown.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 - 180);
            stage.addActor(crown);
        }else {
            crownSkinButtonTexture = new Texture(Gdx.files.internal("shop/crownKiwi.png"));
            crownSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/crownKiwiPress.png"));
            ImageButton crown = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressTexture)));
            crown.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 - 180);
            crown.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    costo = 50;
                    checkIfCanAfford(costo);
                }
            });
            stage.addActor(crown);
        }
        /*
        if(hulkBought){

        }else{
        hulkSkinButtonTexture = new Texture(Gdx.files.internal("shop/hulkKiwi.png"));
        hulkSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/hulkKiwiPress.png"));
        ImageButton hulk = new ImageButton(new TextureRegionDrawable(new TextureRegion(hulkSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(hulkSkinButtonPressTexture)));
        hulk.setPosition(WORLD_WIDTH/2+350, WORLD_HEIGHT/2-200);
        }
        crownSkinButtonTexture = new Texture(Gdx.files.internal("shop/crownKiwi.png"));
        crownSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/crownKiwiPress.png"));
        ImageButton crown = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressTexture)));
        crown.setPosition(WORLD_WIDTH/2+50, WORLD_HEIGHT/2+30);
        */

        stageBuy = new Stage(viewport);
        buyPanelTexture = new Texture(Gdx.files.internal("shop/buy.png"));
        Image buyPanel = new Image(buyPanelTexture);

        yesButtonTexture = new Texture(Gdx.files.internal("shop/yes.png"));
        yesPressedButtonTexture = new Texture(Gdx.files.internal("shop/yesPress.png"));
        ImageButton yesBuy = new ImageButton(new TextureRegionDrawable(new TextureRegion(yesButtonTexture)), new TextureRegionDrawable(new TextureRegion(yesPressedButtonTexture)));
        yesBuy.setPosition(WORLD_WIDTH/2-200, WORLD_HEIGHT/2-120);
        yesBuy.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                buySkin(costo);
                menuDemo.setScreen(new ShopScreen(menuDemo));
            }
        });

        noButtonTexture = new Texture(Gdx.files.internal("shop/no.png"));
        noPressedButtonTexture = new Texture(Gdx.files.internal("shop/noPress.png"));
        ImageButton noBuy = new ImageButton(new TextureRegionDrawable(new TextureRegion(noButtonTexture)), new TextureRegionDrawable(new TextureRegion(noPressedButtonTexture)));
        noBuy.setPosition(WORLD_WIDTH/2+30, WORLD_HEIGHT/2-120);
        noBuy.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        stageNoCoins = new Stage(viewport);
        noCoinsPanelTexture = new Texture(Gdx.files.internal("shop/noCoins.png"));
        Image noCoins = new Image(noCoinsPanelTexture);

        okButtonTexture = new Texture(Gdx.files.internal("shop/ok.png"));
        okButtonPressedTexture = new Texture(Gdx.files.internal("shop/okPress.png"));
        ImageButton ok = new ImageButton(new TextureRegionDrawable(new TextureRegion(okButtonTexture)), new TextureRegionDrawable(new TextureRegion(okButtonPressedTexture)));
        ok.setPosition(WORLD_WIDTH/3+ok.getWidth(), WORLD_HEIGHT/3);
        ok.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        stageCode = new Stage(viewport);
        codePanelTexture = new Texture(Gdx.files.internal("shop/codePanel.png"));
        Image codePanel = new Image(codePanelTexture);

        crossButtonTexture = new Texture(Gdx.files.internal("shop/cross.png"));
        crossButtonPressedTexture = new Texture(Gdx.files.internal("shop/crossPress.png"));
        ImageButton cross = new ImageButton(new TextureRegionDrawable(new TextureRegion(crossButtonTexture)), new TextureRegionDrawable(new TextureRegion(crossButtonPressedTexture)));
        cross.setPosition(2*WORLD_WIDTH/3+20, 2*WORLD_HEIGHT/3-10);
        cross.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        submitButtonTexture = new Texture(Gdx.files.internal("shop/submit.png"));
        submitButtonPressedTexture = new Texture(Gdx.files.internal("shop/submitPress.png"));
        ImageButton submit = new ImageButton(new TextureRegionDrawable(new TextureRegion(submitButtonTexture)), new TextureRegionDrawable(new TextureRegion(submitButtonPressedTexture)));
        submit.setPosition(WORLD_WIDTH/3+80, WORLD_HEIGHT/3-50);
        submit.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        whiteSpaceTexture = new Texture(Gdx.files.internal("shop/whiteSpace.png"));
        Image whiteSpace = new Image(whiteSpaceTexture);
        whiteSpace.setPosition(WORLD_WIDTH/2-whiteSpace.getWidth()/2, WORLD_HEIGHT/2-whiteSpace.getHeight()/2);

        stageCode.addActor(codePanel);
        stageCode.addActor(cross);
        stageCode.addActor(submit);
        stageCode.addActor(whiteSpace);

        stageNoCoins.addActor(noCoins);
        stageNoCoins.addActor(ok);

        stageBuy.addActor(buyPanel);
        stageBuy.addActor(yesBuy);
        stageBuy.addActor(noBuy);

        stage.addActor(coins);
        stage.addActor(code);
        stage.addActor(retur);
        stage.addActor(background);
    }

    private void buySkin(int costo) {
        coinsCollected -= costo;
        savePreferences();
    }

    private void checkIfCanAfford(int cost) {
        if (coinsCollected>= cost){
            state = STATE.RUSURE;
        }else {
            state = STATE.NOCOINS;
        }
    }

    private void savePreferences() {
        preferencias.putInteger("coins", coinsCollected);
        if(costo == 30){
            preferencias.putBoolean("partyBought", true);
        }else if (costo==35){
            preferencias.putBoolean("hatBought", true);
        }else if(costo == 40){
            preferencias.putBoolean("tieBought", true);
        }else if (costo == 50){
            preferencias.putBoolean("crownBought", true);
        }
        preferencias.flush();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        coinsCollected = preferencias.getInteger("coins", COINS_DEFAULT);
        partyBought = preferencias.getBoolean("partyBought", SKIN_BOUGHT);
        hatBought = preferencias.getBoolean("hatBought", SKIN_BOUGHT);
        tieBought = preferencias.getBoolean("tieBought", SKIN_BOUGHT);
        crownBought = preferencias.getBoolean("crownBought", SKIN_BOUGHT);
        hulkBought = preferencias.getBoolean("crownBought", SKIN_BOUGHT);
        ricardoBought = preferencias.getBoolean("ricardoBought", SKIN_BOUGHT);
    }

    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        //super.render(delta);
        clearScreen();
        updateVolume();
        stage.act();
        draw();
    }

    private void draw() {
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        drawCoinsCounter();
        batch.end();
        if(state == STATE.NORMAL){
            Gdx.input.setInputProcessor(stage);
        }else if(state == STATE.RUSURE){
            stageBuy.draw();
            Gdx.input.setInputProcessor(stageBuy);
        }else if(state == STATE.NOCOINS){
            stageNoCoins.draw();
            Gdx.input.setInputProcessor(stageNoCoins);
        }else if(state == STATE.CODE){
            stageCode.draw();
            Gdx.input.setInputProcessor(stageCode);
        }
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    private void drawCoinsCounter() {
        String coinsAsString = Integer.toString(coinsCollected);
        glyphLayout.setText(bitmapFont, coinsAsString);
        bitmapFont.draw(batch, coinsAsString, WORLD_WIDTH/4, 80);
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
        music.dispose();
        //titleTexture.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}