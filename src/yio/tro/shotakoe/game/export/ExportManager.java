package yio.tro.shotakoe.game.export;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.shotakoe.game.general.ObjectsLayer;

public class ExportManager {


    // good symbols: ,<>:#/!@%&
    // bad symbols: ^?|\$*()
    private StringBuilder builder;
    private ExportParameters parameters;


    public ExportManager() {
        builder = new StringBuilder();
        parameters = null;
    }


    public String applyToClipboard(ExportParameters parameters) {
        String result = apply(parameters);
        Clipboard clipboard = Gdx.app.getClipboard();
        clipboard.setContents(result);
        return result;
    }


    public String apply(ExportParameters parameters) {
        this.parameters = parameters;
        builder.setLength(0);

        addTitle();
        saveCameraPosition();
        saveCells();

        builder.append("#");
        return builder.toString();
    }


    private void saveCells() {
        startSection("cells");
        builder.append(getObjectsLayer().cellField.encode());
    }


    private void addTitle() {
        builder.append("shotakoe_code");
    }


    private void saveCameraPosition() {
        if (parameters.cameraController == null) return;
        startSection("camera");
        builder.append(parameters.cameraController.encode());
    }


    private void startSection(String name) {
        builder.append("#").append(name).append(":");
    }


    private ObjectsLayer getObjectsLayer() {
        return parameters.objectsLayer;
    }

}
