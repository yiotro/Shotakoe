package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.menu.MenuSwitcher;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;

public abstract class AbstractPauseMenu extends SceneYio{


    public abstract BackgroundYio getBackgroundValue();


    @Override
    public void initialize() {
        createButtons();
    }


    private void createButtons() {
        TempButtonData[] dataList = getData();

        double iOffset = 0.015;
        double h = 0.07;
        double fullHeight = h * dataList.length + iOffset * (dataList.length - 1);
        double y = 0.45 + fullHeight / 2 - h;

        for (int i = 0; i < dataList.length; i++) {
            ButtonYio buttonYio = uiFactory.getButton()
                    .setSize(0.7, h)
                    .centerHorizontal()
                    .alignBottom(y)
                    .setBackground(dataList[i].backgroundYio)
                    .applyText(dataList[i].key)
                    .amplifyRubberBand()
                    .setReaction(dataList[i].reaction);
            y -= h + iOffset;
            if (i == 0) {
                buttonYio.setHotkeyKeycode(Input.Keys.ENTER);
            }
            if (i == dataList.length - 1) {
                buttonYio.setHotkeyKeycode(Input.Keys.BACK);
            }
        }
    }


    protected abstract TempButtonData[] getData();


    protected Reaction getResumeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.gameView.appear();
                MenuSwitcher.getInstance().createMenuOverlay();
                yioGdxGame.setGamePaused(false);
            }
        };
    }
}
