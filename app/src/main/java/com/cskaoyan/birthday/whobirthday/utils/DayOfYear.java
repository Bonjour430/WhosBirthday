package com.cskaoyan.birthday.whobirthday.utils;

import java.util.Calendar;

/**
 * Created by lamchaohao on 2016/4/22.
 */
public class DayOfYear {

    private static Calendar mCalendar;

    /**
     * 计算某天在一年中的第几天
     * @param month
     * @param day
     */
    public static int getDayOfYear(int month, int day){
        mCalendar = Calendar.getInstance();
        int year =  mCalendar.YEAR;
        month--;
        int sumDay=0;
        switch (month){
            case 12:sumDay += 31;
            case 11:sumDay += 30;
            case 10:sumDay += 31;
            case 9:sumDay += 30;
            case 8:sumDay += 31;
            case 7:sumDay += 31;
            case 6:sumDay += 30;
            case 5:sumDay += 31;
            case 4:sumDay += 30;
            case 3:sumDay += 31;

            case 2: //闰年
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)){
                    sumDay += 29;
                }else{
                    sumDay += 28;
                }
            case 1:sumDay += 31;
        }
        //月数的天数加当前月的天数
        sumDay=sumDay+day;
        return sumDay;
    }


    /**
     *
     * @param date 格式如 4-11，或者 2016-4-22
     * @return int 传入日期是当前年的哪一天
     */
    public static int getDayOfYear(String date){
        String regularExpression = "-";
        String[] dateSplit = date.split(regularExpression);
        if (dateSplit.length==2){
            int month=Integer.parseInt(dateSplit[0]);
            int day=Integer.parseInt(dateSplit[1]);
            return getDayOfYear(month,day);
        }else if(dateSplit.length==3){
            int month=Integer.parseInt(dateSplit[1]);
            int day=Integer.parseInt(dateSplit[2]);
            return getDayOfYear(month,day);
        }
        //格式错误，返回-1
        return -1;
    }

    public static int getHowManyDayLeft(String birth){
        mCalendar = Calendar.getInstance();
        int year =  mCalendar.YEAR;
        //当前天是今天的那一天
        int today = mCalendar.get(Calendar.DAY_OF_YEAR);
        int birthday=getDayOfYear(birth);
        int leftDay=birthday-today+1;
        if (leftDay<0){
            year=year+1;//下一年是否闰年
            if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)){
                leftDay=leftDay+366;
            }else{
                leftDay=leftDay+365;
            }
        }
        return leftDay;
    }
}
