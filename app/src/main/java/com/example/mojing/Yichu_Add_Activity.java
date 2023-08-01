package com.example.mojing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Yichu_Add_Activity extends AppCompatActivity {

    private ImageView imageButton;
    private TextView addBtn;
    public Activity activity=this;
    private Uri croppedImageUri;
    private Danpin danpin;
    private BottomSheetDialog bottomSheetDialog;
    private PersonalItemView fenlei_content,season_content,place_content
            , lingxing_content,bihe_content,xiuchang_content,mianliao_content
            ,fengge_content;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichu_add);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        imageButton = findViewById(R.id.clothBtn);
        addBtn = findViewById(R.id.Btn);
        fenlei_content =findViewById(R.id.fenlei_content);
        season_content =findViewById(R.id.season_content);
        place_content =findViewById(R.id.place_content);
        lingxing_content = findViewById(R.id.lingxing_content);
        bihe_content =findViewById(R.id.bihe_content);
        xiuchang_content=findViewById(R.id.xiuchang_content);
        mianliao_content=findViewById(R.id.mianliao_content);
        fengge_content=findViewById(R.id.fengge_content);

        fenlei_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FenleiBottomSheet();
            }
        });
        danpin = new Danpin();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(imageButton);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // 从图库选择图片后的裁剪操作结果
            Uri selectedImageUri = data.getData();
            cropImage(selectedImageUri); // 调用裁剪图片的方法
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            // 从裁剪页面返回的结果
            croppedImageUri = UCrop.getOutput(data);
            // 检查裁剪后的图片尺寸是否小于
            Bitmap croppedBitmap = getBitmapFromUri(croppedImageUri);
            if (croppedBitmap != null && (croppedBitmap.getWidth() < imageButton.getWidth() || croppedBitmap.getHeight() < imageButton.getHeight())) {
                // 如果尺寸小于200x200像素，则将图片放大到200x200像素
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, imageButton.getWidth(), imageButton.getHeight(), true);
                // 保存裁剪后的图片的 Uri
                croppedImageUri = saveBitmapToCache(scaledBitmap);
            }
            //去除背景
//            try {
//                croppedImageUri=removebackground(croppedImageUri);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            }
            imageButton.setImageURI(croppedImageUri); // 设置裁剪后的图片到对应的 ImageButton
        }
    }
    private Uri removebackground(Uri uri) throws URISyntaxException {
        File pngFile = new File(new URI(uri.toString()));
        final Uri[] result = new Uri[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                // 构建请求体，将图片数据作为请求体传输
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", "image.jpg",
                                RequestBody.create(MediaType.parse("image/png"), pngFile))
                        .build();
                Request request = new Request.Builder()
                        .url("http://47.103.223.106:5004/api/file/rembg")
                        .post(requestBody)
                        .build();
                // 发送请求并获取响应
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    // 检查响应是否成功
                    if (response.isSuccessful()) {
                        // 获取响应体
                        ResponseBody responseBody = response.body();
                        // 处理响应数据
                        String responseData = responseBody.string();
                        JSONObject responseJson = new JSONObject(responseData);
                        int code = responseJson.getInt("code");
                        String msg = responseJson.getString("msg");
                        if(msg.equals("success")){
                            danpin.img_url= responseJson.getJSONObject("data")
                                    .getString("url");

                            result[0] = Uri.parse(danpin.img_url);
                        }
                        System.out.println("Response: " + responseData);
                        // 记得关闭响应体
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return result[0];
    }
    private void openGallery(ImageView imageButton) {
        // 在这里处理打开手机图库并选择图片的逻辑
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
        imageButton.setPressed(true);
    }
    private void cropImage(Uri selectedImageUri) {
        // 使用 UCrop 进行图片裁剪
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        UCrop.of(selectedImageUri, Uri.fromFile(new File(activity.getCacheDir(), "cropped_image.jpg")))
                .withAspectRatio(1, 1) // 设置裁剪框的宽高比为1:1
                .withMaxResultSize(imageButton.getWidth(), imageButton.getHeight()) // 设置裁剪后图片的最大尺寸为200x200像素
                .withOptions(options)
                .start(activity); // 传入当前的 Activity 和 Fragment 实例，以便接收裁剪后的结果
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 将 Bitmap 保存到缓存并获取其 Uri
    private Uri saveBitmapToCache(Bitmap bitmap) {
        File cacheDir = activity.getCacheDir();
        File file = new File(cacheDir, "cropped_image.jpg");
        try {
            OutputStream outputStream = Files.newOutputStream(file.toPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            // 获取文件的 Uri
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 弹出请求失败的对话框
    private void showRequestFailedDialog(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yichu_Add_Activity.this);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void FenleiBottomSheet(){
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_fenlei, null, false);
        bottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}
