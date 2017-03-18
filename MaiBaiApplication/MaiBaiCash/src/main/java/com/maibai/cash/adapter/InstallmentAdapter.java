package com.maibai.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.model.CalculateInstallmentItemBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */

public class InstallmentAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<CalculateInstallmentItemBean> datas;

    public InstallmentAdapter(Context mContext, List<CalculateInstallmentItemBean> datas) {
        this.mContext = mContext;
        this.datas=datas;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null==datas?0:datas.size();
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
            convertView=inflater.inflate(R.layout.view_installment_item,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_is_checked=(ImageView)convertView.findViewById(R.id.iv_is_checked);
            viewHolder.tv_installment_count=(TextView)convertView.findViewById(R.id.tv_installment_count);
            viewHolder.tv_repay_price=(TextView)convertView.findViewById(R.id.tv_repay_price);
            viewHolder.tv_interest=(TextView)convertView.findViewById(R.id.tv_interest);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(datas.get(position).isChecked()){
            viewHolder.iv_is_checked.setImageResource(R.mipmap.selected);
        }else{
            viewHolder.iv_is_checked.setImageResource(R.mipmap.selection);
        }
        String repayTotal=datas.get(position).getRepay_total();
        if("".equals(repayTotal)||null==repayTotal){
            repayTotal="0";
        }
        String payInterest=datas.get(position).getPay_interest();
        if("".equals(payInterest)||null==payInterest){
            payInterest="0";
        }
        String repayTimes=datas.get(position).getRepay_times();
        if("".equals(repayTimes)||null==repayTimes){
            repayTimes="0";
        }
        viewHolder.tv_installment_count.setText(repayTimes);
        viewHolder.tv_repay_price.setText("¥"+(long)(Double.valueOf(repayTotal)/100));
        viewHolder.tv_interest.setText("¥"+(long)(Double.valueOf(payInterest)/100));
        return convertView;
    }
    public class ViewHolder{
        ImageView iv_is_checked;
        TextView tv_installment_count;
        TextView tv_repay_price;
        TextView tv_interest;
    }
}
