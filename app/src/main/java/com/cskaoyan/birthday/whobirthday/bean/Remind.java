package com.cskaoyan.birthday.whobirthday.bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.RemindDao;
import com.cskaoyan.birthday.whobirthday.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by whb on 2016/4/24.
 */
public class Remind extends BmobObject {

    /**
     *  String Mind_create="create table "+Remind_Table+
     " (name varchar(10), " +
     "pre_theDay integer," +
     "pre_one integer," +
     "pre_three integer," +
     "pre_seven integer," +
     "pre_fifteen integer," +
     "pre_month integer," +
     "FOREIGN KEY(name)
     */

    private static String TAG = "Remind";


    public void sync(final Context context){
        //本地的所有Remind信息 RemindDao
        final List<Remind> localReminds;
        final RemindDao remindDao;
        final List<Remind> serverReminds;
        remindDao = new RemindDao(context);
        localReminds = remindDao.getAllReminds();
        Log.i(TAG,"获取本地Remind信息成功"+ localReminds.size());

        //获取服务器上所有的Remind数据
        serverReminds = new ArrayList<>();
        BmobQuery<Remind> query = new BmobQuery<Remind>();
        query.findObjects(context, new FindListener<Remind>() {
            @Override
            public void onSuccess(List<Remind> list) {
                Log.i(TAG,"获取服务器Remind信息成功serverReminds"+ serverReminds.size());
                Log.i(TAG,"获取服务器Remind信息成功"+list.size());

                //后面的代码先执行了，所以没有完成插入
                updateRemind(context,list,localReminds,serverReminds,remindDao);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, i+s+"", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateRemind(final Context context, List<Remind> reminds, List<Remind> localReminds, List<Remind> serverReminds, RemindDao remindDao)
    {
        serverReminds.addAll(reminds);
        Log.i(TAG,"updateRemind"+serverReminds.size());
        /**
         * 同步操作
         */
        HashMap<String,Integer> localRemindsMap = new HashMap<>();
        HashMap<String,Integer> serverRemindsMap = new HashMap<>();

        for (int i = 0; i< localReminds.size(); i++)
        {
            String name = localReminds.get(i).getName();
            localRemindsMap.put(name,i);
            Log.i(TAG,"本地Remind："+name+i);

        }

        Log.i(TAG,"serverReminds："+serverReminds.size());
        for (int i=0;i<serverReminds.size();i++)
        {
            String name = serverReminds.get(i).getName();
            serverRemindsMap.put(name,i);   //订花有两条，但是key的限制，只插入了一条到Map中
            Log.i(TAG,"服务器Remind："+name+i);

        }

        //问题在这里
        Set<String> localnameset = localRemindsMap.keySet();
        Set<String> servernameset = serverRemindsMap.keySet();
        Log.i(TAG,"localnameset："+localnameset.size()+"个数");
        Log.i(TAG,"servernameset："+servernameset.size()+"个数");


        Set<String> downloadRemindSetNames = new HashSet<>();
        downloadRemindSetNames.addAll(servernameset); //一条数据--订花
        downloadRemindSetNames.removeAll(localnameset);  //本地数据0
        Log.i(TAG,"downloadRemindSetNames："+downloadRemindSetNames.size()+"个数");


        Set<String> uploadRemindSetNames = new HashSet<>();
        uploadRemindSetNames.addAll(localnameset);
        uploadRemindSetNames.removeAll(servernameset);

        Set<String> updateRemindSetNames = new HashSet<>();
        updateRemindSetNames.addAll(localnameset);
        updateRemindSetNames.retainAll(servernameset);

        //上传
        final List<BmobObject> uploadReminds = new ArrayList<>();
        for (Iterator iterator = uploadRemindSetNames.iterator();iterator.hasNext(); )
        {
            String next = (String) iterator.next();
            Integer integer = localRemindsMap.get(next);
            Remind remind = localReminds.get(integer);
            uploadReminds.add(remind);
            Log.i(TAG,"上传Remind："+remind.toString());

        }
        new BmobObject().insertBatch(context, uploadReminds, new SaveListener() {

            @Override
            public void onSuccess() {
                Log.i(TAG,"上传Remind成功"+uploadReminds.size());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG,"上传Remind失败"+i+s);

            }
        });

        //下载
        List<BmobObject> downloadReminds = new ArrayList<>();
        Log.i(TAG,"下载Remind的个数："+downloadReminds.size());
        Log.i(TAG,"下载Remind："+downloadRemindSetNames.toString());
        for (Iterator iterator = downloadRemindSetNames.iterator();iterator.hasNext();)
        {
            String next = (String) iterator.next();
            Integer integer = serverRemindsMap.get(next);
            Remind remind = serverReminds.get(integer);
            downloadReminds.add(remind);
            Log.i(TAG,"下载Remind："+remind.toString());

        }
        remindDao.insert(downloadReminds);

        //更新,以本地为准
        List<BmobObject> updateReminds = new ArrayList<>();
        for (Iterator iterator = updateRemindSetNames.iterator();iterator.hasNext();)
        {
            String name = (String) iterator.next();
            Integer localloc = localRemindsMap.get(name);
            Integer serverloc = serverRemindsMap.get(name);

            Remind localRemind = localReminds.get(localloc);
            Remind serverRemind = serverReminds.get(serverloc);

            Remind newRemind = new Remind();
            newRemind.setObjectId(serverRemind.getObjectId());
            newRemind.setPre_theDay(localRemind.getPre_theDay());
            newRemind.setPre_three(localRemind.getPre_three());
            newRemind.setPre_seven(localRemind.getPre_seven());
            newRemind.setPre_fifteen(localRemind.getPre_fifteen());
            newRemind.setPre_month(localRemind.getPre_month());

            updateReminds.add(newRemind);
            Log.i(TAG,"更新Remind："+newRemind.toString());

        }

        new BmobObject().updateBatch(context, updateReminds, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG,"更新Remind成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG,"更新Remind失败"+i+s);
                Toast.makeText(context, i+s, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private String name;
    private Integer pre_theDay;
    private Integer pre_one;
    private Integer pre_three;
    private Integer pre_seven;
    private Integer pre_fifteen;
    private Integer pre_month;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPre_theDay() {
        return pre_theDay;
    }

    public void setPre_theDay(Integer pre_theDay) {
        this.pre_theDay = pre_theDay;
    }

    public Integer getPre_one() {
        return pre_one;
    }

    public void setPre_one(Integer pre_one) {
        this.pre_one = pre_one;
    }

    public Integer getPre_three() {
        return pre_three;
    }

    public void setPre_three(Integer pre_three) {
        this.pre_three = pre_three;
    }

    public Integer getPre_seven() {
        return pre_seven;
    }

    public void setPre_seven(Integer pre_seven) {
        this.pre_seven = pre_seven;
    }

    public Integer getPre_fifteen() {
        return pre_fifteen;
    }

    public void setPre_fifteen(Integer pre_fifteen) {
        this.pre_fifteen = pre_fifteen;
    }

    @Override
    public String toString() {
        return "Remind{" +
                "name='" + name + '\'' +
                ", pre_theDay=" + pre_theDay +
                ", pre_one=" + pre_one +
                ", pre_three=" + pre_three +
                ", pre_seven=" + pre_seven +
                ", pre_fifteen=" + pre_fifteen +
                ", pre_month=" + pre_month +
                '}';
    }

    public Integer getPre_month() {
        return pre_month;
    }

    public void setPre_month(Integer pre_month) {
        this.pre_month = pre_month;
    }
}
