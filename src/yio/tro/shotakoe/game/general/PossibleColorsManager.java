package yio.tro.shotakoe.game.general;

import java.util.ArrayList;

public class PossibleColorsManager {

    ObjectsLayer objectsLayer;
    public ArrayList<ColorYio> colors;
    private ArrayList<Cell> ownedCluster;
    WaveWorker waveSameColor;


    public PossibleColorsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        colors = new ArrayList<>();
        ownedCluster = new ArrayList<>();
        initWaves();
    }


    private void initWaves() {
        waveSameColor = new WaveWorker(getCellField()) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    public void update() {
        colors.clear();
        updateOwnedCluster();
        resetFlags();
        tagOwnedCluster();
        for (Cell cell : ownedCluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (adjacentCell.algoFlag) continue;
                if (!adjacentCell.active) continue;
                if (colors.contains(adjacentCell.color)) continue;
                colors.add(adjacentCell.color);
            }
        }
        removeColorByCellType(CellType.human_start);
        removeColorByCellType(CellType.enemy_start);
    }


    private void tagOwnedCluster() {
        for (Cell cell : ownedCluster) {
            cell.algoFlag = true;
        }
    }


    private void resetFlags() {
        for (Cell cell : getCellField().activeCells) {
            cell.algoFlag = false;
        }
    }


    private void updateOwnedCluster() {
        Cell startCell = getCurrentStartCell();
        waveSameColor.apply(startCell, ownedCluster);
    }


    private Cell getCurrentStartCell() {
        CellType startType = objectsLayer.turnsManager.getStartType();
        return getCellField().getCell(startType);
    }


    private void removeColorByCellType(CellType cellType) {
        Cell cell = getCellField().getCell(cellType);
        if (cell == null) return;
        colors.remove(cell.color);
    }


    private CellField getCellField() {
        return objectsLayer.cellField;
    }
}
