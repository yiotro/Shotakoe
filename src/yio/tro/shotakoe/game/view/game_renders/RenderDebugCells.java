package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderDebugCells extends GameRender{


    private TextureRegion inactiveTexture;
    HashMap<ColorYio, TextureRegion> mapColors;
    private CellField cellField;


    @Override
    protected void loadTextures() {
        inactiveTexture = GraphicsYio.loadTextureRegion("game/debug/x.png", true);
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, GraphicsYio.loadTextureRegion("game/atlas/" + color + ".png", false));
        }
    }


    @Override
    public void render() {
        cellField = getObjectsLayer().cellField;
        renderInactiveCells();
        renderActiveCells();
    }


    private void renderActiveCells() {
        if (!DebugFlags.rawFigures) return;
        for (Cell cell : cellField.activeCells) {
            if (cell.color == null) continue;
            GraphicsYio.drawByCircle(batchMovable, mapColors.get(cell.color), cell.position);
        }
    }


    private void renderInactiveCells() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.15);
        for (Cell cell : cellField.cells) {
            if (cell.active) continue;
            GraphicsYio.drawByCircle(batchMovable, inactiveTexture, cell.position);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {
        inactiveTexture.getTexture().dispose();
    }
}
