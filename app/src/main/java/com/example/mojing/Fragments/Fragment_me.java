package com.example.mojing.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mojing.Dapei_Tag_Activity;
import com.example.mojing.LoginActivity;
import com.example.mojing.MainActivity;
import com.example.mojing.R;
import com.example.mojing.SharedPreferencesManager;

import org.w3c.dom.Text;

public class Fragment_me extends Fragment {
    MainActivity activity;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button exitButton;
    private TextView user_name;
    private TextView user_val;
    public Fragment_me() {
        // Required empty public constructor
    }
    public static Fragment_me newInstance(String param1, String param2) {
        Fragment_me fragment = new Fragment_me();
        return fragment;
    }
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
            user_val.setText(activity.sharedPreferencesManager.getUserVal());
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
                    Logout();
                    activity.restartMainActivity();
                }
            }
        });
    }

    private void Logout(){
        activity.sharedPreferencesManager.setLoggedIn(false);
        activity.sharedPreferencesManager.setUsername("Username");
        activity.sharedPreferencesManager.setUserVal("user_val");
        user_name.setText(activity.sharedPreferencesManager.getUsername());
        user_val.setText(activity.sharedPreferencesManager.getUserVal());
        exitButton.setText("注销");
    }

}