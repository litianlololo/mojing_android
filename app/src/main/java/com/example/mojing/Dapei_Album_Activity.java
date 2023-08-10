package com.example.mojing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dapei_Album_Activity extends AppCompatActivity {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    //    public String uu="http://47.102.43.156:8007/api";
//    public String uuimg="http://47.102.43.156:8007";
    private View PBtn,AIBtn,DBtn;
    private ImageView imgself,imgai,imgdesigner;
    private SharedPreferencesManager sharedPreferencesManager;
    private String chosedone="PBtn";
    private GridLayout photoView;
    private ScrollView ImgScroll;
    private List<Dapei> dapeis = new ArrayList<>();
    private Dapei SelectedDapei;
    private Activity activity = this;
    private int loadedImageCount = 0;
    private String origin="self";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_album);

        PBtn = findViewById(R.id.Pview);
        AIBtn = findViewById(R.id.AIview);
        DBtn = findViewById(R.id.DView);
        photoView = findViewById(R.id.photoView);
        imgself = findViewById(R.id.imgself);
        imgai =findViewById(R.id.imgai);
        ImgScroll =findViewById(R.id.ImgScroll);
        imgdesigner =findViewById(R.id.imgdesigner);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        Drawable radius_border1 = getResources().getDrawable(R.drawable.radius_border1,null);
        Drawable radius_chosed = getResources().getDrawable(R.drawable.radius_border_chosed,null);

        ImageView calendarImg = findViewById(R.id.calendar);
        calendarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(activity, Dapei_Calendar_Activity.class);
                startActivity(tmp);
            }
        });
        PBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("PBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("AIBtn") ){
                        AIBtn.setBackground(radius_border1);
                        imgai.setImageResource(R.drawable.dapei_album_ai1);
                    }else if(chosedone.equals("DBtn")){
                        DBtn.setBackground(radius_border1);
                        imgdesigner.setImageResource(R.drawable.dapei_album_designer1);
                    }
                    PBtn.setBackground(radius_chosed);
                    imgself.setImageResource(R.drawable.dapei_album_self2);
                    chosedone="PBtn";
                    origin="self";
                    //加载个人搭配
                    ImgScroll.removeAllViews();
                    if(sharedPreferencesManager.isLoggedIn()) {
                        loadAll(new AddDapeiCallback() {
                            @Override
                            public void onAddDapei() {
                                CombineAll(new ImageLoadCallback() {
                                    @Override
                                    public void onImageLoaded(Bitmap upImage, Bitmap downImage) {

                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        AIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("AIBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("PBtn") ){
                        PBtn.setBackground(radius_border1);
                        imgself.setImageResource(R.drawable.dapei_album_self1);
                    }else if(chosedone.equals("DBtn")){
                        DBtn.setBackground(radius_border1);
                        imgdesigner.setImageResource(R.drawable.dapei_album_designer1);
                    }
                    AIBtn.setBackground(radius_chosed);
                    imgai.setImageResource(R.drawable.dapei_album_ai2);
                    chosedone="AIBtn";
                    origin="ai";
                    //加载AI搭配
                    ImgScroll.removeAllViews();
                    if(sharedPreferencesManager.isLoggedIn()) {
                        loadAll(new AddDapeiCallback() {
                            @Override
                            public void onAddDapei() {
                                CombineAll(new ImageLoadCallback() {
                                    @Override
                                    public void onImageLoaded(Bitmap upImage, Bitmap downImage) {

                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        DBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedone.equals("DBtn")){
                    photoView.removeAllViews();
                    if(chosedone.equals("PBtn") ){
                        PBtn.setBackground(radius_border1);
                        imgself.setImageResource(R.drawable.dapei_album_self1);
                    }else if(chosedone.equals("AIBtn")){
                        AIBtn.setBackground(radius_border1);
                        imgai.setImageResource(R.drawable.dapei_album_ai1);
                    }
                    DBtn.setBackground(radius_chosed);
                    imgdesigner.setImageResource(R.drawable.dapei_album_designer2);
                    chosedone="DBtn";
                    origin="designer";
                    //加载AI搭配
                    ImgScroll.removeAllViews();
                    if(sharedPreferencesManager.isLoggedIn()) {
                        loadAll(new AddDapeiCallback() {
                            @Override
                            public void onAddDapei() {
                                CombineAll(new ImageLoadCallback() {
                                    @Override
                                    public void onImageLoaded(Bitmap upImage, Bitmap downImage) {

                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        //加载我的搭配
        if(sharedPreferencesManager.isLoggedIn()) {
            loadAll(new AddDapeiCallback() {
                @Override
                public void onAddDapei() {
                    CombineAll(new ImageLoadCallback() {
                        @Override
                        public void onImageLoaded(Bitmap upImage, Bitmap downImage) {

                        }
                    });
                }
            });
        }
    }
    //generateImageLayout(urls);
    private void generateImageLayout(List<Dapei> urls) {
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
                            imageView.setImageBitmap(urls.get(finalPosition).combin_img);
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
                            Intent intent = new Intent(activity, Dapei_Info_Activity.class);
                            intent.putExtra("_id", urls.get(finalPosition)._id); // 传递id变量
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            urls.get(finalPosition).combin_img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            intent.putExtra("bitmap", byteArray);
                            //计算平均分
                            int totalScore = 0;
                            for (int score : urls.get(finalPosition).share_score) {totalScore += score;}
                            int avg_score = 0;
                            if(urls.get(finalPosition).share_score.length!=0)
                                avg_score=totalScore / urls.get(finalPosition).share_score.length;
                            intent.putExtra("avg_score", avg_score);
//                          intent.putExtra("designer_score", urls.get(finalPosition).designer_score);
                            activity.startActivity(intent);
                        }
                    });
                    rowLayout.addView(imageView);
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
            } else if (remainingImages == 2) {
                // Add one blank image to the last row
                ImageView blankImageView = new ImageView(activity);
                blankImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                rowLayout.addView(blankImageView);
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
    //加载我的搭配
    private void loadAll(AddDapeiCallback addDapeiCallback)
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
                        .url(uu+"/cloth/all-match?origin="+origin)
                        .get()
                        .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                        //.addHeader("origin",chosedone);
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
                                JSONArray dataJson = responseJson.getJSONArray("data");
                                AddDapei(dataJson);
                                //showRequestFailedDialog("加载成功");
                                addDapeiCallback.onAddDapei();
                                System.out.println(dataJson);
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
            }
        }).start();
    }
    private void AddDapei(JSONArray dataJson) throws JSONException {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        dapeis= new ArrayList<>();
        for (int i = 0; i < dataJson.length(); i++) {
            JSONObject clothObject = dataJson.getJSONObject(i);

            // Now you can access the properties of each cloth object
            String _id = clothObject.has("_id") ? clothObject.getString("_id") : "";
            String up_cloth = clothObject.has("up_cloth") ?clothObject.getString("up_cloth"): "";
            String down_cloth = clothObject.has("down_cloth") ?clothObject.getString("down_cloth"): "";
            String origin = clothObject.has("origin") ? clothObject.getString("origin") : "";
            String name = clothObject.has("name") ? clothObject.getString("name") : "";

            JSONArray sketchArray = clothObject.getJSONArray("sketch");
            String[] sketch = new String[sketchArray.length()];
            for (int k = 0; k < sketchArray.length(); k++) {
                sketch[k] = sketchArray.getString(k);
            }
            for (String s : sketch) {
                System.out.println(s);
            }
            // 提取 "share_score" 字段
            JSONArray shareScoreArray = clothObject.has("share_score") ?clothObject.getJSONArray("share_score"):null;
            int[] shareScore = new int[shareScoreArray.length()];
            for (int k = 0; k < shareScoreArray.length();k++) {
                shareScore[k] = shareScoreArray.getInt(i);
            }
            for (int score : shareScore) {
                System.out.println("Share Score: " + score);
            }

            // 提取 "designer_score" 字段
            JSONArray designerScoreArray =clothObject.has("designer_score") ? clothObject.getJSONArray("designer_score"):null;
            int[] designerScore = new int[designerScoreArray.length()];
            for (int k = 0; k < designerScoreArray.length(); k++) {
                designerScore[k] = designerScoreArray.getInt(k);
            }
            for (int score : designerScore) {
                System.out.println("Designer Score: " + score);
            }

//            // 提取 "tags" 字段
//            JSONArray tagsArray = clothObject.getJSONArray("tags");
//            String[] tags = new String[tagsArray.length()];
//            for (int k = 0; k < tagsArray.length(); k++) {
//                tags[k] = tagsArray.getString(k);
//            }
            String user_id = clothObject.has("user_id") ? clothObject.getString("user_id") : "";

            Dapei tmp = new Dapei();
            tmp._id=_id;
            tmp.up._id=up_cloth;
            tmp.down._id= down_cloth;
            tmp.origin = origin;
            tmp.up.img_url = sketch[0];
            tmp.down.img_url =sketch[1];
            tmp.share_score = shareScore;
            tmp.designer_score = designerScore;

            dapeis.add(tmp);
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
    private void CombineAll(final ImageLoadCallback callback){

        for(Dapei tmp:dapeis) {
            //加载up
            Glide.with(this)
                    .asBitmap()
                    .load(uuimg + tmp.up.img_url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            // Assign the Bitmap to tmp.up_img variable
                            tmp.up_img = bitmap;
                            // 在加载完成后调用回调
                            checkAndCallCallback(tmp, callback);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle case when loading is cleared
                            System.out.println("oops up");
                            // 在加载完成后调用回调
                            checkAndCallCallback(tmp, callback);
                        }
                    });
            //加载down
            Glide.with(this)
                    .asBitmap()
                    .load(uuimg + tmp.down.img_url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            // Assign the Bitmap to tmp.up_img variable
                            tmp.down_img = bitmap;
                            checkAndCallCallback(tmp, callback);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle case when loading is cleared
                            System.out.println("oops down");
                            checkAndCallCallback(tmp, callback);
                        }
                    });
            //tmp.combin_img=combineImagesVertically(tmp.up_img,tmp.down_img);
        }
    }
    private void checkAndCallCallback(Dapei tmp, ImageLoadCallback callback) {
        if (tmp.up_img != null && tmp.down_img != null) {
            // 两张图片都加载完成，调用合并方法
            Bitmap combinedBitmap = combineImages(tmp.up_img, tmp.down_img);
            tmp.combin_img = combinedBitmap;
            // 两张图片都加载完成，调用回调
            callback.onImageLoaded(tmp.up_img, tmp.down_img);
            loadedImageCount++; // 增加已加载图片数量
            System.out.println(loadedImageCount);
            if (loadedImageCount == dapeis.size()) {
                loadedImageCount=0;
                // 如果已加载图片数量等于总图片数量，表示所有图片都加载完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        generateImageLayout(dapeis);
                    }
                });

            }
        }
    }
    public Bitmap combineImages(Bitmap topImage, Bitmap bottomImage) {
        int imageSizeInDp = 100;
        int marginInDp = 5;

        int imageSizeInPixels = (int) (imageSizeInDp * activity.getResources().getDisplayMetrics().density);
        int marginInPixels = (int) (marginInDp * activity.getResources().getDisplayMetrics().density);

        // Scale the images to fixed dimensions
        Bitmap scaledTopImage = Bitmap.createScaledBitmap(topImage, imageSizeInPixels, imageSizeInPixels, true);
        Bitmap scaledBottomImage = Bitmap.createScaledBitmap(bottomImage, imageSizeInPixels, imageSizeInPixels, true);

        // Scale factor for custom layout
        float scaleFactor = 0.6f;
        Bitmap scaledTopImageCustom = Bitmap.createScaledBitmap(scaledTopImage, (int) (scaledTopImage.getWidth() * scaleFactor), (int) (scaledTopImage.getHeight() * scaleFactor), true);
        Bitmap scaledBottomImageCustom = Bitmap.createScaledBitmap(scaledBottomImage, (int) (scaledBottomImage.getWidth() * scaleFactor), (int) (scaledBottomImage.getHeight() * scaleFactor), true);

        int imageSize = imageSizeInPixels; // Use the fixed image size

        // Create a new blank square bitmap with white background
        Bitmap combinedBitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
        combinedBitmap.eraseColor(Color.WHITE);

        // Calculate the position to place the images in the square
        int offsetXTop = marginInPixels;
        int offsetYTop = marginInPixels;
        int offsetXBottom = imageSize - scaledBottomImageCustom.getWidth() - marginInPixels;
        int offsetYBottom = imageSize - scaledBottomImageCustom.getHeight() - marginInPixels;

        // Create a canvas with the combined bitmap
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the scaled images with custom placement
        canvas.drawBitmap(scaledBottomImageCustom, offsetXBottom, offsetYBottom, null);
        canvas.drawBitmap(scaledTopImageCustom, offsetXTop, offsetYTop, null);

        return combinedBitmap;
    }



        //回调接口，下载完搭配后继续操作
    public interface AddDapeiCallback {
        void onAddDapei();
    }
    // 图片加载完成的回调接口
    public interface ImageLoadCallback {
        void onImageLoaded(Bitmap upImage, Bitmap downImage);
    }
}