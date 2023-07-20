package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dapei_Album_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_album);

        Intent intent = getIntent();
        //获取传递的数据值
        String message = intent.getStringExtra("key");

        TextView text = findViewById(R.id.title);
        text.setSingleLine(true);
        text.setText(message);

        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        showphoto(8);
        String imageData = intent.getStringExtra("image_data");
    }

    private void showphoto(int size) {
        GridLayout photoView = findViewById(R.id.photoView);
        photoView.removeAllViews();

        int columnCount = 2; // Number of columns

        for (int i = 0; i < size; i++) {
            final ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new GridLayout.LayoutParams());
            imageView.setImageResource(R.drawable.dapei_tmp);

            // Set the image view's width and height to desired dimensions
            int desiredWidthInPixels = (int) (getResources().getDisplayMetrics().density * 200); // Convert 200dp to pixels
            int desiredHeightInPixels = (int) (getResources().getDisplayMetrics().density * 400); // Convert 400dp to pixels
            imageView.getLayoutParams().width = desiredWidthInPixels;
            imageView.getLayoutParams().height = desiredHeightInPixels;

            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);
            GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
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