package com.maibai.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.maibai.user.R;
import com.maibai.user.adapter.OrderTipAdapter;
import com.maibai.user.adapter.PayBillDetailAdapter;
import com.maibai.user.adapter.WithDrawalsBillDetailAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CalculateInstallmentBean;
import com.maibai.user.model.ConsumeBigBillItemBean;
import com.maibai.user.model.ConsumeBigBillListBean;
import com.maibai.user.model.ConsumeSmallBillBean;
import com.maibai.user.model.ConsumeSmallBillListBean;
import com.maibai.user.model.SmallOrderItemBean;
import com.maibai.user.model.SmallOrderItemTipDataItemBean;
import com.maibai.user.model.WithdrawalsBillInfoBean;
import com.maibai.user.model.WithdrawalsBillInfoItenBean;
import com.maibai.user.net.api.CalculateInstallment;
import com.maibai.user.net.api.GetConsumeSmallBillList;
import com.maibai.user.net.api.GetWithdrawalsBillInfo;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m on 16-10-19.
 */
public class WithdrawalsOrBillDetailActivity extends BaseActivity implements WithDrawalsBillDetailAdapter.Control, View.OnClickListener, PayBillDetailAdapter.Control {
    private ListView lv_bill_detail;
    private List<WithdrawalsBillInfoItenBean> mDetailList = new ArrayList<WithdrawalsBillInfoItenBean>();
    private WithDrawalsBillDetailAdapter mwithDrawalsBillDetailAdapter;
    private Bundle bundle;
    private CheckBox cb_select_all;
    private TextView tv_selected_amount;
    private TextView tv_repay;
    private Double amount = 0.0;
    private WithdrawalsBillInfoBean mWithdrawalsBillInfoBean;
    private TextView tv_first_value, tv_second_value, tv_third_value;
    private ScrollView sv_container;
    private TextView tv_wait_to_pay, tv_have_pay;
    private TextView tv_bill_amount_and_times, tv_bill_finish_time;
    private TextView tv_repay_next_month;
    private String consume_id;
    private int bill_detail_from = 0;
    private List<SmallOrderItemBean> consumeSmallBillBeanList = new ArrayList<SmallOrderItemBean>();
    private ConsumeSmallBillBean consumeSmallBillBean;
    private RelativeLayout rl_control_container;
    private LinearLayout ll_control_container;
    private PayBillDetailAdapter payBillDetailAdapter;
    private TextView tv_installment;
    private TextView tv_save_amount;
    private ImageView iv_question;
    private List<SmallOrderItemTipDataItemBean> moneyData;
    private TextView tv_first_text, tv_second_text, tv_third_text;
    private LinearLayout ll_withdrawals_container;
    private LinearLayout ll_pay_container;
    private TextView tv_merchant_name, tv_finish_time;
    private String consumeBigId = "";
    private CheckListener checkListener;
    private TextView tv_pay_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }
        bill_detail_from = bundle.getInt(GlobalParams.BILL_DETAIL_FROM_KEY, -1);
        if (-1 == bill_detail_from) {
            return;
        }
        switch (bill_detail_from) {
            case GlobalParams.BILL_DETAIL_FROM_PAY:
                tv_first_text.setText("消费总额（元）");
                tv_second_text.setText("首付金额（元）");
                tv_third_text.setText("买呗代付（元）");
                ll_withdrawals_container.setVisibility(View.GONE);
                ll_pay_container.setVisibility(View.VISIBLE);
                tv_pay_title.setText("待支付");
                ConsumeBigBillListBean consumeBigBillListBean = (ConsumeBigBillListBean) (bundle.getSerializable("ConsumeBigBillListBean"));
                if (null == consumeBigBillListBean) {
                    return;
                }
                ConsumeSmallBillBean dataSmall = consumeBigBillListBean.getData_small();
                if (null == dataSmall) {
                    return;
                }
                String withdrawalsAmount = dataSmall.getAmount();
                if ("".equals(withdrawalsAmount) || null == withdrawalsAmount) {
                    withdrawalsAmount = "0";
                }
                tv_first_value.setText(Double.valueOf(withdrawalsAmount) / 100 + "");
                String haveRepay = dataSmall.getHave_repay();
                if ("".equals(haveRepay) || null == haveRepay) {
                    haveRepay = "0";
                }
                tv_have_pay.setText("已还" + Double.valueOf(haveRepay) / 100 + "元");
                String needPayTotal = dataSmall.getRepay_amount();
                if ("".equals(needPayTotal) || null == needPayTotal) {
                    needPayTotal = "0";
                }
                tv_wait_to_pay.setText("待还" + Double.valueOf(needPayTotal) / 100 + "元");
                String borrowAmount = dataSmall.getBorrow_amount();
                if (null == borrowAmount || "".equals(borrowAmount)) {
                    borrowAmount = "0";
                }

                tv_third_value.setText(Double.valueOf(borrowAmount) / 100 + "");
                String downPayment = dataSmall.getDown_payment();
                if (null == downPayment || "".equals(downPayment)) {
                    downPayment = "0";
                }
                tv_second_value.setText(Double.valueOf(downPayment) / 100 + "");
                String finishTime = dataSmall.getConsume_date();
                if (null == finishTime) {
                    finishTime = "";
                }
                tv_finish_time.setText("订单完成: " + finishTime);
                String merchantName = dataSmall.getMerchant_name();
                if (null == merchantName) {
                    merchantName = "";
                }
                tv_merchant_name.setText(merchantName);
                consumeBigId = dataSmall.getConsume_id();
                consumeSmallBillBean = consumeBigBillListBean.getData_small();
                if (null == consumeBigBillListBean) {
                    return;
                }
                initView(consumeSmallBillBean.getRepay_type());
                consumeSmallBillBeanList = consumeSmallBillBean.getSmall_order_list();
                payBillDetailAdapter = new PayBillDetailAdapter(mContext, consumeSmallBillBeanList);
                lv_bill_detail.setAdapter(payBillDetailAdapter);
                boolean isChecked=false;
                for(int i=0;i<consumeSmallBillBeanList.size();i++){
                    if(!"0".equals(consumeSmallBillBeanList.get(i).getState())&&!isChecked){
                        consumeSmallBillBeanList.get(i).setChecked(true);
                        isChecked=true;
                    }
                }
                updateView();
                break;
            case GlobalParams.BILL_DETAIL_FROM_PAY_BILL:
                tv_first_text.setText("消费总额（元）");
                tv_second_text.setText("首付金额（元）");
                tv_third_text.setText("买呗代付（元）");
                ll_withdrawals_container.setVisibility(View.GONE);
                ll_pay_container.setVisibility(View.VISIBLE);
                tv_pay_title.setText("待支付");
                ConsumeBigBillItemBean consumeBigBillItemBean = (ConsumeBigBillItemBean) bundle.getSerializable("ConsumeBigBillItemBean");
                if (null == consumeBigBillItemBean) {
                    return;
                }
                consumeBigId = consumeBigBillItemBean.getConsume_id();
                if (null == consumeBigId || "".equals(consumeBigId)) {
                    return;
                }
                initView(consumeBigBillItemBean.getRepay_type());
                if (consumeSmallBillBeanList == null) {
                    break;
                }
                payBillDetailAdapter = new PayBillDetailAdapter(mContext, consumeSmallBillBeanList);
                lv_bill_detail.setAdapter(payBillDetailAdapter);
                setListViewHeightBasedOnChildren(lv_bill_detail);
                sv_container.smoothScrollTo(0, 0);
                getPayBillDetail(consumeBigId);
                break;
            case GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS:
                tv_first_text.setText("取现总额(元)");
                tv_second_text.setText("借款期限");
                tv_third_text.setText("剩余应还(元)");
                tv_pay_title.setText("已选");
                ll_withdrawals_container.setVisibility(View.VISIBLE);
                ll_pay_container.setVisibility(View.GONE);
                consume_id = bundle.getString("consume_id");
                initView("2");
                mwithDrawalsBillDetailAdapter = new WithDrawalsBillDetailAdapter(mContext, mDetailList, WithdrawalsOrBillDetailActivity.this);
                lv_bill_detail.setAdapter(mwithDrawalsBillDetailAdapter);
                setListViewHeightBasedOnChildren(lv_bill_detail);
                sv_container.smoothScrollTo(0, 0);
                getWithdrawalsDetail(consume_id);
                break;
        }
    }

    private void initView(String repayType) {
        if ("1".equals(repayType)) {
            //次月
            ll_control_container.setVisibility(View.VISIBLE);
            rl_control_container.setVisibility(View.GONE);
        } else if ("2".equals(repayType)) {
            //分期
            ll_control_container.setVisibility(View.GONE);
            rl_control_container.setVisibility(View.VISIBLE);
        }
    }

    private void getWithdrawalsDetail(String consume_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", consume_id); // TODO, test
            GetWithdrawalsBillInfo getWithdrawalsBillInfo = new GetWithdrawalsBillInfo(mContext);
            getWithdrawalsBillInfo.getWithdrawalsBill(jsonObject, null, true, new BaseNetCallBack<WithdrawalsBillInfoBean>() {
                @Override
                public void onSuccess(WithdrawalsBillInfoBean paramT) {
                    // TODO 初始化部分ui数据
                    mWithdrawalsBillInfoBean = paramT;
                    String withdrawalsAmount = paramT.getData().getAmount();
                    if ("".equals(withdrawalsAmount) || null == withdrawalsAmount) {
                        withdrawalsAmount = "0";
                    }
                    tv_first_value.setText(Double.valueOf(withdrawalsAmount) / 100 + "");
                    String haveRepay = paramT.getData().getHave_repay();
                    if ("".equals(haveRepay) || null == haveRepay) {
                        haveRepay = "0";
                    }
                    tv_have_pay.setText("已还" + Double.valueOf(haveRepay) / 100 + "元");
                    String needPayTotal = paramT.getData().getNeed_repay_total();
                    if ("".equals(needPayTotal) || null == needPayTotal) {
                        needPayTotal = "0";
                    }
                    tv_wait_to_pay.setText("待还" + Double.valueOf(needPayTotal) / 100 + "元");
                    tv_third_value.setText(Double.valueOf(needPayTotal) / 100 + "");
                    String repayTime = paramT.getData().getRepay_times();
                    if (null == repayTime) {
                        repayTime = "";
                    }
                    tv_second_value.setText(repayTime + "个月");
                    tv_bill_amount_and_times.setText(Double.valueOf(withdrawalsAmount) / 100 + "元" + "-" + repayTime + "个月");
                    String finishTime = paramT.getData().getConsume_time();
                    if (null == finishTime) {
                        finishTime = "";
                    }
                    tv_bill_finish_time.setText(finishTime);
                    mDetailList.clear();
                    mDetailList.addAll(paramT.getData().getList());
                    boolean isChecked=false;
                    for(int i=0;i<mDetailList.size();i++){
                        if(!"0".equals(mDetailList.get(i).getState())&&!isChecked){
                            mDetailList.get(i).setChecked(true);
                            isChecked=true;
                        }
                    }
                    updateView();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void getPayBillDetail(final String consumeBigId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", consumeBigId);
            GetConsumeSmallBillList getConsumeSmallBillList = new GetConsumeSmallBillList(mContext);
            getConsumeSmallBillList.getConsumeSmallBillList(jsonObject, null, true, new BaseNetCallBack<ConsumeSmallBillListBean>() {
                @Override
                public void onSuccess(ConsumeSmallBillListBean paramT) {
                    consumeSmallBillBean = paramT.getData();
                    if (null == consumeSmallBillBean) {
                        return;
                    }
                    String withdrawalsAmount = consumeSmallBillBean.getAmount();
                    if ("".equals(withdrawalsAmount) || null == withdrawalsAmount) {
                        withdrawalsAmount = "0";
                    }
                    tv_first_value.setText(Double.valueOf(withdrawalsAmount) / 100 + "");
                    String haveRepay = consumeSmallBillBean.getHave_repay();
                    if ("".equals(haveRepay) || null == haveRepay) {
                        haveRepay = "0";
                    }
                    tv_have_pay.setText("已还" + Double.valueOf(haveRepay) / 100 + "元");
                    String needPayTotal = consumeSmallBillBean.getRepay_amount();
                    if ("".equals(needPayTotal) || null == needPayTotal) {
                        needPayTotal = "0";
                    }
                    tv_wait_to_pay.setText("待还" + Double.valueOf(needPayTotal) / 100 + "元");
                    String borrowAmount = consumeSmallBillBean.getBorrow_amount();
                    if (null == borrowAmount || "".equals(borrowAmount)) {
                        borrowAmount = "0";
                    }

                    tv_third_value.setText(Double.valueOf(borrowAmount) / 100 + "");
                    String downPayment = consumeSmallBillBean.getDown_payment();
                    if (null == downPayment || "".equals(downPayment)) {
                        downPayment = "0";
                    }
                    tv_second_value.setText(Double.valueOf(downPayment) / 100 + "");
                    String finishTime = consumeSmallBillBean.getConsume_date();
                    if (null == finishTime) {
                        finishTime = "";
                    }
                    tv_finish_time.setText("订单完成: " + finishTime);
                    String merchantName = consumeSmallBillBean.getMerchant_name();
                    if (null == merchantName) {
                        merchantName = "";
                    }
                    tv_merchant_name.setText(merchantName);
                    consumeSmallBillBeanList.clear();
                    consumeSmallBillBeanList.addAll(consumeSmallBillBean.getSmall_order_list());
                    boolean isChecked=false;
                    for(int i=0;i<consumeSmallBillBeanList.size();i++){
                        if(!"0".equals(consumeSmallBillBeanList.get(i).getState())&&!isChecked){
                            consumeSmallBillBeanList.get(i).setChecked(true);
                            isChecked=true;
                        }
                    }
                    updateView();
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
        return R.layout.activity_withdrawals_bill_detail;
    }

    @Override
    protected void findViews() {
        lv_bill_detail = (ListView) findViewById(R.id.lv_bill_detail);
        cb_select_all = (CheckBox) findViewById(R.id.cb_select_all);
        tv_selected_amount = (TextView) findViewById(R.id.tv_selected_amount);
        tv_repay = (TextView) findViewById(R.id.tv_repay);
        tv_first_value = (TextView) findViewById(R.id.tv_first_value);
        tv_second_value = (TextView) findViewById(R.id.tv_second_value);
        sv_container = (ScrollView) findViewById(R.id.sv_container);
        tv_wait_to_pay = (TextView) findViewById(R.id.tv_wait_to_pay);
        tv_have_pay = (TextView) findViewById(R.id.tv_have_pay);
        tv_bill_amount_and_times = (TextView) findViewById(R.id.tv_bill_amount_and_times);
        tv_bill_finish_time = (TextView) findViewById(R.id.tv_bill_finish_time);
        rl_control_container = (RelativeLayout) findViewById(R.id.rl_control_container);
        ll_control_container = (LinearLayout) findViewById(R.id.ll_control_container);
        tv_repay_next_month = (TextView) findViewById(R.id.tv_repay_next_month);
        tv_installment = (TextView) findViewById(R.id.tv_installment);
        tv_save_amount = (TextView) findViewById(R.id.tv_save_amount);
        iv_question = (ImageView) findViewById(R.id.iv_question);
        tv_first_text = (TextView) findViewById(R.id.tv_first_text);
        tv_second_text = (TextView) findViewById(R.id.tv_second_text);
        tv_third_text = (TextView) findViewById(R.id.tv_third_text);
        ll_withdrawals_container = (LinearLayout) findViewById(R.id.ll_withdrawals_container);
        ll_pay_container = (LinearLayout) findViewById(R.id.ll_pay_container);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        tv_finish_time = (TextView) findViewById(R.id.tv_finish_time);
        tv_third_value = (TextView) findViewById(R.id.tv_third_value);
        tv_pay_title=(TextView)findViewById(R.id.tv_pay_title);
    }

    @Override
    protected void setListensers() {
        checkListener = new CheckListener();
        cb_select_all.setOnCheckedChangeListener(checkListener);
        tv_repay.setOnClickListener(this);
        tv_repay_next_month.setOnClickListener(this);
        tv_installment.setOnClickListener(this);
        iv_question.setOnClickListener(this);
    }

    public class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (bill_detail_from) {
                case GlobalParams.BILL_DETAIL_FROM_PAY:
                case GlobalParams.BILL_DETAIL_FROM_PAY_BILL:
                    for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                        if (!"0".equals(consumeSmallBillBeanList.get(i).getState()))
                            consumeSmallBillBeanList.get(i).setChecked(isChecked);
                    }
                    updateView();
                    break;
                case GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS:
                    for (int i = 0; i < mDetailList.size(); i++) {
                        if (!"0".equals(mDetailList.get(i).getState()))
                            mDetailList.get(i).setChecked(isChecked);
                    }
                    updateView();
                    break;
            }
        }
    }

    private void updateView() {
        int position = -1;
        switch (bill_detail_from) {
            case GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS:
                amount = 0.0;
                for (int i = 0; i < mDetailList.size(); i++) {
                    boolean isCheck = mDetailList.get(i).isChecked();
                    if (isCheck) {
                        position=i;
                        String repayAmount = mDetailList.get(i).getRepay_amount();
                        if ("".equals(repayAmount) || null == repayAmount) {
                            repayAmount = "0";
                        }
                        amount += Double.parseDouble(repayAmount);
                    }
                }

                tv_selected_amount.setText(amount / 100 + "");
                mwithDrawalsBillDetailAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_bill_detail);
                sv_container.smoothScrollTo(0, 0);
                if (position == mDetailList.size() - 1) {
                    cb_select_all.setOnCheckedChangeListener(null);
                    cb_select_all.setChecked(true);
                    cb_select_all.setOnCheckedChangeListener(checkListener);
                } else {
                    cb_select_all.setOnCheckedChangeListener(null);
                    cb_select_all.setChecked(false);
                    cb_select_all.setOnCheckedChangeListener(checkListener);
                }
                break;
            case GlobalParams.BILL_DETAIL_FROM_PAY:
            case GlobalParams.BILL_DETAIL_FROM_PAY_BILL:
                try {
                    moneyData = new ArrayList<SmallOrderItemTipDataItemBean>();
                    amount = 0.0;
                    double save_amount = 0.0;
                    for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                        boolean isCheck = consumeSmallBillBeanList.get(i).isChecked();
                        if (isCheck) {
                            position = i;
                        }
                    }
                    payBillDetailAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lv_bill_detail);
                    sv_container.smoothScrollTo(0, 0);
                    String needPay = "";
                    String saveAmount;
                    if (position != -1) {
                        moneyData = consumeSmallBillBeanList.get(position).getMoney_data();
                        needPay = consumeSmallBillBeanList.get(position).getNeed_pay();
                        saveAmount = consumeSmallBillBeanList.get(position).getSave_amount();
                    } else {
                        needPay = "0";
                        saveAmount = "0";
                    }
                    if ("".equals(saveAmount) || null == saveAmount) {
                        saveAmount = "0";
                    }
                    save_amount = Double.valueOf(Long.valueOf(saveAmount));
                    if (save_amount > 0) {
                        tv_save_amount.setVisibility(View.VISIBLE);
                        tv_save_amount.setText("已为您节省" + save_amount / 100 + "元服务费");
                    } else {
                        tv_save_amount.setVisibility(View.GONE);
                    }
                    if ("".equals(needPay) || null == needPay) {
                        needPay = "0";
                    }
                    amount = Double.valueOf(Long.valueOf(needPay));
                    tv_selected_amount.setText(amount / 100 + "");
                    if (position != -1 && moneyData != null && 0 != moneyData.size()) {
                        iv_question.setVisibility(View.VISIBLE);
                    } else {
                        iv_question.setVisibility(View.GONE);
                    }
                    if (position == consumeSmallBillBeanList.size() - 1) {
                        cb_select_all.setOnCheckedChangeListener(null);
                        cb_select_all.setChecked(true);
                        cb_select_all.setOnCheckedChangeListener(checkListener);
                        Log.d("cb_select_all", "check");
                    } else {
                        Log.d("cb_select_all", "uncheck");
                        cb_select_all.setOnCheckedChangeListener(null);
                        cb_select_all.setChecked(false);
                        cb_select_all.setOnCheckedChangeListener(checkListener);
                    }
                }catch (Exception e){
                    MobclickAgent.reportError(mContext, LogUtil.getException(e));
                }
                break;
        }
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void check(int position, boolean isCheck) {
        switch (bill_detail_from) {
            case GlobalParams.BILL_DETAIL_FROM_PAY:
            case GlobalParams.BILL_DETAIL_FROM_PAY_BILL:
                if (isCheck) {
                    for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                        if (i <= position && !("0".equals(consumeSmallBillBeanList.get(i).getState()))) {
                            consumeSmallBillBeanList.get(i).setChecked(true);
                        }
                    }
                } else {
                    for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                        if (i >= position && !("0".equals(consumeSmallBillBeanList.get(i).getState()))) {
                            consumeSmallBillBeanList.get(i).setChecked(false);
                        }
                    }
                }
                updateView();
                break;
            case GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS:
                if (isCheck) {
                    for (int i = 0; i < mDetailList.size(); i++) {
                        if (i <= position && !("0".equals(mDetailList.get(i).getState()))) {
                            mDetailList.get(i).setChecked(true);
                        }
                    }
                } else {
                    for (int i = 0; i < mDetailList.size(); i++) {
                        if (i >= position && !("0".equals(mDetailList.get(i).getState()))) {
                            mDetailList.get(i).setChecked(false);
                        }
                    }
                }
                updateView();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_repay:
            case R.id.tv_repay_next_month:
                if (GlobalParams.BILL_DETAIL_FROM_WITHDRAWLS == bill_detail_from) {
                    if (amount == 0.0) {
                        ToastUtil.showToast(mContext, "请至少选择一项账单");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalParams.REPAY_FROM_KEY, GlobalParams.REPAY_FROM_BORROW_DETAIL);
                    bundle.putSerializable(GlobalParams.REPAY_BEAN_KEY, (Serializable) mDetailList);
                    bundle.putSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY, (Serializable) mWithdrawalsBillInfoBean);
                    bundle.putString("totalAmount", amount / 100 + "");
                    gotoActivity(mContext, RePayActivity.class, bundle);
                } else if (GlobalParams.BILL_DETAIL_FROM_PAY == bill_detail_from || GlobalParams.BILL_DETAIL_FROM_PAY_BILL == bill_detail_from) {
                    if (amount == 0.0) {
                        ToastUtil.showToast(mContext, "请至少选择一项账单");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalParams.REPAY_FROM_KEY, GlobalParams.REPAY_FROM_CONSUMPTION);
                    bundle.putSerializable(GlobalParams.REPAY_BEAN_KEY, (Serializable) consumeSmallBillBeanList);
                    bundle.putSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY, (Serializable) consumeSmallBillBean);
                    bundle.putString("totalAmount", amount / 100 + "");
                    gotoActivity(mContext, RePayActivity.class, bundle);
                }
                break;
            case R.id.tv_installment:
                if (amount == 0.0) {
                    ToastUtil.showToast(mContext, "请至少选择一项账单");
                    return;
                }
                try {
                    JSONObject mJson = new JSONObject();
                    mJson.put("customer_id", UserUtil.getId(mContext));
                    mJson.put("consume_id", consumeBigId);
                    final CalculateInstallment mCalculateInstallment = new CalculateInstallment(mContext);
                    mCalculateInstallment.calculateInstallment(mJson, null, true, new BaseNetCallBack<CalculateInstallmentBean>() {
                        @Override
                        public void onSuccess(CalculateInstallmentBean mCalculateInstallmentBean) {
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("CalculateInstallmentBean", mCalculateInstallmentBean);
                            mBundle.putString("consume_id", consumeBigId);
                            gotoActivity(mContext, TurnToInstallmentActivity.class, mBundle);
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e));
                }
                break;
            case R.id.iv_question:
                if (moneyData == null) {
                    ToastUtil.showToast(mContext, "数据错误");
                    return;
                }
                showPromtDialog();
                break;
        }
    }

    private void showPromtDialog() {
        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.permission_without_step_dialog).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_tip_dialog_view, null);
        Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
        ListView lv_question_tip = (ListView) view.findViewById(R.id.lv_question_tip);
        OrderTipAdapter adapter = new OrderTipAdapter(mContext, moneyData);
        lv_question_tip.setAdapter(adapter);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTelephoneUtils(mContext).changeLight();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(mContext).changeDark();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
