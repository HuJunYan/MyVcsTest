package com.tianshen.cash.fragment;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCenterActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.SelWithdrawalsBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.model.StatisticsRollDataBean;
import com.tianshen.cash.model.UserConfig;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.net.api.GetUserConfig;
import com.tianshen.cash.net.api.SelWithdrawals;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.view.MinMaxSeekBar;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    @BindView(R.id.rl_home_tianshen_card)
    RelativeLayout rlHomeTianshenCard;
    @BindView(R.id.tv_home_apply)
    TextView tvHomeApply;
    @BindView(R.id.min_max_sb)
    MinMaxSeekBar minMaxSb;
    @BindView(R.id.tv_home_min_sb)
    TextView tvHomeMinSb;
    @BindView(R.id.tv_home_max_sb)
    TextView tvHomeMaxSb;
    @BindView(R.id.ts_home_news)
    TextSwitcher tsHomeNews;

    private SelWithdrawalsBean mSelWithdrawalsBean; //数据root bean
    private ArrayList<String> mLoanDays;//借款期限(点击借款期限的Dialog用到) 每一个期限就代表一个产品
    private int mCurrentLoanDaysIndex;//当前选择产品在mLoanDays的角标

    private UserConfig mUserConfig; //用户的配置信息
    private List<StatisticsRollDataBean> mStatisticsRollDataBeans; //滚动条得到的数据
    private List<String> mStatisticsRollDatas; //滚动条真实显示的数据

    private int mCurrentIndex;//当前滚动条的位置

    private static final int MSG_SHOW_TEXT = 1;
    private static final int SHOW_TEXT_TIME = 5 * 1000;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SHOW_TEXT:
                    refreshTextSwitcherUI();
                    break;
            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void findViews(View rootView) {

    }

    @Override
    protected void setListensers() {
        rlHomeTianshenCard.setOnClickListener(this);
        rlLoanDay.setOnClickListener(this);
        tvHomeApply.setOnClickListener(this);

        minMaxSb.setOnMinMaxSeekBarChangeListener(new MyOnMinMaxSeekBarChangeListener());
        initTextSwitcher();
    }

    @Override
    protected void initVariable() {
        boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
        if (mIsLogin) {
            initUserConfig();
        } else {
            initSelWithdrawalsData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initSelWithdrawalsData() {

        try {
            JSONObject jsonObject = new JSONObject();
            boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
            if (mIsLogin) {
                jsonObject.put("init", "0");
                long userId = TianShenUserUtil.getUserId(mContext);
                jsonObject.put("customer_id", userId);
                LogUtil.d("abc", "已经登录--->" + userId);
            } else {
                LogUtil.d("abc", "未登录--->");
                jsonObject.put("init", "1");
            }
            final SelWithdrawals selWithdrawals = new SelWithdrawals(mContext);
            selWithdrawals.selWithdrawals(jsonObject, null, true, new BaseNetCallBack<SelWithdrawalsBean>() {
                @Override
                public void onSuccess(SelWithdrawalsBean selWithdrawalsBean) {
                    mSelWithdrawalsBean = selWithdrawalsBean;
                    parserLoanDayData();
                    initStaticsRoll();
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
     * 得到滚动条数据
     */
    private void initStaticsRoll() {
        final StatisticsRoll statisticsRoll = new StatisticsRoll(mContext);
        statisticsRoll.getStatisticsRoll(new JSONObject(), new BaseNetCallBack<StatisticsRollBean>() {
            @Override
            public void onSuccess(StatisticsRollBean paramT) {
                mStatisticsRollDataBeans = paramT.getData();
                parserStatisticsRollData();
                refreshUI();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

    }

    /**
     * 得到用户配置信息
     */
    private void initUserConfig() {
        try {
            JSONObject jsonObject = new JSONObject();
            long userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            GetUserConfig getUserConfig = new GetUserConfig(mContext);
            getUserConfig.userConfig(jsonObject, null, true, new BaseNetCallBack<UserConfig>() {
                @Override
                public void onSuccess(UserConfig paramT) {
                    mUserConfig = paramT;
                    if (mUserConfig == null) {
                        //TODO 展示解析错误的UI
                        return;
                    }
                    initSelWithdrawalsData();
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
     * 解析出借款期限
     */
    private void parserLoanDayData() {
        mLoanDays = new ArrayList<>();
        List<WithdrawalsItemBean> withdrawalsItemBeen = mSelWithdrawalsBean.getData();
        for (int i = 0; i < withdrawalsItemBeen.size(); i++) {
            WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeen.get(i);
            String repay_unit = withdrawalsItemBean.getRepay_unit();
            String repay_times = withdrawalsItemBean.getRepay_times();
            if ("1".equals(repay_unit)) {
                mLoanDays.add(repay_times + "个月");
            } else {
                mLoanDays.add(repay_times + "天");
            }
        }
    }


    /**
     * 解析出滚动条的数据
     */
    private void parserStatisticsRollData() {
        mStatisticsRollDatas = new ArrayList<>();
        for (int i = 0; i < mStatisticsRollDataBeans.size(); i++) {
            StatisticsRollDataBean statisticsRollDataBean = mStatisticsRollDataBeans.get(i);
            String mobile = statisticsRollDataBean.getMobile();
            String money = statisticsRollDataBean.getMoney();
            if (TextUtils.isEmpty(money)) {
                money = "0";
            }
            int moneyInt = Integer.valueOf(money) / 100;
            mStatisticsRollDatas.add("恭喜 " + mobile + "成功借款" + moneyInt + "元");
        }
    }


    /**
     * 初始化TextSwitcher
     */
    private void initTextSwitcher() {

        tsHomeNews.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(mContext);
                tv.setTextSize(11);
                tv.setTextColor(getResources().getColor(R.color.global_txt_black2));
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                Drawable drawable = getResources().getDrawable(R.drawable.the_trumpet);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);//设置TextView的drawableleft
                tv.setCompoundDrawablePadding(10);//设置图片和text之间的间距
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                tv.setLayoutParams(lp);
                return tv;
            }
        });
    }

    /**
     * 刷新UI
     */
    private void refreshUI() {
        refreshCardUI();
        refreshTextSwitcherUI();
        refreshLoanDayUI();
        refreshBubbleSeekBarUI();
    }

    /**
     * 刷新天神卡
     */
    private void refreshCardUI() {

        if (mSelWithdrawalsBean != null) {
            String max_cash = mSelWithdrawalsBean.getMax_cash();
            if (TextUtils.isEmpty(max_cash)) {
                max_cash = "0";
            }
            int max_cashInt = Integer.valueOf(max_cash) / 100;
            //设置信用额度
            tvHomeUserLimitValue.setText(max_cashInt + "");
        }

        if (mUserConfig != null) {
            //设置天神卡号
            String virtual_card_num = mUserConfig.getData().getVirtual_card_num();
            String cardNum = StringUtil.getTianShenCardNum(virtual_card_num);
            tvHomeTianshenCardNum.setText(cardNum);
            String cur_credit_step = mUserConfig.getData().getCur_credit_step();
            String total_credit_step = mUserConfig.getData().getTotal_credit_step();
            //设置认证步骤
            tvHomeTianshenCardRenzheng.setText("认证" + cur_credit_step + "/" + total_credit_step);
        }

    }

    /**
     * 刷新滚动条(恭喜 xxx借到了xxx元钱)
     */
    private void refreshTextSwitcherUI() {
        if (mCurrentIndex == 0) {
            //第一次不执行动画，立刻显示
            tsHomeNews.setCurrentText(mStatisticsRollDatas.get(mCurrentIndex));
        } else if (mCurrentIndex == mStatisticsRollDatas.size()) {
            //跳回第一次
            mCurrentIndex = 0;
            tsHomeNews.setText(mStatisticsRollDatas.get(mCurrentIndex));
        } else {
            //执行动画
            tsHomeNews.setText(mStatisticsRollDatas.get(mCurrentIndex));
        }
        mCurrentIndex++;
        mHandler.removeMessages(MSG_SHOW_TEXT);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_TEXT, SHOW_TEXT_TIME);
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

        try {
            minMaxSb.setMaxMin(max_cashInt, unitInt, unitInt);
            minMaxSb.setCurrentProgress(unitInt);
        } catch (MinMaxSeekBar.SeekBarStepException e) {
            e.printStackTrace();
        }
        tvHomeMinSb.setText(unitInt + "元");
        tvHomeMaxSb.setText(max_cashInt + "元");
    }

    /**
     * 刷新借款期限UI
     */
    private void refreshLoanDayUI() {

        List<WithdrawalsItemBean> withdrawalsItemBeens = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeens.get(mCurrentLoanDaysIndex);
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

        if (mSelWithdrawalsBean == null) {
            return;
        }

        //设置借款金额
        String s = String.format(Locale.CHINA, "%d", progress);
        tvLoanNumValue.setText(s + " 元");

        List<WithdrawalsItemBean> withdrawalsItemBeen = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeen.get(mCurrentLoanDaysIndex);
        List<CashSubItemBean> cash_data = withdrawalsItemBean.getCash_data();
        for (int i = 0; i < cash_data.size(); i++) {
            CashSubItemBean cashSubItemBean = cash_data.get(i);
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

    /**
     * 选择借款天数
     */
    private void showSelectLoanDayDialog() {
        if (mLoanDays == null) {
            return;
        }

        new MaterialDialog.Builder(mContext)
                .title("选择期限")
                .items(mLoanDays)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCurrentLoanDaysIndex = position;
                        refreshLoanDayUI();
                        refreshBubbleSeekBarUI();
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
        if (!mIsLogin) {//先判断用户有没有登录，如果没有登录就跳转到登录页面进行登录
            gotoActivity(mContext, LoginActivity.class, null);
            return;
        }
        switch (view.getId()) {
            case R.id.rl_home_tianshen_card: //点击了天神卡
                gotoActivity(mContext, AuthCenterActivity.class, null);
                break;
            case R.id.rl_loan_day: //点击了期限选择
                showSelectLoanDayDialog();
                break;
            case R.id.tv_home_apply: //点击了立即申请
                gotoActivity(mContext, AuthCenterActivity.class, null);
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
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onRegisterAndLoginSuccess(LoginSuccessEvent event) {
        initUserConfig();
    }

}
