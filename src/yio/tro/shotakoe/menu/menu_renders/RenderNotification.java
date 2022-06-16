package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.NotificationElement;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderNotification extends RenderInterfaceElement {

    private TextureRegion backgroundTexture;
    private NotificationElement notificationElement;
    private BitmapFont font;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        notificationElement = (NotificationElement) element;
        font = notificationElement.font;

        if (notificationElement.getFactor().getProgress() < 0.01) return;

        MenuRenders.renderShadow.renderBottomPart(notificationElement.shadowPosition);
        renderBackground();
        renderMessage();
    }


    private void renderMessage() {
        Color color = font.getColor();
        font.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(font, notificationElement.getFactor().getValue());

        GraphicsYio.renderText(batch, font, notificationElement.message, notificationElement.textPosition);

        GraphicsYio.setFontAlpha(font, 1);
        font.setColor(color);
    }


    private void renderBackground() {

        GraphicsYio.drawByRectangle(
                batch,
                backgroundTexture,
                notificationElement.getViewPosition()
        );
    }


}
