package com.maibai.user.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.maibai.user.R;
import com.maibai.user.view.CircleDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ImageLoaderUtil {
    Context mContext;
    ImageLoader imageLoader;
    ImageLoaderConfiguration configuration;
    public ImageLoaderUtil(Context mContext) {
        this.mContext = mContext;
        configuration = new ImageLoaderConfiguration.Builder(mContext)
                .memoryCacheExtraOptions(480, 800)//内存缓存文件的最大长宽
                .diskCacheExtraOptions(480, 800, null)//本地缓存的详细信息
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)//设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO)//设置任务的处理顺序
                .denyCacheImageMultipleSizesInMemory()//防止内存中图片重复
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))//设置自己的内存缓存大小   2M
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)//内存缓存百分比

                .diskCache(new UnlimitedDiskCache(new File(getPath("maibei"))))//设置缓存的图片在sdcard中的位置
                .diskCacheSize(50 * 1024 * 1024)//硬盘缓存大小    50M
                .diskCacheFileCount(100)//硬盘缓存文件个数
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//md5加密的方式，或new HashCodeFileNameGenerator()
                .imageDownloader(new BaseImageDownloader(mContext))
                .imageDecoder(new BaseImageDecoder(true))//图片解码
                .writeDebugLogs()
                .build();
    }
    public ImageLoader getImageLoader(){
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        return imageLoader;
    }
    public DisplayImageOptions getCircleOption(){
        DisplayImageOptions circleOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.loading_img)
                .showImageOnFail(R.mipmap.loads_failure)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CircleDisplayer())
                .build();
        return circleOptions;
    }
    public DisplayImageOptions getNormalOption(){
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.loading_img)
//                .showImageOnFail(R.mipmap.loads_failure)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

    public String getPath(String filePath) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + filePath + "/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

}
