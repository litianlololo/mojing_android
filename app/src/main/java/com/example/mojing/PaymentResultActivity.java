package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mojing.R;

public class PaymentResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        String designer =getIntent().getStringExtra("designer");
        String payMoney =getIntent().getStringExtra("payMoney");
        int payMethod =getIntent().getIntExtra("payMethod",0);

        ImageButton back = findViewById(R.id.back);
        Button buttonBack = findViewById(R.id.buttonBack);
        TextView textViewMoney = findViewById(R.id.textViewMoney);
        TextView textViewDesignerName = findViewById(R.id.textViewDesignerName);
        ImageView imageIcon = findViewById(R.id.imageIcon);

        textViewMoney.setText(payMoney);
        textViewDesignerName.setText(designer);
        if(0==payMethod)
            imageIcon.setImageResource(R.drawable.icon_zhifubao);
        else
            imageIcon.setImageResource(R.drawable.icon_weixin);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {finish();}
        });
        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {finish();}
        });
    }
}