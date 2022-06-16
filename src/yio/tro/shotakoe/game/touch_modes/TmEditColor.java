package yio.tro.shotakoe.game.touch_modes;

import yio.tro.shotakoe.game.general.*;

public class TmEditColor extends TouchMode{

    Cell lastModifiedCell;
    ColorYio color;


    public TmEditColor(GameController gameController) {
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
        cellByPoint.setColor(color);
        cellByPoint.active = true;
        lastModifiedCell = cellByPoint;
        cellField.updateActiveCells();
        objectsLayer.simulationCacheManager.update();
    }


    @Override
    public void onTouchDown() {
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


    public void setColor(ColorYio color) {
        this.color = color;
    }


    @Override
    public String getNameKey() {
        return "TmEditColor";
    }
}
