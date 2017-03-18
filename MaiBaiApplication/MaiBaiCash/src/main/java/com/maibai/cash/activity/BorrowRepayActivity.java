package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.base.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrowRepayActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_total_amount, tv_merchant_name, tv_repay_type, tv_time, tv_order_num;
    private Button bt_confirm;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        initView();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_borrow_repay;
    }

    private void initView() {
        tv_total_amount.setText(bundle.getString("amount"));
        tv_merchant_name.setText(bundle.getString("merchant"));
        tv_repay_type.setText(bundle.getString("repay_type"));
        tv_time.setText(getTime());
    }

    private String getTime(){
        SimpleDateFormat    formatter    =   new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
        Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
    @Override
    protected void findViews() {
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        tv_repay_type = (TextView) findViewById(R.id.tv_repay_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);

    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
                backActivity();
                break;
        }
    }
}
