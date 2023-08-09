package com.example.mojing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.R;
import com.example.mojing.Fragments.placeholder.VIPDesignerInfoType;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.mojing.VipChatActivity;
import com.example.mojing.VipOrderBottomSheetDialog;

public class VipListAdapter extends RecyclerView.Adapter<VipListAdapter.MyViewHolder> {

    private List<VIPDesignerInfoType> vip_designer_infoList;
    private Context context;
    static ImageView avatarImage;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp=(Bitmap)msg.obj;
                    avatarImage.setImageBitmap(bmp);
                    break;
            }
        };
    };

    public VipListAdapter(Context context, List<VIPDesignerInfoType> data) {
        this.context = context;
        this.vip_designer_infoList = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, introductionText, numberOfOrderText;
        Button chatButton, orderButton;

        public MyViewHolder(View view) {
            super(view);
            avatarImage=view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            introductionText = view.findViewById(R.id.introductionText);
            numberOfOrderText = view.findViewById(R.id.numberOfOrderText);
            chatButton = view.findViewById(R.id.chatButton);
            orderButton = view.findViewById(R.id.orderButton);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final VIPDesignerInfoType userInfo = vip_designer_infoList.get(position);

        // 设置用户头像、名字等数据
        holder.nameText.setText(userInfo.getName());
        holder.introductionText.setText(userInfo.getIntroduction());
        holder.numberOfOrderText.setText(userInfo.getNumberOfOrderText());

        //新建线程加载图片信息，发送到消息队列中
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(userInfo.getAvatar());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();

        // 为聊天按钮设置点击事件监听器
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理点击聊天按钮的逻辑，跳转到聊天页面
                // 这里可以根据需要进行具体的跳转操作，例如使用 Intent 跳转到聊天页面
                Intent chatIntent = new Intent(context, VipChatActivity.class);
                chatIntent.putExtra("id", userInfo.getId());
                chatIntent.putExtra("avatar_url", userInfo.getAvatar());
                chatIntent.putExtra("name_text", userInfo.getName());
                context.startActivity(chatIntent);
            }
        });

        // 为下单按钮设置点击事件监听器
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipOrderBottomSheetDialog dialog = new VipOrderBottomSheetDialog();
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "tag");
            }
        });
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    public int getItemCount() {
        return vip_designer_infoList.size();
    }
}
