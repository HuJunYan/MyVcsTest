package com.maibei.merchants.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.GetVerifyCode;
import com.maibei.merchants.net.api.SignUp;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RegexUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.LoginEditText;
import com.maibei.merchants.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/8/1.
 */
public class RegistUserActivity extends BaseActivity implements View.OnClickListener,MyEditText.MyEditTextListener {

    private ImageButton ib_return_login;
    private MyEditText et_shop_name;
    private MyEditText et_owner_mobile;
    private MyEditText et_phone_num;
    private LoginEditText et_passwod;

    private Button bt_next_reg;

    private String shopName = "";
    private String phoneNum = "";
    private String message = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_regist_user;
    }

    @Override
    protected void findViews() {
        ib_return_login = (ImageButton)findViewById(R.id.ib_return_login);
        et_shop_name = (MyEditText)findViewById(R.id.et_shop_name);
        et_owner_mobile = (MyEditText)findViewById(R.id.et_owner_mobile);
        et_phone_num = (MyEditText)findViewById(R.id.et_phone_num);
        et_passwod = (LoginEditText)findViewById(R.id.et_passwod);
        bt_next_reg = (Button)findViewById(R.id.bt_next_reg);
    }

    @Override
    protected void setListensers() {
        ib_return_login.setOnClickListener(this);
        bt_next_reg.setOnClickListener(this);
        et_phone_num.setListener(this);

        et_shop_name.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                shopName = s;
                if (shopName != "" && phoneNum.length() == 11 && message.length() == 6 && password != "") {
                    bt_next_reg.setEnabled(true);
                }
            }
        });
        et_owner_mobile.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                phoneNum = s;
                if(shopName != "" && phoneNum.length() == 11 && message.length() == 6 && password != ""){
                    bt_next_reg.setEnabled(true);
                }
            }
        });
        et_phone_num.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                message = s;
                if(shopName != "" && phoneNum.length() == 11 && message.length() == 6 && password != ""){
                    bt_next_reg.setEnabled(true);
                }
            }
        });
        et_passwod.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                password = s;
                if(shopName != "" && phoneNum.length() == 11 && message.length() == 6 && password != ""){
                    bt_next_reg.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return_login:
                backActivity();
                break;
            case R.id.bt_next_reg:
                signUp();
//                gotoActivity(RegistUserActivity.this, NotCertifiedActivity.class, null);
                break;
            default:
                break;
        }
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

    private boolean getVerityCode() {
        if (et_owner_mobile.getEditTextString().length() != 11 || !et_owner_mobile.getEditTextString().startsWith("1")) {
            ToastUtil.showToast(this, "请输入正确的手机号");
            return false;
        }
        GetVerifyCode getVerifyCode = new GetVerifyCode(this);
        JSONObject json = new JSONObject();
        try{
            json.put("mobile", et_owner_mobile.getEditTextString());
            json.put("type", "101");
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

    private void signUp(){
        shopName = et_shop_name.getEditTextString().trim();
        phoneNum = et_owner_mobile.getEditTextString().trim();
        message = et_phone_num.getEditTextString().trim();
        password = et_passwod.getEditTextString().trim();
        if(TextUtils.isEmpty(shopName)){
            ToastUtil.showToast(mContext, "请输入店名");
            return;
        }
        if(TextUtils.isEmpty(phoneNum)){
            ToastUtil.showToast(mContext, "请输入手机号");
            return;
        }
        if(!RegexUtil.IsTelephone(phoneNum)){
            ToastUtil.showToast(mContext, "请输入11位有效手机号");
            return;
        }
        if(TextUtils.isEmpty(message)){
            ToastUtil.showToast(mContext, "请输短信验证码");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtil.showToast(mContext, "请输入密码");
            return;
        }

        SignUp signUp = new SignUp(RegistUserActivity.this);
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("merchant_name", shopName);
            mJSONObject.put("mobile", phoneNum);
            mJSONObject.put("verify_code", message);
            mJSONObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        signUp.signUp(mJSONObject, bt_next_reg, true, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                backActivity();
                ToastUtil.showToast(mContext, "注册成功，请登录");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                backActivity();
            }
        });
    }

}
