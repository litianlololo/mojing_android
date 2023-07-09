package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_dapei.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_dapei newInstance(String param1, String param2) {
        Fragment_dapei fragment = new Fragment_dapei();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
}