package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.AnnounceViewElement;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.elements.customizable_list.HorSampleItem;
import yio.tro.shotakoe.menu.elements.customizable_list.MonthListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.ScrollListItem;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RepeatYio;
import yio.tro.shotakoe.stuff.calendar.CalendarManager;
import yio.tro.shotakoe.stuff.calendar.MliMonth;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneTestScreen extends SceneYio {


    private LabelElement labelElement;
    int counter;
    RepeatYio<SceneTestScreen> repeatCount;
    private AnnounceViewElement mainLabel;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createInternals();
        createBackButton();
        initRepeats();
    }


    private void createInternals() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);

        for (MliMonth mliMonth : CalendarManager.getInstance().months) {
            MonthListItem monthListItem = new MonthListItem();
            customizableListYio.addItem(monthListItem);
            monthListItem.setValues(mliMonth.year, mliMonth.monthIndex);
            monthListItem.loadStates();
        }
        customizableListYio.updateItemDeltas();
    }


    private void createMainLabel() {
        double h = 0.5;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAppearParameters(MovementType.soft_rubber_band, 1.5)
                .setTitle("Test screen")
                .setKey("main_label")
                .setText(" ")
                .setTouchable(false);
    }


    private void createSliderTest() {
        ButtonYio buttonYio = uiFactory.getButton()
                .setSize(0.9, 0.4)
                .centerVertical()
                .centerHorizontal()
                .applyText(" ")
                .setTouchable(false);

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 1")
                .centerHorizontal()
                .alignTop(0.05)
                .setPossibleValues(new int[]{0, 1});

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 2")
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setPossibleValues(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

        uiFactory.getSlider()
                .setParent(buttonYio)
                .setTitle("Slider 3")
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setPossibleValues(LevelSize.class);
    }


    private void initRepeats() {
        repeatCount = new RepeatYio<SceneTestScreen>(this, 6) {
            @Override
            public void performAction() {
                parent.count();
            }
        };
    }


    private void createColoredList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.7, 0.7)
                .centerHorizontal()
                .alignBottom(0.1);

        for (int i = 0; i < 16; i++) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setHeight(0.08f * GraphicsYio.height);
            scrollListItem.setTitle("Item " + (i + 1));
            customizableListYio.addItem(scrollListItem);
        }
    }


    @Override
    public void move() {
        super.move();
    }


    private void count() {
        if (labelElement == null) return;
        counter++;
        System.out.println("counter = " + counter);
        labelElement.setString("" + counter);
    }


    private void createHorizontalList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, GraphicsYio.convertToHeight(0.9))
                .centerHorizontal()
                .centerVertical()
                .setHorizontalMode(true)
                .setAnimation(AnimationYio.center);

        for (int i = 0; i < 5; i++) {
            HorSampleItem horSampleItem = new HorSampleItem();
            customizableListYio.addItem(horSampleItem);
        }
    }


    private void createBackButton() {
        spawnBackButton(getOpenSceneReaction(Scenes.secretScreen));
    }


}
