package com.example.mojing;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Dapei {
    public String _id;
    public Danpin up=new Danpin();
    public Bitmap up_img;
    public Bitmap down_img;
    public Bitmap combin_img;
    public Danpin down = new Danpin();
    public String name;
    public String origin;
    public List<String> scene = new ArrayList<>();
    public String[] tags;

    public String dapei_id;
    public int AI_Score;
    public int[] share_score;
    public int[] designer_score;

}
