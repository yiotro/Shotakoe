package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;

public class SizeManager {

    public LevelSize initialLevelSize;
    public float boundWidth;
    public float boundHeight;
    public RectangleYio position;


    public SizeManager() {
        position = new RectangleYio();
    }


    public void initLevelSize(LevelSize levelSize) {
        initialLevelSize = levelSize;
        switch (levelSize) {
            default:
            case def:
                setBounds(1);
                break;
        }
    }


    void setBounds(double multiplier) {
        boundWidth = (float) (multiplier * GraphicsYio.width);
//        boundHeight = 1.6f * boundWidth;
        boundHeight = (float) (multiplier * GraphicsYio.height);
        position.set(0, 0, boundWidth, boundHeight);
    }

}