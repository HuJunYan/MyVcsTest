package com.maibei.merchants.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.MerchantCommisionBean;
import com.maibei.merchants.net.api.MerchantCommision;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.DataHandler;
import com.maibei.merchants.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferActivity extends BaseActivity implements View.OnClickListener{

    private String commision_all;
    private TextView tv_commision_all;
    private TextView tv_shop_name;
    private Button bt_confirm;
    private EditText et_transfer_num;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_shop_name.setText(UserUtil.getName(mContext));
        Bundle bundle=getIntent().getExtras();
        if(null==bundle){
            return;
        }
        commision_all=bundle.getString("commision_all");
        if(null==commision_all||"".equals(commision_all)){
            commision_all="0";
        }
        tv_commision_all.setText(Double.valueOf(commision_all)/100+"元");
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void findViews() {
        tv_commision_all=(TextView)findViewById(R.id.tv_commision_all);
        bt_confirm=(Button)findViewById(R.id.bt_confirm);
        et_transfer_num=(EditText)findViewById(R.id.et_transfer_num);
        tv_shop_name=(TextView)findViewById(R.id.tv_shop_name);
        iv_back=(ImageView)findViewById(R.id.iv_back);
    }

    @Override
    protected void setListensers() {
        bt_confirm.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                backActivity();
                break;
            case R.id.bt_confirm:
                String num=et_transfer_num.getText().toString();
                if("".equals(num)){
                    ToastUtil.showToast(mContext,"请输入额度");
                    return;
                }
                if(Double.valueOf(num)*100>Double.valueOf(commision_all)){
                    ToastUtil.showToast(mContext,"最大提取额度为"+Double.valueOf(commision_all)/100+"元");
                    return;
                }
                getCommission(num);
                break;
        }
    }
    private void getCommission(final String money){

        try {
            MerchantCommision merchantCommision=new MerchantCommision(mContext);
            final long extAmount= (long)(DataHandler.stringToDouble(money)*100);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("merchant_id",UserUtil.getMerchantId(mContext));
            jsonObject.put("money", extAmount+"");
            merchantCommision.merchantCommision(jsonObject, bt_confirm, true, new BaseNetCallBack<MerchantCommisionBean>() {
                @Override
                public void onSuccess(MerchantCommisionBean paramT) {
                    String commision_balance=paramT.getData().getCommision_balance();
                    if(null==commision_balance||"".equals(commision_balance)){
                        commision_balance="0";
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("extAmount", Double.valueOf(extAmount)/100+"");//提取金额(元)
                    bundle.putString("extAmountCount",Double.valueOf(commision_balance)/100+"" );//提取后总金额
                    bundle.putString("type","transfer");
                    gotoActivity(mContext, FinalActivity.class, bundle);
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
