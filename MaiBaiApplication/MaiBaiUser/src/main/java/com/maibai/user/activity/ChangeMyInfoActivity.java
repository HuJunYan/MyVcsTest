package com.maibai.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.view.TitleBar;

public class ChangeMyInfoActivity extends BaseActivity implements View.OnClickListener,TitleBar.TitleBarListener{

    private TitleBar tb_title;
    private EditText et_info;
    private ImageView iv_clean;
    private String content="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        Bundle mBundle=getIntent().getExtras();
        if(null==mBundle){
            return;
        }
        int type=mBundle.getInt(GlobalParams.CHANGE_INFO_TYPE_KEY,0);
        if(type==0){
            return;
        }
        switch (type){
            case GlobalParams.CHANGE_TYPE_WECHAT:
                tb_title.setTitle("修改微信号");
                break;
            case GlobalParams.CHANGE_TYPE_ADDRESS:
                tb_title.setTitle("修改常住地址");
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_NAME:
                tb_title.setTitle("修改单位名称");
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_PHONE:
                tb_title.setTitle("修改单位电话");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_NAME:
                tb_title.setTitle("修改父母姓名");
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_MOBILE:
                tb_title.setTitle("修改父母电话");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case GlobalParams.CHANGE_TYPE_PARENT_ADDRESS:
                tb_title.setTitle("修改父母住址");
                break;
            case GlobalParams.CHANGE_TYPE_BROTHER_NAME:
                tb_title.setTitle("修改直亲姓名");
                break;
            case GlobalParams.CHANGE_TYPE_BROTHER_MOBILE:
                tb_title.setTitle("修改直亲电话");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case GlobalParams.CHANGE_TYPE_COMPANY_ADDRESS:
                tb_title.setTitle("修改单位地址电话");
                break;
            case GlobalParams.CHANGE_TYPE_FRIEND_NAME:
                tb_title.setTitle("朋友姓名");
                break;
            case GlobalParams.CHANGE_TYPE_FRIEND_MOBILE:
                tb_title.setTitle("修改朋友电话");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case GlobalParams.CHANGE_TYPE_COLLEAGUE_NAME:
                tb_title.setTitle("同事姓名");
                break;
            case GlobalParams.CHANGE_TYPE_COLLEAGUE_MOBILE:
                tb_title.setTitle("修改同事电话");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            case GlobalParams.CHANGE_TYPE_QQ:
                tb_title.setTitle("修改QQ号码");
                et_info.setInputType(InputType.TYPE_CLASS_PHONE);
                et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                break;
            /*   itv_colleague_name,
                itv_colleague_mobile,itv_qq*/
        }
        content=mBundle.getString(GlobalParams.CHANGE_INTO_INFO_KEY);
        if(null==content){
            content="";
        }
        et_info.setText(content);
        if("".equals(et_info.getText().toString())){
            iv_clean.setVisibility(View.GONE);
        }else{
            iv_clean.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_change_my_info;
    }

    @Override
    protected void findViews() {
        tb_title=(TitleBar)findViewById(R.id.tb_title);
        et_info=(EditText)findViewById(R.id.et_info);
        iv_clean=(ImageView)findViewById(R.id.iv_clean);
    }

    @Override
    protected void setListensers() {
        iv_clean.setOnClickListener(this);
        tb_title.setListener(this);
        et_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if("".equals(s)){
                    iv_clean.setVisibility(View.GONE);
                    tb_title.setIvRightVisible(View.GONE);
                }else{
                    iv_clean.setVisibility(View.VISIBLE);
                    tb_title.setIvRightVisible(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        et_info.setText("");
        iv_clean.setVisibility(View.GONE);
    }

    @Override
    public void onLeftClick(View view) {

    }

    @Override
    public void onAddressClick(View view) {

    }

    @Override
    public void onRightClick(View view) {
        if("".equals(et_info.getText().toString().trim())){
            ToastUtil.showToast(mContext,"内容不可为空");
        }
        if(et_info.getText().toString().trim().equals(content)){
            ToastUtil.showToast(mContext,"修改后的内容不可相同");
            return;
        }
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString(GlobalParams.CHANGE_INFO_KEY,et_info.getText().toString().trim());
        intent.putExtras(bundle);
        setResult(GlobalParams.CHANGE_RESULT_OK,intent);
        backActivity();
    }
}

