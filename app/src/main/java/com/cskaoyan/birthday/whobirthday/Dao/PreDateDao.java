package com.cskaoyan.birthday.whobirthday.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cskaoyan.birthday.whobirthday.bean.PreDate;
import com.cskaoyan.birthday.whobirthday.dbHelper.DbHelper;
import com.cskaoyan.birthday.whobirthday.utils.CaculatePreDate;

/**
 * Created by lamchaohao on 2016/4/21.
 */
public class PreDateDao {

    private final SQLiteDatabase db;
    //提醒
    public static final int REMIND=1;
    //不提醒
    public static final int NOT_REMIND=0;

    public static final String NAME_COL="name";

    public PreDateDao(Context ctx) {
        DbHelper dbHelper=new DbHelper(ctx,1);
        db = dbHelper.getReadableDatabase();
    }

    /**
     * 保存到predate表中，为某人生日的提前日期
     * @param name
     * @param pre1
     * @param pre3
     * @param pre7
     * @param pre15
     * @param pre30
     * @return
     */
    public long savePreDate(String name,String pre1,String pre3,String pre7,String pre15,String pre30){
        long rowid=-1;
        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("pre_one",pre1);
        cv.put("pre_three",pre3);
        cv.put("pre_seven",pre7);
        cv.put("pre_fifteen",pre15);
        cv.put("pre_month",pre30);
        rowid = db.insert(DbHelper.PreDate_Table, null, cv);
        return rowid;
    }

    public long insertPreDate(String name,String newBirthday){
        String regularExpression = "-";
        String[] split = newBirthday.split(regularExpression);
        int month=-1;
        int day=-1;
        if (split.length==2){
            month= Integer.parseInt(split[0]);
            day= Integer.parseInt(split[1]);
        }else if (split.length==3){
            month= Integer.parseInt(split[1]);
            day= Integer.parseInt(split[2]);
        }

        String preOne = CaculatePreDate.getMinusDate(month, day, 1);
        String preThree = CaculatePreDate.getMinusDate(month, day, 3);
        String preSeven = CaculatePreDate.getMinusDate(month, day, 7);
        String preFifteen = CaculatePreDate.getMinusDate(month, day, 15);
        String preMonth = CaculatePreDate.getMinusDate(month, day, 30);

        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("pre_one",preOne);
        cv.put("pre_three",preThree);
        cv.put("pre_seven",preSeven);
        cv.put("pre_fifteen",preFifteen);
        cv.put("pre_month",preMonth);
        long rowAff=-1;
        rowAff = db.insert(DbHelper.PreDate_Table, null, cv);
        return rowAff;
    }

    /**
     * 更新predate
     * @param name
     * @param newBirthday
     * @return
     */
    public int updatePreDate(String name,String newBirthday){
        String regularExpression = "-";
        String[] split = newBirthday.split(regularExpression);
        int month=-1;
        int day=-1;
        if (split.length==2){
            month= Integer.parseInt(split[0]);
            day= Integer.parseInt(split[1]);
        }else if (split.length==3){
            month= Integer.parseInt(split[1]);
            day= Integer.parseInt(split[2]);
        }

        String preOne = CaculatePreDate.getMinusDate(month, day, 1);
        String preThree = CaculatePreDate.getMinusDate(month, day, 3);
        String preSeven = CaculatePreDate.getMinusDate(month, day, 7);
        String preFifteen = CaculatePreDate.getMinusDate(month, day, 15);
        String preMonth = CaculatePreDate.getMinusDate(month, day, 30);

        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("pre_one",preOne);
        cv.put("pre_three",preThree);
        cv.put("pre_seven",preSeven);
        cv.put("pre_fifteen",preFifteen);
        cv.put("pre_month",preMonth);
        int rowAff=-1;
        rowAff = db.update(DbHelper.PreDate_Table, cv, "name=?", new String[]{name});
        return rowAff;
    }

    /**
     * 某人生日前提前期是否提醒  1代表提醒，0代表不提醒
     * @param name
     * @param pre1
     * @param pre3
     * @param pre7
     * @param pre15
     * @param pre30
     * @return
     */
    public long saveMind(String name,int preTheday,int pre1,int pre3,int pre7,int pre15,int pre30){

        long rowid=-1;
        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("pre_theDay",preTheday);
        cv.put("pre_one",pre1);
        cv.put("pre_three",pre3);
        cv.put("pre_seven",pre7);
        cv.put("pre_fifteen",pre15);
        cv.put("pre_month",pre30);
        rowid = db.insert(DbHelper.Remind_Table, null, cv);
        return rowid;
    }
    public long updateMind(String name,PreDate pd){

        int preTheday=booleanToInt(pd.isMindBirthday());
        int pre1=booleanToInt(pd.isMindOne());
        int pre3=booleanToInt(pd.isMindThree());
        int pre7=booleanToInt(pd.isMindSeven());
        int pre15=booleanToInt(pd.isMindFifteen());
        int pre30=booleanToInt(pd.isMindMonth());

        long rowid=-1;
        ContentValues cv=new ContentValues();
//        cv.put("name",name);
        cv.put("pre_theDay",preTheday);
        cv.put("pre_one",pre1);
        cv.put("pre_three",pre3);
        cv.put("pre_seven",pre7);
        cv.put("pre_fifteen",pre15);
        cv.put("pre_month",pre30);
        rowid = db.update(DbHelper.Remind_Table,cv,NAME_COL+"=?",new String[]{name});
        return rowid;
    }

    /**
     * 删除Remind表信息
     * @param name
     * @return
     */
    public int deleteRemindbyName(String name){
        int rows=-1;
        rows = db.delete(DbHelper.Remind_Table, NAME_COL+"=?", new String[]{name});
        return rows;
    }

    /**
     * 删除PreDate_Table信息
     * @param name
     * @return
     */
    public int deletePreDateByName(String name){
        int rows=-1;
        rows = db.delete(DbHelper.PreDate_Table, NAME_COL+"=?", new String[]{name});
        return rows;
    }

    public PreDate getRemindOrNot(String name){
        String sql="select * from "+DbHelper.Remind_Table+" where name=? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        cursor.moveToNext();
            int preTheday = cursor.getInt(1);
            int preOne = cursor.getInt(2);
            int preThree = cursor.getInt(3);
            int preSeven = cursor.getInt(4);
            int preFifteen = cursor.getInt(5);
            int preMonth = cursor.getInt(6);
        PreDate preMind=new PreDate(intToBoolean(preTheday),intToBoolean(preOne),intToBoolean(preThree),intToBoolean(preSeven),intToBoolean(preFifteen),intToBoolean(preMonth));
        return preMind;
    }

    /**
     * 由int类型的是否提醒转换成boolean
     * @param remind
     * @return
     */
    public boolean intToBoolean(int remind){
        if (remind==0)
            return false;
        else
            return true;
    }

    /**
     * 有boolean类型的提醒转化成int存入数据库
     * @param remind
     * @return
     */
    public int booleanToInt(boolean remind){
        if (remind)
            return REMIND;
        else
            return NOT_REMIND;
    }
}
