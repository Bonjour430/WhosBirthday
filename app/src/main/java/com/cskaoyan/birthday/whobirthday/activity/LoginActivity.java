package com.cskaoyan.birthday.whobirthday.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.UserInfo;
import com.cskaoyan.birthday.whobirthday.utils.Md5Utils;


import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;


/**
 * This activity if used to login.
 * Created by whb
 * 2016-4-21
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_login_username;
    private EditText et_login_password;

    private static String TAG = "onComplete";

    //第三方登录的用户BmobUser
    private UserInfo thirdBmobUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化Bmob
        MyApplication.initBmob(this);

        //初始化ShareSDK
        MyApplication.initShareSDK(this);


        //去掉ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);

        ImageButton btn_login_weibologin = (ImageButton) findViewById(R.id.btn_login_weibologin);
        ImageButton btn_login_qqlogin = (ImageButton) findViewById(R.id.btn_login_qqlogin);
        btn_login_weibologin.setOnClickListener(this);
        btn_login_qqlogin.setOnClickListener(this);

        /**
         * ToolBar设置
         */
        Toolbar toolbar_login =  (Toolbar) findViewById(R.id.toolbar_login);
        //设置为ActionBar
        setSupportActionBar(toolbar_login);
    /*    //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //设置主标题
        getSupportActionBar().setTitle("登录"); //这里不能直接调用ToolBar的setTitle方法，无效
        //设置标题颜色
        toolbar_login.setTitleTextColor(Color.parseColor("#FFFFFF"));
        /*//设置回退按钮点击事件
        toolbar_login.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //给主页面一个 返回码，如果是101，代表没有登录，直接返回主页面
                setResult(101);
                finish();
            }
        });*/



    }


    /**
     * 登录
     * @param view
     */
    public void login(View view)
    {
        //进行登录验证，密码使用MD5加密
        final String username = et_login_username.getText().toString();
        final String password = Md5Utils.md5(et_login_password.getText().toString());

        UserInfo bmobUser = new UserInfo();  //直接登录的BmobUser
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);

        bmobUser.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //登录成功，跳转到主页面

                //这里测试，跳转到设置页面
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                //登录成功之后，将当前密码存储到SP中
                MyApplication.editor.putString("userpassword",password).commit();
                //给主页面一个 返回码，如果是100，代表登录成功

                finish();

            }

            @Override
            public void onFailure(int i, String s) {
                //登录失败
                Toast.makeText(LoginActivity.this, i+s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 注册
     * @param view
     */
    public void register(View view)
    {
        //进行注册验证
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

    /**
     * 第三方登录点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login_weibologin:
                openWeiboAuthorize();
                break;

            case R.id.btn_login_qqlogin:
                openQQAuthorize();
                break;

            default:

                break;
        }
    }

    /**
     * 微博登录
     */
    public  void openWeiboAuthorize() {
        final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener( new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                thirdBmobUser = new UserInfo();
                //获得第三方用户名
                thirdBmobUser.setUsername(weibo.getDb().getUserName());
                thirdBmobUser.setImage(weibo.getDb().getUserIcon());
                //启动一个SetUserPasswordActivity来设置第三方用户密码,在OnActivityResult方法中接收传回来的Intent中携带的密码
                startActivityForResult(new Intent(LoginActivity.this,SetUserPasswordActivity.class),100);


                Log.d(TAG, weibo.getDb().getToken());
                Log.d(TAG, weibo.getDb().getToken());
                Log.d(TAG, weibo.getDb().exportData());
                Log.d(TAG, weibo.getDb().getPlatformNname());
                Log.d(TAG, weibo.getDb().getTokenSecret());
                Log.d(TAG, weibo.getDb().getUserGender());
                Log.d(TAG, weibo.getDb().getUserIcon());
                Log.d(TAG, weibo.getDb().getUserId());
                Log.d(TAG, weibo.getDb().getUserName());
                Log.d(TAG, weibo.getDb().getExpiresIn() + "");
                Log.d(TAG, weibo.getDb().getExpiresTime() + "");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "错误页面");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "取消授权");
            }
        });
        weibo.authorize();
        //移除授权
        //weibo.removeAccount(true);
    }


    /**
     * QQ登录
     */
    public void openQQAuthorize() {
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
                thirdBmobUser = new UserInfo();
                thirdBmobUser.setUsername(qq.getDb().getUserName());
                thirdBmobUser.setImage(qq.getDb().getUserIcon());

                Log.i("openQQAuthorize",platform.getName());

                //启动一个SetUserPasswordActivity来设置第三方用户密码,在OnActivityResult方法中接收传回来的Intent中携带的密码
                startActivityForResult(new Intent(LoginActivity.this,SetUserPasswordActivity.class),100);
                if (res!=null){
                    String id,name,description,profile_image_url;
                    id=res.get("id").toString();//ID
                    name=res.get("name").toString();//用户名
                    description=res.get("description").toString();//描述
                    profile_image_url=res.get("profile_image_url").toString();//头像链接
                    String str="ID: "+id+";\n"+
                            "用户名： "+name+";\n"+
                            "描述："+description+";\n"+
                            "用户头像地址："+profile_image_url;
                    Log.i(TAG,str);
                }

                Log.d(TAG, qq.getDb().getToken());
                Log.d(TAG, qq.getDb().getToken());
                Log.d(TAG, qq.getDb().exportData());
                Log.d(TAG, qq.getDb().getPlatformNname());
                Log.d(TAG, qq.getDb().getTokenSecret());
                Log.d(TAG, qq.getDb().getUserGender());
                Log.d(TAG, qq.getDb().getUserIcon());
                Log.d(TAG, qq.getDb().getUserId());
                Log.d(TAG, qq.getDb().getUserName());
                Log.d(TAG, qq.getDb().getExpiresIn() + "");
                Log.d(TAG, qq.getDb().getExpiresTime() + "");

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "错误页面"+i+throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "取消授权");
            }
        });
        qq.authorize();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult",resultCode+"");
        if (requestCode == 100 && resultCode == Activity.RESULT_OK)
        {
            //第三方授权成功之后，引导用户设置密码，然后将密码进行MD5加密
            final String password = data.getStringExtra("password");
            String md5Password = Md5Utils.md5(password);
            thirdBmobUser.setPassword(md5Password);

            //将经过Md5加密之后的密码，保存到SP中，以便在设置中修改密码的时候验证
            MyApplication.configsp.edit().putString("userpassword",md5Password).commit();

            //用户设置完密码之后，返回LoginActivity，携带回密码
            thirdBmobUser.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(LoginActivity.this, "注册成功！"+thirdBmobUser.getUsername(), Toast.LENGTH_SHORT).show();
                    //注册成功之后自动登录
                    login();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(LoginActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    /**
     * 第三方账号注册之后，直接用第三方账号和自己设置的密码登录
     *
     */
    private void login()
    {
        thirdBmobUser.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //登录成功，跳转到主页面

                //这里测试，跳转到设置页面
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                finish();

            }

            @Override
            public void onFailure(int i, String s) {
                //登录失败
                Toast.makeText(LoginActivity.this, i+s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
