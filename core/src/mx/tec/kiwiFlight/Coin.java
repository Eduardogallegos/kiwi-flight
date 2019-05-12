package mx.tec.kiwiFlight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Coin {

    private static final float COLLISION_SQUARE_WIDTH = 80F;
    private static final float COLLISION_SQUARE_HEIGHT = 40F;

    private static int TILE_WIDTH = 125;
    private static  int TILE_HEIGHT = 125;

    public static final float WIDTH = COLLISION_SQUARE_WIDTH;
    private static final float FRAME_DURATION = 0.1F;


    private final Rectangle collisionRectangle;
    private float x = 0;
    private float y = 0;
    private TextureRegion coinTexture;
    private final Animation animation;
    private float animationTimer = 0;
    private float speedPerSecond = 350F;


    public Coin(Texture coinTexture){
        TextureRegion[][] coinTextures = new TextureRegion(coinTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, coinTextures[0][0], coinTextures[0][1],coinTextures[0][2],coinTextures[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        collisionRectangle = new Rectangle(x,y,COLLISION_SQUARE_WIDTH, COLLISION_SQUARE_HEIGHT);

    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void draw(SpriteBatch batch){
        drawCoin(batch);
    }

    private void drawCoin(SpriteBatch batch) {
        TextureRegion coinTexture = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = collisionRectangle.getX() - coinTexture.getRegionWidth()/2;
        float textureY = collisionRectangle.getY();
        batch.draw(coinTexture, textureX, textureY);
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setX(x);
        collisionRectangle.setY(y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRectangle.x,collisionRectangle.y,collisionRectangle.width, collisionRectangle.height);
    }

    public boolean isKiiwColecting(Kiiw kiiw){
        Circle kiiwCollisionCircle = kiiw.getCollisionCircle();
        return Intersector.overlaps(kiiwCollisionCircle, collisionRectangle);
    }

    public void update(float delta) {
        setPosition(x - (speedPerSecond * delta), y);
        animationTimer+=delta/4;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public float getWidth(){return COLLISION_SQUARE_WIDTH;}

    public float getSpeedPerSecond() {
        return speedPerSecond;
    }

    public void setSpeedPerSecond(float speedPerSecond) {
        this.speedPerSecond = speedPerSecond;
    }
}

