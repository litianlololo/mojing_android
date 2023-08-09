package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ModifyAccountActivity extends AppCompatActivity {
    private String username;
    private String signature;
    private String gender;
    private TextView usernameText,signatureText,genderText;
    private Activity activity=this;
    private PersonalItemView username_content,signature_content,gender_content;
    private SharedPreferencesManager sharedPreferencesManager;
    private  BottomSheetDialog genderbottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        username = sharedPreferencesManager.getUsername();
        signature = sharedPreferencesManager.getUserSignature();
        gender = sharedPreferencesManager.getUserGender();

        usernameText=findViewById(R.id.usernameText);
        signatureText=findViewById(R.id.signatureText);
        genderText =findViewById(R.id.genderText);

        usernameText.setText(username);
        signatureText.setText(signature);
        genderText.setText(gender);

        username_content=findViewById(R.id.username_content);
        signature_content=findViewById(R.id.signature_content);
        gender_content=findViewById(R.id.gender_content);

        username_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("请输入新的昵称", new OnInputCompleteListener() {
                    public void onInputComplete(String input) {
                        username = input;
                        sharedPreferencesManager.setUsername(username);
                        usernameText.setText(username);
                    }
                });
            }
        });
        signature_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("请输入新的个性签名", new OnInputCompleteListener() {
                    public void onInputComplete(String input) {
                        signature = input;
                        sharedPreferencesManager.setUserSignature(signature);
                        signatureText.setText(signature);
                    }
                });
            }
        });
        gender_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderBottomSheet();
            };
        });
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
    }

    // 自定义接口，用于回调输入完成事件
    private interface OnInputCompleteListener {
        void onInputComplete(String input);
    }

    private void showInputDialog(String prompt, final OnInputCompleteListener listener) {
        final EditText input = new EditText(this);
        input.setHint(""); // 设置输入框的提示文字

        new AlertDialog.Builder(this)
                .setTitle(prompt) // 设置弹窗标题
                .setView(input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        listener.onInputComplete(userInput);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void genderBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        genderbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("女");
        dataList.add("男");
        dataList.add("你猜");
        wheelPicker.setData(dataList);
        TextView xiuchangBtn = view.findViewById(R.id.xiuchangBtn);
        TextView shenchangText =findViewById(R.id.shenchangText);
        xiuchangBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                sharedPreferencesManager.setUserGender(select);
                genderText.setText(select);
                genderbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        genderbottomSheetDialog.setContentView(view);
        genderbottomSheetDialog.show();
    }
}