package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.event.RepayEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.model.ConsumeDataBean;
import com.tianshen.cash.model.InstallmentHistoryBean;
import com.tianshen.cash.model.RepayInfoBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.GetRepayInfo;
import com.tianshen.cash.net.api.Repayment;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 确认还款页面
 */

public class ConfirmRepayActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_confirm_money_back)
    TextView tvConfirmMoneyBack;
    @BindView(R.id.tv_confirm_repay_key)
    TextView tvConfirmRepayKey;
    @BindView(R.id.tv_confirm_repay)
    TextView tvConfirmRepay;
    @BindView(R.id.tv_confirm_repay_bank_key)
    TextView tvConfirmRepayBankKey;
    @BindView(R.id.tv_confirm_repay_bank)
    TextView tvConfirmRepayBank;
    @BindView(R.id.tv_confirm_repay_bank_num_key)
    TextView tvConfirmRepayBankNumKey;
    @BindView(R.id.tv_confirm_repay_num_bank)
    TextView tvConfirmRepayNumBank;
    @BindView(R.id.tv_confirm_repay_apply)
    TextView tvConfirmRepayApply;
    @BindView(R.id.tv_confirm_protocol)
    TextView tvConfirmProtocol;


    @BindView(R.id.rl_repay_severity_code)
    RelativeLayout rl_repay_severity_code;
    @BindView(R.id.et_repay_severity_code)
    EditText et_repay_severity_code;
    @BindView(R.id.tv_repay_severity_code)
    TextView tv_repay_severity_code;


    private RepayInfoBean mRepayInfoBean;

    private boolean mIsPaywaySelf; //如果是自己的产品true,如果是掌中是false


    private int mStartTime = 59;


    private static final int MSG_SEVERITY_TIME = 1;
    private static final int MSG_SEVERITY_DELAYED = 1 * 1000;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEVERITY_TIME:
                    refreshSeverityTextUI();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRepayData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_repay;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmRepayApply.setOnClickListener(this);
        tvConfirmProtocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_confirm_repay_apply:
                onClickApply();
                break;
            case R.id.tv_confirm_protocol:
                gotoWebActivity();
                break;
        }
    }

    /**
     * 得到确认还款信息
     */
    private void initRepayData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            GetRepayInfo getRepayInfo = new GetRepayInfo(mContext);
            getRepayInfo.getRepayInfo(jsonObject, null, true, new BaseNetCallBack<RepayInfoBean>() {
                @Override
                public void onSuccess(RepayInfoBean paramT) {
                    mRepayInfoBean = paramT;
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

    /**
     * 刷新UI
     */
    private void refreshUI() {

        if (mRepayInfoBean == null) {
            return;
        }
        String bank_card_num = mRepayInfoBean.getData().getBank_card_num();
        String bank_name = mRepayInfoBean.getData().getBank_name();
        String consumeAmount = mRepayInfoBean.getData().getConsume_amount();
        String overdueAmount = mRepayInfoBean.getData().getOverdue_amount();
        String is_payway = mRepayInfoBean.getData().getIs_payway();

        if ("0".equals(is_payway)) {//0为自己的产品，1为掌众的产品"
            mIsPaywaySelf = true;
        } else {
            mIsPaywaySelf = false;
        }

        try {
            String consumeAmountY = MoneyUtils.changeF2Y(consumeAmount);
            String overdueAmountY = MoneyUtils.changeF2Y(overdueAmount);
            String repayMoney = MoneyUtils.add(consumeAmountY, overdueAmountY);
            tvConfirmRepay.setText(repayMoney + "元");
            tvConfirmRepayBank.setText(bank_name);
            tvConfirmRepayNumBank.setText(bank_card_num);

            if (mIsPaywaySelf) {//自己的产品不需要验证码UI
                rl_repay_severity_code.setVisibility(View.GONE);
            } else {//掌众的产品展示获取验证码的UI
                rl_repay_severity_code.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()){
            return;
        }

        tv_repay_severity_code.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            tv_repay_severity_code.setText("重获取验证码");
            mStartTime = 59;
            tv_repay_severity_code.setEnabled(true);
            mHandler.removeMessages(MSG_SEVERITY_TIME);
        } else {
            tv_repay_severity_code.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED);
        }

    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        if (mRepayInfoBean == null) {
            ToastUtil.showToast(mContext, "数据错误!");
            return;
        }
        if (mIsPaywaySelf) {
            repayBySelf();
        } else {
            repayByZhangZhong();
        }
    }

    /**
     * 自己产品还款
     */
    private void repayBySelf() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            jsonObject.put("paytype", "1"); // (支付渠道，写死1 联动优势)
            jsonObject.put("type", "1"); // （支付类型 1还款 2付首付 不传默认为1）

            String id = mRepayInfoBean.getData().getId();
            String consumeId = mRepayInfoBean.getData().getConsume_id();
            String repayDate = mRepayInfoBean.getData().getRepay_date();
            String consumeAmount = mRepayInfoBean.getData().getConsume_amount();
            String overdueAmount = mRepayInfoBean.getData().getOverdue_amount();

            JSONObject consumeDataJSON= new JSONObject();
            consumeDataJSON.put("consume_id",consumeId);
            consumeDataJSON.put("type","5");
            consumeDataJSON.put("repay_date","");
            consumeDataJSON.put("amount","");
            consumeDataJSON.put("overdue_amount","");

            JSONObject installmentHistoryJSON= new JSONObject();
            installmentHistoryJSON.put("id",id);
            installmentHistoryJSON.put("repay_date",repayDate);
            installmentHistoryJSON.put("amount",consumeAmount);
            installmentHistoryJSON.put("overdue_amount",overdueAmount);

            JSONArray historyArray = new JSONArray();
            historyArray.put(installmentHistoryJSON);

            consumeDataJSON.put("installment_history",historyArray);
            JSONArray consume_data_array = new JSONArray();
            consume_data_array.put(consumeDataJSON);
            jsonObject.put("consume_data",consume_data_array);

            Repayment getRepayInfo = new Repayment(mContext);
            getRepayInfo.repayment(jsonObject, null, true, 5, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    EventBus.getDefault().post(new RepayEvent());
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 掌众还款
     */
    private void repayByZhangZhong() {
        ToastUtil.showToast(mContext, "掌众产品还款");
        backActivity();
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

}
