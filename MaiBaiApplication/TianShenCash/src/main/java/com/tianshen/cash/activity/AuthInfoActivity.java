package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.model.BankListItemBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.net.api.GetCity;
import com.tianshen.cash.net.api.GetCounty;
import com.tianshen.cash.net.api.GetProvince;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息
 */

public class AuthInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_auth_info_back)
    TextView tvAuthInfoBack;
    @BindView(R.id.tv_auth_info_post)
    TextView tvAuthInfoPost;
    @BindView(R.id.tv_auth_info_qq)
    TextView tvAuthInfoQq;
    @BindView(R.id.et_auth_info_qq)
    EditText etAuthInfoQq;
    @BindView(R.id.tv_auth_info_home_address_key)
    TextView tvAuthInfoHomeAddressKey;
    @BindView(R.id.tv_auth_info_home_address)
    TextView tvAuthInfoHomeAddress;
    @BindView(R.id.tv_auth_info_address_empty)
    TextView tvAuthInfoAddressEmpty;
    @BindView(R.id.et_auth_info_address_details)
    EditText etAuthInfoAddressDetails;
    @BindView(R.id.tv_auth_info_work_name)
    TextView tvAuthInfoWorkName;
    @BindView(R.id.et_auth_info_work_name)
    EditText etAuthInfoWorkName;
    @BindView(R.id.tv_auth_info_work_num)
    TextView tvAuthInfoWorkNum;
    @BindView(R.id.et_auth_info_work_num)
    EditText etAuthInfoWorkNum;
    @BindView(R.id.tv_auth_info_work_address_key)
    TextView tvAuthInfoWorkAddressKey;
    @BindView(R.id.tv_auth_info_work_address)
    TextView tvAuthInfoWorkAddress;
    @BindView(R.id.tv_auth_info_work_address_empty)
    TextView tvAuthInfoWorkAddressEmpty;
    @BindView(R.id.et_auth_info_work_address_details)
    EditText etAuthInfoWorkAddressDetails;


    private AddressBean mProvinceBean;
    private AddressBean mCityBean;
    private AddressBean mCountyBean;
    private ArrayList<String> mProvinceData;
    private ArrayList<String> mCityData;
    private ArrayList<String> mCountyData;

    private int mProvincePosition;//选择省的位置
    private int mCityPosition;//选择城市的位置
    private int mCountyPosition;//选择区域的位置

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_info;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthInfoBack.setOnClickListener(this);
        tvAuthInfoPost.setOnClickListener(this);
        tvAuthInfoHomeAddress.setOnClickListener(this);
        tvAuthInfoWorkAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_info_back:
                backActivity();
                break;
            case R.id.tv_auth_info_post:
                postUserInfo();
                break;
            case R.id.tv_auth_info_home_address:
                initProvinceData();
                break;
            case R.id.tv_auth_info_work_address:
                postUserInfo();
                break;
        }
    }

    /**
     * 得到省信息
     */
    private void initProvinceData() {

        JSONObject jsonObject = new JSONObject();
        long userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetProvince getProvince = new GetProvince(mContext);
        getProvince.getProvince(jsonObject, tvAuthInfoHomeAddress, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mProvinceBean = paramT;
                        parserProvinceListData();
                        showProvinceListDialog();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
    }

    /**
     * 得到城市信息
     */
    private void initCityData() {
        JSONObject jsonObject = new JSONObject();
        long userId = TianShenUserUtil.getUserId(mContext);
        AddressBean.Data data = mProvinceBean.getData().get(mProvincePosition);
        String province_id = data.getProvice_id();
        try {
            jsonObject.put("customer_id", userId);
            jsonObject.put("province_id", province_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetCity getCity = new GetCity(mContext);
        getCity.getCity(jsonObject, tvAuthInfoHomeAddress, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mCityBean = paramT;
                        parserCityListData();
                        showCityListDialog();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {
                    }
                });

    }

    /**
     * 得到区域信息
     */
    private void initCountyData() {
        JSONObject jsonObject = new JSONObject();
        long userId = TianShenUserUtil.getUserId(mContext);
        AddressBean.Data data = mCityBean.getData().get(mCityPosition);
        String city_id = data.getCity_id();
        try {
            jsonObject.put("customer_id", userId);
            jsonObject.put("city_id", city_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetCounty getCounty = new GetCounty(mContext);
        getCounty.getCounty(jsonObject, tvAuthInfoHomeAddress, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mCountyBean = paramT;
                        parserCountyListData();
                        showCountyListDialog();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {
                    }
                });
    }

    /**
     * 解省数据给dialog用
     */
    private void parserProvinceListData() {
        ArrayList<AddressBean.Data> datas = mProvinceBean.getData();
        mProvinceData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            AddressBean.Data data = datas.get(i);
            String provice_name = data.getProvice_name();
            mProvinceData.add(provice_name);
        }
    }

    /**
     * 解城市数据给dialog用
     */
    private void parserCityListData() {
        ArrayList<AddressBean.Data> datas = mCityBean.getData();
        mCityData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            AddressBean.Data data = datas.get(i);
            String city_name = data.getCity_name();
            mCityData.add(city_name);
        }
    }

    /**
     * 解区域数据给dialog用
     */
    private void parserCountyListData() {
        ArrayList<AddressBean.Data> datas = mCountyBean.getData();
        mCountyData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            AddressBean.Data data = datas.get(i);
            String city_name = data.getCity_name();
            mCountyData.add(city_name);
        }
    }


    /**
     * 显示省的Dialog
     */
    private void showProvinceListDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择省份")
                .items(mProvinceData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mProvincePosition = position;
                        initCityData();
                    }
                }).show();
    }

    /**
     * 显示城市的Dialog
     */
    private void showCityListDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择城市")
                .items(mCityData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCityPosition = position;
                        initCountyData();
                    }
                }).show();
    }

    /**
     * 显示区域的Dialog
     */
    private void showCountyListDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择区域")
                .items(mCountyData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCountyPosition = position;
                    }
                }).show();
    }

    /**
     * 提交个人信息
     */
    private void postUserInfo() {


    }
}
