package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.MsgOrderFinishedAdapter;
import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;

import java.util.ArrayList;
import java.util.List;

public class MsgOrderFinishedFragment extends Fragment {

    public MsgOrderFinishedFragment() {
    }

    @SuppressWarnings("unused")
    public static MsgOrderFinishedFragment newInstance(int columnCount) {
        MsgOrderFinishedFragment fragment = new MsgOrderFinishedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_order_finished_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.msgOrderFinishedRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       // 实例化适配器对象
        MsgOrderFinishedAdapter msgOrderFinishedAdapter =
                new MsgOrderFinishedAdapter(getActivity(), MsgOrderFragment.msgOrderFinishedList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgOrderFinishedAdapter);


        return view;
    }
}