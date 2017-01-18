package eva.android.com.instafun2.parallax;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator<float[]> {

    /**
     * size of float array
     */
    private int mArraySize;

    /**
     * Allow to create animation on float[]
     *
     * @param arraySize size of array which is going to be animated
     */
    FloatArrayEvaluator(int arraySize) {
        super();
        mArraySize = arraySize;
    }

    @Override
    public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
        float[] evaluatedValue = null;

        if (startValue.length == mArraySize && endValue.length == mArraySize) {
            evaluatedValue = new float[startValue.length];
            for (int i = 0; i < mArraySize; i++) {
                evaluatedValue[i] = startValue[i] + fraction * (endValue[i] - startValue[i]);
            }
        }
        return evaluatedValue;
    }
}