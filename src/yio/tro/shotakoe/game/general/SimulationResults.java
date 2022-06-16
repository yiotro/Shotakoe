package yio.tro.shotakoe.game.general;

public class SimulationResults {

    public GameMode gameMode;
    public int year;
    public int month;
    public int day;
    public int levelIndex;
    public boolean humanWon;
    public String levelName;
    public int humanCluster;
    public int enemyCluster;


    public SimulationResults() {
        gameMode = null;
        year = -1;
        month = -1;
        day = -1;
        levelIndex = -1;
        humanWon = false;
        levelName = "-";
        humanCluster = -1;
        enemyCluster = -1;
    }


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public void setMonth(int month) {
        this.month = month;
    }


    public void setDay(int day) {
        this.day = day;
    }


    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }


    public void setHumanWon(boolean humanWon) {
        this.humanWon = humanWon;
    }


    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }


    public void setHumanCluster(int humanCluster) {
        this.humanCluster = humanCluster;
    }


    public void setEnemyCluster(int enemyCluster) {
        this.enemyCluster = enemyCluster;
    }
}
