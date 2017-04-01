package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 紧急联系人
 */

public class AuthExtroContactsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_auth_info_back)
    TextView tvAuthInfoBack;
    @BindView(R.id.tv_auth_extro_post)
    TextView tvAuthExtroPost;
    @BindView(R.id.tv_auth_nexus1_key)
    TextView tvAuthNexus1Key;
    @BindView(R.id.tv_auth_nexus1)
    TextView tvAuthNexus1;
    @BindView(R.id.tv_auth_nexus_name1_key)
    TextView tvAuthNexusName1Key;
    @BindView(R.id.et_auth_nexus_name1)
    EditText etAuthNexusName1;
    @BindView(R.id.tv_auth_nexus_phone)
    TextView tvAuthNexusPhone;
    @BindView(R.id.et_auth_nexus_phone)
    EditText etAuthNexusPhone;
    @BindView(R.id.tv_auth_nexus2_key)
    TextView tvAuthNexus2Key;
    @BindView(R.id.tv_auth_nexus2)
    TextView tvAuthNexus2;
    @BindView(R.id.tv_auth_nexus_name2_key)
    TextView tvAuthNexusName2Key;
    @BindView(R.id.et_auth_nexus_name2)
    EditText etAuthNexusName2;
    @BindView(R.id.tv_auth_nexus_phone2)
    TextView tvAuthNexusPhone2;
    @BindView(R.id.et_auth_nexus_phone2)
    EditText etAuthNexusPhone2;
    @BindView(R.id.rl_auth_nexus1)
    RelativeLayout rlAuthNexus1;
    @BindView(R.id.rl_auth_nexus2)
    RelativeLayout rlAuthNexus2;

    private ArrayList<String> mNexus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_extro_contacts;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthExtroPost.setOnClickListener(this);
        tvAuthInfoBack.setOnClickListener(this);
        rlAuthNexus1.setOnClickListener(this);
        rlAuthNexus2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_info_back:
                backActivity();
                break;
            case R.id.tv_auth_extro_post:
                postExtroData();
                break;
            case R.id.rl_auth_nexus1:
                showExtroDialog(0);
                break;
            case R.id.rl_auth_nexus2:
                showExtroDialog(1);
                break;
        }
    }

    /**
     * 提交紧急联系人信息
     */
    private void postExtroData() {

    }

    private void showExtroDialog(final int clickPosition) {
        new MaterialDialog.Builder(mContext)
                .title("与我关系")
                .items(mNexus)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        refreshNexusUI(clickPosition, position);
                    }
                })
                .show();
    }


    private void refreshNexusUI(int clickPosition, int selectPosition) {
        if (clickPosition == 0) {
            tvAuthNexus1.setText(mNexus.get(selectPosition));
        } else {
            tvAuthNexus2.setText(mNexus.get(selectPosition));
        }
    }

    /**
     * 初始化dialog数据
     */
    private void initDialogData() {
        mNexus.add("父母");
        mNexus.add("配偶");
        mNexus.add("直亲");
        mNexus.add("朋友");
        mNexus.add("同事");
    }


}
