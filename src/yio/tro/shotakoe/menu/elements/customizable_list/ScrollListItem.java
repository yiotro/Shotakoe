package yio.tro.shotakoe.menu.elements.customizable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.SelfScrollWorkerYio;

public class ScrollListItem extends AbstractSingleLineItem {

    SliReaction clickReaction;
    SliReaction longTapReaction;
    public String key;
    private float height;
    public boolean centered;
    public boolean highlighted;
    public boolean touchable;
    public boolean colored;
    public BackgroundYio backgroundYio;
    public SelfScrollWorkerYio selfScrollWorkerYio;


    public ScrollListItem() {
        clickReaction = null;
        longTapReaction = null;
        key = null;
        height = 0.1f * GraphicsYio.height;
        centered = false;
        highlighted = false;
        touchable = true;
        colored = true;
        backgroundYio = null;
        selfScrollWorkerYio = new SelfScrollWorkerYio();
    }


    @Override
    protected BitmapFont getTitleFont() {
        return Fonts.gameFont;
    }


    public void setFont(BitmapFont font) {
        title.setFont(font);
        title.updateMetrics();
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onClicked() {
        if (touchable && clickReaction != null) {
            clickReaction.apply(this);
        }
    }


    @Override
    protected void move() {
        selfScrollWorkerYio.move();
        if (centered) {
            title.centerVertical(viewPosition);
            title.centerHorizontal(viewPosition);
            title.updateBounds();
            return;
        }
        title.position.x = viewPosition.x + title.delta.x + selfScrollWorkerYio.getDelta();
        title.position.y = viewPosition.y + title.delta.y;
        title.updateBounds();
    }


    @Override
    protected void onLongTapped() {
        super.onLongTapped();
        if (touchable && longTapReaction != null) {
            longTapReaction.apply(this);
        }
    }


    public void checkToEnableSelfScroll() {
        float limit = customizableListYio.getPosition().width - 0.07f * GraphicsYio.width;
        selfScrollWorkerYio.launch(title.width, limit);
    }


    public void setClickReaction(SliReaction clickReaction) {
        this.clickReaction = clickReaction;
    }


    public void setLongTapReaction(SliReaction longTapReaction) {
        this.longTapReaction = longTapReaction;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public void setCentered(boolean centered) {
        this.centered = centered;
    }


    @Override
    protected void onAddedToList() {
        super.onAddedToList();
        if (!colored) return;
        BackgroundYio previousBackground = null;
        ScrollListItem lastSlItem = getLastSlItem();
        if (lastSlItem != null) {
            previousBackground = lastSlItem.backgroundYio;
        }
        backgroundYio = getNextBackground(previousBackground);
    }


    public static BackgroundYio getNextBackground(BackgroundYio previousBackground) {
        int index = 0;
        if (previousBackground != null) {
            index = previousBackground.ordinal();
        }
        BackgroundYio[] values = BackgroundYio.values();
        while (true) {
            index++;
            if (index > values.length - 1) {
                index = 0;
            }
            if (!isColorful(values[index])) continue;
            return values[index];
        }
    }


    public static boolean isColorful(BackgroundYio backgroundYio) {
        switch (backgroundYio) {
            default:
                return false;
            case yellow:
            case green:
            case cyan:
            case orange:
            case magenta:
            case red:
                return true;
        }
    }


    private ScrollListItem getLastSlItem() {
        ScrollListItem scrollListItem = null;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof ScrollListItem)) continue;
            if (item == this) continue;
            scrollListItem = (ScrollListItem) item;
        }
        return scrollListItem;
    }


    public void setColored(boolean colored) {
        this.colored = colored;
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        if (centered) {
            title.delta.x = viewPosition.width / 2 - title.width / 2;
        }
    }


    @Override
    public String getKey() {
        return key;
    }


    public boolean isHighlighted() {
        return highlighted;
    }


    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }


    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }


    public void setBackground(BackgroundYio backgroundYio) {
        this.backgroundYio = backgroundYio;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderScrollListItem;
    }
}
