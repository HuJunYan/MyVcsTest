package com.tianshen.cash.activity;

import android.view.View;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.utils.ToastUtil;

import butterknife.BindView;

/**
 * 确认换款页面
 */

public class ConfirmRepayActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_confirm_money_back)
    TextView tvConfirmMoneyBack;
    @BindView(R.id.tv_confirm_repay_key)
    TextView tvConfirmRepayKey;
    @BindView(R.id.tv_confirm_repay)
    TextView tvConfirmRepay;
    @BindView(R.id.tv_confirm_repay_bank_key)
    TextView tvConfirmRepayBankKey;
    @BindView(R.id.tv_confirm_repay_bank)
    TextView tvConfirmRepayBank;
    @BindView(R.id.tv_confirm_repay_bank_num_key)
    TextView tvConfirmRepayBankNumKey;
    @BindView(R.id.tv_confirm_repay_num_bank)
    TextView tvConfirmRepayNumBank;
    @BindView(R.id.tv_confirm_repay_apply)
    TextView tvConfirmRepayApply;
    @BindView(R.id.tv_confirm_protocol)
    TextView tvConfirmProtocol;

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_repay;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmRepayApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_confirm_repay_apply:
                onClickApply();
                break;
        }
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        ToastUtil.showToast(mContext, "点击了还款");
        backActivity();
    }

}
