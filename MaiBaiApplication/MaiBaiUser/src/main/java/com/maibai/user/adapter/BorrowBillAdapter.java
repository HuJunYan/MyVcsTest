package com.maibai.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.WithdrawalsRecordItemBean;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class BorrowBillAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ImageLoaderUtil imageLoaderUtil;
    private List<WithdrawalsRecordItemBean> data;
    public BorrowBillAdapter(Context context, List<WithdrawalsRecordItemBean>data) {
        this.mContext = context;
        inflater=LayoutInflater.from(context);
        this.data=data;
        imageLoaderUtil=new ImageLoaderUtil(mContext);
        imageLoader=imageLoaderUtil.getImageLoader();
        options=imageLoaderUtil.getNormalOption();
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.consumption_item,null);
            viewHolder.tv_consume_date=(TextView)convertView.findViewById(R.id.tv_consume_date);
            viewHolder.tv_repay_type=(TextView)convertView.findViewById(R.id.tv_repay_type);
            viewHolder.iv_merchan_logo=(ImageView)convertView.findViewById(R.id.iv_merchan_logo);
            viewHolder.tv_total_money=(TextView)convertView.findViewById(R.id.tv_total_money);
            viewHolder.tv_merchant_name=(TextView)convertView.findViewById(R.id.tv_merchant_name);
            viewHolder.rl_bt_holder=(RelativeLayout)convertView.findViewById(R.id.rl_bt_holder);
            int width=new GetTelephoneUtils(mContext).getWindowWidth()/ GlobalParams.CONSUMPTION_IMG_PROPORTION;
            int height=width* GlobalParams.IMG_HEIGHT/GlobalParams.IMG_WIDTH;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            viewHolder.iv_merchan_logo.setLayoutParams(params);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String amount=data.get(position).getAmount();
        if("".equals(amount)||null==amount){
            amount="0";
        }
        viewHolder.tv_total_money.setText("¥"+Double.valueOf(amount)/100);
        String consumTime=data.get(position).getConsume_time();
        if (null==consumTime){
            consumTime="";
        }
        viewHolder.tv_consume_date.setText(consumTime);
        String time=data.get(position).getRepay_times();
        if(null==time){
            time="";
        }
        viewHolder.tv_repay_type.setText("共"+time+"期");
        viewHolder.tv_merchant_name.setText("买呗现金贷平台");
        viewHolder.rl_bt_holder.setVisibility(View.GONE);
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_consume_date;
        private TextView tv_repay_type;
        private ImageView iv_merchan_logo;
        private TextView tv_merchant_name;
        private TextView tv_total_money;
        private RelativeLayout rl_bt_holder;

    }
}
