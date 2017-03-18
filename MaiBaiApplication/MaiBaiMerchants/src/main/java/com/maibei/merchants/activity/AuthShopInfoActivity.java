package com.maibei.merchants.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.maibei.merchants.R;
import com.maibei.merchants.adapter.YearAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.CityListItemBean;
import com.maibei.merchants.model.CountyListItemBean;
import com.maibei.merchants.model.ProvinceBean;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.SaveMerchantInfo;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LocationUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RegexUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.ImageTextView;
import com.maibei.merchants.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 14658 on 2016/7/26.
 */
public class AuthShopInfoActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_next_reg;
    private ImageButton ib_return_auth;

    private ImageTextView itv_operat_time;//经营时间
    private ImageTextView itv_month_sales;//月销售额

    private MyEditText et_owner_name;//店主姓名
    private MyEditText et_owner_id;//店主身份证
    private ImageTextView itv_shop_address;//省市区
    private MyEditText et_detailed_address;//详细地址
    private String ownerName = "";
    private String ownerIdCard = "";
    private String operatTime = "";
    private String monthSales = "";
    private String detail_address="";
    private List<String> yearList = new ArrayList<String>();
    private YearAdapter yearAdapter;
    private  LocationUtil locationUtil;
    private String address="";
    private final int REQUEST_SELECTADDRESS=1;
    @Override
    protected int setContentView() {
        return R.layout.activity_auth_shop_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationUtil=LocationUtil.getInstance(mContext);
        locationUtil.startLocation();
        initYear();
    }
    private void initYear() {
        for (int i = 1; i < 101; i++) {
            String year = i + "年";
            yearList.add(year);
        }
    }
    @Override
    protected void findViews() {
        bt_next_reg = (Button) findViewById(R.id.bt_next_reg);
        itv_operat_time = (ImageTextView) findViewById(R.id.itv_operat_time);
        itv_month_sales = (ImageTextView) findViewById(R.id.itv_month_sales);
        ib_return_auth = (ImageButton) findViewById(R.id.ib_return_auth);
        et_owner_name = (MyEditText) findViewById(R.id.et_owner_name);
        et_owner_id = (MyEditText) findViewById(R.id.et_owner_id);
        itv_shop_address = (ImageTextView) findViewById(R.id.itv_shop_address);
        et_detailed_address=(MyEditText)findViewById(R.id.et_detailed_address);
    }

    @Override
    protected void setListensers() {
        bt_next_reg.setOnClickListener(this);
        itv_operat_time.setListener(new ImageTextView.ImageTextViewListener() {//年限点击
            @Override
            public void onRightClick(View view) {
                showOperateTimeDialog();
            }
        });
        itv_shop_address.setListener(new ImageTextView.ImageTextViewListener() {
            @Override
            public void onRightClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mContext,SelectAddressActivity.class);
                startActivityForResult(intent,REQUEST_SELECTADDRESS);
                ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
            }
        });
        itv_operat_time.setChangeListener(new ChangeInterface() {//年限监听
            @Override
            public void change(String s) {
                operatTime = s;
                if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !=""  && operatTime != "" && monthSales != "") {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        itv_month_sales.setListener(new ImageTextView.ImageTextViewListener() {//金额点击
            @Override
            public void onRightClick(View view) {
                showMonthSalesDialog();
            }
        });
        itv_month_sales.setChangeListener(new ChangeInterface() {//金额监听
            @Override
            public void change(String s) {
                monthSales = s;
                if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !="" && operatTime != "" && monthSales != "") {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        ib_return_auth.setOnClickListener(this);
        et_owner_name.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                ownerName = s;
                if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !=""  && operatTime != "" && monthSales != "") {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        et_owner_id.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                ownerIdCard = s;
                if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !=""  && operatTime != "" && monthSales != "") {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });
        et_detailed_address.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                detail_address=s;
                if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !=""  && operatTime != "" && monthSales != "") {
                    bt_next_reg.setEnabled(true);
                    bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_next_reg:
                saveMerchantInfo();
                break;
            case R.id.ib_return_auth:
                backActivity();
                break;
            default:
                break;
        }
    }

    private void showOperateTimeDialog(){
        yearAdapter = new YearAdapter(mContext, yearList);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_year, null);
        ListView lv_year = (ListView)view.findViewById(R.id.lv_year_list);
        lv_year.setAdapter(yearAdapter);

        final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view);
        final AlertDialog dialog = builder.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = (int)(display.getHeight())/2; ;
        dialog.getWindow().setAttributes(params);

        lv_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String yearName = yearAdapter.getItem(position).toString();
                itv_operat_time.setContentText(yearName);
                dialog.dismiss();
            }
        });
    }

    private void showMonthSalesDialog(){
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(AuthShopInfoActivity.this);
        aBuilder.setItems(getResources().getStringArray(R.array.month_sales), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    itv_month_sales.setContentText("1-5万");
                }else if (which == 1){
                    itv_month_sales.setContentText("5-10万");
                }else if (which == 2){
                    itv_month_sales.setContentText("10万以上");
                }
                dialog.dismiss();
            }
        });
        aBuilder.show();
    }

    private void saveMerchantInfo(){
        ownerName = et_owner_name.getEditTextString().trim();
        ownerIdCard = et_owner_id.getEditTextString().trim();
        address = itv_shop_address.getContentText().trim();
        operatTime = itv_operat_time.getContentText().trim();
        monthSales = itv_month_sales.getContentText().trim();
        detail_address=et_detailed_address.getEditTextString().trim();
        BDLocation bdLocation=locationUtil.getLocation();
        if(TextUtils.isEmpty(ownerName)){
            ToastUtil.showToast(mContext, "请输入店主姓名");
            return;
        }
        if(TextUtils.isEmpty(ownerIdCard) || !RegexUtil.IsIDcard(ownerIdCard)){
            ToastUtil.showToast(mContext, "请输入正确的店主身份证号码");
            return;
        }

        if(TextUtils.isEmpty(operatTime)){
            ToastUtil.showToast(mContext, "请输入经营时间");
            return;
        }
        if(TextUtils.isEmpty(monthSales)){
            ToastUtil.showToast(mContext, "请输入月销售额");
            return;
        }
        if(TextUtils.isEmpty(address)){
            ToastUtil.showToast(mContext, "请选择省市区");
            return;
        }
        if(TextUtils.isEmpty(detail_address)){
            ToastUtil.showToast(mContext,"请输入具体地址");
            return;
        }
        if(null==bdLocation){

            ToastUtil.showToast(mContext,"定位信息有误，请退出并重试");
            return;
        }
        String province=bdLocation.getProvince();
        String city=bdLocation.getCity();
        String country=bdLocation.getDistrict();
        String addrStr="";
        try {
            addrStr = bdLocation.getAddress().address;
        }catch (Exception e){

        }
        if(null==addrStr){
            addrStr="";
        }
        double latitude=bdLocation.getLatitude();
        double longitude=bdLocation.getLongitude();
        if(null==province){
            province="";
        }
        if(null==city){
            city="";
        }
        if(null==country){
            country="";
        }
        SaveMerchantInfo saveMerchantInfo = new SaveMerchantInfo(AuthShopInfoActivity.this);
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            mJSONObject.put("merchant_name", UserUtil.getName(mContext));
            mJSONObject.put("owner_name", ownerName);
            mJSONObject.put("operate_year", operatTime.replace("年",""));
            mJSONObject.put("sales_per_month", monthSales);
            mJSONObject.put("address", address+","+detail_address);
            mJSONObject.put("id_num", ownerIdCard);
            mJSONObject.put("location",latitude+","+longitude);
            mJSONObject.put("province",province);
            mJSONObject.put("city",city);
            mJSONObject.put("country",country);
            mJSONObject.put("addrStr",addrStr);
            saveMerchantInfo.saveMerchantInfo(mJSONObject, bt_next_reg, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    gotoActivity(AuthShopInfoActivity.this, AuthPictureActivity.class, null);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_SELECTADDRESS:
                if(GlobalParams.RESULT_SELECT_ADDRESS_OK==resultCode) {
                    if (null == data) {
                        return;
                    }
                    Bundle bundle = data.getExtras();
                    if (null == bundle) {
                        return;
                    }
                    ProvinceBean provinceBean = (ProvinceBean) bundle.getSerializable(GlobalParams.PROVINCE_KEY);
                    CityListItemBean cityListItemBean = (CityListItemBean) bundle.getSerializable(GlobalParams.CITY_KEY);
                    CountyListItemBean countyListItemBean = (CountyListItemBean) bundle.getSerializable(GlobalParams.CONTRY_KEY);
                    address = provinceBean.getProvince_name() + "," + cityListItemBean.getCity_name() + "," + countyListItemBean.getCounty_name();
                    itv_shop_address.setContentText(address);
                    if (ownerName != "" && ownerIdCard != "" && address != ""&&detail_address !=""  && operatTime != "" && monthSales != "") {
                        bt_next_reg.setEnabled(true);
                        bt_next_reg.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    }
                    break;
                }
        }
    }
}
