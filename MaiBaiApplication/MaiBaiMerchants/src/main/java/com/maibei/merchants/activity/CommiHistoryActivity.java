package com.maibei.merchants.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.CommisionAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.CommisionBean;
import com.maibei.merchants.model.CommisionListBean;
import com.maibei.merchants.net.api.GetMerchantCommision;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommiHistoryActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_return_home;
    private TextView tv_title;
    private ListView lv_data;
    private CommisionAdapter adapter;
    private List<CommisionListBean> commisionListBeanList=new ArrayList<CommisionListBean>();
    private String date="-1";
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(null==bundle){
            return;
        }
        date=bundle.getString("date");
        type=bundle.getString("type");
        if("transed".equals(type)){
            tv_title.setText("已提佣金");
        }else if("late".equals(type)){
            tv_title.setText("逾期佣金佣金");
        }
        adapter=new CommisionAdapter(mContext,commisionListBeanList);
        lv_data.setAdapter(adapter);
        getCommHistoryDetail(date);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_commi_history;
    }

    @Override
    protected void findViews() {
        ib_return_home=(ImageButton)findViewById(R.id.ib_return_home);
        tv_title=(TextView)findViewById(R.id.tv_title);
        lv_data=(ListView)findViewById(R.id.lv_data);
    }

    @Override
    protected void setListensers() {
        ib_return_home.setOnClickListener(this);
    }
    private void getCommHistoryDetail(String date){
        try {
            if("-1".equals(date)){
                return;
            }
            GetMerchantCommision getMerchantCommision=new GetMerchantCommision(mContext);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            jsonObject.put("offset","0");
            jsonObject.put("date_time",date);
            jsonObject.put("length","1000");

            jsonObject.put("type","transed".equals(type)?"1":"2");
            getMerchantCommision.getMerchantCommision(jsonObject, null, true, new BaseNetCallBack<CommisionBean>() {
                @Override
                public void onSuccess(CommisionBean paramT) {
                    init(paramT);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void init(CommisionBean param){
        commisionListBeanList.clear();
        if(null!=param.getData().getCommision_list()) {
            commisionListBeanList.addAll(param.getData().getCommision_list());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return_home:
                backActivity();
                break;
        }
    }
}
