package com.tianshen.cash.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Bundle;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.AuthCreditActivity;
import com.tianshen.cash.activity.AuthMyInfoActivity;
import com.tianshen.cash.activity.EvaluateAmountActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.AuthCreditBean;
import com.tianshen.cash.model.RequiredBean;
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.net.api.GetCreditConf;
import com.tianshen.cash.net.api.GetUserAuthCenter;
import com.tianshen.cash.net.base.BaseNetCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 完成认证&提升额度的dialog工具类
 */

public class CashAmountDialogUtils {

    public interface CashAmountDialogCallBack {
        void onClickGetAmount();

        void onClickUpAmount();
    }

    public static void show(final Context context) {
        show(context, null);
    }

    public static void show(final Context context, final CashAmountDialogCallBack callback) {
        String userId = TianShenUserUtil.getUserId(context);
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);

            GetCreditConf getCreditConf = new GetCreditConf(context);
            getCreditConf.getData(jsonObject, new BaseNetCallBack<AuthCreditBean>() {
                @Override
                public void onSuccess(AuthCreditBean paramT) {

                    LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = mLayoutInflater.inflate(R.layout.dialog_cash_amount, null, false);
                    final Dialog mDialog = new Dialog(context, R.style.MyDialog);
                    int screenWidth = Utils.getWidthPixels(context);
                    mDialog.setContentView(view, new ViewGroup.LayoutParams(screenWidth * 7 / 9, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    TextView tv_auth_center_menu_dialog_get_amount = (TextView) view.findViewById(R.id.tv_auth_center_menu_dialog_get_amount);
                    TextView tv_auth_center_menu_dialog_up_amount = (TextView) view.findViewById(R.id.tv_auth_center_menu_dialog_up_amount);


                    ArrayList<RequiredBean> notRequired = paramT.getData().getNot_required();
                    boolean isShowUpAmount = false;
                    if (notRequired == null || notRequired.size() == 0) {
                        isShowUpAmount = false;
                    } else {
                        for (int i = 0; i < notRequired.size(); i++) {
                            RequiredBean requiredBean = notRequired.get(i);
                            String status = requiredBean.getStatus();
                            if ("0".equals(status)) {
                                isShowUpAmount = true;
                                break;
                            }
                        }
                    }
                    if (isShowUpAmount) {
                        tv_auth_center_menu_dialog_up_amount.setVisibility(View.VISIBLE);
                    } else {
                        tv_auth_center_menu_dialog_up_amount.setVisibility(View.GONE);
                    }

                    tv_auth_center_menu_dialog_get_amount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (callback != null) {
                                callback.onClickGetAmount();
                            }
                            Intent intent = new Intent(context, EvaluateAmountActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                        }
                    });
                    tv_auth_center_menu_dialog_up_amount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (callback != null) {
                                callback.onClickUpAmount();
                            }
                            Intent intent = new Intent(context, AuthCreditActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                        }
                    });
                    mDialog.show();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
