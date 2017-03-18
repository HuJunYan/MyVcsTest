package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.BankListItemBean;
import com.maibei.merchants.model.WithdrawHistoryItemBean;

import java.util.List;

/**
 * Created by 14658 on 2016/8/6.
 */
public class BankCardAdapter extends CommonAdapter<BankListItemBean> {

    public BankCardAdapter(Context context, List<BankListItemBean> datas){
        super(context, datas, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bank_list, null);
            viewHolder.findViews(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BankListItemBean item = mDatas.get(position);
        viewHolder.setData(item);
        return convertView;
    }

    class ViewHolder {
        TextView tv_item_bank;
        TextView tv_item_cardno;
        protected void findViews(View convertView) {
            tv_item_bank = (TextView)convertView.findViewById(R.id.tv_item_bank);
            tv_item_cardno = (TextView)convertView.findViewById(R.id.tv_item_cardno);
        }
        protected void setData(BankListItemBean bankListItemBean){
            tv_item_bank.setText(bankListItemBean.getBank_name());
            tv_item_cardno.setText(bankListItemBean.getBank_id());
        }
    }

}
