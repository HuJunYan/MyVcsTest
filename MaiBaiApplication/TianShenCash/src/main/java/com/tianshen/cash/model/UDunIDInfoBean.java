package com.tianshen.cash.model;

/**
 * Created by Administrator on 2017/8/24.
 */

public class UDunIDInfoBean {

    public String url_photoget;
    public String flag_sex;//性别
    public String ret_msg;
    public String id_name;//名字
    public String be_idcard;//
    public String id_no;//身份证号
    public String date_birthday;//生日
    public String url_frontcard;//身份证正面图片url
    public String addr_card;//地址
    public String branch_issued;//签发机关
    public String state_id;//民族
    public String url_backcard;//身份证背面图片url
    public String ret_code;
    public String result_auth;//是否认真成功
    public String start_card;//有限日期
    public String url_photoliving;//活体图片url

    public RiskTag risk_tag;

    public class RiskTag {
        public String living_attack;//活体攻击 1 存在风险 0 不存在风险
    }
}
