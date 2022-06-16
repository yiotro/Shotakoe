package yio.tro.shotakoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {

    private static SettingsManager instance;
    public boolean soundEnabled;
    public boolean fullScreenMode;
    public boolean simplifiedAnimation;
    public boolean roundedScreen;


    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void saveValues() {
        Preferences prefs = getPreferences();

        prefs.putBoolean("sound", soundEnabled);
        prefs.putBoolean("full_screen", fullScreenMode);
        prefs.putBoolean("simplified_animation", simplifiedAnimation);
        prefs.putBoolean("rounded_screen", roundedScreen);

        prefs.flush();
    }


    public void loadValues() {
        Preferences prefs = getPreferences();

        soundEnabled = prefs.getBoolean("sound", true);
        fullScreenMode = prefs.getBoolean("full_screen", true);
        simplifiedAnimation = prefs.getBoolean("simplified_animation", false);
        roundedScreen = prefs.getBoolean("rounded_screen", false);

        onValuesChanged();
    }


    public void resetValues() {
        Preferences preferences = getPreferences();
        preferences.clear();
        preferences.flush();
        loadValues();
    }


    public void onValuesChanged() {

    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("shotakoe.settings");
    }

}
