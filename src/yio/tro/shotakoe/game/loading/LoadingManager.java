package yio.tro.shotakoe.game.loading;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.loading.loading_processes.*;
import yio.tro.shotakoe.game.view.GameView;
import yio.tro.shotakoe.menu.MenuSwitcher;

public class LoadingManager {

    public YioGdxGame yioGdxGame;
    GameController gameController;
    GameView gameView;
    int w, h;
    public boolean working;
    int currentStep;
    AbstractLoadingProcess currentProcess;
    public LoadingType previousType;
    public LoadingParameters previousParameters;


    public LoadingManager(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        gameView = yioGdxGame.gameView;
        w = gameController.w;
        h = gameController.h;
        working = false;
        currentProcess = null;
        currentStep = -1;
    }


    public void startLoading(LoadingType type, LoadingParameters loadingParameters) {
        if (!checkParametersValidity(loadingParameters)) {
            System.out.println("LoadingManager.startLoading: invalid loading parameters");
            return;
        }
        previousType = type;
        previousParameters = loadingParameters;
        if (!DebugFlags.testingModeEnabled) {
            Yio.safeSay("Loading level...");
        }
        working = true;
        currentStep = 0;
        currentProcess = null;
        createProcess(type);
        currentProcess.setLoadingParameters(loadingParameters);
    }


    private boolean checkParametersValidity(LoadingParameters loadingParameters) {
        if (loadingParameters.levelSize == null) return false;
        if (loadingParameters.fieldWidth == -1) return false;
        if (loadingParameters.fieldHeight == -1) return false;
        return true;
    }


    private void createProcess(LoadingType loadingType) {
        switch (loadingType) {
            default:
                currentProcess = new ProcessEmpty(this);
                break;
            case skirmish:
                currentProcess = new ProcessSkirmish(this);
                break;
            case calendar:
                currentProcess = new ProcessCalendar(this);
                break;
            case campaign:
                currentProcess = new ProcessCampaign(this);
                break;
        }
    }


    public void move() {
        switch (currentStep) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                gameController.defaultValues();
                currentProcess.prepare();
                break;
            case 4:
                gameController.createCamera();
                gameController.createObjectsLayer();
                break;
            case 5:
                currentProcess.createBasicStuff();
                break;
            case 6:
                gameController.onBasicStuffCreated();
                break;
            case 7:
                gameView.onBasicStuffCreated();
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                currentProcess.createAdvancedStuff();
                gameController.onAdvancedStuffCreated();
                prepareMenu();
                endCreation();
                break;
        }

        currentStep++;
    }


    private void endCreation() {
        yioGdxGame.setGamePaused(false);
        gameController.objectsLayer.onEndCreation();
        gameView.updateAnimationTexture();
        working = false;
    }


    public void startInstantly(LoadingType type, LoadingParameters loadingParameters) {
        startLoading(type, loadingParameters);

        while (working) {
            move();
        }
    }


    private void prepareMenu() {
        MenuSwitcher.getInstance().createMenuOverlay();
    }


}
