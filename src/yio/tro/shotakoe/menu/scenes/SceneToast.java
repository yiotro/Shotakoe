package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.menu.elements.ToastElement;
import yio.tro.shotakoe.menu.elements.AnimationYio;

public class SceneToast extends ModalSceneYio{


    private ToastElement toastElement;


    @Override
    protected void initialize() {
        toastElement = uiFactory.getToastElement()
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.04)
                .setAnimation(AnimationYio.none)
                .setBackgroundEnabled(true)
                .setCentered(true)
                .setBackgroundOpacity(0.95)
                .setTextOpacity(0.9);
    }


    @Override
    protected boolean shouldNotTouchFocusScene() {
        return true;
    }


    public void show(String key) {
        show(key, 150);
    }


    public void show(String key, int duration) {
        if (isCurrentlyVisible()) {
            destroy();
        }
        create();
        toastElement
                .setLifeDuration(duration)
                .applyText(key);
    }

}
