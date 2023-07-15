package com.example.mojing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mojing.Adapter.SectionsPagerAdapter;
import com.example.mojing.Fragments.Fragment_VIP;
import com.example.mojing.Fragments.Fragment_dapei;
import com.example.mojing.Fragments.Fragment_me;
import com.example.mojing.Fragments.Fragment_msg;
import com.example.mojing.Fragments.Fragment_yichu;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //sharedPreferences 用于存储用户基础信息
    public SharedPreferencesManager sharedPreferencesManager;
    private TabLayout myTab;
    private ViewPager2 myPager2;

    List<String> titles = new ArrayList<>();
    List<Integer> icons = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        sharedPreferencesManager = new SharedPreferencesManager(this);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myTab = findViewById(R.id.tab);
        myPager2 = findViewById(R.id.viewpager2);

        titles.add("衣橱");
        titles.add("搭配");
        titles.add("VIP");
        titles.add("消息");
        titles.add("我的");

        icons.add(R.drawable.yichu_24);
        icons.add(R.drawable.dapei_24);
        icons.add(R.drawable.vip_24);
        icons.add(R.drawable.msg_24);
        icons.add(R.drawable.user_24);

        fragments.add(new Fragment_yichu());
        fragments.add(new Fragment_dapei());
        fragments.add(new Fragment_VIP());
        fragments.add(new Fragment_msg());
        fragments.add(new Fragment_me());
        myTab.setTabMode(TabLayout.MODE_FIXED);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        myPager2.setAdapter(sectionsPagerAdapter);

        new TabLayoutMediator(myTab, myPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
                tab.setIcon(icons.get(position));
            }
        }).attach();

        // 接收传递的数据
//        Intent intent = getIntent();
//        if (intent != null) {
//            String value = intent.getStringExtra("username");
//            // 根据需要更新组件内容
//            TextView textView = findViewById(R.id.user_name);
//            textView.setText(value);
//        }
//        if (intent != null) {
//            String value = intent.getStringExtra("password");
//            // 根据需要更新组件内容
//            TextView textView = findViewById(R.id.user_val);
//            textView.setText(value);
//        }
    }

    public SharedPreferencesManager getSharedPreferencesManager() {
        return sharedPreferencesManager;
    }

    //重启MainActivity
    public void restartMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 结束当前的Activity
    }
}