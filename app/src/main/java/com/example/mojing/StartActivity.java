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

public class StartActivity extends AppCompatActivity {
    public String uu="http://47.102.43.156:8007/api";
    public String uuimg="http://47.102.43.156:8007";
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
                // 使用密码登录
                // 进行密码登录的逻辑处理
                String phone_number = "15294773148";
                String password = "123456";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("phone", phone_number);
                            json.put("password", password);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        Request request = new Request.Builder()
                                //.url(uu+"/auth/login")
                                .url(uu + "/auth/login")
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
                                        System.out.println("cookie  " + s);
                                        String sessionCookie;
                                        if (s != null) {
                                            // 在这里处理获取到的会话信息
                                            // sessionCookie 变量中存储了服务器返回的会话信息
                                            // 可以将其存储在本地，后续的请求可以携带这个会话信息
                                            sessionCookie = s.substring(0, s.indexOf(";"));
                                            sharedPreferencesManager.setKEY_Session_ID(sessionCookie);
                                        }
                                        setData(responseJson);
                                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish(); // 结束当前的LoginActivity

                                        break;
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
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                // 登录成功，改变登录状态
                if (sharedPreferencesManager.isLoggedIn()) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 结束当前的LoginActivity
                }
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
        // 如果用户未登录并且不是游客模式，则打开登录页面
        if (sharedPreferencesManager.isLoggedIn() || sharedPreferencesManager.isYouke()) {
            Intent loginIntent = new Intent(this, MainActivity.class);
            this.startActivity(loginIntent);
            finish();
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

        String bio =dataJson.optString("bio","");
        String profile =dataJson.optString("profile","/");
        String gender =dataJson.optString("gender","");

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
        sharedPreferencesManager.setIsYouke(true);
        sharedPreferencesManager.setKEY_USER_Profile(profile);
        sharedPreferencesManager.setUserSignature(bio);
        sharedPreferencesManager.setUserGender(gender);
    }
}