package com.tianshen.cash.constant;

public class GlobalParams {

    //face++买呗公司的key
    public static final String FACE_ADD_ADD_APPKEY_OLD = "cX9UMpO-z5GG1KJkuRslGCTiC9JQOUUJ";
    public static final String FACE_ADD_ADD_APPSECRET_OLD = "f8NhOZausOpR1pKNpQA5dgHNr0w3pdn5";
    //face++新公司的key
    public static final String FACE_ADD_ADD_APPKEY_NEW = "79ZS4Ei9K0yihvS3aoJqTEFBGEkh5HnJ";
    public static final String FACE_ADD_ADD_APPSECRET_NEW = "KB8Dj_byOU_O8TcOjtszkWRKK_z8O4SY";


    // 字符串常量
    public static String CONSUMPTIONRECORD_LOAD_LENGTH = "10";

    public static final String CHANGE_LOGIN_PASSWORD = "2";//修改登录密码
    public static final String CHANGE_PAY_PASSWORD = "1";//修改支付密码

    // 相机相关
    public static final int INTO_IDCARDSCAN_FRONT_PAGE = 50; // 身份证正面
    public static final int INTO_IDCARDSCAN_BACK_PAGE = 49; // 身份证反面
    public static final int PAGE_INTO_LIVENESS = 51; //活体检测

    public static String getSlot() {
        return "secret@9maibei.com";
    }

    public static final String GOTO_LOGIN_ACTIVITY = "goto_login_activity";

    public static final int MONEY_LEVER_1ST = 0; // 单位：分
    public static final int MONEY_LEVER_2ND = 20000; // 单位：分
    public static final int MONEY_LEVER_3RD = 50000; // 单位：分

    public static final int AMOUNT_TYPE_0 = 0; // 当前信用额度为0
    public static final int AMOUNT_TYPE_200 = 1; // 当前信用额度为200
    public static final int AMOUNT_TYPE_500 = 2; // 当前信用额度为500
    public static final int AMOUNT_TYPE_500_PLUS = 5; // 当前信用额度为>500

    public static final int CONSUME_TYPE_0 = 0; // 原消费额度+现消费额度-优惠额度=0
    public static final int CONSUME_TYPE_LESS_200 = 1; // 原消费额度+现消费额度-优惠额度<=200
    public static final int CONSUME_TYPE_LESS_500 = 2; // 原消费额度+现消费额度-优惠额度<=500
    public static final int CONSUME_TYPE_500_PLUS = 16; // 原消费额度+现消费额度-优惠额度>500

    // 广播相关的key值
    public static final String PASS_CALL_RECORD_ACTION = "qtq_pass_call_record_action";  // 通话记录没有获取到时，放行广播
    public static final String PASS_CONTACT_ACTION = "qtq_pass_contact_action";  // 通讯录没有获取到时，放行广播
    public static final String ADD_BORROW_TERM_KEY_ACTION = "qtq_add_borrow_term_action";//增大借款期限的推送

    public static final String LOGOUT_ACTION = "qtq_logout_action";//退出账号

    public static final int RETURN_FROM_ACTIVITY_ERROR = 202;   // 该数值不要轻易修改，有关联
    public static final int RETURN_FROM_ACTIVITY_BACK_KEY = 203;   // 该数值不要轻易修改，有关联

    public static final String REFUSE_TYPE_KEY = "refuse_type_key";
    public static final int REFUSE_BY_PERSON_TYPE = 1;
    public static final int REFUSE_BY_MACHINE_TYPE = 2;

    // 订单状态

    public static String LOG_FILE = "waterDriver/log.txt";//log路径
    // bundle相关的key值
    public static final String ADD_BORROW_TERM_KEY = "add_borrow_term_key"; // 增大可借款期限的key
    public static final String LOG_STATUS_NEED_UPLOAD = "log_status_need_upload";//需要上传log
    public static final String LOG_STATUS_IS_UPLOADING = "log_status_is_uploading";//正在上传log
    public static final String LOG_STATUS_IS_UPLOAD_fail = "log_status_upload_fail";//上传失败
    public static final String LOG_STATUS_NOT_NEED_UPLOAD = "log_status_not_need_upload";//不需要上传log

    public static final String IS_FROM_CARD_KEY = "is_from_card_key";
    public static final String CHINA_MOBILE_URL_KEY = "china_mobile_url_key";
    public static final String CHINA_MOBILE_TITLE_KEY = "china_mobile_title_key";
    public static final String WEB_URL_KEY = "web_url_key";

    //活动
    public static final String ACTIVITY_ID = "activity_id";

    public static final String SJD_BACK_DELAY_KEY = "sjd_back_delay_key";

    /**
     * 上传用户信息用到的key {@link NetConstantValue#getUploadUserInfoURL()}
     */
    public static final String USER_INFO_CALL_LIST = "call_list";//通话记录列表 key
    public static final String USER_INFO_APP_LIST = "app_list";//用户安装的app列表 key
    public static final String USER_INFO_MESSAGE_LIST = "message_list";//用户短信记录列表 key
    /**
     * 用户的id key
     */
    public static final String USER_CUSTOMER_ID = "customer_id";

    public static final String APP_QQ_ID = "1105998971";//qq id
    public static final String APP_WX_ID = "wx0eff7356b7c5cda5";//微信id
    public static final String APP_WEIBO_KEY = "3428550251"; //微博 key
    public static final int SHARE_TO_WECHAT_SESSION = 0; //分享给朋友
    public static final int SHARE_TO_WECHAT_TIMELINE = 1; //分享到朋友圈
    public static final String WEIBO_OAUTH_ADDRESS = "https://api.weibo.com/oauth2/default.html";//微博分享回调页面
    public static final String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"//微博分享权限
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static final String WEB_FROM = "web_from";//bundle key  webactivity
    public static final String WEB_TYPE = "web_type";//bundle key  webactivity
    public static final String WEB_MSG_DATA_KEY = "web_msg_data_key";//bundle key  webactivity
    public static final String FROM_HOME = "from_homefragment"; //bundle value from homefragment
    public static final String FROM_MESSAGE = "from_message";//bundle value from messageCenterActvivity
    public static final String FROM_JOKE = "from_joke";//bundle value from joke
    public static final String FROM_PROTOCOL = "from_protocol";//bundle value from joke
    public static final String TYPE_READ = "type_read"; //bundle value from homefragment
    public static final String IDENTITY_STATE_KEY = "is_auth_idcard";
    public static final String CONSUME_ID = "consume_id";//KEY

    public static final String UDUN_AUTH_KEY = "0db4e877-004a-4edc-8426-95b31e557956";//有盾 key

    public static final String SERVICE_ONLINE_KEY = "service_online_key";//客服地址bundl key

    //消息推送 用户点击用到的key
    public static final String NOTIFICATION_MESSAGE_KEY = "notification_message_key";
    public static final String NOTIFICATION_IS_ONRESUME_CLICK = "notification_is_onresume_click";
    //氪信的key
    public static final String KEXIN_KEY = "a6e8009528d52c93c3634294";
}
