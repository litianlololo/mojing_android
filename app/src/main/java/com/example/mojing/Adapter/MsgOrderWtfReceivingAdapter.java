package com.example.mojing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mojing.Fragments.placeholder.MsgOrderInfoType;
import com.example.mojing.R;

import java.util.List;

public class MsgOrderWtfReceivingAdapter extends RecyclerView.Adapter<MsgOrderWtfReceivingAdapter.ViewHolder> {

    private final List<MsgOrderInfoType> mValues;
    private Context context;

    public MsgOrderWtfReceivingAdapter(Context context, List<MsgOrderInfoType> items) {
        this.context = context;
        mValues = items;
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
        holder.moneyText2.setText(userInfo.getMoney());
        holder.orderTimeText.setText(userInfo.getOrderTime());
        holder.avatarImage.setImageResource(userInfo.getAvatarResId());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText, orderNumberText, moneyText1,moneyText2,orderTimeText;
        Button confirmReceiptButton;

        public ViewHolder(View view) {
            super(view);
            avatarImage=view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            orderNumberText = view.findViewById(R.id.orderNumberText);
            moneyText1 = view.findViewById(R.id.moneyText1);
            moneyText2 = view.findViewById(R.id.moneyText2);
            orderTimeText = view.findViewById(R.id.orderTimeText);
            confirmReceiptButton = view.findViewById(R.id.confirmReceiptButton);
        }
    }
}