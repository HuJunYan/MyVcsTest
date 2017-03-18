package com.maibai.cash.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.base.MyApplication;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.CashSubItemBean;

import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.model.WithdrawalsApplyBean;
import com.maibai.cash.model.WithdrawalsOrderBean;

import com.maibai.cash.net.api.UnbindBankCard;
import com.maibai.cash.net.api.WithdrawalsApply;
import com.maibai.cash.net.api.WithdrawalsOrder;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.GetTelephoneUtils;
import com.maibai.cash.utils.LocationUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SendBroadCastUtil;
import com.maibai.cash.utils.SharedPreferencesUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.ViewUtil;
import com.maibai.cash.utils.WithdrawalsApplyResultUtil;
import com.maibai.cash.view.PasswordInputView;
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

        if (mBundle != null) {
            if ("unBindCard".equals(mBundle.getString("intent"))) {
                bt_confirm.setText("确认解绑");
            }
            int applyType = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);
            switch (applyType) {
                case GlobalParams.APPLY_TYPE_INSTALLMENT:
                    break;
                case GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY:
                    bt_confirm.setText("确认");
                    break;
                case GlobalParams.APPLY_TYPE_GET_CASH:
                    bt_confirm.setText("确认提现");
                    break;

            }
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
                            Intent intent=new Intent(GlobalParams.LOGIN_SUCCESS_ACTION);
                            sendBroadcast(intent);
                            String amountStr = paramT.getData().getAmount();
                            if (amountStr!=null&&!"".equals(amountStr)) {
                                int amountInt = (int)(Double.parseDouble(amountStr)/100);
//                                ToastUtil.showToast(mContext, "恭喜，您成功申请可体现额度"+amountInt+"元！");
                                final Dialog dialog = new AlertDialog.Builder(mContext, R.style.withdrawals_diaog).create();
                                View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_withdrawals_success, null);
                                view.getBackground().setAlpha(100);//0~255透明度值
                                TextView tv_amount=(TextView)view.findViewById(R.id.tv_amount);
                                TextView tv_amount_small=(TextView)view.findViewById(R.id.tv_amount_small);
                                Button bt_get_money=(Button)view.findViewById(R.id.bt_get_money);
                                tv_amount.setText(amountInt+"");
                                tv_amount_small.setText(amountInt+"");
                                bt_get_money.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
