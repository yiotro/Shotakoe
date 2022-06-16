package yio.tro.shotakoe.game.touch_modes;

import yio.tro.shotakoe.game.general.*;

public class TmEditActive extends TouchMode{

    Cell lastModifiedCell;
    boolean targetState;


    public TmEditActive(GameController gameController) {
        super(gameController);
    }


    @Override
    public void onModeBegin() {
        lastModifiedCell = null;
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


    void applyTouch() {
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        CellField cellField = objectsLayer.cellField;
        Cell cellByPoint = cellField.getCellByPoint(gameController.currentTouchConverted);
        if (cellByPoint == null) return;
        if (cellByPoint == lastModifiedCell) return;
        if (cellByPoint.isNearEdge()) return;
        changeActiveState(cellByPoint);
        lastModifiedCell = cellByPoint;
        cellField.updateActiveCells();
        objectsLayer.simulationCacheManager.update();
    }


    private void changeActiveState(Cell cellByPoint) {
        cellByPoint.active = targetState;
        cellByPoint.setColor(null);
        if (!cellByPoint.active) {
            onDeactivated(cellByPoint);
        }
    }


    private void onDeactivated(Cell cellByPoint) {

    }


    @Override
    public void onTouchDown() {
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        CellField cellField = objectsLayer.cellField;
        Cell cellByPoint = cellField.getCellByPoint(gameController.currentTouchConverted);
        if (cellByPoint == null) return;
        targetState = !cellByPoint.active;

        applyTouch();
    }


    @Override
    public void onTouchDrag() {
        applyTouch();
    }


    @Override
    public void onTouchUp() {
        applyTouch();
    }


    @Override
    public boolean onClick() {
        return false;
    }


    @Override
    public String getNameKey() {
        return "TmEditActive";
    }
}
