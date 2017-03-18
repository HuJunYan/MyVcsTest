package com.maibei.merchants.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by Administrator on 2016/12/13.
 */

public class DetialGallery extends Gallery {
    public DetialGallery(Context context , AttributeSet attrSet) {
        super(context,attrSet);
        // TODO Auto-generated constructor stub
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
         return false;
    }
}