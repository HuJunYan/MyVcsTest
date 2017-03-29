package com.maibai.cash.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maibai.cash.R;
import com.maibai.cash.adapter.AuthCenterAdapter;
import com.maibai.cash.base.BaseActivity;
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

        initXRecyclerview();
    }

    private void initXRecyclerview() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerviewAuthCenter.setLayoutManager(layoutManager);
        xrecyclerviewAuthCenter.setLoadingMoreEnabled(false);
        xrecyclerviewAuthCenter.setPullRefreshEnabled(false);
        ArrayList listData = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            listData.add("item" + i);
        }
        AuthCenterAdapter mAdapter = new AuthCenterAdapter(listData);
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
}
