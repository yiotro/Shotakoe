package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.TouchableYio;

import java.util.ArrayList;

public class ObjectsLayer implements TouchableYio {

    public GameController gameController;
    public CellField cellField;
    public SimulationCacheManager simulationCacheManager;
    public FiguresManager figuresManager;
    public ParticlesManager particlesManager;
    public FinishManager finishManager;
    public ArrayList<TutorialHint> tutorialHints;
    public ExternalIndicatorsManager externalIndicatorsManager;
    public RecolorManager recolorManager;
    public BombsManager bombsManager;
    public ColorButtonsManager colorButtonsManager;
    public TurnsManager turnsManager;
    public AiManager aiManager;
    public Difficulty difficulty;
    public PossibleColorsManager possibleColorsManager;


    public ObjectsLayer(GameController gameController) {
        this.gameController = gameController;
        cellField = new CellField(this);
        simulationCacheManager = new SimulationCacheManager(this);
        figuresManager = new FiguresManager(this);
        particlesManager = new ParticlesManager(this);
        finishManager = new FinishManager(this);
        tutorialHints = new ArrayList<>();
        externalIndicatorsManager = new ExternalIndicatorsManager(this);
        recolorManager = new RecolorManager(this);
        bombsManager = new BombsManager(this);
        colorButtonsManager = new ColorButtonsManager(this);
        turnsManager = new TurnsManager(this);
        aiManager = new AiManager(this);
        possibleColorsManager = new PossibleColorsManager(this);
        difficulty = null;
    }


    public void move() {
        finishManager.move();
        figuresManager.move();
        particlesManager.move();
        recolorManager.move();
        colorButtonsManager.move();
        aiManager.move();
        bombsManager.move();
    }


    public void onBasicStuffCreated() {

    }


    public void clearForLevelGeneration() {
        cellField.clear();
    }


    public void onAdvancedStuffCreated() {
        forceCamera();
        figuresManager.onAdvancedStuffCreated();
        turnsManager.onAdvancedStuffCreated();
        finishManager.onAdvancedStuffCreated();
        simulationCacheManager.update();
    }


    public void onEndCreation() {
        colorButtonsManager.onEndCreation();
        difficulty = getLoadingParameters().difficulty;
        aiManager.onEndCreation();
    }


    private void forceCamera() {
        CameraController cameraController = gameController.cameraController;
        RectangleYio bounds = gameController.sizeManager.position;
        PointYio focusPoint = new PointYio();
        float middleY = cellField.getMiddleY();
        focusPoint.set(bounds.x + bounds.width / 2, middleY - 0.03f * GraphicsYio.height);
        cameraController.focusImmediately(focusPoint, 0.8);
    }


    public void onTurnMade() {
        turnsManager.onTurnMade();
        colorButtonsManager.onTurnMade();
        turnsManager.checkToSwitchBack();
        bombsManager.onTurnMade();
        finishManager.onTurnMade();
    }


    public boolean isCapturingAllowed() {
        if (!finishManager.isCapturingAllowed()) return false;
        if (!bombsManager.isCapturingAllowed()) return false;
        return true;
    }


    public void onCellTypeChanged(Cell cell, CellType previousType) {
        externalIndicatorsManager.applyUpdate();
        bombsManager.onCellTypeChanged(cell, previousType);
    }


    public void onCellChangedColor(Cell cell) {
        figuresManager.onCellChangedColor(cell);
    }


    public LoadingParameters getLoadingParameters() {
        return gameController.yioGdxGame.loadingManager.previousParameters;
    }


    public void onClick() {

    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        return false;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        return false;
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        return false;
    }


    public void onDestroy() {

    }


}
