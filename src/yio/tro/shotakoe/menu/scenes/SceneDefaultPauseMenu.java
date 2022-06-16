package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.LabelElement;

public class SceneDefaultPauseMenu extends AbstractPauseMenu {


    private LabelElement labelElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected TempButtonData[] getData() {
        return new TempButtonData[]{
                new TempButtonData("resume", BackgroundYio.green, getResumeReaction()),
                new TempButtonData("restart", BackgroundYio.yellow, getOpenSceneReaction(Scenes.confirmRestart)),
                new TempButtonData("main_menu", BackgroundYio.red, getOpenSceneReaction(Scenes.mainMenu)),
        };
    }


    @Override
    public void initialize() {
        super.initialize();
        createLevelIndexLabel();
    }


    private void createLevelIndexLabel() {
        labelElement = uiFactory.getLabelElement()
                .setSize(0.5, 0.035)
                .alignTop(0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.up_then_fade)
                .setFont(Fonts.miniFont)
                .setString("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateLabel();
    }


    private void updateLabel() {
        int levelIndex = getGameController().objectsLayer.getLoadingParameters().levelIndex;
        if (levelIndex == -1) {
            labelElement.setString("");
            return;
        }
        labelElement.setString(languagesManager.getString("level") + " " + levelIndex);
    }
}
