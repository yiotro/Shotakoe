package yio.tro.shotakoe.stuff.factor_yio;

public class MbSoftBigRubberBand extends AbstractMoveBehavior{

    public static double x1 = 0.35;
    public static double y1 = 0.46;
    public static double x2 = 0.91;
    public static double y2 = 1.03;
    double k;
    double a;
    double b;
    double c;
    double d;
    double g;
    double h;
    // f1 = k * x^2
    // f2 = a * x^2 + b * x + c
    // f3 = d * x^2 + g * x + h
    // фото вычислений лежит в материалах


    public MbSoftBigRubberBand() {
        k = y1 / (x1 * x1);
        a = (y1 - 2 * x1 * x1 * k - y2 + 2 * x1 * x2 * k) / (-x1 * x1 - x2 * x2 + 2 * x1 * x2);
        b = 2 * x1 * (k - a);
        c = y2 - a * x2 * x2 - b * x2;
        d = (-2 * a * x2 * x2 - b * x2 - 1 + 2 * a * x2 + b + y2) / (-x2 * x2 + 2 * x2 - 1);
        h = 2 * d * x2 + 1 - d - 2 * a * x2 - b;
        g = 1 - h - d;
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
        if (x < x2) {
            return a * x * x + b * x + c;
        }
        return d * x * x + g * x + h;
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
