package com.maibai.cash.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.base.MyApplication;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.SaveIdCardBean;
import com.maibai.user.idcardlibrary.activity.IDCardScanActivity;
import com.maibai.user.idcardlibrary.util.Util;
import com.maibai.user.livenesslibrary.activity.LivenessActivity;
import com.maibai.user.livenesslibrary.bean.MyMap;
import com.maibai.user.livenesslibrary.util.ConUtil;
import com.maibai.cash.model.IDCardBean;
import com.maibai.cash.model.ImageVerifyRequestBean;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.model.UploadImageBean;
import com.maibai.cash.net.api.CreditFace;
import com.maibai.cash.net.api.IDCardAction;
import com.maibai.cash.net.api.SaveIdCardInformation;
import com.maibai.cash.net.api.UploadImage;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.TimeCount;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.Utils;
import com.maibai.cash.utils.ViewUtil;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map.Entry;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class ResultActivity extends BaseActivity {
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20;
    private final int IMAGE_TYPE_ID_CARD_BACK = 21;
    private final int IMAGE_TYPE_SCAN_FACE = 25;
    IDCardAttr.IDCardSide mIDCardSide;
    private String COMMON_TAG = "";
    private boolean isCanPressBack=true;
    Handler mStartIdCardHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ViewUtil.cancelLoadingDialog();
            if (msg.what == 1) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "mStartIdCardHandler ===0");
                Intent intent = new Intent(ResultActivity.this,
                        IDCardScanActivity.class);
                intent.putExtra("side", 0);
                intent.putExtra("isvertical", true);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                ToastUtil.showToast(ResultActivity.this, "请拍摄身份证正面");
            } else if (msg.what == 2) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "mStartIdCardHandler ===1");
                ToastUtil.showToast(ResultActivity.this, "联网授权失败，请重新认证");
            }
        }
    };
    Handler mStartLivenessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ViewUtil.cancelLoadingDialog();
            switch (msg.what) {
                case 1:
                    gotoLiveness();
                    break;
                case 2:
                    new TimeCount(2000, 2000) {
                        @Override
                        public void onFinish() {
                            livenessNetWorkWarranty();
                        }
                    }.start();
                    break;
            }
        }
    };
    private int facePassScore = 65;
    private double confidence = 0;
    private double idcard_confidence = 0;
    private int mUpLoadIdCardTimes = 0;
    private int mUpLoadCreditFaceTimes = 0;
    private boolean isScanContinuous = false;
    private String[] mImageFullPath = new String[7];
    private ImageView mIDCardImageView;
    private ImageView mPortraitImageView;
    private TextView mPortraitSize;
    private IDCardBean mIDCardBean;
    private byte[] mHeadImg;
    private Intent intent;
    private MediaPlayer mMediaPlayer = null;

    public static String saveJPGFile(Context mContext, byte[] data, int type) {
        if (data == null)
            return null;
        File mediaStorageDir = mContext.getExternalFilesDir("idCardAndLiveness");
        if(null==mediaStorageDir){
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        COMMON_TAG = "ResultActivity: id = " + UserUtil.getId(mContext) + " ; mobile = " + UserUtil.getMobile(mContext);
        LogUtil.d("ret", "ResultActivity onCreate");
        String faceScore = UserUtil.getFacePassScore(mContext);
        if (!(faceScore != null || "".equals(faceScore) || "0".equals(faceScore))) {
            facePassScore = Integer.parseInt(faceScore, 10);
        }
        intent = getIntent();
        if (intent != null) {
            int type = intent.getIntExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
            switch (type) {
                case GlobalParams.SCAN_ID_CARD_TYPE:
                    isScanContinuous = true;
                    MobclickAgent.reportError(mContext, COMMON_TAG + "onCreate ===0");
                    idCardNetWorkWarranty();
                    break;
                case GlobalParams.SCAN_FACE_TYPE:
                    isScanContinuous = false;
                    MobclickAgent.reportError(mContext, COMMON_TAG + "onCreate ===1");
                    livenessNetWorkWarranty();
                    break;
            }
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_resutl;
    }

    @Override
    protected void findViews() {
        mIDCardImageView = (ImageView) findViewById(R.id.result_idcard_image);
        mPortraitImageView = (ImageView) findViewById(R.id.result_portrait_image);
        mPortraitSize = (TextView) findViewById(R.id.result_portrait_size);
    }

    @Override
    protected void setListensers() {

    }

    /**
     * 身份证联网授权
     */
    private void idCardNetWorkWarranty() {
        ViewUtil.createLoadingDialog(this, "正在联网授权", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.reportError(mContext, COMMON_TAG + "idCardNetWorkWarranty ===0");
                Manager manager = new Manager(ResultActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(ResultActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
                manager.takeLicenseFromNetwork(Util.getUUIDString(ResultActivity.this));
                Log.d("ret", idCardLicenseManager.checkCachedLicense() + "    checkCachedLicense");
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    mStartIdCardHandler.sendEmptyMessage(1);
                else
                    mStartIdCardHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    MobclickAgent.reportError(mContext, COMMON_TAG + "onActivityResult ===0");
                    // TODO 先上传正面身份证照
                    byte[] frontImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[0] = saveJPGFile(mContext, frontImg, IMAGE_TYPE_ID_CARD_FRONT);
                    upLoadImage(frontImg, IMAGE_TYPE_ID_CARD_FRONT);
                    break;
                case GlobalParams.INTO_IDCARDSCAN_BACK_PAGE:
                    MobclickAgent.reportError(mContext, COMMON_TAG + "onActivityResult ===1");
                    // TODO 先上传反面身份证照
                    byte[] backImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[1] = saveJPGFile(mContext, backImg, IMAGE_TYPE_ID_CARD_BACK);
                    upLoadImage(backImg, IMAGE_TYPE_ID_CARD_BACK);
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    MobclickAgent.reportError(mContext, COMMON_TAG + "onActivityResult ===2");
                    // TODO 先上次活体检测照片
                    livenessResult(data.getExtras());
                    break;
            }
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_ERROR) {
            MobclickAgent.reportError(mContext, COMMON_TAG + "onActivityResult ===3");
            ToastUtil.showToast(ResultActivity.this, "扫描失败,请手动输入吧");
            Intent intent = new Intent();
            setResult(GlobalParams.RETURN_FROM_ACTIVITY_ERROR, intent);
            backActivity();
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_BACK_KEY) {
            MobclickAgent.reportError(mContext, COMMON_TAG + "onActivityResult ===4");
            backActivity();
        }
    }

    private void upLoadImage(final byte[] imageByte, final int type) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "upLoadImage ===0");
        try {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("type", type + "");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);
            uploadImage.uploadImage(newJson, mImageFullPath[type - 20], true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    MobclickAgent.reportError(mContext, COMMON_TAG + "uploadImageBean.toString() = " + uploadImageBean.toString());
                    switch (type) {
                        case IMAGE_TYPE_ID_CARD_FRONT:
                            MobclickAgent.reportError(mContext, COMMON_TAG + "upLoadImage ===1");
                            setImageSource(imageByte, 0);
                            getIdcardFrontInfo(imageByte);
                            break;
                        case IMAGE_TYPE_ID_CARD_BACK:
                            MobclickAgent.reportError(mContext, COMMON_TAG + "upLoadImage ===2");
                            setImageSource(imageByte, 1);
                            getIdcardBackInfo(imageByte);
                            break;
                    }
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    MobclickAgent.reportError(mContext, COMMON_TAG + "upLoadImage ===3 ; result =" + result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void upLoadLivenessImage(final String delta, final MyMap map, final int type) {
        try {
            int count = map.getImages().size();
            if (count < 5) {
                count = 5;
            }
            String[] imageFullPatyArray = new String[5];
            for (int i = 0; i < count; i++) {
                imageFullPatyArray[i] = mImageFullPath[i + 2];
            }
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("type", type + "");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);
            uploadImage.uploadImageArray(newJson, imageFullPatyArray, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    compareImage(delta, map);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {
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
        for (Entry<String, byte[]> entry : map.getImages().entrySet()) {
            mImageFullPath[i + 2] = saveJPGFile(mContext, entry.getValue(), i + 25);
            i++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.reportError(mContext, COMMON_TAG + "onDestroy ===0");
        Utils.delJPGFile(mContext);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    private void livenessResult(Bundle bundle) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===0");
        String resultOBJ = bundle.getString("result");
        try {
            JSONObject result = new JSONObject(resultOBJ);
            int resID = result.getInt("resultcode");
            if (resID == R.string.verify_success) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===1");
                doPlay(R.raw.meglive_success);
            } else if (resID == R.string.liveness_detection_failed_not_video) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===2");
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed_timeout) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===3");
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===4");
                doPlay(R.raw.meglive_failed);
            } else {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===5");
                doPlay(R.raw.meglive_failed);
            }
            boolean isSuccess = result.getString("result").equals(
                    getResources().getString(R.string.verify_success));
            if (isSuccess) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===6");
//                // TODO 保存活体检测时的照片
                MyMap map = (MyMap) bundle.getSerializable("images");
                saveLivenessImage(map);
                upLoadLivenessImage(result.getString("delta"), map, IMAGE_TYPE_SCAN_FACE);
            } else {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===7");
                ToastUtil.showToast(mContext, "检测失败，请重新检测");
                gotoLiveness();
            }
        } catch (JSONException e) {
            MobclickAgent.reportError(mContext, COMMON_TAG + "livenessResult ===8");
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void setImageSource(byte[] imageSource, int side) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "setImageSource ===0");
        mIDCardSide = (side == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
                : IDCardAttr.IDCardSide.IDCARD_SIDE_BACK);
        Bitmap idcardBmp = BitmapFactory.decodeByteArray(imageSource, 0,
                imageSource.length);
        mIDCardImageView.setImageBitmap(idcardBmp);
        if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
            MobclickAgent.reportError(mContext, COMMON_TAG + "setImageSource ===1");
            mHeadImg = imageSource;
            Bitmap img = BitmapFactory.decodeByteArray(mHeadImg, 0,
                    mHeadImg.length);
            mPortraitImageView.setImageBitmap(img);
        } else {
            MobclickAgent.reportError(mContext, COMMON_TAG + "setImageSource ===2");
            mPortraitImageView.setVisibility(View.GONE);
            mPortraitSize.setText("");
        }
    }

    private void scanIDCardBack() {
        MobclickAgent.reportError(mContext, COMMON_TAG + "scanIDCardBack ===0");
        Intent intent = new Intent(ResultActivity.this,
                IDCardScanActivity.class);
        intent.putExtra("side", 1);
        intent.putExtra("isvertical", true);
        startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_BACK_PAGE);
    }

    private void getIdcardFrontInfo(byte[] data) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardFrontInfo ===0");
        ViewUtil.createLoadingDialog(this, "", false);
        new IDCardAction(this).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardFrontInfo ===1");
                ViewUtil.cancelLoadingDialog();
                mIDCardBean = paramT;
                if (mIDCardBean.id_card_number.length() != 18) {
                    ToastUtil.showToast(mContext, "身份证信息读取失败，请重新扫描身份证正面！");
                    mStartIdCardHandler.sendEmptyMessage(1);
                } else {
                    scanIDCardBack();
                    ToastUtil.showToast(ResultActivity.this, "请拍摄身份证反面");
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardFrontInfo ===2");
                ToastUtil.showToast(ResultActivity.this, "请使用身份证原件");
                backActivity();
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    private void getIdcardBackInfo(byte[] data) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardBackInfo ===0");
        ViewUtil.createLoadingDialog(this, "", false);
        new IDCardAction(this).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardBackInfo ===1");
                ViewUtil.cancelLoadingDialog();
                if (mIDCardBean != null && paramT != null) {
                    MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardBackInfo ===2");
                    mIDCardBean.issued_by = paramT.issued_by;
                    mIDCardBean.valid_date = paramT.valid_date;
                    // TODO 上传身份证信息,把活体检测的代码放到上传身份证信息成功后的回调中
                    saveIdCardInformation();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "getIdcardBackInfo ===3");
                // TODO 错误次数统计,根据次数来判断是否手动输入
                ToastUtil.showToast(ResultActivity.this, "请使用身份证原件");
                scanIDCardBack();
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    /**
     * 保存身份证信息，成功后启用活体检测
     */
    private void saveIdCardInformation() {
        JSONObject json = new JSONObject();
        try {
            MobclickAgent.reportError(mContext, COMMON_TAG + "saveIdCardInformation ===0");
            json.put("customer_id", UserUtil.getId(mContext));
            json.put("real_name", mIDCardBean.name);
            json.put("gender", mIDCardBean.gender);
            json.put("nation", mIDCardBean.race);
            json.put("birthday", mIDCardBean.birthday.year + "年" + mIDCardBean.birthday.month + "月" + mIDCardBean.birthday.day + "日");
            json.put("birthplace", mIDCardBean.address);
            json.put("id_num", mIDCardBean.id_card_number);
            json.put("sign_organ", mIDCardBean.issued_by);
            json.put("valid_period", mIDCardBean.valid_date);
            json.put("type", "1");
            json.put("customer_id", UserUtil.getId(mContext));
            SaveIdCardInformation mSaveIdCardInformationAction = new SaveIdCardInformation(mContext);
            mSaveIdCardInformationAction.saveIdCardInformation(json, new BaseNetCallBack<SaveIdCardBean>() {
                @Override
                public void onSuccess(SaveIdCardBean paramT) {
                    MobclickAgent.reportError(mContext, COMMON_TAG + "saveIdCardInformation ===1");
                    if(null==paramT){
                        ToastUtil.showToast(mContext,"数据失败");
                        return;
                    }
                    if(null==paramT.getData()){
                        ToastUtil.showToast(mContext,"数据失败");
                        return;
                    }
                    if(GlobalParams.NOT_QUALIFIED.equals(paramT.getData().getQualified())){
                        //用户在黑名单时直接跳转至拒绝
                        //用户在黑名单时直接跳转至拒绝
                        String reason=paramT.getData().getReason();
                        if(null==reason){
                            reason="";
                        }
                        UserUtil.setCashCreditStatus(mContext,GlobalParams.CASH_APPLY_REFUSE_BY_MACHINE);
                        UserUtil.setCashCreditReason(mContext,reason);
                        gotoActivity(mContext,VerifyFailActivity.class,null);
                        backActivity();
                        return;
                    }

                    gotoActivity(mContext, ImproveQuotaActivity.class, intent.getExtras());

                    UserUtil.setRealName(mContext, mIDCardBean.name);
                    UserUtil.setIdNum(mContext, mIDCardBean.id_card_number);
                    UserUtil.setCreditStep(mContext, GlobalParams.HAVE_UPLOAD_IDCARD_INFO + "");
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if (mUpLoadIdCardTimes == 0) {
                        MobclickAgent.reportError(mContext, COMMON_TAG + "saveIdCardInformation ===2");
                        ToastUtil.showToast(ResultActivity.this, "上传数据失败，自动重试中");
                        saveIdCardInformation();
                        mUpLoadIdCardTimes++;
                    } else {
                        MobclickAgent.reportError(mContext, COMMON_TAG + "saveIdCardInformation ===3");
                        // TODO 数据上传失败，建议保存数据,下次重试时先查看是否有保存的数据，如果有，直接上传保存的数据，无需再扫脸了
                        ToastUtil.showToast(ResultActivity.this, "上传数据失败,请手动上传");
                        Intent intent = new Intent();
                        setResult(GlobalParams.RETURN_FROM_ACTIVITY_ERROR, intent);
                        backActivity();
                    }
                }
            });
        } catch (JSONException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }

    }

    /**
     * 活体联网授权
     */
    private void livenessNetWorkWarranty() {
        ViewUtil.createLoadingDialog(this, "正在联网授权", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.reportError(mContext, COMMON_TAG + "livenessNetWorkWarranty ===0");
                Manager manager = new Manager(ResultActivity.this);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(
                        ResultActivity.this);
                manager.registerLicenseManager(licenseManager);

                manager.takeLicenseFromNetwork(ConUtil.getUUIDString(ResultActivity.this));
                if (licenseManager.checkCachedLicense() > 0)
                    mStartLivenessHandler.sendEmptyMessage(1);
                else
                    mStartLivenessHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    private void compareImage(String delta, MyMap map) {
        ImageVerifyRequestBean bean = new ImageVerifyRequestBean();
        if (isScanContinuous) {
            MobclickAgent.reportError(mContext, COMMON_TAG + "compareImage ===0");
           /* bean.name = mIDCardBean.name;
            bean.idcard = mIDCardBean.id_card_number;*/
            bean.name = UserUtil.getRealName(mContext);
            bean.idcard = UserUtil.getIdNum(mContext);
        } else {
            MobclickAgent.reportError(mContext, COMMON_TAG + "compareImage ===1");
            bean.name = UserUtil.getRealName(mContext);
            bean.idcard = UserUtil.getIdNum(mContext);
        }
        bean.imageref = Utils.saveJPGFile(this, mHeadImg, "image_ref1");
        bean.delta = delta;
        bean.images = map.getImages();
        if (isScanContinuous) {
            imageVerify_2_scanIDCard(bean);
        } else {
            imageVerify_2_noScanIDCard(bean);
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

    private void imageVerify_2_noScanIDCard(final ImageVerifyRequestBean bean) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_noScanIDCard ===0");
        ViewUtil.createLoadingDialog(ResultActivity.this, "正在与身份证比对，请稍候", false);
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("idcard_name", bean.name);
            requestParams.put("idcard_number", bean.idcard);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        requestParams.put("delta", bean.delta);
        requestParams.put("api_key", "cX9UMpO-z5GG1KJkuRslGCTiC9JQOUUJ");
        requestParams.put("api_secret", "f8NhOZausOpR1pKNpQA5dgHNr0w3pdn5");

        requestParams.put("comparison_type", "1");
        requestParams.put("face_image_type", "meglive");

        for (Entry<String, byte[]> entry : bean.images.entrySet()) {
            requestParams.put(entry.getKey(),
                    new ByteArrayInputStream(entry.getValue()));
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://api.megvii.com/faceid/v2/verify";
        asyncHttpClient.post(url, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_noScanIDCard ===1");
                        ViewUtil.cancelLoadingDialog();
                        String successStr = new String(bytes);
                        Log.d("ret", successStr);
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
//                                    gotoLiveness();
                                } else {
                                    conformCreditFace();
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
                        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_noScanIDCard ===2");
                        ViewUtil.cancelLoadingDialog();
                        ToastUtil.showToast(mContext, "人脸比对失败，请重新检测");
                        gotoLiveness();
                    }
                });
    }


    private void imageVerify_2_scanIDCard(final ImageVerifyRequestBean bean) {
        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_scanIDCard ===0");
        ViewUtil.createLoadingDialog(ResultActivity.this, "正在与身份证比对，请稍候", false);
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("idcard_name", bean.name);
            requestParams.put("idcard_number", bean.idcard);
            requestParams.put("image_ref1", new FileInputStream(new File(bean.imageref)));// 传入身份证头像照片路径
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        requestParams.put("delta", bean.delta);
        requestParams.put("api_key", "cX9UMpO-z5GG1KJkuRslGCTiC9JQOUUJ");
        requestParams.put("api_secret", "f8NhOZausOpR1pKNpQA5dgHNr0w3pdn5");

        requestParams.put("comparison_type", "1");
        requestParams.put("face_image_type", "meglive");

        for (Entry<String, byte[]> entry : bean.images.entrySet()) {
            requestParams.put(entry.getKey(),
                    new ByteArrayInputStream(entry.getValue()));
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://api.megvii.com/faceid/v2/verify";
        asyncHttpClient.post(url, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_scanIDCard ===1");
                        ViewUtil.cancelLoadingDialog();
                        String successStr = new String(bytes);
                        Log.d("ret", successStr);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(successStr);
                            if (!jsonObject.has("error")) {
                                // 活体最好的一张照片和公安部系统上身份证上的照片比较
                                confidence = jsonObject.getJSONObject("result_faceid").getDouble("confidence");
                                // 活体最好的一张照片和拍摄身份证上的照片的比较

                                JSONObject confidenceObj = jsonObject.getJSONObject("result_ref1");
//
                                idcard_confidence = jsonObject.getJSONObject("result_ref1").getDouble("confidence");
                                // 解析faceGen
                                JSONObject jObject = jsonObject.getJSONObject("face_genuineness");
                                float mask_confidence = (float) jObject.getDouble("mask_confidence");
                                float screen_replay_confidence = (float) jObject.getDouble("screen_replay_confidence");
                                float synthetic_face_confidence = (float) jObject.getDouble("synthetic_face_confidence");
                                if (mask_confidence > (float) facePassScore / 100 || screen_replay_confidence > (float) facePassScore / 100 || synthetic_face_confidence > (float) facePassScore / 100 || confidence < facePassScore || idcard_confidence < facePassScore) {
                                    ToastUtil.showToast(mContext, "人脸比对失败，请重新检测");
                                    // 2次弹出对话框，确认取消按钮，点击确认继续调用gotoLiveness，点击取消跳到主页
                                    selectLivenessControl(bean.name, bean.idcard);
//                                    gotoLiveness();
                                } else {
                                    conformCreditFace();
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
                        MobclickAgent.reportError(mContext, COMMON_TAG + "imageVerify_2_scanIDCard ===2");
                        ViewUtil.cancelLoadingDialog();
                        ToastUtil.showToast(mContext, "人脸比对失败，请重新检测");
                        gotoLiveness();
                    }
                });
    }
    private void selectLivenessControl(String name,String idCardNum){
        int scanTime=UserUtil.getScanTimes(mContext);
        UserUtil.setScanTimes(mContext,++scanTime);

        if(2<=scanTime&&scanTime<5){
            showScanErrorDialog(name, idCardNum);
        }else if(5<=scanTime){
            UserUtil.setCashCreditReason(mContext,"您不符合我们的征信条件");
            gotoActivity(mContext,VerifyFailActivity.class,null);
            backActivity();
        }else{
            gotoLiveness();
        }
    }
    private void showScanErrorDialog(String name,String idCardNum){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("信息校验");
        builder.setMessage("请确认您的信息，若信息有误，请联系天神贷\n姓名："+name+"\n"+"身份证号："+idCardNum);
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoLiveness();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
            }
        });
        builder.create().show();
    }
    private void conformCreditFace() throws JSONException {
        MobclickAgent.reportError(mContext, COMMON_TAG + "conformCreditFace ===0");
        CreditFace creditFace = new CreditFace(mContext);
        JSONObject json = new JSONObject();
        json.put("customer_id", UserUtil.getId(mContext));
        json.put("face_pass", "1");
        isCanPressBack=false;
        creditFace.creditFace(json, null, true, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "conformCreditFace ===1");
                Utils.delJPGFile(mContext);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                double mLivenessResult = 0;
                if (isScanContinuous) {
                    mLivenessResult = (confidence + idcard_confidence) / 2;
                } else {
                    mLivenessResult = confidence;
                }
                bundle.putDouble("result", mLivenessResult);
                bundle.putSerializable(GlobalParams.ID_CARD_BEAN_KEY, mIDCardBean);
                intent.putExtras(bundle);
                UserUtil.setCreditStep(mContext, GlobalParams.HAVE_SCAN_FACE + "");
                NumberFormat nFormat = NumberFormat.getNumberInstance();
                nFormat.setMaximumFractionDigits(2);//设置小数点后面位数为
                Log.d("ret", "人脸比对成功，相似度" + nFormat.format(mLivenessResult) + "%");
                ToastUtil.showToast(mContext, "人脸比对成功，相似度" + nFormat.format(mLivenessResult) + "%");
                setResult(RESULT_OK, intent);
                backActivity();
                isCanPressBack=true;
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                MobclickAgent.reportError(mContext, COMMON_TAG + "conformCreditFace ===1 ; mUpLoadCreditFaceTimes = " + mUpLoadCreditFaceTimes);
                if (mUpLoadCreditFaceTimes < 3) {
                    try {
                        conformCreditFace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MobclickAgent.reportError(mContext, LogUtil.getException(e));
                    }
                    mUpLoadCreditFaceTimes++;
                }
            }
        });
    }

    private void gotoLiveness() {

        startActivityForResult(new Intent(ResultActivity.this, LivenessActivity.class),
                GlobalParams.PAGE_INTO_LIVENESS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 所有需要统一处理的onKeyDown写在这个if里面
        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                backActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isOnKeyDown() {
        return isCanPressBack;
    }
}
