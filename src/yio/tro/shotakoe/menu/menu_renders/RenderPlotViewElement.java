package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.plot_view.PlotColor;
import yio.tro.shotakoe.menu.elements.plot_view.PlotDescView;
import yio.tro.shotakoe.menu.elements.plot_view.PlotItem;
import yio.tro.shotakoe.menu.elements.plot_view.PlotViewElement;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderPlotViewElement extends RenderInterfaceElement {

    PlotViewElement pvElement;
    CircleYio tempCircle;
    HashMap<PlotColor, TextureRegion> mapColors;
    private TextureRegion whitePixel;


    public RenderPlotViewElement() {
        tempCircle = new CircleYio();
        tempCircle.setRadius(0.003 * GraphicsYio.width);
    }


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (PlotColor color : PlotColor.values()) {
            String path = "menu/plot/" + color + ".png";
            TextureRegion textureRegion = GraphicsYio.loadTextureRegion(path, false);
            mapColors.put(color, textureRegion);
        }
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        pvElement = (PlotViewElement) element;

        renderBorder();
        renderItems();
        renderDescViews();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderDescViews() {
        if (pvElement.getFactor().isInDestroyState()) return;
        for (PlotDescView descView : pvElement.descViews) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByRectangle(batch, mapColors.get(descView.color), descView.incBounds);
            renderWhiteText(descView.renderableTextYio, whitePixel, alpha);
        }
    }


    private void renderItems() {
        if (pvElement.getFactor().isInDestroyState()) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (PlotItem plotItem : pvElement.items) {
            renderPlotItem(plotItem);
        }
    }


    private void renderBorder() {
        GraphicsYio.setBatchAlpha(batch, 0.25 * alpha);
        GraphicsYio.renderBorder(batch, blackPixel, pvElement.getViewPosition());
    }


    private void renderPlotItem(PlotItem plotItem) {
        TextureRegion textureRegion = mapColors.get(plotItem.color);
        ArrayList<PointYio> points = plotItem.points;
        for (PointYio point : points) {
            tempCircle.center.setBy(point);
            GraphicsYio.drawByCircle(batch, textureRegion, tempCircle);
        }
        for (int i = 0; i < points.size() - 1; i++) {
            PointYio currentPoint = points.get(i);
            PointYio nextPoint = points.get(i + 1);
            GraphicsYio.drawLine(batch, textureRegion, currentPoint, nextPoint, 1.5 * GraphicsYio.borderThickness);
        }
    }
}
