package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private CheckBox checkbox;
    private TextView LoginBtn;
    private TextView RegisteBtn;
    private Button SKIPBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        checkbox = findViewById(R.id.checkbox);
        LoginBtn =findViewById(R.id.LoginBtn);
        RegisteBtn = findViewById(R.id.RegisterBtn);
        SKIPBtn = findViewById(R.id.SKIP);
        SKIPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesManager.setIsYouke(true);
                Intent loginIntent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(loginIntent);finish();
            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkbox.isChecked()){
                    showToast(StartActivity.this,"请先阅读并同意协议");
                }
                else{
                    Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                    StartActivity.this.startActivity(loginIntent);
                }
            }
        });
        RegisteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkbox.isChecked()){
                    showToast(StartActivity.this,"请先阅读并同意协议");
                }
                else{
                    Intent RegisterIntent = new Intent(StartActivity.this, RegisterActivity.class);
                    StartActivity.this.startActivity(RegisterIntent);
                }
            }
        });
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}