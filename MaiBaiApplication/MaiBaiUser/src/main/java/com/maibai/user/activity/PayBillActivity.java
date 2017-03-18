package com.maibai.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.maibai.user.R;
import com.maibai.user.adapter.BillDetailAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CalculateInstallmentBean;
import com.maibai.user.model.ConsumeBigBillItemBean;
import com.maibai.user.model.ConsumeBigBillListBean;
import com.maibai.user.net.api.CalculateInstallment;
import com.maibai.user.net.api.GetConsumeBigBillList;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PayBillActivity extends BaseActivity implements BillDetailAdapter.BillDetailItemListener{
    private ListView lv_bill_list;
    private List<ConsumeBigBillItemBean> mList = new ArrayList<ConsumeBigBillItemBean>();
    private BillDetailAdapter mAdapter;
    private Bundle mBundle;
    private MyBroadCast myBroadCase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if(null==mBundle){
            return;
        }
        registeBroadCast();

        ConsumeBigBillListBean consumeBigBillListBean=(ConsumeBigBillListBean)(mBundle.getSerializable("ConsumeBigBillListBean"));
        if (null==consumeBigBillListBean){
            return;
        }
        mList=consumeBigBillListBean.getData_big();
        mAdapter = new BillDetailAdapter(this, mList, this);
        lv_bill_list.setAdapter(mAdapter);
    }

    private void getBillList() {
        //获取订单列表
        try {
            JSONObject mGetBillJson = new JSONObject();
            mGetBillJson.put("customer_id", UserUtil.getId(mContext));
            GetConsumeBigBillList getConsumeBigBillList=new GetConsumeBigBillList(mContext);
            getConsumeBigBillList.getConsumeBigBillList(mGetBillJson, null, false, new BaseNetCallBack<ConsumeBigBillListBean>() {
                @Override
                public void onSuccess(ConsumeBigBillListBean paramT) {
                    mList.clear();
                    mList.addAll(paramT.getData_big());
                    mAdapter.notifyDataSetChanged();
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

    @Override
    protected int setContentView() {
        return R.layout.activity_bill_detail;
    }

    @Override
    protected void findViews() {
        lv_bill_list = (ListView) findViewById(R.id.lv_bill_list);
    }

    @Override
    protected void setListensers() {

    }

    @Override
    public void onImmediatelyClick(final int position) {
        Bundle bundle=new Bundle();
        bundle.putInt(GlobalParams.BILL_DETAIL_FROM_KEY,GlobalParams.BILL_DETAIL_FROM_PAY_BILL);
        bundle.putSerializable("ConsumeBigBillItemBean",mList.get(position));
        gotoActivity(mContext,WithdrawalsOrBillDetailActivity.class,bundle);
    }

    @Override
    public void onInstallmentClick(final int position) {
        try {
            JSONObject mJson = new JSONObject();
            mJson.put("customer_id", UserUtil.getId(mContext));
            mJson.put("consume_id", mList.get(position).getConsume_id());
            final CalculateInstallment mCalculateInstallment = new CalculateInstallment(mContext);
            mCalculateInstallment.calculateInstallment(mJson, null, true, new BaseNetCallBack<CalculateInstallmentBean>() {
                @Override
                public void onSuccess(CalculateInstallmentBean mCalculateInstallmentBean) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("CalculateInstallmentBean", mCalculateInstallmentBean);
                    mBundle.putString("consume_id", mList.get(position).getConsume_id());
                    gotoActivity(mContext, TurnToInstallmentActivity.class, mBundle);
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

    public void registeBroadCast() {
        myBroadCase = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.WX_REPAY_CONSUMPTION_SUCCESS_ACTION);
        registerReceiver(myBroadCase, intentFilter);
    }

    public class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (GlobalParams.WX_REPAY_CONSUMPTION_SUCCESS_ACTION.equals(intent.getAction())) {
                getBillList();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=myBroadCase) {
            unregisterReceiver(myBroadCase);
        }
    }
}
