package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.LineYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SectionStartListItem  extends AbstractCustomListItem{

    public LineYio left, right;
    float offset = 0.1f * GraphicsYio.width;
    public RenderableTextYio title;
    private float height;
    RectangleYio refPosition;
    public FactorYio transitionFactor;
    public ColorYio color;


    @Override
    protected void initialize() {
        left = new LineYio();
        right = new LineYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        refPosition = new RectangleYio();
        transitionFactor = new FactorYio();
        color = null;
    }


    @Override
    protected void move() {
        updateLeftLine();
        updateRightLine();
        updateTitlePosition();
        moveTransitionFactor();
    }


    @Override
    public void onAppear() {
        super.onAppear();
        transitionFactor.reset();
        transitionFactor.appear(MovementType.approach, 1.8);
    }


    private void moveTransitionFactor() {
        if (customizableListYio.getFactor().getValue() < 0.98) return;
        transitionFactor.move();
    }


    @Override
    protected RectangleYio getReferencePosition() {
        refPosition.setBy(customizableListYio.maskPosition);
        refPosition.x = customizableListYio.getPosition().x;
        return refPosition;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + title.height + 0.005f * GraphicsYio.height;
        FactorYio clFactor = customizableListYio.getFactor();
        if (clFactor.getValue() < 1) {
            if (clFactor.isInAppearState()) {
                title.position.x += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - title.width / 2 - title.position.x);
                title.position.y += (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height + title.height / 2 - title.position.y);
            } else {
                title.position.x -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.width - title.width / 2 - title.position.x);
                title.position.y -= (1 - clFactor.getValue()) * (0.5f * GraphicsYio.height + title.height / 2 - title.position.y);
            }
        }
        title.updateBounds();
    }


    public void setValues(String key, boolean empty, float height) {
        String string = LanguagesManager.getInstance().getString(key);
        this.height = height;
        if (empty) {
            string = "";
        }
        title.setString(string);
        title.updateMetrics();
    }


    private void updateRightLine() {
        right.start.x = viewPosition.x + viewPosition.width / 2 + title.width / 2 + offset / 2;
        right.start.y = viewPosition.y + viewPosition.height / 2;
        right.finish.x = viewPosition.x + viewPosition.width - offset;
        right.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    private void updateLeftLine() {
        left.start.x = viewPosition.x + offset;
        left.start.y = viewPosition.y + viewPosition.height / 2;
        left.finish.x = viewPosition.x + viewPosition.width / 2 - title.width / 2 - offset / 2;
        left.finish.y = viewPosition.y + viewPosition.height / 2;
    }


    @Override
    protected double getWidth() {
        return customizableListYio.getPosition().width;
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {

    }


    public void setColor(ColorYio color) {
        this.color = color;
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSectionStartListItem;
    }
}