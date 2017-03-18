package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.model.CalculateInstallmentBean;
import com.maibai.user.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class TurnToInstallmentSuccessActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_installment_tip;
    private Button bt_confirm;
    private int selectNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle mBundle = getIntent().getExtras();
            selectNum=mBundle.getInt("selectNum",0);
            if (mBundle != null) {
                CalculateInstallmentBean calculateInstallmentBean = (CalculateInstallmentBean)(mBundle.getSerializable("CalculateInstallmentBean"));
                String amount=calculateInstallmentBean.getConsume_amount();
                if(null==amount||"".equals(amount)){
                    amount="0";
                }
                String mRepayDate = calculateInstallmentBean.getData().get(selectNum).getRepay_date();
                if(null==mRepayDate){
                    mRepayDate="";
                }
                String tipText = "尊敬的用户，我们将您的" +  Double.valueOf(Long.valueOf(amount))/100 + "元次月付款转为分期付款，请在每月" + mRepayDate + "准时还款，以免影响您的信用，祝您生活愉快";
                tv_installment_tip.setText(tipText);
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
            String tipText = "尊敬的用户，我们将您的次月付款转为分期付款，请在每月准时还款，以免影响您的信用，祝您生活愉快";
            tv_installment_tip.setText(tipText);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_turn_to_installment_success;
    }

    @Override
    protected void findViews() {
        tv_installment_tip = (TextView) findViewById(R.id.tv_installment_tip);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
                break;
        }
    }
}
