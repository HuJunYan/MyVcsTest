package com.tianshen.cash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.WithdrawalsRefreshBean;
import com.tianshen.cash.net.api.WithdrawalsRefresh;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.WithdrawalsApplyResultUtil;
import com.tianshen.cash.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitVerifyWithdrawalActivity extends BaseActivity implements View.OnClickListener {
    /*
    * 提现--等待审核洁面
    * */

    private Button bt_confirm;
    private VerifyBordcast mVerifyBordcast;
    private TitleBar tb_title;

    private Bundle mBundle;
    private int type = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registBordcast();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            type = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY, -1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_wait_vertify;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        tb_title.setIvLeftVisible(View.INVISIBLE);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                switch (type) {
                    case GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY:
                        refreshWithdrawals();
                        break;
                }
        }
    }

    private void refreshWithdrawals() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            jsonObject.put("consume_id", mBundle.getString(GlobalParams.WITHDRAWALS_VERIFY_ID_KEY));
            WithdrawalsRefresh withdrawalsRefresh = new WithdrawalsRefresh(mContext);
            withdrawalsRefresh.selWithdrawals(jsonObject, null, true, new BaseNetCallBack<WithdrawalsRefreshBean>() {
                @Override
                public void onSuccess(WithdrawalsRefreshBean paramT) {
                    handleRefreshWithrawalsData(paramT);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void handleRefreshWithrawalsData(WithdrawalsRefreshBean paramT) {
        switch (paramT.getData().getStatus()) {
            case GlobalParams.CASH_APPLY_HAVE_BEEN_VERIFY:
                String amount = paramT.getData().getAmount();
                if ("".equals(amount) || null == amount) {
                    amount = "0";
                }
                new WithdrawalsApplyResultUtil(mContext).showSuccessDialog((int) (Double.valueOf(amount) / 100) + "");
                break;
            case GlobalParams.REFUSE_BY_PERSON:
                new WithdrawalsApplyResultUtil(mContext).showFailDialog(paramT.getData().getReason(), false);
                break;
            case GlobalParams.REFUSE_BY_MACHINE:
                new WithdrawalsApplyResultUtil(mContext).showFailDialog(paramT.getData().getReason(), true);
                tb_title.setIvLeftVisible(View.GONE);
                break;
        }
    }


    private void registBordcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION);
        mVerifyBordcast = new VerifyBordcast();
        registerReceiver(mVerifyBordcast, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mVerifyBordcast);
    }

    public class VerifyBordcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                switch (action) {
                    case GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION: {
                        Bundle bundle = intent.getExtras();
                        WithdrawalsRefreshBean withdrawalsRefreshBean = (WithdrawalsRefreshBean) (bundle.getSerializable(GlobalParams.WITHDRAWALS_VERIFY_FINISH_KEY));
                        handleRefreshWithrawalsData(withdrawalsRefreshBean);
                    }
                    break;
                }

            } catch (Exception e) {
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                backActivity();
            }
        }
        return false;
    }

    @Override
    protected boolean isOnKeyDown() {
        return false;
    }
}
