package com.tianshen.cash.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
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
import com.tianshen.cash.view.RepayItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wang on 2017/11/23.
 */

public class ConfirmRepayZiYingActivity extends BaseActivity {
    private RepayInfoBean mRepayInfoBean;

    @BindView(R.id.tv_repay_total_money)
    TextView tv_repay_total_money;
    @BindView(R.id.tv_repay_money)
    TextView tv_repay_money;
    @BindView(R.id.tv_repay_rate)
    TextView tv_repay_rate;
    @BindView(R.id.tv_ziying_repay_confirm)
    TextView tv_ziying_repay_confirm;
    @BindView(R.id.ll_repay_money_container)
    LinearLayout ll_repay_money_container;
    @BindView(R.id.iv_bank_icon)
    ImageView iv_bank_icon;

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_repay_zi_ying;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRepayData();
        HashMap<String, String> bankIconInfo = FileUtils.getBankIconInfo(mContext);
        try {
            Bitmap cmb = BitmapFactory.decodeStream(getAssets().open("bank_icon" + File.separator + bankIconInfo.get("ABC")));

            iv_bank_icon.setImageBitmap(cmb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_confirm_money_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                EventBus.getDefault().post(new RepayFailureEvent());//用户取消还款
                backActivity();
                break;
        }
    }

    /**
     * 得到确认还款信息
     */
    private void initRepayData() {

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
}
