package com.maibai.cash.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.utils.ToastUtil;

import butterknife.BindView;

/**
 * 身份认证页面
 */

public class AuthIdentityActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_identity_auth_back)
    TextView tvIdentityAuthBack;
    @BindView(R.id.tv_identity_post)
    TextView tvIdentityPost;
    @BindView(R.id.tv_identity_auth_name)
    TextView tvIdentityAuthName;
    @BindView(R.id.et_identity_auth_name)
    EditText etIdentityAuthName;
    @BindView(R.id.tv_identity_auth_num)
    TextView tvIdentityAuthNum;
    @BindView(R.id.et_identity_auth_num)
    EditText etIdentityAuthNum;
    @BindView(R.id.tv_identity_auth_pic_key)
    TextView tvIdentityAuthPicKey;
    @BindView(R.id.tv_identity_auth_pic)
    TextView tvIdentityAuthPic;
    @BindView(R.id.tv_identity_auth_pic2_key)
    TextView tvIdentityAuthPic2Key;
    @BindView(R.id.tv_identity_auth_pic2)
    TextView tvIdentityAuthPic2;
    @BindView(R.id.tv_identity_auth_face_key)
    TextView tvIdentityAuthFaceKey;
    @BindView(R.id.tv_identity_auth_face)
    TextView tvIdentityAuthFace;

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_identity;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {

        etIdentityAuthName.setEnabled(false);
        etIdentityAuthNum.setEnabled(false);
        tvIdentityAuthPic.setOnClickListener(this);
        tvIdentityAuthPic2.setOnClickListener(this);
        tvIdentityAuthFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_identity_auth_pic:
                onClickIdentity();
                break;
            case R.id.tv_identity_auth_pic2:
                onClickIdentityBack();
                break;
            case R.id.tv_identity_auth_face:
                onClickFace();
                break;
        }
    }

    /**
     * 点击了身份证正面
     */
    private void onClickIdentity() {

    }

    /**
     * 点击了身份证反面
     */
    private void onClickIdentityBack() {

    }

    /**
     * 点击了人脸识别
     */
    private void onClickFace() {

    }
}
