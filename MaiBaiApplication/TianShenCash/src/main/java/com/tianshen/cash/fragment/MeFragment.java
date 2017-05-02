package com.tianshen.cash.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AboutMaibeiActivity;
import com.tianshen.cash.activity.ConsumptionRecordActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.activity.MyBankCardActivity;
import com.tianshen.cash.activity.SettingActivity;
import com.tianshen.cash.activity.WebActivity;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.model.CompanyInfoBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.net.api.GetCompayInfo;
import com.tianshen.cash.net.api.IKnow;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

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

    @Override
    protected void initVariable() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshUI();
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void findViews(View rootView) {

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
    }

    private void refreshUI() {

        boolean login = TianShenUserUtil.isLogin(mContext);
        if (login) {
            User user = TianShenUserUtil.getUser(mContext);

            String phone = user.getPhone();
            String encryptPhoneNum = StringUtil.encryptPhoneNum(phone);
            tvMeUserName.setText(encryptPhoneNum);

            String isShowServiceTelephone = user.getIs_show_service_telephone();
            if ("1".equals(isShowServiceTelephone)) {
                rlMeTianshenService.setVisibility(View.VISIBLE);
            } else {
                rlMeTianshenService.setVisibility(View.GONE);
            }

        } else {
            tvMeUserName.setText("未登录");
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
        }
    }

    /**
     * 跳转到WebActivity
     */
    private void gotoWebActivity() {
        String serviceOnlineURL = NetConstantValue.getServiceOnlineURL();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, serviceOnlineURL);
        gotoActivity(mContext, WebActivity.class, bundle);
    }

    /**
     * 得到公司信息
     */
    private void initCompanyInfo() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetCompayInfo getCompayInfo = new GetCompayInfo(mContext);
        getCompayInfo.compayInfo(jsonObject, rlMeTianshenService, true, new BaseNetCallBack<CompanyInfoBean>() {

            @Override
            public void onSuccess(CompanyInfoBean paramT) {
                String wechatId = paramT.getData().getWechat_id();
                String service_telephone = paramT.getData().getService_telephone();
                User user = TianShenUserUtil.getUser(mContext);
                user.setService_telephone(service_telephone);
                user.setWechat_id(wechatId);
                TianShenUserUtil.saveUser(mContext, user);
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
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onLoginoutSuccess(LogoutSuccessEvent event) {
        tvMeUserName.setText("未登录");
    }
}
