package com.maibei.merchants.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.maibei.merchants.utils.ToastUtil;

import java.io.File;

/**
 * Created by 14658 on 2016/7/7.
 */
public class UpdateBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            ToastUtil.showToast(context, "有应用被添加");
        }
        else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
            ToastUtil.showToast(context, "有应用被删除");
        }
        else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+File.separator+"maibei";
            deleteFile(new File(path));
            ToastUtil.showToast(context, "应用被替换");
        }
    }

    /**
     * 删除文件
     * @param oldPath
     */
    public static void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
                deleteFile(file);
                file.delete();
            }
        }else{
            oldPath.delete();
        }
    }
}
