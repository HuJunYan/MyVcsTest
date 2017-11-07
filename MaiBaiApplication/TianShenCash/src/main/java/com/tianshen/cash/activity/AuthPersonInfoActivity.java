package com.tianshen.cash.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.net.api.GetCity;
import com.tianshen.cash.net.api.GetCounty;
import com.tianshen.cash.net.api.GetProvince;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.PhoneUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 个人信息认证界面
 */
public class AuthPersonInfoActivity extends BaseActivity {

    @BindView(R.id.tv_auth_info_back)
    TextView mTvAuthInfoBack;
    @BindView(R.id.tv_auth_info_qq)
    TextView mTvAuthInfoQq;
    @BindView(R.id.tv_auth_info_home_address_key_no)
    TextView mTvAuthInfoHomeAddressKeyNo;
    @BindView(R.id.tv_auth_info_home_address)
    TextView mTvAuthInfoHomeAddress;
    @BindView(R.id.tv_auth_info_address_empty)
    TextView mTvAuthInfoAddressEmpty;
    @BindView(R.id.et_auth_info_address_details)
    EditText mEtAuthInfoAddressDetails;
    @BindView(R.id.tv_auth_info_work_name_no)
    TextView mTvAuthInfoWorkNameNo;
    @BindView(R.id.et_auth_info_work_name)
    EditText mEtAuthInfoWorkName;
    @BindView(R.id.tv_auth_info_position)
    TextView mTvAuthInfoPosition;
    @BindView(R.id.ev_auth_info_position)
    EditText mEvAuthInfoPosition;
    @BindView(R.id.tv_auth_info_occupation_key)
    TextView mTvAuthInfoOccupationKey;
    @BindView(R.id.tv_auth_info_occupation)
    TextView mTvAuthInfoOccupation;
    @BindView(R.id.tv_auth_info_income_key)
    TextView mTvAuthInfoIncomeKey;
    @BindView(R.id.tv_auth_info_income)
    TextView mTvAuthInfoIncome;
    @BindView(R.id.tv_auth_info_work_num_no)
    TextView mTvAuthInfoWorkNumNo;
    @BindView(R.id.et_auth_info_work_num)
    EditText mEtAuthInfoWorkNum;
    @BindView(R.id.tv_auth_info_work_address_key_no)
    TextView mTvAuthInfoWorkAddressKeyNo;
    @BindView(R.id.tv_auth_info_work_address)
    TextView mTvAuthInfoWorkAddress;
    @BindView(R.id.tv_auth_info_work_address_empty)
    TextView mTvAuthInfoWorkAddressEmpty;
    @BindView(R.id.et_auth_info_work_address_details)
    EditText mEtAuthInfoWorkAddressDetails;
    @BindView(R.id.tv_auth_nexus1_key_no)
    TextView mTvAuthNexus1KeyNo;
    @BindView(R.id.tv_auth_nexus1)
    TextView mTvAuthNexus1;
    @BindView(R.id.iv_auth_contact_down)
    ImageView mIvAuthContactDown;
    @BindView(R.id.iv_auth_contacts1)
    ImageView mIvAuthContacts1;
    @BindView(R.id.et_auth_nexus_name1)
    EditText mEtAuthNexusName1;
    @BindView(R.id.rl_auth_nexus1)
    RelativeLayout mRlAuthNexus1;
    @BindView(R.id.tv_auth_nexus_phone_no)
    TextView mTvAuthNexusPhoneNo;
    @BindView(R.id.et_auth_nexus_phone)
    EditText mEtAuthNexusPhone;
    @BindView(R.id.tv_auth_nexus2_key_no)
    TextView mTvAuthNexus2KeyNo;
    @BindView(R.id.tv_auth_nexus2)
    TextView mTvAuthNexus2;
    @BindView(R.id.iv_auth_contact_down2)
    ImageView mIvAuthContactDown2;
    @BindView(R.id.iv_auth_contacts2)
    ImageView mIvAuthContacts2;
    @BindView(R.id.et_auth_nexus_name2)
    EditText mEtAuthNexusName2;
    @BindView(R.id.rl_auth_nexus2)
    RelativeLayout mRlAuthNexus2;
    @BindView(R.id.tv_auth_nexus_phone2_no)
    TextView mTvAuthNexusPhone2No;
    @BindView(R.id.et_auth_nexus_phone2)
    EditText mEtAuthNexusPhone2;
    @BindView(R.id.tv_auth_info_post)
    TextView mTvAuthInfoPost;
    private AddressBean mProvinceBean;
    private int mProvincePosition;//选择省的位置
    private int mCityPosition;//选择城市的位置
    private int mCountyPosition;//选择区域的位置
    private String user_address_provice;//用户常驻地址省份,
    private String user_address_city;//用户常驻地址县市
    private String user_address_county;//用户常驻地址地区

    private String company_address_provice;//用户公司地址省份
    private String company_address_city;//用户公司地址县市
    private String company_address_county;//用户公司地址地区

    private AddressBean mCityBean;
    private AddressBean mCountyBean;
    private ArrayList<String> mProvinceData;
    private ArrayList<String> mCityData;
    private ArrayList<String> mCountyData;
    private boolean mIsClickHome;
    private ArrayList<String> mNexus = new ArrayList<>();
    private boolean isGetContactsing;//当前是否正在获取联系人
    private MaterialDialog materialDialog;
    private List<HashMap<String, String>> mContacts;
    private int mContactsPoistion;
    private ArrayList<String> mContactsDialogDada;
    private boolean mIsClickContacts1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_person_info;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }


    @OnClick({R.id.tv_auth_info_back,R.id.tv_auth_info_home_address, R.id.tv_auth_info_work_address, R.id.iv_auth_contact_down, R.id.iv_auth_contacts1, R.id.iv_auth_contact_down2, R.id.iv_auth_contacts2, R.id.tv_auth_info_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_auth_info_back:
                finish();

                break;
            case R.id.tv_auth_info_home_address:
                //常住地址
                mIsClickHome=true;
                initProvinceData();
                break;
            case R.id.tv_auth_info_work_address:
                mIsClickHome = false;
                initProvinceData();
                break;
            case R.id.iv_auth_contact_down:
                showExtroDialog(0);
                break;
            case R.id.iv_auth_contacts1:
                mIsClickContacts1 = true;
                getContacts();
                break;
            case R.id.iv_auth_contact_down2:
                showExtroDialog(1);
                break;
            case R.id.iv_auth_contacts2:
                mIsClickContacts1 = false;
                getContacts();
                break;
            case R.id.tv_auth_info_post:
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetProvince getProvince = new GetProvince(mContext);
        getProvince.getProvince(jsonObject, mTvAuthInfoHomeAddress, true,
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("province_id", province_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetCity getCity = new GetCity(mContext);
        getCity.getCity(jsonObject, mTvAuthInfoHomeAddress, true,
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("city_id", city_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetCounty getCounty = new GetCounty(mContext);
        getCounty.getCounty(jsonObject, mTvAuthInfoHomeAddress, true,
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
        if (mProvinceData == null) {
            ToastUtil.showToast(mContext, "请稍后再试");
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择省份")
                .items(mProvinceData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mProvincePosition = position;
                        initCityData();
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
    }

    /**
     * 显示城市的Dialog
     */
    private void showCityListDialog() {
        if (mCityData == null) {
            ToastUtil.showToast(mContext, "请稍后再试");
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择城市")
                .items(mCityData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCityPosition = position;
                        initCountyData();
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
    }

    /**
     * 显示区域的Dialog
     */
    private void showCountyListDialog() {

        if (mCountyData == null) {
            ToastUtil.showToast(mContext, "");
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择区域")
                .items(mCountyData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCountyPosition = position;
                        refreshAddressUI();
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
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
            mTvAuthInfoHomeAddress.setText(address);
        } else {
            company_address_provice = province;
            company_address_city = city;
            company_address_county = county;
            mTvAuthInfoWorkAddress.setText(address);
        }
    }

    /**
     * 显示关系的dialog
     */
    private void showExtroDialog(final int clickPosition) {

        ArrayList<String> mNexus1 = new ArrayList<>();
        if (clickPosition == 0) {
            mNexus1.add(mNexus.get(0));
            mNexus1.add(mNexus.get(1));
            mNexus1.add(mNexus.get(2));
            mNexus1.add(mNexus.get(3));
            mNexus1.add(mNexus.get(4));
        }
        new MaterialDialog.Builder(mContext)
                .title("与我关系")
                .items(clickPosition == 0 ? mNexus1 : mNexus)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        refreshNexusUI(clickPosition, position);
                    }
                })
                .show();
    }

    private void refreshNexusUI(int clickPosition, int selectPosition) {
        if (clickPosition == 0) {
            mTvAuthNexus1.setText(mNexus.get(selectPosition));
        } else {
            mTvAuthNexus2.setText(mNexus.get(selectPosition));
        }
    }

    /**
     * 显示选择联系人的dialog
     */
    private void showContactsDialog() {

        if (mContactsDialogDada.size() == 0) {
            ToastUtil.showToast(mContext, "没有查找到联系人");
            return;
        }
        if (!isFinishing()) {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .title("选择联系人")
                    .items(mContactsDialogDada)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            mContactsPoistion = position;
                            refreshContactUI();
                        }
                    }).show();
        }
    }

    /**
     * 得到联系人信息
     */
    private void getContacts() {

        //判断当前是否执行任务
        if (isGetContactsing) {
            return;
        }
        //判断当前是否显示dialog
        if (materialDialog != null && materialDialog.isShowing()) {
            return;
        }


        RxPermissions rxPermissions = new RxPermissions(AuthPersonInfoActivity.this);
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<HashMap<String, String>>, ArrayList<String>>() {
                            @Override
                            public ArrayList<String> apply(List<HashMap<String, String>> contacts) throws Exception {
                                mContacts = new ArrayList<HashMap<String, String>>();
                                ArrayList<String> contactsDialogDada = new ArrayList<>();
                                for (int i = 0; i < contacts.size(); i++) {
                                    HashMap<String, String> contactMap = contacts.get(i);
                                    String name = contactMap.get("name");
                                    String phone = contactMap.get("phone");
                                    if (phone == null || phone.length() != 11) {
                                        continue;
                                    }
                                    mContacts.add(contactMap);
                                    contactsDialogDada.add(name + "-" + phone);
                                }
                                return contactsDialogDada;
                            }
                        })
                        .subscribe(getObserver());
            }
        });
    }

    private Observable<List<HashMap<String, String>>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<HashMap<String, String>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HashMap<String, String>>> e) throws Exception {
                if (!e.isDisposed()) {
                    List<HashMap<String, String>> contacts = PhoneUtils.getAllContactInfo(mContext);
                    e.onNext(contacts);
                    e.onComplete();
                }
            }
        });
    }

    private Observer<ArrayList<String>> getObserver() {
        return new Observer<ArrayList<String>>() {
            //任务执行之前
            @Override
            public void onSubscribe(Disposable d) {
                isGetContactsing = true;
                String loadText = mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
                ViewUtil.createLoadingDialog((Activity) mContext, loadText, false);
            }

            //任务执行之后
            @Override
            public void onNext(ArrayList<String> value) {
                mContactsDialogDada = value;
                isGetContactsing = false;
            }

            //任务执行完毕
            @Override
            public void onComplete() {
                ViewUtil.cancelLoadingDialog();
                showContactsDialog();
                isGetContactsing = false;
            }

            //任务异常
            @Override
            public void onError(Throwable e) {
                ViewUtil.cancelLoadingDialog();
                isGetContactsing = false;
            }

        };
    }

    private void refreshContactUI() {
        HashMap<String, String> hashMap = mContacts.get(mContactsPoistion);
        String name = hashMap.get("name");
        String phone = hashMap.get("phone");
        if (mIsClickContacts1) {
            mEtAuthNexusName1.setText(name);
            mEtAuthNexusPhone.setText(phone);
        } else {
            mEtAuthNexusName2.setText(name);
            mEtAuthNexusPhone2.setText(phone);
        }
    }

    /**
     * 初始化dialog数据
     */
    private void initDialogData() {
        mNexus.add("父母");
        mNexus.add("配偶");
        mNexus.add("直亲");
        mNexus.add("朋友");
        mNexus.add("同事");
    }


}
