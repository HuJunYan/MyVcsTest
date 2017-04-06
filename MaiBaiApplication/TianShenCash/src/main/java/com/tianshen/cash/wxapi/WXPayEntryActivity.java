package com.tianshen.cash.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.MainActivity;
import com.tianshen.cash.base.MyApplication;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.SendBroadCastUtil;
import com.tianshen.cash.view.TitleBar;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener, TitleBar.TitleBarListener {
    private IWXAPI api;
    private TextView tv_pay_result;
    private int type = GlobalParams.WX_PAY_CANCEL;
    private TitleBar tb_title;
    private String payFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        payFrom = getIntent().getStringExtra(GlobalParams.WX_PAY_EXTDATA_KEY);
        api = WXAPIFactory.createWXAPI(this, GlobalParams.APP_ID_WX_PAY);
        api.handleIntent(getIntent(), this);
        setContentView(R.layout.activity_wxpay_entry);
        initView();
    }

    public void initView() {
        Button bt_confirm = (Button) findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(this);
        tv_pay_result = (TextView) findViewById(R.id.tv_pay_result);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        tb_title.setListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            type = errCode;
            handler.sendEmptyMessage(errCode);
            if (GlobalParams.REPAY_FROM_SHOUFU.equals(payFrom) && type == GlobalParams.WX_PAY_SUCCESS) {
                new SendBroadCastUtil(WXPayEntryActivity.this).sendBroad(GlobalParams.WX_SHOUFU_SUCCESS, null);
                backFunction();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                backFunction();
                break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case GlobalParams.WX_PAY_SUCCESS:
                    tv_pay_result.setText("支付成功");
                    break;
                case GlobalParams.WX_PAY_FAIL:
                    tv_pay_result.setText("支付失败");
                    break;
                case GlobalParams.WX_PAY_CANCEL:
                    tv_pay_result.setText("支付取消");
                    break;
            }
        }
    };

    @Override
    public void onLeftClick(View view) {
        backFunction();
    }

    @Override
    public void onAddressClick(View view) {

    }

    @Override
    public void onRightClick(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backFunction();
    }
    private void backFunction() {
        switch (type) {
            case GlobalParams.WX_PAY_SUCCESS:
                if (GlobalParams.REPAY_FROM_CONSUMPTION.equals(payFrom)) {
                    try {
                        ((MyApplication) (getApplicationContext())).clearTempActivityInBackStack(MainActivity.class);
                    }catch (Exception e){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        ((Activity) this).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                    }
                    new SendBroadCastUtil(WXPayEntryActivity.this).sendBroad(GlobalParams.WX_REPAY_CONSUMPTION_SUCCESS_ACTION, null);
                } else if (GlobalParams.REPAY_FROM_BORROW.equals(payFrom)) {
                    new SendBroadCastUtil(WXPayEntryActivity.this).sendBroad(GlobalParams.WX_REPAY_BORROW_SUCCESS_ACTION, null);
                    ((MyApplication)(getApplicationContext())).clearTempActivityInBackStack(MainActivity.class);
                } else if (GlobalParams.REPAY_FROM_BORROW_DETAIL.equals(payFrom)) {
                    new SendBroadCastUtil(WXPayEntryActivity.this).sendBroad(GlobalParams.WX_REPAY_BORROW_DETAIL_SUCCESS_ACTION, null);
                    ((MyApplication)(getApplicationContext())).clearTempActivityInBackStack(MainActivity.class);
                }
            case GlobalParams.WX_PAY_FAIL:
            case GlobalParams.WX_PAY_CANCEL:
                finish();
                overridePendingTransition(R.anim.not_exit_push_left_in, R.anim.push_right_out);
                break;
        }
    }
}
