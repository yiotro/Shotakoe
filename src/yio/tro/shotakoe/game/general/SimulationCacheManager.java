package yio.tro.shotakoe.game.general;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import yio.tro.shotakoe.game.view.GameView;
import yio.tro.shotakoe.game.view.game_renders.GameRendersList;
import yio.tro.shotakoe.stuff.FrameBufferYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SimulationCacheManager {

    ObjectsLayer objectsLayer;
    FrameBufferYio frameBufferYio;
    public TextureRegion textureRegion;
    Matrix4 matrix4;


    public SimulationCacheManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        createMatrix();
        textureRegion = null;
        createFrameBuffer();
    }


    private void createFrameBuffer() {
        frameBufferYio = FrameBufferYio.getInstance(
                Pixmap.Format.RGB565,
                (int) (GraphicsYio.width),
                (int) (GraphicsYio.height),
                false
        );
    }


    public void update() {
        GameView gameView = objectsLayer.gameController.yioGdxGame.gameView;
        SpriteBatch batchMovable = gameView.batchMovable;
        frameBufferYio.begin();
        batchMovable.begin();
        batchMovable.setProjectionMatrix(matrix4);
        applyGameRenders();
        checkToCreateTextureRegion();
        batchMovable.end();
        frameBufferYio.end();
        batchMovable.setProjectionMatrix(gameView.orthoCam.combined);
    }


    private void applyGameRenders() {
        GameRendersList instance = GameRendersList.getInstance();
        instance.renderBackground.render();
        instance.renderFieldRaw.render();
        instance.renderFiguresOptimized.render();
    }


    void checkToCreateTextureRegion() {
        if (textureRegion != null) return;
        textureRegion = new TextureRegion(
                frameBufferYio.getColorBufferTexture(),
                (int) (GraphicsYio.width),
                (int) (GraphicsYio.height)
        );
        textureRegion.flip(false, true);
    }


    void createMatrix() {
        matrix4 = new Matrix4();
        matrix4.setToOrtho2D(
                0,
                0,
                GraphicsYio.width,
                GraphicsYio.height
        );
    }
}
