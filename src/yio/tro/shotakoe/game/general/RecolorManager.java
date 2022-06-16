package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class RecolorManager {

    ObjectsLayer objectsLayer;
    WaveWorker waveSameColor;
    private ArrayList<Cell> tempList;
    public ArrayList<RectangleYio> masks;
    PointYio launchPoint;
    ObjectPoolYio<RectangleYio> poolMasks;
    public CircleYio effectPosition;
    public FactorYio effectFactor;
    public float targetRadius;
    public ColorYio effectColor;
    public ArrayList<RectangleYio> topConvexList;
    public ArrayList<RectangleYio> bottomConvexList;
    ObjectPoolYio<RectangleYio> poolTopConvex;
    ObjectPoolYio<RectangleYio> poolBottomConvex;
    private ArrayList<Cell> ownedCluster;
    private ArrayList<Cell> soundCells;
    private ArrayList<Float> soundsDistances;


    public RecolorManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        tempList = new ArrayList<>();
        masks = new ArrayList<>();
        launchPoint = new PointYio();
        effectPosition = new CircleYio();
        effectFactor = new FactorYio();
        topConvexList = new ArrayList<>();
        bottomConvexList = new ArrayList<>();
        ownedCluster = new ArrayList<>();
        soundCells = new ArrayList<>();
        soundsDistances = new ArrayList<>();
        initWaves();
        initPools();
    }


    private void initPools() {
        poolMasks = new ObjectPoolYio<RectangleYio>(masks) {
            @Override
            public RectangleYio makeNewObject() {
                return new RectangleYio();
            }
        };
        poolTopConvex = new ObjectPoolYio<RectangleYio>(topConvexList) {
            @Override
            public RectangleYio makeNewObject() {
                return new RectangleYio();
            }
        };
        poolBottomConvex = new ObjectPoolYio<RectangleYio>(bottomConvexList) {
            @Override
            public RectangleYio makeNewObject() {
                return new RectangleYio();
            }
        };
    }


    private void initWaves() {
        waveSameColor = new WaveWorker(objectsLayer.cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    public void onColorChosen(ColorYio color, CellType cellType) {
        CellField cellField = objectsLayer.cellField;
        Cell startCell = cellField.getCell(cellType);
        if (startCell == null) return;
        waveSameColor.apply(startCell, ownedCluster);
        detectSoundsCells(color, startCell);
        updateSoundDistances(startCell);
        objectsLayer.figuresManager.setUpdateAllowed(false);
        changeColor(ownedCluster, color);
        objectsLayer.figuresManager.setUpdateAllowed(true);
        objectsLayer.figuresManager.applyUpdate();
        ExternalIndicator indicator = objectsLayer.externalIndicatorsManager.getIndicator(cellType);
        launchAnimation(indicator.getLaunchPoint(), startCell);
        objectsLayer.finishManager.onColorChosen(startCell);
        objectsLayer.onTurnMade();
    }


    private void updateSoundDistances(Cell startCell) {
        soundsDistances.clear();
        for (Cell cell : soundCells) {
            soundsDistances.add(cell.position.center.distanceTo(startCell.position.center));
        }
    }


    private void detectSoundsCells(ColorYio targetColor, Cell startCell) {
        soundCells.clear();
        resetFlags();
        for (Cell cell : ownedCluster) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (!adjacentCell.active) continue;
                if (adjacentCell.color != targetColor) continue;
                if (adjacentCell.algoFlag) continue;
                tagClusterForSound(startCell, adjacentCell);
            }
        }
    }


    private void tagClusterForSound(Cell startCell, Cell clusterCell) {
        waveSameColor.apply(clusterCell, tempList);
        Cell closestCell = findClosestCell(tempList, startCell);
        soundCells.add(closestCell);
        tagByTempList();
    }


    private Cell findClosestCell(ArrayList<Cell> list, Cell startCell) {
        Cell bestCell = null;
        float minDistance = 0;
        for (Cell cell : list) {
            float currentDistance = cell.position.center.distanceTo(startCell.position.center);
            if (bestCell == null || currentDistance < minDistance) {
                bestCell = cell;
                minDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private void changeColor(ArrayList<Cell> list, ColorYio color) {
        for (Cell cell : list) {
            cell.setColor(color);
        }
    }


    public void launchAnimation(PointYio launchPoint, Cell hookCell) {
        this.launchPoint.setBy(launchPoint);
        updateMasks(hookCell);
        updateTargetRadius();
        effectPosition.center.setBy(launchPoint);
        effectFactor.reset();
        effectFactor.appear(MovementType.inertia, calculateEffectSpeed());
        effectColor = hookCell.color;
        updateEffectPosition();
        updateConvex();
    }


    public boolean isInProcessOfAnimation() {
        return effectFactor.isInAppearState() && effectFactor.getValue() < 1;
    }


    private double calculateEffectSpeed() {
        float minRadius = 0.3f * GraphicsYio.width;
        float maxRadius = 1.01f * GraphicsYio.width;
        float value = (targetRadius - minRadius) / (maxRadius - minRadius);
        value = Math.max(0, value);
        value = Math.min(1, value);
        return 1.7 - value * 0.8;
    }


    private void updateConvex() {
        poolTopConvex.clearExternalList();
        poolBottomConvex.clearExternalList();
        updateConvex(Direction.up, poolTopConvex);
        updateConvex(Direction.down, poolBottomConvex);
    }


    void updateConvex(Direction direction, ObjectPoolYio<RectangleYio> pool) {
        for (Cell cell : tempList) {
            if (!shouldStartConvex(cell, direction)) continue;
            spawnConvex(cell, direction, pool);
        }
    }


    private void spawnConvex(Cell startCell, Direction direction, ObjectPoolYio<RectangleYio> pool) {
        Cell finishCell = findConvexFinish(startCell, direction);
        RectangleYio freshObject = pool.getFreshObject();
        setRectangleByCorners(freshObject, startCell, finishCell);
        FiguresManager figuresManager = objectsLayer.figuresManager;
        float convexHeight = Math.min(figuresManager.convexHeight, 0.45f * figuresManager.objectsLayer.cellField.cellSize);
        switch (direction) {
            default:
                System.out.println("Figure.spawnConvex: problem");
                break;
            case up:
                freshObject.y = freshObject.y + freshObject.height - convexHeight;
                freshObject.height = convexHeight;
                break;
            case down:
                freshObject.height = convexHeight;
                break;
        }
        if (startCell.sameColor(Direction.left)) {
            freshObject.increase(2 * startCell.innerDelta, Direction.left);
        }
        if (finishCell.sameColor(Direction.right)) {
            freshObject.increase(2 * finishCell.innerDelta, Direction.right);
        }
    }


    private void setRectangleByCorners(RectangleYio rect, Cell bottomLeftCell, Cell topRightCell) {
        CircleYio bottomLeftPosition = bottomLeftCell.innerPosition;
        CircleYio topRightPosition = topRightCell.innerPosition;
        rect.x = bottomLeftPosition.center.x - bottomLeftPosition.radius;
        rect.y = bottomLeftPosition.center.y - bottomLeftPosition.radius;
        rect.width = topRightPosition.center.x + topRightPosition.radius - rect.x;
        rect.height = topRightPosition.center.y + topRightPosition.radius - rect.y;
    }


    private Cell findConvexFinish(Cell startCell, Direction direction) {
        Cell cell = startCell;
        while (true) {
            Cell adjacentCell = cell.getAdjacentCell(Direction.right);
            if (!adjacentCell.active) break;
            if (adjacentCell.color != cell.color) break;
            if (!needsConvex(adjacentCell, direction)) break;
            cell = adjacentCell;
        }
        return cell;
    }


    private boolean shouldStartConvex(Cell cell, Direction direction) {
        if (!needsConvex(cell, direction)) return false;
        Cell leftCell = cell.getAdjacentCell(Direction.left);
        if (leftCell.color == cell.color && needsConvex(leftCell, direction)) return false;
        return true;
    }


    private boolean needsConvex(Cell cell, Direction direction) {
        if (!cell.active) return false;
        Cell adjacentCell = cell.getAdjacentCell(direction);
        if (!adjacentCell.active) return true;
        return adjacentCell.color != cell.color;
    }


    private void updateTargetRadius() {
        Cell furthestCell = findFurthestCell(tempList);
        if (furthestCell == null) {
            System.out.println("RecolorManager.updateTargetRadius: problem");
            targetRadius = 0;
            return;
        }
        targetRadius = 1.15f * launchPoint.distanceTo(furthestCell.position.center) + 2.5f * furthestCell.position.radius;
    }


    private Cell findFurthestCell(ArrayList<Cell> list) {
        Cell bestCell = null;
        float maxDistance = 0;
        for (Cell cell : list) {
            float currentDistance = cell.position.center.distanceTo(launchPoint);
            if (bestCell == null || currentDistance > maxDistance) {
                bestCell = cell;
                maxDistance = currentDistance;
            }
        }
        return bestCell;
    }


    private void updateMasks(Cell hookCell) {
        waveSameColor.apply(hookCell, tempList);
        resetFlags();
        tagByTempList();
        poolMasks.clearExternalList();
        for (Cell cell : tempList) {
            RectangleYio freshObject = poolMasks.getFreshObject();
            freshObject.setBy(cell.innerPosition);
            if (cell.getAdjacentCell(Direction.up).algoFlag) {
                freshObject.increase(2 * cell.innerDelta, Direction.up);
            }
            if (cell.getAdjacentCell(Direction.right).algoFlag) {
                freshObject.increase(2 * cell.innerDelta, Direction.right);
            }
        }
    }


    private void tagByTempList() {
        for (Cell cell : tempList) {
            cell.algoFlag = true;
        }
    }


    private void resetFlags() {
        for (Cell cell : objectsLayer.cellField.cells) {
            cell.algoFlag = false;
        }
    }


    public void move() {
        effectFactor.move();
        if (objectsLayer.colorButtonsManager.buffer.size() > 0) {
            effectFactor.move(); // 2x speed
        }
        updateEffectPosition();
        checkForCaptureSounds();
        if (effectFactor.isInAppearState() && effectFactor.getProgress() == 1) {
            effectFactor.reset();
            objectsLayer.simulationCacheManager.update();
        }
    }


    private void checkForCaptureSounds() {
        if (soundsDistances.size() == 0) return;
        for (int i = soundsDistances.size() - 1; i >= 0; i--) {
            float distance = soundsDistances.get(i);
            if (0.9f * effectPosition.radius < distance) continue;
            SoundManager.playSound(SoundType.select_unit);
            soundsDistances.remove(i);
        }
    }


    public float getEffectAlpha() {
//        return 1;
        return Math.min(1, 0.3f + 0.7f * effectFactor.getValue());
    }


    private void updateEffectPosition() {
        effectPosition.radius = effectFactor.getValue() * targetRadius;
    }
}
