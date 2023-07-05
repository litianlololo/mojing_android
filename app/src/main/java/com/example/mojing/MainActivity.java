package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.bottom_navigation);
        // 设置RadioGroup的选中监听器
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理选中项的逻辑
                switch (checkedId) {
                    case R.id.nav_yichu:
                        showToast("衣橱");
                        // 处理衣橱选中的逻辑
                        break;
                    case R.id.nav_dapei:
                        showToast("搭配");
                        // 处理搭配选中的逻辑
                        break;
                    case R.id.nav_vip:
                        // 处理VIP选中的逻辑
                        showToast("VIP");
                        break;
                    case R.id.nav_msg:
                        // 处理消息选中的逻辑
                        showToast("消息");
                        break;
                    case R.id.nav_me:
                        showToast("我的");
                        // 处理我的选中的逻辑
                        break;
                }
            }
        });
        // 默认选中第一个RadioButton
        radioGroup.check(R.id.nav_yichu);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}