package com.cskaoyan.birthday.whobirthday.utils;

/**
 * 计算星座
 * Created by lamchaohao on 2016/4/21.
 */
public class CaculateZodiac {

    /**
     *
     * @param birthday 格式如：2016-10-17
     * @return String 返回星座
     */
    public static String getZodiac(String birthday){
        String[] split = birthday.split("-");
        int month=Integer.parseInt(split[1]);
        int day=Integer.parseInt(split[2]);
        String star="";
        if (month == 1 && day >= 20 || month == 2 && day <= 18) {
            star = "水瓶座";
        }
        if (month == 2 && day >= 19 || month == 3 && day <= 20) {
            star = "双鱼座";
        }
        if (month == 3 && day >= 21 || month == 4 && day <= 19) {
            star = "白羊座";
        }
        if (month == 4 && day >= 20 || month == 5 && day <= 20) {
            star = "金牛座";
        }
        if (month == 5 && day >= 21 || month == 6 && day <= 21) {
            star = "双子座";
        }
        if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            star = "巨蟹座";
        }
        if (month == 7 && day >= 23 || month == 8 && day <= 22) {
            star = "狮子座";
        }
        if (month == 8 && day >= 23 || month == 9 && day <= 22) {
            star = "处女座";
        }
        if (month == 9 && day >= 23 || month == 10 && day <= 22) {
            star = "天秤座";
        }
        if (month == 10 && day >= 23 || month == 11 && day <= 21) {
            star = "天蝎座";
        }
        if (month == 11 && day >= 22 || month == 12 && day <= 21) {
            star = "射手座";
        }
        if (month == 12 && day >= 22 || month == 1 && day <= 19) {
            star = "摩羯座";
        }
        return star;
    }

}
