package com.tianshen.cash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.MyViewPagerAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplication;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.fragment.MyFragment;
import com.tianshen.cash.fragment.WithdrawalsFragment;
import com.tianshen.cash.model.JpushAddBorrowTermBean;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RequestPermissionUtil;
import com.tianshen.cash.utils.WithdrawalsApplyResultUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RadioButton  rb_my, rb_withdrawals;
    private RadioGroup  rg_control_button;
    private MyFragment mMyFragment;
    private WithdrawalsFragment mWithdrawalsFragment;
    private ViewPager vp_main;
    private ArrayList<Fragment> mFragmentList;
    private MyViewPagerAdapter mViewPagerAdapter;
    private LoginBordcast mLoginBordcast;
    private LocationUtil mLocationUtil;

    private RelativeLayout rl_main_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestPermissionUtil requestPermissionUtil=new RequestPermissionUtil(mContext);
        requestPermissionUtil.requestCameraContactsLocationPermission();
        mLocationUtil = LocationUtil.getInstance(MainActivity.this);
        regist();
        InitView();
        isShowVerifyView();
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
        rg_control_button = (RadioGroup) findViewById(R.id.rg_control_button);
        rb_withdrawals = (RadioButton) findViewById(R.id.rb_withdrawals);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        rl_main_root = (RelativeLayout) findViewById(R.id.rl_main_root);
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

        if (mWithdrawalsFragment == null) {
            mWithdrawalsFragment = new WithdrawalsFragment();
        }
        if (mMyFragment == null) {
            mMyFragment = new MyFragment();
        }

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<Fragment>();// 初始化数据
//            mFragmentList.add(mZeroYuanBuyFragment);
            mFragmentList.add(mWithdrawalsFragment);
            mFragmentList.add(mMyFragment);
        }
    }

    private void setViewPager() {
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
            vp_main.setAdapter(mViewPagerAdapter);
            vp_main.setOffscreenPageLimit(2);
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


    /**
     * 根据服务器返回来的标志显示"正在审核中的视图"
     */
    private void isShowVerifyView() {
        MyApplication myApplication = (MyApplication) mContext.getApplicationContext();
        String on_verify = myApplication.getOn_verify();

        if ("1".equals(on_verify)) {//1为审核中的视图
            Log.d("abc","显示正在审核视图");
            rg_control_button.setVisibility(View.GONE);
            vp_main.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.rl_main_root, new MyFragment());
            ft.commit();
        }
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

}
