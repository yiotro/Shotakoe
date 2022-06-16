package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.loading.LoadingManager;

public class ProcessEmpty extends AbstractLoadingProcess{

    public ProcessEmpty(LoadingManager loadingManager) {
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

    }
}
