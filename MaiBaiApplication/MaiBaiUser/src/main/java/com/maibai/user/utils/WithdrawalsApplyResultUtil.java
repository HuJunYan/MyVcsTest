package com.maibai.user.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.activity.MainActivity;
import com.maibai.user.base.MyApplication;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.CashVerifyConfirm;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;

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
        view.getBackground().setAlpha(100);//0~255透明度值
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
            jsonObject.put("customer_id", UserUtil.getId(context));
            cashVerifyConfirm.cashVerifyConfirm(jsonObject, view, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    new GetTelephoneUtils(context).changeLight();
                    dialog.dismiss();
                    UserUtil.setCashNeedPop(context, "0");
                    (((MyApplication)(context.getApplicationContext())).getApplication()).clearTempActivityInBackStack(MainActivity.class);
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
        view.getBackground().setAlpha(100);//0~255透明度值
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
                    (((MyApplication)(context.getApplicationContext())).getApplication()).clearTempActivityInBackStack(MainActivity.class);
                   /* ((Activity)context).finish();
                    ((Activity) context). overridePendingTransition(R.anim.not_exit_push_left_in, R.anim.push_right_out);*/
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(context).changeDark();
    }
}
