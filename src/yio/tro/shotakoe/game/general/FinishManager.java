package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.RepeatYio;
import yio.tro.shotakoe.stuff.calendar.CalendarManager;
import yio.tro.shotakoe.stuff.calendar.MliMonth;

import java.util.ArrayList;

public class FinishManager {

    ObjectsLayer objectsLayer;
    RepeatYio<FinishManager> repeatApply;
    public boolean levelCompleted;
    SimulationResults simulationResults;
    WaveWorker waveSameColor;
    private ArrayList<Cell> tempList;
    public int targetSize;
    private boolean targetSizeReached;
    public int turnLimit;
    private boolean turnLimitReached;
    boolean winByStalemate;


    public FinishManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        levelCompleted = false;
        simulationResults = null;
        tempList = new ArrayList<>();
        targetSize = -1;
        targetSizeReached = false;
        winByStalemate = false;
        initRepeats();
        initWaves();
    }


    private void initWaves() {
        waveSameColor = new WaveWorker(getCellField()) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    private void initRepeats() {
        repeatApply = new RepeatYio<FinishManager>(this, 5, 5) {
            @Override
            public void performAction() {
                parent.apply();
            }
        };
    }


    public void onAdvancedStuffCreated() {
        targetSize = objectsLayer.getLoadingParameters().targetSize;
        turnLimit = objectsLayer.getLoadingParameters().turnLimit;
    }


    void move() {
        repeatApply.move();
    }


    public boolean isCapturingAllowed() {
        if (targetSizeReached) return false;
        if (turnLimitReached) return false;
        return true;
    }


    public void onTurnMade() {
        if (turnLimit == -1) return;
        if (turnLimitReached) return;
        if (objectsLayer.turnsManager.getLoopsCounter() <= turnLimit) return;
        turnLimitReached = true;
    }


    void apply() {
        if (levelCompleted) return;
        if (objectsLayer.colorButtonsManager.buffer.size() > 0) return;
        if (objectsLayer.recolorManager.isInProcessOfAnimation()) return;
        checkForTargetSize();
        checkForNoPossibleMovesLeft();
        checkForExplosion();
        checkForTurnLimit();
        checkForStalemateWin();
    }


    private void checkForStalemateWin() {
        if (getYioGdxGame().gamePaused) return; // already finished
        if (!winByStalemate) return;
        generateSimulationResults();
        simulationResults.humanWon = true;
        doFinish();
    }


    private void checkForTurnLimit() {
        if (getYioGdxGame().gamePaused) return; // already finished
        if (turnLimit == -1) return;
        if (!turnLimitReached) return;
        generateSimulationResults();
        if (getCellField().contains(CellType.enemy_start)) {
            int humanCluster = calculateClusterSize(CellType.human_start);
            int enemyCluster = calculateClusterSize(CellType.enemy_start);
            simulationResults.setHumanWon(humanCluster >= enemyCluster);
        } else {
            simulationResults.setHumanWon(false);
        }
        doFinish();
    }


    private void checkForTargetSize() {
        if (getYioGdxGame().gamePaused) return; // already finished
        if (targetSize == -1) return;
        if (!targetSizeReached) return;
        generateSimulationResults();
        Cell startCell = getCellField().getCell(CellType.human_start);
        Cell enemyCell = getCellField().getCell(CellType.enemy_start);
        if (enemyCell != null && enemyCell.sameColor(startCell)) {
            TurnsManager turnsManager = objectsLayer.turnsManager;
            TurnType nextTurnType = turnsManager.getNextTurnType();
            simulationResults.setHumanWon(nextTurnType == TurnType.human);
        } else {
            simulationResults.setHumanWon(isTargetSizeReachedBy(startCell));
        }
        doFinish();
    }


    private boolean isTargetSizeReachedBy(Cell startCell) {
        waveSameColor.apply(startCell, tempList);
        return tempList.size() >= targetSize;
    }


    public void onColorChosen(Cell startCell) {
        if (targetSize == -1) return;
        if (!isTargetSizeReachedBy(startCell)) return;
        targetSizeReached = true;
    }


    private void checkForExplosion() {
        if (getYioGdxGame().gamePaused) return; // already finished
        BombsManager bombsManager = objectsLayer.bombsManager;
        if (!bombsManager.explosionHappened) return;
        if (objectsLayer.particlesManager.isAtLeastOneParticleVisible()) return;
        generateSimulationResults();
        simulationResults.setHumanWon(bombsManager.culprit != CellType.human_start);
        doFinish();
    }


    private void checkForNoPossibleMovesLeft() {
        if (getYioGdxGame().gamePaused) return; // already finished
        if (objectsLayer.possibleColorsManager.colors.size() > 0) return;
        generateSimulationResults();
        int humanCluster = calculateClusterSize(CellType.human_start);
        int enemyCluster = calculateClusterSize(CellType.enemy_start);
        simulationResults.setHumanWon(humanCluster >= enemyCluster);
        doFinish();
    }


    private void doFinish() {
        checkToNoteDownClusterSizes();
        checkToSaveProgress();
        getYioGdxGame().applyFullTransitionToUI();
        Scenes.simulationResults.setSimulationResults(simulationResults);
        Scenes.simulationResults.create();
    }


    private void checkToNoteDownClusterSizes() {
        Cell enemyCell = getCellField().getCell(CellType.enemy_start);
        if (enemyCell == null) return;
        Cell startCell = getCellField().getCell(CellType.human_start);
        if (enemyCell.sameColor(startCell)) return;
        simulationResults.humanCluster = calculateClusterSize(CellType.human_start);
        simulationResults.enemyCluster = calculateClusterSize(CellType.enemy_start);
    }


    private int calculateClusterSize(CellType cellType) {
        Cell cell = getCellField().getCell(cellType);
        if (cell == null) return 0;
        waveSameColor.apply(cell, tempList);
        return tempList.size();
    }


    private CellField getCellField() {
        return objectsLayer.cellField;
    }


    private String generateLevelName() {
        switch (simulationResults.gameMode) {
            default:
                return "";
            case campaign:
                return "" + simulationResults.levelIndex;
            case calendar:
                String monthNameKey = MliMonth.getMonthNameKey(simulationResults.month);
                String monthName = LanguagesManager.getInstance().getString(monthNameKey);
                return "'" + monthName + " " + simulationResults.day + "'";
        }
    }


    private void checkToSaveProgress() {
        if (!simulationResults.humanWon) return;
        switch (simulationResults.gameMode) {
            default:
                break;
            case calendar:
                CalendarManager.getInstance().onCalendarDayCompleted(simulationResults);
                break;
            case campaign:
                CampaignManager.getInstance().onLevelCompleted(simulationResults.levelIndex);
                break;
        }
    }


    private void generateSimulationResults() {
        simulationResults = new SimulationResults();
        simulationResults.setGameMode(objectsLayer.gameController.gameMode);
        LoadingParameters previousParameters = getYioGdxGame().loadingManager.previousParameters;
        switch (simulationResults.gameMode) {
            default:
                break;
            case calendar:
                simulationResults.setYear(previousParameters.year);
                simulationResults.setMonth(previousParameters.month);
                simulationResults.setDay(previousParameters.day);
                break;
            case campaign:
                simulationResults.setLevelIndex(previousParameters.levelIndex);
                break;
        }
        simulationResults.setLevelName(generateLevelName());
    }


    private YioGdxGame getYioGdxGame() {
        return objectsLayer.gameController.yioGdxGame;
    }


}
