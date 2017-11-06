package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息、信用认证入口view
 */
public class AuthMyInfoActivity extends BaseActivity {
    @BindView(R.id.tv_auth_info_back)
    TextView mTvAuthInfoBack;
    @BindView(R.id.tv_auth_info_post)
    TextView mTvAuthInfoPost;
    @BindView(R.id.tv_auth_info_marry_key)
    TextView mTvAuthInfoMarryKey;
    @BindView(R.id.tv_auth_info_marry)
    TextView mTvAuthInfoMarry;
    @BindView(R.id.tv_auth_info_educational_key)
    TextView mTvAuthInfoEducationalKey;
    @BindView(R.id.tv_auth_info_educational)
    TextView mTvAuthInfoEducational;
    @BindView(R.id.red_point)
    ImageView mRedPoint;
    @BindView(R.id.tv_auth_remind1)
    TextView mTvAuthRemind1;
    @BindView(R.id.tv_auth_info_home_address_key)
    TextView mTvAuthInfoHomeAddressKey;
    @BindView(R.id.tv_auth_info_home_address)
    TextView mTvAuthInfoHomeAddress;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.tv_auth_info_money)
    TextView mTvAuthInfomoney;
    @BindView(R.id.et_auth_info_address_details)
    EditText mEtAuthInfoAddressDetails;
    @BindView(R.id.green_point)
    TextView mGreenPoint;
    @BindView(R.id.rl_auth_extral)
    RelativeLayout mRlAuthExtral;
    @BindView(R.id.iv_auth_tb)
    ImageView mIvAuthTb;
    @BindView(R.id.tv_auth_info_work_num)
    TextView mTvAuthInfoWorkNum;

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_auth_one)
    RelativeLayout mRlAuthOne;
    @BindView(R.id.rl_auth_two)
    RelativeLayout mRlAuthTwo;
    @BindView(R.id.rl_auth_three)
    RelativeLayout mRlAuthThree;
    private String TYPE = "1";  //当前页面的标识： 1个人信息认证  2信用认证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_my_info;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

    }

    /**
     * 当前页面UI
     *
     * @param type 当前页面的标识 1个人信息认证  2信用认证
     */
    public void showView(String type) {
        if ("1".equals(type)) {
            mTitle.setText("个人信息认证");
            mRlAuthExtral.setVisibility(View.GONE);
            mRlAuthThree.setVisibility(View.GONE);
            mTvAuthInfoHomeAddressKey.setText("个人信息");
            mTvAuthInfomoney.setText("收款银行卡");

        } else {
            mTitle.setText("信用认证");
            mRlAuthExtral.setVisibility(View.VISIBLE);
            mRlAuthThree.setVisibility(View.VISIBLE);
            mTvAuthInfoHomeAddressKey.setText("手机运营商");
            mTvAuthInfomoney.setText("芝麻信用");
        }

    }

    @OnClick({R.id.rl_auth_one, R.id.rl_auth_two, R.id.rl_auth_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_auth_one:
                break;
            case R.id.rl_auth_two:
                break;
            case R.id.rl_auth_three:
                break;
        }
    }
}
