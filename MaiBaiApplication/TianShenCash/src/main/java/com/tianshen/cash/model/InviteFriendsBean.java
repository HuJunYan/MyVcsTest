package com.tianshen.cash.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class InviteFriendsBean {
    public int code;
    public String msg;
    public InviteData data;


    public class InviteData {
        public String invite_url;
        public List<TopList> top_list;
        public List<RuleList> activity_list;

    }

    public class TopList {
        public String mobile_string;
        public String invite_num_string;
        public String invite_reward_string;
    }

    public class RuleList {
        public String activity_string;
    }

}