package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ConsumeDataBean;
import com.tianshen.cash.model.ConsumeSmallBillBean;
import com.tianshen.cash.model.GetBankListBean;
import com.tianshen.cash.model.InstallmentHistoryBean;
import com.tianshen.cash.model.MineBean;
import com.tianshen.cash.model.SmallOrderItemBean;
import com.tianshen.cash.model.WeChatOrder;
import com.tianshen.cash.model.WithdrawalsBillInfoBean;
import com.tianshen.cash.model.WithdrawalsBillInfoItenBean;
import com.tianshen.cash.model.WithdrawalsBillItemBean;
import com.tianshen.cash.net.api.GetBindBankList;
import com.tianshen.cash.net.api.GetWeChatOrder;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.CashBillListUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RePayActivity extends BaseActivity implements View.OnClickListener {

    private Bundle bundle;
    private TextView tv_total_money, tv_shop_name, tv_bank_name, tv_bank_card_title, tv_merchant_name_title;
    private LinearLayout ll_repay_bank_card, ll_repay_wechat;
    private Button bt_confirm;
    private int type = GlobalParams.REPAY_BY_BANK_CARD;//还款方式
    private ImageView iv_repay_by_bank_card, iv_repay_by_wechat;
    private MineBean mineBean;
    private IWXAPI api;//微信支付
    private WeChatOrder mWeChatOrder;
    private String merchantName = "";
    private String from = "";
    private String totalMoney = "";
    private GetBankListBean getBankListBean;
    private List<WithdrawalsBillItemBean> widthdrawalsBillItemList = new ArrayList<WithdrawalsBillItemBean>();
    private List<WithdrawalsBillInfoItenBean> mDetailList = new ArrayList<WithdrawalsBillInfoItenBean>();
    private WithdrawalsBillInfoBean mWithdrawalsBillInfoBean;
    private List<SmallOrderItemBean> consumeSmallBillBeanList;
    private  ConsumeSmallBillBean consumeSmallBillBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWeChatPay();
        bundle = getIntent().getExtras();
        initData();
        initView();
        initCheck(type);
    }

    private void initData() {
        if (null != bundle) {
            from = bundle.getString(GlobalParams.REPAY_FROM_KEY);
            if ("".equals(from) || null == from) {
                return;
            }
            switch (from) {
                case GlobalParams.REPAY_FROM_CONSUMPTION:
                    consumeSmallBillBeanList=(List<SmallOrderItemBean>)bundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                    consumeSmallBillBean=(ConsumeSmallBillBean)bundle.getSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY);
                    totalMoney = bundle.getString("totalAmount");
                    merchantName = consumeSmallBillBean.getMerchant_name();
                    break;
                case GlobalParams.REPAY_FROM_BORROW:
                    widthdrawalsBillItemList = (List<WithdrawalsBillItemBean>) bundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                    totalMoney = bundle.getString("totalAmount");
                    break;
                case GlobalParams.REPAY_FROM_BORROW_DETAIL:
                    mDetailList = (List<WithdrawalsBillInfoItenBean>) bundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                    mWithdrawalsBillInfoBean = (WithdrawalsBillInfoBean) bundle.getSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY);
                    totalMoney = bundle.getString("totalAmount");
                    break;
            }
        }
    }

    private void initView() {
        tv_total_money.setText(totalMoney);
        getBankInfo();
        switch (from) {
            case GlobalParams.REPAY_FROM_CONSUMPTION:
                tv_merchant_name_title.setText("消费商家");
                tv_shop_name.setText(merchantName);
                break;
            case GlobalParams.REPAY_FROM_BORROW_DETAIL:
            case GlobalParams.REPAY_FROM_BORROW:
                tv_merchant_name_title.setText("收款平台");
                tv_shop_name.setText("天神贷");
                break;
        }
    }

    private void initCheck(int type) {
        switch (type) {
            case GlobalParams.REPAY_BY_BANK_CARD:
                iv_repay_by_wechat.setImageResource(R.drawable.unrouted);
                iv_repay_by_bank_card.setImageResource(R.drawable.lift_ok);
                break;
            case GlobalParams.REPAY_BY_WECHAT:
                iv_repay_by_wechat.setImageResource(R.drawable.lift_ok);
                iv_repay_by_bank_card.setImageResource(R.drawable.unrouted);
                break;
        }
    }

    private void getBankInfo() {
        GetBindBankList getBindBankList = new GetBindBankList(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getBindBankList.getBindBankList(jsonObject, null, true, new BaseNetCallBack<GetBankListBean>() {
                @Override
                public void onSuccess(GetBankListBean paramT) {
                    getBankListBean = paramT;
                    if (paramT.getData().size() > 0) {
                        String bankCardNum = paramT.getData().get(0).getCard_num();
                        String bankName = paramT.getData().get(0).getBank_name();
                        tv_bank_name.setText(bankName + "(" + bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length()) + ")");
                        tv_bank_card_title.setText("已绑定银行卡");
                    } else {
                        tv_bank_name.setText("银行卡支付");
                        tv_bank_card_title.setText("需要绑定银行卡");
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

    @Override
    protected int setContentView() {
        return R.layout.activity_re_pay;
    }

    @Override
    protected void findViews() {
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        tv_bank_card_title = (TextView) findViewById(R.id.tv_bank_card_title);
        ll_repay_bank_card = (LinearLayout) findViewById(R.id.ll_repay_bank_card);
        ll_repay_wechat = (LinearLayout) findViewById(R.id.ll_repay_wechat);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        iv_repay_by_bank_card = (ImageView) findViewById(R.id.iv_repay_by_bank_card);
        iv_repay_by_wechat = (ImageView) findViewById(R.id.iv_repay_by_wechat);
        tv_merchant_name_title = (TextView) findViewById(R.id.tv_merchant_name_title);
    }

    @Override
    protected void setListensers() {
        ll_repay_bank_card.setOnClickListener(this);
        ll_repay_wechat.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_repay_bank_card:
                type = GlobalParams.REPAY_BY_BANK_CARD;
                initCheck(type);
                break;
            case R.id.ll_repay_wechat:
                type = GlobalParams.REPAY_BY_WECHAT;
                initCheck(type);
                break;
            case R.id.bt_confirm:
                if ("".equals(from) || null == from) {
                    break;
                }
                if (type == GlobalParams.REPAY_BY_BANK_CARD) {
                    if (getBankListBean.getData().size() > 0) {
                        gotoActivity(mContext, RepayPasswordActivity.class, bundle);
                    } else {
                        if (GlobalParams.REPAY_FROM_BORROW.equals(from) || GlobalParams.REPAY_FROM_BORROW_DETAIL.equals(from)) {
                            ToastUtil.showToast(mContext, "您未绑定银行卡，请先去绑卡");
                            return;
                        } else {
                            gotoActivity(mContext, AddBankCardActivity.class, bundle);
                        }
                    }
                    break;
                }
                if (type == GlobalParams.REPAY_BY_WECHAT) {
                    if (isWXAppInstalledAndSupported()) {
                        getWeChatOrder();
                    }
                }
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

    private void getWeChatOrder() {
        //统一下单
        try {
            int type = 0;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("paytype", "2");
            switch (from) {
                case GlobalParams.REPAY_FROM_BORROW:
                    type = 5;
                    jsonObject.put("type", "1");
                    CashBillListUtil.sortByConsumeId(widthdrawalsBillItemList);
                    List<ConsumeDataBean> consumeDataList = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList = new ArrayList<InstallmentHistoryBean>();
                    String consumeId = "";
                    boolean isConsumeIdInit = false;
                    for (int i = 0; i < widthdrawalsBillItemList.size(); i++) {
                            if (!isConsumeIdInit) {
                                isConsumeIdInit = true;
                                consumeId = widthdrawalsBillItemList.get(i).getConsume_id();
                            }
                            if (consumeId.equals(widthdrawalsBillItemList.get(i).getConsume_id())) {
                                InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                                installmentHistoryBean.setId(widthdrawalsBillItemList.get(i).getBill_id());
                                String lateFee=widthdrawalsBillItemList.get(i).getLate_fee();
                                if("".equals(lateFee)||null==lateFee){
                                    lateFee="0";
                                }
                                installmentHistoryBean.setOverdue_amount(lateFee);
                                installmentHistoryBean.setAmount(widthdrawalsBillItemList.get(i).getRepay_money());
                                installmentHistoryBean.setRepay_date(widthdrawalsBillItemList.get(i).getRepay_date());
                                installmentHistoryBeanList.add(installmentHistoryBean);
                            } else {
                                ConsumeDataBean consumeData = new ConsumeDataBean();
                                consumeData.setConsume_id(consumeId);
                                consumeData.setType("5");
                                consumeData.setRepay_date("");
                                consumeData.setAmount("");
                                consumeData.setOverdue_amount("");
                                consumeData.setInstallment_history(installmentHistoryBeanList);
                                consumeDataList.add(consumeData);
                                consumeId = widthdrawalsBillItemList.get(i).getConsume_id();
                                installmentHistoryBeanList=new ArrayList<InstallmentHistoryBean>();
                                InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                                installmentHistoryBean.setId(widthdrawalsBillItemList.get(i).getBill_id());
                                String lateFee=widthdrawalsBillItemList.get(i).getLate_fee();
                                if("".equals(lateFee)||null==lateFee){
                                    lateFee="0";
                                }
                                installmentHistoryBean.setOverdue_amount(lateFee);
                                installmentHistoryBean.setAmount(widthdrawalsBillItemList.get(i).getRepay_money());
                                installmentHistoryBean.setRepay_date(widthdrawalsBillItemList.get(i).getRepay_date());
                                installmentHistoryBeanList.add(installmentHistoryBean);
                            }
                           if(i==widthdrawalsBillItemList.size()-1){
                                ConsumeDataBean consumeData = new ConsumeDataBean();
                                consumeData.setConsume_id(widthdrawalsBillItemList.get(i).getConsume_id());
                                consumeData.setType("5");
                                consumeData.setRepay_date("");
                                consumeData.setAmount("");
                                consumeData.setOverdue_amount("");
                                consumeData.setInstallment_history(installmentHistoryBeanList);
                                consumeDataList.add(consumeData);
                            }
                    }
                    jsonObject.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataList)));
                    break;
                case GlobalParams.REPAY_FROM_BORROW_DETAIL:
                    type = 5;
                    jsonObject.put("type", "1");
                    List<ConsumeDataBean> consumeDataBeenList = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList2 = new ArrayList<InstallmentHistoryBean>();
                    for (int i = 0; i < mDetailList.size(); i++) {
                        if (mDetailList.get(i).isChecked()) {
                            InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                            installmentHistoryBean.setId(mDetailList.get(i).getId());
                            String principal = mDetailList.get(i).getPrincipal();
                            if ("".equals(principal) || null == principal) {
                                principal = "0";
                            }
                            String lateFee = mDetailList.get(i).getLate_fee();
                            if ("".equals(lateFee) || null == lateFee) {
                                lateFee = "0";
                            }
                            String repayData=mDetailList.get(i).getRepay_date();
                            if(null==repayData){
                                repayData="";
                            }
                            installmentHistoryBean.setAmount(principal);
                            installmentHistoryBean.setRepay_date(repayData);
                            installmentHistoryBean.setOverdue_amount(lateFee);
                            installmentHistoryBeanList2.add(installmentHistoryBean);
                        }
                    }
                    ConsumeDataBean consumeDataBean = new ConsumeDataBean();
                    consumeDataBean.setInstallment_history(installmentHistoryBeanList2);
                    consumeDataBean.setConsume_id(mWithdrawalsBillInfoBean.getData().getConsume_id());
                    consumeDataBean.setRepay_date("");
                    consumeDataBean.setAmount("");
                    consumeDataBean.setOverdue_amount("");
                    consumeDataBean.setType("5");
                    consumeDataBeenList.add(consumeDataBean);
                    jsonObject.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataBeenList)));
                    break;
                case GlobalParams.REPAY_FROM_CONSUMPTION:
                    jsonObject.put("type", "1");
                    List<ConsumeDataBean> consumeDataBeenList5 = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList5 = new ArrayList<InstallmentHistoryBean>();
                    ConsumeDataBean consumeDataBean5 = new ConsumeDataBean();
                    boolean isNextMonth="1".equals(consumeSmallBillBean.getRepay_type())?true:false;
                    if(!isNextMonth) {
                        type=2;
                        for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                            if (consumeSmallBillBeanList.get(i).isChecked()) {
                                InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                                installmentHistoryBean.setId(consumeSmallBillBeanList.get(i).getId());
                                String principal = consumeSmallBillBeanList.get(i).getPrincipal();
                                if ("".equals(principal) || null == principal) {
                                    principal = "0";
                                }
                                String lateFee = consumeSmallBillBeanList.get(i).getLate_fee();
                                if ("".equals(lateFee) || null == lateFee) {
                                    lateFee = "0";
                                }
                                String repayData = consumeSmallBillBeanList.get(i).getRepay_date();
                                if (null == repayData) {
                                    repayData = "";
                                }
                                installmentHistoryBean.setAmount(principal);
                                installmentHistoryBean.setRepay_date(repayData);
                                installmentHistoryBean.setOverdue_amount(lateFee);
                                installmentHistoryBeanList5.add(installmentHistoryBean);
                            }
                        }
                        consumeDataBean5.setInstallment_history(installmentHistoryBeanList5);
                    }
                    else{
                        type=1;
                    }

                    consumeDataBean5.setRepay_date(isNextMonth?consumeSmallBillBeanList.get(0).getRepay_date():"");
                    consumeDataBean5.setAmount(isNextMonth?consumeSmallBillBeanList.get(0).getPrincipal():"");
                    consumeDataBean5.setOverdue_amount(isNextMonth?consumeSmallBillBeanList.get(0).getLate_fee():"");
                    consumeDataBean5.setConsume_id(consumeSmallBillBean.getConsume_id());
                    consumeDataBean5.setType(consumeSmallBillBean.getRepay_type());
                    consumeDataBeenList5.add(consumeDataBean5);
                    jsonObject.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataBeenList5)));
                    break;
            }
            GetWeChatOrder mGetWeChatOrder = new GetWeChatOrder(mContext);
            mGetWeChatOrder.getWeChatOrder(jsonObject, null, true, type, new BaseNetCallBack<WeChatOrder>() {
                @Override
                public void onSuccess(WeChatOrder paramT) {
                    mWeChatOrder = paramT;
                    payByWeChat();
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

    private void payByWeChat() {
        //调起微信支付
        PayReq request = new PayReq();
        request.appId = GlobalParams.APP_ID_WX_PAY;
        request.partnerId = mWeChatOrder.getData().getPartnerid();
        request.prepayId = mWeChatOrder.getData().getPrepayid();
        request.packageValue = mWeChatOrder.getData().getPackagevalue();
        request.nonceStr = mWeChatOrder.getData().getNoncestr();
        request.timeStamp = mWeChatOrder.getData().getTimestamp();
        request.sign = mWeChatOrder.getData().getSign();
        request.extData = from;
        api.sendReq(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
