package yio.tro.shotakoe.game.export;

import yio.tro.shotakoe.game.general.CameraController;
import yio.tro.shotakoe.game.general.ObjectsLayer;

public class ExportParameters {

    CameraController cameraController;
    ObjectsLayer objectsLayer;


    public ExportParameters() {
        cameraController = null;
        objectsLayer = null;
    }


    public void setCameraController(CameraController cameraController) {
        this.cameraController = cameraController;
    }


    public void setObjectsLayer(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
    }
}
