/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maibai.user.net.base;

import android.content.Context;

public interface BaseView<T> {

    /**
     * fragment 下需要使用
     * 关联fragment和T
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 一般网络响应时调用,用来取消进度条和显示Toast的
     * @param msg
     */
    void showToast(String msg);
    /**
     * 一般网络响应时调用,用来取消进度条和显示Toast的
     * @param msg
     */
    void showToast(int msg);

    /**
     * 获取fragment的上下文
     * @return
     */
    Context getContext();

}
