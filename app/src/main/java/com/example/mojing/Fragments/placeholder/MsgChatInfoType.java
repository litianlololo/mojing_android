package com.example.mojing.Fragments.placeholder;

import java.util.Date;

public class MsgChatInfoType {
    private String avatarUrl;
    private String chatId;
    private String designerId;
    private String name;
    private String msgText;
    private Date time;

    public MsgChatInfoType(String avatarUrl, String name, String msgText, Date time,String chatId,String designerId) {
        this.name = name;
        this.msgText = msgText;
        this.avatarUrl = avatarUrl;
        this.time = time;
        this.chatId= chatId;
        this.designerId=designerId;
    }

    public String getName() { return name; }

    public String getMsg() {
        return msgText;
    }
    public String getChatId(){return chatId;}
    public String getDesignerId(){return designerId;}
    public Date getTime() {
        return time;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
