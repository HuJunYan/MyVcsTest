package com.maibai.user.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.activity.AboutMaibeiActivity;
import com.maibai.user.activity.AddBankCardActivity;
import com.maibai.user.activity.ConsumptionRecordActivity;
import com.maibai.user.activity.LoginActivity;
import com.maibai.user.activity.MyBankCardActivity;
import com.maibai.user.activity.MyInfoActivity;
import com.maibai.user.activity.PayBillActivity;
import com.maibai.user.activity.RepayPasswordActivity;
import com.maibai.user.activity.SettingActivity;
import com.maibai.user.activity.WithdrawalsBillActivity;
import com.maibai.user.activity.WithdrawalsOrBillDetailActivity;
import com.maibai.user.base.BaseFragment;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ConsumeBigBillListBean;
import com.maibai.user.model.MineBean;
import com.maibai.user.model.MineCardInfoBean;
import com.maibai.user.net.api.GetConsumeBigBillList;
import com.maibai.user.net.api.Mine;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.MainUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.view.ImageTextView;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    private View ll_circle;
    private LinearLayout ll_get_cash_bill;
    private TextView tv_sum;
    private MineBean mMineBean;
    private TitleBar tb_title;
    private LinearLayout layout_container;
    private TextView tv_pay_back;
    private LinearLayout layout_transaction_container;
    private TextView tv_amout_this_month;
    private List<MineCardInfoBean> myCards = new ArrayList<MineCardInfoBean>();
    private ImageTextView itv_understand_maibei;
    private ImageTextView itv_my_bank_card;
    private ImageTextView itv_this_period_bill;
    private ImageView iv_promt;
    private ImageTextView itv_my_info;
    private LinearLayout ll_circle_login;
    private LinearLayout ll_pay_bill;
    private TextView tv_login;
    private TextView tv_withdrawals_repay_this_month;
    private MyBroadcastReciver myBroadcastReciver = new MyBroadcastReciver();

    final int REQUEST_LOGIN = 4, LOGINSUCCESS = 5, REQUEST_SETTING = 6, UNREGISTERE = 7, REPAY_REQUEST = 11, REPAYSUCCESS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        registerReceiver();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.REMOVEBIND_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.BIND_CARD_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.WECHAT_PAY_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.REFRESH_HOME_PAGE_ACTION);
        mContext.registerReceiver(myBroadcastReciver, intentFilter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initMineData();
        }
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void findViews(View rootView) {
        ll_circle = rootView.findViewById(R.id.ll_circle);
        ll_get_cash_bill = (LinearLayout) rootView.findViewById(R.id.ll_get_cash_bill);
        tv_sum = (TextView) rootView.findViewById(R.id.tv_sum);
        tb_title = (TitleBar) rootView.findViewById(R.id.tb_title);
        layout_container = (LinearLayout) rootView.findViewById(R.id.layout_container);
        tv_pay_back = (TextView) rootView.findViewById(R.id.tv_pay_back);
        iv_promt = (ImageView) rootView.findViewById(R.id.iv_promt);
        layout_transaction_container = (LinearLayout) rootView.findViewById(R.id.layout_transaction_container);
        tv_amout_this_month = (TextView) rootView.findViewById(R.id.tv_amout_this_month);
        itv_understand_maibei = (ImageTextView) rootView.findViewById(R.id.itv_understand_maibei);
        itv_this_period_bill = (ImageTextView) rootView.findViewById(R.id.itv_this_period_bill);
        itv_my_bank_card = (ImageTextView) rootView.findViewById(R.id.itv_my_bank_card);
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);
        ll_circle_login = (LinearLayout) rootView.findViewById(R.id.ll_circle_login);
        itv_my_info = (ImageTextView) rootView.findViewById(R.id.itv_my_info);
        ll_pay_bill = (LinearLayout) rootView.findViewById(R.id.ll_pay_bill);
        tv_withdrawals_repay_this_month = (TextView) rootView.findViewById(R.id.tv_withdrawals_repay_this_month);

    }

    private void initMineData() {
        if (MainUtil.isLogin(mContext)) {
            iv_promt.setVisibility(View.VISIBLE);
            tv_pay_back.setVisibility(View.VISIBLE);
            layout_transaction_container.setVisibility(View.VISIBLE);
            layout_container.setVisibility(View.VISIBLE);
            ll_circle.setVisibility(View.VISIBLE);

            tv_login.setVisibility(View.GONE);
            ll_circle_login.setVisibility(View.GONE);
            ll_circle.setBackgroundResource(R.mipmap.bta_my_refund);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("customer_id", UserUtil.getId(mContext));
                Mine mine = new Mine(mContext);
                mine.mine(jsonObject, null, false, new BaseNetCallBack<MineBean>() {
                    @Override
                    public void onSuccess(MineBean mineBean) {
                        mMineBean = mineBean;
                        String needRepayAmount = mineBean.getData().getNeed_repay_amount();
                        if ("".equals(needRepayAmount) || null == needRepayAmount) {
                            needRepayAmount = "0";
                        }
                        tv_sum.setText(Double.valueOf(Long.valueOf(needRepayAmount)) / 100 + "");
                        String consumeBillAmount = mineBean.getData().getConsume_bill_amount();
                        if ("".equals(consumeBillAmount) || null == consumeBillAmount) {
                            consumeBillAmount = "0";
                        }
                        tv_amout_this_month.setText(Double.valueOf(Long.parseLong(consumeBillAmount))/ 100 + "");

                        String withdrawalsBillAmount=mineBean.getData().getWithdrawals_bill_amount();
                        if("".equals(withdrawalsBillAmount)||null==withdrawalsBillAmount){
                            withdrawalsBillAmount="0";
                        }
                        tv_withdrawals_repay_this_month.setText(Double.valueOf(Long.valueOf(withdrawalsBillAmount))/100+"");
                        myCards = mineBean.getData().getBank_card_list();
                        SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.IS_MY_PAGE_NEED_REFRESH, "not_need_refresh");
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
            }
        } else {
            iv_promt.setVisibility(View.GONE);
            tv_pay_back.setVisibility(View.GONE);
            layout_transaction_container.setVisibility(View.GONE);
            layout_container.setVisibility(View.GONE);
            ll_circle.setVisibility(View.GONE);
            tv_login.setVisibility(View.VISIBLE);
            ll_circle_login.setVisibility(View.VISIBLE);
        }
    }

    private void changeToLoginMode() {
        if (MainUtil.isLogin(mContext)) {
            ToastUtil.showToast(mContext, "你尚未成功注册，或审核中");
            return;
        }
        iv_promt.setVisibility(View.VISIBLE);
        tv_pay_back.setVisibility(View.VISIBLE);
        layout_transaction_container.setVisibility(View.VISIBLE);

        layout_container.setVisibility(View.VISIBLE);
        ll_circle.setVisibility(View.VISIBLE);
        ll_circle.setBackgroundResource(R.mipmap.bta_my_refund);
        final Mine mine = new Mine(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            mine.mine(jsonObject, null, false, new BaseNetCallBack<MineBean>() {
                @Override
                public void onSuccess(MineBean mineBean) {
                    mMineBean = mineBean;
                    String needRepayAmount = mineBean.getData().getNeed_repay_amount();
                    if ("".equals(needRepayAmount) || null == needRepayAmount) {
                        needRepayAmount = "0";
                    }
                    tv_sum.setText(Double.valueOf(Long.valueOf(needRepayAmount)) / 100 + "");
                    String consumeBillAmount = mineBean.getData().getConsume_bill_amount();
                    if ("".equals(consumeBillAmount) || null == consumeBillAmount) {
                        consumeBillAmount = "0";
                    }
                    tv_amout_this_month.setText(Double.valueOf(Long.valueOf(consumeBillAmount))/100 + "");

                    String withdrawalsBillAmount=mineBean.getData().getWithdrawals_bill_amount();
                    if("".equals(withdrawalsBillAmount)||null==withdrawalsBillAmount){
                        withdrawalsBillAmount="0";
                    }
                    tv_withdrawals_repay_this_month.setText(Double.valueOf(Long.valueOf(withdrawalsBillAmount))/100+"");
                    myCards = mineBean.getData().getBank_card_list();
                    SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.IS_MY_PAGE_NEED_REFRESH, "not_need_refresh");
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

    private void changeToLogoutMode() {
        ll_circle.setVisibility(View.GONE);
        layout_transaction_container.setVisibility(View.GONE);
        ll_circle.setBackgroundResource(R.mipmap.my_not_login);
        layout_container.setVisibility(View.GONE);
        tv_pay_back.setVisibility(View.GONE);
        iv_promt.setVisibility(View.GONE);
    }

    public class TBTiltleListener implements TitleBar.TitleBarListener2 {
        @Override
        public void onLeftClick(View view) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onRightClick(View view) {
            if (!MainUtil.isLogin(mContext)) {
                gotoActivity(mContext, LoginActivity.class, null);
                return;
            }
            Intent intent = new Intent();
            intent.setClass(mContext, SettingActivity.class);
            intent.putExtra("isResume", MainUtil.isLogin(mContext));
            gotoActivityForResult(mContext, intent, REQUEST_SETTING);
        }
    }

    @Override
    protected void setListensers() {
        ll_circle.setOnClickListener(this);
        ll_get_cash_bill.setOnClickListener(this);
        tb_title.setListener(new TBTiltleListener());
        itv_understand_maibei.setOnClickListener(this);
        itv_my_bank_card.setOnClickListener(this);
        itv_this_period_bill.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        ll_circle_login.setOnClickListener(this);
        itv_my_info.setOnClickListener(this);
        ll_pay_bill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!MainUtil.isLogin(mContext)) {
            gotoActivity(mContext, LoginActivity.class, null);
            return;
        }
        switch (v.getId()) {
            case R.id.ll_circle:
                if (mMineBean != null && mMineBean.getData() != null ) {
                    if((null==mMineBean.getData().getConsume_bill_amount()||"0".equals(mMineBean.getData().getConsume_bill_amount()))&&!"0".equals(mMineBean.getData().getWithdrawals_bill_amount())){
                        gotoActivity(mContext, WithdrawalsBillActivity.class, null);
                        return;
                    }
                    gotoPayBillActivity();
                    return;
                }
                if (mMineBean == null || mMineBean.getData() == null || mMineBean.getData().getBank_card_list().size() == 0) {
                    addBankCard();
                } else {
                    repayMoney();
                }
                break;
            case R.id.ll_get_cash_bill://取现账单
                gotoActivity(mContext, WithdrawalsBillActivity.class, null);
                break;
            case R.id.itv_understand_maibei:
                gotoActivity(mContext, AboutMaibeiActivity.class, null);
                break;
            case R.id.itv_my_bank_card:
                String creditStep = UserUtil.getCreditStep(mContext);
                if ("0".equals(creditStep) || "".equals(creditStep) || null == creditStep) {
                    ToastUtil.showToast(mContext, "您未提交身份证，暂不能绑定银行卡");
                    return;
                } else {
                    gotoActivity(mContext, MyBankCardActivity.class, null);
                }
                break;
            case R.id.ll_circle_login:
            case R.id.tv_login:
                gotoActivity(mContext, LoginActivity.class, null);
                break;
            case R.id.itv_this_period_bill://本期账单
                gotoActivity(mContext, ConsumptionRecordActivity.class, null);
                break;
            case R.id.itv_my_info:
                gotoActivity(mContext, MyInfoActivity.class, null);
                break;
            case R.id.ll_pay_bill:
                //消费账单
                gotoPayBillActivity();
                break;

        }
    }

    private void gotoPayBillActivity() {

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customer_id",UserUtil.getId(mContext));
            GetConsumeBigBillList getConsumeBigBillList=new GetConsumeBigBillList(mContext);
            getConsumeBigBillList.getConsumeBigBillList(jsonObject, null, true, new BaseNetCallBack<ConsumeBigBillListBean>() {
                @Override
                public void onSuccess(ConsumeBigBillListBean paramT) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("mine", mMineBean);
                    bundle.putSerializable("ConsumeBigBillListBean",paramT);
                    if("1".equals(paramT.getType())){
                        gotoActivity(mContext, PayBillActivity.class, bundle);
                    }else if("2".equals(paramT.getType())){
                        bundle.putInt(GlobalParams.BILL_DETAIL_FROM_KEY,GlobalParams.BILL_DETAIL_FROM_PAY);
                        gotoActivity(mContext, WithdrawalsOrBillDetailActivity.class,bundle);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    gotoActivity(mContext,PayBillActivity.class,null);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBankCard() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MineBean", mMineBean);
        bundle.putString("payments_type", "whole");
        bundle.putString("type", "pay");
        gotoActivity(mContext, AddBankCardActivity.class, bundle);

    }

    private void repayMoney() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MineBean", mMineBean);
        bundle.putString("payments_type", "whole");
        String repayAmount = tv_sum.getText().toString().trim();
        if (!"".equals(repayAmount) && !"0".equals(repayAmount)) {
            Intent intent = new Intent(mContext, RepayPasswordActivity.class);
            intent.putExtras(bundle);
            gotoActivityForResult(mContext, intent, REPAY_REQUEST);
        } else {
            ToastUtil.showToast(mContext, "提款金额为0！");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReciver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == LOGINSUCCESS) {
                    changeToLoginMode();
                }
            case REQUEST_SETTING:
                if (resultCode == UNREGISTERE) {
                    changeToLogoutMode();
                }
                break;

            case REPAY_REQUEST:
                if (resultCode == REPAYSUCCESS) {
                    tv_sum.setText("0.00");
                }
                break;
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        //广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            try {
                if (GlobalParams.BIND_CARD_SUCCESS_ACTION.equals(action) || GlobalParams.REMOVEBIND_SUCCESS_ACTION.equals(action) || GlobalParams.WECHAT_PAY_SUCCESS_ACTION.equals(action) || GlobalParams.REFRESH_HOME_PAGE_ACTION.equals(action) || GlobalParams.WX_REPAY_CONSUMPTION_SUCCESS_ACTION.equals(action)) {
                    initMineData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
            }
        }
    }

}
