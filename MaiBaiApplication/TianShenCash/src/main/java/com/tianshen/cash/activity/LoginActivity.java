package com.tianshen.cash.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.model.TianShenLoginBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.net.api.SignIn;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RegexUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.ChangeInterface;
import com.tianshen.cash.view.MyLoginEditText;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final int LOGINSUCCESS = 5;
    private final int REGISTEREQUEST=6;
    private final int REGISTESUCCESS=7;
    private int mRegIdQueryTimes;

    private String mobile = "", password = "";
    private String mUniqueId;

    private ImageView img_back;
    private Button bt_login;
    private TextView tv_forget;

    private TextView tv_registe;
    private MyLoginEditText et_mobile, et_password;
    private SignIn mSignIn;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (380 == msg.what) {
                    String reg_id = JPushInterface.getRegistrationID(mContext);
                    LogUtil.d("ret", "reg_id = " + reg_id);
                    if (reg_id == null || "".equals(reg_id)) {
                        if (mRegIdQueryTimes == 7) {
                            login(mobile, password);
                            mRegIdQueryTimes = 0;
                            return;
                        }
                        Message msgNext = new Message();
                        msgNext.what = 380;
                        mHandler.sendMessageDelayed(msgNext, 500);
                        mRegIdQueryTimes++;
                        if (mRegIdQueryTimes == 1) {
                            ToastUtil
                                    .showToast(mContext, getResources().getString(R.string.initialization_please_wait));
                        }
                    } else {
                        login(mobile, password);
                        mRegIdQueryTimes = 0;
                    }
                }
            }
        };
    }

    public void initData() {

        RxPermissions rxPermissions = new RxPermissions(LoginActivity.this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                mUniqueId = TelephonyMgr.getDeviceId();
            }
        });

        mSignIn = new SignIn(mContext);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        img_back = (ImageView) findViewById(R.id.img_back);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        et_mobile = (MyLoginEditText) findViewById(R.id.et_mobile);
        et_password = (MyLoginEditText) findViewById(R.id.et_password);
        tv_registe=(TextView) findViewById(R.id.tv_registe);
    }

    @Override
    protected void setListensers() {
        img_back.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        bt_login.setClickable(false);
        tv_forget.setOnClickListener(this);
        tv_registe.setOnClickListener(this);
        et_mobile.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                mobile = s.toString();
                if (mobile.length() == 11) {
                    if(RegexUtil.IsTelephone(mobile)){
                        if(!password.equals("")){
                            bt_login.setClickable(true);
                            bt_login.setBackgroundResource(R.drawable.select_bt);
                        }
                    }else{
                        ToastUtil.showToast(mContext,"不是合法的手机号码");
                    }
                } else {
                    bt_login.setClickable(false);
                    bt_login.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
        et_password.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                password = s.toString();
                if (!password.equals("") && mobile.length() == 11) {
                    if( RegexUtil.IsTelephone(mobile)) {
                        bt_login.setClickable(true);
                        bt_login.setBackgroundResource(R.drawable.select_bt);
                    }
                } else {
                    bt_login.setClickable(false);
                    bt_login.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                backActivity();
                break;
            case R.id.tv_forget:
                Bundle bundle=new Bundle();
                bundle.putString("type", GlobalParams.CHANGE_LOGIN_PASSWORD);
                gotoActivity(mContext,ForgetPasswordActivity.class,bundle);
                break;
            case R.id.bt_login:
                mobile = et_mobile.getText().toString().trim();
                password = et_password.getText().toString().trim();
                bt_login.setClickable(false);
                Message msg = new Message();
                msg.what = 380;
                mHandler.sendMessage(msg);
                break;
            case R.id.tv_registe:
                Intent intent=new Intent(mContext,RegisteActivity.class);
                startActivityForResult(intent,REGISTEREQUEST);
                overridePendingTransition(R.anim.push_right_in,R.anim.not_exit_push_left_out);
                break;
        }
    }

    public void login(final String mobile, final String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("device_id", mUniqueId);
            json.put("mobile", mobile);
            json.put("password", password);

            String jpushId = TianShenUserUtil.getUserJPushId(mContext);
            if (TextUtils.isEmpty(jpushId)) {
                jpushId = JPushInterface.getRegistrationID(mContext);
            }
            json.put("push_id", jpushId);
            final String finalJpushId = jpushId;
            mSignIn.signIn(json, bt_login, true, new BaseNetCallBack<TianShenLoginBean>() {
                @Override
                public void onSuccess(TianShenLoginBean paramT) {
//                    SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.JPUSH_ID_KEY, finalPush_id);
//                    UserUtil.setLoginPassword(mContext, password);
//                    setResult(LOGINSUCCESS);
//                    Set<String> tags=new HashSet<String>();
//                    BDLocation bdLocation = LocationUtil.getInstance(mContext).getLocation();
//                    if (bdLocation != null) {
//                        tags.add(bdLocation.getCityCode());
//                        tags.add(bdLocation.getCountryCode());
//                        tags.add(bdLocation.getProvince());
//                        JPushInterface.setAliasAndTags(mContext, TianShenUserUtil.getUserId(mContext), tags);
//                    }
//                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION,null);
//                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.LOGIN_SUCCESS_ACTION,null);
//                    //Todo
//                    backActivity();

                    //保存用户信息
                    TianShenUserUtil.saveUserToken(mContext, paramT.getData().getToken());
                    TianShenUserUtil.saveUserId(mContext, paramT.getData().getCustomer_id());
                    TianShenUserUtil.saveUserPhoneNum(mContext, mobile);
                    TianShenUserUtil.saveUserJPushId(mContext, finalJpushId);


                    Set<String> tags = new HashSet<>();
                    BDLocation bdLocation = LocationUtil.getInstance(mContext).getLocation();
                    if (bdLocation != null) {
                        tags.add(bdLocation.getCityCode());
                        tags.add(bdLocation.getCountryCode());
                        tags.add(bdLocation.getProvince());
                        JPushInterface.setAliasAndTags(mContext, TianShenUserUtil.getUserId(mContext), tags);
                    }

                    gotoActivity(mContext, MainActivity.class, null);
                    EventBus.getDefault().post(new LoginSuccessEvent());

                }
                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    bt_login.setClickable(true);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg0){
            case REGISTEREQUEST:
                if(arg1==REGISTESUCCESS){
                    backActivity();
                }
                break;
        }
    }


    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event) {
        backActivity();
    }
}
