package com.maibai.user.utils;

import android.content.Context;

import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ContactsBean;
import com.maibai.user.model.LastSaveCallRecordTimeBean;
import com.maibai.user.model.RecordEntityBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.model.SMSMessageBean;
import com.maibai.user.model.SaveCallRecordBean;
import com.maibai.user.net.api.GetLastSaveCallRecordTime;
import com.maibai.user.net.api.SaveCallRecord;
import com.maibai.user.net.api.SaveContacts;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.UserUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class UploadToServerUtil {
    private Context mContext;
    private Long lastSaveTime = 0l;
    private UploadCallBack uploadCallBack;
    public UploadToServerUtil(Context mContext) {
        this.mContext = mContext;
    }
    public void uploadUserInfo(int type){
        //上传通讯录
        if(type==GlobalParams.UPLOADCALLCONTACTS){
            uploadCallContacts();
        } else {
            if(0l==lastSaveTime){
                initLastSaveCallRecordTime(type);
            } else{
                if(type==GlobalParams.UPLOADCALLRECORD){
                    uploadCallRecord(lastSaveTime);
                } else if(type==GlobalParams.UPLOADMESSAGE){
                    uploadMessage(lastSaveTime);
                }
            }
        }
    }
    public void setCallBack(UploadCallBack uploadCallBack){
        this.uploadCallBack=uploadCallBack;
    }
    /*
     × 获取服务器中存储的上次通话记录的最近时间
     */
    public void initLastSaveCallRecordTime(final int type) {
        GetLastSaveCallRecordTime getLastSaveCallRecordTime = new GetLastSaveCallRecordTime(mContext);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            getLastSaveCallRecordTime.getLastSaveCallRecordTime(jsonObject, new BaseNetCallBack<LastSaveCallRecordTimeBean>() {
                @Override
                public void onSuccess(LastSaveCallRecordTimeBean lastSaveCallRecordTimeBean) {
                   String mLastSaveTime = lastSaveCallRecordTimeBean.getData().getLast_save_time();// 上次保存通话记录的时间，Unix 时间戳
                     lastSaveTime=Long.valueOf(mLastSaveTime, 10);
                    switch (type){
                        case GlobalParams.UPLOADCALLRECORD:

                            uploadCallRecord(lastSaveTime);
                            break;
                        case GlobalParams.UPLOADMESSAGE:
                            uploadMessage(lastSaveTime);
                            break;
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void uploadCallContacts(){
        List<ContactsBean> list = new GetTelephoneUtils(mContext).getPhoneContacts();
        if (list.size() == 0) {
            uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADCALLCONTACTS);
            return;
        }
        SaveContacts mSaveContactsAction = new SaveContacts(mContext);
        JSONObject json = new JSONObject();
        try {
            json.put("customer_id", UserUtil.getId(mContext));
            json.put("contact_list", new JSONArray(GsonUtil.bean2json(list)));
            mSaveContactsAction.saveContacts(json, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    uploadCallBack.uploadSuccessCallBack(GlobalParams.UPLOADCALLCONTACTS);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADCALLCONTACTS);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }
    public void uploadCallRecord(Long mLastSaveTime) {
        //根据时间戳从手机获取到相应的通话记录并上传
        int isPermissionOpen = 0;
        try {
            GetTelephoneUtils telephoneUtils = new GetTelephoneUtils(mContext);
            isPermissionOpen = 1;
            JSONObject callRecordJson = telephoneUtils.getCallRecordJsonObject(mLastSaveTime);
            isPermissionOpen = 2;
            if (callRecordJson == null) {
                return;
            }
            JSONArray jsonArray = callRecordJson.getJSONArray("record_list");
            if (jsonArray.length() == 0) {
                uploadCallBack.uploadSuccessCallBack(GlobalParams.UPLOADCALLRECORD);
            }else{
                List<RecordEntityBean> recordList = telephoneUtils.getCallRecord();
                if (recordList != null && recordList.size() > 0) {
                    SaveCallRecord saveCallRecord = new SaveCallRecord(mContext);
                    saveCallRecord.saveCallRecord(callRecordJson, null, false, new BaseNetCallBack<SaveCallRecordBean>() {
                        @Override
                        public void onSuccess(SaveCallRecordBean saveCallRecordBean) {
                            uploadCallBack.uploadSuccessCallBack(GlobalParams.UPLOADCALLRECORD);
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {
                            uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADCALLRECORD);
                        }
                    });
                } else {
                    uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADCALLRECORD);
                }

            }

        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
            if (isPermissionOpen == 1) {
                new PermissionUtils(mContext).showPermissionDialog(0);//通话记录
            }
        }
    }

    private void uploadMessage(Long mLastSaveTime){

        int isPermissionOpen = 0;
        try {
            GetTelephoneUtils telephoneUtils = new GetTelephoneUtils(mContext);
            isPermissionOpen = 1;
            JSONObject messageJson = telephoneUtils.getLatestSMSMessageObject(mLastSaveTime);
            isPermissionOpen = 2;
            if (messageJson == null) {
                uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADMESSAGE);
                return;
            }
            JSONArray jsonArray = messageJson.getJSONArray("message_list");
            if (jsonArray.length() == 0) {
                List<SMSMessageBean> recordList = telephoneUtils.getSMSFromPhone();
                if (recordList != null && recordList.size() > 0) {
                    SaveCallRecord saveCallRecord = new SaveCallRecord(mContext);
                    saveCallRecord.saveCallRecord(messageJson, null, false, new BaseNetCallBack<SaveCallRecordBean>() {
                        @Override
                        public void onSuccess(SaveCallRecordBean saveCallRecordBean) {
                            uploadCallBack.uploadSuccessCallBack(GlobalParams.UPLOADMESSAGE);
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {
                            uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADMESSAGE);
                        }
                    });
                } else {
                    uploadCallBack.uploadFailCallBack(GlobalParams.UPLOADMESSAGE);
                    return;
                }

            }

        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
            if (isPermissionOpen == 1) {
                new PermissionUtils(mContext).showPermissionDialog(0);//通话记录
            }
        }
    }
    public interface UploadCallBack{
        void uploadSuccessCallBack(int type);
        void uploadFailCallBack(int type);
    }
}
