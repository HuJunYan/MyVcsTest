package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.event.ApplyEvent;
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

    private OrderConfirmBean mOrderConfirmBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrderConfirmData();
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
        tvConfirmApply.setOnClickListener(this);
    }

    /**
     * 得到确认借款数据
     */
    private void initOrderConfirmData() {

        try {
            JSONObject jsonObject = new JSONObject();
            long userId = TianShenUserUtil.getUserId(mContext);

            String consume_amount = TianShenUserUtil.getUserConsumeAmount(mContext);
            String repay_id = TianShenUserUtil.getUserRepayId(mContext);
            jsonObject.put("customer_id", userId);
            jsonObject.put("repay_id", repay_id);
            jsonObject.put("consume_amount", consume_amount);

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
            case R.id.tv_confirm_apply:
                onClickApply();
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
        String consume_amount = mOrderConfirmBean.getData().getConsume_amount(); //用户申请金额
        String timer = mOrderConfirmBean.getData().getTimer();//借款时长
        String poundage = mOrderConfirmBean.getData().getPoundage();//手续费
        String amount = mOrderConfirmBean.getData().getAmount();//到账金额
        String bank_name = mOrderConfirmBean.getData().getBank_name();//绑定银行卡所属银行
        String id_num = mOrderConfirmBean.getData().getId_num();//绑定银行卡卡号
        try {
            String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
            String poundageY = MoneyUtils.changeF2Y(poundage);
            String amountY = MoneyUtils.changeF2Y(amount);
            tvConfirmWithdrawal.setText(consume_amountY + "元");
            tvConfirmProcedures.setText(poundageY + "元");
            tvConfirmTransfer.setText(amountY + "元");
            tvConfirmRepay.setText(consume_amountY + "元");
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

        try {
            JSONObject jsonObject = new JSONObject();


            User user = TianShenUserUtil.getUser(mContext);

            long userId = user.getId();
            String consume_amount = user.getConsume_amount();
            String repay_id = user.getRepay_id();
            String location = user.getLocation();
            String city = user.getCity();
            String country = user.getCountry();
            String address = user.getAddress();
            String province = user.getProvince();
            boolean payWayBySelf = TianShenUserUtil.isPayWayBySelf(mContext);
            String type;
            if (payWayBySelf) {
                type = "0";
            } else {
                type = "1";
            }
            String black_box = new GetTelephoneUtils(mContext).getBlackBox();

            jsonObject.put("customer_id", userId);
            jsonObject.put("type", type);
            jsonObject.put("consume_amount", consume_amount);
            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);
            jsonObject.put("black_box", black_box);
            jsonObject.put("push_id", "");
            jsonObject.put("repay_id", repay_id);

            final Order order = new Order(mContext);
            order.order(jsonObject, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    if (paramT.getCode() == 0) {
                        EventBus.getDefault().post(new ApplyEvent());
                        gotoActivity(mContext, MainActivity.class, null);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
