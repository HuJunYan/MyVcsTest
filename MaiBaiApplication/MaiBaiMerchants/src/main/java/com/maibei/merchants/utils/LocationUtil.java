package com.maibei.merchants.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by zhangchi on 2016/6/30.
 */
public class LocationUtil implements BDLocationListener {
    private static LocationUtil mLocationUtil;
    private LocationClient mLocationClient;
    private BDLocation mLocation;
    private MainLocationListener mMainListener;
    private RequestLocationListener mReqListener;
    private boolean isLocationRunning = false;

    private final int SCANSPAN = 1000 * 60 * 10;
    private boolean isRequest = false;
    private Context mContext;

    private LocationUtil(Context context) {
        mContext = context;
        mLocationClient = new LocationClient(context.getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(this);
    }

    public static LocationUtil getInstance(Context context) {
        if (mLocationUtil == null) {
            mLocationUtil = new LocationUtil(context);
        }
        return mLocationUtil;
    }

    public BDLocation getLocation() {
        return mLocation;
    }

    public void setMainListener(MainLocationListener mainListener) {
        this.mMainListener = mainListener;
    }

    public void setReqListener(RequestLocationListener reqListener) {
        this.mReqListener = reqListener;
    }

    public void startLocation() {
        Log.d("ret", "startLocation   ==== 0");
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            Log.d("ret", "startLocation   ==== 1");
            if (!isLocationRunning) {
                Log.d("ret", "startLocation   ==== 2");
//                mLocationClient = new LocationClient(mContext.getApplicationContext());
//                initLocation();
//                mLocationClient.registerLocationListener(this);
                mLocationClient.start();
                isLocationRunning = true;
            }
        } else if (mLocationClient == null) {
            Log.d("ret", "startLocation   ==== 3");
        } else {
            Log.d("ret", "startLocation   ==== 4");
        }
    }

    public void requestLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            isRequest = true;
            mLocationClient.requestLocation();
        }
    }

    public void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            if (isLocationRunning) {
                mLocationClient.stop();
                isLocationRunning = false;
            }
        }
    }
    public String getProvinceName(){
        SharedPreferencesUtil share=SharedPreferencesUtil.getInstance(mContext);
        return share.getString("province");
    }
    public String getCityName(){
        SharedPreferencesUtil share=SharedPreferencesUtil.getInstance(mContext);
        return share.getString("city");
    }
    public String getCountryName(){
        SharedPreferencesUtil share=SharedPreferencesUtil.getInstance(mContext);
        return share.getString("country");
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            mLocation = bdLocation;
          /*  Address address = bdLocation.getAddress();
            SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(mContext);
            share.putString("location", bdLocation.getLatitude() + "," + bdLocation.getLongitude());
            share.putString("province", address.province);
            share.putString("city", address.city);
            share.putString("country", address.district);
            share.putString("address", address.address);
//            Log.d("ret", "province = " + address.province + " ; city = " + address.city + " ; country = " + address.district);*/
        }
        if (isRequest) {
            if (mReqListener != null) {
                mReqListener.onRequestReceiveLocation(bdLocation);
                isRequest = false;
            }
        } else if (mMainListener != null) {
            mMainListener.onMainReveiveLocation(bdLocation);
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(SCANSPAN);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public interface MainLocationListener {
        void onMainReveiveLocation(BDLocation bdLocation);
    }

    public interface RequestLocationListener {
        void onRequestReceiveLocation(BDLocation bdLocation);
    }


}
