package yio.tro.shotakoe.game.loading;

import yio.tro.shotakoe.game.general.Difficulty;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.general.TurnType;
import yio.tro.shotakoe.game.generator.LgParameters;

public class LoadingParameters {

    public LevelSize levelSize;
    public Difficulty difficulty;
    public long seed;
    public int year;
    public int month;
    public int day;
    public int levelIndex;
    public int fieldWidth;
    public int fieldHeight;
    public LgParameters lgParameters;
    public int targetSize;
    public int turnLimit;
    public TurnType firstTurn;


    public LoadingParameters() {
        levelSize = null;
        difficulty = null;
        seed = 0;
        year = -1;
        month = -1;
        day = -1;
        levelIndex = -1;
        fieldWidth = -1;
        fieldHeight = -1;
        lgParameters = new LgParameters();
        targetSize = -1;
        turnLimit = -1;
        firstTurn = null;
    }


    public void setBy(LoadingParameters src) {
        levelSize = src.levelSize;
        difficulty = src.difficulty;
        seed = src.seed;
        year = src.year;
        month = src.month;
        day = src.day;
        levelIndex = src.levelIndex;
        fieldWidth = src.fieldWidth;
        fieldHeight = src.fieldHeight;
        lgParameters.setBy(src.lgParameters);
        targetSize = src.targetSize;
        turnLimit = src.turnLimit;
        firstTurn = src.firstTurn;
    }


    public void setLevelSize(LevelSize levelSize) {
        this.levelSize = levelSize;
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    public void setSeed(long seed) {
        this.seed = seed;
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


    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }


    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }


    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }


    public void setTurnLimit(int turnLimit) {
        this.turnLimit = turnLimit;
    }


    public void setFirstTurn(TurnType firstTurn) {
        this.firstTurn = firstTurn;
    }
}
