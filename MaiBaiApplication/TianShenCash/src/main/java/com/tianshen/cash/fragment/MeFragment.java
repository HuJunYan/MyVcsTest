package com.tianshen.cash.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AboutMaibeiActivity;
import com.tianshen.cash.activity.ConsumptionRecordActivity;
import com.tianshen.cash.activity.InviteFriendsActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.activity.MyBankCardActivity;
import com.tianshen.cash.activity.RedPackageActivity;
import com.tianshen.cash.activity.ServiceOnlineActivity;
import com.tianshen.cash.activity.SettingActivity;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.FinishCurrentActivityEvent;
import com.tianshen.cash.event.GetRedPackageEvent;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.model.ActivityBean;
import com.tianshen.cash.model.CompanyInfoBean;
import com.tianshen.cash.model.MyHomeBean;
import com.tianshen.cash.net.api.GetActivity;
import com.tianshen.cash.net.api.GetCompayInfo;
import com.tianshen.cash.net.api.GetMyHome;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class MeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.iv_me_user)
    ImageView ivMeUser;
    @BindView(R.id.tv_me_user_name)
    TextView tvMeUserName;
    @BindView(R.id.iv_me_history)
    ImageView ivMeHistory;
    @BindView(R.id.tv_me_history)
    TextView tvMeHistory;
    @BindView(R.id.iv_me_bank_card)
    ImageView ivMeBankCard;
    @BindView(R.id.tv_me_bank_card)
    TextView tvMeBankCard;
    @BindView(R.id.iv_me_tianshen_service)
    ImageView ivMeTianshenService;
    @BindView(R.id.tv_me_tianshen_service)
    TextView tvMeTianshenService;
    @BindView(R.id.iv_me_about)
    ImageView ivMeAbout;
    @BindView(R.id.tv_me_about)
    TextView tvMeAbout;
    @BindView(R.id.iv_me_setting)
    ImageView ivMeSetting;
    @BindView(R.id.tv_me_setting)
    TextView tvMeSetting;
    @BindView(R.id.rl_me_user)
    RelativeLayout rlMeUser;
    @BindView(R.id.rl_me_history)
    RelativeLayout rlMeHistory;
    @BindView(R.id.rl_me_bank_card)
    RelativeLayout rlMeBankCard;
    @BindView(R.id.rl_me_tianshen_service)
    RelativeLayout rlMeTianshenService;
    @BindView(R.id.rl_me_about)
    RelativeLayout rlMeAbout;
    @BindView(R.id.rl_me_setting)
    RelativeLayout rlMeSetting;
    @BindView(R.id.rl_me_tianshen_service_online)
    RelativeLayout rl_me_tianshen_service_online;
    @BindView(R.id.tv_me_weixin)
    TextView tv_me_weixin;

    @BindView(R.id.rl_me_red_package)
    RelativeLayout rl_me_red_package;
    @BindView(R.id.rl_me_tianshen_friend)
    RelativeLayout rl_me_tianshen_friend;

    @BindView(R.id.tv_me_red_package)
    TextView tv_me_red_package;
    @BindView(R.id.tv_me_tianshen_friend)
    TextView tv_me_tianshen_friend;

    @Override
    protected int setContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void findViews(View rootView) {

    }

    @Override
    protected void initView() {
        initUI();
    }

    @Override
    protected void initData() {
        boolean login = TianShenUserUtil.isLogin(mContext);
        if (login) {
            initMyInfo();
        }
    }


    @Override
    protected void setListensers() {
        rlMeUser.setOnClickListener(this);
        rlMeHistory.setOnClickListener(this);
        rlMeBankCard.setOnClickListener(this);
        rlMeTianshenService.setOnClickListener(this);
        rlMeAbout.setOnClickListener(this);
        rlMeSetting.setOnClickListener(this);
        rl_me_tianshen_service_online.setOnClickListener(this);
        tv_me_weixin.setOnClickListener(this);
        rl_me_red_package.setOnClickListener(this);
        rl_me_tianshen_friend.setOnClickListener(this);
    }

    private void initMyInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetMyHome getMyHome = new GetMyHome(mContext);
            getMyHome.myHome(jsonObject, null, true, new BaseNetCallBack<MyHomeBean>() {
                @Override
                public void onSuccess(MyHomeBean bean) {
                    refreshUI(bean);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        boolean login = TianShenUserUtil.isLogin(mContext);
        if (login) {
            String phone = TianShenUserUtil.getUserPhoneNum(mContext);
            String encryptPhoneNum = StringUtil.encryptPhoneNum(phone);
            tvMeUserName.setText(encryptPhoneNum);
            boolean isShow = TianShenUserUtil.isShowServiceTelephone(mContext);
            if (isShow) {
                rlMeTianshenService.setVisibility(View.VISIBLE);
            } else {
                rlMeTianshenService.setVisibility(View.GONE);
            }
        } else {
            tvMeUserName.setText("未登录");
        }
    }


    private void refreshUI(MyHomeBean bean) {

        MyHomeBean.Data data = bean.getData();
        if (data == null) {
            return;
        }
        String packetString = data.getRed_packet_string();
        String shareString = data.getShare_string();
        if (TextUtils.isEmpty(packetString)) {
            tv_me_red_package.setVisibility(View.GONE);
        } else {
            tv_me_red_package.setText(packetString);
        }
        if (TextUtils.isEmpty(shareString)) {
            tv_me_tianshen_friend.setVisibility(View.GONE);
        } else {
            tv_me_tianshen_friend.setText(shareString);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_me_user:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                break;
            case R.id.rl_me_history:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoActivity(mContext, ConsumptionRecordActivity.class, null);
                break;
            case R.id.rl_me_bank_card:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoActivity(mContext, MyBankCardActivity.class, null);
                break;
            case R.id.rl_me_tianshen_service:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                initCompanyInfo();
                break;
            case R.id.rl_me_about:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoActivity(mContext, AboutMaibeiActivity.class, null);
                break;
            case R.id.rl_me_setting:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoActivity(mContext, SettingActivity.class, null);
                break;
            case R.id.rl_me_tianshen_service_online:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoWebActivity();
                break;
            case R.id.tv_me_weixin:
                copyWeiXin();
                break;
            case R.id.rl_me_red_package:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                gotoActivity(getActivity(), RedPackageActivity.class, null);
                break;
            case R.id.rl_me_tianshen_friend:
                if (!TianShenUserUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(GlobalParams.ACTIVITY_ID, "");
                gotoActivity(mContext, InviteFriendsActivity.class, bundle);
                break;
        }
    }

    /**
     * 跳转到在线客服页面
     */
    private void gotoWebActivity() {
        gotoActivity(mContext, ServiceOnlineActivity.class, null);
    }

    /**
     * 得到公司信息
     */
    private void initCompanyInfo() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetCompayInfo getCompayInfo = new GetCompayInfo(mContext);
        getCompayInfo.compayInfo(jsonObject, rlMeTianshenService, true, new BaseNetCallBack<CompanyInfoBean>() {

            @Override
            public void onSuccess(CompanyInfoBean paramT) {
                String wechatId = paramT.getData().getWechat_id();
                String service_telephone = paramT.getData().getService_telephone();
                TianShenUserUtil.saveServiceTelephone(mContext, service_telephone);
                TianShenUserUtil.saveWeiXin(mContext, wechatId);
                showMyKefu();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void showMyKefu() {
        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.withdrawals_diaog).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_mykefu, null);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_call = (TextView) view.findViewById(R.id.tv_call);
        TextView tv_dialog_service_phone = (TextView) view.findViewById(R.id.tv_dialog_service_phone);

        final String serviceTelephone = TianShenUserUtil.getServiceTelephone(mContext);
        tv_dialog_service_phone.setText(serviceTelephone);

        RxPermissions rxPermissions = new RxPermissions(getActivity());

        RxView.clicks(tv_call)
                .compose(rxPermissions.ensureEach(Manifest.permission.CALL_PHONE))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            new GetTelephoneUtils(mContext).changeLight();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + serviceTelephone));
                            startActivity(intent);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                        } else {
                        }
                        dialog.dismiss();
                    }
                });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new GetTelephoneUtils(mContext).changeLight();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(mContext).changeDark();
    }


    /**
     * 拷贝微信公众号到剪切版
     */
    private void copyWeiXin() {
        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("tianshendai");
        ToastUtil.showToast(mContext, "已复制微信公众号");
    }

    /**
     * 收到了登录成功的消息
     */
    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event) {
        String phone = TianShenUserUtil.getUserPhoneNum(mContext);
        String encryptPhoneNum = StringUtil.encryptPhoneNum(phone);
        tvMeUserName.setText(encryptPhoneNum);
        boolean isShow = TianShenUserUtil.isShowServiceTelephone(mContext);
        if (isShow) {
            rlMeTianshenService.setVisibility(View.VISIBLE);
        } else {
            rlMeTianshenService.setVisibility(View.GONE);
        }
        initMyInfo();
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onLoginoutSuccess(LogoutSuccessEvent event) {
        tvMeUserName.setText("未登录");
    }

    @Subscribe
    public void onTokenError(FinishCurrentActivityEvent event) {
        tvMeUserName.setText("未登录");

    }

    /**
     * 收到了红包提现成功的消息
     */
    @Subscribe
    public void onGetRedPackageSuccess(GetRedPackageEvent event) {
        LogUtil.d("abc", "收到了红包提现成功的消息--刷新UI");
        initMyInfo();
    }
}
