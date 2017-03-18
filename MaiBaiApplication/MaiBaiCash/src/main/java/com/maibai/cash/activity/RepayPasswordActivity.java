package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.ConsumeDataBean;
import com.maibai.cash.model.ConsumeSmallBillBean;
import com.maibai.cash.model.InstallmentHistoryBean;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.model.SmallOrderItemBean;
import com.maibai.cash.model.WithdrawalsBillInfoBean;
import com.maibai.cash.model.WithdrawalsBillInfoItenBean;
import com.maibai.cash.model.WithdrawalsBillItemBean;
import com.maibai.cash.net.api.Repayment;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.CashBillListUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SendBroadCastUtil;
import com.maibai.cash.utils.SharedPreferencesUtil;
import com.maibai.cash.view.PasswordInputView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepayPasswordActivity extends BaseActivity implements View.OnClickListener {
    private final int DELAY_QUERY_ORDER_STATE = 1;
    private int mQueryTimes = 0;
    private Button bt_confirm;
    private PasswordInputView piv_password;
    private Bundle mBundle;
    private List<WithdrawalsBillInfoItenBean> mDetailList;
    private WithdrawalsBillInfoBean mWithdrawalsBillInfoBean;
    private List<WithdrawalsBillItemBean> withdrawalsBillItemBeanList;
    String from = "";
    private  List<SmallOrderItemBean>consumeSmallBillBeanList;
    private  ConsumeSmallBillBean consumeSmallBillBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            from = mBundle.getString(GlobalParams.REPAY_FROM_KEY);
            initData();
        }

    }

    private void initData() {
        switch (from) {
            case GlobalParams.REPAY_FROM_BORROW:
                withdrawalsBillItemBeanList = (List<WithdrawalsBillItemBean>) mBundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                break;
            case GlobalParams.REPAY_FROM_SHOUFU:
                consumeSmallBillBeanList=(List<SmallOrderItemBean>)mBundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                consumeSmallBillBean=(ConsumeSmallBillBean)mBundle.getSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY);
            case GlobalParams.REPAY_FROM_CONSUMPTION:
                consumeSmallBillBeanList=(List<SmallOrderItemBean>)mBundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                consumeSmallBillBean=(ConsumeSmallBillBean)mBundle.getSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY);
                break;
            case GlobalParams.REPAY_FROM_BORROW_DETAIL:
                mDetailList = (List<WithdrawalsBillInfoItenBean>) mBundle.getSerializable(GlobalParams.REPAY_BEAN_KEY);
                mWithdrawalsBillInfoBean = (WithdrawalsBillInfoBean) mBundle.getSerializable(GlobalParams.REPAY_BEAN_TOTAL_DATA_KEY);
                break;
        }

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_repay_password;
    }

    @Override
    protected void findViews() {
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        piv_password = (PasswordInputView) findViewById(R.id.piv_password);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            int type = 0;
            JSONObject mJson = new JSONObject();
            mJson.put("customer_id", UserUtil.getId(mContext));
            mJson.put("paytype", "1");
            mJson.put("pay_pass", piv_password.getText().toString());
            switch (from) {
                case GlobalParams.REPAY_FROM_SHOUFU: // 首付
                    mJson.put("type", "2");
                    mJson.put("amount", mBundle.get("down_payment"));
                    mJson.put("consume_id", mBundle.getString("consume_id"));
                    type = 0;
                    break;
                case GlobalParams.REPAY_FROM_CONSUMPTION: // 消费还款
                    mJson.put("type", "1");
                    List<ConsumeDataBean> consumeDataBeenList5 = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList5 = new ArrayList<InstallmentHistoryBean>();
                    ConsumeDataBean consumeDataBean5 = new ConsumeDataBean();
                    boolean isNextMonth="1".equals(consumeSmallBillBean.getRepay_type())?true:false;
                    if(!isNextMonth) {
                        type=2;
                        for (int i = 0; i < consumeSmallBillBeanList.size(); i++) {
                            if (consumeSmallBillBeanList.get(i).isChecked()) {
                                InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                                installmentHistoryBean.setId(consumeSmallBillBeanList.get(i).getId());
                                String principal = consumeSmallBillBeanList.get(i).getPrincipal();
                                if ("".equals(principal) || null == principal) {
                                    principal = "0";
                                }
                                String lateFee = consumeSmallBillBeanList.get(i).getLate_fee();
                                if ("".equals(lateFee) || null == lateFee) {
                                    lateFee = "0";
                                }
                                String repayData = consumeSmallBillBeanList.get(i).getRepay_date();
                                if (null == repayData) {
                                    repayData = "";
                                }
                                installmentHistoryBean.setAmount(principal);
                                installmentHistoryBean.setRepay_date(repayData);
                                installmentHistoryBean.setOverdue_amount(lateFee);
                                installmentHistoryBeanList5.add(installmentHistoryBean);
                            }
                        }
                        consumeDataBean5.setInstallment_history(installmentHistoryBeanList5);
                    }
                    else{
                        type=1;
                    }
                    consumeDataBean5.setRepay_date(isNextMonth?consumeSmallBillBeanList.get(0).getRepay_date():"");
                    consumeDataBean5.setAmount(isNextMonth?consumeSmallBillBeanList.get(0).getPrincipal():"");
                    consumeDataBean5.setOverdue_amount(isNextMonth?consumeSmallBillBeanList.get(0).getLate_fee():"");
                    consumeDataBean5.setConsume_id(consumeSmallBillBean.getConsume_id());
                    consumeDataBean5.setType(consumeSmallBillBean.getRepay_type());
                    consumeDataBeenList5.add(consumeDataBean5);
                    mJson.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataBeenList5)));
                    break;
                case GlobalParams.REPAY_FROM_BORROW:  // 提现记录还款
                    mJson.put("type", "1");
                    CashBillListUtil.sortByConsumeId(withdrawalsBillItemBeanList);
                    List<ConsumeDataBean> consumeDataList3 = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList3 = new ArrayList<InstallmentHistoryBean>();
                    String consumeId = "";
                    boolean isConsumeIdInit = false;
                    for (int i = 0; i < withdrawalsBillItemBeanList.size(); i++) {
                        if (!isConsumeIdInit) {
                            isConsumeIdInit = true;
                            consumeId = withdrawalsBillItemBeanList.get(i).getConsume_id();
                        }
                        if (consumeId.equals(withdrawalsBillItemBeanList.get(i).getConsume_id())) {
                            InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                            installmentHistoryBean.setId(withdrawalsBillItemBeanList.get(i).getBill_id());
                            String overdueAmount = withdrawalsBillItemBeanList.get(i).getLate_fee();
                            installmentHistoryBean.setOverdue_amount("".equals(overdueAmount)?"0":overdueAmount);
                            installmentHistoryBean.setAmount(withdrawalsBillItemBeanList.get(i).getRepay_money());
                            installmentHistoryBean.setRepay_date(withdrawalsBillItemBeanList.get(i).getRepay_date());
                            installmentHistoryBeanList3.add(installmentHistoryBean);
                        } else {
                            ConsumeDataBean consumeData3 = new ConsumeDataBean();
                            consumeData3.setConsume_id(consumeId);
                            consumeData3.setType("5");
                            consumeData3.setRepay_date("");
                            consumeData3.setAmount("");
                            consumeData3.setOverdue_amount("");
                            consumeData3.setInstallment_history(installmentHistoryBeanList3);
                            consumeDataList3.add(consumeData3);
                            consumeId = withdrawalsBillItemBeanList.get(i).getConsume_id();
                            installmentHistoryBeanList3=new ArrayList<InstallmentHistoryBean>();
                            InstallmentHistoryBean installmentHistoryBean = new InstallmentHistoryBean();
                            installmentHistoryBean.setId(withdrawalsBillItemBeanList.get(i).getBill_id());
                            String overdueAmount = withdrawalsBillItemBeanList.get(i).getLate_fee();
                            installmentHistoryBean.setOverdue_amount("".equals(overdueAmount)?"0":overdueAmount);
                            installmentHistoryBean.setAmount(withdrawalsBillItemBeanList.get(i).getRepay_money());
                            installmentHistoryBean.setRepay_date(withdrawalsBillItemBeanList.get(i).getRepay_date());
                            installmentHistoryBeanList3.add(installmentHistoryBean);
                        }
                        if(i==withdrawalsBillItemBeanList.size()-1){
                            ConsumeDataBean consumeData3 = new ConsumeDataBean();
                            consumeData3.setConsume_id(withdrawalsBillItemBeanList.get(i).getConsume_id());
                            consumeData3.setType("5");
                            consumeData3.setRepay_date("");
                            consumeData3.setAmount("");
                            consumeData3.setOverdue_amount("");
                            consumeData3.setInstallment_history(installmentHistoryBeanList3);
                            consumeDataList3.add(consumeData3);
                        }
                    }
                    type = 5;
                    mJson.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataList3)));
                    break;
                case GlobalParams.REPAY_FROM_BORROW_DETAIL: // 提现详情还款
                    mJson.put("type", "1");
                    mJson.put("repay_date", "");
                    mJson.put("amount", "");
                    mJson.put("overdue_amount", "");
                    List<ConsumeDataBean> consumeDataBeenList4 = new ArrayList<ConsumeDataBean>();
                    List<InstallmentHistoryBean> installmentHistoryBeanList4 = new ArrayList<InstallmentHistoryBean>();
                    for (int i = 0; i < mDetailList.size(); i++) {
                        if (mDetailList.get(i).isChecked()) {
                            InstallmentHistoryBean installmentHistoryBean4 = new InstallmentHistoryBean();
                            installmentHistoryBean4.setId(mDetailList.get(i).getId());
                            String principal = mDetailList.get(i).getPrincipal();
                            if ("".equals(principal) || null == principal) {
                                principal = "0";
                            }
                            String lateFee = mDetailList.get(i).getLate_fee();
                            if ("".equals(lateFee) || null == lateFee) {
                                lateFee = "0";
                            }
                            String repayData=mDetailList.get(i).getRepay_date();
                            if(null==repayData){
                                repayData="";
                            }
                            installmentHistoryBean4.setAmount(principal);
                            installmentHistoryBean4.setRepay_date(repayData);
                            installmentHistoryBean4.setOverdue_amount("".equals(lateFee)?"0":lateFee);
                            installmentHistoryBeanList4.add(installmentHistoryBean4);
                        }
                    }
                    ConsumeDataBean consumeDataBean4 = new ConsumeDataBean();
                    consumeDataBean4.setInstallment_history(installmentHistoryBeanList4);
                    consumeDataBean4.setConsume_id(mWithdrawalsBillInfoBean.getData().getConsume_id());
                    consumeDataBean4.setRepay_date("");
                    consumeDataBean4.setAmount("");
                    consumeDataBean4.setOverdue_amount("");
                    consumeDataBean4.setType("5");
                    consumeDataBeenList4.add(consumeDataBean4);
                    type = 5;
                    mJson.put("consume_data", new JSONArray(GsonUtil.bean2json(consumeDataBeenList4)));
                    break;
            }
            Repayment mRepayment = new Repayment(mContext);
            mRepayment.repayment(mJson, v, true,type,  new BaseNetCallBack<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean mResponseBean) {
                            switch (from) {
                                case GlobalParams.REPAY_FROM_CONSUMPTION:
                                    SharedPreferencesUtil.getInstance(mContext).putString(GlobalParams.IS_MY_PAGE_NEED_REFRESH, "need_refresh");
                                    gotoActivity(mContext, RepaySuccessActivity.class, null);
                                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REFRESH_HOME_PAGE_ACTION, null);
                                    mBundle.putBoolean("isFenqi", false);
                                    backActivity();
                                    break;
                                case GlobalParams.REPAY_FROM_BORROW:
                                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REPAY_WITHDRAWAL_SUCCESS_ACTION,null);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("amount", mBundle.getString("totalAmount"));
                                    bundle.putString("merchant", "趣提钱");
                                    bundle.putString("repay_type", "银行还款");
                                    bundle.putString("time", "12:00");
                                    bundle.putString("orderNum", "00000000");
                                    gotoActivity(mContext, BorrowRepayActivity.class, bundle);
                                    backActivity();
                                    break;
                                case GlobalParams.REPAY_FROM_BORROW_DETAIL:
                                    new SendBroadCastUtil(mContext).sendBroad(GlobalParams.REPAY_WITHDRAWAL_SUCCESS_ACTION,null);
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("amount", mBundle.getString("totalAmount"));
                                    bundle2.putString("merchant", "趣提钱");
                                    bundle2.putString("repay_type", "银行还款");
                                    bundle2.putString("time", "12:00");
                                    bundle2.putString("orderNum", "00000000");
                                    gotoActivity(mContext, BorrowRepayActivity.class, bundle2);
                                    backActivity();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {

                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }


}
