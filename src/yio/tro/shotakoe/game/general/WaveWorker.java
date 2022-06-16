package yio.tro.shotakoe.game.general;

import java.util.ArrayList;

public abstract class WaveWorker {

    CellField cellField;
    ArrayList<Cell> propagationList;
    ArrayList<Cell> targetList;


    public WaveWorker(CellField cellField) {
        this.cellField = cellField;
        propagationList = new ArrayList<>();
        targetList = null;
    }


    protected abstract boolean condition(Cell cell, Cell parentCell);


    protected void onCellReached(Cell cell, Cell parentCell) {
        if (targetList == null) {
            System.out.println("WaveWorker.onCellReached: should probably override this method");
            return;
        }
        targetList.add(cell);
    }


    public void apply(Cell startCell) {
        apply(startCell, null);
    }


    public void apply(Cell startCell, ArrayList<Cell> targetList) {
        this.targetList = targetList;
        if (targetList != null) {
            targetList.clear();
        }
        resetFlags();
        propagationList.clear();
        addToList(startCell);
        onCellReached(startCell, null);
        while (propagationList.size() > 0) {
            Cell firstCell = propagationList.get(0);
            propagationList.remove(firstCell);
            for (Cell adjacentCell : firstCell.adjacentCells) {
                if (!isAdjacentCellValid(adjacentCell)) continue;
                if (!condition(adjacentCell, firstCell)) continue;
                addToList(adjacentCell);
                onCellReached(adjacentCell, firstCell);
            }
        }
    }


    protected boolean isAdjacentCellValid(Cell adjacentCell) {
        if (adjacentCell == null) return false;
        if (!adjacentCell.active) return false;
        if (adjacentCell.waveFlag) return false;
        return true;
    }


    void addToList(Cell cell) {
        cell.waveFlag = true;
        propagationList.add(cell);
    }


    protected void resetFlags() {
        for (Cell cell : cellField.activeCells) {
            cell.waveFlag = false;
        }
    }
}
