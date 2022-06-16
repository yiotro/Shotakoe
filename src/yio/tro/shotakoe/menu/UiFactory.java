package yio.tro.shotakoe.menu;

import yio.tro.shotakoe.menu.elements.*;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.elements.easter_egg.EasterEggElement;
import yio.tro.shotakoe.menu.elements.forefinger.ForefingerElement;
import yio.tro.shotakoe.menu.elements.gameplay.CaptureViewElement;
import yio.tro.shotakoe.menu.elements.gameplay.GoalViewElement;
import yio.tro.shotakoe.menu.elements.gameplay.TurnViewElement;
import yio.tro.shotakoe.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.shotakoe.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.shotakoe.menu.elements.plot_view.PlotViewElement;
import yio.tro.shotakoe.menu.scenes.SceneYio;

public class UiFactory {

    MenuControllerYio menuControllerYio;
    SceneYio sceneYio;


    public UiFactory(SceneYio sceneYio) {
        this.sceneYio = sceneYio;
        menuControllerYio = sceneYio.menuControllerYio;
    }


    public ButtonYio getButton() {
        ButtonYio buttonYio = new ButtonYio(menuControllerYio);
        addElementToScene(buttonYio);

        return buttonYio;
    }


    public CheckButtonYio getCheckButton() {
        return (CheckButtonYio) addElementToScene(new CheckButtonYio(menuControllerYio));
    }


    public CircleButtonYio getCircleButton() {
        return (CircleButtonYio) addElementToScene(new CircleButtonYio(menuControllerYio));
    }


    public ScrollableAreaYio getScrollableAreaYio() {
        return (ScrollableAreaYio) addElementToScene(new ScrollableAreaYio(menuControllerYio));
    }


    public NotificationElement getNotificationElement() {
        return (NotificationElement) addElementToScene(new NotificationElement(menuControllerYio));
    }


    public ViewTouchModeElement getViewTouchModeElement() {
        return (ViewTouchModeElement) addElementToScene(new ViewTouchModeElement(menuControllerYio));
    }


    public CustomizableListYio getCustomizableListYio() {
        CustomizableListYio customizableListYio = new CustomizableListYio(menuControllerYio);
        return (CustomizableListYio) addElementToScene(customizableListYio);
    }


    public ForefingerElement getForefingerElement() {
        return (ForefingerElement) addElementToScene(new ForefingerElement(menuControllerYio));
    }


    public LabelElement getLabelElement() {
        return (LabelElement) addElementToScene(new LabelElement(menuControllerYio));
    }


    public ToastElement getToastElement() {
        return (ToastElement) addElementToScene(new ToastElement(menuControllerYio));
    }


    public ExceptionViewElement getExceptionViewElement() {
        return (ExceptionViewElement) addElementToScene(new ExceptionViewElement(menuControllerYio));
    }


    public AnnounceViewElement getAnnounceViewElement() {
        return (AnnounceViewElement) addElementToScene(new AnnounceViewElement(menuControllerYio));
    }


    public ScrollHelperElement getScrollHelperElement() {
        return (ScrollHelperElement) addElementToScene(new ScrollHelperElement(menuControllerYio));
    }


    public SliderElement getSlider() {
        return (SliderElement) addElementToScene(new SliderElement(menuControllerYio));
    }


    public TopCoverElement getTopCoverElement() {
        return (TopCoverElement) addElementToScene(new TopCoverElement(menuControllerYio));
    }


    public CustomKeyboardElement getCustomKeyboardElement() {
        return (CustomKeyboardElement) addElementToScene(new CustomKeyboardElement(menuControllerYio));
    }


    public DelayedActionElement getDelayedActionElement() {
        return (DelayedActionElement) addElementToScene(new DelayedActionElement(menuControllerYio));
    }


    public DarkenElement getDarkenElement() {
        return (DarkenElement) addElementToScene(new DarkenElement(menuControllerYio));
    }


    public PlotViewElement getPlotViewElement() {
        return (PlotViewElement) addElementToScene(new PlotViewElement(menuControllerYio));
    }


    public EasterEggElement getEasterEggElement() {
        return (EasterEggElement) addElementToScene(new EasterEggElement(menuControllerYio));
    }


    public GoalViewElement getGoalViewElement() {
        return (GoalViewElement) addElementToScene(new GoalViewElement(menuControllerYio));
    }


    public CaptureViewElement getCaptureViewElement() {
        return (CaptureViewElement) addElementToScene(new CaptureViewElement(menuControllerYio));
    }


    public TurnViewElement getTurnViewElement() {
        return (TurnViewElement) addElementToScene(new TurnViewElement(menuControllerYio));
    }


    private InterfaceElement addElementToScene(InterfaceElement interfaceElement) {
        sceneYio.addLocalElement(interfaceElement);
        menuControllerYio.addElement(interfaceElement);
        return interfaceElement;
    }
}
