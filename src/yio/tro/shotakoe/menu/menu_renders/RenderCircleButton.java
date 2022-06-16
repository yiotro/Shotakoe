package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.CircleButtonYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderCircleButton extends RenderInterfaceElement{

    private CircleButtonYio circleButton;
    private TextureRegion selectEffectTexture;


    @Override
    public void loadTextures() {
        selectEffectTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        circleButton = (CircleButtonYio) element;

        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByCircle(batch, circleButton.textureRegion, circleButton.renderPosition);
        renderSelection();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSelection() {
        if (!circleButton.isSelected()) return;
        if (circleButton.getFactor().getProgress() < 0.01) return;

        GraphicsYio.setBatchAlpha(batch, alpha * circleButton.selectionFactor.getValue());
        GraphicsYio.drawByCircle(batch, selectEffectTexture, circleButton.effectPosition);
    }

}
