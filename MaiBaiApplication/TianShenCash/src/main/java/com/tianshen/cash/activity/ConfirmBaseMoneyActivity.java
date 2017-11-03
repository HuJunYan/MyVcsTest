package com.tianshen.cash.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Spanned;
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
import com.tianshen.cash.utils.SafeUtil;
import com.tianshen.cash.utils.SpannableUtils;
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
import io.reactivex.functions.Consumer;

import static com.tianshen.cash.R.id.tv_confirm_apply;

/**
 * 确认申请页面
 */

public class ConfirmBaseMoneyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_confirm_money_back)
    TextView tvConfirmMoneyBack;
    @BindView(R.id.tv_confirm_withdrawal_key)
    TextView tvConfirmWithdrawalKey;
    @BindView(R.id.tv_confirm_withdrawal)
    TextView tvConfirmWithdrawal;
    @BindView(R.id.tv_confirm_time_key)
    TextView tvConfirmTimeKey;
    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    @BindView(R.id.tv_confirm_procedures_key)
    TextView tvConfirmProceduresKey;
    @BindView(R.id.tv_confirm_procedures)
    TextView tvConfirmProcedures;
    @BindView(R.id.tv_confirm_transfer_key)
    TextView tvConfirmTransferKey;
    @BindView(R.id.tv_confirm_transfer)
    TextView tvConfirmTransfer;
    @BindView(R.id.tv_confirm_banck_card_key)
    TextView tvConfirmBanckCardKey;
    @BindView(R.id.tv_confirm_banck_card)
    TextView tvConfirmBanckCard;
    @BindView(R.id.tv_confirm_repay_key)
    TextView tvConfirmRepayKey;
    @BindView(R.id.tv_confirm_repay)
    TextView tvConfirmRepay;
    @BindView(R.id.tv_confirm_apply)
    TextView tvConfirmApply;
    @BindView(R.id.check_box)
    CheckBox check_box;
    @BindView(R.id.tv_confirm_rate_money)
    TextView tv_confirm_rate_money;
    @BindView(R.id.tv_confirm_base_protocol)
    TextView tv_confirm_base_protocol;

    private OrderConfirmBean mOrderConfirmBean;

    private JSONObject mJSONObject;  // 上传用户信息的json
    private int step = 0;   // 获取信息的步数  分别为 app列表 短信 通话记录
    private List<CharacterStyle> ssList;
    private String mPoundageY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseLoanInfoData();
        MaiDianUtil.ding(this,MaiDianUtil.FLAG_17);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil mLocationUtil = LocationUtil.getInstance();
        mLocationUtil.setIsCallBack(false);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_base_money;
    }

    @Override
    protected void findViews() {
        initGotoWebData();
    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmApply.setOnClickListener(this);
        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvConfirmApply.setBackground(getResources().getDrawable(R.drawable.shape_home_button_border));
                    tvConfirmApply.setEnabled(true);
                } else {
                    tvConfirmApply.setBackground(getResources().getDrawable(R.drawable.shape_home_button_unchecked));
                    tvConfirmApply.setEnabled(false);
                }
            }
        });
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
                RxPermissions rxPermissions = new RxPermissions(ConfirmBaseMoneyActivity.this);
                rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            LocationUtil mLocationUtil = LocationUtil.getInstance();
                            mLocationUtil.startLocation(ConfirmBaseMoneyActivity.this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_money_back:
                backActivity();
                break;
            case tv_confirm_apply:
                //1.弹窗信息
                showMoneyPopu(mPoundageY);
                MaiDianUtil.ding(this,MaiDianUtil.FLAG_18);
                break;
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
        tvConfirmMoneyBack.setVisibility(View.VISIBLE);

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
            String amountY = MoneyUtils.changeF2Y(amount);
            String repaymentAmoutY = MoneyUtils.changeF2Y(repayment_amout);
            tvConfirmWithdrawal.setText(consume_amountY + "元");
            tvConfirmProcedures.setText(mPoundageY + "元");
            tvConfirmTransfer.setText(amountY + "元");
            tvConfirmRepay.setText(repaymentAmoutY + "元");
            tvConfirmTime.setText(timer + "天");
            String cardNum = SafeUtil.encodeBankCardNum(card_num);
            tvConfirmBanckCard.setText(bank_name + cardNum);
            String interestY = MoneyUtils.changeF2Y(interest);
            tv_confirm_rate_money.setText(interestY + "元");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //存储ID
        String repayId = mOrderConfirmBean.getData().getRepay_id();
        TianShenUserUtil.saveUserRepayId(mContext, repayId);


    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
        if (mOrderConfirmBean == null) {
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


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final BaseLoanInfoApply baseLoanInfoApply = new BaseLoanInfoApply(mContext);
        baseLoanInfoApply.baseLoanInfoApply(jsonObject, tvConfirmApply, new BaseNetCallBack<PostDataBean>() {
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
     * 跳转到WebActivity
     *
     * @param type 1 居间协议 2 借款协议 3 人行征信协议
     */
    private void gotoWebActivity(int type) {
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
     * 回到首页
     */
    private void gotoMainActivity() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        gotoActivity(mContext, MainActivity.class, null);
        finish();
    }

    /**
     * 从认证中心返回主页
     */
    @Subscribe
    public void onAuthCenterBack(LocationEvent event) {
        LogUtil.d("abc", "收到了定位成功的消息");
        uploadUserInfo();
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
            RxPermissions rxPermissions = new RxPermissions(ConfirmBaseMoneyActivity.this);
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        LocationUtil mLocationUtil = LocationUtil.getInstance();
                        mLocationUtil.startLocation(ConfirmBaseMoneyActivity.this);
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
                        PhoneInfoUtil.getMessage_list(ConfirmBaseMoneyActivity.this, myCallBack, message_last_date);
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
                MaiDianUtil.ding(getBaseContext(),MaiDianUtil.FLAG_19);
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

    int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    private void initGotoWebData() {
        if (ssList == null) {
            ssList = new ArrayList<>();
        }
        ssList.clear();
        ssList.add(webSpan);
        ssList.add(webSpan2);
//        ssList.add(webSpan3);
        String text = getResources().getString(R.string.confirm_protocol_all_text);
        SpannableUtils.setWebSpannableString(tv_confirm_base_protocol, text, "《", "》", ssList, getResources().getColor(R.color.global_txt_orange));
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
//    private ClickableSpan webSpan3 = new ClickableSpan() {
//        @Override
//        public void onClick(View widget) {
//            gotoWebActivity(3);
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
////            super.updateDrawState(ds);
//            ds.setUnderlineText(false);
//        }
//    };

    /**
     * 借钱弹窗提示
     */
    public void showMoneyPopu(String money) {
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
}
