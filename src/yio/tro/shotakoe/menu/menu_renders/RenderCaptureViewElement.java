package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.gameplay.CaptureViewElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderCaptureViewElement extends RenderInterfaceElement{


    private CaptureViewElement cvElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        cvElement = (CaptureViewElement) element;
        RenderableTextYio title = cvElement.title;
        BitmapFont font = title.font;
        GraphicsYio.setFontAlpha(font, cvElement.getAlpha());
        GraphicsYio.renderText(batch, title);
        GraphicsYio.setFontAlpha(font, 1);
    }
}
