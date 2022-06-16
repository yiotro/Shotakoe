package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.Random;

public class ParticlesManager {

    ObjectsLayer objectsLayer;
    public ArrayList<Particle> particles;
    ObjectPoolYio<Particle> poolParticles;
    RepeatYio<ParticlesManager> repeatRemoveDead;
    CircleYio tStart;
    CircleYio tFinish;


    public ParticlesManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        particles = new ArrayList<>();
        tStart = new CircleYio();
        tFinish = new CircleYio();
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDead = new RepeatYio<ParticlesManager>(this, 120) {
            @Override
            public void performAction() {
                parent.checkToRemoveParticles();
            }
        };
    }


    private void initPools() {
        poolParticles = new ObjectPoolYio<Particle>(particles) {
            @Override
            public Particle makeNewObject() {
                return new Particle(ParticlesManager.this);
            }
        };
    }


    public void move() {
        moveParticles();
        repeatRemoveDead.move();
    }


    public void spawnExplosion(PointYio center) {
        float r = 0.03f * GraphicsYio.width;
        for (int i = 0; i < 200; i++) {
            tStart.center.setBy(center);
            tStart.setRadius((1 + 0.25 * getRandomTwoSideMultiplier()) * r);
            tFinish.setBy(tStart);
            double angle = Yio.getRandomAngle();
            tFinish.center.relocateRadial((1 + 0.5 * getRandom().nextDouble()) * 8 * tStart.radius, angle);
            tFinish.radius *= 0.6 - 0.2 * getRandom().nextDouble();
            Particle freshObject = poolParticles.getFreshObject();
            freshObject.startMoving(tStart, tFinish, 1.5 + 0.35 * getRandom().nextDouble());
            freshObject.setType(getTypeForSplash());
        }
        tStart.setRadius(0.3f * GraphicsYio.width);
        tFinish.setBy(tStart);
        tFinish.radius *= 1.15f;
        Particle giantCircle = poolParticles.getFreshObject();
        giantCircle.startMoving(tStart, tFinish, 1.2);
        giantCircle.setType(PaType.giant_circle);
    }


    public boolean isAtLeastOneParticleVisible() {
        for (Particle particle : particles) {
            if (particle.isVisible()) return true;
        }
        return false;
    }


    private PaType getTypeForSplash() {
        if (getRandom().nextDouble() < 0.25) return getRandomStrokeType();
        return PaType.circle_red;
    }


    private PaType getRandomStrokeType() {
        if (getRandom().nextDouble() < 0.25) return PaType.stroke_black;
        return PaType.stroke_red;
    }


    private float getRandomTwoSideMultiplier() {
        return 2 * getRandom().nextFloat() - 1;
    }


    private void checkToRemoveParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            if (!particle.isReadyToBeRemoved()) continue;
            poolParticles.removeFromExternalList(particle);
        }
    }


    private void moveParticles() {
        for (Particle particle : particles) {
            particle.move();
        }
    }


    private Random getRandom() {
        return YioGdxGame.random;
    }
}
