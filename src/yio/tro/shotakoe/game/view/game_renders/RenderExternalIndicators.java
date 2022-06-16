package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ExternalIndicator;
import yio.tro.shotakoe.game.general.ExternalIndicatorsManager;
import yio.tro.shotakoe.game.general.IndicatorType;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderExternalIndicators extends GameRender{

    HashMap<IndicatorType, TextureRegion> mapTextures;
    private ExternalIndicatorsManager externalIndicatorsManager;


    @Override
    protected void loadTextures() {
        mapTextures = new HashMap<>();
        for (IndicatorType indicatorType : IndicatorType.values()) {
            mapTextures.put(indicatorType, GraphicsYio.loadTextureRegion("game/atlas/indicator_" + indicatorType + ".png", true));
        }
    }


    @Override
    public void render() {
        externalIndicatorsManager = getObjectsLayer().externalIndicatorsManager;
        for (ExternalIndicator indicator : externalIndicatorsManager.indicators) {
            GraphicsYio.drawByCircle(
                    batchMovable,
                    mapTextures.get(indicator.type),
                    indicator.position
            );
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
