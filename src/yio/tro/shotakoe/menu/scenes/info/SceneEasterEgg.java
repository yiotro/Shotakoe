package yio.tro.shotakoe.menu.scenes.info;

import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.easter_egg.EasterEggElement;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneEasterEgg extends SceneYio {


    public EasterEggElement easterEggElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        createEasterEgg();
        spawnBackButton(getOpenSceneReaction(Scenes.mainMenu));
    }


    private void createEasterEgg() {
        easterEggElement = uiFactory.getEasterEggElement()
                .setSize(1, 1)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.inertia, MenuParams.ANIM_SPEED);
    }
}
