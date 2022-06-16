package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.CornerPlug;
import yio.tro.shotakoe.game.general.Figure;
import yio.tro.shotakoe.game.general.FiguresManager;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;

import java.util.HashMap;

public class RenderFiguresOptimized extends GameRender {

    HashMap<ColorYio, TextureRegion> mapColors;
    private TextureRegion convexTopTexture;
    private TextureRegion convexBottomTexture;
    private FiguresManager figuresManager;
    private TextureRegion xTexture;
    private TextureRegion defTexture;


    @Override
    protected void loadTextures() {
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, loadRoughTexture(color + ".png"));
        }
        convexTopTexture = loadRoughTexture("convex_top.png");
        convexBottomTexture = loadRoughTexture("convex_bottom.png");
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
        defTexture = GraphicsYio.loadTextureRegion("game/atlas/def_dark.png", false);
    }


    @Override
    public void render() {
        if (DebugFlags.rawFigures) return;
        figuresManager = getObjectsLayer().figuresManager;
        renderShapes();
        renderCornerPlugs();
        renderTopConvex();
        renderBottomConvex();
        renderDebug();
    }


    private void renderCornerPlugs() {
        for (CornerPlug cornerPlug : figuresManager.cornerPlugs) {
            GraphicsYio.drawByRectangle(batchMovable, defTexture, cornerPlug.position);
        }
    }


    private void renderDebug() {
        if (!DebugFlags.showShapesDebug) return;
        GraphicsYio.setBatchAlpha(batchMovable, 0.25);
        for (Figure figure : figuresManager.figures) {
            for (RectangleYio shape : figure.shapes) {
                GraphicsYio.drawByRectangle(batchMovable, xTexture, shape);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderBottomConvex() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.08);
        for (Figure figure : figuresManager.figures) {
            for (RectangleYio pos : figure.bottomConvexList) {
                GraphicsYio.drawByRectangle(batchMovable, convexBottomTexture, pos);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderTopConvex() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.12);
        for (Figure figure : figuresManager.figures) {
            for (RectangleYio pos : figure.topConvexList) {
                GraphicsYio.drawByRectangle(batchMovable, convexTopTexture, pos);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderShapes() {
        for (Figure figure : figuresManager.figures) {
            for (RectangleYio shape : figure.shapes) {
                GraphicsYio.drawByRectangle(batchMovable, mapColors.get(figure.color), shape);
            }
        }
    }


    @Override
    protected void disposeTextures() {
        convexBottomTexture.getTexture().dispose();
        convexTopTexture.getTexture().dispose();
        xTexture.getTexture().dispose();
    }
}
