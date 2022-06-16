package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.menu.scenes.SceneYio;

public abstract class AbstractLoadingSceneYio extends SceneYio{

    private LabelElement label;
    boolean ready;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        ready = false;

        label = uiFactory.getLabelElement()
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .centerVertical()
                .setTitle("...");
    }


    @Override
    public void move() {
        super.move();

        if (!ready) return;
        if (label.getFactor().getProgress() < 1) return;

        ready = false;
        applyAction();
    }


    protected abstract void applyAction();


    @Override
    protected void onAppear() {
        super.onAppear();

        yioGdxGame.applyFullTransitionToUI();
        ready = true;
    }
}
