package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.game.general.*;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneEditPanel extends ModalSceneYio {


    private double dx;
    private double dy;
    private double bSize;
    private double offset;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.48);
        createInternals();
    }


    private void createInternals() {
        createManualEditButtons();
        createClearButton();
        createSimpleGenerationButton();
    }


    private void createSimpleGenerationButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.7, 0.05)
                .centerHorizontal()
                .alignUnder(previousElement, 0.01)
                .setTouchOffset(0.01)
                .setBackground(BackgroundYio.gray)
                .applyText("Simple generation")
                .setReaction(getSimpleGenerationReaction());
    }


    private Reaction getSimpleGenerationReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.setupSimpleGeneration.create();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        getGameController().resetTouchMode();
    }


    private void createClearButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.7, 0.05)
                .centerHorizontal()
                .alignTop(0.22)
                .setTouchOffset(0.01)
                .setBackground(BackgroundYio.gray)
                .applyText("Clear")
                .setReaction(getClearReaction());
    }


    private Reaction getClearReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onClearButtonPressed();
            }
        };
    }


    public void onClearButtonPressed() {
        getObjectsLayer().clearForLevelGeneration();
        getObjectsLayer().cellField.deactivateEverything();
        getObjectsLayer().simulationCacheManager.update();
    }


    private void createManualEditButtons() {
        offset = 0.02;
        bSize = 0.105;
        dx = offset;
        dy = offset;
        createSingleButton("menu/check_button/chk_def.png", getTmEditActiveReaction());
        for (ColorYio color : ColorYio.values()) {
            createSingleButton("game/atlas/" + color + ".png", getColorReaction(color));
        }
        createCellTypeButton(CellType.human_start);
        createCellTypeButton(CellType.enemy_start);
        createCellTypeButton(CellType.bomb);
    }


    private void createCellTypeButton(CellType cellType) {
        createSingleButton("game/atlas/indicator_" + cellType + ".png", getEditCellTypeReaction(cellType));
    }


    private Reaction getEditCellTypeReaction(final CellType cellType) {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                TouchMode.tmEditCellType.setCellType(cellType);
                gameController.setTouchMode(TouchMode.tmEditCellType);
            }
        };
    }


    private Reaction getColorReaction(final ColorYio color) {
        return new Reaction() {
            @Override
            protected void apply() {
                gameController.setTouchMode(TouchMode.tmEditColor);
                TouchMode.tmEditColor.setColor(color);
                destroy();
            }
        };
    }


    private Reaction getTmEditActiveReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                gameController.setTouchMode(TouchMode.tmEditActive);
                destroy();
            }
        };
    }


    private void createSingleButton(String path, Reaction reaction) {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(bSize)
                .alignTop(dy)
                .alignLeft(dx)
                .setTouchOffset(offset / 2)
                .loadCustomTexture(path)
                .setReaction(reaction);

        dx += offset + bSize;
        if (dx > 0.9 - offset - bSize) {
            dx = offset;
            dy += GraphicsYio.convertToHeight(offset + bSize);
        }
    }
}
