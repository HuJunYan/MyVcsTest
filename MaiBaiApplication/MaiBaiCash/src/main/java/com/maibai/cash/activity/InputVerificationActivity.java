package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.BankListItemBean;
import com.maibai.cash.model.BindVerifySmsBean;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.net.api.BindBankCard;
import com.maibai.cash.net.api.GetBindVerifySms;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SendBroadCastUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class InputVerificationActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {
    private String bind_no = "";
    private TextView tv_mobile_num;
    private Bundle mBundle;
    private MyEditText et_verification_code;
    private Button bt_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        et_verification_code.startTimer();
        getBindVerifyCode();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_input_verification;
    }

    @Override
    protected void findViews() {
        et_verification_code = (MyEditText) findViewById(R.id.et_verification_code);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tv_mobile_num = (TextView) findViewById(R.id.tv_mobile_num);
    }

    @Override
    protected void setListensers() {
        et_verification_code.setListener(this);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                if ("".equals(et_verification_code.getEditTextString().trim())) {
                    ToastUtil.showToast(mContext, "请输入验证码");
                    break;
                }
                bindCard();
                break;
        }
    }

    @Override
    public boolean onRightClick(View view) {
        getBindVerifyCode();
        return true;
    }

    private void getBindVerifyCode() {
        try {
            JSONObject mJson = new JSONObject();
            BankListItemBean bankListItemBean=(BankListItemBean)mBundle.getSerializable("bankListItemBean");
            mJson.put("bank_name",bankListItemBean.getBank_name());
            mJson.put("bank_id",bankListItemBean.getBank_id());
            mJson.put("customer_id", UserUtil.getId(mContext));
            mJson.put("card_user_name", mBundle.getString("card_user_name"));
            mJson.put("card_num", mBundle.getString("card_num"));
            mJson.put("reserved_mobile", mBundle.getString("reserved_mobile"));
            GetBindVerifySms mGetBindVerifyCode = new GetBindVerifySms(mContext);
            mGetBindVerifyCode.getBindVerifySms(mJson, et_verification_code, true, new BaseNetCallBack<BindVerifySmsBean>() {
                @Override
                public void onSuccess(BindVerifySmsBean paramT) {
                    bind_no = paramT.getData().getBind_no();
                    ToastUtil.showToast(mContext, "验证码发送成功");
                    tv_mobile_num.setText("请输入手机" + mBundle.getString("reserved_mobile").substring(0, 3) + "***" + mBundle.getString("reserved_mobile").substring(mBundle.getString("reserved_mobile").length() - 5, mBundle.getString("reserved_mobile").length()) + "收到的短信验证码");
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    et_verification_code.finishTimer();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void bindCard() {
        try {
            JSONObject mJson = new JSONObject();
            BankListItemBean bankListItemBean=(BankListItemBean)mBundle.getSerializable("bankListItemBean");
            mJson.put("bank_name",bankListItemBean.getBank_name());
            mJson.put("bank_id",bankListItemBean.getBank_id());
            mJson.put("customer_id", UserUtil.getId(mContext));
            mJson.put("card_user_name", mBundle.getString("card_user_name"));
            mJson.put("card_num", mBundle.getString("card_num"));
            mJson.put("reserved_mobile", mBundle.getString("reserved_mobile"));
            mJson.put("verify_code", et_verification_code.getEditTextString());
            mJson.put("bind_no", bind_no);
            BindBankCard mBindBankCard = new BindBankCard(mContext);
            mBindBankCard.bindBankCard(mJson, bt_confirm, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    /*String cardNum=mBundle.getString("card_num");
                    String cardUserName=mBundle.getString("card_user_name");
                    String type = mBundle.getString(GlobalParams.REPAY_FROM_KEY);
                    UserUtil.setBindCard(mContext,"1");
                    UserUtil.setBankName(mContext,cardUserName);
                    UserUtil.setCardNum(mContext,cardNum);
                    int applyType = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);
                    if (GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY == applyType
                            || GlobalParams.APPLY_TYPE_INSTALLMENT == applyType
                            || GlobalParams.APPLY_TYPE_GET_CASH == applyType) { //申请现金贷，消费分期，提取现金
                        Bundle bundle = new Bundle();
                        bundle.putAll(mBundle);
                        bundle.putString(GlobalParams.CARD_NUM_KEY, cardNum);
                        bundle.putString(GlobalParams.CARD_USER_NAME, cardUserName);
                        String from = mBundle.getString(GlobalParams.REPAY_FROM_KEY);
                        if (GlobalParams.REPAY_FROM_SHOUFU.equals(from)) {
                            gotoActivity(mContext, RepayPasswordActivity.class, getIntent().getExtras());
                        } else {
                            if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
                                gotoActivity(mContext, InputPayPwdActivity.class, getIntent().getExtras());
                            } else {
                                gotoActivity(mContext, SetPayPwdActivity.class, getIntent().getExtras());
                            }
                        }
                        backActivity();
                    } else {  // 还款绑卡获取验证码
                        if ("bindBankCard".equals(type)) { // 从我的银行卡列表中进入绑卡获取验证码
                            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.BIND_CARD_SUCCESS_ACTION, null);
                            backActivity();
                        } else if (GlobalParams.REPAY_FROM_CONSUMPTION.equals(type)) {  // 消费还款
                            Bundle bundle = new Bundle();
                            bundle.putString(GlobalParams.CARD_NUM_KEY, cardNum);
                            bundle.putString(GlobalParams.CARD_USER_NAME, cardUserName);
                            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.BIND_CARD_SUCCESS_ACTION, bundle);
                            gotoActivity(mContext, RepayPasswordActivity.class, mBundle);
                            backActivity();
                        }
                    }*/
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }
}
