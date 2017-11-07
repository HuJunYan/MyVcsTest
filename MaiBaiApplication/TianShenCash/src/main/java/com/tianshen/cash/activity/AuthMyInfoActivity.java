package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianshen.cash.R.id.tv_auth_info_back;

/**
 * 个人信息、信用认证入口view
 */
public class AuthMyInfoActivity extends BaseActivity {

    @BindView(tv_auth_info_back)
    TextView mTvAuthInfoBack;
    @BindView(R.id.tv_auth_info_post)
    TextView mTvAuthInfoPost;
    @BindView(R.id.tv_auth_info_marry_key)
    TextView mTvAuthInfoMarryKey;
    @BindView(R.id.tv_auth_info_marry)
    TextView mTvAuthInfoMarry;
    @BindView(R.id.tv_auth_info_educational_key)
    TextView mTvAuthInfoEducationalKey;
    @BindView(R.id.tv_auth_info_educational)
    TextView mTvAuthInfoEducational;
    @BindView(R.id.red_point)
    ImageView mRedPoint;
    @BindView(R.id.tv_auth_remind1)
    TextView mTvAuthRemind1;

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.tv_auth_info_money)
    TextView mTvAuthInfomoney;
    @BindView(R.id.et_auth_info_address_details)
    EditText mEtAuthInfoAddressDetails;
    @BindView(R.id.green_point)
    TextView mGreenPoint;
    @BindView(R.id.rl_auth_extral)
    RelativeLayout mRlAuthExtral;
    @BindView(R.id.iv_auth_tb)
    ImageView mIvAuthTb;
    @BindView(R.id.tv_auth_info_work_num)
    TextView mTvAuthInfoWorkNum;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rl_auth_one)
    RelativeLayout mRlAuthOne;
    @BindView(R.id.rl_auth_two)
    RelativeLayout mRlAuthTwo;
    @BindView(R.id.rl_auth_three)
    RelativeLayout mRlAuthThree;
    @BindView(R.id.tv_auth_info_one)
    TextView mTvAuthInfoOne;
    public static final String  PERSONFLAG = "1";  //当前页面的标识： 1个人信息认证  2信用认证
    public static final String  CREDITFLAG = "2";  //当前页面的标识： 1个人信息认证  2信用认证
    public static final String  ACTIVITY_FLAG = "TYEP";
    private  String type ="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_my_info;
    }

    @Override
    protected void findViews() {
//        type = getIntent().getExtras().getString(ACTIVITY_FLAG);
        showView("1");
    }

    @Override
    protected void setListensers() {

    }

    /**
     * 当前页面UI
     *
     * @param type 当前页面的标识 1个人信息认证  2信用认证
     */
    public void showView(String type) {
        if ("1".equals(type)) {
            mTitle.setText("个人信息认证");
            mRlAuthExtral.setVisibility(View.GONE);
            mRlAuthThree.setVisibility(View.GONE);
            mTvAuthInfoOne.setText("个人信息");
            mTvAuthInfomoney.setText("收款银行卡");

        } else {
            mTitle.setText("信用认证");
            mRlAuthExtral.setVisibility(View.VISIBLE);
            mRlAuthThree.setVisibility(View.VISIBLE);
            mTvAuthInfoOne.setText("手机运营商");
            mTvAuthInfomoney.setText("芝麻信用");
        }

    }

    @OnClick({tv_auth_info_back,R.id.rl_auth_one, R.id.rl_auth_two, R.id.rl_auth_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_auth_info_back:
                finish();
                break;
            case R.id.rl_auth_one:
                if ("1".equals(type)) {
                    startActivity(new Intent(this, AuthPersonInfoActivity.class));
                } else {
                    gotoChinaMobileActivity("", "手机认证");
                }

                break;
            case R.id.rl_auth_two:
                if ("1".equals(type)) {
                    //第一步认证过才可以认证第二步
                    startActivity(new Intent(this, AuthBlankActivity.class));
                } else {
                    gotoChinaMobileActivity("", "芝麻信用");
                }
                break;
            case R.id.rl_auth_three:
                //淘宝认证
                gotoTaoBaoAuth();
                break;
        }
    }

    /**
     * 跳转到运营商认证
     */
    private void gotoChinaMobileActivity(String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.CHINA_MOBILE_URL_KEY, url);
        bundle.putString(GlobalParams.CHINA_MOBILE_TITLE_KEY, title);
        gotoActivity(mContext, ChinaMobileActivity.class, bundle);
    }

    /**
     * 跳转到淘宝renz
     */
    private void gotoTaoBaoAuth() {
        String userId = TianShenUserUtil.getUserId(mContext);
        String apiKey = "012a5b3a9bf94ac984fbb7c400c460aa";

        MxParam mxParam = new MxParam();
        mxParam.setUserId(userId);
        mxParam.setApiKey(apiKey);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_TAOBAO);

        MoxieSDK.getInstance().start(AuthMyInfoActivity.this, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        case MxParam.ResultCode.IMPORTING:
                            break;
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
                            ToastUtil.showToast(mContext, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            ToastUtil.showToast(mContext, "认证成功!");
                            moxieContext.finish();
                            return true;
                    }
                }
                return false;
            }
        });

    }
}
