package com.tianshen.cash.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.LocationEvent;
import com.tianshen.cash.model.CashAmountBean;
import com.tianshen.cash.net.api.GetCashAmountService;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.StatusBarUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class EvaluateAmountActivity extends BaseActivity {

    @BindView(R.id.iv_evaluate_top_bg)
    ImageView iv_evaluate_top_bg;
    @BindView(R.id.srl_evaluate)
    SmartRefreshLayout srl_evaluate;
    @BindView(R.id.ll_evaluate_tips)
    View ll_evaluate_tips;

    @Override
    protected int setContentView() {
        return R.layout.activity_evaluate_amount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        resetViewSize();


        String location = TianShenUserUtil.getLocation(mContext);
        if (TextUtils.isEmpty(location)) {
            RxPermissions rxPermissions = new RxPermissions(EvaluateAmountActivity.this);
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        LocationUtil mLocationUtil = LocationUtil.getInstance();
                        mLocationUtil.startLocation(EvaluateAmountActivity.this);
                        mLocationUtil.setIsCallBack(true);
                    } else {
                        ToastUtil.showToast(mContext, "请打开定位权限!");
                    }
                }
            });
            ToastUtil.showToast(mContext, "请打开定位权限!");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        String city = TianShenUserUtil.getCity(mContext);
        String country = TianShenUserUtil.getCountry(mContext);
        String address = TianShenUserUtil.getAddress(mContext);
        String province = TianShenUserUtil.getProvince(mContext);
        String black_box = new GetTelephoneUtils(mContext).getBlackBox();
        String jpush_id = TianShenUserUtil.getUserJPushId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);
            jsonObject.put("black_box", black_box);
            jsonObject.put("push_id", jpush_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetCashAmountService getCashAmountService = new GetCashAmountService(mContext);
        getCashAmountService.getData(jsonObject, new BaseNetCallBack<CashAmountBean>() {
            @Override
            public void onSuccess(CashAmountBean bean) {
                srl_evaluate.finishRefresh();
                LogUtil.d("abc", "evaluate Amount Success");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                srl_evaluate.finishRefresh();
                LogUtil.d("abc", "evaluate Amount failure");

            }
        });
    }

    private void resetViewSize() {

        ll_evaluate_tips.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightPixels = getResources().getDisplayMetrics().heightPixels;
                ll_evaluate_tips.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int top = ll_evaluate_tips.getTop();
                int bottom = ll_evaluate_tips.getBottom();
                int height = Math.abs(top - bottom);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_evaluate_top_bg.getLayoutParams();
                layoutParams.height = heightPixels - height;
                iv_evaluate_top_bg.setLayoutParams(layoutParams);
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
        LocationUtil mLocationUtil = LocationUtil.getInstance();
        mLocationUtil.setIsCallBack(false);
    }

    @Subscribe
    public void onAuthCenterBack(LocationEvent event) {
        LogUtil.d("abc", "收到了定位成功的消息");
        initData();
    }
}
