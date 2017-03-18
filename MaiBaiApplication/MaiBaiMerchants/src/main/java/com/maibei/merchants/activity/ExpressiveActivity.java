package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.WithDrawBean;
import com.maibei.merchants.net.api.WithDraw;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.AmountFilterUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by huxiao on 2016/6/22.
 */
public class ExpressiveActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_tixian;
    private EditText et_amount_count;
    private ImageButton ib_return_home;
    private String commiCount;
    private String amountCount;
    private String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AmountFilterUtil.setPricePoint(et_amount_count);
        amountCount = UserUtil.getBalance(mContext);//总金额数（分）
        commiCount=UserUtil.getCommiBanlance(mContext);//佣金余额（分）
        if(null==amountCount||"".equals(amountCount)){
            amountCount="0";
        }
        if(null==commiCount||"".equals(commiCount)){
            commiCount="0";
        }
        LogUtil.d("RRR", "merchantId======" + UserUtil.getMerchantId(mContext));
        Bundle bundle=new Bundle();
        bundle=getIntent().getExtras();
        if(null==bundle){
            return;
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_expressive;
    }

    @Override
    protected void findViews() {
        et_amount_count = (EditText)findViewById(R.id.et_amount_count);
        bt_tixian = (Button)findViewById(R.id.bt_tixian);
        ib_return_home = (ImageButton)findViewById(R.id.ib_return_home);
    }


    @Override
    protected void setListensers() {
        bt_tixian.setOnClickListener(this);
        ib_return_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tixian:
                extract();
                break;
            case R.id.ib_return_home:
                backActivity();
                break;
            default:
                break;
        }
    }

    //提取金额
    private void extract() {
        amount = et_amount_count.getText().toString();
        if (TextUtils.isEmpty(amount)){
            ToastUtil.showToast(mContext, "请输入提取金额");
            return;
        }
        double amountNum = Double.valueOf(amount).doubleValue() * 100;//提取金额（分）
        double balance = Double.valueOf(amountCount).doubleValue();//总金额（分）
        if(amountNum<=0){
            ToastUtil.showToast(mContext,"提现金额不能必须大于0元");
            return;
        }
            if (amountNum > balance) {//提取金额不能大于总金额
                warringDialog();
                return;
            }
        JSONObject mJSONObject = new JSONObject();
        try {
            int i = (int)amountNum;
            Integer money = Integer.valueOf(i);
            mJSONObject.put("merchant_id",UserUtil.getMerchantId(mContext));
            mJSONObject.put("money",money);
                withDrawals(mJSONObject);
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    private void withDrawals(JSONObject mJSONObject) {
        WithDraw withDraw = new WithDraw(ExpressiveActivity.this);
        withDraw.withDraw(mJSONObject, bt_tixian, true, new BaseNetCallBack<WithDrawBean>() {
            @Override
            public void onSuccess(WithDrawBean paramT) {
                if (paramT.getData() != null ) {
                    Bundle bundle = new Bundle();
                    bundle.putString("extAmount", amount);//提取金额(元)
                    bundle.putString("extAmountCount", paramT.getData().getBalance());//提取后总金额
                    bundle.putString("type","widthdrawal");
                    gotoActivity(ExpressiveActivity.this, FinalActivity.class, bundle);
                    closeActivity();
                } else {
                    ToastUtil.showToast(mContext, "您还没有绑定银行卡，请先绑卡！");
                    gotoActivity(mContext, BankCardInfoActivity.class, null);
                    backActivity();
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                if (errorCode == 124) {
                    gotoActivity(mContext, AuthBankCardActivity.class, null);
                    backActivity();
                }
            }
        });
    }

    /**
     * 金额错误提示
     */
    protected void warringDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpressiveActivity.this);
        builder.setMessage("提取金额多于现有金额，请重新输入！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void closeActivity() {
        Intent intent = new Intent();
        intent.setAction("SIGN_OUT_ACTIVITY"); // 说明动作
        sendBroadcast(intent);// 该函数用于发送广播
        backActivity();
    }

}
