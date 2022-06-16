package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.ScrollListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;

import java.util.HashMap;

public class RenderScrollListItem extends AbstractRenderCustomListItem{

    ScrollListItem scrollListItem;
    RectangleYio tempRectangle;
    public HashMap<BackgroundYio, TextureRegion> mapBackgrounds;


    public RenderScrollListItem() {
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        mapBackgrounds = new HashMap<>();
        for (BackgroundYio backgroundYio : BackgroundYio.values()) {
            TextureRegion textureRegion;
            try {
                textureRegion = loadBackgroundTexture(backgroundYio);
            } catch (Exception e) {
                textureRegion = null;
            }
            mapBackgrounds.put(backgroundYio, textureRegion);
        }
    }


    private TextureRegion loadBackgroundTexture(BackgroundYio backgroundYio) {
        return GraphicsYio.loadTextureRegion("menu/list/" + backgroundYio + ".png", false);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        scrollListItem = (ScrollListItem) item;

        renderDarken();
        renderHighlight();
        renderColoredBackground();
        renderTextOptimized(scrollListItem.title, alpha * scrollListItem.selfScrollWorkerYio.getAlpha());
        renderSelection();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderColoredBackground() {
        if (!scrollListItem.colored) return;
        GraphicsYio.drawByRectangle(
                batch,
                mapBackgrounds.get(scrollListItem.backgroundYio),
                scrollListItem.viewPosition
        );
    }


    private void renderSelection() {
        if (!scrollListItem.touchable) return;
        renderDefaultSelection(scrollListItem);
    }


    private void renderHighlight() {
        if (!scrollListItem.isHighlighted()) return;

        RectangleYio pos = scrollListItem.viewPosition;
        tempRectangle.x = pos.x;
        tempRectangle.width = 4 * GraphicsYio.borderThickness;
        tempRectangle.y = pos.y + 0.2f * pos.height;
        tempRectangle.height = 0.6f * pos.height;

        GraphicsYio.setBatchAlpha(batch, 0.5 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, tempRectangle);
    }


    private void renderDarken() {
        if (!scrollListItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, scrollListItem.darkValue * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, scrollListItem.viewPosition);
    }
}
