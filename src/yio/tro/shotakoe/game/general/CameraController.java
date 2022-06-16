package yio.tro.shotakoe.game.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.export.Encodeable;
import yio.tro.shotakoe.stuff.*;

public class CameraController implements TouchableYio, Encodeable {


    public static final int DOUBLE_TAP_DELAY = 300;
    public static final double ZOOM_CUT = 0.001;
    YioGdxGame yioGdxGame;
    GameController gameController;
    public OrthographicCamera orthoCam;
    boolean blockDragMovement, kineticsEnabled, boundsEnabled;
    int w, h;
    float boundWidth, boundHeight, zoomMinimum, zoomMaximum;
    float camDx, camDy, camDz, targetZoomLevel;
    public float viewZoomLevel, comfortableZoomLevel;
    long touchDownTime, lastTapTime;
    RectangleYio field; // bounds of level
    public RectangleYio frame; // what is visible
    RectangleYio lastMultiTouch, currentMultiTouch;
    double[][] zoomValues;
    double kineticsSpeed;
    PointYio currentTouch, position;
    PointYio viewPosition, defaultDragBounds, backVisBounds;
    PointYio delta, kinetics, actualDragBounds, lastTapPoint;
    private float bottomSpecialOffset;


    public CameraController(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        w = gameController.w;
        h = gameController.h;

        currentTouch = new PointYio();
        position = new PointYio();
        viewPosition = new PointYio();
        field = new RectangleYio();
        frame = new RectangleYio();
        lastTapPoint = new PointYio();
        currentMultiTouch = new RectangleYio();
        lastMultiTouch = new RectangleYio();
        defaultDragBounds = new PointYio();
        backVisBounds = new PointYio();
        delta = new PointYio();
        kinetics = new PointYio();
        actualDragBounds = new PointYio();

        zoomMinimum = 0.7f;
        kineticsSpeed = 0.01 * w;
        comfortableZoomLevel = 0.9f;
        bottomSpecialOffset = 0.15f * GraphicsYio.width;
        kineticsEnabled = false;
        setBoundsEnabled(true);
    }


    public void initZoomValues() {
        zoomValues = getHighGraphicsZooms();
        updateZoomMaximum();
        updateZoomMinimum();
        updateComfortableZoomLevel();
    }


    private double[][] getHighGraphicsZooms() {
        return new double[][]{
                {1.3, 2.6, 1.1},
                {1.3, 2.6, 1.4},
                {1.3, 2.6, 1.75},
                {1.3, 2.6, 2.1},
        };
    }


    private void updateZoomMinimum() {
        zoomMinimum = 0.7f;
        setTargetZoomLevel(zoomMinimum);
    }


    private void updateComfortableZoomLevel() {
        comfortableZoomLevel = 0.9f;
        if (comfortableZoomLevel >= zoomMinimum) return;
        comfortableZoomLevel = zoomMinimum;
    }


    public void updateZoomMaximum() {
        LevelSize levelSize = gameController.sizeManager.initialLevelSize;
        int lsIndex = levelSize.ordinal();

        if (lsIndex >= zoomValues.length) {
            lsIndex = zoomValues.length - 1;
        }

        setZoomMaximum(zoomValues[lsIndex][2]);
    }


    public void onBasicStuffCreated() {
        initZoomValues();
        forceToTargetPosition();
    }


    public void forceToTargetPosition() {
        setBoundsEnabled(false);
        for (int i = 0; i < 30; i++) {
            move();
        }

        setBoundsEnabled(true);
        for (int i = 0; i < 20; i++) {
            move();
        }
    }


    private void updateCurrentMultiTouch() {
        currentMultiTouch.set(0, 0, Gdx.input.getX(1) - Gdx.input.getX(0), Gdx.input.getY(1) - Gdx.input.getY(0));
    }


    private void updateLastMultiTouch() {
        lastMultiTouch.setBy(currentMultiTouch);
    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        // initial touch with one finger
        if (gameController.currentTouchCount == 1) {
            touchDownTime = gameController.currentTime;
            blockDragMovement = false;
            currentTouch.setBy(touchPoint);
            delta.set(0, 0);
        }

        // multi touch
        if (gameController.currentTouchCount >= 2) {
            blockDragMovement = true;
            updateCurrentMultiTouch();
            updateLastMultiTouch();
        }

        return false;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        delta.x = 1.4f * (currentTouch.x - touchPoint.x) * viewZoomLevel;
        delta.y = 1.4f * (currentTouch.y - touchPoint.y) * viewZoomLevel;

        if (!blockDragMovement) {
            position.x += delta.x;
            position.y += delta.y;

            applyBoundsToPosition();
        }

        currentTouch.setBy(touchPoint);

        // pinch to zoom
        if (gameController.currentTouchCount == 2) {
            updateCurrentMultiTouch();

            double currentDistance = Yio.distance(0, 0, currentMultiTouch.width, currentMultiTouch.height);
            double lastDistance = Yio.distance(0, 0, lastMultiTouch.width, lastMultiTouch.height);
            double zoomDelta = 0.004 * (lastDistance - currentDistance);

            changeZoomLevel(zoomDelta);

            updateLastMultiTouch();
        }

        return false;
    }


    public void setTargetZoomLevel(float targetZoomLevel) {
        this.targetZoomLevel = targetZoomLevel;
    }


    public void changeZoomLevel(double zoomDelta) {
        targetZoomLevel += zoomDelta;
        if (targetZoomLevel < zoomMinimum) {
            targetZoomLevel = zoomMinimum;
        }
        if (targetZoomLevel > zoomMaximum) {
            targetZoomLevel = zoomMaximum;
        }
    }


    private void applyBoundsToPosition() {
        if (!boundsEnabled) return;

        if (position.x > actualDragBounds.x) {
            position.x = actualDragBounds.x;
        }

        if (position.x < -actualDragBounds.x) {
            position.x = -actualDragBounds.x;
        }

        if (position.y > actualDragBounds.y) {
            position.y = actualDragBounds.y;
        }

        if (position.y < -actualDragBounds.y - bottomSpecialOffset) {
            position.y = -actualDragBounds.y - bottomSpecialOffset;
        }
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        double speed = Yio.distance(0, 0, delta.x, delta.y);

        if (!blockDragMovement && (speed > kineticsSpeed || touchWasQuick())) {
            kineticsEnabled = true;
            kinetics.x = delta.x;
            kinetics.y = delta.y;
        }

        currentTouch.setBy(touchPoint);

        if (touchWasQuick() && gameController.currentTouchCount == 0 && speed < kineticsSpeed) {
            tapReaction(touchPoint);
        }

        return false;
    }


    public void onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            changeZoomLevel(0.5);
        } else if (amount == -1) {
            changeZoomLevel(-0.6);
        }
    }


    private void tapReaction(PointYio touchPoint) {
        long currentTapTime = System.currentTimeMillis();

        if (doubleTapConditions(currentTapTime)) {
            doubleTapReaction(touchPoint);
            forgetAboutLastTap();
        } else {
            lastTapTime = currentTapTime;
        }

        lastTapPoint.setBy(currentTouch);
    }


    private boolean doubleTapConditions(long currentTapTime) {
        if (currentTapTime - lastTapTime > DOUBLE_TAP_DELAY) return false;
        if (currentTouch.distanceTo(lastTapPoint) > 0.1f * GraphicsYio.width) return false;
        return true;
    }


    private void doubleTapReaction(PointYio touchPoint) {
        if (gameController.touchMode != null && gameController.touchMode.isDoubleClickDisabled()) return;

        if (targetZoomLevel == 0.55f) {
            setTargetZoomLevel(1.92f);

            if (targetZoomLevel > zoomMaximum) {
                targetZoomLevel = zoomMaximum;
            }

            return;
        }

        setTargetZoomLevel(0.55f);
    }


    private boolean touchWasQuick() {
        return System.currentTimeMillis() - touchDownTime < 200;
    }


    public void move() {
        updateDragBounds();
        updateField();

        moveKinetics();
        moveDrag();
        moveZoom();

        updateFrame();
        updateVoidVisibility();
    }


    private void updateDragBounds() {
        actualDragBounds.setBy(defaultDragBounds);
        actualDragBounds.x -= 0.4 * w * viewZoomLevel;
        actualDragBounds.y -= 0.45 * h * viewZoomLevel;

        if (actualDragBounds.x < 0) {
            actualDragBounds.x = 0;
        }

        if (actualDragBounds.y < 0) {
            actualDragBounds.y = 0;
        }
    }


    private void moveKinetics() {
        if (!kineticsEnabled) return;

        if (Yio.distance(0, 0, kinetics.x, kinetics.y) < 0.5 * kineticsSpeed) {
            kineticsEnabled = false;
        }

        position.x += kinetics.x;
        position.y += kinetics.y;

        applyBoundsToPosition();

        kinetics.x *= 0.85;
        kinetics.y *= 0.85;
    }


    private void updateVoidVisibility() {
        backVisBounds.setBy(defaultDragBounds);
        backVisBounds.x -= 0.5 * w * viewZoomLevel;
        backVisBounds.y -= 0.5 * h * viewZoomLevel;

        gameController.setVoidVisible(
                Math.abs(position.x) > backVisBounds.x || Math.abs(position.y) > backVisBounds.y
        );
    }


    private void updateField() {
        field.x = 0.5f * w - orthoCam.position.x / orthoCam.zoom;
        field.y = 0.5f * h - orthoCam.position.y / orthoCam.zoom;
        field.width = boundWidth / orthoCam.zoom;
        field.height = boundHeight / orthoCam.zoom;
    }


    private void moveZoom() {
        if (viewZoomLevel == targetZoomLevel) return;

        camDz = 0.2f * (targetZoomLevel - viewZoomLevel);
        if (Math.abs(camDz) < ZOOM_CUT) {
            viewZoomLevel = targetZoomLevel;
            return;
        }

        yioGdxGame.gameView.orthoCam.zoom += camDz;
        viewZoomLevel += camDz;
        yioGdxGame.gameView.updateCam();
        applyBoundsToPosition(); // bounds may change on zoom
    }


    public boolean isCircleInViewFrame(CircleYio circleYio) {
        return isPointInViewFrame(circleYio.center, circleYio.radius);
    }


    public boolean isCircleInViewFrame(CircleYio circleYio, double offset) {
        return isPointInViewFrame(circleYio.center, (float) (circleYio.radius + offset));
    }


    public boolean isPointInViewFrame(PointYio pos, float offset) {
        if (pos.x < frame.x - offset) return false;
        if (pos.x > frame.x + frame.width + offset) return false;
        if (pos.y < frame.y - offset) return false;
        if (pos.y > frame.y + frame.height + offset) return false;

        return true;
    }


    public boolean isRectangleInViewFrame(RectangleYio pos, float offset) {
        if (pos.x + pos.width < frame.x - offset) return false;
        if (pos.x > frame.x + frame.width + offset) return false;
        if (pos.y + pos.height < frame.y - offset) return false;
        if (pos.y > frame.y + frame.height + offset) return false;
        return true;
    }


    void moveDrag() {
        camDx = 0.5f * (position.x - viewPosition.x);
        camDy = 0.5f * (position.y - viewPosition.y);

        viewPosition.x += camDx;
        viewPosition.y += camDy;

        yioGdxGame.gameView.orthoCam.translate(camDx, camDy);
        yioGdxGame.gameView.updateCam();
    }


    public void setBounds(float width, float height) {
        boundWidth = width;
        boundHeight = height;
        defaultDragBounds.set(boundWidth / 2, boundHeight / 2);
    }


    public void onLevelBoundsSet() {
        SizeManager sizeManager = gameController.sizeManager;
        setBounds(sizeManager.boundWidth, sizeManager.boundHeight);
    }


    public void setZoomMaximum(double zoomMaximum) {
        this.zoomMaximum = (float) zoomMaximum;
    }


    void createCamera() {
        orthoCam = yioGdxGame.gameView.orthoCam;
        orthoCam.translate((boundWidth - w) / 2, (boundHeight - h) / 2); // focus camera on center
        targetZoomLevel = orthoCam.zoom;
        updateFrame();
    }


    void defaultValues() {
        viewZoomLevel = 1;
        position.set(0, 0);
        viewPosition.setBy(position);
    }


    void updateFrame() {
        frame.x = (0 - 0.5f * w) * orthoCam.zoom + orthoCam.position.x;
        frame.y = (0 - 0.5f * h) * orthoCam.zoom + orthoCam.position.y;
        frame.width = w * orthoCam.zoom;
        frame.height = h * orthoCam.zoom;
    }


    public double[][] getZoomValues() {
        return zoomValues;
    }


    public void forgetAboutLastTap() {
        lastTapTime = 0;
    }


    public float getTargetZoomLevel() {
        return targetZoomLevel;
    }


    public void focusOnPoint(PointYio position) {
        focusOnPoint(position.x, position.y);
    }


    public void focusOnPoint(double x, double y) {
        position.x = (float) (x - gameController.sizeManager.boundWidth / 2);
        position.y = (float) (y - gameController.sizeManager.boundHeight / 2);
    }


    public void ensureComfortableZoomLevel() {
        if (targetZoomLevel <= comfortableZoomLevel) return;
        setTargetZoomLevel(comfortableZoomLevel);
    }


    public void setBoundsEnabled(boolean boundsEnabled) {
        this.boundsEnabled = boundsEnabled;
    }


    public float getZoomMinimum() {
        return zoomMinimum;
    }


    public float getZoomMaximum() {
        return zoomMaximum;
    }


    public void flyUp(boolean instant) {
        PointYio pointYio = new PointYio();
        pointYio.x = gameController.sizeManager.boundWidth / 2;
        pointYio.y = gameController.sizeManager.boundHeight / 2;
        if (instant) {
            focusImmediately(pointYio, zoomMaximum - 0.1);
        } else {
            setTargetZoomLevel(zoomMaximum);
            focusOnPoint(pointYio);
        }
    }


    @Override
    public String encode() {
        return Yio.roundUp((position.x + boundWidth / 2) / GraphicsYio.width) + " " +
                Yio.roundUp((position.y + boundHeight / 2) / GraphicsYio.height) + " " +
                Yio.roundUp(viewZoomLevel);
    }


    public void decode(String src) {
        String[] split = src.split(" ");
        float x = Float.valueOf(split[0]);
        float y = Float.valueOf(split[1]);
        float z = Float.valueOf(split[2]);
        x *= GraphicsYio.width;
        x -= boundWidth / 2;
        y *= GraphicsYio.height;
        y -= boundHeight / 2;

        applyImmediately(x, y, z);
    }


    public void applyImmediately(double x, double y, double z) {
        forceZoomValue((float) z);
        position.set(x, y);
        for (int i = 0; i < 100; i++) {
            move();
        }
    }


    public void focusImmediately(PointYio pointYio, double z) {
        forceZoomValue((float) z);
        focusOnPoint(pointYio);
        for (int i = 0; i < 100; i++) {
            move();
        }
    }


    private void forceZoomValue(float z) {
        setTargetZoomLevel(z);
        for (int i = 0; i < 100; i++) {
            move();
        }
    }


    public void onAdvancedStuffCreated() {
        automaticallyDetectZoomValue();
    }


    private void automaticallyDetectZoomValue() {
        float z = 1;
        while (true) {
            forceZoomValue(z);
            if (isWholeFieldFullyVisible()) break;
            z += 0.02f;
        }
    }


    private boolean isWholeFieldFullyVisible() {
        CellField cellField = gameController.objectsLayer.cellField;
        for (Cell cell : cellField.activeCells) {
            if (!frame.isPointInside(cell.position.center, 1.5f * cell.position.radius)) return false;
        }
        return true;
    }


    public RectangleYio getFrame() {
        return frame;
    }


    public PointYio getPosition() {
        return position;
    }


    @Override
    public String toString() {
        return "[CameraController: " +
                encode() +
                "]";
    }
}
