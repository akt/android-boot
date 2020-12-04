package com.wacai.android.overscroll.adapters;

import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by zisha on 2017/5/18.
 */

public class MultiViewOverScrollDecoratorAdapter implements IOverScrollDecoratorAdapter {


    protected final View mView;
    protected final View mInsideView;
    private IOverScrollDecoratorAdapter mOverScrollDecoratorProxyAdapter;
    private int mDirection = SLIDING_DIRECTION_BOTH_WAY;

    public MultiViewOverScrollDecoratorAdapter(View view, View insideView) {
        mView = view;
        mInsideView = insideView;

        if (mInsideView instanceof AbsListView) {
            mOverScrollDecoratorProxyAdapter = new AbsListViewOverScrollDecorAdapter((AbsListView) insideView);
        } else if (mInsideView instanceof HorizontalScrollView) {
            mOverScrollDecoratorProxyAdapter = new HorizontalScrollViewOverScrollDecorAdapter((HorizontalScrollView) insideView);
        } else if (mInsideView instanceof ScrollView) {
            mOverScrollDecoratorProxyAdapter = new ScrollViewOverScrollDecorAdapter((ScrollView) insideView);
        } else if (mInsideView instanceof ScrollView) {
            mOverScrollDecoratorProxyAdapter = new ScrollViewOverScrollDecorAdapter((ScrollView) insideView);
        } else {
            mOverScrollDecoratorProxyAdapter = new StaticOverScrollDecorAdapter(insideView);
        }
    }

    public MultiViewOverScrollDecoratorAdapter(View view, View insideView, int direction) {
        this(view, insideView);
        mDirection = direction;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public View getInsideView() {
        return mInsideView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return mDirection != SLIDING_DIRECTION_END && mOverScrollDecoratorProxyAdapter.isInAbsoluteStart();
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return mDirection != SLIDING_DIRECTION_START && mOverScrollDecoratorProxyAdapter.isInAbsoluteEnd();
    }
}