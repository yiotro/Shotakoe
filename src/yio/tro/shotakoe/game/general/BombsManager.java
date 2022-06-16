package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;

import java.util.ArrayList;

public class BombsManager {

    ObjectsLayer objectsLayer;
    public ArrayList<Cell> bombCells;
    public ArrayList<Cell> triggerCells;
    private ArrayList<Cell> tempList;
    private ArrayList<Cell> currentCluster;
    WaveWorker waveSameColor;
    public boolean readyToExplode;
    public boolean explosionHappened;
    public CellType culprit;


    public BombsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        bombCells = new ArrayList<>();
        triggerCells = new ArrayList<>();
        tempList = new ArrayList<>();
        currentCluster = new ArrayList<>();
        readyToExplode = false;
        explosionHappened = false;
        culprit = null;
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


    public void onTurnMade() {
        checkToTriggerExplosion(CellType.human_start);
        checkToTriggerExplosion(CellType.enemy_start);
    }


    public boolean isCapturingAllowed() {
        return !explosionHappened;
    }


    void move() {
        if (!readyToExplode) return;
        FactorYio effectFactor = objectsLayer.recolorManager.effectFactor;
        if (effectFactor.isInAppearState() && effectFactor.getValue() < 0.9) return;
        for (Cell cell : bombCells) {
            objectsLayer.particlesManager.spawnExplosion(cell.position.center);
        }
        SoundManager.playSound(SoundType.explosion);
        readyToExplode = false;
        explosionHappened = true;
    }


    private void checkToTriggerExplosion(CellType cellType) {
        if (readyToExplode) return;
        if (explosionHappened) return;
        if (!areTriggerCellsCaptured(cellType)) return;
        readyToExplode = true;
        if (objectsLayer.getLoadingParameters().lgParameters.sameStartingCluster) {
            TurnsManager turnsManager = objectsLayer.turnsManager;
            TurnType nextTurnType = turnsManager.getNextTurnType();
            culprit = turnsManager.convertToCellType(nextTurnType);
        } else {
            culprit = cellType;
        }
    }


    private boolean areTriggerCellsCaptured(CellType cellType) {
        Cell startCell = getCellField().getCell(cellType);
        if (startCell == null) return false;
        waveSameColor.apply(startCell, currentCluster);
        resetFlags();
        for (Cell cell : currentCluster) {
            cell.algoFlag = true;
        }
        for (Cell triggerCell : triggerCells) {
            if (triggerCell.algoFlag) return true;
        }
        return false;
    }


    public void onCellTypeChanged(Cell cell, CellType previousType) {
        if (cell.type == CellType.bomb) {
            bombCells.add(cell);
        }
        if (previousType == CellType.bomb) {
            bombCells.remove(cell);
        }
        updateTriggerCells();
    }


    private void updateTriggerCells() {
        triggerCells.clear();
        resetFlags();
        for (Cell cell : bombCells) {
            waveSameColor.apply(cell, currentCluster);
            doProcessCluster(currentCluster);
            doExpandCluster(currentCluster);
            triggerCells.remove(cell);
        }
    }


    private void doProcessCluster(ArrayList<Cell> cluster) {
        for (Cell cell : cluster) {
            cell.algoFlag = true;
            triggerCells.add(cell);
        }
    }


    private void doExpandCluster(ArrayList<Cell> cluster) {
        for (Cell cell : cluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (!adjacentCell.active) continue;
                if (adjacentCell.color == cell.color) continue;
                if (adjacentCell.algoFlag) continue;
                waveSameColor.apply(adjacentCell, tempList);
                doProcessCluster(tempList);
            }
        }
    }


    private void resetFlags() {
        for (Cell cell : getCellField().activeCells) {
            cell.algoFlag = false;
        }
    }


    private CellField getCellField() {
        return objectsLayer.cellField;
    }
}
