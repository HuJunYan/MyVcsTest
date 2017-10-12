package com.tianshen.cash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wang on 2017/10/12. //宽度为高度的一半
 */

public class HalfHeightImageView extends ImageView {
    public HalfHeightImageView(Context context) {
        super(context);
    }

    public HalfHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth / 2);
    }
}
