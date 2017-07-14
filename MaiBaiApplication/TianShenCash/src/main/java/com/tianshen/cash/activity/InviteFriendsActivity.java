package com.tianshen.cash.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.net.base.BaseUiListener;
import com.tianshen.cash.utils.TianShenShareUtils;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.InviteBottomDialog;
import com.tianshen.cash.view.InviteRankView;
import com.tianshen.cash.view.InviteRuleView;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity implements InviteBottomDialog.ShareWeiboListener {

    private InviteBottomDialog inviteBottomDialog;
    @BindView(R.id.ll_invite_rank_data)
    LinearLayout ll_invite_rank_data;
    IUiListener listener;
    @BindView(R.id.ll_invite_rule_data)
    LinearLayout ll_invite_rule_data;
    private WbShareHandler wbShareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InviteRankView inviteRankView = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_1, "136****3212", "200人", "1000元");
        InviteRankView inviteRankView2 = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_2, "134****2322", "100人", "700元");
        InviteRankView inviteRankView3 = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_3, "157****3122", "60人", "400元");
        ll_invite_rank_data.addView(inviteRankView);
        ll_invite_rank_data.addView(inviteRankView2);
        ll_invite_rank_data.addView(inviteRankView3);
        InviteRuleView inviteRuleView = new InviteRuleView(getApplicationContext()).setData(R.drawable.invite_rule_1, "这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则");
        InviteRuleView inviteRuleView2 = new InviteRuleView(getApplicationContext()).setData(R.drawable.invite_rule_2, "这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则");
        InviteRuleView inviteRuleView3 = new InviteRuleView(getApplicationContext()).setData(R.drawable.invite_rule_3, "这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则这是规则");
        ll_invite_rule_data.addView(inviteRuleView);
        ll_invite_rule_data.addView(inviteRuleView2);
        ll_invite_rule_data.addView(inviteRuleView3);


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
        listener = new BaseUiListener(this);
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
        inviteBottomDialog = new InviteBottomDialog(this, listener).setWeiBoListener(this);
        inviteBottomDialog.show();
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
        weiboMultiMessage.mediaObject = TianShenShareUtils.getWebpageObj(this, "http://www.qq.com", "分享标题", "分享描述");
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
        }

        @Override
        public void onWbShareCancel() {
            ToastUtil.showToast(getApplicationContext(), "分享取消");

        }

        @Override
        public void onWbShareFail() {
            ToastUtil.showToast(getApplicationContext(), "分享失败");

        }

    };
}
