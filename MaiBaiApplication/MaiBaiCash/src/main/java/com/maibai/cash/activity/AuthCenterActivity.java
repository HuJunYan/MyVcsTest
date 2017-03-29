package com.maibai.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maibai.cash.R;
import com.maibai.cash.adapter.AuthCenterAdapter;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.model.AuthCenterItemBean;
import com.maibai.cash.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户认证中心页面
 */

public class AuthCenterActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_auth_center_back)
    TextView tvAuthCenterBack;
    @BindView(R.id.tv_auth_center_post)
    TextView tvAuthCenterPost;
    @BindView(R.id.xrecyclerview_auth_center)
    XRecyclerView xrecyclerviewAuthCenter;

    private ArrayList<AuthCenterItemBean> mAuthCenterItemBeans;

    public static final int MSG_CLICK_ITEM = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_CLICK_ITEM:
                    int position = (int) message.obj;
                    onClickItem(position);
                    break;
            }
        }

    };

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_center;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthCenterBack.setOnClickListener(this);
        tvAuthCenterPost.setOnClickListener(this);
        mAuthCenterItemBeans = initXRecyclerviewData();
        initXRecyclerview();
    }

    private void initXRecyclerview() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerviewAuthCenter.setLayoutManager(layoutManager);
        xrecyclerviewAuthCenter.setLoadingMoreEnabled(false);
        xrecyclerviewAuthCenter.setPullRefreshEnabled(false);
        AuthCenterAdapter mAdapter = new AuthCenterAdapter(mContext, mAuthCenterItemBeans, mHandler);
        xrecyclerviewAuthCenter.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_center_back:
                backActivity();
                break;
            case R.id.tv_auth_center_post:
                ToastUtil.showToast(mContext, "点击了提交");
                break;
        }
    }

    /**
     * 点击了列表
     */
    private void onClickItem(int position) {
        ToastUtil.showToast(mContext, "点击了--->" + position);
    }

    /**
     * 得到初始化数据(从本地得到)
     */
    private ArrayList<AuthCenterItemBean> initXRecyclerviewData() {
        ArrayList<AuthCenterItemBean> authCenterItemBeans = new ArrayList<>();

        AuthCenterItemBean authCenterItemBean0 = new AuthCenterItemBean();
        authCenterItemBean0.setName("身份认证");
        authCenterItemBean0.setDrawable_id(R.drawable.ic_auth_center_identity_item);

        AuthCenterItemBean authCenterItemBean1 = new AuthCenterItemBean();
        authCenterItemBean1.setName("个人信息");
        authCenterItemBean1.setDrawable_id(R.drawable.ic_auth_center_info_item);

        AuthCenterItemBean authCenterItemBean2 = new AuthCenterItemBean();
        authCenterItemBean2.setName("紧急联系人");
        authCenterItemBean2.setDrawable_id(R.drawable.ic_auth_center_urgent_item);

        AuthCenterItemBean authCenterItemBean3 = new AuthCenterItemBean();
        authCenterItemBean3.setName("收款银行卡");
        authCenterItemBean3.setDrawable_id(R.drawable.ic_auth_center_bank_card_item);

        AuthCenterItemBean authCenterItemBean4 = new AuthCenterItemBean();
        authCenterItemBean4.setName("手机运营商");
        authCenterItemBean4.setDrawable_id(R.drawable.ic_auth_center_phone_item);

        AuthCenterItemBean authCenterItemBean5 = new AuthCenterItemBean();
        authCenterItemBean5.setName("更多信息可选填");
        authCenterItemBean5.setDrawable_id(R.drawable.ic_auth_center_more_item);

        authCenterItemBeans.add(authCenterItemBean0);
        authCenterItemBeans.add(authCenterItemBean1);
        authCenterItemBeans.add(authCenterItemBean2);
        authCenterItemBeans.add(authCenterItemBean3);
        authCenterItemBeans.add(authCenterItemBean4);
        authCenterItemBeans.add(authCenterItemBean5);

        return authCenterItemBeans;
    }
}
