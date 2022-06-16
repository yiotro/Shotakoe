package yio.tro.shotakoe.menu.reactions;

import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.YioGdxGame;

public abstract class Reaction {

    public static RbNothing rbNothing;
    protected GameController gameController;
    protected MenuControllerYio menuControllerYio;
    protected YioGdxGame yioGdxGame;


    public static void initialize() {
        rbNothing = new RbNothing();
    }


    protected abstract void apply();


    public void perform(MenuControllerYio menuControllerYio) {
        if (menuControllerYio != null) {
            this.menuControllerYio = menuControllerYio;
            yioGdxGame = menuControllerYio.yioGdxGame;
            gameController = yioGdxGame.gameController;
        }
        apply();
    }


}
