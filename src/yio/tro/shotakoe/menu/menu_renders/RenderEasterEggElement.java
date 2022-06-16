package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.easter_egg.EasterEggElement;
import yio.tro.shotakoe.menu.elements.easter_egg.EeItem;
import yio.tro.shotakoe.stuff.AtlasLoader;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.Masking;
import yio.tro.shotakoe.stuff.RectangleYio;

public class RenderEasterEggElement extends RenderInterfaceElement {

    TextureRegion[] colors;
    private EasterEggElement eeElement;
    private TextureRegion backgroundTexture;
    RectangleYio rectangleYio;


    public RenderEasterEggElement() {
        rectangleYio = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
        AtlasLoader atlasLoader = new AtlasLoader("game/atlas/", false);
        colors = new TextureRegion[ColorYio.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = atlasLoader.getTexture(ColorYio.values()[i] + ".png");
        }
    }


    @Override
    public void render(InterfaceElement element) {
        eeElement = (EasterEggElement) element;

        GraphicsYio.setBatchAlpha(batch, alpha);
        if (eeElement.getFactor().getProgress() == 1) {
            renderInternals();
        } else {
            renderWithMasking();
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderWithMasking() {
        batch.end();
        Masking.begin();
        prepareShapeRenderer();
        drawRectShape(eeElement.getViewPosition());
        shapeRenderer.end();
        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(batch);
    }


    private void renderInternals() {
        renderBackground();
        renderItems();
    }


    private void renderItems() {
        GraphicsYio.setBatchAlpha(batch, 0.5);
        for (EeItem item : eeElement.items) {
            if (!item.isVisible()) continue;
            rectangleYio.setBy(item.position);
            rectangleYio.y = GraphicsYio.height / 2 + (GraphicsYio.height / 2 - (rectangleYio.y + rectangleYio.height));
            GraphicsYio.drawByRectangle(batch, colors[item.colorIndex], rectangleYio);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(batch, backgroundTexture, eeElement.getViewPosition());
    }
}
