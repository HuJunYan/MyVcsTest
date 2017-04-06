package com.tianshen.cash.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/8/19.
 */
public class TelephoneScreenChangeUtils {
    Context mContext;

    public TelephoneScreenChangeUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void changeDark(){
        dimBackground(1.0f, 0.5f);
    }
    public void changeLight(){
        dimBackground(0.5f,1.0f);
    }

    public void dimBackground(final float from, final float to) {
        final Window window =((Activity) mContext).getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();}
}
