package yio.tro.shotakoe.menu.elements.gameplay;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class ViewTouchModeElement extends InterfaceElement<ViewTouchModeElement> {

    public RenderableTextYio title;
    public boolean hasText;
    FactorYio textAlphaFactor;
    public RectangleYio backgroundPosition;


    public ViewTouchModeElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        hasText = false;
        textAlphaFactor = new FactorYio();
        backgroundPosition = new RectangleYio();
    }


    @Override
    protected ViewTouchModeElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTextPosition();
        moveTextAlpha();
        updateBackgroundPosition();
    }


    private void updateBackgroundPosition() {
        backgroundPosition.setBy(title.bounds);
        backgroundPosition.increase(0.02f * GraphicsYio.width);
    }


    private void moveTextAlpha() {
        if (!textAlphaFactor.move()) return;
        hasText = (textAlphaFactor.getProgress() > 0 || textAlphaFactor.isInAppearState());
    }


    private void updateTextPosition() {
        if (!hasText) return;
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.updateBounds();
    }


    public void onTouchModeSet(TouchMode touchMode) {
        if (touchMode == null) {
            textAlphaFactor.destroy(MovementType.lighty, 1.7);
            return;
        }

        String nameKey = touchMode.getNameKey();

        if (nameKey == null) {
            textAlphaFactor.destroy(MovementType.lighty, 1.7);
            return;
        }

        hasText = true;
        title.setString(LanguagesManager.getInstance().getString(nameKey));
        title.updateMetrics();
        updateTextPosition();
        updateBackgroundPosition();

        textAlphaFactor.appear(MovementType.approach, 1.66);
    }


    public double getTextAlpha() {
        return textAlphaFactor.getValue();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderViewTouchMode;
    }
}
