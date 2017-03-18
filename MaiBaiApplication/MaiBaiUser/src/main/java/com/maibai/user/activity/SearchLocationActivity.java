package com.maibai.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.maibai.user.R;
import com.maibai.user.adapter.AddressListAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.utils.LocationUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.ViewUtil;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetGeoCoderResultListener, TitleBar.TitleBarListener2, View.OnClickListener, LocationUtil.RequestLocationListener, AdapterView.OnItemClickListener {
    private TextView tv_current_address;
    private TextView tv_get_location;
    private ListView lv_near_address;
    private ListView lv_search_address;
    private TitleBar tb_title;
    private View rl_current_address;

    private PoiSearch mPoiSearchCity = null;
    private GeoCoder mGeoCoder = null;
    private LocationUtil mLocationUtil;
    private LatLng mLatLng;
    private List<PoiInfo> mNearList = new ArrayList<PoiInfo>();
    private List<PoiInfo> mCityList = new ArrayList<PoiInfo>();
    private AddressListAdapter mNearAdapter;
    private AddressListAdapter mCityAdapter;
    private Intent mResultIntent;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initListView();
        mLocationUtil = LocationUtil.getInstance(this);
        mLocationUtil.setReqListener(this);
        mPoiSearchCity = PoiSearch.newInstance();
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mPoiSearchCity.setOnGetPoiSearchResultListener(this);
        String name = getIntent().getExtras().getString("name");
        if (!"定位中...".equals(name)) {
            tv_current_address.setText(name);
        }
        mLatLng = getIntent().getExtras().getParcelable("location");
        if (mLatLng != null) {
            ViewUtil.createLoadingDialog(SearchLocationActivity.this, "加载中", true);
        } else {
            tv_current_address.setText("定位失败，请重新定位");
        }
        searchNearPoiList();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_search_location;
    }

    @Override
    protected void findViews() {
        tv_current_address = (TextView) findViewById(R.id.tv_current_address);
        tv_get_location = (TextView) findViewById(R.id.tv_get_location);
        lv_search_address = (ListView) findViewById(R.id.lv_search_address);
        lv_near_address = (ListView) findViewById(R.id.lv_near_address);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        rl_current_address = findViewById(R.id.rl_current_address);
    }

    @Override
    protected void setListensers() {
        tv_current_address.setOnClickListener(this);
        tv_get_location.setOnClickListener(this);
        tb_title.setListener(this);
        lv_search_address.setOnItemClickListener(this);
        lv_near_address.setOnItemClickListener(this);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        try {
            mNearList.clear();
            if (reverseGeoCodeResult != null && reverseGeoCodeResult.getPoiList() != null) {
                mNearList.addAll(reverseGeoCodeResult.getPoiList());
            }
            mNearAdapter.notifyDataSetChanged();
            ViewUtil.cancelLoadingDialog();
        } catch (Exception e) {
            ViewUtil.cancelLoadingDialog();
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        try {
            Log.d("ret", "onGetReverseGeoCodeResult");
            mCityList.clear();
            if (poiResult != null && poiResult.getAllPoi() != null) {
                mCityList.addAll(poiResult.getAllPoi());
            }
            mCityAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ViewUtil.cancelLoadingDialog();
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count == 0) {
            rl_current_address.setVisibility(View.VISIBLE);
            lv_search_address.setVisibility(View.GONE);
            mCityList.clear();
            mCityAdapter.notifyDataSetChanged();
        } else {
            if (lv_search_address.getVisibility() == View.GONE) {
                rl_current_address.setVisibility(View.GONE);
                lv_search_address.setVisibility(View.VISIBLE);
            }
            searchInCity();
        }
    }

    @Override
    public void onRightClick(View view) {
        searchInCity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_current_address:
                backActivity();
                break;
            case R.id.tv_get_location:
                ViewUtil.createLoadingDialog(SearchLocationActivity.this, "加载中", true);
                mLocationUtil.requestLocation();
                break;
        }

    }

    @Override
    public void onRequestReceiveLocation(BDLocation bdLocation) {
        try {
            if (bdLocation.getAddress().address == null) {
                tv_current_address.setText("定位失败，请重新定位");
                ViewUtil.cancelLoadingDialog();
                mLatLng = null;
                return;
            }
            mLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            List<Poi> list = bdLocation.getPoiList();
            String name = list.get(0).getName();
            tv_current_address.setText(name);
            setLocationResult(name);
            searchNearPoiList();
        } catch (Exception e) {
            ViewUtil.cancelLoadingDialog();
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiInfo poi = (PoiInfo) parent.getAdapter().getItem(position);
        Log.d("ret", poi.name);
        mLatLng = poi.location;
        setLocationResult(poi.name);
        backActivity();
    }

    private void initListView() {
        mNearAdapter = new AddressListAdapter(this, mNearList);
        mCityAdapter = new AddressListAdapter(this, mCityList);
        lv_near_address.setAdapter(mNearAdapter);
        lv_search_address.setAdapter(mCityAdapter);
    }

    private void searchInCity() {
        mPoiSearchCity.searchInCity(new PoiCitySearchOption().city("北京").keyword(tb_title.getEditTextContent()).pageCapacity(10));
    }

    private void searchNearPoiList() {
        if (mLatLng != null) {
            Log.d("ret", "searchNearPoiList");
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mLatLng.latitude, mLatLng.longitude)));
        }
    }

    private void setLocationResult(String address) {
        mResultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", mLatLng);
        bundle.putString("name", address);
        mResultIntent.putExtras(bundle);
        setResult(RESULT_OK, mResultIntent);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    @Override
    public void onLeftClick(View view) {
    }
}
