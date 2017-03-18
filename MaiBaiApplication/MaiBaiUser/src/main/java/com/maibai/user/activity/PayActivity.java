package com.maibai.user.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maibai.user.R;
import com.maibai.user.adapter.StagesTypeAdapter;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.InstallmentInfoItemBean;
import com.maibai.user.model.NearByMerchantItemBean;
import com.maibai.user.model.PreOrderAmountBean;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.GetPreOrderAmount;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.GetTelephoneUtils;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.MainUtil;
import com.maibai.user.utils.SharedPreferencesUtil;
import com.maibai.user.utils.TelephoneScreenChangeUtils;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.utils.UploadToServerUtil;
import com.maibai.user.utils.Utils;
import com.maibai.user.utils.ViewUtil;
import com.maibai.user.view.MyGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, UploadToServerUtil.UploadCallBack,AdapterView.OnItemClickListener{
    private boolean mIsPreOrderAmountReturnError = false;
    private boolean mIsPreOrderAmountReturn = false;
    private boolean mIsSaveInfoByClickBtn = false;
    private boolean mIsCallRecordUpload = false;
    private boolean mIsCallRecordPassByServer = false;
    private boolean mIsContactsUpload = false;
    private boolean mIsContactsPassByServer = false;
    private long mRealPrice = 0;
    private String mMerchantId;
    private String mPrice;
    private String mPreOrderAmountReturnErrorMsg;
    private NearByMerchantItemBean merchant;
    private EditText et_price;
    private Button bt_confirm;
    private TextView tv_repay_total;
    private MyGridView mgv_stages_type;
    private Timer timer = null;
    private CheckBox check_agree;
    private ImageView iv_merchan_logo;
    private TextView tv_merchant_name;
    private TextView tv_protocol;
    private LinearLayout layout_pro_container;
    private LinearLayout ll_merchant_info;
    private TextView tv_discount_money;
    private TextView tv_real_pay;
    private TextView tv_shoufu;
    private RelativeLayout rl_shoufu;
    private RelativeLayout rl_input;
    private RelativeLayout rl_real_pay, rl_all_discount;
    private LinearLayout ll_downpay_edit;
    private PayBroadcastReciver mPayBroadcastReciver = new PayBroadcastReciver();
    private UploadToServerUtil uploadUtil;
    private Context mContext = PayActivity.this;
    private final int REFRESH_NULL_VALUE_VIEW = 2;
    private PreOrderAmountBean mPreOrderAmountBean;
    private String down_type = GlobalParams.DOWN_TYPE_DEFAT;//1-自动计算方式,2-手动输入计算模式
    private String down_payment = "0";//首付金额（手动输入）
    private String discount = "0";//优惠额度
    private StagesTypeAdapter stagesTypeAdapter;
    private int stageType = 0;
    private String repayTime="-1";
    private List<InstallmentInfoItemBean> installmentInfoItemBeanList = new ArrayList<InstallmentInfoItemBean>();
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case REFRESH_NULL_VALUE_VIEW:
                    rl_all_discount.setVisibility(View.GONE);
                    rl_real_pay.setVisibility(View.INVISIBLE);
                    rl_shoufu.setVisibility(View.GONE);
                    break;
                case GlobalParams.STAGE_TYPE_REFRESH:
                    stagesTypeAdapter.notifyDataSetChanged();
                    break;
                case GlobalParams.DIALOG_SHOW:
                    ViewUtil.createLoadingDialog(PayActivity.this, "数据加载中", false);
                    break;
                case GlobalParams.DIALOG_DISSMISS:
                    ViewUtil.cancelLoadingDialog();
                    break;
                case GlobalParams.REFRESH_CLEAR_VIEW:
                    tv_repay_total.setText("0");
                    updateView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
        Bundle bundle = getIntent().getExtras();
        mMerchantId = bundle.getString("merchant_id");
        try {
            merchant = (NearByMerchantItemBean) bundle.getSerializable("merchant");
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        stagesTypeAdapter = new StagesTypeAdapter(mContext, installmentInfoItemBeanList);
        mgv_stages_type.setAdapter(stagesTypeAdapter);
        init();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_pay;
    }

    @Override
    protected void findViews() {
        et_price = (EditText) findViewById(R.id.et_price);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        check_agree = (CheckBox) findViewById(R.id.check_agree);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        layout_pro_container = (LinearLayout) findViewById(R.id.layout_pro_container);
        rl_input = (RelativeLayout) findViewById(R.id.rl_input);
        iv_merchan_logo = (ImageView) findViewById(R.id.iv_merchan_logo);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        rl_all_discount = (RelativeLayout) findViewById(R.id.rl_all_discount);
        rl_real_pay = (RelativeLayout) findViewById(R.id.rl_real_pay);
        ll_merchant_info = (LinearLayout) findViewById(R.id.ll_merchant_info);
        tv_discount_money = (TextView) findViewById(R.id.tv_discount_money);
        tv_real_pay = (TextView) findViewById(R.id.tv_real_pay);
        rl_shoufu = (RelativeLayout) findViewById(R.id.rl_shoufu);
        tv_shoufu = (TextView) findViewById(R.id.tv_shoufu);
        mgv_stages_type = (MyGridView) findViewById(R.id.mgv_stages_type);
        ll_downpay_edit = (LinearLayout) findViewById(R.id.ll_downpay_edit);
        tv_repay_total=(TextView)findViewById(R.id.tv_repay_total);
        int width = new GetTelephoneUtils(mContext).getWindowWidth() / GlobalParams.PAY_IMG_PROPORTION;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        iv_merchan_logo.setLayoutParams(params);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        et_price.addTextChangedListener(water);
        check_agree.setOnCheckedChangeListener(this);
        tv_protocol.setOnClickListener(this);
        rl_input.setOnClickListener(this);
        ll_downpay_edit.setOnClickListener(this);
        mgv_stages_type.setOnItemClickListener(this);
    }

    private void init() {
        layout_pro_container.setVisibility(View.VISIBLE);
        bt_confirm.setClickable(true);
        bt_confirm.setBackgroundResource(R.drawable.select_bt);
        getPreOrderAmount(GlobalParams.INIT_PAGE);
        uploadUtil = new UploadToServerUtil(mContext);
        uploadUtil.setCallBack(this);
        uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
        if (null == merchant) {
            ll_merchant_info.setVisibility(View.INVISIBLE);
            return;
        }
        tv_merchant_name.setText(merchant.getName());
    }

    @Override
    public void uploadSuccessCallBack(int type) {
        //上传通讯录、通话记录等的回调
        switch (type) {
            case GlobalParams.UPLOADCALLCONTACTS:
                //上传联系人成功
                mIsContactsUpload = true;
                if (mIsSaveInfoByClickBtn == true) {
                    bt_confirm.performClick();
                }
                break;
            case GlobalParams.UPLOADCALLRECORD:
                //上传通话记录成功
                mIsCallRecordUpload = true;
                if ((GlobalParams.MONEY_LEVER_1ST + "").equals(UserUtil.getCustomerAmount(mContext))) { // 用户需要去提交联系人信息
                    uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
                } else if (mIsSaveInfoByClickBtn == true) {
                    bt_confirm.performClick();
                }
                break;
        }
    }

    @Override
    public void uploadFailCallBack(int type) {
        switch (type) {
            case GlobalParams.UPLOADCALLCONTACTS:
                //上传联系人成功
                mIsContactsUpload = false;
                break;
            case GlobalParams.UPLOADCALLRECORD:
                //上传通话记录成功
                mIsCallRecordUpload = false;
                break;
        }
    }

    private TextWatcher water = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("ret", "beforeTextChanged ");
            mIsPreOrderAmountReturn = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("ret", "onTextChanged ");
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("ret", "afterTextChanged ");
            setTimer();
        }
    };

    private void setTimer() {
        timerToBeNull();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerToBeNull();
                down_type = GlobalParams.DOWN_TYPE_DEFAT;
                getPreOrderAmount(GlobalParams.NOT_INIT_PAGE);
            }
        }, 1500);
    }

    private void timerToBeNull() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void getPreOrderAmount(final String isInit) {
        try {
            mPrice = et_price.getText().toString();
            if (mPrice == null || "".equals(mPrice)) {
                handler.sendEmptyMessage(REFRESH_NULL_VALUE_VIEW);
                mPrice = "0";
            }
            mPrice = ((long) (Double.parseDouble(mPrice) * 100)) + "";
            if(isInit.equals(GlobalParams.NOT_INIT_PAGE)&&"0".equals(mPrice)){
                mPreOrderAmountBean.getData().setDiscount_amount("0");
                mPreOrderAmountBean.getData().setDown_payment("0");
                for(int i=0;i<installmentInfoItemBeanList.size();i++){
                    installmentInfoItemBeanList.get(i).setRepay_total("0");
                }
                handler.sendEmptyMessage(GlobalParams.REFRESH_CLEAR_VIEW);
                return;
            }
            GetPreOrderAmount getPreOrderAmount = new GetPreOrderAmount(mContext);
            JSONObject jsonObject = new JSONObject();
            handler.sendEmptyMessage(GlobalParams.DIALOG_SHOW);
            jsonObject.put("is_init", isInit);
            jsonObject.put("customer_id", UserUtil.getId(mContext));
            jsonObject.put("merchant_id", mMerchantId);
            jsonObject.put("amount", mPrice);
            jsonObject.put("type", "2");
            jsonObject.put("down_type", down_type);
            jsonObject.put("down_payment", down_payment);
            getPreOrderAmount.getPreOrderAmount(jsonObject, null, false, new BaseNetCallBack<PreOrderAmountBean>() {
                @Override
                public void onSuccess(PreOrderAmountBean paramT) {
                    handler.sendEmptyMessage(GlobalParams.DIALOG_DISSMISS);
                    mPreOrderAmountBean = paramT;
                    mIsPreOrderAmountReturn = true;
                    installmentInfoItemBeanList.clear();
                    installmentInfoItemBeanList.addAll(paramT.getData().getInstallment_info());
                    mIsPreOrderAmountReturnError = false;
                    stageType = installmentInfoItemBeanList.size() - 1;
                    for(int i=0;i<installmentInfoItemBeanList.size();i++){
                        if(repayTime.equals(installmentInfoItemBeanList.get(i).getRepay_times())){
                            stageType=i;
                        }
                    }
                    transList(stageType);
                    updateInstallData();
                    handler.sendEmptyMessage(GlobalParams.STAGE_TYPE_REFRESH);
                    if (isInit.equals(GlobalParams.INIT_PAGE)) {
                        tv_merchant_name.setText(paramT.getData().getMerchant_name());
                    } else {
                        mRealPrice = (long) (Double.parseDouble(paramT.getData().getAmount()));
                        // TODO 在这里处理预订单金额计算的数据及ui动态展示
                        updateView();
                        rl_real_pay.setVisibility(View.VISIBLE);
                        tv_real_pay.setText(Double.valueOf(paramT.getData().getAmount()) / 100 + "");
                    }
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    handler.sendEmptyMessage(GlobalParams.DIALOG_DISSMISS);
                    mIsPreOrderAmountReturnError = true;
                    ResponseBean responseBean = GsonUtil.json2bean(result, ResponseBean.class);
                    mPreOrderAmountReturnErrorMsg = responseBean.getMsg();
                }
            });
        } catch (Exception e) {
            handler.sendEmptyMessage(GlobalParams.DIALOG_DISSMISS);
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void updateView() {
        if (Double.valueOf(mPreOrderAmountBean.getData().getDiscount_amount()) > 0) {
            rl_all_discount.setVisibility(View.VISIBLE);
            tv_discount_money.setText("-¥" + Double.valueOf(mPreOrderAmountBean.getData().getDiscount_amount()) / 100);
            discount = mPreOrderAmountBean.getData().getDiscount_amount();
        } else {
            rl_all_discount.setVisibility(View.GONE);
        }
        if (Double.valueOf(mPreOrderAmountBean.getData().getDown_payment()) > 0) {
            rl_shoufu.setVisibility(View.VISIBLE);
            tv_shoufu.setText("¥" + Double.valueOf(mPreOrderAmountBean.getData().getDown_payment()) / 100);
            down_payment = mPreOrderAmountBean.getData().getDown_payment();
        } else {
            rl_shoufu.setVisibility(View.GONE);
        }
    }

    private void updateInstallData() {
        String repayToal=installmentInfoItemBeanList.get(stageType).getRepay_total();
        if("".equals(repayToal)||null==repayToal){
            repayToal="0";
        }
        tv_repay_total.setText((long)Double.parseDouble(repayToal)/100+"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                handleConfirm();
                break;
            case R.id.tv_protocol:
                Bundle bundle = new Bundle();
                bundle.putInt("pro_type", GlobalParams.PRO_SERVER); // 用户服务器协议
                gotoActivity(mContext, ProtocolActivity.class, bundle);
                break;
            case R.id.rl_input:
                et_price.setFocusable(true);
                InputMethodManager manager = (InputMethodManager) et_price.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.ll_downpay_edit:
                showDownpaymentEditDialog();
                break;
            default:
                break;
        }
    }

    private void showDownpaymentEditDialog() {
        final TelephoneScreenChangeUtils telephoneScreenChangeUtils = new TelephoneScreenChangeUtils(mContext);
        telephoneScreenChangeUtils.changeDark();
        final Dialog dialog = new Dialog(mContext, R.style.dialog);
        View view_downpay_edit = LayoutInflater.from(mContext).inflate(R.layout.view_downpay_edit, null);
        Button bt_cancel = (Button) view_downpay_edit.findViewById(R.id.bt_cancel);
        Button bt_confirm = (Button) view_downpay_edit.findViewById(R.id.bt_confirm);
        final EditText et_down_pay = (EditText) view_downpay_edit.findViewById(R.id.et_down_pay);
        et_down_pay.setText(Double.parseDouble(down_payment) / 100 + "");
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                telephoneScreenChangeUtils.changeLight();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String downPay = et_down_pay.getText().toString().trim();
                if("".equals(downPay)){
                    downPay="0";
                }
                if (Double.parseDouble(et_price.getText().toString().trim()) - Double.parseDouble(discount) / 100 <= Double.parseDouble(downPay)) {
                    ToastUtil.showToast(mContext, "首付额度必须小于需支付额度");
                    return;
                }
                String downPaymentLowest=mPreOrderAmountBean.getData().getDown_payment_lowest();
                if(null==downPaymentLowest||"".equals(downPaymentLowest)){
                    downPaymentLowest="0";
                }
                Double downPaymentLowestDouble=Double.valueOf(downPaymentLowest)/100;
                if(Double.parseDouble(et_price.getText().toString().trim())<downPaymentLowestDouble){
                    ToastUtil.showToast(mContext, "首付金额不能低于"+downPaymentLowestDouble+"元！");
                    return;
                }
                dialog.dismiss();
                telephoneScreenChangeUtils.changeLight();
                if ("".equals(downPay)) {
                    down_payment = "0";
                } else {
                    long downPaymentLong = (long) (Double.parseDouble(downPay) * 100);
                    if (!down_payment.equals(downPaymentLong + "")) {
                        down_payment = downPaymentLong + "";
                        tv_shoufu.setText(downPaymentLong / 100 + "");
                        down_type = GlobalParams.DOWN_TYPE_CHANGED;
                        getPreOrderAmount(GlobalParams.NOT_INIT_PAGE);
                    }
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(view_downpay_edit);
        et_down_pay.setFocusable(true);
    }

    private void handleConfirm() {
        if (mIsPreOrderAmountReturnError) {
            ToastUtil.showToast(mContext, mPreOrderAmountReturnErrorMsg);
            return;
        }
        if (mRealPrice == 0 || !mIsPreOrderAmountReturn) {
            ToastUtil.showToast(mContext, "数据加载中，请稍后重试");
            return;
        }

        SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
        String location = share.getString("location");
        String province = share.getString("province");
        String city = share.getString("city");
        String country = share.getString("country");
        String address = share.getString("address");
        if (!isInputValid()) {
            ToastUtil.showToast(mContext, "请输入消费金额！");
            return;
        }

        mPrice = ((long) (Double.parseDouble(et_price.getText().toString()) * 100)) + "";
        if (!mIsCallRecordUpload && !mIsCallRecordPassByServer) {
            uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLRECORD);
            mIsSaveInfoByClickBtn = true;
        } else if (MainUtil.isNewUser(mContext) && !mIsContactsUpload && !mIsContactsPassByServer) {
            uploadUtil.uploadUserInfo(GlobalParams.UPLOADCALLCONTACTS);
            mIsSaveInfoByClickBtn = true;
        } else {
            if ("".equals(location) || null == location || "".equals(province) || null == province || "".equals(city) || null == city || "".equals(country) || null == country || "".equals(address) || null == address) {
                // TODO 这里可以调用一下获取权限打开步骤的接口
                ToastUtil.showToast(mContext, "定位参数有误，请检查定位权限并重启", Toast.LENGTH_LONG);
                return;
            }
            gotoTargetActivity();
        }
    }

    private boolean isInputValid() {
        String inputPriceStr = et_price.getText().toString().trim();
        if (inputPriceStr == null || "".equals(inputPriceStr)) {
            return false;
        }
        long inputPriceLong = (long) (Double.parseDouble(inputPriceStr) * 100);
        if (inputPriceLong > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void gotoTargetActivity() {
        int amountType = Utils.getAmountType(mContext);
        switch (amountType) {
            case GlobalParams.AMOUNT_TYPE_0:
                payMoneyHandle0();
                break;
            case GlobalParams.AMOUNT_TYPE_200:
            case GlobalParams.AMOUNT_TYPE_500:
            case GlobalParams.AMOUNT_TYPE_500_PLUS:
                payMoneyHandle();
                break;
        }
    }

    private boolean isNextMonth(){
            if("1".equals(installmentInfoItemBeanList.get(stageType).getRepay_times())){
                return true;
            }else {
                return false;
            }

    }
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("price", mPrice);
        bundle.putString("real_price", mRealPrice + "");
        bundle.putString("merchant_id", mMerchantId);
        bundle.putInt(GlobalParams.APPLY_TYPE_KEY, GlobalParams.APPLY_TYPE_INSTALLMENT);
        bundle.putString("down_type", down_type);
        bundle.putString("down_payment", down_payment);
        bundle.putString("id", installmentInfoItemBeanList.get(stageType).getId());
        if(installmentInfoItemBeanList.size()==0){
            return null;
        }
        if (isNextMonth()) {
            bundle.putString("repay_type", "next_month");
            UserUtil.setType(mContext, "1");
        } else {
            bundle.putString("repay_times", installmentInfoItemBeanList.get(stageType).getRepay_times());
            bundle.putString("repay_type", "installment");//分期
            UserUtil.setType(mContext, "2");
        }
        return bundle;
    }

    private void payMoneyHandle0() {
        String creditStep = UserUtil.getCreditStep(mContext);
        switch (creditStep){
            case GlobalParams.USER_STATUS_NEW:
                Bundle bundle=getBundle();
                if (null==bundle){
                    return;
                }
                gotoActivity(mContext, ContactsInfoActivity.class, bundle);
                break;
            case GlobalParams.HAVE_UPLOAD_IDCARD_INFO: {
                Intent intent = new Intent();
                intent.setClass(PayActivity.this, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
            }
                break;
            case GlobalParams.HAVE_SCAN_FACE: // 已经扫脸通过
                Bundle bundle1=getBundle();
                if(null==bundle1){
                    return;
                }
                gotoActivity(mContext, ImproveQuotaActivity.class, bundle1);
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO: {
                Intent intent = new Intent();
                intent.setClass(PayActivity.this, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
            }
                break;
        }
    }

    private void payMoneyHandle() {
        String creditStep = UserUtil.getCreditStep(mContext);
        Intent intent=new Intent();
        switch (creditStep){
            case GlobalParams.USER_STATUS_NEW:
                gotoActivity(mContext, ContactsInfoActivity.class, getBundle());
                break;
            case GlobalParams.HAVE_UPLOAD_IDCARD_INFO:
               intent.setClass(PayActivity.this, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
                break;
            case GlobalParams.HAVE_SCAN_FACE:
                gotoActivity(mContext, ImproveQuotaActivity.class, getBundle());
                break;
            case GlobalParams.HAVE_UPLOAD_CONTACTS_INFO:
                intent.setClass(PayActivity.this, ResultActivity.class);
                intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_ID_CARD_TYPE);
                startActivityForResult(intent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_agree:
                if (isChecked) {
                    bt_confirm.setClickable(true);
                    bt_confirm.setBackgroundResource(R.drawable.select_bt);
                } else {
                    bt_confirm.setClickable(false);
                    bt_confirm.setBackgroundResource(R.drawable.button_gray);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    Intent intent=new Intent();
                    intent.setClass(PayActivity.this, ResultActivity.class);
                    intent.putExtra(GlobalParams.SCAN_ID_CARD_KEY, GlobalParams.SCAN_FACE_TYPE);
                    startActivityForResult(intent, GlobalParams.PAGE_INTO_LIVENESS);
                    break;
                case GlobalParams.PAGE_INTO_LIVENESS:
                    gotoActivity(mContext, ImproveQuotaActivity.class, getBundle());
                    break;
            }
        } else if (resultCode == GlobalParams.RETURN_FROM_ACTIVITY_ERROR) {
            switch (requestCode) {
                case GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE:
                    // TODO 转化手动输入身份证，完了以后再去扫脸
                    Bundle bundle = getBundle();
                    bundle.putBoolean(GlobalParams.IF_NEED_SCAN_FACE_KEY, true);
                    gotoActivity(mContext, ActivateTheQuotaActivity.class, bundle);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPayBroadcastReciver);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalParams.PASS_CONTACT_ACTION);
        intentFilter.addAction(GlobalParams.PASS_CALL_RECORD_ACTION);
        intentFilter.addAction(GlobalParams.WX_SHOUFU_SUCCESS);
        mContext.registerReceiver(mPayBroadcastReciver, intentFilter);
    }


    private void transList(int position) {
        if (position >= installmentInfoItemBeanList.size()) {
            position = installmentInfoItemBeanList.size() - 1;
            stageType = position;
        }
        installmentInfoItemBeanList.get(position).setFocus(true);
        for (int i = 0; i < installmentInfoItemBeanList.size(); i++) {
            if (i == position) {
                installmentInfoItemBeanList.get(i).setFocus(true);
            } else {
                installmentInfoItemBeanList.get(i).setFocus(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        stageType = position;
        repayTime=installmentInfoItemBeanList.get(position).getRepay_times();
        transList(position);
        updateInstallData();
        stagesTypeAdapter.notifyDataSetChanged();
    }

    private class PayBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String topActivityName = Utils.getTopActivityName(PayActivity.this);
            Log.d("ret", "topActiviyName = " + topActivityName);
            if (topActivityName.contains("PayActivity")) {
                if (GlobalParams.PASS_CALL_RECORD_ACTION.equals(action)) {
                    mIsCallRecordPassByServer = true;
                } else if (GlobalParams.PASS_CONTACT_ACTION.equals(action)) {
                    mIsContactsPassByServer = true;
                }
                if (mIsSaveInfoByClickBtn == true) {
                    bt_confirm.performClick();
                }
            } else if (GlobalParams.WX_SHOUFU_SUCCESS.equals(intent.getAction())) {
                UserUtil.setStatus(mContext, GlobalParams.HAVE_BEEN_VERIFY);
                backActivity();
            }
        }
    }
}
