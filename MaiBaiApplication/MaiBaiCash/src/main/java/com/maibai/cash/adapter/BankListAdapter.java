package com.maibai.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.model.BankListBean;

/**
 * Created by Administrator on 2016/8/23.
 */
public class BankListAdapter extends BaseAdapter {
    BankListBean bankListBean;
    Context context;
    LayoutInflater inflater;

    public BankListAdapter(Context context, BankListBean bankListBean) {
        this.context = context;
        this.bankListBean = bankListBean;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(null==bankListBean){
            return 0;
        }else{
            if(null==bankListBean.getData()){
                return  0;
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
        if (convertView==null){
            convertView=inflater.inflate(R.layout.bank_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_bank_name=(TextView)convertView.findViewById(R.id.tv_bank_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String bankName=bankListBean.getData().get(position).getBank_name();
        if(null==bankName){
            bankName="";
        }
        viewHolder.tv_bank_name.setText(bankName);
        return convertView;
    }
    private class ViewHolder{
        TextView tv_bank_name;
    }
}
