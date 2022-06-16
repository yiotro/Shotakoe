package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.game.general.Difficulty;
import yio.tro.shotakoe.game.generator.LevelGenerator;
import yio.tro.shotakoe.game.generator.LgParameters;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.SliderElement;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;

import java.util.Random;

public class SceneSetupSimpleGeneration extends ModalSceneYio {


    private SliderElement sliderDifficulty;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.22);
        createInternals();
        createGenerateButton();
        sliderDifficulty.setValueIndex(3);
    }


    private void createGenerateButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.5, 0.055)
                .centerHorizontal()
                .alignBottom(0.025)
                .setBackground(BackgroundYio.gray)
                .applyText("Generate")
                .setTouchOffset(0.02)
                .setReaction(getGenerateReaction());
    }


    private Reaction getGenerateReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onGenerateButtonPressed();
            }
        };
    }


    private void onGenerateButtonPressed() {
        destroy();
        Difficulty difficulty = Difficulty.values()[sliderDifficulty.getValueIndex()];
        Random random = new Random();
        //
        (new LevelGenerator(getObjectsLayer())).apply(new LgParameters(), random);
    }


    private void createInternals() {
        sliderDifficulty = uiFactory.getSlider()
                .setParent(defaultPanel)
                .alignTop(0.025)
                .centerHorizontal()
                .setTitle("Difficulty")
                .setPossibleValues(Difficulty.class);
    }
}
