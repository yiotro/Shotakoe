package yio.tro.shotakoe.game.tests;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.debug.DebugFlags;

public abstract class AbstractTest {


    GameController gameController;
    long timeStart;


    public AbstractTest() {

    }


    public void perform(GameController gameController) {
        this.gameController = gameController;

        prepare();
        execute();
        if (isInstant()) {
            end();
        }
    }


    public void move() {

    }


    protected void end() {
        DebugFlags.testingModeEnabled = false;
        gameController.yioGdxGame.setCurrentTest(null);
    }


    public abstract boolean isInstant();


    protected void prepare() {
        DebugFlags.testingModeEnabled = true;
        timeStart = System.currentTimeMillis();
        gameController.yioGdxGame.setCurrentTest(this);
    }


    public abstract String getName();


    protected abstract void execute();


    protected String getFinishTitle() {
        int time = Yio.convertMillisIntoFrames(System.currentTimeMillis() - timeStart);
        return getClass().getSimpleName() + " finished in " + Yio.convertTime(time);
    }
}
