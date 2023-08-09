package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.MsgChatAdapter;
import com.example.mojing.Fragments.placeholder.MsgChatInfoType;
import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgChatFragment extends Fragment {

    public MsgChatFragment() {
    }

    @SuppressWarnings("unused")
    public static MsgChatFragment newInstance(int columnCount) {
        MsgChatFragment fragment = new MsgChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_chat_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.msgChatRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<MsgChatInfoType> msgChatInfoTypeList = new ArrayList<>();
        msgChatInfoTypeList.add(new MsgChatInfoType(R.drawable.dapei_album,"User 1","我是我我不是",new Date(1577865600000L)));
        msgChatInfoTypeList.add(new MsgChatInfoType(R.drawable.dapei_vest_24,"User 2","为了维护您的权益，我们为了维护您的权益，维护了您的权益。",new Date(1577865600000L)));
        msgChatInfoTypeList.add(new MsgChatInfoType(R.drawable.dapei_album,"User 3","哈哈哈哈哈",new Date(1577865600000L)));
        msgChatInfoTypeList.add(new MsgChatInfoType(R.drawable.dapei_vest_24,"User 56","[图片]",new Date(1577865600000L)));

        // 实例化适配器对象
        MsgChatAdapter msgChatAdapter = new MsgChatAdapter(getActivity(), msgChatInfoTypeList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgChatAdapter);


        return view;
    }
}