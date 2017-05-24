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
import com.tianshen.cash.event.SuperMarkerClickEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.SuperMarkerBean;
import com.tianshen.cash.net.api.GetSuperMarkerList;
import com.tianshen.cash.net.api.SubmitVerifyCode;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

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
        initSuperMarketListData();
    }

    private void initSuperMarketListData() {

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            GetSuperMarkerList getSuperMarkerList = new GetSuperMarkerList(mContext);
            getSuperMarkerList.getSuperMarkerList(jsonObject, null, true, new BaseNetCallBack<SuperMarkerBean>() {
                @Override
                public void onSuccess(SuperMarkerBean paramT) {
                    if (paramT == null) {
                        return;
                    }
                    mSuperMarketList = paramT.getData().getSupermarket_list();
                    initXRecyclerview();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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

    /**
     * 收到了点击某个市场的事件
     */
    @Subscribe
    public void onSuperMarkerClick(SuperMarkerClickEvent event) {
        String super_marker_url = event.getSuper_marker_url();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, super_marker_url);
        gotoActivity(mContext, WebActivity.class, bundle);
    }
}
