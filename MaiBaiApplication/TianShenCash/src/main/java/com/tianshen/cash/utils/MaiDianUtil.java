package com.tianshen.cash.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.api.MaiDian;
import com.tianshen.cash.net.base.BaseNetCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cuiyue on 2017/10/26.
 */

public class MaiDianUtil {

//    启动页调用更新接口时   1
//    认证中心页，进入时    2
//    认证中心页，退出时    3
//    身份认证页面，进入时   4
//    身份认证页面，退出时   5
//    face＋＋身份证正面启动扫描时   6
//    face＋＋身份证正面扫描退出时（扫描结果成功和失败要区分开）  7
//    face＋＋身份证反面启动扫描时   8
//    face＋＋身份证反面扫描退出时（扫描结果成功和失败要区分开）  9
//    face＋＋扫脸启动扫描时     10
//    face＋＋扫脸扫描退出时（扫描结果成功和失败要区分开） 11
//    有盾认证启动时  12
//    有盾认证结束    13
//    点击首页立即申请按钮，上传联系人之前获取联系人列表，当联系人数量为0时     14
//    点击首页立即申请按钮，上传联系人之前获取联系人列表，当联系人数量不为0时   15
//    点击首页立即申请按钮，上传联系人接口成功时     16
//    确认申请页面，进入时           17
//    确认申请页面，点击确认按钮时    18
//    确认申请页面，预下单完成时      19
//    运营商认证页面，进入时         20
//    运营商认证页面，认证成功时      21
//    运营商认证页面，认证失败时      22
//    首页弹出活动窗口，点击活动窗口时（要区分不同的活动）  23
//    我的页面，点击邀请好友时                         24
//    在h5的邀请好友页面，点击立即邀友赚钱时             25
//            "result": "1"//当flag==7、flag==9、flag==11 需要传 1成功 2失败 3未知  //默认值是-1
//            "activity_result": "1" //当flag==23 需要传"activity"接口中的"activity_type"这个字段的值 //默认值是-1

    public static final String FLAG_1 = "1";
    public static final String FLAG_2 = "2";
    public static final String FLAG_3 = "3";
    public static final String FLAG_4 = "4";
    public static final String FLAG_5 = "5";
    public static final String FLAG_6 = "6";
    public static final String FLAG_7 = "7";
    public static final String FLAG_8 = "8";
    public static final String FLAG_9 = "9";
    public static final String FLAG_10 = "10";
    public static final String FLAG_11 = "11";
    public static final String FLAG_12 = "12";
    public static final String FLAG_13 = "13";
    public static final String FLAG_14 = "14";
    public static final String FLAG_15 = "15";
    public static final String FLAG_16 = "16";
    public static final String FLAG_17 = "17";
    public static final String FLAG_18 = "18";
    public static final String FLAG_19 = "19";
    public static final String FLAG_20 = "20";
    public static final String FLAG_21 = "21";
    public static final String FLAG_22 = "22";
    public static final String FLAG_23 = "23";
    public static final String FLAG_24 = "24";
    public static final String FLAG_25 = "25";

    public static final String RESULT_DEFAULT = "-1";
    public static final String RESULT_SUCCESS = "1";
    public static final String RESULT_FAILURE = "2";
    public static final String RESULT_UNKNOWN = "3";

    public static final String ACTIVITY_RESULT_DEFAULT = "-1";


    public static void ding(Context context, String flag) {
        ding(context, flag, RESULT_DEFAULT, ACTIVITY_RESULT_DEFAULT);
    }

    public static void ding(Context context, String flag, String result, String activity_result) {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(context);

        if (TextUtils.isEmpty(userId)) {
            return;
        }

        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            jsonObject.put("flag", flag);
            jsonObject.put("result", result);
            jsonObject.put("activity_result", activity_result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MaiDian maiDian = new MaiDian(context);
        maiDian.ding(jsonObject, new BaseNetCallBack<PostDataBean>() {

            @Override
            public void onSuccess(PostDataBean bean) {
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }
}
