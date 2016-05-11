package com.cskaoyan.birthday.whobirthday.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.Dao.PreDateDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.bean.PreDate;
import com.cskaoyan.birthday.whobirthday.utils.CaculateZodiac;
import com.lidroid.xutils.BitmapUtils;

import java.util.Calendar;
import java.util.List;

public class FriendDetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int UPDATE_PERSON =300 ;
    public static final int DELETE_PERSON =400 ;
    private ImageButton ib_frdetail_mind;
    private Toolbar mToolbar;

    private String mGender;
    private String mName;
    private String mPicPath;
    private String mBirthday;
    private String mPhone;
    private boolean mRemind;
    private EditText et_frdetail_name;
    private EditText et_frdetail_birthday;
    private EditText et_frdetail_phone;
    private EditText et_frdetail_zodiac;
    private ImageView iv_frdeatil_gender;
    private TextView tv_details_name;
    private PreDate remindOrNot;
    private PreDateDao preDateDao;
    private Switch sw_frdetail_mind;
    private PersonInfoDao ptDao;
    private boolean mToolbarFlag;
    private EditText et_frdetail_group;
    private String mGroup;
    Calendar calendar ;
    private GroupDao mGroupDao;
    private String[] groupArr;
    private RelativeLayout rl_fridetail_pic;
    private BitmapUtils mBU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉默认的actionbar(需要在setContentView之前)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_friend_content);

        initView();
        initData();
        initEven();

    }

    private void initView() {
        mBU = new BitmapUtils(this);
        rl_fridetail_pic = (RelativeLayout) findViewById(R.id.rl_Fridetail_pic);
        tv_details_name = (TextView) findViewById(R.id.tv_details_name_into_picture);
        et_frdetail_name = (EditText) findViewById(R.id.et_frdetail_name);
        et_frdetail_birthday = (EditText) findViewById(R.id.et_frdetail_birthday);
        et_frdetail_phone = (EditText) findViewById(R.id.et_frdetail_phone);
        et_frdetail_group = (EditText) findViewById(R.id.et_frdetail_group);
        sw_frdetail_mind = (Switch) findViewById(R.id.sw_frdetail_mind);
        ib_frdetail_mind = (ImageButton) findViewById(R.id.ib_frdetail_mind);
        et_frdetail_zodiac = (EditText) findViewById(R.id.et_frdetail_zodiac);
        iv_frdeatil_gender = (ImageView) findViewById(R.id.iv_frdeatil_gender);
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_details_friends_info);
        //set the toolbar titleText color
        mToolbar.setTitleTextColor(Color.WHITE);

        mName = getIntent().getStringExtra("name");
        mGender = getIntent().getStringExtra("gender");
        mPicPath = getIntent().getStringExtra("headpic");
        mPhone = getIntent().getStringExtra("phone");
        mGroup = getIntent().getStringExtra("group");
        mBirthday = getIntent().getStringExtra("birthday");
        mBU.configDefaultLoadFailedImage(R.drawable.contact_background);
        mBU.display(rl_fridetail_pic,mPicPath);
        ptDao = new PersonInfoDao(this);
        mRemind = ptDao.queryPersonRemind(mName);
        sw_frdetail_mind.setChecked(mRemind);
        if (mRemind){
            ib_frdetail_mind.setImageResource(R.drawable.ic_alarm_add_purple_600_24dp);
        }else {
            ib_frdetail_mind.setImageResource(R.drawable.ic_alarm_off_grey_800_24dp);
        }
        tv_details_name.setText(mName);
        et_frdetail_name.setText(mName);
        et_frdetail_birthday.setText(mBirthday);
        et_frdetail_phone.setText(mPhone);
        et_frdetail_group.setText(mGroup);
        et_frdetail_zodiac.setText(CaculateZodiac.getZodiac(mBirthday));
        if ("女".equals(mGender)){
            iv_frdeatil_gender.setImageResource(R.drawable.gender_female);
        }
    }

    private void initData(){
        calendar = Calendar.getInstance();
        mGroupDao = new GroupDao(this);
        preDateDao = new PreDateDao(FriendDetailActivity.this);
        List<String> groups =  mGroupDao.getGroups();
        groupArr = new String[groups.size()];
        for (int i=0;i<groups.size();i++){
            groupArr[i]=groups.get(i);
        }
    }

    private void initEven() {
        //给toolbar填充菜单内容
        mToolbar.inflateMenu(R.menu.details_info_toolbar_menu);
        //给toolbar菜单按钮设置监听事件
        mToolbarFlag = false;
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    //编辑按钮
                    case R.id.details_toolbar_menu_edit_info:
                        if(!mToolbarFlag){
                            //重新填充menu
                            mToolbar.getMenu().clear();
                            mToolbar.inflateMenu(R.menu.details_info_toolbar_done_menu);
                            mToolbarFlag =true;
                            setEditStatu(true);
                        }
                        break;
                    //删除按钮
                    case R.id.details_toolbar_menu_delete_info:
                        AlertDialog.Builder builder=new AlertDialog.Builder(FriendDetailActivity.this);
                        builder.setTitle("删除"+mName+"吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int i = ptDao.deletePersonbyName(mName);
                                        preDateDao.deletePreDateByName(mName);
                                        preDateDao.deleteRemindbyName(mName);
                                        if (i!=-1){
                                            Intent deleteIntent=new Intent();
                                            deleteIntent.putExtra("name",mName);
                                            setResult(DELETE_PERSON,deleteIntent);
                                            finish();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                        break;
                    //保存按钮
                    case R.id.details_toolbar_menu_done_info:
                        //重新填充menu
                        mToolbar.getMenu().clear();
                        mToolbar.inflateMenu(R.menu.details_info_toolbar_menu);
                        mToolbarFlag =false;
                        setEditStatu(false);
                        ptDao.updatePersonbyName(PersonInfoDao.NAME_COL,et_frdetail_name.getText().toString(),mName);
                        ptDao.updatePersonbyName(PersonInfoDao.ATGROUP_COL,mGroup,mName);
                        ptDao.updatePersonbyName(PersonInfoDao.BIRTHDAY_COL,mBirthday,mName);
                        ptDao.updatePersonbyName(PersonInfoDao.PHONE_COL,et_frdetail_phone.getText().toString(),mName);

                        //保存 pre_date
                        PreDateDao pdDao=new PreDateDao(FriendDetailActivity.this);
                        pdDao.updatePreDate(et_frdetail_name.getText().toString(),mBirthday);
                        final Snackbar snackbar = Snackbar.make(et_frdetail_name,"已保存",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setAction("取消",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        Intent intent=new Intent();
                        intent.putExtra("name",et_frdetail_name.getText().toString());
                        intent.putExtra("birthday",mBirthday);
                        intent.putExtra("phone",et_frdetail_phone.getText().toString());
                        intent.putExtra("group",mGroup);
                        setResult(UPDATE_PERSON,intent);
                        //性别暂时没改--------
                        break;
                }
                return true;
            }
        });

        //set the toolar navigation onclick listener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //给switch开关设置监听
        sw_frdetail_mind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mRemind=isChecked;
                    ptDao.updatePersonRemind(mName,mRemind);
                if (isChecked){
                    ib_frdetail_mind.setImageResource(R.drawable.ic_alarm_add_purple_600_24dp);
                }else {
                    ib_frdetail_mind.setImageResource(R.drawable.ic_alarm_off_grey_800_24dp);
                }
            }
        });

        ib_frdetail_mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mindDay={"生日当天","提前1天","提前3天","提前7天","提前15天","提前30天"};
                remindOrNot = preDateDao.getRemindOrNot(mName);
                boolean[] chechMind={remindOrNot.isMindBirthday(), remindOrNot.isMindOne(), remindOrNot.isMindThree(),
                remindOrNot.isMindSeven(), remindOrNot.isMindFifteen(), remindOrNot.isMindMonth()};
                AlertDialog.Builder builder=new AlertDialog.Builder(FriendDetailActivity.this);
                builder.setTitle("设置提醒时间")
                        .setMultiChoiceItems(mindDay, chechMind, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                switch (which){
                                    case 0:
                                        remindOrNot.setMindBirthday(isChecked);
                                        break;
                                    case 1:
                                        remindOrNot.setMindOne(isChecked);
                                        break;
                                    case 2:
                                        remindOrNot.setMindThree(isChecked);
                                        break;
                                    case 3:
                                        remindOrNot.setMindSeven(isChecked);
                                        break;
                                    case 4:
                                        remindOrNot.setMindFifteen(isChecked);
                                        break;
                                    case 5:
                                        remindOrNot.setMindMonth(isChecked);
                                        break;

                                }
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long rowId = preDateDao.updateMind(mName, remindOrNot);
                                if (rowId!=-1)
                                    Toast.makeText(FriendDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
    }

    /**
     * 改变edittext的状态
     * @param able
     */
    private void setEditStatu(boolean able) {
        et_frdetail_name.setEnabled(able);
        et_frdetail_birthday.setEnabled(able);
        et_frdetail_birthday.setFocusable(false);
        et_frdetail_phone.setEnabled(able);
        et_frdetail_group.setEnabled(able);
        et_frdetail_group.setFocusable(false);
//        et_frdetail_zodiac.setEnabled(able);
        if (able){
            et_frdetail_birthday.setOnClickListener(this);
            et_frdetail_group.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_frdetail_birthday:
                showSelectDateDialog();
                break;
            case R.id.et_frdetail_group:
                showSelectGroup();
                break;
        }
    }

    private void showSelectGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        int selectItem=0;
        for(int i=0;i<groupArr.length;i++){
            if (mGroup.equals(groupArr[i]))
                selectItem=i;
        }
        builder.setTitle("选择分组")
                .setSingleChoiceItems(groupArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGroup=groupArr[which];
                        et_frdetail_group.setText(mGroup);
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ptDao.updatePersonbyName(PersonInfoDao.ATGROUP_COL,mGroup,mName);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


    /**
     * 显示选择日期对话框
     */
    private void showSelectDateDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.item_date_picker,null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.dp_datePicker);
        //设置日期简略显示 否则详细显示 包括:星期周
        datePicker.setCalendarViewShown(false);
        //初始化当前日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        builder.setView(view);
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //日期格式
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                et_frdetail_birthday.setText(sb);
                mBirthday=sb.toString();
                //同时设置星座
                et_frdetail_zodiac.setText(CaculateZodiac.getZodiac(sb.toString()));
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
