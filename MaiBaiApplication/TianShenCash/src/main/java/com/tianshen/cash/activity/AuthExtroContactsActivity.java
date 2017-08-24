package com.tianshen.cash.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.tianshen.cash.model.ExtroContactsBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.api.GetExtroContacts;
import com.tianshen.cash.net.api.SaveExtroContacts;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.PhoneUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
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
 * 紧急联系人
 */

public class AuthExtroContactsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_auth_info_back)
    TextView tvAuthInfoBack;
    @BindView(R.id.tv_auth_extro_post)
    TextView tvAuthExtroPost;
    @BindView(R.id.tv_auth_nexus1_key)
    TextView tvAuthNexus1Key;
    @BindView(R.id.tv_auth_nexus1)
    TextView tvAuthNexus1;
    @BindView(R.id.tv_auth_nexus_name1_key)
    TextView tvAuthNexusName1Key;
    @BindView(R.id.et_auth_nexus_name1)
    EditText etAuthNexusName1;
    @BindView(R.id.tv_auth_nexus_phone)
    TextView tvAuthNexusPhone;
    @BindView(R.id.et_auth_nexus_phone)
    EditText etAuthNexusPhone;
    @BindView(R.id.tv_auth_nexus2_key)
    TextView tvAuthNexus2Key;
    @BindView(R.id.tv_auth_nexus2)
    TextView tvAuthNexus2;
    @BindView(R.id.tv_auth_nexus_name2_key)
    TextView tvAuthNexusName2Key;
    @BindView(R.id.et_auth_nexus_name2)
    EditText etAuthNexusName2;
    @BindView(R.id.tv_auth_nexus_phone2)
    TextView tvAuthNexusPhone2;
    @BindView(R.id.et_auth_nexus_phone2)
    EditText etAuthNexusPhone2;
    @BindView(R.id.rl_auth_nexus1)
    RelativeLayout rlAuthNexus1;
    @BindView(R.id.rl_auth_nexus2)
    RelativeLayout rlAuthNexus2;

    @BindView(R.id.iv_auth_contacts1)
    ImageView iv_auth_contacts1;

    @BindView(R.id.iv_auth_contacts2)
    ImageView iv_auth_contacts2;

    private boolean mIsClickContacts1;
    private int mContactsPoistion;

    private boolean isGetContactsing;//当前是否正在获取联系人

    private MaterialDialog materialDialog;

    private List<HashMap<String, String>> mContacts;

    private ArrayList<String> mContactsDialogDada;

    private ArrayList<String> mNexus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogData();
        initExtroData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_extro_contacts;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtil.cancelLoadingDialog();
    }

    @Override
    protected void setListensers() {
        tvAuthExtroPost.setOnClickListener(this);
        tvAuthInfoBack.setOnClickListener(this);
        rlAuthNexus1.setOnClickListener(this);
        rlAuthNexus2.setOnClickListener(this);
        iv_auth_contacts1.setOnClickListener(this);
        iv_auth_contacts2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_info_back:
                backActivity();
                break;
            case R.id.tv_auth_extro_post:
                postExtroData();
                break;
            case R.id.rl_auth_nexus1:
                showExtroDialog(0);
                break;
            case R.id.rl_auth_nexus2:
                showExtroDialog(1);
                break;
            case R.id.iv_auth_contacts1:
                mIsClickContacts1 = true;
                getContacts();
                break;
            case R.id.iv_auth_contacts2:
                mIsClickContacts1 = false;
                getContacts();
                break;
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


        RxPermissions rxPermissions = new RxPermissions(AuthExtroContactsActivity.this);
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<HashMap<String, String>>, ArrayList<String>>() {
                            @Override
                            public ArrayList<String> apply(List<HashMap<String, String>> contacts) throws Exception {
                                mContacts = contacts;
                                ArrayList<String> contactsDialogDada = new ArrayList<>();
                                for (int i = 0; i < contacts.size(); i++) {
                                    HashMap<String, String> contactMap = contacts.get(i);
                                    String name = contactMap.get("name");
                                    String phone = contactMap.get("phone");
                                    if (phone == null || phone.length() != 11) {
                                        continue;
                                    }
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
     * 显示关系的dialog
     */
    private void showExtroDialog(final int clickPosition) {

        ArrayList<String> mNexus1 = new ArrayList<>();
        if (clickPosition == 0) {
            mNexus1.add(mNexus.get(0));
            mNexus1.add(mNexus.get(1));
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
            tvAuthNexus1.setText(mNexus.get(selectPosition));
        } else {
            tvAuthNexus2.setText(mNexus.get(selectPosition));
        }
    }

    private void refreshContactUI() {
        HashMap<String, String> hashMap = mContacts.get(mContactsPoistion);
        String name = hashMap.get("name");
        String phone = hashMap.get("phone");
        if (mIsClickContacts1) {
            etAuthNexusName1.setText(name);
            etAuthNexusPhone.setText(phone);
        } else {
            etAuthNexusName2.setText(name);
            etAuthNexusPhone2.setText(phone);
        }
    }

    /**
     * 刷新整体UI
     */
    private void refreshRootUI(ExtroContactsBean extroContactsBean) {
        if (extroContactsBean == null) {
            return;
        }
        ArrayList<ExtroContactsBean.Data> datas = extroContactsBean.getData();

        if (datas == null || datas.size() == 0) {
            return;
        }

        String contact_phone1 = datas.get(0).getContact_phone();
        String contact_phone2 = datas.get(1).getContact_phone();

        String contact_name1 = datas.get(0).getContact_name();
        String contact_name2 = datas.get(1).getContact_name();

        String contact_type1 = datas.get(0).getType();
        String contact_type2 = datas.get(1).getType();

        etAuthNexusName1.setText(contact_name1);
        etAuthNexusName2.setText(contact_name2);
        etAuthNexusPhone.setText(contact_phone1);
        etAuthNexusPhone2.setText(contact_phone2);

        int index1 = Integer.parseInt(contact_type1) - 1;
        int index2 = Integer.parseInt(contact_type2) - 1;

        if (index1 < 0) {
            index1 = 0;
        }
        if (index2 < 0) {
            index2 = 0;
        }

        String nexus1 = mNexus.get(index1);
        String nexus2 = mNexus.get(index2);

        tvAuthNexus1.setText(nexus1);
        tvAuthNexus2.setText(nexus2);

    }

    /**
     * 得到紧急联系人信息
     */
    private void initExtroData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);

            GetExtroContacts getExtroContacts = new GetExtroContacts(mContext);
            getExtroContacts.getExtroContacts(jsonObject, new BaseNetCallBack<ExtroContactsBean>() {
                @Override
                public void onSuccess(ExtroContactsBean extroContactsBean) {
                    refreshRootUI(extroContactsBean);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交紧急联系人信息
     */
    private void postExtroData() {

        String name1 = etAuthNexusName1.getText().toString().trim();
        String name2 = etAuthNexusName2.getText().toString().trim();

        String phone1 = etAuthNexusPhone.getText().toString().trim();
        String phone2 = etAuthNexusPhone2.getText().toString().trim();


        if (TextUtils.isEmpty(name1)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(name2)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(phone1)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        if (TextUtils.isEmpty(phone2)) {
            ToastUtil.showToast(mContext, "请完善资料!");
            return;
        }
        String userPhoneNum = TianShenUserUtil.getUserPhoneNum(mContext);
        if (phone1.equals(phone2)) {
            ToastUtil.showToast(mContext, "两个联系人的电话号码不能相同");
            return;
        }
        if (phone1.equals(userPhoneNum)) {
            ToastUtil.showToast(mContext, "联系人电话不能和注册手机号一致");
            return;
        }
        if (phone2.equals(userPhoneNum)) {
            ToastUtil.showToast(mContext, "联系人电话不能和注册手机号一致");
            return;
        }
        if (phone1.length() != 11 || phone2.length() != 11){
            ToastUtil.showToast(mContext,"手机号格式不对");
            return;
        }
        String nexusTxt1 = tvAuthNexus1.getText().toString();
        String nexusTxt2 = tvAuthNexus2.getText().toString();

        int type1 = mNexus.indexOf(nexusTxt1) + 1;
        int type2 = mNexus.indexOf(nexusTxt2) + 1;

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("type", type1 + "");
            jsonObject1.put("contact_name", name1);
            jsonObject1.put("contact_phone", phone1);


            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("type", type2 + "");
            jsonObject2.put("contact_name", name2);
            jsonObject2.put("contact_phone", phone2);


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(0, jsonObject1);
            jsonArray.put(1, jsonObject2);


            jsonObject.put("data", jsonArray);

            SaveExtroContacts saveExtroContacts = new SaveExtroContacts(mContext);
            saveExtroContacts.saveExtroContacts(jsonObject, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    int code = paramT.getCode();
                    if (code == 0) {
                        ToastUtil.showToast(mContext, "保存成功");
                        backActivity();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
