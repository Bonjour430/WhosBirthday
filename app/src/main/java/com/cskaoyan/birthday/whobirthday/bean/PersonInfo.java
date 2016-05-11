package com.cskaoyan.birthday.whobirthday.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.bmob.v3.BmobObject;

/**
 * Created by lamchaohao on 2016/4/20.
 */
public class PersonInfo extends BmobObject{

    private int id;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private String image;
    private String atGroup;
    private int remind ;
    private String username;
    private String createTime;
    private String updateTime;

    /**
     * 无参构造
     */
    public PersonInfo() {
    }

    /**
     * 无创建时间构造方法
     * @param id
     * @param name
     * @param gender
     * @param birth
     * @param phone
     * @param image
     * @param atGroup
     * @param remind
     */
    public PersonInfo(int id, String name, String gender, String birth, String phone, String image, String atGroup, int remind,String username) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.image = image;
        this.atGroup = atGroup;
        this.remind = remind;
        this.username=username;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = sdf.getTimeZone();
        Calendar instance = Calendar.getInstance(timeZone);
        Date time = instance.getTime();
        String format = sdf.format(time);
        this.createTime=format;
        this.updateTime=format;
    }

    /**
     * 全部字段构造方法
     * @param id
     * @param name
     * @param gender
     * @param birth
     * @param phone
     * @param image
     * @param atGroup
     * @param remind
     * @param username
     * @param createTime
     * @param updateTime
     */
    public PersonInfo(int id, String name, String gender, String birth, String phone, String image, String atGroup, int remind, String username, String createTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.image = image;
        this.atGroup = atGroup;
        this.remind = remind;
        this.username = username;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 无id构造方法
     * @param name
     * @param gender
     * @param birth
     * @param phone
     * @param image
     * @param atGroup
     * @param remind
     * @param objectId
     */
    public PersonInfo(String name, String gender, String birth, String phone, String image, String atGroup, int remind, String objectId) {
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.image = image;
        this.atGroup = atGroup;
        this.remind = remind;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = sdf.getTimeZone();
        Calendar instance = Calendar.getInstance(timeZone);
        Date time = instance.getTime();
        String format = sdf.format(time);
        this.createTime=format;
        this.updateTime=format;

    }

    public PersonInfo(String name, String gender, String birth, String phone, String image, String atGroup, int remind) {
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.image = image;
        this.atGroup = atGroup;
        this.remind = remind;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = sdf.getTimeZone();
        Calendar instance = Calendar.getInstance(timeZone);
        Date time = instance.getTime();
        String format = sdf.format(time);
        this.createTime=format;
        this.updateTime=format;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAtGroup() {
        return atGroup;
    }

    public void setAtGroup(String atGroup) {
        this.atGroup = atGroup;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", atGroup='" + atGroup + '\'' +
                ", remind=" + remind +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
