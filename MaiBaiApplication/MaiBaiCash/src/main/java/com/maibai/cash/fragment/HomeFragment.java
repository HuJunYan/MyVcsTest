package com.maibai.cash.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseFragment;
import com.maibai.cash.model.CashSubItemBean;
import com.maibai.cash.model.SelWithdrawalsBean;
import com.maibai.cash.model.WithdrawalsItemBean;
import com.maibai.cash.net.api.SelWithdrawals;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.view.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.tv_home_tianshen_card_name)
    TextView tvHomeTianshenCardName;
    @BindView(R.id.tv_home_user_limit_value)
    TextView tvHomeUserLimitValue;
    @BindView(R.id.ll_home_tianshen_card_limit)
    LinearLayout llHomeTianshenCardLimit;
    @BindView(R.id.tv_home_tianshen_card_num)
    TextView tvHomeTianshenCardNum;
    @BindView(R.id.tv_home_tianshen_card_renzheng)
    TextView tvHomeTianshenCardRenzheng;
    @BindView(R.id.bubble_seekbar_home)
    BubbleSeekBar bubbleSeekbarHome;
    @BindView(R.id.tv_loan_num_key)
    TextView tvLoanNumKey;
    @BindView(R.id.tv_loan_num_value)
    TextView tvLoanNumValue;
    @BindView(R.id.iv_procedures_home)
    ImageView ivProceduresHome;
    @BindView(R.id.tv_procedures_value)
    TextView tvProceduresValue;
    @BindView(R.id.tv_procedures_key)
    TextView tvProceduresKey;
    @BindView(R.id.tv_loan_day_key)
    TextView tvLoanDayKey;
    @BindView(R.id.tv_loan_day_value)
    TextView tvLoanDayValue;
    @BindView(R.id.iv_loan_day_arrow)
    ImageView ivLoanDayArrow;
    @BindView(R.id.ll_home_top)
    LinearLayout llHomeTop;
    @BindView(R.id.rl_loan_day)
    RelativeLayout rlLoanDay; //借款天数外层的RelativeLayout


    private SelWithdrawalsBean mSelWithdrawalsBean;


    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void findViews(View rootView) {

    }

    @Override
    protected void setListensers() {
        initBubbleSeekBar();
        rlLoanDay.setOnClickListener(this);
        bubbleSeekbarHome.setOnProgressChangedListener(new MyOnProgressChangedListenerAdapter());
    }

    @Override
    protected void initVariable() {
        initSelWithdrawalsData();
    }

    private void initSelWithdrawalsData() {


        try {
            JSONObject jsonObject = new JSONObject();

            boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
            if (mIsLogin) {
                jsonObject.put("init", "0");
                jsonObject.put("customer_id", UserUtil.getId(mContext));
            } else {
                jsonObject.put("init", "1");
            }
            final SelWithdrawals selWithdrawals = new SelWithdrawals(mContext);
            selWithdrawals.selWithdrawals(jsonObject, null, true, new BaseNetCallBack<SelWithdrawalsBean>() {
                @Override
                public void onSuccess(SelWithdrawalsBean selWithdrawalsBean) {
                    mSelWithdrawalsBean = selWithdrawalsBean;
                    refreshUI();
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
     * 刷新UI
     */
    private void refreshUI() {
        refreshCardUI();
        refreshLoanDayUI();
        refreshBubbleSeekBarUI();
    }

    /**
     * 刷新天神卡
     */
    private void refreshCardUI() {

        String max_cash = mSelWithdrawalsBean.getMax_cash();
        if (TextUtils.isEmpty(max_cash)) {
            max_cash = "0";
        }
        int max_cashInt = Integer.valueOf(max_cash) / 100;
        tvHomeUserLimitValue.setText(max_cashInt + "");
    }

    /**
     * 刷新seekBar
     */
    private void refreshBubbleSeekBarUI() {

        //总长度
        String max_cash = mSelWithdrawalsBean.getMax_cash();
        if (TextUtils.isEmpty(max_cash)) {
            max_cash = "0";
        }
        int max_cashInt = Integer.valueOf(max_cash) / 100;

        //每一个刻度的长度
        String unit = mSelWithdrawalsBean.getUnit();
        if (TextUtils.isEmpty(unit)) {
            unit = "0";
        }
        int unitInt = Integer.valueOf(unit) / 100;

        //得到seekBar分成几份
        int sectionCount = (max_cashInt - unitInt) / unitInt;
        bubbleSeekbarHome.getConfigBuilder()
                .min(unitInt)
                .max(max_cashInt)
                .sectionCount(sectionCount)
//                .trackColor(ContextCompat.getColor(getContext(), R.color.color_gray))
//                .secondTrackColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .thumbColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .showSectionText()
//                .sectionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
//                .sectionTextSize(18)
//                .showThumbText()
//                .thumbTextColor(ContextCompat.getColor(getContext(), R.color.color_red))
//                .thumbTextSize(18)
//                .bubbleColor(ContextCompat.getColor(getContext(), R.color.color_green))
//                .bubbleTextSize(18)
                .showSectionMark()
                .seekBySection()
                .autoAdjustSectionMark()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BOTTOM_SIDES)
                .build();
    }

    /**
     * 刷新借款期限UI
     */
    private void refreshLoanDayUI() {

        List<WithdrawalsItemBean> withdrawalsItemBeens = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeens.get(0);
        String repayTimes = withdrawalsItemBean.getRepay_times();
        String repay_unit = withdrawalsItemBean.getRepay_unit();
        if ("1".equals(repay_unit)) { //判断是月还是天  1-月，2-天
            tvLoanDayValue.setText(repayTimes + " 月");
        } else {
            tvLoanDayValue.setText(repayTimes + " 天");
        }

    }

    /**
     * 刷新当前借款的金额，和手续费用
     */
    private void refreshLoanNumUI(int progress) {

        //设置借款金额
        String s = String.format(Locale.CHINA, "%d", progress);
        tvLoanNumValue.setText(s + " 元");

        List<WithdrawalsItemBean> withdrawalsItemBeen = mSelWithdrawalsBean.getData();
        for (int i = 0; i < withdrawalsItemBeen.size(); i++) { //遍历所有产品
            WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeen.get(i);
            String mCurrentLoanDayValue = (String) tvLoanDayValue.getText();
            String repayTimes = withdrawalsItemBean.getRepay_times();
            if (mCurrentLoanDayValue.contains(repayTimes)) { //根据当前选择期限筛选出当前选择的产品
                List<CashSubItemBean> cash_data = withdrawalsItemBean.getCash_data();
                for (int j = 0; j < cash_data.size(); j++) {
                    CashSubItemBean cashSubItemBean = cash_data.get(j);
                    String withdrawalAmount = cashSubItemBean.getWithdrawal_amount();
                    int withdrawalAmountInt = Integer.valueOf(withdrawalAmount) / 100; //申请的金额也就是滑动当前位置的金额
                    if (progress == withdrawalAmountInt) {
                        String transfer_amount = cashSubItemBean.getTransfer_amount();
                        int transfer_amountInt = Integer.valueOf(transfer_amount) / 100; //到账金额
                        int procedures = withdrawalAmountInt - transfer_amountInt;//手续金额
                        //设置手续金额
                        tvProceduresValue.setText(procedures + " 元");
                    }
                }
            }
        }
    }

    /**
     * 初始化滑动条
     */
    private void initBubbleSeekBar() {
        bubbleSeekbarHome.getConfigBuilder()
                .min(500)
                .max(3000)
//                .progress(20)
                .sectionCount(5)
//                .trackColor(ContextCompat.getColor(getContext(), R.color.color_gray))
//                .secondTrackColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .thumbColor(ContextCompat.getColor(getContext(), R.color.color_blue))
//                .showSectionText()
//                .sectionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
//                .sectionTextSize(18)
//                .showThumbText()
//                .thumbTextColor(ContextCompat.getColor(getContext(), R.color.color_red))
//                .thumbTextSize(18)
//                .bubbleColor(ContextCompat.getColor(getContext(), R.color.color_green))
//                .bubbleTextSize(18)
                .showSectionMark()
                .seekBySection()
                .autoAdjustSectionMark()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BOTTOM_SIDES)
                .build();
    }

    /**
     * 选择借款天数
     */
    private void selectLoanDay() {
        ToastUtil.showToast(mContext, "点击了借款期限");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_loan_day:
                selectLoanDay();
                break;
        }
    }


    /**
     * 滑动条监听
     */
    private class MyOnProgressChangedListenerAdapter extends BubbleSeekBar.OnProgressChangedListenerAdapter {

        private int mProgress = 0;

        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            if (mProgress != progress) {
                refreshLoanNumUI(progress);
                mProgress = progress;
            }
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            if (mProgress != progress) {
                refreshLoanNumUI(progress);
                mProgress = progress;
            }
        }
    }
}
