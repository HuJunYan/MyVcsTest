package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.model.AddressBean;
import com.maibai.cash.model.BankListItemBean;
import com.maibai.cash.model.StatisticsRollBean;
import com.maibai.cash.net.api.GetCity;
import com.maibai.cash.net.api.GetProvince;
import com.maibai.cash.net.api.StatisticsRoll;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.TianShenUserUtil;

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


    private AddressBean mAddressBean;
    private ArrayList<String> mProvinceData;
    private ArrayList<String> mCityData;
    private ArrayList<String> mCountyData;

    private int mProvincePosition;//选择省的位置
    private int mCityPosition;//选择城市的位置

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
                        mAddressBean = paramT;
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
        AddressBean.Data data = mAddressBean.getData().get(mProvincePosition);
        int provice_id = data.getProvice_id();
        LogUtil.d("abc","provice_id-->"+provice_id);
        try {
            jsonObject.put("customer_id", userId);
            jsonObject.put("provice_id",provice_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetCity getCity = new GetCity(mContext);
        getCity.getCity(new JSONObject(), tvAuthInfoHomeAddress, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mAddressBean = paramT;
                        parserCityListData();
                        showCityListDialog();
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
        ArrayList<AddressBean.Data> datas = mAddressBean.getData();
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
        ArrayList<AddressBean.Data> datas = mAddressBean.getData();
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
        ArrayList<AddressBean.Data> datas = mAddressBean.getData();
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
     * 显示省的Dialog
     */
    private void showCityListDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择城市")
                .items(mCityData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCityPosition = position;
                    }
                }).show();
    }


    /**
     * 提交个人信息
     */
    private void postUserInfo() {


    }
}
