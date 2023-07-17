package com.example.mojing.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mojing.Dapei_Tag_Activity;
import com.example.mojing.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_dapei#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_dapei extends Fragment {
    private ImageButton ImgBtn_1;
    private ImageButton ImgBtn_2;
    private ImageButton imageButton;
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
                String TAG="Main";
                score_edit.setText("dianji");
                Intent intent = new Intent(getActivity(), Dapei_Tag_Activity.class);
                startActivity(intent);
            }
        });

        ImgBtn_1 = getActivity().findViewById(R.id.ImgBtn_1);
        ImgBtn_2 = getActivity().findViewById(R.id.ImgBtn_2);
        ImgBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(ImgBtn_1);
                imageButton= getActivity().findViewById(R.id.ImgBtn_1);
            }
        });

        ImgBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(ImgBtn_2);
                imageButton= getActivity().findViewById(R.id.ImgBtn_2);
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
    }
}
