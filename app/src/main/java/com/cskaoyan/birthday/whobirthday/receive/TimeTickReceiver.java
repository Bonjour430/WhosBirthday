package com.cskaoyan.birthday.whobirthday.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.activity.AlarmActivity;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.utils.DayOfYear;

import java.util.Calendar;


/**
 * Created by  caoyafei on 2016/4/22.
 */
public class TimeTickReceiver  extends BroadcastReceiver {
 //   private static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;
 //   private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;

    public Context mcontext;
    Calendar mCalendar;
    private Thread mObserveThread;

    //在此处会每分钟触发一次，打印log
    @Override
    public void onReceive(Context context, Intent intent) {
     //在此处可以写一个延时操作，监听每天的变化，延时10个小时执行！
     Log.i(" TimeTickReceiver","我是每分钟被触发一次！");
         mcontext=context;
        //每分钟都接收到广播，那就是每分钟都新建一个子线程，所以越堆越多,所以加入判断
        startObserve();
    }

    private void startObserve(){
        //该方法保证创建的子线程只有一个
        if (mObserveThread==null){
            Log.i("startObserve","mObserveThread==null！");
            mObserveThread = new Thread(new ObserveThread());
            mObserveThread.start();
        }
    }

    Handler handler =new Handler(){

        public void handleMessage(Message msg){
            if(msg.what==1){
                //触发去比对，看是否·要调用闹钟的方法
                Log.i("延时后的首次触发","触发成功！");
                Intent intent = new Intent();
                intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK );
              /*  测试用的
                int daynum = 0;
                String str ="04-24";
                daynum = DayOfYear.getHowManyDayLeft(str);
                Log.i("04-26和今天相差的天数是："," "+daynum);

                */
                //非静态的方法只有在类被引入进来才能使用，因为非静态的是先有类才能有对象
                PersonInfoDao personTableDao = new PersonInfoDao(mcontext);
                String whosName =  personTableDao.queryfromPersonAndMind();
                if(!whosName.isEmpty()&&whosName!=null){
                    //如果flag是true，则触发响铃操作。
                    intent.putExtra("name",whosName);
                    intent.setClass(mcontext,AlarmActivity.class);
                    mcontext.startActivity(intent);
                }
            }


        }
    };

    //写一个查询数据库生日那一列的方法

    class  ObserveThread  implements Runnable{

        @Override
        public void run() {
            mCalendar=Calendar.getInstance();
            while(true){
                try {
                    Log.i("ObserveThread","只有一个线程！");

                    //睡眠一分钟
                    Thread.sleep(1000*60*1);
                    Message msg = new Message();
                    msg.what = 1;
                    String clocktime = MyApplication.getConfigsp().getString("clocktime", "");
                    //设置显示当前系统的时间值
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    int currentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = mCalendar.get(Calendar.MINUTE);

                    //一直对应显示Sp里储存的时间值
                    String[] split = clocktime.split("-");
                    int hour = Integer.parseInt(split[0]);
                    int minute = Integer.parseInt(split[1]);
                    //时间核对
                    if (currentHour==hour&&currentMinute==minute){
                        handler.sendMessage(msg);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
