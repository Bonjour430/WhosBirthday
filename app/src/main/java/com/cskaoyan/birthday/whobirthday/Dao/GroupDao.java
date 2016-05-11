package com.cskaoyan.birthday.whobirthday.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cskaoyan.birthday.whobirthday.dbHelper.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lamchaohao on 2016/4/21.
 */
public class GroupDao {

    private final SQLiteDatabase db;
    public static final String Group_Table="GroupTB";
    private List<String> groups;
    public GroupDao(Context ctx) {
        DbHelper dbHelper=new DbHelper(ctx,1);
        db = dbHelper.getReadableDatabase();
        groups=new ArrayList<>();
    }

   public void updateGroup(String oldGroupName,String newGroupName){
       ContentValues cv=new ContentValues();
       cv.put("group_name",newGroupName);
       db.update(Group_Table,cv,"group_name=?",new String[]{oldGroupName});
   }

    /**
     * 保存分组
     * @param groupName
     * @return
     */
    public long saveGroup(String groupName){
        long rowId=-1;
        ContentValues cv=new ContentValues();
        cv.put("group_name",groupName);
        rowId = db.insert(Group_Table, null, cv);
        return rowId;
    }

    /**
     * 获取所有分组
     * @return
     */
    public List<String> getGroups(){
        String sql="select group_name from "+Group_Table +";";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            String group = cursor.getString(0);
            groups.add(group);
        }
        return groups;
    }


}
