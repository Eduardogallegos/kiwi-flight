package com.mygdx.menudemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private static final float COLLISION_SQUARE_WIDTH = 50F;
    private static final float MAX_SPEED_PER_SECOND = 130F;


    public static final float WIDTH = COLLISION_SQUARE_WIDTH;

    private final Rectangle collisionRectangle;
    private float x = 0;
    private float y = 0;


    public Obstacle(){
        this.collisionRectangle = new Rectangle(x,y,COLLISION_SQUARE_WIDTH, COLLISION_SQUARE_WIDTH);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
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
        setPosition(x - (MAX_SPEED_PER_SECOND * delta), y);
    }

    public float getX() {
        return x;
    }
}
