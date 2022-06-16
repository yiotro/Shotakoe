package yio.tro.shotakoe.menu.elements.gameplay;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.SettingsManager;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.RepeatYio;

public class TurnViewElement extends InterfaceElement<TurnViewElement> {

    public RenderableTextYio title;
    RepeatYio<TurnViewElement> repeatUpdate;


    public TurnViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdate = new RepeatYio<TurnViewElement>(this, 15) {
            @Override
            public void performAction() {
                parent.updateTitleString();
            }
        };
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected TurnViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatUpdate.move();
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        float delta = 0.02f * GraphicsYio.width;
        if (SettingsManager.getInstance().roundedScreen) {
            delta = 0.05f * GraphicsYio.width;
        }
        title.position.x = viewPosition.x + viewPosition.width - delta - title.width;
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        updateTitleString();
    }


    private void updateTitleString() {
        String string = generateTitleString();
        if (title.string.equals(string)) return;
        setTitle(string);
    }


    private String generateTitleString() {
        if (getObjectsLayer().finishManager.turnLimit == -1) return "-";
        int humanTurnsCounter = getObjectsLayer().turnsManager.getLoopsCounter();
        return LanguagesManager.getInstance().getString("turn") + " " + humanTurnsCounter;
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
        return MenuRenders.renderTurnViewElement;
    }
}
