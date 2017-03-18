package com.maibai.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.adapter.CreditStatusAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.JpushLotOfVerifyStatusBean;
import com.maibai.user.model.OrderBean;
import com.maibai.user.model.OrderRealPayBean;
import com.maibai.user.model.OrderRefreshBean;
import com.maibai.user.model.WithdrawalsRefreshBean;
import com.maibai.user.net.api.GetOrderRefresh;
import com.maibai.user.net.api.OrderQuery;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WaitVerifyPayActivity extends BaseActivity implements View.OnClickListener, CreditStatusAdapter.AgainVerifyInterface {
    /*消费--等待审核界面*/
    private ListView lv_verify_status;
    private TextView tv_apply_value;
    private TextView tv_merchant_name;
    private TextView tv_server_num;
    private TextView tv_time;
    private OrderRefreshBean mOrderRefreshBean;
    private Button bt_refresh;
    private Bundle mBundle;
    private final int DELAY_QUERY_ORDER_STATE = 1;
    private final int DELAY_ORDER_REFRESH=2;
    private int mQueryTimes = 0;
    private VerifyBordcast mVerifyBordcast;
    private String consumeId = "";
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            try {
                switch (message.what) {
                    case DELAY_QUERY_ORDER_STATE:
                        mQueryTimes++;
                        if (mQueryTimes >= 3) {
                            ToastUtil.showToast(mContext, "请到付款列表查看买单结果！");
                        } else {
                            orderRealPay(mOrderRefreshBean);
                        }
                        break;
                    case DELAY_ORDER_REFRESH:
                        handleVerfyResult();
                        break;
                }
            } catch (Exception e) {
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (null != mBundle) {
            consumeId = mBundle.getString(GlobalParams.CONSUME_ID_KEY);
        }
        registBordcast();
        handleVerfyResult();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_wait_vertify_pay;
    }

    @Override
    protected void findViews() {
        lv_verify_status = (ListView) findViewById(R.id.lv_verify_status);
        tv_apply_value = (TextView) findViewById(R.id.tv_apply_value);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        tv_server_num = (TextView) findViewById(R.id.tv_server_num);
        tv_time = (TextView) findViewById(R.id.tv_time);
        bt_refresh = (Button) findViewById(R.id.bt_refresh);
    }

    @Override
    protected void setListensers() {
        bt_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_refresh:
                handleVerfyResult();
                break;
        }
    }

    private void handleVerfyResult() {
        GetOrderRefresh getOrderRefresh = new GetOrderRefresh(mContext);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("consume_id", consumeId);
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getOrderRefresh.getOrderRefresh(jsonObject, bt_refresh, false, new BaseNetCallBack<OrderRefreshBean>() {
                @Override
                public void onSuccess(OrderRefreshBean paramT) {
                    mOrderRefreshBean = paramT;
                    handleRefreshData(paramT);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Log.d("ret","orderRefreshError");
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void handleRefreshData(OrderRefreshBean paramT) {
        try {
            if (null == paramT.getData()) {
                return;
            }
            String amount = paramT.getData().getAmount();
            if ("".equals(amount) || null == amount) {
                amount = "0";
            }
            tv_apply_value.setText(Double.valueOf(amount) / 100 + "元");
            String merchantName = paramT.getData().getMerchant_name();
            if (null == merchantName) {
                merchantName = "";
            }
            tv_merchant_name.setText(merchantName);
            String orderNum = paramT.getData().getOrder_id();
            if (null == orderNum) {
                orderNum = "";
            }
            tv_server_num.setText(orderNum);
            String orderTime = paramT.getData().getAdd_time();
            if (null == orderTime) {
                orderTime = "";
            }
            tv_time.setText(orderTime);
            String statusStr = paramT.getData().getStatus();
            UserUtil.setStatus(mContext, statusStr);
            switch (statusStr) {
                case GlobalParams.WAIT_PAY_SHOUFU:
                    gotoDownPaymentPage(paramT);
                    break;
                case GlobalParams.WAIT_VERIFY:
                    updateStatusView(paramT);
                    if ("2".equals(paramT.getData().getErr_code())) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(DELAY_ORDER_REFRESH);
                            }
                        }, 10000);
                    }
//                    ToastUtil.showToast(mContext, "买呗小妹正努力审核，请稍等");
                    break;
                case GlobalParams.REFUSE_BY_PERSON:
                    gotoVerifyFailActivity(mContext, GlobalParams.REFUSE_BY_PERSON_TYPE, paramT.getData().getReason());
                    backActivity();
                    break;
                case GlobalParams.REFUSE_BY_MACHINE:
                    gotoVerifyFailActivity(mContext, GlobalParams.REFUSE_BY_MACHINE_TYPE, paramT.getData().getReason());
                    backActivity();
                    break;
                case GlobalParams.HAVE_BEEN_VERIFY:
                    orderRealPay(paramT);
                    break;
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void updateStatusView(OrderRefreshBean param) {
        CreditStatusAdapter adapter = new CreditStatusAdapter(mContext, param);
        lv_verify_status.setAdapter(adapter);
    }

    private void orderRealPay(OrderRefreshBean paramT) throws JSONException {
        OrderQuery orderRealPay = new OrderQuery(mContext);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customer_id", UserUtil.getId(mContext));
        jsonObject.put("consume_id", paramT.getData().getConsume_id());
        orderRealPay.orderQuery(jsonObject, null, true, new BaseNetCallBack<OrderRealPayBean>() {
            @Override
            public void onSuccess(OrderRealPayBean paramT) {
                if ("2".equals(paramT.getData().getState())) {
                    handler.sendEmptyMessageDelayed(DELAY_QUERY_ORDER_STATE, 1000);
                } else {
                    UserUtil.setStatus(mContext, GlobalParams.HAVE_BEEN_VERIFY);
                    Bundle bundle = new Bundle();
                    OrderBean orderBean = Utils.orderRealPayBean2OrderBean(paramT);

                    bundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, orderBean);
                    bundle.putBoolean("isSuccess", true);
                    if ("1".equals(paramT.getData().getRepay_type())) {
                        gotoActivity(mContext, PaySucNextMonthActivity.class, bundle);
                    } else if ("2".equals(paramT.getData().getRepay_type())) {
                        gotoActivity(mContext, PaySuccessActivity.class, bundle);
                    } else if ("3".equals(paramT.getData().getRepay_type())) {
                        new SendBroadCastUtil(mContext).sendBroad(GlobalParams.VERIFY_SUCCESS_ACTION, null);
                        ToastUtil.showToast(mContext, "提高额度申请审核成功");
                        ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
                    }
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void gotoDownPaymentPage(OrderRefreshBean paramT) {
        UserUtil.setDownPayment(mContext, paramT.getData().getDown_payment());
        UserUtil.setConsumeId(mContext, paramT.getData().getConsume_id());
        UserUtil.setBindCard(mContext, paramT.getData().getBind_card());
        if ("1".equals(paramT.getData().getBind_card())) {
            UserUtil.setCardNum(mContext, paramT.getData().getCard_num());
            UserUtil.setBankName(mContext, paramT.getData().getBank_name());
        }
        mBundle.putSerializable(GlobalParams.ORDER_REFRESH_BEAN_KEY, paramT);
        mBundle.putInt(GlobalParams.APPLY_TYPE_KEY, mBundle.getInt(GlobalParams.APPLY_TYPE_KEY));
        mBundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_REFRESH_OR_PUSH);
        gotoActivity(mContext, VerifySuccessActivity.class, mBundle);
        backActivity();
    }

    private void gotoVerifyFailActivity(Context mContext, int refuseByPerson, String reason) {
        UserUtil.setReason(mContext, reason);
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalParams.REFUSE_TYPE_KEY, refuseByPerson);
        gotoActivity(mContext, VerifyFailActivity.class, bundle);
    }

    private void registBordcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.LOT_OF_VERIFY_STATUE_ACTION);
        intentFilter.addAction(GlobalParams.WX_SHOUFU_SUCCESS);
        intentFilter.addAction(GlobalParams.VERIFY_FINISHED_ACTION);
        mVerifyBordcast = new VerifyBordcast();
        registerReceiver(mVerifyBordcast, intentFilter);
    }

    @Override
    public void againSuccess() {
        handleVerfyResult();
    }

    public class VerifyBordcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                switch (action) {
                    case GlobalParams.LOT_OF_VERIFY_STATUE_ACTION:
                        Bundle bundle = intent.getExtras();
                        JpushLotOfVerifyStatusBean jpushLotOfVerifyStatusBean = (JpushLotOfVerifyStatusBean) (bundle.getSerializable(GlobalParams.LOT_OF_VERIFY_STATUE_KEY));
                        consumeId = jpushLotOfVerifyStatusBean.getMsg_content().getConsume_id();
                        handleVerfyResult();
                        break;
                    case GlobalParams.WX_SHOUFU_SUCCESS:
                        backActivity();
                        break;
                    case GlobalParams.VERIFY_FINISHED_ACTION:
                        Bundle bundle1 = intent.getExtras();
                        OrderRefreshBean orderRefreshBean = (OrderRefreshBean) (bundle1.getSerializable(GlobalParams.VERIFY_FINISHED_KEY));
                        handleRefreshData(orderRefreshBean);
                        break;
                }

            } catch (Exception e) {
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mVerifyBordcast);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 所有需要统一处理的onKeyDown写在这个if里面
        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
            }
        }
        return false;
    }

    @Override
    protected boolean isOnKeyDown(){
        return false;
    }
}
