package com.tianshen.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.GetBankListBean;


/**
 * Created by Administrator on 2016/8/19.
 */
public class BankCardInfoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    GetBankListBean bankListBean;

    public BankCardInfoAdapter(Context context, GetBankListBean bankListBean) {
        this.context = context;
        this.bankListBean = bankListBean;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == bankListBean) {
            return 0;
        } else {
            if (null == bankListBean.getData()) {
                return 0;
            }
        }
        return bankListBean.getData().size();
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
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.bank_card_list_item, null);
            viewHolder.bank_name = (TextView) convertView.findViewById(R.id.bank_name);
            viewHolder.bank_card_num = (TextView) convertView.findViewById(R.id.bank_card_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String bankName = bankListBean.getData().get(position).getBank_name();
        if (!("".equals(bankName) || null == bankName))
            viewHolder.bank_name.setText(bankName);
        String bankCardNum = bankListBean.getData().get(position).getCard_num();
        if (!("".equals(bankCardNum) || null == bankCardNum)) {
            if (bankCardNum.length() < 4) {
                viewHolder.bank_card_num.setText(bankCardNum);
            } else {
                viewHolder.bank_card_num.setText(bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length()));
            }
        }
        return convertView;
    }

    public class ViewHolder {
        TextView bank_name;
        TextView bank_card_num;

    }
}
