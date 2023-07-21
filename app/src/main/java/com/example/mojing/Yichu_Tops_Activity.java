package com.example.mojing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Yichu_Tops_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichu_tops);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        Button btn_addTop = findViewById(R.id.btn_addTop);
        btn_addTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Yichu_Tops_Activity.this, Yichu_Add_Activity.class);
                //传递额外的数据到目标Activity
                intent.putExtra("key", "添加上衣");
                startActivity(intent);
            }
        });
    }
}