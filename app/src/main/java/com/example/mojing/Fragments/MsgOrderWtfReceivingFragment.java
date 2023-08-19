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

        List<MsgOrderInfoType> msgOrderInfoTypeList = new ArrayList<>();
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 1","ba",180,1, "https://pic2.zhimg.com/80/v2-cef1bd681556b3352f3b8bea15d4e0fd_1440w.webp"));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 2","baga",180,1, "https://pic3.zhimg.com/80/v2-911318ac58f4d15608e6b20a94834c3a_1440w.webp"));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 3","bafgshdjklukuytdhrgbfaga",180,1, "https://pic3.zhimg.com/80/v2-285a104b2e557836ca09a386e6ee73d2_1440w.webp"));
        msgOrderInfoTypeList.add(new MsgOrderInfoType("User 56","baga",180,1, "https://pic1.zhimg.com/80/v2-c29322edcd03ead5207049b44abad0d8_1440w.webp"));

        // 实例化适配器对象
        MsgOrderWtfReceivingAdapter msgOrderWtfReceivingAdapter = new MsgOrderWtfReceivingAdapter(getActivity(), msgOrderInfoTypeList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgOrderWtfReceivingAdapter);


        return view;
    }
}