package com.cskaoyan.birthday.whobirthday.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * Created by Administrator on 2016/4/22.
 */
public class VibratorUtil {

    private static Vibrator vib;

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public VibratorUtil(final Activity activity) {  //final  不知道为什么使用Final类型
        vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
    }



    public static void Vibrate(long milliseconds) {
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(long[] pattern,boolean isRepeat) {
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
    public static void Vibrate(boolean isRepeat) {
        long[] longs = {1000,3000,0,3000};
        vib.vibrate(longs,isRepeat ? 1 : -1);
    }
    public static void cancleVibrate() {
        vib.cancel();
    }
}
