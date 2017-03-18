package com.nostra13.universalimageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by Administrator on 2016/12/6.
 */

public class BGImageViewAware extends ImageViewAware {
    public BGImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public BGImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        //重写父类方法，将图片设为背景
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = (ImageView) this.viewRef.get();
            if (imageView != null) {
                imageView.setBackground(new BitmapDrawable(bitmap));
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean setImageDrawable(Drawable drawable) {
        return super.setImageDrawable(drawable);
    }

}
