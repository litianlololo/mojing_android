package com.example.mojing.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mojing.InitFigureActivity;
import com.example.mojing.MainActivity;
import com.example.mojing.RegisterActivity;
import com.example.mojing.Yichu_Tops_Activity;
import com.example.mojing.Yichu_Bottoms_Activity;
import com.example.mojing.Yichu_Add_Activity;
import com.example.mojing.R;


import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment_yichu extends Fragment {
    private ImageView SImg,XImg,LImg;
    private TextView SText,XText,LText;
    private String type1,type2;
    private Activity activity;
    private List<String> shangzhuang = new ArrayList<>();
    private List<String> xiazhuang = new ArrayList<>();
    private List<String> lianshenzhuang = new ArrayList<>();
    private LinearLayout Btnlayout;
    public Fragment_yichu() {
        // Required empty public constructor
    }
    public static Fragment_yichu newInstance(String param1, String param2) {
        Fragment_yichu fragment = new Fragment_yichu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yichu, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity= getActivity();
        SImg = getActivity().findViewById(R.id.SImg);
        XImg = getActivity().findViewById(R.id.XImg);
        LImg = getActivity().findViewById(R.id.LImg);
        SText =getActivity().findViewById(R.id.SText);
        XText =getActivity().findViewById(R.id.XText);
        LText =getActivity().findViewById(R.id.LText);
        Btnlayout =activity.findViewById(R.id.Btnlayout);
        shangzhuang = new ArrayList<>();
        xiazhuang = new ArrayList<>();
        lianshenzhuang = new ArrayList<>();
        shangzhuang.add("全部");
        shangzhuang.add("T恤");
        shangzhuang.add("大衣");
        shangzhuang.add("衬衫");
        shangzhuang.add("西装");
        shangzhuang.add("开衫");
        shangzhuang.add("棒球服");
        shangzhuang.add("卫衣");
        shangzhuang.add("夹克");
        shangzhuang.add("斗篷/披风");
        shangzhuang.add("毛衣针织");
        shangzhuang.add("背心/吊带");
        shangzhuang.add("皮衣/皮革");
        shangzhuang.add("羽绒服");
        shangzhuang.add("棉衣/羊羔绒");
        shangzhuang.add("风衣");
        shangzhuang.add("POLO衫");
        shangzhuang.add("牛仔外套");

        xiazhuang.add("全部");
        xiazhuang.add("打底裤");
        xiazhuang.add("休闲裤");
        xiazhuang.add("运动裤");
        xiazhuang.add("牛仔裤");
        xiazhuang.add("半身裙");
        xiazhuang.add("其他裤子");

        lianshenzhuang.add("全部");
        lianshenzhuang.add("连衣裙");
        lianshenzhuang.add("连身裤");
        AddShangzhuangTextView();
        ImageView add_icon =getActivity().findViewById(R.id.add_icon);
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), Yichu_Add_Activity.class);
                startActivity(tmp);
            }
        });
        View.OnClickListener SClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(type1!="上装"){
                    type1="上装";
                    SImg.setImageResource(R.drawable.navbar_icon_home_press);
                    //SText.setTextColor(R.color.black);

                    XImg.setImageResource(R.drawable.navbar_icon_home_default_1);
                    //XText.setTextColor(R.color.defaultText);
                    LImg.setImageResource(R.drawable.navbar_icon_home_default_2);
                    //LText.setTextColor(R.color.defaultText);

                    AddShangzhuangTextView();
                }
            }
        };
        SImg.setOnClickListener(SClick);
        SText.setOnClickListener(SClick);

        View.OnClickListener XClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(type1!="下装"){
                    type1="下装";
                    XImg.setImageResource(R.drawable.navbar_icon_home_press_1);
                    //XText.setTextColor(R.color.black);

                    SImg.setImageResource(R.drawable.navbar_icon_home_default);
                   // SText.setTextColor(R.color.defaultText);
                    LImg.setImageResource(R.drawable.navbar_icon_home_default_2);
                    //LText.setTextColor(R.color.defaultText);

                    AddTextView(xiazhuang);
                }
            }
        };
        XImg.setOnClickListener(XClick);
        XText.setOnClickListener(XClick);

        View.OnClickListener LClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(type1!="连身装"){
                    type1="连身装";
                    LImg.setImageResource(R.drawable.navbar_icon_home_press_2);
                    //LText.setTextColor(R.color.black);

                    SImg.setImageResource(R.drawable.navbar_icon_home_default);
                    //SText.setTextColor(R.color.defaultText);
                    XImg.setImageResource(R.drawable.navbar_icon_home_default_1);
                    //XText.setTextColor(R.color.defaultText);

                    AddTextView(lianshenzhuang);
                }
            }
        };
        LImg.setOnClickListener(LClick);
        LText.setOnClickListener(LClick);
    }
    public void AddShangzhuangTextView()
    {
        Btnlayout.removeAllViews();
        List<TextView> textViews = new ArrayList<>();
        // 使用foreach循环遍历shangzhuang列表
        for (String str : shangzhuang) {

            // 创建TextView并设置文本内容
            TextView textView = new TextView(activity);
            textView.setText(str);
            // 设置文本居中
            textView.setGravity(Gravity.CENTER);
            // 设置布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1
            );
            textView.setLayoutParams(layoutParams);
            // 设置背景选择器
            textView.setBackgroundResource(R.drawable.textview_background);
            // 添加TextView到列表中
            textViews.add(textView);
            // 设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type2= str;
                    // 遍历所有TextView，设置背景色
                    for (TextView tv : textViews) {
                        if (tv == view) {
                            // 当前点击的TextView，设置为白色背景
                            tv.setBackgroundResource(android.R.color.white);
                        } else {
                            // 其他TextView，设置为36BFBEBE背景
                            tv.setBackgroundResource(R.drawable.textview_background);
                        }
                    }
                }
            });
            // 将TextView添加到Btnlayout中
            Btnlayout.addView(textView);
        }
        textViews.get(0).setBackgroundResource(android.R.color.white);
    }
    public void AddTextView(List<String> s)
    {
        Btnlayout.removeAllViews();
        List<TextView> textViews = new ArrayList<>();
        // 使用foreach循环遍历shangzhuang列表
        for (String str : s) {
            // 创建TextView并设置文本内容
            TextView textView = new TextView(activity);
            textView.setText(str);
            // 设置文本居中
            textView.setGravity(Gravity.CENTER);
            // 设置布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            textView.setLayoutParams(layoutParams);
            // 设置背景选择器
            textView.setBackgroundResource(R.drawable.textview_background);
            // 添加TextView到列表中
            textViews.add(textView);
            // 设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type2= str;
                    // 遍历所有TextView，设置背景色
                    for (TextView tv : textViews) {
                        if (tv == view) {
                            // 当前点击的TextView，设置为白色背景
                            tv.setBackgroundResource(android.R.color.white);
                        } else {
                            // 其他TextView，设置为36BFBEBE背景
                            tv.setBackgroundResource(R.drawable.textview_background);
                        }
                    }
                }
            });
            // 将TextView添加到Btnlayout中
            Btnlayout.addView(textView);
        }
        textViews.get(0).setBackgroundResource(android.R.color.white);
    }
}