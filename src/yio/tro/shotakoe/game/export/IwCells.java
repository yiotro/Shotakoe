package yio.tro.shotakoe.game.export;

import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.ObjectsLayer;

public class IwCells extends AbstractImportWorker {

    public IwCells(ObjectsLayer objectsLayer) {
        super(objectsLayer);
    }


    @Override
    protected String getDefaultSectionName() {
        return "cells";
    }


    @Override
    protected void apply() {
        CellField cellField = objectsLayer.cellField;
        cellField.deactivateEverything();
        for (String token : source.split(",")) {
            if (token.length() < 5) continue;
            String[] split = token.split(" ");
            int x = Integer.valueOf(split[0]);
            int y = Integer.valueOf(split[1]);
            Cell cell = cellField.array[x][y];
            cell.active = true;
            if (!split[2].equals("null")) {
                ColorYio color = ColorYio.valueOf(split[2]);
                cell.setColor(color);
            }
        }
        cellField.updateActiveCells();
        objectsLayer.figuresManager.onCellsImported();
    }
}
