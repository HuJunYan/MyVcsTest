package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.OtherLoanBean;
import com.tianshen.cash.net.api.GetOtherLoanService;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.SpannableUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.MinMaxSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 确认借款界面
 */
public class ConfirmBorrowingActivity extends BaseActivity {
    public static final int TXTYPE = 3;

    @BindView(R.id.tv_home_money)
    TextView mTvHomeMoney;
    @BindView(R.id.tv_home_money_key)
    TextView mTvHomeMoneyKey;
    @BindView(R.id.tv_home_max_sb_thumb)
    TextView mTvHomeMaxSbThumb;
    @BindView(R.id.rl_home_max_sb_thumb)
    RelativeLayout mRlHomeMaxSbThumb;
    @BindView(R.id.min_max_sb)
    MinMaxSeekBar mMinMaxSb;
    @BindView(R.id.tv_home_min_sb)
    TextView mTvHomeMinSb;
    @BindView(R.id.tv_home_max_sb)
    TextView mTvHomeMaxSb;
    @BindView(R.id.tv_borrow_key_no)
    TextView mTvBorrowKeyNo;
    @BindView(R.id.tv_borrow_money_key_no)
    TextView mTvBorrowMoneyKeyNo;
    @BindView(R.id.check_box)
    CheckBox mCheckBox;
    @BindView(R.id.tv_confirm_protocol)
    TextView mTvConfirmProtocol;
    @BindView(R.id.tv_home_apply)
    TextView mTvHomeApply;
    @BindView(R.id.tv_borrow_time)
    TextView mTvBorrowTime;
    @BindView(R.id.tv_borrow_blank_card)
    TextView mTvBorrowBlankCard;
    private OtherLoanBean mOtherLoanBean;
    private String mCurrentOrderMoney;
    //用户借款金额
    private String mBorrowMoney;
    private ArrayList<CharacterStyle> ssList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_borrowing;
    }

    @Override
    protected void findViews() {
        initOtherLoanBeanData();
    }

    @Override
    protected void setListensers() {

        mMinMaxSb.setOnMinMaxSeekBarChangeListener(new MyOnMinMaxSeekBarChangeListener());
    }

    @OnClick({R.id.min_max_sb, R.id.tv_home_apply, R.id.tv_confirm_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.min_max_sb:
                break;

            case R.id.tv_confirm_protocol:
                initGotoWebData();
                break;
            case R.id.tv_home_apply:
                //掌众确认借款
                if (!mCheckBox.isChecked()) {
                    ToastUtil.showToast(this, "您必须同意协议才可以借款");
                    return;
                }
                onClickApply();
                break;
            default:

                break;
        }
    }

    /**
     * seekbar滑动监听
     */
    private class MyOnMinMaxSeekBarChangeListener implements MinMaxSeekBar.OnMinMaxSeekBarChangeListener {

        @Override
        public void onProgressChanged(float progress) {
            refreshLoanNumUI((int) progress);
        }

        @Override
        public void onStartTrackingTouch(float progress) {
        }

        @Override
        public void onStopTrackingTouch(float progress) {
            refreshLoanNumUI((int) progress);
        }
    }

    /**
     * 刷新当前借款的金额，和综合费用
     */
    private void refreshLoanNumUI(int progress) {

        if (mOtherLoanBean == null) {
            return;
        }

        //设置借款金额
        String currentMoney = MoneyUtils.addTwoPoint(progress);
        mTvHomeMoney.setText(currentMoney);
        mBorrowMoney = MoneyUtils.changeY2F(currentMoney);


        OtherLoanBean.Data data = mOtherLoanBean.getData();
        List<CashSubItemBean> cash_data = data.getCash_data();
        for (int i = 0; i < cash_data.size(); i++) {
            CashSubItemBean cashSubItemBean = cash_data.get(i);
            String withdrawalAmount = cashSubItemBean.getWithdrawal_amount();
            try {
                String withdrawalAmountY = MoneyUtils.changeF2Y(withdrawalAmount);
                //申请的金额也就是滑动当前位置的金额
                int withdrawalAmountInt = Integer.valueOf(withdrawalAmountY);
                if (progress == withdrawalAmountInt) {
                    mCurrentOrderMoney = withdrawalAmount;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 刷新seekBar
     */
    private void refreshBubbleSeekBarUI() {

        try {
            //最小值
            String min_cash = mOtherLoanBean.getData().getMin_cash();
            String min_cashY = MoneyUtils.changeF2Y(min_cash);
            int min_cashInt = Integer.valueOf(min_cashY);

            //最大值
            String max_cash = mOtherLoanBean.getData().getMax_cash();
            String max_cashY = MoneyUtils.changeF2Y(max_cash);
            int max_cashInt = Integer.valueOf(max_cashY);

            //默认值 为最大值
            String def_cash = mOtherLoanBean.getData().getMax_cash();
            String def_cashY = MoneyUtils.changeF2Y(def_cash);
            final int def_cashInt = Integer.valueOf(def_cashY);
            mBorrowMoney = mOtherLoanBean.getData().getMax_cash();


                    //刻度值
            String unit = mOtherLoanBean.getData().getUnit();
            String unitY = MoneyUtils.changeF2Y(unit);
            int unitInt = Integer.valueOf(unitY);
            if (max_cashInt <= min_cashInt) {
                mMinMaxSb.setEnabled(false);
            } else {
                mMinMaxSb.setEnabled(true);
            }
            mTvHomeMinSb.setText(min_cashInt + "元");
            mTvHomeMaxSb.setText(max_cashInt + "元");



            mMinMaxSb.setMaxMin(max_cashInt, min_cashInt, unitInt);
            mMinMaxSb.setCurrentProgress(def_cashInt);

//            //重置金额偏移量
//            minMaxSb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    minMaxSb.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    moveSeekBarThumbMoney(def_cashInt);
//                }
//            });


        } catch (Exception e) {
            ToastUtil.showToast(mContext, "数据错误!");
            e.printStackTrace();
        }

    }

    /**
     * 设置spannable点击规则
     */
    private void initGotoWebData() {
        if (ssList == null) {
            ssList = new ArrayList<>();
        }
        ssList.clear();
        ssList.add(webSpan);
        ssList.add(webSpan2);
        ssList.add(webSpan3);
        String text = getResources().getString(R.string.confirm_protocol_all_text2);
        SpannableUtils.setWebSpannableString(mTvConfirmProtocol, text, "《", "》", ssList, getResources().getColor(R.color.global_txt_orange));
    }

    private ClickableSpan webSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(2);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    };
    private ClickableSpan webSpan2 = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(1);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    };

    private ClickableSpan webSpan3 = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            gotoWebActivity(3);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    };

    /**
     * 初始化数据
     */
    private void initOtherLoanBeanData() {

        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            LogUtil.d("abc", "其他借款方式用户id:" + userId);
            final GetOtherLoanService selWithdrawals = new GetOtherLoanService(mContext);
            selWithdrawals.getData(jsonObject, new BaseNetCallBack<OtherLoanBean>() {


                @Override
                public void onSuccess(OtherLoanBean paramT) {
                    mOtherLoanBean = paramT;
                    String borrowDay = mOtherLoanBean.getData().getRepay_times();

                    if (!TextUtils.isEmpty(borrowDay)){
                        mTvBorrowTime.setText(borrowDay+"天");
                    }
                    refreshBubbleSeekBarUI();
                    mTvBorrowBlankCard.setText(mOtherLoanBean.getData().getBank_card_info_str());
                    refreshLoanNumUI(mMinMaxSb.getMinMaxSeekBarCurrentProgress());

                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 点击了确认
     */
    private void onClickApply() {
        if (null == mOtherLoanBean) {
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
            //用户申请金额
            String consume_amount = mBorrowMoney;
            final String repay_id = mOtherLoanBean.getData().getRepay_id();

            String black_box = new GetTelephoneUtils(mContext).getBlackBox();
            String type = "1"; //掌众下单
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, customer_id);
            jsonObject.put("type", type);
            jsonObject.put("consume_amount", consume_amount);
            jsonObject.put("location", location);
            jsonObject.put("province", province);
            jsonObject.put("city", city);
            jsonObject.put("country", country);
            jsonObject.put("address", address);
            jsonObject.put("black_box", black_box);
            jsonObject.put("push_id", jpush_id);
            jsonObject.put("repay_id", repay_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetOtherLoanService order = new GetOtherLoanService(mContext);
        order.getData(jsonObject, new BaseNetCallBack<OtherLoanBean>() {
            @Override
            public void onSuccess(OtherLoanBean paramT) {
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
     * 回到首页
     */
    private void gotoMainActivity() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        gotoActivity(mContext, MainActivity.class, null);
        finish();
    }


    /**
     * 跳转到WebActivity
     *
     * @param type 1 居间协议 2 借款协议
     */
    private void gotoWebActivity(int type) {
        if (mOtherLoanBean == null) {
            return;
        }
        String userPayProtocolURL = NetConstantValue.getUserPayServerURL();
        String repay_id = mOtherLoanBean.getData().getRepay_id();
        //借款本金
        String consume_amount =mBorrowMoney;
        StringBuilder sb = new StringBuilder();
        sb.append(userPayProtocolURL);
        sb.append("?" + GlobalParams.USER_CUSTOMER_ID + "=" + TianShenUserUtil.getUserId(this));
        sb.append("&repay_id=" + repay_id);
        sb.append("&consume_amount=" + consume_amount);
        sb.append("&agreement_type=" + type);

        Bundle bundle = new Bundle();
        if (TXTYPE==type){
            bundle.putString(GlobalParams.WEB_URL_KEY, mOtherLoanBean.getData().getBank_credit_investigation_url());
        }else {
            bundle.putString(GlobalParams.WEB_URL_KEY, sb.toString());
        }
        gotoActivity(mContext, WebActivity.class, bundle);


    }


}
