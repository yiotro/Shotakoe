package yio.tro.shotakoe.menu.elements;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.VisualTextContainer;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ExceptionViewElement extends InterfaceElement<ExceptionViewElement> {

    Exception exception;
    public VisualTextContainer visualTextContainer;
    RectangleYio internalPosition;


    public ExceptionViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        visualTextContainer = new VisualTextContainer();
        internalPosition = new RectangleYio();
    }


    @Override
    protected ExceptionViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateInternalPosition();
        visualTextContainer.move(internalPosition);
    }


    private void updateInternalPosition() {
        internalPosition.setBy(viewPosition);
        float delta = 0.01f * GraphicsYio.width;
        internalPosition.x += delta;
        internalPosition.width -= 2 * delta;
    }


    public ExceptionViewElement setException(Exception exception) {
        this.exception = exception;

        ArrayList<String> strings = generateStringsList(exception);

        visualTextContainer.clear();
        visualTextContainer.setSize(position.width, position.height);
        visualTextContainer.applyManyTextLines(Fonts.miniFont, strings);
        updateInternalPosition();
        visualTextContainer.move(internalPosition);
        return this;
    }


    private ArrayList<String> generateStringsList(Exception exception) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("[shotakoe " + 0 + "]");
        strings.add(" ");
        String caughtExceptionSource = LanguagesManager.getInstance().getString("caught_exception");
        strings.addAll(Yio.createArrayListFromString(caughtExceptionSource));
        strings.add(" ");
        strings.add("-> " + removePackage(exception.toString()));
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            String temp = removePackage("" + stackTraceElement);
            StringBuilder builder = new StringBuilder();
            builder.append("- ");
            StringTokenizer tokenizer = new StringTokenizer(temp, ".");
            while (tokenizer.hasMoreTokens()) {
                builder.append(tokenizer.nextToken()).append(". ");
            }
            strings.add(builder.toString());
        }
        return strings;
    }


    private String removePackage(String src) {
        String sub = "yio.tro.shotakoe.";
        if (!src.contains(sub)) return src;
        return src.replace(sub, "");
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
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
        return MenuRenders.renderExceptionViewElement;
    }
}
