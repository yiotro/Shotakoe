package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.stuff.*;

import java.util.HashMap;

public class RenderFiguresRaw extends GameRender{

    HashMap<ColorYio, TextureRegion> mapColors;
    private CellField cellField;
    private TextureRegion convexTopTexture;
    private TextureRegion convexBottomTexture;
    RectangleYio tempRectangle;
    CircleYio tempCircle;


    public RenderFiguresRaw() {
        tempRectangle = new RectangleYio();
        tempCircle = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, GraphicsYio.loadTextureRegion("game/atlas/" + color + ".png", false));
        }
        convexTopTexture = GraphicsYio.loadTextureRegion("game/atlas/convex_top.png", false);
        convexBottomTexture = GraphicsYio.loadTextureRegion("game/atlas/convex_bottom.png", false);
    }


    @Override
    public void render() {
        if (!DebugFlags.rawFigures) return;
        cellField = getObjectsLayer().cellField;
        renderInnerPositions();
        renderConnections();
        renderSquares();
        renderConvex(Direction.up);
        renderConvex(Direction.down);
    }


    private void renderSquares() {
        for (Cell cell : cellField.activeCells) {
            if (cell.color == null) continue;
            if (!cell.sameColor(Direction.up)) continue;
            if (!cell.sameColor(Direction.right)) continue;
            Cell topCell = cell.getAdjacentCell(Direction.up);
            if (!topCell.sameColor(Direction.right)) continue;
            tempCircle.setBy(cell.innerPosition);
            tempCircle.center.relocateRadial(cellField.cellSize / 2, Math.PI / 4);
            GraphicsYio.drawByCircle(batchMovable, mapColors.get(cell.color), tempCircle);
        }
    }


    private void renderConvex(Direction direction) {
        GraphicsYio.setBatchAlpha(batchMovable, getConvexAlpha(direction));
        TextureRegion convexTexture = getConvexTexture(direction);
        for (Cell cell : cellField.activeCells) {
            if (!shouldStartConvex(cell, direction)) continue;
            tempRectangle.setBy(cell.innerPosition);
            tempRectangle.height = 0.015f * GraphicsYio.height;
            if (direction == Direction.up) {
                tempRectangle.y = cell.innerPosition.center.y + cell.innerPosition.radius - tempRectangle.height;
            }
            Cell tempCell = cell;
            while (tempCell.color == cell.color) {
                tempRectangle.width = tempCell.innerPosition.center.x + tempCell.innerPosition.radius - tempRectangle.x;
                tempCell = tempCell.getAdjacentCell(Direction.right);
                if (tempCell.sameColor(direction)) break;
            }
            GraphicsYio.drawByRectangle(batchMovable, convexTexture, tempRectangle);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private double getConvexAlpha(Direction direction) {
        switch (direction) {
            default:
                System.out.println("RenderDebugColors.getConvexAlpha: problem");
                return 0;
            case up:
                return 0.12;
            case down:
                return 0.08;
        }
    }


    private boolean shouldStartConvex(Cell cell, Direction direction) {
        if (cell.color == null) return false;
        if (cell.sameColor(direction)) return false;
        if (cell.sameColor(Direction.left)) {
            Cell leftCell = cell.getAdjacentCell(Direction.left);
            return leftCell.sameColor(direction);
        }
        return true;
    }


    private TextureRegion getConvexTexture(Direction direction) {
        switch (direction) {
            default:
                return null;
            case up:
                return convexTopTexture;
            case down:
                return convexBottomTexture;
        }
    }


    private void renderConnections() {
        for (Cell cell : cellField.activeCells) {
            renderSingleConnection(cell, Direction.up);
            renderSingleConnection(cell, Direction.right);
        }
    }


    private void renderSingleConnection(Cell cell, Direction direction) {
        if (cell.color == null) return;
        Cell adjacentCell = cell.getAdjacentCell(direction);
        if (!adjacentCell.active) return;
        if (adjacentCell.color != cell.color) return;
        tempCircle.setBy(cell.innerPosition);
        double angle = DirectionWorker.getAngle(direction);
        tempCircle.center.relocateRadial(cellField.cellSize / 2, angle);
        GraphicsYio.drawByCircle(batchMovable, mapColors.get(cell.color), tempCircle);
    }


    private void renderInnerPositions() {
        for (Cell cell : cellField.activeCells) {
            if (cell.color == null) continue;
            GraphicsYio.drawByCircle(batchMovable, mapColors.get(cell.color), cell.innerPosition);
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
