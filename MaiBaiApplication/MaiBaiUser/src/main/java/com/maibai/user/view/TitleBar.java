package com.maibai.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maibai.user.R;

/**
 * Created by zhangchi on 2016/6/28.
 */
public class TitleBar extends RelativeLayout implements RightClickAbleEditText.DrawableRightListener, TextWatcher, View.OnClickListener {
    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_address;
    private TextView tv_title;
    private RightClickAbleEditText et_content;
    private Context mContext;
    private View ll_address;
    private View view;

    private final int TYPE_EDIT_TEXT = 1;
    private final int TYPE_ADDRESS = 2;
    private boolean isBack = true;
    private boolean isLeftClickAble = true;
    private boolean isRightClickAble = true;
    private TitleBarListener mListener;
    private TitleBarListener2 mListener2;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = LayoutInflater.from(mContext).inflate(R.layout.view_title_bar, this);
        init(attrs);
    }

    public void setListener(TitleBarListener listener) {
        mListener = listener;
    }

    public void setListener(TitleBarListener2 listener) {
        mListener2 = listener;
    }

    private void init(AttributeSet attrs) {
        findViews(view);
        initData(attrs);
    }

    private void findViews(View view) {
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_address = view.findViewById(R.id.ll_address);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        et_content = (RightClickAbleEditText) view.findViewById(R.id.et_content);
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        et_content.setOnDrawableRightListener(this);
        et_content.addTextChangedListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }

    private void initData(AttributeSet attrs) {
        TypedArray arr = mContext.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        Drawable img = arr.getDrawable(R.styleable.TitleBar_tbImage);
        Drawable rtImg = arr.getDrawable(R.styleable.TitleBar_tbRtImage);
        String text = arr.getString(R.styleable.TitleBar_tbText);
        String address = arr.getString(R.styleable.TitleBar_tbAddress);
        String hint = arr.getString(R.styleable.TitleBar_tbHint);
        int type = arr.getInt(R.styleable.TitleBar_tbType, 0);
        if (!(arr.getResourceId(R.styleable.TitleBar_tbImage, 1) == R.mipmap.icon_houtui||arr.getResourceId(R.styleable.TitleBar_tbImage, 1) == R.mipmap.metion_quit)) {
            isBack = false;
        }
        if (img != null) {
            iv_left.setImageDrawable(img);
        } else {
            isLeftClickAble = false;
        }
        if (rtImg != null) {
            iv_right.setImageDrawable(rtImg);
        } else {
            isRightClickAble = false;
        }
        if (text != null) {
            tv_title.setText(text);
        }
        if (address != null) {
            tv_address.setText(address);
        }
        if (hint != null) {
            et_content.setHint(hint);
        }
        if (type == TYPE_EDIT_TEXT) {
            tv_title.setVisibility(GONE);
            ll_address.setVisibility(GONE);
            et_content.setVisibility(VISIBLE);
            et_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            iv_right.setVisibility(INVISIBLE);
            isRightClickAble = false;
        } else if (type == TYPE_ADDRESS) {
            tv_title.setVisibility(GONE);
            et_content.setVisibility(GONE);
            ll_address.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onDrawableRightClick(View view) {
        et_content.setText("");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count == 0) {
            et_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            iv_right.setVisibility(View.INVISIBLE);
            isRightClickAble = false;
        } else {
            if (et_content.getCompoundDrawables()[2] == null) {
                et_content.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_cuowu, 0);
            }
            iv_right.setVisibility(View.VISIBLE);
            isRightClickAble = true;
        }
        if (mListener2 != null) {
            mListener2.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                if (isBack) {
                    Activity activity = (Activity) mContext;
                    activity.finish();
                    activity.overridePendingTransition(R.anim.not_exit_push_left_in, R.anim.push_right_out);
                    if (isLeftClickAble) {
                        if (mListener != null) {
                            mListener.onLeftClick(iv_left);
                        } else if (mListener2 != null) {
                            mListener2.onLeftClick(iv_left);
                        }
                    }
                }else{
                    if (isLeftClickAble) {
                        if (mListener != null) {
                            mListener.onLeftClick(iv_left);
                        } else if (mListener2 != null) {
                            mListener2.onLeftClick(iv_left);
                        }
                    }
                }
                break;
            case R.id.iv_right:
                if (isRightClickAble) {
                    if (mListener != null) {
                        mListener.onRightClick(iv_right);
                    } else if (mListener2 != null) {
                        mListener2.onRightClick(iv_right);
                    }
                }
                break;
            case R.id.tv_address:
                if (mListener != null) {
                    mListener.onAddressClick(tv_address);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mListener2 != null) {
            mListener2.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mListener2 != null) {
            mListener2.afterTextChanged(s);
        }
    }


    public interface TitleBarListener {
        void onLeftClick(View view);

        void onAddressClick(View view);

        void onRightClick(View view);
    }

    public interface TitleBarListener2 {
        void onLeftClick(View view);

        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);

        void onRightClick(View view);
    }

    public void setAddress(String text) {
        if (text != null) {
            tv_address.setText(text);
        }
    }

    public void setIvLeftVisible(int VisibleType) {
        iv_left.setVisibility(VisibleType);
    }

    public void setIvRightVisible(int VisibleType) {
        iv_right.setVisibility(VisibleType);
    }

    public String getAddress() {
        return tv_address.getText().toString();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public String getTitle() {
        return tv_title.getText().toString().trim();
    }

    public String getEditTextContent() {
        return et_content.getText().toString();
    }

}
