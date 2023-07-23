package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class InitSanweiActivity extends AppCompatActivity {
    public static final int  XIONGWEI = 80,XIONGWEIMAX=160,XIONGWEIMIN=50;
    public static final int  YAOWEI = 56,YAOWEIMAX=160,YAOWEIMIN=30;
    public static final int  TUNWEI = 85,TUNWEIMAX=160,TUNWEIMIN=50;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView skip;
    private Button nextBtn;
    private SeekBar xiongweiBar,yaoweiBar,tunweiBar;
    private TextView xiongweiText,yaoweiText,tunweiText;
    private int xiongwei=XIONGWEI,yaowei=YAOWEI,tunwei=TUNWEI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_sanwei);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        
        skip = findViewById(R.id.skip);
        nextBtn=findViewById(R.id.nextBtn);
        xiongweiBar = findViewById(R.id.xiongweiBar);
        yaoweiBar = findViewById(R.id.yaoweiBar);
        tunweiBar = findViewById(R.id.tunweiBar);
        
        xiongweiText = findViewById(R.id.xiongwei);
        yaoweiText = findViewById(R.id.yaowei);
        tunweiText = findViewById(R.id.tunwei);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesManager.setIsYouke(true);
                Intent tmp = new Intent(InitSanweiActivity.this, MainActivity.class);
                startActivity(tmp);
                finish();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesManager.setFigureXiongwei(Integer.toString(xiongwei));
                sharedPreferencesManager.setFigureYaowei(Integer.toString(yaowei));
                sharedPreferencesManager.setFigureTunwei(Integer.toString(tunwei));

                Intent tmp = new Intent(InitSanweiActivity.this, MainActivity.class);
                startActivity(tmp);
                finish();
            }
        });

        xiongweiBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当SeekBar的进度发生改变时调用，fromUser表示是否是用户拖动改变的进度
                // 这里可以获取当前进度值
                int currentProgress = progress;
                xiongweiText.setText(currentProgress+"cm");
                xiongwei=currentProgress;
                // 进行相关操作，比如更新UI显示当前值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 当用户开始拖动SeekBar时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当用户停止拖动SeekBar时调用
            }
        });
        yaoweiBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当SeekBar的进度发生改变时调用，fromUser表示是否是用户拖动改变的进度
                // 这里可以获取当前进度值
                int currentProgress = progress;
                yaoweiText.setText(currentProgress+"cm");
                yaowei=currentProgress;
                // 进行相关操作，比如更新UI显示当前值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 当用户开始拖动SeekBar时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当用户停止拖动SeekBar时调用
            }
        });
        tunweiBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当SeekBar的进度发生改变时调用，fromUser表示是否是用户拖动改变的进度
                // 这里可以获取当前进度值
                int currentProgress = progress;
                tunweiText.setText(currentProgress+"cm");
                tunwei=currentProgress;
                // 进行相关操作，比如更新UI显示当前值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 当用户开始拖动SeekBar时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当用户停止拖动SeekBar时调用
            }
        });
    }
}