package com.mygdx.menudemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Kiiw {

    private static final float COLLISION_RADIUS=30F;
    private static final float FRAME_DURATION = 0.1F;
    private static final int TILE_WIDTH = 200;
    private static final int TILE_HEIGHT = 120;

    public static final float RADIUS = COLLISION_RADIUS;

    private final Circle collisionCircle;
    private TextureRegion kiiwTexture;
    private final Animation animation;

    private float x = 0;
    private float y = 97;
    private float animationTimer = 0;

    public Kiiw(Texture kiiwTexture) {
        TextureRegion [][] kiiwTextures = new TextureRegion(kiiwTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, kiiwTextures[0][0], kiiwTextures[0][1],kiiwTextures[0][2],kiiwTextures[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        collisionCircle = new Circle(x,y, COLLISION_RADIUS);
    }

    public void draw(SpriteBatch batch){
        TextureRegion kiiwTexture = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = collisionCircle.x - kiiwTexture.getRegionWidth()/2;
        float textureY = collisionCircle.y ;
        batch.draw(kiiwTexture, textureX, textureY);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    public void update(float delta) {
        animationTimer += delta;
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getHeigth() {
        return COLLISION_RADIUS;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}