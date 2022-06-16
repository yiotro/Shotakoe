package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.YioGdxGame;

public class TurnsManager {

    ObjectsLayer objectsLayer;
    TurnType turn;
    public int turnsCounter;


    public TurnsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        turn = null;
        turnsCounter = -1;
    }


    public void onTurnMade() {
        if (isEnemyPresentOnField()) {
            doSwitchTurn();
        }
        turnsCounter++;
    }


    public void checkToSwitchBack() {
        PossibleColorsManager possibleColorsManager = objectsLayer.possibleColorsManager;
        possibleColorsManager.update();
        if (possibleColorsManager.colors.size() > 0) return;

        if (objectsLayer.cellField.contains(CellType.bomb)) {
            objectsLayer.finishManager.winByStalemate = true;
            return;
        }

        // this is needed to avoid infinite recursion
        doSwitchTurn();
        possibleColorsManager.update();
        int predictedPossibleColors = possibleColorsManager.colors.size();
        doSwitchTurn();
        if (predictedPossibleColors == 0) return;

        objectsLayer.onTurnMade();
    }


    public void onAdvancedStuffCreated() {
        decideFirstTurn();
        initTurnsCounter();
    }


    private void initTurnsCounter() {
        if (isEnemyPresentOnField()) {
            turnsCounter = 2;
        } else {
            turnsCounter = 1;
        }
    }


    public int getLoopsCounter() {
        if (isEnemyPresentOnField()) {
            return turnsCounter / 2;
        }
        return turnsCounter;
    }


    private void decideFirstTurn() {
        TurnType firstTurnParameter = objectsLayer.getLoadingParameters().firstTurn;
        if (firstTurnParameter != null) {
            turn = firstTurnParameter;
            return;
        }
        turn = TurnType.human;
        if (isEnemyPresentOnField() && YioGdxGame.random.nextBoolean()) {
            doSwitchTurn();
        }
    }


    private boolean isEnemyPresentOnField() {
        return objectsLayer.getLoadingParameters().lgParameters.enemy;
    }


    private void doSwitchTurn() {
        if (turn == TurnType.human) {
            setTurn(TurnType.ai);
            return;
        }
        setTurn(TurnType.human);
    }


    public TurnType getNextTurnType() {
        if (!isEnemyPresentOnField()) return turn;
        if (turn == TurnType.human) return TurnType.ai;
        return TurnType.human;
    }


    public boolean isHumanTurn() {
        return turn == TurnType.human;
    }


    public boolean isAiTurn() {
        return turn == TurnType.ai;
    }


    public void setTurn(TurnType turn) {
        this.turn = turn;
    }


    public CellType convertToCellType(TurnType turnType) {
        switch (turnType) {
            default:
                return null;
            case human:
                return CellType.human_start;
            case ai:
                return CellType.enemy_start;
        }
    }


    public CellType getStartType() {
        switch (turn) {
            default:
                return null;
            case human:
                return CellType.human_start;
            case ai:
                return CellType.enemy_start;
        }
    }
}
