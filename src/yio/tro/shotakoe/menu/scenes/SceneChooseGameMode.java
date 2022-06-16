package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneChooseGameMode extends SceneYio{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        createButtons();
        createSecretButton();
        spawnBackButton(getBackReaction());
    }


    private void createSecretButton() {
        uiFactory.getButton()
                .setSize(0.08)
                .alignTop(0)
                .alignRight(0)
                .setHidden(true)
                .setReaction(getOpenSceneReaction(Scenes.secretScreen));
    }


    private void createButtons() {
        TempButtonData[] dataList = initData();

        double iOffset = 0.015;
        double h = 0.07;
        double fullHeight = h * dataList.length + iOffset * (dataList.length - 1);
        double y = 0.45 + fullHeight / 2 - h;

        for (int i = 0; i < dataList.length; i++) {
            ButtonYio buttonYio = uiFactory.getButton()
                    .setSize(0.7, h)
                    .centerHorizontal()
                    .alignBottom(y)
                    .setTouchOffset(GraphicsYio.convertToWidth(iOffset / 2))
                    .setBackground(dataList[i].backgroundYio)
                    .setKey(dataList[i].key)
                    .applyText(dataList[i].key)
                    .amplifyRubberBand()
                    .setReaction(dataList[i].reaction);
            y -= h + iOffset;
            if (i == 0) {
                buttonYio.setHotkeyKeycode(Input.Keys.ENTER);
            }
        }
    }


    private TempButtonData[] initData() {
        return new TempButtonData[]{
                new TempButtonData("quick_game", BackgroundYio.magenta, getOpenSceneReaction(Scenes.setupSkirmish)),
                new TempButtonData("calendar", BackgroundYio.cyan, getOpenSceneReaction(Scenes.calendar)),
                new TempButtonData("campaign", BackgroundYio.green, getOpenSceneReaction(Scenes.campaign)),
        };
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainMenu.create();
            }
        };
    }
}
