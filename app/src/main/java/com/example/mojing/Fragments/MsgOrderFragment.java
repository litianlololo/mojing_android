package com.example.mojing.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.mojing.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MsgOrderFragment extends Fragment {

    private View contextView;// 总视图
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"4\n已完成","8\n待发货","\n待收货"};

    public MsgOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_msg_order, container, false);
        tabLayout = contextView.findViewById(R.id.tab_layout);
        viewPager = contextView.findViewById(R.id.view_pager);


        //todo:这个为什么按那个放大镜图标才会显示搜索框，按放大镜后面的部分就没有反应？这个searchview的宽是parent的，我想按到这个控件的任何一处都可以输入文字
//        LinearLayout searchContainer = contextView.findViewById(R.id.searchContainer);
        SearchView searchView = contextView.findViewById(R.id.searchView);

//        searchContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.requestFocus();
//                // 显示键盘（可选）
//                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });

        return contextView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fragment中嵌套fragment, Manager需要用(getChildFragmentManager())
        MsgOrderFragment.MPagerAdapter mPagerAdapter = new MsgOrderFragment.MPagerAdapter(getChildFragmentManager());
        initFragment();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mPagerAdapter);
    }

    private void initFragment() {
        fragmentList.add(new MsgOrderFinishedFragment());
        fragmentList.add(new MsgOrderWtfDeliveryFragment());
        fragmentList.add(new MsgOrderWtfReceivingFragment());
    }

    class MPagerAdapter extends FragmentPagerAdapter {


        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        //返回tablayout的标题文字;

        private final int TAB_FONT_SIZE = 20; // 设置 Tab 字体大小
        @Override
        public CharSequence getPageTitle(int position) {
            SpannableString spannableString = new SpannableString(temp[position]);
            spannableString.setSpan(new AbsoluteSizeSpan(TAB_FONT_SIZE, true), 0,
                    spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }
}