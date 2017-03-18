package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.BankCardAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.base.MyApplication;
import com.maibei.merchants.model.BankListBean;
import com.maibei.merchants.model.BankListItemBean;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.GetBankList;
import com.maibei.merchants.net.api.GetReserveMobileVerifyCode;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RegexUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.maibei.merchants.view.ImageTextView;
import com.maibei.merchants.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14658 on 2016/7/26.
 */
public class AuthBankCardActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_next_ver;
    private Button bt_jump;
    private ImageButton ib_return_picture;

    private MyEditText et_card_holder;
    private MyEditText et_id_num;
    private MyEditText et_card_no;
    private ImageTextView tv_card_type;
    private MyEditText et_bank_branch;
    private MyEditText et_reserve_mobile;

    private String cardHolder = "";
    private String idNum = "";
    private String bankCardNo = "";
    private String cardType = "";
    private String bankbranch = "";
    private String reserveMobile = "";
    private  boolean isInit=false;
    private String cardId;
    private LinearLayout ll_flow;
    private BankCardAdapter bankCardAdapter;
    private ListView lv_bank_list;
    List<BankListItemBean> bankListItemBeans = new ArrayList<BankListItemBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        Intent intent=getIntent();
        isInit=false;
        if(null==intent){
            isInit=false;
        }else{
            Bundle bundle=intent.getExtras();
            if(null==bundle){
                isInit=false;
            }else{
                isInit=bundle.getBoolean("isInit",false);
            }
        }
        initView(isInit);
    }
    private void initView(boolean isInit){
        if(isInit) {
            ll_flow.setVisibility(View.VISIBLE);
            bt_jump.setVisibility(View.VISIBLE);
        }else{
            ll_flow.setVisibility(View.GONE);
            bt_jump.setVisibility(View.GONE);
        }
    }
    @Override
    protected int setContentView() {
        return R.layout.activity_bank_card;
    }

    @Override
    protected void findViews() {
        bt_next_ver = (Button) findViewById(R.id.bt_next_ver);
        bt_jump = (Button) findViewById(R.id.bt_jump);
        ib_return_picture = (ImageButton) findViewById(R.id.ib_return_picture);
        et_card_holder = (MyEditText) findViewById(R.id.et_card_holder);
        et_id_num = (MyEditText) findViewById(R.id.et_id_num);
        et_card_no = (MyEditText) findViewById(R.id.et_card_no);
        tv_card_type = (ImageTextView) findViewById(R.id.tv_card_type);
        et_bank_branch = (MyEditText) findViewById(R.id.et_bank_branch);
        et_reserve_mobile = (MyEditText) findViewById(R.id.et_reserve_mobile);
        ll_flow=(LinearLayout)findViewById(R.id.ll_flow);
    }

    @Override
    protected void setListensers() {
        bt_next_ver.setOnClickListener(this);
        bt_jump.setOnClickListener(this);
        ib_return_picture.setOnClickListener(this);
        tv_card_type.setListener(new ImageTextView.ImageTextViewListener() {
            @Override
            public void onRightClick(View view) {
                showBankCardTypeDialog();
            }
        });
        et_card_holder.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                cardHolder = s;
                updateNextBtn();
            }
        });
        et_id_num.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                idNum = s;
                updateNextBtn();
            }
        });
        et_card_no.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                bankCardNo = s;
                updateNextBtn();
            }
        });
        tv_card_type.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                cardType = s;
                updateNextBtn();
            }
        });
        et_bank_branch.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                bankbranch = s;
                updateNextBtn();
            }
        });
        et_reserve_mobile.setChangeListener(new ChangeInterface() {
            @Override
            public void change(String s) {
                reserveMobile = s;
                updateNextBtn();
            }
        });
    }

    private void updateNextBtn() {
        if (cardHolder != "" && bankCardNo != "" && reserveMobile != "" && bankbranch != "" && cardType != ""&& idNum != "") {
            bt_next_ver.setEnabled(true);
            bt_next_ver.setTextColor(getResources().getColor(R.color.white));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next_ver:
                getReserveMobileVerifyCode();
                break;
            case R.id.ib_return_picture:
                if (isInit){
                    gotoActivity(mContext, ToExamineActivity.class, null);
                    ((MyApplication) getApplication()).clearTempActivityInBackStack(ToExamineActivity.class);
                }else{
                    backActivity();
                }

                break;
            case R.id.bt_jump:
                gotoActivity(mContext, ToExamineActivity.class, null);
                ((MyApplication) getApplication()).clearTempActivityInBackStack(ToExamineActivity.class);
                break;
            default:
                break;
        }
    }
    /**
     * 获取银行卡类型
     */
    private void showBankCardTypeDialog(){
        bankCardNo = et_card_no.getEditTextString().toString();
        if(TextUtils.isEmpty(bankCardNo)){
            ToastUtil.showToast(mContext, "请输入银行卡号");
            return;
        }

        if (!RegexUtil.IsBankCard(bankCardNo)) {
            ToastUtil.showToast(mContext, "请正确填写银行卡号");
            return;
        }
        GetBankList getBankList = new GetBankList(this);
        JSONObject mJSONObject = new JSONObject();
        try{
            mJSONObject.put("merchant_id", UserUtil.getMerchantId(mContext));
            mJSONObject.put("bank_card", bankCardNo);
            getBankList.getBankList(mJSONObject, new BaseNetCallBack<BankListBean>() {
                @Override
                public void onSuccess(BankListBean paramT) {
                    if (paramT.getCode() == 0){
                        bankListItemBeans.clear();
                        bankListItemBeans.addAll(paramT.getData());

                        bankCardAdapter = new BankCardAdapter(mContext, bankListItemBeans);

                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.diaog_bank, null);
                        lv_bank_list = (ListView) view.findViewById(R.id.lv_bank_list);
                        lv_bank_list.setAdapter(bankCardAdapter);
                        final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                        builder.setView(view);
                        final AlertDialog dialog = builder.show();

                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        WindowManager windowManager = getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        params.height = (int)(display.getHeight())/2; ;
                        dialog.getWindow().setAttributes(params);

                        lv_bank_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BankListItemBean bean = (BankListItemBean) bankCardAdapter.getItem(position);
                                tv_card_type.setContentText(bean.getBank_name());
                                cardId = bean.getBank_id();
                                dialog.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    private void getReserveMobileVerifyCode(){
        cardHolder = et_card_holder.getEditTextString().toString();
        idNum = et_id_num.getEditTextString().toString();
        bankCardNo = et_card_no.getEditTextString().toString();
        cardType = tv_card_type.getContentText().trim();
        bankbranch = et_bank_branch.getEditTextString().toString();
        reserveMobile = et_reserve_mobile.getEditTextString().toString();
//        final String bankType = tv_card_type.getText().toString();
        if(TextUtils.isEmpty(cardHolder)){
            ToastUtil.showToast(mContext, "请输入持卡人姓名");
            return;
        }
        if(TextUtils.isEmpty(idNum)){
            ToastUtil.showToast(mContext, "请输入持卡人身份证号");
            return;
        }
        if(TextUtils.isEmpty(bankCardNo)){
            ToastUtil.showToast(mContext, "请输入银行卡号");
            return;
        }

        if (!RegexUtil.IsBankCard(bankCardNo)) {
            ToastUtil.showToast(mContext, "请正确填写银行卡号");
            return;
        }
        if(TextUtils.isEmpty(cardType)){
            ToastUtil.showToast(mContext, "银行卡类型不能为空");
            return;
        }
        if(TextUtils.isEmpty(cardType)){
            ToastUtil.showToast(mContext, "银行卡类型不能为空");
            return;
        }
        if(TextUtils.isEmpty(bankbranch)){
            ToastUtil.showToast(mContext, "请输入支行名称");
            return;
        }

        if(!RegexUtil.IsTelephone(reserveMobile)){
            ToastUtil.showToast(mContext, "请输入11位有效手机号");
            return;
        }

        GetReserveMobileVerifyCode getReserveMobileVerifyCode = new GetReserveMobileVerifyCode(this);
        JSONObject mjson = new JSONObject();
        try{
            mjson.put("mobile", reserveMobile);
            mjson.put("type", "1");

            getReserveMobileVerifyCode.getReserveMobileVerifyCode(mjson, bt_next_ver, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    Bundle bundle = new Bundle();
                    bundle.putString("bank_card", bankCardNo);
                    bundle.putString("id_num", idNum);
                    bundle.putString("card_owner", cardHolder);
                    bundle.putString("reserve_mobile", reserveMobile);
                    bundle.putString("card_type", "0");
                    bundle.putString("bank_id", cardId);
                    bundle.putString("bank_brhname", bankbranch);
                    gotoActivity(AuthBankCardActivity.this, BankCardMessageActivity.class, bundle);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (isOnKeyDown()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isInit){
                    gotoActivity(mContext, ToExamineActivity.class, null);
                    ((MyApplication) getApplication()).clearTempActivityInBackStack(ToExamineActivity.class);
                }else{
                    backActivity();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isOnKeyDown() {
        return true;
    }
}
