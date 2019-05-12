package mx.tec.kiwiFlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 640;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;
    private static final String ACTUAL_SKIN = "default";
    private final Preferences preferencias;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress = 0;
    private final kiwiFlight kiwiFlight;
    private final int LEVEL;
    private String skin;

    public LoadingScreen(kiwiFlight kiwiFlight, int level) {
        this.kiwiFlight = kiwiFlight;
        this.LEVEL = level;
        this.preferencias = kiwiFlight.getPreferences();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        loadPreferences();
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        kiwiFlight.getAssetManager().load("level"+LEVEL+"/song.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/coin.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/hit.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/KiiwLose.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/KiiwWin.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/Kiwhine.mp3", Music.class);
        kiwiFlight.getAssetManager().load("defaultLevels/pausa.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/Barra.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/lifes0.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/lifes1.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/lifes2.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/Coins.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/pausepanel.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/exit.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/exitPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/restart.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/restartPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/resume.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/resumePressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/settings.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/settingsPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("level"+LEVEL+"/roca.png", Texture.class);
        kiwiFlight.getAssetManager().load("level"+LEVEL+"/arbol.png", Texture.class);
        kiwiFlight.getAssetManager().load("level"+LEVEL+"/pasto.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/gamecoin.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/gameOverPanel.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/yes.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/yesPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/no.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/noPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/ok.png", Texture.class);
        kiwiFlight.getAssetManager().load("level"+LEVEL+"/panel.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/okPressed.png", Texture.class);
        kiwiFlight.getAssetManager().load("level4/hawk.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/settingsPanel.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/pausaPress.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/flecha.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/flechaPress.png", Texture.class);
        kiwiFlight.getAssetManager().load("defaultLevels/lifesNull.png", Texture.class);
        if(LEVEL!=4) {
            if(skin.compareTo("default") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/RunningKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingKiwi.png", Texture.class);
            }else if(skin.compareTo("crown") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/CrownKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningCrownKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingCrownKiwi.png", Texture.class);
            }else if(skin.compareTo("hat") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/HatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningTopHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingTopHatKiwi.png", Texture.class);
            }else if(skin.compareTo("hulk") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/HulkKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningHulkKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingHulkKiwi.png", Texture.class);
            }else if(skin.compareTo("party") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/partyKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningPartyHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingPartyHatKiwi.png", Texture.class);
            }else if(skin.compareTo("tie") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/TieKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningTieKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingTieKiwi.png", Texture.class);
            }else if(skin.compareTo("ricardo") == 0) {
                kiwiFlight.getAssetManager().load("defaultLevels/ricardoKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningDancingKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingDancingKiwi.png", Texture.class);
            }
        }
        if(LEVEL == 4) {
            if(skin.compareTo("default") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingKiwi.png", Texture.class);
            }else if(skin.compareTo("party") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingPartyHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningPartyHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingPartyHatKiwi.png", Texture.class);
            }else if(skin.compareTo("hat") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingTopHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningTopHatKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingTopHatKiwi.png", Texture.class);
            }else if(skin.compareTo("tie") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingTieKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningTieKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingTieKiwi.png", Texture.class);
            }else if(skin.compareTo("crown") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingCrownKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningCrownKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingCrownKiwi.png", Texture.class);
            }else if(skin.compareTo("hulk") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingHulkKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningHulkKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingHulkKiwi.png", Texture.class);
            }else if(skin.compareTo("ricardo") == 0) {
                kiwiFlight.getAssetManager().load("level4/FlyingDancingKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/WinningDancingKiwi.png", Texture.class);
                kiwiFlight.getAssetManager().load("defaultLevels/LosingDancingKiwi.png", Texture.class);
            }
        }

        if(LEVEL == 1) {
            kiwiFlight.getAssetManager().load("level1/next.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/nextPressed.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/obstacles.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/got.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/gotPressed.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/back.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/backPressed.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/speed.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/time.png", Texture.class);
            kiwiFlight.getAssetManager().load("level1/coins.png", Texture.class);
            kiwiFlight.getAssetManager().load("defaultLevels/panelSkip.png", Texture.class);
            kiwiFlight.getAssetManager().load("defaultLevels/skipPress.png", Texture.class);
        }

    }

    private void loadPreferences() {
        skin = preferencias.getString("actualSkin", ACTUAL_SKIN);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void update() {
        if (kiwiFlight.getAssetManager().update()) {
            kiwiFlight.setScreen(new level(kiwiFlight, LEVEL));
        } else {
            progress = kiwiFlight.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (WORLD_WIDTH) / 2, (WORLD_HEIGHT/ 2),
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
}