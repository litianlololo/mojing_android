package com.example.mojing.Fragments;

import android.annotation.SuppressLint;
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
import com.example.mojing.Me_Figure_Activity;
import com.example.mojing.ModifyAccountActivity;
import com.example.mojing.PersonalItemView;
import com.example.mojing.R;
import com.example.mojing.SettingActivity;
import com.example.mojing.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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


public class Fragment_me extends Fragment {
    MainActivity activity;
    private SharedPreferencesManager sharedPreferencesManager;
    private Button exitButton;
    private ImageButton settingBtn;
    private TextView signatureText;
    private TextView user_name;
    private TextView user_ID;
    private TextView shengaoText, tizhongText, xiongweiText, yaoweiText, tunweiText;
    private ImageButton downloadBtn;
    private Button ModifyBtn;
    private PersonalItemView xiongwei_content, yaowei_content, tunwei_content;
    private TextView changjingBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        // 在这里更新 A Activity 的内容
        user_name.setText(sharedPreferencesManager.getUsername());
        signatureText.setText(sharedPreferencesManager.getUserSignature());
        shengaoText.setText(sharedPreferencesManager.getFigureShengao()+"cm");
        tizhongText.setText(sharedPreferencesManager.getFigureTizhong()+"kg");
        xiongweiText.setText(sharedPreferencesManager.getFigureXiongwei()+"cm");
        yaoweiText.setText(sharedPreferencesManager.getFigureYaowei()+"cm");
        tunweiText.setText(sharedPreferencesManager.getFigureTunwei()+"cm");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        sharedPreferencesManager = activity.getSharedPreferencesManager();

        user_name = activity.findViewById(R.id.user_name);
        user_ID = activity.findViewById(R.id.user_val);
        signatureText = activity.findViewById(R.id.signatureText);
        shengaoText = activity.findViewById(R.id.shengaoText);
        tizhongText = activity.findViewById(R.id.tizhongText);
        xiongweiText = activity.findViewById(R.id.xiongweiText);
        yaoweiText = activity.findViewById(R.id.yaoweiText);
        tunweiText = activity.findViewById(R.id.tunweiText);
        settingBtn = activity.findViewById(R.id.settingBtn);
        changjingBtn = activity.findViewById(R.id.changjingBtn);
        signatureText.setText(sharedPreferencesManager.getUserSignature());

//        changjingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomSheetDialog();
//            }
//        });
        //没有登录
        if (!activity.sharedPreferencesManager.isLoggedIn()) {
            //
        } else {
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
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), SettingActivity.class);
                startActivity(tmp);
            }
        });
//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), Me_Figure_Activity.class);
                startActivity(tmp);
            }
        };
        xiongwei_content = getActivity().findViewById(R.id.xiongwei_content);
        yaowei_content = getActivity().findViewById(R.id.yaowei_content);
        tunwei_content = getActivity().findViewById(R.id.tunwei_content);
        xiongwei_content.setOnClickListener(clickListener);
        yaowei_content.setOnClickListener(clickListener);
        tunwei_content.setOnClickListener(clickListener);
    }
//    public void showBottomSheetDialog() {
//        MainActivity.MyBottomSheetDialogFragment bottomSheetDialogFragment = new MainActivity.MyBottomSheetDialogFragment();
//        bottomSheetDialogFragment.show(getParentFragmentManager(), bottomSheetDialogFragment.getTag());
//
//        // 设置弹窗的高度为屏幕高度的1/4
//        bottomSheetDialogFragment.getDialog().getWindow().setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                getResources().getDisplayMetrics().heightPixels / 4
//        );
//    }
}