package com.mygdx.menudemo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Kiiw {

    private static final int TILE_WIDTH = 118;
    private static final int TILE_HEIGHT = 118;
    private static final float FRAME_DURATION = 0.25F;
    private static final float COLLISION_RADIUS = 24f;
    private static final float RUN_ACCEL = 0.15F;
    private static final float DISTANCE_BETWEEN_PADS = 13f;

    //private final Animation animation;
    private final Circle collisionCircle;

    private float x = 0;
    private float y = 0;
    private float animationTimer = 0;
    private float xSpeed = 0;
    private TextureRegion kiiwTexture;

    public Kiiw(/*TextureRegion kiiwTexture*/){
        /*TextureRegion[][] kiiwTextures = new TextureRegion(kiiwTexture).split(TILE_WIDTH, TILE_HEIGHT);

        animation = new Animation(FRAME_DURATION, kiiwTextures[0][0],kiiwTextures[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
*/
        collisionCircle = new Circle(x,y,COLLISION_RADIUS);
    }

    public void draw(SpriteBatch batch){
        //TextureRegion kiiwTexture = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = collisionCircle.x - kiiwTexture.getRegionWidth()/2;
        float textureY = collisionCircle.y - kiiwTexture.getRegionHeight()/2;
        batch.draw(kiiwTexture, textureX, textureY);
    }

    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta){
        animationTimer+=delta;
        xSpeed+= RUN_ACCEL;
    }

    public void changePadUp(){
        y += DISTANCE_BETWEEN_PADS;
        setPosition(x, y);
    }

    public void changePadDown(){
        y -= DISTANCE_BETWEEN_PADS;
        setPosition(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }

}
