package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.LpWorker;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;
import java.util.Random;

public class CampaignCustomItem extends AbstractCustomListItem{

    public static final int ROW = 6;
    public ArrayList<CciInnerItem> items;
    public float iconSize = 0.11f * GraphicsYio.width;
    public float delta = 0.04f * GraphicsYio.width;
    private CciInnerItem touchedItem;
    public ColorYio color;


    @Override
    protected void initialize() {
        items = new ArrayList<>();
        color = null;
    }


    public void set(int startIndex, int endIndex) {
        for (int index = startIndex; index <= endIndex; index++) {
            CciInnerItem innerItem = new CciInnerItem(this);
            innerItem.setIndex(index);
            innerItem.setType(CampaignManager.getInstance().getLevelType(index));
            items.add(innerItem);
        }
    }


    public void setColor(ColorYio color) {
        this.color = color;
    }


    @Override
    protected void move() {
        viewPosition.x = customizableListYio.getPosition().x;
        viewPosition.width = customizableListYio.getPosition().width;
        for (CciInnerItem item : items) {
            item.move();
            if (!customizableListYio.touchedCurrently) {
                item.selectionEngineYio.move();
            }
        }
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return iconSize + delta;
    }


    @Override
    protected void onPositionChanged() {
        updateItemMetrics();
    }


    private void updateItemMetrics() {
        float lineWidth = ROW * iconSize + (ROW - 1) * delta;
        float x = (float) (getWidth() / 2 - lineWidth / 2);
        float y = (float) (getHeight() / 2 - iconSize / 2);
        for (CciInnerItem item : items) {
            item.delta.set(x, y);
            item.position.width = iconSize;
            item.position.height = iconSize;
            x += iconSize + delta;
        }
    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        touchedItem = findTouchedItem(touchPoint);
        if (touchedItem == null) return;

        touchedItem.selectionEngineYio.applySelection();
    }


    public boolean containsLevelIndex(int index) {
        for (CciInnerItem item : items) {
            if (item.index == index) return true;
        }
        return false;
    }


    private CciInnerItem findTouchedItem(PointYio touchPoint) {
        for (CciInnerItem item : items) {
            if (!item.selectionPosition.isPointInside(touchPoint)) continue;
            return item;
        }
        return null;
    }


    @Override
    protected void onClicked() {
        if (touchedItem == null) return;

        int index = touchedItem.index;
        CciType type = touchedItem.type;
        if (type == CciType.unknown && !DebugFlags.unlockLevels) return;
        YioGdxGame yioGdxGame = getGameController().yioGdxGame;
        CampaignManager.getInstance().launchCampaignLevel(yioGdxGame, index);
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderCampaignCustomItem;
    }
}
