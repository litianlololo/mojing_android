package com.example.mojing;

public class VIP_Designer_InfoType {
    private String name;
    private String introduction;
    private int avatarResId; // 头像资源 ID

    private int numberOfOrder;

    public VIP_Designer_InfoType(String name, String introduction, int avatarResId, int numberOfOrder) {
        this.name = name;
        this.introduction = introduction;
        this.avatarResId = avatarResId;
        this.numberOfOrder = numberOfOrder;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getNumberOfOrderText() {
        return String.valueOf(numberOfOrder);
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
