package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderSimulationCache extends GameRender{

    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        TextureRegion textureRegion = getObjectsLayer().simulationCacheManager.textureRegion;
        if (textureRegion == null) return;
        GraphicsYio.drawByRectangle(
                batchMovable,
                textureRegion,
                getObjectsLayer().gameController.sizeManager.position
        );
    }


    @Override
    protected void disposeTextures() {

    }
}
