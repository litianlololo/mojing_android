package com.example.mojing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dapei_Calendar_Activity extends AppCompatActivity {
    private int year;
    private int month;
    private int day;
    private int daysInMonth;
    private int dayOfWeek;
    private TextView calendarText;
    private List<ImageView> calendarImgs;
    private List<ConstraintLayout> calendarCons;
    private List<TextView> calendarTexts;
    private Calendar calendar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapei_calendar);
        calendarText = findViewById(R.id.calendarText);
        calendarImgs = new ArrayList<>();
        calendarTexts = new ArrayList<>();
        calendarCons = new ArrayList<>();
        // 将ConstraintLayout添加到列表
        for (int i = 1; i <= 42; i++) {
            int consId = getResources().getIdentifier("c" + i, "id", getPackageName());
            ConstraintLayout cons = findViewById(consId);
            cons.setBackgroundResource(R.drawable.radius_border4);
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
        initcalendar();
    }
    @SuppressLint("SetTextI18n")
    private void initcalendar()
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 注意要加一，因为月份从0开始
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 格式化月份为两位数
        String formattedMonth = String.format("%02d", month);
        calendarText.setText(year + "." + formattedMonth);

        calendar.set(Calendar.MONTH, month - 1); // 月份需要减一
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 获取这个月1号是星期几，1代表星期日，2代表星期一，依此类推
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        dayOfWeek--; //表示偏移量

        TableRow ext = findViewById(R.id.extraTableRow);
        //ext.setVisibility(View.VISIBLE);
        for(int i = 0;i<=41;i++){
            calendarTexts.get(i).setText("");
            calendarImgs.get(i).setImageDrawable(null);
        }
        if(daysInMonth+dayOfWeek<=35)
            ext.setVisibility(View.GONE);
        else
            ext.setVisibility(View.VISIBLE);
        for(int i=1;i<=daysInMonth;i++){
            TextView tmpText = calendarTexts.get(i-1+dayOfWeek);
            tmpText.setText(Integer.toString(i));
        }
    }
}