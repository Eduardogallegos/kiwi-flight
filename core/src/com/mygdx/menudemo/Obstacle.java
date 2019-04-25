package com.mygdx.menudemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private static final float COLLISION_SQUARE_WIDTH = 50F;

    public static final float WIDTH = COLLISION_SQUARE_WIDTH;
    private static final float FRAME_DURATION = 0.1F;

    private static int TILE_WIDTH;
    private static  int TILE_HEIGHT;

    private final Rectangle collisionRectangle;
    private float x = 0;
    private float y = 0;
    private boolean Grass;
    private TextureRegion obstacleTexture;
    private final Animation animation;
    private float animationTimer = 0;
    private float speedPerSecond = 350F;


    public Obstacle(boolean isGrass, Texture obstacleTexture, int TILE_WIDTH, int TILE_HEIGHT){
        this.Grass = isGrass;
        this.TILE_WIDTH = TILE_WIDTH;
        this.TILE_HEIGHT = TILE_HEIGHT;
        TextureRegion[][] obstacleTextures = new TextureRegion(obstacleTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, obstacleTextures[0][0], obstacleTextures[0][1],obstacleTextures[0][2],obstacleTextures[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        collisionRectangle = new Rectangle(x,y,COLLISION_SQUARE_WIDTH, COLLISION_SQUARE_WIDTH);

    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void draw(SpriteBatch batch){
        drawObstacle(batch);
    }

    private void drawObstacle(SpriteBatch batch) {
        TextureRegion obstacleTexture = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = collisionRectangle.getX() - obstacleTexture.getRegionWidth()/2;
        float textureY = collisionRectangle.getY();
        batch.draw(obstacleTexture, textureX, textureY);
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setX(x);
        collisionRectangle.setY(y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRectangle.x,collisionRectangle.y,collisionRectangle.width, collisionRectangle.height);
    }

    public boolean isKiiwColliding(Kiiw kiiw){
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

    public boolean grass(){
        return Grass;
    }

    public float getSpeedPerSecond() {
        return speedPerSecond;
    }

    public void setSpeedPerSecond(float speedPerSecond) {
        this.speedPerSecond = speedPerSecond;
    }
}
