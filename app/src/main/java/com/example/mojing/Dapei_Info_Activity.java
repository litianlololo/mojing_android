package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dapei_Info_Activity extends AppCompatActivity {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private ImageView deleteBtn;
    private SharedPreferencesManager sharedPreferencesManager;
    private Activity activity=this;
    private ImageView Img;
    private TextView AvgScoreText,AIScoreText;
    private TextView calendarBtn;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_info);

        Img = findViewById(R.id.Img);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        AvgScoreText = findViewById(R.id.AvgScoreText);
        AIScoreText = findViewById(R.id.AIScoreText);
        deleteBtn = findViewById(R.id.deleteBtn);
        calendarBtn = findViewById(R.id.calendarBtn);

        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        // 获取传递的Intent对象
        Intent intent = getIntent();
        // 从Intent对象中获取传递的id变量，假设id是整数类型
        byte[] byteArray = intent.getByteArrayExtra("bitmap");
        Bitmap receivedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String _id = intent.getStringExtra("_id");
        AvgScoreText.setText(Integer.toString(intent.getIntExtra("avg_score",0)));
        Img.setImageBitmap(receivedBitmap);
        getAIScore(_id);
//        String share_score=intent.getStringExtra("share_score");
//        String designer_score = intent.getStringExtra("designer_score");
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个 Intent 并添加数据
                Intent intent = new Intent(activity, Dapei_Calendar_Activity.class);
                intent.putExtra("match_id", _id);
                intent.putExtra("comb_img", receivedBitmap);
                // 启动新活动
                startActivity(intent);
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("确认删除");
                builder.setMessage("确定要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //创建一个OkHttpClient对象
                                OkHttpClient okHttpClient = new OkHttpClient();
                                // 创建请求
                                Request.Builder requestBuilder = new Request.Builder()
                                        .url(uu + "/cloth/del-match?match_id=" + _id)
                                        .delete()
                                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                                // 将会话信息添加到请求头部
                                if (sharedPreferencesManager.getKEY_Session_ID() != null) {
                                    //showRequestFailedDialog(sharedPreferencesManager.getKEY_Session_ID());
                                } else {
                                    showRequestFailedDialog("null");
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
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ShowAndDrop("删除成功");
                                                    }
                                                });
                                                break;
                                            case 1001:
                                                System.out.println(sharedPreferencesManager.getUsername());
                                                showRequestFailedDialog("缺失搭配ID");
                                                break;
                                            case 4001:
                                                System.out.println(sharedPreferencesManager.getUsername());
                                                showRequestFailedDialog("登录过期，请先登录");
                                                break;
                                            default:
                                                showRequestFailedDialog("删除失败");
                                                break;
                                        }
                                        System.out.println("Response: " + responseData);
                                        // 记得关闭响应体
                                        responseBody.close();
                                    } else {
                                        // 请求失败，处理错误
                                        System.out.println("Request failed");
                                        showRequestFailedDialog("网络错误，删除失败");
                                    }
                                } catch (IOException e) {
                                    showRequestFailedDialog("网络错误，删除失败");
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "取消," do nothing (the delete operation is not executed)
                    }
                });
                builder.show();
            }
        });
    }
    //获取AI评分
    private void getAIScore(String _id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("match_id",_id);
                    json.put("temperature",35);
                    //json.put("scene","");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                // 创建请求
                Request.Builder requestBuilder = new Request.Builder()
                        .url(uu + "/cloth/match/ai-score")
                        .post(requestBody)
                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
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
                        System.out.println(responseData);
                        // 提取键为"code"的值
                        int code = responseJson.getInt("code");
                        //确定返回状态
                        switch (code) {
                            case 200:
                                activity.runOnUiThread(new Runnable() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void run() {
                                        int score;
                                        try {
                                            JSONObject dataJson = responseJson.getJSONObject("data");
                                            score= dataJson.has("score") ? dataJson.getInt("score") : -1;
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                        AIScoreText.setText(Integer.toString(score));
                                    }
                                });
                                break;
                            case 1001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("缺失搭配ID");
                                break;
                            case 4001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("登录过期，请先登录");
                                break;
                            default:
                                showRequestFailedDialog("删除失败");
                                break;
                        }
                        System.out.println("Response: " + responseData);
                        // 记得关闭响应体
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                        showRequestFailedDialog("网络错误，删除失败");
                    }
                } catch (IOException e) {
                    showRequestFailedDialog("网络错误，删除失败");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    // 弹出请求失败的对话框
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private void ShowAndDrop(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("返回搭配相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定后执行页面跳转和关闭当前Activity
                Intent intent = new Intent(activity, Dapei_Album_Activity.class);
                startActivity(intent);
                finish(); //
            }
        });
        builder.show();
    }
}