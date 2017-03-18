package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ConsumeSmallBillListBean;
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
public class GetConsumeSmallBillList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetConsumeSmallBillList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getConsumeSmallBillListURL();
    }

    public void getConsumeSmallBillList(JSONObject jsonObject, final BaseNetCallBack<ConsumeSmallBillListBean> mConsumeSmallBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeSmallBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeSmallBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getConsumeSmallBillList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ConsumeSmallBillListBean> mConsumeSmallBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeSmallBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeSmallBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ConsumeSmallBillListBean> mConsumeSmallBillListCallBack) {
        try {
            if (isRelease) {
                ConsumeSmallBillListBean mConsumeSmallBillListBean = (ConsumeSmallBillListBean) GsonUtil.json2bean(result, ConsumeSmallBillListBean.class);
//                for (int i = 0; i < mConsumeSmallBillListBean.getData().size(); i++) {
                    mConsumeSmallBillListBean.getData().setMerchant_logo(mConsumeSmallBillListBean.getImg_url() + mConsumeSmallBillListBean.getData().getMerchant_logo());
//                }
                mConsumeSmallBillListCallBack.onSuccess(mConsumeSmallBillListBean);
            } else {
                mConsumeSmallBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ConsumeSmallBillListBean> mConsumeSmallBillListCallBack) {
        try {
            if (isRelease) {
                mConsumeSmallBillListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mConsumeSmallBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ConsumeSmallBillListBean test() {
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
        String consume_id = "";
        try {
            consume_id = mJSONObject.getString("consume_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (consume_id == null || "".equals(consume_id)) {
            throw new RuntimeException("consume_id is null");
        }

        ConsumeSmallBillListBean mConsumeSmallBillListBean = new ConsumeSmallBillListBean();
        mConsumeSmallBillListBean.setCode(0);
        mConsumeSmallBillListBean.setMsg("GetConsumeSmallBillList in success");
//        mConsumeSmallBillListBean.setData_big(new ArrayList<ConsumeBigBillItemBean>());
//        ConsumeSmallBillBean consumeSmallBillBean = new ConsumeSmallBillBean();
        mConsumeSmallBillListBean.getData().setConsume_id("100");
        mConsumeSmallBillListBean.getData().setMerchant_name("商户名称");
        mConsumeSmallBillListBean.getData().setMerchant_logo("http://pic.58pic.com/58pic/15/28/08/76X58PIC2UP_1024.jpg");
        mConsumeSmallBillListBean.getData().setConsume_date("2016-08-01 18:22");
        mConsumeSmallBillListBean.getData().setAmount("200000");
        int downPayment = ((int) (Math.random() * 3));
        if (downPayment == 1) {
            mConsumeSmallBillListBean.getData().setDown_payment("1000");
            mConsumeSmallBillListBean.getData().setBorrow_amount("199000");
        } else {
            mConsumeSmallBillListBean.getData().setDown_payment("0");
            mConsumeSmallBillListBean.getData().setBorrow_amount("200000");
        }
        mConsumeSmallBillListBean.getData().setRepay_amount("150000");
        mConsumeSmallBillListBean.getData().setHave_repay("1000000");
        mConsumeSmallBillListBean.getData().setSmall_order_list(new ArrayList<SmallOrderItemBean>());
        int random = ((int) (Math.random() * 3)) + 1;
        if (random == 1) {
            mConsumeSmallBillListBean.getData().setRepay_type("1");
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
            mConsumeSmallBillListBean.getData().getSmall_order_list().add(smallOrderItemBean);
        } else {
            mConsumeSmallBillListBean.getData().setRepay_type("2");
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
                mConsumeSmallBillListBean.getData().getSmall_order_list().add(smallOrderItemBean);
            }
        }


        return mConsumeSmallBillListBean;
    }
}
