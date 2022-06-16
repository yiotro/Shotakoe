package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ExternalIndicatorsManager {

    public ObjectsLayer objectsLayer;
    public ArrayList<ExternalIndicator> indicators;
    ObjectPoolYio<ExternalIndicator> poolIndicators;


    public ExternalIndicatorsManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        indicators = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<ExternalIndicator>(indicators) {
            @Override
            public ExternalIndicator makeNewObject() {
                return new ExternalIndicator(ExternalIndicatorsManager.this);
            }
        };
    }


    public void applyUpdate() {
        poolIndicators.clearExternalList();
        tryToAddIndicator(IndicatorType.start);
        tryToAddIndicator(IndicatorType.enemy_start);
    }


    private void tryToAddIndicator(IndicatorType indicatorType) {
        CellType cellType = convertIndicatorTypeToCellType(indicatorType);
        Cell cell = objectsLayer.cellField.getCell(cellType);
        if (cell == null) return;
        ExternalIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.setValues(indicatorType, cell);
    }


    public ExternalIndicator getIndicator(CellType cellType) {
        return getIndicator(convertCellTypeToIndicatorType(cellType));
    }


    public ExternalIndicator getIndicator(IndicatorType type) {
        for (ExternalIndicator externalIndicator : indicators) {
            if (externalIndicator.type == type) return externalIndicator;
        }
        return null;
    }


    private IndicatorType convertCellTypeToIndicatorType(CellType cellType) {
        switch (cellType) {
            default:
                return null;
            case human_start:
                return IndicatorType.start;
            case enemy_start:
                return IndicatorType.enemy_start;
        }
    }


    private CellType convertIndicatorTypeToCellType(IndicatorType indicatorType) {
        switch (indicatorType) {
            default:
                return null;
            case start:
                return CellType.human_start;
            case enemy_start:
                return CellType.enemy_start;
        }
    }
}
