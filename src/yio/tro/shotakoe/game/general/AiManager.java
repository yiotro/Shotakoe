package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;

public class AiManager {

    ObjectsLayer objectsLayer;
    ArrayList<ColorYio> possibleColors;
    WaveWorker waveSameColor;
    ArrayList<Cell> tempList;
    ArrayList<Cell> ownedCluster;
    HashMap<ColorYio, Integer> mapColorValues;
    public ArrayList<AlgoCluster> algoClusters;
    ObjectPoolYio<AlgoCluster> poolClusters;
    PointYio center;
    private ArrayList<AlgoCluster> propagationList;
    ArrayList<AlgoCluster> disputedClusters;
    boolean active;
    private ArrayList<ColorYio> triggerColors;


    public AiManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        possibleColors = new ArrayList<>();
        ownedCluster = new ArrayList<>();
        tempList = new ArrayList<>();
        mapColorValues = new HashMap<>();
        algoClusters = new ArrayList<>();
        center = new PointYio();
        propagationList = new ArrayList<>();
        disputedClusters = new ArrayList<>();
        active = false;
        triggerColors = new ArrayList<>();
        initWaves();
        initPools();
    }


    private void initPools() {
        poolClusters = new ObjectPoolYio<AlgoCluster>(algoClusters) {
            @Override
            public AlgoCluster makeNewObject() {
                return new AlgoCluster();
            }
        };
    }


    private void initWaves() {
        waveSameColor = new WaveWorker(getCellField()) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    private CellField getCellField() {
        return objectsLayer.cellField;
    }


    void move() {
        if (!active) return;
        if (!objectsLayer.turnsManager.isAiTurn()) return;
        if (objectsLayer.recolorManager.isInProcessOfAnimation()) return;
        if (!objectsLayer.gameController.yioGdxGame.gameView.coversAllScreen()) return;
        if (!objectsLayer.isCapturingAllowed()) return;
        updatePossibleColors();
        if (possibleColors.size() == 0) return;
        Cell startCell = getCellField().getCell(CellType.enemy_start);
        ColorYio bestColor = apply(startCell);
        if (bestColor == null || !possibleColors.contains(bestColor)) {
            System.out.println("AiManager.move: problem, AI have chosen unavailable color " + bestColor);
            objectsLayer.onTurnMade();
            return;
        }
        objectsLayer.recolorManager.onColorChosen(bestColor, CellType.enemy_start);
    }


    private ColorYio apply(Cell startCell) {
        updateTriggerColors(startCell);
        if (shouldActivelyAvoidBomb()) return chooseColorToAvoidBomb(startCell);
        switch (objectsLayer.difficulty) {
            default:
                System.out.println("AiManager.apply: not defined");
                return applyExpert(startCell);
            case easy:
                return getRandomPossibleSafeColor();
            case normal:
                return applyNormal(startCell);
            case hard:
                return applyHard(startCell);
            case expert:
                return applyExpert(startCell);
        }
    }


    private ColorYio chooseColorToAvoidBomb(Cell startCell) {
        updateOwnedCluster(startCell);
        resetFlags();
        tagOwnedCluster();
        ColorYio colorForMinimumProfit = findColorForMinimumProfit();
        if (colorForMinimumProfit == null) return getRandomPossibleSafeColor();
        return colorForMinimumProfit;
    }


    private ColorYio findColorForMinimumProfit() {
        ColorYio bestColor = null;
        int minProfit = 0;
        for (ColorYio color : possibleColors) {
            if (!isColorSafe(color)) continue;
            int currentProfit = calculatePossibleProfit(color);
            if (bestColor == null || currentProfit < minProfit) {
                bestColor = color;
                minProfit = currentProfit;
            }
        }
        return bestColor;
    }


    private boolean shouldActivelyAvoidBomb() {
        LoadingParameters loadingParameters = objectsLayer.getLoadingParameters();
        LgParameters lgParameters = loadingParameters.lgParameters;
        if (!lgParameters.bomb) return false;
        if (lgParameters.sameStartingCluster) return false;
        if (!lgParameters.enemy) return false;
        return true;
    }


    private void updateTriggerColors(Cell startCell) {
        triggerColors.clear();
        if (objectsLayer.bombsManager.bombCells.size() == 0) return;
        updateOwnedCluster(startCell);
        resetFlags();
        for (Cell triggerCell : objectsLayer.bombsManager.triggerCells) {
            triggerCell.algoFlag = true;
        }
        for (Cell cell : ownedCluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (!adjacentCell.active) continue;
                if (cell.color == adjacentCell.color) continue;
                if (!adjacentCell.algoFlag) continue;
                ColorYio color = adjacentCell.color;
                if (triggerColors.contains(color)) continue;
                triggerColors.add(color);
            }
        }
    }


    private ColorYio applyExpert(Cell startCell) {
        generateGraph();
        updateCenter();
        AlgoCluster centerCluster = findCenterCluster();
        if (centerCluster.type == CellType.normal) {
            return tryToSafelyGoToCluster(startCell.algoCluster, centerCluster);
        }
        AlgoCluster clusterWithMaxNeighbours = findNormalClusterWithMaxNeighbours();
        if (clusterWithMaxNeighbours != null && clusterWithMaxNeighbours.cells.size() > 6) {
            return tryToSafelyGoToCluster(startCell.algoCluster, clusterWithMaxNeighbours);
        }
        ColorYio disputeColor = tryToCaptureDisputedClusters(startCell);
        if (disputeColor != null) {
            return disputeColor;
        }
        return applyNormal(startCell);
    }


    private ColorYio tryToCaptureDisputedClusters(Cell startCell) {
        updateDisputedClusters();
        for (ColorYio color : possibleColors) {
            if (!isColorSafe(color)) continue;
            if (calculatePossibleDisputedGain(color) < 3) continue;
            return color;
        }
        return null;
    }


    private int calculatePossibleDisputedGain(ColorYio colorYio) {
        int sum = 0;
        for (AlgoCluster cluster : disputedClusters) {
            if (cluster.getColor() != colorYio) continue;
            sum += cluster.cells.size();
        }
        return sum;
    }


    private void updateDisputedClusters() {
        disputedClusters.clear();
        for (AlgoCluster cluster : algoClusters) {
            if (cluster.isNot(CellType.normal)) continue;
            if (!isDisputed(cluster)) continue;
            disputedClusters.add(cluster);
        }
    }


    private boolean isDisputed(AlgoCluster cluster) {
        return cluster.isLinkedTo(CellType.human_start) && cluster.isLinkedTo(CellType.enemy_start);
    }


    private ColorYio tryToSafelyGoToCluster(AlgoCluster startCluster, AlgoCluster targetCluster) {
        ColorYio wayToCluster = findWayToCluster(startCluster, targetCluster);
        if (!possibleColors.contains(wayToCluster) || !isColorSafe(wayToCluster)) {
            return applyHard(startCluster.cells.get(0));
        }
        return wayToCluster;
    }


    private AlgoCluster findNormalClusterWithMaxNeighbours() {
        AlgoCluster bestCluster = null;
        int maxNeighbours = 0;
        for (AlgoCluster cluster : algoClusters) {
            if (cluster.isNot(CellType.normal)) continue;
            int currentNeighbours = cluster.adjacentClusters.size();
            if (bestCluster == null || currentNeighbours > maxNeighbours) {
                bestCluster = cluster;
                maxNeighbours = currentNeighbours;
            }
        }
        return bestCluster;
    }


    private ColorYio findWayToCluster(AlgoCluster startCluster, AlgoCluster targetCluster) {
        prepareClustersForWaveAlgorithm();
        propagationList.clear();
        addToPropagationList(startCluster);
        while (propagationList.size() > 0 && targetCluster.wavePointer == null) {
            AlgoCluster firstCluster = propagationList.get(0);
            propagationList.remove(firstCluster);
            for (AlgoCluster adjacentCluster : firstCluster.adjacentClusters) {
                if (adjacentCluster.waveFlag) continue;
                if (adjacentCluster.type == CellType.human_start) continue;
                addToPropagationList(adjacentCluster);
                onClusterReachedByWave(adjacentCluster, firstCluster);
            }
        }
        AlgoCluster cluster = targetCluster;
        while (cluster != null) {
            if (cluster.wavePointer == startCluster) return cluster.getColor();
            cluster = cluster.wavePointer;
        }
        return getRandomPossibleSafeColor();
    }


    private void onClusterReachedByWave(AlgoCluster cluster, AlgoCluster previousCluster) {
        cluster.wavePointer = previousCluster;
    }


    private void addToPropagationList(AlgoCluster cluster) {
        propagationList.add(cluster);
        cluster.waveFlag = true;
    }


    private void prepareClustersForWaveAlgorithm() {
        for (AlgoCluster cluster : algoClusters) {
            cluster.waveFlag = false;
            cluster.wavePointer = null;
        }
    }


    private AlgoCluster findCenterCluster() {
        Cell closestCellInList = getClosestCellInList(center, getCellField().activeCells);
        return closestCellInList.algoCluster;
    }


    private void updateCenter() {
        center.set(
                getCellField().getMiddleX(),
                getCellField().getMiddleY()
        );
    }


    private void generateGraph() {
        poolClusters.clearExternalList();
        generateClusters();
        linkClusters();
        updateClusterTypes();
    }


    private void updateClusterTypes() {
        for (AlgoCluster cluster : algoClusters) {
            cluster.type = CellType.normal;
        }
        Cell startCell = getCellField().getCell(CellType.human_start);
        if (startCell != null) {
            startCell.algoCluster.type = CellType.human_start;
        }
        Cell enemyStartCell = getCellField().getCell(CellType.enemy_start);
        if (enemyStartCell != null) {
            enemyStartCell.algoCluster.type = CellType.enemy_start;
        }
    }


    private void linkClusters() {
        for (Cell cell : getCellField().activeCells) {
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
        for (Cell cell : getCellField().activeCells) {
            if (cell.algoFlag) continue;
            waveSameColor.apply(cell, tempList);
            addClusterByTempList();
            tagTempList();
        }
    }


    private void addClusterByTempList() {
        AlgoCluster freshObject = poolClusters.getFreshObject();
        for (Cell cell : tempList) {
            freshObject.cells.add(cell);
            cell.algoCluster = freshObject;
        }
        freshObject.updateGeometricCenter();
    }


    private ColorYio applyHard(Cell startCell) {
        updateOwnedCluster(startCell);
        resetFlags();
        tagOwnedCluster();
        if (ownedCluster.size() > 0.05f * getCellField().activeCells.size()) {
            return applyNormal(startCell);
        }
        ColorYio colorForMaximumLength = findColorForMaximumLength(startCell);
        if (colorForMaximumLength == null) return getRandomPossibleSafeColor();
        return colorForMaximumLength;
    }


    private ColorYio findColorForMaximumLength(Cell startCell) {
        ColorYio bestColor = null;
        float maxDistance = 0;
        for (ColorYio color : possibleColors) {
            if (!isColorSafe(color)) continue;
            float currentDistance = calculatePossibleDistance(startCell, color);
            if (bestColor == null || currentDistance > maxDistance) {
                bestColor = color;
                maxDistance = currentDistance;
            }
        }
        return bestColor;
    }


    private float calculatePossibleDistance(Cell startCell, ColorYio colorYio) {
        float maxDistance = 0;
        for (Cell cell : ownedCluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (!adjacentCell.active) continue;
                if (adjacentCell.algoFlag) continue;
                if (adjacentCell.color != colorYio) continue;
                waveSameColor.apply(adjacentCell, tempList);
                if (tempList.size() == 0) continue;
                Cell furthestCellInList = getFurthestCellInList(startCell, tempList);
                maxDistance = Math.max(maxDistance, furthestCellInList.position.center.distanceTo(startCell.position.center));
                tagTempList();
            }
        }
        return maxDistance;
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


    private Cell getFurthestCellInList(Cell hookCell, ArrayList<Cell> list) {
        Cell bestCell = null;
        float maxDistance = 0;
        for (Cell cell : list) {
            float currentDistance = cell.position.center.distanceTo(hookCell.position.center);
            if (bestCell == null || currentDistance > maxDistance) {
                bestCell = cell;
                maxDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private ColorYio applyNormal(Cell startCell) {
        updateOwnedCluster(startCell);
        resetFlags();
        tagOwnedCluster();
        ColorYio colorForMaximumProfit = findColorForMaximumProfit();
        if (colorForMaximumProfit == null) return getRandomPossibleSafeColor();
        return colorForMaximumProfit;
    }


    private ColorYio findColorForMaximumProfit() {
        ColorYio bestColor = null;
        int maxProfit = 0;
        for (ColorYio color : possibleColors) {
            if (!isColorSafe(color)) continue;
            int currentProfit = calculatePossibleProfit(color);
            if (bestColor == null || currentProfit > maxProfit) {
                bestColor = color;
                maxProfit = currentProfit;
            }
        }
        return bestColor;
    }


    private boolean isColorSafe(ColorYio color) {
        return !triggerColors.contains(color);
    }


    private int calculatePossibleProfit(ColorYio colorYio) {
        int sum = 0;
        for (Cell cell : ownedCluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (!adjacentCell.active) continue;
                if (adjacentCell.algoFlag) continue;
                if (adjacentCell.color != colorYio) continue;
                waveSameColor.apply(adjacentCell, tempList);
                sum += tempList.size();
                tagTempList();
            }
        }
        return sum;
    }


    private void tagTempList() {
        for (Cell cell : tempList) {
            cell.algoFlag = true;
        }
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


    private void updateOwnedCluster(Cell startCell) {
        waveSameColor.apply(startCell, ownedCluster);
    }


    public void onEndCreation() {
        active = true;
    }


    private ColorYio getRandomPossibleSafeColor() {
        if (!isThereAtLeastOneSafePossibleColor()) return getRandomPossibleColor();
        while (true) {
            ColorYio color = getRandomPossibleColor();
            if (isColorSafe(color)) return color;
        }
    }


    private boolean isThereAtLeastOneSafePossibleColor() {
        for (ColorYio color : possibleColors) {
            if (isColorSafe(color)) return true;
        }
        return false;
    }


    private ColorYio getRandomPossibleColor() {
        return possibleColors.get(YioGdxGame.random.nextInt(possibleColors.size()));
    }


    public void updatePossibleColors() {
        objectsLayer.possibleColorsManager.update();
        possibleColors.clear();
        possibleColors.addAll(objectsLayer.possibleColorsManager.colors);
    }
}
