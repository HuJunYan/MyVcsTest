package com.maibei.merchants.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.ResetPassword;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/7/27.
 */
public class SettingPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton ib_return_info;
    private Button bt_confirm_ok;

    private MyEditText et_ownwr_pwd;
    private MyEditText et_confirm_pwd;

    private String phoneNo;
    private String verityCode;
    private String ownerPwd = "";
    private String confirmPwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        phoneNo = bundle.getString("phoneNo");
        verityCode = bundle.getString("verityCode");
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_pwd;
    }

    @Override
    protected void findViews() {
        ib_return_info = (ImageButton) findViewById(R.id.ib_return_info);
        bt_confirm_ok = (Button) findViewById(R.id.bt_confirm_ok);
        et_ownwr_pwd = (MyEditText) findViewById(R.id.et_ownwr_pwd);
        et_confirm_pwd = (MyEditText) findViewById(R.id.et_confirm_pwd);
    }

    @Override
    protected void setListensers() {
        ib_return_info.setOnClickListener(this);
        bt_confirm_ok.setOnClickListener(this);
        et_ownwr_pwd.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                ownerPwd = s;
                if (ownerPwd != "" && confirmPwd != "") {
                    bt_confirm_ok.setEnabled(true);
                }
            }
        });
        et_confirm_pwd.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                confirmPwd = s;
                if (ownerPwd != "" && confirmPwd != "") {
                    bt_confirm_ok.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return_info:
                backActivity();
                break;
            case R.id.bt_confirm_ok:
                resetPassword();
                break;
            default:
                break;
        }
    }

    private void resetPassword() {
        ownerPwd = et_ownwr_pwd.getEditTextString();
        confirmPwd = et_confirm_pwd.getEditTextString();
        if (TextUtils.isEmpty(ownerPwd)){
            ToastUtil.showToast(mContext, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)){
            ToastUtil.showToast(mContext, "请确认密码");
            return;
        }
        if (!ownerPwd.equals(confirmPwd)){
            ToastUtil.showToast(mContext, "两次输入的密码不一样");
            return;
        }
        ResetPassword resetPassword = new ResetPassword(this);
        JSONObject mJsonObject = new JSONObject();
        try{
            mJsonObject.put("mobile", phoneNo);
            mJsonObject.put("password", ownerPwd);
            mJsonObject.put("verify_code", verityCode);
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        resetPassword.resetPassword(mJsonObject, bt_confirm_ok, true, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                ToastUtil.showToast(mContext, "密码修改成功");
                backActivity();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }
}
