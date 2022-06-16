package yio.tro.shotakoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class FpsRenderer {

    YioGdxGame yioGdxGame;
    RenderableTextYio title;
    RectangleYio incBounds;
    private final TextureRegion whitePixel;
    private SpriteBatch batch;


    public FpsRenderer(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        batch = yioGdxGame.batch;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.position.x = 0.02f * GraphicsYio.width;
        title.position.y = 0.95f * GraphicsYio.height;
        incBounds = new RectangleYio();
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    void render() {
        if (!DebugFlags.showFps) return;
        updateTitle();
        updateIncBounds();
        batch.begin();
        renderBackground();
        GraphicsYio.renderText(batch, title);
        batch.end();
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, 0.25);
        GraphicsYio.drawByRectangle(batch, whitePixel, incBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private void updateTitle() {
        int framesPerSecond = Gdx.graphics.getFramesPerSecond();
        title.setString(framesPerSecond + " [" + RefreshRateDetector.getInstance().refreshRate + "]");
        title.updateMetrics();
        title.updateBounds();
    }
}

