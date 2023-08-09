package com.example.mojing.Fragments.placeholder;

import java.sql.Time;
import java.util.Date;

public class MsgChatInfoType {
    private int avatarResId;
    private String name;
    private String msgText;
    private Date time;

    public MsgChatInfoType(int avatarResId, String name, String msgText, Date time) {
        this.name = name;
        this.msgText = msgText;
        this.avatarResId = avatarResId;
        this.time = time;
    }

    public String getName() { return name; }

    public String getMsg() {
        return msgText;
    }

    public Date getTime() {
        return time;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
