package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mojing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PaymentResultActivity extends AppCompatActivity {
    private Context context;
    public String uu="http://47.102.43.156:8007/api";
    private SharedPreferencesManager sharedPreferencesManager;
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        context=this;

        String designer =getIntent().getStringExtra("designer");
        String payMoney =getIntent().getStringExtra("payMoney");
        int payMethod =getIntent().getIntExtra("payMethod",0);
        String id =getIntent().getStringExtra("id");
        if (getIntent().hasExtra("byteArray")) {
            bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0, getIntent().getByteArrayExtra("byteArray").length);
        }

        Button buttonGoToChat = findViewById(R.id.buttonGoToChat);
        TextView textViewMoney = findViewById(R.id.textViewMoney);
        TextView textViewDesignerName = findViewById(R.id.textViewDesignerName);
        ImageView imageIcon = findViewById(R.id.imageIcon);

        textViewMoney.setText(payMoney);
        textViewDesignerName.setText(designer);
        if(0==payMethod)
            imageIcon.setImageResource(R.drawable.icon_zhifubao);
        else
            imageIcon.setImageResource(R.drawable.icon_weixin);

        buttonGoToChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(context, VipOrderChatActivity.class);
                chatIntent.putExtra("id", id);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                chatIntent.putExtra("byteArray", bs.toByteArray());

                chatIntent.putExtra("name_text", designer);
                context.startActivity(chatIntent);
                finish();
            }
        });
    }
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentResultActivity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
}