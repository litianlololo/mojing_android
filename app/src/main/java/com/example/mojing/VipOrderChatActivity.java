package com.example.mojing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.mojing.Fragments.placeholder.MsgChatDetailsInfoType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VipOrderChatActivity extends AppCompatActivity {
    public String uu="http://47.102.43.156:8007/api";
    private SharedPreferencesManager sharedPreferencesManager;
    private ImageView imageViewDesignerAvatar;
    private TextView textViewDesignerName;
    private EditText editTextToDesigner;
    private CheckBox checkBoxToCheckWardrobe;
    private boolean checkboxValue;
    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_order_chat);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        imageViewDesignerAvatar=findViewById(R.id.imageViewDesignerAvatar);
        textViewDesignerName=findViewById(R.id.textViewDesignerName);
        editTextToDesigner=findViewById(R.id.editTextToDesigner);
        checkBoxToCheckWardrobe=findViewById(R.id.checkBoxToCheckWardrobe);
        // 初始化checkboxValue为CheckBox的初始状态
        checkboxValue = checkBoxToCheckWardrobe.isChecked();
        // 设置头像图片
        if (getIntent().hasExtra("byteArray")) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0, getIntent().getByteArrayExtra("byteArray").length);
            imageViewDesignerAvatar.setImageBitmap(bitmap);
        }

        String id =getIntent().getStringExtra("id");
        String nameText =getIntent().getStringExtra("name_text");
        textViewDesignerName.setText(nameText);


//        ImageButton btnBack = findViewById(R.id.buttonBack);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { finish(); }
//        });

        // 监听CheckBox状态改变事件，更新checkboxValue的值
        checkBoxToCheckWardrobe.setOnCheckedChangeListener((buttonView, isChecked) ->
                checkboxValue = isChecked);

        Button buttonShijian = findViewById(R.id.buttonShijian);
        buttonShijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着时间 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonDidian = findViewById(R.id.buttonDidian);
        buttonDidian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着地点 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonChanghe = findViewById(R.id.buttonChanghe);
        buttonChanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#穿着场合 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        Button buttonBeizhu = findViewById(R.id.buttonBeizhu);
        buttonBeizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixedText = "\n#其他备注 ";   // 要添加的固定文本

                String currentText = editTextToDesigner.getText().toString();
                String newText = currentText + fixedText;

                editTextToDesigner.setText(newText);

                // 将光标移动到最后
                editTextToDesigner.setSelection(newText.length());

                // 弹出键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        // 请求焦点
        editTextToDesigner.requestFocus();

        // 弹出键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editTextToDesigner, InputMethodManager.SHOW_IMPLICIT);
        }

        buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextToDesigner.getText().toString();
                if(content.equals("")){
                    showRequestFailedDialog("请填写留言");
                    return;
                }
                // 获取当前日期
                LocalDate currentDate = LocalDate.now();

                // 将日期增加7天
                LocalDate futureDate = currentDate.plusDays(7);

                // 定义日期格式化器
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // 格式化日期为字符串
                String formattedDate = futureDate.format(formatter);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("comment",content);
                            json.put("type", 0);
                            json.put("deadline", formattedDate);
                            json.put("can_view_outfits", checkboxValue);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        HttpUrl.Builder urlBuilder = HttpUrl.parse(uu + "/service/customer/service/commit").newBuilder();
//                            urlBuilder.addQueryParameter("chat_id", chatIdText);
//                            urlBuilder.addQueryParameter("sender_id", customerID);
                        urlBuilder.addQueryParameter("designer_id", id);
                        String url = urlBuilder.build().toString();
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(url)
                                .post(requestBody)
//                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                                .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID_with_fake_cookie());

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
                                        finish();
                                        break;
                                    default:
                                        showRequestFailedDialog("发送失败viporderchat");
                                        break;
                                }
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                showRequestFailedDialog("网络错误viporderchat");
                            }
                        } catch (IOException e) {
                            showRequestFailedDialog("网络错误viporderchat");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            }
        });
    }
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(VipOrderChatActivity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
}