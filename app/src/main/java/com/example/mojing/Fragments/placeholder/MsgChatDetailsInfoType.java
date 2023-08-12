package com.example.mojing.Fragments.placeholder;

import android.graphics.Bitmap;

public class MsgChatDetailsInfoType {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;
    private Bitmap bitmapAvatar;

    public MsgChatDetailsInfoType(Bitmap bitmapAvatar,String content,int type){
        this.bitmapAvatar=bitmapAvatar;
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
    public Bitmap getBitmapAvatar(){return bitmapAvatar;}
}
