package com.example.mojing.Fragments;

import android.app.AlertDialog;
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

import com.example.mojing.Fragments.placeholder.MsgChatInfoType;
import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;
import com.example.mojing.SharedPreferencesManager;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MsgOrderFragment extends Fragment {
    public String uu="http://47.102.43.156:8007/api";
    private SharedPreferencesManager sharedPreferencesManager;

    private View contextView;// 总视图
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"\n已完成","\n待发货","\n待收货"};
    int[] statusNumber=new int[3]; //"\n已完成","\n待发货","\n待收货"

    public static List<MsgOrderInfoType> msgOrderFinishedList = new ArrayList<>();
    public static List<MsgOrderInfoType> msgOrderWtfDeliveryList = new ArrayList<>();
    public static List<MsgOrderInfoType> msgOrderWtfReceivingList = new ArrayList<>();

    public MsgOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_msg_order, container, false);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        tabLayout = contextView.findViewById(R.id.tab_layout);
        viewPager = contextView.findViewById(R.id.view_pager);

        //todo:AKS这个为什么按那个放大镜图标才会显示搜索框，按放大镜后面的部分就没有反应？这个searchview的宽是parent的，我想按到这个控件的任何一处都可以输入文字
//        LinearLayout searchContainer = contextView.findViewById(R.id.searchContainer);
//        SearchView searchView = contextView.findViewById(R.id.searchView);

//        searchContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.requestFocus();
//                // 显示键盘（可选）
//                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
        msgOrderFinishedList.add(new MsgOrderInfoType("User 1","baga",new BigDecimal(180),1, "https://pic1.zhimg.com/v2-95ed6ea0f78292c9cad905ad117c7fcc_r.jpg?source=1940ef5c"));
        msgOrderWtfDeliveryList.add(new MsgOrderInfoType("User 2","bagaaga",new BigDecimal(179.9),3, "https://pic1.zhimg.com/80/v2-da2b0a3b96103d87a682409fc5a261a9_1440w.webp?source=1940ef5c"));
        msgOrderWtfReceivingList.add(new MsgOrderInfoType("User 3","bagabagaga",new BigDecimal(180.00),4, "https://pic2.zhimg.com/80/v2-cef1bd681556b3352f3b8bea15d4e0fd_1440w.webp"));
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

        // 调用 loadAll 方法获取信息
        loadAll(new AddCallback() {
            @Override
            public void onAddDesigner() {
                // 数据加载完成后，通知适配器进行刷新
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {}
                });
            }
        });
    }

    void loadAll(AddCallback addCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse(uu + "/service/customer/service/list").newBuilder();
//                urlBuilder.addQueryParameter("customer_id", customerID);
                String url = urlBuilder.build().toString();
                Request.Builder requestBuilder = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());

                // 发送请求并获取响应
                try {
                    Request request = requestBuilder.build();
                    Response response = okHttpClient.newCall(request).execute();
                    // 检查响应是否成功
                    if (response.isSuccessful()) {
                        // 获取响应体
                        ResponseBody responseBody = response.body();
                        // 处理响应数据
                        String responseData = responseBody.string();
                        JSONObject responseJson = new JSONObject(responseData);
                        // 提取键为"code"的值
                        int code = responseJson.getInt("code");
                        //确定返回状态
                        switch (code) {
                            case 200:
                                JSONArray dataJson = responseJson.getJSONArray("data");
                                AddDesigner(dataJson);
                                // 更新UI，通知适配器进行刷新
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {}
                                });
                                addCallback.onAddDesigner();
                                break;
                            default:
                                showRequestFailedDialog("网络连接失败");
                                break;
                        }
                        //System.out.println("Response: " + responseData);
                        // 记得关闭响应体
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                        showRequestFailedDialog("请求失败");
                    }
                } catch (IOException e) {
                    showRequestFailedDialog("网络错误: msg-order");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void showRequestFailedDialog(String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    public interface AddCallback {
        void onAddDesigner();
    }

    void AddDesigner(JSONArray dataJson) throws JSONException {

        System.out.println("bagaa");

        for (int i = 0; i < dataJson.length(); i++) {
            //todo:bagaaks
//            // 获取当前对象
//            JSONObject obj = dataJson.getJSONObject(i);
//
//            String chatId = obj.getString("chat_id");
//            String designerId = obj.getString("designer_id");
//            JSONObject latestMessage = obj.getJSONObject("lastest_message");
//            String nickname = obj.getString("nickname");
//            String avatar = obj.getString("avatar");
//
//            System.out.println("Chat ID: " + chatId);
//            System.out.println("Designer ID: " + designerId);
//            System.out.println("Nickname: " + nickname);
//            System.out.println("Avatar: " + avatar);
//
//            String senderId = latestMessage.getString("sender_id");
//            String receiverId = latestMessage.getString("receiver_id");
//            String sentTime = latestMessage.getString("sent_time");
//            String content = latestMessage.getString("content");
//
//            System.out.println("Sender ID: " + senderId);
//            System.out.println("Receiver ID: " + receiverId);
//            System.out.println("Sent Time: " + sentTime);
//            System.out.println("Content: " + content);
//
//            System.out.println("----------------------");
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = null;
//            try {
//                date = sdf.parse(sentTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

//            msgChatInfoTypeList.add(new MsgChatInfoType(avatar, nickname,content,date,chatId,designerId));
        }
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

        private final int TAB_FONT_SIZE = 19; // 设置 Tab 字体大小
        @Override
        public CharSequence getPageTitle(int position) {
            SpannableString spannableString = new SpannableString(temp[position]);
            spannableString.setSpan(new AbsoluteSizeSpan(TAB_FONT_SIZE, true), 0,
                    spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }
}