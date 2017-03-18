package com.maibai.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.maibai.cash.R;

import java.util.List;

/**
 * Created by zhangchi on 2016/6/29.
 */
public class AddressListAdapter extends CommonAdapter<PoiInfo> {
    public AddressListAdapter(Context context, List<PoiInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false);
            holder.findViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(mDatas.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_address;

        protected void findViews(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
        }

        protected void setData(PoiInfo poi) {
            if (poi != null) {
                tv_name.setText(poi.name);
                tv_address.setText(poi.address);
            }
        }
    }
}
