package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderBackground extends GameRender{


    public TextureRegion backgroundTexture;


    @Override
    protected void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("game/background/main.png", false);
    }


    @Override
    public void render() {
        GraphicsYio.drawByRectangle(
                batchMovable,
                gameView.voidTexture,
                gameController.cameraController.frame
        );
        batchMovable.draw(
                backgroundTexture,
                0,
                0,
                gameController.sizeManager.boundWidth,
                gameController.sizeManager.boundHeight
        );
    }


    @Override
    protected void disposeTextures() {
        backgroundTexture.getTexture().dispose();
    }
}
