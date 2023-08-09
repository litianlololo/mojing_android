package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mojing.Fragments.Fragment_yichu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dapei_ChoseDown_Activity extends AppCompatActivity {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private SharedPreferencesManager sharedPreferencesManager;
    private Activity activity;
    private ScrollView ImgScroll;
    private Danpin[] danpins = new Danpin[1];
    private Danpin SelectedDanpin;
    private List<Danpin> selectedDanpin = new ArrayList<>();
    private int currentSelectedPosition = -1;
    private List<ImageView> imageViewsList = new ArrayList<>();
    private TextView choseBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_chose_down);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        ImgScroll= findViewById(R.id.ImgScroll);
        choseBtn = findViewById(R.id.choseBtn);
        activity= this;

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Yichu_Single_Activity.this, MainActivity.class);
                //startActivity(intent);
                finish(); // 结束当前的Yichu_Single_Activity
            }
        });
        System.out.println(sharedPreferencesManager.isLoggedIn());
        if(sharedPreferencesManager.isLoggedIn()) {
            //初始化我的衣装
            loadAll(new Fragment_yichu.AddDanpinCallback() {
                @Override
                public void onAddDanpin() {
                    loadingImg();
//                System.out.println(selectedURL);
                }
            }, new Fragment_yichu.ImageLoadingCallback() {
                @Override
                public void onImagesLoaded(List<Danpin> urls) {
                    // 当图片加载完成时，这个方法会被调用
                    // 使用加载完的'urls'生成图片布局
                    generateImageLayout(urls);
                }
            });
        }
        choseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                if(currentSelectedPosition!=-1) {
                    resultIntent.putExtra("_id", SelectedDanpin._id); // 传递id
                    resultIntent.putExtra("img_url", SelectedDanpin.img_url); // 传递url
                }
                setResult(2, resultIntent);
                finish();
            }
        });
    }
    private void generateImageLayout(List<Danpin> urls) {
        if (!sharedPreferencesManager.isLoggedIn())
            return;

        // 创建一个垂直线性布局用于放置图片的容器
        LinearLayout containerLayout = new LinearLayout(activity);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.setMargins(10, 10, 10, 10); // 设置容器与 ScrollView 边界的间隔
        containerLayout.setLayoutParams(containerParams);

        // 定义每行要显示的图片数量
        int imagesPerRow = 3;
        int totalUrls = urls.size();
        for (int i = 0; i < totalUrls; i += imagesPerRow) {
            // 创建一个水平线性布局用于放置一行的图片
            LinearLayout rowLayout = new LinearLayout(activity);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, 10); // 设置行之间的间隔
            rowLayout.setLayoutParams(rowParams);

            // 动态添加每行的图片
            for (int j = 0; j < imagesPerRow; j++) {
                int position = i + j;
                if (position < totalUrls) {
                    // 创建ImageView
                    ImageView imageView = new ImageView(activity);
                    int finalPosition = position;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(activity)
                                    .load(uuimg + urls.get(finalPosition).img_url)
                                    .into(imageView);
                        }
                    });
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );
                    imageParams.setMarginEnd(5); // 设置图片之间的间隔
                    imageView.setLayoutParams(imageParams);
                    imageView.setBackgroundResource(R.drawable.yichu_img_border);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (currentSelectedPosition == finalPosition) {
                                SelectedDanpin=new Danpin();
                                // 当前图片已被选中，
                                imageView.setBackgroundResource(R.drawable.yichu_img_border);
                                currentSelectedPosition = -1;
                            } else {
                                // 恢复上一个被点击的图片样式
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currentSelectedPosition != -1) {
                                            ImageView preimg = imageViewsList.get(currentSelectedPosition);
                                            preimg.setBackgroundResource(R.drawable.yichu_img_border);
                                        }
                                    }
                                });

                                SelectedDanpin = urls.get(finalPosition);
                                currentSelectedPosition = finalPosition;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setBackgroundResource(R.drawable.img_chosed);
                                    }
                                });
                            }
                        }
                    });
                    rowLayout.addView(imageView);
                    imageViewsList.add(imageView);
                }
            }
            int remainingImages = totalUrls - i;
            if (remainingImages == 1) {
                // Add two blank images to the last row
                ImageView blankImageView1 = new ImageView(activity);
                blankImageView1.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                rowLayout.addView(blankImageView1);
                ImageView blankImageView2 = new ImageView(activity);
                blankImageView2.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                rowLayout.addView(blankImageView2);
                imageViewsList.add(blankImageView1);
                imageViewsList.add(blankImageView2);
            } else if (remainingImages == 2) {
                // Add one blank image to the last row
                ImageView blankImageView = new ImageView(activity);
                blankImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                rowLayout.addView(blankImageView);
                imageViewsList.add(blankImageView);
            }
            // 将一行图片添加到容器中
            containerLayout.addView(rowLayout);
        }

        // 将容器布局添加到 ScrollView 中
        ImgScroll.removeAllViews();
        ImgScroll.addView(containerLayout);
    }


    // 像素转换工具方法：dp 转 px
    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    //加载我的衣装
    private void loadAll(Fragment_yichu.AddDanpinCallback addDanpincallback, Fragment_yichu.ImageLoadingCallback imageLoadingCallback)
    {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                // 创建请求
                Request.Builder requestBuilder = new Request.Builder()
                        .url(uu+"/cloth/my-clothes")
                        .get()
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
                        // 提取键为"code"的值
                        int code = responseJson.getInt("code");
                        //确定返回状态
                        switch (code) {
                            case 200:
                                JSONObject dataJson = responseJson.getJSONObject("data");
                                AddDanpin(dataJson);
                                //showRequestFailedDialog("加载成功");
                                addDanpincallback.onAddDanpin();
                                //System.out.println(dataJson);
                                break;
                            case 1001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("请先登录");
                                break;
                            default:
                                showRequestFailedDialog("添加失败");
                                break;
                        }
                        //System.out.println("Response: " + responseData);
                        // 记得关闭响应体
                        responseBody.close();
                    } else {
                        // 请求失败，处理错误
                        System.out.println("Request failed");
                        showRequestFailedDialog("请求失败");
                    }
                } catch (IOException e) {
                    showRequestFailedDialog("网络错误，添加失败");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // 确保在 UI 线程中调用 imageLoadingCallback.onImagesLoaded(selectedURL)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageLoadingCallback.onImagesLoaded(selectedDanpin);
                    }
                });
            }
        }).start();
//        imageLoadingCallback.onImagesLoaded(selectedURL);
    }
    private void AddDanpin(JSONObject dataJson) throws JSONException {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        JSONArray myClothesArray = dataJson.getJSONArray("my_clothes");
        danpins= new Danpin[myClothesArray.length()];
        for (int i = 0; i < myClothesArray.length(); i++) {
            JSONObject clothObject = myClothesArray.getJSONObject(i);

            // Now you can access the properties of each cloth object
            String _id = clothObject.has("_id") ? clothObject.getString("_id") : "";
            String img_url = clothObject.has("img_url") ?clothObject.getString("img_url"): "";
            String type = clothObject.has("type") ?clothObject.getString("type"): "";
            String type2 = clothObject.has("type2") ? clothObject.getString("type2") : "";
            String bihe = clothObject.has("bihe") ? clothObject.getString("bihe") : "";
            String fengge = clothObject.has("fengge") ? clothObject.getString("fengge") : "";
            String lingxing = clothObject.has("lingxing") ? clothObject.getString("lingxing") : "";
            String mianliao = clothObject.has("mianliao") ? clothObject.getString("mianliao") : "";
            String xiuchang = clothObject.has("xiuchang") ? clothObject.getString("xiuchang") : "";
            String storeplace = clothObject.has("storeplace") ? clothObject.getString("storeplace") : "";
            String shenchang = clothObject.has("shenchang") ? clothObject.getString("shenchang") : "";
            boolean spring = clothObject.optBoolean("spring", false);
            boolean summer = clothObject.optBoolean("summer", false);
            boolean autumn = clothObject.optBoolean("autumn", false);
            boolean winter = clothObject.optBoolean("winter", false);

            Danpin tmp = new Danpin();
            tmp._id=_id;
            tmp.season.spring=spring;
            tmp.season.summer=summer;
            tmp.season.autumn=autumn;
            tmp.season.winter=winter;
            tmp.img_url=img_url;
            tmp.type= convertUnicodeToChinese(type);
            tmp.type2=convertUnicodeToChinese(type2);
            tmp.bihe=bihe;
            tmp.fengge=fengge;
            tmp.lingxing=lingxing;
            tmp.mianliao=mianliao;
            tmp.xiuchang=xiuchang;
            tmp.storeplace=storeplace;
            tmp.shenchang = shenchang;
            System.out.println(img_url);
            //System.out.println("unicode:   "+ tmp.type.equals(type));
            danpins[i]=tmp;
        }
    }
    //unicode 转中文
    public static String convertUnicodeToChinese(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        int length = unicodeStr.length();
        for (int i = 0; i < length; i++) {
            char c = unicodeStr.charAt(i);
            if (c == '\\' && i + 1 < length && unicodeStr.charAt(i + 1) == 'u') {
                String hex = unicodeStr.substring(i + 2, i + 6);
                int codePoint = Integer.parseInt(hex, 16);
                sb.append((char) codePoint);
                i += 5;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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
    private void loadingImg() {
        if (!sharedPreferencesManager.isLoggedIn())
            return;
//        selectedURL = new ArrayList<>();
        selectedDanpin = new ArrayList<>();
        System.out.println("衣装总数： " + danpins.length);
        for (Danpin tmp : danpins) {
            //筛选类型
            if (tmp.type.equals("下装") || tmp.type.equals("连身装")) {
//                    selectedURL.add(tmp.img_url);
                selectedDanpin.add(tmp);
            }
        }
    }
}