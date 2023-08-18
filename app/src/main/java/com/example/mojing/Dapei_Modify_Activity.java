package com.example.mojing;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dapei_Modify_Activity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private String match_id;
    private String up_img_url;
    private String down_img_url;
    private Activity activity=this;
    private BottomSheetDialog bottomSheetDialog;
    private int changjingCNT=5;
    private Boolean[] isView;
    private boolean[] changjingChosed = new boolean[5];
    private View[] changjingView;
    private TextView changjingText,savaBtn;
    private ImageView ImgBtn_1,ImgBtn_2;
    private String uu="http://47.103.223.106:5004/api";
    private String uuimg="http://47.103.223.106:5004";
    private Dapei dapei=new Dapei();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_modify);

        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Dapei_Album_Activity.class);
                startActivity(intent);
                finish(); // 返回上一个Activity
            }
        });

        sharedPreferencesManager = new SharedPreferencesManager(this);
        // 获取传递的Intent对象
        Intent intent = getIntent();
        // 从Intent对象中获取传递的id变量，假设id是整数类型
        match_id = intent.getStringExtra("match_id");
        up_img_url = intent.getStringExtra("up_img_url");
        down_img_url = intent.getStringExtra("down_img_url");
        ArrayList<String> SceneList = intent.getStringArrayListExtra("scene");

        dapei._id=match_id;
        System.out.println("dapei_id "+dapei._id);
        dapei.up.img_url=up_img_url;
        dapei.down.img_url=down_img_url;
        dapei.scene = SceneList;
        System.out.println(dapei.scene);

        ImgBtn_1 = findViewById(R.id.imgBtn_1);
        ImgBtn_2 = findViewById(R.id.imgBtn_2);
        ImgBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Dapei_ChoseUp_Activity.class);
                activityLauncher_up.launch(intent);
            }
        });

        ImgBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Dapei_ChoseDown_Activity.class);
                activityLauncher_down.launch(intent);
            }
        });
        changjingText=findViewById(R.id.changjingBtn2);
        changjingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet();
            }
        });
        savaBtn=findViewById(R.id.saveBtn);
        savaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("_id",dapei._id);
                            if(dapei.up._id!=null) {
                                json.put("up_cloth", dapei.up._id);
                                json.put("up_img_url", dapei.up.img_url);
                            }
                            if(dapei.down._id!=null) {
                                json.put("down_cloth", dapei.down._id);
                                json.put("down_img_url", dapei.down.img_url);
                            }
                            JSONArray sceneArray = new JSONArray(); int i=0;
                            if(changjingChosed[i++]) sceneArray.put("工作");
                            if(changjingChosed[i++]) sceneArray.put("休闲");
                            if(changjingChosed[i++]) sceneArray.put("运动");
                            if(changjingChosed[i++]) sceneArray.put("旅行");
                            if(changjingChosed[i++]) sceneArray.put("约会");
                            json.put("scene",sceneArray);
                            System.out.println("JSON:"+json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(uu+"/cloth/modify-match")
                                .put(requestBody)
                                .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                        // 将会话信息添加到请求头部
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
                                System.out.println(responseData);
                                //确定返回状态
                                switch (code) {
                                    case 200:
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("已成功修改");
                                            }
                                        });
                                        JSONObject dataJson = responseJson.getJSONObject("data");
                                        System.out.println(dataJson);
                                        break;
                                    default:
                                        showRequestFailedDialog("修改搭配失败");
                                        break;
                                }
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                showRequestFailedDialog("网络错误，添加失败");
                            }
                        } catch (IOException e) {
                            showRequestFailedDialog("网络错误，添加失败");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
        Glide.with(activity)
                .load(uuimg + up_img_url)
                //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                .error(R.drawable.error) // Error image (optional)
                .into(ImgBtn_1);
        Glide.with(activity)
                .load(uuimg + down_img_url)
                //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                .error(R.drawable.error) // Error image (optional)
                .into(ImgBtn_2);
    }
    private ActivityResultLauncher<Intent> activityLauncher_up = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("_id")){
                        dapei.up._id=data.getStringExtra("_id");
                    }
                    System.out.println("up._id= "+dapei.up._id);
                    if (data != null && data.hasExtra("img_url")) {
                        dapei.up.img_url = data.getStringExtra("img_url");
                        // 在这里处理收到的数据
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(activity)
                                        .load(uuimg + dapei.up.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_1);
                            }
                        });
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> activityLauncher_down = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 2) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("_id")){
                        dapei.down._id=data.getStringExtra("_id");
                    }
                    if (data != null && data.hasExtra("img_url")) {
                        dapei.down.img_url = data.getStringExtra("img_url");
                        // 在这里处理收到的数据
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(activity)
                                        .load(uuimg + dapei.down.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_2);
                            }
                        });
                    }
                }
            }
    );
    @SuppressLint("UseCompatLoadingForDrawables")
    private void BottomSheet(){
        Drawable radius_border1,radius_chosed;
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.dapei_changjing, null, false);
        bottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        bottomSheetDialog.setContentView(view);
        changjingView=new View[changjingCNT];
        isView=new Boolean[5];
        Arrays.fill(isView, Boolean.FALSE);
        changjingView[0]=view.findViewById(R.id.workView);
        changjingView[1]=view.findViewById(R.id.travelView);
        changjingView[2] =view.findViewById(R.id.dateView);
        changjingView[3]=view.findViewById(R.id.leisureView);
        changjingView[4]=view.findViewById(R.id.sportsView);
        radius_border1 = getResources().getDrawable(R.drawable.radius_border1,null);
        radius_chosed = getResources().getDrawable(R.drawable.radius_border_chosed,null);
        for(String str:dapei.scene){
            switch (str){
                case "工作":
                    changjingChosed[0]=true;
                    break;
                case "休闲":
                    changjingChosed[1]=true;
                    break;
                case "运动":
                    changjingChosed[2]=true;
                    break;
                case "旅行":
                    changjingChosed[3]=true;
                    break;
                case "约会":
                    changjingChosed[4]=true;
                    break;
            }
        }
        for (int tmp = 0; tmp < changjingCNT; tmp++) {
            int finalTmp = tmp;
            if(changjingChosed[finalTmp])
                changjingView[finalTmp].setBackground(radius_chosed);
            changjingView[tmp].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isView[finalTmp]) {
                        //该场景未被选中
                        changjingChosed[finalTmp]=true;
                        changjingView[finalTmp].setBackground(radius_chosed);
                    } else {
                        //该场景已被选中
                        changjingChosed[finalTmp]=false;
                        changjingView[finalTmp].setBackground(radius_border1);
                    }
                    isView[finalTmp] = !isView[finalTmp];
                }
            });
        }

        bottomSheetDialog.show();
    }
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
}