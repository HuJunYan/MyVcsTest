package com.tianshen.cash.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.WithdrawalsApplyBean;
import com.tianshen.cash.net.api.SetPayPassword;
import com.tianshen.cash.net.api.WithdrawalsApply;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SharedPreferencesUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class SetPayPwdActivity extends BaseActivity implements View.OnClickListener {
    private String password;
    private Button bt_confirm;
    private MyEditText et_set_pwd;
    private MyEditText et_repeat_pwd;
    private TextView tv_protocol;
    private SetPayPassword mSetPayPasswordAction;

    private Bundle mBundle;
    private boolean isClickAble = true;
    private CheckBox checkBox;
    private int PRO_SERVER = 2;//用户服务协议

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        mBundle.putString("blackBox", new GetTelephoneUtils(mContext).getBlackBox());
        mSetPayPasswordAction = new SetPayPassword(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_set_pwd;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        et_set_pwd = (MyEditText) findViewById(R.id.et_set_pwd);
        et_repeat_pwd = (MyEditText) findViewById(R.id.et_repeat_pwd);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        checkBox = (CheckBox) findViewById(R.id.check_agree);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bt_confirm.setClickable(true);
                    bt_confirm.setBackgroundResource(R.drawable.select_bt);
                } else {
                    bt_confirm.setClickable(false);
                    bt_confirm.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                if (isClickAble) {
                    isClickAble = false;
                    password = et_set_pwd.getEditTextString();
                    if (password.length() != 6) {
                        ToastUtil.showToast(this, "请输入6位数字密码");
                        isClickAble = true;
                        return;
                    }
                    if (!password.equals(et_repeat_pwd.getEditTextString())) {
                        ToastUtil.showToast(this, "两次输入的密码不一致");
                        isClickAble = true;
                        return;
                    }
                    mBundle.putString("password", password);
                    ViewUtil.createLoadingDialog(this, "", false);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("black_box", mBundle.getString("blackBox"));
                        json.put("customer_id", TianShenUserUtil.getUserId(mContext));
                        json.put("pay_pass", password);
                        final String push_id = JPushInterface.getRegistrationID(mContext);
                        if (push_id != null && !"".equals(push_id)) {
                            json.put("push_id", push_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MobclickAgent.reportError(mContext, LogUtil.getException(e));
                    }
                    mSetPayPasswordAction.setPayPassword(json, bt_confirm, true, new BaseNetCallBack<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean mResponseBean) {
                            if (mBundle != null) {
                                int applyType = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);


                                LogUtil.d("abc", "SetPaypwd---getInt--->" + applyType);

                                switch (applyType) {
                                    case GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY:
                                        withdrawalsApply();
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {
                            isClickAble = true;
                            ViewUtil.cancelLoadingDialog();
                        }
                    });
                }
                break;
            case R.id.tv_protocol:
                Bundle bundle = new Bundle();
                bundle.putInt("pro_type", PRO_SERVER);
                gotoActivity(mContext, ProtocolActivity.class, bundle);
                break;
        }
    }

    private void withdrawalsApply() {

        LogUtil.d("abc", "SetPayPwd----withdrawalsApply");

        SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
        WithdrawalsApply withdrawalsApply = new WithdrawalsApply(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", TianShenUserUtil.getUserId(mContext));
            jsonObject.put("province", share.getString("province"));
            jsonObject.put("city", share.getString("city"));
            jsonObject.put("country", share.getString("country"));
            jsonObject.put("location", share.getString("location"));
            jsonObject.put("pay_pass", password);
            jsonObject.put("black_box", new GetTelephoneUtils(mContext).getBlackBox());
            final String push_id = JPushInterface.getRegistrationID(mContext);
            if (push_id != null && !"".equals(push_id)) {
                jsonObject.put("push_id", push_id);
            }
            final CashSubItemBean cashSubItemBean = (CashSubItemBean) (getIntent().getExtras().getSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY));
            jsonObject.put("apply_amount", cashSubItemBean.getWithdrawal_amount());
            withdrawalsApply.withdrawalsApply(jsonObject, bt_confirm, true, new BaseNetCallBack<WithdrawalsApplyBean>() {
                @Override
                public void onSuccess(WithdrawalsApplyBean paramT) {

                    String isNeedVerify = paramT.getData().getIs_need_verify();


                    LogUtil.d("abc", "SetPayPwd----withdrawalsApply---onSuccess---isNeedVerify--->" + isNeedVerify);

                    switch (isNeedVerify) {
                        case "1":
                            Bundle bundle = new Bundle();
                            bundle.putString(GlobalParams.WITHDRAWALS_VERIFY_ID_KEY, paramT.getData().getConsume_id());
                            bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
                            gotoActivity(mContext, WaitVerifyWithdrawalActivity.class, bundle);//提现待审核
                            break;
                        case "0":
                            Intent intent = new Intent(GlobalParams.LOGIN_SUCCESS_ACTION);
                            sendBroadcast(intent);
                            String amountStr = paramT.getData().getAmount();
                            if (amountStr != null && !"".equals(amountStr)) {
                                int amountInt = (int) (Double.parseDouble(amountStr) / 100);
//                                ToastUtil.showToast(mContext, "恭喜，您成功申请可体现额度"+amountInt+"元！");
//                                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);

                                final Dialog dialog = new AlertDialog.Builder(mContext, R.style.withdrawals_diaog).create();
                                View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_withdrawals_success, null);
                                view.getBackground().setAlpha(100);//0~255透明度值
                                TextView tv_amount = (TextView) view.findViewById(R.id.tv_amount);
                                TextView tv_amount_small = (TextView) view.findViewById(R.id.tv_amount_small);
                                Button bt_get_money = (Button) view.findViewById(R.id.bt_get_money);
                                tv_amount.setText(amountInt + "");
                                tv_amount_small.setText(amountInt + "");
                                bt_get_money.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                                    }
                                });
                                dialog.setCancelable(false);
                                dialog.show();
                                dialog.setContentView(view);
                                new GetTelephoneUtils(mContext).changeDark();
                            }
                            break;
                    }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
