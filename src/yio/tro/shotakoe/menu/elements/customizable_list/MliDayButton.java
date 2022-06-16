package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.stuff.*;

public class MliDayButton {

    MonthListItem monthListItem;
    public MliDayState state;
    public CircleYio position;
    PointYio delta;
    int index;
    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;
    public CircleYio touchPosition;


    public MliDayButton(MonthListItem monthListItem) {
        this.monthListItem = monthListItem;
        position = new CircleYio();
        delta = new PointYio();
        index = -1;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        selectionEngineYio = new SelectionEngineYio();
        state = null;
        touchPosition = new CircleYio();
    }


    void move() {
        updatePosition();
        updateTouchPosition();
        moveTitle();
        moveSelection();
    }


    private void updateTouchPosition() {
        touchPosition.center.setBy(position.center);
    }


    private void updatePosition() {
        position.center.x = monthListItem.viewPosition.x + delta.x;
        position.center.y = monthListItem.viewPosition.y + delta.y;
    }


    private void moveTitle() {
        if (state != MliDayState.unlocked) return;
        title.position.x = position.center.x - title.width / 2;
        title.position.y = position.center.y + title.height / 2;
        title.updateBounds();
    }


    private void moveSelection() {
        CustomizableListYio customizableListYio = monthListItem.customizableListYio;
        if (customizableListYio != null && customizableListYio.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(touchPosition.center.x - touchPoint.x) > touchPosition.radius) return false;
        if (Math.abs(touchPosition.center.y - touchPoint.y) > touchPosition.radius) return false;
        return true;
    }


    public void setIndex(int index) {
        this.index = index;
        title.setString("" + index);
        title.updateMetrics();
    }


    public void setState(MliDayState state) {
        this.state = state;
    }
}
