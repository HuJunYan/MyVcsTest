package com.tianshen.cash.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.UserRepayDetailBean;
import com.tianshen.cash.net.api.UserRepayDetailApi;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.MoneyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/24.
 */

public class RepayDetailDialogView {

    public RepayDetailDialogView(final Activity activity, String userId, String consume_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put(GlobalParams.CONSUME_ID, consume_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserRepayDetailApi userRepayDetailApi = new UserRepayDetailApi(activity);
        userRepayDetailApi.getUserRepayDetail(jsonObject, new BaseNetCallBack<UserRepayDetailBean>() {
            @Override
            public void onSuccess(UserRepayDetailBean paramT) {
                Dialog dialog = new Dialog(activity, R.style.MyDialog);
                View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_repay_detail, null);
                dialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = DensityUtil.dp2px(300);
                contentView.setLayoutParams(layoutParams);
                dialog.getWindow().setGravity(Gravity.CENTER);
                if (!activity.isFinishing()) {
                    dialog.show();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });

    }

}
