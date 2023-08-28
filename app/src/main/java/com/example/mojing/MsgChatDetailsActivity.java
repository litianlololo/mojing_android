package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mojing.Adapter.MsgChatDetailsAdapter;
import com.example.mojing.Fragments.Fragment_VIP;
import com.example.mojing.Fragments.placeholder.MsgChatDetailsInfoType;
import com.example.mojing.Fragments.placeholder.VIPDesignerInfoType;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MsgChatDetailsActivity extends AppCompatActivity {

    public String uu="http://47.102.43.156:8007/api";
    String customerID="64b0b9f3f90395f59d5a9432";
    public Activity activity = this;
    private List<MsgChatDetailsInfoType> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private ImageButton back;
    private TextView textViewTitleText;
    private Bitmap bitmapAvatar;
    private LinearLayoutManager layoutManager;
    private MsgChatDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_chat_details);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);
        textViewTitleText=findViewById((R.id.textViewTitleText));
        layoutManager = new LinearLayoutManager(this);
        adapter = new MsgChatDetailsAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        bitmapAvatar = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0, getIntent().getByteArrayExtra("byteArray").length);

        String nameText =getIntent().getStringExtra("name_text");
        String chatIdText =getIntent().getStringExtra("chatid_text");
        String designerIdText =getIntent().getStringExtra("designerid_text");
        textViewTitleText.setText(nameText);

//        msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, "test :What's your name?",MsgChatDetailsInfoType.TYPE_RECEIVED));
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);

// 调用 loadAll 方法获取设计师信息
        loadAll(new AddCallback() {
            @Override
            public void onAddDetails() {
                // 数据加载完成后，通知适配器进行刷新
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {finish();}
        });

/*       我们还需要为button建立一个监听器，我们需要将编辑框的内容发送到 RecyclerView 上：
            ①获取内容，将需要发送的消息添加到 List 当中去。
            ②调用适配器的notifyItemInserted方法，通知有新的数据加入了，赶紧将这个数据加到 RecyclerView 上面去。
            ③调用RecyclerView的scrollToPosition方法，以保证一定可以看的到最后发出的一条消息。*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!content.equals("")) {
                    msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, content,MsgChatDetailsInfoType.TYPE_SEND));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");//清空输入框中的内容

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                            JSONObject json = new JSONObject();
                            try {
                                json.put("content",content);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            //创建一个OkHttpClient对象
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(uu + "/chat/sendMessage").newBuilder();
                            urlBuilder.addQueryParameter("chat_id", chatIdText);
                            urlBuilder.addQueryParameter("sender_id", customerID);
                            urlBuilder.addQueryParameter("receiver_id", designerIdText);
                            String url = urlBuilder.build().toString();
                            // 创建请求
                            Request.Builder requestBuilder = new Request.Builder()
                                    .url(url)
                                    .post(requestBody);

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
                                            // todo: aks200好像不用干什么
                                            break;
                                        default:
                                            showRequestFailedDialog("发送失败");
                                            break;
                                    }
                                    System.out.println("Response: " + responseData);
                                    // 记得关闭响应体
                                    responseBody.close();
                                } else {
                                    // 请求失败，处理错误
                                    System.out.println("Request failed");
                                    showRequestFailedDialog("网络错误msgchatdetial");
                                }
                            } catch (IOException e) {
                                showRequestFailedDialog("网络错误msgchatdetial");
                                e.printStackTrace();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                }
////                自定义一问一答
//                if(msgList.size() == 2){
//                    msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, "What's your name?",MsgChatDetailsInfoType.TYPE_RECEIVED));
//                    adapter.notifyItemInserted(msgList.size()-1);
//                    msgRecyclerView.scrollToPosition(msgList.size()-1);
//                }
//                if(msgList.size() == 4){
//                    msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, "Nice to meet you,Bye!",MsgChatDetailsInfoType.TYPE_RECEIVED));
//                    adapter.notifyItemInserted(msgList.size()-1);
//                    msgRecyclerView.scrollToPosition(msgList.size()-1);
//                }
            }
        });
    }

    private List<MsgChatDetailsInfoType> getData(){
        List<MsgChatDetailsInfoType> list = new ArrayList<>();
//        list.add(new MsgChatDetailsInfoType(bitmapAvatar, "Hello",MsgChatDetailsInfoType.TYPE_RECEIVED));
        return list;
    }

    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MsgChatDetailsActivity.this);
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
                HttpUrl.Builder urlBuilder = HttpUrl.parse(uu + "/chat/getChatDetail").newBuilder();
                urlBuilder.addQueryParameter("customer_id", customerID);
                urlBuilder.addQueryParameter("designer_id", "64d3933ff392e07c93839d5a");
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
                                AddDetails(dataJson);
                                // 更新UI，通知适配器进行刷新
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                addCallback.onAddDetails();
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
                    showRequestFailedDialog("网络错误(msgchatdetail)");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    void AddDetails(JSONArray dataJson) throws JSONException {

        System.out.println("bagaadddetails--------------------a");
        // 遍历对象列表
        for (int i = 0; i < dataJson.length(); i++) {
            System.out.println("bagaadddetails--------------------b");
            // 获取当前对象
            JSONObject obj = dataJson.getJSONObject(i);

            // 提取参数
            String senderId = obj.getString("sender_id");
            String receiverId = obj.getString("receiver_id");
            String sentTime = obj.getString("sent_time");
            String content = obj.getString("content");

            // 输出参数
            System.out.println("bagaSender ID: " + senderId);
            System.out.println("bagaReceiver ID: " + receiverId);
            System.out.println("bagaSent Time: " + sentTime);
            System.out.println("bagaContent: " + content);

            System.out.println("bagaadddetails--------------------");

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = null;
//            try {
//                date = sdf.parse(sentTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            if(senderId.equals(customerID)){
                msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, content, MsgChatDetailsInfoType.TYPE_SEND));
                System.out.println("bagaadddetails--------------------send");
            }else{
                msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, content, MsgChatDetailsInfoType.TYPE_RECEIVED));
                System.out.println("bagaadddetails--------------------received");
            }
            System.out.println("bagaadddetails--------------------c");
//
// todo: aks这里要是滚动就会闪退
//            adapter.notifyItemInserted(msgList.size()-1);
//            msgRecyclerView.scrollToPosition(msgList.size()-1);
            System.out.println("bagaadddetails--------------------d");
        }
    }
    public interface AddCallback {
        void onAddDetails();
    }
}


