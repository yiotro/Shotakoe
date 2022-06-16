package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.game.general.Difficulty;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.generator.LevelGenerator;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.game.loading.LoadingManager;

import java.util.Random;

public class ProcessCampaign extends AbstractLoadingProcess{


    private Random random;


    public ProcessCampaign(LoadingManager loadingManager) {
        super(loadingManager);
    }


    @Override
    public void prepare() {
        initGameMode(GameMode.campaign);
        initLevelSize(LevelSize.def);
    }


    @Override
    public void createBasicStuff() {

    }


    @Override
    public void createAdvancedStuff() {
//        if ((new TutorialWorker(getObjectsLayer())).apply(loadingParameters.levelIndex)) return;
        random = new Random(getSeed());
        Difficulty difficulty = getDifficulty();
        //
        LgParameters lgParameters = loadingParameters.lgParameters;
        (new LevelGenerator(getObjectsLayer())).apply(lgParameters, random);
        getObjectsLayer().move();
    }


    private Difficulty getDifficulty() {
        int index = loadingParameters.levelIndex;
        if (index < 30) return getDifficulty(Difficulty.normal, Difficulty.hard, 0.33);
        if (index < 40) return getDifficulty(Difficulty.normal, Difficulty.hard, 0.66);
        if (index < 50) return Difficulty.hard;
        if (index < 60) return getDifficulty(Difficulty.hard, Difficulty.expert, 0.33);
        if (index < 70) return getDifficulty(Difficulty.hard, Difficulty.expert, 0.66);
        if (index < 100) return getDifficulty(Difficulty.expert, Difficulty.hard, 0.15);
        return getDifficulty(Difficulty.expert, Difficulty.hard, 0.1);
    }


    private Difficulty getDifficulty(Difficulty defaultDifficulty, Difficulty alternativeDifficulty, double chance) {
        if (random.nextDouble() < chance) return alternativeDifficulty;
        return defaultDifficulty;
    }


    private long getSeed() {
        return 2731237 + loadingParameters.levelIndex * 9182371;
    }
}
