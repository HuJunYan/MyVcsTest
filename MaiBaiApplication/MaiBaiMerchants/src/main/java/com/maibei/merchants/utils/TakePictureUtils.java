package com.maibei.merchants.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guochunpeng on 16-8-26.
 */
public class TakePictureUtils {
    private Activity activity;

    private Context context;
    public TakePictureUtils(Activity activity) {
        this.activity = activity;
    }

    public TakePictureUtils(Context context) {
        this.context = context;
    }

    public void getCamera(int requestCode, String path){
        Intent takePictureFromCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureFromCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        activity.startActivityForResult(takePictureFromCameraIntent, requestCode);
    }
    public void resizeBitmap(String path){
        int maxWidth=811,maxHeight=1092;
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        int originWidth=bitmap.getWidth();
        int originHeight=bitmap.getHeight();
        if(originWidth<maxWidth&&originHeight<maxHeight){
            return ;
        }
        int width=originWidth;
        int height=originHeight;

        if(originWidth>maxWidth) {
            width=maxWidth;
            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        }
        if(height>maxHeight){
            height=maxHeight;
            bitmap=Bitmap.createBitmap(bitmap,0,0,width,height);
        }
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
