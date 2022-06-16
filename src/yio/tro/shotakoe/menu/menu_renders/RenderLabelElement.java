package yio.tro.shotakoe.menu.menu_renders;

import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderLabelElement extends RenderInterfaceElement{


    private LabelElement labelElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        labelElement = (LabelElement) element;
        if (!labelElement.backgroundEnabled) {
            renderWithoutBackground();
            return;
        }
        renderWithBackground();
    }


    private void renderWithBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangleUpsideDown(batch, labelElement.cacheTitleTexture, labelElement.incBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderWithoutBackground() {
        if (labelElement.hasParent() && !labelElement.getParent().getViewPosition().contains(labelElement.title.bounds)) return;
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                labelElement.title,
                labelElement.getFactor().getValue() * labelElement.targetAlpha
        );
    }

}
