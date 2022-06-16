package yio.tro.shotakoe.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.game.view.game_renders.GameRender;
import yio.tro.shotakoe.game.view.game_renders.GameRendersList;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.*;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class GameView {

    YioGdxGame yioGdxGame;
    public GameController gameController;
    public int w, h;
    public FactorYio appearFactor;
    FrameBuffer frameBuffer;
    public SpriteBatch batchMovable, batchSolid;
    public OrthographicCamera orthoCam;
    TextureRegion transitionFrameTexture;
    public TextureRegion voidTexture;
    public TextureRegion blackPixel;
    double zoomLevelOne, zoomLevelTwo;
    public int currentZoomQuality;
    public RectangleYio transitionFramePosition;
    private RectangleYio screenPosition;
    public TextureRegion darkPixel;
    private PointYio animationPoint;
    public AtlasLoader roughAtlas;
    public AtlasLoader smoothAtlas;


    public GameView(YioGdxGame yioGdxGame) {
        // must be created after GameController and MenuViewYio
        this.yioGdxGame = yioGdxGame;
        gameController = yioGdxGame.gameController;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        appearFactor = new FactorYio();
        frameBuffer = FrameBufferYio.getInstance(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Texture texture = frameBuffer.getColorBufferTexture();
        transitionFrameTexture = new TextureRegion(texture);
        transitionFrameTexture.flip(false, true);
        batchMovable = new SpriteBatch();
        batchSolid = yioGdxGame.batch;
        createOrthoCam();
        loadTextures();
        GameRendersList.getInstance().updateGameRenders(this);
        transitionFramePosition = new RectangleYio();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        animationPoint = new PointYio();
    }


    public void createOrthoCam() {
        orthoCam = new OrthographicCamera(yioGdxGame.w, yioGdxGame.h);
        orthoCam.position.set(orthoCam.viewportWidth / 2f, orthoCam.viewportHeight / 2f, 0);
        updateCam();
    }


    void loadTextures() {
        roughAtlas = new AtlasLoader("game/atlas/", false);
        smoothAtlas = new AtlasLoader("game/atlas/", true);
        voidTexture = GraphicsYio.loadTextureRegion("game/background/main.png", false);
        darkPixel = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
    }


    public void updateCam() {
        orthoCam.update();
        batchMovable.setProjectionMatrix(orthoCam.combined);
    }


    public void defaultValues() {
        currentZoomQuality = 0;
    }


    public void appear() {
        appearFactor.setValues(0.02, 0);
        appearFactor.appear(MovementType.approach, 1.15 * MenuParams.ANIM_SPEED);
        onAppear();
    }


    private void onAppear() {
        updateAnimationPoint();
        updateAnimationTexture();
        prepareScenes();
    }


    private void updateAnimationPoint() {
        animationPoint.setBy(yioGdxGame.menuControllerYio.currentTouchPoint);
    }


    private void prepareScenes() {
        for (InterfaceElement interfaceElement : yioGdxGame.menuControllerYio.getInterfaceElements()) {
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.isResistantToAutoDestroy()) continue;
            interfaceElement.destroy();
        }
    }


    public void destroy() {
        if (appearFactor.getProgress() == 0) return;
        appearFactor.destroy(MovementType.lighty, 3 * MenuParams.ANIM_SPEED);
        onDestroy();
    }


    private void onDestroy() {
        updateAnimationPoint();
        updateAnimationTexture();
    }


    public void updateAnimationTexture() {
        frameBuffer.begin();
        batchSolid.begin();
        batchSolid.draw(voidTexture, 0, 0, w, h);
        batchSolid.end();
        currentZoomQuality = 0; // zoom quality will be updated immediately after
        renderInternals();
        frameBuffer.end();
        updateZoomQuality();
    }


    public void onPause() {
        GameRender.disposeAllTextures();
    }


    public void onResume() {
        loadTextures(); // atlas
        GameRendersList.getInstance().updateGameRenders(this); // load textures
    }


    public void renderInternals() {
        batchMovable.begin();
        GameRendersList instance = GameRendersList.getInstance();

        instance.renderSimulationCache.render();
        instance.renderRecolorAnimation.render();
        instance.renderTutorialHints.render();
//        instance.renderFiguresRaw.render();
        instance.renderBombs.render();
        instance.renderExternalIndicators.render();
        instance.renderParticles.render();
        instance.renderColorButtons.render();

        renderDyingTouchModes();
        renderCurrentTouchMode();
        renderDebug();
        batchMovable.end();
    }


    private void renderDyingTouchModes() {
        for (TouchMode dyingTm : gameController.dyingTms) {
            GameRender render = dyingTm.getRender();
            if (render == null) return;
            render.render();
        }
    }


    private void renderCurrentTouchMode() {
        GameRender render = gameController.touchMode.getRender();
        if (render == null) return;
        render.render();
    }


    private void renderDebug() {
        if (!DebugFlags.debugEnabled) return;
        GameRendersList instance = GameRendersList.getInstance();
        instance.renderDebugCells.render();
        instance.renderAlgoClusters.render();
    }


    public void render() {
        if (appearFactor.getProgress() < 0.01) return;

        if (appearFactor.getProgress() < 1) {
            renderTransitionFrame();
            return;
        }

        renderVoid();
        renderInternals();
    }


    private void renderVoid() {
        if (!gameController.voidVisible) return;
        batchSolid.begin();
        batchSolid.draw(voidTexture, 0, 0, w, h);
        batchSolid.end();
    }


    void renderTransitionFrame() {
        updateTransitionFramePosition();

        batchSolid.begin();
        if (appearFactor.isInAppearState()) {
            GraphicsYio.setBatchAlpha(batchSolid, Math.min(appearFactor.getValue() * 0.5, 1));
            GraphicsYio.drawByRectangle(batchSolid, darkPixel, screenPosition);
            GraphicsYio.setBatchAlpha(batchSolid, Math.min(appearFactor.getValue() * 1.2, 1));
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
        } else {
            GraphicsYio.setBatchAlpha(batchSolid, Math.max(0, 1 - 1.25f * (1 - appearFactor.getValue())));
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
        }
        GraphicsYio.setBatchAlpha(batchSolid, 1);
        batchSolid.end();
    }


    private void updateTransitionFramePosition() {
        float value = appearFactor.getValue();
        transitionFramePosition.set(
                (1 - value) * 0.5 * GraphicsYio.width,
                (1 - value) * 0.5 * GraphicsYio.height,
                value * GraphicsYio.width,
                value * GraphicsYio.height
        );
    }


    public void move() {
        appearFactor.move();
    }


    public boolean coversAllScreen() {
        return appearFactor.getProgress() == 1;
    }


    public void updateZoomQuality() {
        currentZoomQuality = 0;
    }


    public void setZoomLevels(double[][] zoomValues, LevelSize levelsSize) {
        int lsIndex = levelsSize.ordinal();

        if (lsIndex >= zoomValues.length) {
            lsIndex = zoomValues.length - 1;
        }

        zoomLevelOne = zoomValues[lsIndex][0];
        zoomLevelTwo = zoomValues[lsIndex][1];
    }


    public void onBasicStuffCreated() {
        defaultValues();
        setZoomLevels(
                gameController.cameraController.getZoomValues(),
                gameController.sizeManager.initialLevelSize
        );
        appear();
    }


}
