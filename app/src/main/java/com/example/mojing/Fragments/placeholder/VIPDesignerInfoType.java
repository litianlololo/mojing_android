package com.example.mojing.Fragments.placeholder;

public class VIPDesignerInfoType {
    private String name;
    private String introduction;
    private int avatarResId; // 头像资源 ID

    private int numberOfOrder;

    public VIPDesignerInfoType(String name, String introduction, int avatarResId, int numberOfOrder) {
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
