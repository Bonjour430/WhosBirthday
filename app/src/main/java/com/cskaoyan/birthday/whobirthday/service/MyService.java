package com.cskaoyan.birthday.whobirthday.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.activity.AlarmActivity;
import com.cskaoyan.birthday.whobirthday.receive.TimeTickReceiver;
import com.cskaoyan.birthday.whobirthday.utils.DayOfYear;

/*   caoyafei
写一个开机就启动的service，每天20点钟就检测，如果有符合件的生日提醒就调用铃声

   但是，如果要开机就启动一个Service首先就必须，注册一个广播接受者
 */


public class MyService extends Service {
    private IntentFilter mFilter;
    private TimeTickReceiver mTickReceiver;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //在此处启动AlarmAvtivity
        Log.i("MyService","service被创建了");

          //动态注册监听每分钟每天变化的receiver
        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_TIME_TICK); //每分钟变化的action  
        mFilter.addAction(Intent.ACTION_DATE_CHANGED);//监听日期的变化

        mFilter.addAction(Intent.ACTION_TIME_CHANGED); //设置了系统时间的action  
        mTickReceiver = new TimeTickReceiver();
        registerReceiver(mTickReceiver, mFilter);
        Log.i("onCreate()","启动了TimeService");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  return super.onStartCommand(intent, flags, startId);
         return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
