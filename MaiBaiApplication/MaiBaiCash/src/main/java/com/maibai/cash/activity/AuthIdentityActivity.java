package com.maibai.cash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
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
import com.maibai.cash.model.WithdrawalsItemBean;
import com.maibai.cash.net.api.IDCardAction;
import com.maibai.cash.net.api.SaveIdCardInformation;
import com.maibai.cash.net.api.UploadImage;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.ViewUtil;
import com.maibai.user.idcardlibrary.activity.IDCardScanActivity;
import com.maibai.user.idcardlibrary.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20; //上传图片 type  身份证正面
    private final int IMAGE_TYPE_ID_CARD_BACK = 21; //上传图片 type  身份证反面
    private final int IMAGE_TYPE_SCAN_FACE = 25; //上传图片 type  活体检测图组


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
                    break;
                case GlobalParams.INTO_IDCARDSCAN_BACK_PAGE:
                    // TODO 先上传反面身份证照
                    byte[] backImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[1] = saveJPGFile(mContext, backImg, IMAGE_TYPE_ID_CARD_BACK);
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
                backActivity();
                break;
            case R.id.tv_identity_post:
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

    /**
     * 上传图片测试
     */
    private void postImageTest() {

        try {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();

            long customer_id = TianShenUserUtil.getUserId(mContext);

            jsonObject.put("customer_id", customer_id + "");
            jsonObject.put("type", "20");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);
            Drawable drawable = ivIdentityAuthFace.getDrawable();
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            String path = saveJPGFile(mContext, bytes, 20);

            ivIdentityAuthPic.setImageBitmap(bitmap);

            uploadImage.uploadImage(newJson, path, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    ToastUtil.showToast(mContext, "上传成功!");
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    ToastUtil.showToast(mContext, "上传失败!errorCode--->" + errorCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }


    /**
     * 上传图片
     */
    private void upLoadImage(final byte[] imageByte, final int type) {
        LogUtil.d("abc","upLoadImage--in");

        long userID = TianShenUserUtil.getUserId(mContext);

        try {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", userID);
            jsonObject.put("type", type + "");
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);

            String path = "";
            switch (type) {
                case IMAGE_TYPE_ID_CARD_FRONT:
                    path = mImageFullPath[0];
                    break;
                case IMAGE_TYPE_ID_CARD_BACK:
                    path = mImageFullPath[1];
                    break;
            }

            uploadImage.uploadImage(newJson, path, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    LogUtil.d("abc","upLoadImage--onSuccess");
                    switch (type) {
                        case IMAGE_TYPE_ID_CARD_FRONT:
                            setImageSource(imageByte);
                            getIdcardFrontInfo(imageByte);
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

    private void setImageSource(byte[] imageSource) {
        LogUtil.d("abc","setImageSource");
        Bitmap idcardBmp = BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
        switch (mIsClickPosition) {
            case 0:
                ivIdentityAuthPic.setImageBitmap(idcardBmp);
                break;
            case 1:
                ivIdentityAuthPic2.setImageBitmap(idcardBmp);
                break;
            case 2:
                ivIdentityAuthFace.setImageBitmap(idcardBmp);
                break;
        }
    }

    /**
     * 得到身份证正面信息
     */
    private void getIdcardFrontInfo(byte[] data) {
        LogUtil.d("abc","getIdcardFrontInfo");
        ViewUtil.createLoadingDialog(this, "", false);
        new IDCardAction(this).getIDCardInfo(data, new BaseNetCallBack<IDCardBean>() {
            @Override
            public void onSuccess(IDCardBean paramT) {
                ViewUtil.cancelLoadingDialog();
                mIDCardBean = paramT;
                refreshNameAndNumUI();
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
     * 刷新名字和身份证号UI
     */
    private void refreshNameAndNumUI() {
        String name = mIDCardBean.name;
        String num = mIDCardBean.id_card_number;
        etIdentityAuthName.setText(name);
        etIdentityAuthNum.setText(num);

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
