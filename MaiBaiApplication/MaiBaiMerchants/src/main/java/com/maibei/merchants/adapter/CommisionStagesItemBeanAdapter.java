package com.maibei.merchants.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.maibei.merchants.R;
import com.maibei.merchants.model.CommisionStagesItemBean;

import java.util.List;

/**
 * Created by chunpengguo on 2016/12/31.
 */

public class CommisionStagesItemBeanAdapter extends BaseAdapter {
    Context context;
    List<CommisionStagesItemBean> datas;
    LayoutInflater inflater;
    public CommisionStagesItemBeanAdapter(Context context, List<CommisionStagesItemBean> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
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
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.commit_stage_item,null);
            viewHolder.iv_type=(ImageView)view.findViewById(R.id.iv_type);
            viewHolder.tv_time=(TextView)view.findViewById(R.id.tv_time);
            viewHolder.tv_amount=(TextView)view.findViewById(R.id.tv_amount);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.tv_time.setText(datas.get(i).getTime());
        viewHolder.tv_amount.setText(datas.get(i).getMoney());
        switch (datas.get(i).getStatus()){
            case "0":
                viewHolder.iv_type.setImageResource(R.mipmap.in_effect);
                break;
            case "1":
                viewHolder.iv_type.setImageResource(R.mipmap.shop_attained);
                break;
            case "2":
                viewHolder.iv_type.setImageResource(R.mipmap.no_effect);
                break;
            case "3":
                viewHolder.iv_type.setImageResource(R.mipmap.overdue);
                viewHolder.tv_time.setTextColor(ContextCompat.getColor(context,R.color.commit_detail_late_coloe));
                viewHolder.tv_amount.setTextColor(ContextCompat.getColor(context,R.color.commit_detail_late_coloe));
                break;
            case "4":
                viewHolder.iv_type.setImageResource(R.mipmap.privilege);
                viewHolder.tv_time.setTextColor(ContextCompat.getColor(context,R.color.commit_detail_youhui_color));
                viewHolder.tv_amount.setTextColor(ContextCompat.getColor(context,R.color.commit_detail_youhui_color));
        }
        return view;
    }
    public class ViewHolder{
        ImageView iv_type;
        TextView tv_time;
        TextView tv_amount;
    }
}
