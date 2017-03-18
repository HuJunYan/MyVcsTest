package com.maibei.merchants.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.GetVerifyCode;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RegexUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/7/27.
 */
public class InfoVerificatActivity extends BaseActivity implements View.OnClickListener,MyEditText.MyEditTextListener {

    private Button bt_next_back_pwd;
    private ImageButton ib_return_login;

    private MyEditText et_owner_mobile_pwd;
    private MyEditText et_phone_num;

    private String ownerMobilePwd = "";
    private String phoneNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_info_verification;
    }

    @Override
    protected void findViews() {
        bt_next_back_pwd = (Button) findViewById(R.id.bt_next_back_pwd);
        ib_return_login = (ImageButton) findViewById(R.id.ib_return_login);
        et_owner_mobile_pwd = (MyEditText) findViewById(R.id.et_owner_mobile_pwd);
        et_phone_num = (MyEditText) findViewById(R.id.et_phone_num);
    }

    @Override
    protected void setListensers() {
        bt_next_back_pwd.setOnClickListener(this);
        ib_return_login.setOnClickListener(this);
        et_phone_num.setListener(this);
        et_owner_mobile_pwd.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                ownerMobilePwd = s;
                if (ownerMobilePwd != "" && phoneNum != "") {
                    bt_next_back_pwd.setEnabled(true);
                    bt_next_back_pwd.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        et_phone_num.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                phoneNum = s;
                if(ownerMobilePwd != "" && phoneNum != ""){
                    bt_next_back_pwd.setEnabled(true);
                    bt_next_back_pwd.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next_back_pwd:
                nextSetPassword();
                break;
            case R.id.ib_return_login:
                backActivity();
                break;
            default:
                break;
        }
    }

    private void nextSetPassword(){
        ownerMobilePwd = et_owner_mobile_pwd.getEditTextString();
        phoneNum = et_phone_num.getEditTextString();
        if(TextUtils.isEmpty(ownerMobilePwd)){
            ToastUtil.showToast(mContext, "请输入手机号码");
            return;
        }
        if(!RegexUtil.IsTelephone(ownerMobilePwd)){
            ToastUtil.showToast(mContext, "请输入11位有效手机号");
            return;
        }
        if(TextUtils.isEmpty(phoneNum) || phoneNum.length() != 6){
            ToastUtil.showToast(mContext, "请输入6位验证码");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("phoneNo", et_owner_mobile_pwd.getEditTextString());
        bundle.putString("verityCode", et_phone_num.getEditTextString());
        gotoActivity(InfoVerificatActivity.this, SettingPasswordActivity.class, bundle);
        finish();
    }

    @Override
    public boolean onRightClick(View view) {
        switch (view.getId()) {
            case R.id.et_phone_num:
                return getVerityCode();
            default:
                break;
        }
        return false;
    }

    public boolean getVerityCode() {
        if (et_owner_mobile_pwd.getEditTextString().length() != 11 || !RegexUtil.IsTelephone(et_owner_mobile_pwd.getEditTextString())) {
            ToastUtil.showToast(this, "请输入正确的手机号");
            return false;
        }
        GetVerifyCode getVerifyCode = new GetVerifyCode(this);
        JSONObject json = new JSONObject();
        try{
            json.put("mobile", et_owner_mobile_pwd.getEditTextString());
            json.put("type", "100");
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        getVerifyCode.getVerifyCode(json, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                ToastUtil.showToast(mContext, "验证码发送成功");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                et_phone_num.finishTimer();
            }
        });
        return true;
    }
}
