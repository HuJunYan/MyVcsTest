package com.tianshen.cash.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.AboutMaibeiActivity;
import com.tianshen.cash.activity.ConsumptionRecordActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.activity.MyBankCardActivity;
import com.tianshen.cash.activity.SettingActivity;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.TianShenUserUtil;

import butterknife.BindView;

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
    }

    private void refreshUI() {
        String phoneNum = TianShenUserUtil.getUserPhoneNum(mContext);
        if (TextUtils.isEmpty(phoneNum)) {
            tvMeUserName.setText("未登录");
        } else {
            tvMeUserName.setText(phoneNum);
        }
    }

    @Override
    public void onClick(View view) {

        if (!TianShenUserUtil.isLogin(mContext)) {
            gotoActivity(mContext, LoginActivity.class, null);
        }

        switch (view.getId()) {
            case R.id.rl_me_user:
                break;
            case R.id.rl_me_history:
                gotoActivity(mContext, ConsumptionRecordActivity.class, null);
                break;
            case R.id.rl_me_bank_card:
                gotoActivity(mContext, MyBankCardActivity.class, null);
                break;
            case R.id.rl_me_tianshen_service:
                showMyKefu();
                break;
            case R.id.rl_me_about:
                gotoActivity(mContext, AboutMaibeiActivity.class, null);
                break;
            case R.id.rl_me_setting:
                gotoActivity(mContext, SettingActivity.class, null);
                break;
        }
    }

    private void showMyKefu(){
        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.withdrawals_diaog).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_mykefu, null);
        TextView tv_cancel=(TextView)view.findViewById(R.id.tv_cancel);
        TextView tv_call=(TextView)view.findViewById(R.id.tv_call);

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new GetTelephoneUtils(mContext).changeLight();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13001137644"));
                startActivity(intent);
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
}
