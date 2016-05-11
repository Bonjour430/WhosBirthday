package com.cskaoyan.birthday.whobirthday.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;


import com.cskaoyan.birthday.whobirthday.R;

/**
 * create by chang
 */
public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉默认的actionbar(需要在setContentView之前)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aboutme);
        initEvent();
    }

    private void initEvent() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_aboutme);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
