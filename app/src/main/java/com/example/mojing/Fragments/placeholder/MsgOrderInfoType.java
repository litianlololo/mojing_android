package com.example.mojing.Fragments.placeholder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MsgOrderInfoType {
    private String name;
    private String orderNumber;
    private BigDecimal money;
    private int orderTime;
    private String avatarUrl;

    public MsgOrderInfoType(String name, String orderNumber,BigDecimal money, int orderTime, String avatarUrl) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.avatarUrl = avatarUrl;
        this.money = money;
        this.orderTime = orderTime;
    }

    private BigDecimal toLessDigit(BigDecimal number){
        String numberStr = number.toString();
        int decimalIndex = numberStr.indexOf(".");
        char digit2 = numberStr.charAt(decimalIndex+2);
        if(numberStr.length()>=6&&digit2!='0')return number.setScale(2, RoundingMode.DOWN);
        char digit1 = numberStr.charAt(decimalIndex+1);
        if(numberStr.length()>=5&&digit1!='0')return number.setScale(1, RoundingMode.DOWN);
        else return number.setScale(0, RoundingMode.DOWN);
    }

    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMoney() {
        return toLessDigit(money).toPlainString();
    }

    public String getOrderTime() {
        return "x"+String.valueOf(orderTime);
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getAllMoney() {
        System.out.println("bagademical "+toLessDigit(money));
        return (toLessDigit(money).multiply(new BigDecimal(orderTime)).toPlainString());
    }
}
