package com.maibai.user.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.maibai.user.R;
import com.maibai.user.adapter.InstallmentAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CalculateInstallmentBean;
import com.maibai.user.model.CalculateInstallmentItemBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.ChangeToInstallment;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.view.ImageTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class TurnToInstallmentActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_confirm;
    private Bundle mBundle;
    private CalculateInstallmentBean mCalculateInstallmentBean;
    private ImageTextView itv_total_price;
    private ImageTextView itv_repay_date;
    private String mBillId;
    private ChangeToInstallment mChangeToInstallment;
    private ListView lv_installment;
    private List<CalculateInstallmentItemBean> datas;
    private int selectNum = 0;
    private InstallmentAdapter adapter;
    private ScrollView sv_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeToInstallment = new ChangeToInstallment(mContext);
        mBundle = getIntent().getExtras();
        mCalculateInstallmentBean = (CalculateInstallmentBean) mBundle.getSerializable("CalculateInstallmentBean");
        mBillId = mBundle.getString("consume_id");
        init();
    }

    private void init() {
        // float 保留2位小数点
        DecimalFormat fnum = new DecimalFormat("##0.00");
        itv_total_price.setRightText("￥" + fnum.format(Float.valueOf(mCalculateInstallmentBean.getConsume_amount()) / 100));
        datas = mCalculateInstallmentBean.getData();
        adapter = new InstallmentAdapter(mContext, datas);
        setRepayData();
        lv_installment.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lv_installment);
        sv_container.smoothScrollTo(0,0);
        lv_installment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectNum = position;
                setRepayData();
            }
        });
    }

    private void setRepayData() {
        if(selectNum>=datas.size())
            selectNum=datas.size()-1;
        for (int i = 0; i < datas.size(); i++) {
            if (selectNum == i) {
                datas.get(i).setChecked(true);
            } else {
                datas.get(i).setChecked(false);
            }
        }
        itv_repay_date.setRightText("每月" + mCalculateInstallmentBean.getData().get(selectNum).getRepay_date() + "之前");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_turn_to_installment;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        itv_total_price = (ImageTextView) findViewById(R.id.itv_total_price);
        itv_repay_date = (ImageTextView) findViewById(R.id.itv_repay_date);
        lv_installment = (ListView) findViewById(R.id.lv_installment);
        sv_container=(ScrollView)findViewById(R.id.sv_container);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                try {
                    JSONObject mJson = new JSONObject();
                    mJson.put("customer_id", UserUtil.getId(mContext));
                    mJson.put("consume_id", mBillId);
                    String repayId=datas.get(selectNum).getId();
                    if(null==repayId){
                        repayId="";
                    }
                    mJson.put("repay_id",repayId);
                    mChangeToInstallment.changeToInstallment(mJson, null, true, new BaseNetCallBack<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean responseBean) {
                            SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.IS_MY_PAGE_NEED_REFRESH, "need_refresh");
                            mBundle.putInt("selectNum", selectNum);
                            gotoActivity(mContext, TurnToInstallmentSuccessActivity.class, mBundle);
                            backActivity();
                            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION, null);
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e));
                }
                break;
        }
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
