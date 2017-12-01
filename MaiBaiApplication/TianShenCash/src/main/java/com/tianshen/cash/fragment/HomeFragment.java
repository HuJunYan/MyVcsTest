package com.tianshen.cash.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCenterActivity;
import com.tianshen.cash.activity.BindBankCardConfirmActivity;
import com.tianshen.cash.activity.ConfirmBaseMoneyActivity;
import com.tianshen.cash.activity.ConfirmDiffRateMoneyActivity;
import com.tianshen.cash.activity.ConfirmMoneyActivity;
import com.tianshen.cash.activity.ConfirmRepayActivity;
import com.tianshen.cash.activity.InviteFriendsActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.activity.MessageCenterActivity;
import com.tianshen.cash.activity.SJDActivity;
import com.tianshen.cash.activity.SuperMarkerActivity;
import com.tianshen.cash.activity.WebActivity;
import com.tianshen.cash.adapter.OrderStatusAdapter;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.ApplyEvent;
import com.tianshen.cash.event.AuthCenterBackEvent;
import com.tianshen.cash.event.FinishCurrentActivityEvent;
import com.tianshen.cash.event.LoginSuccessEvent;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.event.QuotaEvent;
import com.tianshen.cash.event.RepayEvent;
import com.tianshen.cash.event.RepayFailureEvent;
import com.tianshen.cash.event.TimeOutEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.ActivityBean;
import com.tianshen.cash.model.ActivityDataBean;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.ContactsBean;
import com.tianshen.cash.model.IknowBean;
import com.tianshen.cash.model.ManualRefreshBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.SelWithdrawalsBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.model.StatisticsRollDataBean;
import com.tianshen.cash.model.UserConfig;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.model.XiangShangDataBean;
import com.tianshen.cash.net.api.AddSuperMarketCount;
import com.tianshen.cash.net.api.CheckIsXiangShang;
import com.tianshen.cash.net.api.GetActivity;
import com.tianshen.cash.net.api.GetUserConfig;
import com.tianshen.cash.net.api.GetVerifySmsForConfirmLoan;
import com.tianshen.cash.net.api.IKnow;
import com.tianshen.cash.net.api.ManualRefresh;
import com.tianshen.cash.net.api.SJDLoanBack;
import com.tianshen.cash.net.api.SaveContacts;
import com.tianshen.cash.net.api.SelWithdrawals;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.api.SubmitVerifyCode;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.utils.ImageLoader;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.MoneyUtils;
import com.tianshen.cash.utils.PhoneUtils;
import com.tianshen.cash.utils.StatusBarUtil;
import com.tianshen.cash.utils.StringUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.utils.ViewUtil;
import com.tianshen.cash.view.MinMaxSeekBar;
import com.tianshen.cash.view.RepayDetailDialogView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;


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
    @BindView(R.id.tv_loan_day_key)
    TextView tvLoanDayKey;
    @BindView(R.id.tv_loan_day_value)
    TextView tvLoanDayValue;
    @BindView(R.id.ll_home_top)
    LinearLayout llHomeTop;
    @BindView(R.id.ll_loan_day)
    LinearLayout ll_loan_day;
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
    @BindView(R.id.rl_alipay)
    RelativeLayout mRlAlipay;

    @BindView(R.id.xrecyclerview_order_status)
    XRecyclerView xrecyclerview_order_status;

    @BindView(R.id.tv_home_confirm_money)
    TextView tv_home_confirm_money;

    @BindView(R.id.tv_repay_by_bank)
    TextView tv_repay_by_bank;

    @BindView(R.id.tv_repay_by_ali)
    TextView tv_repay_by_ali;

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

    @BindView(R.id.iv_danger_money)
    ImageView iv_danger_money;
    @BindView(R.id.iv_normal_money)
    ImageView iv_normal_money;

    @BindView(R.id.rl_home_max_sb_thumb)
    RelativeLayout rl_home_max_sb_thumb;

    @BindView(R.id.tv_home_max_sb_thumb)
    TextView tv_home_max_sb_thumb;

    @BindView(R.id.iv_home_market)
    ImageView iv_home_market;

    @BindView(R.id.iv_home_msg)
    ImageView iv_home_msg;

    @BindView(R.id.tv_home_money)
    TextView tv_home_money;

    @BindView(R.id.tv_home_get_money)
    TextView tv_home_get_money;

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
    private int mQuotaCount;//工具类会发送2次事件...fuck bug


    private TextView tv_dialog_get_verify_code;
    private int mStartTime = 59;

    private static final int MSG_SEVERITY_TIME = 2;
    private static final int MSG_SEVERITY_DELAYED = 1 * 1000;

    private static final int MSG_SHOW_TEXT = 1;
    private static final int SHOW_TEXT_TIME = 5 * 1000;

    private static final int MSG_AUTO_TO_OTHER = 3;
    private static final int MSG_AUTO_TO_OTHER_TIME = 3 * 1000;

    private boolean mIsAutoJumpToOther = true;

    private float density;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SHOW_TEXT:
                    refreshStaticsRollUI();
                    break;
                case MSG_SEVERITY_TIME:
                    refreshSeverityTextUI();
                    break;
                case MSG_AUTO_TO_OTHER:
                    onClickMarket();
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
    protected void initView() {
        initTextSwitcher();
        density = getResources().getDisplayMetrics().density;

    }

    @Override
    protected void initData() {
        boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
        if (mIsLogin) {
            CrashReport.setUserId(TianShenUserUtil.getUserId(mContext));
            initUserConfig();
            initActivity();
        } else {
            CrashReport.setUserId(TianShenUserUtil.getUserId(mContext));
            initSelWithdrawalsData();
        }
        initStaticsRoll();
    }

    @Override
    protected void setListensers() {
        rlHomeTianshenCard.setOnClickListener(this);
        ll_loan_day.setOnClickListener(this);
        tvHomeApply.setOnClickListener(this);
        tv_repay_by_bank.setOnClickListener(this);
        tv_repay_by_ali.setOnClickListener(this);
//        ivProceduresHome.setOnClickListener(this);
        tv_home_confirm_money.setOnClickListener(this);
        iv_danger_money.setOnClickListener(this);
        iv_home_market.setOnClickListener(this);
        iv_normal_money.setOnClickListener(this);
        iv_home_msg.setOnClickListener(this);
        minMaxSb.setOnMinMaxSeekBarChangeListener(new MyOnMinMaxSeekBarChangeListener());
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        StatusBarUtil.setStatusBarWhiteOrGradient(getActivity(), true);
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
            case R.id.ll_loan_day: //点击了期限选择
                showSelectLoanDayDialog();
                break;
            case R.id.tv_home_apply: //点击了立即申请
                onClickApply();
                break;
            case R.id.tv_repay_by_bank: //点击了银行卡还款
                repayByBank();
                break;
            case R.id.tv_repay_by_ali: //点击了支付宝还款
                repayByAli();
                break;
            case R.id.tv_home_confirm_money: //点击了刷新&我知道了按钮
                onClickIKnow();
                break;
//            case R.id.iv_procedures_home: //点击了借款提示
//                ToastUtil.showToast(mContext, "本界面显示的费率及期限仅供参考，最终的借款金额、借款费率和期限，会根据您提交的资料是否真实以及综合信用评估结果来确定。");
//                break;
            case R.id.iv_danger_money: //点击了逾期提示
            case R.id.iv_normal_money: //点击了逾期提示
                showDangerTipsDialog();
                break;
            case R.id.iv_home_market: //点击了浏览超市
                onClickMarket();
                break;
            case R.id.iv_home_msg: //点击了消息
                gotoActivity(mContext, MessageCenterActivity.class, null);
                break;

        }
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
        mQuotaCount = 0;
    }


    /**
     * 点击了立即申请按钮
     */
    private void onClickApply() {
        if (mUserConfig == null || mUserConfig.getData() == null) {
            ToastUtil.showToast(getActivity(), "数据错误");
            return;
        }

        String cur_credit_step = mUserConfig.getData().getCur_credit_step();
        String total_credit_step = mUserConfig.getData().getTotal_credit_step();


        //存储借款订单信息(到订单确认页面会用到)
        List<WithdrawalsItemBean> withdrawalsItemBeens = mSelWithdrawalsBean.getData();
        WithdrawalsItemBean withdrawalsItemBean = withdrawalsItemBeens.get(mCurrentLoanDaysIndex);
        String id = withdrawalsItemBean.getId();//选择产品的ID
        TianShenUserUtil.saveUserRepayId(mContext, id);
        TianShenUserUtil.saveUserConsumeAmount(mContext, mCurrentOrderMoney);

        if (cur_credit_step.equals(total_credit_step)) {//认证完毕检查是否绑定了"向上"银行卡
            checkIsBindXiangShang();
        } else {//没有认证完毕跳转到认证中心页面
            Bundle applyBundle = new Bundle();
            applyBundle.putBoolean(GlobalParams.IS_FROM_CARD_KEY, false);
            gotoActivity(mContext, AuthCenterActivity.class, applyBundle);
        }

    }

    /**
     * 检查是否绑定了"向上"银行卡
     */
    private void checkIsBindXiangShang() {
        CheckIsXiangShang mCheckIsXiangShang = new CheckIsXiangShang(mContext);
        JSONObject json = new JSONObject();
        try {
            json.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            mCheckIsXiangShang.check(json, new BaseNetCallBack<XiangShangDataBean>() {
                @Override
                public void onSuccess(XiangShangDataBean bean) {
                    String isOK = bean.getData().getIs_xiangshang();
                    if ("1".endsWith(isOK)) {//(1:是;0:否)
                        mQuotaCount = 0;
                        getContacts();
                    } else {
                        gotoActivity(mContext, BindBankCardConfirmActivity.class, null);
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    /**
     * 得到联系人信息
     */
    private void getContacts() {

        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) { //获得联系人权限
                    getObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Function<List<HashMap<String, String>>, List<ContactsBean>>() {
                                @Override
                                public List<ContactsBean> apply(List<HashMap<String, String>> contacts) throws Exception {
                                    List<ContactsBean> mContactsBeanList = new ArrayList<>();
                                    for (int i = 0; i < contacts.size(); i++) {
                                        HashMap<String, String> contactMap = contacts.get(i);
                                        String name = contactMap.get("name");
                                        String phone = contactMap.get("phone");
                                        ContactsBean mContactsBean = new ContactsBean();
                                        mContactsBean.setContact_name(name);
                                        mContactsBean.setContact_phone(phone);
                                        mContactsBeanList.add(mContactsBean);
                                    }
                                    return mContactsBeanList;
                                }
                            })
                            .subscribe(getObserver());
                } else { //没有获得权限
                    String is_need_contacts = mUserConfig.getData().getIs_need_contacts();
                    if ("0".equals(is_need_contacts)) {//不强制上传联系人
                        gotoConfirmBaseMoneyActivity();
                    } else if ("1".equals(is_need_contacts)) {
                        ToastUtil.showToast(mContext, "请您设置打开通信录读取");
                    }
                }
            }
        });
    }


    private Observable<List<HashMap<String, String>>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<HashMap<String, String>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HashMap<String, String>>> e) throws Exception {
                if (!e.isDisposed()) {
                    List<HashMap<String, String>> contacts = PhoneUtils.getAllContactInfo(mContext);
                    e.onNext(contacts);
                    e.onComplete();
                }
            }
        });
    }

    private Observer<List<ContactsBean>> getObserver() {
        return new Observer<List<ContactsBean>>() {
            //任务执行之前
            @Override
            public void onSubscribe(Disposable d) {
                String loadText = mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
                ViewUtil.createLoadingDialog((Activity) mContext, loadText, false);
            }

            //任务执行之后
            @Override
            public void onNext(List<ContactsBean> value) {
                if (value.size() == 0) {
                    MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_14);
                    String is_need_contacts = mUserConfig.getData().getIs_need_contacts();
                    if ("0".equals(is_need_contacts)) {//不强制上传联系人
                        gotoConfirmBaseMoneyActivity();
                    } else if ("1".equals(is_need_contacts)) {
                        ToastUtil.showToast(mContext, "请您设置打开通信录读取");
                    }
                } else {
                    MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_15);
                    uploadContacts(value);
                }
            }

            //任务执行完毕
            @Override
            public void onComplete() {
                ViewUtil.cancelLoadingDialog();
            }

            //任务异常
            @Override
            public void onError(Throwable e) {
                ViewUtil.cancelLoadingDialog();
            }

        };
    }

    /**
     * 上传联系人
     */
    private void uploadContacts(List<ContactsBean> list) {
        SaveContacts mSaveContactsAction = new SaveContacts(mContext);
        JSONObject json = new JSONObject();
        try {
            json.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            json.put("contact_list", new JSONArray(GsonUtil.bean2json(list)));
            mSaveContactsAction.saveContacts(json, tvHomeApply, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_16);
                    gotoConfirmBaseMoneyActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    String is_need_contacts = mUserConfig.getData().getIs_need_contacts();
                    if ("0".equals(is_need_contacts)) {//不强制上传联系人
                        gotoConfirmBaseMoneyActivity();
                    } else if ("1".equals(is_need_contacts)) {
                        ToastUtil.showToast(mContext, "请您设置打开通信录读取");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void initSelWithdrawalsData() {

        try {
            JSONObject jsonObject = new JSONObject();
            boolean mIsLogin = TianShenUserUtil.isLogin(mContext);
            if (mIsLogin) {
                jsonObject.put("init", "0");
                String userId = TianShenUserUtil.getUserId(mContext);
                jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
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
                    refreshCardUI();
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetUserConfig getUserConfig = new GetUserConfig(mContext);
            getUserConfig.userConfig(jsonObject, null, true, new BaseNetCallBack<UserConfig>() {
                @Override
                public void onSuccess(UserConfig paramT) {
                    mUserConfig = paramT;
                    checkUserConfig();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if (xrecyclerview_order_status != null) {
                        xrecyclerview_order_status.refreshComplete();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到活动
     */
    private void initActivity() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetActivity getActivity = new GetActivity(mContext);
            getActivity.activity(jsonObject, null, true, new BaseNetCallBack<ActivityBean>() {
                @Override
                public void onSuccess(ActivityBean activityBean) {
                    showBannerDialog(activityBean);
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

        if (mUserConfig == null || mUserConfig.getData() == null) {
            //TODO 展示解析错误的UI
            ToastUtil.showToast(getActivity(), "数据错误");
            return;
        }

        //    如果没有唤醒支付宝url,就隐藏支付宝还款入口
        String url = mUserConfig.getData().getAli_repay_url();
        if (TextUtils.isEmpty(url)){
            mRlAlipay.setVisibility(View.GONE);
        }else {
            mRlAlipay.setVisibility(View.VISIBLE);
        }

        //刷新右上角的消息
        String count = mUserConfig.getData().getMessage_count();
        refreshMessage(count);

        //刷新天神卡
        refreshCardUI();

        //存储用户相关
        String is_payway = mUserConfig.getData().getIs_payway();
        String is_show_service_telephone = mUserConfig.getData().getIs_show_service_telephone();
        if (TextUtils.isEmpty(is_show_service_telephone)) {
            is_show_service_telephone = "0";
        }

        TianShenUserUtil.saveIsPayWayBySelf(mContext, is_payway);
        TianShenUserUtil.saveIsShowServiceTelephone(mContext, is_show_service_telephone);

        boolean clickedHomeGetMoneyButton = TianShenUserUtil.isClickedHomeGetMoneyButton(mContext);
        boolean clickedHomeRePayMoneyButton = TianShenUserUtil.isClickedHomeRePayMoneyButton(mContext);

        String status = mUserConfig.getData().getStatus();

        tv_home_tianshen_card_can_pay.setVisibility(View.GONE);
        tv_home_confirm_money.setVisibility(View.GONE);

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
                break;
            case "2"://2:审核通过；
                showConsumeStatusUI();//显示用户订单轨迹的UI
                if ("1".equals(is_payway)) { //如果是掌众需要弹出dialog
                    String zzOrderMark = mUserConfig.getData().getZzOrderMark();
                    if ("0".equals(zzOrderMark)) {
                        tv_home_confirm_money.setText("提现");
                        tv_home_confirm_money.setVisibility(View.VISIBLE);
                    } else if ("1".equals(zzOrderMark)) {
                        showVerifyCodeDialog();
                    }
                } else if ("0".equals(is_payway)) {
                    if (mUserConfig.getData().getDiff_rate() == 1) {
                        tv_home_confirm_money.setText("提现");
                        tv_home_confirm_money.setVisibility(View.VISIBLE);
                    }
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
                break;
            case "5"://5:放款失败
                showConsumeStatusUI();
                break;
            case "6"://6放款中
                showConsumeStatusUI();
                break;
            case "7"://7 已还款 (废弃)
//                showConsumeStatusUI();
//                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
            case "8": //8已经提交还款（还款金额还没到账
                showConsumeStatusUI();
                break;
            case "9": //9决策失败
                showConsumeStatusUI();
                break;
            case "12": //12还款失败
                showConsumeStatusUI();
                tv_home_confirm_money.setText("重新还款");
                tv_home_confirm_money.setVisibility(View.VISIBLE);
                break;
            case "16"://16 提现申请已提交
                showConsumeStatusUI();
                tv_home_confirm_money.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 判断是手机贷还款还是其他还款
     */
    private void repayByBank() {
        if (mUserConfig == null || mUserConfig.getData() == null) {
            return;
        }
        String isPayway = mUserConfig.getData().getIs_payway();
        if ("2".equals(isPayway)) {
            String sjdUrl = mUserConfig.getData().getSjd_url();
            gotoSJDActivity(sjdUrl);
        } else {
            String consume_id = mUserConfig.getData().getConsume_id();
            Bundle bundle = new Bundle();
            bundle.putString(GlobalParams.CONSUME_ID, consume_id);
            gotoActivity(mContext, ConfirmRepayActivity.class, bundle);
            ll_repay.setVisibility(View.GONE);
        }
    }

    /**
     * 跳转到支付宝还款
     */
    private void repayByAli() {
        if (mUserConfig == null || mUserConfig.getData() == null) {
            return;
        }
//        Bundle bundle = new Bundle();
        String url = mUserConfig.getData().getAli_repay_url();
        if (TextUtils.isEmpty(url)){
            ToastUtil.showToast(getActivity(),"您暂不支持支付宝还款,请联系客服");
            return;
        }

        String ali_repay_url = url + "&src=android";
//        bundle.putString(GlobalParams.WEB_URL_KEY, ali_repay_url);
//        gotoActivity(mContext, AliRepayWebActivity.class, bundle);

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(ali_repay_url);
        intent.setData(content_url);
        startActivity(intent);

        ll_repay.setVisibility(View.GONE);
        showRrefreshAliRepayDialog();
    }

    /**
     * 显示刷新支付宝结果的dialog
     */
    private void showRrefreshAliRepayDialog() {

        new MaterialDialog.Builder(mContext)
                .title("温馨提示")
                .content("请点击确定按钮,查看还款是否成功")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        initUserConfig();
                    }
                })
                .show()
                .setCancelable(false);
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
            case "2": //审核通过(放款中)
                String zzOrderMark = mUserConfig.getData().getZzOrderMark();
                String is_payway = mUserConfig.getData().getIs_payway();
                if ("1".equals(is_payway) && "0".equals(zzOrderMark)) {
                    gotoConfirmMoneyActivity();
                } else if ("0".equals(is_payway) && mUserConfig.getData().getDiff_rate() == 1) {
                    gotoDiffRateMoneyActivity();
                }
                break;
            case "3": //借款成功
                showFriendlyTipsDialog();
                break;
            case "12": //还款失败
                repayIknow();
                break;
        }
    }

    private void gotoDiffRateMoneyActivity() {
        String consume_id = mUserConfig.getData().getConsume_id();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.CONSUME_ID, consume_id);
        gotoActivity(mContext, ConfirmDiffRateMoneyActivity.class, bundle);
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
        isShowMarker();
    }

    /**
     * 是否显示流量超市
     */
    private void isShowMarker() {
        String flow_supermarket_num = mUserConfig.getData().getFlow_supermarket_num();
        if (TextUtils.isEmpty(flow_supermarket_num)) {
            iv_home_market.setVisibility(View.GONE);
        } else {
            int num = Integer.parseInt(flow_supermarket_num);
            if (num == 0) {
                iv_home_market.setVisibility(View.GONE);
            } else {
                iv_home_market.setVisibility(View.VISIBLE);
                if (mIsAutoJumpToOther) {
                    mIsAutoJumpToOther = false;
                    mHandler.sendEmptyMessageDelayed(MSG_AUTO_TO_OTHER, MSG_AUTO_TO_OTHER_TIME);
                }

            }
        }
    }

    /**
     * 显示还款UI
     */
    private void showRepayUI() {
        ll_not_order.setVisibility(View.GONE);
        ll_order.setVisibility(View.GONE);
        ll_repay.setVisibility(View.VISIBLE);

        String is_payway = mUserConfig.getData().getIs_payway();
        String repayment_day = mUserConfig.getData().getRepayment_time_day();
        //如果当前是手机贷 并且当前没有日期
        if ("2".equals(is_payway) && TextUtils.isEmpty(repayment_day)) {
            sjdLoanBack();
            return;
        }

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
                tv_repay_overdue_sum_money.setText(MoneyUtils.getPointTwoMoney(consume_amount, overdue_amount));//一共
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
                String consumeAmountY = MoneyUtils.getPointTwoMoney(consumeAmount);
                tv_repay_money.setText(consumeAmountY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initXRecyclerview() {
        if (mOrderStatusAdapter == null) {
            mOrderStatusAdapter = new OrderStatusAdapter(mContext, mUserConfig);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            xrecyclerview_order_status.setLayoutManager(layoutManager);
            xrecyclerview_order_status.setLoadingMoreEnabled(false);
            xrecyclerview_order_status.setPullRefreshEnabled(true);
            xrecyclerview_order_status.setLoadingListener(new MyLoadingListener());
            ArrowRefreshHeader arrowRefreshHeader = new ArrowRefreshHeader(mContext);
            arrowRefreshHeader.mMeasuredHeight = 50;
            arrowRefreshHeader.setVisibleHeight(50);
            xrecyclerview_order_status.setRefreshHeader(arrowRefreshHeader);
            xrecyclerview_order_status.setAdapter(mOrderStatusAdapter);
        } else {
            mOrderStatusAdapter.setData(mUserConfig);
            mOrderStatusAdapter.notifyDataSetChanged();
            xrecyclerview_order_status.refreshComplete();
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
     * 显示进度条上方的金额
     */
    private void showSeekBarThumbMoney() {
        rl_home_max_sb_thumb.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条上方的金额
     */
    private void hideSeekBarThumbMoney() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                rl_home_max_sb_thumb.setVisibility(View.INVISIBLE);
            }
        }, 350);
    }

//    /**
//     * 金额移动动画
//     */
//    private void moveSeekBarThumbMoney(int progress) {
//        String s = String.format(Locale.CHINA, "%d", progress);
//        tv_home_max_sb_thumb.setText(s);
//        float val;
//        if (minMaxSb.getMax() == 0) {  //当服务器返回的min = max 的时候  getmax = 0  会导致下面的计算出错
//            val = 0;
//        } else {
//            val = (((float) minMaxSb.getProgress() * (float) (minMaxSb.getWidth() - 2 * minMaxSb.getThumbOffset())) / minMaxSb.getMax());
//        }
//        float offset = minMaxSb.getThumbOffset();
//        float newX = val + offset;
//        if (minMaxSb.getProgress() == minMaxSb.getMax() && minMaxSb.getMax() != 0) {
//            newX -= density * 10;
//        }
//        LogUtil.d("abcd", "val = " + val);
//        LogUtil.d("abcd", "offset = " + offset);
//        ObjectAnimator x = ObjectAnimator.ofFloat(rl_home_max_sb_thumb, "x", rl_home_max_sb_thumb.getX(), newX);
//        ObjectAnimator y = ObjectAnimator.ofFloat(rl_home_max_sb_thumb, "y", rl_home_max_sb_thumb.getY(), rl_home_max_sb_thumb.getY());
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(x, y);
//        animatorSet.setDuration(0);
//        animatorSet.start();
//    }

    /**
     * 刷新右上角的消息
     */
    private void refreshMessage(String count) {
        int msgCount = 0;
        if (!TextUtils.isEmpty(count)) {
            msgCount = Integer.parseInt(count);
        }
        Drawable drawable;
        if (msgCount > 0) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_message_home_new);
        } else {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_message_home);
        }
        iv_home_msg.setImageDrawable(drawable);
    }

    /**
     * 刷新天神卡
     */
    private void refreshCardUI() {
        if (TianShenUserUtil.isLogin(mContext)) {
            if (mUserConfig == null) {
                return;
            }
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
        } else {
            if (mSelWithdrawalsBean == null) {
                return;
            }
            String cardNum = StringUtil.getTianShenCardNum("8888888888888888");
            tvHomeTianshenCardNum.setText(cardNum);
            tvHomeTianshenCardRenzheng.setText("认证" + 0 + "/" + 0);
            tv_home_tianshen_card_can_pay.setVisibility(View.GONE);
            try {
                String max_cash = mSelWithdrawalsBean.getMax_cash();
                String max_cashY = MoneyUtils.changeF2Y(max_cash);
                tvHomeUserLimitValue.setText(max_cashY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (getActivity().isFinishing()) {
            return;
        }

        tv_dialog_get_verify_code.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            tv_dialog_get_verify_code.setText("重获取验证码");
            mStartTime = 59;
            tv_dialog_get_verify_code.setEnabled(true);
            mHandler.removeMessages(MSG_SEVERITY_TIME);
        } else {
            tv_dialog_get_verify_code.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED);
        }

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
            final int def_cashInt = Integer.valueOf(def_cashY);

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

//            //重置金额偏移量
//            minMaxSb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    minMaxSb.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    moveSeekBarThumbMoney(def_cashInt);
//                }
//            });


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
            tvLoanDayValue.setText(repayTimes);
        }

    }

    /**
     * 刷新当前借款的金额，和综合费用
     */
    private void refreshLoanNumUI(int progress) {

        if (mSelWithdrawalsBean == null) {
            return;
        }

        //设置借款金额
        String currentMoney = MoneyUtils.addTwoPoint(progress);
        tv_home_money.setText(currentMoney);
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("consume_id", consume_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final IKnow iKnow = new IKnow(mContext);
        iKnow.know(jsonObject, new BaseNetCallBack<IknowBean>() {
            @Override
            public void onSuccess(IknowBean paramT) {
                if (paramT.getCode() == 0) {

                    String content = paramT.getData().getContent();
                    String is_pop = paramT.getData().getIs_pop();
                    if ("1".equals(is_pop)) {
                        showMoneyUpDialog(content);
                    }

                    String status = mUserConfig.getData().getStatus();
                    switch (status) {
                        case "0":
                            TianShenUserUtil.clearMoneyStatus(mContext);
                            break;
                        case "12":
                            TianShenUserUtil.saveIsClickedHomeGetMoneyButton(mContext, true);
                            break;
                    }
                    initUserConfig();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    /**
     * 通知服务器手机贷返回
     */
    private void sjdLoanBack() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SJDLoanBack loanBack = new SJDLoanBack(mContext);
        loanBack.sjdLoanBack(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                initUserConfig();
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

        if (getActivity().isFinishing()) {
            return;
        }

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
                TianShenUserUtil.saveIsClickedHomeGetMoneyButton(mContext, true);
                showRepayUI();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    /**
     * 显示额度提升Dialog
     */
    private void showMoneyUpDialog(String content) {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_money_up, null, false);
        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        TextView tv_dialog_money_up = (TextView) view.findViewById(R.id.tv_dialog_money_up);
        TextView tv_dialog_money_up_iknow = (TextView) view.findViewById(R.id.tv_dialog_money_up_iknow);
        tv_dialog_money_up.setText(content);
        tv_dialog_money_up_iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 显示逾期Dialog
     */
    private void showDangerTipsDialog() {

        if (getActivity().isFinishing()) {
            return;
        }
        if (mUserConfig == null || mUserConfig.getData() == null) {
            return;
        }
        new RepayDetailDialogView(getActivity(), TianShenUserUtil.getUserId(mContext), mUserConfig.getData().getConsume_id());
//
//        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = mLayoutInflater.inflate(R.layout.dialog_danger_tips, null, false);
//        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);
//        mDialog.setContentView(view);
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.setCancelable(false);
//        TextView tv_dialog_danger_tips = (TextView) view.findViewById(R.id.tv_dialog_danger_tips);
//        tv_dialog_danger_tips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
    }

    /**
     * 显示第三方产品(掌众)输入验证码Dialog
     */
    private void showVerifyCodeDialog() {

        if (getActivity().isFinishing()) {
            return;
        }

        //第一次打开dialog调用验证码type为0，之后为1
        mVerifyCodeType = "0";

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_verify_code, null, false);
        mVerifyCodeDialog = new Dialog(mContext, R.style.MyDialog);
        mVerifyCodeDialog.setContentView(view);
        mVerifyCodeDialog.setCanceledOnTouchOutside(false);
        mVerifyCodeDialog.setCancelable(true);

        tv_dialog_get_verify_code = (TextView) view.findViewById(R.id.tv_dialog_get_verify_code);
        final EditText et_dialog_verify_code = (EditText) view.findViewById(R.id.et_dialog_verify_code);
        TextView tv_dialog_get_money = (TextView) view.findViewById(R.id.tv_dialog_get_money);

        tv_dialog_get_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSeverityTextUI();
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
     * 显示活动的Dialog
     */
    private void showBannerDialog(ActivityBean activityBean) {
        ArrayList<ActivityDataBean> activityBeanData = activityBean.getData().getActivity_list();
        if (activityBeanData == null || activityBeanData.size() == 0) {
            return;
        }

        //判断是否是同一天
        long lastTime = TianShenUserUtil.getShowActivityTime(mContext);
        if (Utils.isSameDay(lastTime)) {//同一天

            //判断之前显示了几次
            String remind_num = activityBean.getData().getRemind_num();
            int remindNum = 0;
            if (!TextUtils.isEmpty(remind_num)) {
                remindNum = Integer.parseInt(remind_num);
                remindNum = remindNum - 1;
            }

            int oldCount = TianShenUserUtil.getShowActivityCount(mContext);
            if (oldCount > remindNum) {
                //显示超过服务器约定的次数
                return;
            }
            oldCount++;
            TianShenUserUtil.saveShowActivityCount(mContext, oldCount);

        } else { //不是同一天
            TianShenUserUtil.saveShowActivityCount(mContext, 1);
        }

        long time = System.currentTimeMillis();
        TianShenUserUtil.saveShowActivityTime(mContext, time);

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_banner, null, false);
        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);

        ViewPager vp_dialog_banner = (ViewPager) view.findViewById(R.id.vp_dialog_banner);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator_dialog_banner_indicator);
        ImageView iv_dialog_banner_close = (ImageView) view.findViewById(R.id.iv_dialog_banner_close);

//        int screenWidth = Utils.getWidthPixels(mContext);
//        int screenHeight = Utils.getHeightPixels(mContext);
//        mDialog.setContentView(view, new ViewGroup.LayoutParams(screenWidth * 8 / 9, screenHeight * 2 / 3));

        mDialog.setContentView(view);

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        final ArrayList<View> viewList = new ArrayList<>();// 将要分页显示的View装入数组中

        for (int i = 0; i < activityBeanData.size(); i++) {
            final ActivityDataBean data = activityBeanData.get(i);
            final String activityType = data.getActivity_type();
            String picUrl = data.getPic_url();
            final String activityId = data.getActivity_id();
            if ("0".equals(activityType)) {//分享活动
                View pageView = mLayoutInflater.inflate(R.layout.dialog_banner_invite_friends, null);
                ImageView iv_dialog_banner_invite_friends = (ImageView) pageView.findViewById(R.id.iv_dialog_banner_invite_friends);
                ImageLoader.load(mContext.getApplicationContext(), picUrl, iv_dialog_banner_invite_friends);
                iv_dialog_banner_invite_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(GlobalParams.ACTIVITY_ID, activityId);
                        MaiDianUtil.ding(getActivity(), MaiDianUtil.FLAG_23, MaiDianUtil.RESULT_DEFAULT, activityType);
                        gotoActivity(mContext, InviteFriendsActivity.class, bundle);
                        mDialog.dismiss();
                    }
                });
                viewList.add(pageView);
            } else if ("1".equals(activityType)) {//转盘活动
                View pageView = mLayoutInflater.inflate(R.layout.dialog_banner_read, null);
                ImageView iv_dialog_banner_read = (ImageView) pageView.findViewById(R.id.iv_dialog_banner_read);
                ImageLoader.load(mContext.getApplicationContext(), picUrl, iv_dialog_banner_read);
                iv_dialog_banner_read.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        String url = data.getActivity_url();
                        url += "?customer=" + TianShenUserUtil.getUserId(getActivity());
                        url += "&token=" + TianShenUserUtil.getUserToken(getActivity());
                        url += "&type=1";
                        bundle.putString(GlobalParams.WEB_URL_KEY, url);
                        bundle.putString(GlobalParams.WEB_FROM, GlobalParams.FROM_HOME);
                        MaiDianUtil.ding(getActivity(), MaiDianUtil.FLAG_23, MaiDianUtil.RESULT_DEFAULT, activityType);
                        gotoActivity(mContext, WebActivity.class, bundle);
                        mDialog.dismiss();
                    }
                });
                viewList.add(pageView);
            } else if ("2".equals(activityType)) { //阅读
                View pageView = mLayoutInflater.inflate(R.layout.dialog_banner_invite_friends, null);
                ImageView iv_dialog_banner_invite_friends = (ImageView) pageView.findViewById(R.id.iv_dialog_banner_invite_friends);
                ImageLoader.load(mContext.getApplicationContext(), picUrl, iv_dialog_banner_invite_friends);
                iv_dialog_banner_invite_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        String url = data.getActivity_url();
                        bundle.putString(GlobalParams.WEB_URL_KEY, url);
                        bundle.putString(GlobalParams.WEB_TYPE, GlobalParams.TYPE_READ);
                        MaiDianUtil.ding(getActivity(), MaiDianUtil.FLAG_23, MaiDianUtil.RESULT_DEFAULT, activityType);
                        gotoActivity(mContext, WebActivity.class, bundle);
                        mDialog.dismiss();
                    }
                });
                viewList.add(pageView);
            } else if ("3".equals(activityType)) { //只看图片
                View pageView = mLayoutInflater.inflate(R.layout.dialog_banner_invite_friends, null);
                ImageView iv_dialog_banner_invite_friends = (ImageView) pageView.findViewById(R.id.iv_dialog_banner_invite_friends);
                ImageLoader.load(mContext.getApplicationContext(), picUrl, iv_dialog_banner_invite_friends);
                iv_dialog_banner_invite_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaiDianUtil.ding(getActivity(), MaiDianUtil.FLAG_23, MaiDianUtil.RESULT_DEFAULT, activityType);
                    }
                });

                viewList.add(pageView);
            }

        }
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };

        vp_dialog_banner.setAdapter(pagerAdapter);
        vp_dialog_banner.setOffscreenPageLimit(viewList.size());

        if (activityBeanData.size() == 1) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(vp_dialog_banner);
        }
        vp_dialog_banner.setCurrentItem(0);

        iv_dialog_banner_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }


    /**
     * 得到第三方验证码
     */
    private void initVerifySmsForConfirmLoanUrl(final String type) {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            String consume_id = mUserConfig.getData().getConsume_id();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("consume_id", consume_id);
            jsonObject.put("type", type);
            GetVerifySmsForConfirmLoan getVerifySmsForConfirmLoan = new GetVerifySmsForConfirmLoan(mContext);
            getVerifySmsForConfirmLoan.getVerifySmsForConfirmLoan(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if ("0".equals(type)) {
                        if (errorCode == 10002) {
                            initVerifySmsForConfirmLoanUrl("1");
                        }
                    }
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
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("consume_id", consume_id);
            jsonObject.put("verufy_code", verufy_code);
            jsonObject.put("type", "1");
            SubmitVerifyCode submitVerifyCode = new SubmitVerifyCode(mContext);
            submitVerifyCode.verifyCode(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    mVerifyCodeDialog.dismiss();
                    initUserConfig();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    mVerifyCodeDialog.dismiss();
                    initUserConfig();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下拉刷新（极端情况下调用）
     */
    private void initManualRefresh() {
        JSONObject jsonObject = new JSONObject();
        try {
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ManualRefresh manualRefresh = new ManualRefresh(mContext);
        manualRefresh.manualRefresh(jsonObject, null, new BaseNetCallBack<ManualRefreshBean>() {
            @Override
            public void onSuccess(ManualRefreshBean paramT) {
                if (paramT == null) {
                    if (xrecyclerview_order_status != null) {
                        xrecyclerview_order_status.refreshComplete();
                    }
                    return;
                }
                String status = paramT.getData().getStatus();
                if (!"1".equals(status)) {
                    initUserConfig();
                } else {
                    if (xrecyclerview_order_status != null) {
                        xrecyclerview_order_status.refreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                if (xrecyclerview_order_status != null) {
                    xrecyclerview_order_status.refreshComplete();
                }
            }
        });
    }

    /**
     * 点击了流量超市
     */
    private void onClickMarket() {
        String superMarkerNUM = mUserConfig.getData().getFlow_supermarket_num();
        int superMarkerNum = 0;
        if (!TextUtils.isEmpty(superMarkerNUM)) {
            superMarkerNum = Integer.parseInt(superMarkerNUM);
        }
        if (superMarkerNum == 1) {
            String flowSupermarketId = mUserConfig.getData().getFlow_supermarket_id();
            addSuperMarketCount(flowSupermarketId);
        } else if (superMarkerNum > 1) {
            gotoActivity(mContext, SuperMarkerActivity.class, null);
        }
    }

    /**
     * 统计用户点击流量超市的点击量
     */
    private void addSuperMarketCount(String flowSupermarketId) {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("supermarket_id", flowSupermarketId);
            AddSuperMarketCount addSuperMarketCount = new AddSuperMarketCount(mContext);
            addSuperMarketCount.addSuperMarketCount(jsonObject, null, true, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    gotoSuperMarkerh5();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    gotoSuperMarkerh5();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到流量超市H5页面
     */
    private void gotoSuperMarkerh5() {
        String superMarkerURL = mUserConfig.getData().getFlow_supermarket_url();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, superMarkerURL);
        gotoActivity(mContext, WebActivity.class, bundle);
    }

    /**
     * 跳转到手机贷H5页面
     */
    private void gotoSJDActivity(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.WEB_URL_KEY, url);
        bundle.putBoolean(GlobalParams.SJD_BACK_DELAY_KEY, false);
        gotoActivity(mContext, SJDActivity.class, bundle);
    }

    /**
     * 跳转到预下单页面
     */
    private void gotoConfirmBaseMoneyActivity() {
        gotoActivity(mContext, ConfirmBaseMoneyActivity.class, null);
    }

    /**
     * 跳转到掌众下单确认页面
     */
    private void gotoConfirmMoneyActivity() {
        gotoActivity(mContext, ConfirmMoneyActivity.class, null);
    }

    /**
     * seekbar滑动监听
     */
    private class MyOnMinMaxSeekBarChangeListener implements MinMaxSeekBar.OnMinMaxSeekBarChangeListener {

        @Override
        public void onProgressChanged(float progress) {
            refreshLoanNumUI((int) progress);
//            moveSeekBarThumbMoney((int) progress);
        }

        @Override
        public void onStartTrackingTouch(float progress) {
//            showSeekBarThumbMoney();
        }

        @Override
        public void onStopTrackingTouch(float progress) {
            refreshLoanNumUI((int) progress);
//            hideSeekBarThumbMoney();
        }
    }

    /**
     * 状态列表刷新
     */
    private class MyLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            if (mUserConfig == null || mUserConfig.getData() == null) {
                ToastUtil.showToast(getActivity(), "数据错误");
                return;
            }
            String status = mUserConfig.getData().getStatus();
            String isPayway = mUserConfig.getData().getIs_payway();
            if ("1".equals(status) && "99".equals(isPayway)) {
                initManualRefresh();
            } else {
                initUserConfig();
            }
        }

        @Override
        public void onLoadMore() {

        }
    }

    /**
     * 收到了在注册页面登录成功的消息
     */
    @Subscribe
    public void onRegisterAndLoginSuccess(LoginSuccessEvent event) {
        LogUtil.d("abc", "收到了登录成功消息--刷新UI");
        CrashReport.setUserId(TianShenUserUtil.getUserId(mContext));
        initUserConfig();
        initActivity();
    }

    @Subscribe
    public void onTokenDeprecatedOrResetPassword(FinishCurrentActivityEvent event) {
        CrashReport.setUserId("unknown");
        initSelWithdrawalsData();
        initStaticsRoll();
    }

    /**
     * 收到了退出成功的消息
     */
    @Subscribe
    public void onLoginoutSuccess(LogoutSuccessEvent event) {
        LogUtil.d("abc", "收到了退出登录成功消息--刷新UI");
        CrashReport.setUserId("unknown");
        refreshMessage("0");
        initSelWithdrawalsData();
        initStaticsRoll();
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
     * 还款失败的event
     *
     * @param event
     */
    @Subscribe
    public void onReplayFaile(RepayFailureEvent event) {
        ll_repay.setVisibility(View.VISIBLE);
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
     * 刷新UserConfig
     */
    @Subscribe
    public void onUserConfigChangedEvent(UserConfigChangedEvent event) {
        initUserConfig();
    }

    /**
     * 收到了强行跳转到下单页面
     */
    @Subscribe
    public synchronized void onQuotaEvent(QuotaEvent event) {
        LogUtil.d("abc", "收到了强行跳转到下单页面");
        if (mQuotaFlag && mQuotaCount == 0) {
//            gotoActivity(mContext, ConfirmMoneyActivity.class, null);
        }
        mQuotaCount++;
    }

}
