package com.tianshen.cash.utils;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

    public static void load(Activity activity, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(activity).load(imageUrl).fitCenter().placeholder(placeholder).crossFade().into(iv);
    }

    public static void load(Activity activity, String imageUrl, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(activity).load(imageUrl).fitCenter().crossFade().into(iv);
    }

    public static void load(Fragment fragment, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(fragment).load(imageUrl).fitCenter().placeholder(placeholder).crossFade().into(iv);
    }

    public static void load(Fragment fragment, String imageUrl, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(fragment).load(imageUrl).fitCenter().crossFade().into(iv);
    }

    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).crossFade().into(view);
    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }

}
