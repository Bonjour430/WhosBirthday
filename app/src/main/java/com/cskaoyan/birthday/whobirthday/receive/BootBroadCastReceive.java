package com.cskaoyan.birthday.whobirthday.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.service.MyService;

/**
 * Created by caoyafei on 2016/4/22. 接受开机启动广播
 */
public class BootBroadCastReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //后边的XXX.class就是要启动的服务   
        //通过开机注册广播，就会立即启动service 
         Intent serviceintent = new Intent(context,MyService.class);
         context.startService(serviceintent);
         Log.i("BootBroadCastReceive","我接到广播了！");
    }
}
