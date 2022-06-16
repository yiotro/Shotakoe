package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.PaType;
import yio.tro.shotakoe.game.general.Particle;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderParticles extends GameRender {

    HashMap<PaType, TextureRegion> mapTextures;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (PaType type : PaType.values()) {
            mapTextures.put(type, loadRoughTexture(type + ".png"));
        }
    }


    @Override
    public void render() {
        for (Particle particle : getObjectsLayer().particlesManager.particles) {
            if (!particle.isVisible()) continue;
            GraphicsYio.setBatchAlpha(batchMovable, particle.getAlpha());
            GraphicsYio.drawByCircle(batchMovable, mapTextures.get(particle.type), particle.position);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
