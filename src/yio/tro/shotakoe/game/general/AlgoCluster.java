package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class AlgoCluster implements ReusableYio {

    public ArrayList<Cell> cells;
    public ArrayList<AlgoCluster> adjacentClusters;
    public CellType type;
    public PointYio geometricCenter;
    public boolean waveFlag;
    public AlgoCluster wavePointer;
    public int waveValue;


    public AlgoCluster() {
        cells = new ArrayList<>();
        adjacentClusters = new ArrayList<>();
        geometricCenter = new PointYio();
    }


    @Override
    public void reset() {
        cells.clear();
        adjacentClusters.clear();
        geometricCenter.reset();
        type = null;
        waveFlag = false;
        wavePointer = null;
        waveValue = 0;
    }


    public ColorYio getColor() {
        if (cells.size() == 0) return null;
        return cells.get(0).color;
    }


    public boolean isLinkedTo(AlgoCluster algoCluster) {
        return adjacentClusters.contains(algoCluster);
    }


    public boolean isLinkedTo(CellType cellType) {
        for (AlgoCluster adjacentCluster : adjacentClusters) {
            if (adjacentCluster.type == cellType) return true;
        }
        return false;
    }


    public boolean isNot(CellType cellType) {
        return type != cellType;
    }


    public void updateGeometricCenter() {
        geometricCenter.reset();
        for (Cell cell : cells) {
            geometricCenter.add(cell.position.center);
        }
        geometricCenter.x /= cells.size();
        geometricCenter.y /= cells.size();
    }


    @Override
    public String toString() {
        return "[AlgoCluster: " +
                cells.size() +  " " + getColor() +
                "]";
    }
}
