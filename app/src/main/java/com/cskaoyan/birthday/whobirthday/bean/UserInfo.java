package com.cskaoyan.birthday.whobirthday.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/4/20.
 *
 * updated by whb on 2016/4/24
 */
public class UserInfo extends BmobUser{

    //用户名使用BmobUser中已经定义的，不用自己在定义了

    //BmobUser中已经定义了username，password，Phone，email
       /*
        用户名,密码,性别,头像,手机号,邮箱,
        ,password,gender,image,phone,email
       */
    String gender;
    String image;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public UserInfo() {
    }
}
