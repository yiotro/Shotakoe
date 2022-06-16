package yio.tro.shotakoe.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.game.tests.AbstractTest;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class DtCustomItem extends AbstractSingleLineItem {

    public AbstractTest test;


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.gameFont;
    }


    public void setTest(AbstractTest test) {
        this.test = test;
        setTitle(test.getName());
    }


    @Override
    protected double getHeight() {
        return 0.065f * GraphicsYio.height;
    }


    @Override
    protected void onClicked() {
        test.perform(getGameController());
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSingleListItem;
    }
}
