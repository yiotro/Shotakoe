package yio.tro.shotakoe.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.VisualTextContainer;

import java.util.ArrayList;

public class ToastElement extends InterfaceElement<ToastElement> {

    public VisualTextContainer visualTextContainer;
    public boolean backgroundEnabled;
    BitmapFont font;
    long lifeDuration;
    long deathTime;
    public RectangleYio viewBounds;
    boolean centered;
    public double backgroundOpacity;
    public float textOpacity;


    public ToastElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        visualTextContainer = new VisualTextContainer();
        viewBounds = new RectangleYio();
        backgroundEnabled = false;
        font = Fonts.miniFont;
        lifeDuration = -1;
        centered = false;
        backgroundOpacity = 1;
        textOpacity = 0.8f;
    }


    public ToastElement applyText(String key) {
        visualTextContainer.clear();
        visualTextContainer.position.setBy(position);
        visualTextContainer.position.height = position.height;
        visualTextContainer.applyManyTextLines(font, LanguagesManager.getInstance().getString(key));
        for (RenderableTextYio renderableTextYio : visualTextContainer.textList) {
            renderableTextYio.centered = centered;
        }
        visualTextContainer.updateTextPosition();
        return this;
    }


    public ToastElement setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public ToastElement setBackgroundOpacity(double backgroundOpacity) {
        this.backgroundOpacity = backgroundOpacity;
        return this;
    }


    public ToastElement setTextOpacity(double textOpacity) {
        this.textOpacity = (float) textOpacity;
        return this;
    }


    public ToastElement setLifeDuration(int lifeDuration) {
        this.lifeDuration = lifeDuration;
        return this;
    }


    @Override
    protected ToastElement getThis() {
        return this;
    }


    private void updateViewBounds() {
        viewBounds.setBy(viewPosition);

        ArrayList<RenderableTextYio> textList = visualTextContainer.textList;
        if (textList.size() == 0) return;

        RenderableTextYio topText = textList.get(0);
        RenderableTextYio bottomText = textList.get(textList.size() - 1);

        if (textList.size() == 2 && bottomText.string.equals(" ")) {
            bottomText = topText;
        }

        viewBounds.y = viewPosition.y + bottomText.bounds.y - 0.02f * GraphicsYio.height;
        viewBounds.height = topText.bounds.y + topText.bounds.height - bottomText.bounds.y + 0.03f * GraphicsYio.height;
    }


    private void checkToDie() {
        if (appearFactor.isInDestroyState()) return;
        if (!isReadyToDie()) return;
        destroy();
    }


    private boolean isReadyToDie() {
        if (lifeDuration == -1) return false;
        return System.currentTimeMillis() > deathTime;
    }


    @Override
    public void onMove() {
        moveVisualContainer();
        updateViewBounds();
        checkToDie();
    }


    private void moveVisualContainer() {
        visualTextContainer.position.setBy(viewPosition);
        visualTextContainer.updateTextPosition();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        deathTime = System.currentTimeMillis() + lifeDuration;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    public ToastElement setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderToastElement;
    }

}
