package com.maibei.merchants.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.net.base.ChangeInterface;


/**
 * Created by zhangchi on 2016/6/23.
 */
public class ImageTextView extends LinearLayout implements View.OnClickListener {

    private View rootView;
    private TextView tv_left;
    private ImageView iv_right;
    private TextView tv_content;
    private TextView tv_right;
    private Context mContext;
    private int leftWidth = 50;
    private boolean isLeftWidthChange = false;

    ChangeInterface changeInterface;

    private RelativeLayout rl_quan;

    private ImageTextViewListener mListener;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_image_textview, this);
        findViews(rootView);
        init(attrs);
    }

    public void setListener(ImageTextViewListener listener) {
        mListener = listener;
    }

    public void setChangeListener(ChangeInterface changeInterface){
        this.changeInterface=changeInterface;
        tv_content.addTextChangedListener(new TextChangedListener());
    }

    private void findViews(View rootView) {
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        tv_right = (TextView) rootView.findViewById(R.id.tv_right);
        tv_left = (TextView) rootView.findViewById(R.id.tv_left);
        iv_right = (ImageView) rootView.findViewById(R.id.iv_right);
        rl_quan = (RelativeLayout) rootView.findViewById(R.id.rl_quan);
        rl_quan.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        leftWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, leftWidth, dm);
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        String titleText = array.getString(R.styleable.ImageTextView_itvTitle);
        String text = array.getString(R.styleable.ImageTextView_itvText);
        String rtText = array.getString(R.styleable.ImageTextView_itvRText);
//        Drawable draw = array.getDrawable(R.styleable.ImageTextView_itvDrawable);
        Drawable rtDraw = array.getDrawable(R.styleable.ImageTextView_itvRtDrawable);
        int ltWidth = (int) array.getDimension(R.styleable.ImageTextView_itvLtWidth, leftWidth);
        int color=array.getInt(R.styleable.ImageTextView_title_color,0xFFFFFFFF);
        if (ltWidth != leftWidth) {
            isLeftWidthChange = true;
            leftWidth = ltWidth;
        }
        array.recycle();
        if (text != null) {
            tv_content.setText(text);
        }
        if (rtText != null) {
            tv_right.setText(rtText);
        }
        if (titleText != null) {
            tv_left.setText(titleText);
        }
        if (rtDraw != null) {
            iv_right.setImageDrawable(rtDraw);
        }
        if(color!=0xFFFFFFFF){
            tv_content.setTextColor(color);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (isLeftWidthChange) {
//            iv_left.getLayoutParams().width = leftWidth;
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setVis(int vis){
        tv_content.setVisibility(vis);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onRightClick(this);
        }
    }

    public interface ImageTextViewListener {
        void onRightClick(View view);
    }


    public void setRightImageViewIcon(int resId) {
        iv_right.setImageResource(resId);
    }

    public void setRightText(String string) {
        tv_right.setText(string);
    }

    public void setContentText(String string) {
        tv_content.setText(string);
    }

    public String getContentText(){
        return tv_content.getText().toString().trim();
    }

    public class TextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changeInterface.change(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}
