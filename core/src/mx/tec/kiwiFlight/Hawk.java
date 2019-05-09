package mx.tec.kiwiFlight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Hawk {
    private static final int TILE_WIDTH = 250;
    private static final int TILE_HEIGHT = 197;
    private static final float FRAME_DURATION = 0.3F;
    private final Animation animation;
    private TextureRegion hawkTexture;
    private float animationTimer = 0;
    private float x = -60;
    private float y = 450;

    public Hawk(Texture hawkTexture) {
        TextureRegion[][] hawkTextures = new TextureRegion(hawkTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, hawkTextures[0][0], hawkTextures[0][1],hawkTextures[0][2],hawkTextures[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void draw(SpriteBatch batch) {
        hawkTexture = (TextureRegion) animation.getKeyFrame(animationTimer);
        batch.draw(hawkTexture, x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        animationTimer += delta;
    }

    public float getX() {
        return x;
    }
}
