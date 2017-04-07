package com.tianshen.cash.activity;

import android.view.View;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.event.ApplyEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 确认银行卡页面
 */

public class ConfirmBankCardActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_bank_card;
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
            case R.id.tv_confirm_apply:
                onClickApply();
                break;
        }
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
    }

}
