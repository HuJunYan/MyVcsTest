package com.maibai.user.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.MainUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.view.MyTextView;

public class ChangePwdSelectActivity extends BaseActivity implements View.OnClickListener{

private MyTextView mtv_change_login_password,mtv_change_pay_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_change_pwd_select;
    }

    @Override
    protected void findViews() {
        mtv_change_login_password=(MyTextView)findViewById(R.id.mtv_change_login_password);
        mtv_change_pay_password=(MyTextView)findViewById(R.id.mtv_change_pay_password);
    }

    @Override
    protected void setListensers() {
        mtv_change_login_password.setOnClickListener(this);
        mtv_change_pay_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.mtv_change_login_password:
                bundle.putString("type", GlobalParams.CHANGE_LOGIN_PASSWORD);
                gotoActivity(mContext,ForgetPasswordActivity.class,bundle);
                break;
            case R.id.mtv_change_pay_password:
                if(!"0".equals(UserUtil.getIsSetPayPass(mContext))){
                    bundle.putString("type",GlobalParams.CHANGE_PAY_PASSWORD);
                    gotoActivity(mContext,ForgetPasswordActivity.class,bundle);
                }else {
                    ToastUtil.showToast(mContext,"您未授信，不可修改支付密码");
                }
                break;
        }
    }
}
