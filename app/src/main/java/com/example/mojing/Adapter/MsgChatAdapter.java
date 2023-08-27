package com.example.mojing.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mojing.Fragments.placeholder.MsgChatInfoType;
import com.example.mojing.MsgChatDetailsActivity;
import com.example.mojing.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MsgChatAdapter extends RecyclerView.Adapter<MsgChatAdapter.ViewHolder> {

    static ImageView avatarImage;
    Bitmap bmp;
    private List<MsgChatInfoType> mValues;
    private Context context;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    System.out.println("111");
                    bmp=(Bitmap)msg.obj;
                    avatarImage.setImageBitmap(bmp);
                    break;
            }
        };
    };

    public MsgChatAdapter(Context context, List<MsgChatInfoType> items) {
        this.context = context;
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_msg_chat, parent, false);
        return new MsgChatAdapter.ViewHolder(view);

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MsgChatInfoType userInfo = mValues.get(position);

        // 设置用户头像、名字等数据
        holder.nameText.setText(userInfo.getName());
        String msg =userInfo.getMsg();
        if(msg.length()>15) msg=msg.substring(0, 15)+"...";
        holder.msgText.setText(msg);

        //新建线程加载图片信息，发送到消息队列中
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(userInfo.getAvatarUrl());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();

        Date currentDate = new Date();
        Date messageDate = userInfo.getTime();

// 判断消息的日期是否是今天
        boolean isToday = android.text.format.DateUtils.isToday(messageDate.getTime());

// 格式化日期和时间
        String formattedTime;
        if (isToday) {
            // 如果是今天，格式化为小时和分钟
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            formattedTime = timeFormat.format(messageDate);
        } else {
            // 如果不是今天，计算与当前日期的差距并显示 "几天前"
            long diffInMillis = currentDate.getTime() - messageDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            formattedTime = diffInDays + "天前";
        }

// 将时间显示到 TextView 中
        holder.timeText.setText(formattedTime);
        holder.chatLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建 Intent 对象，指定要启动的目标 Activity
                Intent intent = new Intent(context, MsgChatDetailsActivity.class);

//                intent.putExtra("id", userInfo.getId());

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                intent.putExtra("byteArray", bs.toByteArray());

                intent.putExtra("name_text", userInfo.getName());
                intent.putExtra("chatid_text", userInfo.getChatId());
                intent.putExtra("designerid_text", userInfo.getDesignerId());

                // 启动目标 Activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, timeText, msgText;

        LinearLayout chatLinear;

        public ViewHolder(View view) {
            super(view);
            avatarImage=view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            timeText = view.findViewById(R.id.timeText);
            msgText = view.findViewById(R.id.msgText);
            chatLinear = view.findViewById(R.id.chatLinear);
        }
    }
}