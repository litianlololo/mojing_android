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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aigestudio.wheelpicker.WheelPicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public Activity activity = this;
    private Uri croppedImageUri;
    private Danpin danpin;
    private BottomSheetDialog fenleibottomSheetDialog;
    private BottomSheetDialog seasonbottomSheetDialog;
    private  BottomSheetDialog placebottomSheetDialog;
    private  BottomSheetDialog lingxingbottomSheetDialog,fenggebottomSheetDialog;
    private  BottomSheetDialog bihebottomSheetDialog,xiuchangbottomSheetDialog,mianliaobottomSheetDialog;
    private PersonalItemView fenlei_content, season_content, place_content, lingxing_content, bihe_content, xiuchang_content, mianliao_content, fengge_content;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private String fenleiselect1="";
    private String fenleiselect2="";
    private TextView fenleiBtn;
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
        fenlei_content = findViewById(R.id.fenlei_content);
        season_content = findViewById(R.id.season_content);
        place_content = findViewById(R.id.place_content);
        lingxing_content = findViewById(R.id.lingxing_content);
        bihe_content = findViewById(R.id.bihe_content);
        xiuchang_content = findViewById(R.id.xiuchang_content);
        mianliao_content = findViewById(R.id.mianliao_content);
        fengge_content = findViewById(R.id.fengge_content);
        season_content = findViewById(R.id.season_content);
        place_content = findViewById(R.id.place_content);
        lingxing_content = findViewById(R.id.lingxing_content);
        bihe_content = findViewById(R.id.bihe_content);
        xiuchang_content =findViewById(R.id.xiuchang_content);
        mianliao_content =findViewById(R.id.mianliao_content);
        fengge_content = findViewById(R.id.fengge_content);
        mianliao_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MianliaoBottomSheet();
            }
        });
        fengge_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FenggeBottomSheet();
            }
        });
        xiuchang_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XiuchangBottomSheet();
            }
        });
        bihe_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiheBottomSheet();
            }
        });
        lingxing_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LingxingBottomSheet();
            }
        });
        fenlei_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FenleiBottomSheet();
            }
        });
        season_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {SeasonBottomSheet();}
        });
        place_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceBottomSheet();
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
                        if (msg.equals("success")) {
                            danpin.img_url = responseJson.getJSONObject("data")
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
    private void FenleiBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_fenlei, null, false);
        fenleibottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker firstLevelPicker = view.findViewById(R.id.firstLevelPicker);
        WheelPicker secondLevelPicker = view.findViewById(R.id.secondLevelPicker);
        fenleiBtn = view.findViewById(R.id.fenleiBtn);
        fenleiBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // 获取第一级选中的选项内容
                int firstLevelSelectedIndex = firstLevelPicker.getCurrentItemPosition();
                fenleiselect1 = (String) firstLevelPicker.getData().get(firstLevelSelectedIndex);

                // 获取第二级选中的选项内容
                int secondLevelSelectedIndex = secondLevelPicker.getCurrentItemPosition();
                fenleiselect2 = (String) secondLevelPicker.getData().get(secondLevelSelectedIndex);

                TextView fenleiText = findViewById(R.id.fenleiText);
                fenleiText.setText(fenleiselect1+"-"+fenleiselect2);
                fenleibottomSheetDialog.cancel();
            }
        });
        // 设置第一级数据
        List<String> firstLevelData = new ArrayList<>();
        firstLevelData.add("上衣");
        firstLevelData.add("下装");
        firstLevelData.add("连体装");
        firstLevelPicker.setData(firstLevelData);

        // 初始化第二级数据（假设第一级选择"Option 1"时，第二级可选项为："Sub-option 1"、"Sub-option 2"、"Sub-option 3"）
        List<String> secondLevelData = new ArrayList<>();
        secondLevelData.add("上衣");
        secondLevelData.add("大衣");
        secondLevelData.add("衬衫");
        secondLevelData.add("西装");
        secondLevelData.add("开衫");
        secondLevelData.add("棒球服");
        secondLevelData.add("卫衣");
        secondLevelData.add("夹克");
        secondLevelData.add("斗篷/披风");
        secondLevelData.add("毛衣针织");
        secondLevelData.add("背心/吊带");
        secondLevelData.add("T恤");
        secondLevelData.add("皮衣/皮革");
        secondLevelData.add("羽绒服");
        secondLevelData.add("棉衣/羊羔绒");
        secondLevelData.add("风衣");
        secondLevelData.add("POLO衫");
        secondLevelData.add("牛仔外套");
        secondLevelPicker.setData(secondLevelData);

        // 监听第一级选择变化
        firstLevelPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelSelected(int position) {
                // 等同于选择监听器的onItemSelected，停止滑动时所在的position
                // 当第一级选择器的选项发生变化时触发
                // index 是选中的选项的索引，data 是选中的选项数据
                // 在这里根据第一级选择的值，动态更新第二级数据
                if (position == 0) { // 如果第一级选择了"Option 1"
                    List<String> subOptionData = new ArrayList<>();
                    subOptionData.add("上衣");
                    subOptionData.add("大衣");
                    subOptionData.add("衬衫");
                    subOptionData.add("西装");
                    subOptionData.add("开衫");
                    subOptionData.add("棒球服");
                    subOptionData.add("卫衣");
                    subOptionData.add("夹克");
                    subOptionData.add("斗篷/披风");
                    subOptionData.add("毛衣针织");
                    subOptionData.add("背心/吊带");
                    subOptionData.add("T恤");
                    subOptionData.add("皮衣/皮革");
                    subOptionData.add("羽绒服");
                    subOptionData.add("棉衣/羊羔绒");
                    subOptionData.add("风衣");
                    subOptionData.add("POLO衫");
                    subOptionData.add("牛仔外套");
                    secondLevelPicker.setData(subOptionData);
                } else if (position == 1) { // 如果第一级选择了"Option 2"
                    List<String> subOptionData = new ArrayList<>();
                    subOptionData.add("打底裤");
                    subOptionData.add("休闲裤");
                    subOptionData.add("运动裤");
                    subOptionData.add("牛仔裤");
                    subOptionData.add("半身裙");
                    subOptionData.add("其他裤子");
                    secondLevelPicker.setData(subOptionData);
                } else if (position == 2) { // 如果第一级选择了"Option 3"
                    List<String> subOptionData = new ArrayList<>();
                    subOptionData.add("连衣裙");
                    subOptionData.add("连身裤");
                    secondLevelPicker.setData(subOptionData);
                }
            }
            @Override
            public void onWheelScrolled(int offset) {
                // 滑动距离，初始状态（即一开始position=0时）为0
                // 数据往上滑（即手往下滑）为正数，往下滑为负数
            }
            @Override
            public void onWheelScrollStateChanged(int state) {
                // 滚动状态监听器，0表示没有在滑动，1表示触屏造成的滑动，
                // 2表示停止触屏时造成的滑动（停止触屏后的回弹）
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        firstLevelPicker.setCurved(true);
        secondLevelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        firstLevelPicker.setIndicator(true);
        firstLevelPicker.setIndicatorColor(0xFF123456); //16进制
        firstLevelPicker.setIndicatorSize(3); //单位是px
        secondLevelPicker.setIndicator(true);
        secondLevelPicker.setIndicatorColor(0xFF123456); //16进制
        secondLevelPicker.setIndicatorSize(3); //单位是px
        fenleibottomSheetDialog.setContentView(view);
        fenleibottomSheetDialog.show();
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void SeasonBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_season, null, false);
        seasonbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        TextView seasonBtn = view.findViewById(R.id.seasonBtn);
        CheckBox chuncheck = view.findViewById(R.id.chuncheck);
        CheckBox xiacheck = view.findViewById(R.id.xiacheck);
        CheckBox qiucheck = view.findViewById(R.id.qiucheck);
        CheckBox dongcheck = view.findViewById(R.id.dongcheck);
        seasonBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String result="";
                if(chuncheck.isChecked()) result+="春 ";
                if(xiacheck.isChecked()) result+="夏 ";
                if(qiucheck.isChecked()) result+="秋 ";
                if(dongcheck.isChecked()) result+="冬";

                TextView seasonText = findViewById(R.id.seasonText);
                seasonText.setText(result);
                seasonbottomSheetDialog.cancel();
            }
        });

        seasonbottomSheetDialog.setContentView(view);
        seasonbottomSheetDialog.show();
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void PlaceBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_place, null, false);
        placebottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        TextView placeBtn = view.findViewById(R.id.placeBtn);
        EditText editText = view.findViewById(R.id.placeText);
        placeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String result="";
                if (editText != null)
                    result = editText.getText().toString();
                else showRequestFailedDialog("null");
                TextView placeText = findViewById(R.id.placeText);
                placeText.setText(result);
                placebottomSheetDialog.cancel();
            }
        });

        placebottomSheetDialog.setContentView(view);
        placebottomSheetDialog.show();
    }
    private void LingxingBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_lingxing, null, false);
        lingxingbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("圆领");
        dataList.add("v领");
        dataList.add("一字领");
        dataList.add("立领");
        dataList.add("堆领");
        wheelPicker.setData(dataList);
        TextView lingxingBtn = view.findViewById(R.id.lingxingBtn);
        TextView lingxingText =findViewById(R.id.lingxingText);
        lingxingBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                lingxingText.setText(select);
                lingxingbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        lingxingbottomSheetDialog.setContentView(view);
        lingxingbottomSheetDialog.show();
    }
    private void BiheBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_bihe, null, false);
        bihebottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("单排扣");
        dataList.add("开衫");
        dataList.add("套头");
        dataList.add("纽扣");
        dataList.add("拉链");
        dataList.add("双排扣");
        dataList.add("斜襟");dataList.add("其他");
        wheelPicker.setData(dataList);
        TextView biheBtn = view.findViewById(R.id.biheBtn);
        TextView biheText =findViewById(R.id.biheText);
        biheBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                biheText.setText(select);
                bihebottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        bihebottomSheetDialog.setContentView(view);
        bihebottomSheetDialog.show();
    }
    private void XiuchangBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        xiuchangbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("短袖");
        dataList.add("无袖");
        dataList.add("长袖");
        wheelPicker.setData(dataList);
        TextView xiuchangBtn = view.findViewById(R.id.xiuchangBtn);
        TextView xiuchangText =findViewById(R.id.xiuchangText);
        xiuchangBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                xiuchangText.setText(select);
                xiuchangbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        xiuchangbottomSheetDialog.setContentView(view);
        xiuchangbottomSheetDialog.show();
    }
    private void MianliaoBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        mianliaobottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("丝质");
        dataList.add("绸缎");
        dataList.add("涤纶");
        dataList.add("纱质/雪纺");
        dataList.add("针织");
        dataList.add("蕾丝");
        dataList.add("牛仔");
        dataList.add("皮草");
        dataList.add("金属光泽");
        dataList.add("西装面料");
        dataList.add("羊毛");
        dataList.add("棉");
        dataList.add("麻");
        dataList.add("毛妮");
        dataList.add("其他");
        wheelPicker.setData(dataList);
        TextView xiuchangBtn = view.findViewById(R.id.xiuchangBtn);
        TextView mianliaoText =findViewById(R.id.mianliaoText);
        xiuchangBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                mianliaoText.setText(select);
                mianliaobottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        mianliaobottomSheetDialog.setContentView(view);
        mianliaobottomSheetDialog.show();
    }
    private void FenggeBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        fenggebottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("运动风");
        dataList.add("甜美可爱风");
        dataList.add("基本款百搭");
        dataList.add("度假风");
        dataList.add("男友风");
        dataList.add("时髦风");
        dataList.add("经典风");
        dataList.add("商务风");
        dataList.add("浪漫风");
        dataList.add("复古风");
        dataList.add("派对风");
        wheelPicker.setData(dataList);
        TextView xiuchangBtn = view.findViewById(R.id.xiuchangBtn);
        TextView fenggeText =findViewById(R.id.fenggeText);
        xiuchangBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String select = "";
                int SelectedIndex = wheelPicker.getCurrentItemPosition();
                select = (String) wheelPicker.getData().get(SelectedIndex);
                fenggeText.setText(select);
                fenggebottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(true);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        fenggebottomSheetDialog.setContentView(view);
        fenggebottomSheetDialog.show();
    }
}