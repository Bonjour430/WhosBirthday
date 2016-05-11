package com.cskaoyan.birthday.whobirthday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.R;

//caoyafei

public class AlarmActivity extends Activity {

    MediaPlayer alarmMusic;
    public Context mcontext;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_alarm_controller);
        super.onCreate(savedInstanceState);
        Log.i("AlarmActivity","我被启动了");
        //mcontent必须要在这里初始化
        mcontext=this;
        //最好是写一个service 在每天的一个固定的时间，检测，如果有符合条件的就播放铃声
        mName=getIntent().getStringExtra("name");
        alarmMusic();
        sendNotice();

    }
      //提炼一个加载闹钟的方法
    public void  alarmMusic(){
        // 加载指定音乐，并为之创建MediaPlayer对象  
        alarmMusic =MediaPlayer.create(this, R.raw.alarm);
        alarmMusic.setLooping(true);
        // 播放音乐  
        alarmMusic.start();
        // 创建一个对话框  
        new AlertDialog.Builder(AlarmActivity.this)
            .setTitle("生日提醒")
            .setMessage("今天是"+mName+"的生日！")
            .setPositiveButton(
                    "确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 停止音乐  
                            alarmMusic.stop();
                            // 结束该Activity  
                            AlarmActivity.this.finish();
                        }
                    }
            ).show();
  }

    //发送通知  这个方法为什么会崩溃？

    public void  sendNotice(){
       //现在的需求是：从数据库拿到图片路径，然后将图片在作为通知的图片
        Intent intent = new Intent(this, FriendDetailActivity.class);
        intent.putExtra("name",mName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
       Notification notification =new Notification.Builder(mcontext)
               .setContentTitle("New mail from ")
               .setContentText("亲，今天是"+mName+"的生日")
               .setSmallIcon(R.drawable.icon_default)
               .setContentIntent(pendingIntent)
               .setAutoCancel(true) //自动的维护通知的消失
               .build();

        // 设置通知的标识为, 点一次被关闭
        notification.flags|= Notification.FLAG_AUTO_CANCEL;
        // 设置通知的声音为系统短信的铃声
        notification.defaults = Notification.DEFAULT_SOUND;
        // 弹出通知, id为0
        nm.notify(1, notification);
    }


        /*
        //判断那些情况下 需要播放铃声    给播放铃声加一些限制条件
    public void  playAlarm(){
        //现在要做的就是判断现在的日期，和数据库表里面的日期的相差的天数

        int x =15;
        switch(x) {
            case 0:
                alarmMusic();
                sendNotice();
                break;
            case 1:
                alarmMusic();
                sendNotice();
                break;

            case 3:
                alarmMusic();
                sendNotice();
                break;

            case 5:
                alarmMusic();
                sendNotice();
                break;

            case 15:
                alarmMusic();
                sendNotice();
                break;
            case 30:
                alarmMusic();
                sendNotice();
                break;
            default:
                break;
        }
    }     */
}




