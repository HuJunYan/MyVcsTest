package com.tianshen.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.StatisticsRollDataBean;

import java.util.List;

/**
 * Created by chunpengguo on 2017/1/14.
 */

public class PrompAdapter extends BaseAdapter {
    Context context;
    List<StatisticsRollDataBean> data;
    LayoutInflater inflater;
    public PrompAdapter(Context context, List<StatisticsRollDataBean> data) {
        this.context = context;
        this.data = data;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null==data?0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(null==view){
            view=inflater.inflate(R.layout.promp_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_name);
            viewHolder.tv_money=(TextView)view.findViewById(R.id.tv_money);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.tv_name.setText(data.get(i).getMobile());
        String money=data.get(i).getMoney();
        if(null==money||"".equals(money)){
            money="0";
        }
        viewHolder.tv_money.setText(Integer.valueOf(money)/100+"");
        return view;
    }
    public class ViewHolder{
        TextView tv_name,tv_money;
    }
}
