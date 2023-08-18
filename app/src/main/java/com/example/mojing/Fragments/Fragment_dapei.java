package com.example.mojing.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mojing.Danpin;
import com.example.mojing.Dapei;
import com.example.mojing.Dapei_Album_Activity;
import com.example.mojing.Dapei_ChoseDown_Activity;
import com.example.mojing.Dapei_ChoseUp_Activity;
import com.example.mojing.Dapei_SetTags_Activity;
import com.example.mojing.Dapei_Tag_Activity;
import com.example.mojing.LoginActivity;
import com.example.mojing.MainActivity;
import com.example.mojing.R;
import com.example.mojing.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_dapei#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_dapei extends Fragment {
    public String uu="http://47.103.223.106:5004/api";
    public String uuimg="http://47.103.223.106:5004";
    private ImageButton ImgBtn_1;
    private ImageButton ImgBtn_2;
    private ImageButton imageButton;
    private ImageView downloadBtn;
    private Button shareBtn;
    private Bitmap combinedBitmap;
    private Boolean iscombined = false;
    private Button TagBtn;
    private TextView changjingText;
    private String fileName;
    private SharedPreferencesManager sharedPreferencesManager;
    private BottomSheetDialog bottomSheetDialog;
    private Boolean[] isView;
    private View[] changjingView;
    private Drawable radius_border1,radius_chosed;
    private static int changjingCNT=5;
    MainActivity activity;
    private boolean Automode;
    private TextView AutoBtn,PerBtn;
    private ColorStateList backgroundColor;
    private ImageView refreshBtn;
    private Danpin up,down;
    private Dapei dapei= new Dapei();
    private String origin;
    private boolean[] changjingChosed = new boolean[5];
    private boolean changed1,changed2;
    public Fragment_dapei() {
        // Required empty public constructor
    }

    public static Fragment_dapei newInstance(String param1, String param2) {
        Fragment_dapei fragment = new Fragment_dapei();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dapei, container, false);
        return view;
    }

    //在fragment不能直接进行点击事件，需要放到oncreatActivity中,重载该函数即可
    @SuppressLint({"UseCompatLoadingForDrawables", "CutPasteId"})
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton imgButton = (ImageButton) getActivity().findViewById(R.id.img_button);
        activity = (MainActivity) getActivity();
        assert activity != null;
        sharedPreferencesManager = activity.getSharedPreferencesManager();
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                String TAG = "Main";
                Intent intent = new Intent(getActivity(), Dapei_Album_Activity.class);
                startActivity(intent);
            }
        });
        Automode = true;
        ImgBtn_1 = getActivity().findViewById(R.id.ImgBtn_1);
        ImgBtn_2 = getActivity().findViewById(R.id.ImgBtn_2);
        downloadBtn = getActivity().findViewById(R.id.download);
        changjingText = getActivity().findViewById(R.id.changjingBtn);
        AutoBtn=getActivity().findViewById(R.id.AutoBtn);
        PerBtn=getActivity().findViewById(R.id.PerBtn);
        backgroundColor= ColorStateList.valueOf(Color.parseColor("#36BFBEBE"));
        refreshBtn = getActivity().findViewById(R.id.refreshBtn);
        up=new Danpin(); down = new Danpin();
        //刷新 加载新的AI搭配
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sharedPreferencesManager.isLoggedIn())
                    return;
                GetAI_match(new Deal_ai_matchCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void deal_AI_match() {
                        TextView scoreText = activity.findViewById(R.id.scoreText);
                        scoreText.setText(Integer.toString(dapei.AI_Score));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(activity)
                                        .load(uuimg + dapei.up.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_1);
                                Glide.with(activity)
                                        .load(uuimg + dapei.down.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_2);

                            }
                        });
                    }
                });
            }
        });
        AutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Automode){
                    //修改按钮样式
                    //AutoBtn.setBackground(radius_chosed);/#36BFBEBE
                    AutoBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    AutoBtn.setTextColor(getResources().getColor(R.color.white,null));
                    //PerBtn.setBackground(radius_chosed);
                    PerBtn.setBackgroundTintList(backgroundColor);
                    PerBtn.setTextColor(getResources().getColor(R.color.black,null));
                    //逻辑切换为自动模式
                    //设置上下装按钮禁用12211111
                    ImgBtn_1.setClickable(false);
                    ImgBtn_2.setClickable(false);
                    ImgBtn_1.setImageResource(R.drawable.image_jacket);
                    ImgBtn_2.setImageResource(R.drawable.image_trousers);
                    dapei = new Dapei();
                    refreshBtn.setVisibility(View.VISIBLE);
                    Automode=true;
                }
            }
        });
        PerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Automode){
                    //修改按钮样式
                    //PerBtn.setBackground(radius_chosed);
                    PerBtn.setTextColor(getResources().getColor(R.color.white,null));
                    PerBtn.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    //AutoBtn.setBackground(radius_chosed);
                    AutoBtn.setBackgroundTintList(backgroundColor);
                    AutoBtn.setTextColor(getResources().getColor(R.color.black,null));
                    //逻辑切换为个人模式
                    //设置上下装按钮可用
                    ImgBtn_1.setClickable(true);
                    ImgBtn_2.setClickable(true);
                    ImgBtn_1.setImageResource(R.drawable.image_jacket);
                    ImgBtn_2.setImageResource(R.drawable.image_trousers);
                    refreshBtn.setVisibility(View.GONE);
                    dapei = new Dapei();
                    TextView scoreText = activity.findViewById(R.id.scoreText);
                    scoreText.setText("-");
                    Automode=false;
                }
            }
        });
        changjingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet();
            }
        });
        ImgBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Dapei_ChoseUp_Activity.class);
                activityLauncher_up.launch(intent);
            }
        });

        ImgBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Dapei_ChoseDown_Activity.class);
                activityLauncher_down.launch(intent);
            }
        });
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("up_cloth",up._id);
                            json.put("down_cloth",down._id);
                            json.put("up_img_url",up.img_url);
                            json.put("down_img_url",down.img_url);
                            System.out.println("up_cloth "+up._id);
                            System.out.println("down_cloth "+down._id);
                            if(Automode) origin="ai"; else origin="self";
                            json.put("origin",origin);
//                            Bitmap tmp1,tmp2,tmp0;
//                            tmp1 = ((BitmapDrawable) ImgBtn_1.getDrawable()).getBitmap();
//                            tmp2 =((BitmapDrawable) ImgBtn_2.getDrawable()).getBitmap();
//                            tmp0 = combineImages(tmp1,tmp2);
                            JSONArray sketchArray = new JSONArray();
                            json.put("sketch",sketchArray);
                            JSONArray sceneArray = new JSONArray(); int i=0;
                            if(changjingChosed[i++]) sceneArray.put("工作");
                            if(changjingChosed[i++]) sceneArray.put("休闲");
                            if(changjingChosed[i++]) sceneArray.put("运动");
                            if(changjingChosed[i++]) sceneArray.put("旅行");
                            if(changjingChosed[i++]) sceneArray.put("约会");

                            json.put("scene",sceneArray);
                            json.put("temperature",null);
                            json.put("user_id",sharedPreferencesManager.getUserID());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                        // 创建请求
                        Request.Builder requestBuilder = new Request.Builder()
                                .url(uu+"/cloth/add-match")
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
                                System.out.println(responseData);
                                //确定返回状态
                                switch (code) {
                                    case 200:
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showRequestFailedDialog("添加搭配成功");
                                            }
                                        });
                                        JSONObject dataJson = responseJson.getJSONObject("data");
                                        System.out.println(dataJson);
                                        dapei._id = dataJson.getString("_id");
                                        break;
                                    case 1001:
                                        System.out.println(sharedPreferencesManager.getUsername());
                                        showRequestFailedDialog("请先登录");
                                        break;
                                    case 2002:
                                        System.out.println(sharedPreferencesManager.getUsername());
                                        showRequestFailedDialog("搭配衣装不存在");
                                        break;
                                    default:
                                        showRequestFailedDialog("添加搭配失败");
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
                        // Assuming both images have the same width
                        //combinedBitmap = combineImagesVertically(((BitmapDrawable) ImgBtn_1.getDrawable()).getBitmap(), ((BitmapDrawable) ImgBtn_2.getDrawable()).getBitmap());
                        //saveImageToGallery(combinedBitmap);
                        //iscombined = true;
                        //showRequestFailedDialog("保存成功，已同时保存到手机相册");
                }
        });
        ImgBtn_1.setClickable(false);
        ImgBtn_2.setClickable(false);
    }
    private ActivityResultLauncher<Intent> activityLauncher_up = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("_id")){
                        up._id=data.getStringExtra("_id");
                    }
                    System.out.println("up._id= "+up._id);
                    if (data != null && data.hasExtra("img_url")) {
                        up.img_url = data.getStringExtra("img_url");
                        // 在这里处理收到的数据
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(activity)
                                        .load(uuimg + up.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_1);
                            }
                        });
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> activityLauncher_down = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 2) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("_id")){
                        down._id=data.getStringExtra("_id");
                }
                    if (data != null && data.hasExtra("img_url")) {
                        down.img_url = data.getStringExtra("img_url");
                        // 在这里处理收到的数据
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(activity)
                                        .load(uuimg + down.img_url)
                                        //.placeholder(R.drawable.placeholder_image) // Placeholder image (optional)
                                        .error(R.drawable.error) // Error image (optional)
                                        .into(ImgBtn_2);
                            }
                        });
                    }
                }
            }
    );
    //有错位效果
    private Bitmap combineImagesVertically(Bitmap topImage, Bitmap bottomImage) {

        int combinedWidth = Math.max(topImage.getWidth(), bottomImage.getWidth());
        int combinedHeight = topImage.getHeight() + bottomImage.getHeight();

        // Create a new blank bitmap with white background
        Bitmap combinedBitmap = Bitmap.createBitmap(combinedWidth, combinedHeight, Bitmap.Config.ARGB_8888);
        combinedBitmap.eraseColor(Color.WHITE);

        // Calculate the position to place the topImage on the bottomImage
        int offsetX =  topImage.getWidth() / 8;
        int offsetY = 3*topImage.getHeight() / 4;

        // Create a canvas with the combined bitmap
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the bottomImage first
        canvas.drawBitmap(bottomImage, offsetX, offsetY+20, null);

        // Draw the topImage on the bottomImage at the calculated position
        canvas.drawBitmap(topImage, 0, 20, null);

        return combinedBitmap;
    }

    // Function to save the image to the gallery
    private void saveImageToGallery(Bitmap bitmap) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        fileName = System.currentTimeMillis() + ".jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.WIDTH, bitmap.getWidth());
        values.put(MediaStore.Images.Media.HEIGHT, bitmap.getHeight());

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outputStream = contentResolver.openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showRequestFailedDialog(String str) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("注意")
                            .setMessage(str)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Positive button click listener (if needed)
                                    // You can add code here to handle the click event
                                }
                            })
                            .show();
                }
            });
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void BottomSheet(){
        //创建布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dapei_changjing, null, false);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        //设置布局
        bottomSheetDialog.setContentView(view);
        changjingView=new View[changjingCNT];
        isView=new Boolean[5];
        Arrays.fill(isView, Boolean.FALSE);
        changjingView[0]=view.findViewById(R.id.workView);
        changjingView[1]=view.findViewById(R.id.travelView);
        changjingView[2] =view.findViewById(R.id.dateView);
        changjingView[3]=view.findViewById(R.id.leisureView);
        changjingView[4]=view.findViewById(R.id.sportsView);
        radius_border1 = getResources().getDrawable(R.drawable.radius_border1,null);
        radius_chosed = getResources().getDrawable(R.drawable.radius_border_chosed,null);
        for(String str:dapei.scene){
            switch (str){
                case "工作":
                    changjingChosed[0]=true;
                    break;
                case "休闲":
                    changjingChosed[1]=true;
                    break;
                case "运动":
                    changjingChosed[2]=true;
                    break;
                case "旅行":
                    changjingChosed[3]=true;
                    break;
                case "约会":
                    changjingChosed[4]=true;
                    break;
            }
        }
        for (int tmp = 0; tmp < changjingCNT; tmp++) {
            int finalTmp = tmp;
            if(changjingChosed[finalTmp])
                changjingView[finalTmp].setBackground(radius_chosed);
            changjingView[tmp].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isView[finalTmp]) {
                        //该场景未被选中
                        changjingChosed[finalTmp]=true;
                        changjingView[finalTmp].setBackground(radius_chosed);
                    } else {
                        //该场景已被选中
                        changjingChosed[finalTmp]=false;
                        changjingView[finalTmp].setBackground(radius_border1);
                    }
                    isView[finalTmp] = !isView[finalTmp];
                }
            });
        }

        bottomSheetDialog.show();
    }
    private void GetAI_match(Deal_ai_matchCallback deal_ai_matchCallback){
        if(!sharedPreferencesManager.isLoggedIn())
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                // 创建请求
                Request.Builder requestBuilder = new Request.Builder()
                        .url(uu+"/cloth/ai-match")
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
                        System.out.println("responseJson : "+responseJson);
                        //确定返回状态
                        switch (code) {
                            case 200:
                                JSONArray dataJson = responseJson.getJSONArray("data");
                                deal_AI_match(dataJson);
                                deal_ai_matchCallback.deal_AI_match();
                                System.out.println("dataJson : "+dataJson);
                                break;
                            case 4001:
                                System.out.println(sharedPreferencesManager.getUsername());
                                showRequestFailedDialog("登录过期，请重新登录");
                                break;
                            default:
                                showRequestFailedDialog("加载失败");
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
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageLoadingCallback.onImagesLoaded(selectedDanpin);
//                    }
//                });
            }
        }).start();
    }
    //将返回的AI搭配加入到up down中
    private void deal_AI_match(JSONArray dataJson) throws JSONException {
        if (!sharedPreferencesManager.isLoggedIn())
            return;
        up = new Danpin();
        down = new Danpin();
        dapei = new Dapei();
        if (dataJson.length() == 0) return;
        JSONObject clothObject = dataJson.getJSONObject(0).getJSONObject("up");
        // Now you can access the properties of each cloth object
        String _id = clothObject.has("_id") ? clothObject.getString("_id") : "";
        String img_url = clothObject.has("img_url") ? clothObject.getString("img_url") : "";
        String type = clothObject.has("type") ? clothObject.getString("type") : "";
        String type2 = clothObject.has("type2") ? clothObject.getString("type2") : "";
        String bihe = clothObject.has("bihe") ? clothObject.getString("bihe") : "";
        String fengge = clothObject.has("fengge") ? clothObject.getString("fengge") : "";
        String lingxing = clothObject.has("lingxing") ? clothObject.getString("lingxing") : "";
        String mianliao = clothObject.has("mianliao") ? clothObject.getString("mianliao") : "";
        String xiuchang = clothObject.has("xiuchang") ? clothObject.getString("xiuchang") : "";
        String storeplace = clothObject.has("storeplace") ? clothObject.getString("storeplace") : "";
        boolean spring = clothObject.optBoolean("spring", false);
        boolean summer = clothObject.optBoolean("summer", false);
        boolean autumn = clothObject.optBoolean("autumn", false);
        boolean winter = clothObject.optBoolean("winter", false);

        Danpin tmp = new Danpin();
        tmp._id = _id;
        tmp.season.spring = spring;
        tmp.season.summer = summer;
        tmp.season.autumn = autumn;
        tmp.season.winter = winter;
        tmp.img_url = img_url;
        tmp.type = convertUnicodeToChinese(type);
        tmp.type2 = convertUnicodeToChinese(type2);
        tmp.bihe = bihe;
        tmp.fengge = fengge;
        tmp.lingxing = lingxing;
        tmp.mianliao = mianliao;
        tmp.xiuchang = xiuchang;
        tmp.storeplace = storeplace;
        System.out.println(img_url);
        up = tmp;

        clothObject = dataJson.getJSONObject(0).getJSONObject("down");
        // Now you can access the properties of each cloth object
        _id = clothObject.has("_id") ? clothObject.getString("_id") : "";
        img_url = clothObject.has("img_url") ? clothObject.getString("img_url") : "";
        type = clothObject.has("type") ? clothObject.getString("type") : "";
        type2 = clothObject.has("type2") ? clothObject.getString("type2") : "";
        fengge = clothObject.has("fengge") ? clothObject.getString("fengge") : "";
        mianliao = clothObject.has("mianliao") ? clothObject.getString("mianliao") : "";
        storeplace = clothObject.has("storeplace") ? clothObject.getString("storeplace") : "";
        String shenchang = clothObject.has("shenchang") ? clothObject.getString("shenchang") : "";
        spring = clothObject.optBoolean("spring", false);
        summer = clothObject.optBoolean("summer", false);
        autumn = clothObject.optBoolean("autumn", false);
        winter = clothObject.optBoolean("winter", false);

        tmp = new Danpin();
        tmp._id = _id;
        tmp.season.spring = spring;
        tmp.season.summer = summer;
        tmp.season.autumn = autumn;
        tmp.season.winter = winter;
        tmp.img_url = img_url;
        tmp.type = convertUnicodeToChinese(type);
        tmp.type2 = convertUnicodeToChinese(type2);
        tmp.fengge = fengge;
        tmp.mianliao = mianliao;
        tmp.storeplace = storeplace;
        tmp.shenchang = shenchang;
        System.out.println(img_url);
        down = tmp;

        int Ai_score =  dataJson.getJSONObject(0).getInt("score");
        JSONArray sceneArray  = dataJson.getJSONObject(0).getJSONArray("scene");
        // 将"scene"数组中的值添加到List<String>中
        List<String> sceneList = new ArrayList<>();
        for (int i = 0; i < sceneArray.length(); i++) {
            sceneList.add(convertUnicodeToChinese(sceneArray.getString(i)));
        }
        dapei.up =up;
        dapei.down = down;
        dapei.AI_Score = Ai_score;
        dapei.scene = sceneList;
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
    public interface Deal_ai_matchCallback {
        void deal_AI_match();
    }
}