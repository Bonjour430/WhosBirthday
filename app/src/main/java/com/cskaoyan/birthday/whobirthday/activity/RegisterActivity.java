package com.cskaoyan.birthday.whobirthday.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.UserInfo;
import com.cskaoyan.birthday.whobirthday.utils.Md5Utils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATUS_CODE_USERNAME_EXIST = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);


        //设置ToolBar
        Toolbar toolbar_register = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("注册");
        toolbar_register.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Button btn_register_register = (Button) findViewById(R.id.btn_register_register);
        Button btn_register_cancel = (Button) findViewById(R.id.btn_register_cancel);

        btn_register_register.setOnClickListener(this);
        btn_register_cancel.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_register_register:
                //用户注册，发送到Bmob云
                register();
                break;

            case R.id.btn_register_cancel:
                finish();
                break;

            default:

                break;

        }
    }

    /**
     * 注册时只需要用户名、密码，之后可以在设置-->我的账号中绑定手机号和邮箱
     *      用户名要求是6-10位的字母
     *      密码要求是6-10位的数字
     */
    private void register()
    {
        EditText et_register_username = (EditText) findViewById(R.id.et_register_username);
        EditText et_register_password = (EditText) findViewById(R.id.et_register_password);
        EditText et_register_password_confirm = (EditText) findViewById(R.id.et_register_password_confirm);

        String username = et_register_username.getText().toString();
        final String password = et_register_password.getText().toString();
        String password_confirm = et_register_password_confirm.getText().toString();

        if (password.equals(password_confirm))
        {
            if (!password.matches("\\d{6,10}"))
            {
                Toast.makeText(this,"密码格式不符合要求！",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!username.matches("\\w{6,10}"))
            {
                Toast.makeText(this,"用户名格式不符合要求！",Toast.LENGTH_SHORT).show();
                return;
            }



            final UserInfo user = new UserInfo();
            user.setUsername(username);
            user.setPassword(Md5Utils.md5(password));  //注册时，密码使用MD5加密

            user.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    //进入主页

                    //注册成功之后，将密码缓存到SP中
                    MyApplication.editor.putString("userpassword",password).commit();

                    //这里为了测试，注册完成之后直接进入到我的账号页面
                    Intent intent = new Intent(RegisterActivity.this,MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {

                    if (i == STATUS_CODE_USERNAME_EXIST)
                    {
                        Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"两次输入密码不一致！请重新输入！",Toast.LENGTH_SHORT).show();
        }


    }
}
