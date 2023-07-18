package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.MsgFragmentAdapter;
import com.example.mojing.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Fragment_msg extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public Fragment_msg() {
        // Required empty public constructor
    }

    public static Fragment_msg newInstance(String param1, String param2) {
        Fragment_msg fragment = new Fragment_msg();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        // 创建ViewPager的适配器并设置给它
        MsgFragmentAdapter fragmentAdapter = new MsgFragmentAdapter(getChildFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new MsgOrderItemFragment(), "订单");
        fragmentAdapter.addFragment(new MsgChatItemFragment(), "消息");
        viewPager.setAdapter(fragmentAdapter);

        // 将TabLayout和ViewPager关联起来
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(fragmentAdapter.getFragmentTitle(position))
        );
        tabLayoutMediator.attach();

        return view;
    }
}