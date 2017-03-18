package com.maibai.user.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.activity.ActivateTheQuotaActivity;
import com.maibai.user.activity.AddBankCardActivity;
import com.maibai.user.activity.ContactsInfoActivity;
import com.maibai.user.activity.ImproveQuotaActivity;
import com.maibai.user.activity.LoginActivity;
import com.maibai.user.activity.MainActivity;
import com.maibai.user.activity.ResultActivity;
import com.maibai.user.activity.WithdrawalsConfirmActivity;
import com.maibai.user.adapter.WithdrawalsTypeAdapter;
import com.maibai.user.base.BaseFragment;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.CashSubItemBean;
import com.maibai.user.model.GetBankListBean;
import com.maibai.user.model.SelWithdrawalsBean;
import com.maibai.user.model.SignInBean;
import com.maibai.user.model.WithdrawalsItemBean;
import com.maibai.user.net.api.GetBindBankList;
import com.maibai.user.net.api.SelWithdrawals;
import com.maibai.user.net.api.SignIn;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.MainUtil;
import com.maibai.user.utils.PermissionUtils;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.UploadToServerUtil;
import com.maibai.user.utils.Utils;
import com.maibai.user.view.MyGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class WithdrawalsFragment extends BaseFragment implements View.OnClickListener, UploadToServerUtil.UploadCallBack {

    private boolean mIsll_zero_quotaCanClick = true;
    private boolean isUpLoadCallContacts = false, isUploadCallRecord = false;//上传状态：NOT_UPLOAD代表未上传，UPLOAD_FAIL代表上传失败，UPLOAD_SUCCESS代表上传成功
    private boolean mIsCallRecordPassByServer = false;
    private boolean mIsContactsPassByServer = false;
    private boolean mIsInitState = true;

    private TextView tv_quota;
    private TextView tv_max_quota;
    private TextView tv_repay_amount_month1;
    private MyGridView mgv_repay_time;
    private Button bt_apply_quota;
    private SeekBar sb_quota;
    private SelWithdrawalsBean selWithdrawalsBean = new SelWithdrawalsBean();
    private final String ISINIT = "1";
    private final String NOT_INIT = "0";
    private List<WithdrawalsItemBean> withdrawalsItemBeanList = new ArrayList<WithdrawalsItemBean>();
    private WithdrawalsTypeAdapter adapter;
    private int selectPosition = 0;
    private int quota = 0;
    private CashSubItemBean cashSubItemBean;
    private int default_unit = 1000;
    private int maxAmount = 0;
    private int defAmount = 0;
    private UploadToServerUtil uploadUtil;
    private LinearLayout ll_initial;
    private LinearLayout ll_not_initial;
    private TextView tv_arrival_amount, tv_repay_amount_month2, tv_repay_time;
    private RelativeLayout rl_min_max_quota;
    private WithdrawalsReciver mWithdrawalsReciver = new WithdrawalsReciver();
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view=super.onCreateView(inflater, container, savedInstanceState);
        int seekBarHeight= BitmapFactory.decodeResource(this.getResources(),R.mipmap.icon_movement).getHeight();
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin=seekBarHeight/2+20;
        rl_min_max_quota.setLayoutParams(params);
        return view;
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
                    initView();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
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
        String maxCash = selWithdrawalsBean.getMax_cash();
        if ("".equals(maxCash) || null == maxCash) {
            maxCash = "0";
        }
        maxAmount = Integer.valueOf(maxCash) / 100;
        tv_max_quota.setText(maxAmount + "");
        String defCash = selWithdrawalsBean.getDef_cash();
        if ("".equals(defCash) || null == defCash) {
            defCash = "0";
        }
        defAmount = Integer.valueOf(defCash) / 100;
        sb_quota.setMax(maxAmount);
        sb_quota.setProgress(defAmount);
        refreshView(selectPosition);
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
            if ((int) (withDrawalAmount / 100) == quota) {
                cashSubItemBean = cashSubItemBeanList.get(i);
                String repayTotal = cashSubItemBean.getRepay_total();
                if ("".equals(repayTotal) || null == repayTotal) {
                    repayTotal = "0";
                }
                if (mIsInitState) {
                    tv_repay_amount_month1.setText((int) (Double.valueOf(repayTotal) / 100) + "");
                } else {
                    String arrivalAmount = cashSubItemBean.getTransfer_amount();
                    if ("".equals(arrivalAmount) || null == arrivalAmount) {
                        arrivalAmount = "0";
                    }
                    tv_arrival_amount.setText((int) (Double.valueOf(arrivalAmount) / 100) + "");
                    tv_repay_time.setText(withdrawalsItemBeanList.get(selectPosition).getRepay_times());
                    tv_repay_amount_month2.setText((int) (Double.valueOf(repayTotal) / 100) + "");
                }
            }
        }
    }

    @Override
    protected void initVariable() {
        registerReceiver();
    }

    private boolean isInitState() {
        String balanceCashAmount = UserUtil.getCashBalanceAmount(mContext);
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

    private void init() {
        mIsInitState = isInitState();
        adapter = new WithdrawalsTypeAdapter(mContext, withdrawalsItemBeanList);
        mgv_repay_time.setAdapter(adapter);
        selWithdrawals();
        uploadUtil = new UploadToServerUtil(mContext);
        uploadUtil.setCallBack(this);
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

    public void freshBalanceAmount(boolean isShowLoadingDialog) {
        try {
            SignIn mSignIn = new SignIn(mContext);
            String device_id = UserUtil.getDeviceId(mContext);
            if (device_id == null || "".equals(device_id)) {
                return;
            }
            JSONObject json = new JSONObject();
            json.put("device_id", device_id);
            json.put("mobile", UserUtil.getMobile(mContext));
            final String password=UserUtil.getLoginPsw(mContext);
            json.put("password", password);
            String push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            json.put("push_id", push_id);
            mSignIn.signIn(json, null, isShowLoadingDialog, new BaseNetCallBack<SignInBean>() {
                @Override
                public void onSuccess(SignInBean paramT) {
                    UserUtil.setLoginPassword(mContext, password);
                    init();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Log.e("loginError", "errorType" + errorType + "  erroCode" + errorCode);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            init();
            isFirst = false;
        }
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
                tv_quota.setText("¥" + progress);
                quota = progress;
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
    public void onClick(View view) {
        if (!MainUtil.isLogin(mContext)) {
            gotoActivity(mContext, LoginActivity.class, null);
            return;
        }
        switch (view.getId()) {
            case R.id.bt_apply_quota:
                if (quota == 0) {
                    ToastUtil.showToast(mContext, "额度不可为0");
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
        switch (UserUtil.getCreditStep(mContext)) {
            case GlobalParams.USER_STATUS_NEW:
                // TODO 去扫描身份证，如果扫描不成功，就跳转到手动输入页面手动输入
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
                // TODO 直接去扫脸
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
            }
            break;
            case GlobalParams.HAVE_SCAN_FACE:// 已经扫脸通过
                if (mIsInitState) {
                    String cashAmount = UserUtil.getCashAmount(mContext);
                    if ("".equals(cashAmount) || null == cashAmount) {
                        cashAmount = "0";
                    }
                    if (Double.valueOf(cashAmount) > 0.0) {
                        ToastUtil.showToast(mContext, "您当前可用额度为0，可提现额度为0");
                        return;
                    }
                    gotoActivity(mContext, ImproveQuotaActivity.class, bundle);
                } else {
                    getBankInfo(bundle);
                }
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO: {
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
            }
            break;
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
                    newBundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_GET_CASH);
                    if (paramT.getData().size() > 0) {
                        newBundle.putString(GlobalParams.WITHDRAWALS_BANK_NAME_KEY, paramT.getData().get(0).getBank_name());
                        newBundle.putString(GlobalParams.WITHDRAWALS_CARD_NUM_KEY, paramT.getData().get(0).getCard_num());
                        newBundle.putString(GlobalParams.WITHDRAWALS_REPAY_ID_KEY, withdrawalsItemBeanList.get(selectPosition).getId());
                        newBundle.putString(GlobalParams.WITHDRAWALS_REPAY_TIMES_KEY, withdrawalsItemBeanList.get(selectPosition).getRepay_times());
                        gotoActivity(mContext, WithdrawalsConfirmActivity.class, newBundle);
                    } else {
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

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.PASS_CALL_RECORD_ACTION);
        intentFilter.addAction(GlobalParams.PASS_CONTACT_ACTION);
        intentFilter.addAction(GlobalParams.LOGIN_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.WITHDRAWALS_ORDER_SUCCESS_ACTION);
        intentFilter.addAction(GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION);
        intentFilter.addAction(GlobalParams.REPAY_WITHDRAWAL_SUCCESS_ACTION);
        mContext.registerReceiver(mWithdrawalsReciver, intentFilter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mWithdrawalsReciver);
        super.onDestroy();
    }


    private class WithdrawalsReciver extends BroadcastReceiver {
        //广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String topActivityName = Utils.getTopActivityName(getActivity());
            if (topActivityName.contains("MainActivity") && ((MainActivity) getActivity()) != null && 1 == ((MainActivity) getActivity()).getTopFragmentIndex()) {
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
            }
        }
    }
}
