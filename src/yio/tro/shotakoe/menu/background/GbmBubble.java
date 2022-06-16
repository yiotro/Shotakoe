package yio.tro.shotakoe.menu.background;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.JumpEngineYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class GbmBubble implements ReusableYio {

    GbmParticlesManager gbmParticlesManager;
    CircleYio position;
    CircleYio viewPosition;
    FactorYio appearFactor;
    FactorYio delayFactor;
    PointYio start;
    FactorYio explosionFactor;
    JumpEngineYio jumpEngineYio;
    double jAngle;
    private float maxJumpValue;
    GbmColor color;
    private double angle;
    double distance;
    FactorYio movementFactor;


    public GbmBubble(GbmParticlesManager gbmParticlesManager) {
        this.gbmParticlesManager = gbmParticlesManager;
        position = new CircleYio();
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        delayFactor = new FactorYio();
        explosionFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        maxJumpValue = 0.025f * GraphicsYio.width;
        start = new PointYio();
        movementFactor = new FactorYio();
    }


    @Override
    public void reset() {
        position.reset();
        viewPosition.reset();
        appearFactor.reset();
        delayFactor.reset();
        explosionFactor.reset();
        appearFactor.appear(MovementType.approach, 0.12);
        jumpEngineYio.reset();
        start.reset();
        angle = Yio.getRandomAngle();
        distance = 0;
        movementFactor.reset();
        movementFactor.appear(MovementType.inertia, 0.03);
    }


    void move() {
        appearFactor.move();
        delayFactor.move();
        explosionFactor.move();
        movementFactor.move();
        updatePosition();
        updateRadius();
        checkToLaunchDelayFactor();
        checkToLaunchExplosionFactor();
        moveJumpEngine();
    }


    private void updatePosition() {
        position.center.setBy(start);
        position.center.relocateRadial(movementFactor.getValue() * distance, angle);
    }


    private void moveJumpEngine() {
        jumpEngineYio.move();
        viewPosition.center.setBy(position.center);
        if (jumpEngineYio.getValue() == 0) return;
        viewPosition.center.relocateRadial(jumpEngineYio.getValue() * maxJumpValue, jAngle);
        viewPosition.radius *= 1 + 0.25 * jumpEngineYio.getValue();
    }


    void applyJump(double angle) {
        jAngle = angle;
        jumpEngineYio.apply(1, 1);
    }


    boolean isCurrentlyVisible() {
        return explosionFactor.getValue() < 1;
    }


    boolean isReadyToBeRemoved() {
        return explosionFactor.getProgress() == 1;
    }


    float getAlpha() {
        return 0.08f * appearFactor.getValue() * (1 - explosionFactor.getValue());
    }


    private void checkToLaunchExplosionFactor() {
        if (explosionFactor.isInAppearState()) return;
        if (delayFactor.getValue() < 1) return;
        explosionFactor.appear(MovementType.lighty, 0.7);
    }


    private void checkToLaunchDelayFactor() {
        if (delayFactor.isInAppearState()) return;
        if (appearFactor.getValue() < 1) return;
        delayFactor.appear(MovementType.simple, 0.2);
    }


    void updateStart() {
        start.setBy(position.center);
    }


    private void updateRadius() {
        viewPosition.setRadius(position.radius);
        if (appearFactor.getValue() < 1) {
            viewPosition.radius *= appearFactor.getValue();
        }
        if (explosionFactor.getValue() > 0) {
            viewPosition.radius *= 1 + 1 * explosionFactor.getValue();
        }
    }
}
