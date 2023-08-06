package com.example.mojing.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.MsgOrderFragmentAdapter;
import com.example.mojing.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A fragment representing a list of Items.
 */
public class MsgOrderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MsgOrderFragmentAdapter fragmentAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MsgOrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MsgOrderFragment newInstance(int columnCount) {
        MsgOrderFragment fragment = new MsgOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_order, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        fragmentAdapter = new MsgOrderFragmentAdapter(getChildFragmentManager(), getLifecycle());

//        fragmentAdapter.addFragment(new MsgOrderItemFragment(), "订单1");
//        fragmentAdapter.addFragment(new MsgOrderItemFragment(), "订单\n2");
//        fragmentAdapter.addFragment(new MsgOrderItemFragment(), "订单3\n");

        // TODO: 问题出现在MsgOrderItemFragment这个加载不出来。

        fragmentAdapter.addFragment(new MsgChatItemFragment(),"baga");

        viewPager.setAdapter(fragmentAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // 获取每个标签的标题，并设置给对应的 tab
                    String title = fragmentAdapter.getFragmentTitle(position);
                    tab.setText(title);
                }
        );
        tabLayoutMediator.attach();

        return view;
    }

}