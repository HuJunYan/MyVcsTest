package com.maibai.user.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.OrderRefreshBean;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by chenrongshang on 16/9/16.
 */
public class VerifyFailActivity extends BaseActivity implements View.OnClickListener{
    private Button bt_confirm;
    private TextView tv_reason;
    private Bundle mBundle;
    private TitleBar tb_title_bar;
    private String reason;
    private OrderRefreshBean orderRefreshBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle=getIntent().getExtras();
        if(null!=mBundle) {
            orderRefreshBean = (OrderRefreshBean) mBundle.getSerializable("orderRefreshBean");
        }else{
            return;
        }
        if(null==orderRefreshBean){
            int type = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);
            if (GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY == type) {
                reason = UserUtil.getCashCreditReason(mContext);
            } else {
                reason= UserUtil.getReason(mContext);
            }
        }
        else{
            reason=orderRefreshBean.getData().getReason();
        }
        initView();
    }

    private void initView(){
        tv_reason.setText(reason);
        switch (mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)){
            case GlobalParams.REFUSE_BY_PERSON_TYPE:
                tb_title_bar.setIvLeftVisible(View.VISIBLE);

                break;
            case GlobalParams.REFUSE_BY_MACHINE_TYPE:
                tb_title_bar.setIvLeftVisible(View.GONE);
                bt_confirm.setVisibility(View.GONE);
                bt_confirm.setText("退出");
                break;
        }
    }
    @Override
    protected int setContentView() {
        return R.layout.activity_verify_fail;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button)findViewById(R.id.bt_confirm);
        tv_reason=(TextView)findViewById(R.id.tv_reason);
        tb_title_bar=(TitleBar)findViewById(R.id.tb_title_bar);

    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null==mBundle){
//            ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
            gotoMainActivity();
            return;
        }
        switch (v.getId()){
            case R.id.bt_confirm:
                if(GlobalParams.REFUSE_BY_PERSON_TYPE==mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)){
//                    ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
                    gotoMainActivity();
                }else if(GlobalParams.REFUSE_BY_MACHINE_TYPE==mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)) {
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 所有需要统一处理的onKeyDown写在这个if里面
        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                gotoMainActivity();
//              ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
            }
        }
        return false;
    }

    @Override
    protected boolean isOnKeyDown() {
        if (null==mBundle)
            return true;
        switch (mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)){
            case GlobalParams.REFUSE_BY_PERSON_TYPE:
                return true;
            case GlobalParams.REFUSE_BY_MACHINE_TYPE:
                return false;
        }
        return false;
    }

    private void gotoMainActivity() {
        try {
            ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            gotoActivity(VerifyFailActivity.this, MainActivity.class, null);
            finish();
        }
    }
}
