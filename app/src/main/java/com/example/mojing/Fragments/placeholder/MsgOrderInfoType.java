package com.example.mojing.Fragments.placeholder;

public class MsgOrderInfoType {
    private String name;
    private String orderNumber;
    private int money;
    private int orderTime;
    private String avatarUrl;

    public MsgOrderInfoType(String name, String orderNumber,int money, int orderTime, String avatarUrl) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.avatarUrl = avatarUrl;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
