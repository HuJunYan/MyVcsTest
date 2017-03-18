package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.WithdrawHistoryItemBean;

import java.util.List;

/**
 * Created by 14658 on 2016/7/28.
 */
public class WithdrawHistoryAdapter extends BaseAdapter {

    private String type="";
    private Context mContext;
    private List<WithdrawHistoryItemBean> datas;
    public WithdrawHistoryAdapter(Context context, List<WithdrawHistoryItemBean> datas,String type) {
        this.type=type;
        this.datas=datas;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return datas.size();
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
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tip_history, null);
            viewHolder.findViews(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        WithdrawHistoryItemBean item = datas.get(position);
        viewHolder.setData(item);
        return convertView;
    }

    class ViewHolder {
        TextView tv_item_tip_time;
        TextView tv_item_tip_amount;
        TextView tv_content;
        protected void findViews(View convertView) {
            tv_item_tip_time = (TextView)convertView.findViewById(R.id.tv_item_tip_time);
            tv_item_tip_amount = (TextView)convertView.findViewById(R.id.tv_item_tip_amount);
            tv_content=(TextView)convertView.findViewById(R.id.tv_content);
        }
        protected void setData(WithdrawHistoryItemBean withdrawHistoryItemBean){
            tv_item_tip_time.setText(withdrawHistoryItemBean.getWithdraw_time());
            if (withdrawHistoryItemBean.getAmount() != null && !"".equals(withdrawHistoryItemBean.getAmount())){
                double amount = Double.valueOf(withdrawHistoryItemBean.getAmount());
                tv_item_tip_amount.setText(amount/100+"");
            }
            if("orderWidthdrawal".equals(type)){
                tv_content.setText("提现");
            }else if("comm".equals(type)){
                tv_content.setText("转入提现额度");
            }
        }
    }

}
