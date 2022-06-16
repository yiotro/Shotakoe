package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.elements.CheckButtonYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderCheckButton extends RenderInterfaceElement {

    TextureRegion activeTexture;
    private BitmapFont font;
    private CheckButtonYio checkButton;
    private TextureRegion defTexture;


    @Override
    public void loadTextures() {
        activeTexture = GraphicsYio.loadTextureRegion("menu/check_button/chk_active.png", true);
        defTexture = GraphicsYio.loadTextureRegion("menu/check_button/chk_def.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        checkButton = (CheckButtonYio) element;
        font = Fonts.gameFont;

        if (element.getFactor().getProgress() < 0.2) return;

        renderSelection();
        renderActiveSquare();
        renderText();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderText() {
        if (checkButton.renderableText.width > checkButton.getViewPosition().width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, checkButton.renderableText, alpha);
    }


    private void renderActiveSquare() {
        if (alpha < 1) {
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
        if (checkButton.activeFactor.getProgress() < 1) {
            GraphicsYio.drawByRectangle(batch, defTexture, checkButton.squareView);
        }
        if (checkButton.activeFactor.getProgress() > 0) {
            GraphicsYio.setBatchAlpha(batch, Math.min(checkButton.activeFactor.getValue(), alpha));
            GraphicsYio.drawByRectangle(batch, activeTexture, checkButton.squareView);
        }
    }


    private void renderSelection() {
        if (checkButton.selectionFactor.getProgress() > 0) {
            GraphicsYio.setBatchAlpha(batch, 0.2 * checkButton.selectionFactor.getValue());
            GraphicsYio.drawByRectangle(batch, blackPixel, checkButton.getViewPosition());
            GraphicsYio.setBatchAlpha(batch, 1);
        }
    }


}
