package com.wacai.android.overscroll.adapters;

import android.view.View;

import com.wacai.android.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.wacai.android.overscroll.VerticalOverScrollBounceEffectDecorator;

/**
 * A static adapter for views that are ALWAYS over-scroll-able (e.g. image view).
 *
 * @author amit
 * @see HorizontalOverScrollBounceEffectDecorator
 * @see VerticalOverScrollBounceEffectDecorator
 */
public class StaticOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final View mView;
    private int mDirection = SLIDING_DIRECTION_BOTH_WAY;

    public StaticOverScrollDecorAdapter(View view) {
        mView = view;
    }

    public StaticOverScrollDecorAdapter(View view, int direction) {
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
        return mDirection != SLIDING_DIRECTION_END;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return mDirection != SLIDING_DIRECTION_START;
    }
}
