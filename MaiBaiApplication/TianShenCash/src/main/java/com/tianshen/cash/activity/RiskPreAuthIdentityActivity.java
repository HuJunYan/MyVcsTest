package com.tianshen.cash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.idcard.activity.IDCardScanActivity;
import com.tianshen.cash.idcard.util.Util;
import com.tianshen.cash.model.IDCardBean;
import com.tianshen.cash.model.IdNumInfoBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.UploadImageBean;
import com.tianshen.cash.net.api.GetIdNumInfo;
import com.tianshen.cash.net.api.IDCardAction;
import com.tianshen.cash.net.api.SaveIDCardBack;
import com.tianshen.cash.net.api.SaveIDCardFront;
import com.tianshen.cash.net.api.UploadImage;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.ImageLoader;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.RomUtils;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class RiskPreAuthIdentityActivity extends BaseActivity {


    @BindView(R.id.iv_scan_identity_front)//身份证正面
            ImageView iv_scan_identity_front;
    @BindView(R.id.iv_scan_identity_back) //身份证反面
            ImageView iv_scan_identity_back;
    private boolean mCanScanFace;
    private byte[] mHeadImg;
    private String is_auth_idcard;
    private IdNumInfoBean mIdNumInfoBean;
    private boolean mSaveIDCardFront; //是否上传身份证正面
    private int mIsClickPosition; //0==身份证正面,1==身份证背面，

    private IDCardBean mIDCardBean;
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20; //上传图片 type  身份证正面
    private final int IMAGE_TYPE_ID_CARD_BACK = 21; //上传图片 type  身份证反面
    private final int IMAGE_TYPE_SCAN_FACE = 25; //上传图片 type  活体检测图组

    private static final int MSG_IDCARD_NETWORK_WARRANTY_OK = 1; //face++身份证联网授权成功
    private static final int MSG_IDCARD_NETWORK_WARRANTY_ERROR = 2;//face++身份证联网授权失败
    private static final int MSG_IDCARD_NETWORK_FACE_OK = 3;//face++扫脸授权失败
    private static final int MSG_IDCARD_NETWORK_FACE_ERROR = 4;//face++扫脸授权失败

    private String[] mImageFullPath = new String[7];
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_IDCARD_NETWORK_WARRANTY_OK:
                    gotoFaceAddAddActivity();
                    break;
                case MSG_IDCARD_NETWORK_WARRANTY_ERROR:
                    ToastUtil.showToast(mContext, "联网授权失败，请重新认证");
                    break;
                case MSG_IDCARD_NETWORK_FACE_OK:
                    ViewUtil.cancelLoadingDialog();
                    gotoFaceAddAddActivity();
                    break;
                case MSG_IDCARD_NETWORK_FACE_ERROR:
                    ToastUtil.showToast(mContext, "联网授权失败，请重新认证");
                    ViewUtil.cancelLoadingDialog();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIdNumInfo();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_auth_identity;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_risk_pre_back, R.id.iv_scan_identity_front, R.id.iv_scan_identity_back})
    public void back(View view) {
        switch (view.getId()) {
            case R.id.tv_risk_pre_back:
                backActivity();
                break;
            case R.id.iv_scan_identity_front://身份证正面
                requestPermissionsToNextActivity(R.id.iv_scan_identity_front);
                break;
            case R.id.iv_scan_identity_back://身份证反面
                requestPermissionsToNextActivity(R.id.iv_scan_identity_back);
                break;
        }
    }


    /**
     * 得到用户认证的信息
     */
    private void initIdNumInfo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            is_auth_idcard = extras.getString(GlobalParams.IDENTITY_STATE_KEY, "0");
        }
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetIdNumInfo getIdNumInfo = new GetIdNumInfo(mContext);

        getIdNumInfo.getIdNumInfo(jsonObject, false,
                new BaseNetCallBack<IdNumInfoBean>() {
                    @Override
                    public void onSuccess(IdNumInfoBean paramT) {
                        mIdNumInfoBean = paramT;
                        refreshRootUI();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
    }

    /**
     * 刷新整体UI
     */
    private void refreshRootUI() {

        if (mIdNumInfoBean == null) {
            return;
        }

        String real_name = mIdNumInfoBean.getData().getReal_name();//真是姓名
        String id_num = mIdNumInfoBean.getData().getId_num();//身份证号

        String front_idCard_url = mIdNumInfoBean.getData().getFront_idCard_url();//身份证正面url
        String back_idCard_url = mIdNumInfoBean.getData().getBack_idCard_url(); //身份证反面url
        String face_url = mIdNumInfoBean.getData().getFace_url(); //扫脸url
        if (!TextUtils.isEmpty(real_name) && !TextUtils.isEmpty(id_num)) {
//            etIdentityAuthName.setTextColor(getResources().getColor(R.color.global_txt_black4));
//            etIdentityAuthNum.setTextColor(getResources().getColor(R.color.global_txt_black4));
        }
//        etIdentityAuthName.setText(real_name);
//        etIdentityAuthNum.setText(id_num);

        ImageLoader.load(getApplicationContext(), front_idCard_url, iv_scan_identity_front);
        ImageLoader.load(getApplicationContext(), back_idCard_url, iv_scan_identity_back);
//        ImageLoader.load(getApplicationContext(), face_url, ivIdentityAuthFace);

        if (!TextUtils.isEmpty(front_idCard_url) && !TextUtils.isEmpty(back_idCard_url)) {
            mCanScanFace = true;
        }
        TianShenUserUtil.saveUserName(mContext, real_name);
        TianShenUserUtil.saveUserIDNum(mContext, id_num);

    }
    //请求相机权限 并根据结果 决定是否进行跳转

    private void requestPermissionsToNextActivity(final int id) {
        boolean isReallyHasPermission = checkPermissionForFlyme();
        if (!isReallyHasPermission) {
            ToastUtil.showToast(this, "请去设置开启照相机权限");
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(RiskPreAuthIdentityActivity.this);
        rxPermissions.request(android.Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (mIdNumInfoBean == null || mIdNumInfoBean.getData() == null) {
                    return;
                }
                if (aBoolean) {
                    switch (id) {
                        case R.id.iv_scan_identity_front:
                            onClickIdentity();
                            break;
                        case R.id.iv_scan_identity_back:
                            onClickIdentityBack();
                            break;
                    }
                } else {
                    ToastUtil.showToast(RiskPreAuthIdentityActivity.this, "请去设置开启照相机权限");
                }
            }
        });
    }

    /**
     * 点击了身份证正面
     */
    private void onClickIdentity() {
        mIsClickPosition = 0;
        idCardNetWorkWarranty();
    }

    /**
     * 点击了身份证反面
     */
    private void onClickIdentityBack() {
        mIsClickPosition = 1;
        //先判断没有上传身份证正面
        if (!mSaveIDCardFront) {
            ToastUtil.showToast(mContext, "先上传身份证正面");
            return;
        }
        idCardNetWorkWarranty();
    }

    /**
     * 身份证联网授权
     */
    private void idCardNetWorkWarranty() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(MyApplicationLike.getsApplication());
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(mContext);
                manager.registerLicenseManager(idCardLicenseManager);
                manager.takeLicenseFromNetwork(Util.getUUIDString(mContext));
                if (idCardLicenseManager.checkCachedLicense() > 0) {
                    mHandler.sendEmptyMessage(MSG_IDCARD_NETWORK_WARRANTY_OK);
                } else {
                    mHandler.sendEmptyMessage(MSG_IDCARD_NETWORK_WARRANTY_ERROR);
                }
            }
        }).start();

    }


    /**
     * 跳转到face++的页面
     */
    private void gotoFaceAddAddActivity() {
        switch (mIsClickPosition) {
            case 0: //正面
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_6);
                Intent idCardScanIntent = new Intent(mContext, IDCardScanActivity.class);
                idCardScanIntent.putExtra("isvertical", true);
                idCardScanIntent.putExtra("side", 0);
                startActivityForResult(idCardScanIntent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                ToastUtil.showToast(mContext, "请拍摄身份证正面");
                break;
            case 1://反面
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_8);
                Intent idCardScanBackIntent = new Intent(mContext, IDCardScanActivity.class);
                idCardScanBackIntent.putExtra("isvertical", true);
                idCardScanBackIntent.putExtra("side", 1);
                startActivityForResult(idCardScanBackIntent, GlobalParams.INTO_IDCARDSCAN_BACK_PAGE);
                ToastUtil.showToast(mContext, "请拍摄身份证反面");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    LogUtil.d("abc", "onActivityResult--身份证正面");
                    byte[] frontImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[0] = saveJPGFile(mContext, frontImg, IMAGE_TYPE_ID_CARD_FRONT);
                    upLoadImage(frontImg);
                    break;
                case GlobalParams.INTO_IDCARDSCAN_BACK_PAGE:
                    LogUtil.d("abc", "onActivityResult--身份证反面");
                    byte[] backImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[1] = saveJPGFile(mContext, backImg, IMAGE_TYPE_ID_CARD_BACK);
                    upLoadImage(backImg);
                    break;

            }
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_ERROR) {
            ToastUtil.showToast(mContext, "扫描失败,请手动输入吧");
            Intent intent = new Intent();
            setResult(GlobalParams.RETURN_FROM_ACTIVITY_ERROR, intent);
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_BACK_KEY) {
        }
    }


    /**
     * 上传图片
     */
    private void upLoadImage(final byte[] imageByte) {

        String userID = TianShenUserUtil.getUserId(mContext);

        String path = "";
        String type = "";
        switch (mIsClickPosition) {
            case 0:
                type = IMAGE_TYPE_ID_CARD_FRONT + "";
                path = mImageFullPath[0];
                break;
            case 1:
                type = IMAGE_TYPE_ID_CARD_BACK + "";
                path = mImageFullPath[1];
                break;
            case 2:
                break;
        }

        try {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userID);
            jsonObject.put("type", type);
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);

            uploadImage.uploadImage(newJson, path, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    LogUtil.d("abc", "upLoadImage--onSuccess");
                    switch (mIsClickPosition) {
                        case 0:
                            setImageSource(imageByte);
                            getIdcardFrontInfo(imageByte);
                            break;
                        case 1:
                            setImageSource(imageByte);
                            getIdcardBackInfo(imageByte);
                            break;
                        case 2:
                            break;
                    }
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    /**
     * 得到身份证反面信息
     */
    private void getIdcardBackInfo(byte[] data) {
        ViewUtil.createLoadingDialog(this, "", false);


        String api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
        String api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;


        new IDCardAction(this, api_key, api_secret).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                ViewUtil.cancelLoadingDialog();
                if (mIDCardBean != null && paramT != null) {
                    mIDCardBean.issued_by = paramT.issued_by;
                    mIDCardBean.valid_date = paramT.valid_date;
                    initSaveIDCardBack();
                    MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_9, MaiDianUtil.RESULT_SUCCESS, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_9, MaiDianUtil.RESULT_FAILURE, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    /**
     * 保存身份证反面信息到云端
     */
    private void initSaveIDCardBack() {

        String sign_organ = mIDCardBean.issued_by; //身份证签发机关
        String valid_date = mIDCardBean.valid_date; //身份证有效期限

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("sign_organ", sign_organ);
            jsonObject.put("valid_period", valid_date);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SaveIDCardBack saveIDCardBack = new SaveIDCardBack(mContext);
        saveIDCardBack.saveIDCardBack(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                int code = paramT.getCode();
                if (0 == code) {
                    mCanScanFace = true;
                    ToastUtil.showToast(mContext, "上传身份证反面信息成功!");
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void setImageSource(byte[] imageSource) {
        Bitmap idcardBmp = BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
        Drawable drawable = new BitmapDrawable(idcardBmp);
        switch (mIsClickPosition) {
            case 0:
                mHeadImg = imageSource;
                iv_scan_identity_front.setImageDrawable(drawable);
                LogUtil.d("abc", "设置身份证正面");
                break;
            case 1:
                iv_scan_identity_back.setImageDrawable(drawable);
                LogUtil.d("abc", "设置身份证反面");
                break;
        }
    }

    /**
     * 得到身份证正面信息
     */
    private void getIdcardFrontInfo(byte[] data) {
        LogUtil.d("abc", "getIdcardFrontInfo");
        ViewUtil.createLoadingDialog(this, "", false);


        String api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
        String api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;
        new IDCardAction(this, api_key, api_secret).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                ViewUtil.cancelLoadingDialog();
                mIDCardBean = paramT;
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_7, MaiDianUtil.RESULT_SUCCESS, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                if (mIDCardBean.id_card_number.length() != 18) {
                    MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_7, MaiDianUtil.RESULT_FAILURE, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                    ToastUtil.showToast(mContext, "身份证信息读取失败，请重新扫描身份证正面！");
                }
                refreshNameAndNumUI();
                initSaveIDCardFront();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_7, MaiDianUtil.RESULT_FAILURE, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                backActivity();
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    /**
     * 刷新名字和身份证号UI
     */
    private void refreshNameAndNumUI() {
        String name = mIDCardBean.name;
        String num = mIDCardBean.id_card_number;
        //todo  设置身份证信息
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(num)) {
//            etIdentityAuthName.setTextColor(getResources().getColor(R.color.global_txt_black4));
//            etIdentityAuthNum.setTextColor(getResources().getColor(R.color.global_txt_black4));
        }
//        etIdentityAuthName.setText(name);
//        etIdentityAuthNum.setText(num);

    }

    /**
     * 保存身份证正面信息到云端
     */
    private void initSaveIDCardFront() {


        final String real_name = mIDCardBean.name; //身份证姓名
        String gender = mIDCardBean.gender; // 身份证性别
        String nation = mIDCardBean.race; // 民族
        IDCardBean.Birthday birthdayBean = mIDCardBean.birthday;
        String year = birthdayBean.year;
        String month = birthdayBean.month;
        String day = birthdayBean.day;
        String birthday = year + "年" + month + "月" + day + "日"; //出生日期
        String birthplace = mIDCardBean.address; //住址
        final String id_num = mIDCardBean.id_card_number; // 身份证号

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("real_name", real_name);
            jsonObject.put("gender", gender);
            jsonObject.put("nation", nation);
            jsonObject.put("birthday", birthday);
            jsonObject.put("birthplace", birthplace);
            jsonObject.put("id_num", id_num);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SaveIDCardFront saveIDCardFront = new SaveIDCardFront(mContext);
        saveIDCardFront.saveIDCardFront(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                int code = paramT.getCode();
                if (0 == code) {
                    mSaveIDCardFront = true;
                    TianShenUserUtil.saveUserName(mContext, real_name);
                    TianShenUserUtil.saveUserIDNum(mContext, id_num);
                    ToastUtil.showToast(mContext, "上传身份证正面信息成功!");
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

    }

    public static String saveJPGFile(Context mContext, byte[] data, int type) {
        if (data == null)
            return null;
        File mediaStorageDir = mContext.getExternalFilesDir("idCardAndLiveness");
        if (null == mediaStorageDir) {
            return null;
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            String jpgFileName = type + ".jpg";
            fos = new FileOutputStream(mediaStorageDir + "/" + jpgFileName);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return mediaStorageDir.getAbsolutePath() + "/" + jpgFileName;
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e1));
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e1));
                }
            }
        }
        return null;
    }
    //检查是否是魅族Flyme系统 如果是 则判断是否有相机权限 不是则不判断

    private boolean checkPermissionForFlyme() {
        boolean isFlyme = RomUtils.FlymeSetStatusBarLightMode(); //是否是魅族
        boolean isReallyHasPermission;
        if (isFlyme) {
            if (isCameraCanUse()) {
                isReallyHasPermission = true;
            } else {
                isReallyHasPermission = false;
            }
        } else {
            isReallyHasPermission = true;
        }
        return isReallyHasPermission;

    }

    //针对魅族Flyme系统判断是否有相机权限
    public boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

}
