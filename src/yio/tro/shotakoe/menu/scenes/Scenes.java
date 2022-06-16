package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.menu.scenes.gameplay.*;
import yio.tro.shotakoe.menu.scenes.info.SceneEasterEgg;
import yio.tro.shotakoe.menu.scenes.info.SceneWhatToRead;
import yio.tro.shotakoe.menu.scenes.info.help.*;
import yio.tro.shotakoe.menu.scenes.info.SceneAboutGame;
import yio.tro.shotakoe.menu.scenes.info.SceneSpecialThanks;
import yio.tro.shotakoe.menu.scenes.options.SceneLanguages;
import yio.tro.shotakoe.menu.scenes.options.SceneSettings;

public class Scenes {

    public static SceneMainMenu mainMenu;
    public static SceneAboutGame aboutGame;
    public static SceneChooseGameMode chooseGameMode;
    public static SceneGameOverlay gameOverlay;
    public static SceneDefaultPauseMenu defaultPauseMenu;
    public static SceneNotification notification;
    public static SceneSettings settings;
    public static SceneHelpMenu helpMenu;
    public static SceneSpecialThanks specialThanks;
    public static SceneLanguages languages;
    public static SceneTestScreen testScreen;
    public static SceneDebugTests debugTests;
    public static SceneTestResults testResults;
    public static SceneSecretScreen secretScreen;
    public static SceneSetupSkirmish setupSkirmish;
    public static SceneKeyboard keyboard;
    public static SceneDebugPanel debugPanel;
    public static SceneMechanicsOverlay mechanicsOverlay;
    public static SceneForefinger forefinger;
    public static SceneExceptionReport exceptionReport;
    public static SceneConfirmRestart confirmRestart;
    public static SceneToast toast;
    public static SceneResearchFactorBehavior researchFactorBehavior;
    public static SceneSetupPlotParameters setupPlotParameters;
    public static SceneSpawnPlotsByMovementType spawnPlotsByMovementType;
    public static SceneSpawnPlotsBySpeed spawnPlotsBySpeed;
    public static SceneEditPanel editPanel;
    public static SceneSetupSimpleGeneration setupSimpleGeneration;
    public static SceneSimulationResults simulationResults;
    public static SceneCalendar calendar;
    public static SceneCampaign campaign;
    public static SceneAboutProVersion aboutProVersion;
    public static SceneEasterEgg easterEgg;
    public static SceneMyGames myGames;
    public static SceneWhatToRead whatToRead;


    public static void createAllScenes() {
        mainMenu = new SceneMainMenu();
        aboutGame = new SceneAboutGame();
        chooseGameMode = new SceneChooseGameMode();
        gameOverlay = new SceneGameOverlay();
        defaultPauseMenu = new SceneDefaultPauseMenu();
        notification = new SceneNotification();
        settings = new SceneSettings();
        helpMenu = new SceneHelpMenu();
        specialThanks = new SceneSpecialThanks();
        languages = new SceneLanguages();
        testScreen = new SceneTestScreen();
        debugTests = new SceneDebugTests();
        testResults = new SceneTestResults();
        secretScreen = new SceneSecretScreen();
        setupSkirmish = new SceneSetupSkirmish();
        keyboard = new SceneKeyboard();
        debugPanel = new SceneDebugPanel();
        mechanicsOverlay = new SceneMechanicsOverlay();
        forefinger = new SceneForefinger();
        exceptionReport = new SceneExceptionReport();
        confirmRestart = new SceneConfirmRestart();
        toast = new SceneToast();
        researchFactorBehavior = new SceneResearchFactorBehavior();
        setupPlotParameters = new SceneSetupPlotParameters();
        spawnPlotsByMovementType = new SceneSpawnPlotsByMovementType();
        spawnPlotsBySpeed = new SceneSpawnPlotsBySpeed();
        editPanel = new SceneEditPanel();
        setupSimpleGeneration = new SceneSetupSimpleGeneration();
        simulationResults = new SceneSimulationResults();
        calendar = new SceneCalendar();
        campaign = new SceneCampaign();
        aboutProVersion = new SceneAboutProVersion();
        easterEgg = new SceneEasterEgg();
        myGames = new SceneMyGames();
        whatToRead = new SceneWhatToRead();
    }
}
