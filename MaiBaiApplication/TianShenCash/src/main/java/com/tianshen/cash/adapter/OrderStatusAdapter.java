package com.tianshen.cash.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.model.UserConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserConfig.Data.Consume> consume_status_list;
    SimpleDateFormat simpleDateFormat;


    public OrderStatusAdapter(Context context, UserConfig userConfig) {
        this.mContext = context;
        this.consume_status_list = userConfig.getData().getConsume_status_list();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd mm:ss");
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

        Drawable ic_time_line_old = mContext.getResources().getDrawable(R.drawable.ic_time_line_old);
        Drawable ic_time_line_current = mContext.getResources().getDrawable(R.drawable.ic_time_line_current);

        if (position == 0) { //顶部时间轴
            viewHolder.iv_time_line_line_top.setVisibility(View.GONE);
            viewHolder.iv_time_line_status.setImageDrawable(ic_time_line_old);
        } else if (position == consume_status_list.size() - 1) { //尾部时间轴
            viewHolder.iv_time_line_line_bottom.setVisibility(View.GONE);
            viewHolder.iv_time_line_status.setImageDrawable(ic_time_line_current);
        } else { //中间的时间轴
            viewHolder.iv_time_line_line_top.setVisibility(View.VISIBLE);
            viewHolder.iv_time_line_line_bottom.setVisibility(View.VISIBLE);
            viewHolder.iv_time_line_status.setImageDrawable(ic_time_line_old);
        }

        UserConfig.Data.Consume consume = consume_status_list.get(position);
        String consume_status_title = consume.getConsume_status_title();
        String consume_status_description = consume.getConsume_status_description();
        String consume_status_time = consume.getConsume_status_time();
        viewHolder.iv_time_line_title.setText(consume_status_title);
        viewHolder.iv_time_line_description.setText(consume_status_description);
        try {
            Date date = simpleDateFormat.parse(consume_status_time);
            viewHolder.iv_time_line_time.setText(new SimpleDateFormat("MM.dd mm:ss").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            viewHolder.iv_time_line_time.setText(consume_status_time);
        }

    }

    public void setData(UserConfig userConfig) {
        this.consume_status_list = userConfig.getData().getConsume_status_list();
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (consume_status_list == null) {
            return 0;
        }
        return consume_status_list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_time_line_status;
        public ImageView iv_time_line_line_top;
        public ImageView iv_time_line_line_bottom;
        public TextView iv_time_line_title;
        public TextView iv_time_line_description;
        public TextView iv_time_line_time;

        public ViewHolder(View view) {
            super(view);
            iv_time_line_status = (ImageView) view.findViewById(R.id.iv_time_line_status);
            iv_time_line_line_top = (ImageView) view.findViewById(R.id.iv_time_line_line_top);
            iv_time_line_line_bottom = (ImageView) view.findViewById(R.id.iv_time_line_line_bottom);
            iv_time_line_title = (TextView) view.findViewById(R.id.iv_time_line_title);
            iv_time_line_description = (TextView) view.findViewById(R.id.iv_time_line_description);
            iv_time_line_time = (TextView) view.findViewById(R.id.iv_time_line_time);
        }
    }
}
