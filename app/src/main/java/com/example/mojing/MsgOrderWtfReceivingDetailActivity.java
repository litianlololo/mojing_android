package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mojing.R;

public class MsgOrderWtfReceivingDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_order_wtf_receiving_detail);

        TextView textViewOrderNumber = findViewById(R.id.orderNumberText);
        ImageView imageViewAvatar = findViewById(R.id.avatarImage);
        TextView textViewName = findViewById(R.id.nameText);
        TextView textViewMoney = findViewById(R.id.moneyText);
        TextView textViewOrderTime = findViewById(R.id.orderTimeText);
        TextView textViewAllMoney = findViewById(R.id.textViewAllMoney);
        TextView textViewTime1 = findViewById(R.id.textViewTime1);
        TextView textViewTime2 = findViewById(R.id.textViewTime2);
        TextView textViewTime3 = findViewById(R.id.textViewTime3);

        String nameText =getIntent().getStringExtra("nameText");
        String moneyText =getIntent().getStringExtra("moneyText");
        String allMoneyText =getIntent().getStringExtra("allMoneyText");
        String orderNumberText =getIntent().getStringExtra("orderNumberText");
        String orderTimeText =getIntent().getStringExtra("orderTimeText");

        textViewOrderNumber.setText(orderNumberText);
        textViewName.setText(nameText);
        textViewMoney.setText(moneyText);
        textViewOrderTime.setText(orderTimeText);
        textViewAllMoney.setText(allMoneyText);

        // 设置头像图片
        if (getIntent().hasExtra("byteArray")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0, getIntent().getByteArrayExtra("byteArray").length);
            imageViewAvatar.setImageBitmap(bitmap);
        }

        ImageButton imageButtonBack = findViewById(R.id.buttonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
    }
}