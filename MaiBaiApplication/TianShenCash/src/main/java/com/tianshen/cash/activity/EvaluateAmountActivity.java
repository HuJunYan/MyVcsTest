package com.tianshen.cash.activity;

import android.os.Handler;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.utils.StatusBarUtil;

import butterknife.BindView;

public class EvaluateAmountActivity extends BaseActivity {

    @BindView(R.id.iv_evaluate_top_bg)
    ImageView iv_evaluate_top_bg;
    @BindView(R.id.srl_evaluate)
    SmartRefreshLayout srl_evaluate;

    @Override
    protected int setContentView() {
        return R.layout.activity_evaluate_amount;
    }

    @Override
    protected void findViews() {
        StatusBarUtil.setStatusViewTranslucent(this);
        iv_evaluate_top_bg.setFitsSystemWindows(true);
        srl_evaluate.setRefreshHeader(new ClassicsHeader(this));
        srl_evaluate.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl_evaluate.finishRefresh();

                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void setListensers() {

    }

}
