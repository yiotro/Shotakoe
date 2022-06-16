package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;

public class Cell {

    CellField cellField;
    public int x;
    public int y;
    public CircleYio position;
    public Cell[] adjacentCells;
    public boolean active;
    public ColorYio color;
    public CircleYio innerPosition;
    public boolean waveFlag;
    public boolean algoFlag;
    public int algoValue;
    public FactorYio alphaFactor; // used by figures
    public float innerDelta;
    public Direction algoDirection;
    public CellType type;
    public AlgoCluster algoCluster;


    public Cell(CellField cellField, int x, int y) {
        this.cellField = cellField;
        this.x = x;
        this.y = y;
        position = new CircleYio();
        adjacentCells = new Cell[4];
        active = false;
        color = null;
        innerPosition = new CircleYio();
        algoValue = -1;
        alphaFactor = new FactorYio();
        innerDelta = GraphicsYio.borderThickness;
        algoDirection = null;
        type = CellType.normal;
        algoCluster = null;
    }


    void updatePosition() {
        float size = cellField.cellSize;
        position.setRadius(size / 2);
        position.center.set(
                cellField.position.x + x * size + size / 2,
                cellField.position.y + y * size + size / 2
        );
        innerPosition.setBy(position);
        innerPosition.radius -= innerDelta;
    }


    void updateAdjacentCells() {
        adjacentCells[0] = cellField.getCellSafely(x, y + 1);
        adjacentCells[1] = cellField.getCellSafely(x + 1, y);
        adjacentCells[2] = cellField.getCellSafely(x, y - 1);
        adjacentCells[3] = cellField.getCellSafely(x - 1, y);
    }


    public Direction getDirectionTo(Cell targetCell) {
        for (int i = 0; i < adjacentCells.length; i++) {
            if (adjacentCells[i] != targetCell) continue;
            return Direction.values()[i];
        }
        return null;
    }


    public Cell getAdjacentCell(Direction direction) {
        return adjacentCells[direction.ordinal()];
    }


    public boolean isAdjacentTo(Cell cell) {
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell == cell) return true;
        }
        return false;
    }


    public boolean isAdjacentToColor(ColorYio color) {
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell.active && adjacentCell.color == color) return true;
        }
        return false;
    }


    public boolean sameColor(Cell cell) {
        return color == cell.color;
    }


    public boolean sameColor(Direction direction) {
        Cell adjacentCell = getAdjacentCell(direction);
        return adjacentCell.active && color == adjacentCell.color;
    }


    public boolean isAdjacentToInactiveCell() {
        for (Cell adjacentCell : adjacentCells) {
            if (!adjacentCell.active) return true;
        }
        return false;
    }


    public boolean hasActiveNeighbour(Direction direction) {
        Cell adjacentCell = getAdjacentCell(direction);
        return adjacentCell != null && adjacentCell.active;
    }


    public int countActiveNeighbours() {
        int c = 0;
        for (Cell adjacentCell : adjacentCells) {
            if (!adjacentCell.active) continue;
            c++;
        }
        return c;
    }


    public boolean isNearEdge() {
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell == null) return true;
        }
        return false;
    }


    public boolean hasColor() {
        return color != null;
    }


    public boolean isNot(CellType cellType) {
        return type != cellType;
    }


    public void setType(CellType type) {
        if (this.type == type) return;
        CellType previousType = this.type;
        this.type = type;
        onCellTypeChanged(previousType);
    }


    private void onCellTypeChanged(CellType previousType) {
        cellField.objectsLayer.onCellTypeChanged(this, previousType);
    }


    public boolean isFurtherThan(Cell anotherCell, Direction direction) {
        switch (direction) {
            default:
                System.out.println("LevelGenerator.isFurther: problem");
                return true;
            case up:
                return y > anotherCell.y;
            case right:
                return x > anotherCell.x;
            case down:
                return y < anotherCell.y;
            case left:
                return x < anotherCell.x;
        }
    }


    public void setColor(ColorYio color) {
        if (this.color == color) return;
        this.color = color;
        cellField.objectsLayer.onCellChangedColor(this);
    }


    void resetActiveStatus() {
        active = !isNearEdge();
    }


    @Override
    public String toString() {
        return "[Cell: " + type + " " + x + ", " + y + "]";
    }
}
