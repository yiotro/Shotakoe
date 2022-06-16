package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.VisualTextContainer;

public class RenderButton extends RenderInterfaceElement {

    TextureRegion buttonPixel, blackPixel;
    ButtonYio buttonYio;
    RectangleYio viewPosition;
    RectangleYio touchArea;
    RectangleYio tempRectangle;
    RenderableTextYio tempRenderableText;


    public RenderButton() {
        touchArea = new RectangleYio();
        tempRectangle = new RectangleYio();
        tempRenderableText = new RenderableTextYio();
    }


    @Override
    public void loadTextures() {
        buttonPixel = GraphicsYio.loadTextureRegion("pixels/button.png", false);
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        buttonYio = (ButtonYio) element;
        if (buttonYio.getFactor().getProgress() < 0.01) return;
        if (buttonYio.isHidden()) return;
        
        viewPosition = buttonYio.getViewPosition();
        renderCurrentButton();
    }


    public void renderCurrentButton() {
        renderButtonShadow();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderDefaultBackground();
        renderCustomBackground();
        renderInternalTextTexture();
        renderConvex();

        GraphicsYio.setBatchAlpha(batch, 1);

        renderSelection();
        renderBorder();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderConvex() {
        if (buttonYio.hasCustomTexture()) return;
        if (!buttonYio.convex) return;
        GraphicsYio.setBatchAlpha(batch, 0.12 * alpha * (1 - buttonYio.selectionEngineYio.factorYio.getValue()));
        MenuRenders.renderConvex.renderConvex(buttonYio.getViewPosition(), buttonYio.viewCornerRadius);
    }


    void renderInternalTextTexture() {
        if (buttonYio.hasCustomTexture()) return;
        if (buttonYio.textContainers.size() == 0) return;
        if (buttonYio.internalTextTexture == null) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangleUpsideDown(batch, buttonYio.internalTextTexture, buttonYio.internalRenderPosition);
    }


    private void renderBorder() {
        if (!buttonYio.isBorderEnabled()) return;
        GraphicsYio.setBatchAlpha(batch, 0.25 * alpha);
        MenuRenders.renderRoundBorder.renderRoundBorder(viewPosition, buttonYio.viewCornerRadius);
    }


    private void renderCustomBackground() {
        if (!buttonYio.hasCustomTexture()) return;
        GraphicsYio.drawByRectangle(batch, buttonYio.getCustomTexture(), viewPosition);
    }


    private void renderDefaultBackground() {
        if (buttonYio.hasCustomTexture()) return;
        MenuRenders.renderRoundShape.renderRoundShape(
                batch,
                buttonYio.roundShapePosition,
                buttonYio.background,
                buttonYio.viewCornerRadius,
                buttonYio.getFactor().getProgress() == 1
        );
    }


    private void renderTextInDebugMode() {
        // this shouldn't be used in normal situation
        if (buttonYio.getFactor().getProgress() < 0.95) return;
        if (buttonYio.textContainers.size() == 0) return;

        for (VisualTextContainer textContainer : buttonYio.textContainers) {
            tempRectangle.setBy(textContainer.position);
            tempRectangle.x += viewPosition.x;
            tempRectangle.y += viewPosition.y;
            GraphicsYio.setBatchAlpha(batch, 1);
            for (RenderableTextYio renderableTextYio : textContainer.textList) {
                tempRenderableText.setBy(renderableTextYio);
                tempRenderableText.position.x += tempRectangle.x;
                tempRenderableText.position.y += tempRectangle.y;
                tempRenderableText.updateBounds();
                GraphicsYio.setFontAlpha(renderableTextYio.font, 0.25);
                GraphicsYio.renderText(batch, tempRenderableText);
                GraphicsYio.setFontAlpha(renderableTextYio.font, 1);
            }
        }
    }


    private void renderTouchArea() {
        updateTouchArea();

        GraphicsYio.renderBorder(batch, blackPixel, touchArea);
    }


    private void updateTouchArea() {
        RectangleYio pos = buttonYio.getViewPosition();
        touchArea.x = pos.x - buttonYio.touchOffset;
        touchArea.width = pos.width + 2 * buttonYio.touchOffset;
        touchArea.y = pos.y - buttonYio.touchOffset;
        touchArea.height = pos.height + 2 * buttonYio.touchOffset;
    }


    public void renderSelection() {
        if (!buttonYio.isSelected()) return;

        GraphicsYio.setBatchAlpha(batch, buttonYio.selectionEngineYio.getAlpha() * alpha);

        if (buttonYio.selectionTexture == null) {
            if (buttonYio.rectangularSelectionEnabled) {
                GraphicsYio.drawByRectangle(batch, blackPixel, viewPosition);
            } else {
                MenuRenders.renderRoundShape.renderRoundShape(viewPosition, BackgroundYio.black, buttonYio.viewCornerRadius);
            }
        } else {
            GraphicsYio.drawByRectangle(batch, buttonYio.selectionTexture, buttonYio.touchAreaPosition);
        }

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    public void renderButtonShadow() {
        if (!buttonYio.isVisible()) return;
        if (!buttonYio.shadowEnabled) return;
        if (buttonYio.getShadowAlpha() == 0) return;

        GraphicsYio.setBatchAlpha(batch, buttonYio.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(viewPosition);
    }

}
