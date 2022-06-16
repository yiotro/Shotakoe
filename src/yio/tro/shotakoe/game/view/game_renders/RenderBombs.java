package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.BombsManager;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class RenderBombs extends GameRender{


    private TextureRegion bombTexture;
    CircleYio circleYio;
    private TextureRegion xTexture;


    public RenderBombs() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        bombTexture = GraphicsYio.loadTextureRegion("game/atlas/bomb.png", true);
        xTexture = GraphicsYio.loadTextureRegion("game/debug/x.png", true);
    }


    @Override
    public void render() {
        BombsManager bombsManager = getObjectsLayer().bombsManager;
        if (getObjectsLayer().bombsManager.explosionHappened) return;
        for (Cell cell : bombsManager.bombCells) {
            circleYio.setBy(cell.innerPosition);
            circleYio.setRadius(0.75f * cell.innerPosition.radius);
            GraphicsYio.drawByCircle(batchMovable, bombTexture, circleYio);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 0.25);
        for (Cell cell : bombsManager.triggerCells) {
            circleYio.setBy(cell.position);
            circleYio.radius = 0.8f * cell.innerPosition.radius;
            GraphicsYio.drawByCircle(batchMovable, xTexture, circleYio);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {
        bombTexture.getTexture().dispose();
    }
}
