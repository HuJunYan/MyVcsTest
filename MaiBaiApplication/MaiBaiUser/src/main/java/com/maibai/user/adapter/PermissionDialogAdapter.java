package com.maibai.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.maibai.user.R;
import com.maibai.user.model.PermissionHintItemBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class PermissionDialogAdapter extends BaseAdapter{

    Context mContext;
    List<PermissionHintItemBean> params;
    LayoutInflater inflater;

    public PermissionDialogAdapter(Context mContext, List<PermissionHintItemBean> params) {
        this.mContext = mContext;
        this.params = params;
        this.inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return params.size();
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
            convertView =inflater.inflate(R.layout.item_permission_dialog,null);
            viewHolder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            viewHolder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_content.setText(params.get(position).getHint());
        viewHolder.tv_num.setText(params.get(position).getStep());
        return convertView;
    }

    public class ViewHolder{
        TextView tv_content,tv_num;
    }

}
