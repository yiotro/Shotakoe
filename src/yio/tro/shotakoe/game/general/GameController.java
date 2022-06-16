package yio.tro.shotakoe.game.general;

import com.badlogic.gdx.Gdx;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.DebugActionsController;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.ClickDetector;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;

public class GameController {

    public YioGdxGame yioGdxGame;
    public int w, h;
    public int currentTouchCount;
    long currentTime;
    public PointYio touchDownPos, currentTouch;
    public DebugActionsController debugActionsController;
    public GameMode gameMode;
    public ObjectsLayer objectsLayer;
    ClickDetector clickDetector;
    public boolean voidVisible;
    public CameraController cameraController;
    public PointYio currentTouchConverted;
    public TouchMode touchMode;
    public ArrayList<TouchMode> dyingTms;
    public SizeManager sizeManager;


    public GameController(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        touchDownPos = new PointYio();
        currentTouch = new PointYio();
        cameraController = new CameraController(this);
        debugActionsController = new DebugActionsController(this);
        clickDetector = new ClickDetector();
        gameMode = null;
        currentTouchConverted = new PointYio();
        dyingTms = new ArrayList<>();
        sizeManager = new SizeManager();

        TouchMode.createModes(this);
        touchMode = null;
    }


    public void move() {
        currentTime = System.currentTimeMillis();
        cameraController.move();
        moveTouchMode();
        objectsLayer.move();
    }


    private void moveTouchMode() {
        if (touchMode != null) {
            touchMode.move();
        }

        for (int i = dyingTms.size() - 1; i >= 0; i--) {
            TouchMode dtm = dyingTms.get(i);
            dtm.move();
            if (dtm.isReadyToBeRemoved()) {
                dyingTms.remove(dtm);
            }
        }
    }


    public void defaultValues() {
        cameraController.defaultValues();
        currentTouchCount = 0;
        touchDownPos.set(0, 0);
        setTouchMode(TouchMode.tmNone);
    }


    public void createCamera() {
        yioGdxGame.gameView.createOrthoCam();
        cameraController.createCamera();
        yioGdxGame.gameView.updateCam();
    }


    public void createObjectsLayer() {
        if (objectsLayer != null) {
            objectsLayer.onDestroy();
        }
        objectsLayer = new ObjectsLayer(this);
    }


    public void debugActions() {
        debugActionsController.updateReferences();
        debugActionsController.debugActions();
    }


    public YioGdxGame getYioGdxGame() {
        return yioGdxGame;
    }


    public void touchDown(int screenX, int screenY) {
        currentTouchCount++;
        updateTouchPoints(screenX, screenY);
        touchDownPos.setBy(currentTouch);
        clickDetector.onTouchDown(currentTouch);

        if (objectsLayer.onTouchDown(currentTouch)) return;
        touchMode.touchDownReaction();
    }


    boolean touchedAsClick() {
        return clickDetector.isClicked();
    }


    public void updateTouchPoints(int screenX, int screenY) {
        currentTouch.x = screenX;
        currentTouch.y = screenY;

        if (cameraController.orthoCam != null) {
            currentTouchConverted.x = (screenX - 0.5f * w) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.x;
            currentTouchConverted.y = (screenY - 0.5f * h) * cameraController.orthoCam.zoom + cameraController.orthoCam.position.y;
        }
    }


    public void touchUp(int screenX, int screenY, int pointer, int button) {
        currentTouchCount = 0;

        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchUp(currentTouch);
        if (objectsLayer.onTouchUp(currentTouch)) return;
        checkForClick();
        touchMode.touchUpReaction();
    }


    private void checkForClick() {
        if (currentTouchCount != 0) return;
        if (!touchedAsClick()) return;
        onClick();
    }


    public void onMouseWheelScrolled(int amount) {
        if (touchMode != null) {
            if (touchMode.onMouseWheelScrolled(amount)) {
                return; // touch mode can catch mouse wheel scroll
            }
        }

        cameraController.onMouseWheelScrolled(amount);
    }


    public void setTouchMode(TouchMode touchMode) {
        if (this.touchMode == touchMode) return;

        TouchMode previousTouchMode = this.touchMode;
        this.touchMode = touchMode;
        onTmEnd(previousTouchMode);
        touchMode.onModeBegin();
        Scenes.gameOverlay.onTouchModeSet(touchMode);

        dyingTms.remove(touchMode);
    }


    private void onTmEnd(TouchMode previousTouchMode) {
        if (previousTouchMode == null) return;
        previousTouchMode.kill();
        previousTouchMode.onModeEnd();

        if (!dyingTms.contains(previousTouchMode)) {
            dyingTms.add(previousTouchMode);
        }
    }


    public void resetTouchMode() {
        switch (gameMode) {
            default:
            case training:
            case custom:
                setTouchMode(TouchMode.tmDefault);
                break;
        }
    }


    private void onClick() {
        if (touchMode.onClick()) {
            cameraController.forgetAboutLastTap();
            return;
        }
        objectsLayer.onClick();
    }


    public void touchDrag(int screenX, int screenY, int pointer) {
        updateTouchPoints(screenX, screenY);
        clickDetector.onTouchDrag(currentTouch);
        if (objectsLayer.onTouchDrag(currentTouch)) return;
        touchMode.touchDragReaction();
    }


    public void onBasicStuffCreated() {
        cameraController.onBasicStuffCreated();
        objectsLayer.onBasicStuffCreated();
        resetTouchMode();
    }


    public void onAdvancedStuffCreated() {
        objectsLayer.onAdvancedStuffCreated();
        cameraController.onAdvancedStuffCreated();
    }


    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }


    public void onEscapedToPauseMenu() {
        if (gameMode == null) return; // no simulations were started yet
        resetTouchMode();
    }


    public void onPause() {

    }


    public void onResume() {
        currentTouchCount = 0;
    }


    public void setVoidVisible(boolean voidVisible) {
        this.voidVisible = voidVisible;
    }
}
