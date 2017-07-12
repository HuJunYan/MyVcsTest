package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.net.base.BaseUiListener;
import com.tianshen.cash.view.InviteBottomDialog;
import com.tianshen.cash.view.InviteRankView;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {

    private InviteBottomDialog inviteBottomDialog;
    @BindView(R.id.ll_invite_rank_data)
    LinearLayout ll_invite_rank_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InviteRankView inviteRankView = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_1, "136****3212", "200人", "1000元");
        InviteRankView inviteRankView2 = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_2, "134****2322", "100人", "700元");
        InviteRankView inviteRankView3 = new InviteRankView(getApplicationContext()).setData(R.drawable.invite_rank_3, "157****3122", "60人", "400元");
        ll_invite_rank_data.addView(inviteRankView);
        ll_invite_rank_data.addView(inviteRankView2);
        ll_invite_rank_data.addView(inviteRankView3);


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
        inviteBottomDialog = new InviteBottomDialog(this);
        inviteBottomDialog.show();
    }

    //
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.handleResultData(data, new BaseUiListener(getApplicationContext()));
        }
    }
}
