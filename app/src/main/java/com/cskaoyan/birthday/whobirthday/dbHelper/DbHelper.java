package com.cskaoyan.birthday.whobirthday.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lamchaohao on 2016/4/24.
 *
 */
public class DbHelper extends SQLiteOpenHelper {

    //联系人表
    public static final String Person_Table="PersonInfo";
    //用户表
    public static final String User_Table="UserInfo";
    //提前日期表
    public static final String PreDate_Table="PreDate";
    //提醒设置表
    public static final String Remind_Table="Remind";
    //分组
    public static final String Group_Table="GroupTB";
    public DbHelper(Context context, int version) {
        super(context, "Info.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Info数据库中 创建 User表
        String User_create="create table "+User_Table+
                " (objectId varchar(20) ," +
                "username varchar(10)," +
                "password varchar(18) not null," +
                "gender varchar(2)," +
                "image varchar(8)," +
                "phone varchar(11)," +
                "email varchar(21)," +
                "createTime varchar(20)," +
                "updateTime varchar(20)); ";
         db.execSQL(User_create);
        //Info数据库中 创建 PersonDetails表
        //lamchaohao 4-21更改group为_group，因为group在数据库中时关键字，使用的话会报错
        String Person_create="create table "+Person_Table+
                " (id integer primary key autoincrement," +
                "name varchar(10) unique," +
                "gender varchar(2)," +
                "birthday varchar(10)," +
                "phone varchar(11)," +
                "imagePath varchar(50)," +
                "atGroup varchar(10)," +
                "remind integer, "+
                "userName varchar(20), "+
                "objectId varchar(20)," +
                "createTime varchar(20)," +
                "updateTime varchar(20)); ";
         db.execSQL(Person_create);

        //某人生日提前1-30天的日期，保存的格式为 月-日，如：4-11 代表4月11日 注意,外键级联操作并不起作用
        String PreDate_create="create table "+PreDate_Table+
                " (name varchar(10) unique," +
                "pre_one varchar(10)," +
                "pre_three varchar(10)," +
                "pre_seven varchar(10)," +
                "pre_fifteen varchar(10)," +
                "pre_month varchar(10)," +
                "FOREIGN KEY(name) REFERENCES "+Person_Table+ "(name) ON UPDATE CASCADE ON DELETE CASCADE);";
        db.execSQL(PreDate_create);

        //创建Remind_Table，字段表示 0代表不提醒，1代表提醒  注意,外键级联操作并不起作用
        String Mind_create="create table "+Remind_Table+
                " (name varchar(10) unique, " +
                "pre_theDay integer," +
                "pre_one integer," +
                "pre_three integer," +
                "pre_seven integer," +
                "pre_fifteen integer," +
                "pre_month integer," +
                "FOREIGN KEY(name) REFERENCES "+Person_Table+ "(name) ON UPDATE CASCADE ON DELETE CASCADE);";
        db.execSQL(Mind_create);

        //创建分组表
        String Group_create="create table "+Group_Table+
                " (id integer primary key autoincrement," +
                "group_name varchar(10));";
        db.execSQL(Group_create);
        ContentValues cv=new ContentValues();
        //默认插入四个分组
        cv.put("group_name","亲人");
        db.insert(Group_Table,null,cv);
        cv.put("group_name","朋友");
        db.insert(Group_Table,null,cv);
        cv.put("group_name","同学");
        db.insert(Group_Table,null,cv);
        cv.put("group_name","同事");
        db.insert(Group_Table,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
