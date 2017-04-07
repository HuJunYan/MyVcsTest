package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.event.ApplyEvent;
import com.tianshen.cash.event.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 确认借款页面
 */

public class ConfirmMoneyActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.tv_confirm_protocol)
    TextView tvConfirmProtocol;

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_money;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_apply:
                onClickApply();
                break;
        }
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        EventBus.getDefault().post(new ApplyEvent());
        gotoActivity(mContext, MainActivity.class, null);
    }

}
