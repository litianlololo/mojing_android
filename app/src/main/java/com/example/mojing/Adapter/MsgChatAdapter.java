package com.example.mojing.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mojing.Fragments.placeholder.MsgChatInfoType;
import com.example.mojing.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MsgChatAdapter extends RecyclerView.Adapter<MsgChatAdapter.ViewHolder> {

    private final List<MsgChatInfoType> mValues;
    private Context context;

    public MsgChatAdapter(Context context, List<MsgChatInfoType> items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_msg_chat, parent, false);
        return new MsgChatAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MsgChatInfoType userInfo = mValues.get(position);

        // 设置用户头像、名字等数据
        holder.nameText.setText(userInfo.getName());
        holder.avatarImage.setImageResource(userInfo.getAvatarResId());
        String msg =userInfo.getMsg();
        if(msg.length()>19) msg=msg.substring(0, 19)+"...";
        holder.msgText.setText(msg);


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

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText, timeText, msgText;

        public ViewHolder(View view) {
            super(view);
            avatarImage=view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            timeText = view.findViewById(R.id.timeText);
            msgText = view.findViewById(R.id.msgText);
        }
    }
}