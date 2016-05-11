package com.cskaoyan.birthday.whobirthday.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.dbHelper.DbHelper;
import com.cskaoyan.birthday.whobirthday.decorators.DividerItemDecoration;
import com.cskaoyan.birthday.whobirthday.decorators.EventDecorator;
import com.cskaoyan.birthday.whobirthday.utils.DateChange;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobObject;

/**
 * 日历页面
 * Created by xcc on 2016/4/19.
 */
public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener {

    private MaterialCalendarView calendar;
    private SQLiteDatabase db;
    private List<BmobObject> mPersonInfoList;
    private List<PersonInfo> mShowBirthList;
    private int year;
    private RecyclerView recyclerview_birthdaycalendar_showmessage;
    private MyRecycleAdapter myRecycleAdapter;
    private Toolbar toolbar_birthdaycalendar_bar;
    private CollapsingToolbarLayout collapse_birthdaycalendar_slide;
    private Calendar calendarInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initView();
        initData();
        initDB();
        querydb();
        initCalendar();

        //启动标记绿点的线程
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

    }

    private void initView() {
        toolbar_birthdaycalendar_bar = (Toolbar) findViewById(R.id.toolbar_birthdaycalendar_bar);
        calendar = (MaterialCalendarView) findViewById(R.id.material_birthdaycalendar_calendar);
        recyclerview_birthdaycalendar_showmessage = (RecyclerView) findViewById(R.id.recyclerview_birthdaycalendar_showmessage);
        collapse_birthdaycalendar_slide = (CollapsingToolbarLayout) findViewById(R.id.collapse_birthdaycalendar_slide);
        mPersonInfoList = new ArrayList<>();
        mShowBirthList = new ArrayList<>();
        myRecycleAdapter = new MyRecycleAdapter();
    }

    private void initData() {
        //将toolbar设置为ActionBar
        setSupportActionBar(toolbar_birthdaycalendar_bar);
        //设置Title不可以移动
        collapse_birthdaycalendar_slide.setTitleEnabled(false);
        calendar.setOnDateChangedListener(this);
        recyclerview_birthdaycalendar_showmessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_birthdaycalendar_showmessage.setAdapter(myRecycleAdapter);
        recyclerview_birthdaycalendar_showmessage.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initDB() {
        DbHelper dbHelper = new DbHelper(this,1);
        db = dbHelper.getWritableDatabase();
    }


    /**
     * 进入日历显示的内容
     */
    private void initCalendar() {

        calendarInstance = Calendar.getInstance();
        //进页面后默认显示当前日期
        calendar.setSelectedDate(calendarInstance.getTime());
        //获取当年的年份
        year = calendarInstance.get(Calendar.YEAR);
        //进入日历显示今天的过生日的联系人
        CalendarDay calendarDay = CalendarDay.from(calendarInstance);
        showBirthdayItem(calendarDay);
    }


    /**
     * 将数据库中的生日信息存到mPersonInfoList中
     */
    private void querydb() {

//    Cursor cursor = db.rawQuery("select * from birthday", null);
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String birth = cursor.getString(cursor.getColumnIndex("birth"));
//            //添加所有过生日的联系人
//            mPersonInfoList.add(new ItemBean(name, birth));
//        }
        PersonInfoDao ptDao=new PersonInfoDao(this);

        mPersonInfoList= ptDao.queryAllPerson();
    }

    /**
     * 显示所有过生日的联系人信息
     *
     * @param date the date that was selected or unselected
     */
    private void showBirthdayItem(@NonNull CalendarDay date) {
        mShowBirthList.clear();
        for (BmobObject person : mPersonInfoList) {
            PersonInfo p=(PersonInfo)person;
            Date birthdate = DateChange.stringToDate(p.getBirth());
            CalendarDay from = CalendarDay.from(birthdate);
            if (date.equals(from)) {
                mShowBirthList.add(p);
            }
            myRecycleAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 日历被选择的时候监听事件
     *
     * @param widget   the view associated with this listener
     * @param date     the date that was selected or unselected
     * @param selected true if the day is now selected, false otherwise
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //显示当天的生日详情
        showBirthdayItem(date);
    }

    /**
     * RecycleView的适配器
     */
    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(CalendarActivity.this).inflate(R.layout.item_bir_calendar, parent, false));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_birthday_name.setText(mShowBirthList.get(position).getName());
            holder.tv_birthday_time.setText(mShowBirthList.get(position).getBirth());
        }

        @Override
        public int getItemCount() {
            //显示生日的list
            return mShowBirthList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView tv_birthday_name;
            private final TextView tv_birthday_time;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_birthday_name = (TextView) itemView.findViewById(R.id.tv_birthday_name);
                tv_birthday_time = (TextView) itemView.findViewById(R.id.tv_birthday_time);
            }
        }
    }

    /**
     * 将过生日的联系人在日历标记
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            List<CalendarDay> days = new ArrayList<>();
            ;
            for (BmobObject person : mPersonInfoList) {
                PersonInfo pInfo= (PersonInfo) person;
                Date date = DateChange.stringToDate(pInfo.getBirth());
                //将Date转换为CalendarDay类型
                CalendarDay from = CalendarDay.from(date);
                //将要标记红点的日期添加到list中
                days.add(from);
            }
            return days;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            //在日历上标记红点
            calendar.addDecorator(new EventDecorator(Color.parseColor("#00BFA5"), calendarDays));
        }
    }
}

