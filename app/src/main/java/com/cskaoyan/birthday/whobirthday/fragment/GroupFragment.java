package com.cskaoyan.birthday.whobirthday.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.cskaoyan.birthday.whobirthday.Dao.GroupDao;
import com.cskaoyan.birthday.whobirthday.Dao.PersonInfoDao;
import com.cskaoyan.birthday.whobirthday.R;
import com.cskaoyan.birthday.whobirthday.activity.FriendDetailActivity;
import com.cskaoyan.birthday.whobirthday.activity.MainActivity;
import com.cskaoyan.birthday.whobirthday.adapter.ExpGroupAdapter;
import com.cskaoyan.birthday.whobirthday.bean.GroupBean;
import com.cskaoyan.birthday.whobirthday.bean.PersonInfo;
import com.cskaoyan.birthday.whobirthday.utils.SortBirthday;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * create by lamchaohao on 2016/4/23
 */
public class GroupFragment extends Fragment {

    public static final int DETAIL_REQUEST = 105;
    private ExpGroupAdapter mExpGroupAdapter;
    private List<GroupBean> groupBeanList;
    private List<GroupBean> tempGBList;
    private int mGroupPosition;
    private int mChildPosition;
    private List<String> mGroups;
    private PersonInfoDao ptDao;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group, container, false);

        GroupDao gd=new GroupDao(getActivity());
        ptDao = new PersonInfoDao(getActivity());
        mGroups = gd.getGroups();
        tempGBList = new ArrayList<>();
        groupBeanList = new ArrayList<>();
        for (int i = 0; i< mGroups.size(); i++){
            //根据组名，获取当组的所有组员
            List<BmobObject> groupMember = ptDao.getPeopleByGroup(mGroups.get(i));
            //先排序
            List<BmobObject> sortChildList = SortBirthday.sortLeftDay(groupMember);
            //添加一组,包括组名,组员
            tempGBList.add(new GroupBean(mGroups.get(i),sortChildList));
            groupBeanList.add(new GroupBean(mGroups.get(i),sortChildList));
        }
        ExpandableListView expLv_group = (ExpandableListView) view.findViewById(R.id.expLv_group);
        mExpGroupAdapter = new ExpGroupAdapter(getActivity(), groupBeanList);
        expLv_group.setAdapter(mExpGroupAdapter);

        expLv_group.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mGroupPosition=groupPosition;
                mChildPosition=childPosition;
                Intent intent=new Intent(getActivity(), FriendDetailActivity.class);

                PersonInfo person = (PersonInfo) groupBeanList.get(groupPosition).getChildList().get(childPosition);
                intent.putExtra("name",person.getName());
                intent.putExtra("birthday",person.getBirth());
                intent.putExtra("phone",person.getPhone());
                intent.putExtra("gender",person.getGender());
                intent.putExtra("headpic",person.getImage());
                intent.putExtra("group",person.getAtGroup());
                startActivityForResult(intent,DETAIL_REQUEST);
                return true;
            }
        });

        return view;
    }

    /**
     * 刷新数据源
     */
    private void refresh(){
        groupBeanList.clear();
        for (int i = 0; i< mGroups.size(); i++){
            //根据组名，获取当组的所有组员
            List<BmobObject> groupMember = ptDao.getPeopleByGroup(mGroups.get(i));
            //先排序
            List<BmobObject> sortChildList = SortBirthday.sortLeftDay(groupMember);
            //添加一组,包括组名,组员
            groupBeanList.add(new GroupBean(mGroups.get(i),sortChildList));
        }
        mExpGroupAdapter.notifyDataSetChanged();
    }

    public void setDataSet(PersonInfo person,int flag) {
        switch (flag) {
            case MainActivity.CREATE_REQUEST_CODE://增加了联系人
                refresh();
                break;
            case FriendDetailActivity.UPDATE_PERSON://修改了联系人
                //因为可能是在AllContactFragment进来的...所以mGroupPosition，mChildPosition有可能未初始化...
                refresh();
                break;
            case FriendDetailActivity.DELETE_PERSON://删除了某位联系人
                refresh();
                break;
        }
    }

    }
