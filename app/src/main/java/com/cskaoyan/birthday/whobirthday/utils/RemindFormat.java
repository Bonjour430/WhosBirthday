package com.cskaoyan.birthday.whobirthday.utils;

/**
 * Created by lamchaohao on 2016/4/23.
 */
public class RemindFormat {
    //提醒
    public static final int REMIND=1;
    //不提醒
    public static final int NOT_REMIND=0;

    public static boolean intToBoolean(int mind){
        if (mind==0)
            return false;
        else
            return true;
    }

    /**
     * 有boolean类型的提醒转化成int存入数据库
     * @param mind
     * @return
     */
    public static int booleanToInt(boolean mind){
        if (mind)
            return REMIND;
        else
            return NOT_REMIND;
    }
}
