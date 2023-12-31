package com.example.mojing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Yichu_Add_Activity extends AppCompatActivity {
    public String uu="http://47.102.43.156:8007/api";
    public String uuimg="http://47.102.43.156:8007";
    private ImageView imageButton;
    private TextView addBtn;
    public Activity activity = this;
    private Uri croppedImageUri;
    private Danpin danpin;
    private BottomSheetDialog fenleibottomSheetDialog;
    private BottomSheetDialog seasonbottomSheetDialog;
    private  BottomSheetDialog placebottomSheetDialog;
    private  BottomSheetDialog lingxingbottomSheetDialog,fenggebottomSheetDialog;
    private  BottomSheetDialog bihebottomSheetDialog,xiuchangbottomSheetDialog,mianliaobottomSheetDialog,shenchangbottomSheetDialog;
    private PersonalItemView fenlei_content, season_content, place_content, shenchang_content,lingxing_content, bihe_content, xiuchang_content, mianliao_content, fengge_content;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private SharedPreferencesManager sharedPreferencesManager;
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
                Intent intent = new Intent(Yichu_Add_Activity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 结束当前的Yichu_Single_Activity
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
        shenchang_content = findViewById(R.id.shenchang_content);
        sharedPreferencesManager = new SharedPreferencesManager(this);
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
        shenchang_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShenchangBottomSheet();
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
        //添加单品
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferencesManager.isYouke())
                {
                    showRequestFailedDialog("游客无此权限，请先登录");
                    return;
                }
                if(danpin.img_url == null){
                    showRequestFailedDialog("请先添加图片");
                    return;
                }
                if(danpin.type == null){
                    showRequestFailedDialog("请先完善衣装信息");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("name","");
                            json.put("version","");
                            json.put("pattern",null);
                            json.put("scene",null);
                            json.put("fabric",null);
                            json.put("temperature",null);
                            json.put("highlights",null);
                            json.put("user_id",sharedPreferencesManager.getUserID());
                            json.put("img_url", danpin.img_url);
                            json.put("type", danpin.type);
                            json.put("type2", danpin.type2);
                            json.put("bihe", danpin.bihe);
                            json.put("fengge", danpin.fengge);
                            json.put("lingxing", danpin.lingxing);
                            json.put("mianliao", danpin.mianliao);
                            json.put("spring", danpin.season.spring);
                            json.put("summer", danpin.season.summer);
                            json.put("autumn", danpin.season.autumn);
                            json.put("winter", danpin.season.winter);
                            json.put("xiuchang", danpin.xiuchang);
                            json.put("storeplace", danpin.storeplace);
                            json.put("shenchang",danpin.shenchang);
                            System.out.println("JSON+    "+json);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(uu+"/cloth/add")
                                .post(requestBody)
                                .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                        // 将会话信息添加到请求头部
                        if (sharedPreferencesManager.getKEY_Session_ID() != null) {
                            //showRequestFailedDialog(sharedPreferencesManager.getKEY_Session_ID());
                        }else{
                            showRequestFailedDialog("null");
                        }
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
                                // 提取键为"code"的值
                                int code = responseJson.getInt("code");
                                //确定返回状态
                                switch (code) {
                                    case 200:
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ShowAndDrop("添加成功");
                                            }
                                        });
                                        JSONObject dataJson = responseJson.getJSONObject("data");
                                        System.out.println(dataJson);
                                        danpin._id = dataJson.getString("_id");
                                        break;
                                    case 1001:
                                        System.out.println(sharedPreferencesManager.getUsername());
                                        showRequestFailedDialog("请先登录");
                                        break;
                                    default:
                                        showRequestFailedDialog("添加失败");
                                        break;
                                }
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                showRequestFailedDialog("网络错误，添加失败");
                            }
                        } catch (IOException e) {
                            showRequestFailedDialog("网络错误，添加失败");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
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
            try {
                removebackground(croppedImageUri, new RemoveBackgroundCallback() {
                    @Override
                    public void onBackgroundRemoved(Uri result) {
                        // 在这里处理返回的结果
                        if (result != null) {
                            croppedImageUri = result;
                            runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            Glide.with(activity)
                                    .load(uuimg+croppedImageUri)
                                            .into(imageButton);
                            }
                        });
                        }else {
                            // 处理请求失败等情况
                            showRequestFailedDialog("网络请求失败，请检查网络或稍后再试");
                        }
                    }
                });
            } catch (URISyntaxException e) {
                showRequestFailedDialog("网络请求失败，请检查网络或稍后再试");
                throw new RuntimeException(e);
            }

        }
    }
    //回调接口，用来加载图片
    public interface RemoveBackgroundCallback {
        void onBackgroundRemoved(Uri result);
    }

    private Uri removebackground(Uri uri,RemoveBackgroundCallback callback) throws URISyntaxException {
        File pngFile = new File(new URI(uri.toString()));
        final Uri[] result = new Uri[1];
        System.out.println("pngFile   "+pngFile);
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
                        .url(uu+"/file/rembg")
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
                        System.out.println("img_url: " + result[0]);
                        callback.onBackgroundRemoved(result[0]);
                        // 记得关闭响应体
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
                //.withMaxResultSize(imageButton.getWidth(), imageButton.getHeight()) // 设置裁剪后图片的最大尺寸为 x200像素
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
        fenleiBtn = view.findViewById(R.id.enBtn);
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

                danpin.type = fenleiselect1;
                danpin.type2 = fenleiselect2;

                TextView fenleiText = findViewById(R.id.fenleiText);
                fenleiText.setText(fenleiselect1+"-"+fenleiselect2);
                fenleibottomSheetDialog.cancel();
            }
        });
        // 设置第一级数据
        List<String> firstLevelData = new ArrayList<>();
        firstLevelData.add("上衣");
        firstLevelData.add("下装");
        firstLevelData.add("连身装");
        firstLevelPicker.setData(firstLevelData);

        // 初始化第二级数据（假设第一级选择"Option 1"时，第二级可选项为："Sub-option 1"、"Sub-option 2"、"Sub-option 3"）
        List<String> secondLevelData = new ArrayList<>();
        secondLevelData.add("T恤");
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
                    subOptionData.add("T恤");
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
                    subOptionData.add("皮衣/皮革");
                    subOptionData.add("羽绒服");
                    subOptionData.add("棉衣/羊羔绒");
                    subOptionData.add("风衣");
                    subOptionData.add("POLO衫");
                    subOptionData.add("牛仔外套");
                    secondLevelPicker.setData(subOptionData);
                    lingxing_content.setVisibility(View.VISIBLE);
                    bihe_content.setVisibility(View.VISIBLE);
                    shenchang_content.setVisibility(View.GONE);
                } else if (position == 1) { // 如果第一级选择了"Option 2"
                    List<String> subOptionData = new ArrayList<>();
                    subOptionData.add("打底裤");
                    subOptionData.add("休闲裤");
                    subOptionData.add("运动裤");
                    subOptionData.add("牛仔裤");
                    subOptionData.add("半身裙");
                    subOptionData.add("其他裤子");
                    secondLevelPicker.setData(subOptionData);
                    xiuchang_content.setVisibility(View.GONE);
                    lingxing_content.setVisibility(View.GONE);
                    bihe_content.setVisibility(View.GONE);
                    shenchang_content.setVisibility(View.VISIBLE);
                } else if (position == 2) { // 如果第一级选择了"Option 3"
                    List<String> subOptionData = new ArrayList<>();
                    subOptionData.add("连衣裙");
                    subOptionData.add("连身裤");
                    secondLevelPicker.setData(subOptionData);
                    xiuchang_content.setVisibility(View.GONE);
                    lingxing_content.setVisibility(View.GONE);
                    bihe_content.setVisibility(View.GONE);
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
        firstLevelPicker.setCurved(false);
        secondLevelPicker.setCurved(false);
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
                if(chuncheck.isChecked()){ result+="春 "; danpin.season.spring=true;}
                if(xiacheck.isChecked()) {result+="夏 ";danpin.season.summer=true;}
                if(qiucheck.isChecked()) {result+="秋 ";danpin.season.autumn=true;}
                if(dongcheck.isChecked()) {result+="冬";danpin.season.winter=true;}

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
                danpin.storeplace=result;
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
                danpin.lingxing=select;
                lingxingText.setText(select);
                lingxingbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
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
                danpin.bihe = select;
                biheText.setText(select);
                bihebottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
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
                danpin.xiuchang = select;
                xiuchangText.setText(select);
                xiuchangbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
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
        dataList.add("毛呢");
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
                danpin.mianliao = select;
                mianliaoText.setText(select);
                mianliaobottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
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
                danpin.fengge = select;
                fenggeText.setText(select);
                fenggebottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        fenggebottomSheetDialog.setContentView(view);
        fenggebottomSheetDialog.show();
    }
    private void ShenchangBottomSheet() {
        //创建布局
        View view = LayoutInflater.from(activity).inflate(R.layout.danpin_xiuchang, null, false);
        shenchangbottomSheetDialog = new BottomSheetDialog(activity);
        //设置布局
        WheelPicker wheelPicker = view.findViewById(R.id.wheelPicker);
        // 设置数据
        List<String> dataList = new ArrayList<>();
        dataList.add("七分裤");
        dataList.add("长裤");
        dataList.add("短裤");
        dataList.add("热裤");
        dataList.add("其他");
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
                danpin.shenchang = select;
                shenchangText.setText(select);
                shenchangbottomSheetDialog.cancel();
            }
        });
        // 设置是否有卷曲感，不能微调卷曲幅度，默认false
        wheelPicker.setCurved(false);
        //设置是否有指示器，设置后选中项的上下会用线框柱
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456); //16进制
        wheelPicker.setIndicatorSize(3); //单位是px
        shenchangbottomSheetDialog.setContentView(view);
        shenchangbottomSheetDialog.show();
    }
    private void ShowAndDrop(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("前往单品详情页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定后执行页面跳转和关闭当前Activity
                Intent intent = new Intent(Yichu_Add_Activity.this, Yichu_Single_Activity.class);
                intent.putExtra("_id", danpin._id); // 传递id变量
                startActivity(intent);
                finish(); // 结束当前的Yichu_Single_Activity
            }
        });
        builder.show();
    }
}