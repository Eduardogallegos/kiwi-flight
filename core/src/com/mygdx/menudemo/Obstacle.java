package com.mygdx.menudemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private static final float COLLISION_RECTANGLE_WIDTH = 13f;
    private static final float COLLISION_RECTANGLE_HEIGHT = 13f;
    private final Rectangle obstacle;
    private static final float MAX_SPEED_PER_SECOND = 100F;
    private static final float DISTANCE_BETWEEN_PADS = 13f;

    public static final float WIDTH = COLLISION_RECTANGLE_WIDTH;

    private float x = 0;
    private float y = 0;
    private boolean pointClaimed = false;
    /*private final Texture rock;
    private final Texture tree;
    private final Texture grass;*/
    private enum PAD{
        ONE,TWO,THREE,FOUR,FIVE
    }
    private PAD pad = PAD.ONE;

    public Obstacle(){
        this.obstacle = new Rectangle(x,y, COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
    }

    /*public void draw(SpriteBatch batch){
        batch.draw();
    }*/

    public void setPosition(float x){
        this.x =x;
        switch (pad){
            case ONE:{
                this.y = 0;
            }
            break;
            case TWO:{
                this.y = DISTANCE_BETWEEN_PADS;
            }
            break;
            case THREE:{
                this.y = 2* DISTANCE_BETWEEN_PADS;
            }
            break;
            case FOUR:{
                this.y = 3*DISTANCE_BETWEEN_PADS;
            }
            break;
            case FIVE:{
                this.y = 4*DISTANCE_BETWEEN_PADS;
            }
            break;
        }
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        obstacle.setX(x);
    }
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.circle(obstacle.x,obstacle.y,obstacle.width, (int) obstacle.height);
    }

    public void update(float delta){
        setPosition(x-(MAX_SPEED_PER_SECOND*delta));
    }

    public boolean isKiiwColliding(Kiiw kiiw){
        Circle kiiwCollisionCircle = kiiw.getCollisionCircle();
        return
                Intersector.overlaps(kiiwCollisionCircle, obstacle);
    }

    public boolean isPointClaimed() {
        return pointClaimed;
    }

    public void markPointClaimed() {
        pointClaimed = true;

    }

    public float getX() {
        return x;
    }
}
