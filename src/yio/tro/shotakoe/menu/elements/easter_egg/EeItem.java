package yio.tro.shotakoe.menu.elements.easter_egg;

import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class EeItem implements ReusableYio {

    EasterEggElement easterEggElement;
    public RectangleYio position;
    float speed;
    public int colorIndex;
    float targetHeight;


    public EeItem(EasterEggElement easterEggElement) {
        this.easterEggElement = easterEggElement;
        position = new RectangleYio();
        targetHeight = 1.5f * GraphicsYio.height;
    }


    @Override
    public void reset() {
        position.reset();
        speed = 0;
        colorIndex = -1;
    }


    void move() {
        applySpeed();
        checkToTeleport();
    }


    private void checkToTeleport() {
        if (!shouldTeleport()) return;
        while (position.y < GraphicsYio.height) {
            position.y += GraphicsYio.height;
        }
        if (hasReachedTargetHeight()) return;
        position.height *= 1.1;
    }


    private boolean shouldTeleport() {
        return !isVisible() && !isReadyToBeRemoved();
    }


    private void applySpeed() {
        position.y -= speed;
    }


    public boolean isReadyToBeRemoved() {
        if (isVisible()) return false;
        return easterEggElement.items.size() == EasterEggElement.MAX_SIZE;
    }


    private boolean hasReachedTargetHeight() {
        return position.height > targetHeight;
    }


    public boolean isVisible() {
        return position.y + position.height >= 0;
    }
}
