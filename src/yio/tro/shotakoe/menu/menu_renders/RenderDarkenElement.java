package yio.tro.shotakoe.menu.menu_renders;

import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderDarkenElement extends RenderInterfaceElement{

    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, element.getViewPosition());
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
