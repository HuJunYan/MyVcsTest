package com.tianshen.cash.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.FaceScanSuccessEvent;
import com.tianshen.cash.event.IdcardImageEvent;
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
import com.tianshen.cash.utils.FileUtils;
import com.tianshen.cash.utils.ImageLoader;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RomUtils;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class RiskPreAuthIdentityActivity extends BaseActivity {


    @BindView(R.id.iv_scan_identity_front)//身份证正面
            ImageView iv_scan_identity_front;
    @BindView(R.id.iv_scan_identity_back) //身份证反面
            ImageView iv_scan_identity_back;
    @BindView(R.id.view_placeholder)
    View view_placeholder; //占位view
    @BindView(R.id.ll_risk_pre_bottom_tips_layout) //底部提示
            View ll_risk_pre_bottom_tips_layout;
    @BindView(R.id.rl_risk_pre_id_num_layout)//身份证号
            View rl_risk_pre_id_num_layout;
    @BindView(R.id.rl_risk_pre_name_layout)//名字
            View rl_risk_pre_name_layout;
    @BindView(R.id.et_risk_pre_id_num)
    EditText et_risk_pre_id_num;
    @BindView(R.id.et_risk_pre_real_name)
    EditText et_risk_pre_real_name;
    @BindView(R.id.tv_risk_pre_forget_id_card)
    TextView tv_risk_pre_forget_id_card; //忘带身份证提示
    private boolean mCanScanFace;
    private IdNumInfoBean mIdNumInfoBean;
    private int mIsClickPosition; //0==身份证正面,1==身份证背面，

    private IDCardBean mIDCardBean;
    private final int IMAGE_TYPE_ID_CARD_FRONT = 20; //上传图片 type  身份证正面
    private final int IMAGE_TYPE_ID_CARD_BACK = 21; //上传图片 type  身份证反面
    private boolean isSaveFrontImageSuccess; //身份证正面信息是否ok

    private static final int MSG_IDCARD_NETWORK_WARRANTY_OK = 1; //face++身份证联网授权成功
    private static final int MSG_IDCARD_NETWORK_WARRANTY_ERROR = 2;//face++身份证联网授权失败

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
            }
        }
    };

    private String up_status_front = "1";


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

    @OnClick({R.id.tv_risk_pre_back, R.id.iv_scan_identity_front, R.id.iv_scan_identity_back, R.id.tv_risk_pre_commit, R.id.tv_risk_pre_forget_id_card})
    public void onClick(View view) {
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
            case R.id.tv_risk_pre_commit:// 点击了提交 判断前往扫脸
//                if (mHasIdentityInfo) {//服务器已经有了身份证信息 直接跳转
//                    gotoActivity(mContext, RiskPreScanFaceActivity.class, null);
//                    return;
//                }
                if (mCanScanFace) { //正反面 都已经扫描完毕
                    initSaveIDCardFront(); //保存正面信息
                    return;
                } else {
                    ToastUtil.showToast(mContext, "请先扫描身份证");
                }
                break;
            case R.id.tv_risk_pre_forget_id_card: //点击了忘带身份证
                gotoActivity(mContext, RiskPreAuthIdentitySupplyActivity.class, null);
                finish();
                break;
        }
    }


    /**
     * 得到用户认证的信息
     */
    private void initIdNumInfo() {
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
        //设置显隐 和信息
        if (!TextUtils.isEmpty(real_name) && !TextUtils.isEmpty(id_num)) {
            tv_risk_pre_forget_id_card.setVisibility(View.GONE);
            ll_risk_pre_bottom_tips_layout.setVisibility(View.VISIBLE);
            rl_risk_pre_id_num_layout.setVisibility(View.VISIBLE);
            rl_risk_pre_name_layout.setVisibility(View.VISIBLE);
            view_placeholder.setVisibility(View.GONE);
            et_risk_pre_real_name.setText(real_name);
            et_risk_pre_id_num.setText(id_num);
            if (TextUtils.isEmpty(mIdNumInfoBean.getData().getNation())) {
                up_status_front = "2";
            }
        }

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
        if (isSaveFrontImageSuccess) {
            idCardNetWorkWarranty();
        } else {
            ToastUtil.showToast(mContext, "先上传身份证正面");
        }
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
        }
    }


    /**
     * 得到头像的byte[]
     */
    private byte[] getHeadImage() {
        iv_scan_identity_front.setDrawingCacheEnabled(true);//获取bm前执行，否则无法获取
        Bitmap bitmap = iv_scan_identity_front.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        iv_scan_identity_front.setDrawingCacheEnabled(false);
        return baos.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    LogUtil.d("abc", "onActivityResult--身份证正面");
                    byte[] frontImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[0] = FileUtils.authIdentitySaveJPGFile(mContext, frontImg, IMAGE_TYPE_ID_CARD_FRONT);
                    upLoadImage(frontImg);
                    break;
                case GlobalParams.INTO_IDCARDSCAN_BACK_PAGE:
                    LogUtil.d("abc", "onActivityResult--身份证反面");
                    byte[] backImg = data.getByteArrayExtra("idcardImg");
                    mImageFullPath[1] = FileUtils.authIdentitySaveJPGFile(mContext, backImg, IMAGE_TYPE_ID_CARD_BACK);
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
        LogUtil.d("abc", "mIsClickPosition = " + mIsClickPosition);
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
                    LogUtil.d("abc", "upLoadImage--onSuccess mIsClickPosition = " + mIsClickPosition);
                    switch (mIsClickPosition) {
                        case 0:
                            setImageSource(imageByte);
                            getIdcardFrontInfo(imageByte);
                            break;
                        case 1:
                            setImageSource(imageByte);
                            getIdcardBackInfo(imageByte);
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
                mCanScanFace = true;

                if (paramT != null) {
                    if (mIdNumInfoBean == null) {
                        mIdNumInfoBean = new IdNumInfoBean();
                        mIdNumInfoBean.setData(new IdNumInfoBean.Data());
                    }
                    //设置身份证反面信息
                    mIdNumInfoBean.getData().setValid_period(paramT.valid_date);
                    mIdNumInfoBean.getData().setSign_organ(paramT.issued_by);
                    LogUtil.d("abc", "设置身份证反面信息 success");
//                    initSaveIDCardBack();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
                LogUtil.d("abc", "设置身份证反面信息 failure");
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    /**
     * 保存身份证反面信息到云端
     */
    private void initSaveIDCardBack() {
        IdNumInfoBean.Data data = mIdNumInfoBean.getData();
        String sign_organ = data.getSign_organ(); //身份证签发机关
        String valid_date = data.getValid_period(); //身份证有效期限

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("sign_organ", sign_organ);
            jsonObject.put("valid_period", valid_date);
            jsonObject.put("up_status", up_status_front);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SaveIDCardBack saveIDCardBack = new SaveIDCardBack(mContext);
        saveIDCardBack.saveIDCardBack(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                int code = paramT.getCode();
                if (0 == code) {
                    byte[] headImage = getHeadImage();
                    EventBus.getDefault().postSticky(new IdcardImageEvent(headImage));
                    ToastUtil.showToast(mContext, "上传身份证信息成功!");
                    gotoActivity(mContext, RiskPreScanFaceActivity.class, null);
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
                setIdBeanData();
                if (mIDCardBean.id_card_number.length() != 18) {
                    ToastUtil.showToast(mContext, "身份证信息读取失败，请重新扫描身份证正面！");
                }
                mCanScanFace = false;
                up_status_front = "1";
                isSaveFrontImageSuccess = true;
                refreshNameAndNumUI();
//                initSaveIDCardFront();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ToastUtil.showToast(mContext, "请使用身份证原件");
                backActivity();
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    //扫描完身份证正面信息 获取到以后  设置在bean中
    private void setIdBeanData() {
        if (mIDCardBean != null) {
            if (mIdNumInfoBean == null) {
                mIdNumInfoBean = new IdNumInfoBean();
                mIdNumInfoBean.setData(new IdNumInfoBean.Data());
            }
            IdNumInfoBean.Data data = mIdNumInfoBean.getData();
            data.setReal_name(mIDCardBean.name);
            data.setGender(mIDCardBean.gender);
            data.setNation(mIDCardBean.race);

            IDCardBean.Birthday birthdayBean = mIDCardBean.birthday;
            String year = birthdayBean.year;
            String month = birthdayBean.month;
            String day = birthdayBean.day;
            data.setBirthday(year + "年" + month + "月" + day + "日");
            data.setBirthplace(mIDCardBean.address);
            data.setId_num(mIDCardBean.id_card_number);
//            mIDCardBean.issued_by = paramT.issued_by;
//            mIDCardBean.valid_date = paramT.valid_date;
        }
    }

    /**
     * 刷新名字和身份证号UI
     */
    private void refreshNameAndNumUI() {
        String name = mIdNumInfoBean.getData().getReal_name();
        String num = mIdNumInfoBean.getData().getId_num();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(num)) {
            tv_risk_pre_forget_id_card.setVisibility(View.GONE);
            ll_risk_pre_bottom_tips_layout.setVisibility(View.VISIBLE);
            rl_risk_pre_id_num_layout.setVisibility(View.VISIBLE);
            rl_risk_pre_name_layout.setVisibility(View.VISIBLE);
            view_placeholder.setVisibility(View.GONE);
            et_risk_pre_real_name.setText(name);
            et_risk_pre_id_num.setText(num);
        }
    }


    /**
     * 保存身份证正面信息到云端
     */
    private void initSaveIDCardFront() {
        IdNumInfoBean.Data data = mIdNumInfoBean.getData();
        if (data == null) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        final String real_name = et_risk_pre_real_name.getText().toString().trim(); //身份证姓名
        String gender = data.getGender(); // 身份证性别
        String nation = data.getNation(); // 民族

        String birthday = data.getBirthday(); //出生日期
        String birthplace = data.getBirthplace(); //住址
        final String id_num = et_risk_pre_id_num.getText().toString().trim(); // 身份证号
        if (TextUtils.isEmpty(id_num) || TextUtils.isEmpty(real_name)) {
            ToastUtil.showToast(mContext, "姓名和身份证号不能为空");
            return;
        }
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
            jsonObject.put("up_status", up_status_front);// 正常扫脸

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SaveIDCardFront saveIDCardFront = new SaveIDCardFront(mContext);
        saveIDCardFront.saveIDCardFront(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                int code = paramT.getCode();
                if (0 == code) {
                    TianShenUserUtil.saveUserName(mContext, real_name);
                    TianShenUserUtil.saveUserIDNum(mContext, id_num);
//                    ToastUtil.showToast(mContext, "上传身份证正面信息成功!");
                    initSaveIDCardBack();
                }

            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

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

    @Subscribe
    public void onFaceScanSuccessEvent(FaceScanSuccessEvent event) {
        finish();
    }


}
