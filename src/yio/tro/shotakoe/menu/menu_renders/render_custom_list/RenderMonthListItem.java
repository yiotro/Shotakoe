package yio.tro.shotakoe.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.MliDayButton;
import yio.tro.shotakoe.menu.elements.customizable_list.MonthListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderMonthListItem extends AbstractRenderCustomListItem{

    HashMap<ColorYio, TextureRegion> mapColors;
    private TextureRegion completedTexture;
    private TextureRegion lockedTexture;
    private MonthListItem mlItem;
    private TextureRegion whitePixel;
    private TextureRegion unlockedTexture;


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (ColorYio color : ColorYio.values()) {
            mapColors.put(color, GraphicsYio.loadTextureRegion("menu/calendar/" + color + ".png", false));
        }
        completedTexture = GraphicsYio.loadTextureRegion("menu/calendar/completed.png", true);
        lockedTexture = GraphicsYio.loadTextureRegion("menu/calendar/calendar_locked.png", true);
        unlockedTexture = GraphicsYio.loadTextureRegion("menu/campaign/campaign_unlocked.png", true);
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        mlItem = (MonthListItem) item;
        GraphicsYio.setBatchAlpha(batch, 0.6 * alpha);
        GraphicsYio.drawByRectangle(batch, mapColors.get(mlItem.color), mlItem.viewPosition);
        GraphicsYio.drawByRectangle(batch, whitePixel, mlItem.titleBackground);
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, mlItem.title, alpha);
        for (MliDayButton dayButton : mlItem.dayButtons) {
            renderDayButton(dayButton);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderDayButton(MliDayButton dayButton) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        switch (dayButton.state) {
            default:
                System.out.println("RenderMonthListItem.renderDayButton");
                break;
            case locked:
                GraphicsYio.drawByCircle(batch, lockedTexture, dayButton.position);
                break;
            case unlocked:
                GraphicsYio.drawByCircle(batch, unlockedTexture, dayButton.position);
                GraphicsYio.renderTextOptimized(batch, blackPixel, dayButton.title, alpha);
                break;
            case completed:
                GraphicsYio.drawByCircle(batch, completedTexture, dayButton.position);
                break;
        }
        if (dayButton.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, dayButton.selectionEngineYio.getAlpha());
            GraphicsYio.drawByCircle(batch, blackPixel, dayButton.touchPosition);
        }
    }
}
