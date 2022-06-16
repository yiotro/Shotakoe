package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.PaidStatus;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LpWorker;
import yio.tro.shotakoe.game.general.SimulationResults;
import yio.tro.shotakoe.game.loading.LoadingManager;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;

import java.util.Random;

public class SceneSimulationResults extends SceneYio {


    private ButtonYio panel;
    private LabelElement label;
    SimulationResults simulationResults;


    public SceneSimulationResults() {
        simulationResults = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainMenu));
        createPanel();
        createLabel();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(panel)
                .setSize(0.33, 0.06)
                .alignRight(0.03)
                .setTouchOffset(0.1)
                .alignBottom(0.025)
                .setBackground(BackgroundYio.gray)
                .setConvex(false)
                .applyText("next")
                .setReaction(getNextReaction());
    }


    private Reaction getNextReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onNextButtonPressed();
            }
        };
    }


    private void onNextButtonPressed() {
        LoadingManager loadingManager = yioGdxGame.loadingManager;
        LoadingParameters previousParameters = loadingManager.previousParameters;
        switch (loadingManager.previousType) {
            default:
                break;
            case skirmish:
                Scenes.setupSkirmish.create();
//                loadingParameters.setBy(previousParameters);
//                loadingParameters.setSeed(YioGdxGame.random.nextLong());
//                yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish, loadingParameters);
                break;
            case calendar:
                Scenes.calendar.create();
                break;
            case campaign:
                int targetLevelIndex;
                if (simulationResults.humanWon) {
                    targetLevelIndex = CampaignManager.getInstance().getNextLevelIndex(previousParameters.levelIndex);
                    if (targetLevelIndex == previousParameters.levelIndex) {
                        onReachedEndOfCampaign();
                        break;
                    }
                } else {
                    targetLevelIndex = previousParameters.levelIndex; // try again
                }
                CampaignManager.getInstance().launchCampaignLevel(yioGdxGame, targetLevelIndex);
                break;
        }
    }


    private void onReachedEndOfCampaign() {
        if (PaidStatus.PRO_VERSION) {
            Scenes.easterEgg.create();
            return;
        }
        Scenes.aboutProVersion.create();
    }


    private void createLabel() {
        label = uiFactory.getLabelElement()
                .setParent(panel)
                .setSize(0.01, 0.06)
                .alignTop(0.015)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        label.setTitle(generateMessage() + getAdditionalInfo());
        label.trimTitleToFitParent();
    }


    private String getAdditionalInfo() {
        if (simulationResults.humanCluster == -1) return "";
        if (simulationResults.enemyCluster == -1) return "";
        if (simulationResults.gameMode == GameMode.calendar) return "";
        if (simulationResults.gameMode == GameMode.campaign) return "";
        return " (" + simulationResults.humanCluster + " - " + simulationResults.enemyCluster + ")";
    }


    private String generateMessage() {
        switch (simulationResults.gameMode) {
            default:
            case training:
            case custom:
                if (simulationResults.humanWon) {
                    return languagesManager.getString("victory");
                } else {
                    return languagesManager.getString("defeat");
                }
            case calendar:
            case campaign:
                if (simulationResults.humanWon) {
                    String src = languagesManager.getString("level_completed");
                    String levelName = simulationResults.levelName;
                    if (levelName.length() == 0) {
                        return src.replace("[name] ", "");
                    }
                    return src.replace("[name]", levelName);
                } else {
                    return languagesManager.getString("defeat");
                }
        }
    }


    private void createPanel() {
        double height = 0.2;
        panel = uiFactory.getButton()
                .setSize(0.9, height)
                .centerHorizontal()
                .alignBottom(Math.max(0.05, 0.45 - height / 2))
                .setSilent(true)
                .setBackground(BackgroundYio.white);
    }


    public void setSimulationResults(SimulationResults simulationResults) {
        this.simulationResults = simulationResults;
    }
}
