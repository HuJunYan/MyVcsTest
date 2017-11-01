package com.tianshen.cash.utils;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class ImageLoader {

    public static void load(Activity activity, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(activity).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().placeholder(placeholder).crossFade().into(iv);
    }

    public static void load(Activity activity, String imageUrl, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(activity).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().crossFade().into(iv);
    }

    public static void load(Fragment fragment, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(fragment).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().placeholder(placeholder).crossFade().into(iv);
    }

    public static void load(Fragment fragment, String imageUrl, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(fragment).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().crossFade().into(iv);
    }

    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).crossFade().into(view);
    }

    public static void load(Context context, String imageUrl, final ImageView view) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }

        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).crossFade().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                view.setImageDrawable(resource);
            }
        });
    }

    public static void load(Context context, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().placeholder(placeholder).crossFade().into(iv);
    }

    public static void loadCache(Context context, String imageUrl, @DrawableRes int placeholder, ImageView iv) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(context).load(imageUrl).centerCrop().placeholder(placeholder).crossFade().into(iv);
    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }

}
