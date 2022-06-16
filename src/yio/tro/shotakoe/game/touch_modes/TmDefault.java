package yio.tro.shotakoe.game.touch_modes;

import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.game.view.game_renders.GameRender;
import yio.tro.shotakoe.game.view.game_renders.GameRendersList;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class TmDefault extends TouchMode {

    public boolean touchedCurrently;


    public TmDefault(GameController gameController) {
        super(gameController);
    }


    @Override
    public void onModeBegin() {

    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {

    }


    @Override
    public boolean isCameraMovementEnabled() {
        return false;
    }


    @Override
    public void onTouchDown() {
        if (getObjectsLayer().finishManager.levelCompleted) return;
        touchedCurrently = true;
        getObjectsLayer().colorButtonsManager.onTouchDown();
    }


    @Override
    public void onTouchDrag() {
        if (getObjectsLayer().finishManager.levelCompleted) return;

    }


    @Override
    public void onTouchUp() {
        touchedCurrently = false;
    }


    @Override
    public boolean onClick() {
        getObjectsLayer().colorButtonsManager.onClick();
        return false;
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmDefault;
    }


    private CellField getCellField() {
        return getObjectsLayer().cellField;
    }


    private ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    @Override
    public String getNameKey() {
        return null;
    }
}
