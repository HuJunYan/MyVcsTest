package com.tianshen.cash.fragment;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

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
                ToastUtil.showToast(mContext, "点击了借款记录");
                break;
            case R.id.rl_me_bank_card:
                ToastUtil.showToast(mContext, "点击了银行卡");
                break;
            case R.id.rl_me_tianshen_service:
                ToastUtil.showToast(mContext, "点击了联系客服");
                break;
            case R.id.rl_me_about:
                ToastUtil.showToast(mContext, "点击了关于天神贷");
                break;
            case R.id.rl_me_setting:
                ToastUtil.showToast(mContext, "点击了设置");
                break;
        }
    }
}
