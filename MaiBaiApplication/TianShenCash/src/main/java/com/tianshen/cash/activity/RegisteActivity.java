package com.tianshen.cash.activity;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.model.SignUpBean;
import com.tianshen.cash.model.TianShenLoginBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.model.VerifyCodeBean;
import com.tianshen.cash.net.api.GetVerifyCode;
import com.tianshen.cash.net.api.SignIn;
import com.tianshen.cash.net.api.SignUp;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RegexUtil;
import com.tianshen.cash.utils.SharedPreferencesUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class RegisteActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {

    private static final int REGISTESUCCESS = 7;
    private MyEditText et_mobile, et_get_verification, et_password, et_confirm_password;
    private Button bt_regite;
    private TextView tv_protocol;
    private CheckBox check_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_registe;
    }

    @Override
    protected void findViews() {
        et_mobile = (MyEditText) findViewById(R.id.et_mobile);
        et_get_verification = (MyEditText) findViewById(R.id.et_get_verification);
        et_password = (MyEditText) findViewById(R.id.et_password);
        et_confirm_password = (MyEditText) findViewById(R.id.et_confirm_password);
        bt_regite = (Button) findViewById(R.id.bt_regite);
        check_agree = (CheckBox) findViewById(R.id.check_agree);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
    }

    @Override
    protected void setListensers() {
        et_get_verification.setListener(this);
        bt_regite.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        check_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.check_agree:
                        if (isChecked) {
                            bt_regite.setClickable(true);
                            bt_regite.setBackgroundResource(R.drawable.select_bt);
                        } else {
                            bt_regite.setClickable(false);
                            bt_regite.setBackgroundResource(R.drawable.button_gray);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public boolean onRightClick(View view) {
        switch (view.getId()) {
            case R.id.et_get_verification:
                return getVerityCode();
        }
        return false;
    }

    private boolean getVerityCode() {
        if ("".equals(et_mobile.getEditTextString().trim()) || null == et_mobile.getEditTextString().trim() || !RegexUtil.IsTelephone(et_mobile.getEditTextString().trim())) {
            ToastUtil.showToast(mContext, "请输入正确的手机号");
            return false;
        }
        try {
            JSONObject json = new JSONObject();
            json.put("mobile", et_mobile.getEditTextString().trim());
            json.put("type", 4 + "");
            GetVerifyCode mGetVerifyCodeAction = new GetVerifyCode(mContext);
            mGetVerifyCodeAction.getVerifyCode(json, new BaseNetCallBack<VerifyCodeBean>() {
                @Override
                public void onSuccess(VerifyCodeBean verifyCodeBean) {
                    ToastUtil.showToast(mContext, "验证码发送成功");
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    et_get_verification.finishTimer();
                }
            });
        } catch (JSONException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regite:
                if ("".equals(et_mobile.getEditTextString().trim()) || null == et_mobile.getEditTextString().trim() || !RegexUtil.IsTelephone(et_mobile.getEditTextString().trim())) {
                    ToastUtil.showToast(mContext, "请输入正确的手机号");
                    return;
                }
                if ("".equals(et_get_verification.getEditTextString().trim()) || null == et_get_verification.getEditTextString().trim()) {
                    ToastUtil.showToast(mContext, "请输入验证码");
                    return;
                }
                if ("".equals(et_password.getEditTextString().trim()) || null == et_password.getEditTextString().trim()) {
                    ToastUtil.showToast(mContext, "请输入密码");
                    return;
                }
                if (et_password.getEditTextString().trim().length() < 6 || et_confirm_password.getEditTextString().trim().length() < 6 || et_password.getEditTextString().trim().length() > 18 || et_confirm_password.getEditTextString().trim().length() > 18) {
                    ToastUtil.showToast(mContext, "请输入6~18位有效密码");
                    return;
                }
                if ("".equals(et_confirm_password.getEditTextString().trim()) || null == et_confirm_password.getEditTextString().trim()) {
                    ToastUtil.showToast(mContext, "请重复输入密码");
                    return;
                }

                if (!et_password.getEditTextString().trim().equals(et_confirm_password.getEditTextString().trim())) {
                    ToastUtil.showToast(mContext, "密码输入不一致");
                    et_password.setText("");
                    et_confirm_password.setText("");
                    return;
                }
                SignUp signUp = new SignUp(mContext);
                try {
                    JSONObject jsonObject = new JSONObject();
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    jsonObject.put("device_id", TelephonyMgr.getDeviceId());
                    jsonObject.put("mobile", et_mobile.getEditTextString().trim());
                    jsonObject.put("password", et_password.getEditTextString().trim());
                    jsonObject.put("verify_code", et_get_verification.getEditTextString().trim());
                    signUp.signUp(jsonObject, null, true, new BaseNetCallBack<SignUpBean>() {
                        @Override
                        public void onSuccess(SignUpBean paramT) {
                            ToastUtil.showToast(mContext, "注册成功");
                            backActivity();
//                            gotoActivity(mContext, LoginActivity.class, null);
//                            finish();
//                           login(et_mobile.getEditTextString().trim(),et_password.getEditTextString().trim());
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {

                        }
                    });
                } catch (Exception e) {
                    MobclickAgent.reportError(mContext, LogUtil.getException(e));
                }
                break;
            case R.id.img_back:
                backActivity();
                break;
            case R.id.tv_protocol:// 用户注册协议
                gotoWebActivity();
                break;
        }
    }

    private void gotoWebActivity() {
        String userServiceProtocolURL = NetConstantValue.getUserServiceProtocolURL();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, userServiceProtocolURL);
        gotoActivity(mContext, WebActivity.class, bundle);
    }

}
