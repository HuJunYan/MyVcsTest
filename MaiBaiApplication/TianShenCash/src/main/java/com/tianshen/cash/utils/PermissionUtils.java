package com.tianshen.cash.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.adapter.PermissionDialogAdapter;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.PermissionHintBean;
import com.tianshen.cash.model.PermissionHintItemBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.GetPermissionHint;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.UserUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class PermissionUtils {
    Context mContext;


    public PermissionUtils(Context context) {
        this.mContext = context;
    }

    public void showPermissionDialog(final int type){
        GetPermissionHint getPermissionHint=new GetPermissionHint(mContext);
        try{
            JSONObject json=new JSONObject();
            json.put("customer_id", UserUtil.getId(mContext));
            json.put("permission_type",type+"");
            json.put("device_id",UserUtil.getDeviceId(mContext));
            json.put("device_os","android");
            json.put("device_os_version", Build.VERSION.RELEASE);
            json.put("device_mode", Build.MODEL);
            json.put("device_brand", Build.MANUFACTURER);
            getPermissionHint.getPermissionHint(json, new BaseNetCallBack< PermissionHintBean>(){
                @Override
                public void onSuccess(PermissionHintBean paramT) {
                    if (paramT.getData().getOptions().size() == 0) {
                        sendPassBoardCast(type);
                    } else {
                        Log.d("ret", "showPermissionDialog=======");
                        showOpenTipDialog(paramT);
                    }
                }
                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    ResponseBean responseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                    showPermissionWithoutStep(responseBean.getMsg());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showOpenTipDialog(PermissionHintBean paramT) {
        List<PermissionHintItemBean> list = paramT.getData().getOptions();
        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.permission_without_step_dialog).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_permission, null);
        view.getBackground().setAlpha(100);//0~255透明度值
        ListView listView = (ListView) view.findViewById(R.id.list_dialog_item);
        listView.setDivider(null);
        PermissionDialogAdapter adapter = new PermissionDialogAdapter(mContext, list);
        listView.setClickable(false);
        listView.setAdapter(adapter);
        ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTelephoneUtils(mContext).changeLight();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(mContext).changeDark();
    }

    private void sendPassBoardCast(int type) {
        Intent intent = new Intent();
        if (type == 0) {
            intent.setAction(GlobalParams.PASS_CALL_RECORD_ACTION);
        } else if (type == 1){
            intent.setAction(GlobalParams.PASS_CONTACT_ACTION);
        }
        mContext.sendBroadcast(intent);
    }

    public void showPermissionWithoutStep(String message){
        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.permission_without_step_dialog).create();
        View view= LayoutInflater.from(mContext).inflate(R.layout.view_dialog_permission_without_step, null);
        view.getBackground().setAlpha(100);//0~255透明度值
        Button bt_cancel=(Button)view.findViewById(R.id.bt_cancel);
        TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
        if(!"".equals(message)&&message!=null){
            tv_content.setText(message);
        }
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTelephoneUtils(mContext). changeLight();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        new GetTelephoneUtils(mContext).changeDark();
    }

}
