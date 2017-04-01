package com.maibai.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.model.PostDataBean;
import com.maibai.cash.net.api.SaveExtroContacts;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

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
     * 得到紧急联系人信息
     */
    private void initExtroData() {

    }

    /**
     * 提交紧急联系人信息
     */
    private void postExtroData() {

        String name1 = etAuthNexusName1.getText().toString().trim();
        String name2 = etAuthNexusName2.getText().toString().trim();

        String phone1 = etAuthNexusPhone.getText().toString().trim();
        String phone2 = etAuthNexusPhone2.getText().toString().trim();

        String nexusTxt1 = (String) tvAuthNexus1.getText();
        String nexusTxt2 = (String) tvAuthNexus2.getText();
        int type1 = mNexus.indexOf(nexusTxt1) + 1;
        int type2 = mNexus.indexOf(nexusTxt2) + 1;

        try {
            JSONObject jsonObject = new JSONObject();
            long userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("relavtion_type", type1 + "");
            jsonObject1.put("contact_name", name1);
            jsonObject1.put("contact_mobile", phone1);


            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("relavtion_type", type2 + "");
            jsonObject2.put("contact_name", name2);
            jsonObject2.put("contact_mobile", phone2);


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(0, jsonObject1);
            jsonArray.put(1, jsonObject2);


            jsonObject.put("data", jsonArray);

            SaveExtroContacts saveExtroContacts = new SaveExtroContacts(mContext);
            saveExtroContacts.saveExtroContacts(jsonObject, new BaseNetCallBack<PostDataBean>() {
                @Override
                public void onSuccess(PostDataBean paramT) {
                    int code = paramT.getCode();
                    if (code == 0) {
                        ToastUtil.showToast(mContext, "保存成功");
                        backActivity();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
