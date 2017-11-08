package com.tianshen.cash.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.FaceScanSuccessEvent;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.UploadImageBean;
import com.tianshen.cash.net.api.SaveIDCardFront;
import com.tianshen.cash.net.api.UploadImage;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.BitmapUtils;
import com.tianshen.cash.utils.FileUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class RiskPreAuthIdentitySupplyActivity extends BaseActivity {
    @BindView(R.id.iv_risk_pre_add_front)
    ImageView iv_risk_pre_add_front; //身份证正面图
    @BindView(R.id.iv_risk_pre_add_back)
    ImageView iv_risk_pre_add_back;//身份证反面图
    @BindView(R.id.et_risk_pre_supply_real_name)
    EditText et_risk_pre_supply_real_name; //姓名
    @BindView(R.id.et_risk_pre_supply_id_num)
    EditText et_risk_pre_supply_id_num;
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20; //上传图片 type  身份证正面
    private final int IMAGE_TYPE_ID_CARD_BACK = 21; //上传图片 type  身份证反面
    private int clickPosition;
    private String[] mImageFullPath = new String[2];
    private boolean isSaveFrontImage;
    private boolean isSaveBackImage;
    private String name;
    private String id_num;
    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_auth_identity_supply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_risk_pre_supply_back, R.id.tv_risk_pre_supply_commit, R.id.iv_risk_pre_add_front, R.id.iv_risk_pre_add_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_risk_pre_supply_back:
                backActivity();
                break;
            case R.id.tv_risk_pre_supply_commit://点击了提交
                uploadInfo();
                break;
            case R.id.iv_risk_pre_add_front:
            case R.id.iv_risk_pre_add_back:
                requestPermissionForImage(v.getId());
                break;
        }
    }

    /**
     * 上传身份信息
     */
    public void upLoadIdNumAndName() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("real_name", name);
            jsonObject.put("gender", "");
            jsonObject.put("nation", "");
            jsonObject.put("birthday", "");
            jsonObject.put("birthplace", "");
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
                    TianShenUserUtil.saveUserName(mContext, name);
                    TianShenUserUtil.saveUserIDNum(mContext, id_num);
                    ToastUtil.showToast(mContext, "上传身份证信息成功!");
                    gotoActivity(mContext, RiskPreScanFaceActivity.class, null);
                }

            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void uploadInfo() {
        if (!isSaveFrontImage) {
            ToastUtil.showToast(mContext, "请上传身份证正面照片");
            return;
        }
        if (!isSaveBackImage) {
            ToastUtil.showToast(mContext, "请上传身份证反面照片");
            return;
        }
        String name = et_risk_pre_supply_real_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(mContext, "请输入真实姓名");
            return;
        }
        String id_num = et_risk_pre_supply_id_num.getText().toString().trim();
        if (TextUtils.isEmpty(id_num)) {
            ToastUtil.showToast(mContext, "请输入身份证号");
            return;
        }
        this.name = name;
        this.id_num = id_num;
        upLoadImage(IMAGE_TYPE_ID_CARD_FRONT);

    }


    private void requestPermissionForImage(final int id) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    switch (id) {
                        case R.id.iv_risk_pre_add_front: //身份证正面
                            clickPosition = IMAGE_TYPE_ID_CARD_FRONT;
                            gotoSelectImage(clickPosition);
                            break;
                        case R.id.iv_risk_pre_add_back: //身份证反面
                            clickPosition = IMAGE_TYPE_ID_CARD_BACK;
                            gotoSelectImage(clickPosition);
                            break;
                    }
                }
            }
        });
    }

    private void gotoSelectImage(int position) {
        Intent intent_pick = new Intent(Intent.ACTION_PICK, null);
        intent_pick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BitmapUtils.IMAGE_UNSPECIFIED);
        startActivityForResult(intent_pick, position);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            Bitmap bitmap = null;
            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                bitmap = BitmapUtils.resizeBitmap(bmp);
                //上传图片
            } catch (FileNotFoundException e) {
                ToastUtil.showToast(mContext, "图片错误");
                e.printStackTrace();
            }
            if (bitmap != null) {
                byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);
                switch (requestCode) {
                    case IMAGE_TYPE_ID_CARD_FRONT:
                        mImageFullPath[0] = FileUtils.authIdentitySaveJPGFile(mContext, bytes, requestCode);
                        isSaveFrontImage = true;
                        break;
                    case IMAGE_TYPE_ID_CARD_BACK:
                        mImageFullPath[1] = FileUtils.authIdentitySaveJPGFile(mContext, bytes, requestCode);
                        isSaveBackImage = true;
                        break;
                }
                setImageSource(bytes, requestCode);

//                upLoadImage(bytes, requestCode);
            } else {
                ToastUtil.showToast(mContext, "图片错误");
            }


        }

    }


    /**
     * 上传图片
     */
    private void upLoadImage(final int mIsClickPosition) {

        String userID = TianShenUserUtil.getUserId(mContext);
        LogUtil.d("abc", "mIsClickPosition = " + mIsClickPosition);
        String path = "";
        String type = "";
        switch (mIsClickPosition) {
            case IMAGE_TYPE_ID_CARD_FRONT:
                type = IMAGE_TYPE_ID_CARD_FRONT + "";
                path = mImageFullPath[0];
                break;
            case IMAGE_TYPE_ID_CARD_BACK:
                type = IMAGE_TYPE_ID_CARD_BACK + "";
                path = mImageFullPath[1];
                break;
        }

        try {
            final UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userID);
            jsonObject.put("type", type);
            JSONObject newJson = SignUtils.signJsonNotContainList(jsonObject);

            uploadImage.uploadImage(newJson, path, true, new BaseNetCallBack<UploadImageBean>() {
                @Override
                public void onSuccess(UploadImageBean uploadImageBean) {
                    LogUtil.d("abc", "upLoadImage--onSuccess mIsClickPosition = " + mIsClickPosition);
                    switch (mIsClickPosition) {
                        case IMAGE_TYPE_ID_CARD_FRONT:
                            upLoadImage(IMAGE_TYPE_ID_CARD_BACK);
                            break;
                        case IMAGE_TYPE_ID_CARD_BACK:
                            upLoadIdNumAndName();
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

    private void setImageSource(byte[] imageSource, int mIsClickPosition) {

        Bitmap idcardBmp = BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
        switch (mIsClickPosition) {
            case IMAGE_TYPE_ID_CARD_FRONT:
                iv_risk_pre_add_front.setImageBitmap(idcardBmp);
                LogUtil.d("abc", "设置身份证正面");
                break;
            case IMAGE_TYPE_ID_CARD_BACK:
                iv_risk_pre_add_back.setImageBitmap(idcardBmp);
                LogUtil.d("abc", "设置身份证反面");
                break;
        }
    }

    @Subscribe
    public void onFaceScanSuccessEvent(FaceScanSuccessEvent event) {
        finish();
    }
}
