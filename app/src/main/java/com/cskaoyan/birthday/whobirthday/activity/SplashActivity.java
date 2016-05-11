package com.cskaoyan.birthday.whobirthday.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.service.MyService;

//caoyafei
public class SplashActivity extends AppCompatActivity {

    /*private ImageView imageView2;*/
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //启动MyService
        startService(new Intent(this, MyService.class));
        imageView1 = (ImageView) findViewById(R.id.iv1);

       new Thread(new SleepThread()).start();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    class SleepThread extends Thread{
        @Override
        public void run() {
            try {
                sleep(1500);
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
