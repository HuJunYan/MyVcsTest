package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.WithdrawalsBillAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.WithdrawalsBillItemBean;
import com.tianshen.cash.model.WithdrawalsBillListBean;
import com.tianshen.cash.net.api.GetWithdrawalsBill;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WithdrawalsBillActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, WithdrawalsBillAdapter.Control {
    private CheckBox cb_select_all;
    private TextView tv_selected_amount;
    private TextView tv_repay;
    private XListView lv_bill;
    private final int LENTH = 20;//本页长度
    private final int STOP_REFRESH = 4;
    private Double totoalAmount = 0.0;//选中还款总数之
    private WithdrawalsBillAdapter adapter;
    private List<WithdrawalsBillItemBean> widthdrawalsBillItemList = new ArrayList<WithdrawalsBillItemBean>();
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case STOP_REFRESH:
                    lv_bill.stopLoadMore();
                    lv_bill.stopRefresh();
                    lv_bill.setRefreshTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(true);
        lv_bill.setPullRefreshEnable(true);
        lv_bill.setPullLoadEnable(true);
        adapter = new WithdrawalsBillAdapter(mContext, widthdrawalsBillItemList, this);
        lv_bill.setAdapter(adapter);
    }

    private void initRepayTotalAmount() {
        int position = -1;
        totoalAmount = 0.0;
        for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
            if (widthdrawalsBillItemList.get(i).isChecked()) {
                position = i;
                String repayAmount = widthdrawalsBillItemList.get(i).getRepay_amount();
                if ("".equals(repayAmount) || null == repayAmount) {
                    repayAmount = "0";
                }
                totoalAmount += Double.valueOf(repayAmount);
            }
        }
        tv_selected_amount.setText(Double.valueOf(totoalAmount) / 100 + "");
        if (position == widthdrawalsBillItemList.size() - 1) {
            cb_select_all.setOnCheckedChangeListener(null);
            cb_select_all.setChecked(true);
            cb_select_all.setOnCheckedChangeListener(this);
        } else {
            cb_select_all.setOnCheckedChangeListener(null);
            cb_select_all.setChecked(false);
            cb_select_all.setOnCheckedChangeListener(this);
        }
    }

    private void initData(final boolean isInit) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", TianShenUserUtil.getUserId(mContext));
            jsonObject.put("offset", isInit ? 0 : widthdrawalsBillItemList.size());
            jsonObject.put("length", LENTH + "");
            GetWithdrawalsBill getWithdrawalsBill = new GetWithdrawalsBill(mContext);
            getWithdrawalsBill.getWithdrawalsBill(jsonObject, null, true, new BaseNetCallBack<WithdrawalsBillListBean>() {
                @Override
                public void onSuccess(WithdrawalsBillListBean paramT) {
                    handler.sendEmptyMessage(STOP_REFRESH);
                    if (isInit) {
                        widthdrawalsBillItemList.clear();
                    }
                    widthdrawalsBillItemList.addAll(paramT.getData());
                    adapter.notifyDataSetChanged();

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

    @Override
    protected int setContentView() {
        return R.layout.activity_get_cash_bill;
    }

    @Override
    protected void findViews() {
        cb_select_all = (CheckBox) findViewById(R.id.cb_select_all);
        tv_selected_amount = (TextView) findViewById(R.id.tv_selected_amount);
        tv_repay = (TextView) findViewById(R.id.tv_repay);
        lv_bill = (XListView) findViewById(R.id.lv_bill);
    }

    @Override
    protected void setListensers() {
        tv_repay.setOnClickListener(this);
        cb_select_all.setOnCheckedChangeListener(this);
        lv_bill.setXListViewListener(this);
        lv_bill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            }
        });
    }

    @Override
    public void checkedAll(int position, boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
                if (i <= position) {
                    widthdrawalsBillItemList.get(i).setChecked(true);
                }
            }
        } else {
            for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
                if (i >= position) {
                    widthdrawalsBillItemList.get(i).setChecked(false);
                }
            }
        }
        adapter.notifyDataSetChanged();
        initRepayTotalAmount();
    }

    @Override
    public void gotoDetail(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("consume_id", widthdrawalsBillItemList.get(position).getConsume_id());
        bundle.putInt(GlobalParams.BILL_DETAIL_FROM_KEY, GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS);
        gotoActivity(mContext, WithdrawalsOrBillDetailActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_repay:
                if (totoalAmount == 0.0) {
                    ToastUtil.showToast(mContext, "请至少选择一项账单");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(GlobalParams.REPAY_FROM_KEY, GlobalParams.REPAY_FROM_BORROW);

                List<WithdrawalsBillItemBean> newWithdrawalsBillItemBean = new ArrayList<WithdrawalsBillItemBean>();
                for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
                    if (widthdrawalsBillItemList.get(i).isChecked()) {
                        newWithdrawalsBillItemBean.add(widthdrawalsBillItemList.get(i));
                    }
                }
                bundle.putSerializable(GlobalParams.REPAY_BEAN_KEY, (Serializable) newWithdrawalsBillItemBean);
                bundle.putString("totalAmount", Double.valueOf(totoalAmount) / 100 + "");
                gotoActivity(mContext, RePayActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
            widthdrawalsBillItemList.get(i).setChecked(isChecked);
        }
        adapter.notifyDataSetChanged();
        initRepayTotalAmount();
    }

    @Override
    public void onRefresh() {
        initData(true);
    }

    @Override
    public void onLoadMore() {
        initData(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
