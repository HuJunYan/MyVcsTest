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
import com.tianshen.cash.model.AuthCenterItemBean;
import com.tianshen.cash.model.UserConfig;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private UserConfig mUserConfig;
    private Context mContext;
    private ArrayList<UserConfig.Data.Consume> consume_status_list;


    public OrderStatusAdapter(Context context, UserConfig userConfig) {
        this.mContext = context;
        this.mUserConfig = userConfig;
        this.consume_status_list = mUserConfig.getData().getConsume_status_list();
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_status, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

    }

    public void setData(UserConfig userConfig) {
        this.mUserConfig = userConfig;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
//        if (consume_status_list == null) {
//            return 0;
//        }
        return 10;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl_auth_center_item;
        public ImageView iv_auth_center_item;
        public TextView tv_auth_center_name_item;
        public TextView tv_auth_center_status_item;

        public ViewHolder(View view) {
            super(view);
            rl_auth_center_item = (RelativeLayout) view.findViewById(R.id.rl_auth_center_item);
            iv_auth_center_item = (ImageView) view.findViewById(R.id.iv_auth_center_item);
            tv_auth_center_name_item = (TextView) view.findViewById(R.id.tv_auth_center_name_item);
            tv_auth_center_status_item = (TextView) view.findViewById(R.id.tv_auth_center_status_item);

        }
    }
}
