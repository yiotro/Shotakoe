package yio.tro.shotakoe.stuff.factor_yio;

import java.util.HashMap;

public class MbFactoryYio {

    private static MbFactoryYio instance;
    HashMap<MovementType, AbstractMoveBehavior> mapBehaviors;


    public MbFactoryYio() {
        initMap();
    }


    public void initMap() {
        mapBehaviors = new HashMap<>();
        mapBehaviors.put(MovementType.inertia, new MbInertia());
        mapBehaviors.put(MovementType.stay, new MbStay());
        mapBehaviors.put(MovementType.simple, new MbSimple());
        mapBehaviors.put(MovementType.material, new MbMaterial());
        mapBehaviors.put(MovementType.lighty, new MbLighty());
        mapBehaviors.put(MovementType.approach, new MbApproach());
        mapBehaviors.put(MovementType.rubber_band, new MbRubberBand());
        mapBehaviors.put(MovementType.big_rubber_band, new MbBigRubberBand());
        mapBehaviors.put(MovementType.soft_rubber_band, new MbSoftRubberBand());
        mapBehaviors.put(MovementType.soft_big_rubber_band, new MbSoftBigRubberBand());
    }


    public static void initialize() {
        instance = null;
    }


    public static MbFactoryYio getInstance() {
        if (instance == null) {
            instance = new MbFactoryYio();
        }
        return instance;
    }


    public AbstractMoveBehavior getBehavior(MovementType movementType) {
        return mapBehaviors.get(movementType);
    }
}
