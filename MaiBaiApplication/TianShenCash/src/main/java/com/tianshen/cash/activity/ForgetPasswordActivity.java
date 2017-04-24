package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.VerifyCodeBean;
import com.tianshen.cash.net.api.GetVerifyCode;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RegexUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.ChangeInterface;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {
    private String mobile = "", verityCode = "";
    private MyEditText et_mobile, et_card;
    private Button bt_next;
    private GetVerifyCode mGetVerifyCodeAction;
    private Bundle bundle;
    private final int SETTING_PASSWORD_SUCCESS = 3, REQUEST_SETING_PASSWORD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetVerifyCodeAction = new GetVerifyCode(this);
        bundle = getIntent().getExtras();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void findViews() {
        et_mobile = (MyEditText) findViewById(R.id.et_mobile);
        et_card = (MyEditText) findViewById(R.id.et_card);
        bt_next = (Button) findViewById(R.id.bt_next);
    }

    @Override
    protected void setListensers() {
        bt_next.setOnClickListener(this);
        bt_next.setClickable(false);
        et_card.setListener(this);
        et_card.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void chageAfter(Editable s) {
                verityCode = s.toString();
                if (verityCode.length() == 6 && mobile.length() == 11) {
                    if (RegexUtil.IsTelephone(mobile)) {
                        bt_next.setClickable(true);
                        bt_next.setBackgroundResource(R.drawable.select_bt);
                    }
                } else {
                    bt_next.setClickable(false);
                    bt_next.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
        et_mobile.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                mobile = s.toString();
                if (mobile.length() == 11) {
                    if (RegexUtil.IsTelephone(mobile)) {
                        if (verityCode.length() == 6) {
                            bt_next.setClickable(true);
                            bt_next.setBackgroundResource(R.drawable.select_bt);
                        }
                    } else {
                        ToastUtil.showToast(mContext, "不是合法的手机号");
                    }
                } else {
                    bt_next.setClickable(false);
                    bt_next.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (mobile.equals("")) {
                    ToastUtil.showToast(mContext, "请输入手机号", Toast.LENGTH_SHORT);
                    return;
                }
                if (!et_mobile.getEditTextString().startsWith("1")) {
                    ToastUtil.showToast(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT);
                    return;
                }
                if (verityCode.equals("")) {
                    ToastUtil.showToast(mContext, "请输入验证码", Toast.LENGTH_SHORT);
                    return;
                }
                bundle.putString("mobile", mobile);
                bundle.putString("verify_code", verityCode);
                Intent intent = new Intent(mContext, ResetPwdActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_SETING_PASSWORD);
                ((ForgetPasswordActivity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
        }
    }


    @Override
    public boolean onRightClick(View view) {
        switch (view.getId()) {
            case R.id.et_card:
                Log.d("ret", "onRightClick:11 ");
                boolean login = TianShenUserUtil.isLogin(mContext);
                if (login) {
                    String userPhoneNum = TianShenUserUtil.getUserPhoneNum(mContext);
                    if (!et_mobile.getEditTextString().trim().equals(userPhoneNum)) {
                        ToastUtil.showToast(mContext, "请输入本人手机号");
                        return false;
                    }
                }
                return getVerityCode();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == REQUEST_SETING_PASSWORD) {
            if (arg1 == SETTING_PASSWORD_SUCCESS) {
                backActivity();
            }
        }
    }

    private boolean getVerityCode() {
        if (et_mobile.getEditTextString().length() != 11 || !RegexUtil.IsTelephone(mobile)) {
            ToastUtil.showToast(this, "请输入正确的手机号");
            return false;
        }
        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("mobile", et_mobile.getEditTextString());
            if (GlobalParams.CHANGE_PAY_PASSWORD.equals(bundle.getString("type"))) {
                json.put("type", "3");
            } else {
                json.put("type", "5");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        mGetVerifyCodeAction.getVerifyCode(json, new BaseNetCallBack<VerifyCodeBean>() {
            @Override
            public void onSuccess(VerifyCodeBean verifyCodeBean) {
                ToastUtil.showToast(mContext, "验证码发送成功");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                et_card.finishTimer();
            }
        });
        return true;
    }

}
