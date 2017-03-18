package com.maibai.cash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.ConsumeItemBean;
import com.maibai.cash.model.ConsumeListBean;
import com.maibai.cash.model.OrderBean;
import com.maibai.cash.model.OrderRefreshBean;
import com.maibai.cash.model.WeChatOrder;
import com.maibai.cash.net.api.GetWeChatOrder;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenrongshang on 16/9/16.
 */
public class VerifySuccessActivity extends BaseActivity implements View.OnClickListener {
    private int mEnterType = 0;
    private final int DELAY_QUERY_ORDER_STATE = 1;
    private int mQueryTimes = 0;
    private String consume_id;
    private String down_payment;
    private String bind_card;
    private String card_num;
    private String bank_name;

    private Button bt_confirm;
    private TextView tv_early_stage_money;
    private TextView tv_early_stage_money2;
    private TextView tv_bank_name,tv_bank_card_title;
    private LinearLayout ll_repay_bank_card;
    private LinearLayout ll_repay_wechat;
    private ImageView iv_repay_by_bank_card;
    private ImageView iv_repay_by_wechat;
    private Bundle mBundle;
    private OrderRefreshBean mOrderRefreshBean;
    private ConsumeItemBean mConsumeItemBean;
    private OrderBean mOrderBean;
    private IWXAPI api;//微信支付
    private WXPayBroadCase wxPayBroadCast;
    private int type = GlobalParams.REPAY_BY_BANK_CARD;//还款方式


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mEnterType = mBundle.getInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY);
            switch (mEnterType) {
                case GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_SIGNIN:
                    consume_id = UserUtil.getConsumeId(mContext);
                    down_payment = UserUtil.getDownPayment(mContext);
                    bind_card = UserUtil.getBindCard(mContext);
                    card_num = UserUtil.getCardNum(mContext);
                    bank_name = UserUtil.getBankName(mContext);
                    break;
                case GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_PLACE_AN_ORDER:
                    mOrderBean = (OrderBean) mBundle.getSerializable(GlobalParams.ORDER_BEAN_KEY);
                    consume_id = mOrderBean.getData().getConsume_id();
                    down_payment = mOrderBean.getData().getDown_payment();
                    bind_card = mOrderBean.getData().getBind_card();
                    card_num = mOrderBean.getData().getCard_num();
                    bank_name = mOrderBean.getData().getBank_name();
                    break;
                case GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_REFRESH_OR_PUSH:
                    mOrderRefreshBean = (OrderRefreshBean) mBundle.getSerializable(GlobalParams.ORDER_REFRESH_BEAN_KEY);
                    consume_id = mOrderRefreshBean.getData().getConsume_id();
                    down_payment = mOrderRefreshBean.getData().getDown_payment();
                    bind_card = mOrderRefreshBean.getData().getBind_card();
                    card_num = mOrderRefreshBean.getData().getCard_num();
                    bank_name = mOrderRefreshBean.getData().getBank_name();
                    break;
                case GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_CONSUM:
                    ConsumeListBean mConsumeListBean=(ConsumeListBean)mBundle.getSerializable(GlobalParams.CONSUMELISTBEAN_KEY);
                    mConsumeItemBean=(ConsumeItemBean)mBundle.getSerializable(GlobalParams.CONSUMEITEMBEAN_KEY);
                    consume_id=mConsumeItemBean.getConsume_id();
                    down_payment=mConsumeItemBean.getDown_payment();
                    bind_card=mConsumeListBean.getBind_card();
                    card_num=mConsumeListBean.getCard_num();
                    bank_name=mConsumeListBean.getBank_name();
                    break;
            }
        }
        initWeChatPay();
        registeBroadCase();
        initCheck(type);
        initView();
    }

    private void initView(){
        if ("1".equals(bind_card)) {
            tv_bank_name.setText(bank_name +"("+card_num.substring(card_num.length()-4,card_num.length())+")");
            tv_bank_card_title.setText("已绑定银行卡");
        } else {
            tv_bank_name.setText("银行卡支付");
            tv_bank_card_title.setText("需要绑定银行卡");
        }
        if (down_payment == null || "".equals(down_payment)) {
            down_payment = "0";
        }
        tv_early_stage_money.setText(Double.parseDouble(down_payment)/100 + "");
        tv_early_stage_money2.setText("￥" + Double.parseDouble(down_payment)/100 + "");
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_verify_success;
    }
    private void initCheck(int type) {
        switch (type) {
            case GlobalParams.REPAY_BY_BANK_CARD:
                iv_repay_by_wechat.setImageResource(R.mipmap.unrouted);
                iv_repay_by_bank_card.setImageResource(R.mipmap.lift_ok);
                break;
            case GlobalParams.REPAY_BY_WECHAT:
                iv_repay_by_wechat.setImageResource(R.mipmap.lift_ok);
                iv_repay_by_bank_card.setImageResource(R.mipmap.unrouted);
                break;
        }
    }
    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tv_early_stage_money=(TextView)findViewById(R.id.tv_early_stage_money);
        tv_early_stage_money2=(TextView)findViewById(R.id.tv_early_stage_money2);
        ll_repay_bank_card=(LinearLayout)findViewById(R.id.ll_repay_bank_card);
        ll_repay_wechat=(LinearLayout)findViewById(R.id.ll_repay_wechat);
        iv_repay_by_bank_card=(ImageView)findViewById(R.id.iv_repay_by_bank_card);
        iv_repay_by_wechat=(ImageView)findViewById(R.id.iv_repay_by_wechat);
        tv_bank_name=(TextView)findViewById(R.id.tv_bank_name);
        tv_bank_card_title=(TextView)findViewById(R.id.tv_bank_card_title);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        ll_repay_bank_card.setOnClickListener(this);
        ll_repay_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                mBundle.putString(GlobalParams.REPAY_FROM_KEY,GlobalParams.REPAY_FROM_SHOUFU);
                mBundle.putInt(GlobalParams.APPLY_TYPE_KEY, mBundle.getInt(GlobalParams.APPLY_TYPE_KEY));
                mBundle.putString("consume_id",consume_id);
                mBundle.putString("down_payment",down_payment);
                if (type == GlobalParams.REPAY_BY_BANK_CARD) {
                    mBundle.putString("paytype",GlobalParams.BANK_PEY_TYPE);
                    if ("1".equals(bind_card)) {
                        gotoActivity(mContext, RepayPasswordActivity.class, mBundle);
                    } else {
                        gotoActivity(mContext, AddBankCardActivity.class, mBundle);
                    }
                }else if (type == GlobalParams.REPAY_BY_WECHAT) {
                    if (isWXAppInstalledAndSupported()) {
                        getWeChatOrder();
                    }
                }
                break;
            case R.id.ll_repay_bank_card:
                type = GlobalParams.REPAY_BY_BANK_CARD;
                initCheck(type);
                break;
            case R.id.ll_repay_wechat:
                type = GlobalParams.REPAY_BY_WECHAT;
                initCheck(type);
                break;
        }
    }


    private void initWeChatPay() {
        api = WXAPIFactory.createWXAPI(mContext, null);
        api.registerApp(GlobalParams.APP_ID_WX_PAY);
    }

    private boolean isWXAppInstalledAndSupported() {
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
                && api.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }

    private void getWeChatOrder(){
        //统一下单
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("amount", down_payment+"");
            jsonObject.put("overdue_amount","0");
            jsonObject.put("consume_id",consume_id);
            jsonObject.put("type","2");
            jsonObject.put("paytype",GlobalParams.WECHAT_PAY_TYPE);
            new GetWeChatOrder(mContext).getWeChatOrder(jsonObject, null, true, 0, new BaseNetCallBack<WeChatOrder>() {
                @Override
                public void onSuccess(WeChatOrder paramT) {
                    payByWeChat(paramT);
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
    private void payByWeChat(WeChatOrder mWeChatOrder){
        //调起微信支付
        PayReq request = new PayReq();
        request.appId = GlobalParams.APP_ID_WX_PAY;
        request.partnerId = mWeChatOrder.getData().getPartnerid();
        request.prepayId= mWeChatOrder.getData().getPrepayid();
        request.packageValue = mWeChatOrder.getData().getPackagevalue();
        request.nonceStr= mWeChatOrder.getData().getNoncestr();
        request.timeStamp= mWeChatOrder.getData().getTimestamp();
        request.sign= mWeChatOrder.getData().getSign();
        request.extData=GlobalParams.REPAY_FROM_SHOUFU;
        api.sendReq(request);
    }

    public void registeBroadCase(){
        wxPayBroadCast=new WXPayBroadCase();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(GlobalParams.WX_SHOUFU_SUCCESS);
        intentFilter.addAction(GlobalParams.BIND_CARD_SUCCESS_ACTION);
        registerReceiver(wxPayBroadCast,intentFilter);
    }
    public class WXPayBroadCase extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){

                case GlobalParams.BIND_CARD_SUCCESS_ACTION:
                    bind_card="1";
                    card_num=intent.getExtras().getString(GlobalParams.CARD_NUM_KEY);
                    bank_name=intent.getExtras().getString(GlobalParams.CARD_USER_NAME);
                    initView();
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wxPayBroadCast);
    }
}
