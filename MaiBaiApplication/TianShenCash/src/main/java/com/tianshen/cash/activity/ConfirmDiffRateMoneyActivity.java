package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.DiffRateInfoBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.api.GetDiffOrderPayApi;
import com.tianshen.cash.net.api.GetDiffRateInfoApi;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.TianShenUserUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 确认借款页面  差异化费率
 */

public class ConfirmDiffRateMoneyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_confirm_money_back)
    TextView tvConfirmMoneyBack;
    @BindView(R.id.tv_confirm_withdrawal_key)
    TextView tvConfirmWithdrawalKey;
    @BindView(R.id.tv_confirm_withdrawal)
    TextView tvConfirmWithdrawal;
    @BindView(R.id.tv_confirm_time_key)
    TextView tvConfirmTimeKey;
    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    @BindView(R.id.tv_confirm_procedures_key)
    TextView tvConfirmProceduresKey;
    @BindView(R.id.tv_confirm_procedures)
    TextView tvConfirmProcedures;
    @BindView(R.id.tv_confirm_transfer_key)
    TextView tvConfirmTransferKey;
    @BindView(R.id.tv_confirm_transfer)
    TextView tvConfirmTransfer;
    @BindView(R.id.tv_confirm_banck_card_key)
    TextView tvConfirmBanckCardKey;
    @BindView(R.id.tv_confirm_banck_card)
    TextView tvConfirmBanckCard;
    @BindView(R.id.tv_confirm_repay_key)
    TextView tvConfirmRepayKey;
    @BindView(R.id.tv_confirm_repay)
    TextView tvConfirmRepay;
    @BindView(R.id.tv_confirm_apply)
    TextView tvConfirmApply;
    private String consume_id;
    private DiffRateInfoBean mDiffRateInfoBean;
    private boolean mInPostMoneyStatus;//是否请求提现状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrderConfirmData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_diff_rate_money;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmApply.setOnClickListener(this);
    }

    /**
     * 得到确认借款数据
     */
    private void initOrderConfirmData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                consume_id = bundle.getString(GlobalParams.CONSUME_ID);
            }
        }
        try {
            JSONObject jsonObject = new JSONObject();
            String customer_id = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, customer_id);
            jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
            GetDiffRateInfoApi getOrderConfirm = new GetDiffRateInfoApi(mContext);
            getOrderConfirm.getDiffRateInfo(jsonObject, null, true, new BaseNetCallBack<DiffRateInfoBean>() {
                @Override
                public void onSuccess(DiffRateInfoBean bean) {
                    mDiffRateInfoBean = bean;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_confirm_apply:
                onClickApply();
                break;
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (mDiffRateInfoBean == null || mDiffRateInfoBean.data == null) {
            return;
        }
        showNormalUI();
    }

    /**
     * 显示正常的UI
     */
    private void showNormalUI() {
        tvConfirmMoneyBack.setVisibility(View.VISIBLE);
        DiffRateInfoBean.DiffRateInfo diffRateInfo = mDiffRateInfoBean.data;
        String consume_amount = diffRateInfo.consume_amount;
        String poundage = diffRateInfo.poundage;
        String amount = diffRateInfo.amount;
        String repayment_amout = diffRateInfo.repayment_amout;
        String timer = diffRateInfo.timer;
        String bank_info = diffRateInfo.bank_info;
        try {
            String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
            String poundageY = MoneyUtils.changeF2Y(poundage);
            String amountY = MoneyUtils.changeF2Y(amount);
            String repaymentAmoutY = MoneyUtils.changeF2Y(repayment_amout);
            tvConfirmWithdrawal.setText(consume_amountY + "元");
            tvConfirmProcedures.setText(poundageY + "元");
            tvConfirmTransfer.setText(amountY + "元");
            tvConfirmRepay.setText(repaymentAmoutY + "元");
            tvConfirmTime.setText(timer + "天");
            tvConfirmBanckCard.setText(bank_info);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        if (null == mDiffRateInfoBean || mInPostMoneyStatus || mDiffRateInfoBean.data == null) {
            return;
        }
        mInPostMoneyStatus = true;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(this));
            jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
            GetDiffOrderPayApi getDiffOrderPayApi = new GetDiffOrderPayApi(this);
            getDiffOrderPayApi.getDiffOrderPay(jsonObject, tvConfirmApply, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean responseBean) {
                    mInPostMoneyStatus = false;
                    if (responseBean.getCode() == 0) {
                        gotoMainActivity();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    mInPostMoneyStatus = false;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * 回到首页
     */
    private void gotoMainActivity() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        gotoActivity(mContext, MainActivity.class, null);
        finish();
    }


}
