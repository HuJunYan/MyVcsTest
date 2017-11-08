package com.tianshen.cash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import butterknife.OnClick;

import static com.tianshen.cash.R.id.et_auth_card_num;
import static com.tianshen.cash.R.id.tv_bank_card;



/**
 * 绑定收款银行卡界面
 */
public class AuthBlankActivity extends BaseActivity {

    private static final int MSG_SEVERITY_TIME = 1;
    private static final int MSG_DIALOG_SEVERITY_TIME = 2;
    private static final int MSG_SEVERITY_DELAYED = 1 * 1000;
    @BindView(R.id.tv_auth_bank_card_back)
    TextView mTvAuthBankCardBack;
    @BindView(R.id.tv_auth_bank_card_title)
    TextView mTvAuthBankCardTitle;
    @BindView(R.id.tv_auth_bank_card_person)
    TextView mTvAuthBankCardPerson;
    @BindView(R.id.et_auth_bank_card_person)
    EditText mEtAuthBankCardPerson;
    @BindView(R.id.tv_bank_card)
    TextView mTvBankCard;
    @BindView(R.id.rl_bank_card)
    RelativeLayout mRlBankCard;
    @BindView(et_auth_card_num)
    EditText mEtAuthCardNum;
    @BindView(R.id.tv_bank_province)
    TextView mTvBankProvince;
    @BindView(R.id.rl_province)
    RelativeLayout mRlProvince;
    @BindView(R.id.et_bank_card_phone_num)
    EditText mEtBankCardPhoneNum;
    @BindView(R.id.et_severity_code)
    EditText mEtSeverityCode;
    @BindView(R.id.tv_severity_code)
    TextView mTvSeverityCode;
    @BindView(R.id.tv_auth_info_post)
    TextView mTvAuthInfoPost;
    private BankListBean mBankListBean;
    private ArrayList<String> mDialogData;
    private int mCurrentBankCardIndex;
    private AddressBean mProvinceBean;
    private AddressBean mCityBean;
    private ArrayList<String> mProvinceData;
    private int mProvincePosition;
    private ArrayList<String> mCityData;
    private String bind_no;
    private int mStartTime = 59;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEVERITY_TIME:
                    refreshSeverityTextUI();
                    break;
                case MSG_DIALOG_SEVERITY_TIME:
//                 NothingTODO    refreshDialogSeverityTextUI();
                    break;
            }
        }
    };
    private int mCityPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_auth_blank;
    }

    @Override
    protected void findViews() {
        initIdNumInfo();

    }

    @Override
    protected void setListensers() {

    }

    @OnClick({R.id.tv_auth_bank_card_back, tv_bank_card, R.id.tv_bank_province, R.id.tv_severity_code, R.id.tv_auth_info_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_bank_card_back:
                backActivity();
                break;
            case tv_bank_card:
                initBankListData();
                break;
            case R.id.tv_bank_province:
                initProvinceData();
                break;
            case R.id.tv_severity_code:
                initSeverityCode();

                break;
            case R.id.tv_auth_info_post:
                bindBankCard();
                break;
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
                        mEtAuthBankCardPerson.setText("胡俊焰");
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
     * 刷新银行卡UI，把用户选择的银行卡设置到EditText上
     */
    private void refreshBankUI() {
        if (mDialogData != null) {
            String bankCardName = mDialogData.get(mCurrentBankCardIndex);
            mTvBankCard.setText(bankCardName);
        }

    }

    /**
     * 得到省信息
     */
    private void initProvinceData() {

        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final GetProvince getProvince = new GetProvince(mContext);
        getProvince.getProvince(jsonObject, mTvBankProvince, true,
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
     * 得到城市信息
     */
    private void initCityData() {
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

        final GetCity getCity = new GetCity(mContext);
        getCity.getCity(jsonObject, mTvBankProvince, true,
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
     * 解省数据给dialog用
     */
    private void parserProvinceListData() {
        ArrayList<AddressBean.Data> datas = mProvinceBean.getData();
        mProvinceData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            AddressBean.Data data = datas.get(i);
            String provice_name = data.getProvice_name();
            mProvinceData.add(provice_name);
        }
    }

    /**
     * 解城市数据给dialog用
     */
    private void parserCityListData() {
        ArrayList<AddressBean.Data> datas = mCityBean.getData();
        mCityData = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            AddressBean.Data data = datas.get(i);
            String city_name = data.getCity_name();
            mCityData.add(city_name);
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
                        initCityData();
                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
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

                        String province = mProvinceData.get(mProvincePosition);
                        mCityPosition=position;
                        String city = mCityData.get(mCityPosition);
                        String address = province + "-" + city ;
                        mTvBankProvince.setText(address);

                    }
                });
        if (this != null && !isFinishing()) {
            builder.show();
        }
    }


    /**
     * 得到验证码
     */
    private void initSeverityCode() {

        //银行卡名字
        String bank_name = mTvBankCard.getText().toString().trim();
        String card_user_name = mEtAuthBankCardPerson.getText().toString().trim();
        String card_num = mEtAuthCardNum.getText().toString().trim();
        String reserved_mobile = mEtBankCardPhoneNum.getText().toString().trim();

        String bankId = "";
        if (mBankListBean != null) {
            BankListItemBean itemBean = mBankListBean.getData().get(mCurrentBankCardIndex);
            bankId = itemBean.getBank_id();
        }

        if (TextUtils.isEmpty(bankId)) {
            ToastUtil.showToast(mContext, "请完善资料");
            mTvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(mTvBankCard.getText())) {
            ToastUtil.showToast(mContext, "请完善资料");
            mTvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(card_user_name)) {
            ToastUtil.showToast(mContext, "请完善资料");
            mTvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(card_num)) {
            ToastUtil.showToast(mContext, "请完善资料");
            mTvSeverityCode.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(reserved_mobile)) {
            ToastUtil.showToast(mContext, "请完善资料");
            mTvSeverityCode.setEnabled(true);
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
            mGetBindVerifyCode.getBindVerifySms(mJson, mTvSeverityCode, true, new BaseNetCallBack<BindVerifySmsBean>() {
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
     * 刷新验证码UI
     */
    private void refreshSeverityTextUI() {

        if (isFinishing()) {
            return;
        }

        mTvSeverityCode.setText(mStartTime + "");
        mStartTime--;
        if (mStartTime == 0) {
            mTvSeverityCode.setText("重获取验证码");
            mStartTime = 59;
            mTvSeverityCode.setEnabled(true);
            mHandler.removeMessages(MSG_SEVERITY_TIME);
        } else {
            mTvSeverityCode.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_SEVERITY_TIME, MSG_SEVERITY_DELAYED);
        }

    }

    /**
     * 绑定银行卡
     */
    private void bindBankCard() {

        String customer_id = TianShenUserUtil.getUserId(mContext);
        String card_user_name = mEtAuthBankCardPerson.getText().toString().trim();
        String card_num = mEtAuthCardNum.getText().toString().trim();
        String reserved_mobile = mEtBankCardPhoneNum.getText().toString().trim();
        String verify_code = mEtSeverityCode.getText().toString().trim();
        //银行卡名字
        String bank_name = mTvBankCard.getText().toString();

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
            final JSONObject mJson = new JSONObject();
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
            LogUtil.i("test",mJson.toString());
            mBindBankCard.bindBankCard(mJson, mTvAuthInfoPost, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    int code = paramT.getCode();
                    if (code == 0) {
                        LogUtil.i("test",mJson.toString());
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




}
