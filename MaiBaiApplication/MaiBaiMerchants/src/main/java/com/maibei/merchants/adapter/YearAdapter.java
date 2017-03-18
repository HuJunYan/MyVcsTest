package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maibei.merchants.R;

import java.util.List;

/**
 * Created by 14658 on 2016/8/7.
 */
public class YearAdapter extends CommonAdapter<String> {

    public YearAdapter(Context context, List<String> datas){
        super(context, datas, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_year, null);
            viewHolder.findViews(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String item = mDatas.get(position);
        viewHolder.setData(item);
        return convertView;
    }

    class ViewHolder {
        TextView tv_item_year;
        protected void findViews(View convertView) {
            tv_item_year = (TextView)convertView.findViewById(R.id.tv_item_year);
        }
        protected void setData(String yearBean){
            tv_item_year.setText(yearBean);
        }
    }
}
