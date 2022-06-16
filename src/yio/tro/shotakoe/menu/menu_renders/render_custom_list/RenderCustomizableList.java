package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.Masking;

public class RenderCustomizableList extends RenderInterfaceElement {


    CustomizableListYio customizableListYio;
    private TextureRegion redPixel;


    @Override
    public void loadTextures() {
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        customizableListYio = (CustomizableListYio) element;

        if (customizableListYio.getFactor().getProgress() < 0.01) return;

        renderShadow();
        renderDarken();
        GraphicsYio.setBatchAlpha(batch, alpha);
        renderBackground();
        renderBorders();

        renderItems();
        renderConvex();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderConvex() {
        if (!customizableListYio.convexEnabled) return;
        GraphicsYio.setBatchAlpha(batch, 0.12f * customizableListYio.getAlpha());
        MenuRenders.renderConvex.renderConvex(customizableListYio.getViewPosition());
    }


    private void renderDarken() {
        if (!customizableListYio.darken) return;
        if (customizableListYio.getFactor().getProgress() == 1) return;
        if (customizableListYio.getFactor().getProgress() == 0) return;
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, customizableListYio.screenPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBorders() {
        if (!customizableListYio.areShowBordersEnabled()) return;
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.getViewPosition());
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.maskPosition);
    }


    private void renderBackground() {
        if (!customizableListYio.isBackgroundEnabled()) return;
        MenuRenders.renderRoundShape.renderRoundShape(
                customizableListYio.getViewPosition(),
                customizableListYio.backgroundColor,
                customizableListYio.cornerRadius
        );
    }


    private void renderShadow() {
        if (!customizableListYio.isShadowEnabled()) return;
        if (customizableListYio.getShadowAlpha() == 0) return;
        GraphicsYio.setBatchAlpha(batch, customizableListYio.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(
                customizableListYio.getViewPosition(),
                GraphicsYio.shadowCornerRadius
        );
    }


    private void renderItems() {
        if (customizableListYio.getFactor().getProgress() < 0.33) return;
        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(customizableListYio.maskPosition, 0.92f * customizableListYio.cornerRadius);
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        renderLtTarget();
        Masking.end(batch);
    }


    void renderInternals() {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!item.isCurrentlyVisible()) continue;
            AbstractRenderCustomListItem renderSystem = item.getRender();
            renderSystem.setAlpha(alpha);
            renderSystem.renderItem(item);
        }
    }


    private void renderLtTarget() {
        if (!customizableListYio.ltActive) return;
        GraphicsYio.renderBorder(batch, redPixel, customizableListYio.ltTarget.viewPosition, 2 * GraphicsYio.borderThickness);
    }


}
