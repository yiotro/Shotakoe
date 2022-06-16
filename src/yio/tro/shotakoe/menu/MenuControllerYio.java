package yio.tro.shotakoe.menu;

import yio.tro.shotakoe.*;
import yio.tro.shotakoe.game.general.GameMode;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.button.CacheTexturesManager;
import yio.tro.shotakoe.menu.elements.forefinger.ForefingerElement;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;

import java.util.ArrayList;

public class MenuControllerYio {

    public YioGdxGame yioGdxGame;
    ArrayList<InterfaceElement> interfaceElements, visibleElements;
    public LanguagesManager languagesManager;
    private SceneYio focusScene;
    public CacheTexturesManager cacheTexturesManager;
    public PointYio currentTouchPoint;
    private ArrayList<InterfaceElement> tempList;
    private ClickDetector clickDetector;


    public MenuControllerYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        interfaceElements = new ArrayList<>();
        languagesManager = LanguagesManager.getInstance();
        visibleElements = new ArrayList<>();
        InterfaceElement.initScreenElement(this);
        cacheTexturesManager = new CacheTexturesManager(this);
        focusScene = null;
        currentTouchPoint = new PointYio();
        currentTouchPoint.set(GraphicsYio.width / 2, GraphicsYio.height / 2);
        tempList = new ArrayList<>();
        clickDetector = new ClickDetector();

        MenuSwitcher.getInstance().onMenuControllerCreated(this);
        initRepeats();
        createAllScenes();
    }


    public void createInitialScene() {
        yioGdxGame.applyBackground(BackgroundYio.black);
        Scenes.calendar.create();
        Scenes.campaign.create();
        Scenes.mainMenu.create();
    }


    private void createAllScenes() {
        Scenes.createAllScenes(); // create scenes without initialization
        SceneYio.updateAllScenes(this); // update scenes
    }


    private void initRepeats() {

    }


    public void move() {
        moveScene();

        for (InterfaceElement visibleElement : visibleElements) {
            visibleElement.moveElement();
        }

        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            if (visibleElements.get(i).checkToPerformAction()) break;
        }
    }


    public void checkToRemoveInvisibleElements() {
        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            InterfaceElement element = visibleElements.get(i);
            if (element == null || element.isVisible()) continue;
            if (element.getFactor().isInAppearState()) continue;
            removeVisibleElement(element);
        }
    }


    public void removeVisibleElement(InterfaceElement element) {
        visibleElements.remove(element);
    }


    private void moveScene() {
        if (focusScene == null) return;
        focusScene.move();
    }


    public void onReturningBackButtonPressed() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.enableReverseAnimMode();
        }
        yioGdxGame.generalBackgroundManager.enableReverseMode();
    }


    public void setFocusScene(SceneYio focusScene) {
        this.focusScene = focusScene;
    }


    public void addElement(InterfaceElement interfaceElement) {
        // considered that ui element is not in list at this moment
        interfaceElements.add(interfaceElement);
    }


    public void addVisibleElement(InterfaceElement interfaceElement) {
        if (interfaceElement == null) {
            Yio.printStackTrace();
            return;
        }
        if (visibleElements.contains(interfaceElement)) return;
        visibleElements.add(interfaceElement);
    }


    public boolean isTouchAllowedByScripts() {
        if (yioGdxGame.gameController.gameMode == GameMode.editor) return true;
        if (!yioGdxGame.gameView.coversAllScreen()) return true;
        if (true) return true; // should check if there are no scripts

        ForefingerElement forefinger = Scenes.forefinger.forefinger;
        return forefinger != null && forefinger.getFactor().getProgress() > 0.95;
    }


    private void updateCurrentTouchPoint(int screenX, int screenY) {
        currentTouchPoint.x = screenX;
        currentTouchPoint.y = screenY;
    }


    public boolean touchDown(int screenX, int screenY) {
        if (!isTouchAllowedByScripts()) return false;
        updateCurrentTouchPoint(screenX, screenY);
        updateTempListByVisibleElements();
        clickDetector.onTouchDown(currentTouchPoint);
        for (int i = tempList.size() - 1; i >= 0; i--) {
            InterfaceElement interfaceElement = tempList.get(i);
            if (!interfaceElement.isVisible()) continue;
            if (!interfaceElement.isTouchable()) continue;
            if (!interfaceElement.isActive()) continue;
            if (interfaceElement.touchDownElement(screenX, screenY) && interfaceElement.isCaptureTouch()) {
//                System.out.println("interfaceElement = " + interfaceElement);
                return true;
            }
        }
        return false;
    }


    public void touchDrag(int screenX, int screenY, int pointer) {
        updateCurrentTouchPoint(screenX, screenY);
        clickDetector.onTouchDrag(currentTouchPoint);
        for (InterfaceElement interfaceElement : visibleElements) {
            interfaceElement.touchDragElement(screenX, screenY, pointer);
        }
    }


    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        updateCurrentTouchPoint(screenX, screenY);
        clickDetector.onTouchUp(currentTouchPoint);
        updateTempListByVisibleElements();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            InterfaceElement element = tempList.get(i);
            if (element.touchUpElement(screenX, screenY, pointer, button) && element.isCaptureTouch()) {
                return true;
            }
        }
        if (clickDetector.isClicked()) {
            onClickOutside();
        }
        return false;
    }


    private void onClickOutside() {
        yioGdxGame.generalBackgroundManager.particlesManager.applyJumps(currentTouchPoint);
    }


    private void updateTempListByVisibleElements() {
        tempList.clear();
        tempList.addAll(visibleElements);
    }


    public boolean onMouseWheelScrolled(int amount) {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.onMouseWheelScrolled(amount)) return true;
        }
        return false;
    }


    public void onAppPause() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.onAppPause();
        }
        cacheTexturesManager.onAppPause();
    }


    public void onAppResume() {
        cacheTexturesManager.onAppResume();
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.onAppResume();
        }
    }


    public void forceDyingElementsToEnd() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (!interfaceElement.getFactor().isInDestroyState()) continue;
            interfaceElement.forceDestroyToEnd();
        }
    }


    public void destroyGameView() {
        if (yioGdxGame.gameView == null) return;
        yioGdxGame.gameView.destroy();
    }


    public void showVisibleElementsInConsole() {
        System.out.println();
        System.out.println("Visible elements: ");
        for (InterfaceElement visibleElement : visibleElements) {
            if (visibleElement == null) {
                System.out.println("null");
                continue;
            }
            if (visibleElement.getFactor().getProgress() == 0) {
                System.out.println("- " + visibleElement);
            } else {
                System.out.println("+ " + visibleElement);
            }
        }
        System.out.println();
    }


    public void clear() {
        interfaceElements.clear();
        visibleElements.clear();

        // reload all scenes
        SceneYio.sceneList.clear();
        createAllScenes();
    }


    public ArrayList<InterfaceElement> getInterfaceElements() {
        return interfaceElements;
    }


    public InterfaceElement getElement(String key) {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (!key.equals(interfaceElement.getKey())) continue;
            return interfaceElement;
        }

        return null;
    }


    public ArrayList<InterfaceElement> getVisibleElements() {
        return visibleElements;
    }


    public SceneYio getFocusScene() {
        return focusScene;
    }
}
