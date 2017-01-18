package eva.android.com.instafun2.parallax;

public class ParallaxPlane {

    private float mTranslationRatio;

    private int mTranslationDirection;

    /**
     * Plane used during translation motion
     *
     * @param radius
     * @param direction
     */
    ParallaxPlane(float radius, int direction) {
        mTranslationRatio = radius;
        mTranslationDirection = direction;
    }

    float getTranslationRatio() {
        return mTranslationRatio;
    }

    int getTranslationDirection() {
        return mTranslationDirection;
    }
}