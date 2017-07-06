package com.tianshen.cash.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshen.cash.R;


/**
 * Created by zhangchi on 2016/6/23.
 */
public class ImageTextView extends LinearLayout implements View.OnClickListener {

    private View rootView;
    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_content;
    private TextView tv_right;
    private TextView tv_hint;
    private Context mContext;
    private int leftWidth = 50;
    private boolean isLeftWidthChange = false;

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

    private void findViews(View rootView) {
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        tv_right = (TextView) rootView.findViewById(R.id.tv_right);
        iv_left = (ImageView) rootView.findViewById(R.id.iv_left);
        iv_right = (ImageView) rootView.findViewById(R.id.iv_right);
        tv_hint = (TextView) rootView.findViewById(R.id.tv_hint);
        iv_right.setOnClickListener(this);
    }

    public void setRtVisibility(int isShow) {
        iv_right.setVisibility(isShow);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        leftWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, leftWidth, dm);
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        String text = array.getString(R.styleable.ImageTextView_itvText);
        String rtText = array.getString(R.styleable.ImageTextView_itvRText);
        Drawable draw = array.getDrawable(R.styleable.ImageTextView_itvDrawable);
        Drawable rtDraw = array.getDrawable(R.styleable.ImageTextView_itvRtDrawable);
        int ltWidth = (int) array.getDimension(R.styleable.ImageTextView_itvLtWidth, leftWidth);
        String hint = array.getString(R.styleable.ImageTextView_itv_hint);
        int tv_left_color = array.getInt(R.styleable.ImageTextView_tv_left_color, 0xFF000000);
        int tv_right_color = array.getInt(R.styleable.ImageTextView_tv_right_color, 0xFF000000);
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
        if (draw != null) {
            iv_left.setImageDrawable(draw);
        }
        if (rtDraw != null) {
            iv_right.setImageDrawable(rtDraw);
        }
        if (!(hint == null || "".equals(hint))) {
            tv_hint.setText(hint);
        }
        if (tv_left_color != 0xFF000000) {
            tv_content.setTextColor(tv_left_color);
        }
        if (tv_right_color != 0xFF000000) {
            tv_right.setTextColor(tv_right_color);
        }

    }

    public void setRightTextColor(int color) {
        tv_right.setTextColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isLeftWidthChange) {
            iv_left.getLayoutParams().width = leftWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

}
