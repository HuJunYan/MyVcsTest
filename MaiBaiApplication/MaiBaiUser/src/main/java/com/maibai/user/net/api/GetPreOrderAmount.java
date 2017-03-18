package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.InstallmentInfoItemBean;
import com.maibai.user.model.PreOrderAmountBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-9-18.
 */
public class GetPreOrderAmount extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetPreOrderAmount(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getPreOrderAmountURL();
    }

    public void getPreOrderAmount(JSONObject jsonObject, final BaseNetCallBack<PreOrderAmountBean> mPreOrderAmountCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mPreOrderAmountCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mPreOrderAmountCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getPreOrderAmount(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<PreOrderAmountBean> mPreOrderAmountCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mPreOrderAmountCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mPreOrderAmountCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<PreOrderAmountBean> mPreOrderAmountCallBack) {
        try {
            if (isRelease) {
                PreOrderAmountBean mPreOrderAmountBean = (PreOrderAmountBean) GsonUtil.json2bean(result, PreOrderAmountBean.class);
                mPreOrderAmountBean.getData().setMerchant_logo(mPreOrderAmountBean.getImg_url() + mPreOrderAmountBean.getData().getMerchant_logo());
                mPreOrderAmountCallBack.onSuccess(mPreOrderAmountBean);
            } else {
                mPreOrderAmountCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PreOrderAmountBean> mPreOrderAmountCallBack) {
        try {
            if (isRelease) {
                mPreOrderAmountCallBack.onFailure(result, errorType, errorCode);
            } else {
                mPreOrderAmountCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private PreOrderAmountBean test() {
        boolean isInit = false;
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }

        String is_init = "";
        try {
            is_init = mJSONObject.getString("is_init");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (is_init == null || "".equals(is_init)) {
            throw new RuntimeException("is_init is null");
        }
        if ("1".equals(is_init)) {
            isInit = true;
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

        String merchant_id = "";
        try {
            merchant_id = mJSONObject.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (merchant_id == null || "".equals(merchant_id)) {
            throw new RuntimeException("merchant_id is null");
        }

        String amount = "";
        String type = "";
        String down_type = "";
        String down_payment = "";

        if (!isInit) {
            try {
                amount = mJSONObject.getString("amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (amount == null || "".equals(amount)) {
                throw new RuntimeException("amount is null");
            }

            try {
                type = mJSONObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type == null || "".equals(type)) {
                throw new RuntimeException("type is null");
            }

            try {
                down_type = mJSONObject.getString("down_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (down_type == null || "".equals(down_type)) {
                throw new RuntimeException("down_type is null");
            }

            try {
                down_payment = mJSONObject.getString("down_payment");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (down_payment == null || "".equals(down_payment)) {
                throw new RuntimeException("down_payment is null");
            }
        }

        double[] interestRate = {0, 0.020, 0.022, 0.024, 0.026, 0.028, 0.030, 0.032};
        int[] repayTimes = {1, 3, 6, 10, 15, 18, 24, 36};

        PreOrderAmountBean mPreOrderAmountBean = new PreOrderAmountBean();
        mPreOrderAmountBean.setCode(0);
        mPreOrderAmountBean.setMsg("GetPreOrderAmount success");
        double amountDouble = 0;
        if (!isInit) {
            double consumeAmountDouble = (Double.parseDouble(amount));
            double firstOrderDouble = consumeAmountDouble * 0.002;
            double fullReduceDouble = consumeAmountDouble * 0.001;
            double rebateAmountDouble = consumeAmountDouble * 0.003;
            double discountAmountDouble = Math.ceil(firstOrderDouble + fullReduceDouble + rebateAmountDouble);
            double downPaymentDouble = consumeAmountDouble * 0.1;
            amountDouble = Math.ceil(consumeAmountDouble - downPaymentDouble - discountAmountDouble);
            mPreOrderAmountBean.getData().setType(type);
            mPreOrderAmountBean.getData().setDiscount_first_order((long) firstOrderDouble + "");
            mPreOrderAmountBean.getData().setDiscount_full_reduce((long) fullReduceDouble + "");
            mPreOrderAmountBean.getData().setDiscount_rebate_amount((long) rebateAmountDouble + "");
            mPreOrderAmountBean.getData().setDiscount_amount((long) discountAmountDouble + "");
            mPreOrderAmountBean.getData().setDown_payment((long) downPaymentDouble + "");
            mPreOrderAmountBean.getData().setAmount((long) amountDouble + "");
            mPreOrderAmountBean.getData().setMerchant_name("商户名称");
            mPreOrderAmountBean.getData().setMerchant_logo("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1474966898&di=72a3ab7c5a877b2a9681b32216f7e4ea&src=http://img1.50tu.com/meinv/xinggan/2013-08-24/6ccf24e8f65e490aa868196398d34ca6.jpg");
        }

        int random = ((int) (Math.random() * 6)) + 1;
        for (int i = 0; i < random; i++) {
            InstallmentInfoItemBean installmentInfoItemBean = new InstallmentInfoItemBean();
            installmentInfoItemBean.setRepay_times(repayTimes[i] + "");
            if (isInit) {
                installmentInfoItemBean.setRepay_total("0");
            } else {
                installmentInfoItemBean.setRepay_total((long) (Math.ceil(amountDouble * (1 + interestRate[i]) / repayTimes[i])) + "");
            }
            mPreOrderAmountBean.getData().getInstallment_info().add(installmentInfoItemBean);
        }

        return mPreOrderAmountBean;
    }
}
