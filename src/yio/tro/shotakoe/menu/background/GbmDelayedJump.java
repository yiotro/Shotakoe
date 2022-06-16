package yio.tro.shotakoe.menu.background;

import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class GbmDelayedJump implements ReusableYio {

    GbmBubble bubble;
    long targetTime;
    PointYio touchPoint;
    boolean alive;


    public GbmDelayedJump() {
        touchPoint = new PointYio();
    }


    @Override
    public void reset() {
        bubble = null;
        targetTime = 0;
        touchPoint.reset();
        alive = true;
    }


    void move() {
        if (!alive) return;
        if (System.currentTimeMillis() < targetTime) return;
        alive = false;
        if (bubble.jumpEngineYio.getValue() > 1) return;
        bubble.applyJump(touchPoint.angleTo(bubble.position.center));
    }


    boolean isReadyToBeRemoved() {
        return !alive;
    }
}
