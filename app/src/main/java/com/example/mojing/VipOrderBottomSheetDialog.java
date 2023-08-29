package com.example.mojing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;

public class VipOrderBottomSheetDialog extends BottomSheetDialogFragment {
    private String name;
    private Bitmap avatarBmp;

    public void setData(String name, Bitmap avatarBmp) {
        this.name = name;
        this.avatarBmp = avatarBmp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vip_order, container, false);

        ImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);

        // 或者显示头像（使用Bitmap对象）
        avatarImageView.setImageBitmap(avatarBmp);
        // 显示名字
        nameTextView.setText(name);

        TextView  cancelText=view.findViewById(R.id.cancelText);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}