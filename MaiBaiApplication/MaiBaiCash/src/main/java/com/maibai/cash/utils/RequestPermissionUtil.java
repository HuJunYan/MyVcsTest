package com.maibai.cash.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/10/31.
 */

public class RequestPermissionUtil {
    Context mContext;

    public RequestPermissionUtil(Context mContext) {
        this.mContext = mContext;
    }

    private final int CAMERA_JAVA_REQUEST_CODE = 2;
    private String[] PERMISSTION_CAMERA_CONTACTS_LOCATION={
            Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION
    };
    public void requestCameraContactsLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermition=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.CAMERA);
            int contactsPermittion=ContextCompat.checkSelfPermission((Activity) mContext,Manifest.permission.READ_CONTACTS);
            int locationPermisstion=ContextCompat.checkSelfPermission((Activity)mContext,Manifest.permission.ACCESS_FINE_LOCATION);
            if (cameraPermition!= PackageManager.PERMISSION_GRANTED||contactsPermittion!=PackageManager.PERMISSION_GRANTED||locationPermisstion!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSTION_CAMERA_CONTACTS_LOCATION, CAMERA_JAVA_REQUEST_CODE);
            } else {

            }
        } else {
            return;
        }
    }
}
