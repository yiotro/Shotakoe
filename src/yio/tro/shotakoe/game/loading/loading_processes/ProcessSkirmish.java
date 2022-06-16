package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.generator.LevelGenerator;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.game.loading.LoadingManager;

import java.util.Random;

public class ProcessSkirmish extends AbstractLoadingProcess {

    public ProcessSkirmish(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.custom);
        initLevelSize(LevelSize.def);
    }


    @Override
    public void createBasicStuff() {

    }


    @Override
    public void createAdvancedStuff() {
        Random random = new Random(getSeed());

        LgParameters lgParameters = loadingParameters.lgParameters;
        lgParameters.setActiveCellsValue(0.85);
        (new LevelGenerator(getObjectsLayer())).apply(lgParameters, random);
        getObjectsLayer().move();
    }


    private long getSeed() {
        long seed = loadingParameters.seed;
        if (seed != 0) return seed;
        return YioGdxGame.random.nextLong();
    }
}
