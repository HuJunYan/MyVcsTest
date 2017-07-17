package com.tianshen.cash.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.WechatShareEvent;
import com.tianshen.cash.model.InviteFriendsBean;
import com.tianshen.cash.net.api.InviteFriendsApi;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.utils.QRCodeUtils;
import com.tianshen.cash.utils.TianShenShareUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.InviteBottomDialog;
import com.tianshen.cash.view.InviteRankView;
import com.tianshen.cash.view.InviteRuleView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity implements InviteBottomDialog.ShareWeiboListener {

    private InviteBottomDialog inviteBottomDialog;
    @BindView(R.id.ll_invite_rank_data)
    LinearLayout ll_invite_rank_data;
    @BindView(R.id.ll_invite_rule_data)
    LinearLayout ll_invite_rule_data;
    private WbShareHandler wbShareHandler;
    private String mShareUrl;
    private List<InviteFriendsBean.TopList> mRankList;
    private List<InviteFriendsBean.RuleList> mRuleList;
    private int[] mRankResArray = {R.drawable.invite_rank_1, R.drawable.invite_rank_2,
            R.drawable.invite_rank_3, R.drawable.invite_rank_4, R.drawable.invite_rank_5};
    private int[] mRuleResArray = {R.drawable.invite_rule_1, R.drawable.invite_rule_2, R.drawable.invite_rule_3,
            R.drawable.invite_rule_4, R.drawable.invite_rule_5, R.drawable.invite_rule_6,
            R.drawable.invite_rule_7, R.drawable.invite_rule_8, R.drawable.invite_rule_9,};
    private Bitmap mQRBitmap;
    private float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        density = getResources().getDisplayMetrics().density;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InviteFriendsApi inviteFriendsApi = new InviteFriendsApi(this);
        inviteFriendsApi.getInviteData(jsonObject, new BaseNetCallBack<InviteFriendsBean>() {
            @Override
            public void onSuccess(InviteFriendsBean inviteFriendsBean) {
                if (inviteFriendsBean != null && inviteFriendsBean.data != null) {
                    InviteFriendsBean.InviteData data = inviteFriendsBean.data;
                    mShareUrl = data.invite_url;
                    mRuleList = data.activity_list;
                    mRankList = data.top_list;
                }
                refreshUI();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                String json = "{\"code\": 0,\"msg\": \"success\",\"data\":{\"invite_url\":\"http://www.baidu.com\",\"top_list\":[{\"mobile_string\":\"187****1234\",\"invite_num_string\":\"30人\",\"invite_reward_string\":\"500元\"},{\"mobile_string\":\"187****1234\",\"invite_num_string\":\"30人\",\"invite_reward_string\":\"500元\"}],\"activity_list\":[{\"activity_string\":\"规则内容\"}]}}";
                InviteFriendsBean inviteFriendsBean = GsonUtil.json2bean(json, InviteFriendsBean.class);
                InviteFriendsBean.InviteData data = inviteFriendsBean.data;
                mShareUrl = data.invite_url;
                mRuleList = data.activity_list;
                mRankList = data.top_list;

                refreshUI();
            }
        });
    }

    private void refreshUI() {
        ll_invite_rank_data.removeAllViews();
        ll_invite_rule_data.removeAllViews();
        if (mRankList != null) {
            for (int i = 0; i < (mRankList.size() > mRankResArray.length ? mRankResArray.length : mRankList.size()); i++) {
                InviteFriendsBean.TopList rankBean = mRankList.get(i);
                InviteRankView inviteRankView = new InviteRankView(getApplicationContext()).setData(mRankResArray[i], rankBean.mobile_string, rankBean.invite_num_string, rankBean.invite_reward_string);
                ll_invite_rank_data.addView(inviteRankView);

            }
        }
        if (mRuleList != null) {
            for (int i = 0; i < (mRuleList.size() > mRuleResArray.length ? mRuleResArray.length : mRuleList.size()); i++) {
                InviteRuleView inviteRankView = new InviteRuleView(getApplicationContext()).setData(mRuleResArray[i], mRuleList.get(i).activity_string);
                ll_invite_rule_data.addView(inviteRankView);
            }
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
    }

    @OnClick({R.id.tv_back, R.id.tv_invite_friends_make_money})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                backActivity();
                break;
            case R.id.tv_invite_friends_make_money:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        if (mShareUrl == null) {
            showDataErrorTip();
            return;
        }
        mQRBitmap = QRCodeUtils.createQRCode(mShareUrl, (int) (120 * density + 0.5f));
        inviteBottomDialog = new InviteBottomDialog(this, listener).setWeiBoListener(this).setQRCodeBitmap(mQRBitmap).setShareUrl(mShareUrl);
        inviteBottomDialog.show();
    }

    private void showDataErrorTip() {
        ToastUtil.showToast(this, "数据错误");
    }

    //
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.handleResultData(data, listener);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
    }

    //分享到微博
    public void shareToWeibo(Activity activity) {
        WbSdk.install(this, new AuthInfo(this.getApplicationContext(), GlobalParams.APP_WEIBO_KEY, GlobalParams.WEIBO_OAUTH_ADDRESS, GlobalParams.WEIBO_SCOPE));
        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = TianShenShareUtils.getTextObj("haha");
        weiboMultiMessage.mediaObject = TianShenShareUtils.getWebpageObj(this, mShareUrl, "分享标题", "分享描述");
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }


    //分享到微博的回调 dialog 回调
    @Override
    public void shareToWeibo() {
        shareToWeibo(this);
    }


    private WbShareCallback wbShareCallback = new WbShareCallback() {

        @Override
        public void onWbShareSuccess() {
            ToastUtil.showToast(getApplicationContext(), "分享成功");
            if (inviteBottomDialog != null) {
                inviteBottomDialog.cancel();
            }
        }

        @Override
        public void onWbShareCancel() {
//            ToastUtil.showToast(getApplicationContext(), "分享取消");
        }

        @Override
        public void onWbShareFail() {
//            ToastUtil.showToast(getApplicationContext(), "分享失败");
        }
    };

    IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            ToastUtil.showToast(mContext, "分享成功");
            if (inviteBottomDialog != null) {
                inviteBottomDialog.cancel();
            }
        }

        @Override
        public void onError(UiError uiError) {
//            ToastUtil.showToast(mContext, "分享失败");
        }

        @Override
        public void onCancel() {
//            ToastUtil.showToast(mContext, "分享取消");
        }
    };


    @Subscribe
    public void onWeChatShareEvent(WechatShareEvent event) {
        if (inviteBottomDialog != null) {
            inviteBottomDialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQRBitmap != null && !mQRBitmap.isRecycled()) {
            mQRBitmap.recycle();
            mQRBitmap = null;
        }
    }
}