package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class Particle implements ReusableYio {

    ParticlesManager particlesManager;
    public CircleYio position;
    CircleYio start;
    CircleYio finish;
    FactorYio roadFactor;
    FactorYio delayFactor;
    FactorYio alphaFactor;
    public ColorYio color;
    public PaType type;


    public Particle(ParticlesManager particlesManager) {
        this.particlesManager = particlesManager;
        position = new CircleYio();
        start = new CircleYio();
        finish = new CircleYio();
        roadFactor = new FactorYio();
        delayFactor = new FactorYio();
        alphaFactor = new FactorYio();
        reset();
    }


    @Override
    public void reset() {
        color = null;
        type = null;
        position.reset();
        start.reset();
        finish.reset();
        roadFactor.reset();
        delayFactor.reset();
        alphaFactor.reset();
        alphaFactor.appear(MovementType.approach, 7);
    }


    void move() {
        roadFactor.move();
        delayFactor.move();
        alphaFactor.move();
        updatePosition();
        checkToStartDelay();
        checkToFadeOut();
    }


    private void checkToStartDelay() {
        if (roadFactor.getValue() < 1) return;
        if (delayFactor.isInAppearState()) return;
        if (position.radius > 0.2f * GraphicsYio.width) {
            delayFactor.appear(MovementType.simple, 2.5);
        } else {
            delayFactor.appear(MovementType.simple, 0.8 + 10 * YioGdxGame.random.nextDouble());
        }
    }


    private void checkToFadeOut() {
        if (delayFactor.getValue() < 1) return;
        if (alphaFactor.isInDestroyState()) return;
        alphaFactor.destroy(MovementType.lighty, 2.5);
    }


    private void updatePosition() {
        position.setBy(start);
        position.center.x += roadFactor.getValue() * (finish.center.x - start.center.x);
        position.center.y += roadFactor.getValue() * (finish.center.y - start.center.y);
        position.radius += roadFactor.getValue() * (finish.radius - start.radius);
    }


    public float getAlpha() {
        return alphaFactor.getValue();
    }


    public boolean isVisible() {
        return alphaFactor.getValue() > 0;
    }


    boolean isReadyToBeRemoved() {
        return roadFactor.getValue() == 1 && alphaFactor.getValue() == 0;
    }


    public void startMoving(CircleYio start, CircleYio finish, double speedMultiplier) {
        this.start.setBy(start);
        this.finish.setBy(finish);
        roadFactor.reset();
        roadFactor.appear(MovementType.approach, 3 * speedMultiplier);
        updatePosition();
    }


    public void setColor(ColorYio color) {
        this.color = color;
    }


    public boolean isStroke(PaType type) {
        switch (type) {
            default:
                return false;
            case stroke_red:
            case stroke_black:
                return true;
        }
    }


    public void setType(PaType type) {
        // this method should be called after startMoving()
        this.type = type;
        if (isStroke(type)) {
            start.setAngle(start.center.angleTo(finish.center)); // yes, it's not a mistake
            start.radius *= 2;
            finish.radius *= 2;
        }
    }
}
