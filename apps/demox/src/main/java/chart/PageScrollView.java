package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.core.util.Pools;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PageScrollView extends BaseViewGroup {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int MAX_DURATION = 600;
    private static final int FLOAT_VIEW_SCROLL = 1;

    protected int mOrientation;
    protected int mMiddleMargin = 0;
    protected float mSizeFixedPercent = 0;

    protected int mFloatViewStart = -1;
    protected int mFloatViewEnd = -1;
    protected boolean isChildCenter = false;
    protected boolean isChildFillParent = false;

    protected int mSwapViewIndex = -1;
    protected int mFloatViewStartIndex = -1;
    protected int mFloatViewEndIndex = -1;

    protected int mFloatViewStartMode = 0;
    protected int mFloatViewEndMode = 0;

    int mTouchSlop;
    int mMinDistance;
    int mMinimumVelocity;
    int mMaximumVelocity;
    private int mOverFlingDistance;

    int mCurrItem = 0;
    int mPrevItem = -1;
    int mFirstVisiblePosition = -1;
    int mLastVisiblePosition = -1;
    int mVirtualCount = 0;

    boolean mIsBeingDragged = false;
    boolean mNeedResolveFloatOffset = false;

    PointF mPointDown = new PointF();
    PointF mPointLast = new PointF();
    //index,offset,duration,center
    Rect mScrollInfo = new Rect(-1, -1, -1, -1);
    VelocityTracker mVelocityTracker = null;
    OverScroller mScrollerScrollView = null;
    OverScroller mScrollerPageView = null;

    PageTransformer mPageTransformer;
    OnPageChangeListener mPageListener = null;
    OnVisibleRangeChangeListener mOnVisibleRangeChangeListener = null;

    private Comparator<PointF> mComparator;
    private List<PointF> mPairList;
    private static final Pools.SimplePool<PointF> sPairPools = new Pools.SimplePool(8);

    public PageScrollView(Context context) {
        super(context);
        init(context);
    }

    public PageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PageScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        final float density = context.getResources().getDisplayMetrics().density;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = (int) (350 * density);
        mMinDistance = (int) (mTouchSlop * 1.2f);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverFlingDistance = configuration.getScaledOverflingDistance() * 2;
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }

    public void setItemClickToScroll() {
        final View.OnClickListener pageClick1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = indexOfItemView(v);
                if (index >= 0) {
                    scrollToCentre(index, 0, -1);
                }
            }
        };
        int pageItemCount = getItemCount();
        for (int i = 0; i < pageItemCount; i++) {
            View child = getItemView(i);
            child.setOnClickListener(pageClick1);
        }
    }

    protected OverScroller getScroller() {
        if (mScrollerPageView == null) {
            mScrollerPageView = new OverScroller(getContext(), new Interpolator() {
                @Override
                public float getInterpolation(float t) {
                    t -= 1.0f;
                    return t * t * t * t * t + 1.0f;
                }
            });
        }
        return mScrollerPageView;

    }

    protected View getVirtualChildAt(int index, boolean withoutGone) {
        int virtualCount = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if ((withoutGone && child.getVisibility() == View.GONE))
                continue;
            if (virtualCount == index) {
                return child;
            }
            virtualCount++;
        }
        return null;
    }

    protected int getVirtualChildCount(boolean withoutGone) {
        int virtualCount = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if ((withoutGone && child.getVisibility() == View.GONE))
                continue;
            virtualCount++;
        }
        return virtualCount;
    }

    public int getItemCount() {
        int pageCount = mVirtualCount;
        if (pageCount == 0) {
            pageCount = mVirtualCount = getVirtualChildCount(true);
        }
        return pageCount;
    }

    public View getItemView(int index) {
        View result = null;
        int pageCount = getItemCount();
        if (index >= 0 && index < pageCount) {
            result = getVirtualChildAt(index, true);
        }
        return result;
    }

    public int indexOfItemView(View view) {
        if (view != null) {
            int virtualIndex = 0;
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if ((child.getVisibility() == View.GONE))
                    continue;
                if (view == child) {
                    return virtualIndex;
                }
                virtualIndex++;
            }
        }
        return -1;
    }

    protected boolean floatViewScrollNeeded(View view, boolean horizontal, boolean floatStartPosition) {
        int scrollRange = getScrollRange(horizontal);
        return view != null && scrollRange > 0;
    }

    protected int translateMeasure(int spec, int padding, boolean limitedSize) {
        int specMode = View.MeasureSpec.getMode(spec);
        int specSize = View.MeasureSpec.getSize(spec);
        int size = Math.max(0, specSize - padding);
        return View.MeasureSpec.makeMeasureSpec(size, limitedSize ? specMode : View.MeasureSpec.UNSPECIFIED);
    }

    protected void measureFloatViewIfNeed(int virtualCount) {
        mFloatViewStartIndex = -1;
        mFloatViewEndIndex = -1;
        if (virtualCount >= 2) {
            boolean horizontal = mOrientation == HORIZONTAL;
            if (mFloatViewStart >= 0 && mFloatViewStart < virtualCount) {
                measureFloatView(mFloatViewStart, horizontal, true);
            }
            if (mFloatViewEnd >= 0 && mFloatViewEnd < virtualCount) {
                measureFloatView(mFloatViewEnd, horizontal, false);
            }
        }
    }

    protected void measureFloatView(int itemIndex, boolean horizontal, boolean floatStart) {
        View view = getVirtualChildAt(itemIndex, true);
        if (getVirtualChildAt(itemIndex, false) == view && floatViewScrollNeeded(view, horizontal, floatStart)) {
            if (floatStart) {
                mFloatViewStartIndex = indexOfChild(view);
                mFloatViewStartMode = FLOAT_VIEW_SCROLL;
            } else {
                mFloatViewEndIndex = indexOfChild(view);
                mFloatViewEndMode = FLOAT_VIEW_SCROLL;
            }
        }
    }

    @Override
    protected void dispatchMeasure(int widthMeasureSpecNoPadding, int heightMeasureSpecNoPadding, int maxSelfWidthNoPadding, int maxSelfHeightNoPadding) {
        boolean horizontal = mOrientation == HORIZONTAL;
        int childState = 0, headerExtraWidth = 0, headerExtraHeight = 0;
        mVirtualCount = getVirtualChildCount(true);
        if (mVirtualCount > 0) {
            final int measureSpecWidth = translateMeasure(widthMeasureSpecNoPadding, headerExtraWidth, !horizontal);
            final int measureSpecHeight = translateMeasure(heightMeasureSpecNoPadding, headerExtraHeight, horizontal);
            int fixedSize = 0, scrollParentRealSize;
            scrollParentRealSize = maxSelfWidthNoPadding;
            if (mSizeFixedPercent > 0 && mSizeFixedPercent <= 1) {
                fixedSize = (int) (scrollParentRealSize * mSizeFixedPercent);
            }
            childState = measureMiddleViewHorizontal(measureSpecWidth, measureSpecHeight, fixedSize, scrollParentRealSize) | childState;
        }
        setMeasureState(childState);
    }

    @Override
    protected void doAfterMeasure(int measuredWidth, int measuredHeight, int contentWidth, int contentHeight) {
        if (isChildFillParent && mVirtualCount > 0) {
            int contentWidthWithPadding = contentWidth + getPaddingLeft() + getPaddingRight();
            int contentHeightWithPadding = contentHeight + getPaddingTop() + getPaddingBottom();
            boolean horizontal = mOrientation == HORIZONTAL;
            int adjustTotal = horizontal ? (measuredWidth - contentWidthWithPadding) : (measuredHeight - contentHeightWithPadding);
            if (adjustTotal > mVirtualCount) {
                adjustMatchParentMeasure(adjustTotal, horizontal);
            }
        }
        measureFloatViewIfNeed(mVirtualCount);
        super.doAfterMeasure(measuredWidth, measuredHeight, contentWidth, contentHeight);
    }


    protected int measureMiddleViewHorizontal(int widthMeasureSpec, int heightMeasureSpec, int childFixedSize, final int parentRealSize) {
        final int childCount = getChildCount();
        int childFixedWidthSpec = childFixedSize <= 0 ? 0 : View.MeasureSpec.makeMeasureSpec(childFixedSize, View.MeasureSpec.EXACTLY);
        int contentWidth = 0;
        int contentHeight = 0;
        int measuredCount = 0;
        int childState = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            PageScrollView.LayoutParams params = (PageScrollView.LayoutParams) child.getLayoutParams();
            int childMarginHorizontal = params.getMarginHorizontal();
            int childMarginVertical = params.getMarginVertical();
            int childWidthSpec = childFixedWidthSpec == 0 ? getMiddleChildMeasureSpec(widthMeasureSpec, parentRealSize, childMarginHorizontal, params.width) : childFixedWidthSpec;
            int childHeightSpec = getMiddleChildMeasureSpec(heightMeasureSpec, 0, childMarginVertical, params.height);
            params.measure(child, childWidthSpec, childHeightSpec);
            if (mMiddleMargin > 0 && measuredCount > 0) {
                contentWidth += mMiddleMargin;
            }
            contentWidth += (child.getMeasuredWidth() + childMarginHorizontal);
            int itemHeight = child.getMeasuredHeight() + childMarginVertical;
            if (contentHeight < itemHeight) {
                contentHeight = itemHeight;
            }
            childState |= child.getMeasuredState();
            measuredCount++;
        }
        setContentSize(Math.max(getContentWidth(), contentWidth), getContentHeight() + contentHeight);
        return childState;
    }

    private void destroyCacheMeasureSize() {
        Iterator<PointF> its = mPairList.iterator();
        while (its.hasNext()) {
            PointF it = its.next();
            sPairPools.release(it);
            its.remove();
        }
    }

    private int buildCacheMeasureSize(int childCount, boolean horizontal) {
        if (mPairList == null) {
            mPairList = new ArrayList(8);
            mComparator = new Comparator<PointF>() {
                @Override
                public int compare(PointF l, PointF r) {
                    return Float.compare(l.y, r.y);
                }
            };
        } else {
            destroyCacheMeasureSize();
        }
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            PointF pair = sPairPools.acquire();
            if (pair == null) {
                pair = new PointF();
            }
            if (horizontal) {
                pair.set(i, child.getMeasuredWidth() + ((PageScrollView.LayoutParams) child.getLayoutParams()).getMarginHorizontal());
            } else {
                pair.set(i, child.getMeasuredHeight() + ((PageScrollView.LayoutParams) child.getLayoutParams()).getMarginVertical());
            }
            mPairList.add(pair);
        }
        return mPairList.size();
    }

    private void adjustMatchMeasureSize(int matchSize, float space) {
        Collections.sort(mPairList, mComparator);
        int startIndex = 0;
        while (space > 1) {
            int diffIndex = -1;
            PointF start = mPairList.get(startIndex), current = null;
            for (int i = startIndex + 1; i < matchSize; i++) {
                current = mPairList.get(i);
                if (current.y > start.y) {
                    diffIndex = i;
                    break;
                }
            }
            if (diffIndex == -1) {
                float addedSize = space / matchSize;
                for (PointF point : mPairList) {
                    point.y = point.y + addedSize;
                }
                space = 0;
            } else {
                float addedSize = Math.min(current.y - start.y, space / diffIndex);
                for (int i = 0; i < diffIndex; i++) {
                    start = mPairList.get(i);
                    start.y = start.y + addedSize;
                }
                space = space - (addedSize * diffIndex);
                startIndex = diffIndex;
            }
        }
    }

    private void adjustMatchParentMeasure(float space, boolean horizontal) {
        final int matchSize = buildCacheMeasureSize(getChildCount(), horizontal), addedTotalSize = (int) space;
        if (matchSize > 0) {
            adjustMatchMeasureSize(matchSize, space);
            for (PointF point : mPairList) {
                View child = getChildAt((int) point.x);
                int goodSize = (int) point.y;
                if (horizontal) {
                    goodSize = goodSize - ((PageScrollView.LayoutParams) child.getLayoutParams()).getMarginHorizontal();
                    child.measure(View.MeasureSpec.makeMeasureSpec(goodSize, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
                } else {
                    goodSize = goodSize - ((PageScrollView.LayoutParams) child.getLayoutParams()).getMarginVertical();
                    child.measure(View.MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(goodSize, View.MeasureSpec.EXACTLY));
                }
            }
            if (horizontal) {
                setContentSize(getContentWidth() + addedTotalSize, getContentHeight());
            } else {
                setContentSize(getContentWidth(), getContentHeight() + addedTotalSize);
            }
            destroyCacheMeasureSize();
        }
    }

    @Override
    protected void doAfterLayout(boolean firstAttachLayout) {
        if (firstAttachLayout) {
            boolean willScrolled = false;
            if (mScrollInfo.left >= 0 || mPrevItem == -1) {
                if (mScrollInfo.left >= 0) {
                    View pageView = getItemView(mScrollInfo.left);
                    if (pageView != null) {
                        willScrolled = scrollTo(pageView, mScrollInfo.top, mScrollInfo.right, 1 == mScrollInfo.bottom);
                    }
                    mScrollInfo.set(-1, -1, -1, -1);
                } else {
                    if (mPrevItem == -1 && mVirtualCount > 0) {
                        setCurrentItem(mCurrItem);
                    }
                }
            }
            if (!willScrolled) {
                boolean horizontal = mOrientation == HORIZONTAL;
                int scrolled = horizontal ? getScrollX() : getScrollY();
                resolveVisiblePosition(scrolled, horizontal);
                mNeedResolveFloatOffset = false;
                if (mFloatViewStartMode == FLOAT_VIEW_SCROLL || mFloatViewEndMode == FLOAT_VIEW_SCROLL) {
                    updatePositionForFloatView(scrolled, horizontal);
                }
                if ((mPageListener != null || mPageTransformer != null)) {
                    resolvePageOffset(scrolled, horizontal);
                }
            }
        } else {
            if (mNeedResolveFloatOffset) {
                mNeedResolveFloatOffset = false;
                boolean horizontal = mOrientation == HORIZONTAL;
                int scrolled = horizontal ? getScrollX() : getScrollY();
                if (mFloatViewStartIndex >= 0 && mSwapViewIndex < 0) {
                    mSwapViewIndex = computeSwapViewIndex(scrolled, horizontal);
                }
                if (mFloatViewStartMode == FLOAT_VIEW_SCROLL || mFloatViewEndMode == FLOAT_VIEW_SCROLL) {
                    updatePositionForFloatView(scrolled, horizontal);
                }
            }
        }
        super.doAfterLayout(firstAttachLayout);
    }

    @Override
    protected void dispatchLayout(int contentLeft, int contentTop, int paddingLeft, int paddingTop, int selfWidthNoPadding, int selfHeightNoPadding) {
        if (mOrientation == HORIZONTAL) {
            onLayoutHorizontal(Math.max(contentLeft, paddingLeft), contentTop, selfWidthNoPadding);
        } else {
            onLayoutVertical(contentLeft, Math.max(contentTop, paddingTop), selfHeightNoPadding);
        }
    }

    protected void onLayoutVertical(int baseLeft, int baseTop, int accessHeight) {
        int childLeft, childTop, childRight, childBottom;
        int middleWidth = getContentWidth();

        final int count = getChildCount();
        final int baseRight = baseLeft + middleWidth;
        childTop = baseTop;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            PageScrollView.LayoutParams params = (PageScrollView.LayoutParams) child.getLayoutParams();
            childTop += params.topMargin;
            childBottom = childTop + child.getMeasuredHeight();
            childLeft = getContentStartH(baseLeft, baseRight, child.getMeasuredWidth(), params.leftMargin, params.rightMargin, isChildCenter ? Gravity.CENTER : params.gravity);
            childRight = childLeft + child.getMeasuredWidth();
            child.layout(childLeft, childTop, childRight, childBottom);
            childTop = childBottom + params.bottomMargin;
            if (mMiddleMargin > 0) {
                childTop += mMiddleMargin;
            }
        }
    }

    protected void onLayoutHorizontal(int baseLeft, int baseTop, int accessWidth) {
        int childLeft, childTop, childRight, childBottom;
        int middleHeight = getContentHeight();

        final int count = getChildCount();
        final int baseBottom = baseTop + middleHeight;
        childLeft = baseLeft;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            PageScrollView.LayoutParams params = (PageScrollView.LayoutParams) child.getLayoutParams();
            childLeft += params.leftMargin;
            childRight = childLeft + child.getMeasuredWidth();
            childTop = getContentStartV(baseTop, baseBottom, child.getMeasuredHeight(), params.topMargin, params.bottomMargin, isChildCenter ? Gravity.CENTER : params.gravity);
            childBottom = childTop + child.getMeasuredHeight();
            child.layout(childLeft, childTop, childRight, childBottom);
            childLeft = childRight + params.rightMargin;
            if (mMiddleMargin > 0) {
                childLeft += mMiddleMargin;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        boolean swapIndexEnable = mFloatViewStartIndex >= 0 && mSwapViewIndex >= 0;
        if (swapIndexEnable && isChildrenDrawingOrderEnabled() == false) {
            setChildrenDrawingOrderEnabled(true);
        } else {
            if (swapIndexEnable == false) {
                setChildrenDrawingOrderEnabled(false);
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int order = i;
        if (mFloatViewStartIndex >= 0 && mSwapViewIndex >= 0) {
            if (mFloatViewStartIndex == i) {
                return mSwapViewIndex;
            }
            if (i == mSwapViewIndex) {
                return mFloatViewStartIndex;
            }
        }
        return order;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVirtualCount == 0 || !isEnabled()) {
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        if (action == MotionEvent.ACTION_MOVE) {
            handleTouchActionMove(event);
        } else {
            if (action == MotionEvent.ACTION_DOWN) {
                handleTouchActionDown(event);
            }
            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                handleTouchActionUp(event);
            }
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mVirtualCount == 0 || !isEnabled()) {
            mIsBeingDragged = false;
        } else {
            final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
            if (action == MotionEvent.ACTION_MOVE) {
                handleTouchActionMove(ev);
            } else {
                if (action == MotionEvent.ACTION_DOWN) {
                    handleTouchActionDown(ev);
                }
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    handleTouchActionUp(ev);
                }
            }
        }
        return mIsBeingDragged;
    }

    private void handleTouchActionMove(MotionEvent ev) {
        float x = ev.getX(), y = ev.getY();
        if (mIsBeingDragged) {
            scrollDxDy((int) (mPointLast.x - x), (int) (mPointLast.y - y));
            mPointLast.set(x, y);
        } else {
            int dx = (int) (mPointDown.x - x), dy = (int) (mPointDown.y - y);
            int dxAbs = Math.abs(dx), dyAbs = Math.abs(dy);
            boolean dragged, horizontal = mOrientation == HORIZONTAL;
            if (horizontal) {
                if (dragged = (dxAbs > mTouchSlop && (dxAbs * 0.6f) > dyAbs)) {
                    dx = (dx > 0 ? mTouchSlop : -mTouchSlop) >> 2;
                    dy = 0;
                }
            } else {
                if (dragged = (dyAbs > mTouchSlop && (dyAbs * 0.6f) > dxAbs)) {
                    dy = (dy > 0 ? mTouchSlop : -mTouchSlop) >> 2;
                    dx = 0;
                }
            }
            if (!dragged) {
                if (Math.max(dxAbs, dyAbs) > (mTouchSlop << 2)) {
                    dx = (int) (mPointLast.x - x);
                    dy = (int) (mPointLast.y - y);
                    dxAbs = Math.abs(dx);
                    dyAbs = Math.abs(dy);
                    if (horizontal && (dxAbs * 0.6f) > dyAbs) {
                        mPointDown.set(mPointLast);
                    }
                    if (!horizontal && (dyAbs * 0.6f) > dxAbs) {
                        mPointDown.set(mPointLast);
                    }
                }
            }
            if (dragged) {
                mIsBeingDragged = true;
                markAsWillDragged(true);
                scrollDxDy(dx, dy);
            }
            mPointLast.set(x, y);
        }
    }

    private void handleTouchActionUp(MotionEvent ev) {
        if (mIsBeingDragged) {
            mIsBeingDragged = false;
            mPointLast.set(ev.getX(), ev.getY());
            int velocityX = 0, velocityY = 0;
            final VelocityTracker velocityTracker = mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                velocityX = (int) velocityTracker.getXVelocity();
                velocityY = (int) velocityTracker.getYVelocity();
            }
            if (!flingToWhere((int) (mPointLast.x - mPointDown.x), (int) (mPointLast.y - mPointDown.y), -velocityX, -velocityY)) {
                markAsWillIdle();
            }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void handleTouchActionDown(MotionEvent ev) {
        mPointDown.set(ev.getX(), ev.getY());
        mPointLast.set(mPointDown);
        if (SCROLL_STATE_SETTLING == mScrollState) {
            OverScroller scroller = getScroller();
            scroller.computeScrollOffset();
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
                mIsBeingDragged = true;
                markAsWillDragged(true);
            }
        }
    }

    private boolean isFlingAllowed(int scrolled, int scrollRange, int velocity) {
        return !(velocity == 0 || (velocity < 0 && scrolled <= 0) || (velocity > 0 && scrolled >= scrollRange));
    }

    private boolean flingToWhere(int movedX, int movedY, int velocityX, int velocityY) {
        int scrolled, scrollRange, velocity, moved;
        boolean horizontal = mOrientation == HORIZONTAL, willScroll;
        if (horizontal) {
            scrolled = getScrollX();
            scrollRange = getHorizontalScrollRange();
            velocity = velocityX;
            moved = movedX;
        } else {
            scrolled = getScrollY();
            scrollRange = getVerticalScrollRange();
            velocity = velocityY;
            moved = movedY;
        }
        if (velocity == 0) {
            velocity = -(int) Math.signum((horizontal ? movedX : movedY));
        }
        if (willScroll = isFlingAllowed(scrolled, scrollRange, velocity)) {
            int targetIndex = mCurrItem;
            int itemSize = horizontal ? getChildAt(mCurrItem).getWidth() : getChildAt(mCurrItem).getHeight();
            int containerSize = horizontal ? mVisibleBounds.width() : mVisibleBounds.height();
            int absVelocity = velocity > 0 ? velocity : -velocity;
            int pageItemCount = getItemCount();
            if (Math.abs(moved) > mMinDistance) {
                int halfItemSize = itemSize / 2;
                if (absVelocity > mMinimumVelocity) {
                    if (velocity > 0 && mCurrItem < pageItemCount - 1 && (velocity / 10 - moved) > halfItemSize) {
                        targetIndex++;
                    }
                    if (velocity < 0 && mCurrItem > 0 && (moved - velocity / 10) > halfItemSize) {
                        targetIndex--;
                    }
                } else {
                    if (moved > halfItemSize && mCurrItem > 0) {
                        targetIndex--;
                    }
                    if (moved < -halfItemSize && mCurrItem < pageItemCount - 1) {
                        targetIndex++;
                    }
                }
                int targetScroll = computeScrollOffset(targetIndex, 0, true, horizontal);
                if (willScroll = (targetScroll != scrolled)) {
                    setCurrentItem(targetIndex);
                    int dScroll = targetScroll - scrolled;
                    int duration = computeScrollDurationForItem(dScroll, absVelocity, itemSize, containerSize);
                    if (horizontal) {
                        getScroller().startScroll(scrolled, getScrollY(), dScroll, 0, duration);
                    } else {
                        getScroller().startScroll(getScrollX(), scrolled, 0, dScroll, duration);
                    }
                    markAsWillScroll();
                }
            } else {
                if (horizontal) {
                    getScroller().fling(scrolled, getScrollY(), velocity, 0, 0, scrollRange, 0, 0, mOverFlingDistance, 0);
                } else {
                    getScroller().fling(getScrollX(), scrolled, 0, velocity, 0, 0, 0, scrollRange, 0, mOverFlingDistance);
                }
                markAsWillScroll();
            }
        }
        return willScroll;
    }

    private void scrollDxDy(int scrollDx, int scrollDy) {
        if (mOrientation == HORIZONTAL) {
            int scrollWant = getScrollX() + scrollDx;
            int scrollRange = getHorizontalScrollRange();
            if (scrollWant < 0) scrollWant = 0;
            if (scrollWant > scrollRange) scrollWant = scrollRange;
            scrollTo(scrollWant, getScrollY());
        } else {
            int scrollWant = getScrollY() + scrollDy;
            int scrollRange = getVerticalScrollRange();
            if (scrollWant < 0) scrollWant = 0;
            if (scrollWant > scrollRange) scrollWant = scrollRange;
            scrollTo(getScrollX(), scrollWant);
        }
    }

    public boolean scrollTo(int index, int offset, int duration) {
        if (mAttachLayout) {
            return scrollTo(getItemView(index), offset, duration, false);
        } else {
            if (index >= 0) {
                mScrollInfo.set(index, offset, duration, 0);
            }
        }
        return false;
    }

    public boolean scrollToCentre(int index, int offset, int duration) {
        if (mAttachLayout) {
            return scrollTo(getItemView(index), offset, duration, true);
        } else {
            if (index >= 0) {
                mScrollInfo.set(index, offset, duration, 1);
            }
        }
        return false;
    }

    public boolean scrollTo(View child, int offset, int duration, boolean childCenter) {
        int pageIndex = indexOfItemView(child);
        if (pageIndex == -1) return false;
        if (mAttachLayout) {
            if (mScrollInfo.left >= 0) {
                mScrollInfo.set(-1, -1, -1, -1);
            }
            boolean horizontal = mOrientation == HORIZONTAL;

            int scrolled, targetScroll, scrollRange, containerSize;
            if (horizontal) {
                containerSize = mVisibleBounds.width();
                targetScroll = offsetX(child, childCenter, true) + offset;
                scrolled = getScrollX();
                scrollRange = getHorizontalScrollRange();
            } else {
                containerSize = mVisibleBounds.height();
                targetScroll = offsetY(child, childCenter, true) + offset;
                scrolled = getScrollY();
                scrollRange = getVerticalScrollRange();
            }
            targetScroll = Math.max(0, Math.min(scrollRange, targetScroll));
            if (targetScroll != scrolled) {
                setCurrentItem(pageIndex);
                int dScroll = targetScroll - scrolled;
                if (duration < 0) {
                    duration = computeScrollDuration(Math.abs(dScroll), 0, containerSize, MAX_DURATION);
                }
                if (duration == 0) {
                    if (horizontal) {
                        scrollTo(targetScroll, getScrollY());
                    } else {
                        scrollTo(getScrollX(), targetScroll);
                    }
                } else {
                    if (horizontal) {
                        getScroller().startScroll(scrolled, getScrollY(), dScroll, 0, duration);
                    } else {
                        getScroller().startScroll(getScrollX(), scrolled, 0, dScroll, duration);
                    }
                    markAsWillScroll();
                }
                return true;
            }
        } else {
            mScrollInfo.set(pageIndex, offset, duration, childCenter ? 1 : 0);
        }
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (mVirtualCount > 0) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            x = clamp(x, mVisibleBounds.width(), getContentWidth());
            y = clamp(y, mVisibleBounds.height(), getContentHeight());
            if (x != scrollX || y != scrollY) {
                super.scrollTo(x, y);
            }
        }
    }

    protected int computeScrollOffset(View child, int offset, boolean centreWithParent, boolean horizontal) {
        int scrollRange, targetScroll;
        if (horizontal) {
            targetScroll = offsetX(child, centreWithParent, true) + offset;
            scrollRange = getHorizontalScrollRange();
        } else {
            targetScroll = offsetY(child, centreWithParent, true) + offset;
            scrollRange = getVerticalScrollRange();
        }
        return Math.max(0, Math.min(scrollRange, targetScroll));
    }

    protected int computeScrollOffset(int childPosition, int offset, boolean centreWithParent, boolean horizontal) {
        View child = getVirtualChildAt(childPosition, true);
        return child == null ? 0 : computeScrollOffset(child, offset, centreWithParent, horizontal);
    }

    protected int computeScrollDurationForItem(int willMoved, int absVelocity, int itemSized, int containerSize) {
        if (itemSized <= 0) {
            return computeScrollDuration(Math.abs(willMoved), absVelocity, containerSize, MAX_DURATION);
        }
        int duration;
        if (absVelocity > 0) {
            int halfWidth = containerSize / 2;
            float distanceRatio = distanceInfluenceForSnapDuration(Math.min(1f, Math.abs(willMoved) / (float) itemSized));
            float distance = halfWidth + halfWidth * distanceRatio;
            duration = 5 * Math.round(1000 * Math.abs(distance / absVelocity));
        } else {
            final float pageDelta = (float) Math.abs(willMoved) / itemSized;
            duration = (int) ((pageDelta + 1) * MAX_DURATION / 2);
        }
        return Math.min(duration, MAX_DURATION);
    }

    private int computeScrollDuration(int absWillMoved, int absVelocity, int containerSize, int maxDuration) {
        final int halfContainerSize = containerSize / 2;
        final float distanceRatio = Math.min(1.f, 1.f * absWillMoved / containerSize);
        final float distance = halfContainerSize + halfContainerSize *
                distanceInfluenceForSnapDuration(distanceRatio);
        final int duration;
        if (absVelocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / absVelocity));
        } else {
            duration = (int) (((absWillMoved / (float) containerSize) + 1) * maxDuration / 2);
        }
        return Math.min(duration, maxDuration);
    }

    @Override
    public void computeScroll() {
        boolean scrollerWorkNow = SCROLL_STATE_SETTLING == mScrollState;
        OverScroller scroller = scrollerWorkNow ? getScroller() : null;
        if (scroller != null && scroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (scrollerWorkNow) {
                markAsWillIdle();
            }
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    private void enableLayers(boolean enable) {
        final int childCount = getChildCount();
        final int layerType = enable ? ViewCompat.LAYER_TYPE_HARDWARE : ViewCompat.LAYER_TYPE_NONE;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewCompat.setLayerType(child, layerType, null);
        }
    }

    private boolean setCurrentItem(int willItem) {
        if (mCurrItem != willItem || mPrevItem == -1) {
            int preItem = mCurrItem == willItem ? mPrevItem : mCurrItem;
            mPrevItem = mCurrItem;
            mCurrItem = willItem;
            if (isLogAccess()) {
                print(String.format("selectChange  $$$$:%d >>>>>>>>> %d", preItem, mCurrItem));
            }
            if (mPageListener != null) {
                mPageListener.onPageSelected(willItem, preItem);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onScrollStateChanged(int newState, int prevState) {
        if (mPageListener != null) {
            mPageListener.onScrollStateChanged(newState, prevState);
        }
        if (mPageTransformer != null) {
            // PageTransformers can do complex things that benefit from hardware layers.
            enableLayers(newState != SCROLL_STATE_IDLE);
        }
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, Rect visibleBounds, boolean fromScrollChanged) {
        mNeedResolveFloatOffset = false;
        boolean horizontal = mOrientation == HORIZONTAL;
        int scrolled = horizontal ? scrollX : scrollY;
        resolveVisiblePosition(scrolled, horizontal);
        if (mFloatViewStartIndex >= 0) {
            mSwapViewIndex = computeSwapViewIndex(scrolled, horizontal);
        }
        if (mFloatViewStartMode == FLOAT_VIEW_SCROLL || mFloatViewEndMode == FLOAT_VIEW_SCROLL) {
            updatePositionForFloatView(scrolled, horizontal);
        }
        if (mPageListener != null || mPageTransformer != null) {
            resolvePageOffset(scrolled, horizontal);
        }
        super.onScrollChanged(scrollX, scrollY, visibleBounds, fromScrollChanged);
    }

    protected int computeSwapViewIndex(int scrolled, boolean horizontal) {
        if (mFloatViewStartIndex >= 0) {
            int count = getChildCount(), baseLine;
            View view = getChildAt(mFloatViewStartIndex);
            baseLine = (horizontal ? view.getRight() : view.getBottom()) + scrolled;
            for (int i = mFloatViewStartIndex + 1; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == View.GONE)
                    continue;
                if (horizontal) {
                    if (child.getRight() >= baseLine) {
                        return i;
                    }
                } else {
                    if (child.getBottom() >= baseLine) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    protected void resetPositionForFloatView(int realChildIndex, boolean horizontal) {
        View child = realChildIndex >= 0 ? getChildAt(realChildIndex) : null;
        if (child != null) {
            child.setTranslationX(0);
            child.setTranslationY(0);
        }
    }


    private void updatePositionForFloatView(int scrolled, boolean horizontal) {
        float viewTranslated;
        int wantTranslated;
        //TODO FLOAT MARGIN SHOULD MAKE AS A ATTRIBUTION.
        if (mFloatViewStartMode == FLOAT_VIEW_SCROLL) {
            View view = getItemView(mFloatViewStartIndex);
            PageScrollView.LayoutParams params = (LayoutParams) view.getLayoutParams();
            if (horizontal) {
                wantTranslated = scrolled - view.getLeft();
                viewTranslated = view.getTranslationX();
            } else {
                wantTranslated = scrolled - view.getTop();
                viewTranslated = view.getTranslationY();
            }
            wantTranslated = Math.max(0, wantTranslated);
            if (wantTranslated != viewTranslated) {
                if (horizontal) {
                    view.setTranslationX(wantTranslated);
                } else {
                    view.setTranslationY(wantTranslated);
                }
            }
        }
        if (mFloatViewEndMode == FLOAT_VIEW_SCROLL) {
            View view = getItemView(mFloatViewEndIndex);
            PageScrollView.LayoutParams params = (LayoutParams) view.getLayoutParams();
            int scrollRange;
            if (horizontal) {
                scrollRange = getHorizontalScrollRange();
                wantTranslated = scrolled - scrollRange + (getContentWidth() - view.getRight());
                viewTranslated = view.getTranslationX();
            } else {
                scrollRange = getVerticalScrollRange();
                wantTranslated = scrolled - scrollRange + (getContentHeight() - view.getBottom());
                viewTranslated = view.getTranslationY();
            }
            wantTranslated = Math.min(0, wantTranslated);
            if (wantTranslated != viewTranslated) {
                if (horizontal) {
                    view.setTranslationX(wantTranslated);
                } else {
                    view.setTranslationY(wantTranslated);
                }
            }
        }
    }

    private void resolveVisiblePosition(int scrolled, boolean horizontal) {
        int visibleStart, visibleEnd;
        if (horizontal) {
            visibleStart = mVisibleBounds.left;
            visibleEnd = mVisibleBounds.right;
        } else {
            visibleStart = mVisibleBounds.top;
            visibleEnd = mVisibleBounds.bottom;
        }
        int childCount = getChildCount(), counted = 0;
        int firstVisible = -1, lastVisible = -1;
        boolean visible;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            if (horizontal) {
                visible = !((child.getRight() <= visibleStart) || child.getLeft() >= visibleEnd);
            } else {
                visible = !((child.getBottom() <= visibleStart) || child.getTop() >= visibleEnd);
            }
            if (visible) {
                if (firstVisible == -1) {
                    firstVisible = counted;
                }
                lastVisible = counted;
            } else {
                if (firstVisible >= 0) {
                    break;
                }
            }
            counted++;
        }
        if (firstVisible != -1) {
            if (firstVisible != mFirstVisiblePosition || lastVisible != mLastVisiblePosition) {
                int oldFirstVisible = mFirstVisiblePosition;
                int oldLastVisible = mLastVisiblePosition;
                mFirstVisiblePosition = firstVisible;
                mLastVisiblePosition = lastVisible;
                if (isLogAccess()) {
                    print(String.format("visibleRangeChanged  ****:[%d , %d]", firstVisible, lastVisible));
                }
                if (mOnVisibleRangeChangeListener != null) {
                    mOnVisibleRangeChangeListener.onVisibleRangeChanged(firstVisible, lastVisible, oldFirstVisible, oldLastVisible);
                }
            }
        }
    }

    private void resolvePageOffset(int scrolled, boolean horizontal) {
        int targetOffset = computeScrollOffset(mCurrItem, 0, true, horizontal);
        int prevIndex = mCurrItem;
        if (scrolled > targetOffset && prevIndex < mVirtualCount - 1) {
            prevIndex++;
        }
        if (scrolled < targetOffset && prevIndex > 0) {
            prevIndex--;
        }
        int minIndex, maxIndex, minOffset, maxOffset;
        if (prevIndex > mCurrItem) {
            minIndex = mCurrItem;
            minOffset = targetOffset;
            maxIndex = prevIndex;
            maxOffset = maxIndex == minIndex ? minOffset : computeScrollOffset(maxIndex, 0, true, horizontal);
        } else {
            maxIndex = mCurrItem;
            maxOffset = targetOffset;
            minIndex = prevIndex;
            minOffset = minIndex == maxIndex ? maxOffset : computeScrollOffset(minIndex, 0, true, horizontal);
        }
        int distance = maxOffset - minOffset;
        int positionOffsetPixels = 0;
        float positionOffset = 0;
        if (distance > 0) {
            positionOffsetPixels = scrolled - minOffset;
            positionOffset = positionOffsetPixels / (float) distance;
        }
        if (mPageListener != null) {
            mPageListener.onPageScrolled(minIndex, positionOffset, positionOffsetPixels);
        }
        if (mPageTransformer != null) {
            dispatchTransformPosition(scrolled, horizontal);
        }
    }

    private void dispatchTransformPosition(int scrolled, boolean horizontal) {
        int pageItemIndex = 0, childCount = getChildCount();
        int pageItemStart = Math.max(0, mFirstVisiblePosition - 1);
        int pageItemEnd = Math.min(mVirtualCount - 1, mLastVisiblePosition + 1);
        for (int i = 0; i < childCount && pageItemIndex <= pageItemEnd; i++) {
            View child = getChildAt(i);
            if (skipChild(child))
                continue;
            if (pageItemIndex >= pageItemStart) {
                PageScrollView.LayoutParams params = (LayoutParams) child.getLayoutParams();
                int contentLength = horizontal ? (child.getWidth() + params.getMarginHorizontal()) : (child.getHeight() + params.getMarginVertical());
                if (mMiddleMargin > 0) {
                    if (pageItemIndex == 0 || pageItemIndex == mVirtualCount - 1) {
                        contentLength += (mMiddleMargin / 2);
                    } else {
                        contentLength += mMiddleMargin;
                    }
                }
                float transformerPosition = (scrolled - computeScrollOffset(child, 0, true, horizontal)) / (float) contentLength;
                mPageTransformer.transformPage(child, transformerPosition, horizontal);
            }
            pageItemIndex++;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();
        mFirstVisiblePosition = -1;
        mLastVisiblePosition = -1;
        mCurrItem = 0;
        mPrevItem = -1;
        mVirtualCount = 0;
        mScrollInfo.set(-1, -1, -1, -1);
    }

    @Override
    protected boolean skipChild(View child) {
        return super.skipChild(child);
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }

    public static int getMiddleChildMeasureSpec(int spec, int parentSize, int padding, int childDimension) {
        if (ViewGroup.LayoutParams.MATCH_PARENT == childDimension && parentSize > 0) {
            childDimension = parentSize;
        }
        return getChildMeasureSpec(spec, padding, childDimension);
    }

    public interface PageTransformer {
        void transformPage(View view, float position, boolean horizontal);

        void recoverTransformPage(View view, boolean horizontal);
    }

    public interface OnPageChangeListener extends OnScrollChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position, int oldPosition);

    }

    public interface OnVisibleRangeChangeListener {
        void onVisibleRangeChanged(int firstVisible, int lastVisible, int oldFirstVisible, int oldLastVisible);
    }
}
