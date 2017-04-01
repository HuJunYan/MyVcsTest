package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息
 */

public class AuthInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_auth_info_back)
    TextView tvAuthInfoBack;
    @BindView(R.id.tv_auth_info_post)
    TextView tvAuthInfoPost;
    @BindView(R.id.tv_auth_info_qq)
    TextView tvAuthInfoQq;
    @BindView(R.id.et_auth_info_qq)
    EditText etAuthInfoQq;
    @BindView(R.id.tv_auth_info_home_address)
    TextView tvAuthInfoHomeAddress;
    @BindView(R.id.et_auth_info_home_address)
    EditText etAuthInfoHomeAddress;
    @BindView(R.id.tv_auth_info_address_empty)
    TextView tvAuthInfoAddressEmpty;
    @BindView(R.id.et_auth_info_address_details)
    EditText etAuthInfoAddressDetails;
    @BindView(R.id.tv_auth_info_work_name)
    TextView tvAuthInfoWorkName;
    @BindView(R.id.et_auth_info_work_name)
    EditText etAuthInfoWorkName;
    @BindView(R.id.tv_auth_info_work_num)
    TextView tvAuthInfoWorkNum;
    @BindView(R.id.et_auth_info_work_num)
    EditText etAuthInfoWorkNum;
    @BindView(R.id.tv_auth_info_work_address)
    TextView tvAuthInfoWorkAddress;
    @BindView(R.id.et_auth_info_work_address)
    EditText etAuthInfoWorkAddress;
    @BindView(R.id.tv_auth_info_work_address_empty)
    TextView tvAuthInfoWorkAddressEmpty;
    @BindView(R.id.et_auth_info_work_address_details)
    EditText etAuthInfoWorkAddressDetails;

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_info;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthInfoPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_info_post:
                break;
        }
    }

    /**
     * 提交个人信息
     */
    private void postUserInfo() {

    }


}
