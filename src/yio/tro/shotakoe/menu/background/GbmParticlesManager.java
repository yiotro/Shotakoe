package yio.tro.shotakoe.menu.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.RefreshRateDetector;
import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.stuff.AtlasLoader;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.RepeatYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GbmParticlesManager {


    GeneralBackgroundManager generalBackgroundManager;
    YioGdxGame yioGdxGame;
    ArrayList<GbmParticle> particles;
    RepeatYio<GbmParticlesManager> repeatRemove;
    RepeatYio<GbmParticlesManager> repeatSpawnParticles;
    RepeatYio<GbmParticlesManager> repeatSpawnBubbles;
    HashMap<GbmColor, TextureRegion> mapTextures;
    ObjectPoolYio<GbmParticle> poolParticles;
    TextureRegion blackCircleTexture;
    ArrayList<GbmBubble> bubbles;
    ObjectPoolYio<GbmBubble> poolBubbles;
    ArrayList<GbmDelayedJump> delayedJumps;
    ObjectPoolYio<GbmDelayedJump> poolDelayedJumps;


    public GbmParticlesManager(GeneralBackgroundManager generalBackgroundManager) {
        this.generalBackgroundManager = generalBackgroundManager;
        yioGdxGame = generalBackgroundManager.yioGdxGame;
        particles = new ArrayList<>();
        bubbles = new ArrayList<>();
        delayedJumps = new ArrayList<>();
        initRepeats();
        initPools();
        performInitialSimulation();
    }


    private void initPools() {
        poolParticles = new ObjectPoolYio<GbmParticle>(particles) {
            @Override
            public GbmParticle makeNewObject() {
                return new GbmParticle(GbmParticlesManager.this);
            }
        };
        poolBubbles = new ObjectPoolYio<GbmBubble>(bubbles) {
            @Override
            public GbmBubble makeNewObject() {
                return new GbmBubble(GbmParticlesManager.this);
            }
        };
        poolDelayedJumps = new ObjectPoolYio<GbmDelayedJump>(delayedJumps) {
            @Override
            public GbmDelayedJump makeNewObject() {
                return new GbmDelayedJump();
            }
        };
    }


    private void performInitialSimulation() {
        int n = (int) (500 / RefreshRateDetector.getInstance().multiplier);
        for (int i = 0; i < n; i++) {
            move();
        }
    }


    void loadTextures() {
        AtlasLoader atlasBgm = new AtlasLoader("menu/bgm_particles/", true);
        mapTextures = new HashMap<>();
        for (GbmColor color : GbmColor.values()) {
            mapTextures.put(color, atlasBgm.getTexture(color + ".png"));
        }
        blackCircleTexture = atlasBgm.getTexture("black_circle.png");
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<GbmParticlesManager>(this, 120) {
            @Override
            public void performAction() {
                parent.removeParticles();
                parent.removeBubbles();
                parent.removeJumps();
            }
        };
        repeatSpawnParticles = new RepeatYio<GbmParticlesManager>(this, 60) {
            @Override
            public void performAction() {
                parent.spawnParticle();
            }
        };
        repeatSpawnBubbles = new RepeatYio<GbmParticlesManager>(this, 15) {
            @Override
            public void performAction() {
                parent.spawnBubble();
            }
        };
    }


    void spawnBubble() {
        GbmBubble freshObject = poolBubbles.getFreshObject();
        Random random = YioGdxGame.random;
        freshObject.position.center.x = random.nextFloat() * GraphicsYio.width;
        freshObject.position.center.y = random.nextFloat() * GraphicsYio.height;
        freshObject.color = GbmColor.values()[random.nextInt(GbmColor.values().length)];
        freshObject.viewPosition.setBy(freshObject.position);
        freshObject.position.radius = 0.02f * GraphicsYio.width;
        freshObject.updateStart();
        freshObject.distance = (1 + 2f * random.nextFloat()) * 0.03f * GraphicsYio.height;
    }


    void spawnParticle() {
        GbmParticle freshObject = poolParticles.getFreshObject();
        Random random = YioGdxGame.random;
        freshObject.position.center.x = random.nextFloat() * GraphicsYio.width;
        freshObject.position.center.y = random.nextFloat() * GraphicsYio.height;
        freshObject.position.radius = (1 + 0.5f * random.nextFloat()) * 0.02f * GraphicsYio.height;
        freshObject.color = GbmColor.values()[random.nextInt(GbmColor.values().length)];
        freshObject.lifeFactor.appear(MovementType.simple, 0.07);
        freshObject.updateStart();
        freshObject.distance = (1 + 2f * random.nextFloat()) * 0.1f * GraphicsYio.height;
    }


    void move() {
        if (!generalBackgroundManager.isCurrentlyVisible()) return;
        repeatRemove.move();
//        repeatSpawnParticles.move();
        repeatSpawnBubbles.move();
        moveParticles();
        moveBubbles();
        moveJumps();
    }


    private void moveJumps() {
        for (GbmDelayedJump gbmDelayedJump : delayedJumps) {
            gbmDelayedJump.move();
        }
    }


    private void moveBubbles() {
        for (GbmBubble gbmBubble : bubbles) {
            gbmBubble.move();
        }
    }


    public void applyJumps(PointYio touchPoint) {
        for (GbmBubble gbmBubble : bubbles) {
            if (!gbmBubble.isCurrentlyVisible()) continue;
            GbmDelayedJump freshObject = poolDelayedJumps.getFreshObject();
            freshObject.bubble = gbmBubble;
            freshObject.touchPoint.setBy(touchPoint);
            float distanceValue = gbmBubble.position.center.distanceTo(touchPoint) / GraphicsYio.width;
            long deltaTime = (long) (distanceValue * 500);
            freshObject.targetTime = System.currentTimeMillis() + deltaTime;
        }
    }


    void removeJumps() {
        for (int i = delayedJumps.size() - 1; i >= 0; i--) {
            GbmDelayedJump gbmDelayedJump = delayedJumps.get(i);
            if (!gbmDelayedJump.isReadyToBeRemoved()) continue;
            poolDelayedJumps.removeFromExternalList(gbmDelayedJump);
        }
    }


    void removeBubbles() {
        for (int i = bubbles.size() - 1; i >= 0; i--) {
            GbmBubble gbmBubble = bubbles.get(i);
            if (!gbmBubble.isReadyToBeRemoved()) continue;
            poolBubbles.removeFromExternalList(gbmBubble);
        }
    }


    void removeParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            GbmParticle gbmParticle = particles.get(i);
            if (gbmParticle.isCurrentlyVisible()) continue;
            if (!gbmParticle.appearFactor.isInDestroyState()) continue;
            poolParticles.removeFromExternalList(gbmParticle);
        }
    }


    private void moveParticles() {
        for (GbmParticle gbmParticle : particles) {
            gbmParticle.move();
        }
    }


    void render(SpriteBatch batch) {
//        for (GbmParticle gbmParticle : particles) {
//            if (!gbmParticle.isCurrentlyVisible()) continue;
//            GraphicsYio.setBatchAlpha(batch, 0.33f * gbmParticle.appearFactor.getValue());
//            GraphicsYio.drawByCircle(batch, mapTextures.get(gbmParticle.color), gbmParticle.position);
//        }
        for (GbmBubble gbmBubble : bubbles) {
            if (!gbmBubble.isCurrentlyVisible()) continue;
            GraphicsYio.setBatchAlpha(batch, gbmBubble.getAlpha());
            GraphicsYio.drawByCircle(batch, blackCircleTexture, gbmBubble.viewPosition);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
