package com.example.mojing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.Adapter.MsgOrderWtfDeliveryAdapter;
import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;

import java.util.ArrayList;
import java.util.List;

public class MsgOrderWtfDeliveryFragment extends Fragment {

    public MsgOrderWtfDeliveryFragment() {
    }

    @SuppressWarnings("unused")
    public static MsgOrderWtfDeliveryFragment newInstance(int columnCount) {
        MsgOrderWtfDeliveryFragment fragment = new MsgOrderWtfDeliveryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_order_wtf_delivery_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.msgOrderWtfDeliveryRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<MsgOrderInfoType> msgOrderInfoTypeList = new ArrayList<>();
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 1","baga",180,1, R.drawable.dapei_album));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 2","bag8olgikflyryjsryja",180,1, R.drawable.dapei_vest_24));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 3","baga",180,1, R.drawable.dapei_album));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 56","baga",180,1, R.drawable.dapei_vest_24));

        // 实例化适配器对象
        MsgOrderWtfDeliveryAdapter msgOrderWtfDeliveryAdapter = new MsgOrderWtfDeliveryAdapter(getActivity(), msgOrderInfoTypeList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgOrderWtfDeliveryAdapter);


        return view;
    }
}