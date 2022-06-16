package yio.tro.shotakoe.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.VisualTextContainer;

public class BigTextItem extends AbstractCustomListItem{

    public VisualTextContainer visualTextContainer;
    double h;
    private float incDelta;
    private BitmapFont font;


    @Override
    protected void initialize() {
        visualTextContainer = new VisualTextContainer();
        h = 0.1f * GraphicsYio.height;
        incDelta = 0.07f * GraphicsYio.height;
        font = Fonts.miniFont;
    }


    public void applyText(CustomizableListYio customizableListYio, String key) {
        String string = key;
        if (key.length() < 200) {
            string = LanguagesManager.getInstance().getString(key);
        }
        visualTextContainer.setSize(0.9 * customizableListYio.getPosition().width, h);
        visualTextContainer.applyManyTextLines(font, string);
        visualTextContainer.updateHeightToMatchText(incDelta);
        h = visualTextContainer.position.height;
    }


    @Override
    protected void move() {
        viewPosition.x = customizableListYio.getPosition().x + customizableListYio.getInternalOffset();
        visualTextContainer.move(viewPosition);
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return h;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {

    }


    @Override
    protected void onLongTapped() {

    }


    public void setIncDelta(float incDelta) {
        this.incDelta = incDelta;
    }


    public void setFont(BitmapFont font) {
        this.font = font;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderBigTextItem;
    }
}
