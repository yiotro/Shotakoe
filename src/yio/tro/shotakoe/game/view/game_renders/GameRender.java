package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.GameController;
import yio.tro.shotakoe.game.general.ObjectsLayer;
import yio.tro.shotakoe.game.view.GameView;
import yio.tro.shotakoe.stuff.AtlasLoader;

public abstract class GameRender {

    protected GameView gameView;
    protected GameController gameController;
    protected SpriteBatch batchMovable, batchSolid;
    protected AtlasLoader roughAtlas;
    protected AtlasLoader smoothAtlas;
    float w, h;


    public GameRender() {
        GameRendersList.getInstance().gameRenders.listIterator().add(this);
    }


    void update(GameView gameView) {
        this.gameView = gameView;
        gameController = gameView.gameController;
        batchMovable = gameView.batchMovable;
        batchSolid = gameView.batchSolid;
        w = gameView.w;
        h = gameView.h;
        roughAtlas = gameView.roughAtlas;
        smoothAtlas = gameView.smoothAtlas;
        loadTextures();
    }


    public static void disposeAllTextures() {
        for (GameRender gameRender : GameRendersList.getInstance().gameRenders) {
            gameRender.disposeTextures();
        }
    }


    protected abstract void loadTextures();


    public abstract void render();


    protected abstract void disposeTextures();


    protected TextureRegion loadRoughTexture(String fileName) {
        return roughAtlas.getTexture(fileName);
    }


    protected TextureRegion loadSmoothTexture(String fileName) {
        return smoothAtlas.getTexture(fileName);
    }


    protected int getCurrentZoomQuality() {
        return gameView.currentZoomQuality;
    }


    public ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    public TextureRegion getBlackPixel() {
        return gameView.blackPixel;
    }
}
