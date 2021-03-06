package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.RefreshRepayDataEvent;
import com.tianshen.cash.event.RepayEvent;
import com.tianshen.cash.event.RepayFailureEvent;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.RepayInfoBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.GetRepayInfo;
import com.tianshen.cash.net.api.GetVerifySmsForRepayment;
import com.tianshen.cash.net.api.PayConfirmZhangzhong;
import com.tianshen.cash.net.api.Repayment;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.RepayDetailDialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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
    @BindView(R.id.iv_dialog_money)
    ImageView iv_dialog_money;


    private RepayInfoBean mRepayInfoBean;

    private boolean mIsPaywaySelf; //如果是自己的产品true,如果是掌中是false


    private int mStartTime = 59;

    private String mVerifySmsForRepaymentType = "0";

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
        tvConfirmProtocol.setOnClickListener(this);
        tv_repay_severity_code.setOnClickListener(this);
        iv_dialog_money.setOnClickListener(this);
        RxView.clicks(tvConfirmRepayApply)//1秒钟之内禁用重复点击
                .throttleFirst(60, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                onClickApply();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                EventBus.getDefault().post(new RepayFailureEvent());//用户取消还款
                backActivity();
                break;
            case R.id.tv_confirm_protocol:
                gotoWebActivity();
                break;
            case R.id.tv_repay_severity_code:
                mHandler.sendEmptyMessage(MSG_SEVERITY_TIME);
                initVerifySmsForRepayment();
                break;
            case R.id.iv_dialog_money:
                showDetailDialog();
                break;
        }
    }

    private void showDetailDialog() {
        if (mRepayInfoBean == null || mRepayInfoBean.getData() == null) {
            return;
        }
        new RepayDetailDialogView(this, TianShenUserUtil.getUserId(mContext), mRepayInfoBean.getData().getConsume_id());
    }

    /**
     * 得到确认还款信息
     */
    private void initRepayData() {

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String consume_id = extras.getString(GlobalParams.CONSUME_ID, "");
                jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
            }
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
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

        tvConfirmRepay.setText(MoneyUtils.getPointTwoMoney(consumeAmount, overdueAmount) + "元");
        tvConfirmRepayBank.setText(bank_name);
        tvConfirmRepayNumBank.setText(bank_card_num);

        if (mIsPaywaySelf) {//自己的产品不需要验证码UI
            rl_repay_severity_code.setVisibility(View.GONE);
        } else {//掌众的产品展示获取验证码的UI
            rl_repay_severity_code.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取掌中还款的验证码
     */
    private void initVerifySmsForRepayment() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("type", mVerifySmsForRepaymentType);
            GetVerifySmsForRepayment getVerifySmsForRepayment = new GetVerifySmsForRepayment(mContext);
            getVerifySmsForRepayment.getVerify(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    if (paramT.getCode() == 0) {
                        ToastUtil.showToast(mContext, "发送验证码成功!");
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

    /**
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()) {
            return;
        }

        tv_repay_severity_code.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            tv_repay_severity_code.setText("重获取验证码");
            mStartTime = 59;
            tv_repay_severity_code.setEnabled(true);
            mVerifySmsForRepaymentType = "1";
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("paytype", "1"); // (支付渠道，写死1 联动优势)
            jsonObject.put("type", "1"); // （支付类型 1还款 2付首付 不传默认为1）

            String id = mRepayInfoBean.getData().getId();
            String consumeId = mRepayInfoBean.getData().getConsume_id();
            String repayDate = mRepayInfoBean.getData().getRepay_date();
            String consumeAmount = mRepayInfoBean.getData().getConsume_amount();
            String overdueAmount = mRepayInfoBean.getData().getOverdue_amount();

            JSONObject consumeDataJSON = new JSONObject();
            consumeDataJSON.put("consume_id", consumeId);
            consumeDataJSON.put("type", "5");
            consumeDataJSON.put("repay_date", "");
            consumeDataJSON.put("amount", "");
            consumeDataJSON.put("overdue_amount", "");

            JSONObject installmentHistoryJSON = new JSONObject();
            installmentHistoryJSON.put("id", id);
            installmentHistoryJSON.put("repay_date", repayDate);
            installmentHistoryJSON.put("amount", consumeAmount);
            installmentHistoryJSON.put("overdue_amount", overdueAmount);

            JSONArray historyArray = new JSONArray();
            historyArray.put(installmentHistoryJSON);

            consumeDataJSON.put("installment_history", historyArray);
            JSONArray consume_data_array = new JSONArray();
            consume_data_array.put(consumeDataJSON);
            jsonObject.put("consume_data", consume_data_array);

            Repayment getRepayInfo = new Repayment(mContext);
            getRepayInfo.repayment(jsonObject, tvConfirmRepayApply, true, 5, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    EventBus.getDefault().post(new RepayEvent());
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    EventBus.getDefault().post(new RepayFailureEvent());
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

        String verifyCode = et_repay_severity_code.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast(mContext, "请输入验证码!");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("verifyCode", verifyCode);
            PayConfirmZhangzhong payConfirmZhangzhong = new PayConfirmZhangzhong(mContext);
            payConfirmZhangzhong.payConfirm(jsonObject, tvConfirmRepayApply, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    if (paramT.getCode() == 0) {
                        EventBus.getDefault().post(new RepayEvent());
                        backActivity();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    EventBus.getDefault().post(new RepayFailureEvent());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            EventBus.getDefault().post(new RepayFailureEvent());
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe
    public void onRefreshRepayDataEvent(RefreshRepayDataEvent event) {
        initRepayData();
    }
}
