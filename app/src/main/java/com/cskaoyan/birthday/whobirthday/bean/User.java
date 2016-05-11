package com.cskaoyan.birthday.whobirthday.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by whb on 2016/4/19.
 */
public class User extends BmobUser {

    private  String username;
    private  String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //4-23更新，去掉电话，邮箱字段

}
