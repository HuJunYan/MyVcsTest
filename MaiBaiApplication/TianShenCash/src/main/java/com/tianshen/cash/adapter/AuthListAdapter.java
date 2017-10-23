package com.tianshen.cash.adapter;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCenterActivity;
import com.tianshen.cash.model.AuthCenterItemBean;

import java.util.List;

/**
 * Created by cuiyue on 2017/8/14.
 */

public class AuthListAdapter extends BaseMultiItemQuickAdapter<AuthCenterItemBean, BaseViewHolder> {


    private Handler mHandler;

    public AuthListAdapter(List<AuthCenterItemBean> data, Handler handler) {
        super(data);
        this.mHandler = handler;
        addItemType(AuthCenterItemBean.NORMAL_TYPE, R.layout.item_auth_list_normal);
        addItemType(AuthCenterItemBean.TXT_TYPE, R.layout.item_auth_list_txt);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthCenterItemBean authCenterItemBean) {
        switch (helper.getItemViewType()) {
            case AuthCenterItemBean.NORMAL_TYPE:
                final String name = authCenterItemBean.getName();
                int drawable_id = authCenterItemBean.getDrawable_id();
                String status = authCenterItemBean.getStatus();//0没有认证，1已经认证
                helper.setText(R.id.tv_auth_center_name_item, name);
                helper.setImageResource(R.id.iv_auth_center_item, drawable_id);
                TextView tv_auth_center_status_item = helper.getView(R.id.tv_auth_center_status_item);
                if ("0".equals(status)) {//没有认证
                    if (authCenterItemBean.isOptionalType()){
                        tv_auth_center_status_item.setText("待填写");
                    }else {
                        tv_auth_center_status_item.setText("待填写");
                    }
                    tv_auth_center_status_item.setTextColor(mContext.getResources().getColor(R.color.global_txt_orange));
                } else {
                    tv_auth_center_status_item.setText("已认证");
                    tv_auth_center_status_item.setTextColor(mContext.getResources().getColor(R.color.global_popular_color));
                }

                Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_arraw_right);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                tv_auth_center_status_item.setCompoundDrawables(null, null, drawable, null);
                tv_auth_center_status_item.setCompoundDrawablePadding(15);

                View rl_auth_center_item = helper.getView(R.id.rl_auth_center_item);
                rl_auth_center_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message msg = Message.obtain();
                        msg.what = AuthCenterActivity.MSG_CLICK_ITEM;
                        msg.obj = name;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case AuthCenterItemBean.TXT_TYPE:
                break;
        }
    }
}
