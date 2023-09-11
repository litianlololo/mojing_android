package com.example.mojing.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.Adapter.MsgOrderWtfReceivingAdapter;
import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;

import java.util.ArrayList;
import java.util.List;

public class MsgOrderWtfReceivingFragment extends Fragment {

    public MsgOrderWtfReceivingFragment() {
    }

    @SuppressWarnings("unused")
    public static MsgOrderWtfReceivingFragment newInstance(int columnCount) {
        MsgOrderWtfReceivingFragment fragment = new MsgOrderWtfReceivingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_order_wtf_receiving_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.msgOrderWtfReceivingRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 实例化适配器对象
        MsgOrderWtfReceivingAdapter msgOrderWtfReceivingAdapter =
                new MsgOrderWtfReceivingAdapter(getActivity(), MsgOrderFragment.msgOrderWtfReceivingList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgOrderWtfReceivingAdapter);


        return view;
    }
}