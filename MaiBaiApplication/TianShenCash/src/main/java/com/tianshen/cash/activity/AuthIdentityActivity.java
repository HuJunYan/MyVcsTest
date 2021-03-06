package com.tianshen.cash.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.idcard.activity.IDCardScanActivity;
import com.tianshen.cash.idcard.util.Util;
import com.tianshen.cash.liveness.activity.LivenessActivity;
import com.tianshen.cash.liveness.bean.MyMap;
import com.tianshen.cash.liveness.util.ConUtil;
import com.tianshen.cash.model.IDCardBean;
import com.tianshen.cash.model.IdNumInfoBean;
import com.tianshen.cash.model.ImageVerifyRequestBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.UDunIDInfoBean;
import com.tianshen.cash.model.UploadImageBean;
import com.tianshen.cash.net.api.CreditFace;
import com.tianshen.cash.net.api.GetIdNumInfo;
import com.tianshen.cash.net.api.IDCardAction;
import com.tianshen.cash.net.api.SaveIDCardBack;
import com.tianshen.cash.net.api.SaveIDCardFront;
import com.tianshen.cash.net.api.UploadImage;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.ImageLoader;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.RomUtils;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 身份认证页面
 */

public class AuthIdentityActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_identity_auth_back)
    TextView tvIdentityAuthBack;
    @BindView(R.id.tv_identity_post)
    TextView tvIdentityPost;
    @BindView(R.id.tv_identity_auth_name)
    TextView tvIdentityAuthName;
    @BindView(R.id.et_identity_auth_name)
    EditText etIdentityAuthName;
    @BindView(R.id.tv_identity_auth_num)
    TextView tvIdentityAuthNum;
    @BindView(R.id.et_identity_auth_num)
    EditText etIdentityAuthNum;
    @BindView(R.id.tv_identity_auth_pic_key)
    TextView tvIdentityAuthPicKey;
    @BindView(R.id.tv_identity_auth_pic2_key)
    TextView tvIdentityAuthPic2Key;
    @BindView(R.id.tv_identity_auth_face_key)
    TextView tvIdentityAuthFaceKey;
    @BindView(R.id.iv_identity_auth_pic)
    ImageView ivIdentityAuthPic;
    @BindView(R.id.iv_identity_auth_pic2)
    ImageView ivIdentityAuthPic2;
    @BindView(R.id.iv_identity_auth_face)
    ImageView ivIdentityAuthFace;

    private IdNumInfoBean mIdNumInfoBean;

    private String[] mImageFullPath = new String[7];
    private IDCardBean mIDCardBean;
    private MediaPlayer mMediaPlayer = null;

    private byte[] mHeadImg;

    private double confidence = 0;
    private int facePassScore = 65;

    private int mIsClickPosition; //0==身份证正面,1==身份证背面，2==人脸识别
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20; //上传图片 type  身份证正面
    private final int IMAGE_TYPE_ID_CARD_BACK = 21; //上传图片 type  身份证反面
    private final int IMAGE_TYPE_SCAN_FACE = 25; //上传图片 type  活体检测图组

    private boolean isCanPressBack = true;
    private boolean mSaveIDCardFront; //是否上传身份证正面

    private boolean mCanScanFace;

    private boolean mFaceKeyIsNew = false;

    private static final int MSG_IDCARD_NETWORK_WARRANTY_OK = 1; //face++身份证联网授权成功
    private static final int MSG_IDCARD_NETWORK_WARRANTY_ERROR = 2;//face++身份证联网授权失败
    private static final int MSG_IDCARD_NETWORK_FACE_OK = 3;//face++扫脸授权失败
    private static final int MSG_IDCARD_NETWORK_FACE_ERROR = 4;//face++扫脸授权失败
    //Udun相关
    private static final String UDUN_VERIFY_SUCCESS_CODE = "000000"; //扫描完成
    private static final String UDUN_VERIFY_USER_CANCEL = "900001";//用户取消
    private static final String UDUN_VERIFY_SUCCESS_SIGN = "T";//扫描成功
    private static final String ID_HAS_IDENTITY = "1"; //已认证
    private static final int CHANGE_TYPE_UDUN = 2; //FACE++ 1  UDUN 2

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
    private String is_auth_idcard;

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
                case GlobalParams.PAGE_INTO_LIVENESS:
                    LogUtil.d("abc", "onActivityResult--人脸识别");
                    livenessResult(data.getExtras());
                    break;
            }
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_ERROR) {
            ToastUtil.showToast(mContext, "扫描失败,请手动输入吧");
            Intent intent = new Intent();
            setResult(GlobalParams.RETURN_FROM_ACTIVITY_ERROR, intent);
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_BACK_KEY) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ding();
        initIdNumInfo();
    }

    private void ding() {
        MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_4);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_identity;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        ViewUtil.cancelLoadingDialog();
    }

    @Override
    protected void setListensers() {
        etIdentityAuthName.setEnabled(false);
        etIdentityAuthNum.setEnabled(false);
        tvIdentityPost.setOnClickListener(this);
        tvIdentityAuthBack.setOnClickListener(this);
        ivIdentityAuthPic.setOnClickListener(this);
        ivIdentityAuthPic2.setOnClickListener(this);
        ivIdentityAuthFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_identity_auth_back:
            case R.id.tv_identity_post:
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_5);
                backActivity();
                break;
            case R.id.iv_identity_auth_pic:
                requestPermissionsToNextActivity(R.id.iv_identity_auth_pic);
                break;
            case R.id.iv_identity_auth_pic2:
                requestPermissionsToNextActivity(R.id.iv_identity_auth_pic2);
                break;
            case R.id.iv_identity_auth_face:
                requestPermissionsToNextActivity(R.id.iv_identity_auth_face);
                break;
        }
        ;
    }


    //请求相机权限 并根据结果 决定是否进行跳转

    private void requestPermissionsToNextActivity(final int id) {
        boolean isReallyHasPermission = checkPermissionForFlyme();
        if (!isReallyHasPermission) {
            ToastUtil.showToast(this, "请去设置开启照相机权限");
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(AuthIdentityActivity.this);
        rxPermissions.request(android.Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (mIdNumInfoBean == null || mIdNumInfoBean.getData() == null) {
                    return;
                }
                if (aBoolean) {

                    if (mIdNumInfoBean.getData().change_type == CHANGE_TYPE_UDUN) {
                        if (ID_HAS_IDENTITY.equals(is_auth_idcard)) {
                            ToastUtil.showToast(mContext, "亲，您暂无权限进行重新认证");
                            return;
                        }
                        uDunIdentity();
                    } else {
                        switch (id) {
                            case R.id.iv_identity_auth_pic:
                                onClickIdentity();
                                break;
                            case R.id.iv_identity_auth_pic2:
                                onClickIdentityBack();
                                break;
                            case R.id.iv_identity_auth_face:
                                onClickFace();
                                break;
                        }
                    }

                } else {
                    ToastUtil.showToast(AuthIdentityActivity.this, "请去设置开启照相机权限");
                }
            }
        });

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
     * 点击了人脸识别
     */
    private void onClickFace() {
        mIsClickPosition = 2;
        //判断没有上传身份证背面
        if (!mCanScanFace) {
            ToastUtil.showToast(mContext, "先上传身份证");
            return;
        }
        livenessNetWorkWarranty();
    }


    /**
     * 刷新整体UI
     */
    private void refreshRootUI() {

        if (mIdNumInfoBean == null) {
            return;
        }

        String real_name = mIdNumInfoBean.getData().getReal_name();
        String id_num = mIdNumInfoBean.getData().getId_num();

        String front_idCard_url = mIdNumInfoBean.getData().getFront_idCard_url();
        String back_idCard_url = mIdNumInfoBean.getData().getBack_idCard_url();
        String face_url = mIdNumInfoBean.getData().getFace_url();
        if (!TextUtils.isEmpty(real_name) && !TextUtils.isEmpty(id_num)) {
            etIdentityAuthName.setTextColor(getResources().getColor(R.color.global_txt_black4));
            etIdentityAuthNum.setTextColor(getResources().getColor(R.color.global_txt_black4));
        }
        etIdentityAuthName.setText(real_name);
        etIdentityAuthNum.setText(id_num);

        ImageLoader.load(getApplicationContext(), front_idCard_url, ivIdentityAuthPic);
        ImageLoader.load(getApplicationContext(), back_idCard_url, ivIdentityAuthPic2);
        ImageLoader.load(getApplicationContext(), face_url, ivIdentityAuthFace);

        if (!TextUtils.isEmpty(front_idCard_url) && !TextUtils.isEmpty(back_idCard_url)) {
            mCanScanFace = true;
        }

        TianShenUserUtil.saveUserName(mContext, real_name);
        TianShenUserUtil.saveUserIDNum(mContext, id_num);

        String face_change_key = mIdNumInfoBean.getData().getFace_change_key();
        if ("0".equals(face_change_key)) {
            mFaceKeyIsNew = false;
        } else if ("1".equals(face_change_key)) {
            mFaceKeyIsNew = true;
        }

    }

    /**
     * 刷新名字和身份证号UI
     */
    private void refreshNameAndNumUI() {
        String name = mIDCardBean.name;
        String num = mIDCardBean.id_card_number;
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(num)) {
            etIdentityAuthName.setTextColor(getResources().getColor(R.color.global_txt_black4));
            etIdentityAuthNum.setTextColor(getResources().getColor(R.color.global_txt_black4));
        }
        etIdentityAuthName.setText(name);
        etIdentityAuthNum.setText(num);

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
     * 活体联网授权
     */
    private void livenessNetWorkWarranty() {
        LogUtil.d("abc", "livenessNetWorkWarranty");
        ViewUtil.createLoadingDialog(this, "正在联网授权", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(mContext);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(mContext);
                manager.registerLicenseManager(licenseManager);
                manager.takeLicenseFromNetwork(ConUtil.getUUIDString(mContext));
                if (licenseManager.checkCachedLicense() > 0) {
                    mHandler.sendEmptyMessage(MSG_IDCARD_NETWORK_FACE_OK);
                } else {
                    mHandler.sendEmptyMessage(MSG_IDCARD_NETWORK_FACE_ERROR);
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
            case 2://人脸检测
                isCanPressBack = false;
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_10);
                startActivityForResult(new Intent(mContext, LivenessActivity.class), GlobalParams.PAGE_INTO_LIVENESS);
                break;
        }
    }

    private void livenessResult(Bundle bundle) {

        MyMap myMap = (MyMap) bundle.getSerializable("images");
        if (myMap == null) {
            MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_11, MaiDianUtil.RESULT_FAILURE, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
            ToastUtil.showToast(mContext, "检测失败，重新再试!");
            mIsClickPosition = 2;
            gotoFaceAddAddActivity();
            return;
        }

        String resultOBJ = bundle.getString("result");
        try {
            JSONObject result = new JSONObject(resultOBJ);
            int resID = result.getInt("resultcode");
            if (resID == R.string.verify_success) {
                doPlay(R.raw.meglive_success);
            } else if (resID == R.string.liveness_detection_failed_not_video) {
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed_timeout) {
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed) {
                doPlay(R.raw.meglive_failed);
            } else {
                doPlay(R.raw.meglive_failed);
            }
            boolean isSuccess = result.getString("result").equals(getResources().getString(R.string.verify_success));
            if (isSuccess) {
                // 保存活体检测时的照片
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_11, MaiDianUtil.RESULT_SUCCESS, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                MyMap map = (MyMap) bundle.getSerializable("images");
                saveLivenessImage(map);
                upLoadLivenessImage(result.getString("delta"), map, IMAGE_TYPE_SCAN_FACE);
            } else {
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_11, MaiDianUtil.RESULT_FAILURE, MaiDianUtil.ACTIVITY_RESULT_DEFAULT);
                ToastUtil.showToast(mContext, "检测失败，请重新检测");
                gotoFaceAddAddActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void saveLivenessImage(MyMap map) {
        int count = map.getImages().size();
        if (count > 5) {
            count = 5;
        }
        int i = 0;
        for (Map.Entry<String, byte[]> entry : map.getImages().entrySet()) {
            mImageFullPath[i + 2] = saveJPGFile(mContext, entry.getValue(), i + 25);
            i++;
        }
    }

    private void doPlay(int rawId) {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        mMediaPlayer.reset();
        try {
            AssetFileDescriptor localAssetFileDescriptor = getResources()
                    .openRawResourceFd(rawId);
            mMediaPlayer.setDataSource(
                    localAssetFileDescriptor.getFileDescriptor(),
                    localAssetFileDescriptor.getStartOffset(),
                    localAssetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(localIOException));
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
     * 上传扫脸照片
     */
    private void upLoadLivenessImage(final String delta, final MyMap map, final int type) {

        LogUtil.d("abc", "upLoadLivenessImage");

        try {
            int count = map.getImages().size();
            if (count < 5) {
                count = 5;
            }
            String[] imageFullPatyArray = new String[5];
            for (int i = 0; i < count; i++) {
                imageFullPatyArray[i] = mImageFullPath[i + 2];
            }
            final String facePath = imageFullPatyArray[1];

            String userID = TianShenUserUtil.getUserId(mContext);

            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userID);
            jsonObject.put("type", type + "");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);
            uploadImage.uploadImageArray(newJson, imageFullPatyArray, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    LogUtil.d("abc", "upLoadLivenessImage---onSuccess");
                    ImageLoader.load(getApplicationContext(), facePath, ivIdentityAuthFace);
                    compareImage(delta, map);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    LogUtil.d("abc", "upLoadLivenessImage---onFailure");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void compareImage(String delta, MyMap map) {
        ImageVerifyRequestBean bean = new ImageVerifyRequestBean();
        String name = TianShenUserUtil.getUserName(mContext);
        String id_num = TianShenUserUtil.getUserIDNum(mContext);
        byte[] headImage = getHeadImage();
        bean.name = name;
        bean.idcard = id_num;
        bean.imageref = Utils.saveJPGFile(this, headImage, "image_ref1");
        bean.delta = delta;
        bean.images = map.getImages();
        imageVerify_2_noScanIDCard(bean);

    }

    /**
     * 得到头像的byte[]
     */
    private byte[] getHeadImage() {
        ivIdentityAuthPic.setDrawingCacheEnabled(true);//获取bm前执行，否则无法获取
        Bitmap bitmap = ivIdentityAuthPic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ivIdentityAuthPic.setDrawingCacheEnabled(false);
        return baos.toByteArray();
    }


    private void imageVerify_2_noScanIDCard(final ImageVerifyRequestBean bean) {
        LogUtil.d("abc", "imageVerify_2_noScanIDCard--in");

        ViewUtil.createLoadingDialog(this, "正在与身份证比对，请稍候", false);
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("idcard_name", bean.name);
            requestParams.put("idcard_number", bean.idcard);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        requestParams.put("delta", bean.delta);
        requestParams.put("comparison_type", "1");
        requestParams.put("face_image_type", "meglive");

        String api_key;
        String api_secret;
        if (mFaceKeyIsNew) {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;
        } else {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_OLD;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_OLD;
        }

        LogUtil.d("abc", "mFaceKeyIsNew--->" + mFaceKeyIsNew);

        requestParams.put("api_key", api_key);
        requestParams.put("api_secret", api_secret);

        for (Map.Entry<String, byte[]> entry : bean.images.entrySet()) {
            requestParams.put(entry.getKey(), new ByteArrayInputStream(entry.getValue()));
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://api.megvii.com/faceid/v2/verify";
        asyncHttpClient.post(url, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        ViewUtil.cancelLoadingDialog();
                        String successStr = new String(bytes);
                        Log.d("ret", successStr);
                        LogUtil.d("abc", "imageVerify_2_noScanIDCard--successStr-->" + successStr);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(successStr);
                            if (!jsonObject.has("error")) {
                                // 活体最好的一张照片和公安部系统上身份证上的照片比较
                                confidence = jsonObject.getJSONObject("result_faceid").getDouble("confidence");
                                // 活体最好的一张照片和拍摄身份证上的照片的比较
                                // 解析faceGen
                                JSONObject jObject = jsonObject.getJSONObject("face_genuineness");
                                float mask_confidence = (float) jObject.getDouble("mask_confidence");
                                float screen_replay_confidence = (float) jObject.getDouble("screen_replay_confidence");
                                float synthetic_face_confidence = (float) jObject.getDouble("synthetic_face_confidence");
                                if (mask_confidence > (float) facePassScore / 100 || screen_replay_confidence > (float) facePassScore / 100 || synthetic_face_confidence > (float) facePassScore / 100 || confidence < facePassScore) {
                                    ToastUtil.showToast(mContext, "人脸比对失败，请重新检测");
                                    selectLivenessControl(bean.name, bean.idcard);
                                } else {
                                    conformCreditFace(confidence + "");
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            MobclickAgent.reportError(mContext, LogUtil.getException(e1));
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers,
                                          byte[] bytes, Throwable throwable) {
                        ViewUtil.cancelLoadingDialog();
                        ToastUtil.showToast(mContext, "人脸比对失败，请重新检测");
                        gotoFaceAddAddActivity();
                    }
                });
    }


    /**
     * 扫脸成功告诉服务器
     */
    private void conformCreditFace(String score) {
        LogUtil.d("abc", "conformCreditFace---in");

        CreditFace creditFace = new CreditFace(mContext);
        String userID = TianShenUserUtil.getUserId(mContext);

        JSONObject json = new JSONObject();
        try {
            json.put(GlobalParams.USER_CUSTOMER_ID, userID);
            json.put("face_pass", "1");
            json.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        creditFace.creditFace(json, null, true, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                Utils.delJPGFile(mContext);
//                double mLivenessResult = 0;
//                mLivenessResult = confidence;
//                UserUtil.setCreditStep(mContext, GlobalParams.HAVE_SCAN_FACE + "");
//                NumberFormat nFormat = NumberFormat.getNumberInstance();
//                nFormat.setMaximumFractionDigits(2);//设置小数点后面位数为
//                ToastUtil.showToast(mContext, "人脸比对成功，相似度" + nFormat.format(mLivenessResult) + "%");
                isCanPressBack = true;
                ToastUtil.showToast(mContext, "人脸比对成功!");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void selectLivenessControl(String name, String idCardNum) {
        int scanTime = UserUtil.getScanTimes(mContext);
        UserUtil.setScanTimes(mContext, ++scanTime);
        if (2 <= scanTime && scanTime < 5) {
            showScanErrorDialog(name, idCardNum);
        } else if (5 <= scanTime) {
            UserUtil.setCashCreditReason(mContext, "您不符合我们的征信条件");
            gotoActivity(mContext, VerifyFailActivity.class, null);
            MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_5);
            backActivity();
        } else {
            gotoFaceAddAddActivity();
        }
    }

    private void showScanErrorDialog(String name, String idCardNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("信息校验");
        builder.setMessage("请确认您的信息，若信息有误，请联系天神贷\n姓名：" + name + "\n" + "身份证号：" + idCardNum);
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoFaceAddAddActivity();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
            }
        });
        builder.create().show();
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
                ivIdentityAuthPic.setImageDrawable(drawable);
                LogUtil.d("abc", "设置身份证正面");
                break;
            case 1:
                ivIdentityAuthPic2.setImageDrawable(drawable);
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


        String api_key;
        String api_secret;
        if (mFaceKeyIsNew) {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;
        } else {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_OLD;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_OLD;
        }


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
     * 得到身份证反面信息
     */
    private void getIdcardBackInfo(byte[] data) {
        ViewUtil.createLoadingDialog(this, "", false);


        String api_key;
        String api_secret;
        if (mFaceKeyIsNew) {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;
        } else {
            api_key = GlobalParams.FACE_ADD_ADD_APPKEY_OLD;
            api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_OLD;
        }

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

    @Override
    protected boolean isOnKeyDown() {
        return isCanPressBack;
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

    public void uDunIdentity() {
        MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_12);
        String order = TianShenUserUtil.getUserId(mContext);
        AuthBuilder mAuthBuilder = new AuthBuilder(order, GlobalParams.UDUN_AUTH_KEY, NetConstantValue.getUDunNotifyURL(), new OnResultListener() {
            @Override
            public void onResult(String s) {
                MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_13);
                try {
                    UDunIDInfoBean uDunIDInfoBean = GsonUtil.json2bean(s, UDunIDInfoBean.class);
                    if (UDUN_VERIFY_SUCCESS_CODE.equals(uDunIDInfoBean.ret_code) && UDUN_VERIFY_SUCCESS_SIGN.equals(uDunIDInfoBean.result_auth)) {
                        if (ID_HAS_IDENTITY.equals(is_auth_idcard)) {
                            return;
                        }
                        if (!TextUtils.isEmpty(uDunIDInfoBean.id_no) && !TextUtils.isEmpty(uDunIDInfoBean.id_name)) {
                            etIdentityAuthName.setTextColor(getResources().getColor(R.color.global_txt_black4));
                            etIdentityAuthNum.setTextColor(getResources().getColor(R.color.global_txt_black4));
                        }
                        etIdentityAuthName.setText(uDunIDInfoBean.id_name);
                        etIdentityAuthNum.setText(uDunIDInfoBean.id_no);
                        ImageLoader.load(getApplicationContext(), uDunIDInfoBean.url_frontcard, ivIdentityAuthPic);
                        ImageLoader.load(getApplicationContext(), uDunIDInfoBean.url_backcard, ivIdentityAuthPic2);
                        ImageLoader.load(getApplicationContext(), uDunIDInfoBean.url_photoliving, ivIdentityAuthFace);
                    } else if (UDUN_VERIFY_USER_CANCEL.equals(uDunIDInfoBean.ret_code)) {
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "身份认证失败,请稍候再试");
                    }
                } catch (Exception e) {
                    //ignored
                }

            }
        });
        mAuthBuilder.faceAuth(mContext);
    }
}
