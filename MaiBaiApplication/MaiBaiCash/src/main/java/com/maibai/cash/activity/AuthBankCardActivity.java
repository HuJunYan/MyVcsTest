package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.utils.ToastUtil;

import butterknife.BindView;

/**
 * 银行卡信息
 */

public class AuthBankCardActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_auth_bank_card_back)
    TextView tvAuthBankCardBack;
    @BindView(R.id.tv_auth_info_post)
    TextView tvAuthInfoPost;
    @BindView(R.id.tv_auth_bank_card_person)
    TextView tvAuthBankCardPerson;
    @BindView(R.id.et_auth_bank_card_person)
    EditText etAuthBankCardPerson;
    @BindView(R.id.tv_bank_card_key)
    TextView tvBankCardKey;
    @BindView(R.id.et_bank_card)
    EditText etBankCard;
    @BindView(R.id.tv_bank_card_num_key)
    TextView tvBankCardNumKey;
    @BindView(R.id.et_auth_info_work_name)
    EditText etAuthInfoWorkName;
    @BindView(R.id.tv_bank_card_phone_num_key)
    TextView tvBankCardPhoneNumKey;
    @BindView(R.id.et_bank_card_phone_num)
    EditText etBankCardPhoneNum;
    @BindView(R.id.tv_severity_code_key)
    TextView tvSeverityCodeKey;
    @BindView(R.id.et_severity_code)
    EditText etSeverityCode;
    @BindView(R.id.tv_severity_code)
    TextView tvSeverityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_bank_card;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_center_back:
                backActivity();
                break;
            case R.id.tv_auth_center_post:
                ToastUtil.showToast(mContext, "点击了提交");
                break;
        }
    }

    private void onClickItem(int position) {
    }


}
