package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.BankListBean;
import com.maibei.merchants.model.BankListItemBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class BankCardInfoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
   BankListBean bankListBean;

    public BankCardInfoAdapter(Context context,BankListBean bankListBean) {
        this.context = context;
        this.bankListBean = bankListBean;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
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
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.bank_card_list_item,null);
            viewHolder.bank_name=(TextView)convertView.findViewById(R.id.bank_name);
//            viewHolder.bank_card_type=(TextView)convertView.findViewById(R.id.bank_card_type);
            viewHolder.bank_card_num=(TextView)convertView.findViewById(R.id.bank_card_num);
            convertView.setTag(viewHolder);
        }
        viewHolder=(ViewHolder)convertView.getTag();
        if(!("".equals(bankListBean.getData().get(position).getBank_name())||null==bankListBean.getData().get(position).getBank_name()))
        viewHolder.bank_name.setText(bankListBean.getData().get(position).getBank_name());
        String bankCardNum=bankListBean.getData().get(position).getCard_num();
        if(!("".equals(bankCardNum)||null==bankCardNum))
        viewHolder.bank_card_num.setText(bankCardNum.substring(bankCardNum.length()-4,bankCardNum.length()));
        return convertView;
    }
    public class ViewHolder{
        TextView bank_name;
//        TextView bank_card_type;
        TextView bank_card_num;

    }
}
