package com.cskaoyan.birthday.whobirthday.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;

public class SetUserPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_setuserpwd_pwd;
    private Button btn_setuserpwd_cpnfirm;
    private EditText et_setuserpwd_confirm;

    private static String TAG = "SetUserPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_password);

        Log.i(TAG,"SetUserPasswordActivity onCreate");

        btn_setuserpwd_cpnfirm = (Button) findViewById(R.id.btn_setuserpwd_cpnfirm);
        et_setuserpwd_pwd = (EditText) findViewById(R.id.et_setuserpwd_pwd);
        et_setuserpwd_confirm = (EditText) findViewById(R.id.et_setuserpwd_confirm);


        btn_setuserpwd_cpnfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        /**
         * \d{6,10}
         */
        String pwd = et_setuserpwd_pwd.getText().toString();
        String pwdconfirm = et_setuserpwd_confirm.getText().toString();

        if (pwd.matches("\\d{6,10}") && pwdconfirm.matches("\\d{6,10}"))
        {
            //符合6-10位数字或者字母的组合
            if (pwd.equals(pwdconfirm))
            {
                //密码设置成功
                MyApplication.editor
                        .putString("password",pwd)
                        .commit();
                Toast.makeText(SetUserPasswordActivity.this, "密码设置成功！", Toast.LENGTH_SHORT).show();

                //密码设置成功，用Intent传递回给LoginActivity
                Intent intent = new Intent();
                intent.putExtra("password", pwd);
                setResult(Activity.RESULT_OK, intent);
                Log.i("setUserPassword","UserPassword"+pwd);
                //关闭当前的窗体
                finish();


            }else {
                Toast.makeText(SetUserPasswordActivity.this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                et_setuserpwd_pwd.setText("");
                et_setuserpwd_confirm.setText("");
            }

        }else{
            Toast.makeText(SetUserPasswordActivity.this, "密码格式不符合要求，请重新输入！", Toast.LENGTH_SHORT).show();
            et_setuserpwd_pwd.setText("");
            et_setuserpwd_confirm.setText("");
        }
    }
}
