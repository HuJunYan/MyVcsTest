package com.tianshen.cash.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.model.BankListBean;
import com.tianshen.cash.model.BankListItemBean;
import com.tianshen.cash.model.BindVerifySmsBean;
import com.tianshen.cash.model.IdNumInfoBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.BindBankCard;
import com.tianshen.cash.net.api.GetAllBankList;
import com.tianshen.cash.net.api.GetBindVerifySms;
import com.tianshen.cash.net.api.GetCity;
import com.tianshen.cash.net.api.GetIdNumInfo;
import com.tianshen.cash.net.api.GetProvince;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 银行卡信息
 */

public class AuthBankCardActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_bank_province)
    TextView tv_bank_province;
    @BindView(R.id.tv_bank_city)
    TextView tv_bank_city;
    @BindView(R.id.rl_bank_card)
    RelativeLayout rl_bank_card;
    @BindView(R.id.rl_province)
    RelativeLayout rl_province;
    @BindView(R.id.rl_city)
    RelativeLayout rl_city;
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

    @BindView(R.id.tv_auth_bank_card_title)
    TextView tv_auth_bank_card_title;

    TextView tv_dialog_hami_get_verify_code;

    private BankListBean mBankListBean; //银行卡列表数据
    private ArrayList<String> mDialogData;// //银行卡列表dialog数据

    private String bind_no;//获取验证码时候得到的


    private int mCurrentBankCardIndex;//当前用户选择银行卡的位置

    private int mStartTime = 59;


    private static final int MSG_SEVERITY_TIME = 1;
    private static final int MSG_DIALOG_SEVERITY_TIME = 2;
    private static final int MSG_SEVERITY_DELAYED = 1 * 1000;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEVERITY_TIME:
                    refreshSeverityTextUI();
                    break;
                case MSG_DIALOG_SEVERITY_TIME:
                    refreshDialogSeverityTextUI();
                    break;
            }
        }
    };
    private AddressBean mProvinceBean;
    private ArrayList<String> mProvinceData;
    private int mProvincePosition;
    private AddressBean mCityBean;
    private ArrayList<String> mCityData;
    private int mCityPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIdNumInfo();
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
        rl_province.setOnClickListener(this);
        rl_city.setOnClickListener(this);
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
            case R.id.rl_province:
                initProvinceData();
                break;
            case R.id.rl_city:
                initCityData();
                break;
            case R.id.tv_severity_code:
                tvSeverityCode.setEnabled(false);
                initSeverityCode();
                break;
        }
    }

    private void initProvinceData() {

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GetProvince getProvince = new GetProvince(mContext, 2);
        getProvince.getProvince(jsonObject, rl_province, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mProvinceBean = paramT;
                        parserProvinceListData();
                        showProvinceListDialog();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
    }

    /**
     * 解省数据给dialog用
     */
    private void parserProvinceListData() {
        ArrayList<AddressBean.Data> datas = mProvinceBean.getData();
        if (datas != null) {
            mProvinceData = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                AddressBean.Data data = datas.get(i);
                String provice_name = data.getProvice_name();
                mProvinceData.add(provice_name);
            }
        }
    }

    /**
     * 显示省的Dialog
     */
    private void showProvinceListDialog() {
        if (mProvinceData == null) {
            ToastUtil.showToast(mContext, "请稍后再试");
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择省份")
                .items(mProvinceData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mProvincePosition = position;
                        resetCityData();
                        tv_bank_province.setText(mProvinceData.get(position));
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
    }

    private void resetCityData() {
        tv_bank_city.setText("");
        mCityData = null;
        mCityBean = null;
        mCityPosition = 0;
    }

    private void initCityData() {
        if (mProvinceData == null) {
            ToastUtil.showToast(mContext, "请先选择开户行省");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        AddressBean.Data data = mProvinceBean.getData().get(mProvincePosition);
        String province_id = data.getProvice_id();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("province_id", province_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetCity getCity = new GetCity(mContext, 2);
        getCity.getCity(jsonObject, rl_city, true,
                new BaseNetCallBack<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean paramT) {
                        mCityBean = paramT;
                        parserCityListData();
                        showCityListDialog();
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {
                    }
                });

    }

    /**
     * 解城市数据给dialog用
     */
    private void parserCityListData() {
        ArrayList<AddressBean.Data> datas = mCityBean.getData();
        mCityData = new ArrayList<>();
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                AddressBean.Data data = datas.get(i);
                String city_name = data.getCity_name();
                mCityData.add(city_name);
            }
        }

    }

    /**
     * 显示城市的Dialog
     */
    private void showCityListDialog() {

        if (mCityData == null) {
            ToastUtil.showToast(mContext, "请稍后再试");
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择城市")
                .items(mCityData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCityPosition = position;
                        tv_bank_city.setText(mCityData.get(position));
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
    }

    /**
     * 得到用户认证的信息
     */
    private void initIdNumInfo() {

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetIdNumInfo getIdNumInfo = new GetIdNumInfo(mContext);

        getIdNumInfo.getIdNumInfo(jsonObject, false,
                new BaseNetCallBack<IdNumInfoBean>() {
                    @Override
                    public void onSuccess(IdNumInfoBean paramT) {
                        if (paramT == null || paramT.getData() == null) {
                            return;
                        }
                        String name = paramT.getData().getReal_name();
                        etAuthBankCardPerson.setText(name);
                    }

                    @Override
                    public void onFailure(String url, int errorType, int errorCode) {

                    }
                });
    }

    /**
     * 得到银行卡列表数据
     */
    private void initBankListData() {
        try {
            JSONObject jsonObject = new JSONObject();
            String userID = TianShenUserUtil.getUserId(mContext);
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userID);
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
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                BankListItemBean bankListItemBean = datas.get(i);
                String bank_name = bankListItemBean.getBank_name();
                mDialogData.add(bank_name);
            }
        }
    }


    /**
     * 显示银行卡列表Dialog
     */
    private void showBankListDialog() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext)
                .title("选择银行卡")
                .items(mDialogData)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCurrentBankCardIndex = position;
                        refreshBankUI();

                        LogUtil.d("abc", "showBankListDialog---onSelection");
                    }
                });
        if (!this.isFinishing()) {
            builder.show();
        }
    }

    /**
     * 刷新银行卡UI，吧用户选择的银行卡设置到EditText上
     */
    private void refreshBankUI() {
        if (mDialogData != null) {
            String bankCardName = mDialogData.get(mCurrentBankCardIndex);
            tv_bank_card.setText(bankCardName);
        }

    }


    /**
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()) {
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
     * 刷新验证码UI
     */
    private void refreshDialogSeverityTextUI() {

        if (isFinishing()) {
            return;
        }

        tv_dialog_hami_get_verify_code.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            tv_dialog_hami_get_verify_code.setText("重获取验证码");
            mStartTime = 59;
            tv_dialog_hami_get_verify_code.setEnabled(true);
            mHandler.removeMessages(MSG_DIALOG_SEVERITY_TIME);
        } else {
            tv_dialog_hami_get_verify_code.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_DIALOG_SEVERITY_TIME, MSG_SEVERITY_DELAYED);
        }

    }

    /**
     * 得到验证码
     */
    private void initSeverityCode() {

        String bank_name = tv_bank_card.getText().toString().trim(); //银行卡名字
        String card_user_name = etAuthBankCardPerson.getText().toString().trim();
        String card_num = et_auth_card_num.getText().toString().trim();
        String reserved_mobile = etBankCardPhoneNum.getText().toString().trim();

        String bankId = "";
        if (mBankListBean != null) {
            BankListItemBean itemBean = mBankListBean.getData().get(mCurrentBankCardIndex);
            bankId = itemBean.getBank_id();
        }

        if (TextUtils.isEmpty(bankId)) {
            ToastUtil.showToast(mContext, "请完善资料");
            tvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(tv_bank_card.getText())) {
            ToastUtil.showToast(mContext, "请完善资料");
            tvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(card_user_name)) {
            ToastUtil.showToast(mContext, "请完善资料");
            tvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(card_num)) {
            ToastUtil.showToast(mContext, "请完善资料");
            tvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(reserved_mobile)) {
            ToastUtil.showToast(mContext, "请完善资料");
            tvSeverityCode.setEnabled(true);
            return;
        }
        String userId = TianShenUserUtil.getUserId(mContext);

        try {
            JSONObject mJson = new JSONObject();
            mJson.put("bank_name", bank_name);
            mJson.put("bank_id", bankId);
            mJson.put(GlobalParams.USER_CUSTOMER_ID, userId);
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

        String customer_id = TianShenUserUtil.getUserId(mContext);
        String card_user_name = etAuthBankCardPerson.getText().toString().trim();
        String card_num = et_auth_card_num.getText().toString().trim();
        String reserved_mobile = etBankCardPhoneNum.getText().toString().trim();
        String verify_code = etSeverityCode.getText().toString().trim();

        String bank_name = tv_bank_card.getText().toString(); //银行卡名字

        String bank_id = "";
        if (mBankListBean != null) {
            BankListItemBean itemBean = mBankListBean.getData().get(mCurrentBankCardIndex);
            bank_id = itemBean.getBank_id();
        }
        String city_code = "";
        if (mCityBean != null) {
            city_code = mCityBean.getData().get(mCityPosition).getCity_id();
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
        if (TextUtils.isEmpty(city_code)) {
            ToastUtil.showToast(mContext, "请先完善资料!");
            return;
        }

        try {
            JSONObject mJson = new JSONObject();
            mJson.put(GlobalParams.USER_CUSTOMER_ID, customer_id);
            mJson.put("card_user_name", card_user_name);
            mJson.put("card_num", card_num);
            mJson.put("reserved_mobile", reserved_mobile);
            mJson.put("verify_code", verify_code);
            mJson.put("bank_name", bank_name);
            mJson.put("bank_id", bank_id);
            mJson.put("bind_no", bind_no);
            mJson.put("city_code", city_code);
            BindBankCard mBindBankCard = new BindBankCard(mContext);
            mBindBankCard.bindBankCard(mJson, tvAuthInfoPost, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    int code = paramT.getCode();
                    if (code == 0) {
                        ToastUtil.showToast(mContext, "绑卡成功!");
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        backActivity();

//                        showHaMiVerifyCodeDialog();
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


    /**
     * 显示哈密银行绑卡验证码dialog
     */
    private void showHaMiVerifyCodeDialog() {

        if (isFinishing()) {
            return;
        }

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_hami_verify_code, null, false);
        Dialog mVerifyCodeDialog = new Dialog(mContext, R.style.MyDialog);
        mVerifyCodeDialog.setContentView(view);
        mVerifyCodeDialog.setCanceledOnTouchOutside(false);
        mVerifyCodeDialog.setCancelable(true);

        tv_dialog_hami_get_verify_code = (TextView) view.findViewById(R.id.tv_dialog_hami_get_verify_code);
        final EditText et_dialog_verify_code = (EditText) view.findViewById(R.id.et_dialog_hami_verify_code);
        TextView tv_dialog_hami_ok = (TextView) view.findViewById(R.id.tv_dialog_hami_ok);

        tv_dialog_hami_get_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHaMiSeverityCode();
            }
        });

        tv_dialog_hami_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verufy_code = et_dialog_verify_code.getText().toString().trim();
                if (TextUtils.isEmpty(verufy_code)) {
                    ToastUtil.showToast(mContext, "输入验证码");
                    return;
                }
                //todo 绑定哈密银行验证码
            }
        });
        mVerifyCodeDialog.show();
    }

    /**
     * 得到哈密银行绑定银行卡验证码
     */
    private void getHaMiSeverityCode() {
        mHandler.removeMessages(MSG_SEVERITY_TIME);
        mStartTime = 59;
        //todo 得到验证码成功后调用
        refreshDialogSeverityTextUI();
    }

}
