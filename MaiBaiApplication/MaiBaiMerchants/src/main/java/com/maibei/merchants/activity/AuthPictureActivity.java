package com.maibei.merchants.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.cropimage.CropImage;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.model.UploadImageBean;
import com.maibei.merchants.model.UserLoginBean;
import com.maibei.merchants.net.api.UpdToApprove;
import com.maibei.merchants.net.api.UploadImage;
import com.maibei.merchants.net.api.UserLogin;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.BitmapUtils;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RequestPermissionUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.maibei.merchants.utils.ShellUtils;
import com.maibei.merchants.utils.SignUtils;
import com.maibei.merchants.utils.TakePictureUtils;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 14658 on 2016/7/26.
 */
public class AuthPictureActivity extends BaseActivity implements View.OnClickListener {

    private int mCurrentPictureIndex = 10000;
    private int[] mUploadStatusArray = {0, 0, 0,0,0,0,0,0};

    private Button bt_next_reg;
    private ImageTextView itv_business_licence;//营业执照
    private ImageTextView itv_owner_id_card;//业主身份证
    private ImageTextView itv_shop_id_prcture;//门店图片
    private ImageTextView itv_indoor_prcture1;//店内环境一
    private ImageTextView itv_indoor_prcture2;//店内环境二
    private ImageTextView itv_cashier_prcture;//收银台照片
    private ImageTextView itv_counter_prcture;//商品柜台照骗
    private ImageTextView itv_periphery_prcture;//没点周边
    private ImageButton ib_return_shop;
    private String mTmpImagePath;
    private List<ImageTextView> mImageTextViewList = new ArrayList<ImageTextView>();
    private String mImageFileNameList[] = {"business_licence", "owner_id_card", "shop_id_prcture","indoor_picture1","indoor_picture2","cashier_picture","counter_picture","periphery"};

    private String cropedPhotoPath="";

    private final int GOTO_CROP_IMAGE_REQEST=5;
    private void initData() {
        mImageTextViewList.add(itv_business_licence);
        mImageTextViewList.add(itv_owner_id_card);
        mImageTextViewList.add(itv_shop_id_prcture);
        mImageTextViewList.add(itv_indoor_prcture1);
        mImageTextViewList.add(itv_indoor_prcture2);
        mImageTextViewList.add(itv_cashier_prcture);
        mImageTextViewList.add(itv_counter_prcture);
        mImageTextViewList.add(itv_periphery_prcture);
        mTmpImagePath = getDirPath("MaiBeiMerchants");
        ShellUtils.ChangeReadWritePermission(mTmpImagePath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RequestPermissionUtil(mContext).requestCameraPermission();
        initData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_picture;
    }

    @Override
    protected void findViews() {
        bt_next_reg = (Button) findViewById(R.id.bt_next_reg);
        itv_business_licence = (ImageTextView) findViewById(R.id.itv_business_licence);
        itv_owner_id_card = (ImageTextView) findViewById(R.id.itv_owner_id_card);
        itv_shop_id_prcture = (ImageTextView) findViewById(R.id.itv_shop_id_prcture);
        ib_return_shop = (ImageButton) findViewById(R.id.ib_return_shop);

        itv_indoor_prcture1 = (ImageTextView) findViewById(R.id.itv_indoor_prcture1);
        itv_indoor_prcture2 = (ImageTextView) findViewById(R.id.itv_indoor_prcture2);
        itv_cashier_prcture = (ImageTextView) findViewById(R.id.itv_cashier_prcture);
        itv_counter_prcture = (ImageTextView) findViewById(R.id.itv_counter_prcture);
        itv_periphery_prcture = (ImageTextView) findViewById(R.id.itv_periphery_prcture);
    }

    @Override
    protected void setListensers() {
        bt_next_reg.setOnClickListener(this);
        itv_business_licence.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.BUSINESS_LICENCE);
            }
        });
        itv_business_licence.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_owner_id_card.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.OWNER_ID_CARD);
            }
        });
        itv_owner_id_card.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_shop_id_prcture.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.SHOP_ID_PRCTURE);
            }
        });
        itv_shop_id_prcture.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });

        itv_indoor_prcture1.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.INDOOR_PICTURE1);
            }
        });
        itv_indoor_prcture1.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_indoor_prcture2.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.INDOOR_PICTURE2);
            }
        });
        itv_indoor_prcture2.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_cashier_prcture.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.CASHIER_PICTURE);
            }
        });
        itv_cashier_prcture.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_counter_prcture.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.COUNTER_PICTURE);
            }
        });
        itv_counter_prcture.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_periphery_prcture.setListener(new ImageTextView.ImageTextViewListener() {//点击
            @Override
            public void onRightClick(View view) {
                handleClick(GlobalParams.PERIPHERY_PICTURE);
            }
        });
        itv_periphery_prcture.setChangeListener(new ChangeInterface() {//文字监听
            @Override
            public void change(String s) {
                boolean result=true;
                for(int i=0;i<mUploadStatusArray.length;i++){
                    if(mUploadStatusArray[i]==0){
                        result=false;
                    }
                }
                if (result) {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });

        ib_return_shop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_next_reg:
                updToApprove();
                break;
            case R.id.ib_return_shop:
                backActivity();
                break;
            default:
                break;
        }
    }

    private void updToApprove() {
        UpdToApprove updToApprove = new UpdToApprove(mContext);
        JSONObject json = new JSONObject();
        try {
            json.put("merchant_id", UserUtil.getMerchantId(mContext));
            json.put("status", "3");
            updToApprove.updToApprove(json, bt_next_reg, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    rushReview();

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
    private void rushReview(){
        JSONObject mJson = new JSONObject();
        SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
        try {
            mJson.put("mobile", share.getString("phoneNumber"));
            mJson.put("password", share.getString("password"));
            String push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            mJson.put("push_id", push_id);
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        UserLogin mUserLogin = new UserLogin(this);
        mUserLogin.userLogin(mJson, new BaseNetCallBack<UserLoginBean>() {
            @Override
            public void onSuccess(UserLoginBean userLoginBean) {
                Bundle bundle=new Bundle();
                bundle.putBoolean("isInit",true);
                gotoActivity(AuthPictureActivity.this, AuthBankCardActivity.class, bundle);
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }
    public boolean isPermissionAvilable(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission= ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.CAMERA);
            int storagePermission=ContextCompat.checkSelfPermission((Activity) mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (cameraPermission!= PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showToast(mContext,"摄像头权限被禁止，请先打开摄像头权限");
                return false;
            }
            if(storagePermission!=PackageManager.PERMISSION_GRANTED){
                ToastUtil.showToast(mContext,"读取内存权限被禁止，请先打开读取内存卡权限");
                return false;
            }
            return true;
        }else{
            return true;
        }
    }
    private void handleClick(int viewTypeIndex) {

        if(!isPermissionAvilable()){
            return;
        }
        int baseIndex = viewTypeIndex - GlobalParams.BUSINESS_LICENCE;
        mCurrentPictureIndex = baseIndex;
        showGetPictureDialog(baseIndex);
    }
    public String getDirPath(String filePath){
        String path= Environment.getExternalStorageDirectory().getPath() + "/"+filePath+"/";
        File file=new File(path);
        if(!file.exists()){
            boolean result=file.mkdir();
            Log.e("mkdirs",result+"");
            if(!result){
                return Environment.getExternalStorageDirectory().getPath()+"/";
            }
        }
        return path;
    }
    private void showGetPictureDialog(final int index) {

        mCurrentPictureIndex = index;
        cropedPhotoPath=mTmpImagePath+mImageFileNameList[index]+".PNG";
        final AlertDialog mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_get_picture,
                new RelativeLayout(mContext), false);
        window.setContentView(contentView);

        LinearLayout ll_take_a_picture = (LinearLayout) contentView.findViewById(R.id.ll_take_a_picture);
        LinearLayout ll_get_picture_from_photo = (LinearLayout) contentView.findViewById(R.id.ll_get_picture_from_photo);

        ll_take_a_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(index+GlobalParams.BUSINESS_LICENCE==GlobalParams.SHOP_ID_PRCTURE) {
                    new TakePictureUtils((Activity) mContext).getCamera(GlobalParams.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_NEED_CROP, cropedPhotoPath);
                }else{
                    new TakePictureUtils((Activity) mContext).getCamera(GlobalParams.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_WITHHOUT_CROP, cropedPhotoPath);
                }
                mAlertDialog.dismiss();
            }
        });
        ll_get_picture_from_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(index+GlobalParams.BUSINESS_LICENCE==GlobalParams.SHOP_ID_PRCTURE) {
                    BitmapUtils.getoSystemPhoto(mContext, GlobalParams.GET_PICTURE_FROM_XIANGCE_CODE_NEED_CROP);
                }else{
                    BitmapUtils.getoSystemPhoto(mContext, GlobalParams.GET_PICTURE_FROM_XIANGCE_CODE_WITHOUT_CROP);
                }
                mAlertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("ret", "onActivityResult: requestCode = " + requestCode);
        try {
            switch (requestCode) {
                case GlobalParams.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_NEED_CROP: // 系统相机
                    if (resultCode == Activity.RESULT_OK) {
                        gotoCropImage(cropedPhotoPath);
                    }
                    break;
                case GlobalParams.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_WITHHOUT_CROP:
                    if(resultCode==RESULT_OK){
                        uploadPhoto(cropedPhotoPath);
                    }
                    break;
                case GlobalParams.GET_PICTURE_FROM_XIANGCE_CODE_NEED_CROP: // 系统相册回调
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = intent.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        cropedPhotoPath = c.getString(columnIndex);
                        gotoCropImage(cropedPhotoPath);
                    }
                    break;
                case GlobalParams.GET_PICTURE_FROM_XIANGCE_CODE_WITHOUT_CROP:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = intent.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        cropedPhotoPath = c.getString(columnIndex);
                        uploadPhoto(cropedPhotoPath);
                    }
                    break;
                case GOTO_CROP_IMAGE_REQEST://图片裁剪回调
                    if(resultCode==RESULT_OK){
                        uploadPhoto(cropedPhotoPath);
                    }
            }
        }catch (Exception e){
            LogUtil.d("ret", "onActivityResult ================================== 9");
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }


    private void gotoCropImage(String imagePath){
        Bundle bundle=new Bundle();
        bundle.putString("image-path",imagePath);
        bundle.putInt("aspectX",2160);
        bundle.putInt("aspectY",1720);
        bundle.putInt("outputX",2160);
        bundle.putInt("outputY",1720);
        bundle.putBoolean("scale",true);
        bundle.putBoolean("scaleUpIfNeeded",true);
        Intent intent=new Intent(mContext, CropImage.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,GOTO_CROP_IMAGE_REQEST);
    }
    private void uploadPhoto(String imagePath){
        if (mCurrentPictureIndex != 10000) {
            UploadImage uploadImage = new UploadImage(mContext);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
                int type = mCurrentPictureIndex + 100;
                jsonObject.put("type", (type>102?type+4:type)+ "");
                JSONObject newJson = SignUtils.signJSONbject(jsonObject);
                uploadImage.uploadImage(newJson, imagePath,true, type, new BaseNetCallBack<UploadImageBean>() {
                    @Override
                    public void onSuccess(UploadImageBean paramT) {
                        mImageTextViewList.get(mCurrentPictureIndex).setRightImageViewIcon(R.mipmap.lift_ok);
                        mUploadStatusArray[mCurrentPictureIndex] = 1;
                        mImageTextViewList.get(mCurrentPictureIndex).setContentText("点击可重新提交");
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
