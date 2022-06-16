package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.game.loading.LoadingManager;

public class RestartManager {

    GameController gameController;


    public RestartManager(GameController gameController) {
        this.gameController = gameController;
    }


    public void apply() {
        LoadingManager loadingManager = gameController.yioGdxGame.loadingManager;
        switch (loadingManager.previousType) {
            default:
                loadingManager.startInstantly(loadingManager.previousType, loadingManager.previousParameters);
                break;
        }
    }
}
