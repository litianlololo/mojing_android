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
import android.widget.TextView;

import com.example.mojing.LoginActivity;
import com.example.mojing.MainActivity;
import com.example.mojing.R;
import com.example.mojing.SharedPreferencesManager;


public class Fragment_me extends Fragment {
    MainActivity activity;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button exitButton;
    private TextView user_name;
    private TextView user_val;
    //    public Fragment_me() {
//        // Required empty public constructor
//    }
//    public static Fragment_me newInstance(String param1, String param2) {
//        Fragment_me fragment = new Fragment_me();
//        return fragment;
//    }
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
        user_val = activity.findViewById(R.id.user_val);
        exitButton = activity.findViewById(R.id.btn_exit);
        //没有登录
        if(!activity.sharedPreferencesManager.isLoggedIn()){
            exitButton.setText("点击登录/注册");
        }else{
            user_name.setText(activity.sharedPreferencesManager.getUsername());
            user_val.setText(activity.sharedPreferencesManager.getUserPassword());
        }
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //没有登录
                if(!activity.sharedPreferencesManager.isLoggedIn()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    showConfirmationDialog();
                }
            }
        });
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
        user_val.setText(activity.sharedPreferencesManager.getUserPassword());
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

    }
}