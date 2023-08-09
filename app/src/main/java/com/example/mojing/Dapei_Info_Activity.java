package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

public class Dapei_Info_Activity extends AppCompatActivity {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private ImageView Img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_info);

        Img = findViewById(R.id.Img);
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        // 获取传递的Intent对象
        Intent intent = getIntent();
        // 从Intent对象中获取传递的id变量，假设id是整数类型
        byte[] byteArray = intent.getByteArrayExtra("bitmap");
        Bitmap receivedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Img.setImageBitmap(receivedBitmap);
//        String share_score=intent.getStringExtra("share_score");
//        String designer_score = intent.getStringExtra("designer_score");
    }
}