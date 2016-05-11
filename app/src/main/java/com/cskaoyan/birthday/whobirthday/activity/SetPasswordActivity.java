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

/**
 * This activity if used to set APP password（not user password）.
 * Created by whb
 * 2016-4-21
 */
public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText et_setpwd_pwd;
    private Button btn_setpwd_cpnfirm;
    private EditText et_setpwd_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        btn_setpwd_cpnfirm = (Button) findViewById(R.id.btn_setpwd_cpnfirm);
        et_setpwd_pwd = (EditText) findViewById(R.id.et_setpwd_pwd);
        et_setpwd_confirm = (EditText) findViewById(R.id.et_setpwd_confirm);

        btn_setpwd_cpnfirm.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        /**
         * \d{6,10}
         */
         String pwd = et_setpwd_pwd.getText().toString();
         String pwdconfirm = et_setpwd_confirm.getText().toString();

        if (pwd.matches("\\d{6,10}") && pwdconfirm.matches("\\d{6,10}"))
        {
            //符合6-10位数字或者字母的组合
            if (pwd.equals(pwdconfirm))
            {
                //密码设置成功
                //跳转回Setting页面
                MyApplication.editor
                        .putString("password",pwd)
                        .commit();
                Toast.makeText(SetPasswordActivity.this, "密码设置成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,SettingActivity.class));
                finish();

            }else {
                Toast.makeText(SetPasswordActivity.this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                et_setpwd_pwd.setText("");
                et_setpwd_confirm.setText("");
            }

        }else{
            Toast.makeText(SetPasswordActivity.this, "密码格式不符合要求，请重新输入！", Toast.LENGTH_SHORT).show();
            et_setpwd_pwd.setText("");
            et_setpwd_confirm.setText("");
        }
    }
}
