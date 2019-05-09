package com.mygdx.kiwiFlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

class ShopScreen extends ScreenAdapter {

    private static final float MUSIC_VOLUME_DEFAULT = 1;
    private static  final int COINS_DEFAULT = 0;
    private static final boolean SKIN_BOUGHT = false;
    private final kiwiFlight kiwiFlight;
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
    private Texture ceroButtonPressedTexture;
    private Texture ceroButtonTexture;
    private int inputSpaces = 4;
    private String code = "";
    private Texture oneButtonTexture;
    private Texture oneButtonPressedTexture;
    private Texture twoButtonTexture;
    private Texture twoButtonPressedTexture;
    private Texture threeButtonTexture;
    private Texture threeButtonPressedTexture;
    private Texture fourButtonTexture;
    private Texture fourButtonPressedTexture;
    private Texture sevenButtonTexture;
    private Texture sevenButtonPressedTexture;
    private Texture fiveButtonTexture;
    private Texture fiveButtonPressedTexture;
    private Texture sixButtonTexture;
    private Texture sixButtonPressedTexture;
    private Texture eightButtonTexture;
    private Texture eightButtonPressedTexture;
    private Texture nineButtonTexture;
    private Texture nineButtonPressedTexture;
    private Texture eraseButtonPressedTexture;
    private Texture eraseButtonTexture;

    private Label inputLabel;
    private Label outputLabel;
    private Stage quitStage;
    private Texture quitPanelTexture;
    private Texture yesQuitTexture;
    private Texture yesQuitPressTexture;
    private Texture noQuitTexture;
    private Texture noQuitPressTexture;
    private String skinDecode;

    private enum STATE{
        NORMAL, RUSURE, NOCOINS, CODE, QUIT
    }

    private STATE state = STATE.NORMAL;

    public ShopScreen(kiwiFlight kiwiFlight) {
        this.kiwiFlight = kiwiFlight;
        this.preferencias = kiwiFlight.getPreferences();
    }

    public void show() {
        loadPreferences();

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, camera.position.z);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);


        camera.update();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        music = Gdx.audio.newMusic(Gdx.files.internal("shop/song.mp3"));
        updateVolume();
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
                kiwiFlight.setScreen(new StartScreen(kiwiFlight));
                dispose();
            }
        });

        codeTexture = new Texture(Gdx.files.internal("shop/code.png"));
        codePressTexture = new Texture(Gdx.files.internal("shop/codePress.png"));
        ImageButton codeButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(codeTexture)), new TextureRegionDrawable(new TextureRegion(codePressTexture)));
        codeButton.setPosition(200, 150);
        codeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                code = "";
                inputSpaces = 4;
                createStageCode();
                state = STATE.CODE;
            }
        });

        createQuitPanel();

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
                    costo = 100;
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
                    costo = 75;
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
                    costo = 85;
                    checkIfCanAfford(costo);
                }
            });
            stage.addActor(tie);
        }

        if (crownBought){
            crownSkinButtonTexture = new Texture(Gdx.files.internal("shop/crownKiwiSold.png"));
            crownSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/crownKiwiSold.png"));
            ImageButton crown = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressTexture)));
            crown.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 - 200);
            stage.addActor(crown);
        }else {
            crownSkinButtonTexture = new Texture(Gdx.files.internal("shop/crownKiwi.png"));
            crownSkinButtonPressTexture = new Texture(Gdx.files.internal("shop/crownKiwiPress.png"));
            ImageButton crown = new ImageButton(new TextureRegionDrawable(new TextureRegion(crownSkinButtonTexture)), new TextureRegionDrawable(new TextureRegion(crownSkinButtonPressTexture)));
            crown.setPosition(WORLD_WIDTH / 2 + 350, WORLD_HEIGHT / 2 - 200);
            crown.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    costo = 120;
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
                dispose();
                music.stop();
                kiwiFlight.setScreen(new ShopScreen(kiwiFlight));
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
        createStageCode();

        stageNoCoins.addActor(noCoins);
        stageNoCoins.addActor(ok);

        stageBuy.addActor(buyPanel);
        stageBuy.addActor(yesBuy);
        stageBuy.addActor(noBuy);

        stage.addActor(coins);
        stage.addActor(codeButton);
        stage.addActor(retur);
        stage.addActor(background);
    }

    private void createQuitPanel() {
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
    }

    private void createStageCode() {
        codePanelTexture = new Texture(Gdx.files.internal("shop/codePanel.png"));
        Image codePanel = new Image(codePanelTexture);

        crossButtonTexture = new Texture(Gdx.files.internal("shop/cross.png"));
        crossButtonPressedTexture = new Texture(Gdx.files.internal("shop/crossPress.png"));
        ImageButton cross = new ImageButton(new TextureRegionDrawable(new TextureRegion(crossButtonTexture)), new TextureRegionDrawable(new TextureRegion(crossButtonPressedTexture)));
        cross.setPosition(WORLD_WIDTH/3+230, 2*WORLD_HEIGHT/3-10);
        cross.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        submitButtonTexture = new Texture(Gdx.files.internal("shop/submit.png"));
        submitButtonPressedTexture = new Texture(Gdx.files.internal("shop/submitPress.png"));
        ImageButton submit = new ImageButton(new TextureRegionDrawable(new TextureRegion(submitButtonTexture)), new TextureRegionDrawable(new TextureRegion(submitButtonPressedTexture)));
        submit.setPosition(WORLD_WIDTH/3-150, WORLD_HEIGHT/3-50);
        submit.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                checkCode();
            }
        });

        whiteSpaceTexture = new Texture(Gdx.files.internal("shop/whiteSpace.png"));
        Image whiteSpace = new Image(whiteSpaceTexture);
        whiteSpace.setPosition(WORLD_WIDTH/3-whiteSpace.getWidth()/2, WORLD_HEIGHT/2-whiteSpace.getHeight()/2);

        //use a Label widget
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        inputLabel = new Label("", labelStyle);
        inputLabel.setPosition(WORLD_WIDTH/3-50, WORLD_HEIGHT/2-inputLabel.getHeight()/2);
        outputLabel = new Label("", labelStyle);
        outputLabel.setPosition(WORLD_WIDTH/3-50, WORLD_HEIGHT/2-outputLabel.getHeight());

        ceroButtonTexture = new Texture(Gdx.files.internal("shop/0.png"));
        ceroButtonPressedTexture = new Texture(Gdx.files.internal("shop/0Press.png"));
        ImageButton number0 = new ImageButton(new TextureRegionDrawable(new TextureRegion(ceroButtonTexture)), new TextureRegionDrawable(new TextureRegion(ceroButtonPressedTexture)));
        number0.setPosition(2*WORLD_WIDTH/3, WORLD_HEIGHT/3-80);
        number0.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("0");
            }
        });

        oneButtonTexture = new Texture(Gdx.files.internal("shop/1.png"));
        oneButtonPressedTexture = new Texture(Gdx.files.internal("shop/1Press.png"));
        ImageButton number1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(oneButtonTexture)), new TextureRegionDrawable(new TextureRegion(oneButtonPressedTexture)));
        number1.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-20);
        number1.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("1");
            }
        });

        twoButtonTexture = new Texture(Gdx.files.internal("shop/2.png"));
        twoButtonPressedTexture = new Texture(Gdx.files.internal("shop/2Press.png"));
        ImageButton number2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(twoButtonTexture)), new TextureRegionDrawable(new TextureRegion(twoButtonPressedTexture)));
        number2.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-20);
        number2.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("2");
            }
        });

        threeButtonTexture = new Texture(Gdx.files.internal("shop/3.png"));
        threeButtonPressedTexture = new Texture(Gdx.files.internal("shop/3Press.png"));
        ImageButton number3 = new ImageButton(new TextureRegionDrawable(new TextureRegion(threeButtonTexture)), new TextureRegionDrawable(new TextureRegion(threeButtonPressedTexture)));
        number3.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-20);
        number3.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("3");
            }
        });

        fourButtonTexture = new Texture(Gdx.files.internal("shop/4.png"));
        fourButtonPressedTexture = new Texture(Gdx.files.internal("shop/4Press.png"));
        ImageButton number4 = new ImageButton(new TextureRegionDrawable(new TextureRegion(fourButtonTexture)), new TextureRegionDrawable(new TextureRegion(fourButtonPressedTexture)));
        number4.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-120);
        number4.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("4");
            }
        });

        fiveButtonTexture = new Texture(Gdx.files.internal("shop/5.png"));
        fiveButtonPressedTexture = new Texture(Gdx.files.internal("shop/5Press.png"));
        ImageButton number5 = new ImageButton(new TextureRegionDrawable(new TextureRegion(fiveButtonTexture)), new TextureRegionDrawable(new TextureRegion(fiveButtonPressedTexture)));
        number5.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-120);
        number5.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("5");
            }
        });

        sixButtonTexture = new Texture(Gdx.files.internal("shop/6.png"));
        sixButtonPressedTexture = new Texture(Gdx.files.internal("shop/6Press.png"));
        ImageButton number6 = new ImageButton(new TextureRegionDrawable(new TextureRegion(sixButtonTexture)), new TextureRegionDrawable(new TextureRegion(sixButtonPressedTexture)));
        number6.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-120);
        number6.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("6");
            }
        });

        sevenButtonTexture = new Texture(Gdx.files.internal("shop/7.png"));
        sevenButtonPressedTexture = new Texture(Gdx.files.internal("shop/7Press.png"));
        ImageButton number7 = new ImageButton(new TextureRegionDrawable(new TextureRegion(sevenButtonTexture)), new TextureRegionDrawable(new TextureRegion(sevenButtonPressedTexture)));
        number7.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-220);
        number7.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("7");
            }
        });

        eightButtonTexture = new Texture(Gdx.files.internal("shop/8.png"));
        eightButtonPressedTexture = new Texture(Gdx.files.internal("shop/8Press.png"));
        ImageButton number8 = new ImageButton(new TextureRegionDrawable(new TextureRegion(eightButtonTexture)), new TextureRegionDrawable(new TextureRegion(eightButtonPressedTexture)));
        number8.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-220);
        number8.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("8");
            }
        });

        nineButtonTexture = new Texture(Gdx.files.internal("shop/9.png"));
        nineButtonPressedTexture = new Texture(Gdx.files.internal("shop/9Press.png"));
        ImageButton number9 = new ImageButton(new TextureRegionDrawable(new TextureRegion(nineButtonTexture)), new TextureRegionDrawable(new TextureRegion(nineButtonPressedTexture)));
        number9.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-220);
        number9.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                doCode("9");
            }
        });

        eraseButtonTexture = new Texture(Gdx.files.internal("shop/back.png"));
        eraseButtonPressedTexture = new Texture(Gdx.files.internal("shop/backPress.png"));
        ImageButton erase = new ImageButton(new TextureRegionDrawable(new TextureRegion(eraseButtonTexture)), new TextureRegionDrawable(new TextureRegion(eraseButtonPressedTexture)));
        erase.setPosition(2*WORLD_WIDTH/3 + 150, WORLD_HEIGHT/3-80);
        erase.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                eraseCode();
            }
        });

        stageCode.addActor(codePanel);
        stageCode.addActor(cross);
        stageCode.addActor(submit);
        stageCode.addActor(whiteSpace);
        stageCode.addActor(inputLabel);
        stageCode.addActor(number0);
        stageCode.addActor(number1);
        stageCode.addActor(number2);
        stageCode.addActor(number3);
        stageCode.addActor(number4);
        stageCode.addActor(number5);
        stageCode.addActor(number6);
        stageCode.addActor(number7);
        stageCode.addActor(number8);
        stageCode.addActor(number9);
        stageCode.addActor(erase);
    }

    private void checkCode() {
        if (code.compareTo("1234") == 0 && !hulkBought){
            createCodeValidationPanel(true);
            costo = 150;
            skinDecode = "hulk";
            savePreferences();
            loadPreferences();
        }else if (code.compareTo("4928") == 0 && !ricardoBought){
            createCodeValidationPanel(true);
            costo = 400;
            skinDecode = "ricardo";
            savePreferences();
            loadPreferences();
        }else{
            createCodeValidationPanel(false);
            skinDecode = "";
        }
    }

    private void createCodeValidationPanel(boolean validation) {
        stageCode.clear();
        if(validation){
            codePanelTexture = new Texture(Gdx.files.internal("shop/validCode.png"));
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = bitmapFont;
            outputLabel = new Label("", labelStyle);
            outputLabel.setPosition(WORLD_WIDTH/3-80, WORLD_HEIGHT/2-42);
        }else{
            codePanelTexture = new Texture(Gdx.files.internal("shop/invalidCode.png"));
        }

        crossButtonTexture = new Texture(Gdx.files.internal("shop/cross.png"));
        crossButtonPressedTexture = new Texture(Gdx.files.internal("shop/crossPress.png"));
        ImageButton cross = new ImageButton(new TextureRegionDrawable(new TextureRegion(crossButtonTexture)), new TextureRegionDrawable(new TextureRegion(crossButtonPressedTexture)));
        cross.setPosition(WORLD_WIDTH/3+230, 2*WORLD_HEIGHT/3-10);
        cross.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });


        ceroButtonTexture = new Texture(Gdx.files.internal("shop/0.png"));
        oneButtonTexture = new Texture(Gdx.files.internal("shop/1.png"));
        twoButtonTexture = new Texture(Gdx.files.internal("shop/2.png"));
        threeButtonTexture = new Texture(Gdx.files.internal("shop/3.png"));
        fourButtonTexture = new Texture(Gdx.files.internal("shop/4.png"));
        fiveButtonTexture = new Texture(Gdx.files.internal("shop/5.png"));
        sixButtonTexture = new Texture(Gdx.files.internal("shop/6.png"));
        sevenButtonTexture = new Texture(Gdx.files.internal("shop/7.png"));
        eightButtonTexture = new Texture(Gdx.files.internal("shop/8.png"));
        nineButtonTexture = new Texture(Gdx.files.internal("shop/9.png"));
        eraseButtonTexture = new Texture(Gdx.files.internal("shop/back.png"));

        okButtonTexture = new Texture(Gdx.files.internal("shop/ok.png"));
        okButtonPressedTexture = new Texture(Gdx.files.internal("shop/okPress.png"));
        ImageButton ok = new ImageButton(new TextureRegionDrawable(new TextureRegion(okButtonTexture)), new TextureRegionDrawable(new TextureRegion(okButtonPressedTexture)));
        ok.setPosition(WORLD_WIDTH/3-75, WORLD_HEIGHT/3-50);
        ok.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                state = STATE.NORMAL;
            }
        });

        Image codePanel = new Image(codePanelTexture);
        Image number0 = new Image(ceroButtonTexture);
        number0.setPosition(2*WORLD_WIDTH/3, WORLD_HEIGHT/3-80);
        Image number1 = new Image(oneButtonTexture);
        number1.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-20);
        Image number2 = new Image(twoButtonTexture);
        number2.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-20);
        Image number3 = new Image(threeButtonTexture);
        number3.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-20);
        Image number4 = new Image(fourButtonTexture);
        number4.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-120);
        Image number5 = new Image(fiveButtonTexture);
        number5.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-120);
        Image number6 = new Image(sixButtonTexture);
        number6.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-120);
        Image number7 = new Image(sevenButtonTexture);
        number7.setPosition(2*WORLD_WIDTH/3, 2*WORLD_HEIGHT/3-220);
        Image number8 = new Image(eightButtonTexture);
        number8.setPosition(2*WORLD_WIDTH/3+150, 2*WORLD_HEIGHT/3-220);
        Image number9 = new Image(nineButtonTexture);
        number9.setPosition(2*WORLD_WIDTH/3+300, 2*WORLD_HEIGHT/3-220);
        Image erase = new Image(eraseButtonTexture);
        erase.setPosition(2*WORLD_WIDTH/3 + 150, WORLD_HEIGHT/3-80);

        stageCode.addActor(codePanel);
        stageCode.addActor(cross);
        stageCode.addActor(outputLabel);
        stageCode.addActor(number0);
        stageCode.addActor(number1);
        stageCode.addActor(number2);
        stageCode.addActor(number3);
        stageCode.addActor(number4);
        stageCode.addActor(number5);
        stageCode.addActor(number6);
        stageCode.addActor(number7);
        stageCode.addActor(number8);
        stageCode.addActor(number9);
        stageCode.addActor(erase);
        stageCode.addActor(ok);
    }

    private void eraseCode() {
        if(inputSpaces<4){
            int index = 3-inputSpaces;
            code = code.substring(0, index);
            inputSpaces++;
        }
    }

    private void doCode(String input) {
        if(inputSpaces > 0) {
            inputSpaces--;
            code += input;
        }
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
        if(costo == 100)preferencias.putBoolean("partyBought", true);
        else if (costo==75)preferencias.putBoolean("hatBought", true);
        else if(costo == 85)preferencias.putBoolean("tieBought", true);
        else if (costo == 120)preferencias.putBoolean("crownBought", true);
        else if(costo == 150)preferencias.putBoolean("hulkBought",  true);
        else if(costo == 400)preferencias.putBoolean("ricardoBought", true);
        preferencias.flush();
    }

    private void loadPreferences() {
        musicVolume = preferencias.getFloat("musicVolume", MUSIC_VOLUME_DEFAULT);
        coinsCollected = preferencias.getInteger("coins", COINS_DEFAULT);
        partyBought = preferencias.getBoolean("partyBought", SKIN_BOUGHT);
        hatBought = preferencias.getBoolean("hatBought", SKIN_BOUGHT);
        tieBought = preferencias.getBoolean("tieBought", SKIN_BOUGHT);
        crownBought = preferencias.getBoolean("crownBought", SKIN_BOUGHT);
        hulkBought = preferencias.getBoolean("hulkBought", SKIN_BOUGHT);
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
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            state = STATE.QUIT;
        }
        if(state == STATE.QUIT){
            quitStage.draw();
            Gdx.input.setInputProcessor(quitStage);
        }
    }

    private void draw() {
        stage.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        if(state == STATE.NORMAL) drawCoinsCounter();
        else if (state == STATE.CODE) {
            drawCodeInput();
            drawCodeOutout();
        }
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

    private void drawCodeOutout() {
        outputLabel.setText(skinDecode);
    }

    private void updateVolume() {
        music.setVolume(musicVolume);
    }

    private void drawCoinsCounter() {
        String coinsAsString = Integer.toString(coinsCollected);
        glyphLayout.setText(bitmapFont, coinsAsString);
        bitmapFont.draw(batch, coinsAsString, WORLD_WIDTH/4, 80);
    }

    private void drawCodeInput() {
        inputLabel.setText(code);
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