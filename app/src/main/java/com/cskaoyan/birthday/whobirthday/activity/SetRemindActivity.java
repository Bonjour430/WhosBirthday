package com.cskaoyan.birthday.whobirthday.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;

public class SetRemindActivity extends AppCompatActivity {

    private TextView tv_setRemind_now;
    private TimePicker tp_setRemind;
    private SharedPreferences configsp;
    private Button bt_setRemind_save;
    private int mHour;
    private int mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_remind);

        initView();
    }

    private void initView() {
        tv_setRemind_now = (TextView) findViewById(R.id.tv_setRemind_now);
        tp_setRemind = (TimePicker) findViewById(R.id.tp_setRemind);
        bt_setRemind_save = (Button) findViewById(R.id.bt_setRemind_save);
        configsp = MyApplication.getConfigsp();
        String clocktime = configsp.getString("clocktime", "");
        String[] split = clocktime.split("-");
        final int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        tv_setRemind_now.setText("当前提醒时间："+hour+"时"+minute+"分");
        mMinute=tp_setRemind.getCurrentMinute();
        mHour=tp_setRemind.getCurrentHour();
        tp_setRemind.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour=hourOfDay;
                mMinute=minute;
            }
        });
        bt_setRemind_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configsp.edit().putString("clocktime",mHour+"-"+mMinute).commit();
                finish();
            }
        });
    }
}
