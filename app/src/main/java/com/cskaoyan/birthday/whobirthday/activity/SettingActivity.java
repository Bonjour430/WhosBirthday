package com.cskaoyan.birthday.whobirthday.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;

import cn.bmob.v3.BmobUser;

/**
 * This activity if used to set something for app.
 * Created by whb
 * 2016-4-21
 */
public class SettingActivity extends AppCompatActivity {

    private String[] settingListText;
    private ListView settingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_setting);

        //设置ToolBar
        Toolbar toolbar_setting = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar_setting);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        getSupportActionBar().setTitle("设置");
        toolbar_setting.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar_setting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        settingListText = new String[]{"生日提醒","主题颜色","密码锁","数据同步设置","退出登录"};

        settingListView = (ListView) findViewById(R.id.lv_setting);
        settingListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,settingListText));

        settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //生日提醒
                        startActivity(new Intent(SettingActivity.this,SetRemindActivity.class));
                        break;
                    case 1:
                        //主题颜色
                        startActivity(new Intent(SettingActivity.this,AlarmActivity.class));
                        break;
                    case 2:
                        //密码锁
                        String pwd = MyApplication.configsp.getString("password", "xxx");
                        if ( ! pwd.equals("xxx"))
                        {
                            //之前设置过密码，进入到输入密码的页面
                            startActivity(new Intent(SettingActivity.this,InputPasswordActivity.class));

                        }else{
                            //之前没有设置过密码，进入到设置密码的页面
                            startActivity(new Intent(SettingActivity.this,SetPasswordActivity.class));
                        }
                        break;
                    case 3:
                        //数据同步设置
                        break;
                    case 4:
                        //退出登录
                        exit();
                        break;
                    default:
                        break;


                }
            }
        });
    }

    private void exit() {
        BmobUser.logOut(this);   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(this); // 现在的currentUser是null了

        startActivity(new Intent(this, LoginActivity.class));

    }
}
