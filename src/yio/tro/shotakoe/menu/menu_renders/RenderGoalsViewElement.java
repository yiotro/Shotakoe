package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.gameplay.GoalViewElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.Masking;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.SelfScrollWorkerYio;

public class RenderGoalsViewElement extends RenderInterfaceElement{


    private GoalViewElement gvElement;
    private RenderableTextYio title;
    private SelfScrollWorkerYio selfScrollWorkerYio;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        gvElement = (GoalViewElement) element;
        title = gvElement.title;
        selfScrollWorkerYio = gvElement.selfScrollWorkerYio;
        if (selfScrollWorkerYio.isActive()) {
            renderWithMasking();
        } else {
            renderTitle();
        }
    }


    private void renderWithMasking() {
        batch.end();
        Masking.begin();
        prepareShapeRenderer();
        drawRectShape(gvElement.getViewPosition());
        shapeRenderer.end();
        batch.begin();
        Masking.continueAfterBatchBegin();
        renderTitle();
        Masking.end(batch);
    }


    private void renderTitle() {
        BitmapFont font = title.font;
        GraphicsYio.setFontAlpha(font, gvElement.getAlpha() * selfScrollWorkerYio.getAlpha());
        GraphicsYio.renderText(batch, title);
        GraphicsYio.setFontAlpha(font, 1);
    }
}
