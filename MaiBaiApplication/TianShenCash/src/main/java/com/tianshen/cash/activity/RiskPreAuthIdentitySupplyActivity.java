package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RiskPreAuthIdentitySupplyActivity extends BaseActivity {
    @BindView(R.id.iv_risk_pre_add_front)
    ImageView iv_risk_pre_add_front; //身份证正面图
    @BindView(R.id.iv_risk_pre_add_back)
    ImageView iv_risk_pre_add_back;//身份证反面图
    @BindView(R.id.et_risk_pre_supply_real_name)
    EditText et_risk_pre_supply_real_name; //姓名
    @BindView(R.id.et_risk_pre_supply_id_num)
    EditText et_risk_pre_supply_id_num;
    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_auth_identity_supply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_risk_pre_supply_commit, R.id.iv_risk_pre_add_front, R.id.iv_risk_pre_add_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_risk_pre_supply_commit://点击了提交
                break;
            case R.id.iv_risk_pre_add_front:
            case R.id.iv_risk_pre_add_back:

                break;
        }
    }

}
