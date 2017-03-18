package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.model.WithdrawalsOrderBean;

public class WithdrawalsSuccessActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_withdrawals_amount;
    private TextView tv_repay_time;
    private TextView tv_repay_month;
    private TextView tv_transfer_amount;
    private TextView tv_bank_name;
    private TextView tv_bank_card_num;
    private Button bt_confirm;
    private TextView tv_time_borrow_success_time;
    private TextView tv_time_borrow_time;
    private WithdrawalsOrderBean withdrawalsOrderBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(null!=bundle){
            withdrawalsOrderBean=(WithdrawalsOrderBean)bundle.getSerializable("withdrawalsOrder");
            initView();
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_withdrawals_success;
    }

    private void initView(){
        int amountInt = (int)(Double.parseDouble(withdrawalsOrderBean.getData().getAmount())/100);
        tv_withdrawals_amount.setText(amountInt+"");
        tv_repay_time.setText(withdrawalsOrderBean.getData().getRepay_times()+"个月");
        String repayMonth=withdrawalsOrderBean.getData().getRepay_amount();
        if("".equals(repayMonth)||null==repayMonth){
            repayMonth="0";
        }
        tv_repay_month.setText((int)(Math.ceil((Double.valueOf(repayMonth))/100))+"元");
        String transferAmount=withdrawalsOrderBean.getData().getTransfer_amount();
        if(null==transferAmount||"".equals(transferAmount)){
            transferAmount="0";
        }
        tv_transfer_amount.setText((int)(Double.valueOf(transferAmount)/100)+"元");
        String bankName=withdrawalsOrderBean.getData().getBank_name();
        if(null==bankName){
            bankName="";
        }
        tv_bank_name.setText(bankName);
        String cardNum=withdrawalsOrderBean.getData().getCard_num();
        if(null==cardNum){
            cardNum="";
        }
        tv_bank_card_num.setText(cardNum);
        String borrowSuccessTime=withdrawalsOrderBean.getData().getTransfer_time();
        tv_time_borrow_success_time.setText(borrowSuccessTime);
        String borrowTime=withdrawalsOrderBean.getData().getConsume_time();
        tv_time_borrow_time.setText(borrowTime);
    }
    @Override
    protected void findViews() {
        tv_withdrawals_amount=(TextView)findViewById(R.id.tv_withdrawals_amount);
        tv_repay_time=(TextView)findViewById(R.id.tv_repay_time);
        tv_repay_month=(TextView)findViewById(R.id.tv_repay_month);
        tv_transfer_amount=(TextView)findViewById(R.id.tv_transfer_amount);
        tv_bank_name=(TextView)findViewById(R.id.tv_bank_name);
        tv_bank_card_num=(TextView)findViewById(R.id.tv_bank_card_num);
        bt_confirm=(Button) findViewById(R.id.bt_confirm);
        tv_time_borrow_success_time=(TextView)findViewById(R.id.tv_time_borrow_success_time);
        tv_time_borrow_time=(TextView)findViewById(R.id.tv_time_borrow_time);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                backActivity();
                break;
        }
    }
}
