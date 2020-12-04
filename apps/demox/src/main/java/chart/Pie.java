package chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.ak.demo.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 画一个饼状的报表
 *
 * @author lanlan
 */
public class Pie extends View implements DegreesScroller.OnStatusChangeListener {

    public static final String TAG_ID = "TAG_ID";
    public static final String TAG_LABLE = "TAG_LABLE";
    public static final String TAG_FIRST = "TAG_FIRST";
    public static final String TAG_SECOND = "TAG_SECOND";
    public static final String TAG_INITED = "TAG_INITED";

    public static final float SCROLL_OFF_PERCENTAGE = 1.0f;


    public static final int MAX_DISPLAY_COLORS = 7;

    //Only easy to use, and faster than call by function
    private ArrayList<PieData> mDataList;
    private float mTotal;
    private float mOthersTotal;

    private Context mContext;
    private Paint mPaint;
    private Paint mPaintPercentLabelTxt;
    private Paint mPaintPercentTxt;
    private Paint mPaintLabelTxt;
    private Paint mLinePaint;
    private Paint mBgPaint;
    private float mPercentLabelTxtSize;
    private float mPercentTxtSize;
    private float mLabelTextSize;

    //Pie param
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private RectF mRect;

    private RectF mOutRect;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mDownTouchX;
    private float mDownTouchY;
    private String mMoneyFlag = "";

    private int mLastTouchAction;

    private float mRotateDegrees;
    private VelocityTracker mVelocityTracker;
    private DegreesScroller mScroller;
    private boolean mIsLastClockwise;
    private int mSpaceDisplay;
    private NumberFormat mPercentFormat;

    private float[] mDegreesSkip = new float[MAX_DISPLAY_COLORS];
    private float[] mDegreesDistance = new float[MAX_DISPLAY_COLORS];
    private float[] mDegreesStart = new float[MAX_DISPLAY_COLORS];

    //內环圆的半径
    private float innerRadius = 0;
    //像素比例，饼图内部的组件对应整个饼图大小的比例
    private float percentage = 0.f;

    private int mLastSelectedIndex = -1;

    private OnValueChangedListener onValueChangedListener;

    public Pie(Context context) {
        super(context);

        mContext = context;
        init();
    }

    public Pie(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mScroller = new DegreesScroller(context);
        mScroller.setOnStatusChangeListener(this);
        TypedArray params = mContext.obtainStyledAttributes(attrs,
                R.styleable.Pie);
        mPercentLabelTxtSize = params.getDimension(R.styleable.Pie_percentLabelTextSize, context.getResources().getDimensionPixelSize(R.dimen.txtSizeF8));
        mPercentTxtSize = params.getDimension(R.styleable.Pie_percentTextSize, context.getResources().getDimensionPixelSize(R.dimen.txtSizeF7));
        mLabelTextSize = params.getDimension(R.styleable.Pie_labelTextSize, context.getResources().getDimensionPixelSize(R.dimen.txtSizeF4));
        params.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintPercentLabelTxt = new Paint();
        mPaintPercentLabelTxt.setAntiAlias(true);
        mPaintPercentLabelTxt.setTextSize(mPercentLabelTxtSize);
        mPaintPercentLabelTxt.setColor(mContext.getResources().getColor(R.color.darkGray));

        mPaintPercentTxt = new Paint();
        mPaintPercentTxt.setAntiAlias(true);
        mPaintPercentTxt.setTextSize(mPercentTxtSize);
        mPaintPercentTxt.setColor(mContext.getResources().getColor(R.color.darkGray));

        mPaintLabelTxt = new Paint();
        mPaintLabelTxt.setAntiAlias(true);
        mPaintLabelTxt.setTextSize(mLabelTextSize);
        mPaintLabelTxt.setColor(mContext.getResources().getColor(R.color.createcredetail));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.TRANSPARENT);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mContext.getResources().getColor(R.color.pie_bg));


        mRect = new RectF();
        mOutRect = new RectF();

        mPercentFormat = NumberFormat.getPercentInstance();
        mPercentFormat.setMinimumFractionDigits(2);

        mSpaceDisplay = getResources().getDimensionPixelOffset(R.dimen.size40);
    }

    public void setData(ArrayList<PieData> data, String moneyTypeUuid) {
        mDataList = data;
//		if (UserProfile.getPropertyValue(UserPreferencesKey.KEY_HAS_MULTI_MONEYTYPE, 0) == 1) {
//			mMoneyFlag = Frame.getInstance().getDaoSession().getMoneyTypeDao()
//                    .load(moneyTypeUuid).getFlag();
//		} else {
//			mMoneyFlag = "";
//		}
//
        initTotal(data);
        initDegreesSkip(data);

        float endDegrees = (180 - mDegreesStart[0] - mDegreesStart[0] - mDegreesSkip[0]) / 2;
        startScroll(endDegrees);
    }

    /**
     * 计算总消费金额
     *
     * @param dataList
     */
    private void initTotal(ArrayList<PieData> dataList) {
        if (dataList == null) {
            return;
        }

        mTotal = 0;
        mOthersTotal = 0;
        int nCount = dataList.size();
        for (int i = 0; i < nCount; ++i) {
            PieData pieData = dataList.get(i);
            if (pieData == null) {
                continue;
            }
            double itemMoney = pieData.getValue();

            mTotal += itemMoney;
            if (i >= MAX_DISPLAY_COLORS - 1) {
                mOthersTotal += itemMoney;
            }
        }
    }

//    private final float DISTANCE_LENGTH = 0.0f;

    /**
     * 计算各圆弧角度
     *
     * @param dataList
     */
    private void initDegreesSkip(ArrayList<PieData> dataList) {
        if (dataList == null) {
            return;
        }

        int len = Math.min(dataList.size(), MAX_DISPLAY_COLORS);
        float dStart = 0;
        mDegreesSkip = new float[MAX_DISPLAY_COLORS];
        mDegreesStart = new float[MAX_DISPLAY_COLORS];
        mDegreesDistance = new float[MAX_DISPLAY_COLORS];

        for (int i = 0; i < len; i++) {
            PieData item = dataList.get(i);
            if (item == null) {
                continue;
            }
            mDegreesStart[i] = dStart;
            if (i != len - 1) {
                if (mTotal != 0) {
                    mDegreesSkip[i] = (float) item.getValue() / mTotal * 360;
//                    mDegreesDistance[i] = DISTANCE_LENGTH;
                }
            } else {
                mDegreesSkip[i] = 360 - dStart;
            }

            dStart += (mDegreesSkip[i]);
        }
    }

    /**
     * @return true if the pie is able to show, else not.
     */
    public final boolean isValideView() {
        return mDataList != null && mDataList.size() > 0 && mTotal > 0;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureWidth = (int) (measureWidth * SCROLL_OFF_PERCENTAGE);
        setMeasuredDimension(measureWidth, measureWidth);

    }


    private void initDrawParams() {
        int canvasHeight = getHeight();

        int canvasWidth = getWidth();

        percentage = canvasWidth / 264;

        innerRadius = (140 * percentage) / 2;
        int outerLength = (int) (10 * percentage);
        int diameter = canvasWidth - outerLength * 2;
        mRect.set(outerLength, outerLength, outerLength + diameter, outerLength + diameter);
        mOutRect.set(0, 0, outerLength + diameter + outerLength, outerLength + diameter + outerLength);
        mRadius = diameter >> 1;
        mCenterX = outerLength + mRadius;
        mCenterY = outerLength + mRadius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initDrawParams();

        if (!isValideView()) {
            return;
        }
        drawBg(canvas);
        drawPie(canvas);
        drawCenterCircle(canvas);
        drawText(canvas);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawArc(mOutRect, 0, 360, true, mBgPaint);
    }


    private void drawCenterCircle(Canvas canvas) {

        canvas.drawCircle(mCenterX, mCenterY, innerRadius, mBgPaint);
        int bottomLength = (int) (14 * percentage);
        int sideLength = (int) (9 * percentage);

        float belowCenter = (float) (Math.sqrt(Math.pow(innerRadius, 2) - Math.pow(bottomLength / 2, 2)));
        Path path = new Path();
        path.moveTo(mCenterX - bottomLength / 2, mCenterY + belowCenter);// 此点为多边形的起点
        path.lineTo(mCenterX + bottomLength / 2, mCenterY + belowCenter);
        path.lineTo(mCenterX, mCenterY + belowCenter + sideLength);
        path.close(); //
        canvas.drawPath(path, mBgPaint);

    }

    private void drawText(Canvas canvas) {
        int directIndex = getNowDirectIndex();
        PieData item = mDataList.get(directIndex);
        //FIXME 环形的文字计算
        String typeName = "";
        Double strMoney;
//        if (directIndex == 6 && mDataList.size() > 7) {
//            typeName = mContext.getString(R.string.sumOfOthers);
//            strMoney = MoneyUtil.formatDecimal(mOthersTotal, 2);
//        } else {
        typeName = item.getName();
        strMoney = item.getValue();
//        }

        String shortTypeName = typeName;
        if (typeName.length() > 4) {
            shortTypeName = typeName.subSequence(0, 4) + "...";
        }
        canvas.drawText(shortTypeName, mCenterX - ((int) mPaintPercentLabelTxt.measureText(shortTypeName) >> 1), mCenterY, mPaintPercentLabelTxt);

        String percent = mPercentFormat.format(mDegreesSkip[directIndex] / 360);
        canvas.drawText(percent, mCenterX - ((int) mPaintPercentTxt.measureText(percent) >> 1), mCenterY + mPaintPercentTxt.getTextSize(), mPaintPercentTxt);

        String label = typeName + ": " + mMoneyFlag + strMoney;
//        float top = Math.min(canvas.getHeight() + ((int) mPaintLabelTxt.getTextSize() >> 1), mOutRect.bottom + outerLength * 2 + mSpaceDisplay + mPaintLabelTxt.getTextSize());
//        canvas.drawText(label, mCenterX - ((int) mPaintLabelTxt.measureText(label) >> 1), top, mPaintLabelTxt);
    }

    private void notifyValueChangeListener() {
        int i = getNowDirectIndex();
        if (i != mLastSelectedIndex) {
            mLastSelectedIndex = i;
            if (null != onValueChangedListener) {
                PieData selectedData = mDataList.get(mLastSelectedIndex);
                float percent = (float) (Math.round(selectedData.getValue() / mTotal * 10000)) / 10000;

                onValueChangedListener.onValueChanged(mLastSelectedIndex, selectedData, percent);
            }
        }
    }

    private int getNowDirectIndex() {
        int len = Math.min(mDataList.size(), MAX_DISPLAY_COLORS);
        for (int i = 0; i < len; ++i) {
            float sDegrees = mDegreesStart[i];
            float eDegrees = mDegreesStart[i] + mDegreesSkip[i];
            if (mRotateDegrees > 90 - eDegrees && mRotateDegrees < 90 - sDegrees
                    || mRotateDegrees > 450 - eDegrees && mRotateDegrees < 450 - sDegrees
                    || mRotateDegrees > -270 - eDegrees && mRotateDegrees < -270 - sDegrees) {
                return i;
            }
        }
        return 0;
    }

    private void drawPie(Canvas canvas) {
        int len = Math.min(mDataList.size(), MAX_DISPLAY_COLORS);
        for (int i = 0; i < len; i++) {
            mPaint.setColor(mDataList.get(i).getColor());
            float startAngle = mDegreesStart[i] + mRotateDegrees;
            float endAngle = mDegreesSkip[i];
            canvas.drawArc(mRect, startAngle, endAngle, true, mPaint);
        }
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        for (int i = 0; i < len && len >1; i++) {
            float startAngle = mDegreesStart[i] + mRotateDegrees;
            float xCosValue = (float) Math.cos(startAngle * Math.PI / 180);
            float ySinValue = (float) Math.sin(startAngle * Math.PI / 180);

            float x1 = mCenterX + mRadius * xCosValue;
            float y1 = mCenterY + mRadius * ySinValue;

            float x2 = mCenterX + innerRadius * xCosValue;
            float y2 = mCenterY + innerRadius * ySinValue;
            mPaint.setStrokeWidth( 1.0f);
            canvas.drawLine(x1, y1, x2, y2, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean hasHandle = false;
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchAction = MotionEvent.ACTION_DOWN;
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                if (!mScroller.isFinished()) {
                    mScroller.setNeedAdjust(false);
                    mScroller.abortAnimation();
                    mLastTouchAction = -1;
                }
                mDownTouchX = event.getX();
                mDownTouchY = event.getY();
                mLastTouchX = mDownTouchX;
                mLastTouchY = mDownTouchY;

                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (mLastTouchAction == MotionEvent.ACTION_DOWN
                        && Math.abs(x - mDownTouchX) < 5 && Math.abs(y - mDownTouchY) < 5) {
                    break;
                }

                getParent().requestDisallowInterceptTouchEvent(true);
                mLastTouchAction = MotionEvent.ACTION_MOVE;
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                }
                mRotateDegrees = calcDegrees(mLastTouchX, mLastTouchY, x, y);
                postInvalidate();
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mLastTouchAction == MotionEvent.ACTION_DOWN
                        && Math.abs(event.getX() - mDownTouchX) < 5
                        && Math.abs(event.getY() - mDownTouchY) < 5) {
                    onClick();
                    getParent().requestDisallowInterceptTouchEvent(true);
//                    hasHandle = true;
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                int velocityX = 0, velocityY = 0;
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    velocityX = (int) mVelocityTracker.getXVelocity();
                    velocityY = (int) mVelocityTracker.getYVelocity();
                }

                mScroller.fling(mRotateDegrees, (float) Math.hypot(velocityX, velocityY), mIsLastClockwise);

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                computeScroll();
                break;
        }


        return true;
    }

    private void onClick() {
        startScroll(calcDegrees(mLastTouchX, mLastTouchY, mCenterX, mCenterY + mRadius));
        mScroller.setNeedAdjust(true);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mRotateDegrees = mScroller.getCurrDegrees() % 360;
            postInvalidate();
            return;
        } else {
            adjustView();
            super.computeScroll();
        }
    }

    private void adjustView() {
        if (!mScroller.isNeedAdjust()) {
            return;
        }

        int len = mDegreesSkip.length;
        for (int i = 0; i < len; i++) {
            adjustViewTo(mDegreesStart[i], mDegreesStart[i] + mDegreesSkip[i]);
        }
    }

    private void adjustViewTo(float sDegrees, float eDegrees) {
        float endDegrees = 0;
        if (mRotateDegrees > 90 - eDegrees && mRotateDegrees < 90 - sDegrees) {
            endDegrees = (180 - sDegrees - eDegrees) / 2;
            startScroll(endDegrees);
        } else if (mRotateDegrees > 450 - eDegrees && mRotateDegrees < 450 - sDegrees) {
            endDegrees = (900 - sDegrees - eDegrees) / 2;
            startScroll(endDegrees);
        } else if (mRotateDegrees > -270 - eDegrees && mRotateDegrees < -270 - sDegrees) {
            endDegrees = (-540 - sDegrees - eDegrees) / 2;
            startScroll(endDegrees);
        }
    }

    private void startScroll(float endDegrees) {
        if (mRotateDegrees == endDegrees) {
            return;
        }

        mScroller.startScroll(mRotateDegrees, endDegrees, mRotateDegrees < endDegrees);
        computeScroll();
    }

    private float calcDegrees(float lastX, float lastY, float newX, float newY) {
        double x1 = lastX - mCenterX;
        double y1 = lastY - mCenterY;
        double x2 = newX - mCenterX;
        double y2 = newY - mCenterY;

        double direction = x1 * y2 - x2 * y1;
        if (direction == 0) {
            return mRotateDegrees;
        }

        mIsLastClockwise = direction > 0;
        float degrees = (float) (Math.acos((x1 * x2 + y1 * y2) / Math.sqrt((Math.pow(x1, 2) + Math.pow(y1, 2)) * (Math.pow(x2, 2) + Math.pow(y2, 2)))) * 180 / Math.PI);
        if (Double.isNaN(degrees)) {
            return mRotateDegrees;
        }

        if (mIsLastClockwise) {
            return mRotateDegrees + degrees % 360;
        } else {
            return mRotateDegrees - degrees % 360;
        }
    }

    public OnValueChangedListener getOnValueChangedListener() {
        return onValueChangedListener;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    @Override
    public void onFinished() {
        notifyValueChangeListener();
    }

    public interface OnValueChangedListener {

        void onValueChanged(int index, PieData pieData, float percent);

    }

}
