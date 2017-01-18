package eva.android.com.instafun2.parallax;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import eva.android.com.instafun2.R;
import eva.android.com.instafun2.activities.UserWallActivity;
import eva.android.com.instafun2.data.UserData;

public class ParallaxRelativeLayout extends RelativeLayout implements SensorEventListener {

    /**
     * constant use to convert nano second into second
     */
    private static final float NS2S = 1.0f / 1000000000.0f;

    /**
     * boundary minimum to avoid noise
     */
    private static final float TRANSLATION_NOISE = 0.6f;

    /**
     * boundary maximum, over it phone rotates
     */
    private static final float MAXIMUM_ACCELERATION = 3.00f;

    /**
     * duration for translation animation
     */
    private static final int ANIMATION_DURATION_IN_MILLI = 200;

    /**
     * smoothing ratio for Low-Pass filter algorithm
     */
    private static final float LOW_PASS_FILTER_SMOOTHING = 3.0f;

    /**
     * ratio used to determine radius according to ZOrder
     */
    private static final int DEFAULT_RADIUS_RATIO = 12;

    /**
     * remapped axis X according to current device orientation
     */
    private int mRemappedViewAxisX;

    /**
     * remapped axis Y according to current device orientation
     */
    private int mRemappedViewAxisY;

    /**
     * remapped orientation X according to current device orientation
     */
    private int mRemappedViewOrientationX;

    /**
     * remapped orientation Y according to current device orientations
     */
    private int mRemappedViewOrientationY;

    /**
     * Children view to animate
     */
    private HashMap<View, Integer> mChildrenToAnimate;

    /**
     * store last acceleration values
     */
    private float[] mLastAcceleration;

    /**
     * use to calculate dT
     */
    private long mTimeStamp;

    /**
     * parallax Background
     */
    private ParallaxBackground mParallaxBackground;

    /**
     * Animator for smooth face motion
     */
    private ObjectAnimator mParallaxAnimator;

    /**
     * last known translation
     */
    private float[] mLastTranslation;

    private Intent intent;

    private Bundle bundle;

    private Context mContext;

    private DisplayMetrics metrics;

    private static final String[] mColors = new String[]{"#f9ad8c", "#d6576a", "#e7d2d6", "#e7d2ca",
            "#aa5b5e", "#f9ad8c", "#e7d2b0"};

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public ParallaxRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ParallaxRelativeLayout,
                0, 0);

        //retrieve custom attribute
        try {
            final Drawable background = a.getDrawable(R.styleable.ParallaxRelativeLayout_parallax_background);
            mParallaxBackground = new ParallaxBackground(context, drawableToBitmap(background));
        } finally {
            a.recycle();
        }

        //allow onDraw for layout
        this.setWillNotDraw(false);


        /**
         * Remap axis and axis' orientation according to the current device rotation
         */
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();
        remapAxis(rotation);

        mChildrenToAnimate = new HashMap<>();

        mLastAcceleration = new float[]{0.0f, 0.0f};

        mLastTranslation = new float[]{0.0f, 0.0f};

        mTimeStamp = 0;

        mParallaxAnimator = ObjectAnimator.ofObject(this, "CurrentTranslationValues",
                new FloatArrayEvaluator(2), 0);

        mParallaxAnimator.setDuration(ANIMATION_DURATION_IN_MILLI);

        intent = new Intent(context, UserWallActivity.class);

        bundle = new Bundle();

        mContext = context;

        metrics = getResources().getDisplayMetrics();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Add all children with a tag in order to apply parallax motion
     *
     * @param viewGroup
     */
    private void addParallaxChildrenRecursively(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                addParallaxChildrenRecursively((ViewGroup) childView);
            }

            if (childView.getTag() != null) {
                Integer zOrder = Integer.valueOf(childView.getTag().toString());
                mChildrenToAnimate.put(childView, zOrder);
            }
        }

    }

    /**
     * Used to remap axis and axis' orientation according to the current device rotation.
     * Orientation inverted = -1 otherwise +1
     *
     * @param rotation current device rotation
     */
    private void remapAxis(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                mRemappedViewAxisX = 0;
                mRemappedViewAxisY = 1;
                mRemappedViewOrientationX = +1;
                mRemappedViewOrientationY = -1;
                break;

            case Surface.ROTATION_90:
                mRemappedViewAxisX = 1;
                mRemappedViewAxisY = 0;
                mRemappedViewOrientationX = -1;
                mRemappedViewOrientationY = -1;
                break;

            case Surface.ROTATION_270:
                mRemappedViewAxisX = 1;
                mRemappedViewAxisY = 0;
                mRemappedViewOrientationX = +1;
                mRemappedViewOrientationY = +1;
                break;
        }
    }

    /**
     * used by object animator to update current orientation values
     *
     * @param evaluatedValues
     */
    public void setCurrentTranslationValues(float[] evaluatedValues) {

        final float translateX = mRemappedViewOrientationX * this.getWidth() / DEFAULT_RADIUS_RATIO * evaluatedValues[mRemappedViewAxisX];
        final float translateY = mRemappedViewOrientationY * this.getHeight() / DEFAULT_RADIUS_RATIO * evaluatedValues[mRemappedViewAxisY];

        //animate background
        mParallaxBackground.setTranslationX(translateX);
        mParallaxBackground.setTranslationY(translateY);


        //  animate each child provided with an integer tag used as zOrder for parallax
        for (View parallaxItem : mChildrenToAnimate.keySet()) {
            ParallaxPlane plane =
                    ParallaxPlaneFactory.createPlane(mChildrenToAnimate.get(parallaxItem));

            parallaxItem.setTranslationX(translateX * plane.getTranslationDirection() / plane.getTranslationRatio());
            parallaxItem.setTranslationY(translateY * plane.getTranslationDirection() / plane.getTranslationRatio());
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //get all children which will be animated
        if (mChildrenToAnimate.isEmpty()) {
            addParallaxChildrenRecursively(this);
        }

        if (mParallaxBackground.getParent() == null) {
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            params.setMargins((int) (-this.getWidth() / 2.0f), (int) (-this.getHeight() / 2.0f), 0, 0);
            this.addView(mParallaxBackground, 0, params);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        final float accelerationX = event.values[mRemappedViewAxisX];
        final float accelerationY = event.values[mRemappedViewAxisY];
        float[] translation = new float[]{0.0f, 0.0f};


        /**
         * Process acceleration to determine motion and let ParallaxRelativeLayout animate
         * it's children and it's background
         */
        if (mTimeStamp != 0) {
            final float dT = (event.timestamp - mTimeStamp) * NS2S;

            /**
             * Use basic integration to retrieve position from acceleration.
             * p = 1/2 * a * dT^2
             */
            if (Math.abs(accelerationX) > MAXIMUM_ACCELERATION) {
                translation[mRemappedViewAxisX] = mLastAcceleration[mRemappedViewAxisX] + 0.5f * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translation[mRemappedViewAxisX] = mLastAcceleration[mRemappedViewAxisX] + 0.5f * accelerationX * dT * dT;
                mLastAcceleration[mRemappedViewAxisX] = accelerationX;
            }

            if (Math.abs(accelerationY) > MAXIMUM_ACCELERATION) {
                translation[mRemappedViewAxisY] = mLastAcceleration[mRemappedViewAxisY] + 0.5f * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translation[mRemappedViewAxisY] = mLastAcceleration[mRemappedViewAxisY] + 0.5f * accelerationY * dT * dT;
                mLastAcceleration[mRemappedViewAxisY] = accelerationY;
            }

            /**
             * In order to keep small variations, the noise is dynamic.
             * We normalized translation and noise it by the means of last and new value.
             */
            final float normalizerX = (Math.abs(mLastTranslation[mRemappedViewAxisX]) + Math.abs(translation[mRemappedViewAxisX])) / 2;
            final float normalizerY = (Math.abs(mLastTranslation[mRemappedViewAxisY]) + Math.abs(translation[mRemappedViewAxisY])) / 2;

            final float translationDifX = Math.abs(mLastTranslation[mRemappedViewAxisX] - translation[mRemappedViewAxisX]) / normalizerX;
            final float translationDifY = Math.abs(mLastTranslation[mRemappedViewAxisY] - translation[mRemappedViewAxisY]) / normalizerY;

            final float dynamicNoiseX = TRANSLATION_NOISE / normalizerX;
            final float dynamicNoiseY = TRANSLATION_NOISE / normalizerY;

            float[] newTranslation = null;

            if (translationDifX > dynamicNoiseX && translationDifY > dynamicNoiseY) {
                newTranslation = translation.clone();
            } else if (translationDifX > dynamicNoiseX) {
                newTranslation = new float[2];
                newTranslation[mRemappedViewAxisX] = translation[mRemappedViewAxisX];
                newTranslation[mRemappedViewAxisY] = mLastTranslation[mRemappedViewAxisY];
            } else if (translationDifY > dynamicNoiseY) {
                newTranslation = new float[2];
                newTranslation[mRemappedViewAxisX] = mLastTranslation[mRemappedViewAxisX];
                newTranslation[mRemappedViewAxisY] = translation[mRemappedViewAxisY];
            }


            /**
             * if new translation aren't noise apply Low-Pass filter algorithm and animate parallax
             * items
             */
            if (newTranslation != null) {

                newTranslation[mRemappedViewAxisX] = mLastTranslation[mRemappedViewAxisX] + (newTranslation[mRemappedViewAxisX] - mLastTranslation[mRemappedViewAxisX]) / LOW_PASS_FILTER_SMOOTHING;
                newTranslation[mRemappedViewAxisY] = mLastTranslation[mRemappedViewAxisY] + (newTranslation[mRemappedViewAxisY] - mLastTranslation[mRemappedViewAxisY]) / LOW_PASS_FILTER_SMOOTHING;

                if (mParallaxAnimator.isRunning()) {
                    mParallaxAnimator.cancel();
                }
                mParallaxAnimator.setObjectValues(mLastTranslation.clone(), newTranslation.clone());
                mParallaxAnimator.start();
                mLastTranslation[mRemappedViewAxisX] = newTranslation[mRemappedViewAxisX];
                mLastTranslation[mRemappedViewAxisY] = newTranslation[mRemappedViewAxisY];


            }

        }
        mTimeStamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void addAllViews(final ArrayList<UserData> mData) {

        addRandomViews();

        for (int i = 0; i < mData.size(); i++) {
            final int finalI = i;

            int scale = new Random().nextInt(100) + 300;
            int top = new Random().nextInt(metrics.heightPixels - scale);
            int left = new Random().nextInt(metrics.widthPixels - scale);
            int tag = new Random().nextInt(4) + 1;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    scale, scale);
            layoutParams.setMargins(left, top, 0, 0);
            CircleImageView imageView = new CircleImageView(mContext);

            Picasso.with(mContext)
                    .load(mData.get(i).userPhoto)
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_error)
                    .into(imageView);

            imageView.setLayoutParams(layoutParams);
            imageView.setTag(tag);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putParcelable("userData", mData.get(finalI));
                    bundle.putParcelableArrayList("comments", mData.get(finalI).comments);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            this.addView(imageView);
        }
    }

    private void addRandomViews() {

        for (String mColor : mColors) {
            int scale = new Random().nextInt(100) + 200;
            int top = new Random().nextInt(metrics.heightPixels - scale);
            int left = new Random().nextInt(metrics.widthPixels - scale);
            int tag = new Random().nextInt(4) + 1;

            LayoutParams layoutParams = new LayoutParams(
                    scale, scale);
            layoutParams.setMargins(left, top, 0, 0);
            CircleImageView imageView = new CircleImageView(mContext);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.parseColor(mColor));
            shape.setAlpha(255*70/100);
            imageView.setBackground(shape);
            imageView.setLayoutParams(layoutParams);
            imageView.setTag(tag);
            this.addView(imageView);
        }
    }

    public void addView(final UserData mData) {

        int scale = new Random().nextInt(100) + 300;
        int top = new Random().nextInt(metrics.heightPixels - scale -200);
        int left = new Random().nextInt(metrics.widthPixels - scale - 200);
        int tag = new Random().nextInt(4) + 1;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scale, scale);
        layoutParams.setMargins(left, top, 0, 0);
        CircleImageView imageView = new CircleImageView(mContext);

        Picasso.with(mContext)
                .load(mData.userPhoto)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .into(imageView);

        imageView.setLayoutParams(layoutParams);
        imageView.setTag(tag);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putParcelable("userData", mData);
                bundle.putParcelableArrayList("comments", mData.comments);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        this.addView(imageView);
    }
}