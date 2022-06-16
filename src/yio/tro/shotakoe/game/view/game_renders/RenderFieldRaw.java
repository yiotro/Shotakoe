package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.stuff.*;

public class RenderFieldRaw extends GameRender {

    private CellField cellField;
    private TextureRegion defTexture;
    PointYio p1;
    PointYio p2;
    PointYio tempPoint;
    private TextureRegion separatorTexture;
    private TextureRegion shadowTexture;
    RectangleYio tempRectangle;
    private float step;
    private TextureRegion darkCellTexture;


    public RenderFieldRaw() {
        tempPoint = new PointYio();
        p1 = new PointYio();
        p2 = new PointYio();
        tempRectangle = new RectangleYio();
        step = GraphicsYio.borderThickness;
    }


    @Override
    protected void loadTextures() {
        defTexture = GraphicsYio.loadTextureRegion("game/atlas/def_dark.png", false);
        separatorTexture = GraphicsYio.loadTextureRegion("game/atlas/separator.png", true);
        shadowTexture = GraphicsYio.loadTextureRegion("game/atlas/shadow.png", false);
    }


    @Override
    public void render() {
        cellField = getObjectsLayer().cellField;
        renderShadows();
        renderActiveCells();
//        renderOutlines();
    }


    private void renderShadows() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.25);
        float height = Math.min(0.03f * GraphicsYio.height, cellField.cellSize);
        for (Cell cell : cellField.activeCells) {
            Cell bottomCell = cell.getAdjacentCell(Direction.down);
            if (bottomCell.active) continue;
            tempRectangle.setBy(cell.position);
            tempRectangle.height = height;
            tempRectangle.y -= 0.85f * tempRectangle.height;
            extendShadow(cell, Direction.left);
            extendShadow(cell, Direction.right);
            GraphicsYio.drawByRectangle(batchMovable, shadowTexture, tempRectangle);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void extendShadow(Cell cell, Direction direction) {
        Cell sideCell = cell.getAdjacentCell(direction);
        if (sideCell.active) return;
        Cell lowerCell = sideCell.getAdjacentCell(Direction.down);
        if (lowerCell.active) return;
        increase(tempRectangle, direction);
    }


    private void renderOutlines() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.66);
        for (Cell cell : cellField.activeCells) {
            for (Cell adjacentCell : cell.adjacentCells) {
                if (adjacentCell == null) continue;
                if (adjacentCell.active) continue;
                Direction direction = cell.getDirectionTo(adjacentCell);
                double angle = DirectionWorker.getAngle(direction);
                float leftDelta = calculateLeftDelta(cell, direction);
                float rightDelta = calculateRightDelta(cell, direction);
                tempPoint.setBy(cell.position.center);
                tempPoint.relocateRadial(cellField.cellSize / 2 + step, angle);
                tempPoint.relocateRadial(cellField.cellSize / 2 + leftDelta, angle + Math.PI / 2);
                p1.setBy(tempPoint);
                tempPoint.relocateRadial(cellField.cellSize + leftDelta + rightDelta, angle - Math.PI / 2);
                p2.setBy(tempPoint);
                GraphicsYio.drawLine(batchMovable, separatorTexture, p1, p2, GraphicsYio.borderThickness);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private float calculateLeftDelta(Cell cell, Direction direction) {
        Cell leftCell = cell.getAdjacentCell(DirectionWorker.rotate(direction, -1));
        Cell leftForwardCell = leftCell.getAdjacentCell(direction);
        if (!leftCell.active && !leftForwardCell.active) return step;
        if (leftForwardCell.active) return -step;
        return 0;
    }


    private float calculateRightDelta(Cell cell, Direction direction) {
        Cell rightCell = cell.getAdjacentCell(DirectionWorker.rotate(direction, 1));
        Cell rightForwardCell = rightCell.getAdjacentCell(direction);
        if (!rightCell.active && !rightForwardCell.active) return step;
        if (rightForwardCell.active) return -step;
        return 0;
    }


    private void renderActiveCells() {
        for (Cell cell : cellField.activeCells) {
            tempRectangle.setBy(cell.position);
            for (Cell adjacentCell : cell.adjacentCells) {
                if (adjacentCell.active) continue;
                Direction direction = cell.getDirectionTo(adjacentCell);
                increase(tempRectangle, direction);
            }
            GraphicsYio.drawByRectangle(batchMovable, defTexture, tempRectangle);
        }
    }


    private void increase(RectangleYio pos, Direction direction) {
        switch (direction) {
            default:
                System.out.println("RenderFieldRaw.increase: problem");
                break;
            case up:
                pos.height += step;
                break;
            case right:
                pos.width += step;
                break;
            case down:
                pos.height += step;
                pos.y -= step;
                break;
            case left:
                pos.width += step;
                pos.x -= step;
                break;
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
