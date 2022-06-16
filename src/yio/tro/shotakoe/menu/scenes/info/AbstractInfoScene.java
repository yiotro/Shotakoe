package yio.tro.shotakoe.menu.scenes.info;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.elements.CircleButtonYio;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.stuff.RectangleYio;

public abstract class AbstractInfoScene extends SceneYio {


    protected ButtonYio infoPanel;
    protected CircleButtonYio backButton;
    protected RectangleYio infoLabelPosition;


    @Override
    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    protected void initInfoLabelPosition() {
        infoLabelPosition = new RectangleYio(0.05, 0.45 - getHeight() / 2, 0.9, getHeight());
    }


    protected double getHeight() {
        return 0.7;
    }


    protected void createInfoMenu(String key, Reaction backButtonBehavior) {
        backButton = spawnBackButton(backButtonBehavior);

        initInfoLabelPosition();
        infoPanel = uiFactory.getButton()
                .setPosition(infoLabelPosition.x, infoLabelPosition.y, infoLabelPosition.width, infoLabelPosition.height)
                .setTouchable(false)
                .setFont(Fonts.miniFont)
                .applyManyTextLines(key);
    }
}
