package com.maibei.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.model.CityListItemBean;
import com.maibei.merchants.model.CountyListItemBean;
import com.maibei.merchants.model.ProvinceBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class AddressSelecterAdapter extends BaseAdapter{
    List datas;
    Context context;
    LayoutInflater inflater;
    private final String STATUS_COUNTRY="country";
    private final String STATUS_CITY="city";
    private final String STATUS_PROVINCE="province";
    private final String STATUS_INIT="init";
    String status="";
    public AddressSelecterAdapter(List datas, Context context, String status){
        this.status=status;
       this.datas=datas;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
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
        ViewHolder viewHolder;
        if(null==convertView){
            convertView=inflater.inflate(R.layout.view_address,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_address_name=(TextView)convertView.findViewById(R.id.tv_address_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        String address="";
        switch (status){
            case STATUS_PROVINCE:
                address=((ProvinceBean)(datas.get(position))).getProvince_name();
                break;
            case STATUS_CITY:
                address=((CityListItemBean)(datas.get(position))).getCity_name();
                break;
            case STATUS_COUNTRY:
                address=((CountyListItemBean)(datas.get(position))).getCounty_name();
                break;
        }
        viewHolder.tv_address_name.setText(address);
        return convertView;
    }
    public class ViewHolder{
        TextView tv_address_name;
    }
}
