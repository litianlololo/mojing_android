package com.example.mojing.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

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

import com.example.mojing.Dapei_SetTags_Activity;
import com.example.mojing.Dapei_Tag_Activity;
import com.example.mojing.LoginActivity;
import com.example.mojing.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_dapei#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_dapei extends Fragment {
    private ImageButton ImgBtn_1;
    private ImageButton ImgBtn_2;
    private ImageButton imageButton;
    private ImageButton downloadBtn;
    private Button shareBtn;
    private Bitmap combinedBitmap;
    private Boolean iscombined = false;
    private Button TagBtn;
    private String fileName;

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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton imgButton = (ImageButton) getActivity().findViewById(R.id.img_button);
        EditText score_edit = (EditText) getActivity().findViewById(R.id.score_edit);
        score_edit.setText("init");
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里编写单击事件的逻辑
                String TAG = "Main";
                score_edit.setText("dianji");
                Intent intent = new Intent(getActivity(), Dapei_Tag_Activity.class);
                startActivity(intent);
            }
        });

        ImgBtn_1 = getActivity().findViewById(R.id.ImgBtn_1);
        ImgBtn_2 = getActivity().findViewById(R.id.ImgBtn_2);
        downloadBtn = getActivity().findViewById(R.id.download);
        shareBtn = getActivity().findViewById(R.id.shareBtn);
        TagBtn = getActivity().findViewById(R.id.TagBtn);
        TagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Dapei_SetTags_Activity.class);
                startActivity(intent);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iscombined) {
                    showRequestFailedDialog("请先保存您的搭配");
                    return;
                } else {
                    // 分享已合并的图片
                }
            }
        });
        ImgBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(ImgBtn_1);
                imageButton = getActivity().findViewById(R.id.ImgBtn_1);
            }
        });

        ImgBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(ImgBtn_2);
                imageButton = getActivity().findViewById(R.id.ImgBtn_2);
            }
        });
    }

    private void openGallery(ImageButton imageButton) {
        // 在这里处理打开手机图库并选择图片的逻辑
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
        imageButton.setPressed(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, imageButton.getWidth(), imageButton.getHeight(), false);
                imageButton.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestCode == 1 && data != null) {
                    Uri selectedImageUri = data.getData();

                    try {
                        Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, imageButton.getWidth(), imageButton.getHeight(), false);

                        // Assuming both images have the same width
                        combinedBitmap = combineImagesVertically(((BitmapDrawable) ImgBtn_1.getDrawable()).getBitmap(), ((BitmapDrawable) ImgBtn_2.getDrawable()).getBitmap());
                        saveImageToGallery(combinedBitmap);
                        iscombined = true;
                        showRequestFailedDialog("保存成功，已同时保存到手机相册");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Function to concatenate two images vertically
    private Bitmap combineImagesVertically(Bitmap topImage, Bitmap bottomImage) {
        int width = topImage.getWidth();
        int height = topImage.getHeight() + bottomImage.getHeight();

        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        canvas.drawBitmap(topImage, 0, 0, null);
        canvas.drawBitmap(bottomImage, 0, topImage.getHeight(), null);

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
}