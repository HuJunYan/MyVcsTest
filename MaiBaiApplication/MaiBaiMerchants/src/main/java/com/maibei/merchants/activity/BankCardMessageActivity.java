package com.maibei.merchants.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.base.MyApplication;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.BindBandCard;
import com.maibei.merchants.net.api.GetReserveMobileVerifyCode;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.BankCardEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/7/27.
 */
public class BankCardMessageActivity extends BaseActivity implements View.OnClickListener, BankCardEditText.MyEditTextListener {

    private BankCardEditText et_bank_num;
    private Button bt_over;
    private TextView tv_phone_tip;

    private String bankCard;//卡号
    private String idNum;//身份证号
    private String cardOwner;//持卡人
    private String reserveMobile;//银行预留手机
    private String verifyCode= "";//验证码

    private String cardType = "0";//账号类型
    private String bankId;//银行ID编号
    private String bankBrhname;//开户行支行全称
    private CheckBox cb_agree;
    private TextView tv_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_bank_message;
    }

    @Override
    protected void findViews() {
        et_bank_num = (BankCardEditText)findViewById(R.id.et_bank_num);
        bt_over = (Button)findViewById(R.id.bt_over);
        tv_phone_tip = (TextView)findViewById(R.id.tv_phone_tip);
        cb_agree =(CheckBox)findViewById(R.id.cb_agree);
        tv_agree = (TextView)findViewById(R.id.tv_agree);
    }

    @Override
    protected void setListensers() {
        bt_over.setOnClickListener(this);
        et_bank_num.setListener(this);
        et_bank_num.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                verifyCode = s;
                if (verifyCode != "") {
                    bt_over.setEnabled(true);
                }
            }
        });
        tv_agree.setOnClickListener(this);
    }

    private void init(){
        Bundle bundle = this.getIntent().getExtras();
        bankCard = bundle.getString("bank_card");
        idNum = bundle.getString("id_num");
        cardOwner = bundle.getString("card_owner");
        reserveMobile = bundle.getString("reserve_mobile");
        bankId = bundle.getString("bank_id");
        bankBrhname = bundle.getString("bank_brhname");
        String interMoble = reserveMobile.substring(0, 3)+"****"+reserveMobile.substring(7, reserveMobile.length());
        tv_phone_tip.setText("请输入手机号为" + interMoble + "收到的验证码");
//        tv_phone_tip.setText("请输入手机号为186****3890收到的验证码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_over:
                bindBankCard();
                break;
            case R.id.tv_agree:
                gotoActivity(BankCardMessageActivity.this, ProtocolActivity.class, null);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onRightClick(View view) {
        switch (view.getId()) {
            case R.id.et_bank_num:
                return getVerityCode();
            default:
                break;
        }
        return false;
    }

    private boolean getVerityCode() {
        GetReserveMobileVerifyCode getReserveMobileVerifyCode = new GetReserveMobileVerifyCode(this);
        JSONObject mjson = new JSONObject();
        try{
            mjson.put("mobile", reserveMobile);
            mjson.put("type", cardType);
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        getReserveMobileVerifyCode.getReserveMobileVerifyCode(mjson, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                if (paramT.getCode() == 0) {
                    ToastUtil.showToast(mContext, "再次获取验证码成功");
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
        return true;
    }

    private void bindBankCard(){
        verifyCode = et_bank_num.getEditTextString();
        if(TextUtils.isEmpty(verifyCode)){
            ToastUtil.showToast(mContext, "验证码不能为空");
            return;
        }
        if(!cb_agree.isChecked()){
            ToastUtil.showToast(mContext, "请同意《分期业务合作协议及平台服务协议》");
            return;
        }
        BindBandCard bindBandCard = new BindBandCard(this);
        JSONObject mJsonObject = new JSONObject();
        try{
            mJsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            mJsonObject.put("bank_card", bankCard);
            mJsonObject.put("id_num", idNum);
            mJsonObject.put("card_owner", cardOwner);
            mJsonObject.put("reserve_mobile", reserveMobile);
            mJsonObject.put("card_type", cardType);
            mJsonObject.put("bank_id", bankId);
            mJsonObject.put("bank_brhname", bankBrhname);
            mJsonObject.put("verify_code", verifyCode);
            bindBandCard.bindBandCard(mJsonObject, bt_over, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    gotoActivity(mContext,ToExamineActivity.class,null);
                    ((MyApplication) getApplication()).clearTempActivityInBackStack(ToExamineActivity.class);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

}
