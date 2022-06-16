package yio.tro.shotakoe.game;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.export.ImportManager;
import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.game.generator.LevelGenerator;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.shotakoe.menu.scenes.Scenes;

import java.util.ArrayList;
import java.util.Random;

public class DebugActionsController {

    GameController gameController;
    private YioGdxGame yioGdxGame;
    private MenuControllerYio menuControllerYio;
    private ObjectsLayer objectsLayer;
    private CellField cellField;


    public DebugActionsController(GameController gameController) {
        this.gameController = gameController;
    }


    public void debugActions() {
        Scenes.myGames.create();
    }


    private void doCheckImport() {
        (new ImportManager(objectsLayer)).apply(getLevelCode());
    }


    private void doAddTutorialHint() {
        TutorialHint tutorialHint = new TutorialHint(objectsLayer);
        tutorialHint.apply("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor");
        objectsLayer.tutorialHints.add(tutorialHint);
    }


    private Cell getRandomActiveCell() {
        ArrayList<Cell> cells = objectsLayer.cellField.activeCells;
        return cells.get(YioGdxGame.random.nextInt(cells.size()));
    }


    private ColorYio getRandomColor() {
        ColorYio[] values = ColorYio.values();
        int index = YioGdxGame.random.nextInt(values.length);
        return values[index];
    }


    private void doShowCameraPositionInConsole() {
        System.out.println("DebugActionsController.doShowCameraPositionInConsole: " + gameController.cameraController);
    }


    private void doShowKeyboard() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("Write something");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                Scenes.notification.show("[" + input + "]");
            }
        });
    }


    private void doShowVisibleInterfaceElements() {
        menuControllerYio.showVisibleElementsInConsole();
    }


    private String getLevelCode() {
        return "shotakoe_code#cells:1 4 null,1 5 null,1 6 null,1 7 null,1 8 null,1 9 null,1 10 aqua,1 12 yellow,2 4 null,2 5 null,2 6 null,2 7 null,2 8 null,2 9 null,2 10 aqua,2 11 aqua,2 12 yellow,3 4 null,3 5 null,3 6 null,3 7 null,3 8 null,3 9 null,3 10 aqua,3 11 yellow,3 12 yellow,4 4 null,4 5 null,4 6 null,4 7 null,4 8 null,4 9 null,4 10 cyan,4 11 blue,4 12 blue,5 4 null,5 5 null,5 6 null,5 7 null,5 8 null,5 9 null,5 10 cyan,5 11 blue,5 12 blue,6 4 null,6 5 null,6 6 null,6 7 null,6 8 null,6 9 null,6 10 null,6 12 blue,7 4 null,7 5 null,7 6 null,7 7 null,7 8 null,7 9 null,7 10 null,7 11 aqua,7 12 aqua,8 4 null,8 5 null,8 6 null,8 7 null,8 8 null,8 9 brown,8 10 brown,8 11 brown,8 12 brown,#anchors:1 4 aqua corner3 0,2 6 brown corner3 0,1 9 red line2 0,2 10 aqua triangle4 0,3 11 yellow corner4b 0,4 4 cyan line3 0,3 8 green corner3 0,3 6 blue bomb5a 0,4 9 red corner3 0,4 7 yellow worm4a 0,4 10 cyan line2 0,5 11 blue bomb5a 0,6 4 brown corner3 0,5 8 aqua worm4b 0,6 6 aqua line2 0,7 8 blue corner3 0,8 4 cyan bomb5a 0,8 6 green triangle4 0,7 10 cyan line2 0,7 11 aqua line2 0,8 9 brown line4 0,#";
    }


    public void updateReferences() {
        yioGdxGame = gameController.yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        objectsLayer = gameController.objectsLayer;
        if (objectsLayer == null) return;
        cellField = objectsLayer.cellField;
    }
}
