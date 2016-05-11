package com.cskaoyan.birthday.whobirthday.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;

public class InputPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_inputpwd_pwd;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);

        et_inputpwd_pwd = (EditText) findViewById(R.id.et_inputpwd_pwd);
        password = MyApplication.configsp.getString("password", "xxx");

        Button btn_inputpwd_confirm = (Button) findViewById(R.id.btn_inputpwd_confirm);
        btn_inputpwd_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        //输入的密码
        String intputpwd = et_inputpwd_pwd.getText().toString();

        if (intputpwd.equals(password))
        {
            //密码输入正确，进入设置新密码的页面
//            startActivity(new Intent(InputPasswordActivity.this,MainActivity.class));
            finish();
        }else {
            //密码输入错误，提示错误
            Toast.makeText(InputPasswordActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            et_inputpwd_pwd.setText("");
        }
    }


}
