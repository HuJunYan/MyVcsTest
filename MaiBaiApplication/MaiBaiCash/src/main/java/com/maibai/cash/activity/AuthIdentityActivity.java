package com.maibai.cash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.IDCardBean;
import com.maibai.cash.model.SaveIdCardBean;
import com.maibai.cash.model.UploadImageBean;
import com.maibai.cash.net.api.IDCardAction;
import com.maibai.cash.net.api.SaveIdCardInformation;
import com.maibai.cash.net.api.UploadImage;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.ViewUtil;
import com.maibai.user.idcardlibrary.activity.IDCardScanActivity;
import com.maibai.user.idcardlibrary.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private String[] mImageFullPath = new String[7];
    private IDCardBean mIDCardBean;

    private int mIsClickPosition; //0==身份证正面,1==身份证背面，2==人脸识别
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20;
    private final int IMAGE_TYPE_ID_CARD_BACK = 21;
    private final int IMAGE_TYPE_SCAN_FACE = 25;


    private static final int MSG_IDCARD_NETWORK_WARRANTY_OK = 1; //face++联网授权成功
    private static final int MSG_IDCARD_NETWORK_WARRANTY_ERROR = 2;//face++联网授权失败


    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_IDCARD_NETWORK_WARRANTY_OK:
                    gotoFaceAddAddActivity();
                    break;
                case MSG_IDCARD_NETWORK_WARRANTY_ERROR:
                    ToastUtil.showToast(mContext, "联网授权失败，请重新认证");
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    // TODO 先上传正面身份证照
                    byte[] frontImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[0] = saveJPGFile(mContext, frontImg, IMAGE_TYPE_ID_CARD_FRONT);
                    upLoadImage(frontImg, IMAGE_TYPE_ID_CARD_FRONT);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(frontImg, 0, frontImg.length);
                    ivIdentityAuthPic.setImageBitmap(bitmap);
                    break;
                case GlobalParams.INTO_IDCARDSCAN_BACK_PAGE:
                    // TODO 先上传反面身份证照
                    byte[] backImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[1] = saveJPGFile(mContext, backImg, IMAGE_TYPE_ID_CARD_BACK);
//                    upLoadImage(backImg, IMAGE_TYPE_ID_CARD_BACK);
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(backImg, 0, backImg.length);
                    ivIdentityAuthPic2.setImageBitmap(bitmap2);
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    // TODO 先上次活体检测照片
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
    protected int setContentView() {
        return R.layout.activity_auth_identity;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        etIdentityAuthName.setEnabled(false);
        etIdentityAuthNum.setEnabled(false);
        ivIdentityAuthPic2.setEnabled(false);
        tvIdentityAuthBack.setOnClickListener(this);
        ivIdentityAuthPic.setOnClickListener(this);
        ivIdentityAuthPic2.setOnClickListener(this);
        ivIdentityAuthFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_identity_auth_back:
                backActivity();
                break;
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
        idCardNetWorkWarranty();
    }

    /**
     * 点击了人脸识别
     */
    private void onClickFace() {
        mIsClickPosition = 2;
        idCardNetWorkWarranty();
    }


    /**
     * 身份证联网授权
     */
    private void idCardNetWorkWarranty() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(mContext);
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
                Intent idCardScanIntent = new Intent(mContext, IDCardScanActivity.class);
                idCardScanIntent.putExtra("isvertical", true);
                idCardScanIntent.putExtra("side", 0);
                startActivityForResult(idCardScanIntent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                ToastUtil.showToast(mContext, "请拍摄身份证正面");
                break;
            case 1://反面
                Intent idCardScanBackIntent = new Intent(mContext, IDCardScanActivity.class);
                idCardScanBackIntent.putExtra("isvertical", true);
                idCardScanBackIntent.putExtra("side", 1);
                startActivityForResult(idCardScanBackIntent, GlobalParams.INTO_IDCARDSCAN_BACK_PAGE);
                ToastUtil.showToast(mContext, "请拍摄身份证反面");
                break;
            case 2:
                break;

        }
    }

    private void upLoadImage(final byte[] imageByte, final int type) {
        try {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("type", type + "");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);
            uploadImage.uploadImage(newJson, mImageFullPath[type - 20], true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    switch (type) {
                        case IMAGE_TYPE_ID_CARD_FRONT:
                            getIdcardFrontInfo(imageByte);
                            ivIdentityAuthPic2.setEnabled(true);
                            break;
                        case IMAGE_TYPE_ID_CARD_BACK:
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
     * 得到身份证正面信息
     */
    private void getIdcardFrontInfo(byte[] data) {
        ViewUtil.createLoadingDialog(this, "", false);
        new IDCardAction(this).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                ViewUtil.cancelLoadingDialog();
                mIDCardBean = paramT;
                if (mIDCardBean.id_card_number.length() != 18) {
                    ToastUtil.showToast(mContext, "身份证信息读取失败，请重新扫描身份证正面！");
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
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
        new IDCardAction(this).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                ViewUtil.cancelLoadingDialog();
                if (mIDCardBean != null && paramT != null) {
                    mIDCardBean.issued_by = paramT.issued_by;
                    mIDCardBean.valid_date = paramT.valid_date;
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
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

}
