package com.example.mojing.Fragments.placeholder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MsgOrderInfoType {
    private String name;
    private String orderNumber;
    private BigDecimal money;
    private int orderTime;
    private String avatarUrl;
    private String time1="";
    private String time2="";
    private String time3="";
    private String time4="";
    public MsgOrderInfoType(String name, String orderNumber,BigDecimal money, int orderTime, String avatarUrl,
                            String time1, String time2, String time3, String time4) {
        this.name = name;
        this.orderNumber = orderNumber;
        this.avatarUrl = avatarUrl;
        this.money = money;
        this.orderTime = orderTime;
        if(null!=time1) this.time1=time1;
        if(null!=time2) this.time2=time2;
        if(null!=time3) this.time3=time3;
        if(null!=time4) this.time4=time4;
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

    public String getTime1(){return time1;}
    public String getTime2(){return time2;}
    public String getTime3(){return time3;}
    public String getTime4(){return time4;}
}
