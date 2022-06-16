package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneExceptionReport extends SceneYio{

    Exception exception;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        uiFactory.getExceptionViewElement()
                .setSize(1, 1)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setException(exception);

        uiFactory.getButton()
                .setSize(1.1, 0.06)
                .centerHorizontal()
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setBackground(BackgroundYio.cyan)
                .applyText("Ok")
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    public void setException(Exception exception) {
        this.exception = exception;
    }
}
