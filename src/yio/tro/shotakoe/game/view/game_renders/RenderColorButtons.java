package yio.tro.shotakoe.game.view.game_renders;

import yio.tro.shotakoe.game.general.ColorButton;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.SelectionEngineYio;

public class RenderColorButtons extends GameRender {

    RectangleYio rectangleYio;


    public RenderColorButtons() {
        rectangleYio = new RectangleYio();
    }


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        for (ColorButton button : getObjectsLayer().colorButtonsManager.buttons) {
            float alpha = button.appearFactor.getValue();
            rectangleYio.setBy(button.viewPosition);
            SelectionEngineYio selectionEngineYio = button.selectionEngineYio;
            GraphicsYio.setBatchAlpha(batchMovable, button.getShadowAlpha());
            MenuRenders.renderShadow.renderShadow(batchMovable, rectangleYio, 0.065f * GraphicsYio.width);
            GraphicsYio.setBatchAlpha(batchMovable, alpha);
            MenuRenders.renderRoundShape.renderRoundShape(batchMovable, rectangleYio, button.backgroundYio, 0.025f * GraphicsYio.width, false);
            GraphicsYio.setBatchAlpha(batchMovable, 0.12 * (1 - selectionEngineYio.factorYio.getValue()) * alpha);
            MenuRenders.renderConvex.renderConvex(batchMovable, rectangleYio, 0.025f * GraphicsYio.width);
            GraphicsYio.setBatchAlpha(batchMovable, 0.3 * alpha);
            MenuRenders.renderRoundBorder.renderRoundBorder(batchMovable, rectangleYio, 0.025f * GraphicsYio.width);
            if (selectionEngineYio.isSelected()) {
                rectangleYio.setBy(button.touchPosition);
                rectangleYio.increase(0.005f * GraphicsYio.width);
                GraphicsYio.setBatchAlpha(batchMovable, selectionEngineYio.getAlpha() * alpha);
                MenuRenders.renderRoundShape.renderRoundShape(batchMovable, rectangleYio, BackgroundYio.black, 0.038f * GraphicsYio.width, false);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
