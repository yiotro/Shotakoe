package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.game.general.Difficulty;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.generator.LevelGenerator;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.game.loading.LoadingManager;

import java.util.Random;

public class ProcessCalendar extends AbstractLoadingProcess{

    public ProcessCalendar(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.calendar);
        initLevelSize(LevelSize.def);
    }


    @Override
    public void createBasicStuff() {

    }


    @Override
    public void createAdvancedStuff() {
        Random random = new Random(getSeed());
        Difficulty difficulty = getDifficulty(random);
        //
        LgParameters lgParameters = loadingParameters.lgParameters;
        (new LevelGenerator(getObjectsLayer())).apply(lgParameters, random);
        getObjectsLayer().move();
    }


    private Difficulty getDifficulty(Random random) {
        if (random.nextDouble() < 0.1) return Difficulty.hard;
        return Difficulty.expert;
    }


    private long getSeed() {
        int year = loadingParameters.year;
        int month = loadingParameters.month;
        int day = loadingParameters.day;
        return year + month * 1000 + day * 100000;
    }
}
