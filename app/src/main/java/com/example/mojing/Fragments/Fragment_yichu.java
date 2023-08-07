package com.example.mojing.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mojing.Danpin;
import com.example.mojing.MainActivity;
import com.example.mojing.SharedPreferencesManager;
import com.example.mojing.Yichu_Add_Activity;
import com.example.mojing.R;
import com.example.mojing.Yichu_Single_Activity;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import kotlin.text.Regex;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Fragment_yichu extends Fragment {
    //public String uu = "http://47.102.43.156:8007/api";
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private ImageView SImg,XImg,LImg;
    private TextView SText,XText,LText;
    private SharedPreferencesManager sharedPreferencesManager;
    private String type1,type2;
    MainActivity activity;

    private List<String> shangzhuang = new ArrayList<>();
    private List<String> xiazhuang = new ArrayList<>();
    private List<String> lianshenzhuang = new ArrayList<>();
    private LinearLayout Btnlayout;
    private int desiredWidth;
    private ScrollView ImgScroll;
//    private List<String> selectedURL = new ArrayList<>();
    private Danpin[] danpins = new Danpin[1];
    private List<Danpin> selectedDanpin = new ArrayList<>();
    public Fragment_yichu() {
        // Required empty public constructor
    }
    public static Fragment_yichu newInstance(String param1, String param2) {
        Fragment_yichu fragment = new Fragment_yichu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_yichu, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (MainActivity) getActivity();
        assert activity != null;
        sharedPreferencesManager = activity.getSharedPreferencesManager();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Step 2: Get the width of scrollView3
        Btnlayout = activity.findViewById(R.id.Btnlayout);
        int BtnlayoutWidth = Btnlayout.getWidth();

        // Step 3: Calculate the desired width for ImgScroll
        desiredWidth = screenWidth - dpToPx(95);
        System.out.println("BtnlayoutWidth: "+BtnlayoutWidth);
        // Step 4: Set the calculated width to ImgScroll
        ImgScroll = activity.findViewById(R.id.ImgScroll);
        ViewGroup.LayoutParams layoutParams = ImgScroll.getLayoutParams();
        layoutParams.width = desiredWidth;
        ImgScroll.setLayoutParams(layoutParams);

        SImg = getActivity().findViewById(R.id.SImg);
        XImg = getActivity().findViewById(R.id.XImg);
        LImg = getActivity().findViewById(R.id.LImg);
        SText =getActivity().findViewById(R.id.SText);
        XText =getActivity().findViewById(R.id.XText);
        LText =getActivity().findViewById(R.id.LText);
        Btnlayout =activity.findViewById(R.id.Btnlayout);
        ImgScroll = activity.findViewById(R.id.ImgScroll);
        shangzhuang = new ArrayList<>();
        xiazhuang = new ArrayList<>();
        lianshenzhuang = new ArrayList<>();
        shangzhuang.add("全部");
        shangzhuang.add("T恤");
        shangzhuang.add("大衣");
        shangzhuang.add("衬衫");
        shangzhuang.add("西装");
        shangzhuang.add("开衫");
        shangzhuang.add("棒球服");
        shangzhuang.add("卫衣");
        shangzhuang.add("夹克");
        shangzhuang.add("斗篷/披风");
        shangzhuang.add("毛衣针织");
        shangzhuang.add("背心/吊带");
        shangzhuang.add("皮衣/皮革");
        shangzhuang.add("羽绒服");
        shangzhuang.add("棉衣/羊羔绒");
        shangzhuang.add("风衣");
        shangzhuang.add("POLO衫");
        shangzhuang.add("牛仔外套");

        xiazhuang.add("全部");
        xiazhuang.add("打底裤");
        xiazhuang.add("休闲裤");
        xiazhuang.add("运动裤");
        xiazhuang.add("牛仔裤");
        xiazhuang.add("半身裙");
        xiazhuang.add("其他裤子");

        lianshenzhuang.add("全部");
        lianshenzhuang.add("连衣裙");
        lianshenzhuang.add("连身裤");
        AddShangzhuangTextView();
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//        yichuList.add(getUriFromDrawableResource(R.drawable.dapei_tmp));
//
//
        //默认type1 type2
        type1="上装";
        type2="全部";
        System.out.println(sharedPreferencesManager.isLoggedIn());
        if(sharedPreferencesManager.isLoggedIn()) {
            //初始化我的衣装
            loadAll(new AddDanpinCallback() {
                @Override
                public void onAddDanpin() {
                    //初始化为 上装 全部
                    loadingImg(type1, type2);
//                System.out.println(selectedURL);
                }
            }, new ImageLoadingCallback() {
                @Override
                public void onImagesLoaded(List<Danpin> urls) {
                    // 当图片加载完成时，这个方法会被调用
                    // 使用加载完的'urls'生成图片布局
                    generateImageLayout(urls);
                }
            });
        }
        ImageView add_icon =getActivity().findViewById(R.id.add_icon);
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), Yichu_Add_Activity.class);
                startActivity(tmp);
                activity.finish();
            }
        });
        View.OnClickListener SClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(type1!="上装"){
                    type1="上装";
                    SImg.setImageResource(R.drawable.navbar_icon_home_press);
                    //SText.setTextColor(R.color.black);

                    XImg.setImageResource(R.drawable.navbar_icon_home_default_1);
                    //XText.setTextColor(R.color.defaultText);
                    LImg.setImageResource(R.drawable.navbar_icon_home_default_2);
                    //LText.setTextColor(R.color.defaultText);

                    AddShangzhuangTextView();
                    type2 = "全部";
                    //重新加载图片
                    if(!sharedPreferencesManager.isLoggedIn())
                        return;
                    loadingImg(type1,type2);
                    generateImageLayout(selectedDanpin);
                }
            }
        };
        SImg.setOnClickListener(SClick);
        SText.setOnClickListener(SClick);

        View.OnClickListener XClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                if(type1!="下装"){
                    type1="下装";
                    XImg.setImageResource(R.drawable.navbar_icon_home_press_1);
                    //XText.setTextColor(R.color.black);

                    SImg.setImageResource(R.drawable.navbar_icon_home_default);
                   // SText.setTextColor(R.color.defaultText);
                    LImg.setImageResource(R.drawable.navbar_icon_home_default_2);
                    //LText.setTextColor(R.color.defaultText);

                    AddTextView(xiazhuang);
                    type2 = "全部";
                    //重新加载图片
                    if(!sharedPreferencesManager.isLoggedIn())
                        return;
                    loadingImg(type1,type2);
                    generateImageLayout(selectedDanpin);
                }
            }
        };
        XImg.setOnClickListener(XClick);
        XText.setOnClickListener(XClick);

        View.OnClickListener LClick = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(type1!="连身装"){
                    type1="连身装";
                    LImg.setImageResource(R.drawable.navbar_icon_home_press_2);
                    //LText.setTextColor(R.color.black);

                    SImg.setImageResource(R.drawable.navbar_icon_home_default);
                    //SText.setTextColor(R.color.defaultText);
                    XImg.setImageResource(R.drawable.navbar_icon_home_default_1);
                    //XText.setTextColor(R.color.defaultText);

                    AddTextView(lianshenzhuang);
                    type2 = "全部";
                    //重新加载图片
                    if(!sharedPreferencesManager.isLoggedIn())
                        return;
                    loadingImg(type1,type2);
                    generateImageLayout(selectedDanpin);
                }
            }
        };
        LImg.setOnClickListener(LClick);
        LText.setOnClickListener(LClick);
    }
    public void AddShangzhuangTextView()
    {

        Btnlayout.removeAllViews();
        List<TextView> textViews = new ArrayList<>();
        // 使用foreach循环遍历shangzhuang列表
        for (String str : shangzhuang) {

            // 创建TextView并设置文本内容
            TextView textView = new TextView(activity);
            textView.setText(str);
            // 设置文本居中
            textView.setGravity(Gravity.CENTER);
            // 设置布局参数
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    0,
//                    1
//            );
            // 设置布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            textView.setLayoutParams(layoutParams);
            // 设置背景选择器
            textView.setBackgroundResource(R.drawable.textview_background);
            // 添加TextView到列表中
            textViews.add(textView);
            // 设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type2= str;
                    // 遍历所有TextView，设置背景色
                    for (TextView tv : textViews) {
                        if (tv == view) {
                            // 当前点击的TextView，设置为白色背景
                            tv.setBackgroundResource(android.R.color.white);
                        } else {
                            // 其他TextView，设置为36BFBEBE背景
                            tv.setBackgroundResource(R.drawable.textview_background);
                        }
                    }
                    //重新加载图片
                    if(!sharedPreferencesManager.isLoggedIn())
                        return;
                    loadingImg(type1,type2);
                    generateImageLayout(selectedDanpin);
                }
            });
            // 将TextView添加到Btnlayout中
            Btnlayout.addView(textView);
        }
        textViews.get(0).setBackgroundResource(android.R.color.white);
    }
    public void AddTextView(List<String> s)
    {
        Btnlayout.removeAllViews();
        List<TextView> textViews = new ArrayList<>();
        // 使用foreach循环遍历shangzhuang列表
        for (String str : s) {
            // 创建TextView并设置文本内容
            TextView textView = new TextView(activity);
            textView.setText(str);
            // 设置文本居中
            textView.setGravity(Gravity.CENTER);
            // 设置布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            textView.setLayoutParams(layoutParams);
            // 设置背景选择器
            textView.setBackgroundResource(R.drawable.textview_background);
            // 添加TextView到列表中
            textViews.add(textView);
            // 设置点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type2= str;
                    // 遍历所有TextView，设置背景色
                    for (TextView tv : textViews) {
                        if (tv == view) {
                            // 当前点击的TextView，设置为白色背景
                            tv.setBackgroundResource(android.R.color.white);
                        } else {
                            // 其他TextView，设置为36BFBEBE背景
                            tv.setBackgroundResource(R.drawable.textview_background);
                        }
                    }
                    //重新加载图片
                    loadingImg(type1,type2);
                    generateImageLayout(selectedDanpin);
                }
            });
            // 将TextView添加到Btnlayout中
            Btnlayout.addView(textView);
        }
        textViews.get(0).setBackgroundResource(android.R.color.white);
    }
    private void generateImageLayout(List<Danpin> urls) {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        // 创建一个垂直线性布局用于放置两列图片的容器
        LinearLayout containerLayout = new LinearLayout(activity);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.setMargins(10, 10, 10, 10); // 设置容器与 ScrollView 边界的间隔
        containerLayout.setLayoutParams(containerParams);
//
        // 分两列添加图片
        for (int i = 0; i < urls.size(); i += 2) {
            // 创建一个水平线性布局用于放置一行的两张图片
            LinearLayout rowLayout = new LinearLayout(activity);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, 10); // 设置行之间的间隔
            rowLayout.setLayoutParams(rowParams);

            // 第一列图片
            ImageView imageView1 = new ImageView(activity);
            int finalI = i;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(activity)
                            .load(uuimg + urls.get(finalI).img_url)
                            //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                            //.error(R.drawable.error) // Error image (optional)
                            .into(imageView1);
                }
            });
            LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
//            LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
//                    dpToPx(140), // 设置图片宽度为200dp
//                    dpToPx(140)  // 设置图片高度为200dp
//            );
            imageParams1.setMarginEnd(5); // 设置图片与第二列图片的间隔
            imageView1.setLayoutParams(imageParams1);
            imageView1.setBackgroundResource(R.drawable.yichu_img_border);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 创建Intent，将id传递给Yichu_Single_Activity
                    Intent intent = new Intent(activity, Yichu_Single_Activity.class);
                    intent.putExtra("_id", urls.get(finalI)._id); // 传递id变量

                    // 启动Yichu_Single_Activity
                    activity.startActivity(intent);
                }
            });
            // 第二列图片
            if (i + 1 < urls.size()) {
                ImageView imageView2 = new ImageView(activity);
                int finalI1 = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(activity)
                                .load(uuimg + urls.get(finalI1 + 1).img_url) // 这里假设获取的图片URL在集合的第一个位置
                                .into(imageView2);
                    }
                });
                LinearLayout.LayoutParams imageParams2 = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
//                LinearLayout.LayoutParams imageParams2 = new LinearLayout.LayoutParams(
//                        dpToPx(140), // 设置图片宽度为100dp
//                        dpToPx(140)  // 设置图片高度为100dp
//                );
                imageView2.setLayoutParams(imageParams2);
                imageView2.setBackgroundResource(R.drawable.yichu_img_border);
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 创建Intent，将id传递给Yichu_Single_Activity
                        Intent intent = new Intent(activity, Yichu_Single_Activity.class);
                        intent.putExtra("_id", urls.get(finalI+1)._id); // 传递id变量
                        // 启动Yichu_Single_Activity
                        activity.startActivity(intent);
                    }
                });
                // 将两列图片添加到行布局中
                rowLayout.addView(imageView1);
                rowLayout.addView(imageView2);
            } else {
                // 如果剩余图片不足两张，则只添加第一列图片
                // 将第一列图片的宽度设置为占据一半的屏幕宽度
                imageParams1.width = desiredWidth / 2; // Set width to half of the screen width
                imageView1.setLayoutParams(imageParams1);

                // Add an empty view to fill the second column's space
                View emptyView = new View(activity);
                LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                        desiredWidth / 2,
                        0
                );
                emptyView.setLayoutParams(emptyParams);

                // 将第一列图片和空的View添加到行布局中
                rowLayout.addView(imageView1);
                rowLayout.addView(emptyView);
            }
            // 将行布局添加到容器中
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
    private void loadAll(AddDanpinCallback addDanpincallback, ImageLoadingCallback imageLoadingCallback)
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
    private void loadingImg(String type1,String type2)
    {
        if(!sharedPreferencesManager.isLoggedIn())
            return;
//        selectedURL = new ArrayList<>();
        selectedDanpin = new ArrayList<>();
        System.out.println("衣装总数： "+danpins.length);
        for(Danpin tmp:danpins){
            if(!type2.equals("全部")){
                //筛选类型
                if(tmp.type.equals(type1) && tmp.type2.equals(type2)){
//                    selectedURL.add(tmp.img_url);
                    selectedDanpin.add(tmp);
                }
            }else{
                //筛选类型
                if(tmp.type.equals(type1)){
//                    selectedURL.add(tmp.img_url);
                    selectedDanpin.add(tmp);
                }
            }
        }
    }
    //回调接口，下载完全部衣装后继续操作
    public interface AddDanpinCallback {
        void onAddDanpin();
    }
    // 图片加载完成的回调接口
    public interface ImageLoadingCallback {
        void onImagesLoaded(List<Danpin> urls);
    }
}