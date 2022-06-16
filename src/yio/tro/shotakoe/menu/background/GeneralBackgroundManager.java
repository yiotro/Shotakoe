package yio.tro.shotakoe.menu.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

import java.util.HashMap;

public class GeneralBackgroundManager {

    private final float posSize;
    YioGdxGame yioGdxGame;
    BackgroundYio currentValue;
    BackgroundYio previousValue;
    public FactorYio transitionFactor;
    private SpriteBatch batch;
    HashMap<BackgroundYio, TextureRegion> mapTextures;
    boolean reverseMode;
    RectangleYio position;
    FactorYio wanderFactor;
    public GbmParticlesManager particlesManager;


    public GeneralBackgroundManager(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        currentValue = BackgroundYio.black;
        previousValue = BackgroundYio.black;
        transitionFactor = new FactorYio();
        position = new RectangleYio();
        posSize = Math.max(GraphicsYio.width, GraphicsYio.height);
        particlesManager = new GbmParticlesManager(this);
        wanderFactor = new FactorYio();
        loadTextures();
    }


    private void loadTextures() {
        mapTextures = new HashMap<>();
        for (BackgroundYio backgroundYio : BackgroundYio.values()) {
            TextureRegion textureRegion;
            try {
                textureRegion = GraphicsYio.loadTextureRegion("menu/background/" + backgroundYio + ".png", false);
            } catch (Exception e) {
                textureRegion = null;
            }
            mapTextures.put(backgroundYio, textureRegion);
        }
        particlesManager.loadTextures();
    }


    public void move() {
        if (yioGdxGame.gameView != null && yioGdxGame.gameView.coversAllScreen()) return;
        transitionFactor.move();
        particlesManager.move();
        moveWanderFactor();
    }


    private void moveWanderFactor() {
        wanderFactor.move();
        if (!wanderFactor.isInAppearState() && wanderFactor.getProgress() == 0) {
            wanderFactor.appear(MovementType.inertia, 0.03);
        }
        if (!wanderFactor.isInDestroyState() && wanderFactor.getProgress() == 1) {
            wanderFactor.destroy(MovementType.inertia, 0.03);
        }
    }


    private void drawBackground(BackgroundYio backgroundYio) {
        position.set(-wanderFactor.getValue() * Math.max(0, posSize - GraphicsYio.width), 0, posSize, posSize);
        GraphicsYio.drawByRectangle(batch, mapTextures.get(backgroundYio), position);
    }


    private void renderOnlyCurrentBackground() { // when transitionFactor.get() == 1
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(currentValue);
    }


    private void renderInTransitionState() {
        float f = transitionFactor.getValue();

        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(previousValue);

        batch.setColor(c.r, c.g, c.b, f);
        drawBackground(currentValue);
        batch.setColor(c.r, c.g, c.b, 1);
    }


    public void render() {
        if (!isCurrentlyVisible()) return;
        batch = yioGdxGame.batch;
        batch.begin();
        renderInternals();
        batch.end();
    }


    private void renderInternals() {
        if (!isCurrentlyVisible()) return;
        renderBackgrounds();
        particlesManager.render(batch);
    }


    boolean isCurrentlyVisible() {
        if (yioGdxGame.gameView == null) return true;
        if (yioGdxGame.gameView.coversAllScreen()) return false;
        if (Scenes.easterEgg.isCurrentlyVisible() && Scenes.easterEgg.easterEggElement.getFactor().getProgress() == 1) return false;
        return true;
    }


    private void renderBackgrounds() {
        if (transitionFactor.getProgress() == 1) {
            renderOnlyCurrentBackground();
            return;
        }
        renderInTransitionState();
    }


    public void enableReverseMode() {
        reverseMode = true;
    }


    public void applyBackground(BackgroundYio value) {
        if (currentValue == value) return;
        reverseMode = false;

        previousValue = currentValue;
        currentValue = value;

        transitionFactor.setValues(0.02, 0.01);
        transitionFactor.appear(MovementType.inertia, MenuParams.ANIM_SPEED - 0.05);
    }

}
