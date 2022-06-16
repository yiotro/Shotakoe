package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public abstract class AbstractConfirmationScene extends ModalSceneYio{

    protected LabelElement questionLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.18);
        defaultPanel.setAppearParameters(MovementType.soft_big_rubber_band, 1.5 * MenuParams.ANIM_SPEED);
        createLabel();
        createButtons();
    }


    private void createButtons() {
        double w = 0.2;
        double delta = (1 - 2 * w) / 3;
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(w, 0.06)
                .alignLeft(delta)
                .setTouchOffset(0.05)
                .alignUnder(questionLabel, 0.015)
                .setBackground(BackgroundYio.gray)
                .setAppearParameters(MovementType.soft_big_rubber_band, 1.5 * MenuParams.ANIM_SPEED)
                .setConvex(false)
                .setKey("no")
                .applyText("no")
                .setReaction(getNoReaction());

        uiFactory.getButton()
                .setParent(previousElement)
                .clone(previousElement)
                .alignRight(delta)
                .alignUnder(questionLabel, 0.015)
                .setAppearParameters(MovementType.soft_big_rubber_band, 1.5 * MenuParams.ANIM_SPEED)
                .applyText("yes")
                .setReaction(getYesReaction());
    }


    protected abstract Reaction getNoReaction();


    protected abstract Reaction getYesReaction();


    @Override
    protected void onAppear() {
        super.onAppear();
        updateQuestionLabel();
    }


    private void updateQuestionLabel() {
        questionLabel.setTitle(languagesManager.getString(getQuestionKey()));
    }


    protected abstract String getQuestionKey();


    private void createLabel() {
        questionLabel = uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.06)
                .alignTop(0.015)
                .centerHorizontal()
                .setAppearParameters(MovementType.soft_big_rubber_band, 1.5 * MenuParams.ANIM_SPEED)
                .setFont(Fonts.gameFont)
                .setTitle("-");
    }


}
