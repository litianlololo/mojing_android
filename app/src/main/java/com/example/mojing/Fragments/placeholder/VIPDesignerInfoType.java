package com.example.mojing.Fragments.placeholder;

public class VIPDesignerInfoType {

    private String id;
    private String name;
    private String introduction;
    private String avatar;
    private int numberOfOrder;

    public VIPDesignerInfoType(String id, String name, String avatar, String introduction, int numberOfOrder) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.avatar = avatar;
        this.numberOfOrder = numberOfOrder;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getIntroduction() {
        return introduction;
    }
    public String getNumberOfOrderText() {
        return String.valueOf(numberOfOrder);
    }
    public String getAvatar() { return avatar; }
}
