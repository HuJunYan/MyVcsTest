package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ContactsInfoBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.ChangeContactsInfo;
import com.tianshen.cash.net.api.GetContactsInfo;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.ImageTextView;
import com.tianshen.cash.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener, TitleBar.TitleBarListener {
    private ImageTextView itv_mobile, itv_user_name, itv_id_num, itv_wechat, itv_address, itv_company_name,
            itv_company_phone, itv_parent_name, itv_parent_mobile, itv_parent_address, itv_brother_name,
            itv_brother_mobile,itv_company_address,itv_friend_name,itv_friend_mobile,itv_colleague_name,
            itv_colleague_mobile,itv_qq;
    private TitleBar tb_title;
    private ContactsInfoBean contactsInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tb_title.setIvRightVisible(View.GONE);
        initData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void findViews() {
        tb_title = (TitleBar) findViewById(R.id.tb_title);
        itv_mobile = (ImageTextView) findViewById(R.id.itv_mobile);
        itv_user_name = (ImageTextView) findViewById(R.id.itv_user_name);
        itv_id_num = (ImageTextView) findViewById(R.id.itv_id_num);
        itv_wechat = (ImageTextView) findViewById(R.id.itv_wechat);
        itv_address = (ImageTextView) findViewById(R.id.itv_address);
        itv_company_name = (ImageTextView) findViewById(R.id.itv_company_name);
        itv_company_phone = (ImageTextView) findViewById(R.id.itv_company_phone);
        itv_parent_name = (ImageTextView) findViewById(R.id.itv_parent_name);
        itv_parent_mobile = (ImageTextView) findViewById(R.id.itv_parent_mobile);
        itv_parent_address = (ImageTextView) findViewById(R.id.itv_parent_address);
        itv_brother_name = (ImageTextView) findViewById(R.id.itv_brother_name);
        itv_brother_mobile = (ImageTextView) findViewById(R.id.itv_brother_mobile);

        itv_company_address = (ImageTextView) findViewById(R.id.itv_company_address);
        itv_friend_name = (ImageTextView) findViewById(R.id.itv_friend_name);
        itv_friend_mobile = (ImageTextView) findViewById(R.id.itv_friend_mobile);
        itv_colleague_name = (ImageTextView) findViewById(R.id.itv_colleague_name);
        itv_colleague_mobile = (ImageTextView) findViewById(R.id.itv_colleague_mobile);
        itv_qq = (ImageTextView) findViewById(R.id.itv_qq);

    }

    @Override
    protected void setListensers() {
        tb_title.setListener(this);
        itv_wechat.setOnClickListener(this);
        itv_address.setOnClickListener(this);
        itv_company_name.setOnClickListener(this);
        itv_company_phone.setOnClickListener(this);
        itv_parent_name.setOnClickListener(this);
        itv_parent_mobile.setOnClickListener(this);
        itv_parent_address.setOnClickListener(this);
        itv_brother_name.setOnClickListener(this);
        itv_brother_mobile.setOnClickListener(this);
        itv_company_address.setOnClickListener(this);
        itv_friend_name.setOnClickListener(this);
        itv_friend_mobile.setOnClickListener(this);
        itv_colleague_name.setOnClickListener(this);
        itv_colleague_mobile.setOnClickListener(this);
        itv_qq.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GlobalParams.CHANGE_RESULT_OK) {
            tb_title.setIvRightVisible(View.VISIBLE);
            updateData(requestCode, data);
        }
    }

    private void initData() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            GetContactsInfo getContactsInfo = new GetContactsInfo(mContext);
            getContactsInfo.getContactsInfo(jsonObject, null, true, new BaseNetCallBack<ContactsInfoBean>() {
                @Override
                public void onSuccess(ContactsInfoBean paramT) {
                    contactsInfoBean = paramT;
                    initView();
                    setViewClickable();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    setItemClickable(false);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        itv_mobile.setRightText(formatText(contactsInfoBean.getData().getUser_mobile(), 4, 7, 4));
        itv_user_name.setRightText(formatName(contactsInfoBean.getData().getUser_name()));
        itv_id_num.setRightText(formatText(contactsInfoBean.getData().getId_card_num(), 7, 14, 8));
        itv_wechat.setRightText(formatLongText(contactsInfoBean.getData().getWechat(), 12));
        itv_address.setRightText(formatLongText(contactsInfoBean.getData().getUser_address(), 12));
        itv_company_name.setRightText(formatLongText(contactsInfoBean.getData().getCompany_name(), 12));
        itv_company_phone.setRightText(formatLongText(contactsInfoBean.getData().getCompany_phone(), 12));
        itv_parent_name.setRightText(formatName(contactsInfoBean.getData().getParent_name()));
        itv_parent_mobile.setRightText(formatText(contactsInfoBean.getData().getParent_phone(), 4, 7, 4));
        itv_parent_address.setRightText(formatLongText(contactsInfoBean.getData().getParent_address(), 12));
        itv_brother_name.setRightText(formatName(contactsInfoBean.getData().getBrothers_name()));
        itv_brother_mobile.setRightText(formatText(contactsInfoBean.getData().getBrothers_phone(), 4, 7, 4));
        itv_company_address.setRightText(formatLongText(contactsInfoBean.getData().getCompany_address(), 12));
        itv_friend_name.setRightText(formatName(contactsInfoBean.getData().getFriends_name()));
        itv_friend_mobile.setRightText(formatText(contactsInfoBean.getData().getFriends_phone(), 4, 7, 4));
        itv_colleague_name.setRightText(formatName(contactsInfoBean.getData().getColleague_name()));
        itv_colleague_mobile.setRightText(formatText(contactsInfoBean.getData().getColleague_phone(), 4, 7, 4));
        itv_qq.setRightText(formatLongText(contactsInfoBean.getData().getQq_num(), 12));

        String isShowRtImg=contactsInfoBean.getData().getIs_can_change();
        if(!(GlobalParams.IS_CAN_CHANGE.equals(isShowRtImg))){
            setRtShow(View.GONE);
        }else{
            setRtShow(View.VISIBLE);
        }
    }
    private void setRtShow(int isShow){
        itv_wechat.setRtVisibility(isShow);
        itv_address.setRtVisibility(isShow);
        itv_company_name.setRtVisibility(isShow);
        itv_company_phone.setRtVisibility(isShow);
        itv_parent_name.setRtVisibility(isShow);
        itv_parent_mobile.setRtVisibility(isShow);
        itv_parent_address.setRtVisibility(isShow);
        itv_brother_name.setRtVisibility(isShow);
        itv_brother_mobile.setRtVisibility(isShow);
        itv_company_address.setRtVisibility(isShow);
        itv_friend_name.setRtVisibility(isShow);
        itv_friend_mobile.setRtVisibility(isShow);
        itv_colleague_name.setRtVisibility(isShow);
        itv_colleague_mobile.setRtVisibility(isShow);
        itv_qq.setRtVisibility(isShow);
    }

    private void setViewClickable() {
        if(GlobalParams.IS_CAN_CHANGE.equals(contactsInfoBean.getData().getIs_can_change())){
            setItemClickable(true);
        }else{
            setItemClickable(false);
        }
    }

    private void setItemClickable(boolean iscanClick){
        itv_wechat.setClickable(iscanClick);
        itv_address.setClickable(iscanClick);
        itv_company_name.setClickable(iscanClick);
        itv_company_phone.setClickable(iscanClick);
        itv_parent_name.setClickable(iscanClick);
        itv_parent_mobile.setClickable(iscanClick);
        itv_parent_address.setClickable(iscanClick);
        itv_brother_name.setClickable(iscanClick);
        itv_brother_mobile.setClickable(iscanClick);
        itv_company_address.setClickable(iscanClick);
        itv_friend_name.setClickable(iscanClick);
        itv_friend_mobile.setClickable(iscanClick);
        itv_colleague_name.setClickable(iscanClick);
        itv_colleague_mobile.setClickable(iscanClick);
        itv_qq.setClickable(iscanClick);
    }
    private void updateData(int requestCode, Intent data) {
        Bundle bundle = data.getExtras();
        if (null == bundle) {
            return;
        }
        String changedText = bundle.getString(GlobalParams.CHANGE_INFO_KEY);
        if (null == changedText) {
            changedText = "";
        }
        switch (requestCode) {
            case GlobalParams.CHANGE_TYPE_WECHAT:
                contactsInfoBean.getData().setWechat(changedText);
                itv_wechat.setRightText(formatLongText(contactsInfoBean.getData().getWechat(), 12));
                break;
            case GlobalParams.CHANGE_TYPE_ADDRESS:
                contactsInfoBean.getData().setUser_address(changedText);
                itv_address.setRightText(formatLongText(contactsInfoBean.getData().getUser_address(), 12));
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_NAME:
                contactsInfoBean.getData().setCompany_name(changedText);
                itv_company_name.setRightText(formatLongText(contactsInfoBean.getData().getCompany_name(), 12));
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_PHONE:
                contactsInfoBean.getData().setCompany_phone(changedText);
                itv_company_phone.setRightText(formatLongText(contactsInfoBean.getData().getCompany_phone(), 12));
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_NAME:
                contactsInfoBean.getData().setParent_name(changedText);
                itv_parent_name.setRightText(formatName(contactsInfoBean.getData().getParent_name()));
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_MOBILE:
                contactsInfoBean.getData().setParent_phone(changedText);
                itv_parent_mobile.setRightText(formatText(contactsInfoBean.getData().getParent_phone(), 4, 7, 4));
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_ADDRESS:
                contactsInfoBean.getData().setParent_address(changedText);
                itv_parent_address.setRightText(formatLongText(contactsInfoBean.getData().getParent_address(), 12));
                break;
           case GlobalParams.CHANGE_TYPE_BROTHER_NAME:
                contactsInfoBean.getData().setBrothers_name(changedText);
                itv_brother_name.setRightText(formatName(contactsInfoBean.getData().getBrothers_name()));
                break;
            case GlobalParams.CHANGE_TYPE_BROTHER_MOBILE:
                contactsInfoBean.getData().setBrothers_phone(changedText);
                itv_brother_mobile.setRightText(formatText(contactsInfoBean.getData().getBrothers_phone(), 4, 7, 4));
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_ADDRESS:
                contactsInfoBean.getData().setCompany_address(changedText);
                itv_company_address.setRightText(formatName(contactsInfoBean.getData().getCompany_address()));
                break;
            case GlobalParams.CHANGE_TYPE_FRIEND_NAME:
                contactsInfoBean.getData().setFriends_name(changedText);
                itv_friend_name.setRightText(formatName(contactsInfoBean.getData().getFriends_name()));
                break;
            case GlobalParams.CHANGE_TYPE_FRIEND_MOBILE:
                contactsInfoBean.getData().setFriends_phone(changedText);
                itv_friend_mobile.setRightText(formatText(contactsInfoBean.getData().getFriends_phone(), 4, 7, 4));
                break;
            case GlobalParams.CHANGE_TYPE_COLLEAGUE_NAME:
                contactsInfoBean.getData().setColleague_name(changedText);
                itv_colleague_name.setRightText(formatName(contactsInfoBean.getData().getColleague_name()));
                break;
            case GlobalParams.CHANGE_TYPE_COLLEAGUE_MOBILE:
                contactsInfoBean.getData().setColleague_phone(changedText);
                itv_colleague_mobile.setRightText(formatText(contactsInfoBean.getData().getColleague_phone(),4,7,4));
                break;
            case GlobalParams.CHANGE_TYPE_QQ:
                contactsInfoBean.getData().setQq_num(changedText);
                itv_qq.setRightText(formatLongText(contactsInfoBean.getData().getQq_num(),12));
                break;
        }
    }

    private String formatText(String content, int start, int end, int xingNum) {
        StringBuffer changedContent = new StringBuffer();
        if (!(content.length() > end)) {
            return content;
        } else {
            changedContent.append(content.substring(0, start));
            for (int i = 0; i < xingNum; i++) {
                changedContent.append("*");
            }
            changedContent.append(content.substring(end, content.length()));
            return changedContent.toString();
        }
    }

    private String formatName(String name) {
        StringBuffer buffer = new StringBuffer("**");
        buffer.append(name.substring(name.length() - 1, name.length()));
        return buffer.toString();
    }

    private String formatLongText(String content, int lenth) {
        if (content.length() > lenth) {
            return content.substring(0, lenth - 1) + "...";
        } else {
            return content;
        }
    }

    @Override
    public void onClick(View v) {
        String isCanChange=contactsInfoBean.getData().getIs_can_change();
        if(null==contactsInfoBean||!(GlobalParams.IS_CAN_CHANGE.equals(isCanChange))){
            return;
        }

        Bundle mBundle = new Bundle();
        Intent intent = new Intent(mContext, ChangeMyInfoActivity.class);
        switch (v.getId()) {
            case R.id.itv_wechat:
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_WECHAT);
                String wechat = contactsInfoBean.getData().getWechat();
                if (null == wechat) {
                    wechat = "";
                }
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, wechat);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_WECHAT);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_address:
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_ADDRESS);
                String userAddress = contactsInfoBean.getData().getUser_address();
                if (null == userAddress) {
                    userAddress = "";
                }
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, userAddress);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_ADDRESS);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_company_name:
                String companyName = contactsInfoBean.getData().getCompany_name();
                if (null == companyName) {
                    companyName = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_COMPANY_NAME);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, companyName);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_COMPANY_NAME);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_company_phone:
                String companyPhone = contactsInfoBean.getData().getCompany_phone();
                if (null == companyPhone) {
                    companyPhone = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_COMPANY_PHONE);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, companyPhone);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_COMPANY_PHONE);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_parent_name:
                String parentName = contactsInfoBean.getData().getParent_name();
                if (null == parentName) {
                    parentName = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_PARENT_NAME);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, parentName);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_PARENT_NAME);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_parent_mobile:
                String parentMobile = contactsInfoBean.getData().getParent_phone();
                if (null == parentMobile) {
                    parentMobile = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_PARENT_MOBILE);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, parentMobile);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_PARENT_MOBILE);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_parent_address:
                String parentAddress = contactsInfoBean.getData().getParent_address();
                if (null == parentAddress) {
                    parentAddress = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_PARENT_ADDRESS);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, parentAddress);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_PARENT_ADDRESS);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_brother_name:
                String brotherName = contactsInfoBean.getData().getBrothers_name();
                if (null == brotherName) {
                    brotherName = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_BROTHER_NAME);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, brotherName);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_BROTHER_NAME);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_brother_mobile:
                String brotherMobile = contactsInfoBean.getData().getBrothers_phone();
                if (null == brotherMobile) {
                    brotherMobile = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_BROTHER_MOBILE);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, brotherMobile);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_BROTHER_MOBILE);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_company_address:
                String companyAddress = contactsInfoBean.getData().getCompany_address();
                if (null == companyAddress) {
                    companyAddress = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_COMPANY_ADDRESS);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, companyAddress);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_COMPANY_ADDRESS);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_friend_name:

                String friendName = contactsInfoBean.getData().getFriends_name();
                if (null == friendName) {
                    friendName = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_FRIEND_NAME);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, friendName);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_FRIEND_NAME);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_friend_mobile:
                String friendMobile = contactsInfoBean.getData().getFriends_phone();
                if (null == friendMobile) {
                    friendMobile = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_FRIEND_MOBILE);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, friendMobile);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_FRIEND_MOBILE);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_colleague_name:
                String colleagueName = contactsInfoBean.getData().getColleague_name();
                if (null == colleagueName) {
                    colleagueName = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_COLLEAGUE_NAME);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, colleagueName);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_COLLEAGUE_NAME);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_colleague_mobile:
                String colleagueMobile = contactsInfoBean.getData().getColleague_phone();
                if (null == colleagueMobile) {
                    colleagueMobile = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_COLLEAGUE_MOBILE);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, colleagueMobile);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_COLLEAGUE_MOBILE);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;
            case R.id.itv_qq:
                String qqNum = contactsInfoBean.getData().getQq_num();
                if (null == qqNum) {
                    qqNum = "";
                }
                mBundle.putInt(GlobalParams.CHANGE_INFO_TYPE_KEY, GlobalParams.CHANGE_TYPE_QQ);
                mBundle.putString(GlobalParams.CHANGE_INTO_INFO_KEY, qqNum);
                intent.putExtras(mBundle);
                startActivityForResult(intent, GlobalParams.CHANGE_TYPE_QQ);
                overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
                break;

        }
    }

    @Override
    public void onLeftClick(View view) {

    }

    @Override
    public void onAddressClick(View view) {

    }

    @Override
    public void onRightClick(View view) {
        changeContactsInfo();
    }

    private void changeContactsInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            jsonObject.put("wechat", contactsInfoBean.getData().getWechat());
            jsonObject.put("qq_num",contactsInfoBean.getData().getQq_num());
            jsonObject.put("company_address",contactsInfoBean.getData().getCompany_address());
            jsonObject.put("friends_name",contactsInfoBean.getData().getFriends_name());
            jsonObject.put("friends_phone",contactsInfoBean.getData().getFriends_phone());
            jsonObject.put("colleague_name",contactsInfoBean.getData().getColleague_name());
            jsonObject.put("colleague_phone",contactsInfoBean.getData().getColleague_phone());
            jsonObject.put("user_address", contactsInfoBean.getData().getUser_address());
            jsonObject.put("company_name", contactsInfoBean.getData().getCompany_name());
            jsonObject.put("company_phone", contactsInfoBean.getData().getCompany_phone());
            jsonObject.put("parent_name", contactsInfoBean.getData().getParent_name());
            jsonObject.put("parent_phone", contactsInfoBean.getData().getParent_phone());
            jsonObject.put("parent_address", contactsInfoBean.getData().getParent_address());
            jsonObject.put("brothers_name", contactsInfoBean.getData().getBrothers_name());
            jsonObject.put("brothers_phone", contactsInfoBean.getData().getBrothers_phone());
            ChangeContactsInfo changeContactsInfo = new ChangeContactsInfo(mContext);
            changeContactsInfo.changeContactsInfo(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    ToastUtil.showToast(mContext, "修改成功");
//                    tb_title.setIvRightVisible(View.GONE);
                    backActivity();
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
