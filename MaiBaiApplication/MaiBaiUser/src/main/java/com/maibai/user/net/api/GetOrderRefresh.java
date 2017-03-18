package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.OrderRefreshBean;
import com.maibai.user.model.VerifyStepBean;
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
import java.util.List;

/**
 * Created by m on 16-9-18.
 */
public class GetOrderRefresh extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetOrderRefresh(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getOrderRefreshURL();
    }

    public void getOrderRefresh(JSONObject jsonObject, final BaseNetCallBack<OrderRefreshBean> mOrderRefreshCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderRefreshCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderRefreshCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getOrderRefresh(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<OrderRefreshBean> mOrderRefreshCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderRefreshCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderRefreshCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<OrderRefreshBean> mOrderRefreshCallBack) {
        try {
            if (isRelease) {
                OrderRefreshBean mOrderRefreshBean = (OrderRefreshBean) GsonUtil.json2bean(result, OrderRefreshBean.class);
                mOrderRefreshCallBack.onSuccess(mOrderRefreshBean);
            } else {
                mOrderRefreshCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<OrderRefreshBean> mOrderRefreshCallBack) {
        try {
            if (isRelease) {
                mOrderRefreshCallBack.onFailure(result, errorType, errorCode);
            } else {
                mOrderRefreshCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private OrderRefreshBean test() {
        String displayArrty[] = {"您的服务单已提交成功，请耐心等待审核","审核图片已由业务代表辅助上传",
                "信审人员已经审核",
                "请由业务代表辅助上传提货单","请等待检查员确认放款"};
//        String errMsgArray[] = {"运营商信息没有收到，请业务代表辅助用链接重新认证", "初审图片不符合要求，请重新上传",
//                "您不符合征信要求，已经被信审核人员拒绝", "提货单图片不符合要求，请重新上传"};
        String nameArray[] = {"系统","业务代表","信审人员","业务代表","提单检查员"};
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

        OrderRefreshBean mOrderRefreshBean = new OrderRefreshBean();
        mOrderRefreshBean.setCode(0);
        mOrderRefreshBean.setMsg("GetOrderRefresh success");
        mOrderRefreshBean.getData().setStatus("5");
        mOrderRefreshBean.getData().setConsume_id("1000");
        mOrderRefreshBean.getData().setAmount("5000000");
        mOrderRefreshBean.getData().setMerchant_name("蓝筹名座测试店");
        mOrderRefreshBean.getData().setOrder_id("12345678910");
        mOrderRefreshBean.getData().setAdd_time("2016-12-27 15:40:30");
        int curStep = ((int) (Math.random() * 4) + 1);
        mOrderRefreshBean.getData().setCur_step(curStep+"");
        if (curStep == 2) {
            int errCode = ((int) (Math.random() * 4));
            mOrderRefreshBean.getData().setErr_code(errCode+"");
        } else if (curStep == 4) {
            mOrderRefreshBean.getData().setErr_code("3");
        }
        int random = ((int) (Math.random() * 3));
        if (random == 1) {
            mOrderRefreshBean.getData().setDiscount("1");
        } else {
            mOrderRefreshBean.getData().setDiscount("0");
        }
        List<VerifyStepBean> step = new ArrayList<VerifyStepBean>();
        for (int i = 0; i < 5; i++) {
            VerifyStepBean verifyStepBean = new VerifyStepBean();
            verifyStepBean.setDisplay(displayArrty[i]);
            verifyStepBean.setDate_time("2016-12-27 15:40:30");
            verifyStepBean.setOpr_name(nameArray[i]);
            step.add(verifyStepBean);
        }
        mOrderRefreshBean.getData().setStep(step);
        return mOrderRefreshBean;
    }
}
