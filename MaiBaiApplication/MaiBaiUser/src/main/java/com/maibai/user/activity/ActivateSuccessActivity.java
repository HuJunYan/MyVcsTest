package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.view.TitleBar;

public class ActivateSuccessActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_result;
    private TextView tv_success;
    private Button bt_confirm;
    private TitleBar tb_title;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mBundle = getIntent().getExtras();
        boolean isSuccess=mBundle.getBoolean("isSuccess",false);
        showResult(isSuccess);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_activate_success;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        iv_result = (ImageView) findViewById(R.id.iv_result);
        tv_success = (TextView) findViewById(R.id.tv_success);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                ((MyApplication)getApplication()).clearTempActivityInBackStack(MainActivity.class);
                break;
        }
    }

    private void showResult(boolean isSuccess) {
        if (isSuccess) {
            tb_title.setTitle("激活成功");
            iv_result.setVisibility(View.VISIBLE);
            tv_success.setText("恭喜，您的初始额度激活成功");
            bt_confirm.setText("好的");
            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION,null);
        } else {
            iv_result.setVisibility(View.GONE);
            ResponseBean responseBean = GsonUtil.json2bean(mBundle.getString("error_result"), ResponseBean.class);
            bt_confirm.setText("返回");
            if (mBundle.getBoolean("is_improve_type")) {
                tb_title.setTitle("提升额度失败");
                tv_success.setText("提额失敗,原因： " + responseBean.getMsg());
            } else {
                tb_title.setTitle("激活额度失败");
                tv_success.setText("激活失败,原因： " + responseBean.getMsg());
            }
        }
    }
}
