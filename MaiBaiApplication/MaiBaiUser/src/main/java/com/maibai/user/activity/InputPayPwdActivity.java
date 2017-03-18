package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CashSubItemBean;
import com.maibai.user.model.OrderBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.model.WithdrawalsApplyBean;
import com.maibai.user.model.WithdrawalsOrderBean;
import com.maibai.user.net.api.OrderInstallmentRepay;
import com.maibai.user.net.api.OrderNextMonthRepay;
import com.maibai.user.net.api.UnbindBankCard;
import com.maibai.user.net.api.WithdrawalsApply;
import com.maibai.user.net.api.WithdrawalsOrder;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.ViewUtil;
import com.maibai.user.view.PasswordInputView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sbyh on 16/7/8.
 */
public class InputPayPwdActivity extends BaseActivity implements View.OnClickListener {
    private String password;
    private PasswordInputView piv_password;
    private Button bt_confirm;
    private Bundle mBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if ("unBindCard".equals(mBundle.getString("intent"))) {
            bt_confirm.setText("确认解绑");
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_input_password;
    }

    @Override
    protected void findViews() {
        piv_password = (PasswordInputView) findViewById(R.id.piv_password);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                password = piv_password.getText().toString();
                if (password == null || "".equals(password)) {
                    ToastUtil.showToast(mContext, "请输入密码！");
                    break;
                }
                mBundle.putString("password", password);
                handleConfirm();
                break;
        }
    }

    private void handleConfirm() {
        try {
            if ("unBindCard".equals(mBundle.getString("intent"))) {
                removeBindBankCard();
                return;
            }
            if (mBundle != null) {
                int applyType = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);
                switch (applyType) {
                    case GlobalParams.APPLY_TYPE_INSTALLMENT:
                        payOrder();
                        break;
                    case GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY:
                        withdrawalsApply();
                        break;
                    case GlobalParams.APPLY_TYPE_GET_CASH:
                        withdrawalsOrder();
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void withdrawalsOrder(){
        CashSubItemBean mCashSubItemBean = (CashSubItemBean)(mBundle.getSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY));
        if(null==mCashSubItemBean){
            return;
        }
        try {
            SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customer_id",UserUtil.getId(mContext));
            jsonObject.put("amount",mCashSubItemBean.getWithdrawal_amount());
            jsonObject.put("pay_pass",mBundle.getString("password"));
            jsonObject.put("location", share.getString("location"));
            jsonObject.put("province",share.getString("province"));
            jsonObject.put("city",share.getString("city"));
            jsonObject.put("country",share.getString("country"));
            jsonObject.put("address",share.getString("address"));
            jsonObject.put("black_box", new GetTelephoneUtils(mContext).getBlackBox());
            final String push_id = JPushInterface.getRegistrationID(mContext);
            if (push_id != null && !"".equals(push_id)) {
                jsonObject.put("push_id", push_id);
            }
            jsonObject.put("repay_id",mBundle.getString("repay_id"));
            WithdrawalsOrder withdrawalsOrder=new WithdrawalsOrder(mContext);
            withdrawalsOrder.withdrawalsOrder(jsonObject, null, true, new BaseNetCallBack<WithdrawalsOrderBean>() {
                @Override
                public void onSuccess(WithdrawalsOrderBean paramT) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("withdrawalsOrder",paramT);
                    gotoActivity(mContext,WithdrawalsSuccessActivity.class,bundle);
                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.WITHDRAWALS_ORDER_SUCCESS_ACTION,null);
                    if (push_id != null && !"".equals(push_id)) {
                        SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.JPUSH_ID_KEY, push_id);
                    }
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
                    if (push_id != null && !"".equals(push_id)) {
                        SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.JPUSH_ID_KEY, push_id);
                    }
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

    private void removeBindBankCard() {
        //解除绑定
        UnbindBankCard unbindBankCard = new UnbindBankCard(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("bank_card", mBundle.getString("bank_card"));
            jsonObject.put("pay_pass", mBundle.getString("password"));
            unbindBankCard.unbindBankCard(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    //解除绑定成功
                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REMOVEBIND_SUCCESS_ACTION, null);
                    UserUtil.setBindCard(mContext,"0");
                    UserUtil.setCardNum(mContext,"");
                    UserUtil.setBankName(mContext,"");
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



    private void payOrder() {
        try {
            JSONObject json = new JSONObject();
            SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
            json.put("customer_id", UserUtil.getId(mContext));
            json.put("merchant_id", mBundle.getString("merchant_id"));
            json.put("amount", mBundle.getString("price", "0"));
            json.put("pay_pass", password);
            json.put("location", share.getString("location"));
            json.put("province", share.getString("province"));
            json.put("city", share.getString("city"));
            json.put("country", share.getString("country"));
            json.put("address", share.getString("address"));
            json.put("black_box", new GetTelephoneUtils(mContext).getBlackBox());
            final String push_id = JPushInterface.getRegistrationID(mContext);
            if (push_id != null && !"".equals(push_id)) {
                json.put("push_id", push_id);
            }
            String downType = mBundle.getString("down_type");
            if (!"".equals(downType) && null != downType) {
                json.put("down_type", downType);
            }
            String downPayment = mBundle.getString("down_payment");
            if (!"".equals(downPayment) && null != downPayment) {
                json.put("down_payment", downPayment);
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
                mBundle.putString(GlobalParams.CONSUME_ID_KEY, paramT.getData().getConsume_id());
                mBundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, paramT);
                mBundle.putBoolean("isSuccess", true);
                if (paramT.getData().getIs_need_verify() == 0 && "1".equals(paramT.getData().getIs_need_fee())) {
                    mBundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_PLACE_AN_ORDER);
                    gotoTaget(true, false, VerifySuccessActivity.class);
                } else {
                    gotoTaget(true, paramT.getData().getIs_need_verify() == 1, PaySuccessActivity.class);
                }
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                if (errorCode == 108 || errorCode == 107) {
                    ToastUtil.showToast(mContext, mResponseBean.getMsg());
                    piv_password.setText("");
                    return;
                }
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
                mBundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, paramT);
                mBundle.putBoolean("isSuccess", true);
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
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                if (errorCode == 108 || errorCode == 107) {
                    ToastUtil.showToast(mContext, mResponseBean.getMsg());
                    piv_password.setText("");
                    return;
                }
                mBundle.putString("error_result", result);
                gotoTaget(false, false, PaySucNextMonthActivity.class);
            }
        });
    }
    private void gotoTaget(boolean isSuccess, boolean isVerify, Class<?> className) {
        ViewUtil.cancelLoadingDialog();
        mBundle.putBoolean("isSuccess", isSuccess);
        if (isVerify) {
            gotoActivity(InputPayPwdActivity.this, WaitVerifyPayActivity.class, mBundle);
        } else {
            gotoActivity(InputPayPwdActivity.this, className, mBundle);
        }
        InputPayPwdActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
