package com.cskaoyan.birthday.whobirthday.activity;




import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;

import android.widget.TextView;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.adapter.TabFragmentAdapter;
import com.cskaoyan.birthday.whobirthday.application.MyApplication;
import com.cskaoyan.birthday.whobirthday.bean.Group;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.bean.Remind;
import com.cskaoyan.birthday.whobirthday.bean.UserInfo;
import com.cskaoyan.birthday.whobirthday.fragment.AllContactFragment;
import com.cskaoyan.birthday.whobirthday.fragment.GroupFragment;
import com.cskaoyan.birthday.whobirthday.utils.SyncUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**主页面
 * create by ZhouJiChang
 */
public class MainActivity extends AppCompatActivity {

    public static final int CREATE_REQUEST_CODE = 104;
    public static final int DETAIL_REQUEST_CODE = 105;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private FloatingActionButton mFloatingButton;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;
    private PersonInfoDao ptDao;
    private SharedPreferences configsp;
    private NavigationView navigation_view;
    private UserInfo mUser;
    private boolean backFlag;
    private long firstTime;
    private long lastTime;
    private FloatingActionMenu mFaMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉默认的actionbar(需要在setContentView之前)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        /**
         * 检测是否设置了密码锁,设置了密码之后，必须输入正确密码才能进入主页面
         */
        configsp = MyApplication.configsp;
        String password = configsp.getString("password","***");
        boolean login = configsp.getBoolean("login", false);
        if (!password.equals("***")&&!login)
        {
            SharedPreferences.Editor edit = configsp.edit();
            edit.putBoolean("login",true);
            edit.commit();
            startActivity(new Intent(this,InputPasswordActivity.class));
        }
        //如果用户没有登录。则弹出登录
        String currentUsername = MyApplication.getCurrentUsername(this);
        if (!currentUsername.isEmpty()){
            initView();
            initEven();
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时候修改sharedpreference的状态
        configsp.edit().putBoolean("login",false).commit();
    }

    private void initView() {
//        FloatingActionMenu
        //右侧导航栏
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        //给toolbar填充菜单内容
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mFaMenu = (FloatingActionMenu) findViewById(R.id.fam_main_add);
        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab_main_add);
        mViewPager = (ViewPager) findViewById(R.id.vp_main_content);
        mTabLayout = (TabLayout) findViewById(R.id.tl_main_tabs);

        /**
         * 如果本地磁盘中有缓存的当前用户，则直接使用缓存的当前用户登录，不必用户每次都输入用户名和密码
         */
        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        if(bmobUser != null){
            // 允许用户使用应用

        }

    }

    private void initEven() {
        View headerView = navigation_view.getHeaderView(0);
        TextView tv_header_accout = (TextView) headerView.findViewById(R.id.tv_header_accout);
        backFlag=false;
        //拿到当前用户
        mUser = BmobUser.getCurrentUser(this,UserInfo.class);
        if (mUser!=null){
            tv_header_accout.setText(mUser.getUsername());
        }

        //给toolbar菜单按钮设置监听事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.toolbar_menu_search:
                        Toast.makeText(getApplicationContext(), "查找按钮", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        List<String> tabList = new ArrayList<>();
        tabList.add("所有人");
        tabList.add("分组");
        //创建两个标签
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(1)));
        //初始化ViewPager控件，用于填充好友概述内容


        mFragmentList = new ArrayList<>();
        //所有联系人的fragment
        Fragment allContactFrgm = new AllContactFragment();
        //分组查看联系人的fragment
        Fragment groupFrgm=new GroupFragment();
        mFragmentList.add(allContactFrgm);
        mFragmentList.add(groupFrgm);
        TabFragmentAdapter mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragmentList, tabList);

        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);



        //----------------左侧滑栏动画效果------------------------------------
        //初始化DrawerLayout控件
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        //给导航键添加动画效果
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //------------左侧滑栏的内容NavigationView----------
        //初始化NavigationView控件
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        //设置菜单图标恢复本来的颜色
        mNavigationView.setItemIconTintList(null);
        ////设置菜单栏的监听事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId ) {
                    case R.id.item_myaccout:
                        startActivity(new Intent(MainActivity.this,MyAccountActivity.class));
                        break;
                    case  R.id.subitem_01:
                        //日历
                        startActivity(new Intent(MainActivity.this,CalendarActivity.class));
                        break;
                    case  R.id.subitem_02:
                        //设置
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        break;
                    case R.id.subitem_sync:

                        SyncUtils syncUtils=new SyncUtils(MainActivity.this);
                        syncUtils.syncPersonInfo();
                        Group.sync(MainActivity.this);
                        new Remind().sync(MainActivity.this);
                        Toast.makeText(MainActivity.this, "开始同步", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.subitem_03:
                        //关于我
                        startActivity(new Intent(MainActivity.this,AboutMeActivity.class));
                        break;
                    case R.id.menu_note:
                        //便签
                        startActivity(new Intent(MainActivity.this,NoteActivity.class));
                        break;
                    case R.id.menu_group:
                        //群组
                        startActivity(new Intent(MainActivity.this,GroupActivity.class));
                        break;
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //浮动按钮
        mFaMenu.setClosedOnTouchOutside(true);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,CreateFriendActivity.class),CREATE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("mainact",requestCode+"----requestCode------");
        Log.i("mainact",resultCode+"-----resultCode-----");
        AllContactFragment allConfragment = (AllContactFragment) mFragmentList.get(0);
        GroupFragment groupFragment = (GroupFragment) mFragmentList.get(1);
        ptDao = new PersonInfoDao(this);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case CREATE_REQUEST_CODE:
                    String mName = data.getStringExtra("name");
                    PersonInfo personInfo = ptDao.queryPerson(mName);
                    allConfragment.setDataSet(personInfo,CREATE_REQUEST_CODE);
                    groupFragment.setDataSet(personInfo,CREATE_REQUEST_CODE);
                    break;
            }
        }
        else{
            switch (resultCode){
                case FriendDetailActivity.UPDATE_PERSON:
                    String updatPersonName = data.getStringExtra("name");
                    PersonInfo personInfo = ptDao.queryPerson(updatPersonName);
                    allConfragment.setDataSet(personInfo,FriendDetailActivity.UPDATE_PERSON);
                    groupFragment.setDataSet(personInfo,FriendDetailActivity.UPDATE_PERSON);
                    Log.i("mainact","UPDATE_PERSON");
                    break;
                case FriendDetailActivity.DELETE_PERSON:
                    allConfragment.setDataSet(null,FriendDetailActivity.DELETE_PERSON);
                    groupFragment.setDataSet(null,FriendDetailActivity.DELETE_PERSON);
                    Log.i("mainact","DELETE_PERSON");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!backFlag){//第一次点击
            Toast.makeText(MainActivity.this, "再按一次返回退出", Toast.LENGTH_SHORT).show();
            firstTime = System.currentTimeMillis();
            backFlag=true;
        }else{
            //第二次点击
            lastTime = System.currentTimeMillis();
            long gapTime=lastTime-firstTime;
            Log.i("onback","gapTime"+gapTime);
            if (gapTime<2000){
                finish();
            }else{
                //防止时间过长再次点击没反应
                Toast.makeText(MainActivity.this, "再按一次返回退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();

            }
            //重置
//            backFlag=false;
        }

    }
}
