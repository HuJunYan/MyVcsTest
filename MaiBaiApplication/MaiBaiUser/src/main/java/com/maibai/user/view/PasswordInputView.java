package com.maibai.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import com.maibai.user.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PasswordInputView extends EditText {

    private static final int defaultContMargin = 3;
    private static final int defaultSplitLineWidth = 3;

    private int borderColor = 0xFFCCCCCC;
    private float borderWidth = 3;
    private float borderRadius = 10;

    private int passwordLength = 6;
    private int passwordColor = 0xFFCCCCCC;
    private float passwordWidth = 8;

    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);
    private int textLength;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(0);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderWidth, dm);
        borderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderRadius, dm);
        passwordWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordWidth, dm);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        borderColor = a.getColor(R.styleable.PasswordInputView_pivBorderColor, borderColor);
        passwordLength = a.getInt(R.styleable.PasswordInputView_pivPasswordLength, passwordLength);
        passwordColor = a.getColor(R.styleable.PasswordInputView_pivPasswordColor, passwordColor);
        float bWidth = a.getDimension(R.styleable.PasswordInputView_pivBorderWidth, borderWidth);
        if (bWidth <= borderWidth) {
            borderWidth = bWidth;
        }
        float bRadius = a.getDimension(R.styleable.PasswordInputView_pivBorderRadius, borderRadius);
        if (bRadius <= borderRadius) {
            borderRadius = bRadius;
        }
        float pwdWidth = a.getDimension(R.styleable.PasswordInputView_pivPasswordWidth, passwordWidth);
        if (pwdWidth <= passwordWidth) {
            passwordWidth = pwdWidth;
        }
        a.recycle();

        if (passwordLength > 6) {
            passwordLength = 6;
        }
        InputFilter[] filters = {new InputFilter.LengthFilter(passwordLength)};
        setFilters(filters);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getMeasuredHeight();

        // 外边框
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

        // 内容区
        RectF rectIn = new RectF(rect.left + borderWidth, rect.top + borderWidth,
                rect.right - borderWidth, rect.bottom - borderWidth);
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

        // 分割线
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        // 密码
        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int length = passwordLength > 3 ? passwordLength : 5;
        setMeasuredDimension(widthMeasureSpec, getMeasuredWidth() / length);
    }
}
