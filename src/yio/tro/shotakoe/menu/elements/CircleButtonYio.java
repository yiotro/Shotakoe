
package yio.tro.shotakoe.menu.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.SoundManager;
import yio.tro.shotakoe.SoundType;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.JumpEngineYio;
import yio.tro.shotakoe.stuff.factor_yio.FactorYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class CircleButtonYio extends InterfaceElement<CircleButtonYio> {

    public FactorYio selectionFactor;
    private boolean needToPerformAction;
    private long timeToPerformAction;
    public TextureRegion textureRegion;
    float touchOffset;
    float effectDeltaRadius;
    Reaction reaction;
    String texturePath;
    public CircleYio renderPosition;
    public CircleYio effectPosition;
    private JumpEngineYio jumpEngineYio;


    public CircleButtonYio(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        selectionFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        needToPerformAction = false;
        timeToPerformAction = 0;
        textureRegion = null;
        reaction = Reaction.rbNothing;
        renderPosition = new CircleYio();
        effectPosition = new CircleYio();
        effectDeltaRadius = 0.1f * GraphicsYio.width;
        texturePath = null;

        setAnimation(MenuParams.DEF_ANIMATION_TYPE);
    }


    @Override
    protected CircleButtonYio getThis() {
        return this;
    }


    @Override
    public void onMove() {
        selectionFactor.move();
        jumpEngineYio.move();
        updateRenderPosition();
        updateEffectPosition();
    }


    private void updateEffectPosition() {
        effectPosition.center.setBy(renderPosition.center);
        effectPosition.radius = 0.5f * viewPosition.width + jumpEngineYio.getValue() * effectDeltaRadius;
    }


    private void updateRenderPosition() {
        renderPosition.center.x = viewPosition.x + viewPosition.width / 2;
        renderPosition.center.y = viewPosition.y + viewPosition.height / 2;
        renderPosition.radius = 0.5f * viewPosition.height + jumpEngineYio.getValue() * effectDeltaRadius;
    }


    public boolean isSelected() {
        return selectionFactor.getProgress() > 0;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        jumpEngineYio.reset();
        selectionFactor.setValues(0, 0);
        selectionFactor.stop();
    }


    @Override
    public boolean checkToPerformAction() {
        if (needToPerformAction && System.currentTimeMillis() > timeToPerformAction) {
            needToPerformAction = false;
            reaction();
            if (isReturningBackButton()) {
                menuControllerYio.onReturningBackButtonPressed();
            }
            return true;
        }

        return false;
    }


    private void reaction() {
        reaction.perform(menuControllerYio);
    }


    @Override
    public void pressArtificially(int keycode) {
        super.pressArtificially(keycode);
        press();
    }


    @Override
    public boolean touchDown() {
        if (currentTouch.distanceTo(renderPosition.center) < viewPosition.width / 2 + touchOffset) {
            press();
            return true;
        }

        return false;
    }


    public void press() {
        if (getFactor().getProgress() < 0.7) return;
        if (appearFactor.isInDestroyState()) return;

        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(MovementType.lighty, 1.1);
        jumpEngineYio.apply(1, 0.45);

        playSound();
        menuControllerYio.yioGdxGame.render();
        needToPerformAction = true;
        timeToPerformAction = System.currentTimeMillis() + 100;
    }


    private void playSound() {
        // special sound for buttons that return back
        if (isReturningBackButton()) {
            SoundManager.playSound(SoundType.back);
            return;
        }

        SoundManager.playSound(SoundType.button);
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        if (selectionFactor.getValue() > 0) return true;
        return false;
    }


    public CircleButtonYio setReaction(Reaction reaction) {
        this.reaction = reaction;
        return getThis();
    }


    @Override
    public CircleButtonYio clone(InterfaceElement src) {
        super.clone(src);

        CircleButtonYio srcButton = (CircleButtonYio) src;
        touchOffset = srcButton.touchOffset;

        setAnimation(src.animationType);

        return getThis();
    }


    public CircleButtonYio loadTexture(String path) {
        textureRegion = GraphicsYio.loadTextureRegion(path, true);
        texturePath = path;

        return this;
    }


    public CircleButtonYio setTouchOffset(double offset) {
        touchOffset = (float) (offset * GraphicsYio.width);

        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCircleButton;
    }


    @Override
    public String toString() {
        return "[CircleButton: " +
                texturePath +
                "]";
    }
}
