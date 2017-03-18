package com.maibei.merchants.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.maibei.merchants.R;
import com.maibei.merchants.net.base.ChangeInterface;
import com.maibei.merchants.utils.TimeCount;

/**
 * Created by zhangchi on 2016/6/21.
 */
public class MyEditText extends LinearLayout implements View.OnFocusChangeListener, View.OnClickListener {
    private View rootView;
    private View v_line;
    private View v_bottom;
    private TextView tv_promp;
    private TextView tv_right;
    private EditText et_content;
    private ImageView iv_right;
    private Context mContext;
    private int leftWidth;
    private int line_margin;

    ChangeInterface changeInterface;

    private final int INPUT_TEXT = 1;
    private final int INPUT_NUMBER = 2;
    private final int INPUT_PASSWORD = 3;
    private TimeCount mTimer;

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_my_edit_text, this);
        findViews(rootView);
        init(attrs);
    }

    private MyEditTextListener mListener;


    public void setListener(MyEditTextListener listener) {
        mListener = listener;
    }

    public void setChangeListener(ChangeInterface changeInterface){
        this.changeInterface=changeInterface;
        et_content.addTextChangedListener(new EditChangedListener());
    }

    private void findViews(View rootView) {
        v_line = rootView.findViewById(R.id.v_line);
        v_bottom = rootView.findViewById(R.id.v_bottom);
        tv_promp = (TextView) rootView.findViewById(R.id.tv_promp);
        tv_right = (TextView) rootView.findViewById(R.id.tv_right);
        et_content = (EditText) rootView.findViewById(R.id.et_content);
        iv_right = (ImageView) rootView.findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        et_content.setOnFocusChangeListener(this);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        String promp = array.getString(R.styleable.MyEditText_promp);
        String hint = array.getString(R.styleable.MyEditText_hint);
        Drawable draw = array.getDrawable(R.styleable.MyEditText_drawable);
        String rtText = array.getString(R.styleable.MyEditText_rt_text);
        String editText = array.getString(R.styleable.MyEditText_edit_text);
        int inputType = array.getInt(R.styleable.MyEditText_inputType, 0);
        boolean isEdit = array.getBoolean(R.styleable.MyEditText_is_edit, true);
        int ltWidth = (int) array.getDimension(R.styleable.MyEditText_left_width, 0);
        int margin = (int) array.getDimension(R.styleable.MyEditText_line_margin, 0);
        int maxLen = array.getInt(R.styleable.MyEditText_maxLength, 0);

        array.recycle();
        if (promp != null) {
            tv_promp.setText(promp);
        }
        if (editText != null) {
            et_content.setText(editText);
        }
        if (hint != null) {
            et_content.setHint(hint);
        }
        if (draw != null) {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageDrawable(draw);
        }
        if (rtText != null) {
            v_line.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(rtText);
        }
        if (ltWidth != 0) {
            leftWidth = ltWidth;
        }
        if (margin != 0) {
            line_margin = margin;
        }
        if (INPUT_TEXT == inputType) {
            et_content.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (INPUT_NUMBER == inputType) {
            et_content.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (INPUT_PASSWORD == inputType) {
            et_content.setInputType(InputType.TYPE_CLASS_TEXT);
            et_content.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (maxLen > 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLen)};
            et_content.setFilters(filters);
        }
        et_content.setFocusable(isEdit);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("ret", "hasFocus" + hasFocus);
        if (hasFocus) {
            v_bottom.setBackgroundResource(R.color.shallow_blue);
        } else {
            v_bottom.setBackgroundResource(R.color.light_gray);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.tv_right && mListener.onRightClick(this)) {
                mTimer = new TimeCount(tv_right, 60000, 1000, "重新获取");
                mTimer.start();
            } else if (v.getId() == R.id.iv_right) {
                mListener.onRightClick(this);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (leftWidth != 0) {
            tv_promp.getLayoutParams().width = leftWidth;
        }
        if (line_margin != 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v_bottom.getLayoutParams();
            params.setMargins(line_margin, 0, 0, 0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public interface MyEditTextListener {
        boolean onRightClick(View view);
    }

    public void finishTimer() {
        mTimer.finish();
    }

    public void setRightImageViewIcon(int resId) {
        iv_right.setImageResource(resId);
    }

    public String getEditTextString() {
        return et_content.getText().toString().trim();
    }

    public EditText getEt_content() {
        return et_content;
    }

    public void setEt_content(EditText et_content) {
        this.et_content = et_content;
    }

    public class EditChangedListener implements TextWatcher {

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
