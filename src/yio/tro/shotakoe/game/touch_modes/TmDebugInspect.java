package yio.tro.shotakoe.game.touch_modes;

import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.general.ObjectsLayer;

public class TmDebugInspect extends TouchMode{

    public TmDebugInspect(GameController gameController) {
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

    }


    @Override
    public void onTouchDrag() {

    }


    @Override
    public void onTouchUp() {

    }


    @Override
    public boolean onClick() {
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        CellField cellField = objectsLayer.cellField;
        Cell cellByPoint = cellField.getCellByPoint(gameController.currentTouchConverted);
        if (cellByPoint == null) return false;
        System.out.println();
        System.out.println("TmDebugInspect.onClick");
        System.out.println("cellByPoint = " + cellByPoint);
        return true;
    }


    @Override
    public String getNameKey() {
        return "TmDebugInspect";
    }
}
