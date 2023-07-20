package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
    private TextView tv_shengao;
    private TextView tv_tizhong;
    private TextView tv_xiongwei;
    private TextView tv_yaowei;
    private TextView tv_tunwei;
    private EditText etTunwei, etShengao, etTizhong, etYaowei, etXiongwei;

    private Button btnModify;

    private SharedPreferencesManager sharedPreferencesManager;

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
        btnModify = findViewById(R.id.btn_modify);

        etTunwei = findViewById(R.id.et_tunwei);
        etShengao = findViewById(R.id.et_shengao);
        etTizhong = findViewById(R.id.et_tizhong);
        etYaowei = findViewById(R.id.et_yaowei);
        etXiongwei = findViewById(R.id.et_xiongwei);

        etTunwei.setVisibility(View.GONE);
        etShengao.setVisibility(View.GONE);
        etTizhong.setVisibility(View.GONE);
        etYaowei.setVisibility(View.GONE);
        etXiongwei.setVisibility(View.GONE);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        tv_shengao.setText(sharedPreferencesManager.getFigureShengao());
        tv_tizhong.setText(sharedPreferencesManager.getFigureTizhong());
        tv_tunwei.setText(sharedPreferencesManager.getFigureTunwei());
        tv_xiongwei.setText(sharedPreferencesManager.getFigureXiongwei());
        tv_yaowei.setText(sharedPreferencesManager.getFigureYaowei());

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
                if (btnModify.getText().toString().equals("修改")) {
                    // 将 TextView 替换为 EditText
                    tv_tunwei.setVisibility(View.GONE);
                    tv_shengao.setVisibility(View.GONE);
                    tv_tizhong.setVisibility(View.GONE);
                    tv_yaowei.setVisibility(View.GONE);
                    tv_xiongwei.setVisibility(View.GONE);

                    etTunwei.setVisibility(View.VISIBLE);
                    etShengao.setVisibility(View.VISIBLE);
                    etTizhong.setVisibility(View.VISIBLE);
                    etYaowei.setVisibility(View.VISIBLE);
                    etXiongwei.setVisibility(View.VISIBLE);

                    etTunwei.setText(tv_tunwei.getText().toString());
                    etShengao.setText(tv_shengao.getText().toString());
                    etTizhong.setText(tv_tizhong.getText().toString());
                    etYaowei.setText(tv_yaowei.getText().toString());
                    etXiongwei.setText(tv_xiongwei.getText().toString());

                    btnModify.setText("保存修改");
                } else if (btnModify.getText().toString().equals("保存修改")) {
                    // 保存修改的值到变量中
                    String modifiedTunwei = etTunwei.getText().toString();
                    String modifiedShengao = etShengao.getText().toString();
                    String modifiedTizhong = etTizhong.getText().toString();
                    String modifiedYaowei = etYaowei.getText().toString();
                    String modifiedXiongwei = etXiongwei.getText().toString();

                    if (!modifiedTunwei.equals("")) {
                        sharedPreferencesManager.setFigureTunwei(modifiedTunwei);
                        tv_tunwei.setText(modifiedTunwei);
                    }
                    if (!modifiedTunwei.equals("")) {
                        sharedPreferencesManager.setFigureShengao(modifiedShengao);
                        tv_shengao.setText(modifiedShengao);
                    }
                    if (!modifiedTunwei.equals("")) {
                        sharedPreferencesManager.setFigureTizhong(modifiedTizhong);
                        tv_tizhong.setText(modifiedTizhong);
                    }
                    if (!modifiedTunwei.equals("")) {
                        sharedPreferencesManager.setFigureXiongwei(modifiedXiongwei);
                        tv_yaowei.setText(modifiedYaowei);
                    }
                    if (!modifiedTunwei.equals("")) {
                        sharedPreferencesManager.setFigureYaowei(modifiedYaowei);
                        tv_xiongwei.setText(modifiedXiongwei);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            JSONObject json = new JSONObject();
                            try {
                                json.put("username", sharedPreferencesManager.getUsername());
                                json.put("phone_number", sharedPreferencesManager.getUserPhone());
                                json.put("shengao", sharedPreferencesManager.getFigureShengao());
                                json.put("tizhong", sharedPreferencesManager.getFigureTizhong());
                                json.put("xiongwei", sharedPreferencesManager.getFigureXiongwei());
                                json.put("yaowei", sharedPreferencesManager.getFigureYaowei());
                                json.put("tunwei", sharedPreferencesManager.getFigureTunwei());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            //创建一个OkHttpClient对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                            Request request = new Request.Builder()
                                    .url("http://47.102.43.156:8007/auth/update")
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
                                            showRequestFailedDialog("修改成功");
                                            break;
                                        case 1001:
                                            System.out.println(sharedPreferencesManager.getUsername());
                                            showRequestFailedDialog("请先登录");
                                            break;
                                    }
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

                    // 将 EditText 替换回 TextView
                    etTunwei.setVisibility(View.GONE);
                    etShengao.setVisibility(View.GONE);
                    etTizhong.setVisibility(View.GONE);
                    etYaowei.setVisibility(View.GONE);
                    etXiongwei.setVisibility(View.GONE);

                    tv_tunwei.setVisibility(View.VISIBLE);
                    tv_shengao.setVisibility(View.VISIBLE);
                    tv_tizhong.setVisibility(View.VISIBLE);
                    tv_yaowei.setVisibility(View.VISIBLE);
                    tv_xiongwei.setVisibility(View.VISIBLE);

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