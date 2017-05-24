package com.tianshen.cash.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextSwitcher;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tianshen.cash.R;
import com.tianshen.cash.adapter.AuthCenterAdapter;
import com.tianshen.cash.adapter.SuperMarkerAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.SuperMarkerBean;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by cuiyue on 2017/5/24.
 */

public class SuperMarkerActivity extends BaseActivity {


    @BindView(R.id.xrecyclerview_super_marker)
    XRecyclerView xrecyclerview_super_marker;

    private SuperMarkerAdapter mAdapter;

    private ArrayList<SuperMarkerBean.Data.SuperMarketData> mSuperMarketList;

    @Override
    protected int setContentView() {
        return R.layout.activity_super_marker;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initXRecyclerview();
    }

    private void initData() {
        mSuperMarketList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            SuperMarkerBean.Data.SuperMarketData superMarketData = new SuperMarkerBean().new Data().new SuperMarketData();
            superMarketData.setName("name-->" + i);
            mSuperMarketList.add(superMarketData);
        }
    }


    private void initXRecyclerview() {
        if (mAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            xrecyclerview_super_marker.setLayoutManager(layoutManager);
            Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.item_divider);
            xrecyclerview_super_marker.addItemDecoration(xrecyclerview_super_marker.new DividerItemDecoration(dividerDrawable));
            xrecyclerview_super_marker.setLoadingMoreEnabled(false);
            xrecyclerview_super_marker.setPullRefreshEnabled(false);
            mAdapter = new SuperMarkerAdapter(mContext, mSuperMarketList);
            xrecyclerview_super_marker.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mSuperMarketList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
