package com.maibei.merchants.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.WithdrawHistoryAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.WithdrawHistoryBean;
import com.maibei.merchants.model.WithdrawHistoryItemBean;
import com.maibei.merchants.net.api.GetWithdrawHistory;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14658 on 2016/7/27.
 */
public class WithdrawHistoryActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_tip_history;
    private WithdrawHistoryAdapter withdrawHistoryAdapter;
    private ImageButton ib_return_home;
    List<WithdrawHistoryItemBean> itemBeanList = new ArrayList<WithdrawHistoryItemBean>();
    private Bundle mBundle;
    private String type;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle=getIntent().getExtras();
        if(null==mBundle){
            return;
        }
        type=mBundle.getString("type");
        if("orderWidthdrawal".equals(type)) {
            tv_title.setText("账户提现记录");
        }else if("comm".equals(type)){
            tv_title.setText("佣金提现记录");
        }
        withdrawHistoryAdapter = new WithdrawHistoryAdapter(this, itemBeanList,type);
        lv_tip_history.setAdapter(withdrawHistoryAdapter);
        getWithDrawHistory();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_tixian_history;
    }


    @Override
    protected void findViews() {
        lv_tip_history = (ListView)findViewById(R.id.lv_tip_history);
        ib_return_home = (ImageButton)findViewById(R.id.ib_return_home);
        tv_title=(TextView)findViewById(R.id.tv_title);
    }

    @Override
    protected void setListensers() {
        ib_return_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return_home:
                backActivity();
                break;
            default:
                break;
        }
    }

    private void getWithDrawHistory(){
        GetWithdrawHistory getWithdrawHistory = new GetWithdrawHistory(this);
        JSONObject mJson = new JSONObject();
        try {
            mJson.put("merchant_id", UserUtil.getMerchantId(mContext));
            mJson.put("offset", "0");
            mJson.put("length", "1000");
            if("Amount".equals(type)){
                mJson.put("get_type","0");
            }else if("comm".equals(type)){
                mJson.put("get_type","1");
            }
            getWithdrawHistory.getWithdrawHistory(mJson, new BaseNetCallBack<WithdrawHistoryBean>() {
                @Override
                public void onSuccess(WithdrawHistoryBean paramT) {
                    itemBeanList.addAll(paramT.getData());
                    withdrawHistoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }
}
