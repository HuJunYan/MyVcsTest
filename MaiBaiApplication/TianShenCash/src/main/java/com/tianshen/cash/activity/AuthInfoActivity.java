package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.model.BankListItemBean;
import com.tianshen.cash.model.ConstantBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.net.api.GetCity;
import com.tianshen.cash.net.api.GetCounty;
import com.tianshen.cash.net.api.GetCustomerInfo;
import com.tianshen.cash.net.api.GetProvince;
import com.tianshen.cash.net.api.SaveUserInfo;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

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

    @BindView(R.id.tv_auth_info_marry)
    TextView tv_auth_info_marry;

    @BindView(R.id.tv_auth_info_educational)
    TextView tv_auth_info_educational;

    private AddressBean mProvinceBean;
    private AddressBean mCityBean;
    private AddressBean mCountyBean;
    private ArrayList<String> mProvinceData;
    private ArrayList<String> mCityData;
    private ArrayList<String> mCountyData;

    private int mProvincePosition;//选择省的位置
    private int mCityPosition;//选择城市的位置
    private int mCountyPosition;//选择区域的位置

    private String user_address_provice;//用户常驻地址省份,
    private String user_address_city;//用户常驻地址县市
    private String user_address_county;//用户常驻地址地区

    private String company_address_provice;//用户公司地址省份
    private String company_address_city;//用户公司地址县市
    private String company_address_county;//用户公司地址地区


    private String[] marryData = {"未婚", "已婚", "离异", "再婚"};
    private String[] educationalData = {"小学", "初中", "高中", "本科", "硕士"};

    private boolean mIsClickHome;



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
        tv_auth_info_marry.setOnClickListener(this);
        tv_auth_info_educational.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUserInfo();
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
                mIsClickHome = true;
                initProvinceData();
                break;
            case R.id.tv_auth_info_work_address:
                mIsClickHome = false;
                initProvinceData();
                break;
            case R.id.tv_auth_info_marry:
                showMarryDialog();
                break;
            case R.id.tv_auth_info_educational:
                showEducationalDialog();
                break;
        }
    }

    /**
     * 得到省信息
     */
    private void initProvinceData() {

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
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
        String userId = TianShenUserUtil.getUserId(mContext);
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
        String userId = TianShenUserUtil.getUserId(mContext);
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
            String county_name = data.getCounty_name();
            mCountyData.add(county_name);
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
     * 显示选择婚姻状态的dialog
     */
    private void showMarryDialog() {
        new MaterialDialog.Builder(mContext)
                .title("婚姻状态")
                .items(marryData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                    }
                }).show();
    }

    /**
     * 显示选择学历的dialog
     */
    private void showEducationalDialog() {
        new MaterialDialog.Builder(mContext)
                .title("最高学历")
                .items(educationalData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
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
                        refreshAddressUI();
                    }
                }).show();
    }


    /**
     * 刷新整体UI
     */
    private void refreshRootUI(ConstantBean constantBean) {
        if (constantBean == null) {
            return;
        }


        user_address_provice = constantBean.getData().getUser_address_provice();
        user_address_city = constantBean.getData().getUser_address_city();
        user_address_county = constantBean.getData().getUser_address_county();

        company_address_city = constantBean.getData().getCompany_address_city();
        company_address_county = constantBean.getData().getCompany_address_county();
        company_address_provice = constantBean.getData().getCompany_address_provice();

        String qq_num = constantBean.getData().getQq_num();
        String company_name = constantBean.getData().getCompany_name();
        String company_phone = constantBean.getData().getCompany_phone();
        String company_address_detail = constantBean.getData().getCompany_address_detail();
        String user_address_detail = constantBean.getData().getUser_address_detail();
        String homeAddress = user_address_provice + "-" + user_address_city + "-" + user_address_county;
        String workAddress = company_address_provice + "-" + company_address_city + "-" + company_address_county;

        etAuthInfoQq.setText(qq_num);
        tvAuthInfoHomeAddress.setText(homeAddress);
        tvAuthInfoWorkAddress.setText(workAddress);
        etAuthInfoAddressDetails.setText(user_address_detail);
        etAuthInfoWorkName.setText(company_name);
        etAuthInfoWorkNum.setText(company_phone);
        etAuthInfoWorkAddressDetails.setText(company_address_detail);
    }

    /**
     * 刷新UI
     */
    private void refreshAddressUI() {
        String province = mProvinceData.get(mProvincePosition);
        String city = mCityData.get(mCityPosition);
        String county = mCountyData.get(mCountyPosition);
        String address = province + "-" + city + "-" + county;
        if (mIsClickHome) {
            user_address_provice = province;
            user_address_city = city;
            user_address_county = county;
            tvAuthInfoHomeAddress.setText(address);
        } else {
            company_address_provice = province;
            company_address_city = city;
            company_address_county = county;
            tvAuthInfoWorkAddress.setText(address);
        }
    }


    /**
     * 得到用户数据
     */
    private void initUserInfo() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GetCustomerInfo getCustomerInfo = new GetCustomerInfo(mContext);
        getCustomerInfo.getCustomerInfo(jsonObject, null, true, new BaseNetCallBack<ConstantBean>() {
            @Override
            public void onSuccess(ConstantBean paramT) {
                refreshRootUI(paramT);
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    /**
     * 提交个人信息
     */
    private void postUserInfo() {

        String qq = etAuthInfoQq.getText().toString().trim();
        String user_address_detail = etAuthInfoAddressDetails.getText().toString().trim();
        String company_name = etAuthInfoWorkName.getText().toString().trim();
        String company_phone = etAuthInfoWorkNum.getText().toString().trim();
        String company_address_detail = etAuthInfoWorkAddressDetails.getText().toString().trim();

        if (TextUtils.isEmpty(user_address_provice)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(user_address_city)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(user_address_county)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_address_provice)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_address_city)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_address_county)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(qq)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(user_address_detail)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_name)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_phone)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(company_address_detail)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }


        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);
            jsonObject.put("qq_num", qq);
            jsonObject.put("user_address_provice", user_address_provice);
            jsonObject.put("user_address_city", user_address_city);
            jsonObject.put("user_address_county", user_address_county);
            jsonObject.put("user_address_detail", user_address_detail);
            jsonObject.put("company_name", company_name);
            jsonObject.put("company_phone", company_phone);
            jsonObject.put("company_address_provice", company_address_provice);
            jsonObject.put("company_address_city", company_address_city);
            jsonObject.put("company_address_county", company_address_county);
            jsonObject.put("company_address_detail", company_address_detail);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SaveUserInfo saveUserInfo = new SaveUserInfo(mContext);
        saveUserInfo.saveCustomerInfoUrl(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                int code = paramT.getCode();
                if (code == 0) {
                    ToastUtil.showToast(mContext, "保存成功!");
                    backActivity();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

    }

}
