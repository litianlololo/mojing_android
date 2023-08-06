package com.example.mojing;

public class Danpin {
    public String _id;
    public String name;
    public String img_url;
    public String type;
    public String type2;
    public Season season= new Season();
    public class Season{
        public boolean spring=false;
        public boolean summer=false;
        public boolean autumn=false;
        public boolean winter =false;
    }
    public String storeplace;
    public String lingxing;
    public String bihe;
    public String xiuchang;
    public String mianliao;
    public String fengge;
    public String shenchang;

    public String version;
    public String pattern;
    public String[] scene;
    public String[] fabric;
    public int[] temperature;
    public String[] highlights;
    public String[] tags;
}
