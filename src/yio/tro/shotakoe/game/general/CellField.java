package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.export.Encodeable;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.RectangleYio;

import java.util.ArrayList;

public class CellField implements Encodeable {

    public ObjectsLayer objectsLayer;
    GameController gameController;
    public Cell[][] array;
    public int width, height;
    public float cellSize;
    public RectangleYio position;
    public ArrayList<Cell> cells;
    public ArrayList<Cell> activeCells;


    public CellField(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        gameController = objectsLayer.gameController;
        cellSize = GraphicsYio.width / objectsLayer.getLoadingParameters().fieldWidth;
        cells = new ArrayList<>();
        activeCells = new ArrayList<>();
        initSize();
        initPosition();
        initArray();
    }


    private void initArray() {
        array = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j] = new Cell(this, i, j);
                cells.add(array[i][j]);
            }
        }
        for (Cell cell : cells) {
            cell.updatePosition();
            cell.updateAdjacentCells();
            cell.resetActiveStatus();
        }
        prepareActiveRectangle(height);
        updateActiveCells();
    }


    public void prepareActiveRectangle(int h) {
        for (Cell cell : cells) {
            cell.resetActiveStatus();
        }
        int bottomY = (height - h) / 2;
        int topY = bottomY + h;
        for (int y = 0; y < bottomY; y++) {
            deactivateRow(y);
        }
        for (int y = topY; y < height; y++) {
            deactivateRow(y);
        }
        updateActiveCells();
    }


    private void deactivateRow(int y) {
        for (int x = 0; x < width; x++) {
            array[x][y].active = false;
        }
    }


    public void updateActiveCells() {
        activeCells.clear();
        for (Cell cell : cells) {
            if (!cell.active) continue;
            activeCells.add(cell);
        }
    }


    public Cell getCellSafely(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return array[x][y];
    }


    public Cell getCellByPoint(PointYio pointYio) {
        float x = pointYio.x - position.x;
        float y = pointYio.y - position.y;
        x /= cellSize;
        y /= cellSize;
        return getCellSafely((int) x, (int) y);
    }


    public void clear() {
        prepareActiveRectangle(height);
        for (Cell cell : cells) {
            cell.setColor(null);
            cell.setType(CellType.normal);
            cell.algoFlag = false;
        }
    }


    public void deactivateEverything() {
        activeCells.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j].active = false;
                array[i][j].setColor(null);
            }
        }
    }


    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        for (Cell cell : activeCells) {
            builder.append(cell.x).append(" ").append(cell.y).append(" ").append(cell.color).append(",");
        }
        return builder.toString();
    }


    public float getMiddleX() {
        Cell rightCell = null;
        Cell leftCell = null;
        for (Cell cell : activeCells) {
            if (rightCell == null || cell.x > rightCell.x) {
                rightCell = cell;
            }
            if (leftCell == null || cell.x < leftCell.x) {
                leftCell = cell;
            }
        }
        if (rightCell == null) return 0;
        return (rightCell.position.center.x + leftCell.position.center.x) / 2;
    }


    public float getMiddleY() {
        Cell topCell = null;
        Cell bottomCell = null;
        for (Cell cell : activeCells) {
            if (topCell == null || cell.y > topCell.y) {
                topCell = cell;
            }
            if (bottomCell == null || cell.y < bottomCell.y) {
                bottomCell = cell;
            }
        }
        if (topCell == null) return 0;
        return (topCell.position.center.y + bottomCell.position.center.y) / 2;
    }


    public boolean contains(CellType cellType) {
        return getCell(cellType) != null;
    }


    public float getFieldBottomY() {
        Cell cell = getLowestActiveCell();
        return cell.position.center.y - cell.position.radius;
    }


    private Cell getLowestActiveCell() {
        Cell bestCell = null;
        for (Cell cell : activeCells) {
            if (bestCell == null || cell.y < bestCell.y) {
                bestCell = cell;
            }
        }
        return bestCell;
    }


    private void initPosition() {
        position = new RectangleYio();
        position.width = width * cellSize;
        position.height = height * cellSize;
        position.x = (getSizeManager().boundWidth - position.width) / 2;
        position.y = (getSizeManager().boundHeight - position.height) / 2;
    }


    public Cell getCell(CellType cellType) {
        for (Cell cell : cells) {
            if (cell.isNot(cellType)) continue;
            return cell;
        }
        return null;
    }


    private void initSize() {
        width = objectsLayer.getLoadingParameters().fieldWidth;
        height = objectsLayer.getLoadingParameters().fieldHeight;
    }


    private SizeManager getSizeManager() {
        return gameController.sizeManager;
    }
}
