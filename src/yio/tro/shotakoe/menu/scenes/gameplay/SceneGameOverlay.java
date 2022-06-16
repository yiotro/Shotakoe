package yio.tro.shotakoe.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.MenuSwitcher;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneGameOverlay extends ModalSceneYio {

    public ViewTouchModeElement viewTouchModeElement;


    public SceneGameOverlay() {
        viewTouchModeElement = null;
    }


    @Override
    public void initialize() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(0)
                .alignTop(0)
                .setTouchOffset(0.05)
                .setCustomTexture(getTextureFromAtlas("pause_menu"))
                .setIgnoreResumePause(true)
                .setReaction(getPauseReaction())
                .setKey("pause_menu")
                .setAnimation(AnimationYio.up)
                .setHotkeyKeycode(Input.Keys.BACK)
                .setSelectionTexture(getSelectionTexture());

        viewTouchModeElement = uiFactory.getViewTouchModeElement()
                .setSize(1, 0.06)
                .centerHorizontal()
                .alignTop(0);
    }


    private Reaction getPauseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.applyFullTransitionToUI();
                MenuSwitcher.getInstance().createPauseMenu();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        TouchMode touchMode = yioGdxGame.gameController.touchMode;
        if (touchMode != null) {
            onTouchModeSet(touchMode);
        }
    }


    public void onTouchModeSet(TouchMode touchMode) {
        if (viewTouchModeElement == null) return;
        viewTouchModeElement.onTouchModeSet(touchMode);
    }

}
