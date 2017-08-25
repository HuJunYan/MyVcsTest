package com.tianshen.cash.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.UserRepayDetailBean;
import com.tianshen.cash.net.api.UserRepayDetailApi;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MoneyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/24.
 */

public class RepayDetailDialogView {

    public RepayDetailDialogView(final Activity activity, String userId, String consume_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserRepayDetailApi userRepayDetailApi = new UserRepayDetailApi(activity);
        userRepayDetailApi.getUserRepayDetail(jsonObject, new BaseNetCallBack<UserRepayDetailBean>() {
            @Override
            public void onSuccess(UserRepayDetailBean paramT) {
                Dialog dialog = new Dialog(activity, R.style.MyDialog);
                View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_repay_detail, null);
                dialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = DensityUtil.dp2px(300);
                contentView.setLayoutParams(layoutParams);
                dialog.getWindow().setGravity(Gravity.CENTER);
                loadData(contentView, paramT);
                processShowOrHide(contentView, paramT);
                if (!activity.isFinishing()) {
                    dialog.show();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

    }

    /**
     * 加载展示的数据
     *
     * @param contentView
     * @param paramT
     */
    private void loadData(View contentView, UserRepayDetailBean paramT) {
        UserRepayDetailBean.Detail data = paramT.data;
        //利率
        TextView tv_annual_interest_rate = (TextView) contentView.findViewById(R.id.tv_annual_interest_rate);
        //应还本金
        TextView tv_consume_amount = (TextView) contentView.findViewById(R.id.tv_consume_amount);
        //支付利息
        TextView tv_interest = (TextView) contentView.findViewById(R.id.tv_interest);
        //手续费用
        TextView tv_service_charge = (TextView) contentView.findViewById(R.id.tv_service_charge);
        //滞纳金
        TextView tv_late_fee = (TextView) contentView.findViewById(R.id.tv_late_fee);
        //逾期罚息
        TextView tv_late_charge = (TextView) contentView.findViewById(R.id.tv_late_charge);
        //逾期管理费
        TextView tv_overdue_management_fee = (TextView) contentView.findViewById(R.id.tv_overdue_management_fee);
        //应还款总金额
        TextView tv_collect_customer_money = (TextView) contentView.findViewById(R.id.tv_collect_customer_money);
        setTextView(tv_consume_amount, data.consume_amount);
        setTextView(tv_interest, data.interest);
        setTextView(tv_service_charge, data.service_charge);
        setTextView(tv_late_fee, data.late_fee);
        setTextView(tv_late_charge, data.late_charge);
        setTextView(tv_overdue_management_fee, data.overdue_management_fee);
        setTextView(tv_collect_customer_money, data.collect_customer_money);
        tv_annual_interest_rate.setText("年利率" + Float.parseFloat(data.annual_interest_rate) * 100f + "%");
    }

    private void setTextView(TextView textview, String money) {
        try {
            textview.setText(MoneyUtils.changeF2Y(money) + "元");
        } catch (Exception e) {
        }
    }

    private void processShowOrHide(View contentView, UserRepayDetailBean paramT) {
        UserRepayDetailBean.Detail data = paramT.data;
        //本金item
        View ll_consume_amount = contentView.findViewById(R.id.ll_consume_amount);
        String consume_amount = data.consume_amount;
        processItem(ll_consume_amount, consume_amount);
        //支付利息
        View ll_interest = contentView.findViewById(R.id.ll_interest);
        String interest = data.interest;
        processItem(ll_interest, interest);
        //手续费用
        View ll_service_charge = contentView.findViewById(R.id.ll_service_charge);
        String service_charge = data.service_charge;
        processItem(ll_service_charge, service_charge);
        //滞纳金
        View ll_late_fee = contentView.findViewById(R.id.ll_late_fee);
        String late_fee = data.late_fee;
        processItem(ll_late_fee, late_fee);
        //逾期罚息
        View ll_late_charge = contentView.findViewById(R.id.ll_late_charge);
        String late_charge = data.late_charge;
        processItem(ll_late_charge, late_charge);
        //逾期管理费
        View ll_overdue_management_fee = contentView.findViewById(R.id.ll_overdue_management_fee);
        String overdue_management_fee = data.overdue_management_fee;
        processItem(ll_overdue_management_fee, overdue_management_fee);

    }

    private void processItem(View view, String money) {
        long l = Long.parseLong(money);
        if (l == 0) {
            view.setVisibility(View.GONE);
        }
    }
}
