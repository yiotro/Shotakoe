package yio.tro.shotakoe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.loading.LoadingManager;
import yio.tro.shotakoe.game.tests.AbstractTest;
import yio.tro.shotakoe.game.view.GameView;
import yio.tro.shotakoe.game.view.game_renders.GameRendersList;
import yio.tro.shotakoe.menu.*;
import yio.tro.shotakoe.menu.background.GeneralBackgroundManager;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.FrameBufferYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.StoreLinksYio;
import yio.tro.shotakoe.stuff.calendar.CalendarManager;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MbFactoryYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

import java.util.Random;

public class YioGdxGame extends ApplicationAdapter implements InputProcessor {

    public static long appLaunchTime, initialLoadingTime;
    public static PlatformType platformType;

    public boolean alreadyShownErrorMessageOnce;
    boolean ignoreDrag, loadedResources;
    public boolean gamePaused, readyToUnPause, startedExitProcess;

    public int w, h;
    int frameSkipCount;

    long lastTimeButtonPressed, timeToUnPause;
    public float pressX, pressY;

    public GameController gameController;
    public GameView gameView;
    public SpriteBatch batch;
    public MenuControllerYio menuControllerYio;
    public MenuViewYio menuViewYio;

    FrameBuffer frameBuffer;
    public static Random random = new Random();
    public LoadingManager loadingManager;
    public OnKeyReactions onKeyReactions;
    public GeneralBackgroundManager generalBackgroundManager;
    public boolean slowMo;
    public Stage stage; // for keyboard input
    public InputMultiplexer inputMultiplexer;
    SplashManager splashManager;
    public AbstractTest currentTest;
    FpsRenderer fpsRenderer;
    public boolean minimized;


    @Override
    public void create() {
        loadedResources = false;
        batch = new SpriteBatch();
        splashManager = new SplashManager(this);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        GraphicsYio.width = (float) Gdx.graphics.getWidth();
        GraphicsYio.height = (float) Gdx.graphics.getHeight();
        pressX = 0.5f * w;
        pressY = 0.5f * h;
        frameSkipCount = 50; // >= 2
        frameBuffer = FrameBufferYio.getInstance(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        slowMo = false;
        stage = new Stage();
        currentTest = null;
        minimized = false;
    }


    void generalInitialization() {
        long startTime = System.currentTimeMillis();
        appLaunchTime = System.currentTimeMillis();
        loadedResources = true;

        splashManager.init();
        gamePaused = true;
        alreadyShownErrorMessageOnce = false;
        startedExitProcess = false;

        SceneYio.onGeneralInitialization(); // before menu controller
        Reaction.initialize();
        initializeSingletons();
        SettingsManager.getInstance().loadValues();
        Fonts.init(); // causes main delay on launch
        generalBackgroundManager = new GeneralBackgroundManager(this);
        menuControllerYio = new MenuControllerYio(this);
        menuViewYio = new MenuViewYio(this);
        applyBackground(BackgroundYio.yellow);
        gameController = new GameController(this); // must be called after menu controller is created. (maybe not)
        gameView = new GameView(this);
        gameView.appearFactor.destroy(MovementType.lighty, 1);
        loadingManager = new LoadingManager(gameController);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        SoundManager.loadSounds();
        onKeyReactions = new OnKeyReactions(this);
        fpsRenderer = new FpsRenderer(this);
        menuControllerYio.createInitialScene();

        initialLoadingTime = System.currentTimeMillis() - startTime;
        System.out.println("Initial loading time: " + initialLoadingTime);
    }


    private void initializeSingletons() {
        MbFactoryYio.initialize();
        TextFitParser.initialize();
        LanguagesManager.initialize();
        OneTimeInfo.initialize();
        SettingsManager.initialize();
        GameRendersList.initialize();
        StoreLinksYio.initialize();
        MenuSwitcher.initialize();
        RefreshRateDetector.initialize();
        CalendarManager.initialize();
        CalendarManager.getInstance().loadValues();
        CampaignManager.initialize();
    }


    public void applyFullTransitionToUI() {
        setGamePaused(true);
        gameController.onEscapedToPauseMenu();
        menuControllerYio.destroyGameView();
    }


    public void setGamePaused(boolean gamePaused) {
        if (gamePaused == this.gamePaused) return;

        if (gamePaused) {
            this.gamePaused = true;
            onPaused();
            return;
        }

        if (!gamePaused) {
            unPauseAfterSomeTime();
            onUnPaused();
        }
    }


    private void onUnPaused() {
        applyBackground(BackgroundYio.dark);
    }


    private void onPaused() {
        applyBackground(BackgroundYio.dark);
        menuControllerYio.forceDyingElementsToEnd();
    }


    public void applyBackground(BackgroundYio value) {
        generalBackgroundManager.applyBackground(value);
    }


    public void move() {
        if (!loadedResources) return;

        splashManager.move();

        checkForSlowMo();
        generalBackgroundManager.move();
        RefreshRateDetector.getInstance().move();
        checkToUnPause();

        gameView.move();

        moveInternalGameStuff();
        moveCurrentTest();

        menuControllerYio.move();
        if (loadingManager.working) return; // immediately after button press

        stage.act();
    }


    private void moveInternalGameStuff() {
        if (!loadedResources) return;
        if (gamePaused) return;

        gameView.updateZoomQuality();
        gameController.move();
    }


    private void moveCurrentTest() {
        if (currentTest == null) return;
        currentTest.move();
    }


    private void checkForSlowMo() {
        if (!slowMo) return;

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void checkToUnPause() {
        if (readyToUnPause && System.currentTimeMillis() > timeToUnPause && gameView.coversAllScreen()) {
            gamePaused = false;
            readyToUnPause = false;
            gameController.currentTouchCount = 0;
            frameSkipCount = 10; // >= 2
        }
    }


    public void renderInternals() {
        if (!loadedResources) return;

        generalBackgroundManager.render();

        menuViewYio.applyRender(false);
        gameView.render();
        menuViewYio.applyRender(true);

        fpsRenderer.render();
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!loadedResources) {
            splashManager.renderBeforeInitialization();
            splashManager.increaseSplashCount();
            return;
        }

        tryToMove();

        if (hasToRenderDirectly()) {
            renderInternals();
        } else {
            renderThroughFrameBuffer();
        }

        splashManager.renderAfterInitialization();

        stage.draw();
    }


    private void renderThroughFrameBuffer() {
        if (Gdx.graphics.getDeltaTime() < 0.025 || frameSkipCount >= 2) {
            frameSkipCount = 0;
            frameBuffer.begin();
            renderInternals();
            frameBuffer.end();
        } else {
            frameSkipCount++;
        }
        batch.begin();
        batch.draw(
                frameBuffer.getColorBufferTexture(),
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                false, true
        );
        batch.end();
    }


    private boolean hasToRenderDirectly() {
        if (generalBackgroundManager.transitionFactor.getProgress() < 1) return true;
        return gamePaused || slowMo;
    }


    private void tryToMove() {
        try {
            move();
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onExceptionCaught(exception);
            }
        }
    }


    void unPauseAfterSomeTime() {
        readyToUnPause = true;
        timeToUnPause = System.currentTimeMillis() + 100;
    }


    public static void say(String text) {
        System.out.println(text);
    }


    @Override
    public void pause() {
        super.pause();

        if (startedExitProcess) return;
        minimized = true;

        if (gameView != null) {
            gameView.onPause();
        }

        if (gameController != null) {
            gameController.onPause();
        }

        if (menuControllerYio != null) {
            menuControllerYio.onAppPause();
        }
    }


    @Override
    public void resume() {
        super.resume();

        if (startedExitProcess) return;
        minimized = false;

        if (gameView != null) {
            gameView.onResume();
        }

        if (gameController != null) {
            gameController.onResume();
        }

        if (menuControllerYio != null) {
            menuControllerYio.onAppResume();
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (!splashManager.areKeyInputsAllowed()) return true;

        try {
            onKeyReactions.keyDown(keycode);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onExceptionCaught(exception);
            }
        }

        return true;
    }


    @Override
    public boolean keyUp(int keycode) {

        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    public boolean isGamePaused() {
        return gamePaused;
    }


    public void onExceptionCaught(Exception exception) {
        gameView.destroy();
        setGamePaused(true);
        loadingManager.working = false;
        Scenes.exceptionReport.setException(exception);
        Scenes.exceptionReport.create();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ignoreDrag = true;
        pressX = screenX;
        pressY = h - screenY;
//        System.out.println("Screen touch: " + Yio.roundUp(pressX / w, 4) + " " + Yio.roundUp(pressY / h, 4));
        try {
            if (touchDownReaction(screenX, screenY)) return false;
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onExceptionCaught(exception);
            }
        }
        return false;
    }


    private boolean touchDownReaction(int screenX, int screenY) {
        if (!isGameViewBlockingUiReaction() && menuControllerYio.touchDown(screenX, h - screenY)) {
            lastTimeButtonPressed = System.currentTimeMillis();
            return true;
        } else {
            ignoreDrag = false;
        }

        if (!gamePaused) {
            gameController.touchDown(screenX, Gdx.graphics.getHeight() - screenY);
        }

        return false;
    }


    private boolean isGameViewBlockingUiReaction() {
        FactorYio appearFactor = gameView.appearFactor;
        if (appearFactor.isInAppearState() && appearFactor.getProgress() < 1) return true;
        if (appearFactor.isInDestroyState() && appearFactor.getProgress() > 0.25) return true;
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        try {
            touchUpReaction(screenX, screenY, pointer, button);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onExceptionCaught(exception);
            }
        }
        return false;
    }


    private void touchUpReaction(int screenX, int screenY, int pointer, int button) {
        if (menuControllerYio.touchUp(screenX, h - screenY, pointer, button)) return;

        if (!gamePaused && gameView.coversAllScreen()) {
            gameController.touchUp(screenX, h - screenY, pointer, button);
        }
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        try {
            touchDragReaction(screenX, screenY, pointer);
        } catch (Exception exception) {
            if (!alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                alreadyShownErrorMessageOnce = true;
                onExceptionCaught(exception);
            }
        }

        return false;
    }


    private void touchDragReaction(int screenX, int screenY, int pointer) {
        menuControllerYio.touchDrag(screenX, h - screenY, pointer);

        if (!ignoreDrag && !gamePaused && gameView.coversAllScreen()) {
            gameController.touchDrag(screenX, h - screenY, pointer);
        }
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        return scrolled((int) amountY);
    }


    public boolean scrolled(int amount) {
        if (menuControllerYio.onMouseWheelScrolled(amount)) return true;
        gameController.onMouseWheelScrolled(amount);
        return true;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    public void setCurrentTest(AbstractTest currentTest) {
        this.currentTest = currentTest;
    }


    public void exitApp() {
        startedExitProcess = true;
        TextFitParser instance = TextFitParser.getInstance();
        instance.disposeAllTextures();
        instance.killInstance();

        Gdx.app.exit();
    }


}
