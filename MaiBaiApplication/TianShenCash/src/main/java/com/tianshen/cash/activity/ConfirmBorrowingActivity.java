package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.OtherLoanBean;
import com.tianshen.cash.net.api.GetOtherLoanService;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.view.MinMaxSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 确认借款界面
 */
public class ConfirmBorrowingActivity extends BaseActivity {

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
    private int mCurrentLoanDaysIndex;//当前选择产品在mLoanDays的角标

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
        mTvHomeMoney.setText(0+"");
    }

    @Override
    protected void setListensers() {

        mMinMaxSb.setOnMinMaxSeekBarChangeListener(new MyOnMinMaxSeekBarChangeListener());
    }

    @OnClick({R.id.min_max_sb, R.id.check_box, R.id.tv_home_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.min_max_sb:
                break;
            case R.id.check_box:
                break;
            case R.id.tv_home_apply:
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

        OtherLoanBean.Data data = mOtherLoanBean.getData();
        List<CashSubItemBean> cash_data = data.getCash_data();
        for (int i = 0; i < cash_data.size(); i++) {
            CashSubItemBean cashSubItemBean = cash_data.get(i);
            String withdrawalAmount = cashSubItemBean.getWithdrawal_amount();
            try {
                String withdrawalAmountY = MoneyUtils.changeF2Y(withdrawalAmount);
                int withdrawalAmountInt = Integer.valueOf(withdrawalAmountY); //申请的金额也就是滑动当前位置的金额
                if (progress == withdrawalAmountInt) {
                    mCurrentOrderMoney = withdrawalAmount;
                    mTvHomeMoney.setText(mCurrentOrderMoney);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //初始化数据
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
                    mTvHomeMinSb.setText(mOtherLoanBean.getData().getMin_cash());
                    mTvHomeMaxSb.setText(mOtherLoanBean.getData().getMax_cash());
                    mTvBorrowTime.setText(mOtherLoanBean.getData().getRepay_times());
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
}
