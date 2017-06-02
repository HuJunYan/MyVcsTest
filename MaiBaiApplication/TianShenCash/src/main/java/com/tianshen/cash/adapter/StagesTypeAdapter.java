package com.tianshen.cash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.InstallmentInfoItemBean;
import com.tianshen.cash.utils.GetTelephoneUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class StagesTypeAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<InstallmentInfoItemBean> installmentInfoItemBeanList;

    public StagesTypeAdapter(Context mContext, List<InstallmentInfoItemBean> installmentInfoItemBeanList) {
        this.mContext = mContext;
        this.installmentInfoItemBeanList = installmentInfoItemBeanList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == installmentInfoItemBeanList ? 0 : installmentInfoItemBeanList.size();
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.stages_type_item, null);
            viewHolder.tv_stage_type = (TextView) convertView.findViewById(R.id.tv_stage_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_stage_type.setText(installmentInfoItemBeanList.get(position).getRepay_times() + "个月");
        int width = new GetTelephoneUtils(mContext).getWindowWidth() / 4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width * 10 / 27);
        viewHolder.tv_stage_type.setLayoutParams(params);

        if (installmentInfoItemBeanList.get(position).isFocus()) {
            viewHolder.tv_stage_type.setBackgroundResource(R.drawable.stages_type_item_pressed);
            viewHolder.tv_stage_type.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            viewHolder.tv_stage_type.setBackgroundResource(R.drawable.stages_type_item_normal);
            viewHolder.tv_stage_type.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tv_stage_type;
    }
}
