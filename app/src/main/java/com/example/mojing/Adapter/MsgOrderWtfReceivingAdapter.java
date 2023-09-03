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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.MsgOrderWtfReceivingDetailActivity;
import com.example.mojing.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MsgOrderWtfReceivingAdapter extends RecyclerView.Adapter<MsgOrderWtfReceivingAdapter.ViewHolder> {

    private final List<MsgOrderInfoType> mValues;
    private Context context;
    Bitmap bmp;
    static ImageView avatarImage;
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

    public MsgOrderWtfReceivingAdapter(Context context, List<MsgOrderInfoType> items) {
        this.context = context;
        mValues = items;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_msg_order_wtf_receiving, parent, false);
        return new MsgOrderWtfReceivingAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MsgOrderInfoType userInfo = mValues.get(position);

        // 设置用户头像、名字等数据
        holder.nameText.setText(userInfo.getName());
        holder.orderNumberText.setText(userInfo.getOrderNumber());
        holder.moneyText1.setText(userInfo.getMoney());
        holder.moneyText2.setText(userInfo.getAllMoney());
        holder.orderTimeText.setText(userInfo.getOrderTime());

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

        holder.touchLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建 Intent 对象，指定要启动的目标 Activity
                Intent intent = new Intent(context, MsgOrderWtfReceivingDetailActivity.class);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                intent.putExtra("byteArray", bs.toByteArray());

                intent.putExtra("nameText", userInfo.getName());
                intent.putExtra("moneyText", userInfo.getMoney());
                intent.putExtra("orderNumberText", userInfo.getOrderNumber());
                intent.putExtra("orderTimeText", userInfo.getOrderTime());
                intent.putExtra("allMoneyText", userInfo.getAllMoney());

                // 启动目标 Activity
                context.startActivity(intent);
            }
        });
        holder.confirmReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 创建 Intent 对象，指定要启动的目标 Activity
//                Intent intent = new Intent(context, MsgChatDetailsActivity.class);
//
////                intent.putExtra("id", userInfo.getId());
//
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 50, bs);
//                intent.putExtra("byteArray", bs.toByteArray());
//
//                intent.putExtra("name_text", userInfo.getName());
//
//                // 启动目标 Activity
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, orderNumberText, moneyText1,moneyText2,orderTimeText;
        Button confirmReceiptButton;
        LinearLayout touchLinear;

        public ViewHolder(View view) {
            super(view);
            avatarImage=view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            orderNumberText = view.findViewById(R.id.orderNumberText);
            moneyText1 = view.findViewById(R.id.moneyText);
            moneyText2 = view.findViewById(R.id.allMoneyText);
            orderTimeText = view.findViewById(R.id.orderTimeText);
            confirmReceiptButton = view.findViewById(R.id.confirmReceiptButton);
            touchLinear=view.findViewById(R.id.touchLinearLayout);
        }
    }
}