package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.RecordPopBean;
import com.tianshen.cash.net.api.RecordPopup;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenrongshang on 16/9/16.
 */
public class VerifyFailActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_confirm;
    private TextView tv_reason;
    private Bundle mBundle;
    private TitleBar tb_title_bar;
    private String reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        reason = UserUtil.getCashCreditReason(mContext);
        if (null == reason) {
            reason = "";
        }
        initView();
    }

    private void initView() {
        tv_reason.setText(reason);
        recordPopup();
        /*switch (mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)){
            case GlobalParams.REFUSE_BY_PERSON_TYPE:
                tb_title_bar.setIvLeftVisible(View.VISIBLE);

                break;
            case GlobalParams.REFUSE_BY_MACHINE_TYPE:
                tb_title_bar.setIvLeftVisible(View.GONE);
                bt_confirm.setVisibility(View.GONE);
                bt_confirm.setText("退出");
                break;
        }*/
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_verify_fail;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        tb_title_bar = (TitleBar) findViewById(R.id.tb_title_bar);

    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (null == mBundle) {
                MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                return;
            }
            switch (v.getId()) {
                case R.id.bt_confirm:
                    if (GlobalParams.REFUSE_BY_PERSON_TYPE == mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)) {
                        MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                    } else if (GlobalParams.REFUSE_BY_MACHINE_TYPE == mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)) {
                        System.exit(0);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 所有需要统一处理的onKeyDown写在这个if里面
        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);

            }
        }
        return false;
    }

    @Override
    protected boolean isOnKeyDown() {
        return true;
        /*if (null==mBundle)
            return true;
        switch (mBundle.getInt(GlobalParams.REFUSE_TYPE_KEY)){
            case GlobalParams.REFUSE_BY_PERSON_TYPE:
                return true;
            case GlobalParams.REFUSE_BY_MACHINE_TYPE:
                return false;
        }
        return false;*/
    }

    private void recordPopup() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "1");
            jsonObject.put("customer_id", TianShenUserUtil.getUserId(mContext));
            RecordPopup recordPopup = new RecordPopup(mContext);
            recordPopup.recordPopup(jsonObject, null, false, new BaseNetCallBack<RecordPopBean>() {
                @Override
                public void onSuccess(RecordPopBean paramT) {

                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
