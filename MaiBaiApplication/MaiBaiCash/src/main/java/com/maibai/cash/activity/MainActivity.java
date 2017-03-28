package com.maibai.cash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.maibai.cash.R;
import com.maibai.cash.adapter.MyViewPagerAdapter;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.base.MyApplication;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.event.RegisterAndLoginSuccessEvent;
import com.maibai.cash.fragment.HomeFragment;
import com.maibai.cash.fragment.MyFragment;
import com.maibai.cash.fragment.WithdrawalsFragment;
import com.maibai.cash.model.JpushAddBorrowTermBean;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LocationUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.RequestPermissionUtil;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.WithdrawalsApplyResultUtil;
import com.maibai.cash.view.MyViewPager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RadioButton  rb_my, rb_withdrawals;
    private MyFragment mMyFragment;
//    private WithdrawalsFragment mWithdrawalsFragment;
    private HomeFragment mHomeFragment;
    private MyViewPager vp_main;
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
//        rb_zero_yuan_buy = (RadioButton) findViewById(R.id.rb_zero_yuan_buy);
        rb_withdrawals = (RadioButton) findViewById(R.id.rb_withdrawals);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        vp_main = (MyViewPager) findViewById(R.id.vp_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.rb_zero_yuan_buy:
                vp_main.setCurrentItem(0);
                break;*/
            case R.id.rb_withdrawals:
                vp_main.setCurrentItem(0);
                break;
            case R.id.rb_my:
                vp_main.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
    public int getTopFragmentIndex(){
        /*if(rb_zero_yuan_buy.isChecked()){
            return 0;
        }else*/ if(rb_withdrawals.isChecked()){
                return 0;
        }else if(rb_my.isChecked()){
            return 1;
        }
        return -1;
    }
    @Override
    protected void setListensers() {
//        rb_zero_yuan_buy.setOnClickListener(this);
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
            /*case 0:
                rb_zero_yuan_buy.setChecked(true);
                break;*/
            case 0:
                rb_withdrawals.setChecked(true);
                break;
            case 1:
                rb_my.setChecked(true);
                break;
            default:
                break;
        }
    }


    private void addFragment() {

//        if (mWithdrawalsFragment == null) {
//            mWithdrawalsFragment = new WithdrawalsFragment();
//        }

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }

        if (mMyFragment == null) {
            mMyFragment = new MyFragment();
        }

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<Fragment>();// 初始化数据
//            mFragmentList.add(mZeroYuanBuyFragment);
//            mFragmentList.add(mWithdrawalsFragment);
            mFragmentList.add(mHomeFragment);
            mFragmentList.add(mMyFragment);
        }
    }

    private void setViewPager() {
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
            vp_main.setAdapter(mViewPagerAdapter);
            vp_main.setOffscreenPageLimit(mFragmentList.size());
        }
    }

    private void InitView() {
        addFragment();
        setViewPager();
        rb_withdrawals.performClick();
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
        intentFilter.addAction(GlobalParams.ADD_BORROW_TERM_KEY_ACTION);
        mLoginBordcast = new LoginBordcast();
        registerReceiver(mLoginBordcast, intentFilter);
    }

    public class LoginBordcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if(null==action){
                action="";
            }
            switch (action){
                case GlobalParams.LOGOUT_ACTION:
                    rb_withdrawals.performClick();
                    break;
                case GlobalParams.ADD_BORROW_TERM_KEY_ACTION:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            (((MyApplication)(mContext.getApplicationContext())).getApplication()).clearTempActivityInBackStack(MainActivity.class);
                            Bundle bundle=intent.getExtras();
                            JpushAddBorrowTermBean object=(JpushAddBorrowTermBean) bundle.getSerializable(GlobalParams.ADD_BORROW_TERM_KEY);
                            new WithdrawalsApplyResultUtil(mContext).showBorrowTerm(object.getMsg_content().getBorrow_time());
                        }
                    },2000);

                    break;
            }

        }
    }

    public void validateCashApplyStatus() {
        try {
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
                    //用户在黑名单时直接跳转至拒绝
                    String alreadyNums=UserUtil.getAlreadyNums(mContext);
                    String hopeNums=UserUtil.getHopeNums(mContext);
                    if(null==alreadyNums||"".equals(alreadyNums)) {
                        alreadyNums = "0";
                    }
                    if(null==hopeNums||"".equals(hopeNums)) {
                        hopeNums = "0";
                    }
                    if(Integer.valueOf(hopeNums)>Integer.valueOf(alreadyNums)) {
                        gotoActivity(mContext,VerifyFailActivity.class,null);
                        return;
                    }
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

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onRegisterAndLoginSuccess(RegisterAndLoginSuccessEvent event) {
        rb_withdrawals.performClick();
    }

}
