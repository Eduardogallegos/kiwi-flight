package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
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
    private final MenuDemo menuDemo;
    private final int LEVEL;
    private String skin;
    //private final Game game;

    public LoadingScreen(MenuDemo menuDemo, int level) {
        this.menuDemo = menuDemo;
        this.LEVEL = level;
        this.preferencias = menuDemo.getPreferences();
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
        menuDemo.getAssetManager().load("level"+LEVEL+"/song.mp3", Music.class);
        menuDemo.getAssetManager().load("defaultLevels/coin.mp3", Music.class);
        menuDemo.getAssetManager().load("defaultLevels/hit.mp3", Music.class);
        menuDemo.getAssetManager().load("defaultLevels/KiiwLose.mp3", Music.class);
        menuDemo.getAssetManager().load("defaultLevels/KiiwWin.mp3", Music.class);
        menuDemo.getAssetManager().load("defaultLevels/Kiwhine.mp3", Music.class);
        if(LEVEL!=4) {
            menuDemo.getAssetManager().load("defaultLevels/RunningKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/CrownKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/HatKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/HulkKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/partyKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/TieKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/ricardoKiwi.png", Texture.class);
        }
        menuDemo.getAssetManager().load("defaultLevels/pausa.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/Barra.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/lifes0.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/lifes1.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/lifes2.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/Coins.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/pausepanel.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/exit.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/exitPressed.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/restart.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/restartPressed.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/resume.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/resumePressed.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/settings.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/settingsPressed.png", Texture.class);
        menuDemo.getAssetManager().load("level"+LEVEL+"/roca.png", Texture.class);
        menuDemo.getAssetManager().load("level"+LEVEL+"/arbol.png", Texture.class);
        menuDemo.getAssetManager().load("level"+LEVEL+"/pasto.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/gamecoin.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/gameOverPanel.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/yes.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/yesPressed.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/no.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/noPressed.png", Texture.class);
        if(LEVEL == 4) {
            menuDemo.getAssetManager().load("level4/FlyingKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingPartyHatKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingTopHatKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingTieKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingCrownKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingHulkKiwi.png", Texture.class);
            menuDemo.getAssetManager().load("level4/FlyingDancingKiwi.png", Texture.class);

        }
        menuDemo.getAssetManager().load("defaultLevels/ok.png", Texture.class);
        menuDemo.getAssetManager().load("level"+LEVEL+"/panel.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/okPressed.png", Texture.class);
        menuDemo.getAssetManager().load("level4/hawk.png", Texture.class);
        if(LEVEL == 1) {
            menuDemo.getAssetManager().load("level1/next.png", Texture.class);
            menuDemo.getAssetManager().load("level1/nextPressed.png", Texture.class);
            menuDemo.getAssetManager().load("level1/obstacles.png", Texture.class);
            menuDemo.getAssetManager().load("level1/got.png", Texture.class);
            menuDemo.getAssetManager().load("level1/gotPressed.png", Texture.class);
            menuDemo.getAssetManager().load("level1/back.png", Texture.class);
            menuDemo.getAssetManager().load("level1/backPressed.png", Texture.class);
            menuDemo.getAssetManager().load("level1/speed.png", Texture.class);
            menuDemo.getAssetManager().load("level1/time.png", Texture.class);
            menuDemo.getAssetManager().load("level1/coins.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/panelSkip.png", Texture.class);
            menuDemo.getAssetManager().load("defaultLevels/skipPress.png", Texture.class);
        }
        menuDemo.getAssetManager().load("defaultLevels/settingsPanel.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/pausaPress.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/flecha.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/flechaPress.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningCrownKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningDancingKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningHulkKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningPartyHatKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningTieKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/WinningTopHatKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingCrownKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingDancingKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingHulkKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingPartyHatKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingTieKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/LosingTopHatKiwi.png", Texture.class);
        menuDemo.getAssetManager().load("defaultLevels/lifesNull.png", Texture.class);
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
        if (menuDemo.getAssetManager().update()) {
            menuDemo.setScreen(new level(menuDemo, LEVEL));
        } else {
            progress = MenuDemo.getAssetManager().getProgress();
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