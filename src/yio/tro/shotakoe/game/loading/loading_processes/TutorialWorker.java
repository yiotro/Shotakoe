package yio.tro.shotakoe.game.loading.loading_processes;

import yio.tro.shotakoe.game.export.ImportManager;
import yio.tro.shotakoe.game.general.ObjectsLayer;
import yio.tro.shotakoe.game.general.TutorialHint;

public class TutorialWorker {

    ObjectsLayer objectsLayer;
    int[] hints;


    public TutorialWorker(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        hints = new int[]{0, 1, 2, 4, 6};
    }


    public boolean apply(int levelIndex) {
        String levelCode = getLevelCode(levelIndex);
        if (levelCode.length() < 3) return false;
        (new ImportManager(objectsLayer)).apply(levelCode);
        checkToAddHint(levelIndex);
        return true;
    }


    private void checkToAddHint(int levelIndex) {
        if (!hasHint(levelIndex)) return;
        TutorialHint tutorialHint = new TutorialHint(objectsLayer);
        tutorialHint.apply("t" + levelIndex);
        objectsLayer.tutorialHints.add(tutorialHint);
    }


    private String getLevelCode(int levelIndex) {
        switch (levelIndex) {
            default:
                return "-";
            case 0:
                return "shotakoe_code#cells:5 7 null,5 8 null,5 9 null,5 10 null,#anchors:5 7 red line4 0,#";
        }
    }


    private boolean hasHint(int levelIndex) {
        for (int hint : hints) {
            if (hint == levelIndex) return true;
        }
        return false;
    }
}
