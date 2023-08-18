package com.example.mojing.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.MsgChatAdapter;
import com.example.mojing.Fragments.placeholder.MsgChatInfoType;
import com.example.mojing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class MsgChatFragment extends Fragment {
    List<MsgChatInfoType> msgChatInfoTypeList;
    MsgChatAdapter msgChatAdapter;
    public String uu="http://47.102.43.156:8007/api";
    String customerID="64b0b9f3f90395f59d5a9432";

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

        msgChatInfoTypeList = new ArrayList<>();

        msgChatInfoTypeList.add(new MsgChatInfoType("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F15%2F20170715164510_xm2yL.thumb.400_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623550913&t=d8cae253f81749dbb3da16509d0b1abd",
                "nickname","content",new Date(123414514513L),"chatId","designerId"));

        // 实例化适配器对象
        msgChatAdapter = new MsgChatAdapter(getActivity(), msgChatInfoTypeList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(msgChatAdapter);

        // 调用 loadAll 方法获取信息
        loadAll(new AddCallback() {
            @Override
            public void onAddDesigner() {
                // 数据加载完成后，通知适配器进行刷新
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msgChatAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    void loadAll(AddCallback addCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse(uu + "/chat/getChatList").newBuilder();
                urlBuilder.addQueryParameter("customer_id", customerID);
                String url = urlBuilder.build().toString();
                Request.Builder requestBuilder = new Request.Builder()
                        .url(url)
                        .get();

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
                                    public void run() {
                                        msgChatAdapter.notifyDataSetChanged();
                                    }
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
                    showRequestFailedDialog("网络错误: msg-chat");
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
            // 获取当前对象
            JSONObject obj = dataJson.getJSONObject(i);

            String chatId = obj.getString("chat_id");
            String designerId = obj.getString("designer_id");
            JSONObject latestMessage = obj.getJSONObject("lastest_message");
            String nickname = obj.getString("nickname");
            String avatar = obj.getString("avatar");

            System.out.println("Chat ID: " + chatId);
            System.out.println("Designer ID: " + designerId);
            System.out.println("Nickname: " + nickname);
            System.out.println("Avatar: " + avatar);

            String senderId = latestMessage.getString("sender_id");
            String receiverId = latestMessage.getString("receiver_id");
            String sentTime = latestMessage.getString("sent_time");
            String content = latestMessage.getString("content");

            System.out.println("Sender ID: " + senderId);
            System.out.println("Receiver ID: " + receiverId);
            System.out.println("Sent Time: " + sentTime);
            System.out.println("Content: " + content);

            System.out.println("----------------------");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(sentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            msgChatInfoTypeList.add(new MsgChatInfoType(avatar, nickname,content,date,chatId,designerId));
        }
    }
}