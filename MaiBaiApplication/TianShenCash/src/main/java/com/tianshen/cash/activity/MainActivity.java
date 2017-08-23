package com.tianshen.cash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.MyViewPagerAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.fragment.HomeFragment;
import com.tianshen.cash.fragment.MeFragment;
import com.tianshen.cash.model.JpushAddBorrowTermBean;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.RequestPermissionUtil;
import com.tianshen.cash.utils.WithdrawalsApplyResultUtil;
import com.tianshen.cash.view.MyViewPager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RadioButton  rb_my, rb_withdrawals;
    private MeFragment mMeFragment;
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
        mLocationUtil = LocationUtil.getInstance();
        regist();
        InitView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mLocationUtil.startLocation(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        rb_withdrawals = (RadioButton) findViewById(R.id.rb_withdrawals);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        vp_main = (MyViewPager) findViewById(R.id.vp_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    public int getTopFragmentIndex() {
        if (rb_withdrawals.isChecked()) {
            return 0;
        } else if (rb_my.isChecked()) {
            return 1;
        }
        return -1;
    }
    @Override
    protected void setListensers() {
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

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }

        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
        }

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<Fragment>();// 初始化数据
            mFragmentList.add(mHomeFragment);
            mFragmentList.add(mMeFragment);
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
                            MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                            Bundle bundle=intent.getExtras();
                            JpushAddBorrowTermBean object=(JpushAddBorrowTermBean) bundle.getSerializable(GlobalParams.ADD_BORROW_TERM_KEY);
                            new WithdrawalsApplyResultUtil(mContext).showBorrowTerm(object.getMsg_content().getBorrow_time());
                        }
                    },2000);

                    break;
            }

        }
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onRegisterAndLoginSuccess(LoginSuccessEvent event) {
        rb_withdrawals.performClick();
    }

}
