package com.maibai.user.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.maibai.user.R;
import com.maibai.user.activity.ActivateTheQuotaActivity;
import com.maibai.user.activity.CaptureActivity;
import com.maibai.user.activity.ContactsInfoActivity;
import com.maibai.user.activity.ImproveQuotaActivity;
import com.maibai.user.activity.LoginActivity;
import com.maibai.user.activity.MainActivity;
import com.maibai.user.activity.PayActivity;
import com.maibai.user.activity.ResultActivity;
import com.maibai.user.activity.SearchLocationActivity;
import com.maibai.user.activity.ShopListActivity;
import com.maibai.user.adapter.MerchantAdapter;
import com.maibai.user.adapter.ShopListAdapter;
import com.maibai.user.base.BaseFragment;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.MerchantTypeItemBean;
import com.maibai.user.model.MerchantTypeListBean;
import com.maibai.user.model.NearByMerchantItemBean;
import com.maibai.user.model.NearByMerchantListBean;
import com.maibai.user.model.SignInBean;
import com.maibai.user.net.api.GetMerchantTypeList;
import com.maibai.user.net.api.GetNearByList;
import com.maibai.user.net.api.SignIn;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.FormatUtil;
import com.maibai.user.utils.LocationUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.MainUtil;
import com.maibai.user.utils.PermissionUtils;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.UploadToServerUtil;
import com.maibai.user.utils.Utils;
import com.maibai.user.utils.ViewUtil;
import com.maibai.user.view.MyGridView;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class ZeroYuanBuyFragment extends BaseFragment implements ShopListAdapter.OnShopItemClickListener, AdapterView.OnItemClickListener, View.OnClickListener, TitleBar.TitleBarListener, LocationUtil.MainLocationListener, UploadToServerUtil.UploadCallBack {
//    private boolean isSignInFirstSuccess = true;
    private int begin_line, end_line;
    private boolean isFirst = true;//用户是否初次进入该界面
    private boolean mIsll_zero_quotaCanClick = true;
    private boolean mIsCallRecordPassByServer = false;
    private boolean mIsContactsPassByServer = false;
    private boolean isUpLoadCallContacts = false, isUploadCallRecord = false;//上传状态：NOT_UPLOAD代表未上传，UPLOAD_FAIL代表上传失败，UPLOAD_SUCCESS代表上传成功
    private String mLastLocationNam;
    private TitleBar tb_title;
    private TextView tv_home_scan;
    private TextView tv_balance_quota;
    private TextView tv_balance_quota_before;
    private TextView tv_balance_quota_after;
    private TextView tv_my_quota;
    private View ll_zero_quota;
    private View banner_view;
    private ListView lv_zero_shop;
    private MyGridView gv_merchant_type;
    private ShopListAdapter mShopListAdapter;
    private List<MerchantTypeItemBean> merchantTypeListBeen = new ArrayList<MerchantTypeItemBean>();
    private List<NearByMerchantItemBean> mShopList = new ArrayList<NearByMerchantItemBean>();
    private List<NearByMerchantItemBean> currentShopList = new ArrayList<NearByMerchantItemBean>();

    private GetNearByList mGetNearByList;
    private LatLng mLatLng;
    private LocationUtil mLocationUtil;
    //    PullToRefreshView pullToRefreshView;
    private MyBroadcastReciver myBroadcastReciver = new MyBroadcastReciver();

    private int SHOP_UPDATE_LENGTH = 1000;//每次加载更多时添加的数量；
    private UploadToServerUtil uploadUtil;

    @Override
    protected void initVariable() {

        SharedPreferencesUtil.getInstance(mContext).putLong(GlobalParams.LAST_UPDATE_NEAR_SHOP_LIST_TIME, 0l);
        init();
    }


    private void init() {
        ViewUtil.createLoadingDialog((Activity) mContext, "", true);
        mGetNearByList = new GetNearByList(mContext);
        mShopListAdapter = new ShopListAdapter(mContext, currentShopList, this);
        lv_zero_shop.setAdapter(mShopListAdapter);
        mLocationUtil = LocationUtil.getInstance(mContext);
        mLocationUtil.setMainListener(this);

        BDLocation location = mLocationUtil.getLocation();
        if (location != null && location.getPoiList()!=null) {
            Log.e("getnearShopError:", "定位成功");
            mLastLocationNam = ((Poi) location.getPoiList().get(0)).getName();
            tb_title.setAddress(mLastLocationNam);
            getMerchantTypeList();
            SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.LAST_LOCATION_NAME_KEY, mLastLocationNam);
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            mLastLocationNam = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.LAST_LOCATION_NAME_KEY, "");
            if (!"".equals(mLastLocationNam)) {
                tb_title.setAddress(mLastLocationNam);
                getMerchantTypeList();
            }
        }
        getNearShopList(0, true);
        initHeadView();
        if(MainUtil.isLogin(mContext)){
            freshBalanceAmount(true);
        }
        registerReceiver();
        uploadUtil = new UploadToServerUtil(mContext);
        uploadUtil.setCallBack(this);
    }

    private void getMerchantTypeList(){
        try {
            GetMerchantTypeList getMerchantTypeList=new GetMerchantTypeList(mContext);
            JSONObject jsonObject=new JSONObject();
            BDLocation bdLocation =mLocationUtil.getLocation();
            if (bdLocation == null) {
                String provinceName=mLocationUtil.getProvinceName();
                String cityName=mLocationUtil.getCityName();
                String countryName=mLocationUtil.getCountryName();
                if(null!=provinceName&&!"".equals(provinceName)&&null!=cityName&&!"".equals(cityName)&&null!=countryName&&!"".equals(countryName)){
                    jsonObject.put("province_name",provinceName);
                    jsonObject.put("city_name",cityName);
                    jsonObject.put("county_name",countryName);
                }else{
                    return;
                }
            }else {
                jsonObject.put("province_name", bdLocation.getProvince());
                jsonObject.put("city_name", bdLocation.getCity());
                jsonObject.put("county_name", bdLocation.getDistrict());
            }
            getMerchantTypeList.getMerchantTypeList(jsonObject, new BaseNetCallBack<MerchantTypeListBean>() {
                @Override
                public void onSuccess(MerchantTypeListBean paramT) {
                    merchantTypeListBeen=paramT.getData();
                    MerchantAdapter adapter = new MerchantAdapter(mContext, paramT.getData());
                    gv_merchant_type.setAdapter(adapter);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Log.d("faile",errorCode+"");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void initHeadView() {
        if (!MainUtil.isLogin(mContext)) {
            tv_my_quota.setText("登录");
            tv_balance_quota.setText("10000");
            tv_balance_quota.setTextColor(0xFF7D7D7D);
            tv_balance_quota_before.setTextColor(0xFF7D7D7D);
            tv_balance_quota_after.setTextColor(0xFF7D7D7D);
            Drawable drawable = getResources().getDrawable(R.mipmap.home_up);
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
            tv_my_quota.setCompoundDrawables(null, null, drawable, null);
            ll_zero_quota.setClickable(true);
        } else {
            tv_balance_quota.setTextColor(0xffffffff);
            tv_balance_quota_before.setTextColor(0xffffffff);
            tv_balance_quota_after.setTextColor(0xffffffff);
            String showAmount = new FormatUtil().formatDouble(Double.parseDouble(UserUtil.getBalanceAmount(mContext)) / 100 + 500);
            tv_balance_quota.setText(showAmount);
            tv_my_quota.setText("我的额度");
            tv_my_quota.setTextColor(0xffffffff);
            ll_zero_quota.setClickable(false);
            tv_my_quota.setCompoundDrawables(null, null, null, null);
        }
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_zero_yuan_buy;
    }

    @Override
    protected void findViews(View rootView) {
        banner_view = LayoutInflater.from(mContext).inflate(R.layout.banner_zero_yuan, null);
        tb_title = (TitleBar) rootView.findViewById(R.id.tb_title);
        ll_zero_quota = banner_view.findViewById(R.id.ll_zero_quota);
        lv_zero_shop = (ListView) rootView.findViewById(R.id.lv_zero_shop);
        gv_merchant_type = (MyGridView) banner_view.findViewById(R.id.gv_merchant_type);
        tv_home_scan = (TextView) banner_view.findViewById(R.id.tv_home_scan);
        tv_balance_quota = (TextView) banner_view.findViewById(R.id.tv_balance_quota);
        tv_balance_quota_before = (TextView) banner_view.findViewById(R.id.tv_balance_quota_before);
        tv_balance_quota_after = (TextView) banner_view.findViewById(R.id.tv_balance_quota_after);
        tv_my_quota = (TextView) banner_view.findViewById(R.id.tv_my_quota);
        lv_zero_shop.addHeaderView(banner_view);
        isFirst = false;
    }


    @Override
    protected void setListensers() {
        gv_merchant_type.setOnItemClickListener(this);
        tv_home_scan.setOnClickListener(this);
        ll_zero_quota.setOnClickListener(this);
        tb_title.setListener(this);
        lv_zero_shop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//list停止滚动时加载数据
                    currentShopList.clear();
                    for (int i = 0; i <= end_line + 10; i++) {
                        if (i < mShopList.size())
                            currentShopList.add(mShopList.get(i));
                    }
                    mShopListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount + firstVisibleItem > end_line) {
                    begin_line = firstVisibleItem;
                    end_line = firstVisibleItem + visibleItemCount;
                    if (end_line >= totalItemCount) {
                        end_line = totalItemCount - 1;
                    }
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public void onShopItemClick(int position) {
        try {
            if (!isUserSignIn()) {
                // TODO 先去登录
                gotoActivity(mContext, LoginActivity.class, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("merchant",mShopList.get(position));
            bundle.putString("merchant_id", mShopList.get(position).getId());
            bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_INSTALLMENT);
            gotoActivity(mContext, PayActivity.class, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ShopListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("merchantType",(Serializable)merchantTypeListBeen.get(position));
        bundle.putParcelable("location", mLatLng);
        bundle.putString("name", tb_title.getAddress());
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
        ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    @Override
    public void onClick(View view) {
        if (!isUserSignIn()) {
            // TODO 先去登录
            gotoActivity(mContext, LoginActivity.class, null);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_home_scan:
                if(isHavCameraPermission()) {
                    scanQrCode(CaptureActivity.class);
                }else{
                    ToastUtil.showToast(mContext,"摄像头权限已被禁用，请打开摄像头权限");
                }
                break;
            case R.id.ll_zero_quota:
                if(MainUtil.isLogin(mContext)){

                }else{
                    gotoActivity(mContext,LoginActivity.class,null);
                }
                break;
            default:
                break;
        }
    }

    private void gotoTargetActivity() {
        Bundle bundle = new Bundle();
        switch (UserUtil.getCreditStep(mContext)) {
            case GlobalParams.USER_STATUS_NEW:
                mIsll_zero_quotaCanClick = false;
                if (!isUploadCallRecord && !mIsCallRecordPassByServer) {
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
                    mIsll_zero_quotaCanClick = true;
                } else if (MainUtil.isNewUser(mContext) && !isUpLoadCallContacts && !mIsContactsPassByServer) {
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                    mIsll_zero_quotaCanClick = true;
                } else {
                    gotoActivity(mContext, ContactsInfoActivity.class, bundle);
                }
                break;
            case GlobalParams.HAVE_UPLOAD_IDCARD_INFO: {
                // TODO 直接去扫脸
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
            }
                break;
            case GlobalParams.HAVE_SCAN_FACE:// 已经扫脸通过
                gotoActivity(mContext, ImproveQuotaActivity.class, bundle);
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO:
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                break;
        }
    }

    private boolean isUserSignIn() {
        String customerId = UserUtil.getId(mContext);
        if (customerId != null && !"".equals(customerId) && !"0".equals(customerId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirst) {
            initHeadView();
        }
        if (mLocationUtil != null) {
            if (isVisibleToUser) {
                mLocationUtil.startLocation();
            } else {
                mLocationUtil.stopLocation();
            }
        }
    }

    private void scanQrCode(Class<?> target) {
        Bundle mBundle = new Bundle();
        mBundle.putString("scan_type", "ZeroYuanBuyFragment");
        gotoActivity(mContext, target, mBundle);
    }

    @Override
    public void onLeftClick(View view) {
    }

    @Override
    public void onAddressClick(View view) {
        Intent intent = new Intent(mContext, SearchLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", mLatLng);
        bundle.putString("name", tb_title.getAddress());
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
        ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    @Override
    public void onRightClick(View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        try {
            switch (requestCode) {
                case 2:
                    if (resultCode == getActivity().RESULT_OK && data != null && data.getExtras() != null) {
                        Log.d("ret", "onActivityResult");
                        mLatLng = data.getExtras().getParcelable("location");
                        mLastLocationNam = data.getExtras().getString("name");
                        if (mLastLocationNam != null || !"".equals(mLastLocationNam)) {
                            tb_title.setAddress(mLastLocationNam);
                            getMerchantTypeList();
                            SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.LAST_LOCATION_NAME_KEY, mLastLocationNam);
                        }
                        getNearShopList(0, true);
                    }
                    break;
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    if (resultCode == 5) {
                        new PermissionUtils(mContext).showPermissionDialog(3);//摄像头权限
                        return;
                    }
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(GlobalParams.IF_NEED_SCAN_FACE_KEY, true);
                        gotoActivity(mContext, ActivateTheQuotaActivity.class, bundle);
                    }
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    }
                    break;
            }
        } catch (Exception e) {
            LogUtil.d("ret", "onActivityResult ================================== 9");
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        initHeadView();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mLocationUtil.stopLocation();
        getActivity().unregisterReceiver(myBroadcastReciver);
        super.onDestroy();
    }

    @Override
    public void onMainReveiveLocation(BDLocation bdLocation) {
        try {
            if (bdLocation == null) {
                ViewUtil.cancelLoadingDialog();
                mLatLng = null;
//                getMerchantTypeList();
                return;
            }
            List<Poi> list = bdLocation.getPoiList();
            mLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            Long lastUpdateTime = SharedPreferencesUtil.getInstance(mContext).getLong(GlobalParams.LAST_UPDATE_NEAR_SHOP_LIST_TIME);
            Long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - lastUpdateTime >= SHOP_UPDATE_LENGTH) {
                mLastLocationNam = list.get(0).getName();
                tb_title.setAddress(mLastLocationNam);
                SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.LAST_LOCATION_NAME_KEY, mLastLocationNam);
                getMerchantTypeList();
                getNearShopList(0, true);
            }
        } catch (Exception e) {
            ViewUtil.cancelLoadingDialog();
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void getNearShopList(int offset, final boolean ifClear) {
        String locationString = getValidLocation();
        if (locationString == null) return;
        Log.d("ret", "getNearShopList");
        JSONObject mjson = new JSONObject();
        try {
            mjson.put("category", 0 + "");
            mjson.put("location", locationString);
            mjson.put("offset", offset + "");
            mjson.put("length", SHOP_UPDATE_LENGTH + "");
            Log.e("getnearShopError:", "开始查找商店");
            mGetNearByList.getNearByList(mjson, null, true, new BaseNetCallBack<NearByMerchantListBean>() {
                @Override
                public void onSuccess(NearByMerchantListBean paramT) {
                    if (ifClear)
                        mShopList.clear();
                    mShopList.addAll(paramT.getData());
                    currentShopList.clear();
                    for (int i = 0; i < 5; i++) {
                        if (i < mShopList.size()) {
                            currentShopList.add(mShopList.get(i));
                        } else {
                            break;
                        }
                    }
                    mShopListAdapter.notifyDataSetChanged();
                    ViewUtil.cancelLoadingDialog();
                    if (currentShopList.size() > 0) {
                        long lastUpdateTime = System.currentTimeMillis() / 1000;
                        SharedPreferencesUtil.getInstance(mContext).putLong(GlobalParams.LAST_UPDATE_NEAR_SHOP_LIST_TIME, lastUpdateTime);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    ViewUtil.cancelLoadingDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Nullable
    private String getValidLocation() {
        SharedPreferencesUtil sp = SharedPreferencesUtil.getInstance(mContext);
        String locationString;
        if (mLatLng == null) {
            locationString = sp.getString(GlobalParams.LAST_LOCATION_POSITION_KEY, "0,0");
            if ("0,0".equals(locationString)) {
                return null;
            } else {
                String[] locationArry = locationString.split(",");
                mLatLng = new LatLng(Double.parseDouble(locationArry[0]), Double.parseDouble(locationArry[1]));
            }
        } else {
            locationString = mLatLng.latitude + "," + mLatLng.longitude;
            sp.putString(GlobalParams.LAST_LOCATION_POSITION_KEY, locationString);
        }
        return locationString;
    }

    public void freshBalanceAmount(boolean isShowLoadingDialog) {
        try {
            SignIn mSignIn = new SignIn(mContext);
            String device_id = UserUtil.getDeviceId(mContext);
            if (device_id == null || "".equals(device_id)) {
                return;
            }
            JSONObject json = new JSONObject();
            json.put("device_id", device_id);
            json.put("mobile", UserUtil.getMobile(mContext));
            final String password=UserUtil.getLoginPsw(mContext);
            json.put("password", password);
            String push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            json.put("push_id", push_id);
            mSignIn.signIn(json, null, isShowLoadingDialog, new BaseNetCallBack<SignInBean>() {
                @Override
                public void onSuccess(SignInBean paramT) {
                    UserUtil.setLoginPassword(mContext, password);
                    initHeadView();
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.validateLoginStatus();
//                    if (isSignInFirstSuccess) {
//                        isSignInFirstSuccess = false;
//                        mainActivity.validateCashApplyStatus();
//                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Log.e("loginError", "errorType" + errorType + "  erroCode" + errorCode);
                    initHeadView();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.ACTIVE_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.REFRESH_HOME_PAGE_ACTION);
        intentFilter.addAction(GlobalParams.WECHAT_PAY_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.VERIFY_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.REFRESH_HOME_PAGE_ACTION);
        intentFilter.addAction(GlobalParams.PASS_CALL_RECORD_ACTION);
        intentFilter.addAction(GlobalParams.PASS_CONTACT_ACTION);
        mContext.registerReceiver(myBroadcastReciver, intentFilter);
    }

    @Override
    public void uploadSuccessCallBack(int type) {
        //上传通讯录、通话记录、短信等的回调
        switch (type) {
            case GlobalParams.UPLOADCALLCONTACTS:
                //上传联系人成功
                isUpLoadCallContacts = true;
                ll_zero_quota.performClick();
                break;
            case GlobalParams.UPLOADCALLRECORD:
                //上传通话记录成功
                isUploadCallRecord = true;
                if (MainUtil.isNewUser(mContext) && !isUpLoadCallContacts && !mIsContactsPassByServer) {
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                    mIsll_zero_quotaCanClick = true;
                } else {
                    ll_zero_quota.performClick();
                }
                break;
            case GlobalParams.UPLOADMESSAGE:
                //上传短信成功
                break;
        }
    }

    @Override
    public void uploadFailCallBack(int type) {
        switch (type) {
            case GlobalParams.UPLOADCALLCONTACTS:
                //上传联系人失败
                isUpLoadCallContacts = false;
                break;
            case GlobalParams.UPLOADCALLRECORD:
                //上传通话记录失败
                isUploadCallRecord = false;
                break;
            case GlobalParams.UPLOADMESSAGE:
                //上传短信失败
                break;
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        //广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String topActivityName = Utils.getTopActivityName(getActivity());
            if (GlobalParams.WECHAT_PAY_SUCCESS_ACTION.equals(action)
                    || GlobalParams.REFRESH_HOME_PAGE_ACTION.equals(action)
                    || GlobalParams.VERIFY_SUCCESS_ACTION.equals(action)
                    || GlobalParams.WX_REPAY_CONSUMPTION_SUCCESS_ACTION.equals(action)) {
                freshBalanceAmount(false);
            } else {
                if (topActivityName.contains("MainActivity") && 1==((MainActivity)getActivity()).getTopFragmentIndex()) {
                    if (GlobalParams.PASS_CALL_RECORD_ACTION.equals(action)) {
                        mIsCallRecordPassByServer = true;
                        if (mIsll_zero_quotaCanClick == true) {
                            ll_zero_quota.performClick();
                        }
                    } else if (GlobalParams.PASS_CONTACT_ACTION.equals(action)) {
                        mIsContactsPassByServer = true;
                        if (mIsll_zero_quotaCanClick == true) {
                            ll_zero_quota.performClick();
                        }
                    }
                }
            }
        }
    }
    public boolean isHavCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission= ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.CAMERA);
            if (cameraPermission!= PackageManager.PERMISSION_GRANTED) {
              return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}