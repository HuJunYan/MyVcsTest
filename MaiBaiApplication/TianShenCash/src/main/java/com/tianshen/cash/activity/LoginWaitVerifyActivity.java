package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.model.SignInBean;
import com.tianshen.cash.view.TitleBar;

public class LoginWaitVerifyActivity extends BaseActivity implements View.OnClickListener{

    private TitleBar tb_title;
    private Button bt_confirm;
    private ImageView iv_result;
    private TextView tv_reason;
    private TextView tv_valuation,tv_amount,tv_add_time,tv_type,tv_merchant_name,tv_consume_amount,tv_reduce_amount,tv_repayment_time;
    private Bundle bundle;
    private SignInBean signInBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getIntent().getExtras();
        signInBean=(SignInBean)bundle.getSerializable("signInBean");
        initData();
    }
    private void initData(){
        switch (signInBean.getData().getCredit().getStatus()){
            case "2":
                tb_title.setTitle("待审核");
                iv_result.setImageResource(R.drawable.loading_audit);
                tv_reason.setVisibility(View.GONE);
                tv_valuation.setVisibility(View.GONE);
                tv_reason.setText("您的信息已成功提交\\n请等待系统审核");
                tv_amount.setVisibility(View.VISIBLE);
                tv_add_time.setVisibility(View.VISIBLE);
                tv_type.setVisibility(View.VISIBLE);
                tv_amount.setText(signInBean.getData().getCredit().getAmount());
                tv_add_time.setText(signInBean.getData().getCredit().getAdd_time());
                if("1".equals(signInBean.getData().getCredit().getType())){
                    tv_type.setText("次月还款");
                }else if("2".equals(signInBean.getData().getCredit().getType())){
                    tv_type.setText("分期还款");
                }else if("3".equals(signInBean.getData().getCredit().getType())){
                    tv_type.setText("提高额度");
                }
                if("3".equals(signInBean.getData().getCredit().getType())){
                    tv_merchant_name.setVisibility(View.GONE);
                    tv_consume_amount.setVisibility(View.GONE);
                    tv_reduce_amount.setVisibility(View.GONE);
                    tv_repayment_time.setVisibility(View.GONE);
                    break;
                } else{
                    tv_merchant_name.setVisibility(View.VISIBLE);
                    tv_consume_amount.setVisibility(View.VISIBLE);
                    tv_reduce_amount.setVisibility(View.VISIBLE);
                    tv_repayment_time.setVisibility(View.VISIBLE);
                    tv_merchant_name.setText(signInBean.getData().getCredit().getMerchant_name());
                    tv_consume_amount.setText(signInBean.getData().getCredit().getConsume_amount());
                    tv_reduce_amount.setText(signInBean.getData().getCredit().getReduce_amount());
                    tv_repayment_time.setText(signInBean.getData().getCredit().getRepayment_time());
                }
                break;
            case "3":
            case "4":
                tb_title.setTitle("审核未通过");
                tv_reason.setVisibility(View.VISIBLE);
                tv_valuation.setVisibility(View.VISIBLE);
                iv_result.setImageResource(R.drawable.pay_loser);
                tv_reason.setText(signInBean.getData().getCredit().getReason());
                tv_reason.setText(signInBean.getData().getCredit().getReason());
                tv_valuation.setText(signInBean.getData().getCredit().getValuation());
                tv_amount.setVisibility(View.GONE);
                tv_add_time.setVisibility(View.GONE);
                tv_type.setVisibility(View.GONE);
                tv_merchant_name.setVisibility(View.GONE);
                tv_consume_amount.setVisibility(View.GONE);
                tv_reduce_amount.setVisibility(View.GONE);
                tv_repayment_time.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_login_wait_verify;
    }

    @Override
    protected void findViews() {
        tb_title=(TitleBar)findViewById(R.id.tb_title);
        iv_result=(ImageView)findViewById(R.id.iv_result);
        tv_reason=(TextView)findViewById(R.id.tv_reason);
        bt_confirm=(Button)findViewById(R.id.bt_confirm);
        tv_valuation=(TextView)findViewById(R.id.tv_valuation);
        tv_amount=(TextView)findViewById(R.id.tv_amount);
        tv_add_time=(TextView)findViewById(R.id.tv_add_time);
        tv_type=(TextView)findViewById(R.id.tv_type);
        tv_merchant_name=(TextView)findViewById(R.id.tv_merchant_name);
        tv_consume_amount=(TextView)findViewById(R.id.tv_consume_amount);
        tv_reduce_amount=(TextView)findViewById(R.id.tv_reduce_amount);
        tv_repayment_time=(TextView)findViewById(R.id.tv_repayment_time);

    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                if("4".equals(signInBean.getData().getCredit().getStatus())){
                    System.exit(0);
                }
                if("3".equals(signInBean.getData().getCredit().getStatus())||"2".equals(signInBean.getData().getCredit().getStatus())){
                    backActivity();
                }
                break;
        }
    }
}
