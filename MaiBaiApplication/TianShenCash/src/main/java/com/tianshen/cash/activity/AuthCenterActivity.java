package com.tianshen.cash.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tianshen.cash.R;
import com.tianshen.cash.adapter.AuthCenterAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.AuthCenterBackEvent;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.event.QuotaEvent;
import com.tianshen.cash.fragment.HomeFragment;
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


    @BindView(R.id.tv_auth_center_back)
    TextView tvAuthCenterBack;
    @BindView(R.id.tv_auth_center_post)
    TextView tvAuthCenterPost;
    @BindView(R.id.xrecyclerview_auth_center)
    XRecyclerView xrecyclerviewAuthCenter;

    private boolean mIsFromCard;

    private boolean mIsAllAuthOK;

    private AuthCenterAdapter mAdapter;

    private UploadToServerUtil mUploadToServerUtil;

    private UserAuthCenterBean mUserAuthCenterBean;
    private ArrayList<AuthCenterItemBean> mAuthCenterItemBeans;

    public static final int MSG_CLICK_ITEM = 1;

    private boolean mQuotaFlag; //只有页面正在显示的时候收到此消息才强制跳转到下单页面

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_CLICK_ITEM:
                    int position = (int) message.obj;
                    onClickItem(position);
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
            xrecyclerviewAuthCenter.setLayoutManager(layoutManager);
            Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.item_divider);
            xrecyclerviewAuthCenter.addItemDecoration(xrecyclerviewAuthCenter.new DividerItemDecoration(dividerDrawable));
            xrecyclerviewAuthCenter.setLoadingMoreEnabled(false);
            xrecyclerviewAuthCenter.setPullRefreshEnabled(false);
            mAdapter = new AuthCenterAdapter(mContext, mAuthCenterItemBeans, mHandler);
            xrecyclerviewAuthCenter.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mAuthCenterItemBeans);
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
        if (mIsFromCard) {
            backActivity();
        } else {
            if (mIsAllAuthOK) {
                uploadContacts();
            } else {
                ToastUtil.showToast(mContext, "请先认证!");
            }
        }
    }


    /**
     * 点击了列表
     */
    private void onClickItem(int position) {
        switch (position) {
            case 0://跳转到身份认证
                gotoActivity(mContext, AuthIdentityActivity.class, null);
                break;
            case 1://跳转到个人信息
                gotoActivity(mContext, AuthInfoActivity.class, null);
                break;
            case 2://跳转到紧急联系人
                gotoActivity(mContext, AuthExtroContactsActivity.class, null);
                break;
            case 3://跳转到收款银行卡
                String status = mAuthCenterItemBeans.get(0).getStatus();
                if ("0".equals(status)) {
                    ToastUtil.showToast(mContext, "请先身份认证!");
                    return;
                }
                gotoActivity(mContext, AuthBankCardActivity.class, null);
                break;
            case 4://跳转到运营商认证
                gotoChinaMobileActivity();
                break;

        }
    }

    /**
     * 跳转到运营商认证
     */
    private void gotoChinaMobileActivity() {
        String china_mobile_url = mUserAuthCenterBean.getData().getChina_mobile_url();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.CHINA_MOBILE_URL_KEY, china_mobile_url);
        gotoActivity(mContext, ChinaMobileActivity.class, bundle);
    }

    /**
     * 得到用户认证信息
     */
    private void initAuthCenterData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
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
        mUploadToServerUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
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
                    mUploadToServerUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
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

        AuthCenterItemBean authCenterItemBean1 = new AuthCenterItemBean();
        authCenterItemBean1.setName("个人信息");
        authCenterItemBean1.setDrawable_id(R.drawable.ic_auth_center_info_item);
        authCenterItemBean1.setStatus(userdetail_pass);

        AuthCenterItemBean authCenterItemBean2 = new AuthCenterItemBean();
        authCenterItemBean2.setName("紧急联系人");
        authCenterItemBean2.setDrawable_id(R.drawable.ic_auth_center_urgent_item);
        authCenterItemBean2.setStatus(contacts_pass);

        AuthCenterItemBean authCenterItemBean3 = new AuthCenterItemBean();
        authCenterItemBean3.setName("收款银行卡");
        authCenterItemBean3.setDrawable_id(R.drawable.ic_auth_center_bank_card_item);
        authCenterItemBean3.setStatus(bankcard_pass);

        AuthCenterItemBean authCenterItemBean4 = new AuthCenterItemBean();
        authCenterItemBean4.setName("手机运营商");
        authCenterItemBean4.setDrawable_id(R.drawable.ic_auth_center_phone_item);
        authCenterItemBean4.setStatus(china_mobile);

        AuthCenterItemBean authCenterItemBean5 = new AuthCenterItemBean();
        authCenterItemBean5.setName("更多信息可选填");
        authCenterItemBean5.setDrawable_id(R.drawable.ic_auth_center_more_item);

        authCenterItemBeans.add(authCenterItemBean0);
        authCenterItemBeans.add(authCenterItemBean1);
        authCenterItemBeans.add(authCenterItemBean2);
        authCenterItemBeans.add(authCenterItemBean3);
        authCenterItemBeans.add(authCenterItemBean4);
        authCenterItemBeans.add(authCenterItemBean5);

        return authCenterItemBeans;
    }


    /**
     * 收到了强行跳转到下单页面
     */
    @Subscribe
    public void onQuotaEvent(QuotaEvent event) {
        LogUtil.d("abc", "收到了强行跳转到下单页面");
        if (mQuotaFlag){
            gotoActivity(mContext, ConfirmMoneyActivity.class, null);
        }
    }

}
