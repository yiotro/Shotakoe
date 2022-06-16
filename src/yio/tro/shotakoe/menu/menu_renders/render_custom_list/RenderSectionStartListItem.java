package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.SectionStartListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderSectionStartListItem extends AbstractRenderCustomListItem{

    private SectionStartListItem slItem;
    HashMap<ColorYio, TextureRegion> mapColors;


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, GraphicsYio.loadTextureRegion("menu/campaign/" + color + ".png", false));
        }
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        slItem = (SectionStartListItem) item;

//        if (slItem.color != null) {
//            GraphicsYio.setBatchAlpha(batch, 0.6 * alpha);
//            GraphicsYio.drawByRectangle(batch, mapColors.get(slItem.color), slItem.viewPosition);
//            GraphicsYio.setBatchAlpha(batch, 1);
//        }

        renderTextOptimized(
                slItem.title,
                slItem.customizableListYio.getAlpha(),
                slItem.transitionFactor.getValue()
        );
    }
}
