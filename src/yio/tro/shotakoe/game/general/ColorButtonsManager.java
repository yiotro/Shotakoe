package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;

import java.util.ArrayList;

public class ColorButtonsManager {

    ObjectsLayer objectsLayer;
    public ArrayList<ColorButton> buttons;
    private float offset;
    ArrayList<ColorYio> buffer;
    ArrayList<ColorYio> possibleColors;


    public ColorButtonsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        buttons = new ArrayList<>();
        buffer = new ArrayList<>();
        possibleColors = new ArrayList<>();
    }


    void move() {
        moveButtons();
        checkToPerformAction();
    }


    private void checkToPerformAction() {
        if (buffer.size() == 0) return;
        if (!objectsLayer.isCapturingAllowed()) return;
        RecolorManager recolorManager = objectsLayer.recolorManager;
        if (recolorManager.isInProcessOfAnimation()) return;
        recolorManager.onColorChosen(buffer.get(0), CellType.human_start);
        buffer.remove(0);
    }


    public void onEndCreation() {
        buffer.clear();
        updateButtons();
        onTurnMade();
    }


    private void updateButtons() {
        buttons.clear();
        int quantity = ColorYio.values().length;
        offset = 0.02f * GraphicsYio.width;
        RectangleYio frame = objectsLayer.gameController.cameraController.frame;
        float size = (frame.width - (quantity + 1) * offset) / quantity;
        float x = frame.width - offset - size / 2;
        float fieldBottomY = objectsLayer.cellField.getFieldBottomY();
        float y = fieldBottomY - 1.5f * size;
        for (ColorYio colorYio : ColorYio.values()) {
            ColorButton colorButton = new ColorButton(this);
            colorButton.setColorYio(colorYio);
            colorButton.position.center.set(x, y);
            colorButton.position.setRadius(size / 2);
            colorButton.updateViewPosition();
            colorButton.touchPosition.setBy(colorButton.position);
            colorButton.touchPosition.radius += offset / 2;
            buttons.add(colorButton);
            x -= offset + size;
        }
    }


    public void onTouchDown() {
        ColorButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        currentlyTouchedButton.selectionEngineYio.applySelection();
    }


    public void onClick() {
        if (!objectsLayer.turnsManager.isHumanTurn()) return;
        ColorButton colorButton = getCurrentlyTouchedButton();
        if (colorButton == null) return;
        ColorYio colorYio = colorButton.colorYio;
        if (buffer.size() > 0 && buffer.get(buffer.size() - 1) == colorYio) return;
        if (!objectsLayer.finishManager.isCapturingAllowed()) return;
        colorButton.setJustPressedByHuman(true);
        buffer.add(colorYio);
        SoundManager.playSound(SoundType.attack);
    }


    public void updatePossibleColors() {
        objectsLayer.possibleColorsManager.update();
        possibleColors.clear();
        possibleColors.addAll(objectsLayer.possibleColorsManager.colors);
    }


    public void onTurnMade() {
        updatePossibleColors();
        updateAvailability();
    }


    private void updateAvailability() {
        for (ColorButton colorButton : buttons) {
            boolean targetAvailability = isColorAvailable(colorButton.colorYio);
            colorButton.setAvailability(targetAvailability);
        }
    }


    private boolean isColorAvailable(ColorYio colorYio) {
//        return objectsLayer.turnsManager.isHumanTurn() && possibleColors.contains(colorYio);
        return possibleColors.contains(colorYio);
    }


    private ColorButton getCurrentlyTouchedButton() {
        for (ColorButton colorButton : buttons) {
            if (!colorButton.isTouchedBy(objectsLayer.gameController.currentTouchConverted)) continue;
            return colorButton;
        }
        return null;
    }


    private void moveButtons() {
        for (ColorButton colorButton : buttons) {
            colorButton.move();
        }
    }
}
