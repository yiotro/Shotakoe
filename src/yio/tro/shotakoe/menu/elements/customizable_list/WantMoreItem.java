package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.*;

public class WantMoreItem extends AbstractCustomListItem {

    public RenderableTextYio title;
    public RectangleYio incBounds;
    public RectangleYio touchPosition;
    public SelectionEngineYio selectionEngineYio;


    @Override
    protected void initialize() {
        initTitle();
        incBounds = new RectangleYio();
        touchPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString(LanguagesManager.getInstance().getString("want_more"));
        title.updateMetrics();
    }


    @Override
    protected void move() {
        updateTitlePosition();
        updateIncBounds();
        updateTouchPosition();
        moveSelection();
    }


    private void moveSelection() {
        if (customizableListYio.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateTouchPosition() {
        touchPosition.setBy(incBounds);
        touchPosition.increase(0.015f * GraphicsYio.height);
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.height);
        float horDelta = 0.05f * GraphicsYio.width;
        incBounds.increase(horDelta, Direction.right);
        incBounds.increase(horDelta, Direction.left);
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.08f * GraphicsYio.height;
    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        if (!touchPosition.isPointInside(touchPoint)) return;
        selectionEngineYio.applySelection();
        SoundManager.playSound(SoundType.button);
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        if (!selectionEngineYio.isSelected()) return;
        Scenes.aboutProVersion.create();
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderWantMoreItem;
    }
}
