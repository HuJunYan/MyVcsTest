package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.AmountAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.model.SaleListBean;
import com.maibei.merchants.model.SaleListItemBean;
import com.maibei.merchants.net.api.GetSaleList;
import com.maibei.merchants.net.api.Logout;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.NumAnimView;
import com.maibei.merchants.view.PullToRefreshView;
import com.maibei.merchants.view.ScrollListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private AmountAdapter amountAdapter;
    private ScrollListView sl_user_order;
    private Button bt_tixian;
    private ImageButton iv_qrcode;
    private List<SaleListItemBean> mItemBeans = new ArrayList<SaleListItemBean>();
    private TextView tv_amount;
    private ImageView iv_menu;
    private PopupWindow popupwindow;

    private RelativeLayout rl_history;
    private RelativeLayout rl_bank_info;
    private RelativeLayout rl_about;
    private RelativeLayout rl_quit;
    private RelativeLayout rl_my_commi;
    private long exitTime = 0;
    public static MainActivity instance = null;
    PullToRefreshView pull_refresh;
    private SaleListBean saleListBean;
    private ScrollView sv_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        JPushInterface.setAlias(mContext, UserUtil.getIdNum(mContext), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
        amountAdapter = new AmountAdapter(this, mItemBeans);
        sl_user_order.setAdapter(amountAdapter);
        requestHttpSaleList(0, 5, true);
        registerBroadcastReceiver();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void findViews() {
        sl_user_order = (ScrollListView) findViewById(R.id.sl_user_order);
        bt_tixian = (Button) findViewById(R.id.bt_tixian);
        iv_qrcode = (ImageButton) findViewById(R.id.iv_qrcode);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        pull_refresh = (PullToRefreshView) findViewById(R.id.pull_refresh);
        sv_container = (ScrollView) findViewById(R.id.sv_container);
    }

    @Override
    protected void setListensers() {
        bt_tixian.setOnClickListener(this);
        iv_qrcode.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        pull_refresh.setOnFooterRefreshListener(this);
        pull_refresh.setOnHeaderRefreshListener(this);

    }

    private void init() {
        double amountNum = 0;//我的金额
        String balance = saleListBean.getBalance();
        if (balance != null && !"".equals(balance)) {
            amountNum = Double.valueOf(balance).doubleValue() / 100;//转换成（元）
            if (amountNum > 0) {
                if (amountNum > 100) {
                    NumAnimView.startAnim(tv_amount, amountNum, 1000);
                } else {
                    NumAnimView.startAnim(tv_amount, amountNum);
                }
            } else {
                tv_amount.setText(amountNum + "");
            }
            UserUtil.setBalance(mContext, balance);
        } else {
            tv_amount.setText("暂无金额");
        }
        amountAdapter.notifyDataSetChanged();
        sv_container.scrollTo(0, 0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tixian:
                gotoActivity(MainActivity.this, ExpressiveActivity.class, null);
                break;
            case R.id.iv_qrcode:
                gotoActivity(MainActivity.this, QrCodingActivity.class, null);
                break;
            case R.id.iv_menu:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(view, 0, 5);
                }
                break;
            case R.id.rl_history:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                if (null == saleListBean) {
                    ToastUtil.showToast(mContext, "相关数据不存在");
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("type","orderWidthdrawal");
                gotoActivity(mContext, WithdrawHistoryActivity.class, bundle);
                break;
            case R.id.rl_bank_info:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                gotoActivity(mContext, BankCardInfoActivity.class, null);
                break;
            case R.id.rl_about:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                gotoActivity(MainActivity.this, AboutActivity.class, null);
                break;
            case R.id.rl_quit:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                quitSign();
                break;
            case R.id.rl_my_commi:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }
                gotoActivity(mContext,MyCommiActivity.class,null);
                break;
            default:
                break;
        }
    }

    private void logout() {
        Logout logout = new Logout(this);
        JSONObject mJson = new JSONObject();
        try {
            mJson.put("merchant_id", UserUtil.getMerchantId(mContext));
            logout.logout(mJson, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    gotoActivity(MainActivity.this, NavigationActivity.class, null);
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    private void quitSign() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("将退出登录");
        builder.setCancelable(false);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.getInstance(mContext).putBoolean("isLogin", false);
                logout();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void requestHttpSaleList(int offset, int lenth, final boolean ifClear) {
        GetSaleList getSaleList = new GetSaleList(MainActivity.this);
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            mJSONObject.put("offset", offset + "");
            mJSONObject.put("length", lenth + "");
            getSaleList.getSaleList(mJSONObject, null, false, new BaseNetCallBack<SaleListBean>() {
                @Override
                public void onSuccess(SaleListBean paramT) {
                    saleListBean = paramT;
                    if (null == paramT) {
                        return;
                    }
                    if (ifClear) {
                        mItemBeans.clear();
                    }
                    mItemBeans.addAll(paramT.getData());
                    init();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(this, R.string.click_one_exit);
            exitTime = System.currentTimeMillis();
        } else {
            backActivity();
        }
    }

    public void initmPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popuwindow_menu,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setOutsideTouchable(true);
        popupwindow.setFocusable(true);

        popupwindow.setAnimationStyle(R.style.fadeAnimationFade);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        rl_history = (RelativeLayout) customView.findViewById(R.id.rl_history);
        rl_bank_info = (RelativeLayout) customView.findViewById(R.id.rl_bank_info);
        rl_about = (RelativeLayout) customView.findViewById(R.id.rl_about);
        rl_quit = (RelativeLayout) customView.findViewById(R.id.rl_quit);
        rl_my_commi = (RelativeLayout) customView.findViewById(R.id.rl_my_commi);
        rl_my_commi.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_bank_info.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_quit.setOnClickListener(this);

        if(null!=saleListBean){
            rl_my_commi.setVisibility("1".equals(saleListBean.getCommision_status())?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        requestHttpSaleList(mItemBeans.size(), 10, false);
        pull_refresh.postDelayed(new Runnable() {

            @Override
            public void run() {

                pull_refresh.onFooterRefreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        requestHttpSaleList(0, 5, true);
        pull_refresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                pull_refresh.onHeaderRefreshComplete();
            }
        }, 1000);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SIGN_OUT_ACTIVITY");
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }
}
