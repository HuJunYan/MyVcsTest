package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.utils.ToastUtil;

/**
 * 银行卡信息
 */

public class AuthBankCardActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_bank_card;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_center_back:
                backActivity();
                break;
            case R.id.tv_auth_center_post:
                ToastUtil.showToast(mContext, "点击了提交");
                break;
        }
    }

    private void onClickItem(int position) {
    }


}
