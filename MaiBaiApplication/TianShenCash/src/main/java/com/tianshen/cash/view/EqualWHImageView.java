package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wang on 2017/11/8.
 */

public class EqualWHImageView extends ImageView {
    public EqualWHImageView(Context context) {
        super(context);
    }

    public EqualWHImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EqualWHImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth,measuredWidth);
    }
}
