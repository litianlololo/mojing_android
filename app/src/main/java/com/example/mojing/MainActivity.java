package com.example.mojing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.mojing.Adapter.SectionsPagerAdapter;
import com.example.mojing.Fragments.Fragment_VIP;
import com.example.mojing.Fragments.Fragment_dapei;
import com.example.mojing.Fragments.Fragment_me;
import com.example.mojing.Fragments.Fragment_msg;
import com.example.mojing.Fragments.Fragment_yichu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
        boolean isLoggedIn = sharedPreferencesManager.isLoggedIn();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myTab = findViewById(R.id.tab);
        myPager2 = findViewById(R.id.viewpager2);

        titles.add("衣橱");
        titles.add("搭配");
        titles.add("VIP");
        titles.add("消息");
        titles.add("我的");

        icons.add(R.drawable.tabbar_icon_home_default);
        icons.add(R.drawable.tabbar_icon_home_default_1);
        icons.add(R.drawable.tabbar_icon_home_default_2);
        icons.add(R.drawable.tabbar_icon_home_default_3);
        icons.add(R.drawable.tabbar_icon_home_default_4);

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

        // 如果用户未登录并且不是游客模式，则打开登录页面
        if (!sharedPreferencesManager.isLoggedIn() && !sharedPreferencesManager.isYouke()) {
            Intent loginIntent = new Intent(this, StartActivity.class);
            this.startActivity(loginIntent);
            finish();
        }
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
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Permission.checkPermission(this);
    }
}