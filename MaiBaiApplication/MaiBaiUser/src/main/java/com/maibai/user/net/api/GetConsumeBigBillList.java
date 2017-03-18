package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ConsumeBigBillItemBean;
import com.maibai.user.model.ConsumeBigBillListBean;
import com.maibai.user.model.SmallOrderItemBean;
import com.maibai.user.model.SmallOrderItemTipDataItemBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by m on 16-11-7.
 */
public class GetConsumeBigBillList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetConsumeBigBillList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getConsumeBigBillListURL();
    }

    public void getConsumeBigBillList(JSONObject jsonObject, final BaseNetCallBack<ConsumeBigBillListBean> mConsumeBigBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeBigBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeBigBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getConsumeBigBillList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ConsumeBigBillListBean> mConsumeBigBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeBigBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeBigBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ConsumeBigBillListBean> mConsumeBigBillListCallBack) {
        try {
            if (isRelease) {
                ConsumeBigBillListBean mConsumeBigBillListBean = (ConsumeBigBillListBean) GsonUtil.json2bean(result, ConsumeBigBillListBean.class);
                for (int i = 0; i < mConsumeBigBillListBean.getData_big().size(); i++) {
                    mConsumeBigBillListBean.getData_big().get(i).setMerchant_logo(mConsumeBigBillListBean.getImg_url() + mConsumeBigBillListBean.getData_big().get(i).getMerchant_logo());
                }
                mConsumeBigBillListCallBack.onSuccess(mConsumeBigBillListBean);
            } else {
                mConsumeBigBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ConsumeBigBillListBean> mConsumeBigBillListCallBack) {
        try {
            if (isRelease) {
                mConsumeBigBillListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mConsumeBigBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ConsumeBigBillListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        ConsumeBigBillListBean mConsumeBigBillListBean = new ConsumeBigBillListBean();
        mConsumeBigBillListBean.setCode(0);
        mConsumeBigBillListBean.setMsg("GetConsumeBigBillList in success");
        mConsumeBigBillListBean.setData_big(new ArrayList<ConsumeBigBillItemBean>());
        int type = ((int) (Math.random() * 2)) + 1;
        mConsumeBigBillListBean.setType(type+"");
        if (type== 1) { // 大单数据
            int random = ((int) (Math.random() * 10)) + 3;
            for (int i = 0; i < random; i++) {
                ConsumeBigBillItemBean consumeBigBillItemBean = new ConsumeBigBillItemBean();
                consumeBigBillItemBean.setConsume_id("100" + i);
                int repay_type;
                if (i == 2) {
                    consumeBigBillItemBean.setStatus("4");
                    repay_type = 2;
                } else {
                    consumeBigBillItemBean.setStatus("1");
                    repay_type = (i % 2) + 1;
                }
                consumeBigBillItemBean.setMerchant_name("商户名称" + i);
                consumeBigBillItemBean.setMerchant_logo("http://pic.58pic.com/58pic/15/28/08/76X58PIC2UP_1024.jpg");
                consumeBigBillItemBean.setRepay_type(repay_type + "");
                consumeBigBillItemBean.setTotal_amount("200000");
                consumeBigBillItemBean.setRepay_amount("36000");
                consumeBigBillItemBean.setRepay_date("2016-10-11");
                consumeBigBillItemBean.setConsume_date("2016-08-01 18:22");
                consumeBigBillItemBean.setReal_pay("20000");
                int fineRandom = ((int) (Math.random() * 5));
                if (fineRandom == 0) {
                    consumeBigBillItemBean.setFine("5000");
                } else {
                    consumeBigBillItemBean.setFine("0");
                }
                consumeBigBillItemBean.setDiscount("10000");
                consumeBigBillItemBean.setRepay_now(i+"");
                consumeBigBillItemBean.setTotal_times("20");
                consumeBigBillItemBean.setCurrent_time("1");
                mConsumeBigBillListBean.getData_big().add(consumeBigBillItemBean);
            }
        } else if (type == 2) { // 小单数据
//            ConsumeSmallBillBean consumeSmallBillBean = new ConsumeSmallBillBean();
            mConsumeBigBillListBean.getData_small().setConsume_id("100");
            mConsumeBigBillListBean.getData_small().setMerchant_name("商户名称");
            mConsumeBigBillListBean.getData_small().setMerchant_logo("http://pic.58pic.com/58pic/15/28/08/76X58PIC2UP_1024.jpg");
            mConsumeBigBillListBean.getData_small().setConsume_date("2016-08-01 18:22");
            mConsumeBigBillListBean.getData_small().setAmount("200000");
            int downPayment = ((int) (Math.random() * 3));
            if (downPayment == 1) {
                mConsumeBigBillListBean.getData_small().setDown_payment("1000");
                mConsumeBigBillListBean.getData_small().setBorrow_amount("199000");
            } else {
                mConsumeBigBillListBean.getData_small().setDown_payment("0");
                mConsumeBigBillListBean.getData_small().setBorrow_amount("200000");
            }
            mConsumeBigBillListBean.getData_small().setRepay_amount("150000");
            mConsumeBigBillListBean.getData_small().setHave_repay("1000000");
            mConsumeBigBillListBean.getData_small().setSmall_order_list(new ArrayList<SmallOrderItemBean>());
            int random = ((int) (Math.random() * 3)) + 1;
            if (random == 1) {
                mConsumeBigBillListBean.getData_small().setRepay_type("1");
                SmallOrderItemBean smallOrderItemBean = new SmallOrderItemBean();
                smallOrderItemBean.setId("1100");
                smallOrderItemBean.setPrincipal("36000");
                smallOrderItemBean.setLate_fee("0");
                smallOrderItemBean.setTime("11-10待还");
                smallOrderItemBean.setState("2");
                smallOrderItemBean.setRepay_amount("36000");
                smallOrderItemBean.setRepay_date("2016-11-05");
                smallOrderItemBean.setNeed_pay("36000");
                smallOrderItemBean.setSave_amount("0");
                mConsumeBigBillListBean.getData_small().getSmall_order_list().add(smallOrderItemBean);
            } else {
                mConsumeBigBillListBean.getData_small().setRepay_type("2");
                int count = ((int) (Math.random() * 15)) + 4;
                for (int i = 0; i < count; i++) {
                    SmallOrderItemBean smallOrderItemBean = new SmallOrderItemBean();
                    smallOrderItemBean.setId("1100");
                    smallOrderItemBean.setPrincipal("36000");
                    if (i == 0) {
                        smallOrderItemBean.setLate_fee("0");
                        smallOrderItemBean.setTime("08-10已还");
                        smallOrderItemBean.setState("0");
                    } else if (i == 1) {
                        smallOrderItemBean.setLate_fee("10000");
                        smallOrderItemBean.setTime("逾期10天");
                        smallOrderItemBean.setState("1");
                    } else {
                        smallOrderItemBean.setLate_fee("0");
                        smallOrderItemBean.setTime("11-10待还");
                        smallOrderItemBean.setState("2");
                    }
                    smallOrderItemBean.setRepay_amount("36000");
                    smallOrderItemBean.setRepay_date("2016-11-05");
                    smallOrderItemBean.setNeed_pay((36000*(i+1))+"");
                    smallOrderItemBean.setSave_amount("0");
                    if (i == count-1) {
                        smallOrderItemBean.setSave_amount("10000");
                        smallOrderItemBean.setNeed_pay((36000*(i+1)-10000)+"");
                    }
                    smallOrderItemBean.setMoney_data(new ArrayList<SmallOrderItemTipDataItemBean>());
                    String[] nameArry = {"应付金额", "实付金额", "节省金额"};
                    String[] moneyArry = {"200000", "180000", "20000"};
                    for (int j = 0; j < 3; j++) {
                        SmallOrderItemTipDataItemBean smallOrderItemTipDataItemBean = new SmallOrderItemTipDataItemBean();
                        smallOrderItemTipDataItemBean.setName(nameArry[j]);
                        smallOrderItemTipDataItemBean.setMoney(moneyArry[j]);
                        smallOrderItemBean.getMoney_data().add(smallOrderItemTipDataItemBean);
                    }
                    mConsumeBigBillListBean.getData_small().getSmall_order_list().add(smallOrderItemBean);
                }
            }

        }


        return mConsumeBigBillListBean;
    }
}
