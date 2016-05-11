package com.cskaoyan.birthday.whobirthday.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by whb on 2016/4/19.
 */
public class MyApplication extends Application {

    public static SharedPreferences configsp;

    public static  SharedPreferences.Editor editor;

    /**
     * password 是本软件的应用锁
     *
     * userpassword 是当前账户密码
     */

    @Override
    public void onCreate() {
        super.onCreate();

        configsp=getSharedPreferences("config", MODE_PRIVATE);
        editor =configsp.edit();
        editor.putString("clocktime", "7-30").commit();
        Log.i("MyApplication","MyApplication onCreate");

    }

    public static SharedPreferences getConfigsp() {
        return configsp;
    }

    public static void setConfigsp(SharedPreferences configsp) {
        MyApplication.configsp = configsp;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static void setEditor(SharedPreferences.Editor editor) {
        MyApplication.editor = editor;
    }

    public static void initBmob(Context context)
    {
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(context, "a015c57ad8b2dc541f7bdf4160a98255");
    }

    public static void initShareSDK(Context context)
    {
        ShareSDK.initSDK(context);
    }

    /**
     * 获取当前用户的id，是bmob云端的用户的objectid
     */
    public static String getObjectId(Context context){
        BmobUser currentUser = BmobUser.getCurrentUser(context);
        String objectId = currentUser.getObjectId();
        return objectId;
    }

    public static String getCurrentUsername(Context context)
    {
        BmobUser currentUser = User.getCurrentUser(context);
        if (currentUser!=null){
            String username=currentUser.getUsername();
            return username;
        }else {
            return "";
        }
    }

    public static void setCurrentUsername(Context context,String newName){
        User.getCurrentUser(context).setUsername(newName );
    }

}
