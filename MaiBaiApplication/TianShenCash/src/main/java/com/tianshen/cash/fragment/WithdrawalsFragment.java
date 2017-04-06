package com.tianshen.cash.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.ActivateTheQuotaActivity;
import com.tianshen.cash.activity.AddBankCardActivity;
import com.tianshen.cash.activity.ContactsInfoActivity;
import com.tianshen.cash.activity.ImproveQuotaActivity;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.activity.MainActivity;
import com.tianshen.cash.activity.ResultActivity;
import com.tianshen.cash.activity.VerifyFailActivity;
import com.tianshen.cash.activity.WithdrawalsConfirmActivity;
import com.tianshen.cash.adapter.PrompAdapter;
import com.tianshen.cash.adapter.WithdrawalsTypeAdapter;
import com.tianshen.cash.base.BaseFragment;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.GetBankListBean;
import com.tianshen.cash.model.SelWithdrawalsBean;
import com.tianshen.cash.model.SignInBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.model.StatisticsRollDataBean;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.net.api.GetBindBankList;
import com.tianshen.cash.net.api.SelWithdrawals;
import com.tianshen.cash.net.api.SignIn;
import com.tianshen.cash.net.api.StatisticsRoll;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MainUtil;
import com.tianshen.cash.utils.PermissionUtils;
import com.tianshen.cash.utils.SharedPreferencesUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.UploadToServerUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.utils.ViewUtil;
import com.tianshen.cash.view.MyGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import u.aly.ap;

public class WithdrawalsFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout rl_min_max_quota;
    private LinearLayout ll_initial;
    private LinearLayout ll_not_initial;

    private TextView tv_quota;
    private TextView tv_max_quota;
    private TextView tv_repay_amount_month1; //没有登录或者没有额度下还款的金额
    private TextView tv_min_quota;
    private TextView tv_user_num;
    private TextView tv_arrival_amount; //到账金额具体数字
    private TextView tv_repay_amount_month2;//到期还款具体数字
    private TextView tv_repay_time; //借款期限具体数字
    private TextView tv_unite; //借款期限 后面的单位
    private TextView tv_repay_type;
    private TextView tv_repay_type2;


    private Button bt_apply_quota;
    private SeekBar sb_quota; //滑动条
    private MyGridView mgv_repay_time; //借款期限
    private ListView lv_promt; //顶部滚动条

    private int selectPosition = 0;
    private int quota = 0;
    private int default_unit = 1000;
    private int maxAmount = 0;
    private int defAmount = 0;
    private int uniteAmount = 0;
    private int currentSelection = 0;

    private final String ISINIT = "1";
    private final String NOT_INIT = "0";

    private boolean isSignInFirstSuccess = true;
    private boolean mIsll_zero_quotaCanClick = true;
    private boolean isUpLoadCallContacts = false, isUploadCallRecord = false;//上传状态：NOT_UPLOAD代表未上传，UPLOAD_FAIL代表上传失败，UPLOAD_SUCCESS代表上传成功
    private boolean mIsCallRecordPassByServer = false;
    private boolean mIsContactsPassByServer = false;
    private boolean mIsInitState = true; //true代表当前总的现金贷信用额度是0

    private CashSubItemBean cashSubItemBean;
    private SelWithdrawalsBean selWithdrawalsBean = new SelWithdrawalsBean();
    private List<WithdrawalsItemBean> withdrawalsItemBeanList = new ArrayList<>();
    private List<StatisticsRollDataBean> statisticsRollDataBeanList = new ArrayList<>();

    private WithdrawalsTypeAdapter adapter;
    private UploadToServerUtil uploadUtil;
    private WithdrawalsReciver mWithdrawalsReciver = new WithdrawalsReciver();

    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 33:
                    if (currentSelection < statisticsRollDataBeanList.size() - 1) {
                        currentSelection++;
                        smoothLvPromp();
                    } else {
                        currentSelection = 0;
                        smoothLvPromp();
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        try {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    if (resultCode == 5) {
                        new PermissionUtils(mContext).showPermissionDialog(3);//摄像头权限
                        return;
                    }
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(GlobalParams.IF_NEED_SCAN_FACE_KEY, true);
                        bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
                        bundle.putSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY, cashSubItemBean);
                        bundle.putString("repay_id", withdrawalsItemBeanList.get(selectPosition).getId());
                        gotoActivity(mContext, ActivateTheQuotaActivity.class, bundle);
                    }
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    }
                    break;
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int seekBarHeight = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_movement).getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = seekBarHeight / 2 + 20;
        rl_min_max_quota.setLayoutParams(params);
        return view;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_withdrawals;
    }

    @Override
    protected void findViews(View rootView) {
        tv_quota = (TextView) rootView.findViewById(R.id.tv_quota);
        tv_max_quota = (TextView) rootView.findViewById(R.id.tv_max_quota);
        tv_repay_amount_month1 = (TextView) rootView.findViewById(R.id.tv_repay_amount_month1);
        mgv_repay_time = (MyGridView) rootView.findViewById(R.id.mgv_repay_time);
        bt_apply_quota = (Button) rootView.findViewById(R.id.bt_apply_quota);
        sb_quota = (SeekBar) rootView.findViewById(R.id.sb_quota);
        ll_initial = (LinearLayout) rootView.findViewById(R.id.ll_initial);
        ll_not_initial = (LinearLayout) rootView.findViewById(R.id.ll_not_initial);
        tv_arrival_amount = (TextView) rootView.findViewById(R.id.tv_arrival_amount);
        tv_repay_amount_month2 = (TextView) rootView.findViewById(R.id.tv_repay_amount_month2);
        tv_repay_time = (TextView) rootView.findViewById(R.id.tv_repay_time);
        rl_min_max_quota = (RelativeLayout) rootView.findViewById(R.id.rl_min_max_quota);
        tv_repay_type = (TextView) rootView.findViewById(R.id.tv_repay_type);
        tv_repay_type2 = (TextView) rootView.findViewById(R.id.tv_repay_type2);
        tv_unite = (TextView) rootView.findViewById(R.id.tv_unite);
        tv_min_quota = (TextView) rootView.findViewById(R.id.tv_min_quota);
        tv_user_num = (TextView) rootView.findViewById(R.id.tv_user_num);
        lv_promt = (ListView) rootView.findViewById(R.id.lv_promt);
    }

    @Override
    protected void setListensers() {
        bt_apply_quota.setOnClickListener(this);
        mgv_repay_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                for (int i = 0; i < withdrawalsItemBeanList.size(); i++) {
                    if (i == position) {
                        withdrawalsItemBeanList.get(i).setCheck(true);
                    } else {
                        withdrawalsItemBeanList.get(i).setCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
                refreshView(selectPosition);
            }
        });
        sb_quota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (maxAmount < uniteAmount) {
                    tv_quota.setText("¥0");
                } else {
                    tv_quota.setText("¥" + (progress + uniteAmount));
                }
                quota = progress;
                if (0 == progress + uniteAmount) {
                    bt_apply_quota.setBackgroundResource(R.drawable.button_gray);
                } else {
                    bt_apply_quota.setBackgroundResource(R.drawable.select_bt);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refreshView(selectPosition);
            }
        });
    }

    @Override
    protected void initVariable() {
        registerReceiver();

        uploadUtil = new UploadToServerUtil(mContext);
        uploadUtil.setCallBack(new MyUploadCallBack());

        ViewUtil.createLoadingDialog((Activity) mContext, "", true);
        getStaticsRoll(); //顶部滚动条数据
        if (MainUtil.isLogin(mContext)) { //如果之前用户登录了，需要再次调用登录接口刷新
            freshBalanceAmount(true);
        } else {//没有登录的话基调基本数据即可
            init();
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mWithdrawalsReciver);
        super.onDestroy();
    }

    private void init() {
        mIsInitState = isInitState();
        adapter = new WithdrawalsTypeAdapter(mContext, withdrawalsItemBeanList);
        mgv_repay_time.setAdapter(adapter);
        selWithdrawals();
        if (mIsInitState) {
            ll_initial.setVisibility(View.VISIBLE);
            ll_not_initial.setVisibility(View.GONE);
            bt_apply_quota.setText("申请额度");
        } else {
            ll_initial.setVisibility(View.GONE);
            ll_not_initial.setVisibility(View.VISIBLE);
            bt_apply_quota.setText("确认提现");
        }
    }

    /**
     * 判断是否有额度
     */
    private boolean isInitState() {
        String balanceCashAmount = UserUtil.getCashAmount(mContext)/*getCashBalanceAmount(mContext)*/;
        if (null == balanceCashAmount || "".equals(balanceCashAmount)) {
            balanceCashAmount = "0";
        }
        int balance = Integer.parseInt(balanceCashAmount, 10);
        if (balance > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void selWithdrawals() {
        try {
            JSONObject jsonObject = new JSONObject();
            if (!(MainUtil.isLogin(mContext))) {
                jsonObject.put("customer_id", "0");
            } else {
                jsonObject.put("customer_id", UserUtil.getId(mContext));
            }
            jsonObject.put("init", mIsInitState ? ISINIT : NOT_INIT);
            final SelWithdrawals selWithdrawals = new SelWithdrawals(mContext);
            selWithdrawals.selWithdrawals(jsonObject, null, true, new BaseNetCallBack<SelWithdrawalsBean>() {
                @Override
                public void onSuccess(SelWithdrawalsBean paramT) {
                    selWithdrawalsBean = paramT;
                    withdrawalsItemBeanList.clear();
                    String defaultUnit = paramT.getUnit();
                    if ("".equals(defaultUnit) || null == defaultUnit) {
                        defaultUnit = "100";
                    }
                    default_unit = (int) ((Double.valueOf(defaultUnit)) / 100);
                    withdrawalsItemBeanList.addAll(selWithdrawalsBean.getData());
                    updateView();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView() {
        if (withdrawalsItemBeanList.size() == 0) {
            return;
        }
        for (int i = 0; i < withdrawalsItemBeanList.size(); i++) {
            if (i == selectPosition) {
                withdrawalsItemBeanList.get(selectPosition).setCheck(true);
            } else {
                withdrawalsItemBeanList.get(i).setCheck(false);
            }
        }
        adapter.notifyDataSetChanged();

        //最大提现金额
        String maxCash = selWithdrawalsBean.getMax_cash();
        if ("".equals(maxCash) || null == maxCash) {
            maxCash = "0";
        }
        maxAmount = Integer.valueOf(maxCash) / 100;

        //滑块单位
        String cashUnite = selWithdrawalsBean.getUnit();
        if ("".equals(cashUnite) || null == cashUnite) {
            cashUnite = "0";
        }
        uniteAmount = Integer.valueOf(cashUnite) / 100;

        //当前默认金额
        String defCash = selWithdrawalsBean.getDef_cash();
        if ("".equals(defCash) || null == defCash) {
            defCash = "0";
        }
        defAmount = Integer.valueOf(defCash) / 100;

        tv_max_quota.setText(maxAmount + "");
        if (maxAmount > uniteAmount) {
            tv_min_quota.setText(uniteAmount + "");
        } else {
            tv_min_quota.setText("0");
        }


        sb_quota.setMax(maxAmount - uniteAmount);
        sb_quota.setProgress(defAmount);
        refreshView(selectPosition);
    }

    private void smoothLvPromp() {
        lv_promt.setSelection(currentSelection);
        handler.sendEmptyMessageDelayed(33, 4000);
    }

    private void refreshView(int selectPosition) {
        if (withdrawalsItemBeanList.size() == 0) {
            return;
        }
        if (selectPosition >= withdrawalsItemBeanList.size()) {
            selectPosition = withdrawalsItemBeanList.size() - 1;
        }
        if (quota % default_unit > 0 && quota != maxAmount && quota != defAmount) {
            int remainder = quota % default_unit;
            if (remainder > default_unit / 2 && default_unit * (((int) (quota / default_unit)) + 1) <= maxAmount) {
                quota = default_unit * (((int) (quota / default_unit)) + 1);
            } else {
                quota = default_unit * (int) (quota / default_unit);
            }
            sb_quota.setProgress(quota);
        }

        List<CashSubItemBean> cashSubItemBeanList = withdrawalsItemBeanList.get(selectPosition).getCash_data();
        for (int i = 0; i < cashSubItemBeanList.size(); i++) {
            int withDrawalAmount = Integer.valueOf(cashSubItemBeanList.get(i).getWithdrawal_amount());
            if ((int) (withDrawalAmount / 100) == (quota + uniteAmount)) {
                cashSubItemBean = cashSubItemBeanList.get(i);
                String repayTotal = cashSubItemBean.getRepay_total();
                if ("".equals(repayTotal) || null == repayTotal) {
                    repayTotal = "0";
                }
                if (mIsInitState) {
                    if ("2".equals(withdrawalsItemBeanList.get(selectPosition).getRepay_unit())) {
                        tv_repay_type.setText("到期总还款（元）");
                    } else {
                        tv_repay_type.setText("每期还款（元）");
                    }

                    tv_repay_amount_month1.setText((int) (Double.valueOf(repayTotal) / 100) + "");
                } else {
                    String arrivalAmount = cashSubItemBean.getTransfer_amount();
                    if ("".equals(arrivalAmount) || null == arrivalAmount) {
                        arrivalAmount = "0";
                    }
                    tv_arrival_amount.setText((int) (Double.valueOf(arrivalAmount) / 100) + "");

                    if ("2".equals(withdrawalsItemBeanList.get(selectPosition).getRepay_unit())) {

                        tv_unite.setText("天");
                        tv_repay_type2.setText("到期应还（含服务费）");
                    } else {

                        tv_unite.setText("个月");
                        tv_repay_type2.setText("每月还款（含服务费）");
                    }
                    tv_repay_time.setText(withdrawalsItemBeanList.get(selectPosition).getRepay_times());
                    tv_repay_amount_month2.setText((int) (Double.valueOf(repayTotal) / 100) + "");
                }
            }
        }
    }

    /**
     * 加载顶部滚动条数据
     */
    private void getStaticsRoll() {
        final StatisticsRoll statisticsRoll = new StatisticsRoll(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a", "a");
            statisticsRoll.getStatisticsRoll(jsonObject, new BaseNetCallBack<StatisticsRollBean>() {
                @Override
                public void onSuccess(StatisticsRollBean paramT) {
                    statisticsRollDataBeanList.clear();
                    if (null != paramT) {
                        String peporNum = paramT.getApply_count();
                        if (null == peporNum) {
                            peporNum = "10000";
                        }
                        tv_user_num.setText(peporNum);
                        if (null != paramT.getData()) {
                            statisticsRollDataBeanList.addAll(paramT.getData());
                            PrompAdapter adapter = new PrompAdapter(mContext, statisticsRollDataBeanList);
                            lv_promt.setAdapter(adapter);
                            smoothLvPromp();
                        }
                    }
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
     * 刷新登录接口
     */
    public void freshBalanceAmount(boolean isShowLoadingDialog) {
        try {
            SignIn mSignIn = new SignIn(mContext);
            String device_id = UserUtil.getDeviceId(mContext);
            if (null == device_id || "".equals(device_id)) {
                return;
            }
            JSONObject json = new JSONObject();
            json.put("device_id", device_id);
            json.put("mobile", UserUtil.getMobile(mContext));
            final String password = UserUtil.getLoginPsw(mContext);
            json.put("password", password);
            String push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            json.put("push_id", push_id);
//            mSignIn.signIn(json, null, isShowLoadingDialog, new BaseNetCallBack<SignInBean>() {
//                @Override
//                public void onSuccess(SignInBean paramT) {
//                    UserUtil.setLoginPassword(mContext, password);
//                    init();
//                    if (isSignInFirstSuccess) {
//                        isSignInFirstSuccess = false;
//                        MainActivity mainActivity = (MainActivity) getActivity();
//                        mainActivity.validateCashApplyStatus();
//                    }
//                }
//
//                @Override
//                public void onFailure(String url, int errorType, int errorCode) {
//                    Log.e("loginError", "errorType" + errorType + "  erroCode" + errorCode);
//
//                }
//            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void getBankInfo(final Bundle bundle) {
        GetBindBankList getBindBankList = new GetBindBankList(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getBindBankList.getBindBankList(jsonObject, null, true, new BaseNetCallBack<GetBankListBean>() {
                @Override
                public void onSuccess(GetBankListBean paramT) {
                    Bundle newBundle = new Bundle();
                    newBundle.putAll(bundle);
                    if (paramT.getData().size() > 0) {
                        if (!mIsInitState) {
                            newBundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_GET_CASH);
                            newBundle.putString(GlobalParams.WITHDRAWALS_BANK_NAME_KEY, paramT.getData().get(0).getBank_name());
                            newBundle.putString(GlobalParams.WITHDRAWALS_CARD_NUM_KEY, paramT.getData().get(0).getCard_num());
                            newBundle.putString(GlobalParams.WITHDRAWALS_REPAY_ID_KEY, withdrawalsItemBeanList.get(selectPosition).getId());
                            newBundle.putString(GlobalParams.WITHDRAWALS_REPAY_TIMES_KEY, withdrawalsItemBeanList.get(selectPosition).getRepay_times());
                            newBundle.putString(GlobalParams.WITHDRAWALS_REPAY_UNIT, withdrawalsItemBeanList.get(selectPosition).getRepay_unit());
                            gotoActivity(mContext, WithdrawalsConfirmActivity.class, newBundle);
                        } else {
                            //运营商认证
                            gotoActivity(mContext, ImproveQuotaActivity.class, bundle);
                        }

                    } else {

                        if (!mIsInitState) {
                            newBundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_GET_CASH);
                        }
                        gotoActivity(mContext, AddBankCardActivity.class, newBundle);
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

    @Override
    public void onClick(View view) {
        if (!MainUtil.isLogin(mContext)) {
            gotoActivity(mContext, LoginActivity.class, null);
            return;
        }
        switch (view.getId()) {
            case R.id.bt_apply_quota:
                if (!MainUtil.isLogin(mContext)) {
                    gotoActivity(mContext, LoginActivity.class, null);
                    return;
                }
                if ((quota + uniteAmount) == 0) {
                    ToastUtil.showToast(mContext, "额度不可为0");
                    return;
                }
                if (GlobalParams.CASH_APPLY_REFUSE_BY_MACHINE.equals(UserUtil.getCashCreditStatus(mContext))) {
                    gotoActivity(mContext, VerifyFailActivity.class, null);
                    return;
                }
                gotoTargetActivity();
                break;
        }
    }

    private void gotoTargetActivity() {
        if (null == cashSubItemBean) {
            ToastUtil.showToast(mContext, "数据错误");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_WITHDRAWALS_APPLY);
        bundle.putSerializable(GlobalParams.WITHDRAWALS_BEAN_KEY, cashSubItemBean);
        bundle.putString("repay_id", withdrawalsItemBeanList.get(selectPosition).getId());
        //todo 上面这行会出现throwIndexOutOfBoundsException Invalid index 1, size is 1 发生在用户第一次注册之后点击"申请额度"会产生
        switch (UserUtil.getCreditStep(mContext)) {
            case GlobalParams.USER_STATUS_NEW:
                mIsll_zero_quotaCanClick = false;
                if (!isUploadCallRecord && !mIsCallRecordPassByServer) {
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
                    mIsll_zero_quotaCanClick = true;
                } else if (MainUtil.isNewUser(mContext) && !isUpLoadCallContacts && !mIsContactsPassByServer) {
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                    mIsll_zero_quotaCanClick = true;
                } else {
                    gotoActivity(mContext, ContactsInfoActivity.class, bundle);
                }
                break;
            case GlobalParams.HAVE_UPLOAD_IDCARD_INFO: {
                if (!mIsInitState) {
                    //  扫脸
                    Intent intent = new Intent(mContext, ResultActivity.class);
                    intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
                } else {
                    //运营商认证
                    gotoActivity(mContext, ImproveQuotaActivity.class, bundle);
                }

            }
            break;
            case GlobalParams.HAVE_SCAN_FACE:
                getBankInfo(bundle);
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO:
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                intent.putExtras(bundle);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                break;
        }
    }

    private class MyUploadCallBack implements UploadToServerUtil.UploadCallBack {

        @Override
        public void uploadSuccessCallBack(int type) {
            //上传通讯录、通话记录、短信等的回调
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人成功
                    isUpLoadCallContacts = true;
                    bt_apply_quota.performClick();
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录成功
                    isUploadCallRecord = true;
                    if (MainUtil.isNewUser(mContext) && !isUpLoadCallContacts && !mIsContactsPassByServer) {
                        uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                        mIsll_zero_quotaCanClick = true;
                    } else {
                        bt_apply_quota.performClick();
                    }
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
                    isUpLoadCallContacts = false;
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录失败
                    isUploadCallRecord = false;
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信失败
                    break;
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.PASS_CALL_RECORD_ACTION);
        intentFilter.addAction(GlobalParams.PASS_CONTACT_ACTION);
        intentFilter.addAction(GlobalParams.LOGIN_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.LOGOUT_ACTION);
        intentFilter.addAction(GlobalParams.WITHDRAWALS_ORDER_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION);
        intentFilter.addAction(GlobalParams.REPAY_WITHDRAWAL_SUCCESS_ACTION);
        mContext.registerReceiver(mWithdrawalsReciver, intentFilter);
    }

    private class WithdrawalsReciver extends BroadcastReceiver {
        //广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String topActivityName = Utils.getTopActivityName(getActivity());
            if (topActivityName.contains("MainActivity") && ((MainActivity) getActivity()) != null && 0 == ((MainActivity) getActivity()).getTopFragmentIndex()) {
                if (GlobalParams.PASS_CALL_RECORD_ACTION.equals(action)) {
                    mIsCallRecordPassByServer = true;
                    if (mIsll_zero_quotaCanClick == true) {
                        bt_apply_quota.performClick();
                    }
                } else if (GlobalParams.PASS_CONTACT_ACTION.equals(action)) {
                    mIsContactsPassByServer = true;
                    if (mIsll_zero_quotaCanClick == true) {
                        bt_apply_quota.performClick();
                    }
                }
            } else if (GlobalParams.LOGIN_SUCCESS_ACTION.equals(action) || GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION.equals(action) || GlobalParams.WITHDRAWALS_ORDER_SUCCESS_ACTION.equals(action) || GlobalParams.REPAY_WITHDRAWAL_SUCCESS_ACTION.equals(action)) {
                freshBalanceAmount(MainUtil.isLogin(mContext));
            } else if (GlobalParams.LOGOUT_ACTION.equals(action)) {
                init();
            }
        }

    }

}
