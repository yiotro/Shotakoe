package yio.tro.shotakoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class OneTimeInfo {

    private static OneTimeInfo instance;

    public boolean syncComplete;
    public boolean tutorial;
    public boolean myGames;


    public static OneTimeInfo getInstance() {
        if (instance == null) {
            instance = new OneTimeInfo();
            instance.load();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("shotakoe.oneTimeInfo");
    }


    public void load() {
        Preferences preferences = getPreferences();
        syncComplete = preferences.getBoolean("sync_complete", false);
        tutorial = preferences.getBoolean("tutorial", false);
        myGames = preferences.getBoolean("my_games", false);
    }


    public void save() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("sync_complete", syncComplete);
        preferences.putBoolean("tutorial", tutorial);
        preferences.putBoolean("my_games", myGames);
        preferences.flush();
    }
}
