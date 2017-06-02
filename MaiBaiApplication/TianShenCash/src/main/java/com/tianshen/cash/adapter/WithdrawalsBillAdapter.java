package com.tianshen.cash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.WithdrawalsBillItemBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class WithdrawalsBillAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Control control;
    List<WithdrawalsBillItemBean> datas;
    Context context;

    public WithdrawalsBillAdapter(Context context, List<WithdrawalsBillItemBean> datas, Control control) {
        this.context = context;
        this.datas = datas;
        this.control = control;
        inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.get_cash_bill_item, null);
            viewHolder = new ViewHolder();
            viewHolder.cb_select_item = (CheckBox) convertView.findViewById(R.id.cb_select_item);
            viewHolder.tv_repay_money = (TextView) convertView.findViewById(R.id.tv_repay_money);
            viewHolder.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_total_amount = (TextView) convertView.findViewById(R.id.tv_total_amount);
            viewHolder.tv_total_time = (TextView) convertView.findViewById(R.id.tv_total_time);
            viewHolder.tv_surplus_days = (TextView) convertView.findViewById(R.id.tv_surplus_days);
            viewHolder.rl_right_content = (RelativeLayout) convertView.findViewById(R.id.rl_right_content);
            viewHolder.tv_overdue = (TextView) convertView.findViewById(R.id.tv_overdue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String isOverdue = datas.get(position).getIs_overdue();
        if (null == isOverdue) {
            isOverdue = "";
        }

        if ("1".equals(isOverdue)) {
            viewHolder.tv_overdue.setVisibility(View.VISIBLE);
            String overdue = datas.get(position).getLate_fee();
            if ("".equals(overdue) || null == overdue) {
                overdue = "0";
            }
            viewHolder.tv_overdue.setText("(含逾期费" + Double.valueOf(overdue) / 100 + "元)");
            viewHolder.tv_surplus_days.setTextColor(ContextCompat.getColor(context, R.color.dark_green));
        } else {
            viewHolder.tv_overdue.setVisibility(View.GONE);
            viewHolder.tv_surplus_days.setTextColor(ContextCompat.getColor(context, R.color.default_text_color));
        }
        viewHolder.cb_select_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                control.checkedAll(position, isChecked);
            }
        });
        String repay_amount = datas.get(position).getRepay_amount();
        if (null == repay_amount || "".equals(repay_amount)) {
            repay_amount = "0";
        }
        viewHolder.rl_right_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.gotoDetail(position);
            }
        });
        viewHolder.tv_repay_money.setText(Double.valueOf(repay_amount) / 100 + "元");
        viewHolder.tv_times.setText("[" + datas.get(position).getCurrent_times() + "/" + datas.get(position).getTotal_times() + "期]");
        viewHolder.tv_type.setText("取现消费—");
        String totalAmount = datas.get(position).getTotal_amount();
        if ("".equals(totalAmount) || null == totalAmount) {
            totalAmount = "0";
        }
        viewHolder.tv_total_amount.setText(Double.valueOf(totalAmount) / 100 + "");
        if ("2".equals(datas.get(position).getRepay_unit())) {
            viewHolder.tv_total_time.setText(datas.get(position).getTimer() + "天");
        } else {
            viewHolder.tv_total_time.setText(datas.get(position).getTimer() + "个月");
        }

        viewHolder.tv_surplus_days.setText(datas.get(position).getRemainder_repay_days());
        boolean isChecked = datas.get(position).isChecked();
        if (isChecked != true && isChecked != false) {
            isChecked = false;
        }
        viewHolder.cb_select_item.setChecked(isChecked);
        return convertView;
    }

    public class ViewHolder {
        CheckBox cb_select_item;
        TextView tv_repay_money;
        TextView tv_times;
        TextView tv_type;
        TextView tv_total_amount;
        TextView tv_total_time;
        TextView tv_surplus_days;
        TextView tv_overdue;
        RelativeLayout rl_right_content;
    }

    public interface Control {
        void checkedAll(int position, boolean isChecked);

        void gotoDetail(int position);
    }
}
