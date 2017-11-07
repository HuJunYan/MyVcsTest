package com.tianshen.cash.activity;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;

import butterknife.OnClick;

public class RiskPreScanFaceActivity extends BaseActivity {


    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_scan_face;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }
    @OnClick(R.id.tv_risk_pre_face_back)
    public void back() {
        backActivity();
    }
}
