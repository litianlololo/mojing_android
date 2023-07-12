package com.example.mojing.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.mojing.Dapei_Tag_Activity;
import com.example.mojing.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_dapei#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_dapei extends Fragment {
    public Fragment_dapei() {
        // Required empty public constructor
    }
    public static Fragment_dapei newInstance(String param1, String param2) {
        Fragment_dapei fragment = new Fragment_dapei();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dapei, container, false);
    }
    //在fragment不能直接进行点击事件，需要放到oncreatActivity中,重载该函数即可
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton imgButton = (ImageButton) getActivity().findViewById(R.id.img_button);
        EditText score_edit = (EditText) getActivity().findViewById(R.id.score_edit);
        score_edit.setText("init");
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                String TAG="Main";
                score_edit.setText("dianji");
                Intent intent = new Intent(getActivity(), Dapei_Tag_Activity.class);
                startActivity(intent);
            }
        });
    }
}
