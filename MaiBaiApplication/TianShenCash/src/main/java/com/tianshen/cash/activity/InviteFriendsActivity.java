package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.view.InviteBottomDialog;

import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {

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
                LogUtil.d("aaa", "xixi");
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        new InviteBottomDialog(this).show();
//        Dialog bottomDialog = new Dialog(this, R.style.invite_dialog_style);
//        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_invite_share, null);
//        bottomDialog.setContentView(contentView);
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
//        contentView.setLayoutParams(layoutParams);
//        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
//        bottomDialog.getWindow().setWindowAnimations(R.style.invite_animation);
//        bottomDialog.show();
    }
}
