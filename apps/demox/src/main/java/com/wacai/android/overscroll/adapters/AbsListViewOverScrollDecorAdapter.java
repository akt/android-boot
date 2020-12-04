package com.wacai.android.overscroll.adapters;

import android.view.View;
import android.widget.AbsListView;

import com.wacai.android.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.wacai.android.overscroll.VerticalOverScrollBounceEffectDecorator;

/**
 * An adapter to enable over-scrolling over object of {@link AbsListView}, namely {@link
 * android.widget.ListView} and it's extensions, and {@link android.widget.GridView}.
 *
 * @author amit
 * @see HorizontalOverScrollBounceEffectDecorator
 * @see VerticalOverScrollBounceEffectDecorator
 */
public class AbsListViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final AbsListView mView;
    private int mDirection = SLIDING_DIRECTION_BOTH_WAY;

    public AbsListViewOverScrollDecorAdapter(AbsListView view) {
        mView = view;
    }

    public AbsListViewOverScrollDecorAdapter(AbsListView view, int direction) {
        this(view);
        mDirection = direction;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public View getInsideView() {
        return null;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return mDirection != SLIDING_DIRECTION_END && mView.getChildCount() > 0 && !canScrollListUp();
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return mDirection != SLIDING_DIRECTION_START && mView.getChildCount() > 0 && !canScrollListDown();
    }

    public boolean canScrollListUp() {
        // Ported from AbsListView#canScrollList() which isn't compatible to all API levels
        final int firstTop = mView.getChildAt(0).getTop();
        final int firstPosition = mView.getFirstVisiblePosition();
        return firstPosition > 0 || firstTop < mView.getListPaddingTop();
    }

    public boolean canScrollListDown() {
        // Ported from AbsListView#canScrollList() which isn't compatible to all API levels
        final int childCount = mView.getChildCount();
        final int itemsCount = mView.getCount();
        final int firstPosition = mView.getFirstVisiblePosition();
        final int lastPosition = firstPosition + childCount;
        final int lastBottom = mView.getChildAt(childCount - 1).getBottom();
        return lastPosition < itemsCount || lastBottom > mView.getHeight() - mView.getListPaddingBottom();

    }
}
