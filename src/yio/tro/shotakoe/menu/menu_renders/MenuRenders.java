package yio.tro.shotakoe.menu.menu_renders;

import yio.tro.shotakoe.menu.MenuViewYio;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.*;

import java.util.ArrayList;

public class MenuRenders {

    static ArrayList<RenderInterfaceElement> list = new ArrayList<>();

    public static RenderButton renderButton = new RenderButton();
    public static RenderCheckButton renderCheckButton = new RenderCheckButton();
    public static RenderScrollableArea renderScrollableArea = new RenderScrollableArea();
    public static RenderCircleButton renderCircleButton = new RenderCircleButton();
    public static RenderNotification renderNotification = new RenderNotification();
    public static RenderRoundShape renderRoundShape = new RenderRoundShape();
    public static RenderShadow renderShadow = new RenderShadow();
    public static RenderRoundBorder renderRoundBorder = new RenderRoundBorder();
    public static RenderViewTouchMode renderViewTouchMode = new RenderViewTouchMode();
    public static RenderCustomizableList renderCustomizableList = new RenderCustomizableList();
    public static RenderSingleListItem renderSingleListItem = new RenderSingleListItem();
    public static RenderForefinger renderForefinger = new RenderForefinger();
    public static RenderScrollListItem renderScrollListItem = new RenderScrollListItem();
    public static RenderLabelElement renderLabelElement = new RenderLabelElement();
    public static RenderToastElement renderToastElement = new RenderToastElement();
    public static RenderHorSampleItem renderHorSampleItem = new RenderHorSampleItem();
    public static RenderExceptionViewElement renderExceptionViewElement = new RenderExceptionViewElement();
    public static RenderBigTextItem renderBigTextItem = new RenderBigTextItem();
    public static RenderScrollHelperElement renderScrollHelperElement = new RenderScrollHelperElement();
    public static RenderAnnounceViewElement renderAnnounceViewElement = new RenderAnnounceViewElement();
    public static RenderSlider renderSlider = new RenderSlider();
    public static RenderTopCoverElement renderTopCoverElement = new RenderTopCoverElement();
    public static RenderCustomKeyboardElement renderCustomKeyboardElement = new RenderCustomKeyboardElement();
    public static RenderTitleListItem renderTitleListItem = new RenderTitleListItem();
    public static RenderDelayedActionElement renderDelayedActionElement = new RenderDelayedActionElement();
    public static RenderDarkenElement renderDarkenElement = new RenderDarkenElement();
    public static RenderNumberedSeparatorItem renderNumberedSeparatorItem = new RenderNumberedSeparatorItem();
    public static RenderPlotViewElement renderPlotViewElement = new RenderPlotViewElement();
    public static RenderConvex renderConvex = new RenderConvex();
    public static RenderCampaignCustomItem renderCampaignCustomItem = new RenderCampaignCustomItem();
    public static RenderSectionStartListItem renderSectionStartListItem = new RenderSectionStartListItem();
    public static RenderWantMoreItem renderWantMoreItem = new RenderWantMoreItem();
    public static RenderEasterEggElement renderEasterEggElement = new RenderEasterEggElement();
    public static RenderMonthListItem renderMonthListItem = new RenderMonthListItem();
    public static RenderGoalsViewElement renderGoalsViewElement = new RenderGoalsViewElement();
    public static RenderCaptureViewElement renderCaptureViewElement = new RenderCaptureViewElement();
    public static RenderTurnViewElement renderTurnViewElement = new RenderTurnViewElement();


    public static void updateRenderSystems(MenuViewYio menuViewYio) {
        for (RenderInterfaceElement renderInterfaceElement : list) {
            renderInterfaceElement.update(menuViewYio);
        }
    }
}
