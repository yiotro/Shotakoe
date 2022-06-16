package yio.tro.shotakoe.menu.menu_renders;

import yio.tro.shotakoe.menu.elements.AnnounceViewElement;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderAnnounceViewElement extends RenderInterfaceElement {


    private AnnounceViewElement avElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        avElement = (AnnounceViewElement) element;

        if (avElement.getFactor().getProgress() < 0.02) return;

        renderShadow();
        renderBackground();
        renderConvex();
        renderTitle();
        renderVisualTextContainer();
    }


    private void renderConvex() {
        GraphicsYio.setBatchAlpha(batch, 0.12f * avElement.getAlpha());
        MenuRenders.renderConvex.renderConvex(avElement.adjustedPosition);
    }


    private void renderVisualTextContainer() {
        if (avElement.getFactor().getProgress() < 0.2) return;
        for (RenderableTextYio renderableTextYio : avElement.visualTextContainer.viewList) {
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }


    private void renderTitle() {
        if (avElement.getViewPosition().width < avElement.title.width) return;
        GraphicsYio.renderText(batch, avElement.title);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(
                avElement.adjustedPosition,
                BackgroundYio.white
        );
    }


    private void renderShadow() {
        if (avElement.getShadowAlpha() == 0) return;
        GraphicsYio.setBatchAlpha(batch, avElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(avElement.adjustedPosition);
    }
}
