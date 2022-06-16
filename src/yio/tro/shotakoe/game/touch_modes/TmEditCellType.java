package yio.tro.shotakoe.game.touch_modes;

import yio.tro.shotakoe.game.general.*;

public class TmEditCellType extends TouchMode{

    CellType cellType;


    public TmEditCellType(GameController gameController) {
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
        if (!cellByPoint.active) return false;
        Cell currentCell = cellField.getCell(cellType);
        if (currentCell != null) {
            currentCell.setType(CellType.normal);
        }
        cellByPoint.setType(cellType);
        return true;
    }


    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }


    @Override
    public String getNameKey() {
        return "TmEditCellType";
    }
}
