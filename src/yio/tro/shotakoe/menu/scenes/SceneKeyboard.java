package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.shotakoe.menu.elements.keyboard.CustomKeyboardElement;

public class SceneKeyboard extends ModalSceneYio {

    public CustomKeyboardElement customKeyboardElement;


    @Override
    protected void initialize() {
        customKeyboardElement = uiFactory.getCustomKeyboardElement()
                .setSize(1, 0.1);
    }


    @Override
    protected void onAppear() {
        forceElementsToTop();
    }


    public void setReaction(AbstractKbReaction reaction) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setReaction(reaction);
    }


    public void setValue(String value) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setValue(value);
    }


    public void setHint(String key) {
        if (customKeyboardElement == null) return;
        customKeyboardElement.setHint(languagesManager.getString(key));
    }

}
