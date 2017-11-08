package com.tianshen.cash.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by wang on 2017/11/8.
 */

public class EqualWHAnimationImageView extends ImageView {

    private ObjectAnimator oa;

    public EqualWHAnimationImageView(Context context) {
        this(context, null);
    }

    public EqualWHAnimationImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualWHAnimationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        oa = ObjectAnimator.ofFloat(this, "degree", 0, 360);
        oa.setDuration(7000);
        oa.setInterpolator(new LinearInterpolator());
        oa.setRepeatCount(ValueAnimator.INFINITE);
        oa.setRepeatMode(ValueAnimator.RESTART);
    }

    private void setDegree(float degree) {
        setRotation(degree);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        oa.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        oa.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
