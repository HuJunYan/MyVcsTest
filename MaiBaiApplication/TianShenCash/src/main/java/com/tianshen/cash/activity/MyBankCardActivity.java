package com.tianshen.cash.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.BankCardInfoAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.GetBankListBean;
import com.tianshen.cash.net.api.GetBindBankList;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TelephoneScreenChangeUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyBankCardActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_bank_list;
    private Button bt_remove_binding;
    private RelativeLayout rl_add_bank_card;
    private Dialog dialog;
    private GetBankListBean bankListBean;
    private IntentFilter intentFilter;
    private BankRemoveBindReceiver bankRemoveBindReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewType(false);
        initBankInfo();
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.REMOVEBIND_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.BIND_CARD_SUCCESS_ACTION);
        //创建NetWorkChangeReceiver的实例，并调用registerReceiver()方法进行注册
        bankRemoveBindReceiver = new BankRemoveBindReceiver();
        registerReceiver(bankRemoveBindReceiver, intentFilter);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_my_bank_card;
    }

    @Override
    protected void findViews() {
        lv_bank_list = (ListView) findViewById(R.id.lv_bank_list);
        bt_remove_binding = (Button) findViewById(R.id.bt_remove_binding);
        rl_add_bank_card = (RelativeLayout) findViewById(R.id.rl_add_bank_card);
    }

    @Override
    protected void setListensers() {
        bt_remove_binding.setOnClickListener(this);
        rl_add_bank_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_remove_binding:
                //解除绑定
                showRemoveBindDialog();
                break;
            case R.id.rl_add_bank_card:
                //绑定银行卡
                bindBankCard();
                break;
        }
    }

    private void initBankInfo() {
        //执行获取银行卡列表的方法
        GetBindBankList getBindBankList = new GetBindBankList(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getBindBankList.getBindBankList(jsonObject, null, true, new BaseNetCallBack<GetBankListBean>() {
                @Override
                public void onSuccess(GetBankListBean paramT) {
                    bankListBean = paramT;
                    if (bankListBean.getData().size() > 0) {
                        setViewType(true);
                        BankCardInfoAdapter adapter = new BankCardInfoAdapter(mContext, bankListBean);
                        lv_bank_list.setAdapter(adapter);
                    } else {
                        setViewType(false);
                    }
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

    private void setViewType(boolean isHadCard) {
        if (isHadCard) {
            lv_bank_list.setVisibility(View.VISIBLE);
            bt_remove_binding.setVisibility(View.VISIBLE);
            rl_add_bank_card.setVisibility(View.GONE);
        } else {
            lv_bank_list.setVisibility(View.GONE);
            bt_remove_binding.setVisibility(View.GONE);
            rl_add_bank_card.setVisibility(View.VISIBLE);
        }
    }

    private void bindBankCard() {
        //绑定银行卡
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.REPAY_FROM_KEY, "bindBankCard");//用户主动绑定银行卡，不付款
        gotoActivity(mContext, AddBankCardActivity.class, bundle);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showRemoveBindDialog() {
        dialog = new AlertDialog.Builder(mContext, R.style.remove_bind_dialog).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_remove_bind, null);
        LinearLayout ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
        LinearLayout ll_confirm = (LinearLayout) view.findViewById(R.id.ll_confirm);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephoneScreenChangeUtils telephoneScreenChangeUtils = new TelephoneScreenChangeUtils(mContext);
                telephoneScreenChangeUtils.changeLight();
                dialog.dismiss();
            }
        });
        ll_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephoneScreenChangeUtils telephoneScreenChangeUtils = new TelephoneScreenChangeUtils(mContext);
                telephoneScreenChangeUtils.changeLight();
                dialog.dismiss();
                if (bankListBean == null || bankListBean.getData().size() == 0) {
                    return;
                }
                Bundle mBundle = new Bundle();
                mBundle.putString("intent", "unBindCard");
                mBundle.putString("bank_card", bankListBean.getData().get(0).getCard_num());
                gotoActivity(mContext, InputPayPwdActivity.class, mBundle);
            }
        });
        dialog.setCancelable(false);
        TelephoneScreenChangeUtils telephoneScreenChangeUtils = new TelephoneScreenChangeUtils(mContext);
        telephoneScreenChangeUtils.changeDark();
        dialog.show();
        dialog.setContentView(view);
    }

    private class BankRemoveBindReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case GlobalParams.BIND_CARD_SUCCESS_ACTION:
                case GlobalParams.REMOVEBIND_SUCCESS_ACTION:
                    bankListBean = null;
                    initBankInfo();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bankRemoveBindReceiver);
    }
}
