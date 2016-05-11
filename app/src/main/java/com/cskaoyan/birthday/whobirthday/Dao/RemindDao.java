package com.cskaoyan.birthday.whobirthday.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.bean.Remind;
import com.cskaoyan.birthday.whobirthday.dbHelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by whb on 2016/4/24.
 */
public class RemindDao {

    private final SQLiteDatabase db;
    private Context mContext;


    public static final String Remind_Table="Remind";

    public RemindDao(Context context) {

        DbHelper dbHelper = new DbHelper(context,1);
        db = dbHelper.getReadableDatabase();
        mContext=context;
    }

    public List<Remind> getAllReminds()
    {
        Cursor cursor = db.query(Remind_Table, null, null, null, null, null, null);
        List<Remind> localReminds = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Remind remind = new Remind();
            remind.setName(cursor.getString(cursor.getColumnIndex("name")));
            remind.setPre_theDay(cursor.getInt(cursor.getColumnIndex("pre_theDay")));
            remind.setPre_one(cursor.getInt(cursor.getColumnIndex("pre_one")));
            remind.setPre_three(cursor.getInt(cursor.getColumnIndex("pre_three")));
            remind.setPre_seven(cursor.getInt(cursor.getColumnIndex("pre_seven")));
            remind.setPre_fifteen(cursor.getInt(cursor.getColumnIndex("pre_fifteen")));
            remind.setPre_month(cursor.getInt(cursor.getColumnIndex("pre_month")));
            localReminds.add(remind);
        }
        return localReminds;
    }

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
     * @param downloadReminds
     */

    public void insert(List<BmobObject> downloadReminds) {
        for (int i=0;i<downloadReminds.size();i++)
        {
            Remind remind = (Remind) downloadReminds.get(i);
            ContentValues values = new ContentValues();
            values.put("name",remind.getName());
            values.put("pre_theDay",remind.getPre_theDay());
            values.put("pre_one",remind.getPre_three());
            values.put("pre_three",remind.getPre_three());
            values.put("pre_seven",remind.getPre_three());
            values.put("pre_fifteen",remind.getPre_three());
            values.put("pre_month",remind.getPre_three());

            db.insert(Remind_Table,null,values);
            Log.i("RemindDao",remind.getName());
        }
        Log.i("RemindDao",downloadReminds.size()+"");

    }
}
