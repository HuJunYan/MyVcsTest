package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.CashSubItemBean;
import com.maibai.cash.net.base.UserUtil;

public class WithdrawalsConfirmActivity extends BaseActivity implements View.OnClickListener {
    private String mRepayTimes;
    private String mCardNum;
    private String mBankName;
    private TextView tv_withdrawals_amount;
    private TextView tv_repay_time;
    private TextView tv_repay_month;
    private TextView tv_arrival_amount;
    private TextView tv_procedures_amount;
    private TextView tv_bank_name;
    private TextView tv_bank_card_num;
    private TextView tv_repay_type;
    private LinearLayout ll_procedures;
    private Button bt_confirm;
    private Bundle mBundle;
    private CheckBox cb_agree;
    private TextView tv_protocol;
    private CashSubItemBean mCashSubItemBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mCashSubItemBean = (CashSubItemBean)(mBundle.getSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY));
            mCardNum = mBundle.getString(GlobalParams.WITHDRAWALS_CARD_NUM_KEY);
            mBankName = mBundle.getString(GlobalParams.WITHDRAWALS_BANK_NAME_KEY);
            mRepayTimes = mBundle.getString(GlobalParams.WITHDRAWALS_REPAY_TIMES_KEY);
        }
        if(null!=mCashSubItemBean){
            initView();
        }
    }

    private void initView() {
        if (mCashSubItemBean != null) {
            int withdrawalsAmountInt = (int)(Double.parseDouble(mCashSubItemBean.getWithdrawal_amount())/100);
            tv_withdrawals_amount.setText(withdrawalsAmountInt +"元");
            if("2".equals(mBundle.getString(GlobalParams.WITHDRAWALS_REPAY_UNIT))){
                tv_repay_time.setText(mRepayTimes + "日");
                tv_repay_type.setText("到期应还");
            }else{
                tv_repay_time.setText(mRepayTimes + "个月");
                tv_repay_type.setText("每月还款");
            }

            tv_repay_month.setText(((int)(Double.parseDouble(mCashSubItemBean.getRepay_total())/100))+"元");
            int transferAmount = (int)(Double.parseDouble(mCashSubItemBean.getTransfer_amount())/100);
            tv_arrival_amount.setText(transferAmount+"元");
            int proceduresAmount = withdrawalsAmountInt - transferAmount;
            if (proceduresAmount > 0) {
                ll_procedures.setVisibility(View.VISIBLE);
                tv_procedures_amount.setText((proceduresAmount)+"元");
            } else {
                ll_procedures.setVisibility(View.GONE);
            }
            tv_bank_name.setText(mBankName);
            tv_bank_card_num.setText(mCardNum);
        }
    }
    @Override
    protected int setContentView() {
        return R.layout.activity_withdrawals_confirm;
    }

    @Override
    protected void findViews() {
        tv_withdrawals_amount=(TextView)findViewById(R.id.tv_withdrawals_amount);
        tv_repay_time=(TextView)findViewById(R.id.tv_repay_time);
        tv_repay_month=(TextView)findViewById(R.id.tv_repay_month);
        tv_arrival_amount=(TextView)findViewById(R.id.tv_arrival_amount);
        tv_procedures_amount=(TextView)findViewById(R.id.tv_procedures_amount);
        tv_bank_name=(TextView)findViewById(R.id.tv_bank_name);
        tv_bank_card_num=(TextView)findViewById(R.id.tv_bank_card_num);
        ll_procedures = (LinearLayout)findViewById(R.id.ll_procedures);
        bt_confirm=(Button) findViewById(R.id.bt_confirm);
        tv_repay_type=(TextView)findViewById(R.id.tv_repay_type);
        cb_agree=(CheckBox) findViewById(R.id.cb_agree);
        tv_protocol=(TextView)findViewById(R.id.cb_agree);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bt_confirm.setBackgroundResource(R.drawable.button_red);
                    bt_confirm.setClickable(true);
                    bt_confirm.setEnabled(true);
                }else{
                    bt_confirm.setBackgroundResource(R.drawable.button_gray);
                    bt_confirm.setClickable(false);
                    bt_confirm.setEnabled(false);
                }
            }
        });
        tv_protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                if(null!=mBundle) {
                    if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
                        gotoActivity(mContext, InputPayPwdActivity.class, mBundle);
                    } else {
                        gotoActivity(mContext, SetPayPwdActivity.class, mBundle);
                    }
                    backActivity();
                }
                break;
            case R.id.tv_protocol:
                Bundle bundle = new Bundle();
                bundle.putInt("pro_type", GlobalParams. PRO_SERVER); // 用户服务器协议
                gotoActivity(mContext, ProtocolActivity.class, bundle);
                break;
        }
    }
}
