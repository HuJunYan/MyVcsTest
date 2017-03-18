package com.maibai.user.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.adapter.ShareTypeAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.OrderBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.model.ShareTypeBean;
import com.maibai.user.net.base.BaseUiListener;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.utils.HomePageUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.utils.ShareUtils;
import com.maibai.user.view.ImageTextView;
import com.maibai.user.view.TitleBar;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class PaySucNextMonthActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_consumption_details;
    private RelativeLayout layout_discount_money, layout_real_pay;
    private ImageView iv_checkout_success;
    private TextView tv_checkout_success;
    private TextView tv_next_pay_succ;
    private TextView tv_discount_money;
    private TextView tv_real_pay;
    private ImageTextView itv_total_amount;
    private ImageTextView itv_repay_date;
    private ImageTextView itv_next_month_amount;
    private Button bt_confirm;
    private Button bt_share;
    private TitleBar tb_title;
    private PopupWindow popupwindow;
    private TextView tv_message_title;
    private ImageTextView itv_shoufu;
    private List<ShareTypeBean> shareList = new ArrayList<ShareTypeBean>();
    private ShareTypeAdapter shareTypeAdapter;
    private Bundle mBundle;
    final String APP_ID = "1105551302";
    Tencent mTencent;
    BaseUiListener listener;
    private OrderBean mOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        listener = new BaseUiListener(mContext);
        init();
    }

    private void init() {
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mOrderBean = (OrderBean) mBundle.getSerializable(GlobalParams.ORDER_BEAN_KEY);
        } else {
            showResult(false);
            return;
        }
//        if (mBundle.getBoolean("isSuccess")) {
//            setData();
//        }
        showResult(mBundle.getBoolean("isSuccess", false));
    }

    private void setData() {
        try {
            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION, null);
            String amount = mOrderBean.getData().getAmount();
            if ("".equals(amount) || null == amount) {
                amount = "0";
            }
            itv_total_amount.setRightText("￥" + Double.valueOf(amount) / 100);
            String repayData = mOrderBean.getData().getRepay_date();
            if ("".equals(repayData) || null == repayData) {
                repayData = "10";
            }
            String repayDate = "下月" + repayData + "日之前";
            itv_repay_date.setRightText(repayDate);
            String repayTotal = mOrderBean.getData().getRepay_total();
            if ("".equals(repayTotal) || null == repayTotal) {
                repayTotal = "0";
            }
            itv_next_month_amount.setRightText("￥" + Double.valueOf(repayTotal) / 100);
            String discount = mOrderBean.getData().getDiscount();
            if ("".equals(discount) || null == discount) {
                discount = "0";
            }
            if (Double.valueOf(discount) > 0.0) {
                layout_discount_money.setVisibility(View.VISIBLE);
                tv_discount_money.setText("¥" + (Double.valueOf(discount)) / 100);
            } else {
                layout_discount_money.setVisibility(View.GONE);
            }
            String realPay = mOrderBean.getData().getReal_pay();
            if ("".equals(realPay) || null == realPay) {
                realPay = "0";
            }
            if ((Double.valueOf(realPay)) > 0.0) {
                layout_real_pay.setVisibility(View.VISIBLE);
                tv_real_pay.setText("¥" + (Double.valueOf(realPay)) / 100);
            } else {
                layout_real_pay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void setTextSpecialColor(int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(tv_next_pay_succ.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_next_pay_succ.setText(builder);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_pay_suc_next_month;
    }

    @Override
    protected void findViews() {
        ll_consumption_details = (LinearLayout) findViewById(R.id.ll_consumption_details);
        iv_checkout_success = (ImageView) findViewById(R.id.iv_checkout_success);
        tv_checkout_success = (TextView) findViewById(R.id.tv_checkout_success);
        tv_real_pay = (TextView) findViewById(R.id.tv_real_pay);
        tv_discount_money = (TextView) findViewById(R.id.tv_discount_money);
        itv_total_amount = (ImageTextView) findViewById(R.id.itv_total_amount);
        itv_next_month_amount = (ImageTextView) findViewById(R.id.itv_next_month_amount);
        itv_repay_date = (ImageTextView) findViewById(R.id.itv_repay_date);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        bt_share = (Button) findViewById(R.id.bt_share);
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        tv_next_pay_succ = (TextView) findViewById(R.id.tv_next_pay_succ);
        layout_discount_money = (RelativeLayout) findViewById(R.id.layout_discount_money);
        layout_real_pay = (RelativeLayout) findViewById(R.id.layout_real_pay);
        tv_message_title = (TextView) findViewById(R.id.tv_message_title);
        itv_shoufu = (ImageTextView) findViewById(R.id.itv_shoufu);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        bt_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.bt_confirm:
                    ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
                    break;
                case R.id.bt_share:
                    shareView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void shareView() {
        shareList = HomePageUtil.getShareList();
        shareTypeAdapter = new ShareTypeAdapter(mContext, shareList);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.share, null);
        GridView share_gv = (GridView) view.findViewById(R.id.share_gv);
        share_gv.setAdapter(shareTypeAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        share_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShareUtils shareUtils = new ShareUtils(mContext);
                if (position == 0) {
                    shareUtils.shareWeixin(0);
                } else if (position == 1) {
                    shareUtils.shareWeixin(1);
                } else if (position == 2) {
                    shareUtils.shareToQQ(mTencent, listener);
                } else if (position == 3) {
                    shareUtils.shareQZone(mTencent, listener);
                }
                dialog.dismiss();
            }
        });
    }

    private void showResult(boolean isSuccess) {
        String tipStr = "";
        if (isSuccess) {
            try {
                new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION, null);
                bt_confirm.setText("付款成功");
                tb_title.setTitle("付款成功");
                tv_checkout_success.setText("付款成功");
                ll_consumption_details.setVisibility(View.VISIBLE);
                String amount = mOrderBean.getData().getAmount();
                if ("".equals(amount) || null == amount) {
                    amount = "0";
                }
                itv_total_amount.setRightText("￥" + Double.valueOf(amount) / 100);
                String repayData = mOrderBean.getData().getRepay_date();
                if ("".equals(repayData) || null == repayData) {
                    repayData = "10";
                }
                String repayDate = "下月" + repayData + "之前";
                itv_repay_date.setRightText(repayDate);
                String repayTotal = mOrderBean.getData().getRepay_total();
                if ("".equals(repayTotal) || null == repayTotal) {
                    repayTotal = "0";
                }
                itv_next_month_amount.setRightText("￥" + Double.valueOf(repayTotal) / 100);
                String discount = mOrderBean.getData().getDiscount();
                if ("".equals(discount) || null == discount) {
                    discount = "0";
                }
                if (Double.valueOf(discount) > 0.0) {
                    layout_discount_money.setVisibility(View.VISIBLE);
                    tv_discount_money.setText("¥" + (Double.valueOf(discount)) / 100);
                } else {
                    layout_discount_money.setVisibility(View.GONE);
                }
                String realPay = mOrderBean.getData().getReal_pay();
                if ("".equals(realPay) || null == realPay) {
                    realPay = "0";
                }
                if ((Double.valueOf(realPay)) > 0.0) {
                    layout_real_pay.setVisibility(View.VISIBLE);
                    tv_real_pay.setText("¥" + (Double.valueOf(realPay)) / 100);
                } else {
                    layout_real_pay.setVisibility(View.GONE);
                }
                tipStr = "尊敬的用户，您本次消费使用了" + Double.valueOf(amount) / 100 + "元信用额度，请在下月" + repayData + "之前准时还款，以免影响您的信用，祝您生活愉快";
                tv_next_pay_succ.setText(tipStr);
                if (Double.valueOf(mOrderBean.getData().getDown_payment()) > 0.0) {
                    itv_shoufu.setVisibility(View.VISIBLE);
                    itv_shoufu.setRightText("¥" + Double.valueOf(mOrderBean.getData().getDown_payment()) / 100);
                } else {
                    itv_shoufu.setVisibility(View.GONE);
                }
                setTextSpecialColor(tipStr.indexOf("请在下月") + 2, tipStr.indexOf("之前准时") + 2);
                iv_checkout_success.setImageResource(R.mipmap.pay_success);
            } catch (Exception e) {
                e.printStackTrace();
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
            }
        } else {
            tv_message_title.setText("失败原因：");
            ResponseBean responseBean = GsonUtil.json2bean(mBundle.getString("error_result"), ResponseBean.class);
            tv_next_pay_succ.setText("失败原因： " + responseBean.getMsg());
            bt_confirm.setText("返回首页");
            bt_share.setVisibility(View.GONE);
            tb_title.setTitle("付款结果");
            tv_checkout_success.setText("审核未通过");
            ll_consumption_details.setVisibility(View.GONE);
            iv_checkout_success.setImageResource(R.mipmap.pay_loser);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isOnKeyDown() {
        return true;
    }

}
