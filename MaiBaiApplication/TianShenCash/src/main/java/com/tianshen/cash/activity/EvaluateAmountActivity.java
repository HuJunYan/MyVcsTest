package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.RiskPreEvaluateFinishEvent;
import com.tianshen.cash.event.RiskPreFinishEvent;
import com.tianshen.cash.model.CashAmountBean;
import com.tianshen.cash.net.api.GetCashAmountService;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.StatusBarUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class EvaluateAmountActivity extends BaseActivity {

    @BindView(R.id.iv_evaluate_top_bg)
    ImageView iv_evaluate_top_bg;
    @BindView(R.id.srl_evaluate)
    SmartRefreshLayout srl_evaluate;
    @BindView(R.id.ll_evaluate_tips)
    View ll_evaluate_tips;
    @BindView(R.id.tv_tips2)
    TextView tv_tips2;
    private CashAmountBean mCashAmountBean;
    private boolean isNeedRefresh; //是否需要刷新接口
    private boolean isOnResumeExe;//是否在前台


    @Override
    protected int setContentView() {
        return R.layout.activity_evaluate_amount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetViewSize();
        initData();
    }

    private void initData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GetCashAmountService getCashAmountService = new GetCashAmountService(mContext);
        getCashAmountService.getData(jsonObject, new BaseNetCallBack<CashAmountBean>() {
            @Override
            public void onSuccess(CashAmountBean bean) {
                mCashAmountBean = bean;
                srl_evaluate.finishRefresh();
                refreshDataAndCheckIsNeedGotoMain();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                srl_evaluate.finishRefresh();

            }
        });
    }

    private void refreshDataAndCheckIsNeedGotoMain() {
        if (mCashAmountBean == null || mCashAmountBean.getData() == null) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        CashAmountBean.Data data = mCashAmountBean.getData();
        String cash_amount_status = data.getCash_amount_status();
        if ("1".equals(cash_amount_status)) {//测评完毕
            String is_payway = data.getIs_payway();
            EventBus.getDefault().post(new RiskPreEvaluateFinishEvent(is_payway));
            gotoActivity(mContext, MainActivity.class, null);
            if ("0".equals(is_payway)) {
                finish();
            } else if ("1".equals(is_payway)) {
                gotoActivity(mContext, ConfirmBorrowingActivity.class, null);
                finish();
            }
        }
    }

    @OnClick(R.id.tv_evaluate_joke)
    public void clickJoke(View v) {

        if (mCashAmountBean == null || mCashAmountBean.getData() == null || TextUtils.isEmpty(mCashAmountBean.getData().getJoke_url())) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, mCashAmountBean.getData().getJoke_url());
        bundle.putString(GlobalParams.WEB_FROM, GlobalParams.FROM_JOKE);
        gotoActivity(mContext, WebActivity.class, bundle);
    }

    private void resetViewSize() {

        tv_tips2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_tips2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //拿到屏幕高度
                int heightPixels = getResources().getDisplayMetrics().heightPixels;
                //计算父控件需要的大小
                int top = ll_evaluate_tips.getTop();
                int bottom = ll_evaluate_tips.getBottom();
                RelativeLayout.LayoutParams layoutParamsll = (RelativeLayout.LayoutParams) ll_evaluate_tips.getLayoutParams();
                //设置布局规则
                layoutParamsll.addRule(RelativeLayout.BELOW, R.id.iv_evaluate_top_bg);
                ll_evaluate_tips.setLayoutParams(layoutParamsll);
                int height = Math.abs(top - bottom);
                //重新设置背景图大小
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_evaluate_top_bg.getLayoutParams();
                layoutParams.height = heightPixels - height;
                iv_evaluate_top_bg.setLayoutParams(layoutParams);
                //设置提示可见
                ll_evaluate_tips.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void findViews() {
        StatusBarUtil.setStatusViewTranslucent(this);
        iv_evaluate_top_bg.setFitsSystemWindows(true);
        srl_evaluate.setRefreshHeader(new ClassicsHeader(this));
        srl_evaluate.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
    }

    @Override
    protected void setListensers() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onRiskPreFinishEvent(RiskPreFinishEvent event) {
        if (isOnResumeExe) { //如果在当前页面 刷新
            initData();
        } else {//不在当前页面 记录需要刷新状态
            isNeedRefresh = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedRefresh) { //需要刷新 刷新
            initData();
        }
        isOnResumeExe = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnResumeExe = false;
    }
}
