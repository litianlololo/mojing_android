package com.example.mojing.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mojing.InitFigureActivity;
import com.example.mojing.InitSanweiActivity;
import com.example.mojing.LoginActivity;
import com.example.mojing.MainActivity;
import com.example.mojing.ModifyAccountActivity;
import com.example.mojing.R;
import com.example.mojing.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class Fragment_me extends Fragment {
    MainActivity activity;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button exitButton;
    private TextView user_name;
    private TextView user_ID;
    private ImageButton downloadBtn;
    private Button ModifyBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        sharedPreferencesManager = activity.getSharedPreferencesManager();

        user_name= activity.findViewById(R.id.user_name);
        user_ID = activity.findViewById(R.id.user_val);
        //exitButton = activity.findViewById(R.id.btn_exit);

        //没有登录
        if(!activity.sharedPreferencesManager.isLoggedIn()){
            exitButton.setText("点击登录/注册");
        }else{
            user_name.setText(activity.sharedPreferencesManager.getUsername());
            user_ID.setText(activity.sharedPreferencesManager.getUserID());
        }
        //编辑资料
        ModifyBtn = getActivity().findViewById(R.id.edit);
        ModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), ModifyAccountActivity.class);
                startActivity(tmp);
            }
        });
//        exitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //没有登录
//                if(!activity.sharedPreferencesManager.isLoggedIn()){
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                    showConfirmationDialog();
//                }
//            }
//        });
    }
    //确认退出登录弹窗
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("确认退出登录");
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行注销操作
                Logout();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
//                        JSONObject json = new JSONObject();
//                        try {
//                            json.put("phone_number", sharedPreferencesManager.getUserPhone());
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        //创建一个OkHttpClient对象
//                        OkHttpClient okHttpClient = new OkHttpClient();
//                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
//                        Request request = new Request.Builder()
//                                .url("http://47.102.43.156:8007/auth/remove-account")
//                                .post(requestBody)
//                                .build();
//                        // 发送请求并获取响应
//                        try {
//                            Response response = okHttpClient.newCall(request).execute();
//                            // 检查响应是否成功
//                            if (response.isSuccessful()) {
//                                // 获取响应体
//                                ResponseBody responseBody = response.body();
//                                // 处理响应数据
//                                String responseData = responseBody.string();
//                                System.out.println("Response: " + responseData);
//                                // 记得关闭响应体
//                                responseBody.close();
//                            } else {
//                                // 请求失败，处理错误
//                                System.out.println("Request failed");
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();


                activity.restartMainActivity();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    //退出登录
    private void Logout(){
        DataClear();
        user_name.setText(activity.sharedPreferencesManager.getUsername());
        user_ID.setText(activity.sharedPreferencesManager.getUserID());
        exitButton.setText("退出登录");
    }
    private void DataClear()
    {
        activity.sharedPreferencesManager.setLoggedIn(false);
        activity.sharedPreferencesManager.setUsername("Username");
        activity.sharedPreferencesManager.setUserPassword("user_password");
        activity.sharedPreferencesManager.setFigureYaowei("yaowei");
        activity.sharedPreferencesManager.setFigureXiongwei("xiongwei");
        activity.sharedPreferencesManager.setFigureTizhong("tizhong");
        activity.sharedPreferencesManager.setFigureTunwei("tunwei");
        activity.sharedPreferencesManager.setFigureShengao("shengao");
        activity.sharedPreferencesManager.setUserRole("userRole");
        activity.sharedPreferencesManager.setUserID("userID");
        activity.sharedPreferencesManager.setUserPhone("userPhone");
        //IsYouke重置
        sharedPreferencesManager.setIsYouke(false);
    }
}