package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mojing.Adapter.MsgChatDetailsAdapter;
import com.example.mojing.Fragments.placeholder.MsgChatDetailsInfoType;

import java.util.ArrayList;
import java.util.List;

public class MsgChatDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
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
        textViewTitleText.setText(nameText);

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
                }
//                自定义一问一答
                if(msgList.size() == 2){
                    msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, "What's your name?",MsgChatDetailsInfoType.TYPE_RECEIVED));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
                if(msgList.size() == 4){
                    msgList.add(new MsgChatDetailsInfoType(bitmapAvatar, "Nice to meet you,Bye!",MsgChatDetailsInfoType.TYPE_RECEIVED));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
            }
        });
    }

    private List<MsgChatDetailsInfoType> getData(){
        List<MsgChatDetailsInfoType> list = new ArrayList<>();
//        list.add(new MsgChatDetailsInfoType(bitmapAvatar, "Hello",MsgChatDetailsInfoType.TYPE_RECEIVED));
        return list;
    }
}

