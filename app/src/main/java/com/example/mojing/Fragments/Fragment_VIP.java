package com.example.mojing.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojing.Adapter.VipListAdapter;
import com.example.mojing.Dapei_Album_Activity;
import com.example.mojing.R;
import com.example.mojing.Fragments.placeholder.VIPDesignerInfoType;
import com.example.mojing.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//todo:aks:传ID进vip的chat和order

public class Fragment_VIP extends Fragment {
    List<VIPDesignerInfoType> vip_designer_infoList;
    VipListAdapter vipListAdapter;

    public String uu="http://47.102.43.156:8007/api";

    private SharedPreferencesManager sharedPreferencesManager;

    public Fragment_VIP() {
        // Required empty public constructor
    }

    public static Fragment_VIP newInstance(String param1, String param2) {
        Fragment_VIP fragment = new Fragment_VIP();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sharedPreferencesManager = new SharedPreferencesManager(this);
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

    void loadAll(AddCallback addCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder requestBuilder = new Request.Builder()
                        .url(uu+"/query/getAvailableDesigners")
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
                                        vipListAdapter.notifyDataSetChanged();
                                    }
                                });
                                addCallback.onAddDesigner();
                                break;
                            default:
                                showRequestFailedDialog("添加失败");
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
                    showRequestFailedDialog("网络错误(vip-)");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    void AddDesigner(JSONArray dataJson) throws JSONException {
        // 遍历对象列表
        for (int i = 0; i < dataJson.length(); i++) {
            // 获取当前对象
            JSONObject obj = dataJson.getJSONObject(i);

            // 访问对象的属性并进行处理
            String id = obj.getString("_id");
            String nickname = obj.getString("nickname");
            String avatar = obj.getString("avatar");
            String intro = obj.getString("intro");
            int availableService = obj.getInt("available_service");

            vip_designer_infoList.add(new VIPDesignerInfoType(id, nickname,avatar,intro, availableService));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment__v_i_p, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.vipRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        vip_designer_infoList = new ArrayList<>();

        // 实例化适配器对象
        vipListAdapter = new VipListAdapter(getActivity(), vip_designer_infoList);

        // 将适配器设置给 RecyclerView
        recyclerView.setAdapter(vipListAdapter);

        vip_designer_infoList.add(new VIPDesignerInfoType("id", "nickname",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F15%2F20170715164510_xm2yL.thumb.400_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623550913&t=d8cae253f81749dbb3da16509d0b1abd",
                "intro", 34));

// 调用 loadAll 方法获取设计师信息
        loadAll(new AddCallback() {
            @Override
            public void onAddDesigner() {
                // 数据加载完成后，通知适配器进行刷新
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vipListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return rootView;
    }

    public interface AddCallback {
        void onAddDesigner();
    }

}