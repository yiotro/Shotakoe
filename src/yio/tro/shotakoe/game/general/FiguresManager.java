package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class FiguresManager {

    private final CellField cellField;
    ObjectsLayer objectsLayer;
    public ArrayList<Figure> figures;
    ObjectPoolYio<Figure> poolFigures;
    WaveWorker waveSameColor;
    WaveWorker waveDirections;
    float convexHeight;
    boolean updateAllowed;
    public ArrayList<CornerPlug> cornerPlugs;
    ObjectPoolYio<CornerPlug> poolCornerPlugs;


    public FiguresManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        cellField = objectsLayer.cellField;
        figures = new ArrayList<>();
        convexHeight = 0.015f * GraphicsYio.height;
        updateAllowed = false;
        cornerPlugs = new ArrayList<>();
        initPools();
        initWaveWorkers();
    }


    private void initWaveWorkers() {
        waveSameColor = new WaveWorker(cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
        waveDirections = new WaveWorker(cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }


            @Override
            protected void onCellReached(Cell cell, Cell parentCell) {
                if (parentCell == null) {
                    cell.algoDirection = null;
                    return;
                }
                cell.algoDirection = cell.getDirectionTo(parentCell);
            }
        };
    }


    private void initPools() {
        poolFigures = new ObjectPoolYio<Figure>(figures) {
            @Override
            public Figure makeNewObject() {
                return new Figure(FiguresManager.this);
            }
        };
        poolCornerPlugs = new ObjectPoolYio<CornerPlug>(cornerPlugs) {
            @Override
            public CornerPlug makeNewObject() {
                return new CornerPlug();
            }
        };
    }


    public void applyUpdate() {
        if (!updateAllowed) return;
        poolFigures.clearExternalList();
        resetFlags();
        for (Cell cell : cellField.activeCells) {
            if (!cell.hasColor()) continue;
            if (cell.algoFlag) continue;
            spawnFigure(cell);
        }
        updateCornerPlugs();
    }


    private void updateCornerPlugs() {
        poolCornerPlugs.clearExternalList();
        for (Cell cell : cellField.activeCells) {
            if (!isCornerThatShouldBePlugged(cell)) continue;
            CornerPlug freshObject = poolCornerPlugs.getFreshObject();
            freshObject.setCell(cell);
        }
    }


    private boolean isCornerThatShouldBePlugged(Cell cell) {
        if (!cell.sameColor(Direction.down)) return false;
        if (!cell.sameColor(Direction.left)) return false;
        Cell diagonalCell = cell.getAdjacentCell(Direction.down).getAdjacentCell(Direction.left);
        return !diagonalCell.active || diagonalCell.color != cell.color;
    }


    public Figure getFigure(Cell cell) {
        for (Figure figure : figures) {
            if (figure.cells.contains(cell)) return figure;
        }
        return null;
    }


    private void spawnFigure(Cell startCell) {
        Figure freshObject = poolFigures.getFreshObject();
        freshObject.color = startCell.color;
        waveSameColor.apply(startCell, freshObject.cells);
        tagCells(freshObject);
        freshObject.updateShapes();
        freshObject.updateConvex();
    }


    private void tagCells(Figure figure) {
        for (Cell cell : figure.cells) {
            cell.algoFlag = true;
        }
    }


    private void resetFlags() {
        for (Cell cell : cellField.activeCells) {
            cell.algoFlag = false;
        }
    }


    public void move() {
        for (Figure figure : figures) {
            figure.move();
        }
    }


    public void onCellsImported() {
        setUpdateAllowed(true);
        applyUpdate();
    }


    public void onGenerationFinished() {
        setUpdateAllowed(true);
        applyUpdate();
    }


    public void onAdvancedStuffCreated() {
        setUpdateAllowed(true);
        applyUpdate();
    }


    public void onCellChangedColor(Cell cell) {
        applyUpdate();
    }


    public void setUpdateAllowed(boolean updateAllowed) {
        this.updateAllowed = updateAllowed;
    }
}
