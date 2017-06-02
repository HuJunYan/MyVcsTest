package com.tianshen.cash.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.WithdrawalsBillInfoItenBean;

import java.util.List;

/**
 * Created by m on 16-10-19.
 */
public class WithDrawalsBillDetailAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<WithdrawalsBillInfoItenBean> mDetailList;
    Control control;

    public WithDrawalsBillDetailAdapter(Context mContext, List<WithdrawalsBillInfoItenBean> detailList, Control control) {
        this.mContext = mContext;
        this.mDetailList = detailList;
        this.control = control;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mDetailList ? 0 : mDetailList.size();
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
            convertView = inflater.inflate(R.layout.view_bill_detail_item, null);
            viewHolder.tv_finish_index = (TextView) convertView.findViewById(R.id.tv_finish_index);
            viewHolder.tv_repay_total = (TextView) convertView.findViewById(R.id.tv_repay_total);
            viewHolder.iv_index_back = (ImageView) convertView.findViewById(R.id.iv_index_back);
            viewHolder.rl_bill_detail_item = (LinearLayout) convertView.findViewById(R.id.rl_bill_detail_item);
            viewHolder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
            viewHolder.tv_repay_late_fee = (TextView) convertView.findViewById(R.id.tv_repay_late_fee);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String princ = mDetailList.get(position).getPrincipal();
        if ("".equals(princ) || null == princ) {
            princ = "0";
        }
        String lateFeeText = mDetailList.get(position).getLate_fee();
        if ("".equals(lateFeeText) || null == lateFeeText) {
            lateFeeText = "0";
        }
//        double principal = (int)((Double.parseDouble(princ))/100);
        double lateFee = (Double.parseDouble(lateFeeText) / 100);
        String repayAmount = mDetailList.get(position).getRepay_amount();
        if ("".equals(repayAmount) || null == repayAmount) {
            repayAmount = "0";
        }
        final boolean isCheck = mDetailList.get(position).isChecked();

        switch (mDetailList.get(position).getState()) {
            case "0":
                viewHolder.tv_finish_index.setVisibility(View.VISIBLE);
                viewHolder.iv_index_back.setVisibility(View.INVISIBLE);
                viewHolder.tv_finish_index.setText(position + 1 + "");
                viewHolder.iv_index_back.setVisibility(View.INVISIBLE);
                viewHolder.tv_repay_total.setText((Double.valueOf(repayAmount) / 100) + "元");
//                viewHolder.tv_repay_total.setText((principal+lateFee)+"元");
                if (lateFee > 0) {
                    viewHolder.tv_repay_late_fee.setText("(含逾期" + lateFee + "元)");
                } else {
                    viewHolder.tv_repay_late_fee.setVisibility(View.GONE);
                }
                viewHolder.tv_explain.setText(mDetailList.get(position).getTime());
                viewHolder.tv_finish_index.setTextColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_textColor_gray));
                viewHolder.tv_repay_total.setTextColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_textColor_gray));
                viewHolder.tv_repay_late_fee.setTextColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_textColor_gray));
                viewHolder.tv_explain.setTextColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_textColor_gray));
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_back_gray));
                break;
            case "1":

                viewHolder.tv_repay_total.setText((Double.valueOf(repayAmount) / 100) + "元");
                if (isCheck) {
                    viewHolder.tv_finish_index.setVisibility(View.GONE);
                    viewHolder.iv_index_back.setImageResource(R.drawable.repayment_select);
                } else {
                    viewHolder.tv_finish_index.setVisibility(View.VISIBLE);
                    viewHolder.tv_finish_index.setText(position + 1 + "");
                    viewHolder.iv_index_back.setImageResource(R.drawable.repayment_select_line);
                    viewHolder.tv_finish_index.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                }
                if (lateFee > 0) {
                    viewHolder.tv_repay_late_fee.setText("(含逾期" + lateFee + "元)");
                    viewHolder.tv_repay_late_fee.setTextColor(Color.argb(255, 255, 0, 0));
                } else {
                    viewHolder.tv_repay_late_fee.setVisibility(View.GONE);
                }
                viewHolder.tv_explain.setText(mDetailList.get(position).getTime());
                viewHolder.tv_repay_total.setTextColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress));
                viewHolder.tv_repay_late_fee.setTextColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress));
                viewHolder.tv_explain.setTextColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress));
                if (isCheck) {
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_back_gray));
                } else {
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                }
                break;
            case "2":
                viewHolder.tv_repay_total.setText((Double.valueOf(repayAmount) / 100) + "元");
                if (isCheck) {
                    viewHolder.tv_finish_index.setVisibility(View.GONE);
                    viewHolder.iv_index_back.setImageResource(R.drawable.repayment_select);
                } else {
                    viewHolder.tv_finish_index.setVisibility(View.VISIBLE);
                    viewHolder.tv_finish_index.setText(position + 1 + "");
                    viewHolder.iv_index_back.setImageResource(R.drawable.repayment_select_line);
                    viewHolder.tv_finish_index.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                }
                if (lateFee > 0) {
                    viewHolder.tv_repay_late_fee.setText("(含逾期" + lateFee + "元)");
                    viewHolder.tv_repay_late_fee.setTextColor(Color.argb(255, 255, 0, 0));
                } else {
                    viewHolder.tv_repay_late_fee.setVisibility(View.GONE);
                }
                viewHolder.tv_explain.setText(mDetailList.get(position).getTime());
                viewHolder.tv_repay_total.setTextColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress));
                viewHolder.tv_repay_late_fee.setTextColor(ContextCompat.getColor(mContext, R.color.seek_bar_progress));
                viewHolder.tv_explain.setTextColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_textColor_gray));
                if (isCheck) {
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.borrow_bill_detail_back_gray));
                } else {
                    convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                }
                break;
        }
        viewHolder.rl_bill_detail_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.check(position, !isCheck);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView iv_index_back;
        TextView tv_finish_index;
        TextView tv_repay_total;
        TextView tv_explain;
        TextView tv_repay_late_fee;
        LinearLayout rl_bill_detail_item;
    }

    public interface Control {
        public void check(int position, boolean isCheck);
    }
}
