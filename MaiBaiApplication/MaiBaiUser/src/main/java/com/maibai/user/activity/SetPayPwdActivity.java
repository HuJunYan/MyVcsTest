package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CashSubItemBean;
import com.maibai.user.model.OrderBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.model.WithdrawalsApplyBean;
import com.maibai.user.net.api.OrderInstallmentRepay;
import com.maibai.user.net.api.OrderNextMonthRepay;
import com.maibai.user.net.api.SetPayPassword;
import com.maibai.user.net.api.WithdrawalsApply;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.ViewUtil;
import com.maibai.user.view.MyEditText;
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
    private int PRO_SERVER=2;//用户服务协议
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
        tv_protocol=(TextView)findViewById(R.id.tv_protocol);
        checkBox=(CheckBox)findViewById(R.id.check_agree);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bt_confirm.setClickable(true);
                    bt_confirm.setBackgroundResource(R.drawable.select_bt);
                }
                else{
                    bt_confirm.setClickable(false);
                    bt_confirm.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                        json.put("black_box",mBundle.getString("blackBox"));
                        json.put("customer_id", UserUtil.getId(mContext));
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
                                switch (applyType) {
                                    case GlobalParams.APPLY_TYPE_INSTALLMENT:
                                        payOrder();
                                        break;
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
                Bundle bundle=new Bundle();
                bundle.putInt("pro_type",PRO_SERVER);
                gotoActivity(mContext,ProtocolActivity.class,bundle);
                break;
        }
    }

    private void withdrawalsApply() {
        SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
        WithdrawalsApply withdrawalsApply = new WithdrawalsApply(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("province",share.getString("province"));
            jsonObject.put("city",share.getString("city"));
            jsonObject.put("country", share.getString("country"));
            jsonObject.put("location", share.getString("location"));
            jsonObject.put("pay_pass", password);
            jsonObject.put("black_box", new GetTelephoneUtils(mContext).getBlackBox());
            final String push_id = JPushInterface.getRegistrationID(mContext);
            if (push_id != null && !"".equals(push_id)) {
                jsonObject.put("push_id", push_id);
            }
            final CashSubItemBean cashSubItemBean = (CashSubItemBean)(getIntent().getExtras().getSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY));
            jsonObject.put("apply_amount", cashSubItemBean.getWithdrawal_amount());
            withdrawalsApply.withdrawalsApply(jsonObject, bt_confirm, true, new BaseNetCallBack<WithdrawalsApplyBean>() {
                @Override
                public void onSuccess(WithdrawalsApplyBean paramT) {
                    String isNeedVerify = paramT.getData().getIs_need_verify();
                    switch (isNeedVerify) {
                        case "1":
                            Bundle bundle = new Bundle();
                            bundle.putString(GlobalParams.WITHDRAWALS_VERIFY_ID_KEY, paramT.getData().getConsume_id());
                            bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
                            gotoActivity(mContext, WaitVerifyWithdrawalActivity.class, bundle);//提现待审核
                            break;
                        case "0":
                            String amountStr = paramT.getData().getAmount();
                            if (amountStr!=null&&!"".equals(amountStr)) {
                                int amountInt = (int)(Double.parseDouble(amountStr)/100);
                                ToastUtil.showToast(mContext, "恭喜，您成功申请可体现额度"+amountInt+"元！");
                                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
                            }
                            break;
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void payOrder() {
        try {
            JSONObject json = new JSONObject();
            SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
            json.put("customer_id", UserUtil.getId(mContext));
            json.put("merchant_id", mBundle.getString("merchant_id"));
            json.put("amount", mBundle.getString("price", "0"));
            json.put("pay_pass", et_set_pwd.getEditTextString());
            json.put("location", share.getString("location"));
            json.put("province", share.getString("province"));
            json.put("city", share.getString("city"));
            json.put("country", share.getString("country"));
            json.put("address", share.getString("address"));
            json.put("black_box",mBundle.getString("blackBox"));
            final String push_id = JPushInterface.getRegistrationID(mContext);
            if (push_id != null && !"".equals(push_id)) {
                json.put("push_id", push_id);
            }
            String downType=mBundle.getString("down_type");
            if(!"".equals(downType)&&null!=downType){
                json.put("down_type",downType);
            }
            String downPayment=mBundle.getString("down_payment");
            if(!"".equals(downPayment)&&null!=downPayment){
                json.put("down_payment",downPayment);
            }
            String repayId = mBundle.getString("id");
            if (!"".equals(repayId) && null != repayId) {
                json.put("repay_id", repayId);
            }

            String type = mBundle.getString("repay_type");
            if(type == null || "".equals(type)) {
                String repay_type = UserUtil.getType(mContext);
                if ("2".equals(repay_type)) {
                    type = "installment";
                } else if ("1".equals(repay_type)) {
                    type = "next_month";
                }
                json.put("merchant_id", UserUtil.getMerchantId(mContext));
                json.put("amount", UserUtil.getConsumeAmount(mContext));
            }
            if ("next_month".equals(type)) {
                payNextMonth(json);
            } else if ("installment".equals(type)) {
                payInstalments(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void payInstalments(JSONObject json) {
        OrderInstallmentRepay mOrderInstallmentRepay = new OrderInstallmentRepay(mContext);
        mOrderInstallmentRepay.orderInstallmentRepay(json, new BaseNetCallBack<OrderBean>() {
            @Override
            public void onSuccess(OrderBean paramT) {
                UserUtil.setCustomerAmount(mContext,paramT.getData().getAmount());
                UserUtil.setBalanceAmount(mContext, paramT.getData().getBalance_amount());
                UserUtil.setMaxAmount(mContext, paramT.getData().getMax_amount());
                UserUtil.setIssetPayPass(mContext, "1");
                mBundle.putString(GlobalParams.CONSUME_ID_KEY, paramT.getData().getConsume_id());
                mBundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, paramT);
                mBundle.putBoolean("isSuccess",true);
                if (paramT.getData().getIs_need_verify() == 0 && "1".equals(paramT.getData().getIs_need_fee())) {
                    mBundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_PLACE_AN_ORDER);
                    gotoTaget(true, false, VerifySuccessActivity.class);
                } else {
                    gotoTaget(true, paramT.getData().getIs_need_verify() == 1, PaySuccessActivity.class);
                }
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                mBundle.putString("error_result", result);
                gotoTaget(false, false, PaySuccessActivity.class);
            }
        });
    }

    private void payNextMonth(JSONObject json) {
        OrderNextMonthRepay mOrderNextMonthRepay = new OrderNextMonthRepay(this);
        mOrderNextMonthRepay.orderNextMonthRepay(json, null, true, new BaseNetCallBack<OrderBean>() {
            @Override
            public void onSuccess(OrderBean paramT) {
                UserUtil.setCustomerAmount(mContext,paramT.getData().getAmount());
                UserUtil.setBalanceAmount(mContext, paramT.getData().getBalance_amount());
                UserUtil.setMaxAmount(mContext, paramT.getData().getMax_amount());
                UserUtil.setIssetPayPass(mContext, "1");
                mBundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, paramT);
                mBundle.putBoolean("isSuccess",true);
                if (paramT.getData().getIs_need_verify() == 0 && "1".equals(paramT.getData().getIs_need_fee())) {
                    mBundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_PLACE_AN_ORDER);
                    gotoTaget(true, false, VerifySuccessActivity.class);
                } else {
                    mBundle.putString(GlobalParams.CONSUME_ID_KEY, paramT.getData().getConsume_id());
                    gotoTaget(true, paramT.getData().getIs_need_verify() == 1, PaySucNextMonthActivity.class);
                }
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                mBundle.putString("error_result", result);
                gotoTaget(false, false, PaySucNextMonthActivity.class);
            }
        });
    }

    private void gotoTaget(boolean isSuccess, boolean isVerify, Class<?> className) {
        ViewUtil.cancelLoadingDialog();
        mBundle.putBoolean("isSuccess", isSuccess);
        if (isVerify) {
            gotoActivity(SetPayPwdActivity.this, WaitVerifyPayActivity.class, mBundle);
        } else {
            gotoActivity(SetPayPwdActivity.this, className, mBundle);
        }
        SetPayPwdActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
