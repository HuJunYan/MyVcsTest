package com.tianshen.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.SmallOrderItemTipDataItemBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class OrderTipAdapter extends BaseAdapter {
    Context context;
    List<SmallOrderItemTipDataItemBean> data;
    LayoutInflater inflater;
    public OrderTipAdapter(Context context,List<SmallOrderItemTipDataItemBean> data){
        this.data=data;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return null==data?0:data.size();
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
        if(null==convertView){
            viewHolder =new ViewHolder();
            convertView=inflater.inflate(R.layout.order_item_tip_item,null);
            viewHolder.tv_key=(TextView)convertView.findViewById(R.id.tv_key);
            viewHolder.tv_value=(TextView)convertView.findViewById(R.id.tv_value);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_key.setText(data.get(position).getName());
        viewHolder.tv_value.setText(data.get(position).getMoney());
        return convertView;
    }
    public class ViewHolder{
        TextView tv_key;
        TextView tv_value;
    }
}
