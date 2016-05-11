package com.cskaoyan.birthday.whobirthday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xcc on 2016/4/19.
 */
public class DateChange {

    //将数据库中提取的生日转换为Date类型
    public static Date stringToDate(String birthday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(birthday);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
/*    public static Date MonthToDate(String birthday) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = null;
        try {
            date = simpleDateFormat.parse(birthday);
            Log.i("测试MonthToDate",date+"");
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date newMonthToDate(String birthday) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(birthday);
            Log.i("测试newMonthToDate",date+"");
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
