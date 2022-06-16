package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.VisualTextContainer;

public class TutorialHint {

    ObjectsLayer objectsLayer;
    public VisualTextContainer visualTextContainer;
    RectangleYio position;


    public TutorialHint(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        position = new RectangleYio();
        visualTextContainer = new VisualTextContainer();
    }


    public void apply(String key) {
        updatePosition();
        String string = LanguagesManager.getInstance().getString(key);
        visualTextContainer.setSize(position.width, position.height);
        visualTextContainer.applyManyTextLines(Fonts.miniFont, string);
        visualTextContainer.updateHeightToMatchText(0);
        visualTextContainer.move(position);
    }


    private void updatePosition() {
        float delta = 0.15f * GraphicsYio.width; // camera is slightly zoomed in
        position.set(delta, 0, GraphicsYio.width - 2 * delta, GraphicsYio.height);
        Cell topActiveCell = findTopActiveCell();
        if (topActiveCell == null) return;
        position.y = topActiveCell.position.center.y + 2 * topActiveCell.position.radius;
    }


    private Cell findTopActiveCell() {
        Cell topCell = null;
        for (Cell cell : objectsLayer.cellField.activeCells) {
            if (topCell == null || cell.y > topCell.y) {
                topCell = cell;
            }
        }
        return topCell;
    }
}
