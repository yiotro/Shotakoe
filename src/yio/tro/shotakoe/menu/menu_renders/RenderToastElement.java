package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.ToastElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderToastElement extends RenderInterfaceElement{

    private ToastElement alElement;
    RenderableTextYio tempRenderableText;
    private TextureRegion backgroundTexture;


    public RenderToastElement() {
        tempRenderableText = new RenderableTextYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        alElement = (ToastElement) element;

        renderBackground();
        renderText();
    }


    private void renderText() {
        for (RenderableTextYio renderableTextYio : alElement.visualTextContainer.textList) {
            GraphicsYio.setFontAlpha(renderableTextYio.font, alpha);
            tempRenderableText.setBy(renderableTextYio);
            if (alElement.backgroundEnabled) {
                tempRenderableText.font.setColor(alElement.textOpacity, alElement.textOpacity, alElement.textOpacity, alpha);
            }
            tempRenderableText.position.x += alElement.getViewPosition().x;
            tempRenderableText.position.y += alElement.getViewPosition().y;
            tempRenderableText.updateBounds();
            if (alElement.hasParent() && !alElement.getParent().getViewPosition().contains(tempRenderableText.bounds)) continue;
            GraphicsYio.renderText(batch, tempRenderableText);
            GraphicsYio.setFontAlpha(renderableTextYio.font, 1);
            if (alElement.backgroundEnabled) {
                tempRenderableText.font.setColor(Color.BLACK);
            }
        }
    }


    private void renderBackground() {
        if (!alElement.backgroundEnabled) return;
        GraphicsYio.setBatchAlpha(batch, alElement.backgroundOpacity * alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, alElement.viewBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


}
