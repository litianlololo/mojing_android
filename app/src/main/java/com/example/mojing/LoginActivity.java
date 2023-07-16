package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchTextView;
    private TextView textViewRegister;
    private Button getCodeButton;
    private EditText verificationCodeEditText;
    private boolean isPasswordLogin = true; // 标志当前登录方式，初始为密码登录
    private boolean isRegister = false;     // false 登录 ture 注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        // 初始化视图
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        getCodeButton = findViewById(R.id.buttonGetCode);
        verificationCodeEditText = findViewById(R.id.editTextVerificationCode);
        switchTextView = findViewById(R.id.textViewSwitch);
        textViewRegister=findViewById(R.id.textViewRegister);

        //设置验证码登录相关为不可见
        getCodeButton.setVisibility(View.GONE);
        verificationCodeEditText.setVisibility(View.GONE);


        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });

        // 切换登录方式的点击事件
        switchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordLogin = !isPasswordLogin; // 切换登录方式标志

                if (isPasswordLogin) {
                    // 切换为密码登录界面
                    passwordEditText.setVisibility(View.VISIBLE);
                    getCodeButton.setVisibility(View.GONE);
                    verificationCodeEditText.setVisibility(View.GONE);
                    switchTextView.setText(R.string.switch_to_code_login);
                } else {
                    // 切换为验证码登录界面
                    passwordEditText.setVisibility(View.GONE);
                    getCodeButton.setVisibility(View.VISIBLE);
                    verificationCodeEditText.setVisibility(View.VISIBLE);
                    switchTextView.setText(R.string.switch_to_password_login);
                }
            }
        });
        // 切换注册的点击事件
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegister = !isRegister; // 切换登录方式标志

                if (isRegister) {
                    // 切换为注册界面
                    passwordEditText.setVisibility(View.GONE);
                    getCodeButton.setVisibility(View.VISIBLE);
                    verificationCodeEditText.setVisibility(View.VISIBLE);
                    switchTextView.setVisibility(View.GONE);
                    textViewRegister.setText("返回登录");
                    loginButton.setText("注册");
                } else {
                    // 切换为验证码登录界面
                    passwordEditText.setVisibility(View.GONE);
                    getCodeButton.setVisibility(View.VISIBLE);
                    verificationCodeEditText.setVisibility(View.VISIBLE);
                    switchTextView.setVisibility(View.VISIBLE);
                    textViewRegister.setText("注册");
                    switchTextView.setText(R.string.switch_to_password_login);
                    loginButton.setText("登录");
                    isPasswordLogin=false;
                }
            }
        });

        // 登录按钮的点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isPasswordLogin) {
                    // 使用密码登录
                    // 进行密码登录的逻辑处理
                    if(true){
                        // 登录成功，保存用户信息和登录状态
                        sharedPreferencesManager.setLoggedIn(true);
                        sharedPreferencesManager.setUsername(username);
                        sharedPreferencesManager.setUserVal(password);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username); // 设置需要传递的数据
                        intent.putExtra("password", password); // 设置需要传递的数据
                        startActivity(intent);
                        finish(); // 结束当前的LoginActivity
                    }
                } else {
                    // 使用验证码登录
                    // 进行验证码登录的逻辑处理
                }
            }
        });
        //注册按钮
    }
}



//public class LoginActivity extends AppCompatActivity {
//
//    private EditText usernameEditText;
//    private EditText passwordEditText;
//    private Button loginButton;
//    private SharedPreferencesManager sharedPreferencesManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        sharedPreferencesManager = new SharedPreferencesManager(this);
//        // 初始化视图和SharedPreferencesManager
//        usernameEditText = findViewById(R.id.usernameEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        loginButton = findViewById(R.id.loginButton);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 处理登录按钮点击事件
//                String username = usernameEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//
//                // 假设进行登录验证的逻辑
//                if (isValidLogin(username, password)) {
//                    // 登录成功，保存用户信息和登录状态
//                    sharedPreferencesManager.setLoggedIn(true);
//                    sharedPreferencesManager.setUsername(username);
//                    sharedPreferencesManager.setUserVal(password);
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("username", username); // 设置需要传递的数据
//                    intent.putExtra("password", password); // 设置需要传递的数据
//                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish(); // 结束当前的LoginActivity
//
//                } else {
//                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private boolean isValidLogin(String username, String password) {
//        return true;
//        // 进行登录验证的逻辑，这里仅作示例
//        //return username.equals("admin") && password.equals("123456");
//    }
//}