package yio.tro.shotakoe.menu;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.menu.scenes.Scenes;

public class MenuSwitcher {

    private static MenuSwitcher instance;
    private MenuControllerYio menuControllerYio;
    private YioGdxGame yioGdxGame;


    public MenuSwitcher() {
        menuControllerYio = null;
    }


    public void onMenuControllerCreated(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        yioGdxGame = menuControllerYio.yioGdxGame;
    }


    public static void initialize() {
        instance = null;
    }


    public static MenuSwitcher getInstance() {
        if (instance == null) {
            instance = new MenuSwitcher();
        }
        return instance;
    }


    public void createPauseMenu() {
        switch (getGameController().gameMode) {
            default:
                Scenes.defaultPauseMenu.create();
                break;
        }
    }


    public void createMenuOverlay() {
        Scenes.gameOverlay.create();
        switch (getGameController().gameMode) {
            default:
                Scenes.mechanicsOverlay.create();
                break;
        }
    }


    private GameController getGameController() {
        return yioGdxGame.gameController;
    }
}
