package com.zsxhqg.baseadapterhelper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * TODO: document your custom view class.
 */
public class MyLayout extends LinearLayout {

    private int mHeaderViewId;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private float mLastY;

    public MyLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyLayout, defStyle, 0);
        mHeaderViewId = a.getResourceId(R.styleable.MyLayout_headerView, -1);
        a.recycle();
        // add header view
        if (mHeaderViewId != -1) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            mHeaderView = inflater.inflate(mHeaderViewId, this, false);
            this.addView(mHeaderView, 0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (mHeaderViewHeight == 0) {
            mHeaderViewHeight = mHeaderView.getHeight();
        }
        int action = ev.getAction();
        int currentHeight = mHeaderView.getHeight();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int offset = (int) (ev.getY() - mLastY);
                mLastY = ev.getY();
                int height = currentHeight + offset;
                if (height < 0) {
                    height = 0;
                } else if (height > mHeaderViewHeight) {
                    height = mHeaderViewHeight;
                }
                LayoutParams layoutParams
                        = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                mHeaderView.setLayoutParams(layoutParams);
                return (currentHeight == 0 || currentHeight == mHeaderViewHeight) && super.dispatchTouchEvent(ev);

            case MotionEvent.ACTION_UP:
                if (currentHeight > mHeaderViewHeight / 2) {
                    setHeadViewState(currentHeight, mHeaderViewHeight);
                } else {
                    setHeadViewState(currentHeight, 0);
                }
                return (currentHeight == 0 || currentHeight == mHeaderViewHeight) && super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 更改HeadView 的状态
     *
     * @param from 改变前高度
     * @param to   改变后高度
     */
    private synchronized boolean setHeadViewState(int from, int to) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (Integer) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
                mHeaderView.setLayoutParams(params);
            }
        });
        animator.start();
        return to == 0;
    }

}
