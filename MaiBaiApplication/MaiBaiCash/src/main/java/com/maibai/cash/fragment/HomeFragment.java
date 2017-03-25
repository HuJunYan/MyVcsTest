package com.maibai.cash.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseFragment;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.view.BubbleSeekBar;

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
    RelativeLayout rlLoanDay;

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
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_loan_day:
                selectLoanDay();
                break;
        }
    }

    /**
     * 选择借款天数
     */
    private void selectLoanDay() {
        ToastUtil.showToast(mContext, "点击了借款天数");
    }

    private class MyOnProgressChangedListenerAdapter extends BubbleSeekBar.OnProgressChangedListenerAdapter {
        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            String s = String.format(Locale.CHINA, "%d", progress);
            tvLoanNumValue.setText(s + " 元");
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            String s = String.format(Locale.CHINA, "%d", progress);
            tvLoanNumValue.setText(s + " 元");
        }
    }
}
