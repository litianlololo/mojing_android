package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingActivity extends AppCompatActivity {

    private Button exitBtn;
    private SharedPreferencesManager sharedPreferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        exitBtn= findViewById(R.id.exitButton);
        if(!sharedPreferencesManager.isLoggedIn()) exitBtn.setText("点击登录");
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferencesManager.isLoggedIn())
                    showConfirmationDialog();
                else{
                    // 执行注销操作
                    DataClear();
                    Intent intent = new Intent(SettingActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish(); // 结束当前
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
    }
    //确认退出登录弹窗
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出登录");
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行注销操作
                DataClear();

                Intent intent = new Intent(SettingActivity.this, StartActivity.class);
                startActivity(intent);
                finish(); // 结束当前
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    //退出登录
    private void DataClear()
    {
        sharedPreferencesManager.setLoggedIn(false);
        sharedPreferencesManager.setUsername("Username");
        sharedPreferencesManager.setUserPassword("user_password");
        sharedPreferencesManager.setFigureYaowei(0);
        sharedPreferencesManager.setFigureXiongwei(0);
        sharedPreferencesManager.setFigureTizhong(0);
        sharedPreferencesManager.setFigureTunwei(0);
        sharedPreferencesManager.setFigureShengao(0);
        sharedPreferencesManager.setUserRole("userRole");
        sharedPreferencesManager.setUserID("userID");
        sharedPreferencesManager.setUserPhone("userPhone");
        sharedPreferencesManager.setKEY_Session_ID("");
        sharedPreferencesManager.setUserSignature("");
        sharedPreferencesManager.setUserGender("");
        //IsYouke重置
        sharedPreferencesManager.setIsYouke(false);
    }
}