package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CampaignCustomItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CciInnerItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CciType;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderCampaignCustomItem extends AbstractRenderCustomListItem{


    private CampaignCustomItem campaignCustomItem;
    HashMap<CciType, TextureRegion> mapTextures;
    private float alpha;
    HashMap<ColorYio, TextureRegion> mapColors;


    @Override
    public void loadTextures() {
        mapTextures = new HashMap<>();
        for (CciType cciType : CciType.values()) {
            mapTextures.put(cciType, GraphicsYio.loadTextureRegion("menu/campaign/campaign_" + cciType + ".png", true));
        }
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, GraphicsYio.loadTextureRegion("menu/campaign/" + color + ".png", false));
        }
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        campaignCustomItem = (CampaignCustomItem) item;
        alpha = campaignCustomItem.customizableListYio.getAlpha();

//        GraphicsYio.setBatchAlpha(batch, 0.6 * alpha);
//        GraphicsYio.drawByRectangle(
//                batch,
//                mapColors.get(campaignCustomItem.color),
//                campaignCustomItem.viewPosition
//        );
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (CciInnerItem cciInnerItem : campaignCustomItem.items) {
            renderSingleInnerItem(cciInnerItem);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSingleInnerItem(CciInnerItem cciInnerItem) {
        GraphicsYio.drawByRectangle(batch, mapTextures.get(cciInnerItem.type), cciInnerItem.position);

        if (cciInnerItem.type == CciType.unlocked || DebugFlags.alwaysShowCampaignNumbers) {
            GraphicsYio.renderTextOptimized(batch, blackPixel, cciInnerItem.title, alpha);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }

        if (cciInnerItem.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, cciInnerItem.selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, cciInnerItem.selectionPosition);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
    }
}
