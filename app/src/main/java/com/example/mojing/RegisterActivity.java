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

public class RegisterActivity extends AppCompatActivity {
    private int countdownTime = 60; // 倒计时时长，单位：秒
    public String uu="http://47.102.43.156:8007/api";
    public String uuimg="http://47.102.43.156:8007";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private SharedPreferencesManager sharedPreferencesManager;
    private EditText UserPhoneEditText;
    private Button loginButton;
    private Button getCodeButton;
    private EditText verificationCodeEditText;
    private EditText passwordEditText;
    private EditText userNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        // 初始化视图
        UserPhoneEditText = findViewById(R.id.editTextUserPhone);
        loginButton = findViewById(R.id.buttonLogin);
        getCodeButton = findViewById(R.id.buttonGetCode);
        verificationCodeEditText = findViewById(R.id.editTextVerificationCode);
        passwordEditText = findViewById(R.id.editTextPassword);
        userNameEditText = findViewById(R.id.editTextUserName);
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
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
                            json.put("phone", phone_number);
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
                                // 获取失败的 HTTP 状态码
                                int statusCode = response.code();
                                System.out.println("Request failed with HTTP status code: " + statusCode);

                                // 获取失败的原因
                                String failureMessage = response.message();
                                System.out.println("Failure message: " + failureMessage);

                                // 获取失败响应体的内容（如果有）
                                String errorResponseBody = response.body().string();
                                System.out.println("Error response body: " + errorResponseBody);

                                // 记得关闭响应体
                                response.body().close();
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册按钮
                String phone_number = UserPhoneEditText.getText().toString();
                String code = verificationCodeEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String username = userNameEditText.getText().toString();
                if (phone_number.equals("") || code.equals("")) {
                    return;
                };

//                //默认成功
//                sharedPreferencesManager.setUserPhone(phone_number);
//                sharedPreferencesManager.setUserPassword(password);
//                sharedPreferencesManager.setUsername(username);
//                sharedPreferencesManager.setLoggedIn(true);
//                Intent tmp = new Intent(RegisterActivity.this, InitFigureActivity.class);
//                startActivity(tmp);
//                finish(); // 结束当前的LoginActivity

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("phone", phone_number);
                            json.put("code", code);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        Request request = new Request.Builder()
                                .url(uu+"/auth/register")
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
                                System.out.println(responseJson);
                                // 提取键为"code"的值
                                int code = responseJson.getInt("code");
                                //确定返回状态
                                switch (code) {
                                    case 200:
                                        // 检查响应头部中是否存在 "Set-Cookie" 字段
                                        Headers headers = response.headers();
                                        List<String> cookies = headers.values("Set-Cookie");
                                        String s = cookies.get(0);
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
                                        sharedPreferencesManager.setUserPhone(phone_number);
                                        sharedPreferencesManager.setLoggedIn(true);
                                        //setData(responseJson);
                                        break;
                                    //账号已注册
                                    case 4002:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("账号已存在");
                                            }
                                        });
                                        break;
                                    //验证码错误
                                    case 1002:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("验证码错误");
                                            }
                                        });
                                        break;
                                    default:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("错误代码："+code);
                                            }
                                        });
                                        break;
                                }
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showRequestFailedDialog("请求失败/auth/register");
                                    }
                                });
                            }
                        } catch (IOException e) {
                            showRequestFailedDialog("网络请求失败");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                 //登录成功，改变登录状态
                if (sharedPreferencesManager.isLoggedIn()) {
                    //默认成功
                    Intent tmp = new Intent(RegisterActivity.this, InitFigureActivity.class);
                    startActivity(tmp);
                    finish(); // 结束当前的LoginActivity
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
                    countdownTime = 60;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

        sharedPreferencesManager.setLoggedIn(true);
    }
    // 注册成功后弹出提醒
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册成功")
                .setMessage("恭喜您，注册成功！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 在点击确定按钮后执行跳转到目标 Activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // 可选：如果希望在跳转到目标 Activity 后关闭当前注册 Activity，则调用 finish()。
                    }
                })
                .setCancelable(false) // 设置为 false，确保用户只能点击确定按钮关闭弹窗。
                .show();
    }
}
