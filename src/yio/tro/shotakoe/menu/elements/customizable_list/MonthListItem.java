package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.general.LpWorker;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.calendar.CalendarManager;
import yio.tro.shotakoe.stuff.calendar.CmmItem;

import java.util.ArrayList;
import java.util.Random;

public class MonthListItem extends AbstractCustomListItem {

    public int year;
    public int monthIndex;
    public int daysQuantity;
    public ColorYio color;
    public String nameKey;
    public RenderableTextYio title;
    private float height;
    public ArrayList<MliDayButton> dayButtons;
    private int calculatedNumberOfRows;
    private float r;
    private MliDayButton targetButton;
    public RectangleYio titleBackground;


    public MonthListItem() {
        dayButtons = new ArrayList<>();
    }


    public void setValues(int year, int monthIndex) {
        this.year = year;
        this.monthIndex = monthIndex;
        titleBackground = new RectangleYio();
        updateNameKey();
        initTitle();
        updateColor();
        updateDaysQuantity();
        initDayButtons(); // to calculate number of rows
        updateHeight();
        initDayButtons(); // yes, it should be called once again
    }


    private void initDayButtons() {
        dayButtons.clear();
        int rowQuantity = 7;
        float delta = 0.04f * GraphicsYio.width;
        float parentWidth = customizableListYio.getPosition().width;
        float rowWidth = parentWidth - 2 * delta;
        r = (rowWidth / rowQuantity) / 2;
        int weeklyIndex = CalendarManager.getInstance().getWeeklyIndex(year, monthIndex, 1);
        float x = delta + r + 2 * r * weeklyIndex;
        float topOffset = 0.08f * GraphicsYio.height;
        float y = (float) (height - topOffset - GraphicsYio.convertToHeight(r));
        calculatedNumberOfRows = 1;
        for (int i = 1; i <= daysQuantity; i++) {
            MliDayButton cveDayButton = new MliDayButton(this);
            cveDayButton.setIndex(i);
            cveDayButton.position.radius = 0.8f * r;
            cveDayButton.touchPosition.radius = r;
            cveDayButton.delta.set(x, y);
            dayButtons.add(cveDayButton);
            x += 2 * r;
            if (x > parentWidth - delta) {
                if (i < daysQuantity) { // not last day
                    calculatedNumberOfRows++;
                }
                x = delta + r;
                y -= 2 * r;
            }
        }
        moveDayButtons();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString(LanguagesManager.getInstance().getString(nameKey) + " " + year);
        title.updateMetrics();
    }


    private void updateHeight() {
        height = 0.09f * GraphicsYio.height + 2 * r * calculatedNumberOfRows;
    }


    private void updateDaysQuantity() {
        daysQuantity = CalendarManager.getInstance().getDaysQuantity(year, monthIndex);
    }


    private void updateColor() {
        ColorYio[] values = new ColorYio[]{
                ColorYio.yellow,
                ColorYio.blue,
                ColorYio.green,
                ColorYio.brown,
                ColorYio.aqua,
                ColorYio.red,
                ColorYio.cyan,
                ColorYio.purple,
        };
        int path = 12 * (year - 2019) + monthIndex - 1;
        int index = path % values.length;
        color = values[index];
    }


    void updateNameKey() {
        nameKey = getMonthNameKey(monthIndex);
    }


    public static String getMonthNameKey(int monthIndex) {
        switch (monthIndex) {
            default:
                return "unknown";
            case 1:
                return "january";
            case 2:
                return "february";
            case 3:
                return "march";
            case 4:
                return "april";
            case 5:
                return "may";
            case 6:
                return "june";
            case 7:
                return "july";
            case 8:
                return "august";
            case 9:
                return "september";
            case 10:
                return "october";
            case 11:
                return "november";
            case 12:
                return "december";
        }
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected void move() {
        viewPosition.x = customizableListYio.getPosition().x;
        viewPosition.width = customizableListYio.getPosition().width;
        updateTitlePosition();
        updateTitleBackgroundPosition();
        moveDayButtons();
    }


    private void updateTitleBackgroundPosition() {
        titleBackground.setBy(title.bounds);
        titleBackground.increase(0.015f * GraphicsYio.width);
    }


    private void moveDayButtons() {
        for (MliDayButton mliDayButton : dayButtons) {
            mliDayButton.move();
        }
    }


    public void loadStates() {
        CalendarManager calendarManager = CalendarManager.getInstance();
        CmmItem cmmItem = calendarManager.getItem(year, monthIndex);
        for (MliDayButton mliDayButton : dayButtons) {
            if (calendarManager.isLocked(year, monthIndex, mliDayButton.index)) {
                mliDayButton.setState(MliDayState.locked);
                continue;
            }
            if (cmmItem != null && cmmItem.isCompleted(mliDayButton.index)) {
                mliDayButton.setState(MliDayState.completed);
                continue;
            }
            mliDayButton.setState(MliDayState.unlocked);
        }
    }


    public void lockEverything() {
        for (MliDayButton mliDayButton : dayButtons) {
            mliDayButton.setState(MliDayState.locked);
        }
    }


    public boolean hasAtLeastOneUnlockedDay() {
        for (MliDayButton mliDayButton : dayButtons) {
            if (mliDayButton.state == MliDayState.unlocked) return true;
        }
        return false;
    }


    MliDayButton getCurrentlyTouchedButton(PointYio touchPoint) {
        if (!isCurrentlyVisible()) return null;
        for (MliDayButton mliDayButton : dayButtons) {
            if (!mliDayButton.isTouchedBy(touchPoint)) continue;
            return mliDayButton;
        }
        return null;
    }


    boolean areAllDaysTaggedAsCompleted() {
        for (MliDayButton cveDayButton : dayButtons) {
            if (cveDayButton.state == MliDayState.completed) continue;
            return false;
        }
        return true;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + viewPosition.height - 0.035f * GraphicsYio.height;
        title.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    public void onTouchDown(PointYio touchPoint) {
        targetButton = getCurrentlyTouchedButton(touchPoint);
        if (targetButton == null) return;
        targetButton.selectionEngineYio.applySelection();
    }


    @Override
    protected void onClicked() {
        if (targetButton == null) return;
        SoundManager.playSound(SoundType.button);
        if (targetButton.state == MliDayState.locked) return;
        Random random = new Random(13 * year + 137 * monthIndex + 239 * targetButton.index);
        LpWorker lpWorker = new LpWorker(random);
        LoadingParameters loadingParameters = lpWorker.apply();
        loadingParameters.setYear(year);
        loadingParameters.setMonth(monthIndex);
        loadingParameters.setDay(targetButton.index);
        YioGdxGame yioGdxGame = customizableListYio.menuControllerYio.yioGdxGame;
        yioGdxGame.loadingManager.startInstantly(LoadingType.calendar, loadingParameters);
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderMonthListItem;
    }
}
