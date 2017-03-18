package com.maibai.cash.view;

import android.text.Editable;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface ChangeInterface {
    void change(CharSequence s, int start, int before, int count);
    void changeBefore(CharSequence s, int start, int count, int after);
    void chageAfter(Editable s);
}
