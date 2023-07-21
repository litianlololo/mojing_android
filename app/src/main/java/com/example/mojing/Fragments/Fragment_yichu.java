package com.example.mojing.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mojing.Yichu_Tops_Activity;
import com.example.mojing.Yichu_Bottoms_Activity;
import com.example.mojing.Yichu_Add_Activity;
import com.example.mojing.R;


import android.util.Log;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_yichu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_yichu extends Fragment {
    public Fragment_yichu() {
        // Required empty public constructor
    }
    public static Fragment_yichu newInstance(String param1, String param2) {
        Fragment_yichu fragment = new Fragment_yichu();
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
        return inflater.inflate(R.layout.fragment_yichu, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button addTopButton = (Button) getActivity().findViewById(R.id.addTop);
        addTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                Intent intent = new Intent(getActivity(), Yichu_Add_Activity.class);
                startActivity(intent);
            }
        });
        Button addBottomButton = (Button) getActivity().findViewById(R.id.addBottom);
        addBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                Intent intent = new Intent(getActivity(), Yichu_Add_Activity.class);
                startActivity(intent);
            }
        });
        Button allTopButton = (Button) getActivity().findViewById(R.id.allTop);
        allTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                Intent intent = new Intent(getActivity(), Yichu_Tops_Activity.class);
                startActivity(intent);
            }
        });
        Button allBottomButton = (Button) getActivity().findViewById(R.id.allBottom);
        allBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                Intent intent = new Intent(getActivity(), Yichu_Bottoms_Activity.class);
                startActivity(intent);
            }
        });
    }
}