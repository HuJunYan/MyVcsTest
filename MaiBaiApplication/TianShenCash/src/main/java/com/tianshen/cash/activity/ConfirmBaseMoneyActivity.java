package com.tianshen.cash.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.PhoneInfoUtil;
import com.tianshen.cash.utils.SafeUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

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

    @BindView(R.id.tv_confirm_base_protocol)
    TextView tv_confirm_base_protocol;
    @BindView(R.id.tv_confirm_protocol_2)
    TextView tv_confirm_protocol_2;

    private OrderConfirmBean mOrderConfirmBean;

    private JSONObject mJSONObject;  // 上传用户信息的json
    private int step = 0;   // 获取信息的步数  分别为 app列表 短信 通话记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseLoanInfoData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil mLocationUtil = LocationUtil.getInstance(mContext);
        mLocationUtil.setIsCallBack(false);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_base_money;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvConfirmMoneyBack.setOnClickListener(this);
        tvConfirmApply.setOnClickListener(this);
        tv_confirm_base_protocol.setOnClickListener(this);
        tv_confirm_protocol_2.setOnClickListener(this);
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
                            LocationUtil mLocationUtil = LocationUtil.getInstance(mContext);
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
            case R.id.tv_confirm_apply:
                uploadUserInfo();
                break;
            case R.id.tv_confirm_base_protocol:
                gotoWebActivity(); //借款协议
                break;
            case R.id.tv_confirm_protocol_2:
                gotoWebActivity2();//居间协议
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
        String poundage = mOrderConfirmBean.getData().getPoundage();//手续费
        String amount = mOrderConfirmBean.getData().getAmount();//到账金额
        String bank_name = mOrderConfirmBean.getData().getBank_name();//绑定银行卡所属银行
        String card_num = mOrderConfirmBean.getData().getCard_num();//绑定银行卡卡号
        String repayment_amout = mOrderConfirmBean.getData().getRepayment_amout();//到期还款金额
        try {
            String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
            String poundageY = MoneyUtils.changeF2Y(poundage);
            String amountY = MoneyUtils.changeF2Y(amount);
            String repaymentAmoutY = MoneyUtils.changeF2Y(repayment_amout);
            tvConfirmWithdrawal.setText(consume_amountY + "元");
            tvConfirmProcedures.setText(poundageY + "元");
            tvConfirmTransfer.setText(amountY + "元");
            tvConfirmRepay.setText(repaymentAmoutY + "元");
            tvConfirmTime.setText(timer + "天");
            String cardNum = SafeUtil.encodeBankCardNum(card_num);
            tvConfirmBanckCard.setText(bank_name + cardNum);
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
     */
    private void gotoWebActivity() {

        if (mOrderConfirmBean == null) {
            return;
        }

        String userPayProtocolURL = NetConstantValue.getUserPayProtocolURL();

        String second_party = mOrderConfirmBean.getData().getSecond_party(); //乙方
        String id_num = mOrderConfirmBean.getData().getId_num(); //身份证号
        String unit_address = mOrderConfirmBean.getData().getUnit_address();//单位地址
        String common_address = mOrderConfirmBean.getData().getCommon_address(); //常用地址
        String phone = mOrderConfirmBean.getData().getPhone(); //乙方手机号
        String bank_account = mOrderConfirmBean.getData().getBank_name(); //开户行
        String bank_card = mOrderConfirmBean.getData().getCard_num(); //银联卡账号
        String service_charge = mOrderConfirmBean.getData().getPoundage(); //综合费用
        try {
            service_charge = MoneyUtils.changeF2Y(service_charge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String date_time = mOrderConfirmBean.getData().getDate_time(); // 日期
        String principal = mOrderConfirmBean.getData().getConsume_amount(); //借款本金
        try {
            principal = MoneyUtils.changeF2Y(principal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String expire = mOrderConfirmBean.getData().getExpire(); //到期
        String days = mOrderConfirmBean.getData().getDays(); //共计天数
        String total = mOrderConfirmBean.getData().getTotal(); //年利率
        String protocol_num = mOrderConfirmBean.getData().getProtocol_num();//合同编号
        String overdue = mOrderConfirmBean.getData().getOverdue();// 逾期

        StringBuilder sb = new StringBuilder();
        sb.append(userPayProtocolURL);
        sb.append("?second_party=" + second_party);
        sb.append("&id_num=" + id_num);
        sb.append("&unit_address=" + unit_address);
        sb.append("&common_address=" + common_address);
        sb.append("&phone=" + phone);
        sb.append("&bank_account=" + bank_account);
        sb.append("&bank_card=" + bank_card);
        sb.append("&service_charge=" + service_charge);
        sb.append("&date_time=" + date_time);
        sb.append("&principal=" + principal);
        sb.append("&expire=" + expire);
        sb.append("&days=" + days);
        sb.append("&total=" + total);
        sb.append("&protocol_num=" + protocol_num);
        sb.append("&overdue=" + overdue);

        String userPayProtocolURLF = sb.toString();

        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, userPayProtocolURLF);
        gotoActivity(mContext, WebActivity.class, bundle);
    }

    private void gotoWebActivity2() {
        if (mOrderConfirmBean == null) {
            return;
        }
        String userPayProtocolURL = NetConstantValue.getUserPayServerURL();
        String repay_id = mOrderConfirmBean.getData().getRepay_id();
        String consume_amount = mOrderConfirmBean.getData().getConsume_amount();//借款本金
        StringBuilder sb = new StringBuilder();
        sb.append(userPayProtocolURL);
        sb.append("?" + GlobalParams.USER_CUSTOMER_ID + "=" +TianShenUserUtil.getUserId(this));
        sb.append("&repay_id=" + repay_id);
        sb.append("&consume_amount=" + consume_amount);
        sb.append("&agreement_type=" + "1");

        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, sb.toString());
        gotoActivity(mContext,WebActivity.class,bundle);


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
                        LocationUtil mLocationUtil = LocationUtil.getInstance(mContext);
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
                    PhoneInfoUtil.getMessage_list(ConfirmBaseMoneyActivity.this, myCallBack, message_last_date);
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


}
