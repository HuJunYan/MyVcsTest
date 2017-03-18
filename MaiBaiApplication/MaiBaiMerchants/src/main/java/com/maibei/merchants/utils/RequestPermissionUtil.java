package com.maibei.merchants.utils;

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

    // Storage Permissions
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private final int CAMERA_JAVA_REQUEST_CODE = 2;
    private final int CONTACTS_CODE = 3;
    private final int LOCATION_CODE = 4;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String[] PERMISSION_CAMERA = {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public String[] PERMISSION_CONTACTS = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,Manifest.permission.GET_ACCOUNTS};
    public String[] LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public void requestSDCardPermission() {
        //api23以上访问sd卡的权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int sdCardPermission=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (sdCardPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        (Activity) mContext,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            } else {

            }
        }
    }

    public void requestCameraPermission() {
        //api23以上摄像头权限的申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.CAMERA);
            int storagePermission=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission_group.STORAGE);
            if (cameraPermission!= PackageManager.PERMISSION_GRANTED||storagePermission!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSION_CAMERA, CAMERA_JAVA_REQUEST_CODE);
//                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE );
            } else {

            }
        } else {
            return;
        }
    }

    public void requestContactsPermission() {
        //api23以上联系人权限的申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int contactsPermission=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.READ_CONTACTS);
            if (contactsPermission!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSION_CONTACTS, CONTACTS_CODE);
            } else {

            }
        } else {
            return;
        }
    }

    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int locationPermition=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (locationPermition!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, LOCATION, LOCATION_CODE);
            } else {

            }
        } else {
            return;
        }
    }
}
