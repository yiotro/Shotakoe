package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.TitleListItem;

public class RenderTitleListItem extends AbstractRenderCustomListItem{


    private TitleListItem titleListItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        titleListItem = (TitleListItem) item;
        renderTextOptimized(titleListItem.title, titleListItem.alpha);
    }
}
