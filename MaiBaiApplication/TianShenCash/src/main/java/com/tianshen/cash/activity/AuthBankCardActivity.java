package com.tianshen.cash.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianshen.cash.R;
import com.tianshen.cash.adapter.BankListAdapter;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.model.BankCardInfoBean;
import com.tianshen.cash.model.BankListBean;
import com.tianshen.cash.model.BankListItemBean;
import com.tianshen.cash.model.BindVerifySmsBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.net.api.BindBankCard;
import com.tianshen.cash.net.api.GetAllBankList;
import com.tianshen.cash.net.api.GetBankCardInfo;
import com.tianshen.cash.net.api.GetBindVerifySms;
import com.tianshen.cash.net.api.GetUserAuthCenter;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.GetTelephoneUtils;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tianshen.cash.R.id.et_verification_code;

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
    @BindView(R.id.et_auth_card_num)
    EditText et_auth_card_num;
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

    private BankCardInfoBean mBankCardInfoBean;
    private BankListBean mBankListBean; //银行卡列表数据
    private ArrayList<String> mDialogData;// //银行卡列表dialog数据

    private String bind_no;//获取验证码时候得到的


    private int mCurrentBankCardIndex;//当前用户选择银行卡的位置

    private int mStartTime = 59;


    private static final int MSG_SEVERITY_TIME = 1;
    private static final int MSG_SEVERITY_DELAYED = 1 * 1000;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEVERITY_TIME:
                    refreshSeverityTextUI();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBankData();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_bank_card;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void setListensers() {
        tvAuthBankCardBack.setOnClickListener(this);
        tvAuthInfoPost.setOnClickListener(this);
        rl_bank_card.setOnClickListener(this);
        tvSeverityCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_bank_card_back:
                backActivity();
                break;
            case R.id.tv_auth_info_post:
                bindBankCard();
                break;
            case R.id.rl_bank_card:
                initBankListData();
                break;
            case R.id.tv_severity_code:
                initSeverityCode();
                tvSeverityCode.setEnabled(false);
                break;
        }
    }

    /**
     * 刷新UI
     */
    private void refreshUI(BankCardInfoBean bankCardInfoBean) {
        if (bankCardInfoBean == null) {
            return;
        }

        mBankCardInfoBean = bankCardInfoBean;

        String bank_name = bankCardInfoBean.getData().getBank_name();
        String card_num = bankCardInfoBean.getData().getCard_num();
        String card_user_name = bankCardInfoBean.getData().getCard_user_name();
        String reserved_mobile = bankCardInfoBean.getData().getReserved_mobile();
        tv_bank_card.setText(bank_name);
        etAuthBankCardPerson.setText(card_user_name);
        et_auth_card_num.setText(card_num);
        etAuthBankCardPerson.setText(card_user_name);
        etBankCardPhoneNum.setText(reserved_mobile);
    }

    /**
     * 得到用户认证信息
     */
    private void initBankData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userId = TianShenUserUtil.getUserId(mContext);
            jsonObject.put("customer_id", userId);
            GetBankCardInfo getBankCardInfo = new GetBankCardInfo(mContext);
            getBankCardInfo.getBankCardInfo(jsonObject, new BaseNetCallBack<BankCardInfoBean>() {
                @Override
                public void onSuccess(BankCardInfoBean paramT) {
                    refreshUI(paramT);
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
     * 得到银行卡列表数据
     */
    private void initBankListData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userID = TianShenUserUtil.getUserId(mContext);
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
        }
    }


    /**
     * 显示银行卡列表Dialog
     */
    private void showBankListDialog() {

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


    /**
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()){
            return;
        }

        tvSeverityCode.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            tvSeverityCode.setText("重获取验证码");
            mStartTime = 59;
            tvSeverityCode.setEnabled(true);
            mHandler.removeMessages(MSG_SEVERITY_TIME);
        } else {
            tvSeverityCode.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED);
        }

    }

    /**
     * 得到验证码
     */
    private void initSeverityCode() {
        if (mBankCardInfoBean == null) {
            ToastUtil.showToast(mContext, "请完善资料");
            return;
        }
        BankCardInfoBean.Data bankCardInfoBeanData = mBankCardInfoBean.getData();
        String bank_name = bankCardInfoBeanData.getBank_name(); //银行卡名字
        String bankId = bankCardInfoBeanData.getBank_id();
        String card_user_name = etAuthBankCardPerson.getText().toString().trim();
        String card_num = et_auth_card_num.getText().toString().trim();
        String reserved_mobile = etBankCardPhoneNum.getText().toString().trim();


        LogUtil.d("abc","bank_id--1->"+bankId);
        if (mBankListBean != null) {
            BankListItemBean itemBean = mBankListBean.getData().get(mCurrentBankCardIndex);
            bankId = itemBean.getBank_id();
            LogUtil.d("abc","bank_id--2->"+bankId);
        }



        if (TextUtils.isEmpty(tv_bank_card.getText())) {
            ToastUtil.showToast(mContext, "请完善资料");
            return;
        }
        if (TextUtils.isEmpty(card_user_name)) {
            ToastUtil.showToast(mContext, "请完善资料");
            return;
        }
        if (TextUtils.isEmpty(card_num)) {
            ToastUtil.showToast(mContext, "请完善资料");
            return;
        }
        if (TextUtils.isEmpty(reserved_mobile)) {
            ToastUtil.showToast(mContext, "请完善资料");
            return;
        }
        String userId = TianShenUserUtil.getUserId(mContext);

        try {
            JSONObject mJson = new JSONObject();
            mJson.put("bank_name", bank_name);
            mJson.put("bank_id", bankId);
            mJson.put("customer_id", userId);
            mJson.put("card_user_name", card_user_name);
            mJson.put("card_num", card_num);
            mJson.put("reserved_mobile", reserved_mobile);
            GetBindVerifySms mGetBindVerifyCode = new GetBindVerifySms(mContext);
            mGetBindVerifyCode.getBindVerifySms(mJson, tvSeverityCode, true, new BaseNetCallBack<BindVerifySmsBean>() {
                @Override
                public void onSuccess(BindVerifySmsBean paramT) {
                    ToastUtil.showToast(mContext, "验证码发送成功");
                    bind_no = paramT.getData().getBind_no();
                    refreshSeverityTextUI();
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

    /**
     * 绑定银行卡
     */
    private void bindBankCard() {

        if (mBankCardInfoBean == null) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }

        String customer_id = TianShenUserUtil.getUserId(mContext);
        String card_user_name = etAuthBankCardPerson.getText().toString().trim();
        String card_num = et_auth_card_num.getText().toString().trim();
        String reserved_mobile = etBankCardPhoneNum.getText().toString().trim();
        String verify_code = etSeverityCode.getText().toString().trim();

        String bank_name = tv_bank_card.getText().toString(); //银行卡名字

        String bank_id = mBankCardInfoBean.getData().getBank_id();
        if (mBankListBean != null) {
            BankListItemBean itemBean = mBankListBean.getData().get(mCurrentBankCardIndex);
            bank_id = itemBean.getBank_id();
        }

        if (TextUtils.isEmpty(card_user_name)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }
        if (TextUtils.isEmpty(card_num)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }
        if (TextUtils.isEmpty(reserved_mobile)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }
        if (TextUtils.isEmpty(bank_name)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }
        if (TextUtils.isEmpty(bank_id)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }
        if (TextUtils.isEmpty(verify_code)) {
            ToastUtil.showToast(mContext, "请先获取验证码!");
            return;
        }

        try {
            JSONObject mJson = new JSONObject();
            mJson.put("customer_id", customer_id);
            mJson.put("card_user_name", card_user_name);
            mJson.put("card_num", card_num);
            mJson.put("reserved_mobile", reserved_mobile);
            mJson.put("verify_code", verify_code);
            mJson.put("bank_name", bank_name);
            mJson.put("bank_id", bank_id);
            mJson.put("bind_no", bind_no);
            BindBankCard mBindBankCard = new BindBankCard(mContext);
            mBindBankCard.bindBankCard(mJson, tvAuthInfoPost, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    int code = paramT.getCode();
                    if (code == 0) {
                        ToastUtil.showToast(mContext, "绑卡成功!");
                        backActivity();
                    }
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
