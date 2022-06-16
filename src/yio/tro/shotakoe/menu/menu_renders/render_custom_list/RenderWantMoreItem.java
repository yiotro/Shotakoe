package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.WantMoreItem;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderWantMoreItem extends AbstractRenderCustomListItem{


    private WantMoreItem wmItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        wmItem = (WantMoreItem) item;

        renderIncBounds();
        renderTitle();
        renderSelection();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSelection() {
        if (!wmItem.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, wmItem.customizableListYio.getAlpha() * wmItem.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, wmItem.touchPosition);
    }


    private void renderTitle() {
        GraphicsYio.renderText(batch, wmItem.title);
    }


    private void renderIncBounds() {
        GraphicsYio.setBatchAlpha(batch, 0.8 * alpha);
        MenuRenders.renderRoundShape.renderRoundShape(
                wmItem.incBounds,
                BackgroundYio.full_white,
                0.015f * GraphicsYio.height
        );
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundBorder.renderRoundBorder(
                wmItem.incBounds,
                0.015f * GraphicsYio.height
        );
    }

}
