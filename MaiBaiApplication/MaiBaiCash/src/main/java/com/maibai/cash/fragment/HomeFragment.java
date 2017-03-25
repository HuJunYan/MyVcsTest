package com.maibai.cash.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litesuits.orm.LiteOrm;
import com.maibai.cash.R;
import com.maibai.cash.base.BaseFragment;
import com.maibai.cash.manager.DBManager;
import com.maibai.cash.model.SelWithdrawalsBean;
import com.maibai.cash.model.User;
import com.maibai.cash.net.api.SelWithdrawals;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.MainUtil;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.view.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        selWithdrawals();
    }

    private void selWithdrawals() {


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
                public void onSuccess(SelWithdrawalsBean paramT) {
                    LogUtil.d("abc", "paramT--->" + paramT.getMax_cash());
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
        ToastUtil.showToast(mContext, "点击了借款天数");
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
