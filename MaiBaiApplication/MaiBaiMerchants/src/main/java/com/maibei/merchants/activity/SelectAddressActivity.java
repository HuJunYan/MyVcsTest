package com.maibei.merchants.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.AddressSelecterAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.db.DatabaseHelper;
import com.maibei.merchants.model.CityListBean;
import com.maibei.merchants.model.CityListItemBean;
import com.maibei.merchants.model.CountyListBean;
import com.maibei.merchants.model.CountyListItemBean;
import com.maibei.merchants.model.ProvinceBean;
import com.maibei.merchants.net.api.GetCity;
import com.maibei.merchants.net.api.GetCounty;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SelectAddressActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageButton ib_return;
    private ListView lv_province;
    private CityListItemBean city;
    private CountyListItemBean country;
    private ProvinceBean provinceBean;
    private final String STATUS_COUNTRY="country";
    private final String STATUS_CITY="city";
    private final String STATUS_PROVINCE="province";
    private final String STATUS_INIT="init";
    private List dataList;
    private String status=STATUS_INIT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("省份");
        getProvinces();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_select_address;
    }

    @Override
    protected void findViews() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        ib_return=(ImageButton)findViewById(R.id.ib_return);
        lv_province=(ListView)findViewById(R.id.lv_province);
    }

    @Override
    protected void setListensers() {
        ib_return.setOnClickListener(this);
        lv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (status){
                    case STATUS_INIT:
                        break;
                    case STATUS_PROVINCE:
                        provinceBean=((ProvinceBean)(dataList.get(position)));
                        tv_title.setText("城市");
                        getCity();
                        break;
                    case STATUS_CITY:
                        city=(CityListItemBean)(dataList.get(position));
                        tv_title.setText("区县");
                        getCountry();
                        break;
                    case STATUS_COUNTRY:
                        country=(CountyListItemBean)(dataList.get(position));
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(GlobalParams.PROVINCE_KEY,provinceBean);
                        bundle.putSerializable(GlobalParams.CITY_KEY,city);
                        bundle.putSerializable(GlobalParams.CONTRY_KEY,country);
                        Intent intent=new Intent();
                        intent.putExtras(bundle);
                        setResult(GlobalParams.RESULT_SELECT_ADDRESS_OK,intent);
                        backActivity();
                        break;
                }
            }
        });
    }

    private void getProvinces(){
        try {
            DatabaseHelper databaseHelper=new DatabaseHelper(mContext);
            dataList=databaseHelper.getProvince();
            AddressSelecterAdapter adapter=new AddressSelecterAdapter(dataList,mContext,STATUS_PROVINCE);
            lv_province.setAdapter(adapter);
            status=STATUS_PROVINCE;
        }catch (Exception e){
            ToastUtil.showToast(mContext,"本地存储位置信息有误");
        }

    }
    private void getCity(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("province_id",provinceBean.getProvince_id());
            GetCity getCity=new GetCity(mContext);
            getCity.getCity(jsonObject, null, true, new BaseNetCallBack<CityListBean>() {
                @Override
                public void onSuccess(CityListBean paramT) {
                    if(null==paramT){
                        return;
                    }
                    dataList=paramT.getData();
                    if(null==dataList){
                        return;
                    }
                    AddressSelecterAdapter adapter=new AddressSelecterAdapter(dataList,mContext,STATUS_CITY);
                    lv_province.setAdapter(adapter);
                    status=STATUS_CITY;
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getCountry(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("city_id",city.getCity_id());
            GetCounty getCounty=new GetCounty(mContext);
            getCounty.getCounty(jsonObject, null, true, new BaseNetCallBack<CountyListBean>() {
                @Override
                public void onSuccess(CountyListBean paramT) {
                    if(null==paramT){
                        return;
                    }
                    dataList=paramT.getData();
                    if(null==dataList){
                        return;
                    }
                    AddressSelecterAdapter adapter=new AddressSelecterAdapter(dataList,mContext,STATUS_COUNTRY);
                    lv_province.setAdapter(adapter);
                    status=STATUS_COUNTRY;
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* private String getStatus(){
        if(null!=country){
            tv_title.setText("");
           return STATUS_COUNTRY;
        }else if(null!=city){
            tv_title.setText("区县");
            return STATUS_CITY;
        }else if(null!=provinceBean){
            tv_title.setText("城市");
            return STATUS_PROVINCE;
        }else{
            tv_title.setText("省份");
            return STATUS_INIT;
        }
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return:
                backActivity();
                break;
        }
    }
}
