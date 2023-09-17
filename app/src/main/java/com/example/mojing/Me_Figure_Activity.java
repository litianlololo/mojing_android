package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Me_Figure_Activity extends AppCompatActivity {
    public String uu="http://47.102.43.156:8007/api";
    public String uuimg="http://47.102.43.156:8007";
    private TextView tv_shengao;
    private TextView tv_tizhong;
    private TextView tv_xiongwei;
    private TextView tv_yaowei;
    private Activity activity = this;
    private TextView tv_tunwei,tv_password;
    private EditText etTunwei, etShengao, etTizhong, etYaowei, etXiongwei,etpassword;

    private Button btnModify;

    private SharedPreferencesManager sharedPreferencesManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_figure);

        //初始化
        tv_shengao = findViewById(R.id.tv_shengao);
        tv_tizhong = findViewById(R.id.tv_tizhong);
        tv_yaowei = findViewById(R.id.tv_yaowei);
        tv_xiongwei = findViewById(R.id.tv_xiongwei);
        tv_yaowei = findViewById(R.id.tv_yaowei);
        tv_tunwei = findViewById(R.id.tv_tunwei);
        tv_password = findViewById(R.id.tv_password);
        btnModify = findViewById(R.id.btn_modify);

        etTunwei = findViewById(R.id.et_tunwei);
        etShengao = findViewById(R.id.et_shengao);
        etTizhong = findViewById(R.id.et_tizhong);
        etYaowei = findViewById(R.id.et_yaowei);
        etXiongwei = findViewById(R.id.et_xiongwei);
        etpassword =findViewById(R.id.et_password);

        etTunwei.setVisibility(View.GONE);
        etShengao.setVisibility(View.GONE);
        etTizhong.setVisibility(View.GONE);
        etYaowei.setVisibility(View.GONE);
        etXiongwei.setVisibility(View.GONE);
        etpassword.setVisibility(View.GONE);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        tv_shengao.setText(Integer.toString(sharedPreferencesManager.getFigureShengao()));
        tv_tizhong.setText(Integer.toString(sharedPreferencesManager.getFigureTizhong()));
        tv_tunwei.setText(Integer.toString(sharedPreferencesManager.getFigureTunwei()));
        tv_xiongwei.setText(Integer.toString(sharedPreferencesManager.getFigureXiongwei()));
        tv_yaowei.setText(Integer.toString(sharedPreferencesManager.getFigureYaowei()));
        tv_password.setText(sharedPreferencesManager.getUserPassword());
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferencesManager.isYouke())
                {
                    showRequestFailedDialog("游客无此权限，请先登录");
                    return;
                }
                if (btnModify.getText().toString().equals("修改")) {
                    // 将 TextView 替换为 EditText
                    tv_tunwei.setVisibility(View.GONE);
                    tv_shengao.setVisibility(View.GONE);
                    tv_tizhong.setVisibility(View.GONE);
                    tv_yaowei.setVisibility(View.GONE);
                    tv_xiongwei.setVisibility(View.GONE);
                    tv_password.setVisibility(View.GONE);

                    etTunwei.setVisibility(View.VISIBLE);
                    etShengao.setVisibility(View.VISIBLE);
                    etTizhong.setVisibility(View.VISIBLE);
                    etYaowei.setVisibility(View.VISIBLE);
                    etXiongwei.setVisibility(View.VISIBLE);
                    etpassword.setVisibility(View.VISIBLE);

                    etTunwei.setText(tv_tunwei.getText().toString());
                    etShengao.setText(tv_shengao.getText().toString());
                    etTizhong.setText(tv_tizhong.getText().toString());
                    etYaowei.setText(tv_yaowei.getText().toString());
                    etXiongwei.setText(tv_xiongwei.getText().toString());
                    etpassword.setText(sharedPreferencesManager.getUserPassword());

                    btnModify.setText("保存修改");
                    //showRequestFailedDialog(sharedPreferencesManager.getKEY_Session_ID());
                } else if (btnModify.getText().toString().equals("保存修改")) {
                    // 保存修改的值到变量中
                    String tunweiText = etTunwei.getText().toString();
                    String shengaoText = etShengao.getText().toString();
                    String tizhongText = etTizhong.getText().toString();
                    String yaoweiText = etYaowei.getText().toString();
                    String xiongweiText = etXiongwei.getText().toString();

                    if(tunweiText.isEmpty()){
                        showRequestFailedDialog("臀围不能为空");
                        return;
                    }
                    if(shengaoText.isEmpty()){
                        showRequestFailedDialog("身高不能为空");
                        return;
                    }
                    if(tizhongText.isEmpty()){
                        showRequestFailedDialog("体重不能为空");
                        return;
                    }
                    if(yaoweiText.isEmpty()){
                        showRequestFailedDialog("腰围不能为空");
                        return;
                    }
                    if(xiongweiText.isEmpty()){
                        showRequestFailedDialog("胸围不能为空");
                        return;
                    }

                    int modifiedTunwei = Integer.parseInt(tunweiText);
                    int modifiedShengao =Integer.parseInt( shengaoText);
                    int modifiedTizhong = Integer.parseInt(tizhongText);
                    int modifiedYaowei = Integer.parseInt(yaoweiText);
                    int modifiedXiongwei = Integer.parseInt(xiongweiText);
                    String modifiedPassword = etpassword.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            JSONObject json = new JSONObject();
                            try {
                                if(sharedPreferencesManager.getUsername()!=null)
                                    json.put("username", sharedPreferencesManager.getUsername());
                                else json.put("username", "null");
                                //json.put("phone", sharedPreferencesManager.getUserPhone());
                                json.put("shengao", modifiedShengao);
                                json.put("tizhong", modifiedTizhong);
                                json.put("xiongwei", modifiedXiongwei);
                                json.put("yaowei", modifiedYaowei);
                                json.put("tunwei", modifiedTunwei);
                                System.out.println(modifiedShengao);
                                System.out.println(modifiedTizhong);
                                System.out.println(modifiedXiongwei);
                                System.out.println(modifiedYaowei);
                                System.out.println(modifiedTunwei);
                                if(!modifiedPassword.equals(sharedPreferencesManager.getUserPassword())){
                                    /*
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("修改密码");
                                    builder.setMessage("确定要修改您的密码为"+ modifiedPassword+"吗？");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                json.put("password",modifiedPassword);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                json.put("password",sharedPreferencesManager.getUserPassword());
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                    activity.runOnUiThread(new Runnable() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void run() {
                                            builder.show();
                                        }
                                    });
                                    */
                                    json.put("password",modifiedPassword);
                                }
                                else json.put("password",sharedPreferencesManager.getUserPassword());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            //创建一个OkHttpClient对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                            // 创建请求
                            Request.Builder requestBuilder = new Request.Builder()
                                    .url(uu+"/auth/update")
                                    .post(requestBody)
                                    .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                            // 将会话信息添加到请求头部
                            if (sharedPreferencesManager.getKEY_Session_ID() != null) {
                                //showRequestFailedDialog(sharedPreferencesManager.getKEY_Session_ID());
                            }else{
                            }
                            // 发送请求并获取响应
                            try {
                                Request request = requestBuilder.build();
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
                                            showRequestFailedDialog("修改成功");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                //if (!modifiedTunwei.equals("")) {
                                                    sharedPreferencesManager.setFigureTunwei(modifiedTunwei);
                                                    tv_tunwei.setText(Integer.toString(modifiedTunwei));
                                                    //}
                                                    sharedPreferencesManager.setFigureShengao(modifiedShengao);
                                                    tv_shengao.setText(Integer.toString(modifiedShengao));

                                                    sharedPreferencesManager.setFigureTizhong(modifiedTizhong);
                                                    tv_tizhong.setText(Integer.toString(modifiedTizhong));

                                                    sharedPreferencesManager.setFigureXiongwei(modifiedXiongwei);
                                                    tv_yaowei.setText(Integer.toString(modifiedYaowei));

                                                    sharedPreferencesManager.setFigureYaowei(modifiedYaowei);
                                                    tv_xiongwei.setText(Integer.toString(modifiedXiongwei));

                                                    sharedPreferencesManager.setUserPassword(modifiedPassword);

                                                    tv_password.setText(sharedPreferencesManager.getUserPassword());
                                                }
                                            });
                                            break;
                                        case 1001:
                                            System.out.println(sharedPreferencesManager.getUsername());
                                            showRequestFailedDialog("登录过期，请重新登陆");
                                            break;
                                        case 1002:
                                            showRequestFailedDialog("用户名已存在");
                                            break;
                                    }
                                    System.out.println("Response: " + responseData);
                                    // 记得关闭响应体
                                    responseBody.close();
                                } else {
                                    // 请求失败，处理错误
                                    System.out.println("Request failed");
                                    showRequestFailedDialog("请求失败");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();

                    // 将 EditText 替换回 TextView
                    etTunwei.setVisibility(View.GONE);
                    etShengao.setVisibility(View.GONE);
                    etTizhong.setVisibility(View.GONE);
                    etYaowei.setVisibility(View.GONE);
                    etXiongwei.setVisibility(View.GONE);
                    etpassword.setVisibility(View.GONE);

                    tv_tunwei.setVisibility(View.VISIBLE);
                    tv_shengao.setVisibility(View.VISIBLE);
                    tv_tizhong.setVisibility(View.VISIBLE);
                    tv_yaowei.setVisibility(View.VISIBLE);
                    tv_xiongwei.setVisibility(View.VISIBLE);
                    tv_password.setVisibility(View.VISIBLE);

                    btnModify.setText("修改");
                }
            }
        });
    }

    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Me_Figure_Activity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

}