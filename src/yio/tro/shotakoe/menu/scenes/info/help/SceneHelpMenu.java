package yio.tro.shotakoe.menu.scenes.info.help;

import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;

public class SceneHelpMenu extends SceneYio {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        spawnBackButton(new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainMenu.create();
            }
        });
    }
}
