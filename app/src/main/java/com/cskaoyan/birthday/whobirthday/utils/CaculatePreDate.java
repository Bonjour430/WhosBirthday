package com.cskaoyan.birthday.whobirthday.utils;

import java.util.Calendar;

/**
 * Created by lamchaohao on 2016/4/24.
 */
public class CaculatePreDate {

    /**
     * 计算提前多少天的日期是多少
     * @param preMonth
     * @param preDay
     * @param howLongDay
     * @return 提前的日期
     */
    public static String getMinusDate(int preMonth,int preDay,int howLongDay){
        Calendar calendar=Calendar.getInstance();
        if (preDay<=howLongDay){
            preMonth=preMonth-1;
            if (preMonth<1){
                preMonth=12;
            }
            int tempDay=howLongDay-preDay;
            if (isBigMonth(preMonth)){//大月
                preDay=31-tempDay;
            }else{//小月
                if (preMonth==2){
                    int year = calendar.get(Calendar.YEAR);
                    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)){//闰年
                        preDay=29-tempDay;
                    }else{
                        preDay=28-tempDay;
                    }

                }else{//除了2月
                    preDay=30-tempDay;
                }
            }
        }else{
            preDay=preDay-howLongDay;
        }
        return preMonth+"-"+preDay;
    }

    /**
     * 判断某月份是大月还是小月
     * @param month
     * @return
     */
    public static boolean isBigMonth(int month){
        int[] bigMonth={1,3,5,7,8,10,12};
        boolean isBigMonth=false;
        for (int i=0;i<bigMonth.length;i++){
            if (bigMonth[i]==month)
                isBigMonth=true;
        }
        return isBigMonth;
    }
}
