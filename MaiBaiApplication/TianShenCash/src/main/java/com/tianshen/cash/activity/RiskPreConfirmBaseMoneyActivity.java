package com.tianshen.cash.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.LocationEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.OrderConfirmBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.api.BaseLoanInfoApply;
import com.tianshen.cash.net.api.GetBaseLoanInfo;
import com.tianshen.cash.net.api.GetLoanVerifyCode;
import com.tianshen.cash.net.api.UploadUserInfoApi;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.PhoneInfoUtil;
import com.tianshen.cash.utils.SpannableUtils;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

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
    private JSONObject mJSONObject;
    private OrderConfirmBean mOrderConfirmBean;
    private String mPoundageY;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshSeverityTextUI();
        }
    };
    private int mStartTime = 59;
    private int MSG_SEVERITY_TIME = 10001;

    @Override
    protected int setContentView() {
        return R.layout.activity_risk_pre_confirm_base_money;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseLoanInfoData();
        initGotoWebData();
    }

    /**
     * 得到确认借款数据
     */
    private void initBaseLoanInfoData() {

        try {
            JSONObject jsonObject = new JSONObject();

            String consume_amount = TianShenUserUtil.getUserConsumeAmount(mContext);
            String repay_id = TianShenUserUtil.getUserRepayId(mContext);
            String customer_id = TianShenUserUtil.getUserId(mContext);
            String location = TianShenUserUtil.getLocation(mContext);
            String city = TianShenUserUtil.getCity(mContext);
            String country = TianShenUserUtil.getCountry(mContext);
            String address = TianShenUserUtil.getAddress(mContext);
            String province = TianShenUserUtil.getProvince(mContext);

            if (TextUtils.isEmpty(location)) {
                RxPermissions rxPermissions = new RxPermissions(RiskPreConfirmBaseMoneyActivity.this);
                rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            LocationUtil mLocationUtil = LocationUtil.getInstance();
                            mLocationUtil.startLocation(RiskPreConfirmBaseMoneyActivity.this);
                            mLocationUtil.setIsCallBack(true);
                        } else {
                            ToastUtil.showToast(mContext, "请打开定位权限!");
                        }
                        return;
                    }
                });
                ToastUtil.showToast(mContext, "请打开定位权限!");
                return;
            }

            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, customer_id);
            jsonObject.put("repay_id", repay_id);
            jsonObject.put("consume_amount", consume_amount);

            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);


            final GetBaseLoanInfo getBaseLoanInfo = new GetBaseLoanInfo(mContext);
            getBaseLoanInfo.baseLoanInfo(jsonObject, null, true, new BaseNetCallBack<OrderConfirmBean>() {
                @Override
                public void onSuccess(OrderConfirmBean bean) {
                    mOrderConfirmBean = bean;
                    refreshUI();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (mOrderConfirmBean == null) {
            return;
        }
        showNormalUI();
    }

    /**
     * 显示正常的UI
     */
    private void showNormalUI() {
        String consume_amount = mOrderConfirmBean.getData().getConsume_amount(); //用户申请金额
        String timer = mOrderConfirmBean.getData().getTimer();//借款时长
        String poundage = mOrderConfirmBean.getData().getPoundage();//综合费
        String amount = mOrderConfirmBean.getData().getAmount();//到账金额
        String bank_name = mOrderConfirmBean.getData().getBank_name();//绑定银行卡所属银行
        String card_num = mOrderConfirmBean.getData().getCard_num();//绑定银行卡卡号
        String repayment_amout = mOrderConfirmBean.getData().getRepayment_amout();//到期还款金额
        String interest = mOrderConfirmBean.getData().getInterest();//利息
        try {
            String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
            mPoundageY = MoneyUtils.changeF2Y(poundage);
//            String amountY = MoneyUtils.changeF2Y(amount);
//            String repaymentAmoutY = MoneyUtils.changeF2Y(repayment_amout);
            tv_risk_pre_money.setText(consume_amountY);
//            tvConfirmProcedures.setText(mPoundageY + "元");
//            tvConfirmTransfer.setText(amountY + "元");
//            tvConfirmRepay.setText(repaymentAmoutY + "元");
            tv_risk_pre_money_days.setText(timer);
            String cardNum = StringUtil.getTianShenCardNum(card_num);
            tv_risk_pre_bank_num.setText(cardNum);
//            String cardNum = SafeUtil.encodeBankCardNum(card_num);
            tv_risk_pre_money_bank.setText(bank_name);
            String interestY = MoneyUtils.changeF2Y(interest);
            tv_risk_pre_money_rates.setText(interestY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //存储ID
        String repayId = mOrderConfirmBean.getData().getRepay_id();
        TianShenUserUtil.saveUserRepayId(mContext, repayId);


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

    @OnClick({R.id.tv_risk_pre_confirm_money_back, R.id.tv_risk_pre_confirm, R.id.tv_risk_pre_money_verify_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_risk_pre_confirm_money_back:
                backActivity();
                break;
            case R.id.tv_risk_pre_confirm:
                showMoneyPopu(mPoundageY);
                break;
            case R.id.tv_risk_pre_money_verify_code: //获取验证码
                getVerifyCode();
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
        if (mOrderConfirmBean == null) {
            return;
        }
        String userPayProtocolURL;
        if (type == 3) {
            userPayProtocolURL = mOrderConfirmBean.getData().getBank_credit_investigation_url();
        } else {
            userPayProtocolURL = NetConstantValue.getUserPayServerURL();
        }
        String repay_id = mOrderConfirmBean.getData().getRepay_id();
        String consume_amount = mOrderConfirmBean.getData().getConsume_amount();//借款本金
        StringBuilder sb = new StringBuilder();
        sb.append(userPayProtocolURL);
        if (type != 3) {
            sb.append("?" + GlobalParams.USER_CUSTOMER_ID + "=" + TianShenUserUtil.getUserId(this));
            sb.append("&repay_id=" + repay_id);
            sb.append("&consume_amount=" + consume_amount);
            sb.append("&agreement_type=" + type);
        }

        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, sb.toString());
        gotoActivity(mContext, WebActivity.class, bundle);
    }


    /**
     * 借钱弹窗提示
     */
    public void showMoneyPopu(String money) {
        if (TextUtils.isEmpty(money)) {
            return;
        }
        String verify_code = et_risk_pre_money_verify_code.getText().toString().trim();
        if (TextUtils.isEmpty(verify_code)) {
            ToastUtil.showToast(mContext, "请输入验证码");
            return;
        }
        View mContentView = View.inflate(this, R.layout.view_popuwindow_money, null);
        final PopupWindow mPopUpWindow = new PopupWindow(mContentView);
        mPopUpWindow.setContentView(mContentView);
        mPopUpWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopUpWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.setFocusable(true);
        mPopUpWindow.setTouchable(true);
        mPopUpWindow.setBackgroundDrawable(null);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        TextView confirm = (TextView) mContentView.findViewById(R.id.confirm);
        TextView content = (TextView) mContentView.findViewById(R.id.content);
        content.setText("借款到账后，将收取" + money + getResources().getString(R.string.text_popu_money));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopUpWindow.dismiss();
                //2.上传信息
                uploadUserInfo();
            }
        });
    }

    /**
     * 获取用户的app列表  通话记录 以及 短信列表
     */
    private void uploadUserInfo() {
        if (mOrderConfirmBean == null) {
            return;
        }
        String loadText = getResources().getText(MemoryAddressUtils.loading()).toString();
        String location = TianShenUserUtil.getLocation(mContext);
        if (TextUtils.isEmpty(location)) {
            RxPermissions rxPermissions = new RxPermissions(RiskPreConfirmBaseMoneyActivity.this);
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        LocationUtil mLocationUtil = LocationUtil.getInstance();
                        mLocationUtil.startLocation(RiskPreConfirmBaseMoneyActivity.this);
                        mLocationUtil.setIsCallBack(true);
                    }
                    return;
                }
            });
            ToastUtil.showToast(mContext, "请打开定位权限!");
            return;
        }
        ViewUtil.createLoadingDialog(this, loadText, false);
        mJSONObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(this);
        try {
            mJSONObject.put(GlobalParams.USER_CUSTOMER_ID, userId); //用户id
            mJSONObject.put("location", location); //用户定位信息
            mJSONObject.put("is_wifi", PhoneInfoUtil.getNetworkType(this) ? "1" : "0");
            mJSONObject.put("device_id", UserUtil.getDeviceId(this));
            PhoneInfoUtil.getApp_list(this, myCallBack);
            String call_last_time = null;
            OrderConfirmBean.Data data = mOrderConfirmBean.getData();
            if (data != null) {
                call_last_time = data.getCall_last_time();
            }
            PhoneInfoUtil.getCall_list(this, myCallBack, call_last_time);
        } catch (JSONException e) {
            e.printStackTrace();
            ViewUtil.cancelLoadingDialog();
        }
    }

    private int step;
    private PhoneInfoUtil.PhoneInfoCallback myCallBack = new PhoneInfoUtil.PhoneInfoCallback() {
        @Override
        public void sendMessageToRegister(JSONArray jsonArray, String jsonArrayName) {
            try {
                step++;
                if (GlobalParams.USER_INFO_CALL_LIST.equals(jsonArrayName)) {
                    String message_last_date = null;
                    OrderConfirmBean.Data data = mOrderConfirmBean.getData();
                    if (data != null) {
                        message_last_date = data.getMsg_last_time();
                    }
                    if (!isFinishing()) {
                        PhoneInfoUtil.getMessage_list(RiskPreConfirmBaseMoneyActivity.this, myCallBack, message_last_date);
                    }
                }
                mJSONObject.put(jsonArrayName, jsonArray);
                if (step == 3) {
                    step = 0;
//                    LogUtil.d("userinfo", mJSONObject.toString());
                    startUploadUserInfo();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                step = 0;
                ViewUtil.cancelLoadingDialog();
            }
        }
    };


    // 开始上传用户信息
    private void startUploadUserInfo() {
        UploadUserInfoApi uploadUserInfoApi = new UploadUserInfoApi(this);
        uploadUserInfoApi.uploadUserInfo(mJSONObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                MaiDianUtil.ding(getBaseContext(), MaiDianUtil.FLAG_19);
                onClickApply();
                LogUtil.d("userinfo", "code = " + paramT.getCode() + "msg = " + paramT.getMsg());
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                onClickApply();
                LogUtil.d("userinfo", "failure" + url + ",errortype = " + errorType + ",errorcode = " + errorCode);
            }
        });
    }


    /**
     * 从认证中心返回主页
     */
    @Subscribe
    public void onAuthCenterBack(LocationEvent event) {
        LogUtil.d("abc", "收到了定位成功的消息");
        uploadUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        LocationUtil mLocationUtil = LocationUtil.getInstance();
        mLocationUtil.setIsCallBack(false);
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        if (mOrderConfirmBean == null) {
            return;
        }
        String verify_code = et_risk_pre_money_verify_code.getText().toString().trim();
        if (TextUtils.isEmpty(verify_code)) {
            ToastUtil.showToast(mContext, "请输入验证码");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {

            String customer_id = TianShenUserUtil.getUserId(mContext);
            String jpush_id = TianShenUserUtil.getUserJPushId(mContext);
            String location = TianShenUserUtil.getLocation(mContext);
            String city = TianShenUserUtil.getCity(mContext);
            String country = TianShenUserUtil.getCountry(mContext);
            String address = TianShenUserUtil.getAddress(mContext);
            String province = TianShenUserUtil.getProvince(mContext);

            String consume_amount = mOrderConfirmBean.getData().getConsume_amount(); //用户申请金额
            final String repay_id = mOrderConfirmBean.getData().getRepay_id();

            String black_box = new GetTelephoneUtils(mContext).getBlackBox();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, customer_id);
            jsonObject.put("repay_id", repay_id);
            jsonObject.put("consume_amount", consume_amount);
            jsonObject.put("push_id", jpush_id);
            jsonObject.put("black_box", black_box);

            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);
            jsonObject.put("verify_code", verify_code); //验证码


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final BaseLoanInfoApply baseLoanInfoApply = new BaseLoanInfoApply(mContext);
        baseLoanInfoApply.baseLoanInfoApply(jsonObject, tv_risk_pre_confirm, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                if (paramT.getCode() == 0) {
                    gotoMainActivity();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
            }
        });

    }


    /**
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()) {
            return;
        }

        tv_risk_pre_money_verify_code.setText(String.valueOf(mStartTime));
        mStartTime--;
        if (mStartTime == 0) {
            tv_risk_pre_money_verify_code.setText("重获取验证码");
            mStartTime = 59;
            tv_risk_pre_money_verify_code.setEnabled(true);
            mHandler.removeMessages(MSG_SEVERITY_TIME);
        } else {
            tv_risk_pre_money_verify_code.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, 1000);
        }

    }

    /**
     * 回到首页
     */
    private void gotoMainActivity() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        gotoActivity(mContext, MainActivity.class, null);
        finish();
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        if (mOrderConfirmBean == null || mOrderConfirmBean.getData() == null) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
//        GetVerifyCode getVerifyCode = new GetVerifyCode(mContext);
        GetLoanVerifyCode getVerifyCode = new GetLoanVerifyCode(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
//            jsonObject.put("mobile", mOrderConfirmBean.getData().getPhone());
//            jsonObject.put("type", "9");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //GetLoanVerifyCode getVerifyCode

        getVerifyCode.send(jsonObject, tv_risk_pre_money_verify_code, true, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                ToastUtil.showToast(mContext, "验证码发送成功");
                refreshSeverityTextUI();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });


    }

}
