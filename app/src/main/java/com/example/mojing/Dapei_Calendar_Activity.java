package com.example.mojing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Dapei_Calendar_Activity extends AppCompatActivity {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private int year;
    private int month;
    private int day;
    private Activity activity=this;
    private int daysInMonth;
    private int dayOfWeek;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView calendarText;
    private List<ImageView> calendarImgs;
    private List<ConstraintLayout> calendarCons;
    private List<TextView> calendarTexts;
    private Calendar calendar;
    private int loadedImageCount=0;
    private int match_size;
    private Match newmatch= new Match();
    private boolean isnew;
    private ConstraintLayout consShow;
    private int chosedDay=-1;
    private class Match{
        public int year;
        public int month;
        public int day;
        public String match_id;
        public String up_url;
        public String down_url;
        public Bitmap up_img;
        public Bitmap down_img;
        public Bitmap comb_img;

    }
    private List<Match> matchs = new ArrayList<>();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_calendar);
        //返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个Activity
            }
        });
        sharedPreferencesManager = new SharedPreferencesManager(this);
        calendarText = findViewById(R.id.calendarText);
        calendarImgs = new ArrayList<>();
        calendarTexts = new ArrayList<>();
        calendarCons = new ArrayList<>();

        //从详情页进入
        if (getIntent().hasExtra("match_id")) {
            isnew = true;
            newmatch.match_id = getIntent().getStringExtra("match_id");
            newmatch.comb_img = getIntent().getParcelableExtra("comb_img");
        }
        consShow = findViewById(R.id.consShow);
        // 将ConstraintLayout添加到列表
        for (int i = 1; i <= 42; i++) {
            int consId = getResources().getIdentifier("c" + i, "id", getPackageName());
            ConstraintLayout cons = findViewById(consId);
            cons.setBackgroundResource(R.drawable.radius_border4);
            int finalI = i;
            cons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //搭配详情
                    if (calendarImgs.get(finalI-1).getDrawable() != null) {
                        consShow.setVisibility(View.VISIBLE);
                        TextView data = findViewById(R.id.textView35);
                        ImageView imgv = findViewById(R.id.imageView10);
                        imgv.setImageBitmap(((BitmapDrawable)calendarImgs.get(finalI-1).getDrawable()).getBitmap());
                        // 格式化月份为两位数
                        String formattedMonth = String.format("%02d", month);
                        data.setText(year+"."+formattedMonth+"."+ (finalI - dayOfWeek));
                    }else{
                        consShow.setVisibility(View.GONE);
                    }

                    if(chosedDay!= finalI-1){
                        if(chosedDay!=-1)
                            calendarCons.get(chosedDay).setBackgroundResource(R.drawable.radius_border4);
                        cons.setBackgroundResource(R.drawable.radius_border_chosed2);
                        chosedDay=finalI-1;
                    }
                    if(isnew){
                        ShowAndDrop("要添加在这一天吗？","添加",finalI-1);
                    }
                }
            });
            calendarCons.add(cons);
        }
        // 将ImageView添加到列表
        for (int i = 1; i <= 42; i++) {
            int imgId = getResources().getIdentifier("img" + i, "id", getPackageName());
            ImageView img = findViewById(imgId);
            calendarImgs.add(img);
        }
        // 将TextView添加到列表
        for (int i = 1; i <= 42; i++) {
            int textId = getResources().getIdentifier("text" + i, "id", getPackageName());
            TextView text = findViewById(textId);
            calendarTexts.add(text);
        }
        // 获取当前日期
        calendar = Calendar.getInstance();

        ImageView leftBtn = findViewById(R.id.imageView9);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 减少月份
                calendar.add(Calendar.MONTH, -1);

                initcalendar();
            }
        });
        ImageView rightBtn = findViewById(R.id.imageView22);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 增加月份
                calendar.add(Calendar.MONTH, 1);

                initcalendar();
            }
        });
        ImageView deleteBtn =findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDelete("确定要删除吗？");
            }
        });
        initcalendar();
    }
    @SuppressLint("SetTextI18n")
    private void initcalendar()
    {
        matchs=new ArrayList<>();
        consShow.setVisibility(View.GONE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; // 注意要加一，因为月份从0开始
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // 格式化月份为两位数
        String formattedMonth = String.format("%02d", month);
        calendarText.setText(year + "." + formattedMonth);

        calendar.set(Calendar.MONTH, month - 1); // 月份需要减一
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 获取这个月1号是星期几，1代表星期日，2代表星期一，依此类推
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        dayOfWeek--; //表示偏移量
        if(chosedDay!=-1)
            calendarCons.get(chosedDay).setBackgroundResource(R.drawable.radius_border4);
        chosedDay=-1;
        TableRow ext = findViewById(R.id.extraTableRow);
        //ext.setVisibility(View.VISIBLE);
        for(int i = 0;i<=41;i++){
            calendarCons.get(i).setClickable(false);
            calendarTexts.get(i).setText("");
            calendarImgs.get(i).setImageDrawable(null);
        }
        if(daysInMonth+dayOfWeek<=35)
            ext.setVisibility(View.GONE);
        else
            ext.setVisibility(View.VISIBLE);
        for(int i=1;i<=daysInMonth;i++){
            calendarCons.get(i-1+dayOfWeek).setClickable(true);
            TextView tmpText = calendarTexts.get(i-1+dayOfWeek);
            tmpText.setText(Integer.toString(i));
        }

        loadMonth();
    }
    private void loadMonth(){
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                // 构建查询参数
                String baseUrl = uu+"/cloth/schedule";
                String queryParam1 = "year="+year;
                String queryParam2 = "month="+month;
                // 构建完整的 URL
                String fullUrl = baseUrl + "?" + queryParam1 + "&" + queryParam2;
                // 创建请求
                Request.Builder requestBuilder = new Request.Builder()
                        .url(fullUrl)
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
                                JSONArray dataJson = responseJson.getJSONArray("data");
                                dealdataJson(dataJson, new ImageLoadCallback() {
                                    @Override
                                    public void onImageLoaded(Bitmap upImage, Bitmap downImage) {
                                    }
                                });
                                System.out.println(dataJson);
                                break;
                            case 1001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("登录过期，请重新登陆");
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
    private void dealdataJson(JSONArray dataJson, ImageLoadCallback callback) throws JSONException {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        match_size= dataJson.length();
        for (int i = 0; i < dataJson.length(); i++) {
            JSONObject matchJson = dataJson.getJSONObject(i);
            Match tmp=new Match();
            tmp.day = matchJson.getInt("day");
            tmp.month = matchJson.getInt("month");
            tmp.year = matchJson.getInt("year");
            tmp.match_id = matchJson.getString("match_id");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //创建一个OkHttpClient对象
                    OkHttpClient okHttpClient = new OkHttpClient();
                    // 构建查询参数
                    String baseUrl = uu + "/cloth/match-id";
                    String queryParam1 = "id=" + tmp.match_id;
                    // 构建完整的 URL
                    String fullUrl = baseUrl + "?" + queryParam1;
                    // 创建请求
                    Request.Builder requestBuilder = new Request.Builder()
                            .url(fullUrl)
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
                                    System.out.println(dataJson);
//                                    JSONArray sketchArray = dataJson.getJSONArray("sketch");
//                                    String[] sketch = new String[sketchArray.length()];
//                                    for (int k = 0; k < sketchArray.length(); k++) {
//                                        sketch[k] = sketchArray.getString(k);
//                                    }
                                    String up_url = dataJson.getString("up_img_url");
                                    String down_url = dataJson.getString("down_img_url");
                                    tmp.up_url = up_url;
                                    tmp.down_url = down_url;
                                    Glide.with(activity)
                                            .asBitmap()
                                            .load(uuimg + tmp.up_url)
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
                                    Glide.with(activity)
                                            .asBitmap()
                                            .load(uuimg + tmp.down_url)
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
                                    break;
                                case 1001:
                                    System.out.println(sharedPreferencesManager.getUsername());
                                    showRequestFailedDialog("登录过期，请重新登陆");
                                    break;
                                case 2002:
                                    System.out.println("未查询到搭配");
                                    showRequestFailedDialog("未查询到搭配");
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
//
            matchs.add(tmp);
        }
    }
    private void checkAndCallCallback(Match tmp, ImageLoadCallback callback) {
        if (tmp.up_img != null && tmp.down_img != null) {
            // 两张图片都加载完成，调用合并方法
            Bitmap combinedBitmap = combineImages(tmp.up_img, tmp.down_img);
            tmp.comb_img = combinedBitmap;
            // 两张图片都加载完成，调用回调
            callback.onImageLoaded(tmp.up_img, tmp.down_img);
            loadedImageCount++; // 增加已加载图片数量
            System.out.println(loadedImageCount);
            if (loadedImageCount == match_size) {
                loadedImageCount=0;
                // 如果已加载图片数量等于总图片数量，表示所有图片都加载完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Match tmp:matchs){
                            ImageView tmpImg = calendarImgs.get(tmp.day+dayOfWeek-1);
                            tmpImg.setImageBitmap(tmp.comb_img);
                        }
                    }
                });

            }
        }
    }
    private Bitmap combineImages(Bitmap topImage, Bitmap bottomImage) {
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
    // 图片加载完成的回调接口
    public interface ImageLoadCallback {
        void onImageLoaded(Bitmap upImage, Bitmap downImage);
    }
    private void ShowAndDrop(String message,String positivestr,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton(positivestr, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("year", year);
                            json.put("month", month);
                            json.put("day", chosedDay-dayOfWeek+1);
                            json.put("match_id", newmatch.match_id);
                            json.put("user_id", sharedPreferencesManager.getUserID());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(uu + "/cloth/schedule")
                                .post(requestBody)
                                .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                        // 将会话信息添加到请求头部
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
                                                calendarImgs.get(position).setImageBitmap(newmatch.comb_img);
                                                consShow.setVisibility(View.VISIBLE);
                                                ImageView tmp =findViewById(R.id.imageView10);
                                                tmp.setImageBitmap(newmatch.comb_img);
                                                TextView data = findViewById(R.id.textView35);
                                                // 格式化月份为两位数
                                                @SuppressLint("DefaultLocale") String formattedMonth = String.format("%02d", month);
                                                data.setText(year+"."+formattedMonth+"."+ (position+1-dayOfWeek));
                                                ShowMore();
                                            }
                                        });
                                        break;
                                    case 4001:
                                        System.out.println(sharedPreferencesManager.getUsername());
                                        showRequestFailedDialog("登录过期，请先登录");
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
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    private void ShowDelete(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        System.out.println("chosedDay:"+chosedDay);
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(uu + "/cloth/schedule-date?year="+year+"&month="+month+"&day="+(chosedDay-dayOfWeek+1))
                                .delete()
                                .addHeader("cookie", sharedPreferencesManager.getKEY_Session_ID());
                        // 将会话信息添加到请求头部
                        if (sharedPreferencesManager.getKEY_Session_ID() != null) {
                            //showRequestFailedDialog(sharedPreferencesManager.getKEY_Session_ID());
                        } else {
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
                                        String msg = responseJson.getString("msg");
                                        if(msg=="\\u5220\\u9664\\u5931\\u8d25")
                                            showRequestFailedDialog("删除失败");
                                        else{
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("删除成功");
                                                initcalendar();
                                            }
                                        });}
                                        break;
                                    case 4001:
                                        System.out.println(sharedPreferencesManager.getUsername());
                                        showRequestFailedDialog("登录过期，请先登录");
                                        break;
                                    default:
                                        showRequestFailedDialog("删除失败");
                                        break;
                                }
                                System.out.println("Response: " + responseData);
                                // 记得关闭响应体
                                responseBody.close();
                            } else {
                                // 请求失败，处理错误
                                System.out.println("Request failed");
                                showRequestFailedDialog("网络错误，删除失败");
                            }
                        } catch (IOException e) {
                            showRequestFailedDialog("网络错误，删除失败");
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    private void ShowMore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("添加成功，还要将该搭配添加在其他日程中吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isnew=false;
            }
        });
        builder.show();
    }
}