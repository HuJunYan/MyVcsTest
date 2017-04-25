package com.tianshen.cash.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.litesuits.orm.LiteOrm;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCenterActivity;
import com.tianshen.cash.activity.ConfirmMoneyActivity;
import com.tianshen.cash.activity.ConfirmRepayActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.adapter.AuthCenterAdapter;
import com.tianshen.cash.adapter.OrderStatusAdapter;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.ApplyEvent;
import com.tianshen.cash.event.AuthCenterBackEvent;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.event.QuotaEvent;
import com.tianshen.cash.event.RepayEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.manager.DBManager;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.SelWithdrawalsBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.model.StatisticsRollDataBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.model.UserConfig;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.net.api.GetUserConfig;
import com.tianshen.cash.net.api.GetVerifySmsForConfirmLoan;
import com.tianshen.cash.net.api.IKnow;
import com.tianshen.cash.net.api.SelWithdrawals;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.api.SubmitVerifyCode;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.UploadToServerUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.view.MinMaxSeekBar;
import com.tianshen.user.idcardlibrary.util.Util;

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

    @BindView(R.id.ll_not_order)
    LinearLayout ll_not_order;

    @BindView(R.id.ll_order)
    LinearLayout ll_order;

    @BindView(R.id.ll_repay)
    LinearLayout ll_repay;

    @BindView(R.id.xrecyclerview_order_status)
    XRecyclerView xrecyclerview_order_status;

    @BindView(R.id.tv_home_confirm_money)
    TextView tv_home_confirm_money;

    @BindView(R.id.tv_goto_repay)
    TextView tv_goto_repay;

    @BindView(R.id.ll_repay_normal)
    LinearLayout ll_repay_normal;

    @BindView(R.id.ll_repay_danger)
    LinearLayout ll_repay_danger;

    @BindView(R.id.tv_repay_month)
    TextView tv_repay_month;

    @BindView(R.id.tv_repay_day)
    TextView tv_repay_day;

    @BindView(R.id.tv_repay_money)
    TextView tv_repay_money;

    @BindView(R.id.tv_repay_overdue_day)
    TextView tv_repay_overdue_day;

    @BindView(R.id.tv_repay_overdue_sum_money)
    TextView tv_repay_overdue_sum_money;

    @BindView(R.id.tv_repay_principal_money)
    TextView tv_repay_principal_money;

    @BindView(R.id.tv_repay_danger_money_key)
    TextView tv_repay_danger_money_key;

    @BindView(R.id.tv_home_tianshen_card_can_pay)
    TextView tv_home_tianshen_card_can_pay;

    private OrderStatusAdapter mOrderStatusAdapter;

    private static final String STATUS_NEW = "0"; //新用户，没有下过单

    private SelWithdrawalsBean mSelWithdrawalsBean; //数据root bean
    private ArrayList<String> mLoanDays;//借款期限(点击借款期限的Dialog用到) 每一个期限就代表一个产品
    private int mCurrentLoanDaysIndex;//当前选择产品在mLoanDays的角标

    private UserConfig mUserConfig; //用户的配置信息
    private List<StatisticsRollDataBean> mStatisticsRollDataBeans; //滚动条得到的数据
    private List<String> mStatisticsRollDatas; //滚动条真实显示的数据

    private int mCurrentIndex;//当前滚动条的位置

    private Dialog mVerifyCodeDialog;

    private String mCurrentOrderMoney;
    private String mVerifyCodeType;

    private boolean mQuotaFlag; //只有页面正在显示的时候收到此消息才强制跳转到下单页面

    private UploadToServerUtil mUploadToServerUtil;

    private static final int MSG_SHOW_TEXT = 1;
    private static final int SHOW_TEXT_TIME = 5 * 1000;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SHOW_TEXT:
                    refreshStaticsRollUI();
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
        tv_goto_repay.setOnClickListener(this);
        ivProceduresHome.setOnClickListener(this);
        tv_home_confirm_money.setOnClickListener(this);
        minMaxSb.setOnMinMaxSeekBarChangeListener(new MyOnMinMaxSeekBarChangeListener());
        initTextSwitcher();
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
                Bundle cardBundle = new Bundle();
                cardBundle.putBoolean(GlobalParams.IS_FROM_CARD_KEY, true);
                gotoActivity(mContext, AuthCenterActivity.class, cardBundle);
                break;
            case R.id.rl_loan_day: //点击了期限选择
                showSelectLoanDayDialog();
                break;
            case R.id.tv_home_apply: //点击了立即申请
                onClickApply();
                break;
            case R.id.tv_goto_repay: //点击了立即还款
                gotoActivity(mContext, ConfirmRepayActivity.class, null);
                break;
            case R.id.tv_home_confirm_money: //点击了刷新&我知道了按钮
                onClickIKnow();
                break;
            case R.id.iv_procedures_home: //点击了借款提示
                ToastUtil.showToast(mContext, "该手续费率及期限仅供参考，最终借款费率会根据借款金额、周期及提交资料审核后的综合信用评估结果来收取。");
                break;
        }
    }


    @Override
    protected void initVariable() {
        boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
        if (mIsLogin) {
            initUserConfig();
        } else {
            initSelWithdrawalsData();
        }
        initStaticsRoll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onResume() {
        super.onResume();
        mQuotaFlag = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mQuotaFlag = false;
    }


    /**
     * 点击了立即申请按钮
     */
    private void onClickApply() {

        if (mUserConfig == null) {
            return;
        }
        String cur_credit_step = mUserConfig.getData().getCur_credit_step();
        String total_credit_step = mUserConfig.getData().getTotal_credit_step();


        //存储借款订单信息(到订单确认页面会用到)
        List<WithdrawalsItemBean> withdrawalsItemBeens = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeens.get(mCurrentLoanDaysIndex);
        String id = withdrawalsItemBean.getId();//选择产品的ID
        User user = TianShenUserUtil.getUser(mContext);
        user.setRepay_id(id);
        user.setConsume_amount(mCurrentOrderMoney);
        TianShenUserUtil.saveUser(mContext, user);

        if (cur_credit_step.equals(total_credit_step)) {//认证完毕直接跳转到确认借款页面
            uploadContacts();
        } else {//没有认证完毕跳转到认证中心页面
            Bundle applyBundle = new Bundle();
            applyBundle.putBoolean(GlobalParams.IS_FROM_CARD_KEY, false);
            gotoActivity(mContext, AuthCenterActivity.class, applyBundle);
        }

    }

    /**
     * 上传联系人&通信录
     */
    private void uploadContacts() {
        mUploadToServerUtil = new UploadToServerUtil(mContext);
        mUploadToServerUtil.setCallBack(new MyUploadCallBack());
        mUploadToServerUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
    }

    private class MyUploadCallBack implements UploadToServerUtil.UploadCallBack {

        @Override
        public void uploadSuccessCallBack(int type) {
            //上传通讯录、通话记录、短信等的回调
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人成功
                    gotoActivity(mContext, ConfirmMoneyActivity.class, null);
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录成功
//                    mUploadToServerUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信成功
                    break;
            }
        }

        @Override
        public void uploadFailCallBack(int type) {
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人失败
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录失败
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信失败
                    break;
            }
        }
    }


    private void initSelWithdrawalsData() {

        try {
            JSONObject jsonObject = new JSONObject();
            boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
            if (mIsLogin) {
                jsonObject.put("init", "0");
                String userId = TianShenUserUtil.getUserId(mContext);
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
                    showNoPayUI();
                    refreshLoanDayUI();
                    refreshBubbleSeekBarUI();
                    refreshLoanNumUI(minMaxSb.getMinMaxSeekBarCurrentProgress());
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
                refreshStaticsRollUI();
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
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            GetUserConfig getUserConfig = new GetUserConfig(mContext);
            getUserConfig.userConfig(jsonObject, null, true, new BaseNetCallBack<UserConfig>() {
                @Override
                public void onSuccess(UserConfig paramT) {
                    mUserConfig = paramT;
                    checkUserConfig();
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
     * 检查用户当前的状态，显示不同的UI
     */
    private void checkUserConfig() {
        if (mUserConfig == null) {
            //TODO 展示解析错误的UI
            return;
        }

        //刷新天神卡
        refreshCardUI();


        //存储用户相关
        String is_payway = mUserConfig.getData().getIs_payway();
        User user = TianShenUserUtil.getUser(mContext);
        String is_show_service_telephone = mUserConfig.getData().getIs_show_service_telephone();
        if (TextUtils.isEmpty(is_show_service_telephone)) {
            is_show_service_telephone = "0";
        }
        user.setIs_show_service_telephone(is_show_service_telephone);
        user.setIs_payway(is_payway);
        TianShenUserUtil.saveUser(mContext, user);



        boolean clickedHomeGetMoneyButton = user.isClickedHomeGetMoneyButton();
        boolean clickedHomeRePayMoneyButton = user.isClickedHomeRePayMoneyButton();

        String status = mUserConfig.getData().getStatus();


        //只是模拟掌众
//        is_payway = "1";
//        status = "2";

        tv_home_tianshen_card_can_pay.setVisibility(View.GONE);

        switch (status) {
            case "0"://0:新用户，没有提交过订单；
                ArrayList<UserConfig.Data.Consume> consume_status_list = mUserConfig.getData().getConsume_status_list();
                if (consume_status_list == null || consume_status_list.size() == 0) {
                    initSelWithdrawalsData();//显示用户没有下单的UI
                    tv_home_tianshen_card_can_pay.setVisibility(View.VISIBLE);
                } else {
                    showConsumeStatusUI();//显示用户订单轨迹的UI (已经还款需要点"我知道了,点击我知道了需要调用一个接口")
                    tv_home_confirm_money.setText("我知道了");
                    tv_home_confirm_money.setVisibility(View.VISIBLE);
                }
                break;
            case "1"://1:订单待审核；
                showConsumeStatusUI();//显示用户订单轨迹的UI
                showRefreshButtonUI();//显示刷新按钮
                break;
            case "2"://2:审核通过；
                showConsumeStatusUI();//显示用户订单轨迹的UI
                tv_home_confirm_money.setVisibility(View.GONE);
                if ("1".equals(is_payway)) { //如果是掌众需要弹出dialog
                    showVerifyCodeDialog();
                }
                break;
            case "3"://3:放款成功（钱已经到银行卡）；
                if (clickedHomeGetMoneyButton) {//判断用户没有点过按钮，没有点过按钮吧按钮显示出来
                    showRepayUI();
                } else {
                    showConsumeStatusUI();
                    tv_home_confirm_money.setText("我知道了");
                    tv_home_confirm_money.setVisibility(View.VISIBLE);
                }
                break;
            case "4"://4:审核失败
                showConsumeStatusUI();
                tv_home_confirm_money.setText("我知道了");
                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
            case "5"://5:放款失败
                showConsumeStatusUI();
                tv_home_confirm_money.setText("我知道了");
                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
            case "6"://6放款中
                showConsumeStatusUI();
                tv_home_confirm_money.setVisibility(View.GONE);
                break;
            case "7"://7 已还款
//                showConsumeStatusUI();
//                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
            case "8": //8已经提交还款（还款金额还没到账
                showConsumeStatusUI();
                tv_home_confirm_money.setVisibility(View.GONE);
                break;
            case "9": //9决策失败
                showConsumeStatusUI();
                tv_home_confirm_money.setText("我知道了");
                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 点击了我知道按钮
     */
    private void onClickIKnow() {
        String status = mUserConfig.getData().getStatus();
        switch (status) {
            case "0": //还款成功
                repayIknow();
                break;
            case "1": //等待审核
                initUserConfig();
                break;
            case "3": //借款成功
                showFriendlyTipsDialog();
                break;
            case "4": //审核失败
                initUserConfig();
                break;
            case "5": //放款失败
                initUserConfig();
                break;
            case "9": //决策失败
                initUserConfig();
                break;
        }
    }

    /**
     * 显示数据错误UI
     */
    private void showRefreshButtonUI() {
        tv_home_confirm_money.setText("刷新");
        tv_home_confirm_money.setVisibility(View.VISIBLE);
    }

    /**
     * 显示数据错误UI
     */
    private void showErrorUI() {
        ll_not_order.setVisibility(View.GONE);
        ll_order.setVisibility(View.GONE);
        ll_repay.setVisibility(View.GONE);
    }

    /**
     * 显示未登录或者未下订单UI
     */
    private void showNoPayUI() {
        ll_not_order.setVisibility(View.VISIBLE);
        ll_order.setVisibility(View.GONE);
        ll_repay.setVisibility(View.GONE);
    }

    /**
     * 显示订单轨迹UI
     */
    private void showConsumeStatusUI() {
        ll_not_order.setVisibility(View.GONE);
        ll_repay.setVisibility(View.GONE);
        ll_order.setVisibility(View.VISIBLE);
        initXRecyclerview();
    }

    /**
     * 显示还款UI
     */
    private void showRepayUI() {
        ll_not_order.setVisibility(View.GONE);
        ll_order.setVisibility(View.GONE);
        ll_repay.setVisibility(View.VISIBLE);

        String overdueDays = mUserConfig.getData().getOverdue_days();
        if (TextUtils.isEmpty(overdueDays)) {
            overdueDays = "0";
        }
        int overdueDaysInt = Integer.parseInt(overdueDays);

        if (overdueDaysInt > 0) {//显示逾期的UI
            ll_repay_normal.setVisibility(View.GONE);
            ll_repay_danger.setVisibility(View.VISIBLE);
            tv_repay_overdue_day.setText(overdueDays);
            try {

                String consume_amount = mUserConfig.getData().getConsume_amount(); //本金
                String overdue_amount = mUserConfig.getData().getOverdue_amount();//罚金

                String consume_amountY = MoneyUtils.changeF2Y(consume_amount);
                String overdue_amountY = MoneyUtils.changeF2Y(overdue_amount);

                tv_repay_principal_money.setText(consume_amountY); //本金
                tv_repay_danger_money_key.setText(overdue_amountY);//罚金
                String sum = MoneyUtils.add(consume_amountY, overdue_amountY);
                tv_repay_overdue_sum_money.setText(sum);//一共
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {//显示正常还钱的UI
            ll_repay_normal.setVisibility(View.VISIBLE);
            ll_repay_danger.setVisibility(View.GONE);

            String repayment_time_month = mUserConfig.getData().getRepayment_time_month();
            String repayment_time_day = mUserConfig.getData().getRepayment_time_day();
            String consumeAmount = mUserConfig.getData().getConsume_amount();
            tv_repay_month.setText(repayment_time_month);
            tv_repay_day.setText(repayment_time_day);
            try {
                String consumeAmountY = MoneyUtils.changeF2Y(consumeAmount);
                tv_repay_money.setText(consumeAmountY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initXRecyclerview() {
        if (mOrderStatusAdapter == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            xrecyclerview_order_status.setLayoutManager(layoutManager);
            xrecyclerview_order_status.setLoadingMoreEnabled(false);
            xrecyclerview_order_status.setPullRefreshEnabled(false);
            mOrderStatusAdapter = new OrderStatusAdapter(mContext, mUserConfig);
            xrecyclerview_order_status.setAdapter(mOrderStatusAdapter);
        } else {
            mOrderStatusAdapter.setData(mUserConfig);
            mOrderStatusAdapter.notifyDataSetChanged();
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
     * 刷新天神卡
     */
    private void refreshCardUI() {
        //设置天神卡号
        String virtual_card_num = mUserConfig.getData().getVirtual_card_num();
        String cardNum = StringUtil.getTianShenCardNum(virtual_card_num);
        tvHomeTianshenCardNum.setText(cardNum);
        String cur_credit_step = mUserConfig.getData().getCur_credit_step();
        String total_credit_step = mUserConfig.getData().getTotal_credit_step();
        //设置认证步骤
        tvHomeTianshenCardRenzheng.setText("认证" + cur_credit_step + "/" + total_credit_step);
        //设置信用额度
        try {
            String max_cash = mUserConfig.getData().getMax_cash();
            String max_cashY = MoneyUtils.changeF2Y(max_cash);
            tvHomeUserLimitValue.setText(max_cashY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置天神卡UI
     */
    private void resetCardUI() {
        String cardNum = StringUtil.getTianShenCardNum("8888888888888888");
        tvHomeTianshenCardNum.setText(cardNum);
        tvHomeTianshenCardRenzheng.setText("认证" + 0 + "/" + 5);
        tvHomeUserLimitValue.setText("0");
        tv_home_tianshen_card_can_pay.setVisibility(View.GONE);
    }

    /**
     * 刷新滚动条(恭喜 xxx借到了xxx元钱)
     */
    private void refreshStaticsRollUI() {
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

        try {
            //最小值
            String min_cash = mSelWithdrawalsBean.getMin_cash();
            String min_cashY = MoneyUtils.changeF2Y(min_cash);
            int min_cashInt = Integer.valueOf(min_cashY);

            //最大值
            String max_cash = mSelWithdrawalsBean.getMax_cash();
            String max_cashY = MoneyUtils.changeF2Y(max_cash);
            int max_cashInt = Integer.valueOf(max_cashY);

            //默认值
            String def_cash = mSelWithdrawalsBean.getDef_cash();
            String def_cashY = MoneyUtils.changeF2Y(def_cash);
            int def_cashInt = Integer.valueOf(def_cashY);

            //刻度值
            String unit = mSelWithdrawalsBean.getUnit();
            String unitY = MoneyUtils.changeF2Y(unit);
            int unitInt = Integer.valueOf(unitY);
            if (max_cashInt <= min_cashInt) {
                minMaxSb.setEnabled(false);
            } else {
                minMaxSb.setEnabled(true);
            }
            tvHomeMinSb.setText(min_cashInt + "元");
            tvHomeMaxSb.setText(max_cashInt + "元");
            minMaxSb.setMaxMin(max_cashInt, min_cashInt, unitInt);
            minMaxSb.setCurrentProgress(def_cashInt);
        } catch (Exception e) {
            showErrorUI();
            ToastUtil.showToast(mContext, "数据错误!");
            e.printStackTrace();
        }

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
            try {
                String withdrawalAmountY = MoneyUtils.changeF2Y(withdrawalAmount);
                int withdrawalAmountInt = Integer.valueOf(withdrawalAmountY); //申请的金额也就是滑动当前位置的金额
                if (progress == withdrawalAmountInt) {
                    mCurrentOrderMoney = withdrawalAmount;
                    String transfer_amount = cashSubItemBean.getTransfer_amount();
                    String transfer_amountY = MoneyUtils.changeF2Y(transfer_amount);
                    int transfer_amountInt = Integer.valueOf(transfer_amountY); //到账金额
                    int procedures = withdrawalAmountInt - transfer_amountInt;//手续金额
                    String proceduresStr = String.valueOf(procedures);
                    String proceduresStrF = MoneyUtils.div(proceduresStr, "2", 0);
                    //设置手续金额
                    tvProceduresValue.setText(proceduresStrF + " 元");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 显示借款天数Dialog
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
                        int currentProgress = minMaxSb.getMinMaxSeekBarCurrentProgress();
                        refreshLoanNumUI(currentProgress);
                    }
                }).show();
    }

    /**
     * 点击还款我知道了
     */
    private void repayIknow() {

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        TianShenUserUtil.getUserRepayId(mContext);
        String consume_id = mUserConfig.getData().getConsume_id();
        try {
            jsonObject.put("customer_id", userId);
            jsonObject.put("consume_id", consume_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final IKnow iKnow = new IKnow(mContext);
        iKnow.know(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                if (paramT.getCode() == 0) {
                    TianShenUserUtil.clearMoneyStatus(mContext);
                    initUserConfig();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    /**
     * 显示友情提示Dialog
     */
    private void showFriendlyTipsDialog() {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_friendly_tips, null, false);
        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        TextView tv_dialog_friendly_tips = (TextView) view.findViewById(R.id.tv_dialog_friendly_tips);
        tv_dialog_friendly_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = TianShenUserUtil.getUser(mContext);
                user.setClickedHomeGetMoneyButton(true);
                TianShenUserUtil.saveUser(mContext, user);
                showRepayUI();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 显示第三方产品(掌众)输入验证码Dialog
     */
    private void showVerifyCodeDialog() {

        mVerifyCodeType = "0";

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_verify_code, null, false);
        mVerifyCodeDialog = new Dialog(mContext, R.style.MyDialog);
        mVerifyCodeDialog.setContentView(view);
        mVerifyCodeDialog.setCanceledOnTouchOutside(false);
        mVerifyCodeDialog.setCancelable(false);
        TextView tv_dialog_get_verify_code = (TextView) view.findViewById(R.id.tv_dialog_get_verify_code);
        final EditText et_dialog_verify_code = (EditText) view.findViewById(R.id.et_dialog_verify_code);
        TextView tv_dialog_get_money = (TextView) view.findViewById(R.id.tv_dialog_get_money);

        tv_dialog_get_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVerifySmsForConfirmLoanUrl(mVerifyCodeType);
                mVerifyCodeType = "1";
            }
        });

        tv_dialog_get_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verufy_code = et_dialog_verify_code.getText().toString().trim();
                if (TextUtils.isEmpty(verufy_code)) {
                    ToastUtil.showToast(mContext, "输入验证码");
                    return;
                }
                initThirdPartySubmitVerifyCode(verufy_code);
            }
        });
        mVerifyCodeDialog.show();
    }


    /**
     * 得到第三方验证码
     */
    private void initVerifySmsForConfirmLoanUrl(String type) {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            String consume_id = mUserConfig.getData().getConsume_id();
            jsonObject.put("customer_id", userId);
            jsonObject.put("consume_id", consume_id);
            jsonObject.put("type", type);
            GetVerifySmsForConfirmLoan getVerifySmsForConfirmLoan = new GetVerifySmsForConfirmLoan(mContext);
            getVerifySmsForConfirmLoan.getVerifySmsForConfirmLoan(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
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
     * 发送第三方验证码
     */
    private void initThirdPartySubmitVerifyCode(String verufy_code) {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            String consume_id = mUserConfig.getData().getConsume_id();
            jsonObject.put("customer_id", userId);
            jsonObject.put("consume_id", consume_id);
            jsonObject.put("verufy_code", verufy_code);
            jsonObject.put("type", "1");
            SubmitVerifyCode submitVerifyCode = new SubmitVerifyCode(mContext);
            submitVerifyCode.verifyCode(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    mVerifyCodeDialog.dismiss();
                    gotoActivity(mContext, ConfirmMoneyActivity.class, null);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    mVerifyCodeDialog.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
        LogUtil.d("abc", "收到了登录成功消息--刷新UI");
        initUserConfig();
    }

    /**
     * 收到了退出成功的消息
     */
    @Subscribe
    public void onLoginoutSuccess(LogoutSuccessEvent event) {
        LogUtil.d("abc", "收到了退出登录成功消息--刷新UI");
        initSelWithdrawalsData();
        initStaticsRoll();
        resetCardUI();
    }


    /**
     * 从认证中心返回主页
     */
    @Subscribe
    public void onAuthCenterBack(AuthCenterBackEvent event) {
        LogUtil.d("abc", "从认证中心页面返回--刷新UI");
        initUserConfig();
    }

    /**
     * 收到了在确认借款页面发送的消息
     */
    @Subscribe
    public void onApply(ApplyEvent event) {
        LogUtil.d("abc", "收到了确认借款页面的消息---刷新UI");
        initUserConfig();
    }

    /**
     * 收到了还款的消息
     */
    @Subscribe
    public void onRepay(RepayEvent event) {
        LogUtil.d("abc", "收到了还款的消息--刷新UI");
        initUserConfig();
    }


    /**
     * 收到了服务器下单超时的消息
     */
    @Subscribe
    public void onTimeOut(TimeOutEvent event) {
        LogUtil.d("abc", "收到了超时的消息--刷新UI");
        initUserConfig();
    }

    /**
     * 收到了极光推送过来的消息
     */
    @Subscribe
    public void onUserConfigChangedEvent(UserConfigChangedEvent event) {
        LogUtil.d("abc", "收到了极光推送的消息--刷新UI");
        initUserConfig();
    }

    /**
     * 收到了强行跳转到下单页面
     */
    @Subscribe
    public void onQuotaEvent(QuotaEvent event) {
        LogUtil.d("abc", "收到了强行跳转到下单页面");
        if (mQuotaFlag) {
            gotoActivity(mContext, ConfirmMoneyActivity.class, null);
        }
    }



}
