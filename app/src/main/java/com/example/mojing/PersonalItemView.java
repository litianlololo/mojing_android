package com.example.mojing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义个人中心选项控件
 */
public class PersonalItemView extends RelativeLayout {
    private String targetActivityName;
    public PersonalItemView(final Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_personal_menu, this);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PersonaltemView);
        ImageView icon = findViewById(R.id.icon);
        ImageView more = findViewById(R.id.more);
        ImageView line = findViewById(R.id.line);
        TextView name = findViewById(R.id.name);
        targetActivityName = "com.example.mojing."+typedArray.getString(R.styleable.PersonaltemView_activity);

        icon.setImageDrawable(typedArray.getDrawable(R.styleable.PersonaltemView_icon));
        name.setText(typedArray.getText(R.styleable.PersonaltemView_name));

        if (typedArray.getBoolean(R.styleable.PersonaltemView_show_more, false)){
            more.setVisibility(VISIBLE);
        }
        if (typedArray.getBoolean(R.styleable.PersonaltemView_show_line, false)){
            line.setVisibility(VISIBLE);
        }
        typedArray.recycle();

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理name的点击事件
                // 启动目标Activity
                if (targetActivityName != null) {
                    try {
                        Class<?> targetActivityClass = Class.forName(targetActivityName);
                        Intent intent = new Intent(context, targetActivityClass);
                        context.startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
            icon.setOnClickListener(clickListener);
            name.setOnClickListener(clickListener);
            more.setOnClickListener(clickListener);
    }

    public void setTargetActivityName(String targetActivityName) {
        this.targetActivityName = targetActivityName;
    }
}
