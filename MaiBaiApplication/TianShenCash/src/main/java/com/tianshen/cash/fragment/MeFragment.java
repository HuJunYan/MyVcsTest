package com.tianshen.cash.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.model.User;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeFragment extends BaseFragment {


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

    }

    private void refreshUI() {
        String phoneNum = TianShenUserUtil.getUserPhoneNum(mContext);
        if (TextUtils.isEmpty(phoneNum)) {
            tvMeUserName.setText("未登录");
        } else {
            tvMeUserName.setText(phoneNum);
        }
    }
}
