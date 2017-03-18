package com.maibai.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.CustomerAuthBean;
import com.maibai.cash.model.GetBankListBean;
import com.maibai.cash.net.api.GetAuthListStatus;
import com.maibai.cash.net.api.GetBindBankList;
import com.maibai.cash.net.api.GetCustomerAuth;
import com.maibai.cash.net.base.AuthenticationBean;
import com.maibai.cash.net.base.AuthenticationStatus;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.view.ImageTextView;
import com.maibai.cash.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ImproveQuotaActivity extends BaseActivity implements View.OnClickListener ,TitleBar.TitleBarListener{
    private boolean mIsAssignInWebView = false;
    private boolean mIsReturnFromWebView = false;
    private final int MAX_REFRESH_TIME = 30+1;
    private final int REFRESH_INTERVAL = 2000;
    private int mRefreshTime = 0;
    private int already_authentication_num = 0;
    private TextView tv_current_quota;
    private ImageTextView itv_operator, itv_taobao, itv_jingdong, itv_zhifubao, itv_zhima;
    private Button bt_submite;
    private AuthenticationStatus authenticationStatus = new AuthenticationStatus();
    private String creditMust = GlobalParams.NOT_MUST_AUTHENTICATION;
    private String china_mobile = GlobalParams.NOT_AUTHENTICATION;
    private String taobao_pass = GlobalParams.NOT_AUTHENTICATION;
    private String jd_pass = GlobalParams.NOT_AUTHENTICATION;
    private String zhifubao_pass = GlobalParams.NOT_AUTHENTICATION;
    private String zhima_pass = GlobalParams.NOT_AUTHENTICATION;
    private LinearLayout ll_operator, ll_taobao, ll_jingdong, ll_zhifubao, ll_zhima,ll_juxinli;
    private CustomerAuthBean customerAuthBean;
    private Bundle mBundle;
    private TitleBar tb_title;
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(  mIsReturnFromWebView) {
                        getCustomerAuth();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        init();
    }

    private void init() {
        tv_current_quota.setText(Double.valueOf(UserUtil.getCustomerAmount(mContext))/100+"");
        getListStatus();//获取列表信息
    }

    private void initView(AuthenticationStatus authenticationStatus) {
        for (int i = 0; i < authenticationStatus.getData().getAuth_list().size(); i++) {
            switch (authenticationStatus.getData().getAuth_list().get(i).getId()) {
                case GlobalParams.AUTHENTICATION_CHINA_MOBILE:
                    initView(ll_operator, i);
                    break;
                case GlobalParams.AUTHENTICATION_TAOBAO:
                    initView(ll_taobao, i);
                    break;
                case GlobalParams.AUTHENTICATION_JINGDONG:
                    initView(ll_jingdong, i);
                    break;
                case GlobalParams.AUTHENTICATION_ZHIFUBAO:
                    initView(ll_zhifubao, i);
                    break;
                case GlobalParams.AUTHENTICATION_ZHIMA:
                    initView(ll_zhima, i);
                    break;
            }
        }
    }

    private void initView(LinearLayout view, int i) {
        if (GlobalParams.AUTHENTICATION_OPEN.equals(authenticationStatus.getData().getAuth_list().get(i).getStatus())) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void getListStatus() {
        GetAuthListStatus getAuthListStatus = new GetAuthListStatus(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        getAuthListStatus.getAuthList(jsonObject, null, true, new BaseNetCallBack<AuthenticationStatus>() {
            @Override
            public void onSuccess(AuthenticationStatus paramT) {
                authenticationStatus = paramT;
                initView(paramT);
                getCustomerAuth();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }
    private void getCustomerAuth() {
        GetCustomerAuth mGetCustomerAuth = new GetCustomerAuth(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            mGetCustomerAuth.getCustomerAuth(jsonObject, null, false, new BaseNetCallBack<CustomerAuthBean>() {
                @Override
                public void onSuccess(CustomerAuthBean paramT) {
                    initData(paramT);
                    updateView();
                    customerAuthBean = paramT;
                    if (mIsReturnFromWebView && mRefreshTime < MAX_REFRESH_TIME && !mIsAssignInWebView) {
                        mRefreshTime++;
                        mhandler.sendEmptyMessageDelayed(1, REFRESH_INTERVAL);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if (mIsReturnFromWebView && mRefreshTime < MAX_REFRESH_TIME) {
                        mRefreshTime++;
                        mhandler.sendEmptyMessageDelayed(1, REFRESH_INTERVAL);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    public void initData(CustomerAuthBean customerAuthBean) {
        creditMust = customerAuthBean.getData().getCredit_must();
        china_mobile = customerAuthBean.getData().getChina_mobile();
        taobao_pass = customerAuthBean.getData().getTaobao_pass();
        jd_pass = customerAuthBean.getData().getJd_pass();
        zhifubao_pass = customerAuthBean.getData().getZhima_pass();
        zhima_pass = customerAuthBean.getData().getZhima_pass();
    }

    private void updateView() {
        already_authentication_num = 0;
        //运营商状态更新
        updateView(itv_operator, china_mobile);
        //淘宝认证状态更新
        updateView(itv_taobao, taobao_pass);
        //京东认证状态更新
        updateView(itv_jingdong, jd_pass);
        updateView(itv_zhifubao, zhifubao_pass);
        updateView(itv_zhima, zhima_pass);
        if (GlobalParams.NOT_MUST_AUTHENTICATION.equals(creditMust)) {
            bt_submite.setClickable(true);
            bt_submite.setBackgroundResource(R.drawable.select_bt);
        } else {
            if (already_authentication_num >= Integer.valueOf(creditMust)) {
                bt_submite.setClickable(true);
                bt_submite.setBackgroundResource(R.drawable.select_bt);
            } else {
                bt_submite.setClickable(false);
                bt_submite.setBackgroundResource(R.drawable.button_gray);
            }
        }
    }

    public void updateView(ImageTextView view, String isAuthentication) {
        if (GlobalParams.ALREADY_AUTHENTICATION.equals(isAuthentication)) {
            already_authentication_num++;
            view.setRightText("已认证");
            view.setRightTextColor(ContextCompat.getColor(mContext, R.color.is_authentication_color));
        } else {
            view.setRightText("去认证");
            view.setRightTextColor(ContextCompat.getColor(mContext, R.color.not_authentication_color));
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_improve_quota;
    }

    @Override
    protected void findViews() {
        itv_operator = (ImageTextView) findViewById(R.id.itv_operator);
        itv_taobao = (ImageTextView) findViewById(R.id.itv_taobao);
        itv_jingdong = (ImageTextView) findViewById(R.id.itv_jingdong);
        itv_zhifubao = (ImageTextView) findViewById(R.id.itv_zhifubao);
        itv_zhima = (ImageTextView) findViewById(R.id.itv_zhima);
        ll_operator = (LinearLayout) findViewById(R.id.ll_operator);
        ll_taobao = (LinearLayout) findViewById(R.id.ll_taobao);
        ll_jingdong = (LinearLayout) findViewById(R.id.ll_jingdong);
        ll_zhifubao = (LinearLayout) findViewById(R.id.ll_zhifubao);
        ll_zhima = (LinearLayout) findViewById(R.id.ll_zhima);
        bt_submite = (Button) findViewById(R.id.bt_submite);
        tv_current_quota=(TextView)findViewById(R.id.tv_current_quota);
        tb_title=(TitleBar)findViewById(R.id.tb_title);
    }

    @Override
    protected void setListensers() {
        bt_submite.setOnClickListener(this);
        itv_operator.setOnClickListener(this);
        itv_taobao.setOnClickListener(this);
        itv_zhima.setOnClickListener(this);
        itv_jingdong.setOnClickListener(this);
        itv_zhifubao.setOnClickListener(this);
        tb_title.setListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itv_operator:
                click(china_mobile, GlobalParams.AUTHENTICATION_CHINA_MOBILE);
                break;
            case R.id.itv_taobao:
                click(taobao_pass, GlobalParams.AUTHENTICATION_TAOBAO);
                break;
            case R.id.itv_jingdong:
                click(jd_pass, GlobalParams.AUTHENTICATION_JINGDONG);
                break;
            case R.id.itv_zhifubao:
                click(zhifubao_pass, GlobalParams.AUTHENTICATION_ZHIFUBAO);
                break;
            case R.id.itv_zhima:
                click(zhima_pass, GlobalParams.AUTHENTICATION_ZHIMA);
                break;
            case R.id.bt_submite:
                if (customerAuthBean == null || customerAuthBean.getData() == null || customerAuthBean.getData().getCredit_must() == null
                        || "".equals(customerAuthBean.getData().getCredit_must())) {
                    ToastUtil.showToast(mContext, "请先去认证！");
                    return;
                }
                int creditMust = Integer.parseInt(customerAuthBean.getData().getCredit_must());
                int mustCount = 0;

                if ("1".equals(customerAuthBean.getData().getChina_mobile())) {
                    mustCount++;
                }
                if ("1".equals(customerAuthBean.getData().getTaobao_pass())) {
                    mustCount++;
                }
                if ("1".equals(customerAuthBean.getData().getJd_pass())) {
                    mustCount++;
                }
                if ("1".equals(customerAuthBean.getData().getAlipay_pass())) {
                    mustCount++;
                }
                if ("1".equals(customerAuthBean.getData().getZhima_pass())) {
                    mustCount++;
                }
                if (creditMust > mustCount) {
                    ToastUtil.showToast(mContext, "请先去认证, 您至少需要认证" + creditMust +"项");
                    return;
                }
                if (mBundle != null) {
                    int applyType = mBundle.getInt(GlobalParams.APPLY_TYPE_KEY);
                    switch (applyType) {
//                        case GlobalParams.APPLY_TYPE_INSTALLMENT:
//                            if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
//                                gotoActivity(mContext, InputPayPwdActivity.class, mBundle);
//                            } else {
//                                gotoActivity(mContext, SetPayPwdActivity.class, mBundle);
//                            }
//                            break;
                        case GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY:
//                            getBankInfo();
                            mIsReturnFromWebView=false;
                            if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
                                gotoActivity(mContext, InputPayPwdActivity.class, mBundle);
                            } else {
                                gotoActivity(mContext, SetPayPwdActivity.class, mBundle);
                            }
                            break;
                    }
                }

                break;
        }
    }

    private void getBankInfo() {
        //执行获取银行卡列表的方法
        GetBindBankList getBindBankList = new GetBindBankList(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getBindBankList.getBindBankList(jsonObject, null, true, new BaseNetCallBack<GetBankListBean>() {
                @Override
                public void onSuccess(GetBankListBean paramT) {
                    if (paramT.getData().size() > 0) {
                        if ("1".equals(UserUtil.getIsSetPayPass(mContext))) {
                            gotoActivity(mContext, InputPayPwdActivity.class, getIntent().getExtras());
                        } else {
                            gotoActivity(mContext, SetPayPwdActivity.class, getIntent().getExtras());
                        }
                    } else {
                        gotoActivity(mContext, AddBankCardActivity.class, getIntent().getExtras());
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    public void click(String isAuthentication, String itemId) {
        if (GlobalParams.ALREADY_AUTHENTICATION.equals(isAuthentication)) {
            ToastUtil.showToast(mContext, "已认证");
        } else {
            authentication(itemId);
        }
    }

    private void authentication(String id) {
        for (AuthenticationBean authenticationBean : authenticationStatus.getData().getAuth_list()) {
            if (id.equals(authenticationBean.getId())) {
                startForResult(authenticationBean.getUrl(),id);
                mIsAssignInWebView = true;
            }
        }
    }

    public void startForResult(String url,String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("url", url);
        Intent intent = new Intent(mContext, AuthenticationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, GlobalParams.AUTHENTICATION_REQUEST);
        overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalParams.AUTHENTICATION_REQUEST:
                mIsReturnFromWebView = true;
                mIsAssignInWebView = false;
                mRefreshTime = 0;
                getCustomerAuth();
                break;
        }
    }

    @Override
    protected void backActivity() {
        mIsReturnFromWebView=false;
        super.backActivity();
    }

    @Override
    public void onLeftClick(View view) {
        backActivity();
    }

    @Override
    public void onAddressClick(View view) {

    }

    @Override
    public void onRightClick(View view) {

    }
}
