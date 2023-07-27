package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.VipListAdapter;
import com.example.mojing.R;
import com.example.mojing.VIP_Designer_InfoType;

import java.util.ArrayList;
import java.util.List;


public class Fragment_VIP extends Fragment {

    public Fragment_VIP() {
        // Required empty public constructor
    }

    public static Fragment_VIP newInstance(String param1, String param2) {
        Fragment_VIP fragment = new Fragment_VIP();
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
        View rootView = inflater.inflate(R.layout.fragment__v_i_p, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.vipRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<VIP_Designer_InfoType> vip_designer_infoList = new ArrayList<>();
        vip_designer_infoList.add(new VIP_Designer_InfoType("User 1","bagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabagabaga", R.drawable.yichu_24,12));
        vip_designer_infoList.add(new VIP_Designer_InfoType("User 2","baga", R.drawable.dapei_24,34));
        vip_designer_infoList.add(new VIP_Designer_InfoType("User 3","baga", R.drawable.yichu_24,56));

        // 实例化适配器对象
        VipListAdapter vipListAdapter = new VipListAdapter(getActivity(), vip_designer_infoList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(vipListAdapter);

        return rootView;
    }

}