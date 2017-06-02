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
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.utils.GetTelephoneUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class WithdrawalsTypeAdapter extends BaseAdapter {
    List<WithdrawalsItemBean> datas;
    Context context;
    LayoutInflater layoutInflater;

    public WithdrawalsTypeAdapter(Context context, List<WithdrawalsItemBean> datas) {
        this.context = context;
        this.datas = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == datas ? 0 : datas.size();
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
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.stages_type_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_stage_type = (TextView) convertView.findViewById(R.id.tv_stage_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        boolean isCheck = datas.get(position).isCheck();

        if (isCheck) {
            viewHolder.tv_stage_type.setBackgroundResource(R.drawable.stages_type_item_pressed);
            viewHolder.tv_stage_type.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            viewHolder.tv_stage_type.setBackgroundResource(R.drawable.stages_type_item_normal);
            viewHolder.tv_stage_type.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        String repayTimes = datas.get(position).getRepay_times();
        if (null == repayTimes) {
            repayTimes = "0";
        }
        if ("2".equals(datas.get(position).getRepay_unit())) {
            viewHolder.tv_stage_type.setText(repayTimes + "天");
        } else {
            viewHolder.tv_stage_type.setText(repayTimes + "个月");
        }
        int width = new GetTelephoneUtils(context).getWindowWidth() / 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width * 10 / 30);
        viewHolder.tv_stage_type.setLayoutParams(params);
        return convertView;
    }

    public class ViewHolder {
        TextView tv_stage_type;
    }
}
