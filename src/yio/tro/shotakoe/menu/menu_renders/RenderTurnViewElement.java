package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.gameplay.TurnViewElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderTurnViewElement extends RenderInterfaceElement{


    private TurnViewElement tvElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        tvElement = (TurnViewElement) element;
        RenderableTextYio title = tvElement.title;
        if (title.string.length() < 2) return;
        BitmapFont font = title.font;
        GraphicsYio.setFontAlpha(font, tvElement.getAlpha());
        GraphicsYio.renderText(batch, title);
        GraphicsYio.setFontAlpha(font, 1);
    }
}
