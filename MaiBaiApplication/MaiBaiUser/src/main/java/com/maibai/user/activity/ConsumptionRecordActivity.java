package com.maibai.user.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.adapter.BorrowBillAdapter;
import com.maibai.user.adapter.PayBillAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ConsumeItemBean;
import com.maibai.user.model.ConsumeListBean;
import com.maibai.user.model.OrderRealPayBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.model.WithdrawalsRecordBean;
import com.maibai.user.model.WithdrawalsRecordItemBean;
import com.maibai.user.net.api.GetConsumeList;
import com.maibai.user.net.api.GetWithdrawalsRecord;
import com.maibai.user.net.api.OrderCancel;
import com.maibai.user.net.api.OrderQuery;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.view.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
* 消费记录
* */
public class ConsumptionRecordActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener, PayBillAdapter.ClickFunction {

    private final int STOP_REFRESH = 3;
    private final int DELAY_QUERY_ORDER_STATE = 4;
    private int mLastClickPosition = 0;
    private int mQueryTimes = 0;
    private XListView lv_consumption_record;
    private List<ConsumeItemBean> consumeItemBeanList = new ArrayList<ConsumeItemBean>();
    private PayBillAdapter payBilladapter;
    private BorrowBillAdapter borrowBillAdapter;
    private ConsumeListBean mConsumeListBean;
    private List<WithdrawalsRecordItemBean> withdrawalsRecordItemBeanList=new ArrayList<WithdrawalsRecordItemBean>();
    private TextView tv_pay_bill,tv_pay_line,tv_borrow_bill,tv_borrow_line;
    private RelativeLayout rl_pay,rl_borrow;
    private boolean pay_bill_init=true;
    private boolean borrow_bill_init=true;
    private int type=0;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case STOP_REFRESH:
                    lv_consumption_record.stopRefresh();
                    lv_consumption_record.stopLoadMore();
                    lv_consumption_record.setRefreshTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                    break;
                case DELAY_QUERY_ORDER_STATE:
                    mQueryTimes++;
                    if (mQueryTimes < 3) {
                        putOrder(mLastClickPosition);
                    }
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

    private void updateTitle(){
        switch (type){
            case 0:
                tv_pay_bill.setTextColor(ContextCompat.getColor(mContext,R.color.green_text));
                tv_pay_line.setVisibility(View.VISIBLE);
                tv_pay_line.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green_text));
                tv_borrow_bill.setTextColor(ContextCompat.getColor(mContext,R.color.gray_7272));
                tv_borrow_line.setVisibility(View.INVISIBLE);
                payBilladapter = new PayBillAdapter(mContext, consumeItemBeanList, this);
                lv_consumption_record.setAdapter(payBilladapter);
                if(pay_bill_init){
                    getPayBill(true);
                }
                break;
            case 1:
                tv_pay_bill.setTextColor(ContextCompat.getColor(mContext,R.color.gray_7272));
                tv_pay_line.setVisibility(View.INVISIBLE);
                tv_borrow_bill.setTextColor(ContextCompat.getColor(mContext,R.color.green_text));
                tv_borrow_line.setVisibility(View.VISIBLE);
                tv_borrow_line.setBackgroundColor(ContextCompat.getColor(mContext,R.color.green_text));
                borrowBillAdapter=new BorrowBillAdapter(mContext,withdrawalsRecordItemBeanList);
                lv_consumption_record.setAdapter(borrowBillAdapter);
                if(borrow_bill_init){
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
        tv_pay_bill=(TextView)findViewById(R.id.tv_pay_bill);
        tv_pay_line=(TextView)findViewById(R.id.tv_pay_line);
        tv_borrow_bill=(TextView)findViewById(R.id.tv_borrow_bill);
        tv_borrow_line=(TextView)findViewById(R.id.tv_borrow_line);
        rl_pay=(RelativeLayout)findViewById(R.id.rl_pay);
        rl_borrow=(RelativeLayout)findViewById(R.id.rl_borrow);
    }

    @Override
    protected void setListensers() {
        lv_consumption_record.setXListViewListener(this);
        rl_pay.setOnClickListener(this);
        rl_borrow.setOnClickListener(this);
    }

    private void getPayBill(final boolean isClear) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("offset", isClear ? "0" : consumeItemBeanList.size() + "");
            jsonObject.put("length", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH);
            GetConsumeList getConsumeList = new GetConsumeList(mContext);
            getConsumeList.getConsumeList(jsonObject, null, true, new BaseNetCallBack<ConsumeListBean>() {
                @Override
                public void onSuccess(ConsumeListBean paramT) {
                    mConsumeListBean = paramT;
                    if (isClear) {
                        consumeItemBeanList.clear();
                    }
                    consumeItemBeanList.addAll(paramT.getData());
                    payBilladapter.notifyDataSetChanged();
                    pay_bill_init=false;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(STOP_REFRESH);
                        }
                    }, 1500);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(STOP_REFRESH);
                        }
                    }, 1500);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void getBorrowBill(final boolean isClear){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("offset", isClear ? "0" : withdrawalsRecordItemBeanList.size() + "");
            jsonObject.put("length", GlobalParams.CONSUMPTIONRECORD_LOAD_LENGTH);
            GetWithdrawalsRecord getWithdrawalsRecord=new GetWithdrawalsRecord(mContext);
            getWithdrawalsRecord.getWithdrawalsBill(jsonObject, null, true, new BaseNetCallBack<WithdrawalsRecordBean>() {
                @Override
                public void onSuccess(WithdrawalsRecordBean paramT) {
                    if (isClear) {
                        withdrawalsRecordItemBeanList.clear();
                    }
                    withdrawalsRecordItemBeanList.addAll(paramT.getData());
                    borrowBillAdapter.notifyDataSetChanged();
                    borrow_bill_init=false;
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
        if("0".equals(type)) {
            getPayBill(true);
        }else{
            getBorrowBill(true);
        }
    }

    @Override
    public void onLoadMore() {
        if("0".equals(type)) {
            getPayBill(false);
        }else{
            getBorrowBill(false);
        }
    }

    @Override
    public void clickCancel(int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", consumeItemBeanList.get(position).getConsume_id());
            new OrderCancel(mContext).orderCancel(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    getPayBill(true);
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
    public void clickConfirmOrder(int position) {
        putOrder(position);
    }

    @Override
    public void clickPayDown(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalParams.APPLY_TYPE_KEY,GlobalParams.APPLY_TYPE_INSTALLMENT);
        bundle.putString(GlobalParams.REPAY_FROM_KEY,GlobalParams.REPAY_FROM_SHOUFU);
        bundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_CONSUM);
        bundle.putSerializable(GlobalParams.CONSUMEITEMBEAN_KEY, consumeItemBeanList.get(position));
        bundle.putSerializable(GlobalParams.CONSUMELISTBEAN_KEY, mConsumeListBean);
        gotoActivity(mContext, VerifySuccessActivity.class, bundle);
    }

    private void putOrder(int position) {
        try {
            mLastClickPosition = position;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", consumeItemBeanList.get(position).getConsume_id());
            new OrderQuery(mContext).orderQuery(jsonObject, null, true, new BaseNetCallBack<OrderRealPayBean>() {
                @Override
                public void onSuccess(OrderRealPayBean paramT) {
                    if ("2".equals(paramT.getData().getState())) {
                        handler.sendEmptyMessageDelayed(DELAY_QUERY_ORDER_STATE, 1000);
                    } else {
                        UserUtil.setStatus(mContext, GlobalParams.HAVE_BEEN_VERIFY);
                        new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION, null);
                        getPayBill(true);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isSuccess", false);
                    bundle.putString("error_result", url);
                    gotoActivity(mContext, PaySucNextMonthActivity.class, bundle);
                    backActivity();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_pay:
                type=0;
                updateTitle();
                break;
            case R.id.rl_borrow:
                type=1;
                updateTitle();
                break;
        }
    }

}
