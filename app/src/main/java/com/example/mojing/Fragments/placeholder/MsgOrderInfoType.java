package com.example.mojing.Fragments.placeholder;

public class MsgOrderInfoType {
    private String name;
    private String orderNumber;
    private int money;
    private int orderTime;
    private int avatarResId; // 头像资源 ID

    public MsgOrderInfoType(String name, String orderNumber,int money, int orderTime, int avatarResId) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.avatarResId = avatarResId;
        this.money = money;
        this.orderTime = orderTime;
    }

    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMoney() {
        return String.valueOf(money);
    }

    public String getOrderTime() {
        return "x"+String.valueOf(orderTime);
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
