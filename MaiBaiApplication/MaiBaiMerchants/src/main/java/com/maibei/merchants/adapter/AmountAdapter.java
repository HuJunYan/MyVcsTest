package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.SaleListItemBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 14658 on 2016/7/7.
 */
public class AmountAdapter extends CommonAdapter<SaleListItemBean> {

    private OnAmountItemClickListener mListener;

    public AmountAdapter(Context context, List<SaleListItemBean> datas) {
        super(context, datas, null);
    }

    public AmountAdapter(Context context, List<SaleListItemBean> datas, OnAmountItemClickListener listener) {
        super(context, datas);
        this.mListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_amount_order, null);
            viewHolder.findViews(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SaleListItemBean item = mDatas.get(position);
        viewHolder.setData(item);
        return convertView;
    }
    class ViewHolder {
        TextView tv_item_user_name;
        TextView tv_item_amount;
        TextView tv_item_time;

        protected void findViews(View convertView) {
            tv_item_user_name = (TextView) convertView.findViewById(R.id.tv_item_user_name);
            tv_item_amount = (TextView) convertView.findViewById(R.id.tv_item_amount);
            tv_item_time = (TextView) convertView.findViewById(R.id.tv_item_time);
        }

        protected void setData(SaleListItemBean listItemBean){
            tv_item_user_name.setText(listItemBean.getCustomer_name()+"");
            String consumeAmount=listItemBean.getConsume_amount();
            if(null!=consumeAmount && !"".equals(consumeAmount)) {
                int amount = (int)((double)Double.valueOf(consumeAmount));
                tv_item_amount.setText("￥ " + amount/100 + "元");
            }
            if(listItemBean.getConsume_time() != null && !"".equals(listItemBean.getConsume_time())){
                long time = Long.parseLong(listItemBean.getConsume_time());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sdf.format(new Date(time*1000));
                tv_item_time.setText(date);
            }
        }
    }

    public interface OnAmountItemClickListener {
        void onAmountItemClick(int position);
    }
}
