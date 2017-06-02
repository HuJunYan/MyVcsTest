package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.BorrowBillAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.GotoSJDActivityEvent;
import com.tianshen.cash.model.ConsumeItemBean;
import com.tianshen.cash.model.WithdrawalsRecordBean;
import com.tianshen.cash.model.WithdrawalsRecordItemBean;
import com.tianshen.cash.net.api.GetWithdrawalsRecord;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.view.XListView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
* 借款记录
* */
public class ConsumptionRecordActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private final int STOP_REFRESH = 3;
    private XListView lv_consumption_record;
    private List<ConsumeItemBean> consumeItemBeanList = new ArrayList<ConsumeItemBean>();
    private BorrowBillAdapter borrowBillAdapter;
    private List<WithdrawalsRecordItemBean> withdrawalsRecordItemBeanList = new ArrayList<WithdrawalsRecordItemBean>();
    private TextView tv_pay_bill, tv_pay_line, tv_borrow_bill, tv_borrow_line;
    private RelativeLayout rl_pay, rl_borrow;
    private boolean borrow_bill_init = true;
    private int type = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case STOP_REFRESH:
                    lv_consumption_record.stopRefresh();
                    lv_consumption_record.stopLoadMore();
                    lv_consumption_record.setRefreshTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv_consumption_record.setPullRefreshEnable(true);
        lv_consumption_record.setPullLoadEnable(true);
        updateTitle();
    }

    private void updateTitle() {
        switch (type) {
            case 1:
                tv_pay_bill.setTextColor(ContextCompat.getColor(mContext, R.color.gray_7272));
                tv_pay_line.setVisibility(View.INVISIBLE);
                tv_borrow_bill.setTextColor(ContextCompat.getColor(mContext, R.color.green_text));
                tv_borrow_line.setVisibility(View.VISIBLE);
                tv_borrow_line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_text));
                borrowBillAdapter = new BorrowBillAdapter(mContext, withdrawalsRecordItemBeanList);
                lv_consumption_record.setAdapter(borrowBillAdapter);
                if (borrow_bill_init) {
                    getBorrowBill(true);
                }
                break;
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_consumption_record;
    }

    @Override
    protected void findViews() {
        lv_consumption_record = (XListView) findViewById(R.id.lv_consumption_record);
        tv_pay_bill = (TextView) findViewById(R.id.tv_pay_bill);
        tv_pay_line = (TextView) findViewById(R.id.tv_pay_line);
        tv_borrow_bill = (TextView) findViewById(R.id.tv_borrow_bill);
        tv_borrow_line = (TextView) findViewById(R.id.tv_borrow_line);
        rl_pay = (RelativeLayout) findViewById(R.id.rl_pay);
        rl_borrow = (RelativeLayout) findViewById(R.id.rl_borrow);
    }

    @Override
    protected void setListensers() {
        lv_consumption_record.setXListViewListener(this);
        rl_pay.setOnClickListener(this);
        rl_borrow.setOnClickListener(this);
    }


    private void getBorrowBill(final boolean isClear) {

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            jsonObject.put("offset", isClear ? "0" : withdrawalsRecordItemBeanList.size() + "");
            jsonObject.put("length", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH);
            GetWithdrawalsRecord getWithdrawalsRecord = new GetWithdrawalsRecord(mContext);
            getWithdrawalsRecord.getWithdrawalsBill(jsonObject, null, true, new BaseNetCallBack<WithdrawalsRecordBean>() {
                @Override
                public void onSuccess(WithdrawalsRecordBean paramT) {
                    if (isClear) {
                        withdrawalsRecordItemBeanList.clear();
                    }
                    withdrawalsRecordItemBeanList.addAll(paramT.getData().getList());
                    borrowBillAdapter.notifyDataSetChanged();
                    borrow_bill_init = false;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(STOP_REFRESH);
                        }
                    }, 1500);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getBorrowBill(true);
    }

    @Override
    public void onLoadMore() {
        getBorrowBill(false);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pay:
                type = 0;
                updateTitle();
                break;
            case R.id.rl_borrow:
                type = 1;
                updateTitle();
                break;
        }
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onGotoSJDActivityEvent(GotoSJDActivityEvent event) {
        String sjd_url = event.getSjd_url();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, sjd_url);
        bundle.putBoolean(GlobalParams.SJD_BACK_DELAY_KEY, false);
        gotoActivity(mContext, SJDActivity.class, bundle);
        finish();
    }

}
