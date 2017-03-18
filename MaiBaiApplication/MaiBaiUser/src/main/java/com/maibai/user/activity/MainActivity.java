package com.maibai.user.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.maibai.user.R;
import com.maibai.user.adapter.MyViewPagerAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.fragment.MyFragment;
import com.maibai.user.fragment.WithdrawalsFragment;
import com.maibai.user.fragment.ZeroYuanBuyFragment;
import com.maibai.user.model.OrderRealPayBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.OrderCancel;
import com.maibai.user.net.api.OrderQuery;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LocationUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.RequestPermissionUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.Utils;
import com.maibai.user.utils.WithdrawalsApplyResultUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RadioButton rb_zero_yuan_buy, rb_my, rb_withdrawals;
    private ZeroYuanBuyFragment mZeroYuanBuyFragment;
    private MyFragment mMyFragment;
    private WithdrawalsFragment mWithdrawalsFragment;
    private ViewPager vp_main;
    private ArrayList<Fragment> mFragmentList;
    private MyViewPagerAdapter mViewPagerAdapter;
    private LoginBordcast mLoginBordcast;
    private LocationUtil mLocationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestPermissionUtil requestPermissionUtil=new RequestPermissionUtil(mContext);
        requestPermissionUtil.requestCameraContactsLocationPermission();
        mLocationUtil = LocationUtil.getInstance(MainActivity.this);
        regist();
        InitView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mLocationUtil.startLocation();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        rb_zero_yuan_buy = (RadioButton) findViewById(R.id.rb_zero_yuan_buy);
        rb_withdrawals = (RadioButton) findViewById(R.id.rb_withdrawals);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_zero_yuan_buy:
                vp_main.setCurrentItem(0);
                break;
            case R.id.rb_withdrawals:
                vp_main.setCurrentItem(1);
                break;
            case R.id.rb_my:
                vp_main.setCurrentItem(2);
                break;
            default:
                break;
        }
    }
    public int getTopFragmentIndex(){
        if(rb_zero_yuan_buy.isChecked()){
            return 0;
        }else if(rb_withdrawals.isChecked()){
                return 1;
        }else if(rb_my.isChecked()){
            return 2;
        }
        return -1;
    }
    @Override
    protected void setListensers() {
        rb_zero_yuan_buy.setOnClickListener(this);
        rb_withdrawals.setOnClickListener(this);
        rb_my.setOnClickListener(this);
        vp_main.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int id) {
        switch (id) {
            case 0:
                rb_zero_yuan_buy.setChecked(true);
                break;
            case 1:
                rb_withdrawals.setChecked(true);
                break;
            case 2:
                rb_my.setChecked(true);
                break;
            default:
                break;
        }
    }


    private void addFragment() {
        if (mZeroYuanBuyFragment == null) {
            mZeroYuanBuyFragment = new ZeroYuanBuyFragment();
        }
        if (mWithdrawalsFragment == null) {
            mWithdrawalsFragment = new WithdrawalsFragment();
        }
        if (mMyFragment == null) {
            mMyFragment = new MyFragment();
        }

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<Fragment>();// 初始化数据
            mFragmentList.add(mZeroYuanBuyFragment);
            mFragmentList.add(mWithdrawalsFragment);
            mFragmentList.add(mMyFragment);
        }
    }

    private void setViewPager() {
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
            vp_main.setAdapter(mViewPagerAdapter);
            vp_main.setOffscreenPageLimit(3);
        }
    }

    private void InitView() {
        addFragment();
        setViewPager();
        rb_zero_yuan_buy.performClick();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationUtil.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLoginBordcast);
    }

    private void regist() {
        IntentFilter intentFilter = new IntentFilter(GlobalParams.GOTO_LOGIN_ACTIVITY);
        intentFilter.addAction(GlobalParams.LOGOUT_ACTION);
        mLoginBordcast = new LoginBordcast();
        registerReceiver(mLoginBordcast, intentFilter);
    }

    public class LoginBordcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(null==action){
                action="";
            }
            if (GlobalParams.LOGOUT_ACTION.equals(action)) {
                rb_zero_yuan_buy.performClick();
            }
        }
    }

    public void validateLoginStatus() {
        try {
            switch (UserUtil.getStatus(mContext)) {
                case GlobalParams.HAVE_BEEN_VERIFY:  // 已审核
                    break;
                case GlobalParams.WAIT_PAY_SHOUFU: { // 待交首付
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_INSTALLMENT);
                    bundle.putInt(GlobalParams.ENTER_DOWN_PAYMENT_PAGE_TYPE_KEY, GlobalParams.ENTER_DOWN_PAYMENT_PAGE_FROM_SIGNIN);
                    gotoActivity(mContext, VerifySuccessActivity.class, bundle);
                }
                break;
                case GlobalParams.WAIT_VERIFY: { // 待审核
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_INSTALLMENT);
                    bundle.putString(GlobalParams.CONSUME_ID_KEY, UserUtil.getConsumeId(mContext));
                    gotoActivity(mContext, WaitVerifyPayActivity.class, bundle);
                }
                break;
                case GlobalParams.REFUSE_BY_PERSON: {  // 人工拒绝
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalParams.REFUSE_TYPE_KEY, GlobalParams.REFUSE_BY_PERSON_TYPE);
                    gotoActivity(mContext, VerifyFailActivity.class, bundle);
                }
                break;
                case GlobalParams.REFUSE_BY_MACHINE: {  // 策略拒绝
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalParams.REFUSE_TYPE_KEY, GlobalParams.REFUSE_BY_MACHINE_TYPE);
                    gotoActivity(mContext, VerifyFailActivity.class, bundle);
                }
                break;
                case GlobalParams.WAIT_FOR_PAY: // 订单待付款
                    showDialog();
                    break;

            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void validateCashApplyStatus() {
        try {
            String installmentStatus = UserUtil.getStatus(mContext);
            if (GlobalParams.WAIT_VERIFY.equals(installmentStatus)
                    || GlobalParams.WAIT_PAY_SHOUFU.equals(installmentStatus)
                    || GlobalParams.REFUSE_BY_PERSON.equals(installmentStatus)
                    || GlobalParams.REFUSE_BY_MACHINE.equals(installmentStatus)
                    || GlobalParams.WAIT_FOR_PAY.equals(installmentStatus)) {
                return;
            }
            switch (UserUtil.getCashCreditStatus(mContext)) {
                case GlobalParams.CASH_APPLY_HAVE_BEEN_VERIFY:
                    if ("1".equals(UserUtil.getCashNeedPop(mContext))) {
                        String amount = UserUtil.getCashAmount(mContext);
                        new WithdrawalsApplyResultUtil(mContext).showSuccessDialog((int) (Double.valueOf(amount) / 100) + "");
                    }
                    break;
                case GlobalParams.CASH_APPLY_WAIT_VERIFY: {
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
                    bundle.putString(GlobalParams.WITHDRAWALS_VERIFY_ID_KEY, UserUtil.getCashCreditConsumeId(mContext));
                    gotoActivity(mContext, WaitVerifyWithdrawalActivity.class, bundle);//提现待审核
                }
                break;
                case GlobalParams.CASH_APPLY_REFUSE_BY_PERSON: {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
//                    bundle.putInt(GlobalParams.REFUSE_TYPE_KEY, GlobalParams.REFUSE_BY_PERSON_TYPE);
//                    gotoActivity(mContext, VerifyFailActivity.class, bundle);
                }
                break;
                case GlobalParams.CASH_APPLY_REFUSE_BY_MACHINE: {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
//                    bundle.putInt(GlobalParams.REFUSE_TYPE_KEY, GlobalParams.REFUSE_BY_MACHINE_TYPE);
//                    gotoActivity(mContext, VerifyFailActivity.class, bundle);
                }
                break;
            }
        }catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("您有未完成的订单，确认下单吗?");
        builder.setTitle("提示");
//        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //删除订单的接口
//                cancelOrder();
//                dialog.dismiss();
//            }
//        });
        builder.setNegativeButton("下单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //下单
            orderRealPay();
            dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void cancelOrder() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", UserUtil.getConsumeId(mContext));
            new OrderCancel(mContext).orderCancel(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    ToastUtil.showToast(mContext, "订单已取消");
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void orderRealPay() {
        try {
            OrderQuery orderRealPay = new OrderQuery(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("consume_id", UserUtil.getConsumeId(mContext));
            orderRealPay.orderQuery(jsonObject, null, true, new BaseNetCallBack<OrderRealPayBean>() {
                @Override
                public void onSuccess(OrderRealPayBean paramT) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalParams.ORDER_BEAN_KEY, Utils.orderRealPayBean2OrderBean(paramT));
                    bundle.putBoolean("isSuccess", true);
                    if ("1".equals(paramT.getData().getRepay_type())) {
                        gotoActivity(mContext, PaySucNextMonthActivity.class, bundle);
                    } else if ("2".equals(paramT.getData().getReal_pay())) {
                        gotoActivity(mContext, PaySuccessActivity.class, bundle);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }
}
