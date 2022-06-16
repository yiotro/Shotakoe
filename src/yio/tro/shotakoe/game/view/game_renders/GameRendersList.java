package yio.tro.shotakoe.game.view.game_renders;

import yio.tro.shotakoe.game.view.GameView;
import yio.tro.shotakoe.game.view.game_renders.tm_renders.*;

import java.util.ArrayList;

public class GameRendersList {

    private static GameRendersList instance;
    ArrayList<GameRender> gameRenders = new ArrayList<>();

    public RenderTmDefault renderTmDefault;
    public RenderBackground renderBackground;
    public RenderDebugCells renderDebugCells;
    public RenderFieldRaw renderFieldRaw;
    public RenderSimulationCache renderSimulationCache;
    public RenderFiguresRaw renderFiguresRaw;
    public RenderFiguresOptimized renderFiguresOptimized;
    public RenderParticles renderParticles;
    public RenderTutorialHints renderTutorialHints;
    public RenderExternalIndicators renderExternalIndicators;
    public RenderBombs renderBombs;
    public RenderRecolorAnimation renderRecolorAnimation;
    public RenderColorButtons renderColorButtons;
    public RenderAlgoClusters renderAlgoClusters;
    // initialize them lower


    public GameRendersList() {
        //
    }


    public static GameRendersList getInstance() {
        if (instance == null) {
            instance = new GameRendersList();
            instance.createAllRenders();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void updateGameRenders(GameView gameView) {
        for (GameRender gameRender : gameRenders) {
            gameRender.update(gameView);
        }
    }


    private void createAllRenders() {
        renderTmDefault = new RenderTmDefault();
        renderBackground = new RenderBackground();
        renderDebugCells = new RenderDebugCells();
        renderFieldRaw = new RenderFieldRaw();
        renderSimulationCache = new RenderSimulationCache();
        renderFiguresRaw = new RenderFiguresRaw();
        renderFiguresOptimized = new RenderFiguresOptimized();
        renderParticles = new RenderParticles();
        renderTutorialHints = new RenderTutorialHints();
        renderExternalIndicators = new RenderExternalIndicators();
        renderBombs = new RenderBombs();
        renderRecolorAnimation = new RenderRecolorAnimation();
        renderColorButtons = new RenderColorButtons();
        renderAlgoClusters = new RenderAlgoClusters();
    }
}
