package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.menu.scenes.gameplay.FsItem;

import java.util.Random;

public class LpWorker {

    Random random;
    private LoadingParameters loadingParameters;
    private LgParameters lgParameters;


    public LpWorker(Random random) {
        this.random = random;
    }


    public LoadingParameters apply() {
        int rulesIndex = random.nextInt(10);
        if (random.nextFloat() < 0.2f) {
            rulesIndex = 0;
        }
        if (rulesIndex == 1 && random.nextFloat() < 0.66f) {
            rulesIndex = random.nextInt(10);
        }
        return apply(rulesIndex, -1);
    }


    public LoadingParameters apply(int rulesIndex, int levelIndex) {
        loadingParameters = new LoadingParameters();
        lgParameters = loadingParameters.lgParameters;
        loadingParameters.setLevelSize(LevelSize.def);
        decideDifficulty(levelIndex);
        decideSize(rulesIndex, levelIndex);
        decideRules(rulesIndex);
        return loadingParameters;
    }


    private void decideDifficulty(int levelIndex) {
        if (levelIndex < 10) {
            loadingParameters.setDifficulty(Difficulty.normal);
            return;
        }
        if (levelIndex < 15) {
            loadingParameters.setDifficulty(Difficulty.hard);
            return;
        }
        loadingParameters.setDifficulty(Difficulty.expert);
    }


    private void decideRules(int rulesIndex) {
        int targetSize;
        switch (rulesIndex) {
            default:
                System.out.println("LpWorker.decideRules: problem, rulesIndex = " + rulesIndex);
                break;
            case 0:
            case 3:
                lgParameters.setEnemy(true);
                lgParameters.setSymmetry(random.nextBoolean());
                loadingParameters.setFirstTurn(getFirstTurn());
                break;
            case 1:
                lgParameters.setEnemy(true);
                lgParameters.setBomb(true);
                loadingParameters.setFirstTurn(getFirstTurn());
                lgParameters.setSymmetry(random.nextBoolean());
                limitHoles(0.97);
                break;
            case 2:
                lgParameters.setEnemy(true);
                loadingParameters.setTargetSize(getTargetSize(0.3));
                lgParameters.setSymmetry(random.nextBoolean());
                loadingParameters.setFirstTurn(getFirstTurn());
                break;
            case 4:
                lgParameters.setEnemy(true);
                loadingParameters.setTurnLimit(getTurnLimit(5));
                loadingParameters.setFirstTurn(getFirstTurn());
                lgParameters.setSymmetry(random.nextBoolean());
                break;
            case 5:
                lgParameters.setEnemy(true);
                lgParameters.setSameStartingCluster(true);
                lgParameters.setBomb(true);
                loadingParameters.setFirstTurn(getFirstTurn());
                break;
            case 6:
                lgParameters.setEnemy(true);
                lgParameters.setSameStartingCluster(true);
                loadingParameters.setTargetSize(getTargetSize(0.5));
                loadingParameters.setFirstTurn(getFirstTurn());
                break;
            case 7:
                lgParameters.setEnemy(false);
                lgParameters.setBomb(true);
                targetSize = getTargetSize(0.4);
                loadingParameters.setTargetSize(targetSize);
                loadingParameters.setTurnLimit(getTurnLimit((int) (Math.sqrt(targetSize) * 1.25f)));
                break;
            case 8:
                lgParameters.setEnemy(false);
                lgParameters.setBomb(true);
                loadingParameters.setTargetSize(getTargetSize(0.5));
                break;
            case 9:
                lgParameters.setEnemy(false);
                targetSize = getTargetSize(0.5);
                loadingParameters.setTargetSize(targetSize);
                loadingParameters.setTurnLimit(getTurnLimit((int) (Math.sqrt(targetSize) * 1.02f)));
                limitHoles(0.97);
                break;
        }
    }


    private void limitHoles(double minValue) {
        loadingParameters.lgParameters.activeCellsValue = Math.max(minValue, loadingParameters.lgParameters.activeCellsValue);
    }


    private TurnType getFirstTurn() {
        if (random.nextBoolean()) return TurnType.human;
        return TurnType.ai;
    }


    private int getTurnLimit(int min) {
        return min + random.nextInt(5);
    }


    private int getTargetSize(double ratio) {
        int width = loadingParameters.fieldWidth;
        int height = loadingParameters.fieldHeight;
        return (int) (ratio * width * height + random.nextInt(10));
    }


    private void decideSize(int rulesIndex, int sizeLimit) {
        FsItem[] fsItems = new FsItem[]{
                new FsItem(17, 21),
                new FsItem(19, 21),
                new FsItem(19, 23),
                new FsItem(23, 27),
                new FsItem(25, 30),
                new FsItem(27, 31),
                new FsItem(29, 35),
                new FsItem(33, 40),
                new FsItem(43, 50),
                new FsItem(45, 55),
        };
        int limit = fsItems.length;
        if (sizeLimit > 0) {
            limit = Math.min(limit, sizeLimit);
        }
        if (rulesIndex == 1 || rulesIndex == 5) {
            limit = Math.min(limit, fsItems.length - 3);
        }
        FsItem item = fsItems[random.nextInt(limit)];
        loadingParameters.setFieldWidth(item.width);
        loadingParameters.setFieldHeight(item.height);
    }
}
