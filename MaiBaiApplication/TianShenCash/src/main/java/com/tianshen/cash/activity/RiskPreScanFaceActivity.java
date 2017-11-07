package com.tianshen.cash.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.FaceScanSuccessEvent;
import com.tianshen.cash.event.IdcardImageEvent;
import com.tianshen.cash.liveness.activity.LivenessActivity;
import com.tianshen.cash.liveness.bean.MyMap;
import com.tianshen.cash.liveness.util.ConUtil;
import com.tianshen.cash.model.IdNumInfoBean;
import com.tianshen.cash.model.ImageVerifyRequestBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.UploadImageBean;
import com.tianshen.cash.net.api.CreditFace;
import com.tianshen.cash.net.api.GetIdNumInfo;
import com.tianshen.cash.net.api.UploadImage;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.FileUtils;
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
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class RiskPreScanFaceActivity extends BaseActivity {
    @BindView(R.id.iv_risk_pre_face)
    ImageView iv_risk_pre_face;

    private String is_auth_idcard;
    private IdNumInfoBean mIdNumInfoBean;
    private static final int MSG_IDCARD_NETWORK_FACE_OK = 3;//face++扫脸授权失败
    private static final int MSG_IDCARD_NETWORK_FACE_ERROR = 4;//face++扫脸授权失败
    private final int IMAGE_TYPE_SCAN_FACE = 25; //上传图片 type  活体检测图组
    private int facePassScore = 65;
    private MediaPlayer mMediaPlayer;
    private double confidence = 0;

    private boolean isCanPressBack = true;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
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
    private String[] mImageFullPath = new String[7];
    byte[] heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IdcardImageEvent event = EventBus.getDefault().getStickyEvent(IdcardImageEvent.class);
        if (event != null) {
            heads = event.bytes;
            EventBus.getDefault().removeStickyEvent(IdcardImageEvent.class);
        }
        initIdNumInfo();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_scan_face;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_risk_pre_face_back, R.id.tv_risk_pre_start_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_risk_pre_face_back:
                backActivity();
                break;
            case R.id.tv_risk_pre_start_scan:
                requestPermissionsToNextActivity();
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
        String face_url = mIdNumInfoBean.getData().getFace_url();

        ImageLoader.load(getApplicationContext(), face_url, iv_risk_pre_face);


    }
    //请求相机权限 并根据结果 决定是否进行跳转

    private void requestPermissionsToNextActivity() {
        boolean isReallyHasPermission = checkPermissionForFlyme();
        if (!isReallyHasPermission) {
            ToastUtil.showToast(this, "请去设置开启照相机权限");
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(RiskPreScanFaceActivity.this);
        rxPermissions.request(android.Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (mIdNumInfoBean == null || mIdNumInfoBean.getData() == null) {
                    return;
                }
                if (aBoolean) {
                    livenessNetWorkWarranty();
                } else {
                    ToastUtil.showToast(RiskPreScanFaceActivity.this, "请去设置开启照相机权限");
                }
            }
        });

    }

    /**
     * 跳转到face++的页面
     */
    private void gotoFaceAddAddActivity() {
        isCanPressBack = false;
        startActivityForResult(new Intent(mContext, LivenessActivity.class), GlobalParams.PAGE_INTO_LIVENESS);
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

    //检查是否是魅族Flyme系统 如果是 则判断是否有相机权限 不是则不判断

    private boolean checkPermissionForFlyme() {
        boolean isReallyHasPermission;
        if (MyApplicationLike.isFlyMe) {
            isReallyHasPermission = RomUtils.isCameraCanUse();
        } else {
            isReallyHasPermission = true;
        }
        return isReallyHasPermission;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.PAGE_INTO_LIVENESS:
                    LogUtil.d("abc", "onActivityResult--人脸识别");
                    livenessResult(data.getExtras());
                    break;
            }
        }
    }

    private void livenessResult(Bundle bundle) {

        MyMap myMap = (MyMap) bundle.getSerializable("images");
        if (myMap == null) {
            ToastUtil.showToast(mContext, "检测失败，重新再试!");
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
                MyMap map = (MyMap) bundle.getSerializable("images");
                saveLivenessImage(map);
                upLoadLivenessImage(result.getString("delta"), map, IMAGE_TYPE_SCAN_FACE);
            } else {
                ToastUtil.showToast(mContext, "检测失败，请重新检测");
                gotoFaceAddAddActivity();
            }
        } catch (JSONException e) {
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
                    ImageLoader.load(getApplicationContext(), facePath, iv_risk_pre_face);
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
        byte[] headImage = heads;
        bean.name = name;
        bean.idcard = id_num;
        bean.imageref = Utils.saveJPGFile(this, headImage, "image_ref1");
        bean.delta = delta;
        bean.images = map.getImages();
        imageVerify_2_noScanIDCard(bean);

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

        String api_key = GlobalParams.FACE_ADD_ADD_APPKEY_NEW;
        String api_secret = GlobalParams.FACE_ADD_ADD_APPSECRET_NEW;

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
                EventBus.getDefault().post(new FaceScanSuccessEvent());
                finish();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    @Override
    protected boolean isOnKeyDown() {
        return isCanPressBack;
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


    private void saveLivenessImage(MyMap map) {
        int count = map.getImages().size();
        if (count > 5) {
            count = 5;
        }
        int i = 0;
        for (Map.Entry<String, byte[]> entry : map.getImages().entrySet()) {
            mImageFullPath[i + 2] = FileUtils.authIdentitySaveJPGFile(mContext, entry.getValue(), i + 25);
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
}
