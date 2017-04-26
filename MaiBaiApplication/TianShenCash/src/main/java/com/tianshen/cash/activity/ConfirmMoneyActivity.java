package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.ApplyEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.OrderConfirmBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.net.api.GetOrderConfirm;
import com.tianshen.cash.net.api.Order;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.SafeUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 确认借款页面
 */

public class ConfirmMoneyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_confirm_money_back)
    TextView tvConfirmMoneyBack;
    @BindView(R.id.tv_confirm_withdrawal_key)
    TextView tvConfirmWithdrawalKey;
    @BindView(R.id.tv_confirm_withdrawal)
    TextView tvConfirmWithdrawal;
    @BindView(R.id.tv_confirm_time_key)
    TextView tvConfirmTimeKey;
    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    @BindView(R.id.tv_confirm_procedures_key)
    TextView tvConfirmProceduresKey;
    @BindView(R.id.tv_confirm_procedures)
    TextView tvConfirmProcedures;
    @BindView(R.id.tv_confirm_transfer_key)
    TextView tvConfirmTransferKey;
    @BindView(R.id.tv_confirm_transfer)
    TextView tvConfirmTransfer;
    @BindView(R.id.tv_confirm_banck_card_key)
    TextView tvConfirmBanckCardKey;
    @BindView(R.id.tv_confirm_banck_card)
    TextView tvConfirmBanckCard;
    @BindView(R.id.tv_confirm_repay_key)
    TextView tvConfirmRepayKey;
    @BindView(R.id.tv_confirm_repay)
    TextView tvConfirmRepay;
    @BindView(R.id.tv_confirm_apply)
    TextView tvConfirmApply;
    @BindView(R.id.tv_confirm_protocol)
    TextView tvConfirmProtocol;

    @BindView(R.id.ll_wait_pay)
    LinearLayout ll_wait_pay;

    private int requests_number = 0;


    private OrderConfirmBean mOrderConfirmBean;

    private boolean mIsTimeOut;


    private static final int MSG_ORDER_DATA = 1;
    private static final int SHOW_ORDER_TIME = 10 * 1000;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_ORDER_DATA:
                    requests_number++;
                    if (requests_number == 4) {
                        mHandler.removeMessages(MSG_ORDER_DATA);
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        gotoActivity(mContext, MainActivity.class, null);
                    } else {
                        initOrderConfirmData();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrderConfirmData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_money;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmApply.setOnClickListener(this);
        tvConfirmProtocol.setOnClickListener(this);
    }

    /**
     * 得到确认借款数据
     */
    private void initOrderConfirmData() {

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);

            String consume_amount = TianShenUserUtil.getUserConsumeAmount(mContext);
            String repay_id = TianShenUserUtil.getUserRepayId(mContext);
            jsonObject.put("customer_id", userId);
            jsonObject.put("repay_id", repay_id);
            jsonObject.put("consume_amount", consume_amount);
            jsonObject.put("requests_number", requests_number);

            final GetOrderConfirm getOrderConfirm = new GetOrderConfirm(mContext);
            getOrderConfirm.getOrderConfirm(jsonObject, new BaseNetCallBack<OrderConfirmBean>() {
                @Override
                public void onSuccess(OrderConfirmBean bean) {
                    mOrderConfirmBean = bean;
                    refreshUI();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_confirm_apply:
                onClickApply();
                break;
            case R.id.tv_confirm_protocol:
                gotoWebActivity();
                break;
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (mOrderConfirmBean == null) {
            return;
        }

        String type = mOrderConfirmBean.getData().getType();
        String reportauth = mOrderConfirmBean.getData().getReportauth();
        String consume_amount = mOrderConfirmBean.getData().getConsume_amount();

        if ("1".equals(type)) { //掌众
            if ("1".equals(reportauth)) { //是否授信，1为授信，0没有授信
                if (!TextUtils.isEmpty(consume_amount)) {
                    showNormalUI();

                    //存储ID
                    User user = TianShenUserUtil.getUser(mContext);
                    String repayId = mOrderConfirmBean.getData().getRepay_id();
                    user.setRepay_id(repayId);
                    TianShenUserUtil.saveUser(mContext, user);
                    return;
                }
            }
            showPayWaitUI();
        } else {
            showNormalUI();
        }

    }

    /**
     * 显示等待确认Ui
     */
    private void showPayWaitUI() {
        ll_wait_pay.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(MSG_ORDER_DATA, SHOW_ORDER_TIME);
    }

    /**
     * 显示正常的UI
     */
    private void showNormalUI() {
        ll_wait_pay.setVisibility(View.GONE);

        String consume_amount = mOrderConfirmBean.getData().getConsume_amount(); //用户申请金额
        String timer = mOrderConfirmBean.getData().getTimer();//借款时长
        String poundage = mOrderConfirmBean.getData().getPoundage();//手续费
        String amount = mOrderConfirmBean.getData().getAmount();//到账金额
        String bank_name = mOrderConfirmBean.getData().getBank_name();//绑定银行卡所属银行
        String id_num = mOrderConfirmBean.getData().getId_num();//绑定银行卡卡号
        String repayment_amout = mOrderConfirmBean.getData().getRepayment_amout();//到期还款金额
        try {
            String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
            String poundageY = MoneyUtils.changeF2Y(poundage);
            String amountY = MoneyUtils.changeF2Y(amount);
            String repaymentAmoutY = MoneyUtils.changeF2Y(repayment_amout);
            tvConfirmWithdrawal.setText(consume_amountY + "元");
            tvConfirmProcedures.setText(poundageY + "元");
            tvConfirmTransfer.setText(amountY + "元");
            tvConfirmRepay.setText(repaymentAmoutY + "元");
            tvConfirmTime.setText(timer + "天");
            String cardNum = SafeUtil.encodeBankCardNum(id_num);
            tvConfirmBanckCard.setText(bank_name + cardNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
//                 "customer_id": "（int）用户ID",
//                "type":"0为自己的产品，1为掌众的产品"
//                "consume_amount": "(int)提现金额,单位分",
//
//                "location": "用户坐标，格式：(string)当前用户所在的坐标值 如：123.4212128,45.098324 经度在前纬度在后英文逗号隔开",
//                "province": "用户消费地所在的省",
//                "city": "用户消费地所在的市没有传空字符",
//                "country": "用户消费地所在的区/县",
//                "address": "用户消费地所在的详细地址",
//                "black_box": "指纹黑盒数据，同盾指纹，android和ios必传，h5不传",
//                "push_id": "推送消息id，android必传，ios和h5不传",
//                "repay_id":"选择的产品id,如果是掌众写0,注意：该字段是selWithdrawals接口的data.id值"
        JSONObject jsonObject = new JSONObject();
        try {

            User user = TianShenUserUtil.getUser(mContext);

            String customer_id = user.getCustomer_id();
            String consume_amount = user.getConsume_amount();
            String repay_id = user.getRepay_id();
            String location = user.getLocation();
            String city = user.getCity();
            String country = user.getCountry();
            String address = user.getAddress();
            String province = user.getProvince();
            String jpush_id = user.getJpush_id();


            if (TextUtils.isEmpty(location)) {
                ToastUtil.showToast(mContext, "请先开打GPS,然后再下单。");
                return;
            }

            boolean payWayBySelf = TianShenUserUtil.isPayWayBySelf(mContext);
            String type;
            if (payWayBySelf) {
                type = "0";
            } else {
                type = "1";
            }
            String black_box = new GetTelephoneUtils(mContext).getBlackBox();

            jsonObject.put("customer_id", customer_id);
            jsonObject.put("type", type);
            jsonObject.put("consume_amount", consume_amount);
            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);
            jsonObject.put("black_box", black_box);
            jsonObject.put("push_id", jpush_id);
            jsonObject.put("repay_id", repay_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Order order = new Order(mContext);
        order.order(jsonObject, tvConfirmApply, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                if (paramT.getCode() == 0) {
                    EventBus.getDefault().post(new ApplyEvent());
                    gotoActivity(mContext, MainActivity.class, null);
                }
                mIsTimeOut = false;
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                mIsTimeOut = true;
                tvConfirmApply.setEnabled(false);
                tvConfirmMoneyBack.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        EventBus.getDefault().post(new TimeOutEvent());
                        gotoActivity(mContext, MainActivity.class, null);
                    }
                }, 2500);
            }
        });

    }

    /**
     * 跳转到WebActivity
     */
    private void gotoWebActivity() {
        String userPayProtocolURL = NetConstantValue.getUserPayProtocolURL();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, userPayProtocolURL);
        gotoActivity(mContext, WebActivity.class, bundle);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsTimeOut) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
