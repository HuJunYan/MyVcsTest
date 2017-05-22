package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.SaveIdCardBean;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.SaveIdCardInformation;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.PermissionUtils;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class ActivateTheQuotaActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {
    private boolean isNeedScanFace = true;
    private boolean isIdCardInfoSaved = false;

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
        Utils.delJPGFile(this);
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
        if (18 != et_id_card_num.getEditTextString().trim().length()) {
            ToastUtil.showToast(mContext, "身份证号格式不正确");
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
            json.put("customer_id", TianShenUserUtil.getUserId(mContext));
        } catch (JSONException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
        SaveIdCardInformation mSaveIdCardInformationAction = new SaveIdCardInformation(mContext);
        mSaveIdCardInformationAction.saveIdCardInformation(json, new BaseNetCallBack<SaveIdCardBean>() {
            @Override
            public void onSuccess(SaveIdCardBean paramT) {
                if (null == paramT) {
                    ToastUtil.showToast(mContext, "数据失败");
                    return;
                }
                if (null == paramT.getData()) {
                    ToastUtil.showToast(mContext, "数据失败");
                    return;
                }
                if (GlobalParams.NOT_QUALIFIED.equals(paramT.getData().getQualified())) {
                    //用户在黑名单时直接跳转至拒绝
                    String reason = paramT.getData().getReason();
                    if (null == reason) {
                        reason = "";
                    }
                    UserUtil.setCashCreditReason(mContext, reason);
                    UserUtil.setCashCreditStatus(mContext, GlobalParams.CASH_APPLY_REFUSE_BY_MACHINE);
                    gotoActivity(mContext, VerifyFailActivity.class, null);
                    backActivity();
                    return;
                }
                UserUtil.setRealName(ActivateTheQuotaActivity.this, mName);
                UserUtil.setIdNum(ActivateTheQuotaActivity.this, mIdNum);
                UserUtil.setCreditStep(mContext, GlobalParams.HAVE_UPLOAD_IDCARD_INFO + "");

                //跳到运营商认证
                gotoActivity(mContext, ImproveQuotaActivity.class, mBundle);
                backActivity();

            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
            }
        });
    }


}
