package com.example.mojing.Fragments.placeholder;

public class MsgChatDetailsInfoType {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;

    public MsgChatDetailsInfoType(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
