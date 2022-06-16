package yio.tro.shotakoe.menu.background;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class GbmParticle implements ReusableYio {

    GbmParticlesManager gbmParticlesManager;
    public CircleYio position;
    FactorYio appearFactor;
    FactorYio lifeFactor;
    public GbmColor color;
    private PointYio start;
    private double angle;
    double distance;


    public GbmParticle(GbmParticlesManager gbmParticlesManager) {
        this.gbmParticlesManager = gbmParticlesManager;
        position = new CircleYio();
        appearFactor = new FactorYio();
        lifeFactor = new FactorYio();
        start = new PointYio();
        color = null;
    }


    @Override
    public void reset() {
        appearFactor.reset();
        launchFactor();
        position.reset();
        lifeFactor.reset();
        start.reset();
        angle = Yio.getRandomAngle();
        distance = 0;
    }


    boolean isCurrentlyVisible() {
        return appearFactor.getValue() > 0;
    }


    void move() {
        appearFactor.move();
        lifeFactor.move();
        updatePosition();
        checkToDie();
    }


    private void updatePosition() {
        position.center.setBy(start);
        position.center.relocateRadial(lifeFactor.getValue() * distance, angle);
    }


    void updateStart() {
        start.setBy(position.center);
    }


    private void checkToDie() {
        if (lifeFactor.getValue() < 1) return;
        if (appearFactor.isInDestroyState()) return;
        appearFactor.destroy(MovementType.lighty, 0.1);
    }


    void launchFactor() {
        appearFactor.setValue(0.01);
        appearFactor.appear(MovementType.approach, 0.2);
    }

}
