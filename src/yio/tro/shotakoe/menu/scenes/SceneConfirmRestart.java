package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.game.general.RestartManager;
import yio.tro.shotakoe.menu.reactions.Reaction;

public class SceneConfirmRestart extends AbstractConfirmationScene{

    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                (new RestartManager(gameController)).apply();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_restart";
    }

}
