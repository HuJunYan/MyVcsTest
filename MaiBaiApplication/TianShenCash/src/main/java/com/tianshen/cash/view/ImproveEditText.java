package com.tianshen.cash.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ImproveEditText extends LinearLayout {
    Context mContext;
    View rootView;
    EditText et_value;
    TextView tv_key;
    ImageView img_left;
    public ImproveEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        rootView= LayoutInflater.from(context).inflate(R.layout.view_improve_edit,null);
        initView();
        initData(attrs);
    }
    public void initView(){
        et_value=(EditText)rootView.findViewById(R.id.et_value);
        tv_key=(TextView)rootView.findViewById(R.id.tv_key);
        img_left=(ImageView)rootView.findViewById(R.id.img_left);
    }
    public void initData(AttributeSet attrs){
        TypedArray array=mContext.obtainStyledAttributes(attrs,R.styleable.ImproveEditText);
        Drawable drawable=array.getDrawable(R.styleable.ImproveEditText_improve_edit_text_img);
        String key=array.getString(R.styleable.ImproveEditText_improve_edit_text_key);
        img_left.setImageDrawable(drawable);
        tv_key.setText(key);
    }

    public String getText(){
        return et_value.getText().toString();
    }
}
