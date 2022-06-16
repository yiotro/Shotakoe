package yio.tro.shotakoe.stuff.factor_yio;

public abstract class AbstractMoveBehavior {


    public AbstractMoveBehavior() {

    }


    void updateNeedsToMoveValue(FactorYio f) {
        if (f.inAppearState && getProgress(f) == 1) {
            f.needsToMove = false;
            return;
        }
        if (f.inDestroyState && getProgress(f) == 0) {
            f.needsToMove = false;
        }
    }


    void applyStrictBounds(FactorYio f) {
        if (f.inAppearState && f.value > 1) {
            f.value = 1;
        }
        if (f.inDestroyState && f.value < 0) {
            f.value = 0;
        }
    }


    abstract void move(FactorYio f);


    void onAppear(FactorYio f) {

    }


    void onDestroy(FactorYio f) {

    }


    float getProgress(FactorYio f) {
        return (float) f.value; // value and progress are same things by default
    }

}
