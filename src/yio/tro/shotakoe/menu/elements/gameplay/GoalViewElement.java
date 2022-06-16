package yio.tro.shotakoe.menu.elements.gameplay;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.SelfScrollWorkerYio;

import java.util.ArrayList;

public class GoalViewElement extends InterfaceElement<GoalViewElement> {

    public RenderableTextYio title;
    public SelfScrollWorkerYio selfScrollWorkerYio;


    public GoalViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        selfScrollWorkerYio = new SelfScrollWorkerYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        setTitle("-");
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected GoalViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        selfScrollWorkerYio.move();
        title.centerVertical(viewPosition);
        title.position.x = 0.02f * GraphicsYio.width + selfScrollWorkerYio.getDelta();
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        setTitle(generateTitleString());
        selfScrollWorkerYio.launch(title.width, position.width - 0.04f * GraphicsYio.width);
    }


    private String generateTitleString() {
        ArrayList<String> strings = new ArrayList<>();
        LoadingParameters loadingParameters = getObjectsLayer().getLoadingParameters();
        strings.add(generateSimpleRule("defeat_opponent", loadingParameters.lgParameters.enemy));
        strings.add(generateSimpleRule("avoid_bomb", loadingParameters.lgParameters.bomb));
        strings.add(generateParameterizedRule("capture_cells", loadingParameters.targetSize != -1, loadingParameters.targetSize));
        strings.add(generateParameterizedRule("turn_limit_condition", loadingParameters.turnLimit != -1, loadingParameters.turnLimit));
        StringBuilder builder = new StringBuilder();
        boolean empty = true;
        for (String string : strings) {
            if (string.length() == 0) continue;
            if (!empty) {
                builder.append(", ");
                string = string.toLowerCase();
            }
            builder.append(string);
            empty = false;
        }
        if (builder.length() == 0) {
            builder.append("-");
        }
        return builder.toString();
    }


    private String generateParameterizedRule(String key, boolean condition, int value) {
        if (!condition) return "";
        String string = LanguagesManager.getInstance().getString(key);
        return string.replace("[value]", "" + value);
    }


    private String generateSimpleRule(String key, boolean condition) {
        if (!condition) return "";
        return LanguagesManager.getInstance().getString(key);
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
        return MenuRenders.renderGoalsViewElement;
    }
}
