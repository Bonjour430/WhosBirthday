package com.cskaoyan.birthday.whobirthday.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.User;
import com.cskaoyan.birthday.whobirthday.bean.UserInfo;
import com.cskaoyan.birthday.whobirthday.utils.HeadPicSave;
import com.cskaoyan.birthday.whobirthday.utils.Md5Utils;
import com.lidroid.xutils.BitmapUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 账号信息只有用户名，密码
     *
     */


    private TextView tv_myaccount_username;
    private Button tv_myaccount_password;
    private ImageButton ib_account_image;
    private BitmapUtils mBU;
    private static final int REQUEST_CONTACT=100;
    private static final int REQUEST_CODE_PICK_IMAGE =200;
    private String mPicPath;
    private Bitmap mbitMap;
    private UserInfo mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_my_account);


        /**
         * ToolBar设置
         */
        Toolbar toolbar_myAccout =  (Toolbar) findViewById(R.id.toolbar_myAccout);
        //设置为ActionBar
        setSupportActionBar(toolbar_myAccout);
        //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置主标题
        getSupportActionBar().setTitle("我的账户"); //这里不能直接调用ToolBar的setTitle方法，无效
        //设置标题颜色
        toolbar_myAccout.setTitleTextColor(Color.parseColor("#FFFFFF"));
        //设置回退按钮点击事件
        toolbar_myAccout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //给主页面一个 返回码，如果是101，代表没有登录，直接返回主页面
                setResult(101);
                finish();
            }
        });
        initView();

        initData();

    }

    private void initView(){

        tv_myaccount_username = (TextView) findViewById(R.id.tv_myaccount_username);

        tv_myaccount_password = (Button) findViewById(R.id.tv_myaccount_password);

        ib_account_image = (ImageButton) findViewById(R.id.ib_account_image);

        //修改密码
        tv_myaccount_password.setOnClickListener(this);
        ib_account_image.setOnClickListener(this);

    }

    private void initData(){


        mBU = new BitmapUtils(this);
        //获取当前登录用户的对象
        mUser = BmobUser.getCurrentUser(this,UserInfo.class);
        tv_myaccount_username.setText(mUser.getUsername());
        String userPic = MyApplication.configsp.getString("userPic", "");
        ib_account_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (!userPic.isEmpty()){
            ib_account_image.setImageURI(Uri.fromFile(new File(userPic)));
        }else{
            mBU.display(ib_account_image, mUser.getImage());
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            //修改密码：
            case R.id.tv_myaccount_password:
                //弹出dialog来修改密码
                inputUserPassword();
                break;
            case R.id.ib_account_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_PICK_IMAGE){
            Uri selectedImage = data.getData();
//                BitmapUtils bu=new BitmapUtils(this);
//                bu.display(iv_createFri_bg,selectedImage.toString());
//                bu.display(ib_create_image,selectedImage.toString());
            bitmapFactory(selectedImage,ib_account_image);
        }
    }


    //用于压缩图片
    private void bitmapFactory(Uri uri,ImageView imageView){
        File picFile=new File(uri.getPath());
        mPicPath=picFile.getAbsolutePath();
        long length = picFile.length();
        Bitmap bm=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (length>204800){
            //如果图片大于200kb，则进行压缩
            options.inSampleSize=2;
            bm = BitmapFactory.decodeFile(mPicPath,options);
        }else{
            bm = BitmapFactory.decodeFile(mPicPath);
        }
        mbitMap=bm;
        if (mbitMap!=null){
            mPicPath = HeadPicSave.saveBitMap(this, mUser.getUsername(), mbitMap);
        }
        MyApplication.editor.putString("userPic",mPicPath).commit();
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
    }
    /**
     * 修改密码前，先要输入旧密码
     */
    private void inputUserPassword() {

        //弹出一个AlertDialog来输入密码，正确，则可以修改，错误则不能修改

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.input_account_password, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText et_inputaccount_pwd = (EditText) view.findViewById(R.id.et_inputaccount_pwd);
        View btn_inputaccount_confirm = view.findViewById(R.id.btn_inputaccount_confirm);
        View btn_inputaccount_cancel = view.findViewById(R.id.btn_inputaccount_cancel);

        btn_inputaccount_confirm.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String currentPassword = MyApplication.configsp.getString("userpassword",null);
                String inputpassword = et_inputaccount_pwd.getText().toString();
                inputpassword = Md5Utils.md5(inputpassword);
                if (currentPassword != null && currentPassword.equals(inputpassword))
                {
                    //输入密码，如果和当前登录用户密码一致，则可以修改密码
                    setUserPassword();

                }else {
                     Toast.makeText(MyAccountActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();

            }
        });

        btn_inputaccount_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    /**
     * 重置用户密码
     */
    private void setUserPassword()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.edit_account_password, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText et_editaccount_pwd = (EditText) view.findViewById(R.id.et_editaccount_pwd);
        final EditText et_editaccount_pwdconfirm = (EditText) view.findViewById(R.id.et_editaccount_pwdconfirm);

        View btn_editaccount_confirm = view.findViewById(R.id.btn_editaccount_confirm);
        View btn_editaccount_cancle = view.findViewById(R.id.btn_editaccount_cancle);

        btn_editaccount_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPwd = et_editaccount_pwd.getText().toString();
                String newPwdConfirm = et_editaccount_pwdconfirm.getText().toString();

                //判断两次输入的密码是否符合要求，是否一致
                if (newPwd.equals(newPwdConfirm))
                {
                    if (newPwd.matches("\\d{6,10}"))
                    {
                        //MD5加密
                        final String md5Pwd = Md5Utils.md5(newPwd);

                        //更新服务器上的密码
                        BmobUser.updateCurrentUserPassword(MyAccountActivity.this, MyApplication.configsp.getString("userpassword",null), md5Pwd, new UpdateListener() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Log.i("用户密码修改成功", "密码修改成功，可以用新密码进行登录啦");
                                //更新本地SP中的密码
                                MyApplication.editor.putString("userpassword",md5Pwd).commit();
                                Toast.makeText(MyAccountActivity.this, "新密码设置成功！", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                // TODO Auto-generated method stub
                                Log.i("用户密码修改成功", "密码修改失败："+msg+"("+code+")");
                                Toast.makeText(MyAccountActivity.this, "密码设置失败！", Toast.LENGTH_SHORT).show();

                            }
                        });



                    }else {
                        Toast.makeText(MyAccountActivity.this, "密码格式错误，请输入6-10位数字", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(MyAccountActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();

            }
        });

        btn_editaccount_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
