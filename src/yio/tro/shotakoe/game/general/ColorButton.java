package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.SelectionEngineYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class ColorButton {

    ColorButtonsManager colorButtonsManager;
    public CircleYio position;
    public CircleYio viewPosition;
    public CircleYio touchPosition;
    public BackgroundYio backgroundYio;
    public ColorYio colorYio;
    public SelectionEngineYio selectionEngineYio;
    public FactorYio appearFactor;
    boolean justPressedByHuman;


    public ColorButton(ColorButtonsManager colorButtonsManager) {
        this.colorButtonsManager = colorButtonsManager;
        position = new CircleYio();
        touchPosition = new CircleYio();
        viewPosition = new CircleYio();
        backgroundYio = null;
        colorYio = null;
        selectionEngineYio = new SelectionEngineYio();
        appearFactor = new FactorYio();
        appearFactor.setValue(1);
    }


    public void setColorYio(ColorYio colorYio) {
        this.colorYio = colorYio;
        backgroundYio = BackgroundYio.valueOf("game_" + colorYio);
    }


    void move() {
        updateViewPosition();
        updateTouchPosition();
        moveSelection();
        appearFactor.move();
    }


    private void updateTouchPosition() {
        touchPosition.center.setBy(viewPosition.center);
    }


    public void updateViewPosition() {
        viewPosition.setBy(position);
        if (justPressedByHuman && appearFactor.isInDestroyState()) {
            viewPosition.center.y += (1 - appearFactor.getValue()) * 2f * viewPosition.radius;
        }
    }


    public void setAvailability(boolean availability) {
        if (availability == isAvailable()) return;
        if (availability) {
            show();
        } else {
            hide();
        }
    }


    public float getShadowAlpha() {
        float v = Math.max(0, 1 - 3 * (1 - appearFactor.getValue()));
        if (selectionEngineYio.isSelected()) {
            return v * (1 - selectionEngineYio.factorYio.getValue());
        }
        return v;
    }


    public void show() {
        appearFactor.appear(MovementType.approach, 2);
        justPressedByHuman = false;
    }


    public void hide() {
        appearFactor.destroy(MovementType.lighty, 3.5);
    }


    private void moveSelection() {
        if (TouchMode.tmDefault.touchedCurrently) return;
        selectionEngineYio.move();
    }


    public void setJustPressedByHuman(boolean justPressedByHuman) {
        this.justPressedByHuman = justPressedByHuman;
    }


    public boolean isAvailable() {
        return !appearFactor.isInDestroyState();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (!isAvailable()) return false;
        if (Math.abs(touchPosition.center.x - touchPoint.x) > touchPosition.radius) return false;
        if (Math.abs(touchPosition.center.y - touchPoint.y) > touchPosition.radius) return false;
        return true;
    }
}
