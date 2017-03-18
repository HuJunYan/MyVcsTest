package com.maibei.merchants.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.base.MyApplication;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.net.base.UserUtil;

/**
 * Created by huxiao on 2016/6/22.
 */
public class FinalActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_ok;
    private TextView tv_tishi_message;
    private TextView tv_tixian_sus;
    private TextView tv_result;
    private String money;
    private String moneyCount;
    private String from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_present_success;
    }

    @Override
    protected void findViews() {
        bt_ok = (Button)findViewById(R.id.bt_ok);
        tv_tishi_message = (TextView)findViewById(R.id.tv_tishi_message);
        tv_tixian_sus=(TextView)findViewById(R.id.tv_tixian_sus);
        tv_result=(TextView)findViewById(R.id.tv_result);
    }

    @Override
    protected void setListensers() {
        bt_ok.setOnClickListener(this);
    }

    private void init(){
        Bundle bundle = this.getIntent().getExtras();
        money = bundle.getString("extAmount");
        moneyCount = bundle.getString("extAmountCount");
        from=bundle.getString("type");
        if("transfer".equals(from)){
            tv_tixian_sus.setText("转入成功");
            tv_result.setText("转入成功");
            tv_tishi_message.setText("尊敬的用户，" + money + "元已成功注入您的余额");
        }else{

            tv_tishi_message.setText("尊敬的用户，" + money + "元已成功汇入您的绑定银行卡，预计24小时内到账。");
        }

    }

    @Override
    public void onClick(View view) {
        if("widthdrawal".equals(from)) {
            double displayAmount = Double.valueOf(moneyCount) / 100;//总金额（元）
            Log.d("ret", "moneyCount = " + moneyCount);
            UserUtil.setBalance(mContext, moneyCount);//最新金额数
        }
        gotoActivity(FinalActivity.this, MainActivity.class, null);
        ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
