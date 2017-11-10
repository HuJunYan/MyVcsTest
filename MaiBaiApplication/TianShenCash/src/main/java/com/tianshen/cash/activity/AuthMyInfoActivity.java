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
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.net.api.GetUserAuthCenter;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianshen.cash.R.id.tv_auth_info_back;

/**
 * 个人信息、信用认证入口view
 */
public class AuthMyInfoActivity extends BaseActivity {

    private static final int RESTCODEUSERINFO = 100;
    private static final int RESTCODEBLANKCARD = 1001;
    private static final int RESTCODEPHONE = 1002;
    private static final int RESTCODEZGIMA= 1003;
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
    @BindView(R.id.red_point2)
    ImageView mRedPoint2;
    @BindView(R.id.tv_auth_remind1)
    TextView mTvAuthRemind1;
    @BindView(R.id.tv_auth_remind2)
    TextView mTvAuthRemind2;

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.imageView2)
    ImageView mImageView2;
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
    private  String type ;
    private UserAuthCenterBean mUserAuthCenterBean;
    private String mMobileStatus; //手机认证状态
    private String mUserInfoStatus; //用户信息认证状态
    private String mBlankStatus; //用户银行卡认证状态
    private String mZhiMaStatus; //用户芝麻信用认证状态
    private String mTaobaotatus; //用户淘宝认证状态

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
        initAuthCenterData();
        type = getIntent().getExtras().getString(ACTIVITY_FLAG);

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
            mTvAuthRemind1.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_auth_center_menu_prompt),null,null,null);
            mTvAuthRemind1.setCompoundDrawablePadding(10);
//            mTvAuthRemind1.setCompoundDrawablesRelativeWithIntrinsicBounds();
            mRedPoint.setVisibility(View.GONE);
            mTvAuthRemind1.setVisibility(View.GONE);
            mRedPoint2.setVisibility(View.VISIBLE);
            mTvAuthRemind2.setVisibility(View.VISIBLE);

            mTitle.setText("个人信息认证");
            mRlAuthExtral.setVisibility(View.GONE);
            mRlAuthThree.setVisibility(View.GONE);
            mTvAuthInfoOne.setText("个人信息");
            mTvAuthInfomoney.setText("收款银行卡");
            if (mUserAuthCenterBean==null){
                initAuthCenterData();
            }
            mUserInfoStatus=mUserAuthCenterBean.getData().getUserdetail_pass();
            mBlankStatus=mUserAuthCenterBean.getData().getBankcard_pass();

            showImageView(1,mUserInfoStatus);
            showImageView(2,mBlankStatus);



        } else {
            mRedPoint.setVisibility(View.VISIBLE);
            mTvAuthRemind1.setVisibility(View.VISIBLE);
            mRedPoint2.setVisibility(View.GONE);
            mTvAuthRemind2.setVisibility(View.GONE);
            mTitle.setText("信用认证");
            mRlAuthExtral.setVisibility(View.VISIBLE);
            mRlAuthThree.setVisibility(View.VISIBLE);
            mTvAuthInfoOne.setText("手机运营商");
            mTvAuthInfomoney.setText("芝麻信用");
            mMobileStatus=mUserAuthCenterBean.getData().getChina_mobile();
            mZhiMaStatus=mUserAuthCenterBean.getData().getZhima_pass();
            showImageView(1,mMobileStatus);
            showImageView(2,mZhiMaStatus);

        }

    }

    public void showImageView(int line,String statue){

        switch (line){
            case 1:
                if ("1".equals(statue)) {
                    mImageView.setImageResource(R.drawable.authed_statue);

                }else{
                    mImageView.setImageResource(R.drawable.ic_arraw_right2);
                }
                break;

            case 2:
                if ("1".equals(statue)) {
                    mImageView2.setImageResource(R.drawable.authed_statue);

                }else{
                    mImageView2.setImageResource(R.drawable.ic_arraw_right2);
                }

                break;

            default:
                mImageView.setImageResource(R.drawable.ic_arraw_right2);

                break;
        }
    }

    @OnClick({tv_auth_info_back,R.id.rl_auth_one, R.id.rl_auth_two, R.id.rl_auth_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_auth_info_back:
                finish();
                break;
            case R.id.rl_auth_one:

                if ("1".equals(type)){
                     //个人信息认证
                    Intent intentOne  =new Intent(this,AuthPersonInfoActivity.class);
                    intentOne.putExtra("ONESTATUE",mUserInfoStatus);
                    startActivityForResult(intentOne,RESTCODEUSERINFO);
                }else {
                    //手机认证
                    if (mUserAuthCenterBean != null) {
                        mMobileStatus = mUserAuthCenterBean.getData().getChina_mobile();
                    }
                    if ("1".equals(mMobileStatus)) {
                        ToastUtil.showToast(mContext, "之前已经认证");
                        return;
                    }
                    String china_mobile_url = mUserAuthCenterBean.getData().getChina_mobile_url();
                    gotoChinaMobileActivity(china_mobile_url, "手机认证","3");
                }

                break;
            case R.id.rl_auth_two:
                if ("1".equals(type)) {
                    //手机认证
                    if ("0".equals(mUserInfoStatus)){
                        ToastUtil.showToast(getBaseContext(),"请先认证个人信息");
                        return;
                    }
                    if ("1".equals(mBlankStatus)){
                        ToastUtil.showToast(getBaseContext(),"之前已经认证");
                        return;
                    }

                    Intent intentTwo  =new Intent(this,AuthBlankActivity.class);
                    intentTwo.putExtra("TWOSTATUE",mUserInfoStatus);
                    startActivityForResult(intentTwo,RESTCODEBLANKCARD);
                } else {
                   //     第一步认证过才可以认证第二步 芝麻信用认证
                    if ("0".equals(mMobileStatus)){
                        ToastUtil.showToast(getBaseContext(),"请先认证手机信息");
                        return;
                    }

                    String zhimaStatus = mUserAuthCenterBean.getData().getZhima_pass();
                    if ("1".equals(zhimaStatus)) {
                        ToastUtil.showToast(mContext, "之前已经认证");
                        return;
                    }
                    String zhima_url = mUserAuthCenterBean.getData().getZhima_url();
                    gotoChinaMobileActivity(zhima_url, "芝麻信用","4");
                }
                break;
            case R.id.rl_auth_three:
                //淘宝认证
                gotoTaoBaoAuth();
                break;
        }
    }

    /**
     * 跳转到手机认证和芝麻信用认证
     * @param url webview加载的url
     * @param title webView标题
     * @param type  区分是手机认证还是芝麻信用认证
     */
    private void gotoChinaMobileActivity(String url, String title,String type) {
        /*Bundle bundle = new Bundle();
        bundle.putString(GlobalParams.CHINA_MOBILE_URL_KEY, url);
        bundle.putString(GlobalParams.CHINA_MOBILE_TITLE_KEY, title);
        gotoActivity(mContext, ChinaMobileActivity.class, bundle);*/
        Intent bundle = new Intent(this,ChinaMobileActivity.class);
        bundle.putExtra(GlobalParams.CHINA_MOBILE_URL_KEY, url);
        bundle.putExtra(GlobalParams.CHINA_MOBILE_TITLE_KEY, title);
        if ("3".equals(type)) {
            startActivityForResult(bundle, RESTCODEPHONE);
        }else {
            startActivityForResult(bundle, RESTCODEZGIMA);
        }
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

    /**
     * 得到用户认证信息
     */
    private void initAuthCenterData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetUserAuthCenter getUserAuthCenter = new GetUserAuthCenter(mContext);
            getUserAuthCenter.userAuthCenter(jsonObject, null, true, new BaseNetCallBack<UserAuthCenterBean>() {
                @Override
                public void onSuccess(UserAuthCenterBean paramT) {
                    mUserAuthCenterBean = paramT;
                    mMobileStatus  = mUserAuthCenterBean.getData().getChina_mobile();
                    showView(type);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0){
            case RESTCODEUSERINFO:
                //认证个人信息返回返回
                if (arg2!=null) {
                    String statue = arg2.getStringExtra("RESULTSTATUE");
                    mUserInfoStatus=statue;

                    if ("1".equals(statue)) {
                        mImageView.setImageResource(R.drawable.authed_statue);
                    } else {
                        mImageView.setImageResource(R.drawable.ic_arraw_right2);
                    }
                }

                break;
            case RESTCODEBLANKCARD:
                //认证认证银行卡返回
                if (arg2!=null) {
                    String statue = arg2.getStringExtra("RESULTSTATUE");

                    if ("1".equals(statue)) {
                        mImageView2.setImageResource(R.drawable.authed_statue);
                        backActivity();
                    } else {
                        mImageView2.setImageResource(R.drawable.ic_arraw_right2);
                    }
                }

                break;
            case RESTCODEPHONE:
                //认证手机返回
                if (arg2!=null) {
                    String statue3 = arg2.getStringExtra("RESULTSTATUE");
                    mMobileStatus = statue3;

                    if ("1".equals(statue3)) {
                        mImageView.setImageResource(R.drawable.authed_statue);

                    } else {
                        mImageView.setImageResource(R.drawable.ic_arraw_right2);
                    }
                }

                break;

            case RESTCODEZGIMA:
                //认证芝麻信用返回
                initAuthCenterData();
                /*if (arg2!=null) {
                    String statue4 = arg2.getStringExtra("RESULTSTATUE");

                    if ("1".equals(statue4)) {
                        mImageView2.setImageResource(R.drawable.authed_statue);
                        backActivity();
                    } else {
                        mImageView2.setImageResource(R.drawable.ic_arraw_right2);
                    }
                }*/

                break;



        }



    }
}
