package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dapei_Album_Activity extends AppCompatActivity {

    private View PBtn,AIBtn,DBtn;
    private String chosedone="PBtn";
    private GridLayout photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_album);

        PBtn = findViewById(R.id.Pview);
        AIBtn = findViewById(R.id.AIview);
        DBtn = findViewById(R.id.DView);
        photoView = findViewById(R.id.photoView);

        Drawable radius_border1 = getResources().getDrawable(R.drawable.radius_border1,null);
        Drawable radius_chosed = getResources().getDrawable(R.drawable.radius_border_chosed,null);
        PBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("PBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("AIBtn") ){
                        AIBtn.setBackground(radius_border1);
                    }else if(chosedone.equals("DBtn")){
                        DBtn.setBackground(radius_border1);
                    }
                    PBtn.setBackground(radius_chosed);
                    chosedone="PBtn";
                    //加载个人搭配
                    showphoto(1);
                }
            }
        });
        AIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("AIBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("PBtn") ){
                        PBtn.setBackground(radius_border1);
                    }else if(chosedone.equals("DBtn")){
                        DBtn.setBackground(radius_border1);
                    }
                    AIBtn.setBackground(radius_chosed);
                    chosedone="AIBtn";
                    //加载AI搭配
                    showphoto(2);
                }
            }
        });
        DBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("DBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("PBtn") ){
                        PBtn.setBackground(radius_border1);
                    }else if(chosedone.equals("AIBtn")){
                        AIBtn.setBackground(radius_border1);
                    }
                    DBtn.setBackground(radius_chosed);
                    chosedone="DBtn";
                    //加载AI搭配
                    showphoto(3);
                }
            }
        });
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        showphoto(20);
    }

    private void showphoto(int size) {
        photoView = findViewById(R.id.photoView);
        photoView.removeAllViews();

        int columnCount = 3; // Number of columns
        int spacingInPixels = (int) (getResources().getDisplayMetrics().density * 6); // Convert 6dp to pixels

        for (int i = 0; i < size; i++) {
            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new GridLayout.LayoutParams());
            imageView.setImageResource(R.drawable.dapei_tmp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            // Set the rounded corner background
            imageView.setBackgroundResource(R.drawable.radius_border1);
            // Set the image view's width and height to desired dimensions
            int desiredWidthInPixels = (int) (getResources().getDisplayMetrics().density * 10); // Convert 50dp to pixels
            int desiredHeightInPixels = (int) (getResources().getDisplayMetrics().density * 140); // Convert 100dp to pixels
            imageView.getLayoutParams().width = desiredWidthInPixels;
            imageView.getLayoutParams().height = desiredHeightInPixels;

            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.setMargins(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels);
            imageView.setLayoutParams(layoutParams);

            // Add the image view to the GridLayout
            photoView.addView(imageView);

            // Set a click listener for each image view
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When an image view is clicked, start Dapei_info_Activity
                    Intent intent = new Intent(Dapei_Album_Activity.this, Dapei_Info_Activity.class);
                    // You can pass any necessary data to Dapei_info_Activity using extras in the intent
                    // For example, you might want to pass the image ID or image URL to display in the Dapei_info_Activity
                    // For now, let's just pass a simple message as an example
                    intent.putExtra("image_data", "This is the image data for clicked image.");
                    startActivity(intent);
                }
            });
        }
    }

}