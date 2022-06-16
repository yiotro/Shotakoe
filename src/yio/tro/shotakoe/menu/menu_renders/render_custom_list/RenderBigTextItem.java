package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.BigTextItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderBigTextItem extends AbstractRenderCustomListItem {

    RectangleYio screenPosition;
    private BigTextItem btItem;
    PointYio tempPoint;
    private float offset;


    public RenderBigTextItem() {
        super();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        tempPoint = new PointYio();
        offset = 0.1f * GraphicsYio.height;
    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        btItem = (BigTextItem) item;

        for (RenderableTextYio renderableTextYio : btItem.visualTextContainer.viewList) {
            tempPoint.set(
                    renderableTextYio.position.x + renderableTextYio.width / 2,
                    renderableTextYio.position.y - renderableTextYio.height / 2
            );
            if (!screenPosition.isPointInside(tempPoint, offset)) continue;
            GraphicsYio.setFontAlpha(renderableTextYio.font, alpha);
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }
}
