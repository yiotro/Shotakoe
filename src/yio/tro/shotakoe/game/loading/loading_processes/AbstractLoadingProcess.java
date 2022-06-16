package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.general.ObjectsLayer;
import yio.tro.shotakoe.game.loading.LoadingManager;
import yio.tro.shotakoe.game.loading.LoadingParameters;

public abstract class AbstractLoadingProcess {


    YioGdxGame yioGdxGame;
    GameController gameController;
    LoadingManager loadingManager;
    LoadingParameters loadingParameters;


    public AbstractLoadingProcess(LoadingManager loadingManager) {
        this.loadingManager = loadingManager;
        yioGdxGame = loadingManager.yioGdxGame;
        gameController = yioGdxGame.gameController;
        loadingParameters = null;
    }


    public abstract void prepare();


    public abstract void createBasicStuff();


    public abstract void createAdvancedStuff();


    public void setLoadingParameters(LoadingParameters loadingParameters) {
        this.loadingParameters = loadingParameters;
    }


    public void initGameMode(GameMode gameMode) {
        gameController.setGameMode(gameMode);
    }


    public void initLevelSize(LevelSize levelSize) {
        gameController.sizeManager.initLevelSize(levelSize);
        gameController.cameraController.onLevelBoundsSet();
    }


    protected ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }


    protected GameController getGameController() {
        return loadingManager.yioGdxGame.gameController;
    }
}
