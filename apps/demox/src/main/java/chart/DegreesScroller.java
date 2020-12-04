package chart;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

public class DegreesScroller {
    public static final int SCROLL_MODE = 0;
    public static final int FLING_MODE = 1;
    private int mMode;
    private boolean mIsNeedAdjust;

    private final float mDeceleration;

    private boolean mFinished;
    private float mVelocity;
    private float mStartDegrees;
    private float mFinalDegrees;
    private float mCurrentDegrees;
    private long mStartTime;
    private int mDuration;
    private float mSpeed;

    private boolean mIsClockwise;

    public DegreesScroller(Context context) {
        mFinished = true;
        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = SensorManager.GRAVITY_EARTH   // g (m/s^2)
                * 39.37f                        // inch/meter
                * ppi                           // pixels per inch
                * ViewConfiguration.getScrollFriction() * 3;
    }

    public void fling(float startDegrees, float velocity, boolean isClockwise) {
        mMode = FLING_MODE;
        mFinished = false;
        mIsNeedAdjust = true;
        mIsClockwise = isClockwise;

        mVelocity = velocity;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
//        mDuration = (int) (1000 * velocity / mDeceleration);
        mStartDegrees = startDegrees;

        int totalDegrees = (int) (((velocity * velocity) / (2 * mDeceleration)) / 7);
        if (mIsClockwise) {
            mFinalDegrees = mStartDegrees + totalDegrees;
        } else {
            mFinalDegrees = mStartDegrees - totalDegrees;
        }
    }

    /**
     * Start scrolling by providing a starting point and the distance to travel.
     *
     * @param
     */
    public void startScroll(float startDegrees, float endDegrees, boolean isClockwise) {
        mMode = SCROLL_MODE;
        mFinished = false;
        mIsNeedAdjust = false;
        mIsClockwise = isClockwise;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartDegrees = startDegrees;
        mFinalDegrees = endDegrees;
        float intervalDegrees = Math.abs(mFinalDegrees - mStartDegrees);
        mDuration = (int) (intervalDegrees * 3) + 100;
        if (mDuration == 0) {
            return;
        }

        mSpeed = intervalDegrees / mDuration;
    }

    public boolean isNeedAdjust() {
        return mIsNeedAdjust;
    }

    public void setNeedAdjust(boolean isAdjust) {
        mIsNeedAdjust = isAdjust;
    }

    /**
     * Call this when you want to know the new location.  If it returns true,
     * the animation is not yet finished.  loc will be altered to provide the
     * new location.
     */
    public boolean computeScrollOffset() {
        if (mFinished) {
            return false;
        }

        int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
        float degrees = 0;

        switch (mMode) {
            case SCROLL_MODE:
                if (timePassed >= mDuration) {
                    degrees = mDuration * mSpeed;
                    markAsFinished();
                } else {
                    degrees = timePassed * mSpeed;
                }

                if (mIsClockwise) {
                    mCurrentDegrees = mStartDegrees + degrees;
                } else {
                    mCurrentDegrees = mStartDegrees - degrees;
                }
                break;
            case FLING_MODE:
                float timePassedSeconds = timePassed / 1000.0f;
                float distance = (mVelocity * timePassedSeconds)
                        - (mDeceleration * timePassedSeconds * timePassedSeconds / 2.0f);
                degrees = Math.abs(distance) / 7;

                if (mIsClockwise) {
                    mCurrentDegrees = mStartDegrees + degrees;
                    if (mCurrentDegrees >= mFinalDegrees) {
                        markAsFinished();
                    }
                } else {
                    mCurrentDegrees = mStartDegrees - degrees;
                    if (mCurrentDegrees <= mFinalDegrees) {
                        markAsFinished();
                    }
                }
                break;
        }

        return true;
    }

    public final boolean isFinished() {
        return mFinished;
    }

    public void abortAnimation() {
        markAsFinished();
    }

    private void markAsFinished() {
        mFinished = true;
        if (onStatusChangeListener != null) {
            onStatusChangeListener.onFinished();
        }
    }

    public final float getCurrDegrees() {
        return mCurrentDegrees;
    }

    public final boolean isClockwise() {
        return mIsClockwise;
    }

    private OnStatusChangeListener onStatusChangeListener;

    public OnStatusChangeListener getOnStatusChangeListener() {
        return onStatusChangeListener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.onStatusChangeListener = onStatusChangeListener;
    }

    public interface OnStatusChangeListener {
        void onFinished();
    }

}
