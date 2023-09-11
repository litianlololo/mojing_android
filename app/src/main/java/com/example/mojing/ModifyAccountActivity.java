package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ModifyAccountActivity extends AppCompatActivity {
    public String uu="http://47.102.43.156:8007/api";
    public String uuimg="http://47.102.43.156:8007";
    private String username;
    private String signature;
    private String gender,profile;
    private TextView usernameText,signatureText,genderText,saveBtn;
    private Activity activity=this;
    private PersonalItemView username_content,signature_content,gender_content;
    private SharedPreferencesManager sharedPreferencesManager;
    private ImageView headIcon;
    private Uri headIconUri;
    private Bitmap tmpb;
    private  BottomSheetDialog genderbottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        username = sharedPreferencesManager.getUsername();
        signature = sharedPreferencesManager.getUserSignature();
        gender = sharedPreferencesManager.getUserGender();
        profile = sharedPreferencesManager.getKEY_USER_Profile();

        usernameText=findViewById(R.id.usernameText);
        signatureText=findViewById(R.id.signatureText);
        genderText =findViewById(R.id.genderText);

        usernameText.setText(username);
        signatureText.setText(signature);
        genderText.setText(gender);

        username_content=findViewById(R.id.username_content);
        signature_content=findViewById(R.id.signature_content);
        gender_content=findViewById(R.id.gender_content);
        headIcon = findViewById(R.id.headIcon2);
        saveBtn = findViewById(R.id.saveBtn);

        if(!sharedPreferencesManager.getKEY_USER_Profile().equals("/"))
            Glide.with(activity)
                    .load(uuimg + sharedPreferencesManager.getKEY_USER_Profile())
                    .error(R.drawable.error) // Error image (optional)
                    .into(headIcon);
        headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferencesManager.isYouke())
                {
                    showRequestFailedDialog("游客无此权限，请先登录");
                    return;
                }
                openGallery(headIcon);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferencesManager.isYouke())
                {
                    showRequestFailedDialog("游客无此权限，请先登录");
                    return;
                }
                saveNow();
            }
        });
        username_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("请输入新的昵称", new OnInputCompleteListener() {
                    public void onInputComplete(String input) {
                        username = input;
                        //sharedPreferencesManager.setUsername(username);
                        usernameText.setText(username);
                    }
                });
            }
        });
        signature_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("请输入新的个性签名", new OnInputCompleteListener() {
                    public void onInputComplete(String input) {
                        signature = input;
                        //sharedPreferencesManager.setUserSignature(signature);
                        signatureText.setText(signature);
                    }
                });
            }
        });
        gender_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderBottomSheet();
            };
        });
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
    }
    private void openGallery(ImageView imageButton) {
        // 在这里处理打开手机图库并选择图片的逻辑
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
        imageButton.setPressed(true);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // 从图库选择图片后的裁剪操作结果
            Uri selectedImageUri = data.getData();
            cropImage(selectedImageUri); // 调用裁剪图片的方法
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            headIconUri = UCrop.getOutput(data);

            if (headIconUri != null) {
                // 对裁剪后的图片进行处理，将其变成圆形并处理透明的四角
                Bitmap croppedBitmap = getRoundedCroppedBitmap(headIconUri);
                headIconUri=saveBitmapToCache(croppedBitmap);
                try {
                    uploadFile(headIconUri,new uploadCallback() {
                        @Override
                        public void onupload(Uri result){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    headIcon.setImageBitmap(croppedBitmap);
                                }
                            });
                        }
                    });
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public interface uploadCallback {
        void onupload(Uri result);
    }
    private Uri uploadFile(Uri uri, uploadCallback callback)throws URISyntaxException {
        File pngFile = new File(new URI(uri.toString()));
        final Uri[] result = new Uri[1];
        System.out.println("pngFile   " + pngFile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                // 构建请求体，将图片数据作为请求体传输
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "image.png",
                                RequestBody.create(MediaType.parse("image/png"), pngFile))
                        .build();
                Request request = new Request.Builder()
                        .url(uu + "/file/upload-file")
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
                        JSONObject data = responseJson.getJSONObject("data");
                        System.out.println("headIcon responseJson" + responseJson);
                        int code = responseJson.getInt("code");
                        String msg = responseJson.getString("msg");
                        switch (code) {
                            case 200:
                                String url = data.getString("url");
                                result[0]=Uri.parse(url);
                                profile = url;
                                //sharedPreferencesManager.setKEY_USER_Profile(url);
                                break;
                            case 1001:
                                showRequestFailedDialog("文件为空");
                                break;
                            default:
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                showRequestFailedDialog("网络请求失败");
                        }
                        callback.onupload(result[0]);
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                        showRequestFailedDialog("网络请求失败");
                    }
                } catch (IOException e) {
                    showRequestFailedDialog("网络请求失败");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return result[0];
    }
    private Uri saveBitmapToCache(Bitmap bitmap) {
        File cacheDir = activity.getCacheDir();
        File file = new File(cacheDir, "cropped_image.png");
        try {
            OutputStream outputStream = Files.newOutputStream(file.toPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            // 获取文件的 Uri
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将矩形的裁剪结果转换为圆形的图片
    private Bitmap getRoundedCroppedBitmap(Uri croppedUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedUri);

            // 将图片转换为圆形，处理透明的四角
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void cropImage(Uri selectedImageUri) {
        // 使用 UCrop 进行图片裁剪
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        // 设置裁剪框为圆形
        options.setCircleDimmedLayer(true);

        UCrop.of(selectedImageUri, Uri.fromFile(new File(activity.getCacheDir(), "image.jpg")))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .start(activity);
    }
    // 自定义接口，用于回调输入完成事件
    private interface OnInputCompleteListener {
        void onInputComplete(String input);
    }

    private void showInputDialog(String prompt, final OnInputCompleteListener listener) {
        final EditText input = new EditText(this);
        input.setHint(""); // 设置输入框的提示文字

        new AlertDialog.Builder(this)
                .setTitle(prompt) // 设置弹窗标题
                .setView(input)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        listener.onInputComplete(userInput);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void genderBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        genderbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("女");
        dataList.add("男");
        dataList.add("你猜");
        wheelPicker.setData(dataList);
        TextView xiuchangBtn = view.findViewById(R.id.xiuchangBtn);
        TextView shenchangText =findViewById(R.id.shenchangText);
        xiuchangBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                gender=select;
                //sharedPreferencesManager.setUserGender(select);
                genderText.setText(select);
                genderbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        genderbottomSheetDialog.setContentView(view);
        genderbottomSheetDialog.show();
    }
    private void showRequestFailedDialog(String str) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("注意")
                        .setMessage(str)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }
    //保存修改 网络请求
    private void saveNow()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("profile", profile);
                    json.put("username", username);
                    json.put("bio", signature);
                    json.put("gender", gender);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                // 创建请求
                Request.Builder requestBuilder = new Request.Builder()
                        .url(uu+"/auth/update")
                        .post(requestBody)
                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                // 发送请求并获取响应
                try {
                    Request request = requestBuilder.build();
                    Response response = okHttpClient.newCall(request).execute();
                    // 检查响应是否成功
                    if (response.isSuccessful()) {
                        // 获取响应体
                        ResponseBody responseBody = response.body();
                        // 处理响应数据
                        String responseData = responseBody.string();
                        JSONObject responseJson = new JSONObject(responseData);
                        System.out.println(responseData);
                        // 提取键为"code"的值
                        int code = responseJson.getInt("code");
                        //确定返回状态
                        switch (code) {
                            case 200:
                                showRequestFailedDialog("修改成功");
                                sharedPreferencesManager.setKEY_USER_Profile(profile);
                                sharedPreferencesManager.setUsername(username);
                                sharedPreferencesManager.setUserSignature(signature);
                                sharedPreferencesManager.setUserGender(gender);
                                break;
                            case 1001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("登录过期，请重新登陆");
                                break;
                            default:
                                showRequestFailedDialog("修改失败");
                                break;
                        }
                        System.out.println("Response: " + responseData);
                        // 记得关闭响应体
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                        showRequestFailedDialog("请求失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}