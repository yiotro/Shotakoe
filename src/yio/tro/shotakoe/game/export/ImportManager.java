package yio.tro.shotakoe.game.export;

import yio.tro.shotakoe.game.general.ObjectsLayer;

public class ImportManager {

    ObjectsLayer objectsLayer;


    public ImportManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
    }


    public void apply(String levelCode) {
        (new IwCells(objectsLayer)).perform(levelCode);
        objectsLayer.simulationCacheManager.update();
    }
}
