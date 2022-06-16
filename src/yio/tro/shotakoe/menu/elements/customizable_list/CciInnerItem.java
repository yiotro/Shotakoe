package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;

public class CciInnerItem {

    CampaignCustomItem campaignCustomItem;
    public RectangleYio position;
    public PointYio delta;
    public int index;
    public CciType type;
    public RenderableTextYio title;
    public RectangleYio selectionPosition;
    public SelectionEngineYio selectionEngineYio;


    public CciInnerItem(CampaignCustomItem campaignCustomItem) {
        this.campaignCustomItem = campaignCustomItem;
        position = new RectangleYio();
        delta = new PointYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        selectionPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    void move() {
        updatePosition();
        moveText();
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(position);
        selectionPosition.increase(campaignCustomItem.delta / 2);
    }


    private void moveText() {
        title.centerHorizontal(position);
        title.centerVertical(position);
        title.updateBounds();
    }


    private void updatePosition() {
        position.x = campaignCustomItem.viewPosition.x + delta.x;
        position.y = campaignCustomItem.viewPosition.y + delta.y;
        FactorYio clFactor = campaignCustomItem.customizableListYio.getFactor();
        if (clFactor.getValue() == 1) return;
        if (clFactor.isInAppearState()) {
            position.x += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - position.x);
            position.y += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height - position.y);
        } else {
            position.x -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - position.x);
            position.y -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height - position.y);
        }
    }


    public void setType(CciType type) {
        this.type = type;
    }


    public void setIndex(int index) {
        this.index = index;
        title.setString("" + index);
        if (index == 0) {
            // tutorial
            title.setString("?");
        }
        title.updateMetrics();
    }
}
