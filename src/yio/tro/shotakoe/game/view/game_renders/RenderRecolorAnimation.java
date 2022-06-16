package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.shotakoe.SettingsManager;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.RecolorManager;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.Masking;
import yio.tro.shotakoe.stuff.RectangleYio;

import java.util.HashMap;

public class RenderRecolorAnimation extends GameRender{

    HashMap<ColorYio, TextureRegion> mapColors;
    HashMap<ColorYio, TextureRegion> mapCircles;
    private TextureRegion xTexture;
    private TextureRegion convexTopTexture;
    private TextureRegion convexBottomTexture;
    private TextureRegion blackCircleTexture;
    private CircleYio circleYio;
    private RecolorManager recolorManager;


    public RenderRecolorAnimation() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        mapColors = new HashMap<>();
        mapCircles = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, loadRoughTexture(color + ".png"));
            mapCircles.put(color, GraphicsYio.loadTextureRegion("game/recolor/circle_" + color + ".png", false));
        }
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
        convexTopTexture = loadRoughTexture("convex_top.png");
        convexBottomTexture = loadRoughTexture("convex_bottom.png");
        blackCircleTexture = GraphicsYio.loadTextureRegion("game/recolor/circle_black.png", false);
    }


    @Override
    public void render() {
        recolorManager = getObjectsLayer().recolorManager;
        if (recolorManager.effectFactor.getProgress() == 0) return;
        batchMovable.end();
        Masking.begin();

        ShapeRenderer shapeRenderer = gameController.yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(gameView.orthoCam.combined);
        for (RectangleYio rectangleYio : recolorManager.masks) {
            shapeRenderer.rect(rectangleYio.x, rectangleYio.y, rectangleYio.width, rectangleYio.height);
        }
        shapeRenderer.end();

        batchMovable.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(batchMovable);
    }


    private void renderInternals() {
        if (SettingsManager.getInstance().simplifiedAnimation) {
            renderCrossFade();
        } else {
            renderCircle();
        }
        renderTopConvex();
        renderBottomConvex();
    }


    private void renderCrossFade() {
        circleYio.center.setBy(recolorManager.effectPosition.center);
        circleYio.radius = 1.1f * recolorManager.targetRadius;
        GraphicsYio.setBatchAlpha(batchMovable, recolorManager.effectFactor.getProgress());
        GraphicsYio.drawByCircle(batchMovable, mapCircles.get(recolorManager.effectColor), circleYio);
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderBottomConvex() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.08 * recolorManager.getEffectAlpha());
        for (RectangleYio pos : getObjectsLayer().recolorManager.bottomConvexList) {
            GraphicsYio.drawByRectangle(batchMovable, convexBottomTexture, pos);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderTopConvex() {
        GraphicsYio.setBatchAlpha(batchMovable, 0.12 * recolorManager.getEffectAlpha());
        for (RectangleYio pos : getObjectsLayer().recolorManager.topConvexList) {
            GraphicsYio.drawByRectangle(batchMovable, convexTopTexture, pos);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderCircle() {
        circleYio.setBy(recolorManager.effectPosition);
        circleYio.radius -= 2 * GraphicsYio.borderThickness;
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        GraphicsYio.drawByCircle(batchMovable, blackCircleTexture, circleYio);
        GraphicsYio.setBatchAlpha(batchMovable, recolorManager.getEffectAlpha());
        GraphicsYio.drawByCircle(batchMovable, mapCircles.get(recolorManager.effectColor), recolorManager.effectPosition);
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
