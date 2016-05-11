package com.cskaoyan.birthday.whobirthday.bean;

import android.content.Context;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.bean.GroupBean;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by whb on 2016/4/24.
 */
public class Group extends BmobObject {
    /**
     *  " (id integer primary key autoincrement," +
     "group_name varchar(10));";
     */

    private String group_name;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    //把服务器上的所有分组拉下来，插入到本地数据库
    public static void sync(Context context)
    {

        //服务器所有分组
        final List<Group> serverGroups = new ArrayList<>();
        BmobQuery<Group> query = new BmobQuery<Group>();
        query.findObjects(context, new FindListener<Group>() {
            @Override
            public void onSuccess(List<Group> list) {
                serverGroups.addAll(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        GroupDao groupDao = new GroupDao(context);
        for (int i=0;i<serverGroups.size();i++)
        {
            groupDao.saveGroup(serverGroups.get(i).getGroup_name());
        }



    }
}
