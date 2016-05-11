package com.cskaoyan.birthday.whobirthday.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.Dao.PreDateDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.adapter.GroupAdapter;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.utils.HeadPicSave;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * this activity is used by add friend
 * create by lamchaohao
 * 2016--4--20
 */
public class CreateFriendActivity extends AppCompatActivity implements View.OnClickListener {


    //View定义
    private ImageView ib_create_image;
    private EditText et_createFri_phone;
    private EditText et_createFri_name;
    private RadioGroup rg_createFri_gender;
    private LinearLayout ll_createFri_date;
    private ImageView iv_createFri_fromContact;

    private EditText et_createFri_date;
    //成员定义
    private Calendar calendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button bt_createFri_commit;
    private String mGender;
    private String mName;
    private String mPicPath;
    private String mGroup;
    private String mPhone;
    private Bitmap mbitMap;
    //提前提醒 成员
    private String mBirthday;
    private String mPreDate1;
    private String mPreDate3;
    private String mPreDate7;
    private String mPreDate15;
    private String mPreDate30;
    private int mMindPreTheDay;
    private int mMindPre1;
    private int mMindPre3;
    private int mMindPre7;
    private int mMindPre15;
    private int mMindPre30;

    private CheckBox cb_createFri_theday;
    private CheckBox cb_createFri_pre1;
    private CheckBox cb_createFri_pre3;
    private CheckBox cb_createFri_pre7;
    private CheckBox cb_createFri_pre15;
    private CheckBox cb_createFri_pre30;
    //常量定义
    private static final int REQUEST_CONTACT=100;
    private static final int REQUEST_CODE_PICK_IMAGE =200;
    private ImageView iv_createFri_bg;
    private Spinner sp_createFri_group;
    private RadioButton rb_createFri_female;
    private RadioButton rb_createFri_male;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_friend_content);
        initView();
        initEven();
    }


    /**
     * 初始化view
     */
    private void initView() {
        iv_createFri_bg = (ImageView) findViewById(R.id.iv_createFri_bg);
        ib_create_image = (ImageView) findViewById(R.id.ib_create_image);
        et_createFri_name = (EditText) findViewById(R.id.et_createFri_name);
        iv_createFri_fromContact = (ImageView) findViewById(R.id.iv_createFri_fromContact);
        rg_createFri_gender = (RadioGroup) findViewById(R.id.rg_createFri_gender);
        rb_createFri_female = (RadioButton) findViewById(R.id.rb_createFri_female);
        rb_createFri_male = (RadioButton) findViewById(R.id.rb_createFri_male);
        ll_createFri_date = (LinearLayout) findViewById(R.id.ll_createFri_date);
        et_createFri_date = (EditText) findViewById(R.id.et_createFri_date);
        et_createFri_phone = (EditText) findViewById(R.id.et_createFri_phone);
        bt_createFri_commit = (Button) findViewById(R.id.bt_createFri_commit);
        cb_createFri_theday = (CheckBox) findViewById(R.id.cb_createFri_theday);
        cb_createFri_pre1 = (CheckBox) findViewById(R.id.cb_createFri_pre1);
        cb_createFri_pre3 = (CheckBox) findViewById(R.id.cb_createFri_pre3);
        cb_createFri_pre7 = (CheckBox) findViewById(R.id.cb_createFri_pre7);
        cb_createFri_pre15 = (CheckBox) findViewById(R.id.cb_createFri_pre15);
        cb_createFri_pre30 = (CheckBox) findViewById(R.id.cb_createFri_pre30);
        sp_createFri_group = (Spinner) findViewById(R.id.sp_createFri_group);
        calendar = Calendar.getInstance();

    }

    private void initEven() {
        final GroupDao groupDao=new GroupDao(this);
        
        final List<String> groups = groupDao.getGroups();
        ib_create_image.setOnClickListener(this);
        et_createFri_name.setOnClickListener(this);
        iv_createFri_fromContact.setOnClickListener(this);
        rg_createFri_gender.setOnClickListener(this);
        rb_createFri_female.setOnClickListener(this);
        rb_createFri_male.setOnClickListener(this);
        ll_createFri_date.setOnClickListener(this);
        et_createFri_date.setOnClickListener(this);
        bt_createFri_commit.setOnClickListener(this);
        sp_createFri_group.setAdapter(new GroupAdapter(this));
        sp_createFri_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGroup=groups.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_create_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.et_createFri_name:
                break;
            case R.id.iv_createFri_fromContact:
                Intent openContact=new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                openContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(openContact,REQUEST_CONTACT);
                break;
            case R.id.rb_createFri_female:
                mGender="女";
                break;
            case R.id.rb_createFri_male:
                mGender="男";
                break;
            case R.id.ll_createFri_date:
                showSelectDateDialog();
                break;
            case R.id.et_createFri_date:
                showSelectDateDialog();
                break;
            case R.id.bt_createFri_commit:
                commit();
                break;
        }
    }
    //用于压缩图片
    private void bitmapFactory(Uri uri,ImageView imageView){
        File picFile=new File(uri.getPath());
        mPicPath=picFile.getAbsolutePath();
        long length = picFile.length();
        Bitmap bm=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (length>204800){
            //如果图片大于200kb，则进行压缩
            options.inSampleSize=2;
            bm = BitmapFactory.decodeFile(mPicPath,options);
        }else{
            bm = BitmapFactory.decodeFile(mPicPath);
        }
        mbitMap=bm;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==REQUEST_CONTACT){
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                if(cursor.moveToFirst()) {
                    // Retrieve the phone number from the NUMBER column
                    int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String number = cursor.getString(numberColumn);
                    String name = cursor.getString(nameColumn);
                    et_createFri_name.setText(name);
                    et_createFri_phone.setText(number);
                }
            }else if (requestCode==REQUEST_CODE_PICK_IMAGE){
                Uri selectedImage = data.getData();
//                BitmapUtils bu=new BitmapUtils(this);
//                bu.display(iv_createFri_bg,selectedImage.toString());
//                bu.display(ib_create_image,selectedImage.toString());
                bitmapFactory(selectedImage,iv_createFri_bg);
                bitmapFactory(selectedImage,ib_create_image);
                }
            }
    }

    /**
     * 显示选择日期对话框
     */
    private void showSelectDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateFriendActivity.this);
        View view = View.inflate(CreateFriendActivity.this,R.layout.item_date_picker,null);
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
                et_createFri_date.setText(sb);
                mYear = datePicker.getYear();
                mMonth = datePicker.getMonth();
                mDay = datePicker.getDayOfMonth();
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

    private void caculateMindDay(){
        if (cb_createFri_theday.isChecked()){
            mBirthday=et_createFri_date.getText().toString();
            mMindPreTheDay=PreDateDao.REMIND;
        }
        if (cb_createFri_pre1.isChecked()){
            mMindPre1= PreDateDao.REMIND;
        }
        if (cb_createFri_pre3.isChecked()){
            mMindPre3= PreDateDao.REMIND;
        }
        if (cb_createFri_pre7.isChecked()){
            mMindPre7= PreDateDao.REMIND;
        }
        if (cb_createFri_pre15.isChecked()){
            mMindPre15= PreDateDao.REMIND;
        }
        if (cb_createFri_pre30.isChecked()){
            mMindPre30= PreDateDao.REMIND;
        }
        mBirthday=et_createFri_date.getText().toString();
        mPreDate1 = getMinusDate(mMonth+1, mDay, 1);
        mPreDate3 = getMinusDate(mMonth+1, mDay, 3);
        mPreDate7 = getMinusDate(mMonth+1, mDay, 7);
        mPreDate15 = getMinusDate(mMonth+1,mDay, 15);
        mPreDate30 = getMinusDate (mMonth+1,mDay, 30);

    }

    /**
     * 计算提前多少天的日期是多少
      * @param preMonth
     * @param preDay
     * @param howLongDay
     * @return 提前的日期
     */
    private String getMinusDate(int preMonth,int preDay,int howLongDay){
        if (preDay<=howLongDay){
            preMonth=preMonth-1;
            if (preMonth<1){
                preMonth=12;
            }
            int tempDay=howLongDay-preDay;
            if (isBigMonth(preMonth)){//大月
                preDay=31-tempDay;
            }else{//小月
                if (preMonth==2){
                    int year = calendar.get(Calendar.YEAR);
                    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)){//闰年
                        preDay=29-tempDay;
                    }else{
                        preDay=28-tempDay;
                    }

                }else{//除了2月
                    preDay=30-tempDay;
                }
            }
        }else{
            preDay=preDay-howLongDay;
        }
        return preMonth+"-"+preDay;
    }

    /**
     * 判断某月份是大月还是小月
     * @param month
     * @return
     */
    private boolean isBigMonth(int month){
        int[] bigMonth={1,3,5,7,8,10,12};
        boolean isBigMonth=false;
        for (int i=0;i<bigMonth.length;i++){
            if (bigMonth[i]==month)
                isBigMonth=true;
        }
        return isBigMonth;
    }

    /**
     * 保存到数据库
     */
    private void commit() {
        caculateMindDay();
        mName=et_createFri_name.getText().toString();
        mBirthday=et_createFri_date.getText().toString();
        mPhone=et_createFri_phone.getText().toString();
        int checkedRadioButtonId = rg_createFri_gender.getCheckedRadioButtonId();
        if (checkedRadioButtonId==R.id.rb_createFri_female){
            mGender="女";
        }else{
            mGender="男";
        }
        if (mName.isEmpty()){
//            Toast.makeText(this,"ta的名字不能为空哦",Toast.LENGTH_LONG).show();
            Snackbar.make(et_createFri_phone,"ta的名字不能为空哦",Snackbar.LENGTH_LONG).show();
            return;
        }else if (mBirthday.isEmpty()){
//            Toast.makeText(this,"ta的生日不能为空哦",Toast.LENGTH_LONG).show();
            Snackbar.make(et_createFri_phone,"ta的生日不能为空哦",Snackbar.LENGTH_LONG).show();

            return;
        }
        if (mbitMap!=null){
            mPicPath = HeadPicSave.saveBitMap(this, mName, mbitMap);
        }
        PersonInfoDao ptDao=new PersonInfoDao(this);
        PreDateDao preDateDao=new PreDateDao(this);
        PersonInfo person=new PersonInfo(mName,mGender,mBirthday,mPhone,mPicPath,mGroup,PersonInfoDao.MIND);
        long personRowId = ptDao.insertPerson(person);

        long preDateRowId = preDateDao.savePreDate(mName,mPreDate1,mPreDate3,mPreDate7,mPreDate15,mPreDate30);
        long mindRowId =preDateDao.saveMind(mName,mMindPreTheDay,mMindPre1,mMindPre3,mMindPre7,mMindPre15,mMindPre30);
        if (personRowId!=-1&&preDateRowId!=-1&&mindRowId!=-1){
            Intent intent=new Intent();
            //添加成功后返回一个联系人名字，让mainactivity根据此名字去查询，然后在allContancFragment中更新数据源
            intent.putExtra("name",mName);
            setResult(RESULT_OK,intent);
            this.finish();
        }else{
            Snackbar.make(et_createFri_phone,mName+"已存在与您的生日名单中",Snackbar.LENGTH_LONG).show();
//            Toast.makeText(CreateFriendActivity.this, mName+"已存在与您的生日名单中", Toast.LENGTH_SHORT).show();
        }

    }
}
