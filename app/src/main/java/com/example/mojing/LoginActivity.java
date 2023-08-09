package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    //public String uu = "http://47.102.43.156:8007/api";
    public String uu="http://47.103.223.106:5004/api";
    private int countdownTime = 60; // 倒计时时长，单位：秒

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText UserPhoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView switchTextView;
    private TextView textViewRegister;
    private Button getCodeButton;
    private EditText verificationCodeEditText;
    private boolean isPasswordLogin = true; // 标志当前登录方式，初始为密码登录
    private boolean isRegister = false;     // false 登录 ture 注册
    private boolean needSet =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        // 初始化视图
        UserPhoneEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        getCodeButton = findViewById(R.id.buttonGetCode);
        verificationCodeEditText = findViewById(R.id.editTextVerificationCode);
        switchTextView = findViewById(R.id.textViewSwitch);

        //设置验证码登录相关为不可见
        getCodeButton.setVisibility(View.GONE);
        verificationCodeEditText.setVisibility(View.GONE);


        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);finish(); // 返回上一个Activity
            }
        });
        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = UserPhoneEditText.getText().toString();
                if(phone_number.length()!=11){
                    showRequestFailedDialog("请输入正确的手机号码！");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("phone_number", phone_number);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        Request request = new Request.Builder()
                                .url(uu+"/auth/code")
                                .post(requestBody)
                                .build();
                        // 发送请求并获取响应
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            // 检查响应是否成功
                            if (response.isSuccessful()) {
                                // 获取响应体
                                ResponseBody responseBody = response.body();
                                // 处理响应数据
                                String responseData = responseBody.string();
                                // 提取键为"code"的值
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                // 点击按钮后禁用按钮点击
                getCodeButton.setEnabled(false);
                // 开始倒计时
                startCountdownTimer();
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
        textViewRegister=findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);finish(); //
//                isRegister = !isRegister; // 切换登录注册标志
//
//                if (isRegister) {
//                    // 切换为注册界面
//                    passwordEditText.setVisibility(View.VISIBLE);
//                    getCodeButton.setVisibility(View.VISIBLE);
//                    verificationCodeEditText.setVisibility(View.VISIBLE);
//                    switchTextView.setVisibility(View.GONE);
//                    textViewRegister.setText("返回登录");
//                    loginButton.setText("注册");
//                } else {
//                    // 切换为验证码登录界面
//                    passwordEditText.setVisibility(View.GONE);
//                    getCodeButton.setVisibility(View.VISIBLE);
//                    verificationCodeEditText.setVisibility(View.VISIBLE);
//                    switchTextView.setVisibility(View.VISIBLE);
//                    textViewRegister.setText("注册");
//                    switchTextView.setText(R.string.switch_to_password_login);
//                    loginButton.setText("登录");
//                    isPasswordLogin = false;
//                }
            }
        });
        // 登录按钮的点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRegister) {
                    if (isPasswordLogin) {
                        // 使用密码登录
                        // 进行密码登录的逻辑处理
                        String phone_number = UserPhoneEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        if (phone_number.equals("") || password.equals("")) {
                            return;
                        }
                        ;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("phone", phone_number);
//                                    json.put("username", phone_number);
//                                    json.put("username", );
                                    json.put("password", password);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                //创建一个OkHttpClient对象
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                                Request request = new Request.Builder()
                                        //.url(uu+"/auth/login")
                                        .url(uu+"/auth/login")
                                        .post(requestBody)
                                        .build();
                                // 发送请求并获取响应
                                try {
                                    Response response = okHttpClient.newCall(request).execute();
                                    // 检查响应是否成功
                                    if (response.isSuccessful()) {

                                        // 获取响应体
                                        ResponseBody responseBody = response.body();
                                        // 处理响应数据
                                        String responseData = responseBody.string();
                                        JSONObject responseJson = new JSONObject(responseData);
                                        // 提取键为"code"的值
                                        int code = responseJson.getInt("code");
                                        //确定返回状态
                                        switch (code) {
                                            case 200:
                                                // 检查响应头部中是否存在 "Set-Cookie" 字段
                                                Headers headers = response.headers();
                                                List<String> cookies = headers.values("Set-Cookie");
                                                String s = cookies.get(0);
                                                System.out.println("cookie  "+s);
                                                String sessionCookie;
                                                if (s != null) {
                                                    // 在这里处理获取到的会话信息
                                                    // sessionCookie 变量中存储了服务器返回的会话信息
                                                    // 可以将其存储在本地，后续的请求可以携带这个会话信息
                                                    sessionCookie = s.substring(0, s.indexOf(";"));
                                                    sharedPreferencesManager.setKEY_Session_ID(sessionCookie);
                                                    //showRequestFailedDialog(sessionCookie);
                                                } else {
                                                    // 服务器没有返回会话信息
                                                    // 可能是未登录状态或者会话已经过期
                                                }
                                                setData(responseJson);
                                                // 登录成功，改变登录状态
                                                if (sharedPreferencesManager.isLoggedIn()) {
                                                    if(!needSet) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish(); // 结束当前的LoginActivity
                                                    }else{
                                                        Intent intent = new Intent(LoginActivity.this, InitFigureActivity.class);
                                                        startActivity(intent);
                                                        finish(); // 结束当前的LoginActivity
                                                    }
                                                }
                                                break;
                                            //登录成功
                                            case 2002:
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showRequestFailedDialog("用户不存在");
                                                    }
                                                });
                                                break;
                                            //用户不存在
                                            case 1002:
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showRequestFailedDialog("密码错误");
                                                    }
                                                });
                                                break;
                                            //密码错误
                                        }
                                        System.out.println("username: " + phone_number);
                                        System.out.println("password: " + password);
                                        System.out.println("Response: " + responseData);
                                        // 记得关闭响应体
                                        responseBody.close();
                                    } else {
                                        // 请求失败，处理错误
                                        System.out.println("Request failed");
                                    }
                                } catch (IOException e) {
                                    showRequestFailedDialog("网络请求失败");
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                        // 登录成功，改变登录状态
                        if (sharedPreferencesManager.isLoggedIn()) {
                            if(!needSet) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // 结束当前的LoginActivity
                            }else{
                                Intent intent = new Intent(LoginActivity.this, InitFigureActivity.class);
                                startActivity(intent);
                                finish(); // 结束当前的LoginActivity
                            }
                        }
                    } else {
                        // 使用验证码登录
                        // 进行验证码登录的逻辑处理
                        String phone_number = UserPhoneEditText.getText().toString();
                        String code = verificationCodeEditText.getText().toString();
                        if (phone_number.equals("") || code.equals("")) {
                            return;
                        }
                        ;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                                JSONObject json = new JSONObject();
                                try {
                                    json.put("phone_number", phone_number);
                                    json.put("code", code);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                //创建一个OkHttpClient对象
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                                Request request = new Request.Builder()
                                        .url(uu+"/auth/loginByCode")
                                        .post(requestBody)
                                        .build();
                                // 发送请求并获取响应
                                try {
                                    Response response = okHttpClient.newCall(request).execute();
                                    // 检查响应是否成功
                                    if (response.isSuccessful()) {
                                        // 获取响应体
                                        ResponseBody responseBody = response.body();
                                        // 处理响应数据
                                        String responseData = responseBody.string();
                                        JSONObject responseJson = new JSONObject(responseData);
                                        // 提取键为"code"的值
                                        int code = responseJson.getInt("code");
                                        //确定返回状态
                                        switch (code) {
                                            case 200:
                                                // 检查响应头部中是否存在 "Set-Cookie" 字段
                                                Headers headers = response.headers();
                                                List<String> cookies = headers.values("Set-Cookie");
                                                String s = cookies.get(0);
                                                System.out.println("cookie  "+s);
                                                String sessionCookie;
                                                if (s != null) {
                                                    // 在这里处理获取到的会话信息
                                                    // sessionCookie 变量中存储了服务器返回的会话信息
                                                    // 可以将其存储在本地，后续的请求可以携带这个会话信息
                                                    sessionCookie = s.substring(0, s.indexOf(";"));
                                                    sharedPreferencesManager.setKEY_Session_ID(sessionCookie);
                                                    //showRequestFailedDialog(sessionCookie);
                                                } else {
                                                    // 服务器没有返回会话信息
                                                    // 可能是未登录状态或者会话已经过期
                                                }
                                                setData(responseJson);
                                                // 登录成功，改变登录状态
                                                if (sharedPreferencesManager.isLoggedIn()) {
                                                    if(!needSet) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish(); // 结束当前的LoginActivity
                                                    }else{
                                                        Intent intent = new Intent(LoginActivity.this, InitFigureActivity.class);
                                                        startActivity(intent);
                                                        finish(); // 结束当前的LoginActivity
                                                    }
                                                }
                                                break;
                                            //登录成功
                                            case 2002:
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showRequestFailedDialog("用户不存在");
                                                    }
                                                });
                                                break;
                                            //用户不存在
                                            case 1002:
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        showRequestFailedDialog("验证码错误");
                                                    }
                                                });
                                                break;
                                            //密码错误
                                        }
                                        //System.out.println("Response: " + responseData);
                                        // 记得关闭响应体
                                        responseBody.close();
                                    } else {
                                        // 请求失败，处理错误
                                        System.out.println("Request failed");
                                    }
                                } catch (IOException e) {
                                    showRequestFailedDialog("网络请求失败");
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                    }
                }
        }
        });


    }
    private void startCountdownTimer() {
        // 创建一个倒计时的Runnable
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownTime > 0) {
                    // 设置按钮文字为剩余倒计时时间
                    getCodeButton.setText(countdownTime + "秒后重试");
                    countdownTime--;

                    // 延迟1秒后再次调用该Runnable
                    handler.postDelayed(this, 1000);
                } else {
                    // 倒计时结束后，恢复按钮状态和文字
                    countdownTime=60;
                    getCodeButton.setEnabled(true);
                    getCodeButton.setText("获取验证码");
                }
            }
        };

        // 首次启动倒计时
        handler.post(countdownRunnable);
    }
    // 弹出请求失败的对话框
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
    private void setData(JSONObject responseJson) throws JSONException {
        // 提取键为"data"的值
        JSONObject dataJson = responseJson.getJSONObject("data");
        System.out.println(dataJson);
        Integer shengao = dataJson.optInt("shengao", 0);
        Integer tizhong = dataJson.optInt("tizhong", 0);
        Integer tunwei = dataJson.optInt("tunwei", 0);
        Integer xiongwei = dataJson.optInt("xiongwei", 0);
        Integer yaowei = dataJson.optInt("yaowei", 0);
        String userID = dataJson.optString("_id", "");
        String userPhone = dataJson.optString("phone_number", "");
        String userRole = dataJson.optString("role", "");
        String username = dataJson.optString("username", "");

        if(shengao*tizhong*tunwei*xiongwei*yaowei==0)
            needSet=true;
        sharedPreferencesManager.setFigureShengao(shengao);
        sharedPreferencesManager.setFigureTizhong(tizhong);
        sharedPreferencesManager.setFigureTunwei(tunwei);
        sharedPreferencesManager.setFigureXiongwei(xiongwei);
        sharedPreferencesManager.setFigureYaowei(yaowei);
        sharedPreferencesManager.setUserID(userID);
        sharedPreferencesManager.setUserPhone(userPhone);
        sharedPreferencesManager.setUserRole(userRole);
        sharedPreferencesManager.setUsername(username);
        sharedPreferencesManager.setLoggedIn(true);
    }
}
//sharedPreferencesManager.setUserPassword(dataJson.getString("password"));
//sharedPreferencesManager.setUserPhone(dataJson.getString("phone"));
//sharedPreferencesManager.setUserRole(dataJson.getString("role"));