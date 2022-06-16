package yio.tro.shotakoe.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.ConditionYio;
import yio.tro.shotakoe.menu.scenes.ModalSceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneMechanicsOverlay extends ModalSceneYio {


    private TextureRegion selectionTexture;
    private double bSize;
    private double touchOffset;


    public SceneMechanicsOverlay() {

    }


    @Override
    protected void initialize() {
        loadTextures();
        bSize = GraphicsYio.convertToWidth(0.05);
        touchOffset = 0.06;
        createSuperUserButton();
//        createEditButton();
        createGoalView();
        createCaptureView();
        createTurnView();
    }


    private void createTurnView() {
        uiFactory.getTurnViewElement()
                .setSize(0.5, 0.04)
                .alignBottom(0)
                .alignRight(0)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.inertia, 1);
    }


    private void createCaptureView() {
        uiFactory.getCaptureViewElement()
                .setSize(0.5, 0.04)
                .alignBottom(0)
                .alignLeft(0)
                .setAnimation(AnimationYio.down)
                .setAppearParameters(MovementType.inertia, 1);
    }


    private void createGoalView() {
        uiFactory.getGoalViewElement()
                .setSize(0.9, 0.06)
                .alignTop(0)
                .alignLeft(0)
                .setAnimation(AnimationYio.up)
                .setAppearParameters(MovementType.inertia, 1);
    }


    private void createEditButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignLeft(0)
                .alignBottom(0)
                .setTouchOffset(0.05)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("plus"))
                .setReaction(getOpenSceneReaction(Scenes.editPanel))
                .setAllowedToAppear(getSuperUserCondition())
                .setSelectionTexture(selectionTexture);
    }


    private void createSuperUserButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignLeft(0)
                .alignTop(0.1)
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.left)
                .setCustomTexture(getTextureFromAtlas("open"))
                .setReaction(getOpenSceneReaction(Scenes.debugPanel))
                .setAllowedToAppear(getSuperUserCondition())
                .setSelectionTexture(selectionTexture);
    }


    private ConditionYio getSuperUserCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return DebugFlags.superUserEnabled;
            }
        };
    }


    private void loadTextures() {
        selectionTexture = getSelectionTexture();
    }

}
