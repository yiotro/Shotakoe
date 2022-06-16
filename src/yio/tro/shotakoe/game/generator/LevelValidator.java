package yio.tro.shotakoe.game.generator;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;

public class LevelValidator {

    private final CellField cellField;
    ObjectsLayer objectsLayer;
    public ArrayList<AlgoCluster> algoClusters;
    ArrayList<Cell> tempList;
    WaveWorker waveSameColor;
    PointYio center;
    private ArrayList<AlgoCluster> propagationList;
    private ArrayList<AlgoCluster> tempClusters;


    public LevelValidator(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        cellField = objectsLayer.cellField;
        algoClusters = new ArrayList<>();
        tempList = new ArrayList<>();
        center = new PointYio();
        propagationList = new ArrayList<>();
        tempClusters = new ArrayList<>();
        initWaves();
    }


    private void initWaves() {
        waveSameColor = new WaveWorker(cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    boolean apply() {
        generateGraph();
        updateCenter();
        if (detectUnfairDistanceToCenter()) return false;
        if (detectUnfairDistanceToBomb()) return false;
        if (detectUnfairClusterDensity(4)) return false;
        if (detectUnfairClusterDensity(3)) return false;
        if (detectBombTooBig()) return false;
        if (detectUnfairStartingClusters()) return false;
        if (detectUnfairObstacles(5)) return false;
        return true;
    }


    private boolean detectUnfairObstacles(int depth) {
        AlgoCluster humanCluster = getCluster(CellType.human_start);
        if (humanCluster == null) return false;
        scanArea(humanCluster, depth);
        float humanAverageNeighbours = calculateAverageNeighbours(tempClusters);
        AlgoCluster enemyCluster = getCluster(CellType.enemy_start);
        if (enemyCluster == null) return false;
        scanArea(enemyCluster, depth);
        float enemyAverageNeighbours = calculateAverageNeighbours(tempClusters);
        float ratio = enemyAverageNeighbours / humanAverageNeighbours;
        if (cellField.contains(CellType.bomb) && ratio < 0.95f) return true;
        return ratio > 1.1f;
    }


    private float calculateAverageNeighbours(ArrayList<AlgoCluster> list) {
        float sum = 0;
        for (AlgoCluster cluster : list) {
            sum += cluster.adjacentClusters.size();
        }
        return sum / list.size();
    }


    private boolean detectUnfairStartingClusters() {
        AlgoCluster enemyCluster = getCluster(CellType.enemy_start);
        if (enemyCluster == null) return false;
        AlgoCluster humanCluster = getCluster(CellType.human_start);
        if (humanCluster == null) return false;
        float ratio = (float) humanCluster.cells.size() / (float) enemyCluster.cells.size();
        if (cellField.contains(CellType.bomb)) {
            return ratio > 2f;
        }
        return ratio < 0.66f;
    }


    private boolean detectUnfairDistanceToBomb() {
        AlgoCluster enemyCluster = getCluster(CellType.enemy_start);
        if (enemyCluster == null) return false;
        AlgoCluster humanCluster = getCluster(CellType.human_start);
        if (humanCluster == null) return false;
        AlgoCluster bombCluster = getCluster(CellType.bomb);
        if (bombCluster == null) return false;
        findWayToCluster(enemyCluster, bombCluster);
        int enemyDistance = tempClusters.size();
        findWayToCluster(humanCluster, bombCluster);
        int humanDistance = tempClusters.size();
        float ratio = (float) humanDistance / (float) enemyDistance;
        return ratio < 0.95f;
    }


    private boolean detectBombTooBig() {
        AlgoCluster bombCluster = getCluster(CellType.bomb);
        if (bombCluster == null) return false;
        scanArea(bombCluster, 1);
        float averageSize = calculateAverageSize(tempClusters);
        if (averageSize >= 4) return true;
        int sumSize = calculateSumSize(tempClusters);
        float bombValue = (float) sumSize / (float) cellField.activeCells.size();
        if (bombValue > 0.08f) return true;
        return false;
    }


    private int calculateSumSize(ArrayList<AlgoCluster> list) {
        int sum = 0;
        for (AlgoCluster cluster : list) {
            sum += cluster.cells.size();
        }
        return sum;
    }


    private boolean detectUnfairClusterDensity(int depth) {
        AlgoCluster humanCluster = getCluster(CellType.human_start);
        if (humanCluster == null) return false;
        scanArea(humanCluster, depth);
        float humanAverageSize = calculateAverageSize(tempClusters);
        AlgoCluster enemyCluster = getCluster(CellType.enemy_start);
        if (enemyCluster != null) {
            scanArea(enemyCluster, depth);
            float enemyAverageSize = calculateAverageSize(tempClusters);
            float ratio = enemyAverageSize / humanAverageSize;
            if (cellField.contains(CellType.bomb) && ratio < 0.95f) return true;
            return ratio > 1.1f;
        } else {
            return humanAverageSize < 3f;
        }
    }


    private float calculateAverageSize(ArrayList<AlgoCluster> list) {
        float sum = 0;
        for (AlgoCluster cluster : list) {
            sum += cluster.cells.size();
        }
        return sum / list.size();
    }


    private void scanArea(AlgoCluster startCluster, int steps) {
        prepareClustersForWaveAlgorithm();
        propagationList.clear();
        addToPropagationList(startCluster);
        tempClusters.clear();
        while (propagationList.size() > 0) {
            AlgoCluster firstCluster = propagationList.get(0);
            propagationList.remove(firstCluster);
            tempClusters.add(firstCluster);
            if (firstCluster.waveValue == steps) continue;
            for (AlgoCluster adjacentCluster : firstCluster.adjacentClusters) {
                if (adjacentCluster.waveFlag) continue;
                addToPropagationList(adjacentCluster);
                onClusterReachedByWave(adjacentCluster, firstCluster);
            }
        }
    }


    private boolean detectUnfairDistanceToCenter() {
        AlgoCluster enemyCluster = getCluster(CellType.enemy_start);
        if (enemyCluster == null) return false;
        AlgoCluster humanCluster = getCluster(CellType.human_start);
        if (humanCluster == null) return false;
        AlgoCluster centerCluster = findCenterCluster();
        if (centerCluster == null) return false;
        findWayToCluster(enemyCluster, centerCluster);
        int enemyDistance = tempClusters.size();
        findWayToCluster(humanCluster, centerCluster);
        int humanDistance = tempClusters.size();
        float ratio = (float) humanDistance / (float) enemyDistance;
        if (cellField.contains(CellType.bomb) && ratio < 0.9f) return true;
        return ratio > 1.1f;
    }


    private AlgoCluster findCenterCluster() {
        Cell closestCellInList = getClosestCellInList(center, cellField.activeCells);
        return closestCellInList.algoCluster;
    }


    private Cell getClosestCellInList(PointYio pointYio, ArrayList<Cell> list) {
        Cell bestCell = null;
        float minDistance = 0;
        for (Cell cell : list) {
            float currentDistance = cell.position.center.distanceTo(pointYio);
            if (bestCell == null || currentDistance < minDistance) {
                bestCell = cell;
                minDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private void findWayToCluster(AlgoCluster startCluster, AlgoCluster targetCluster) {
        prepareClustersForWaveAlgorithm();
        propagationList.clear();
        addToPropagationList(startCluster);
        while (propagationList.size() > 0 && targetCluster.wavePointer == null) {
            AlgoCluster firstCluster = propagationList.get(0);
            propagationList.remove(firstCluster);
            for (AlgoCluster adjacentCluster : firstCluster.adjacentClusters) {
                if (adjacentCluster.waveFlag) continue;
                addToPropagationList(adjacentCluster);
                onClusterReachedByWave(adjacentCluster, firstCluster);
            }
        }
        AlgoCluster cluster = targetCluster;
        tempClusters.clear();
        while (cluster != null) {
            tempClusters.add(cluster);
            if (cluster.wavePointer == startCluster) break;
            cluster = cluster.wavePointer;
        }
    }


    private void onClusterReachedByWave(AlgoCluster cluster, AlgoCluster previousCluster) {
        cluster.wavePointer = previousCluster;
        if (previousCluster != null) {
            cluster.waveValue = previousCluster.waveValue + 1;
        }
    }


    private void addToPropagationList(AlgoCluster cluster) {
        propagationList.add(cluster);
        cluster.waveFlag = true;
    }


    private void prepareClustersForWaveAlgorithm() {
        for (AlgoCluster cluster : algoClusters) {
            cluster.waveFlag = false;
            cluster.wavePointer = null;
            cluster.waveValue = 0;
        }
    }


    AlgoCluster getCluster(CellType type) {
        for (AlgoCluster cluster : algoClusters) {
            if (cluster.type == type) return cluster;
        }
        return null;
    }


    private void updateCenter() {
        center.set(
                cellField.getMiddleX(),
                cellField.getMiddleY()
        );
    }


    private void generateGraph() {
        algoClusters.clear();
        generateClusters();
        linkClusters();
        updateClusterTypes();
    }


    private void linkClusters() {
        for (Cell cell : cellField.activeCells) {
            tryToLinkInDirection(cell, Direction.right);
            tryToLinkInDirection(cell, Direction.up);
        }
    }


    private void tryToLinkInDirection(Cell cell, Direction direction) {
        Cell adjacentCell = cell.getAdjacentCell(direction);
        if (!adjacentCell.active) return;
        if (adjacentCell.color == cell.color) return;
        AlgoCluster cluster = cell.algoCluster;
        AlgoCluster adjacentCluster = adjacentCell.algoCluster;
        if (cluster.isLinkedTo(adjacentCluster)) return;
        cluster.adjacentClusters.add(adjacentCluster);
        adjacentCluster.adjacentClusters.add(cluster);
    }


    private void generateClusters() {
        resetFlags();
        for (Cell cell : cellField.activeCells) {
            if (cell.algoFlag) continue;
            waveSameColor.apply(cell, tempList);
            addClusterByTempList();
            tagTempList();
        }
    }


    private void addClusterByTempList() {
        AlgoCluster freshObject = new AlgoCluster();
        algoClusters.add(freshObject);
        for (Cell cell : tempList) {
            freshObject.cells.add(cell);
            cell.algoCluster = freshObject;
        }
        freshObject.updateGeometricCenter();
    }


    private void updateClusterTypes() {
        for (AlgoCluster cluster : algoClusters) {
            cluster.type = CellType.normal;
        }
        for (CellType cellType : CellType.values()) {
            if (cellType == CellType.normal) continue;
            Cell cell = cellField.getCell(cellType);
            if (cell == null) continue;
            cell.algoCluster.type = cellType;
        }
    }


    private void resetFlags() {
        for (Cell cell : cellField.activeCells) {
            cell.algoFlag = false;
        }
    }


    private void tagTempList() {
        for (Cell cell : tempList) {
            cell.algoFlag = true;
        }
    }
}
