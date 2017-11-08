package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.SpannableUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wang on 2017/11/8.
 */

public class RiskPreConfirmBaseMoneyActivity extends BaseActivity {

    @BindView(R.id.tv_risk_pre_money)
    TextView tv_risk_pre_money; //金额view
    @BindView(R.id.tv_risk_pre_money_days)
    TextView tv_risk_pre_money_days; //借款天数
    @BindView(R.id.tv_risk_pre_money_rates)
    TextView tv_risk_pre_money_rates; //借款利息
    @BindView(R.id.tv_risk_pre_money_bank)
    TextView tv_risk_pre_money_bank; //开户银行
    @BindView(R.id.tv_risk_pre_bank_num)
    TextView tv_risk_pre_bank_num;// 银行卡号
    @BindView(R.id.tv_risk_pre_money_verify_code)
    TextView tv_risk_pre_money_verify_code;//获取验证码按钮
    @BindView(R.id.et_risk_pre_money_verify_code)
    EditText et_risk_pre_money_verify_code;//验证码输入框
    @BindView(R.id.check_box)
    CheckBox check_box;//选择框
    @BindView(R.id.tv_risk_pre_agreement)
    TextView tv_risk_pre_agreement; //协议
    @BindView(R.id.tv_risk_pre_confirm)
    TextView tv_risk_pre_confirm;
    private List<CharacterStyle> ssList;

    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_confirm_base_money;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGotoWebData();
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void setListensers() {
        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_risk_pre_confirm.setBackground(getResources().getDrawable(R.drawable.shape_home_button_border));
                    tv_risk_pre_confirm.setEnabled(true);
                } else {
                    tv_risk_pre_confirm.setBackground(getResources().getDrawable(R.drawable.shape_home_button_unchecked));
                    tv_risk_pre_confirm.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.tv_risk_pre_confirm_money_back, R.id.tv_risk_pre_confirm,R.id.tv_risk_pre_money_verify_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_risk_pre_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_risk_pre_confirm:
                //TODO  下单
                break;
            case R.id.tv_risk_pre_money_verify_code: //获取验证码
                break;

        }
    }


    private void initGotoWebData() {
        if (ssList == null) {
            ssList = new ArrayList<>();
        }
        ssList.clear();
        ssList.add(webSpan);
        ssList.add(webSpan2);
        ssList.add(webSpan3);
        String text = getResources().getString(R.string.text_risk_pre_agreement);
        SpannableUtils.setWebSpannableString(tv_risk_pre_agreement, text, "《", "》", ssList, getResources().getColor(R.color.global_txt_orange));
    }

    private ClickableSpan webSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(2);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    };
    private ClickableSpan webSpan2 = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(1);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    };
    private ClickableSpan webSpan3 = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(3);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    };


    /**
     * 跳转到WebActivity
     *
     * @param type 1 居间协议 2 借款协议 3 人行征信协议
     */
    private void gotoWebActivity(int type) {
        //TODO 协议
//        if (mOrderConfirmBean == null) {
//            return;
//        }
//        String userPayProtocolURL;
//        if (type == 3) {
//            userPayProtocolURL = mOrderConfirmBean.getData().getBank_credit_investigation_url();
//        } else {
//            userPayProtocolURL = NetConstantValue.getUserPayServerURL();
//        }
//        String repay_id = mOrderConfirmBean.getData().getRepay_id();
//        String consume_amount = mOrderConfirmBean.getData().getConsume_amount();//借款本金
        StringBuilder sb = new StringBuilder();
//        sb.append(userPayProtocolURL);
//        if (type != 3) {
//            sb.append("?" + GlobalParams.USER_CUSTOMER_ID + "=" + TianShenUserUtil.getUserId(this));
//            sb.append("&repay_id=" + repay_id);
//            sb.append("&consume_amount=" + consume_amount);
//            sb.append("&agreement_type=" + type);
//        }

        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, sb.toString());
        gotoActivity(mContext, WebActivity.class, bundle);


    }
}
