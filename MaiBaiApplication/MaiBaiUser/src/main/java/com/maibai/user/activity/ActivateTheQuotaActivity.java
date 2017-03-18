package com.maibai.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.idcardlibrary.util.Util;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.SaveIdCardInformation;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.PermissionUtils;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.Utils;
import com.maibai.user.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class ActivateTheQuotaActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {
    private boolean isNeedScanFace = true;
    private boolean isIdCardInfoSaved = false;
    private long mPrice = 0l;
    private long mRealPrice = 0l;
    private double mLivenessResult;
    private String uuid;
    private String mName;
    private String mIdNum;
    private Button bt_confirm;
    private MyEditText et_name;
    private MyEditText et_id_card_num;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.getBoolean(GlobalParams.IF_NEED_SCAN_FACE_KEY)) {
                isNeedScanFace = true;
            } else {
                isNeedScanFace = false;
            }
        }
        initPayPrice();
        uuid = Util.getUUIDString(this);
        Utils.delJPGFile(this);
    }

    private void initPayPrice() {
        if (mBundle != null && !"".equals(mBundle.getString("price"))) {
            String priceStr = mBundle.getString("price");
            if (priceStr != null && !"".equals(priceStr)) {
                mPrice = Long.valueOf(mBundle.getString("price"), 10);
            }
            String reaPpriceStr = mBundle.getString("real_price");
            if (reaPpriceStr != null && !"".equals(reaPpriceStr)) {
                mRealPrice = Long.valueOf(mBundle.getString("real_price"), 10);
            }
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_activate_quota;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        et_name = (MyEditText) findViewById(R.id.et_name);
        et_id_card_num = (MyEditText) findViewById(R.id.et_id_card_num);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public boolean onRightClick(View view) {
        return false;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.bt_confirm:
                    handleConfirmClick();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void handleConfirmClick() {
        if (!isInputDataCompleted()) {
            return;
        }

        if (isIdCardInfoSaved) {
            if (isNeedScanFace) {
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
            }
        } else {
            saveIdCardInformation();
        }
    }

    private boolean isInputDataCompleted() {
        if (et_name.getVisibility() == View.VISIBLE && (et_name.getEditTextString() == null || "".equals(et_name.getEditTextString()))) {
            ToastUtil.showToast(mContext, "请输入姓名");
            return false;
        }

        if (et_id_card_num.getVisibility() == View.VISIBLE && (et_id_card_num.getEditTextString() == null || "".equals(et_id_card_num.getEditTextString()))) {
            ToastUtil.showToast(mContext, "请输入身份证号");
            return false;
        }
        if(18!=et_id_card_num.getEditTextString().trim().length()){
            ToastUtil.showToast(mContext,"身份证号格式不正确");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalParams.PAGE_INTO_LIVENESS:
                if (resultCode == RESULT_OK) {
                    gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, mBundle);
                    backActivity();
                } else {
                    returnFromScanFace(resultCode, data);
                }
                break;
            case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
//                if (resultCode == RESULT_OK) {
//                    gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, mBundle);
//                }
                backActivity();
                break;
            default:
                break;

        }
    }

    private void returnFromScanFace(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case 5:
                new PermissionUtils(this).showPermissionDialog(3);//摄像头权限
                break;
            case 3:
//                mLivenessResult = data.getExtras().getDouble("result", 0);
//                NumberFormat nFormat = NumberFormat.getNumberInstance();
//                nFormat.setMaximumFractionDigits(2);//设置小数点后面位数为
//                Log.d("ret", "人脸比对成功，相似度" + nFormat.format(mLivenessResult) + "%");
//                ToastUtil.showToast(mContext, "人脸比对成功，相似度" + nFormat.format(mLivenessResult) + "%");
//                gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, mBundle);
//                backActivity();
                break;
        }
    }

    private void saveIdCardInformation() {
        JSONObject json = new JSONObject();
        try {
            mName = et_name.getEditTextString().trim();
            mIdNum = et_id_card_num.getEditTextString().trim();
            json.put("real_name", mName);
            json.put("id_num", mIdNum);
            json.put("type", "2");
            json.put("customer_id", UserUtil.getId(mContext));
        } catch (JSONException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
        SaveIdCardInformation mSaveIdCardInformationAction = new SaveIdCardInformation(mContext);
        mSaveIdCardInformationAction.saveIdCardInformation(json, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                UserUtil.setRealName(ActivateTheQuotaActivity.this, mName);
                UserUtil.setIdNum(ActivateTheQuotaActivity.this, mIdNum);
                UserUtil.setCreditStep(mContext, GlobalParams.HAVE_UPLOAD_IDCARD_INFO+"");
//                if (isNeedScanFace) {
                    Intent intent = new Intent(mContext, ResultActivity.class);
                    intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                    startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
//                } else {
//                    gotoActivity(ActivateTheQuotaActivity.this, ContactsInfoActivity.class, mBundle);
//                    backActivity();
//                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
            }
        });
    }

    private void gotoTargetActivity0(Bundle bundle) {
        int lowLeverConsumeType = Utils.getConsumeLever(mContext, mRealPrice, 0l);
        switch (lowLeverConsumeType) {
            case GlobalParams.CONSUME_TYPE_0:
            case GlobalParams.CONSUME_TYPE_LESS_200:
            case GlobalParams.CONSUME_TYPE_LESS_500:
                if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
                    gotoActivity(ActivateTheQuotaActivity.this, InputPayPwdActivity.class, bundle);
                } else {
                    gotoActivity(ActivateTheQuotaActivity.this, SetPayPwdActivity.class, bundle);
                }
                break;
            case GlobalParams.CONSUME_TYPE_500_PLUS:
                gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, bundle);
                break;
            default:
//                // 手机号，验证码，扫身份证，扫脸
                gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, bundle);
                break;
        }
    }

    private void gotoTargetActivity200(Bundle bundle) {
        if (mPrice == 0) {
            gotoActivity(ActivateTheQuotaActivity.this, InputPayPwdActivity.class, bundle);
            return;
        }
        int lowLeverConsumeType = Utils.getConsumeLever(mContext, mRealPrice, 0l);
        switch (lowLeverConsumeType) {
            case GlobalParams.CONSUME_TYPE_0:
            case GlobalParams.CONSUME_TYPE_LESS_500:
                gotoActivity(ActivateTheQuotaActivity.this, InputPayPwdActivity.class, bundle);
                break;
            case GlobalParams.CONSUME_TYPE_500_PLUS:
                gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, bundle);
                break;
            default:
//                // 手机号，验证码，扫身份证，扫脸
                gotoActivity(ActivateTheQuotaActivity.this, ImproveQuotaActivity.class, bundle);
                break;
        }
    }
}
