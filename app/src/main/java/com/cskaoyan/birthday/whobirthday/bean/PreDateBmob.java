package com.cskaoyan.birthday.whobirthday.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by whb on 2016/4/24.
 */
public class PreDateBmob extends BmobObject {

    /**
     * String Mind_create="create table "+Remind_Table+
     " (name varchar(10), " +
     "pre_theDay integer," +
     "pre_one integer," +
     "pre_three integer," +
     "pre_seven integer," +
     "pre_fifteen integer," +
     "pre_month integer," +
     "FOREIGN KEY(name) REFERENCES "+Person_Table+ "(name) ON UPDATE CASCADE ON DELETE CASCADE);";
     db.execSQL(Mind_create);
     */
    private String name;

    private Integer pre_theDay;
    private Integer pre_one;
    private Integer pre_three;
    private Integer pre_seven;
    private Integer pre_fifteen;
    private Integer pre_month;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPre_theDay() {
        return pre_theDay;
    }

    public void setPre_theDay(Integer pre_theDay) {
        this.pre_theDay = pre_theDay;
    }

    public Integer getPre_one() {
        return pre_one;
    }

    public void setPre_one(Integer pre_one) {
        this.pre_one = pre_one;
    }

    public Integer getPre_three() {
        return pre_three;
    }

    public void setPre_three(Integer pre_three) {
        this.pre_three = pre_three;
    }

    public Integer getPre_seven() {
        return pre_seven;
    }

    public void setPre_seven(Integer pre_seven) {
        this.pre_seven = pre_seven;
    }

    public Integer getPre_fifteen() {
        return pre_fifteen;
    }

    public void setPre_fifteen(Integer pre_fifteen) {
        this.pre_fifteen = pre_fifteen;
    }

    public Integer getPre_month() {
        return pre_month;
    }

    public void setPre_month(Integer pre_month) {
        this.pre_month = pre_month;
    }
}
