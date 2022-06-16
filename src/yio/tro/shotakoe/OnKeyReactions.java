package yio.tro.shotakoe;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.shotakoe.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.shotakoe.menu.scenes.Scenes;

import java.util.ArrayList;
import java.util.Random;

public class OnKeyReactions {

    YioGdxGame yioGdxGame;
    MenuControllerYio menuControllerYio;
    GameController gameController;


    public OnKeyReactions(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        gameController = yioGdxGame.gameController;
    }


    public void keyDown(int keycode) {
        if (checkForKeyboard(keycode)) return;

        if (keycode == Input.Keys.ESCAPE) {
            keycode = Input.Keys.BACK;
        }

        checkForHotkeyUiReaction(keycode);
        checkOtherStuff(keycode);
    }


    private void checkForHotkeyUiReaction(int keycode) {
        if (keycode == Input.Keys.ENTER && Scenes.keyboard.isCurrentlyVisible()) return;
        ArrayList<InterfaceElement> interfaceElements = menuControllerYio.getInterfaceElements();
        for (int i = interfaceElements.size() - 1; i >= 0; i--) {
            InterfaceElement element = interfaceElements.get(i);
            if (!element.isVisible()) continue;
            if (element.getFactor().getProgress() < 0.95) continue;
            if (!element.acceptsKeycode(keycode)) continue;
            element.pressArtificially(keycode);
            break;
        }
    }


    private boolean checkForKeyboard(int keycode) {
        CustomKeyboardElement customKeyboardElement = Scenes.keyboard.customKeyboardElement;
        if (customKeyboardElement == null) return false;
        if (customKeyboardElement.getFactor().getProgress() < 0.2) return false;

        customKeyboardElement.onPcKeyPressed(keycode);
        return true;
    }


    private void checkOtherStuff(int keycode) {
        if (YioGdxGame.platformType != PlatformType.pc) return;
        switch (keycode) {
            case Input.Keys.D:
                gameController.debugActions(); // debug
                break;
            case Input.Keys.L:
                launchQuickSimulation();
                break;
            case Input.Keys.NUM_0:
                yioGdxGame.setGamePaused(true);
                yioGdxGame.gameView.destroy();
                Scenes.debugTests.create();
                break;
            case Input.Keys.NUM_1:
            case Input.Keys.NUM_2:
                System.out.println("OnKeyReactions.checkOtherStuff");
                break;
            case Input.Keys.C:
                yioGdxGame.applyFullTransitionToUI();
                Scenes.secretScreen.create();
                break;
            case Input.Keys.S:
                yioGdxGame.slowMo = !yioGdxGame.slowMo;
                yioGdxGame.render();
                yioGdxGame.render();
                yioGdxGame.render();
                break;
            case Input.Keys.E:
                if (!yioGdxGame.gameView.coversAllScreen()) break;
                if (Scenes.editPanel.isCurrentlyVisible()) {
                    Scenes.editPanel.destroy();
                    break;
                }
                Scenes.editPanel.create();
                break;
            case Input.Keys.A:
                if (!Scenes.debugPanel.isCurrentlyVisible()) {
                    Scenes.debugPanel.create();
                } else {
                    Scenes.debugPanel.destroy();
                }
                break;
            case Input.Keys.X:
                yioGdxGame.applyFullTransitionToUI();
                Scenes.campaign.create();
                break;
            case Input.Keys.T:
                Scenes.testScreen.create();
                break;
            case Input.Keys.Z:
                gameController.cameraController.setTargetZoomLevel(gameController.cameraController.comfortableZoomLevel);
                break;
            case Input.Keys.U:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(0.1);
                } else {
                    System.out.println("OnKeyReactions.checkOtherStuff");
                }
                break;
            case Input.Keys.I:
                if (!gameController.yioGdxGame.gamePaused) {
                    gameController.cameraController.changeZoomLevel(-0.1);
                } else {
                    System.out.println("OnKeyReactions.checkOtherStuff");
                }
                break;
            case Input.Keys.K:
                loadLastEditorSlot();
                break;
            case Input.Keys.NUM_9:
                openCurrentLevelInEditor();
                break;
            case Input.Keys.J:
                System.out.println("OnKeyReactions.checkOtherStuff");
                break;
            case Input.Keys.P:
                yioGdxGame.gamePaused = !yioGdxGame.gamePaused;
                Scenes.notification.show("Game paused: " + yioGdxGame.gamePaused);
                break;
            case Input.Keys.Q:
                Scenes.keyboard.create();
                Scenes.keyboard.setReaction(new AbstractKbReaction() {
                    @Override
                    public void onInputFromKeyboardReceived(String input) {
                        int index = Integer.valueOf(input);
                        CampaignManager.getInstance().launchCampaignLevel(yioGdxGame, index);
                    }
                });
                break;
        }
    }


    private void loadLastEditorSlot() {
        System.out.println("OnKeyReactions.loadLastEditorSlot");
    }


    private void openCurrentLevelInEditor() {
        System.out.println("OnKeyReactions.openCurrentLevelInEditor");
    }


    private void launchQuickSimulation() {
        LpWorker lpWorker = new LpWorker(new Random());
        yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish, lpWorker.apply());
    }

}
