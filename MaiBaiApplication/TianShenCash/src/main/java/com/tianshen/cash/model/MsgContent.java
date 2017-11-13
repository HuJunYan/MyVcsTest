package com.tianshen.cash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wang on 2017/11/13.
 */


public class MsgContent implements Parcelable {
    public String message_count;
    public String message_id;
    public String message_type;
    public String message_title;
    public String message_url;
    public String message_mark;
    public String message_share_title;
    public String message_share_url;
    public String message_share_description;

    protected MsgContent(Parcel in) {
        message_count = in.readString();
        message_id = in.readString();
        message_type = in.readString();
        message_title = in.readString();
        message_url = in.readString();
        message_mark = in.readString();
        message_share_title = in.readString();
        message_share_url = in.readString();
        message_share_description = in.readString();
    }

    public static final Creator<MsgContent> CREATOR = new Creator<MsgContent>() {
        @Override
        public MsgContent createFromParcel(Parcel in) {
            return new MsgContent(in);
        }

        @Override
        public MsgContent[] newArray(int size) {
            return new MsgContent[size];
        }
    };

    @Override
    public String toString() {
        return "MsgContent{" +
                "message_count='" + message_count + '\'' +
                ", message_id='" + message_id + '\'' +
                ", message_type='" + message_type + '\'' +
                ", message_title='" + message_title + '\'' +
                ", message_url='" + message_url + '\'' +
                ", message_mark='" + message_mark + '\'' +
                ", message_share_title='" + message_share_title + '\'' +
                ", message_share_url='" + message_share_url + '\'' +
                ", message_share_description='" + message_share_description + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message_count);
        dest.writeString(message_id);
        dest.writeString(message_type);
        dest.writeString(message_title);
        dest.writeString(message_url);
        dest.writeString(message_mark);
        dest.writeString(message_share_title);
        dest.writeString(message_share_url);
        dest.writeString(message_share_description);
    }
}