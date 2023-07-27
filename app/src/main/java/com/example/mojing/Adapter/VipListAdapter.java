package com.example.mojing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.R;
import com.example.mojing.VIP_Designer_InfoType;

import java.util.List;
import com.example.mojing.Fragments.Fragment_VIP;
import com.example.mojing.VipChatActivity;
import com.example.mojing.VipOrderActivity;

public class VipListAdapter extends RecyclerView.Adapter<VipListAdapter.MyViewHolder> {

    private List<VIP_Designer_InfoType> vip_designer_infoList;
    private Context context;


    public VipListAdapter(Context context, List<VIP_Designer_InfoType> data) {
        this.context = context;
        this.vip_designer_infoList = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
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
        final VIP_Designer_InfoType userInfo = vip_designer_infoList.get(position);

        // 设置用户头像、名字等数据
        holder.nameText.setText(userInfo.getName());
        holder.introductionText.setText(userInfo.getIntroduction());
        holder.numberOfOrderText.setText(userInfo.getNumberOfOrderText());
        holder.avatarImage.setImageResource(userInfo.getAvatarResId());

        // 为聊天按钮设置点击事件监听器
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理点击聊天按钮的逻辑，跳转到聊天页面
                // 这里可以根据需要进行具体的跳转操作，例如使用 Intent 跳转到聊天页面
                Intent chatIntent = new Intent(context, VipChatActivity.class);
                chatIntent.putExtra("avatar_res_id", R.id.avatarImage);
                chatIntent.putExtra("number_of_order", R.id.numberOfOrderText);
                context.startActivity(chatIntent);
            }
        });

        // 为下单按钮设置点击事件监听器
        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理点击下单按钮的逻辑，跳转到下单页面
                // 这里可以根据需要进行具体的跳转操作，例如使用 Intent 跳转到下单页面
                Intent orderIntent = new Intent(context, VipOrderActivity.class);
                context.startActivity(orderIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return vip_designer_infoList.size();
    }
}
