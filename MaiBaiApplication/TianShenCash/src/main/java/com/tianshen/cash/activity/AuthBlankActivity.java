package com.tianshen.cash.activity;

import android.os.Bundle;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;

/**
 * 绑定收款银行卡界面
 */
public class AuthBlankActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_blank);
    }

    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }
}