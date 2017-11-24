package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.BankSelectedChangeEvent;
import com.tianshen.cash.event.RefreshRepayDataEvent;
import com.tianshen.cash.event.RepayEvent;
import com.tianshen.cash.event.RepayFailureEvent;
import com.tianshen.cash.model.RepayInfoBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.GetRepayInfo;
import com.tianshen.cash.net.api.Repayment;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.FileUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.RepayBankItemView;
import com.tianshen.cash.view.RepayItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wang on 2017/11/23.
 */

public class ConfirmRepayZiYingActivity extends BaseActivity {
    private RepayInfoBean mRepayInfoBean;
    @BindView(R.id.tv_ziying_repay_confirm)
    TextView tv_ziying_repay_confirm;
    @BindView(R.id.tv_alipay_repay_confirm)
    TextView tv_alipay_repay_confirm;
    @BindView(R.id.tv_repay_total_money)
    TextView tv_repay_total_money;
    @BindView(R.id.tv_repay_money)
    TextView tv_repay_money;
    @BindView(R.id.tv_repay_rate)
    TextView tv_repay_rate;
    @BindView(R.id.ll_repay_money_container)
    LinearLayout ll_repay_money_container;
    @BindView(R.id.ll_bank_item_container)
    LinearLayout ll_bank_item_container;
    private int currentPosition = 0;
    private HashMap<String, Integer> bankIconInfo;
    private String aliPayUrl;
    private boolean isAlipay;
    private ArrayList<View> mItemViewList;

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_repay_zi_ying;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankIconInfo = FileUtils.getBankIconInfo();
        initRepayData();
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        RxView.clicks(tv_ziying_repay_confirm)//1秒钟之内禁用重复点击
                .throttleFirst(60, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                repayBySelf();
            }
        });
    }


    @OnClick({R.id.tv_confirm_money_back, R.id.tv_alipay_repay_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                EventBus.getDefault().post(new RepayFailureEvent());//用户取消还款
                backActivity();
                break;
            case R.id.tv_alipay_repay_confirm:
                gotoAliPay();
                break;
        }
    }

    private void gotoAliPay() {
        if (TextUtils.isEmpty(aliPayUrl)) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, aliPayUrl);
        gotoActivity(mContext, AlipayWebActivity.class, bundle);
    }

    /**
     * 得到确认还款信息
     */
    private void initRepayData() {
        if (mItemViewList == null) {
            mItemViewList = new ArrayList<>();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String consume_id = extras.getString(GlobalParams.CONSUME_ID, "");
                jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
            }
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetRepayInfo getRepayInfo = new GetRepayInfo(mContext);
            getRepayInfo.getRepayInfo(jsonObject, null, true, new BaseNetCallBack<RepayInfoBean>() {
                @Override
                public void onSuccess(RepayInfoBean paramT) {
                    mRepayInfoBean = paramT;
                    refreshUI();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (mRepayInfoBean == null || mRepayInfoBean.getData() == null) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        RepayInfoBean.MoneyDetail money_detail = mRepayInfoBean.getData().money_detail;
        if (money_detail != null) {
            tv_repay_total_money.setText(money_detail.consume_amount_str);
            tv_repay_money.setText(money_detail.consume_capital_amount_str);
            tv_repay_rate.setText(money_detail.consume_interest_amount_str);
        }
        ArrayList<RepayInfoBean.CompositeDetail> composite_detail = mRepayInfoBean.getData().composite_detail;
        if (composite_detail != null) {
            setMoneyListData(composite_detail);
        }
        RepayInfoBean.RepayMentStyle repayment_style = mRepayInfoBean.getData().repayment_style;
        if (repayment_style != null) {
            ArrayList<RepayInfoBean.BankList> bank_list = repayment_style.bank_list;
            if (bank_list != null) {
                for (int i = 0; i < bank_list.size(); i++) {
                    RepayInfoBean.BankList bankList = bank_list.get(i);
                    RepayBankItemView repayBankItemView = new RepayBankItemView(mContext).setData(bankList.bank_name, bankIconInfo.get(bankList.bank_gate_id), currentPosition, i);
                    mItemViewList.add(repayBankItemView);
                    ll_bank_item_container.addView(repayBankItemView);
                }
            }
            RepayInfoBean.AliPay alipay = repayment_style.alipay;
            if (alipay != null) {
                aliPayUrl = alipay.alipay_url;
                int aliPayPosition = bank_list == null ? 0 : bank_list.size();
                RepayBankItemView repayBankItemView = new RepayBankItemView(mContext).setAliPayData(alipay.title, alipay.description, aliPayPosition, currentPosition);
                mItemViewList.add(repayBankItemView);
                ll_bank_item_container.addView(repayBankItemView);
            }
            if (bank_list == null || bank_list.size() == 0) {
                isAlipay = true;
            }
            refreshConfirmState(isAlipay);
        }
    }

    private void refreshConfirmState(boolean isAlipay) {
        if (isAlipay) {
            tv_ziying_repay_confirm.setVisibility(View.GONE);
            tv_alipay_repay_confirm.setVisibility(View.VISIBLE);
        } else {
            tv_ziying_repay_confirm.setVisibility(View.VISIBLE);
            tv_alipay_repay_confirm.setVisibility(View.GONE);
        }
    }

    //设置金额集合数据
    private void setMoneyListData(ArrayList<RepayInfoBean.CompositeDetail> composite_detail) {
        for (int i = 0; i < composite_detail.size(); i++) {
            RepayInfoBean.CompositeDetail compositeDetail = composite_detail.get(i);
            ll_repay_money_container.addView(new RepayItemView(mContext).setData(compositeDetail.title, compositeDetail.value));
        }

    }

    /**
     * 自己产品还款
     */
    private void repayBySelf() {
        if (true) {
            ToastUtil.showToast(mContext, "自营");
            return;
        }
        if (mRepayInfoBean == null || mRepayInfoBean.getData() == null || mRepayInfoBean.getData().repayment_style == null) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("paytype", "1"); // (支付渠道，写死1 联动优势)
            jsonObject.put("type", "1"); // （支付类型 1还款 2付首付 不传默认为1）

            String id = mRepayInfoBean.getData().getId();
            String consumeId = mRepayInfoBean.getData().getConsume_id();
            String repayDate = mRepayInfoBean.getData().getRepay_date();
            String consumeAmount = mRepayInfoBean.getData().getConsume_amount();
            String overdueAmount = mRepayInfoBean.getData().getOverdue_amount();

            JSONObject consumeDataJSON = new JSONObject();
            consumeDataJSON.put("consume_id", consumeId);
            consumeDataJSON.put("type", "5");
            consumeDataJSON.put("repay_date", "");
            consumeDataJSON.put("amount", "");
            consumeDataJSON.put("overdue_amount", "");

            JSONObject installmentHistoryJSON = new JSONObject();
            installmentHistoryJSON.put("id", id);
            installmentHistoryJSON.put("repay_date", repayDate);
            installmentHistoryJSON.put("amount", consumeAmount);
            installmentHistoryJSON.put("overdue_amount", overdueAmount);

            JSONArray historyArray = new JSONArray();
            historyArray.put(installmentHistoryJSON);

            consumeDataJSON.put("installment_history", historyArray);
            JSONArray consume_data_array = new JSONArray();
            consume_data_array.put(consumeDataJSON);
            jsonObject.put("consume_data", consume_data_array);

            Repayment getRepayInfo = new Repayment(mContext);
            getRepayInfo.repayment(jsonObject, tv_ziying_repay_confirm, true, 5, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    EventBus.getDefault().post(new RepayEvent());
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    EventBus.getDefault().post(new RepayFailureEvent());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new RepayFailureEvent());
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe
    public void onRefreshRepayDataEvent(RefreshRepayDataEvent event) {
        initRepayData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mItemViewList.size(); i++) {
            EventBus.getDefault().unregister(mItemViewList.get(i));
        }
    }

    @Subscribe
    public void onBankSelectedChangeEvent(BankSelectedChangeEvent event) {
        currentPosition = event.currentPosition;
        isAlipay = event.isAlipay;
        refreshConfirmState(isAlipay);
    }
}
