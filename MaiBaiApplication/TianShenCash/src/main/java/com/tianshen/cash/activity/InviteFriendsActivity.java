package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.net.base.BaseUiListener;
import com.tianshen.cash.view.InviteBottomDialog;

import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {

    private InviteBottomDialog inviteBottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
