package yio.tro.shotakoe.game.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.shotakoe.PaidStatus;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.elements.customizable_list.CciType;
import yio.tro.shotakoe.menu.scenes.Scenes;

import java.util.ArrayList;
import java.util.Random;

public class CampaignManager {


    private static CampaignManager instance;
    public static final int LAST_LEVEL_INDEX = 1517;
    private static final String PREFS = "yio.tro.shotakoe.campaign";
    public ArrayList<Integer> completedLevels;


    public CampaignManager() {
        completedLevels = new ArrayList<>();
    }


    public static void initialize() {
        instance = null;
        getInstance(); // load
    }


    public static CampaignManager getInstance() {
        if (instance == null) {
            instance = new CampaignManager();
            instance.loadValues();
        }

        return instance;
    }


    public void loadValues() {
        Preferences preferences = getPreferences();
        String source = preferences.getString("progress");
        String[] split = source.split(" ");
        completedLevels.clear();
        for (String token : split) {
            if (token.length() < 1) continue;
            completedLevels.add(Integer.valueOf(token));
        }
    }


    public void onLevelCompleted(int index) {
        if (isLevelCompleted(index)) return;
        completedLevels.add(index);
        Scenes.campaign.onLevelMarkedAsCompleted();
        saveValues();
    }


    public int getNextLevelIndex(int index) {
        if (index >= getLastLevelIndex()) return index;
        int nextIndex = index + 1;
        if (DebugFlags.nextIncrementalMode) return nextIndex;
        while (nextIndex != index && isLevelCompleted(nextIndex)) {
            nextIndex++;
            if (nextIndex > getLastLevelIndex()) {
                nextIndex = 0;
            }
        }
        return nextIndex;
    }


    public boolean isLevelCompleted(int index) {
        if (index == -1) return true;

        for (int completedLevel : completedLevels) {
            if (completedLevel == index) return true;
        }
        return false;
    }


    public boolean areAllLevelsCompleted() {
        return completedLevels.size() > getLastLevelIndex();
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        StringBuilder builder = new StringBuilder();
        for (Integer completedLevel : completedLevels) {
            builder.append(completedLevel).append(" ");
        }
        preferences.putString("progress", builder.toString());
        preferences.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public int getIndexOfFirstUnlockedLevel() {
        for (int index = 0; index < getLastLevelIndex(); index++) {
            if (getLevelType(index) != CciType.unlocked) continue;
            return index;
        }
        return 0;
    }


    public int getIndexOfHighestUnlockedLevel() {
        int result = 0;
        for (int index = 0; index < getLastLevelIndex(); index++) {
            if (getLevelType(index) != CciType.unlocked) continue;
            result = index;
        }
        return result;
    }


    public static int getSeed(int index) {
        return 173373 + 239 * index;
    }


    public void launchCampaignLevel(YioGdxGame yioGdxGame, int index) {
        Random random = new Random(getSeed(index));
        LpWorker lpWorker = new LpWorker(random);
        LoadingParameters loadingParameters;
        if (index < 10) {
            loadingParameters = lpWorker.apply(index, index);
        } else {
            loadingParameters = lpWorker.apply();
        }
        loadingParameters.levelIndex = index;
        loadingParameters.firstTurn = TurnType.human;
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign, loadingParameters);
    }


    public static int getLastLevelIndex() {
        if (PaidStatus.PRO_VERSION) return LAST_LEVEL_INDEX;
        return 101; // free version
    }


    public CciType getLevelType(int levelIndex) {
        if (isLevelCompleted(levelIndex)) {
            return CciType.completed;
        }
        if (PaidStatus.PRO_VERSION && levelIndex < 100 && levelIndex % 12 == 0) return CciType.unlocked;
        if (isLevelCompleted(levelIndex - 1) || isLevelCompleted(levelIndex - 2) || isLevelCompleted(levelIndex - 3)) {
            return CciType.unlocked;
        }
        return CciType.unknown;
    }

}
