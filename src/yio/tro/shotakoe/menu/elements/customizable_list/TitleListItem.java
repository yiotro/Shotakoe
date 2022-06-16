package yio.tro.shotakoe.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;

public class TitleListItem extends AbstractSingleLineItem{


    private float height;
    public float alpha;


    public TitleListItem() {
        height = 0.06f * GraphicsYio.height;
        alpha = 1;
    }


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.miniFont;
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onClicked() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderTitleListItem;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    @Override
    protected void moveRenderableTextByDefault(RenderableTextYio renderableTextYio) {
        renderableTextYio.position.x = viewPosition.x + viewPosition.width / 2 - title.width / 2;
        renderableTextYio.position.y = viewPosition.y + renderableTextYio.delta.y;
        FactorYio clFactor = customizableListYio.getFactor();
        if (clFactor.getValue() < 1) {
            if (clFactor.isInAppearState()) {
                renderableTextYio.position.x += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - renderableTextYio.width / 2 - renderableTextYio.position.x);
                renderableTextYio.position.y += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height + renderableTextYio.height / 2 - renderableTextYio.position.y);
            } else {
                renderableTextYio.position.x -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - renderableTextYio.width / 2 - renderableTextYio.position.x);
                renderableTextYio.position.y -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height + renderableTextYio.height / 2 - renderableTextYio.position.y);
            }
        }
        renderableTextYio.updateBounds();
    }

}
