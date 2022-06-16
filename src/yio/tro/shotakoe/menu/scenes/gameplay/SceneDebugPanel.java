package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.export.ExportManager;
import yio.tro.shotakoe.game.export.ExportParameters;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.CheckButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;

public class SceneDebugPanel extends ModalSceneYio {


    private CheckButtonYio chkDebugEnabled;
    private CheckButtonYio chkShowClusters;


    @Override
    protected void initialize() {
        createDarken();
        createCloseButton();
        createDefaultPanel(0.35);
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        chkDebugEnabled.setChecked(DebugFlags.debugEnabled);
        chkShowClusters.setChecked(DebugFlags.showClusters);
    }


    private void createInternals() {
        createCheckButtons();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.06)
                .centerHorizontal()
                .setBackground(BackgroundYio.gray)
                .alignBottom(0.03)
                .applyText("Debug inspect")
                .setReaction(getDebugInspectReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignAbove(previousElement, 0.01)
                .applyText("Export")
                .setReaction(getExportReaction());
    }


    private Reaction getExportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                doExport();
            }
        };
    }


    public void doExport() {
        ExportParameters parameters = new ExportParameters();
        parameters.setObjectsLayer(getObjectsLayer());
        String levelCode = (new ExportManager()).applyToClipboard(parameters);
        System.out.println("SceneDebugPanel.doExport: " + levelCode);
        Scenes.notification.show("Exported");
    }


    private Reaction getDebugInspectReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                gameController.setTouchMode(TouchMode.tmDebugInspect);
            }
        };
    }


    void openCurrentLevelInEditor() {
        System.out.println("SceneDebugPanel.openCurrentLevelInEditor");
    }


    private void createCheckButtons() {
        chkDebugEnabled = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setName("Debug enabled")
                .alignTop(0.02)
                .setReaction(getDebugEnabledReaction());

        chkShowClusters = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setName("Show clusters graph")
                .alignUnder(previousElement, 0)
                .setReaction(getShowClustersReaction());
    }


    private Reaction getShowClustersReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                DebugFlags.showClusters = !DebugFlags.showClusters;
                if (DebugFlags.showClusters) {
                    DebugFlags.debugEnabled = true;
                    chkDebugEnabled.setChecked(true);
                }
            }
        };
    }


    private Reaction getDebugEnabledReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onChkDebugEnabledPressed();
            }
        };
    }


    private void onChkDebugEnabledPressed() {
        DebugFlags.debugEnabled = chkDebugEnabled.isChecked();
        getGameController().resetTouchMode();
    }

}
