package com.maibei.merchants.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.UserLoginBean;
import com.maibei.merchants.net.api.UserLogin;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RegexUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by huxiao on 2016/6/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, BaseNetCallBack<UserLoginBean>{

    private EditText et_phone;
    private EditText et_pwd;

    private Button bt_login;
    private TextView tv_fast_reg;
    private TextView tv_forget_password;
    private boolean possword_show_hide = false;

    private String phoneNumber = "";
    private String password = "";

    private long exitTime = 0;
    private LocationClient mLocationClient;
    private String push_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }


    private void init(){
        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    protected void findViews() {
        bt_login = (Button) findViewById(R.id.bt_login);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_fast_reg = (TextView) findViewById(R.id.tv_fast_reg);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        editTextRightEvent();
    }

    @Override
    protected void setListensers() {
        bt_login.setOnClickListener(this);
        tv_fast_reg.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = s.toString();
                if (phoneNumber != "" && password != "") {
                    bt_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                if (phoneNumber != "" && password != "") {
                    bt_login.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_fast_reg://注册
                gotoActivity(LoginActivity.this, RegistUserActivity.class, null);
                break;
            case R.id.tv_forget_password://忘记密码
                gotoActivity(LoginActivity.this, InfoVerificatActivity.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(UserLoginBean userLoginBean) {
        try {
            if (userLoginBean.getCode() == 0) {
                SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.JPUSH_ID_KEY,push_id);
                UserUtil.setName(mContext, userLoginBean.getData().getName());
                SharedPreferencesUtil.getInstance(mContext).putBoolean("isLogin", true);
                SharedPreferencesUtil.getInstance(mContext).putString("phoneNumber", phoneNumber);
                SharedPreferencesUtil.getInstance(mContext).putString("password", password);
                String loginStatusStr = userLoginBean.getData().getStatus();
                int loginStatusInt = Integer.parseInt(loginStatusStr, 10);
                switch (loginStatusInt) {
                    case 0:
                        gotoActivity(LoginActivity.this, ToExamineActivity.class, null);
                        backActivity();
                        break;
                    case 1:
                        gotoActivity(LoginActivity.this, MainActivity.class, null);//审核通过
                        backActivity();
                        break;
                    case 2:
                        ToastUtil.showToast(mContext, "该用户不存在");
                        break;
                    case 3:
                        gotoActivity(LoginActivity.this, NotCertifiedActivity.class, null);
                        backActivity();
                        break;
//                    case 4:
//                        gotoActivity(LoginActivity.this, AuthBankCardActivity.class, null);
//                        backActivity();
//                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onFailure(String url, int errorType, int errorCode) {
        if (errorCode == 205) {
            ToastUtil.showToast(mContext, "用户名或密码不对");
            return;
        }
    }

    private void editTextRightEvent(){
        et_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Drawable drawable = et_pwd.getCompoundDrawables()[2];
                if (drawable == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_pwd.getWidth() - et_pwd.getPaddingRight() - drawable.getIntrinsicWidth()){
                    if(!possword_show_hide){
                        possword_show_hide = true;
                        //显示密码
                        et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else {
                        possword_show_hide = false;
                        //隐藏密码
                        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
                return false;
            }
        });
    }

    private void login() {
        phoneNumber= et_phone.getText().toString().trim();
        password = et_pwd.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            ToastUtil.showToast(mContext, "请输入手机号号码");
            return;
        }
        if (!RegexUtil.IsTelephone(phoneNumber)) {
            ToastUtil.showToast(mContext, "请正确输入手机号码");
            return;
        }

        if (TextUtils.isEmpty(password)){
            ToastUtil.showToast(mContext, "密码不能为空");
            return;
        }
        try {
            UserLogin mUserLogin = new UserLogin(LoginActivity.this);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("mobile", phoneNumber);
            mJSONObject.put("password", password);
           push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            mJSONObject.put("push_id", push_id);
            mUserLogin.userLogin(mJSONObject, bt_login, true, this);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(this, R.string.click_one_exit);
            exitTime = System.currentTimeMillis();
        } else {
            backActivity();
        }
    }
}
