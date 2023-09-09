package com.example.mojing.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mojing.Fragments.placeholder.VIPDesignerInfoType;
import com.example.mojing.PaymentResultActivity;
import com.example.mojing.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class VipOrderBottomSheetDialog extends BottomSheetDialogFragment {
    private Context context;
    private String name;
    private String id;
    private Bitmap avatarBmp;

    private int buyNum=1;
    private BigDecimal price=new BigDecimal("179.99");
    private int payMethod=0;//0支付宝，1微信

    public VipOrderBottomSheetDialog(Context context, String name, Bitmap avatarBmp, String id) {
        this.context = context;
        this.name = name;
        this.avatarBmp = avatarBmp;
        this.id = id;
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
        TextView textViewNum = view.findViewById(R.id.textViewNum);
        TextView sumMoney = view.findViewById(R.id.sumMoney);
        Button buttonDe = view.findViewById(R.id.buttonDe);
        Button buttonIn = view.findViewById(R.id.buttonIn);
        RadioGroup radioGroupBuyMethod = view.findViewById(R.id.radioGroupPayMethod);
        Button buttonOK = view.findViewById(R.id.buttonOK);

        // 或者显示头像（使用Bitmap对象）
        avatarImageView.setImageBitmap(avatarBmp);
        // 显示名字
        nameTextView.setText(name);
        sumMoney.setText(price.toString());

        TextView cancelText=view.findViewById(R.id.cancelText);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buttonDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyNum>1){
                    --buyNum;
                    textViewNum.setText(buyNum+"");
                    sumMoney.setText(price.multiply(new BigDecimal(buyNum)).toString());
                }
            }
        });
        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buyNum<1000){
                    ++buyNum;
                    textViewNum.setText(buyNum+"");
                    sumMoney.setText(price.multiply(new BigDecimal(buyNum)).toString());
                }
            }
        });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent chatIntent = new Intent(context, PaymentResultActivity.class);
                chatIntent.putExtra("payMethod", payMethod);
                chatIntent.putExtra("payMoney", price.multiply(new BigDecimal(buyNum)).toString());
                chatIntent.putExtra("designer", name);
                chatIntent.putExtra("id", id);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                avatarBmp.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                chatIntent.putExtra("byteArray", bs.toByteArray());

                context.startActivity(chatIntent);
            }
        });
        radioGroupBuyMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理选中的RadioButton
                switch (checkedId) {
                    case R.id.radioButtonZhifubao:
                        payMethod = 0;
                        break;
                    case R.id.radioButtonWeixin:
                        payMethod = 1;
                        break;
                }
            }
        });

        return view;
    }
}