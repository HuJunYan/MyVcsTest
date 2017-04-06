package com.tianshen.cash.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianshen.cash.R;


/**
 * Created by Administrator on 2016/7/28.
 * 设置界面目录样式
 */
public class MyTextView extends LinearLayout implements View.OnClickListener{

    Context mContext;
    TextView tv_content;
    ImageView iv_right;
    MyTextViewListener listener;
    RelativeLayout layout_father;
    TextView tv_right;
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_my_text, this);
        findViews(rootView);
        init(attrs);
    }

    public void findViews(View rootView){
        tv_content=(TextView)rootView.findViewById(R.id.tv_content);
        iv_right=(ImageView)rootView.findViewById(R.id.iv_right);
        layout_father=(RelativeLayout)rootView.findViewById(R.id.layout_father);
        tv_right=(TextView)rootView.findViewById(R.id.tv_right);
    }
    public void setListener(MyTextViewListener listener){
        this.listener=listener;
    }
   interface MyTextViewListener{
        void clickFatherLayout(View view);
    }

    public void setTv_right(String content_right){
        tv_right.setText(content_right);
    }
    public void init(AttributeSet attrs){
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String content=array.getString(R.styleable.MyTextView_setting_tv_content);
        Drawable draw_right=array.getDrawable(R.styleable.MyTextView_setting_img_right);
        int tv_size=array.getInt(R.styleable.MyTextView_setting_tv_size, 0);
        String content_right=array.getString(R.styleable.MyTextView_setting_tv_right);
        int height=array.getInt(R.styleable.MyTextView_setting_layout_father_height,0);
        tv_content.setText(content);
        if(tv_size!=0) {
            tv_content.setTextSize(tv_size);
        }
        if(draw_right!=null) {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageDrawable(draw_right);
        }
        if(!(content_right==""&&content_right==null)){
            tv_right.setText(content_right);
        }
          /* if(padding!=0){

               RelativeLayout.LayoutParams params=new  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
               params.setMargins(padding,padding,padding,padding);
               tv_content.setLayoutParams(params);
//               iv_right.setLayoutParams(params);
           }*/
       /* if(height!=0){

           RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.height=height;
            layout_father.setLayoutParams(params);
        }*/

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_father:
                listener.clickFatherLayout(layout_father);
                break;
        }
    }
}
