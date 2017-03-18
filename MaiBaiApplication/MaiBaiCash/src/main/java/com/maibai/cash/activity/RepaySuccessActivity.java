package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.base.MyApplication;
import com.maibai.cash.net.base.BaseUiListener;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.ShareUtils;
import com.tencent.tauth.Tencent;

public class RepaySuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_tip;
    private Button bt_confirm;
    private ImageView iv_share_qq, iv_share_qzone;
    final String APP_ID = "1105551302";
    Tencent mTencent;
    BaseUiListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double maxAmount = Double.parseDouble(UserUtil.getMaxAmount(mContext)) / 10000;
        tv_tip.setText("每月还款，我们会逐渐提高您的额度，最高" + maxAmount + "万元。");
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        listener = new BaseUiListener(mContext);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_repay_success;
    }

    @Override
    protected void findViews() {
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        iv_share_qzone = (ImageView) findViewById(R.id.iv_share_qzone);
        iv_share_qq = (ImageView) findViewById(R.id.iv_share_qq);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        iv_share_qzone.setOnClickListener(this);
        iv_share_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
                break;
            case R.id.iv_share_qzone:
                new ShareUtils(this).shareQZone(mTencent, listener);
                break;
            case R.id.iv_share_qq:
                new ShareUtils(this).shareToQQ(mTencent, listener);
                break;
            default:
                break;
        }
    }
}
