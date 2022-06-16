package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.menu.elements.forefinger.ForefingerElement;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneForefinger extends ModalSceneYio{

    public ForefingerElement forefinger;


    @Override
    protected void initialize() {
        forefinger = uiFactory.getForefingerElement()
                .setAppearParameters(MovementType.approach, 8);
    }


    @Override
    protected void onAppear() {
        forceElementsToTop();
    }


    @Override
    public boolean isCurrentlyVisible() {
        return forefinger != null && forefinger.getFactor().getProgress() > 0;
    }
}
