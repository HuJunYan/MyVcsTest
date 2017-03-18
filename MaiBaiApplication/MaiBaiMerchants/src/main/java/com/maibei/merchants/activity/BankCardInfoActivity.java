package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.maibei.merchants.R;
import com.maibei.merchants.adapter.BankCardInfoAdapter;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.BankListBean;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.GetMerchantBankList;
import com.maibei.merchants.net.api.UnbindBankCard;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.TelephoneScreenChangeUtils;
import com.maibei.merchants.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/19.
 */
public class BankCardInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_houtui, iv_add;
    private ListView lv_bank_list;
    private BankListBean bankListBean;
    private Button bt_remove_binding;
    private  Dialog dialog;
    private RelativeLayout rl_add_bank_card;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_bank_card_info;
    }

    @Override
    protected void findViews() {
        iv_houtui = (ImageView) findViewById(R.id.iv_houtui);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        lv_bank_list = (ListView) findViewById(R.id.lv_bank_list);
        bt_remove_binding=(Button)findViewById(R.id.bt_remove_binding);
        rl_add_bank_card=(RelativeLayout)findViewById(R.id.rl_add_bank_card);
    }

    @Override
    protected void setListensers() {
        iv_houtui.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        bt_remove_binding.setOnClickListener(this);
        rl_add_bank_card.setOnClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                break;
            case R.id.iv_houtui:
                backActivity();
                break;
            case R.id.bt_remove_binding:
                showRemoveBindDialog();
                break;
            case R.id.rl_add_bank_card:
                gotoActivity(mContext, AuthBankCardActivity.class, null);
               finish();
                break;
        }
    }

    private void showRemoveBindDialog(){
       dialog = new AlertDialog.Builder(mContext, R.style.remove_bind_dialog).create();
        View view= LayoutInflater.from(mContext).inflate(R.layout.view_dialog_remove_bind, null);
        LinearLayout ll_cancel=(LinearLayout)view.findViewById(R.id.ll_cancel);
        LinearLayout ll_confirm=(LinearLayout)view.findViewById(R.id.ll_confirm);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephoneScreenChangeUtils telephoneScreenChangeUtils=new TelephoneScreenChangeUtils(mContext);
                telephoneScreenChangeUtils.changeLight();
                dialog.dismiss();
            }
        });
        ll_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephoneScreenChangeUtils telephoneScreenChangeUtils=new TelephoneScreenChangeUtils(mContext);
                telephoneScreenChangeUtils.changeLight();
                dialog.dismiss();
                if (bankListBean == null || bankListBean.getData().size() == 0) {
                    return;
                }
                removeBind();
            }
        });
        dialog.setCancelable(false);
        TelephoneScreenChangeUtils telephoneScreenChangeUtils=new TelephoneScreenChangeUtils(mContext);
        telephoneScreenChangeUtils.changeDark();
        dialog.show();
        dialog.setContentView(view);
    }

    private void removeBind(){
        UnbindBankCard unbindBankCard=new UnbindBankCard(mContext);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("merchant_id",UserUtil.getMerchantId(mContext));
            jsonObject.put("bank_card",bankListBean.getData().get(0).getCard_num());
            jsonObject.put("pay_pass","");//支付密码
            unbindBankCard.unbindBankCard(jsonObject, null, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                        TelephoneScreenChangeUtils telephoneScreenChangeUtils=new TelephoneScreenChangeUtils(mContext);
                        telephoneScreenChangeUtils.changeLight();
                    }
                    ToastUtil.showToast(mContext, "银行卡解绑成功！");
                    backActivity();
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
    private void initData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merchant_id", UserUtil.getMerchantId(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        GetMerchantBankList getMerchantBankList = new GetMerchantBankList(mContext);
        getMerchantBankList.getMerchantBankList(jsonObject, null, true, new BaseNetCallBack<BankListBean>() {
            @Override
            public void onSuccess(BankListBean paramT) {
                bankListBean=paramT;
                BankCardInfoAdapter adapter=new BankCardInfoAdapter(mContext,bankListBean);
                lv_bank_list.setAdapter(adapter);
                if (paramT.getData().size() == 0) {
                    rl_add_bank_card.setVisibility(View.VISIBLE);
                    bt_remove_binding.setVisibility(View.GONE);
                } else {
                    rl_add_bank_card.setVisibility(View.GONE);
                    bt_remove_binding.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                rl_add_bank_card.setVisibility(View.VISIBLE);
                bt_remove_binding.setVisibility(View.GONE);
            }
        });
    }
}
