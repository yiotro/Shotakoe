package yio.tro.shotakoe.game.generator;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator {

    ObjectsLayer objectsLayer;
    protected LgParameters parameters;
    Random random;
    private CellField cellField;
    ArrayList<Cell> tempList;
    WaveWorker waveActiveLimited;
    WaveWorker waveActive;
    WaveWorker waveInactive;
    WaveWorker waveSameColor;
    private int middleY;
    private int middleX;
    PointYio center;
    LevelValidator validator;


    public LevelGenerator(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        parameters = null;
        tempList = new ArrayList<>();
        center = new PointYio();
        validator = new LevelValidator(objectsLayer);
        initWaves();
    }


    private void initWaves() {
        waveActiveLimited = new WaveWorker(objectsLayer.cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.active && parentCell.algoValue > 0 && random.nextDouble() > 0.8;
            }


            @Override
            protected void onCellReached(Cell cell, Cell parentCell) {
                super.onCellReached(cell, parentCell);
                if (parentCell == null) return;
                cell.algoValue = parentCell.algoValue - 1;
            }
        };
        waveActive = new WaveWorker(objectsLayer.cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.active;
            }
        };
        waveSameColor = new WaveWorker(objectsLayer.cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
        waveInactive = new WaveWorker(objectsLayer.cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return !cell.active;
            }


            @Override
            protected boolean isAdjacentCellValid(Cell adjacentCell) {
                if (adjacentCell == null) return false;
                if (adjacentCell.waveFlag) return false;
                return true;
            }


            @Override
            protected void resetFlags() {
                for (Cell cell : cellField.cells) {
                    cell.waveFlag = false;
                }
            }
        };
    }


    public void apply(LgParameters lgParameters, Random random) {
        this.random = random;
        this.parameters = lgParameters;
        int c = 0;
        while (c < 500) {
            c++;
            begin();
            doMainPart();
            if (validator.apply()) break;
        }
        end();
    }


    private void doMainPart() {
        spawnHoles();
        guaranteeSingleIsland();
        randomizeColors();
        groupColors();
        checkToApplySymmetry();
        spawnBomb();
        spawnHumanStart();
        spawnEnemyStart();
    }


    private void spawnEnemyStart() {
        if (!parameters.enemy) return;
        if (parameters.symmetry) {
            Cell startCell = cellField.getCell(CellType.human_start);
            int copyX = middleX + (middleX - startCell.x);
            int copyY = middleY + (middleY - startCell.y);
            cellField.getCellSafely(copyX, copyY).setType(CellType.enemy_start);
            return;
        }
        if (parameters.sameStartingCluster) {
            if (tryToSpawnEnemyStartAtSameCluster()) return;
        }
        resetFlags();
        waveInactive.apply(cellField.getCellSafely(0, 0), tempList);
        tagTempList();
        updateTempListByCellsNearTagged();
        updateCenter();
        Cell startCell = cellField.getCell(CellType.human_start);
        PointYio startPosition = startCell.position.center;
        if (parameters.bomb) {
            updateCenterByTriggerCells();
        }
        float targetDistance = center.distanceTo(startPosition);
        removeFromTempListByMinDistance(startPosition, 1.5f * targetDistance);
        Cell enemyStartCell = findClosestToTargetDistanceCell(tempList, targetDistance, center);
        if (enemyStartCell == null) return;
        enemyStartCell.setType(CellType.enemy_start);
    }


    private void updateCenterByTriggerCells() {
        ArrayList<Cell> triggerCells = objectsLayer.bombsManager.triggerCells;
        center.reset();
        for (Cell cell : triggerCells) {
            center.add(cell.position.center);
        }
        center.x /= triggerCells.size();
        center.y /= triggerCells.size();
    }


    private Cell findClosestToTargetDistanceCell(ArrayList<Cell> list, float targetDistance, PointYio pointYio) {
        Cell bestCell = null;
        float minDifference = 0;
        for (Cell cell : list) {
            float currentDistance = pointYio.distanceTo(cell.position.center);
            float currentDifference = Math.abs(currentDistance - targetDistance);
            if (bestCell == null || currentDifference < minDifference) {
                bestCell = cell;
                minDifference = currentDifference;
            }
        }
        return bestCell;
    }


    private void removeFromTempListByMinDistance(PointYio pointYio, float minDistance) {
        for (int i = tempList.size() - 1; i >= 0; i--) {
            Cell cell = tempList.get(i);
            float currentDistance = pointYio.distanceTo(cell.position.center);
            if (currentDistance > minDistance) continue;
            tempList.remove(i);
        }
    }


    private void updateCenter() {
        center.set(
                cellField.getMiddleX(),
                cellField.getMiddleY()
        );
    }


    private boolean tryToSpawnEnemyStartAtSameCluster() {
        Cell startCell = cellField.getCell(CellType.human_start);
        if (!startCell.isAdjacentToColor(startCell.color)) return false;
        waveSameColor.apply(startCell, tempList);
        int c = 100;
        while (c > 0) {
            c--;
            Cell randomCellFromTempList = getRandomCellFromTempList();
            if (randomCellFromTempList == null) continue;
            if (randomCellFromTempList == startCell) continue;
            if (!randomCellFromTempList.isAdjacentToInactiveCell()) continue;
            randomCellFromTempList.setType(CellType.enemy_start);
            return true;
        }
        return false;
    }


    private Cell getFurthestClusteredCellFromTempList(Cell hookCell) {
        Cell bestCell = null;
        float maxDistance = 0;
        for (Cell cell : tempList) {
            if (!cell.isAdjacentToColor(cell.color)) continue;
            if (cell.color == hookCell.color) continue;
            float currentDistance = cell.position.center.distanceTo(hookCell.position.center);
            if (bestCell == null || currentDistance > maxDistance) {
                bestCell = cell;
                maxDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private void removeCertainCellsFromTempList(CellType cellType) {
        for (int i = tempList.size() - 1; i >= 0; i--) {
            Cell cell = tempList.get(i);
            if (cell.type != cellType) continue;
            tempList.remove(i);
        }
    }


    private void checkToApplySymmetry() {
        if (!parameters.symmetry) return;
        if (cellField.width % 2 == 0 || cellField.height % 2 == 0) {
//            System.out.println("LevelGenerator.checkToApplySymmetry: warning, field size may cause problems");
        }
        Cell topCell = findFurthestActiveCell(Direction.up);
        Cell rightCell = findFurthestActiveCell(Direction.right);
        Cell bottomCell = findFurthestActiveCell(Direction.down);
        Cell leftCell = findFurthestActiveCell(Direction.left);
        middleY = (topCell.y + bottomCell.y) / 2;
        middleX = (rightCell.x + leftCell.x) / 2;
        for (Cell cell : cellField.activeCells) {
            if (cell.y <= middleY) continue;
            cell.active = false;
        }
        cellField.updateActiveCells();
        for (Cell cell : cellField.activeCells) {
            int copyX = middleX + (middleX - cell.x);
            int copyY = middleY + (middleY - cell.y);
            Cell copyCell = cellField.getCellSafely(copyX, copyY);
            if (copyCell == null) {
                System.out.println("LevelGenerator.checkToApplySymmetry: problem, copy cell is null");
                continue;
            }
            copyCell.active = true;
            copyCell.color = cell.color;
        }
        cellField.updateActiveCells();
        guaranteeSingleIsland(); // yes, level can become unlinked after symmetry was applied
    }


    private boolean areActiveCellsNearEdge() {
        for (Cell cell : cellField.activeCells) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (adjacentCell == null) return true;
            }
        }
        return false;
    }


    private Cell findFurthestActiveCell(Direction direction) {
        Cell bestCell = null;
        for (Cell cell : cellField.activeCells) {
            if (bestCell == null || cell.isFurtherThan(bestCell, direction)) {
                bestCell = cell;
            }
        }
        return bestCell;
    }


    private void spawnHumanStart() {
        resetFlags();
        waveInactive.apply(cellField.getCellSafely(0, 0), tempList);
        tagTempList();
        updateTempListByCellsNearTagged();
        if (parameters.sameStartingCluster) {
            removeLonelyCellsFromTempList();
        }
        Cell startCell;
        if (parameters.bomb && !parameters.enemy) {
            startCell = findFurthestCell(tempList, cellField.getCell(CellType.bomb));
        } else {
            startCell = getRandomCellFromTempList();
        }
        if (startCell == null) return;
        startCell.setType(CellType.human_start);
    }


    private void removeLonelyCellsFromTempList() {
        for (int i = tempList.size() - 1; i >= 0; i--) {
            Cell cell = tempList.get(i);
            if (cell.isAdjacentToColor(cell.color)) continue;
            tempList.remove(i);
        }
    }


    private void tagTempList() {
        for (Cell cell : tempList) {
            cell.algoFlag = true;
        }
    }


    private Cell findFurthestCell(ArrayList<Cell> list, Cell antiMagnetCell) {
        Cell bestCell = null;
        float maxDistance = 0;
        for (Cell cell : list) {
            float currentDistance = cell.position.center.distanceTo(antiMagnetCell.position.center);
            if (bestCell == null || currentDistance > maxDistance) {
                bestCell = cell;
                maxDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private Cell getRandomCellFromTempList() {
        if (tempList.size() == 0) return null;
        return tempList.get(random.nextInt(tempList.size()));
    }


    private void updateTempListByCellsNearTagged() {
        tempList.clear();
        for (Cell cell : cellField.activeCells) {
            if (!isNearTaggedCell(cell)) continue;
            tempList.add(cell);
        }
    }


    private void spawnBomb() {
        if (!parameters.bomb) return;
        if (parameters.enemy) {
            updateCenter();
            Cell closestCell = findClosestToTargetDistanceCell(cellField.activeCells, 0, center);
            Cell adjacentCell = getRandomActiveAdjacentCell(closestCell);
            adjacentCell.setType(CellType.bomb);
        } else {
            Cell cell = getRandomActiveCell();
            cell.setType(CellType.bomb);
        }
    }


    private Cell getRandomActiveAdjacentCell(Cell cell) {
        int c = 500;
        while (c > 0) {
            c--;
            Cell adjacentCell = getRandomAdjacentCell(cell);
            if (adjacentCell.active) return adjacentCell;
        }
        System.out.println("LevelGenerator.getRandomActiveAdjacentCell: unable to find active adjacent cell");
        return null;
    }


    private Cell getRandomAdjacentCell(Cell cell) {
        return cell.adjacentCells[random.nextInt(cell.adjacentCells.length)];
    }


    private boolean isNearTaggedCell(Cell cell) {
        for (Cell adjacentCell : cell.adjacentCells) {
            if (adjacentCell.algoFlag) return true;
        }
        return false;
    }


    private void groupColors() {
        int c = 999;
        while (c > 0) {
            c--;
            updateTempListByLonelyCells();
            float value = (float) tempList.size() / cellField.activeCells.size();
            if (value < 1 - parameters.groupsValue) break;
            Cell cell = getRandomCellFromTempList();
            if (cell == null) break;
            if (random.nextDouble() < parameters.colorClustersValue) {
                spawnColorCluster(cell);
            } else {
                changeColorToAdjacent(cell);
            }
        }
    }


    private void spawnColorCluster(Cell spawnCell) {
        spawnCell.algoValue = 3 + random.nextInt(3);
        waveActiveLimited.apply(spawnCell, tempList);
        for (Cell cell : tempList) {
            cell.setColor(spawnCell.color);
        }
    }


    private void changeColorToAdjacent(Cell cell) {
        Cell[] adjacentCells = cell.adjacentCells;
        Cell adjacentCell = adjacentCells[random.nextInt(adjacentCells.length)];
        cell.setColor(adjacentCell.color);
    }


    private void updateTempListByLonelyCells() {
        tempList.clear();
        for (Cell cell : cellField.activeCells) {
            if (cell.isAdjacentToColor(cell.color)) continue;
            tempList.add(cell);
        }
    }


    private void guaranteeSingleIsland() {
        int maxSize = detectMaxIslandSize();
        boolean skipped = false; // for situations when there are two big islands of same size
        resetFlags();
        for (Cell cell : cellField.activeCells) {
            if (cell.algoFlag) continue;
            int size = detectIslandSize(cell);
            if (size == maxSize && !skipped) {
                skipped = true;
                continue;
            }
            deactivateTempList();
        }
        cellField.updateActiveCells();
    }


    private void deactivateTempList() {
        for (Cell cell : tempList) {
            cell.active = false;
        }
    }


    private int detectMaxIslandSize() {
        resetFlags();
        int maxSize = 0;
        for (Cell cell : cellField.activeCells) {
            if (cell.algoFlag) continue;
            int size = detectIslandSize(cell);
            maxSize = Math.max(maxSize, size);
        }
        return maxSize;
    }


    private void resetFlags() {
        for (Cell cell : cellField.cells) {
            cell.algoFlag = false;
        }
    }


    private int detectIslandSize(Cell startCell) {
        waveActive.apply(startCell, tempList);
        tagTempList();
        return tempList.size();
    }


    private void randomizeColors() {
        for (Cell cell : cellField.cells) {
            cell.setColor(getRandomColor());
        }
    }


    private void spawnHoles() {
        int c = 200;
        while (c > 0) {
            c--;
            if (calculateCurrentActiveValue() < parameters.activeCellsValue) break;
            spawnSingleHole();
        }
    }


    private void spawnSingleHole() {
        Cell randomActiveCell = getRandomActiveCell();
        randomActiveCell.algoValue = random.nextInt(6);
        waveActiveLimited.apply(randomActiveCell, tempList);
        for (Cell cell : tempList) {
            cell.active = false;
        }
        cellField.updateActiveCells();
    }


    private Cell getRandomActiveCell() {
        ArrayList<Cell> activeCells = cellField.activeCells;
        return activeCells.get(random.nextInt(activeCells.size()));
    }


    private double calculateCurrentActiveValue() {
        return (double) countActiveCellsFarFromEdge() / (double) countCellsFarFromEdge();
    }


    private int countCellsFarFromEdge() {
        int c = 0;
        for (Cell cell : cellField.cells) {
            if (cell.isNearEdge()) continue;
            c++;
        }
        return c;
    }


    private int countActiveCellsFarFromEdge() {
        int c = 0;
        for (Cell cell : cellField.cells) {
            if (!cell.active) continue;
            if (cell.isNearEdge()) continue;
            c++;
        }
        return c;
    }


    private void end() {
        objectsLayer.figuresManager.onGenerationFinished();
        objectsLayer.simulationCacheManager.update();
    }


    private void begin() {
        objectsLayer.figuresManager.setUpdateAllowed(false);
        objectsLayer.clearForLevelGeneration();
        cellField = objectsLayer.cellField;
    }


    private ColorYio getRandomColor() {
        return ColorYio.values()[random.nextInt(parameters.colorsQuantity)];
    }
}
