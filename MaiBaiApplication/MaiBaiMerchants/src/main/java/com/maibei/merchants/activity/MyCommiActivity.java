package com.maibei.merchants.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.CommisionBean;
import com.maibei.merchants.net.api.GetMerchantCommision;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCommiActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back, iv_menu;
    private RelativeLayout rl_commi_detail;
    private RelativeLayout rl_transfer;
    private String commision_all;
    private TextView tv_wait_extract;
    private CommisionBean commisionBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_my_commi;
    }

    @Override
    protected void findViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        rl_commi_detail=(RelativeLayout)findViewById(R.id.rl_commi_detail);
        rl_transfer=(RelativeLayout)findViewById(R.id.rl_transfer);
        tv_wait_extract=(TextView)findViewById(R.id.tv_wait_extract);
    }

    @Override
    protected void setListensers() {
        iv_back.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        rl_commi_detail.setOnClickListener(this);
        rl_transfer.setOnClickListener(this);
    }

    private void initData(){

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            jsonObject.put("offset","0");
            jsonObject.put("length","1000");
            jsonObject.put("type","0");
            GetMerchantCommision getMerchantCommision=new GetMerchantCommision(mContext);
            getMerchantCommision.getMerchantCommision(jsonObject, null, true, new BaseNetCallBack<CommisionBean>() {
                @Override
                public void onSuccess(CommisionBean paramT) {
                    commisionBean=paramT;
                    commision_all=paramT.getData().getCommision_all();
                    if(null==commision_all||"".equals(commision_all)){
                        commision_all="0";
                    }
                    tv_wait_extract.setText(Double.valueOf(commision_all)/100+"");
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.iv_back:
                backActivity();
                break;
            case R.id.iv_menu:
                bundle.putString("type","comm");
                gotoActivity(mContext,WithdrawHistoryActivity.class,bundle);
                break;
            case R.id.rl_commi_detail:
                if(null==commisionBean){
                    ToastUtil.showToast(mContext,"当前无账单数据");
                    return;
                }
                gotoActivity(mContext,CommiDetailActivity.class,null);

                break;
            case R.id.rl_transfer:
                Bundle bundle2=new Bundle();
                bundle2.putString("commision_all",commision_all);
                gotoActivity(mContext,TransferActivity.class,bundle2);
                break;
        }
    }
}
