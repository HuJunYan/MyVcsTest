package com.maibai.cash.jpushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.model.JpushAddBorrowTermBean;
import com.maibai.cash.model.JpushLotOfVerifyStatusBean;
import com.maibai.cash.model.OrderRefreshBean;
import com.maibai.cash.model.WithdrawalsRefreshBean;
import com.maibai.cash.net.api.JpushHandle;
import com.maibai.cash.net.base.JpushCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.service.UploadLogService;
import com.maibai.cash.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/13.
 */
public class JPushReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        LogUtil.d("JPush", JPushInterface.getRegistrationID(context) + "" + (UserUtil.getId(mContext) != null));
        LogUtil.d("JPush", JPushInterface.EXTRA_EXTRA);
        if (null != intent && null != intent.getAction()) {
            if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                if (UserUtil.getId(context) != null && !"".equals(UserUtil.getId(context))) {
                    try {
                        Bundle bundle = intent.getExtras();
                        JpushHandle mJpushHandle = new JpushHandle(mContext);
                        String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
                        LogUtil.d("ret", "jpushResult = " + result);
                        mJpushHandle.jpushHandle(result, new JpushCallBack() {
                            @Override
                            public void onResult(int type, Object object) {
                                switch (type) {
                                    case GlobalParams.VERIFY_FINISHED:
                                        sendVerifyFinishedBroadCast(GlobalParams.VERIFY_FINISHED_ACTION, (OrderRefreshBean)object);
                                        break;
                                    case GlobalParams.WITHDRAWALS_VERIFY_FINISHED:
                                        sendWithdrawalsVerifyFinishedBroadCast(GlobalParams.WITHDRAWALS_VERIFY_FINISHED_ACTION, (WithdrawalsRefreshBean)object);
                                        break;
                                    case GlobalParams.UPDATE_LOG_STATUS:
//                                        ToastUtil.showToast(mContext,"接收到请求，即将执行日志上传");
                                        UserUtil.setLogStatus(context,GlobalParams.LOG_STATUS_NEED_UPLOAD);
                                        uploadLog(context);
                                        break;
                                    case GlobalParams.A_LOT_OF_VERIFY: // 各种审核状态
                                        sendLotOfVerifyStatusBroadCast(GlobalParams.LOT_OF_VERIFY_STATUE_ACTION, (JpushLotOfVerifyStatusBean)object);
                                        break;
                                    case GlobalParams.ADD_BORROW_TERM: // 延长借款期限
                                        sendAddBorrowTermBroadCast(GlobalParams.ADD_BORROW_TERM_KEY_ACTION, (JpushAddBorrowTermBean)object);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });

                    } catch (Exception e) {
                        MobclickAgent.reportError(mContext, LogUtil.getException(e));
                        LogUtil.e("error", LogUtil.getException(e));
                    }
                }
            }
        }
    }

    private void uploadLog(Context context){
        if(GlobalParams.LOG_STATUS_NEED_UPLOAD.equals(UserUtil.getLogStatus(context))||GlobalParams.LOG_STATUS_IS_UPLOAD_fail.equals(UserUtil.getLogStatus(context))) {
            Intent intent = new Intent(context, UploadLogService.class);
            context.startService(intent);
        }
    }

    private void sendVerifyFinishedBroadCast(String action, OrderRefreshBean orderRefreshBean){
        Intent intent=new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalParams.VERIFY_FINISHED_KEY, orderRefreshBean);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }

    private void sendWithdrawalsVerifyFinishedBroadCast(String action, WithdrawalsRefreshBean withdrawalsRefreshBean){
        Intent intent=new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalParams.WITHDRAWALS_VERIFY_FINISH_KEY, withdrawalsRefreshBean);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }

    private void sendLotOfVerifyStatusBroadCast(String action, JpushLotOfVerifyStatusBean jpushLotOfVerifyStatusBean){
        Intent intent=new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalParams.LOT_OF_VERIFY_STATUE_KEY, jpushLotOfVerifyStatusBean);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }

    private void sendAddBorrowTermBroadCast(String action, JpushAddBorrowTermBean jpushAddBorrowTermBean){
        Intent intent=new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalParams.ADD_BORROW_TERM_KEY, jpushAddBorrowTermBean);
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }
}
