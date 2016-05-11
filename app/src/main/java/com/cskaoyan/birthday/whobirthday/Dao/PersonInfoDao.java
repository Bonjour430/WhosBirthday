package com.cskaoyan.birthday.whobirthday.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.dbHelper.DbHelper;
import com.cskaoyan.birthday.whobirthday.utils.DayOfYear;
import com.cskaoyan.birthday.whobirthday.utils.RemindFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.bmob.v3.BmobObject;

/**
 * Created by lamchaohao on 2016/4/24.
 *
 */
public class PersonInfoDao {
    public static boolean  PersonAndMindflag;
    private final SQLiteDatabase db;
    private final List<String> colList;
    public static final String ID_COL="id";
    public static final String NAME_COL="name";
    public static final String GENDER_COL="gender";
    public static final String BIRTHDAY_COL="birthday";
    public static final String PHONE_COL="phone";
    public static final String IMAGEPATH_COL="imagePath";
    public static final String ATGROUP_COL="atGroup";
    public static final String REMIND_COL="remind";
    public static final String OBJECTID_COL="objectId";
    public static final String UPDATETIME_COL="updateTime";
    public static final String CREATETIME_COL="createTime";
    public static final String USERNAME_COL="userName";
    public static final int MIND=1;
    public static final int NOTMIND=0;

    Context mContext;
    private final SimpleDateFormat sdf;

    public PersonInfoDao(Context context) {
            DbHelper dbHelper = new DbHelper(context,1);
            db = dbHelper.getReadableDatabase();
        this.mContext=context;
        colList = new ArrayList<>();

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Cursor cursor = db.rawQuery("select * from " + DbHelper.Person_Table + ";",null );
        int j=0;
        while(cursor.moveToNext()){
            for(int i=j;i<10;i++) {
                String columnName = cursor.getColumnName(i);
                colList.add(columnName);
                j=i;
            }
        }
    }

    /**
     * 2016/4/24 1:19 修改
     * 插入联系人
     * @param name
     * @param gender
     * @param birthday
     * @param atGroup
     * @param picPath
     * @param phone
     * @param remind
     * @return
     */
    public long insertPerson(String name,String gender,String birthday,String atGroup,String picPath,String phone,int remind){
        long arow=-1;
        ContentValues values = new ContentValues();
        values.put(NAME_COL,name);
        values.put(GENDER_COL,gender);
        values.put(BIRTHDAY_COL,birthday);
        values.put(PHONE_COL,phone);
        values.put(IMAGEPATH_COL,picPath);
        values.put(ATGROUP_COL,atGroup);
        values.put(REMIND_COL,remind);  //提醒否
        values.put(USERNAME_COL, MyApplication.getCurrentUsername(mContext));//对应用户的用户名
        //得到时间
        TimeZone timeZone = sdf.getTimeZone();
        Calendar instance = Calendar.getInstance(timeZone);
        Date time = instance.getTime();
        String format = sdf.format(time);
        //第一次插入，更新时间也是为createat时间
        values.put(UPDATETIME_COL, format);
        values.put(CREATETIME_COL,format);


        arow = db.insert(DbHelper.Person_Table, null, values);
        return arow;
    }

    /**
     * 插入数据
     * @param person
     * @return
     */
    public long insertPerson(PersonInfo person){
        long arow=-1;
        ContentValues values = new ContentValues();
        values.put(NAME_COL,person.getName());
        values.put(GENDER_COL,person.getGender());
        values.put(BIRTHDAY_COL,person.getBirth());
        values.put(PHONE_COL,person.getPhone());
        values.put(IMAGEPATH_COL,person.getImage());
        values.put(ATGROUP_COL,person.getAtGroup());
        values.put(REMIND_COL,person.getRemind());  //提醒否
        values.put(OBJECTID_COL,person.getObjectId());
        values.put(USERNAME_COL, MyApplication.getCurrentUsername(mContext));//对应用户的用户名
        //插入时间
        values.put(CREATETIME_COL,person.getCreateTime());
        values.put(UPDATETIME_COL,person.getUpdateTime());
        arow = db.insert(DbHelper.Person_Table, null, values);

        return arow;
    }

    /**
     * 更新联系人某一字段的值
     * @param col 哪一列字段
     * @param value 哪一列字段的值 使用本类的public static final值
     * @param option where option=? 比如 where phone=?
     * @param typeValue 代表？的值
     * @return the number of rows affected
     */
    public int updatePerson(String col,String value,String option,String typeValue){
        int rows=-1;

        if(colList.contains(col)&&colList.contains(option)) {
            ContentValues values = new ContentValues();
            values.put(col, value);
            TimeZone timeZone = sdf.getTimeZone();
            Calendar instance = Calendar.getInstance(timeZone);
            Date time = instance.getTime();
            String format = sdf.format(time);
            //自助插入更新时间
            values.put(UPDATETIME_COL, format);
            rows = db.update(DbHelper.Person_Table, values, option + "=?", new String[]{typeValue});
        }
        return rows;
    }

    /**
     * 根据联系人名字来修改信息
     * @param col 修改哪一列，如gender，可以使用本类的public static final 值 GENDER_COL
     * @param value
     * @param name
     * @return
     */
    public int updatePersonbyName(String col,String value,String name){
        int rows=-1;

        if(colList.contains(col)) {  //正则表达式 确认col是否是列名
            TimeZone timeZone = sdf.getTimeZone();
            Calendar instance = Calendar.getInstance(timeZone);
            Date time = instance.getTime();
            String format = sdf.format(time);
            ContentValues values = new ContentValues();
            //更新时间
            values.put(UPDATETIME_COL, format);
            values.put(col, value);
            rows = db.update(DbHelper.Person_Table, values, NAME_COL+"=?", new String[]{name});
        }
        return rows;
    }

    /**
     * 通过 传入person来更新数据库的记录
     * @param person
     * @return
     */
    public int updatePersonByName(PersonInfo person){
        int rows=-1;
        PersonInfo existPerson = queryPerson(person.getName());
        if (existPerson!=null){
            ContentValues values = new ContentValues();
            values.put(GENDER_COL,person.getGender());
            values.put(BIRTHDAY_COL,person.getBirth());
            values.put(PHONE_COL,person.getPhone());
            values.put(IMAGEPATH_COL,person.getImage());
            values.put(ATGROUP_COL,person.getAtGroup());
            values.put(REMIND_COL,person.getRemind());  //提醒否

            TimeZone timeZone = sdf.getTimeZone();
            Calendar instance = Calendar.getInstance(timeZone);
            Date time = instance.getTime();
            String format = sdf.format(time);
            //更新时间
            values.put(UPDATETIME_COL, format);
           rows = db.update(DbHelper.Person_Table, values, NAME_COL+"=?", new String[]{person.getName()});
        }
        return rows;

    }

    /**
     * 删除某一记录，慎用
     * delete from PersonInfo where col=value
     * @param col
     * @param value
     * @return
     */
    public int deletePerson(String col,String value){
        int rows=-1;
        if(colList.contains(col)) {
            rows = db.delete(DbHelper.Person_Table, col + "=?", new String[]{value});
        }
        return rows;
    }

    /**
     * 根据某人名字删除该记录
     * @param name
     * @return
     */
    public int deletePersonbyName(String name){
        int rows=-1;
       rows = db.delete(DbHelper.Person_Table, NAME_COL+"=?", new String[]{name});
        return rows;
    }

    /**
     * 根据联系人名字查询信息
     * @param personName
     * @return
     */
    public PersonInfo queryPerson(String personName){

        PersonInfo personInfo=null;

        String sql="select * from "+DbHelper.Person_Table+" where "+ NAME_COL +"=? and "+USERNAME_COL+"=?;";
        Cursor cursor = db.rawQuery(sql,new String[]{personName,MyApplication.getCurrentUsername(mContext)});
            int ID_COLIndex = cursor.getColumnIndex(ID_COL);
            int NAME_COLIndex = cursor.getColumnIndex(NAME_COL);
            int GENDER_COLIndex = cursor.getColumnIndex(GENDER_COL);
            int BIRTHDAY_COLIndex = cursor.getColumnIndex(BIRTHDAY_COL);
            int PHONE_COLIndex = cursor.getColumnIndex(PHONE_COL);
            int IMAGEPATH_COLIndex = cursor.getColumnIndex(IMAGEPATH_COL);
            int ATGROUP_COLIndex = cursor.getColumnIndex(ATGROUP_COL);
            int REMIND_COLIndex = cursor.getColumnIndex(REMIND_COL);
            int USERNAME_COLIndex = cursor.getColumnIndex(USERNAME_COL);
            int OBJECTID_COLIndex = cursor.getColumnIndex(OBJECTID_COL);
            int CREATETIME_COLIndex = cursor.getColumnIndex(CREATETIME_COL);
            int UPDATETIME_COLIndex = cursor.getColumnIndex(UPDATETIME_COL);

        while (cursor.moveToNext()){
            int id = cursor.getInt(ID_COLIndex);
            String name = cursor.getString(NAME_COLIndex);
            String gender = cursor.getString(GENDER_COLIndex);
            String birth = cursor.getString(BIRTHDAY_COLIndex);
            String phone = cursor.getString(PHONE_COLIndex);
            String image = cursor.getString(IMAGEPATH_COLIndex);
            String atGroup = cursor.getString(ATGROUP_COLIndex);
            int remind = cursor.getInt(REMIND_COLIndex);
            String username = cursor.getString(USERNAME_COLIndex);
            String objectid = cursor.getString(OBJECTID_COLIndex);
            String creatTime = cursor.getString(CREATETIME_COLIndex);
            String updateTime = cursor.getString(UPDATETIME_COLIndex);
            personInfo= new PersonInfo(id,name,gender,birth,phone,image,atGroup,remind,username,creatTime,updateTime);
            personInfo.setObjectId(objectid);
        }

        return personInfo;
    }

    /**
     * 获取所有联系人信息
     * @return
     */
    public List<BmobObject> queryAllPerson(){

        List<BmobObject> allList=new ArrayList<>();
        String sql="select * from " + DbHelper.Person_Table+ " where "+USERNAME_COL+" =? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{MyApplication.getCurrentUsername(mContext)});
        int ID_COLIndex = cursor.getColumnIndex(ID_COL);
        int NAME_COLIndex = cursor.getColumnIndex(NAME_COL);
        int GENDER_COLIndex = cursor.getColumnIndex(GENDER_COL);
        int BIRTHDAY_COLIndex = cursor.getColumnIndex(BIRTHDAY_COL);
        int PHONE_COLIndex = cursor.getColumnIndex(PHONE_COL);
        int IMAGEPATH_COLIndex = cursor.getColumnIndex(IMAGEPATH_COL);
        int ATGROUP_COLIndex = cursor.getColumnIndex(ATGROUP_COL);
        int REMIND_COLIndex = cursor.getColumnIndex(REMIND_COL);
        int USERNAME_COLIndex = cursor.getColumnIndex(USERNAME_COL);
        int OBJECTID_COLIndex = cursor.getColumnIndex(OBJECTID_COL);
        int CREATETIME_COLIndex = cursor.getColumnIndex(CREATETIME_COL);
        int UPDATETIME_COLIndex = cursor.getColumnIndex(UPDATETIME_COL);

        while (cursor.moveToNext()){
            int id = cursor.getInt(ID_COLIndex);
            String name = cursor.getString(NAME_COLIndex);
            String gender = cursor.getString(GENDER_COLIndex);
            String birth = cursor.getString(BIRTHDAY_COLIndex);
            String phone = cursor.getString(PHONE_COLIndex);
            String image = cursor.getString(IMAGEPATH_COLIndex);
            String atGroup = cursor.getString(ATGROUP_COLIndex);
            int remind = cursor.getInt(REMIND_COLIndex);
            String username = cursor.getString(USERNAME_COLIndex);
            String objectid = cursor.getString(OBJECTID_COLIndex);
            String createTime = cursor.getString(CREATETIME_COLIndex);
            String updateTime = cursor.getString(UPDATETIME_COLIndex);
            PersonInfo personInfo= new PersonInfo(id,name,gender,birth,phone,image,atGroup,remind,username,createTime,updateTime);
            personInfo.setObjectId(objectid);
            allList.add(personInfo);
        }
        return allList;
    }

    /**
     * 根据联系人名更新remind，设置提醒
     * @param name
     * @param remind
     */
    public void updatePersonRemind(String name,boolean remind){

        ContentValues cv=new ContentValues();
        cv.put(REMIND_COL, RemindFormat.booleanToInt(remind));
        db.update(DbHelper.Person_Table,cv,NAME_COL+"=?",new String[]{name});

    }

    /**
     * 根据联系人名字查询设置的提醒
     * @param personName
     * @return
     */
    public boolean queryPersonRemind(String personName){
        String sql="select "+REMIND_COL+" from "+DbHelper.Person_Table +" where "+NAME_COL+"=? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{personName});
        cursor.moveToNext();
        int remind = cursor.getInt(0);
        boolean b = RemindFormat.intToBoolean(remind);
        return b;
    }

    /**
     * 按组名获取该组所有成员
     * @param groupName
     */
    public List<BmobObject> getPeopleByGroup(String groupName){

        List<BmobObject> memberList=new ArrayList<>();

        String sql="select * from "+DbHelper.Person_Table+" where "+ ATGROUP_COL +"=? and "+USERNAME_COL+" =? ;";
        Cursor cursor = db.rawQuery(sql, new String[]{groupName,MyApplication.getCurrentUsername(mContext)});
            //获取所有列的索引号
            int ID_COLIndex = cursor.getColumnIndex(ID_COL);
            int NAME_COLIndex = cursor.getColumnIndex(NAME_COL);
            int GENDER_COLIndex = cursor.getColumnIndex(GENDER_COL);
            int BIRTHDAY_COLIndex = cursor.getColumnIndex(BIRTHDAY_COL);
            int PHONE_COLIndex = cursor.getColumnIndex(PHONE_COL);
            int IMAGEPATH_COLIndex = cursor.getColumnIndex(IMAGEPATH_COL);
            int ATGROUP_COLIndex = cursor.getColumnIndex(ATGROUP_COL);
            int REMIND_COLIndex = cursor.getColumnIndex(REMIND_COL);
            int USERNAME_COLIndex = cursor.getColumnIndex(USERNAME_COL);
            int CREATETIME_COLIndex = cursor.getColumnIndex(CREATETIME_COL);
            int UPDATETIME_COLIndex = cursor.getColumnIndex(UPDATETIME_COL);
        while(cursor.moveToNext()){
            int id = cursor.getInt(ID_COLIndex);
            String name = cursor.getString(NAME_COLIndex);
            String gender = cursor.getString(GENDER_COLIndex);
            String birth = cursor.getString(BIRTHDAY_COLIndex);
            String phone = cursor.getString(PHONE_COLIndex);
            String image = cursor.getString(IMAGEPATH_COLIndex);
            String atGroup = cursor.getString(ATGROUP_COLIndex);
            int remind = cursor.getInt(REMIND_COLIndex);
            String username = cursor.getString(USERNAME_COLIndex);
            String createTime = cursor.getString(CREATETIME_COLIndex);
            String updateTime = cursor.getString(UPDATETIME_COLIndex);

            PersonInfo personInfo= new PersonInfo(id,name,gender,birth,phone,image,atGroup,remind,username,createTime,updateTime);

            //加入集合
            memberList.add(personInfo);
        }

        return memberList;
    }
/*
        caoyafei
     */

    public  String  queryfromPersonAndMind(){
        //得到PersonDetails 的结果集
        String[]  columns1 = {"name","birthday","remind"};
        Cursor personCursor = db.query("PersonInfo", columns1, null, null, null, null, null);
        //初始化 PersonAndMindflag
        PersonAndMindflag=false;
        String whosName="";

        if(personCursor != null && personCursor.getCount() > 0) {
            //循环里面在嵌套一层循环
            while(personCursor.moveToNext()) {        // 移动到下一行, 如果移动到最后一行, 继续往下移动时, 返回false
                String _name= personCursor.getString(personCursor.getColumnIndex("name"));
                Log.i("_name 的值：",_name +"");

                String _birth= personCursor.getString(personCursor.getColumnIndex("birthday"));
                Log.i("_birth/的值：",_birth+"");
                //int _remind = personCursor.getInt(7);
                int _remind=personCursor.getInt(personCursor.getColumnIndex("remind"));
                Log.i("_remind/的值：",_remind+"");
                //首先要计算此人生日距离今天的天数
                //利用当次循环查询出来的生日去计算
                int dayNum = DayOfYear.getHowManyDayLeft(_birth);
                Log.i("dayNum/的值：",dayNum+"");


                String[] columns = {"pre_theDay", "pre_one", "pre_three", "pre_seven", "pre_fifteen", "pre_month"};
                String selection = "name = ?";
                //二者的表关联起来就是通过名字，这里情况特殊，不能有重名，所以可以名字作为主键
                String[] selectionArgs = {_name};

                Cursor mindCursor = db.query("Remind", columns, selection, selectionArgs, null, null, null);
                if (mindCursor != null && mindCursor.getCount() > 0) {

                    while (mindCursor.moveToNext()) {  // true 代表移动成功, false 移动失败
                        int _pre_theDay = mindCursor.getInt(mindCursor.getColumnIndex("pre_theDay"));
                        Log.i("_pre_theDay/的值：",_pre_theDay+"");
                        int _pre_one = mindCursor.getInt(mindCursor.getColumnIndex("pre_one"));
                        Log.i("_pre_one/的值：",_pre_one+"");
                        int _pre_three = mindCursor.getInt(mindCursor.getColumnIndex("pre_three"));
                        int _pre_seven = mindCursor.getInt(mindCursor.getColumnIndex("pre_seven"));
                        int _pre_fifteen = mindCursor.getInt(mindCursor.getColumnIndex("pre_fifteen"));
                        int _pre_month = mindCursor.getInt(mindCursor.getColumnIndex("pre_month"));
                        //首先判断是否满足30天
                        if(_remind==1&&_pre_month==1&&dayNum==30){
                            PersonAndMindflag=true;
                            Log.i("30天的被触发","好吧！");
                            //触发响铃操作
                            break;
                        }else if(_remind==1&&_pre_fifteen==1&&dayNum==15){
                            PersonAndMindflag=true;
                            Log.i("15天的被触发","好吧！");
                            break;
                            //触发响铃操作
                        }else if(_remind==1&&_pre_seven==1&&dayNum==7){
                            PersonAndMindflag=true;
                            Log.i("7天的被触发","好吧！");
                            break;
                            //触发响铃操作
                        }else if(_remind==1&&_pre_three==1&&dayNum==3){
                            PersonAndMindflag=true;
                            Log.i("3天的被触发","好吧！");
                            break;
                            //触发响铃操作
                        }else if(_remind==1&& _pre_one==1&&dayNum==1){
                            PersonAndMindflag=true;
                            Log.i("1天的被触发","好吧！");
                            break;
                            //触发响铃操作
                        }else if(_remind==1&&_pre_theDay==1&&dayNum==0){
                            PersonAndMindflag=true;
                            whosName=_name;
                            Log.i("0天的被触发","好吧！");
                            break;
                            //触发响铃操作
                        }else{
                            Log.i("default被触发","好吧！");
                            continue;
                        }


                    }

                }
                mindCursor.close();
            }

            personCursor.close();

        }
        return whosName;
    }
}
