package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class Figure implements ReusableYio {

    FiguresManager figuresManager;
    public ColorYio color;
    public ArrayList<Cell> cells;
    public ArrayList<RectangleYio> shapes;
    public ArrayList<RectangleYio> topConvexList;
    public ArrayList<RectangleYio> bottomConvexList;
    private ObjectPoolYio<RectangleYio> poolRectangles;


    public Figure(FiguresManager figuresManager) {
        this.figuresManager = figuresManager;
        cells = new ArrayList<>();
        topConvexList = new ArrayList<>();
        bottomConvexList = new ArrayList<>();
        shapes = new ArrayList<>();
        reset();
        initPools();
    }


    private void initPools() {
        poolRectangles = new ObjectPoolYio<RectangleYio>() {
            @Override
            public RectangleYio makeNewObject() {
                return new RectangleYio();
            }
        };
    }


    @Override
    public void reset() {
        color = null;
        cells.clear();
        clearTopConvexList();
        clearBottomConvexList();
        clearShapes();
    }


    void move() {

    }


    void updateConvex() {
        clearTopConvexList();
        updateConvex(Direction.up, topConvexList);
        clearBottomConvexList();
        updateConvex(Direction.down, bottomConvexList);
    }


    void updateConvex(Direction direction, ArrayList<RectangleYio> list) {
        for (Cell cell : cells) {
            if (!shouldStartConvex(cell, direction)) continue;
            spawnConvex(cell, direction, list);
        }
    }


    private void spawnConvex(Cell startCell, Direction direction, ArrayList<RectangleYio> list) {
        Cell finishCell = findConvexFinish(startCell, direction);
        RectangleYio next = poolRectangles.getNext();
        list.add(next);
        setRectangleByCorners(next, startCell, finishCell);
        float convexHeight = Math.min(figuresManager.convexHeight, 0.45f * figuresManager.objectsLayer.cellField.cellSize);
        switch (direction) {
            default:
                System.out.println("Figure.spawnConvex: problem");
                break;
            case up:
                next.y = next.y + next.height - convexHeight;
                next.height = convexHeight;
                break;
            case down:
                next.height = convexHeight;
                break;
        }
        if (startCell.sameColor(Direction.left)) {
            next.increase(2 * startCell.innerDelta, Direction.left);
        }
        if (finishCell.sameColor(Direction.right)) {
            next.increase(2 * finishCell.innerDelta, Direction.right);
        }
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


    void updateShapes() {
        clearShapes();
        spawnSingleCellShapes();
    }


    private void setRectangleByCorners(RectangleYio rect, Cell bottomLeftCell, Cell topRightCell) {
        CircleYio bottomLeftPosition = bottomLeftCell.innerPosition;
        CircleYio topRightPosition = topRightCell.innerPosition;
        rect.x = bottomLeftPosition.center.x - bottomLeftPosition.radius;
        rect.y = bottomLeftPosition.center.y - bottomLeftPosition.radius;
        rect.width = topRightPosition.center.x + topRightPosition.radius - rect.x;
        rect.height = topRightPosition.center.y + topRightPosition.radius - rect.y;
    }


    private void spawnSingleCellShapes() {
        for (Cell cell : cells) {
            RectangleYio next = poolRectangles.getNext();
            next.setBy(cell.innerPosition);
            if (cell.sameColor(Direction.left)) {
                next.increase(2 * cell.innerDelta, Direction.left);
            }
            if (cell.sameColor(Direction.down)) {
                next.increase(2 * cell.innerDelta, Direction.down);
            }
            shapes.add(next);
        }
    }


    private void clearBottomConvexList() {
        for (RectangleYio rectangleYio : bottomConvexList) {
            poolRectangles.add(rectangleYio);
        }
        bottomConvexList.clear();
    }


    private void clearTopConvexList() {
        for (RectangleYio rectangleYio : topConvexList) {
            poolRectangles.add(rectangleYio);
        }
        topConvexList.clear();
    }


    private void clearShapes() {
        for (RectangleYio shape : shapes) {
            poolRectangles.add(shape);
        }
        shapes.clear();
    }

}
