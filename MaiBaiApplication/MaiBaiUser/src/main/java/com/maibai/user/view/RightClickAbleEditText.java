package com.maibai.user.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by zhangchi on 2016/6/28.
 */
public class RightClickAbleEditText extends EditText {
    private DrawableRightListener mRightListener;
    private final int DRAWABLE_RIGHT = 2;

    public RightClickAbleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnDrawableRightListener(DrawableRightListener listener) {
        this.mRightListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width() - 50)) {
                        mRightListener.onDrawableRightClick(this);
                        return true;
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public interface DrawableRightListener {
        public void onDrawableRightClick(View view);
    }
}
