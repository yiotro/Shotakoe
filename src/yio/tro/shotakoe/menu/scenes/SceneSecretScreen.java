package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;

public class SceneSecretScreen extends SceneYio{


    private ButtonYio mainLabel;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
    }


    @Override
    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        createBackButton();
        createMainLabel();
        createButtons();
        createVersionLabel();
    }


    private void createVersionLabel() {
        uiFactory.getLabelElement()
                .setSize(0.12, 0.05)
                .alignTop(0)
                .alignRight(0.02)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setRightAlignEnabled(true)
                .setTitle("[" + 0 + "]");
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .alignBottom(0.03)
                .setBackground(BackgroundYio.gray)
                .setShadow(false)
                .setTouchOffset(0.02)
                .applyText("Action")
                .setReaction(getActionReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignAbove(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Super user")
                .setReaction(getSuperUserReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignAbove(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Test screen")
                .setReaction(getOpenSceneReaction(Scenes.testScreen));

        uiFactory.getButton()
                .clone(previousElement)
                .alignAbove(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Show fps")
                .setReaction(getShowFpsReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignAbove(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Debug tests")
                .setReaction(getOpenSceneReaction(Scenes.debugTests));
    }


    private Reaction getActionReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                DebugFlags.nextIncrementalMode = true;
                Scenes.notification.show("Incremental mode enabled");
            }
        };
    }


    private Reaction getSuperUserReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                switchSuperUser();
            }
        };
    }


    private Reaction getShowFpsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                switchShowFps();
            }
        };
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignTop(0.15)
                .setTouchable(false)
                .setFont(Fonts.gameFont)
                .applyManyTextLines(convertStringToArray("- Secret screen - #Load time: " + YioGdxGame.initialLoadingTime));
    }


    private void createBackButton() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainMenu));
    }


    private void switchSuperUser() {
        if (DebugFlags.superUserEnabled) {
            DebugFlags.superUserEnabled = false;
            DebugFlags.unlockLevels = false;
            Scenes.notification.show("Super user disabled");
        } else {
            DebugFlags.superUserEnabled = true;
            DebugFlags.unlockLevels = true;
            Scenes.notification.show("Super user enabled");
        }
    }


    private void switchShowFps() {
        if (DebugFlags.showFps) {
            DebugFlags.showFps = false;
            Scenes.notification.show("Show fps disabled");
        } else {
            DebugFlags.showFps = true;
            Scenes.notification.show("Show fps enabled");
        }
    }
}
