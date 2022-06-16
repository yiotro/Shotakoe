package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.elements.customizable_list.MonthListItem;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.calendar.CalendarManager;
import yio.tro.shotakoe.stuff.calendar.MliMonth;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneCalendar extends SceneYio {


    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.chooseGameMode));
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAppearParameters(MovementType.inertia, 0.8 * MenuParams.ANIM_SPEED)
                .setDestroyParameters(MovementType.lighty, 2 * MenuParams.ANIM_SPEED);
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        for (MliMonth mliMonth : CalendarManager.getInstance().months) {
            MonthListItem monthListItem = new MonthListItem();
            customizableListYio.addItem(monthListItem);
            monthListItem.setValues(mliMonth.year, mliMonth.monthIndex);
            monthListItem.loadStates();
        }
        customizableListYio.updateItemDeltas();
        doLockOlderMonths();
    }


    private void doLockOlderMonths() {
        boolean lock = false;
        for (AbstractCustomListItem item : customizableListYio.items) {
            MonthListItem monthListItem = (MonthListItem) item;
            if (!lock && monthListItem.hasAtLeastOneUnlockedDay()) {
                lock = true;
                continue;
            }
            if (lock) {
                monthListItem.lockEverything();
            }
        }
    }


    public void onCalendarDayCompleted() {
        for (AbstractCustomListItem item : customizableListYio.items) {
            MonthListItem monthListItem = (MonthListItem) item;
            monthListItem.loadStates();
        }
    }
}
