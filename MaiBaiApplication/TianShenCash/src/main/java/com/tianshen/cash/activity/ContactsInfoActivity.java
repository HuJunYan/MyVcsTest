package com.tianshen.cash.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.UploadContactsInfo;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.PermissionUtils;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactsInfoActivity extends BaseActivity implements View.OnClickListener{
    private boolean isContactsHaveUpload = false;
    private MyEditText et_wechat_num,et_qq_num,et_address,et_company_name,et_company_phone,
            et_company_address,et_parent_name,et_parent_phone,et_parent_address,et_brothers_name,
            et_brothers_phone,et_friend_name,et_friend_num,et_colleague_name,et_colleague_num;
    private Button bt_next;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_contacts_info;
    }

    @Override
    protected void findViews() {
        et_wechat_num=(MyEditText)findViewById(R.id.et_wechat_num);
        et_address=(MyEditText)findViewById(R.id.et_address);
        et_company_name=(MyEditText)findViewById(R.id.et_company_name);
        et_company_phone=(MyEditText)findViewById(R.id.et_company_phone);
        et_parent_name=(MyEditText)findViewById(R.id.et_parent_name);
        et_parent_phone=(MyEditText)findViewById(R.id.et_parent_phone);
        et_parent_address=(MyEditText)findViewById(R.id.et_parent_address);
        et_brothers_name=(MyEditText)findViewById(R.id.et_brothers_name);
        et_brothers_phone=(MyEditText)findViewById(R.id.et_brothers_phone);
        et_qq_num=(MyEditText)findViewById(R.id.et_qq_num);
        et_company_address=(MyEditText)findViewById(R.id.et_company_address);
        et_friend_name=(MyEditText)findViewById(R.id.et_friend_name);
        et_friend_num=(MyEditText)findViewById(R.id.et_friend_num);
        et_colleague_name=(MyEditText)findViewById(R.id.et_colleague_name);
        et_colleague_num=(MyEditText)findViewById(R.id.et_colleague_num);
        bt_next=(Button)findViewById(R.id.bt_next);
    }

    @Override
    protected void setListensers() {
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                if (!isContactsHaveUpload) {
                    uploadContactsInfo();
                }
                break;
        }
    }

    private void uploadContactsInfo(){
        String wechatNum=et_wechat_num.getEditTextString().trim();
        String selfAddress=et_address.getEditTextString().trim();
        String companyName=et_company_name.getEditTextString().trim();
        String companyPhone=et_company_phone.getEditTextString().trim();
        String parentName=et_parent_name.getEditTextString().trim();
        String parentPhone=et_parent_phone.getEditTextString().trim();
        String parentAddress=et_parent_address.getEditTextString().trim();
        String brothersName=et_brothers_name.getEditTextString().trim();
        String brothersPhone=et_brothers_phone.getEditTextString().trim();
        String qqNum=et_qq_num.getEditTextString().trim();
        String companyAddress=et_company_address.getEditTextString().trim();
        String friendName=et_friend_name.getEditTextString().trim();
        String friendNum=et_friend_num.getEditTextString().trim();
        String colleagueName=et_colleague_name.getEditTextString().trim();
        String colleagueNum=et_colleague_num.getEditTextString().trim();
        String customId=UserUtil.getId(mContext);
        if("".equals(wechatNum)||"".equals(selfAddress)||"".equals(companyName)||"".equals(companyPhone)||"".equals(parentName)||"".equals(parentPhone)||"".equals(parentAddress)||
                "".equals(brothersName)||"".equals(brothersPhone)||"".equals(qqNum)||"".equals(companyAddress)||"".equals(friendName)||"".equals(friendNum)||"".equals(colleagueName)||
                "".equals(colleagueNum)){
            ToastUtil.showToast(mContext,"请填写完整");
            return;
        }
        try {
            UploadContactsInfo uploadContactsInfo=new UploadContactsInfo(mContext);
            JSONObject jsonObject=new JSONObject();
            if(!("".equals(customId)||null==customId)){
                jsonObject.put("customer_id", customId);
            }

            if(!("".equals(wechatNum)||null==wechatNum)){
                jsonObject.put("wechat",wechatNum);
            }
            if(!("".equals(selfAddress)||null==selfAddress)){
                jsonObject.put("user_address",selfAddress);
            }
            if(!("".equals(companyName)||null==companyName)){
                jsonObject.put("company_name",companyName);
            }
            if(!("".equals(companyPhone)||null==companyPhone)){
                jsonObject.put("company_phone",companyPhone);
            }
            if(!("".equals(parentName)||null==parentName)){
                jsonObject.put("parent_name",parentName);
            }
            if(!("".equals(parentPhone)||null==parentPhone)){
                jsonObject.put("parent_phone",parentPhone);
            }
            if(!("".equals(parentAddress)||null==parentAddress)){
                jsonObject.put("parent_address",parentAddress);
            }
            if(!("".equals(brothersName)||null==brothersName)){
                jsonObject.put("brothers_name",brothersName);
            }
            if(!("".equals(brothersPhone)||null==brothersPhone)){
                jsonObject.put("brothers_phone",brothersPhone);
            }
            if(!("".equals(qqNum)||null==qqNum)){
                jsonObject.put("qq_num",qqNum);
            }
            if(!("".equals(companyAddress)||null==companyAddress)){
                jsonObject.put("company_address",companyAddress);
            }
            if(!("".equals(friendName)||null==friendName)){
                jsonObject.put("friends_name",friendName);
            }
            if(!("".equals(friendNum)||null==friendNum)){
                jsonObject.put("friends_phone",friendNum);
            }
            if(!("".equals(colleagueName)||null==colleagueName)){
                jsonObject.put("colleague_name",colleagueName);
            }
            if(!("".equals(colleagueNum)||null==colleagueNum)){
                jsonObject.put("colleague_phone",colleagueNum);
            }

            uploadContactsInfo.uploadContactsInfo(jsonObject, bt_next, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    UserUtil.setCreditStep(mContext, GlobalParams.HAVE_UPLOAD_CONTACTS_INFO+"");
                    isContactsHaveUpload = true;
                    gotoTargetActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    private void gotoTargetActivity() {
        switch (UserUtil.getCreditStep(mContext)) {
            case GlobalParams.USER_STATUS_NEW:
                break;
            case GlobalParams.HAVE_UPLOAD_IDCARD_INFO: {
                // TODO 直接去扫脸
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
            }
            break;
            case GlobalParams.HAVE_SCAN_FACE:// 已经扫脸通过
                gotoActivity(mContext, ImproveQuotaActivity.class, mBundle);
                backActivity();
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO:
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        try {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    if (resultCode == 5) {
                        new PermissionUtils(mContext).showPermissionDialog(3);//摄像头权限
                        return;
                    }
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    } else {
                        mBundle.putBoolean(GlobalParams.IF_NEED_SCAN_FACE_KEY, true);
                        gotoActivity(mContext, ActivateTheQuotaActivity.class, mBundle);
                    }
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    if (resultCode == Activity.RESULT_OK) {
                        gotoTargetActivity();
                    }
                    break;
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
