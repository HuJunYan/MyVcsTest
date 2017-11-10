package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.view.MinMaxSeekBar;

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
//            refreshLoanNumUI((int) progress);
        }

        @Override
        public void onStartTrackingTouch(float progress) {
        }

        @Override
        public void onStopTrackingTouch(float progress) {
//            refreshLoanNumUI((int) progress);
        }
    }

    /**
     * 刷新当前借款的金额，和综合费用
     */
    /*private void refreshLoanNumUI(int progress) {

        if (mSelWithdrawalsBean == null) {
            return;
        }

        //设置借款金额
        String currentMoney = MoneyUtils.addTwoPoint(progress);
        mTvHomeMoney.setText(currentMoney);
        tv_home_get_money.setText(currentMoney);

        List<WithdrawalsItemBean> withdrawalsItemBeen = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeen.get(mCurrentLoanDaysIndex);
        List<CashSubItemBean> cash_data = withdrawalsItemBean.getCash_data();
        for (int i = 0; i < cash_data.size(); i++) {
            CashSubItemBean cashSubItemBean = cash_data.get(i);
            String withdrawalAmount = cashSubItemBean.getWithdrawal_amount();
            try {
                String withdrawalAmountY = MoneyUtils.changeF2Y(withdrawalAmount);
                int withdrawalAmountInt = Integer.valueOf(withdrawalAmountY); //申请的金额也就是滑动当前位置的金额
                if (progress == withdrawalAmountInt) {
                    mCurrentOrderMoney = withdrawalAmount;
                    break;
//                    String transfer_amount = cashSubItemBean.getTransfer_amount();
//                    String transfer_amountY = MoneyUtils.changeF2Y(transfer_amount);
//                    int transfer_amountInt = Integer.valueOf(transfer_amountY); //到账金额
//                    int procedures = withdrawalAmountInt - transfer_amountInt;//手续金额
//                    String proceduresStr = String.valueOf(procedures);
//                    String proceduresStrF = MoneyUtils.div(proceduresStr, "2", 0);
                    //设置手续金额
//                    tvProceduresValue.setText(proceduresStrF + " 元");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/
}
