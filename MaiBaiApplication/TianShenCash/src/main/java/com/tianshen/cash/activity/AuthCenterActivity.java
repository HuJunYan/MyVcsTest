package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.tianshen.cash.R;
import com.tianshen.cash.adapter.AuthListAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.AuthCenterBackEvent;
import com.tianshen.cash.event.QuotaEvent;
import com.tianshen.cash.model.AuthCenterItemBean;
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.net.api.GetUserAuthCenter;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.UploadToServerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 用户认证中心页面
 */

public class AuthCenterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TB_AUTH = "淘宝认证";
    @BindView(R.id.tv_auth_center_back)
    TextView tvAuthCenterBack;
    @BindView(R.id.tv_auth_center_post)
    TextView tvAuthCenterPost;
    @BindView(R.id.xrecyclerview_auth_center)
    RecyclerView xrecyclerviewAuthCenter;

    private boolean mIsFromCard;

    private boolean mIsAllAuthOK;

    private AuthListAdapter mAdapter;

    private UploadToServerUtil mUploadToServerUtil;

    private UserAuthCenterBean mUserAuthCenterBean;
    private ArrayList<AuthCenterItemBean> mAuthCenterItemBeans;

    public static final int MSG_CLICK_ITEM = 1;

    private boolean mQuotaFlag; //只有页面正在显示的时候收到此消息才强制跳转到下单页面

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_CLICK_ITEM:
                    String itemName = (String) message.obj;
                    onClickItem(itemName);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFromCard = getIntent().getExtras().getBoolean(GlobalParams.IS_FROM_CARD_KEY);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mQuotaFlag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAuthCenterData();
        mQuotaFlag = true;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_center;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthCenterBack.setOnClickListener(this);
        tvAuthCenterPost.setOnClickListener(this);
    }

    private void initXRecyclerview() {
        if (mAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            xrecyclerviewAuthCenter.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
            xrecyclerviewAuthCenter.setLayoutManager(layoutManager);
            mAdapter = new AuthListAdapter(mAuthCenterItemBeans, mHandler);
            xrecyclerviewAuthCenter.setAdapter(mAdapter);
        } else {
            mAdapter.replaceData(mAuthCenterItemBeans);
            mAdapter.notifyDataSetChanged();
        }
        checkIsAllAuthOK();
    }

    /**
     * 检查是否全部认证成功
     */
    private void checkIsAllAuthOK() {
        boolean isAllAuthOK = false;
        for (int i = 0; i < mAuthCenterItemBeans.size(); i++) {
            AuthCenterItemBean authCenterItemBean = mAuthCenterItemBeans.get(i);
            String status = authCenterItemBean.getStatus();
            if (TB_AUTH.equals(authCenterItemBean.getName())) {
                continue;
            }
            if ("0".equals(status)) {//没有认证
                isAllAuthOK = false;
                break;
            } else {
                isAllAuthOK = true;
            }

        }
        mIsAllAuthOK = isAllAuthOK;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_center_back:
                EventBus.getDefault().post(new AuthCenterBackEvent());
                backActivity();
                break;
            case R.id.tv_auth_center_post:
                onClickPost();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            EventBus.getDefault().post(new AuthCenterBackEvent());
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onClickPost() {
        if (mIsAllAuthOK) {
            EventBus.getDefault().post(new AuthCenterBackEvent());
            backActivity();
        } else {
            ToastUtil.showToast(mContext, "请先认证!");
        }
    }

    /**
     * 跳转到运营商认证
     */
    private void gotoChinaMobileActivity(String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.CHINA_MOBILE_URL_KEY, url);
        bundle.putString(GlobalParams.CHINA_MOBILE_TITLE_KEY, title);
        gotoActivity(mContext, ChinaMobileActivity.class, bundle);
    }

    /**
     * 得到用户认证信息
     */
    private void initAuthCenterData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetUserAuthCenter getUserAuthCenter = new GetUserAuthCenter(mContext);
            getUserAuthCenter.userAuthCenter(jsonObject, null, true, new BaseNetCallBack<UserAuthCenterBean>() {
                @Override
                public void onSuccess(UserAuthCenterBean paramT) {
                    mUserAuthCenterBean = paramT;
                    mAuthCenterItemBeans = initXRecyclerviewData();
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

    /**
     * 上传联系人&通信录
     */
    private void uploadContacts() {
        mUploadToServerUtil = new UploadToServerUtil(mContext);
        mUploadToServerUtil.setCallBack(new MyUploadCallBack());
        mUploadToServerUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
    }

    private class MyUploadCallBack implements UploadToServerUtil.UploadCallBack {

        @Override
        public void uploadSuccessCallBack(int type) {
            //上传通讯录、通话记录、短信等的回调
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人成功
                    gotoActivity(mContext, ConfirmMoneyActivity.class, null);
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录成功
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信成功
                    break;
            }
        }

        @Override
        public void uploadFailCallBack(int type) {
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人失败
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录失败
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信失败
                    break;
            }
        }
    }

    /**
     * 得到初始化数据(从本地得到)
     */
    private ArrayList<AuthCenterItemBean> initXRecyclerviewData() {

        UserAuthCenterBean.DataBean data = mUserAuthCenterBean.getData();
        String id_num = data.getId_num();
        String face_pass = data.getFace_pass();
        String contacts_pass = data.getContacts_pass();
        String bankcard_pass = data.getBankcard_pass();
        String china_mobile = data.getChina_mobile();
        String userdetail_pass = data.getUserdetail_pass();
        String zhima_pass = data.getZhima_pass();
        String wecash_pass = data.getWecash_pass();
        String taobao_pass = data.getTaobao_pass();

        ArrayList<AuthCenterItemBean> authCenterItemBeans = new ArrayList<>();
        if ("0".equals(id_num) || "0".equals(face_pass)) {//判断身份认证和扫脸都成功没。如果有一个失败就算身份认证失败
            id_num = "0";
        } else {
            id_num = "1";
        }

        AuthCenterItemBean authCenterItemBean0 = new AuthCenterItemBean();
        authCenterItemBean0.setName("身份认证");
        authCenterItemBean0.setDrawable_id(R.drawable.ic_auth_center_identity_item);
        authCenterItemBean0.setStatus(id_num);
        authCenterItemBean0.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean1 = new AuthCenterItemBean();
        authCenterItemBean1.setName("手机运营商");
        authCenterItemBean1.setDrawable_id(R.drawable.ic_auth_center_phone_item);
        authCenterItemBean1.setStatus(china_mobile);
        authCenterItemBean1.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean2 = new AuthCenterItemBean();
        authCenterItemBean2.setName("个人信息");
        authCenterItemBean2.setDrawable_id(R.drawable.ic_auth_center_info_item);
        authCenterItemBean2.setStatus(userdetail_pass);
        authCenterItemBean2.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean3 = new AuthCenterItemBean();
        authCenterItemBean3.setName("紧急联系人");
        authCenterItemBean3.setDrawable_id(R.drawable.ic_auth_center_urgent_item);
        authCenterItemBean3.setStatus(contacts_pass);
        authCenterItemBean3.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean4 = new AuthCenterItemBean();
        authCenterItemBean4.setName("联系人信息认证");
        authCenterItemBean4.setDrawable_id(R.drawable.ic_auth_center_shan_yin_item);
        authCenterItemBean4.setStatus(wecash_pass);
        authCenterItemBean4.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean5 = new AuthCenterItemBean();
        authCenterItemBean5.setName("收款银行卡");
        authCenterItemBean5.setDrawable_id(R.drawable.ic_auth_center_bank_card_item);
        authCenterItemBean5.setStatus(bankcard_pass);
        authCenterItemBean5.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean6 = new AuthCenterItemBean();
        authCenterItemBean6.setName("芝麻信用");
        authCenterItemBean6.setDrawable_id(R.drawable.ic_auth_center_zhi_ma_item);
        authCenterItemBean6.setStatus(zhima_pass);
        authCenterItemBean6.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        AuthCenterItemBean authCenterItemBean7 = new AuthCenterItemBean();
        authCenterItemBean7.setItemType(AuthCenterItemBean.TXT_TYPE);

        AuthCenterItemBean authCenterItemBean8 = new AuthCenterItemBean();
        authCenterItemBean8.setName(TB_AUTH);
        authCenterItemBean8.setDrawable_id(R.drawable.ic_auth_center_tao_bao_item);
        authCenterItemBean8.setStatus(taobao_pass);
        authCenterItemBean8.setItemType(AuthCenterItemBean.NORMAL_TYPE);

        authCenterItemBeans.add(authCenterItemBean0);
        authCenterItemBeans.add(authCenterItemBean1);
        authCenterItemBeans.add(authCenterItemBean2);
        authCenterItemBeans.add(authCenterItemBean3);
        String wecash_pass_url = mUserAuthCenterBean.getData().getWecash_pass_url();
        if (!TextUtils.isEmpty(wecash_pass_url)) {
            authCenterItemBeans.add(authCenterItemBean4);
        }
        authCenterItemBeans.add(authCenterItemBean5);
        authCenterItemBeans.add(authCenterItemBean6);
        authCenterItemBeans.add(authCenterItemBean7);
        authCenterItemBeans.add(authCenterItemBean8);
        return authCenterItemBeans;
    }

    /**
     * 点击了列表
     */
    private void onClickItem(String itemName) {

        UserAuthCenterBean.DataBean data = mUserAuthCenterBean.getData();
        String id_num = data.getId_num();
        String face_pass = data.getFace_pass();
        String identityStatus = "";//身份认证认证状态
        String extroContactsStatus = data.getContacts_pass();//紧急联系人认证状态
        String wecash_pass = data.getWecash_pass();//联系人信息认证状态
        String zhimaStatus = data.getZhima_pass();//芝麻认证状态
        String bankcardPass = data.getBankcard_pass();//银行卡认证状态


        if ("0".equals(id_num) || "0".equals(face_pass)) {//判断身份认证和扫脸都成功没。如果有一个失败就算身份认证失败
            identityStatus = "0";
        } else {
            identityStatus = "1";
        }


        switch (itemName) {
            case "身份认证":
                gotoActivity(mContext, AuthIdentityActivity.class, null);
                break;
            case "手机运营商":
                if ("0".equals(identityStatus)) {
                    ToastUtil.showToast(mContext, "请先进行身份认证");
                    return;
                }
                String chinaStatus = null;
                if (mAuthCenterItemBeans != null) {
                    chinaStatus = mAuthCenterItemBeans.get(1).getStatus();
                }
                if ("1".equals(chinaStatus)) {
                    ToastUtil.showToast(mContext, "之前已经认证");
                    return;
                }
                String china_mobile_url = mUserAuthCenterBean.getData().getChina_mobile_url();
                gotoChinaMobileActivity(china_mobile_url, "手机认证");
                break;
            case "个人信息":
                gotoActivity(mContext, AuthInfoActivity.class, null);
                break;
            case "紧急联系人":
                gotoActivity(mContext, AuthExtroContactsActivity.class, null);
                break;
            case "联系人信息认证":
                if ("0".equals(extroContactsStatus)) {
                    ToastUtil.showToast(mContext, "请先认证紧急联系人");
                    return;
                }
                if ("1".equals(wecash_pass)) {
                    ToastUtil.showToast(mContext, "之前已经认证");
                    return;
                }
                String wecash_pass_url = mUserAuthCenterBean.getData().getWecash_pass_url();
                gotoChinaMobileActivity(wecash_pass_url, "联系人信息认证");
                break;
            case "收款银行卡":
                if ("0".equals(identityStatus)) {
                    ToastUtil.showToast(mContext, "请先进行身份认证");
                    return;
                }
                if ("1".equals(bankcardPass)) {
                    ToastUtil.showToast(mContext, "之前已经认证");
                    return;
                }
                gotoActivity(mContext, AuthBankCardActivity.class, null);
                break;
            case "芝麻信用":
                if ("0".equals(identityStatus)) {
                    ToastUtil.showToast(mContext, "请先进行身份认证");
                    return;
                }
                if ("1".equals(zhimaStatus)) {
                    ToastUtil.showToast(mContext, "之前已经认证");
                    return;
                }
                String zhima_url = mUserAuthCenterBean.getData().getZhima_url();
                gotoChinaMobileActivity(zhima_url, "芝麻信用");
                break;
            case TB_AUTH:
                gotoTaoBaoAuth();
                break;
        }

    }

    /**
     * 跳转到淘宝renz
     */
    private void gotoTaoBaoAuth() {
        String userId = TianShenUserUtil.getUserId(mContext);
        String apiKey = "012a5b3a9bf94ac984fbb7c400c460aa";

        MxParam mxParam = new MxParam();
        mxParam.setUserId(userId);
        mxParam.setApiKey(apiKey);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_TAOBAO);

        MoxieSDK.getInstance().start(AuthCenterActivity.this, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        case MxParam.ResultCode.IMPORTING:
                            break;
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            ToastUtil.showToast(mContext, "认证成功!");
                            moxieContext.finish();
                            return true;
                    }
                }
                return false;
            }
        });

    }

    /**
     * 收到了强行跳转到下单页面
     */
    @Subscribe
    public void onQuotaEvent(QuotaEvent event) {
        LogUtil.d("abc", "收到了强行跳转到下单页面");
        if (mQuotaFlag) {
            gotoActivity(mContext, ConfirmMoneyActivity.class, null);
        }
    }

}
