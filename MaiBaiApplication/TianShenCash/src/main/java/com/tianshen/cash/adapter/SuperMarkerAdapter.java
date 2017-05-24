package com.tianshen.cash.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCenterActivity;
import com.tianshen.cash.event.SuperMarkerClickEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.model.AuthCenterItemBean;
import com.tianshen.cash.model.SuperMarkerBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SuperMarkerAdapter extends RecyclerView.Adapter<SuperMarkerAdapter.ViewHolder> {
    private ArrayList<SuperMarkerBean.Data.SuperMarketData> mSuperMarketList;
    private Context mContext;

    public SuperMarkerAdapter(Context context, ArrayList<SuperMarkerBean.Data.SuperMarketData> superMarketList) {
        this.mContext = context;
        this.mSuperMarketList = superMarketList;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_super_marker, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final SuperMarkerBean.Data.SuperMarketData superMarketData = mSuperMarketList.get(position);
        String name = superMarketData.getName();
        viewHolder.tv_super_marker_name_item.setText(name);
        viewHolder.rl_super_marker_root_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperMarkerClickEvent superMarkerClickEvent = new SuperMarkerClickEvent();
                String superMarketDataUrl = superMarketData.getUrl();
                superMarkerClickEvent.setSuper_marker_url(superMarketDataUrl);
                EventBus.getDefault().post(superMarkerClickEvent);
            }
        });
    }

    public void setData(ArrayList<SuperMarkerBean.Data.SuperMarketData> superMarketList) {
        this.mSuperMarketList = superMarketList;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (mSuperMarketList == null) {
            return 0;
        }
        return mSuperMarketList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl_super_marker_root_item;
        public TextView tv_super_marker_name_item;

        public ViewHolder(View view) {
            super(view);
            rl_super_marker_root_item = (RelativeLayout) view.findViewById(R.id.rl_super_marker_root_item);
            tv_super_marker_name_item = (TextView) view.findViewById(R.id.tv_super_marker_name_item);

        }
    }
}
