package com.tianshen.cash.activity;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;

import butterknife.OnClick;

public class RiskPreAuthIdentityActivity extends BaseActivity {


    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_auth_identity;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick(R.id.tv_auth_bank_card_back)
    public void back() {
        backActivity();
    }

}
