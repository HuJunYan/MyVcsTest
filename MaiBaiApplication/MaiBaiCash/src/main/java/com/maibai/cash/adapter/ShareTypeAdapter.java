package com.maibai.cash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.model.ShareTypeBean;

import java.util.List;

/**
 * Created by 14658 on 2016/8/10.
 */
public class ShareTypeAdapter extends CommonAdapter<ShareTypeBean> {

    public ShareTypeAdapter(Context context, List<ShareTypeBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.share_listitem, parent, false);
            holder.findViews(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDatas != null && mDatas.get(position) != null) {
            holder.setDatas(mDatas.get(position));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView share_listitem_icon;
        TextView share_listitem_title;
        protected void findViews(View convertView) {
            share_listitem_icon = (ImageView)convertView.findViewById(R.id.share_listitem_icon);
            share_listitem_title = (TextView)convertView.findViewById(R.id.share_listitem_title);
        }
        protected void setDatas(ShareTypeBean bean){
            share_listitem_icon.setImageResource(bean.drawable);
            share_listitem_title.setText(bean.name);
        }
    }

}
