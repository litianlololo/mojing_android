package com.example.mojing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Checkout implementation for the app
 */
public class VipChatActivity extends AppCompatActivity {
    private ImageView imageViewDesignerAvatar;
    private TextView textViewDesignerName;
    private EditText editTextToDesigner;
    private CheckBox checkBoxToCheckWardrobe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_chat);

        imageViewDesignerAvatar=findViewById(R.id.imageViewDesignerAvatar);
        textViewDesignerName=findViewById(R.id.textViewDesignerName);
        editTextToDesigner=findViewById(R.id.editTextToDesigner);
        checkBoxToCheckWardrobe=findViewById(R.id.checkBoxToCheckWardrobe);

        // 获取传递的头像资源ID
        int avatarResId = getIntent().getIntExtra("avatar_res_id", -1);
        // 设置头像图片
        if (avatarResId != -1) {
//            // 获取头像图片资源
//            Bitmap avatarBitmap = BitmapFactory.decodeResource(getResources(), avatarResId);
//
//            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), avatarBitmap);
//            roundedBitmapDrawable.setCircular(true); // 设置为圆形
//
//            int radius = 30;
//            roundedBitmapDrawable.setCornerRadius(radius);
//            roundedBitmapDrawable.setAntiAlias(true); // 设置抗锯齿
//
//            imageViewDesignerAvatar.setImageDrawable(roundedBitmapDrawable);

            //上述代码：设置圆角失败
            imageViewDesignerAvatar.setImageResource(avatarResId);
        }

        String nameText =getIntent().getStringExtra("name_text");
        textViewDesignerName.setText(nameText);


        ImageButton btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });


        Button buttonShijian = findViewById(R.id.buttonShijian);
        buttonShijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着时间 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonDidian = findViewById(R.id.buttonDidian);
        buttonDidian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着地点 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonChanghe = findViewById(R.id.buttonChanghe);
        buttonChanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着场合 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonBeizhu = findViewById(R.id.buttonBeizhu);
        buttonBeizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#其他备注 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });



        // 请求焦点
        editTextToDesigner.requestFocus();

        // 弹出键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}