package com.maibei.merchants.net.base;

import android.text.Editable;

/**
 * Created by 14658 on 2016/8/5.
 */
public interface ChangePwdInterfac {
    void change(CharSequence s, int start, int before, int count);
    void changeBefore(CharSequence s, int start, int count, int after);
    void chageAfter(Editable s);
}
