package com.maibei.merchants.activity;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.CommisionAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.base.MyApplication;
import com.maibei.merchants.model.CommisionBean;
import com.maibei.merchants.model.CommisionListBean;
import com.maibei.merchants.net.api.GetMerchantCommision;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.view.DetialGallery;
import com.maibei.merchants.view.ScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommiDetailActivity extends BaseActivity implements View.OnClickListener {
    private CommisionBean commisionBean;
    private ImageView iv_back;
    private ImageView iv_issue;
    private Button bt_back_home;
    private TextView tv_commi_had_transfer;
    private TextView tv_amount_late;
    private ScrollListView slv_comm_detail;
    private String date = "0";
    private CommisionAdapter adapter;
    private LinearLayout ll_commi_had_transfer, ll_amount_late;
    private List<CommisionListBean> commisionListBeanList = new ArrayList<CommisionListBean>();
    private DetialGallery gr_content;
    private MyGalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommisionAdapter(mContext, commisionListBeanList);
        slv_comm_detail.setAdapter(adapter);
        gr_content.setSpacing((getWindowWidth() - BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.commission_detail_down).getWidth()-BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.commission_value).getWidth() ) / 2);

        gr_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   String year=date.split("-")[0];
                   String month=date.split("-")[1];
                    if(position==1){
                        getCommHistoryDetail(("1".equals(month)||"01".equals(month))?(Integer.parseInt(year)-1)+"-12":year+"-"+(Integer.parseInt(month)-1));
                    }else if(position==3){
                        getCommHistoryDetail("12".equals(month)?(Integer.parseInt(year)+1)+"-1":year+"-"+(Integer.parseInt(month)+1));
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       getCommHistoryDetail(date);
    }

    private void updateView() {
        if (null == commisionBean) {
            return;
        }
        if (null == commisionBean.getData()) {
            return;
        }

        List<CommisionListBean> currentList = commisionBean.getData().getCommision_list();
        commisionListBeanList.clear();
        if (null != currentList) {
            commisionListBeanList.addAll(currentList);
        }
        adapter.notifyDataSetChanged();
        String had_transfer = commisionBean.getData().getCommission_get();
        if ("".equals(had_transfer) || null == had_transfer) {
            had_transfer = "0";
        }
        tv_commi_had_transfer.setText(Double.valueOf(had_transfer) / 100 + "");
        String fine_amount = commisionBean.getData().getCommission_fine();
        if ("".equals(fine_amount) || null == fine_amount) {
            fine_amount = "0";
        }
        tv_amount_late.setText(Double.valueOf(fine_amount) / 100 + "");
        date=commisionBean.getData().getDate_time();
        if("".equals(date)||null==date){
            return;
        }
        galleryAdapter=new MyGalleryAdapter();
        gr_content.setAdapter(galleryAdapter);
        gr_content.setSelection(2);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_commi_detail;
    }

    private void getCommHistoryDetail(final String date) {
        try {
            GetMerchantCommision getMerchantCommision = new GetMerchantCommision(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            jsonObject.put("offset", "0");
            jsonObject.put("date_time", date);
            jsonObject.put("length", "1000");
            jsonObject.put("type", "0");
            getMerchantCommision.getMerchantCommision(jsonObject, gr_content, true, new BaseNetCallBack<CommisionBean>() {
                @Override
                public void onSuccess(CommisionBean paramT) {
                    commisionBean = paramT;
                    updateView();
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
    protected void findViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_issue = (ImageView) findViewById(R.id.iv_issue);
        bt_back_home = (Button) findViewById(R.id.bt_back_home);
        tv_commi_had_transfer = (TextView) findViewById(R.id.tv_commi_had_transfer);
        tv_amount_late = (TextView) findViewById(R.id.tv_amount_late);
        slv_comm_detail = (ScrollListView) findViewById(R.id.slv_comm_detail);
        ll_commi_had_transfer = (LinearLayout) findViewById(R.id.ll_commi_had_transfer);
        ll_amount_late = (LinearLayout) findViewById(R.id.ll_amount_late);
        gr_content = (DetialGallery) findViewById(R.id.gr_content);
    }

    @Override
    protected void setListensers() {
        iv_back.setOnClickListener(this);
        iv_issue.setOnClickListener(this);
        bt_back_home.setOnClickListener(this);
        ll_commi_had_transfer.setOnClickListener(this);
        ll_amount_late.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.ll_commi_had_transfer:
                bundle.putString("date", date);
                bundle.putString("type", "transed");
                gotoActivity(mContext, CommiHistoryActivity.class, bundle);
                break;
            case R.id.ll_amount_late:
                bundle.putString("date", date);
                bundle.putString("type", "late");
                gotoActivity(mContext, CommiHistoryActivity.class, bundle);
                break;
            case R.id.iv_back:
                backActivity();
                break;
            case R.id.iv_issue:
                break;
            case R.id.bt_back_home:
                gotoActivity(mContext, MainActivity.class, null);
                ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
                break;
        }
    }

    private class MyGalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                viewHolder.iv_circel = (ImageView) convertView.findViewById(R.id.iv_circel);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String year=date.split("-")[0];
            String month=date.split("-")[1];
            String pre_commission=commisionBean.getData().getPre_commission();
            String next_commission=commisionBean.getData().getNext_commission();
            String amount=commisionBean.getData().getCommission_total();
            if("".equals(amount)||null==amount){
                amount="0";
            }
            if("".equals(pre_commission)||null==pre_commission){
                pre_commission="0";
            }
            if("".equals(next_commission)||null==next_commission){
                next_commission="0";
            }
            switch (position){
                case 0:
                case 4:
                    viewHolder.iv_circel.setImageResource(R.mipmap.commission_detail_down);
                    viewHolder.tv_date.setText("0");
                    viewHolder.tv_amount.setText("0");
                    break;
                case 1:
                    viewHolder.iv_circel.setImageResource(R.mipmap.commission_detail_down);
                    viewHolder.tv_date.setText(("1".equals(month)||"01".equals(month))?(Integer.parseInt(year)-1)+"年12月":year+"年"+(Integer.parseInt(month)-1)+"月");
                    viewHolder.tv_amount.setText(Double.valueOf(pre_commission)/100+"");
                    break;
                case 2:
                    viewHolder.iv_circel.setImageResource(R.mipmap.commission_value);
                    viewHolder.tv_date.setText(year+"年"+month+"月");
                    viewHolder.tv_amount.setText(Double.valueOf(amount)/100+"");
                    break;
                case 3:
                    viewHolder.iv_circel.setImageResource(R.mipmap.commission_detail_down);
                    viewHolder.tv_date.setText("12".equals(month)?(Integer.parseInt(year)+1)+"年1月":year+"年"+(Integer.parseInt(month)+1)+"月");
                    viewHolder.tv_amount.setText(Double.valueOf(next_commission)/100+"");
                    break;
            }

            return convertView;
        }

        public class ViewHolder {
            private ImageView iv_circel;
            private TextView tv_date;
            private TextView tv_amount;
        }
    }

    public int getWindowWidth() {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}
