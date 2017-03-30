package com.maibai.cash.adapter;


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

import com.maibai.cash.R;
import com.maibai.cash.activity.AuthCenterActivity;
import com.maibai.cash.model.AuthCenterItemBean;

import java.util.ArrayList;

public class AuthCenterAdapter extends RecyclerView.Adapter<AuthCenterAdapter.ViewHolder> {
    private ArrayList<AuthCenterItemBean> mAuthCenterItemBeans;
    private Context mContext;
    private Handler mHandler;

    public AuthCenterAdapter(Context context, ArrayList<AuthCenterItemBean> authCenterItemBeans, Handler handler) {
        this.mContext = context;
        this.mAuthCenterItemBeans = authCenterItemBeans;
        this.mHandler = handler;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_auth_center, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        AuthCenterItemBean authCenterItemBean = mAuthCenterItemBeans.get(position);
        String name = authCenterItemBean.getName();
        int drawable_id = authCenterItemBean.getDrawable_id();
        String status = authCenterItemBean.getStatus();//0没有认证，1已经认证

        viewHolder.tv_auth_center_name_item.setText(name);
        viewHolder.iv_auth_center_item.setImageDrawable(mContext.getResources().getDrawable(drawable_id));

        if (position == mAuthCenterItemBeans.size() - 1) { //设置最后一个
            viewHolder.tv_auth_center_status_item.setText("待填写");
            viewHolder.tv_auth_center_name_item.setTextColor(mContext.getResources().getColor(R.color.global_txt_gray));
            viewHolder.tv_auth_center_status_item.setTextColor(mContext.getResources().getColor(R.color.global_txt_gray));
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_arraw_right2);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tv_auth_center_status_item.setCompoundDrawables(null, null, drawable, null);//设置TextView的drawable的位置(右边)
            viewHolder.tv_auth_center_status_item.setCompoundDrawablePadding(15);//设置图片和text之间的间距
        } else {
            if ("0".equals(status)) {//没有认证
                viewHolder.tv_auth_center_status_item.setText("待填写");
                viewHolder.tv_auth_center_status_item.setTextColor(mContext.getResources().getColor(R.color.global_txt_orange));
            } else {
                viewHolder.tv_auth_center_status_item.setText("已认证");
                viewHolder.tv_auth_center_status_item.setTextColor(mContext.getResources().getColor(R.color.global_txt_green));
            }
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_arraw_right);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tv_auth_center_status_item.setCompoundDrawables(null, null, drawable, null);
            viewHolder.tv_auth_center_status_item.setCompoundDrawablePadding(15);
        }

        viewHolder.rl_auth_center_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.what = AuthCenterActivity.MSG_CLICK_ITEM;
                msg.obj = position;
                mHandler.sendMessage(msg);
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (mAuthCenterItemBeans == null) {
            return 0;
        }
        return mAuthCenterItemBeans.size();
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
