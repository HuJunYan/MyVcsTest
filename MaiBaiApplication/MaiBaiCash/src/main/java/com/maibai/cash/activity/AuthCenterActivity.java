package com.maibai.cash.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.maibai.cash.R;
import com.maibai.cash.adapter.AuthCenterAdapter;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.model.AuthCenterItemBean;
import com.maibai.cash.model.UserAuthCenterBean;
import com.maibai.cash.net.api.GetUserAuthCenter;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;

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

    private UserAuthCenterBean mUserAuthCenterBean;
    private ArrayList<AuthCenterItemBean> mAuthCenterItemBeans;

    public static final int MSG_CLICK_ITEM = 1;

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
        initAuthCenterData();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerviewAuthCenter.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.item_divider);
        xrecyclerviewAuthCenter.addItemDecoration(xrecyclerviewAuthCenter.new DividerItemDecoration(dividerDrawable));
        xrecyclerviewAuthCenter.setLoadingMoreEnabled(false);
        xrecyclerviewAuthCenter.setPullRefreshEnabled(false);
        AuthCenterAdapter mAdapter = new AuthCenterAdapter(mContext, mAuthCenterItemBeans, mHandler);
        xrecyclerviewAuthCenter.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_center_back:
                backActivity();
                break;
            case R.id.tv_auth_center_post:
                ToastUtil.showToast(mContext, "点击了提交");
                break;
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

        }
    }

    /**
     * 得到用户认证信息
     */
    private void initAuthCenterData() {
        try {
            JSONObject jsonObject = new JSONObject();
            long userId = TianShenUserUtil.getUserId(mContext);
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
     * 得到初始化数据(从本地得到)
     */
    private ArrayList<AuthCenterItemBean> initXRecyclerviewData() {

        UserAuthCenterBean.DataBean data = mUserAuthCenterBean.getData();
        String id_num = data.getId_num();
        String userdetail_pass = data.getUserdetail_pass();
        String contacts_pass = data.getContacts_pass();
        String bankcard_pass = data.getBankcard_pass();
        String china_mobile = data.getChina_mobile();


        ArrayList<AuthCenterItemBean> authCenterItemBeans = new ArrayList<>();

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
}
