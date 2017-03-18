package com.maibai.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;
import com.maibai.user.adapter.BankListAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.model.BankListBean;
import com.maibai.user.model.BankListItemBean;
import com.maibai.user.net.api.GetAllBankList;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.RegexUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.view.ChangeInterface;
import com.maibai.user.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBankCardActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_confirm;
    private TextView tv_card_type_value;
    private MyEditText et_reserved_phone;
    private MyEditText et_cardholder_name;
    private MyEditText et_bank_card_account;
    private RelativeLayout rl_bank_card_type;

    private Bundle mBundle;
    private BankListItemBean bankListItemBean;

    private final String TAG = "AddBandCardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    protected void findViews() {
        et_reserved_phone = (MyEditText) findViewById(R.id.et_reserved_phone);
        et_cardholder_name = (MyEditText) findViewById(R.id.et_cardholder_name);
        et_bank_card_account = (MyEditText) findViewById(R.id.et_bank_card_account);
        rl_bank_card_type = (RelativeLayout) findViewById(R.id.rl_bank_card_type);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        et_bank_card_account.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_bank_card_account.setKeyListener("0123456789 ");
        tv_card_type_value=(TextView)findViewById(R.id.tv_card_type_value);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        rl_bank_card_type.setOnClickListener(this);
        et_bank_card_account.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "changed: s:" + s + "  start:" + start + "  before:" + before + "  count:" + count);
                if ((start == 3 || start == 8 || start == 13 || start == 18) && count > 0) {
                    String content = s.toString();
                    content += " ";
                    et_bank_card_account.setText(content);
                    et_bank_card_account.setSelection(start + count + 1);
                }
            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "before: s:" + s + "  start:" + start + "    count:" + count + "    after:" + after);
            }

            @Override
            public void chageAfter(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                if (null == et_cardholder_name.getEditTextString() || "".equals(et_cardholder_name.getEditTextString())) {
                    ToastUtil.showToast(mContext, "持卡人姓名不可为空");
                    return;
                }
                if (null == getTextWithoutSpace(et_bank_card_account.getEditTextString()) || "".equals(getTextWithoutSpace(et_bank_card_account.getEditTextString()))) {
                    ToastUtil.showToast(mContext, "银行卡号不可为空");
                    return;
                }
                if (!RegexUtil.IsBankCard(getTextWithoutSpace(et_bank_card_account.getEditTextString()))) {
                    ToastUtil.showToast(mContext, "请正确填写银行卡号");
                    return;
                }
                if (null == et_reserved_phone.getEditTextString() || "".equals(et_reserved_phone.getEditTextString())) {
                    ToastUtil.showToast(mContext, "手机号不可为空");
                    return;
                }
                if (!RegexUtil.IsTelephone(et_reserved_phone.getEditTextString())) {
                    ToastUtil.showToast(mContext, "请输入11位有效手机号");
                    return;
                }
                if(null==bankListItemBean){
                    ToastUtil.showToast(mContext,"请选择银行");
                    return;
                }
                mBundle.putSerializable("bankListItemBean",bankListItemBean);
                mBundle.putString("customer_id", UserUtil.getId(mContext));
                mBundle.putString("card_user_name", et_cardholder_name.getEditTextString());
                mBundle.putString("card_num", getTextWithoutSpace(et_bank_card_account.getEditTextString()));
                mBundle.putString("reserved_mobile", et_reserved_phone.getEditTextString());
                gotoActivity(mContext, InputVerificationActivity.class, mBundle);
                backActivity();
                break;
            case R.id.rl_bank_card_type:
                showBankListDialog();
                break;
        }
    }
    private String getTextWithoutSpace(String bank_account) {
        return bank_account.replace(" ", "");
    }

    private void showBankListDialog() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            GetAllBankList getAllBankList = new GetAllBankList(mContext);
            getAllBankList.getAllBankList(jsonObject, null, true, new BaseNetCallBack<BankListBean>() {
                @Override
                public void onSuccess(final BankListBean paramT) {
                    final Dialog dialog = new AlertDialog.Builder(mContext, R.style.permission_without_step_dialog).create();
                    View view = LayoutInflater.from(mContext).inflate(R.layout.bank_list_dialog, null);
                    ListView listView = (ListView) view.findViewById(R.id.lv_bank_list);
                    BankListAdapter adapter = new BankListAdapter(mContext, paramT);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            bankListItemBean = paramT.getData().get(position);
                            tv_card_type_value.setText(paramT.getData().get(position).getBank_name());
                            tv_card_type_value.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                            dialog.dismiss();
                            new GetTelephoneUtils(mContext).changeLight();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.setContentView(view);
                    new GetTelephoneUtils(mContext).changeDark();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }
}
