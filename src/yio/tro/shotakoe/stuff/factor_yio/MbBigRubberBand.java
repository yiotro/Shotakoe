package yio.tro.shotakoe.stuff.factor_yio;

public class MbBigRubberBand extends AbstractMoveBehavior {

    double x1 = 0.5;
    double y1 = 0.64;
    double k;
    double a;
    double b;
    double c;
    // f1 = a1 * x^2
    // f2 = a2 * x^2 + b2 * x + c2
    // фото вычислений лежит в материалах


    public MbBigRubberBand() {
        k = y1 / (x1 * x1);
        a = (y1 - 2 * x1 * x1 * k - 1 + 2 * x1 * k) / (2 * x1 - x1 * x1 - 1);
        b = 2 * x1 * (k - a);
        c = 1 - a - b;
    }


    @Override
    void move(FactorYio f) {
        if (f.inAppearState) {
            moveUp(f);
            return;
        }
        moveDown(f);
    }


    private double apply(double x) {
        if (x < x1) {
            return k * x * x;
        }
        return a * x * x + b * x + c;
    }


    private void moveUp(FactorYio f) {
        f.x += f.speedMultiplier / 60f;
        if (f.x > 1) {
            f.x = 1;
        }
        f.value = apply(f.x);
    }


    private void moveDown(FactorYio f) {
        f.x -= f.speedMultiplier / 60f;
        if (f.x < 0) {
            f.x = 0;
        }
        f.value = 1 - apply(1 - f.x);
    }


    @Override
    void onAppear(FactorYio f) {
        f.speedMultiplier /= 0.2; // normalize
        f.x = 0;
    }


    @Override
    void onDestroy(FactorYio f) {
        f.speedMultiplier /= 0.2; // normalize
        f.x = 1;
    }


    @Override
    float getProgress(FactorYio f) {
        return (float) f.x;
    }

}
