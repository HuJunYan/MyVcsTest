package com.maibai.cash.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.maibai.cash.R;
import com.maibai.cash.adapter.BankListAdapter;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.model.BankListBean;
import com.maibai.cash.model.BankListItemBean;
import com.maibai.cash.model.WithdrawalsItemBean;
import com.maibai.cash.net.api.GetAllBankList;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.GetTelephoneUtils;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 银行卡信息
 */

public class AuthBankCardActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.rl_bank_card)
    RelativeLayout rl_bank_card;
    @BindView(R.id.tv_auth_bank_card_back)
    TextView tvAuthBankCardBack;
    @BindView(R.id.tv_auth_info_post)
    TextView tvAuthInfoPost;
    @BindView(R.id.tv_auth_bank_card_person)
    TextView tvAuthBankCardPerson;
    @BindView(R.id.et_auth_bank_card_person)
    EditText etAuthBankCardPerson;
    @BindView(R.id.tv_bank_card_key)
    TextView tvBankCardKey;
    @BindView(R.id.tv_bank_card)
    TextView tv_bank_card;
    @BindView(R.id.tv_bank_card_num_key)
    TextView tvBankCardNumKey;
    @BindView(R.id.et_auth_info_work_name)
    EditText etAuthInfoWorkName;
    @BindView(R.id.tv_bank_card_phone_num_key)
    TextView tvBankCardPhoneNumKey;
    @BindView(R.id.et_bank_card_phone_num)
    EditText etBankCardPhoneNum;
    @BindView(R.id.tv_severity_code_key)
    TextView tvSeverityCodeKey;
    @BindView(R.id.et_severity_code)
    EditText etSeverityCode;
    @BindView(R.id.tv_severity_code)
    TextView tvSeverityCode;

    private BankListBean mBankListBean; //银行卡列表数据
    private ArrayList<String> mDialogData;// //银行卡列表dialog数据

    private int mCurrentBankCardIndex;//当前用户选择银行卡的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_bank_card;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvAuthBankCardBack.setOnClickListener(this);
        tvAuthInfoPost.setOnClickListener(this);
        rl_bank_card.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_bank_card_back:
                backActivity();
                break;
            case R.id.tv_auth_info_post:
                ToastUtil.showToast(mContext, "点击了提交");
                break;
            case R.id.rl_bank_card:
                initBankListData();
                break;
        }
    }

    private void initBankListData() {
        try {
            JSONObject jsonObject = new JSONObject();
            long userID = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userID);
            GetAllBankList getAllBankList = new GetAllBankList(mContext);
            getAllBankList.getAllBankList(jsonObject, null, true, new BaseNetCallBack<BankListBean>() {
                @Override
                public void onSuccess(final BankListBean paramT) {
                    LogUtil.d("abc", "onSuccess");
                    mBankListBean = paramT;
                    if (mBankListBean != null) {
                        parserBankListData();
                        showBankListDialog();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    LogUtil.d("abc", "onFailure");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析银行卡列表数据给dialog用
     */
    private void parserBankListData() {
        List<BankListItemBean> datas = mBankListBean.getData();
        mDialogData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            BankListItemBean bankListItemBean = datas.get(i);
            String bank_name = bankListItemBean.getBank_name();
            mDialogData.add(bank_name);

            LogUtil.d("abc", "bank_name--->" + bank_name);
        }
    }

    private void showBankListDialog() {

        LogUtil.d("abc", "showBankListDialog---in");

        new MaterialDialog.Builder(mContext)
                .title("选择银行卡")
                .items(mDialogData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCurrentBankCardIndex = position;
                        refreshBankUI();

                        LogUtil.d("abc", "showBankListDialog---onSelection");
                    }
                }).show();

        LogUtil.d("abc", "showBankListDialog---over");
    }

    /**
     * 刷新银行卡UI，吧用户选择的银行卡设置到EditText上
     */
    private void refreshBankUI() {
        String bankCardName = mDialogData.get(mCurrentBankCardIndex);
        tv_bank_card.setText(bankCardName);
    }

}
