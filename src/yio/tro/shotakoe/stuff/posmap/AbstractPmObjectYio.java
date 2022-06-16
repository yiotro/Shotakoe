package yio.tro.shotakoe.stuff.posmap;

import yio.tro.shotakoe.stuff.PointYio;

public abstract class AbstractPmObjectYio {

    protected PointYio posMapPosition;
    PmSectorIndex previousIndex, currentIndex;


    public AbstractPmObjectYio() {
        previousIndex = new PmSectorIndex();
        currentIndex = new PmSectorIndex();
        posMapPosition = new PointYio();
    }


    protected abstract void updatePosMapPosition();
}
