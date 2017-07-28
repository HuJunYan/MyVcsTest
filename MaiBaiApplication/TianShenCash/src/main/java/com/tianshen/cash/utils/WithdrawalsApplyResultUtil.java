package com.tianshen.cash.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.MainActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.CashVerifyConfirm;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/18.
 */

public class WithdrawalsApplyResultUtil {
    Context context;
    public WithdrawalsApplyResultUtil(Context context){
        this.context=context;
    }

    public void showSuccessDialog(String amount){
        final Dialog dialog = new AlertDialog.Builder(context, R.style.withdrawals_diaog).create();
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_withdrawals_success, null);
        view.getBackground().mutate().setAlpha(100);//0~255透明度值
        TextView tv_amount=(TextView)view.findViewById(R.id.tv_amount);
        TextView tv_amount_small=(TextView)view.findViewById(R.id.tv_amount_small);
        Button bt_get_money=(Button)view.findViewById(R.id.bt_get_money);
        tv_amount.setText(amount);
        tv_amount_small.setText(amount);
        bt_get_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashVerifyConfirm(dialog, v);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(context).changeDark();
    }



    public void showBorrowTerm(String amount){
        final Dialog dialog = new AlertDialog.Builder(context, R.style.withdrawals_diaog).create();
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_add_borrow_tem, null);
        view.getBackground().mutate().setAlpha(100);//0~255透明度值
        TextView tv_amount=(TextView)view.findViewById(R.id.tv_amount);
        TextView tv_amount_small=(TextView)view.findViewById(R.id.tv_amount_small);
        Button bt_get_money=(Button)view.findViewById(R.id.bt_get_money);
        tv_amount.setText(amount);
        tv_amount_small.setText(amount);
        bt_get_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashVerifyConfirm(dialog, v);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(context).changeDark();
    }
    private void cashVerifyConfirm(final Dialog dialog, View view){
        JSONObject jsonObject = new JSONObject();
        CashVerifyConfirm cashVerifyConfirm = new CashVerifyConfirm(context);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(context));
            cashVerifyConfirm.cashVerifyConfirm(jsonObject, view, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    new GetTelephoneUtils(context).changeLight();
                    dialog.dismiss();
                    UserUtil.setCashNeedPop(context, "0");
                    MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (Exception e) {

        }
    }

    public void showFailDialog(String content, final boolean isMachine){
        final Dialog dialog = new AlertDialog.Builder(context, R.style.withdrawals_diaog).create();
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_withdrawals_fail, null);
        view.getBackground().mutate().setAlpha(100);//0~255透明度值
        TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
        Button bt_confirm=(Button)view.findViewById(R.id.bt_confirm);
        tv_content.setText(content);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTelephoneUtils(context).changeLight();
                dialog.dismiss();
                if(isMachine){
                    return;
                }else{
                    MyApplicationLike.getMyApplicationLike().clearTempActivityInBackStack(MainActivity.class);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(context).changeDark();
    }
}
