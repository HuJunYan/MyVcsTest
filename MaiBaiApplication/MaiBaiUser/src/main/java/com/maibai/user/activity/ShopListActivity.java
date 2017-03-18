package com.maibai.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.maibai.user.R;
import com.maibai.user.adapter.ShopListAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.MerchantTypeItemBean;
import com.maibai.user.model.NearByMerchantItemBean;
import com.maibai.user.model.NearByMerchantListBean;
import com.maibai.user.net.api.GetNearByList;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.view.PullToRefreshView;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends BaseActivity implements ShopListAdapter.OnShopItemClickListener, TitleBar.TitleBarListener{
    private int begin_line, end_line;
    private TitleBar tb_title;
    private TextView tv_shop;
    private ListView lv_shop_list;
    private ShopListAdapter mAdapter;
    private List<NearByMerchantItemBean> mList = new ArrayList<NearByMerchantItemBean>();
    private LatLng mLatLng;
    private View view;
    private GetNearByList mGetNearByList;
    private Bundle mBundle;
    private MerchantTypeItemBean merchantTypeItemBean;

    private List<NearByMerchantItemBean> currentShopList = new ArrayList<NearByMerchantItemBean>();

    private int SHOP_UPDATE_LENGTH = 1000;//每次加载更多时添加的数量；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetNearByList = new GetNearByList(mContext);
        init();
        getNearbyBusinesses(0, true);
    }

    private void getNearbyBusinesses(int offset, final boolean isClear) {
        try {
            SharedPreferencesUtil sp = SharedPreferencesUtil.getInstance(mContext);
            String locationString;
            if (mLatLng != null) {
                Log.d("ret", "location = " + mLatLng.latitude + "," + mLatLng.longitude);
                locationString = mLatLng.latitude + "," + mLatLng.longitude;
                sp.putString(GlobalParams.LAST_LOCATION_POSITION_KEY, locationString);
            } else {
                Log.d("ret", "location is null");
                locationString = sp.getString(GlobalParams.LAST_LOCATION_POSITION_KEY, "0,0");
                if ("0,0".equals(locationString)) {
                    return;
                }
                String[] locationArry = locationString.split(",");
                mLatLng = new LatLng(Double.parseDouble(locationArry[0]), Double.parseDouble(locationArry[1]));
            }
            JSONObject mjson = new JSONObject();
            mjson.put("category", merchantTypeItemBean.getType_id());
            mjson.put("offset", offset);
            mjson.put("length", SHOP_UPDATE_LENGTH);
            mjson.put("location", locationString);
            mGetNearByList.getNearByList(mjson, null, true, new BaseNetCallBack<NearByMerchantListBean>() {
                @Override
                public void onSuccess(NearByMerchantListBean paramT) {
                    if (isClear)
                        mList.clear();
                    mList.addAll(paramT.getData());
                    for (int i = 0; i < 5; i++) {
                        if (i < mList.size()) {
                            currentShopList.add(mList.get(i));
                        } else {
                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void init() {
        mBundle = getIntent().getExtras();
        merchantTypeItemBean = (MerchantTypeItemBean) mBundle.getSerializable("merchantType");
        tv_shop.setText("附近" + merchantTypeItemBean.getType_name());
        if (!"定位中...".equals(mBundle.getString("name"))) {
            tb_title.setAddress(mBundle.getString("name"));
        }
        mLatLng = mBundle.getParcelable("location");
        mAdapter = new ShopListAdapter(this, currentShopList, this);
        lv_shop_list.setAdapter(mAdapter);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected void findViews() {
        view = LayoutInflater.from(mContext).inflate(R.layout.banner_shop_list, null);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        tv_shop = (TextView) view.findViewById(R.id.tv_shop);
        lv_shop_list = (ListView) findViewById(R.id.lv_shop_list);
        lv_shop_list.addHeaderView(view);
    }

    @Override
    protected void setListensers() {
        tb_title.setListener(this);
        lv_shop_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//list停止滚动时加载数据
                    currentShopList.clear();
                    for (int i = 0; i <= end_line + 10; i++) {
                        if (i < mList.size())
                            currentShopList.add(mList.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount + firstVisibleItem > end_line) {
                    begin_line = firstVisibleItem;
                    end_line = firstVisibleItem + visibleItemCount;
                    if (end_line >= totalItemCount) {
                        end_line = totalItemCount - 1;
                    }
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public void onShopItemClick(int position) {
        try {
            if (!isUserSignIn()) {
                gotoActivity(mContext, LoginActivity.class, null);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("merchant_id", mList.get(position).getId());
            bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_INSTALLMENT);
            gotoActivity(ShopListActivity.this, PayActivity.class, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ret", "onActivityResult");
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            mLatLng = data.getExtras().getParcelable("location");
            tb_title.setAddress(data.getExtras().getString("name"));
            setLocationResult();
            getNearbyBusinesses(mList.size(), false);
        }
    }

    @Override
    public void onLeftClick(View view) {
    }

    @Override
    public void onAddressClick(View view) {
        Intent intent = new Intent(mContext, SearchLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", mLatLng);
        bundle.putString("name", tb_title.getAddress());
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
        ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    @Override
    public void onRightClick(View view) {
    }

    private void setLocationResult() {
        Intent mResultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", mLatLng);
        bundle.putString("name", tb_title.getAddress());
        mResultIntent.putExtras(bundle);
        setResult(RESULT_OK, mResultIntent);
    }

    private boolean isUserSignIn() {
        String customerId = UserUtil.getId(mContext);
        if (customerId != null && !"".equals(customerId) && !"0".equals(customerId)) {
            return true;
        } else {
            return false;
        }
    }

}
