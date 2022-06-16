package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;

public abstract class ModalSceneYio extends SceneYio {


    protected ButtonYio defaultPanel;
    protected ButtonYio closeButton;
    private SceneYio previousFocusScene;


    public ModalSceneYio() {
        super();
        defaultPanel = null;
        previousFocusScene = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return null;
    }


    @Override
    protected void applyBackground() {

    }


    @Override
    public void create() {
        if (isCurrentlyVisible()) return;
        super.create();
    }


    @Override
    public void addLocalElement(InterfaceElement interfaceElement) {
        interfaceElement.setFakeDyingStatusEnabled(true);
        super.addLocalElement(interfaceElement);
    }


    @Override
    protected void beginCreation() {
        updatePreviousFocusScene();
        if (!shouldNotTouchFocusScene()) {
            menuControllerYio.setFocusScene(this);
        }
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    private void updatePreviousFocusScene() {
        if (shouldNotTouchFocusScene()) return;
        previousFocusScene = menuControllerYio.getFocusScene();
        if (previousFocusScene == this) return;
        while (previousFocusScene != null && previousFocusScene instanceof ModalSceneYio) {
            SceneYio temp = ((ModalSceneYio) previousFocusScene).previousFocusScene;
            if (temp == previousFocusScene) break;
            previousFocusScene = temp;
        }
    }


    protected boolean shouldNotTouchFocusScene() {
        return false;
    }


    @Override
    protected void endInitialization() {
        super.endInitialization();

        for (InterfaceElement element : getLocalElementsList()) {
            element.setOnTopOfGameView(true);
        }
    }


    protected void createDefaultPanel(double height) {
        defaultPanel = uiFactory.getButton()
                .setSize(0.9, height)
                .centerHorizontal()
                .alignBottom(Math.max(0.05, 0.45 - height / 2))
                .setSilent(true)
                .setBackground(BackgroundYio.white);
    }


    protected void createCloseButton() {
        closeButton = uiFactory.getButton()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setHidden(true)
                .setHotkeyKeycode(Input.Keys.BACK)
                .setDebugName("invisible_close_button")
                .setReaction(getCloseReaction());
    }


    protected void createDarken() {
        uiFactory.getDarkenElement().setSize(1, 1);
    }


    @Override
    public void destroy() {
        if (previousFocusScene != null) {
            menuControllerYio.setFocusScene(previousFocusScene);
        }
        super.destroy();
    }


    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }

}
